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

package org.eclipse.richbeans.widgets;

import org.eclipse.richbeans.api.event.ValueListener;

/**
 * Interface used to override bounds settings.
 * Used when input devices are bound to each other in value.
 * 
 * @author Matthew Gerring
 *
 */
public interface BoundsProvider {
	/**
	 * The bound value
	 * @return double
	 */
    public double getBoundValue();
    
    /**
     * The acceptor of the BoundsProvider can also listen
     * to value changes from the BoundsProvider and update
     * it's bounds as required.
     * @param l
     */
    public void addValueListener(final ValueListener l);
}

	