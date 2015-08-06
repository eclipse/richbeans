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
package org.eclipse.richbeans.widgets.decorator;

import java.util.EventListener;

public interface IValueChangeListener extends EventListener{

	/**
	 * This listener is very trickey. Be warned the value that the box
	 * is currently taking is returned by evt.getValue(), if you ask the 
	 * box for the value or do a ((BoundsDecorator)evt.getSource()).getValue()
	 * it will be the previous value!
	 * @param evt
	 */
	void valueValidating(ValueChangeEvent evt);
}
