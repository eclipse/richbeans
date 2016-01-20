/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.richbeans.api.event;

import java.util.EventListener;

/**
 * Listener interface for being notified of value changes
 * @author Matthew Gerring
 *
 */
public interface BoundsListener extends EventListener {

	/**
	 * Called when value is greater than bounds
	 * @param e
	 */
	public void valueGreater(BoundsEvent e);
	
	/**
	 * Called when value is less than bounds
	 * @param e
	 */
	public void valueLess(BoundsEvent e);
	
	/**
	 * Called when value is in bounds
	 * @param e
	 */
	public void valueLegal(BoundsEvent e);

}

	