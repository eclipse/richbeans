package org.eclipse.richbeans.generator;

import java.util.Map;

import org.eclipse.richbeans.api.generator.RichbeansUiAnnotations;
import org.eclipse.richbeans.generator.builder.RichbeansWidgetBuilder;
import org.eclipse.richbeans.widgets.decorator.BoundsDecorator;
import org.eclipse.richbeans.widgets.decorator.FloatDecorator;
import org.eclipse.richbeans.widgets.decorator.IntegerDecorator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;

/**
 * This processor takes the plain SWT {@link Text} widget returned by the {@link RichbeansWidgetBuilder} and
 * decorates it using the appropriate Richbeans decorator. Then if limits have been defined for the field using
 * the annotations in {@link RichbeansUiAnnotations} the minimum and maximum bounds are set.
 * 
 * @author James Mudd
 */
public class RichbeansDecoratorWidgetProcessor implements AdvancedWidgetProcessor<Control, SwtMetawidget> {

	@Override
	public Control processWidget(Control widget, String elementName,
			Map<String, String> attributes, SwtMetawidget metawidget) {

		// Check if the widget is a Text widget otherwise it can't be decorated
		if (!(widget instanceof Text)) {
			return widget;
		}

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		// Create a bounds decorated widget
		BoundsDecorator decoratedWidget = null;

		if (clazz != null) {
			if (clazz.isPrimitive()) {
				// Integer
				if (int.class.equals(clazz)) {
					decoratedWidget = new IntegerDecorator((Text) widget);
				}
				if (float.class.equals(clazz) || double.class.equals(clazz)) {
					decoratedWidget = new FloatDecorator((Text) widget);
				}
			}

			// Add limits if specified by annotations
			if (attributes.get("minimumValue") != null) {
				decoratedWidget.setMinimum(Double.valueOf(attributes.get("minimumValue")));
			}
			if (attributes.get("maximumValue") != null) {
				decoratedWidget.setMaximum(Double.valueOf(attributes.get("maximumValue")));
			}
		}

		return widget;
	}

	@Override
	public void onStartBuild(SwtMetawidget metawidget) {
		// Do nothing
	}

	@Override
	public void onEndBuild(SwtMetawidget metawidget) {
		// Do nothing
	}

}
