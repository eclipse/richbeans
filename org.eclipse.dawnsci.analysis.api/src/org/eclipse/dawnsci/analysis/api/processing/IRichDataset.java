/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.api.processing;

import java.util.Map;

import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;

/**
 * This class is a lazy dataset with the information required to slice it.
 */
public interface IRichDataset {

	/**
	 * 
	 * @return The data which we are slicing
	 */
	public ILazyDataset getData();

	/**
	 * 
	 * @return A map of data dimension to String representing the slice done on this dimension. 
	 */
	public Map<Integer, String> getSlicing();

}