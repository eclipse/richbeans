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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.richbeans.widgets.cell.NumberCellEditor;
import org.eclipse.richbeans.widgets.cell.SpinnerCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;

public class TableCellEditingSupport extends EditingSupport {
	private final Map<Class<?>, Function<String, ?>> convertors = new HashMap<>();
	private final String column;
	private Table table;

	public TableCellEditingSupport(ColumnViewer viewer, Table table, String column) {
		super(viewer);
		this.table = table;
		this.column = column;

		convertors.put(String.class, Function.<String>identity());
		convertors.put(Integer.class, Integer::valueOf);
		convertors.put(Integer.TYPE, Integer::valueOf);
		convertors.put(Long.class, Long::valueOf);
		convertors.put(Long.TYPE, Long::valueOf);
		convertors.put(Double.class, Double::valueOf);
		convertors.put(Double.TYPE, Double::valueOf);
		convertors.put(Float.class, Float::valueOf);
		convertors.put(Float.TYPE, Float::valueOf);
	}

	@Override
	public void setValue(Object element, Object value) {
		IBeanValueProperty property = BeanProperties.value(element.getClass(), column);
		Object convertedValue = convertors
				.get(property.getValueType())
				.apply(value.toString());
		property.setValue(element, convertedValue);
	}

	@Override
	protected Object getValue(Object element) {
		return BeanProperties.value(element.getClass(), column).getValue(element);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		Class<?> type = (Class<?>)BeanProperties.value(element.getClass(), column).getValueType();

		if (isInteger(type)){
			return new SpinnerCellEditor(table, SWT.NONE);
		} else if (Number.class.isAssignableFrom(type) || type.isPrimitive()){
			return new NumberCellEditor(table, type, SWT.NONE);
		}
		return new TextCellEditor(table, SWT.NONE);
	}

	private <T> boolean isInteger(Class<T> clazz){
		return
				Integer.class.isAssignableFrom(clazz)||
				Long.class.isAssignableFrom(clazz)||
				Integer.TYPE == clazz ||
				Long.TYPE == clazz;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}
}