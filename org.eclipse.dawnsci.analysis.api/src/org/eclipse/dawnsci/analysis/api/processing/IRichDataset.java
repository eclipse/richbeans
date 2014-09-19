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
 * This dataset holds several bits of information about scientific data.
 * 
 * The class contains methods for getting/setting the data components and
 * operating on the rich dataset.
 */
public interface IRichDataset {

	// TODO much of what rich dataset performed is now handled by the metadatatype classes,
	// should probably remove it
	
	// Boiler plate getters and setters for data
	public ILazyDataset getData();

	public Map<Integer, String> getSlicing();

}