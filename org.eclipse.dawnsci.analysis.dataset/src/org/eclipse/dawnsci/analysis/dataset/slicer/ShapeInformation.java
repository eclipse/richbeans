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

package org.eclipse.dawnsci.analysis.dataset.slicer;

import org.apache.commons.lang.ArrayUtils;

public class ShapeInformation {

	private int[] subSampledShape;
	private int[] dataDimensions;
	private int totalSlices;

	public ShapeInformation(int[] subSampledShape, int[] dataDimensions, int totalSlices) {
		this.subSampledShape = subSampledShape;
		this.dataDimensions = dataDimensions;
		this.totalSlices = totalSlices;
	}
	
	public int[] getSubSampledShape() {
		return subSampledShape;
	}

	public int getTotalSlices() {
		return totalSlices;
	}

	public int[] getDataDimensions() {
		return dataDimensions;
	}
	
	@Override
	public ShapeInformation clone() {
		return new ShapeInformation(subSampledShape.clone(),dataDimensions.clone(), totalSlices);
	}
	
	public boolean isDataDimension(int dim) {
		return ArrayUtils.contains(dataDimensions, dim);
	}
}
