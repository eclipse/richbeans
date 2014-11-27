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

import org.eclipse.dawnsci.analysis.api.metadata.MetadataType;

public class SliceFromSeriesMetadata implements MetadataType {
	
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
}
