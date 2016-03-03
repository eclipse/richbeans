package org.eclipse.richbeans.generator;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.iface.DomInspector;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.inspector.xml.XmlInspectorConfig;
import org.metawidget.swt.SwtMetawidget;

public class GuiGeneratorService implements IGuiGeneratorService {

	// Initialise metawidget objects. All of these should be immutable so only one (static) instance is required
	private static final ComboLabelWidgetProcessor COMBO_LABEL_PROCESSOR = new ComboLabelWidgetProcessor();
	private static final RichbeansDecoratorWidgetProcessor DECORATOR_PROCESSOR = new RichbeansDecoratorWidgetProcessor();
	private static final TwoWayDataBindingProcessor BINDING_PROCESSOR = new TwoWayDataBindingProcessor();

	// Initialise a set for the inspectors. The reference to the set is final but the contents will change.
	private static final Set<DomInspector<?>> DOM_INSPECTORS = new LinkedHashSet<>();
	private static final DomInspector<?>[] EMPTY_INSPECTOR_ARRAY = new DomInspector<?>[0];

	private static CompositeInspector inspector;

	static {
		XmlInspectorConfig xmlInspectorConfig = new XmlInspectorConfig();
		xmlInspectorConfig.setInputStream(GuiGeneratorService.class.getResourceAsStream("metawidget-metadata.xml"));
		xmlInspectorConfig.setRestrictAgainstObject(new JavaBeanPropertyStyle());
		xmlInspectorConfig.setValidateAgainstClasses(new JavaBeanPropertyStyle());

		DOM_INSPECTORS.add(new XmlInspector(xmlInspectorConfig));
		DOM_INSPECTORS.add(new PropertyTypeInspector());
		updateCompositeInspector();
	}

	// Use OGSi to register inspectors to provide GUI metadata
	// This is a workaround to get metadata from annotations and beans in org.eclipse.scanning.api, when we are not
	// allowed a dependency in either direction between the richbeans and scanning projects. Somewhere else in the GDA
	// codebase we will need a metawidget inspector to register as an OSGi service and inspect the scanning beans.
	public static synchronized void addDomInspector(DomInspector<?> inspector) {
		DOM_INSPECTORS.add(inspector);
		updateCompositeInspector();
	}
	public static synchronized void removeDomInspector(DomInspector<?> inspector) {
		DOM_INSPECTORS.remove(inspector);
		updateCompositeInspector();
	}
	private static void updateCompositeInspector() {
		inspector = new CompositeInspector(new CompositeInspectorConfig().setInspectors(
				DOM_INSPECTORS.toArray(EMPTY_INSPECTOR_ARRAY)));
	}

	@Override
	public Control generateGui(Object bean, Composite parent) {

		// Create a Metawidget
		SwtMetawidget metawidget = new SwtMetawidget(parent, SWT.NONE);

		// Metawidget builds GUIs in five stages
		// 1. Inspector
		metawidget.setInspector(inspector);

		// 2. InspectionResultProcessors
		// none (would probably like the JEXL processor here in the long run but can't build with it included at the moment)

		// 3. WidgetBuilder
		// (default)

		// 4. WidgetProcessors
		metawidget.addWidgetProcessor(COMBO_LABEL_PROCESSOR);
		metawidget.addWidgetProcessor(DECORATOR_PROCESSOR);
		metawidget.addWidgetProcessor(BINDING_PROCESSOR);

		// Reflection binding processor (for actions) is present by default

		// 5. Layout
		// (default)

		// Finish
		metawidget.setToInspect(bean);
		metawidget.pack();

		return metawidget;
	}
}
