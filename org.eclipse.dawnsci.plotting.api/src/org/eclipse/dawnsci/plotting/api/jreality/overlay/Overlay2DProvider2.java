/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
