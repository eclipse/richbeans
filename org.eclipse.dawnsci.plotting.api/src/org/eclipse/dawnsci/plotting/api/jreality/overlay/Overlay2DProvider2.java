/*
 * Copyright 2012 Diamond Light Source Ltd.
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

package org.eclipse.dawnsci.plotting.api.jreality.overlay;

import org.eclipse.dawnsci.plotting.api.jreality.overlay.objects.OverlayObject;
import org.eclipse.dawnsci.plotting.api.jreality.overlay.primitives.PrimitiveType;

/**
 *
 */
public interface Overlay2DProvider2 extends Overlay2DProvider {

	/**
	 * Register a PrimitiveType but get as return an OverlayObject
	 * instead of just an integer handle 
	 * @param primType which primitive type to be registed
	 * @return OverlayObject or null if failed
	 */	
	
	public OverlayObject registerObject(PrimitiveType primType);	
	
	public OverlayImage registerOverlayImage(int width, int height);
	
}
