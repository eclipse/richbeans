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

import org.eclipse.swt.widgets.Text;

/**
 * An object to make any Text field into a field widget.
 * 
 * @author fcp94556
 *
 */
public class TextFieldDecorator extends FieldDecorator<Text> {
	
	private BoundsDecorator boundsDecorator;
	private Object          value;

	public TextFieldDecorator(Text connection, BoundsDecorator boundsDecorator) {
		super(connection);
		this.boundsDecorator = boundsDecorator;
		boundsDecorator.addValueChangeListener(new IValueChangeListener() {		
			@Override
			public void valueValidating(ValueChangeEvent evt) {
				value= evt.getValue();
				fireValueListeners(evt.getValue());
			}
		});
	}

	@Override
	public Object getValue() {
		if (value!=null) return value;
		return boundsDecorator.getValue();
	}

	@Override
	public void setValue(Object value) {
		if (!(value instanceof Number)) value = Double.parseDouble(value.toString());
	    boundsDecorator.setValue(((Number)value).doubleValue());
	}

}
