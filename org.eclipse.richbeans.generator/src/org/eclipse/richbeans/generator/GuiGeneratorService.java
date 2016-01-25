package org.eclipse.richbeans.generator;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.inspectionresultprocessor.commons.jexl.JexlInspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swt.SwtMetawidget;

public class GuiGeneratorService implements IGuiGeneratorService {

	@Override
	public Control generateGui(Object bean, Composite parent) {

		// Create a Metawidget
		SwtMetawidget metawidget = new SwtMetawidget(parent, SWT.NONE);

		// Metawidget builds GUIs in five stages
		// 1. Inspector
		// Add the UiAnnotationsInspector
		metawidget.setInspector(new CompositeInspector( new CompositeInspectorConfig().setInspectors(
				new PropertyTypeInspector(),
				new MetawidgetAnnotationInspector(),
				new RichbeansAnnotationsInspector())));

		// 2. InspectionResultProcessors
		InspectionResultProcessor<SwtMetawidget> jexlProcessor = new JexlInspectionResultProcessor<SwtMetawidget>();
		metawidget.addInspectionResultProcessor(jexlProcessor);

		// 3. WidgetBuilder
		// (default)

		// 4. WidgetProcessors

		// Combo label decorator to switch combo labels for more readable values
		// (For enums, this will replace the declared enum name with the result of toString())
		metawidget.addWidgetProcessor(new ComboLabelWidgetProcessor());

		// The Richbeans decorator processor will add limits to int float and double UI fields
		RichbeansDecoratorWidgetProcessor decoratorWidgetProcessor = new RichbeansDecoratorWidgetProcessor();
		metawidget.addWidgetProcessor(decoratorWidgetProcessor);

		// Add Eclipse data binding
		TwoWayDataBindingProcessor bindingProcessor = new TwoWayDataBindingProcessor();
		metawidget.addWidgetProcessor(bindingProcessor);

		// TODO add reflection binding processor for actions?

		// 5. Layout
		// (default)

		// Finish
		metawidget.setToInspect(bean);
		metawidget.pack();

		return metawidget;
	}
}
