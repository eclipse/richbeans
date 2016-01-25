package org.eclipse.richbeans.generator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.richbeans.api.generator.RichbeansUiAnnotations.MaximumValue;
import org.eclipse.richbeans.api.generator.RichbeansUiAnnotations.MinimumValue;
import org.metawidget.inspector.annotation.UiReadOnly;

class TestBean {

	public static final String INT_FIELD_MAX_VALUE_STRING = "25";
	public static final String INT_FIELD_MIN_VALUE_STRING = "-10";

	private String stringField;
	private String uiReadOnlyStringField;
	private int intField;

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	public String getStringFieldWithGetterOnly() {
		return "String field with getter only";
	}

	public String getStringField() {
		return stringField;
	}
	public void setStringField(String stringField) {
		Object oldValue = this.stringField;
		this.stringField = stringField;
		pcs.firePropertyChange("stringField", oldValue, this.stringField);
	}

	@UiReadOnly
	public String getUiReadOnlyStringField() {
		return uiReadOnlyStringField;
	}
	public void setUiReadOnlyStringField(String uiReadOnlyStringField) {
		Object oldValue = this.uiReadOnlyStringField;
		this.uiReadOnlyStringField = uiReadOnlyStringField;
		pcs.firePropertyChange("uiReadOnlyStringField", oldValue, this.uiReadOnlyStringField);
	}

	@MinimumValue(INT_FIELD_MIN_VALUE_STRING)
	@MaximumValue(INT_FIELD_MAX_VALUE_STRING)
	public int getIntField() {
		return intField;
	}
	public void setIntField(int intField) {
		Object oldValue = this.intField;
		this.intField = intField;
		pcs.firePropertyChange("intField", oldValue, this.intField);
	}
}