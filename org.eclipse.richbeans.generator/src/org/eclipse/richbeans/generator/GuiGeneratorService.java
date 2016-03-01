package org.eclipse.richbeans.generator;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.inspectionresultprocessor.commons.jexl.JexlInspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.iface.DomInspector;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.inspector.xml.XmlInspectorConfig;
import org.metawidget.swt.SwtMetawidget;

public class GuiGeneratorService implements IGuiGeneratorService {

	private static final DomInspector<?>[] EMPTY_INSPECTOR_ARRAY = new DomInspector<?>[0];
	private static Set<DomInspector<?>> domInspectors;
	private static CompositeInspector inspector;
	private static InspectionResultProcessor<SwtMetawidget> jexlProcessor;
	private static ComboLabelWidgetProcessor comboLabelProcessor;
	private static RichbeansDecoratorWidgetProcessor decoratorWidgetProcessor;
	private static TwoWayDataBindingProcessor bindingProcessor;

	// Initialise metawidget objects. All of these should be immutable so only one (static) instance is required
	static {
		XmlInspectorConfig xmlInspectorConfig = new XmlInspectorConfig();
		xmlInspectorConfig.setInputStream(GuiGeneratorService.class.getResourceAsStream("metawidget-metadata.xml"));
		xmlInspectorConfig.setRestrictAgainstObject(new JavaBeanPropertyStyle());
		xmlInspectorConfig.setValidateAgainstClasses(new JavaBeanPropertyStyle());

		domInspectors = new LinkedHashSet<>();
		domInspectors.add(new XmlInspector(xmlInspectorConfig));
		domInspectors.add(new PropertyTypeInspector());
		updateCompositeInspector();

		jexlProcessor = new JexlInspectionResultProcessor<SwtMetawidget>();
		comboLabelProcessor = new ComboLabelWidgetProcessor();
		decoratorWidgetProcessor = new RichbeansDecoratorWidgetProcessor();
		bindingProcessor = new TwoWayDataBindingProcessor();
	}

	// Use OGSi to register inspectors to provide GUI metadata
	// This is a workaround to get metadata from annotations and beans in org.eclipse.scanning.api, when we are not
	// allowed a dependency in either direction between the richbeans and scanning projects. Somewhere else in the GDA
	// codebase we will need a metawidget inspector to register as an OSGi service and inspect the scanning beans.
	public static synchronized void addDomInspector(DomInspector<?> inspector) {
		domInspectors.add(inspector);
		updateCompositeInspector();
	}
	public static synchronized void removeDomInspector(DomInspector<?> inspector) {
		domInspectors.remove(inspector);
		updateCompositeInspector();
	}
	private static void updateCompositeInspector() {
		inspector = new CompositeInspector(new CompositeInspectorConfig().setInspectors(
				domInspectors.toArray(EMPTY_INSPECTOR_ARRAY)));
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
