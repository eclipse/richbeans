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

package org.eclipse.richbeans.widgets.data;

import org.eclipse.richbeans.api.event.ValueEvent;
import org.eclipse.richbeans.api.event.ValueListener;
import org.eclipse.richbeans.api.widget.IFieldWidget;
import org.eclipse.richbeans.widgets.EventManagerDelegate;

/**
 * A not widget wrapper for data only contained in the editor.
 */
public class DataWrapper implements IFieldWidget {

	private EventManagerDelegate eventDelegate;
	private Object value;
	private String fieldName;
	private boolean isOn = false;


	public DataWrapper() {
		this.eventDelegate = new EventManagerDelegate(this);
	}
	
	public DataWrapper(String fieldName, ValueListener listener) {
		this();
		setFieldName(fieldName);
		addValueListener(listener);
	}


	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public void addValueListener(ValueListener l) {
		eventDelegate.addValueListener(l);
	}

	@Override
	public void removeValueListener(ValueListener l) {
		eventDelegate.removeValueListener(l);
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public boolean isActivated() {
		return true;
	}

	@Override
	public boolean isOn() {
		return isOn;
	}

	@Override
	public void off() {
		isOn = false;
	}

	@Override
	public void on() {
		isOn = true;
	}

	@Override
	public void setEnabled(boolean isEnabled) {

	}

	@Override
	public void setValue(Object value) {
		this.value = value;
		eventDelegate.notifyValueListeners(new ValueEvent(this, getFieldName()));
	}

	@Override
	public void fireValueListeners() {
		final ValueEvent evt = new ValueEvent(this, getFieldName());
		evt.setValue(getValue());
		eventDelegate.notifyValueListeners(evt);
	}

	@Override
	public void fireBoundsUpdaters() {
		// There are none
	}

	@Override
	public void dispose() {

	}

}
