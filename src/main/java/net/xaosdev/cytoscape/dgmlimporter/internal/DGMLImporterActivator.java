package net.xaosdev.cytoscape.dgmlimporter.internal;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.work.TaskManager;
import org.osgi.framework.BundleContext;

/**
 * Activates the Cytoscape Plugin
 * 
 * @author Cornelia Schultz
 */
public class DGMLImporterActivator extends AbstractCyActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        Global.desktopService = getService(context, CySwingApplication.class);
        Global.taskManagerService = getService(context, TaskManager.class);
        Global.rootNetworkService = getService(context, CyRootNetworkManager.class);
        Global.networkManagerService = getService(context, CyNetworkManager.class);
    }

}
