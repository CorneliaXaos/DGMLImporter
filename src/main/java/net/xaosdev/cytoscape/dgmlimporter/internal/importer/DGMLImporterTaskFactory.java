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

import java.io.InputStream;
import org.cytoscape.io.CyFileFilter;
import org.cytoscape.io.read.AbstractInputStreamTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * Constructs a DGMLImporterTask.
 * @author Cornelia Schultz
 */
public class DGMLImporterTaskFactory extends AbstractInputStreamTaskFactory {

    public DGMLImporterTaskFactory(CyFileFilter fileFilter) {
        super(fileFilter);
    }

    @Override
    public TaskIterator createTaskIterator(InputStream in, String string) {
        return new TaskIterator(new DGMLImporterTask(in, string));
    }
    
}
