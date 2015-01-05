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

import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.api.metadata.OriginMetadata;

public class SliceFromSeriesMetadata implements OriginMetadata {
	
	private SourceInformation sourceInfo;
	private SliceInformation sliceInfo;
	
	public SliceFromSeriesMetadata(SourceInformation source, SliceInformation slice) {
		this.sourceInfo = source;
		this.sliceInfo = slice;
	}
	
	public SliceFromSeriesMetadata(SliceInformation slice) {
		this.sliceInfo = slice;
	}
	
	public SliceFromSeriesMetadata(SourceInformation source) {
		this.sourceInfo = source;
	}

	public SourceInformation getSourceInfo() {
		return sourceInfo;
	}

	public SliceInformation getSliceInfo() {
		return sliceInfo;
	}
	
	@Override
	public SliceFromSeriesMetadata clone() {
		SourceInformation soi = sourceInfo != null ? sourceInfo.clone() : null;
		SliceInformation sli = sliceInfo != null ? sliceInfo.clone() : null;
		
		return new SliceFromSeriesMetadata(soi, sli);
		}

	@Override
	public int[] getDataDimensions() {
		return sliceInfo != null ? sliceInfo.getDataDimensions()  : null;
	}

	@Override
	public ILazyDataset getParent() {
		return sourceInfo != null ? sourceInfo.getParent()  : null;
	}

	@Override
	public Slice[] getSliceInOutput() {
		return sliceInfo != null ? sliceInfo.getSliceInOutput()  : null;
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
	public Slice[] getSliceFromInput() {
		return sliceInfo != null ? sliceInfo.getSliceFromInput()  : null;
	}
	
	public int[] getSubSampledShape() {
		return sliceInfo != null ? sliceInfo.getSubSampledShape() : null;
	}
	
	public int getTotalSlices() {
		return sliceInfo != null ? sliceInfo.getTotalSlices() : -1;
	}
	
	public boolean isDataDimension(int dim) {
		return sliceInfo != null ? sliceInfo.isDataDimension(dim) : false;
	}
	
	public void reducedDimensionToSingular(int dim) {
		if (sliceInfo.isDataDimension(dim)) throw new IllegalArgumentException("Cannot reduce data dimension!");
		sliceInfo.reducedDimensionToSingular(dim);
		
	}
}
