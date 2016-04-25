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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableBean {
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private List<TableItemBean> list = new ArrayList<>();

	public void addItem(TableItemBean newBean) {
		List<TableItemBean> originalList = list;
		list = new ArrayList<>();
		list.addAll(originalList);
		list.add(newBean);
		pcs.firePropertyChange("list", originalList, list);
	}
	public void clearList() {
		List<TableItemBean> originalList = list;
		list = new ArrayList<>();
		pcs.firePropertyChange("list", originalList, list);
	}
	public List<TableItemBean> getList() {
		return Collections.unmodifiableList(list);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}
}
