/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.dawnsci.plotting.api.views;

import java.io.Serializable;

/**
 * Interface used to mark a view part as being able to accept data.
 * @author Matthew Gerring
 *
 */
public interface ISettablePlotView {

	/**
	 * Call this method to send data to the PlotView.
	 * 
	 * For instance if a peak fitting tool would like to 
	 * send the peaks back to the GuiBean to be sent to 
	 * the server, it may call setPlottedData(...) with a 
	 * list of IPeak objects to define the peaks.
	 * 
	 * @param data
	 * @param clazz
	 */
	public void updateData(Serializable data, Class<?> clazz);

	
}
