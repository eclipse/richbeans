package org.eclipse.richbeans.generator;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.metawidget.inspectionresultprocessor.commons.jexl.JexlInspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swt.SwtMetawidget;

public class GuiGeneratorService implements IGuiGeneratorService {

	@Override
	public Composite generateGui(Object bean, Composite parent) {

		SwtMetawidget metawidget = new SwtMetawidget(parent, SWT.NONE);

		// Add the UiAnnotationsInspector
		metawidget.setInspector(new CompositeInspector( new CompositeInspectorConfig().setInspectors(
				new PropertyTypeInspector(),
				new MetawidgetAnnotationInspector(),
				new RichbeansUiAnnotationsInspector())));

		// The Richbeans decorator processor will add limits to int float and double UI fields
		RichbeansDecoratorWidgetProcessor decoratorWidgetProcessor = new RichbeansDecoratorWidgetProcessor();
		metawidget.addWidgetProcessor(decoratorWidgetProcessor);
		
		TwoWayDataBindingProcessor bindingProcessor = new TwoWayDataBindingProcessor();
		metawidget.addWidgetProcessor(bindingProcessor);

		InspectionResultProcessor<SwtMetawidget> jexlProcessor = new JexlInspectionResultProcessor<SwtMetawidget>();
		metawidget.addInspectionResultProcessor(jexlProcessor);

		metawidget.setToInspect(bean);
		metawidget.pack();

		return metawidget;
	}
}
