package org.eclipse.richbeans.generator;

import java.util.Map;

import org.eclipse.richbeans.api.generator.RichbeansUiAnnotations;
import org.eclipse.richbeans.widgets.decorator.FloatDecorator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * This processor takes the plain SWT widgets and if required (for
 * <code>float</code> or <code>double</code>) decorates them using the
 * appropriate Richbeans decorator. If limits have been defined for the field
 * using the annotations in {@link RichbeansUiAnnotations} the minimum and
 * maximum bounds are set.
 * 
 * @see RichbeansUiAnnotations
 * 
 * @author James Mudd
 */
public class RichbeansDecoratorWidgetProcessor implements WidgetProcessor<Control, SwtMetawidget> {

	@Override
	public Control processWidget(Control widget, String elementName, Map<String, String> attributes, SwtMetawidget metawidget) {

		// Check if the widget is a SWT Spinner (for int) and try to set limits
		if (widget instanceof Spinner) {
			Spinner spinner = (Spinner) widget;
			if (attributes.get("minimumValue") != null) {
				spinner.setMinimum(Integer.valueOf(attributes.get("minimumValue")));
			}
			if (attributes.get("maximumValue") != null) {
				spinner.setMaximum(Integer.valueOf(attributes.get("maximumValue")));
			}
			return widget;
		}

		// Check if the widget is a Text widget otherwise it can't be decorated
		if (!(widget instanceof Text)) {
			return widget;
		}

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		// Create a decorated widget and apply limits
		if (clazz != null) {
			if (clazz.isPrimitive()) {
				if (float.class.equals(clazz) || double.class.equals(clazz)) {
					FloatDecorator decoratedWidget = new FloatDecorator((Text) widget);

					// Add limits if specified by annotations
					if (attributes.get("minimumValue") != null) {
						decoratedWidget.setMinimum(Double.valueOf(attributes.get("minimumValue")));
					}
					if (attributes.get("maximumValue") != null) {
						decoratedWidget.setMaximum(Double.valueOf(attributes.get("maximumValue")));
					}
				}
			}
		}

		return widget;
	}
}
