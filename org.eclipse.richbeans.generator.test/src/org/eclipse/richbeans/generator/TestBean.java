package org.eclipse.richbeans.generator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.metawidget.inspector.annotation.UiReadOnly;

class TestBean {

	private static final String STRING_FIELD_WITH_GETTER_ONLY = "String field with getter only";

	private String stringField;
	private String uiReadOnlyStringField;

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	public String getStringFieldWithGetterOnly() {
		return STRING_FIELD_WITH_GETTER_ONLY;
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
		pcs.firePropertyChange("uiReadOnlyStringField", oldValue,
				this.uiReadOnlyStringField);
	}
}