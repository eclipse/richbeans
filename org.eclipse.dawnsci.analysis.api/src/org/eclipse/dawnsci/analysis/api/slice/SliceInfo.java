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

import java.util.Arrays;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;

public class SliceInfo {

	private String       sliceName;
	private Slice[]      slice;
	private ILazyDataset data;
	
	public SliceInfo() {
	   this(null, null, null);	
	}
	public SliceInfo(String sliceName, Slice[] slice, ILazyDataset data) {
		super();
		this.sliceName = sliceName;
		this.slice = slice;
		this.data = data;
	}
	
	public IDataset slice() {
		IDataset s = data.getSlice(slice); // TODO Should this be getSliceView(...) ?
		s.setName(sliceName);
		return s;
	}

	public String getSliceName() {
		return sliceName;
	}
	public void setSliceName(String sliceName) {
		this.sliceName = sliceName;
	}
	public Slice[] getSlice() {
		return slice;
	}
	public void setSlice(Slice[] slice) {
		this.slice = slice;
	}
	public ILazyDataset getData() {
		return data;
	}
	public void setData(ILazyDataset data) {
		this.data = data;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + Arrays.hashCode(slice);
		result = prime * result + ((sliceName == null) ? 0 : sliceName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SliceInfo other = (SliceInfo) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (!Arrays.equals(slice, other.slice))
			return false;
		if (sliceName == null) {
			if (other.sliceName != null)
				return false;
		} else if (!sliceName.equals(other.sliceName))
			return false;
		return true;
	}
}
