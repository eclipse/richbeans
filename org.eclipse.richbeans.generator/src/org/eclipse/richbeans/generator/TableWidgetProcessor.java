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
import java.util.stream.Collectors;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.jface.databinding.swt.DisplayRealm;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.ClassUtils;
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
		Class<?> containedClazz = loadContainedClass(contained);

		if (List.class.isAssignableFrom(container) && contained != null && containedClazz != null){
			TableViewer tableViewer = new TableViewer(metawidget);
			Table table = tableViewer.getTable();
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			tableViewer.setContentProvider(new ListStructuredContentProvider());

			Map<String, String> columns = readPropertiesFromType(metawidget, contained);
			createColumns(columns, tableViewer);

			setupDataInput(containedClazz, columns, attributes, metawidget, tableViewer);

			originalWidget.dispose();
			return tableViewer.getControl();
		}
		return originalWidget;
	}

	private Class<?> loadContainedClass(String contained) {
		if (contained != null){
			return ClassUtils.niceForName(contained);
		}
		return null;
	}

	private void setupDataInput(Class<?> containedClazz, Map<String, String> columns, Map<String, String> attributes, SwtMetawidget metawidget, TableViewer tableViewer) {
		Object toInspect = metawidget.getToInspect();

		String propertyName = attributes.get(NAME);
		IBeanValueProperty value = BeanProperties.value(toInspect.getClass(), propertyName);

		Realm realm = DisplayRealm.getRealm(metawidget.getDisplay());
		IObservableValue observableValue = value.observe(realm, toInspect);

		List<IBeanValueProperty> itemProperties = columns.keySet().stream()
				.map((property) -> BeanProperties.value(containedClazz, property))
				.collect(Collectors.toList());

		observableValue.addValueChangeListener(new IValueChangeListener() {
			@Override
			public void handleValueChange(ValueChangeEvent event) {
				listenToItems(tableViewer, toInspect, value, realm, itemProperties);
				tableViewer.setInput(value.getValue(toInspect));
				tableViewer.refresh();
			}
		});

		listenToItems(tableViewer, toInspect, value, realm, itemProperties);
		tableViewer.setInput(value.getValue(toInspect));
	}

	private void listenToItems(TableViewer tableViewer, Object toInspect, IBeanValueProperty value, Realm realm, List<IBeanValueProperty> itemProperties) {
		for (Object item : (List<?>)value.getValue(toInspect)) {
			for (IBeanValueProperty itemValue : itemProperties) {
				IObservableValue observableItem = itemValue.observe(realm, item);
				observableItem.addValueChangeListener((event) -> tableViewer.refresh());
			}
		}
	}

	private void createColumns(Map<String, String> columns, TableViewer tableViewer) {
		for (String column : columns.keySet()) {
			TableViewerColumn columnViewer = new TableViewerColumn(tableViewer, SWT.NONE);
			columnViewer.getColumn().setWidth(200);
			columnViewer.getColumn().setText(columns.get(column));
			columnViewer.setLabelProvider(new ColumnLabelProvider() {
				  @Override
				  public String getText(Object element) {
				    return BeanProperties.value(element.getClass(), column).getValue(element).toString();
				  }
			});
			columnViewer.setEditingSupport(new TableCellEditingSupport(columnViewer.getViewer(), tableViewer.getTable(), column));
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
		   if (!"true".equals(attributesAsMap.get(RichbeansAnnotationsInspector.HIDDEN))){
			   columns.put(attributesAsMap.get(NAME), metawidget.getLabelString(attributesAsMap));
		   }
		}
		return columns;
	}
}
