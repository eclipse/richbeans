package org.eclipse.richbeans.widgets.decorator;

import org.eclipse.richbeans.api.event.ValueEvent;
import org.eclipse.richbeans.api.event.ValueListener;
import org.eclipse.richbeans.api.widget.IFieldWidget;
import org.eclipse.richbeans.widgets.EventManagerDelegate;
import org.eclipse.swt.widgets.Control;

public abstract class FieldDecorator<T extends Control> implements IFieldWidget {

    /**
     * The widget which we are decorating into an IFieldWidget
     */
	protected T       connection;
	protected boolean isOn;
	protected String  fieldName;
	
	/**
	 * Events managed here.
	 */
	protected EventManagerDelegate eventDelegate;

	protected FieldDecorator(T connection) {
		this.connection    = connection;
		this.eventDelegate = new EventManagerDelegate(this);
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
	public void fireValueListeners() {
		fireValueListeners(getValue());
	}

	protected void fireValueListeners(Object value) {
		final ValueEvent evt = new ValueEvent(this, getFieldName());
		evt.setValue(value);
		eventDelegate.notifyValueListeners(evt);
	}


	@Override
	public void fireBoundsUpdaters() {
		final ValueEvent evt = new ValueEvent(this, getFieldName());
		evt.setValue(getValue());
		eventDelegate.notifyBoundsProviderListeners(evt);
	}

	protected boolean active = true;

	@Override
	public boolean isActivated() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
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
	public void setEnabled(boolean isEnabled) {
		connection.setEnabled(isEnabled);
	}

	@Override
	public void dispose() {
		if (eventDelegate!=null) eventDelegate.dispose();
		if (connection!=null)    connection.dispose();
	}


	@Override
	public boolean isOn() {
		return isOn ;
	}

	@Override
	public void off() {
		isOn = false;
	}

	@Override
	public void on() {
		isOn = true;
	}

}
