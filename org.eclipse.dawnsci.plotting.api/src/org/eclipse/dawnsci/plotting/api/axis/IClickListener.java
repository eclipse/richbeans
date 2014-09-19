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

public interface IClickListener extends EventListener {

	/**
	 * Called when a click happens in the plotting.
	 * 
	 * @param evt
	 */
	public void clickPerformed(ClickEvent evt);
	
	/**
	 * Called when a click happens in the plotting.
	 * (May also call clickPerformed(...) )
	 * 
	 * @param evt
	 */
	public void doubleClickPerformed(ClickEvent evt);

}
