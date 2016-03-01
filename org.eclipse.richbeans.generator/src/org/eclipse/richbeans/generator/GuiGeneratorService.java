package org.eclipse.richbeans.generator;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.inspectionresultprocessor.commons.jexl.JexlInspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.inspector.xml.XmlInspectorConfig;
import org.metawidget.swt.SwtMetawidget;

public class GuiGeneratorService implements IGuiGeneratorService {

	private static XmlInspectorConfig xmlInspectorConfig;
	private static CompositeInspector inspector;
	private static InspectionResultProcessor<SwtMetawidget> jexlProcessor;
	private static ComboLabelWidgetProcessor comboLabelProcessor;
	private static RichbeansDecoratorWidgetProcessor decoratorWidgetProcessor;
	private static TwoWayDataBindingProcessor bindingProcessor;

	// Initialise metawidget objects. All of these should be immutable so only one (static) instance is required
	static {
		xmlInspectorConfig = new XmlInspectorConfig();
		xmlInspectorConfig.setInputStream(GuiGeneratorService.class.getResourceAsStream("metawidget-metadata.xml"));
		xmlInspectorConfig.setRestrictAgainstObject(new JavaBeanPropertyStyle());
		xmlInspectorConfig.setValidateAgainstClasses(new JavaBeanPropertyStyle());

		inspector = new CompositeInspector(new CompositeInspectorConfig().setInspectors(
				new XmlInspector(xmlInspectorConfig),
				new PropertyTypeInspector(),
				new MetawidgetAnnotationInspector(),
				new RichbeansAnnotationsInspector()));

		jexlProcessor = new JexlInspectionResultProcessor<SwtMetawidget>();

		comboLabelProcessor = new ComboLabelWidgetProcessor();
		decoratorWidgetProcessor = new RichbeansDecoratorWidgetProcessor();
		bindingProcessor = new TwoWayDataBindingProcessor();
	}

	@Override
	public Control generateGui(Object bean, Composite parent) {

		// Create a Metawidget
		SwtMetawidget metawidget = new SwtMetawidget(parent, SWT.NONE);

		// Metawidget builds GUIs in five stages
		// 1. Inspector
		metawidget.setInspector(inspector);

		// 2. InspectionResultProcessors
		metawidget.addInspectionResultProcessor(jexlProcessor);

		// 3. WidgetBuilder
		// (default)

		// 4. WidgetProcessors
		metawidget.addWidgetProcessor(comboLabelProcessor);
		metawidget.addWidgetProcessor(decoratorWidgetProcessor);
		metawidget.addWidgetProcessor(bindingProcessor);

		// Reflection binding processor (for actions) is present by default

		// 5. Layout
		// (default)

		// Finish
		metawidget.setToInspect(bean);
		metawidget.pack();

		return metawidget;
	}
}
