/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.dawnsci.hdf5.model.internal;

import org.eclipse.dawnsci.hdf5.IHierarchicalDataFile;
import org.eclipse.dawnsci.hdf5.model.IHierarchicalDataModel;

public interface IHierarchicalDataFileGetReader {
	/**
	 * Obtains the IHierarchicalDataFile that should be used by {@link IHierarchicalDataModel}
	 * @return a IHierarchicalDataFile
	 * @throws Exception if there was any problem obtaining the reader
	 */
	IHierarchicalDataFile getReader() throws Exception;
}
