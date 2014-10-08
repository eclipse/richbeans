/*-
 * Copyright (c) 2014 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.dawnsci.analysis.api.image;

import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;

/**
 * This service can be called to process IDataset using BoofCV stitching algorithms
 * 
 * @author wqk87977
 *
 */
public interface IImageStitchingProcess {

	/**
	 * Stitches a list of images with a default matrix size and angle
	 * 
	 * @param input
	 * @return output stitched image
	 */
	public IDataset stitch(List<IDataset> input);

	/**
	 * Stitches a list of images
	 * 
	 * @param input
	 * @param rows
	 * @param columns
	 * @param angle
	 *            rotation in degree
	 * @return output stitched image
	 */
	public IDataset stitch(List<IDataset> input, int rows, int columns, double angle);

}
