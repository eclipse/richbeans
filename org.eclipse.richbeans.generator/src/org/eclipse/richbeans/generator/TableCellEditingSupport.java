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

package org.eclipse.richbeans.generator;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.richbeans.widgets.cell.SpinnerCellEditor;
import org.eclipse.swt.widgets.Table;

public class TableCellEditingSupport extends EditingSupport {
	private final String column;
	private Table table;

	public TableCellEditingSupport(ColumnViewer viewer, Table table, String column) {
		super(viewer);
		this.table = table;
		this.column = column;
	}

	@Override
	protected void setValue(Object element, Object value) {
		BeanProperties.value(element.getClass(), column).setValue(element, value);
	}

	@Override
	protected Object getValue(Object element) {
		return BeanProperties.value(element.getClass(), column).getValue(element);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		Class<?> type = (Class<?>)BeanProperties.value(element.getClass(), column).getValueType();
		if (Number.class.isAssignableFrom(type) || type.isPrimitive()){
			return new SpinnerCellEditor(table);
		}
		return new TextCellEditor(table);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}
}