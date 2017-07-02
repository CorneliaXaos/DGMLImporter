package net.xaosdev.cytoscape.dgmlimporter.internal;

import java.util.HashSet;
import java.util.Properties;
import net.xaosdev.cytoscape.dgmlimporter.internal.importer.DGMLImporterTaskFactory;
import org.cytoscape.io.BasicCyFileFilter;
import org.cytoscape.io.DataCategory;
import org.cytoscape.io.read.InputStreamTaskFactory;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.view.model.CyNetworkViewFactory;
import static org.cytoscape.work.ServiceProperties.TITLE;
import org.osgi.framework.BundleContext;

/**
 * Activates the Cytoscape Plugin
 * 
 * @author Cornelia Schultz
 */
public class DGMLImporterActivator extends AbstractCyActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        // Services
        StreamUtil streamUtil = getService(context, StreamUtil.class);
        Global.networkFactoryService = getService(context, CyNetworkFactory.class);
        Global.networkViewFactoryService = getService(context, CyNetworkViewFactory.class);
        Global.networkManagerService = getService(context, CyNetworkManager.class);
        
        // Create the File Filter
        HashSet<String> extensions = new HashSet<>();
        extensions.add("dgml");
        HashSet<String> contentTypes = new HashSet<>();
        contentTypes.add("txt");
        String description = "Visual Studio Code Maps";
        DataCategory category = DataCategory.NETWORK;
        BasicCyFileFilter filter = new BasicCyFileFilter(extensions,
                contentTypes, description, category, streamUtil);
        
        // Create DGMLReaderTaskFactory
        DGMLImporterTaskFactory factory = new DGMLImporterTaskFactory(filter);
        
        // Register with Cytoscape
        Properties props = new Properties();
        props.setProperty(TITLE, "DGML Importer");
        props.setProperty("readerDescription", "DGML Importer");
        props.setProperty("readerId", "dgmlNetworkReader");
        registerService(context, factory, InputStreamTaskFactory.class, props);
    }

}
