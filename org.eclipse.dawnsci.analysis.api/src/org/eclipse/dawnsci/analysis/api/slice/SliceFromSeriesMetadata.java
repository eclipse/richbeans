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

package org.eclipse.dawnsci.analysis.api.slice;

import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.api.metadata.OriginMetadata;

public class SliceFromSeriesMetadata implements OriginMetadata {
	
	private SourceInformation sourceInfo;
	private ShapeInformation shapeInfo;
	private SliceInformation sliceInfo;
	
	public SliceFromSeriesMetadata(SourceInformation source, ShapeInformation shape, SliceInformation slice) {
		this.sourceInfo = source;
		this.shapeInfo = shape;
		this.sliceInfo = slice;
	}
	
	public SliceFromSeriesMetadata(ShapeInformation shape, SliceInformation slice) {
		this.shapeInfo = shape;
		this.sliceInfo = slice;
	}
	
	public SliceFromSeriesMetadata(SourceInformation source) {
		this.sourceInfo = source;
	}

	public SourceInformation getSourceInfo() {
		return sourceInfo;
	}

	public ShapeInformation getShapeInfo() {
		return shapeInfo;
	}

	public SliceInformation getSliceInfo() {
		return sliceInfo;
	}
	
	@Override
	public SliceFromSeriesMetadata clone() {
		ShapeInformation shi = shapeInfo != null ? shapeInfo.clone() : null;
		SourceInformation soi = sourceInfo != null ? sourceInfo.clone() : null;
		SliceInformation sli = sliceInfo != null ? sliceInfo.clone() : null;
		
		return new SliceFromSeriesMetadata(soi, shi, sli);
		}

	@Override
	public int[] getDataDimensions() {
		return shapeInfo != null ? shapeInfo.getDataDimensions()  : null;
	}

	@Override
	public ILazyDataset getParent() {
		return sourceInfo != null ? sourceInfo.getParent()  : null;
	}

	@Override
	public Slice[] getInitialSlice() {
		return sliceInfo != null ? sliceInfo.getViewSlice()  : null;
	}

	@Override
	public String getDatasetName() {
		return sourceInfo != null ? sourceInfo.getDatasetName()  : null;
	}

	@Override
	public String getFilePath() {
		return sourceInfo != null ? sourceInfo.getFilePath()  : null;
	}

	@Override
	public Slice[] getCurrentSlice() {
		return sliceInfo != null ? sliceInfo.getCurrentSlice()  : null;
	}
}
