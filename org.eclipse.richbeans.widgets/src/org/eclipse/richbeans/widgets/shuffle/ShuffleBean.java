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

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

class ShuffleBean<T> {

	private TableViewer table; 
	private String name; 
	private List<T> list;
	private ShuffleDirection direction;
	
	public ShuffleBean() {
		
	}
	/**
	 * 
	 * @param table
	 * @param name
	 * @param list
	 * @param direction - The direction of a move into this list!
	 */
	public ShuffleBean(TableViewer table, String name, List<T> list, ShuffleDirection direction) {
		super();
		this.table = table;
		this.name = name;
		this.list = list;
		this.direction = direction;
	}
	public TableViewer getTable() {
		return table;
	}
	public void setTable(TableViewer table) {
		this.table = table;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public ShuffleDirection getDirection() {
		return direction;
	}
	public void setDirection(ShuffleDirection direction) {
		this.direction = direction;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShuffleBean other = (ShuffleBean) obj;
		if (direction != other.direction)
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}
}
