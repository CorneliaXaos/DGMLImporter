/*
 * Copyright (C) 2017 Cornelia Schultz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.xaosdev.cytoscape.dgmlimporter.internal.importer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.xaosdev.cytoscape.dgmlimporter.internal.Global;
import org.cytoscape.io.read.CyNetworkReader;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The actual import task.  Performs the "bulk" of the work to import our
 * network.
 * @author Cornelia Schultz
 */
public class DGMLImporterTask implements CyNetworkReader {
    
    /** The InputStream to read. */
    private final InputStream in;
    /** The name of the file. */
    private final String fileName;
    
    /** Tracking variable to detect user cancellation. */
    private boolean cancelled = false;
    /** The DGML Document. */
    private Document document;
    /** The generated network. */
    private CyNetwork network;
    /** The matching NodeTable for the network. */
    private CyTable nodeTable;
    /** The matching EdgeTable for the network. */
    private CyTable edgeTable;
    
    /** A map used to simplify looking up nodes. */
    private HashMap<String, CyNode> nodeMap = new HashMap<>();
    
    /**
     * Constructs the importer.
     * @param in the InputStream to the file.
     * @param fileName the name of the file;
     */
    public DGMLImporterTask(InputStream in, String fileName) {
        this.in = in;
        this.fileName = fileName;
    }

    @Override
    public void run(TaskMonitor tm) throws Exception {
        
        // Create Network
        network = Global.getNetworkFactoryService().createNetwork();
        nodeTable = network.getDefaultNodeTable();
        nodeTable.createColumn("Id", String.class, false);
        nodeTable.createColumn("Category", String.class, false);
        nodeTable.createColumn("IsPublic", Boolean.class, false);
        nodeTable.createColumn("Label", String.class, false);
        edgeTable = network.getDefaultEdgeTable();
        edgeTable.createListColumn("Categories", String.class, false);
        edgeTable.createColumn("IsSourceVirtualized", Boolean.class, false);
        edgeTable.createColumn("IsTargetVirtualized", Boolean.class, false);
        edgeTable.createColumn("Weight", Double.class, false);
        if (cancelled) return;
        
        // Read in our DGML Document
        readXMLDoc(tm);
        if (document == null) {
            tm.showMessage(TaskMonitor.Level.INFO, "Operation failed. Aborting..");
            return;
        }
        if (cancelled) return;
        
        // Process Nodes
        processNodes(tm);
        if (cancelled) return;
        
        // Process Edges
        processLinks(tm);
    }

    @Override
    public CyNetwork[] getNetworks() {
        return new CyNetwork[] {network};
    }

    @Override
    public CyNetworkView buildCyNetworkView(CyNetwork cn) {
        return Global.getNetworkViewFactoryService().createNetworkView(network);
    }

    @Override
    public void cancel() {
        cancelled = true;
    }
    
    /**
     * Reads in the DGML file that was provided.
     * @param tm the TaskMonitor used to report messages
     * @return a Document containing the DGML or null if a failure occurred.
     */
    private void readXMLDoc(TaskMonitor tm) {
        tm.showMessage(TaskMonitor.Level.INFO, "Importing DGML...");
        tm.setProgress(-1);
        
        try {    
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(in);
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            tm.showMessage(TaskMonitor.Level.ERROR, "Failed to import DGML File.");
        }
    }
    
    /**
     * Converts t
     * @param tm
     * @param document 
     */
    private void processNodes(TaskMonitor tm) {
        tm.showMessage(TaskMonitor.Level.INFO, "Processing Nodes...");
        tm.setProgress(0);
        
        NodeList list = document.getElementsByTagName("Node");
        for (int index = 0; index < list.getLength(); index++) {
            // Read Poperties from Node
            Node node = list.item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                
                // Acquire attributes:
                String id = element.getAttribute("Id");
                String category = element.getAttribute("Category");
                Boolean isPublic = Boolean.parseBoolean(
                        element.getAttribute("CodeSchemaProperty_IsPublic"));
                String label = element.getAttribute("Label");
                
                // Create a CyNode, set attributes, and add to CyNetwork
                CyNode cyNode = network.addNode();
                nodeTable.getRow(cyNode.getSUID()).set("Id", id);
                nodeTable.getRow(cyNode.getSUID()).set("Category", category);
                nodeTable.getRow(cyNode.getSUID()).set("IsPublic", isPublic);
                nodeTable.getRow(cyNode.getSUID()).set("Lable", label);
                
                // Add node to lookup map to make things easier below:
                nodeMap.put(id, cyNode);
            }
            
            // Update Progress
            tm.setProgress(1.0 * (index + 1) / list.getLength());
            if (cancelled) return; // abort
        }
    }
    
    private void processLinks(TaskMonitor tm) {
        tm.showMessage(TaskMonitor.Level.INFO, "Processing Links...");
        tm.setProgress(0);
        
        NodeList list = document.getElementsByTagName("Link");
        for (int index = 0; index < list.getLength(); index++) {
            Node node = list.item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                
                // First, create our edge.  If we fail, continue:
                CyNode source = nodeMap.get(element.getAttribute("Source"));
                CyNode target = nodeMap.get(element.getAttribute("Target"));
                if (source == null || target == null) {
                    tm.showMessage(TaskMonitor.Level.WARN,
                            "Bad Source / Target for link: " + index);
                    continue;
                }
                CyEdge edge = network.addEdge(source, target, true);
                
                // Now, extract Properties
                List<String> categories = new ArrayList<>();
                categories.add(element.getAttribute("Category"));
                NodeList subList = element.getElementsByTagName("Category");
                for (int jIndex = 0; jIndex < subList.getLength(); jIndex++) {
                    Node subNode = subList.item(jIndex);
                    
                    if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element subElement = (Element) subNode;
                        categories.add(element.getAttribute("Ref"));
                    }
                }
                Boolean isSourceVirtualized = Boolean.parseBoolean(
                        element.getAttribute("IsSourceVirtualized"));
                Boolean isTargetVirtualized = Boolean.parseBoolean(
                        element.getAttribute("IsTargetVirtualized"));
                Double weight = Double.parseDouble(
                        element.getAttribute("Weight"));
                
                // Finally, apply them to the EdgeTable
                edgeTable.getRow(edge.getSUID()).set("Categories", categories);
                edgeTable.getRow(edge.getSUID()).set("IsSoruceVirtualized",
                        isSourceVirtualized);
                edgeTable.getRow(edge.getSUID()).set("IsTargetVirtualized",
                        isTargetVirtualized);
                edgeTable.getRow(edge.getSUID()).set("Weight", weight);
            }
            
            // Update Progress
            tm.setProgress(1.0 * (index + 1) / list.getLength());
            if (cancelled) return; // abort
        }
    }
}
