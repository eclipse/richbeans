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

import java.util.EventObject;

public class ValueChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8983136524290779712L;
	private Number value;

	public ValueChangeEvent(Object source, Number value) {
		super(source);
		this.value = value;
	}

	public Number getValue() {
		return value;
	}
	public void setValue(Number value) {
		this.value = value;
	}


}
