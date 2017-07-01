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

import java.io.File;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

/**
 * The actual import task.  Performs the "bulk" of the work to import our
 * network.
 * @author Cornelia Schultz
 */
public class DGMLImporterTask extends AbstractTask {
    
    @Tunable(description="DGML File", params="fileCategory=unspecified;input=true")
    public File dgmlFile = null;
    
    @Tunable(description="Include Junk Data")
    public boolean includeJunkData = false;

    @Override
    public void run(TaskMonitor tm) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
