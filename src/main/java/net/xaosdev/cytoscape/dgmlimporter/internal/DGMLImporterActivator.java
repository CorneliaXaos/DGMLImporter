package net.xaosdev.cytoscape.dgmlimporter.internal;

import java.util.Properties;
import net.xaosdev.cytoscape.dgmlimporter.internal.importer.DGMLImporterTaskFactory;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import static org.cytoscape.work.ServiceProperties.PREFERRED_MENU;
import static org.cytoscape.work.ServiceProperties.TITLE;
import org.cytoscape.work.TaskFactory;
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
        
        // Setup our task
        Properties importerProps = new Properties();
        importerProps.put(TITLE, "DGML Importer");
        importerProps.put(PREFERRED_MENU, "Apps");
        
        DGMLImporterTaskFactory importerTaskFactory =
                new DGMLImporterTaskFactory();
        registerService(context, importerTaskFactory,
                TaskFactory.class, importerProps);
    }

}
