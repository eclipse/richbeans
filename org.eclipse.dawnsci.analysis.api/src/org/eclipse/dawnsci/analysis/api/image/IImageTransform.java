/*-
 * Copyright 2014 Diamond Light Source Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.dawnsci.analysis.api.image;

import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;

/**
 * This service can be called to process IDataset using BoofCV transform algorithms
 * 
 * @author wqk87977
 *
 */
public interface IImageTransform {

	/**
	 * Rotates a 2d dataset by n degrees
	 * 
	 * @param data
	 *            dataset to be rotated
	 * @param angle
	 *            in degrees
	 * @return rotated image
	 */
	public IDataset rotate(IDataset data, double angle) throws Exception;

	/**
	 * Rotates a 2d dataset by n degrees
	 * 
	 * @param data
	 *            dataset to be rotated
	 * @param angle
	 *            in degrees
	 * @param keepShape
	 *            if true, the resulting image will have the same shape as the original
	 * @return rotated image
	 * @throws Exception
	 */
	public IDataset rotate(IDataset data, double angle, boolean keepShape) throws Exception;

	/**
	 * Aligns a stack of images using feature association
	 * 
	 * @param images
	 *            stack of images
	 * @return aligned stack of images
	 * @throws Exception
	 */
	public List<IDataset> align(List<IDataset> images) throws Exception;
}
