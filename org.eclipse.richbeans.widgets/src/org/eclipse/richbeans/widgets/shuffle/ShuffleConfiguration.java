package org.eclipse.richbeans.widgets.shuffle;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Class for configuring a ShuffleWidget
 * 
 * @author Matthew Gerring
 *
 */
public class ShuffleConfiguration {

    private String fromToolipText, toToolipText;    
	private List<Object> fromList, toList;
	
	public ShuffleConfiguration() {
		fromList = Collections.emptyList();
		toList   = Collections.emptyList();
	}
    
	public String getFromToolipText() {
		return fromToolipText;
	}
	public void setFromToolipText(String fromToolipText) {
		String old = this.fromToolipText;
		this.fromToolipText = fromToolipText;
		firePropertyChange("fromToolipText", old, fromToolipText);
	}
	public String getToToolipText() {
		return toToolipText;
	}
	public void setToToolipText(String toToolipText) {
		String old = this.toToolipText;
		this.toToolipText = toToolipText;
		firePropertyChange("toToolipText", old, toToolipText);
    }
	
    public List<Object> getFromList() {
		return fromList;
	}
	public void setFromList(List<Object> fromList) {
		List<Object> old = this.fromList;
		this.fromList = fromList;
		firePropertyChange("fromList", old, fromList);
	}
	public List<Object> getToList() {
		return toList;
	}
	public void setToList(List<Object> toList) {
		List<Object> old = this.toList;
		this.toList = toList;
		firePropertyChange("toList", old, toList);
	}

	
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName,
				listener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,
				newValue);
	}

}
