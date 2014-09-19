/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.dawnsci.hdf5;

import ncsa.hdf.object.FileFormat;

/**
 * Internal use only.
 * 
 * @author fcp94556
 *
 */
public interface IFileFormatDataFile extends IHierarchicalDataFile {

	public FileFormat getFileFormat();

}
