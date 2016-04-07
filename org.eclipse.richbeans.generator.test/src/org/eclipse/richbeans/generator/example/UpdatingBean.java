/*-
 * Copyright © 2016 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */

package org.eclipse.richbeans.generator.example;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.metawidget.inspector.annotation.UiReadOnly;

public class UpdatingBean {

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	private boolean update;
	private double x;
	private double y;

	public boolean isUpdate() {
		return update;
	}
	public void setUpdate(boolean newValue) {
		Object oldValue = this.update;
		this.update = newValue;
		pcs.firePropertyChange("update", oldValue, newValue);
	}
	@UiReadOnly
	public double getX() {
		return x;
	}
	public void setX(double newValue) {
		Object oldValue = this.x;
		this.x = newValue;
		pcs.firePropertyChange("x", oldValue, newValue);
	}
	@UiReadOnly
	public double getY() {
		return y;
	}
	public void setY(double newValue) {
		Object oldValue = this.y;
		this.y = newValue;
		pcs.firePropertyChange("y", oldValue, newValue);
	}
}
