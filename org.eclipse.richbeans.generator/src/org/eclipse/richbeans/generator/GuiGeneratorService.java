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
import org.metawidget.swt.widgetbuilder.ReadOnlyWidgetBuilder;
import org.metawidget.swt.widgetbuilder.SwtWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderConfig;

public class GuiGeneratorService implements IGuiGeneratorService {

	@SuppressWarnings("unchecked") // setWidgetBuilders takes varargs and should be annotated see line 44 CompositeWidgetBuilderConfig
	@Override
	public Composite generateGui(Object bean, Composite parent) {

		// Create a Metawidget
		SwtMetawidget metawidget = new SwtMetawidget(parent, SWT.NONE);

		// Metawidget builds GUIs in five stages
		// 1. Inspector
		// Add the UiAnnotationsInspector
		metawidget.setInspector(new CompositeInspector( new CompositeInspectorConfig().setInspectors(
				new PropertyTypeInspector(),
				new MetawidgetAnnotationInspector(),
				new RichbeansUiAnnotationsInspector())));

		// 2. InspectionResultProcessors
		InspectionResultProcessor<SwtMetawidget> jexlProcessor = new JexlInspectionResultProcessor<SwtMetawidget>();
		metawidget.addInspectionResultProcessor(jexlProcessor);

		// 3. WidgetBuilder
		// Create a composite widget builder they will be called in turn until one returns a Control
		CompositeWidgetBuilderConfig<Control, SwtMetawidget> compositeWidgetBuilderConfig = new CompositeWidgetBuilderConfig<Control, SwtMetawidget>();
		// Compiler warnings are suppressed here due to varargs, see note above
		compositeWidgetBuilderConfig.setWidgetBuilders(
				// GDA Custom builders
				new ScannableWidgetBuilder(),
				// Metawidget standard builders
				new ReadOnlyWidgetBuilder(),
				new SwtWidgetBuilder());
		// Set the composite widget builder to be used
		metawidget.setWidgetBuilder(new CompositeWidgetBuilder<Control, SwtMetawidget>(compositeWidgetBuilderConfig));

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
		// e.g metawidget.setMetawidgetLayout(layout);

		// Finish
		metawidget.setToInspect(bean);
		metawidget.pack();

		return metawidget;
	}
}
