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

import static org.metawidget.inspector.InspectionResultConstants.NAME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableWidgetProcessor implements WidgetProcessor<Control, SwtMetawidget> {
	@Override
	public Control processWidget(Control originalWidget, String elementName, Map<String, String> attributes, SwtMetawidget metawidget) {
		Class<?> container = WidgetBuilderUtils.getActualClassOrType( attributes, Object.class );
		String contained = WidgetBuilderUtils.getComponentType(attributes);

		if (List.class.isAssignableFrom(container) && contained != null){
			TableViewer tableViewer = new TableViewer(metawidget);
			Table table = tableViewer.getTable();
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			tableViewer.setContentProvider(new ListStructuredContentProvider());

			Map<String, String> columns = readPropertiesFromType(metawidget, contained);
			createColumns(columns, tableViewer);

			setupDataInput(attributes, metawidget, tableViewer);

			return tableViewer.getControl();
		}
		return originalWidget;
	}

	private void setupDataInput(Map<String, String> attributes, SwtMetawidget metawidget, TableViewer tableViewer) {
		Object toInspect = metawidget.getToInspect();

		String propertyName = attributes.get(NAME);
		IBeanValueProperty value = BeanProperties.value(toInspect.getClass(), propertyName);

		tableViewer.setInput(value.getValue(toInspect));
	}

	private void createColumns(Map<String, String> columns, TableViewer tableViewer) {
		for (String column : columns.keySet()) {
			TableViewerColumn colFirstName = new TableViewerColumn(tableViewer, SWT.NONE);
			colFirstName.getColumn().setWidth(200);
			colFirstName.getColumn().setText(columns.get(column));
			colFirstName.setLabelProvider(new ColumnLabelProvider() {
				  @Override
				  public String getText(Object element) {
				    return BeanProperties.value(element.getClass(), column).getValue(element).toString();
				  }
			});
		}
	}

	private Map<String, String> readPropertiesFromType(SwtMetawidget metawidget, String componentType) {
		String inspectedType = metawidget.inspect( null, componentType, (String[]) null );

		Map<String,String> columns = new HashMap<>();
		Element root = XmlUtils.documentFromString( inspectedType ).getDocumentElement();
		NodeList elements = root.getFirstChild().getChildNodes();

		for ( int i = 0; i < elements.getLength(); i++ ) {
		   Node node = elements.item(i);
		   Map<String, String> attributesAsMap = XmlUtils.getAttributesAsMap( node );
		   columns.put(attributesAsMap.get(NAME), metawidget.getLabelString( attributesAsMap ));
		}
		return columns;
	}
}
