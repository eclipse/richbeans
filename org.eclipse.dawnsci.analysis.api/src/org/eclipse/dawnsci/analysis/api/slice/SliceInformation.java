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

import org.eclipse.dawnsci.analysis.api.dataset.Slice;

public class SliceInformation {

	private Slice[] currentSlice;
	private int number;
	private Slice[] viewSlice;
	
	public SliceInformation(Slice[] current, Slice[] view, int number) {
		this.currentSlice = current;
		this.number = number;
		this.viewSlice = view;
	}

	public Slice[] getCurrentSlice() {
		return currentSlice;
	}
	
	public Slice[] getViewSlice() {
		return viewSlice;
	}

	public int getSliceNumber() {
		return number;
	}
	
	@Override
	public SliceInformation clone() {
		return new SliceInformation(currentSlice.clone(),viewSlice.clone(), number);
	}
}
