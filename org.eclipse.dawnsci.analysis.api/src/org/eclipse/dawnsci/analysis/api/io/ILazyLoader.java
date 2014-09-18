/*-
 * Copyright (c) 2013 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.api.io;

import java.io.Serializable;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.monitor.IMonitor;

public interface ILazyLoader extends Serializable {

	/**
	 * 
	 * @return true if file is readable
	 */
	public boolean isFileReadable();

	/**
	 * @param mon
	 * @param shape
	 * @param start
	 * @param stop
	 * @param step
	 * @return a slice of a dataset
	 * @throws Exception
	 */
	public IDataset getDataset(IMonitor mon, int[] shape, int[] start, int[] stop, int[] step) throws Exception;
}
