/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.dawnsci.plotting.api.axis;

import java.util.EventListener;

/**
 * Interface listener for reporting data position 
 * when the mouse moves over the graph.
 * 
 * @author Matthew Gerring
 *
 */
public interface IPositionListener extends EventListener{

	/**
	 * Notifies as the mouse is dragged over the plot.
	 * Please do not do heavy work in this callback!
	 * 
	 * @param evt
	 */
	void positionChanged(PositionEvent evt);
}
