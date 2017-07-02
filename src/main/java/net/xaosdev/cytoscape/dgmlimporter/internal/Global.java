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
package net.xaosdev.cytoscape.dgmlimporter.internal;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.model.CyNetworkViewFactory;

/**
 * Contains Global Constants used by various portions of the bundle.
 *
 * This doesn't eliminate reference passing, but it makes our lives easier, so..
 * @author Cornelia Schultz
 */
public class Global {
    
    /**
     * Package member used for accessing Cytoscape functionality in other
     * classes.  Used to access the CyNetworkFactory service.
     */
    static CyNetworkFactory networkFactoryService = null;

    /**
     * Package member used for accessing Cytoscape functionality in other 
     * classes.  Used to access the CyNewtorkManager service.
     */
    static CyNetworkManager networkManagerService = null;

    /**
     * Package member used for accessing Cytoscape functionality in other 
     * classes.  Used to access the CyNewtorkViewFactory service.
     */
    static CyNetworkViewFactory networkViewFactoryService = null;
    
    /**
     * Gets the Cytoscape NetworkFactory service.
     * 
     * @return the CyNetworkFactory object of the current Cytoscape instance.
     */
    public static CyNetworkFactory getNetworkFactoryService() {
        return networkFactoryService;
    }
    
    /**
     * Gets the Cytoscape NetworkManager service.
     * 
     * @return the CyNetworkManager object of the current Cytoscape instance.
     */
    public static CyNetworkManager getNetworkManagerService() {
        return networkManagerService;
    }
    
    /**
     * Gets the Cytoscape NetworkViewFactory service.
     * 
     * @return the CyNetworkViewFactory object of the current Cytoscape instance.
     */
    public static CyNetworkViewFactory getNetworkViewFactoryService() {
        return networkViewFactoryService;
    }
}
