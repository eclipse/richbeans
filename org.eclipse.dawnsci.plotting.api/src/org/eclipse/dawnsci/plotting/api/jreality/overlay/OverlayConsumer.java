/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.plotting.api.jreality.overlay;

/**
 *
 */
public interface OverlayConsumer {

	/**
	 * Register an overlay provider to the consumer
	 * @param provider the provider to be registered
	 */
	public void registerProvider(OverlayProvider provider);
	
	/**
	 * Unregisters an overlay provider to the consumer
	 */
	public void unregisterProvider();

	/**
	 * Get rid of all primitives held by consumer
	 */
	public void removePrimitives();
}
