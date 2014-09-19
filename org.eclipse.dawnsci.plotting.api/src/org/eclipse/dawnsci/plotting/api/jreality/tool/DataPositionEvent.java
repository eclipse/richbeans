/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.plotting.api.jreality.tool;

import java.util.EventObject;

/**
 *
 */
public class DataPositionEvent extends EventObject implements IDataPositionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected double position[];
	protected Mode currentMode;
	
	/**
	 * Constructor of a DataPositionEvent 
	 * @param instigater Who is creating this event object?
	 * @param position position coordinates in dataSet space
	 * @param mode current mode (start, drag, end)
	 */
	public DataPositionEvent(Object instigater,
            				 double[] position, 
            				 Mode mode) {
		super(instigater);
		this.position = position.clone();
		this.currentMode = mode;
	}

	/**
	 * Get the current mode
	 * 
	 * @return current mode {@link IDataPositionEvent.Mode}
	 */
	@Override
	public Mode getMode()
	{
		return currentMode;
	}
	
	/**
	 * Get the position in texture coordinates
	 * @return texture coordinates
	 */
	@Override
	public double[] getPosition()
	{
		return position;
	}	
}
