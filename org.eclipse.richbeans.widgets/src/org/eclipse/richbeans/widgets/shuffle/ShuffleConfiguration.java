/*-
 *******************************************************************************
 * Copyright (c) 2011, 2016 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.richbeans.widgets.shuffle;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Class for configuring a ShuffleWidget
 * 
 * @author Matthew Gerring
 * @param T the type of the items we are shuffling
 */
public class ShuffleConfiguration<T> implements Cloneable {

    private String fromToolipText, toToolipText;    
	private List<T> fromList, toList;
	private boolean fromReorder=false, toReorder=true;
	private String fromLabel, toLabel;

	public ShuffleConfiguration() {
		fromList = Collections.emptyList();
		toList   = Collections.emptyList();
	}
	
	public ShuffleConfiguration<T> clone() {
		ShuffleConfiguration<T> ret = new ShuffleConfiguration<>();
		ret.fromToolipText = fromToolipText;
		ret.toToolipText  = toToolipText;
		ret.fromList = new ArrayList<>(fromList);
		ret.toList = new ArrayList<>(toList);
		ret.fromReorder  = fromReorder;
		ret.toReorder  = toReorder;
		ret.fromLabel  = fromLabel;
		ret.toLabel  = toLabel;
		return ret;
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
	
    public List<T> getFromList() {
		return fromList;
	}
	public void setFromList(List<T> fromList) {
		List<T> old = this.fromList;
		this.fromList = fromList;
		firePropertyChange("fromList", old, fromList);
	}
	public List<T> getToList() {
		return toList;
	}
	public void setToList(List<T> toList) {
		List<T> old = this.toList;
		this.toList = toList;
		firePropertyChange("toList", old, toList);
	}
	
	public boolean isFromReorder() {
		return fromReorder;
	}

	public void setFromReorder(boolean fromReorder) {
		this.fromReorder = fromReorder;
	}

	public boolean isToReorder() {
		return toReorder;
	}

	public void setToReorder(boolean toReorder) {
		this.toReorder = toReorder;
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

	public void clearListeners() {
		PropertyChangeListener[] ls = propertyChangeSupport.getPropertyChangeListeners();
		for (int i = 0; i < ls.length; i++) propertyChangeSupport.removePropertyChangeListener(ls[i]);
	}

	public String getFromLabel() {
		return fromLabel;
	}

	public void setFromLabel(String fromLabel) {
		this.fromLabel = fromLabel;
	}

	public String getToLabel() {
		return toLabel;
	}

	public void setToLabel(String toLabel) {
		this.toLabel = toLabel;
	}

}
