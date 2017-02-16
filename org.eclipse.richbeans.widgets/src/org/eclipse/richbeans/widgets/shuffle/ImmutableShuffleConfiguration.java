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
import java.util.ArrayList;
import java.util.List;

class ImmutableShuffleConfiguration<T> extends ShuffleConfiguration<T> {

	private ShuffleConfiguration<T> delegate;

	ImmutableShuffleConfiguration(ShuffleConfiguration<T> delegate) {
		this.delegate = delegate.clone();
	}

	public ShuffleConfiguration<T> clone() {
        return this; // It's immutable and so is the clone.
	}
	
	public String getFromToolipText() {
		return delegate.getFromToolipText(); // Ok string already are
	}

	public void setFromToolipText(String fromToolipText) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public String getToToolipText() {
		return delegate.getToToolipText();// Ok string already are
	}

	public void setToToolipText(String toToolipText) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public List<T> getFromList() {
		return new ArrayList<T>(delegate.getFromList());
	}

	public void setFromList(List<T> fromList) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public List<T> getToList() {
		return new ArrayList<T>(delegate.getToList());
	}

	public void setToList(List<T> toList) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public boolean isFromReorder() {
		return delegate.isFromReorder(); // booleans are
	}

	public void setFromReorder(boolean fromReorder) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public boolean isToReorder() {
		return delegate.isToReorder(); // booleans are
	}

	public void setToReorder(boolean toReorder) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public void clearListeners() {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public String getFromLabel() {
		return delegate.getFromLabel(); // String are
	}

	public void setFromLabel(String fromLabel) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public String getToLabel() {
		return delegate.getToLabel(); // Strings are
	}

	public void setToLabel(String toLabel) {
		throw new IllegalArgumentException(getClass().getSimpleName()+" is immutable!");
	}

	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	public String toString() {
		return delegate.toString();
	}
}
