package org.eclipse.richbeans.generator;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.richbeans.api.generator.RichbeansAnnotations;
import org.eclipse.richbeans.api.generator.RichbeansAnnotations.MaximumValue;
import org.eclipse.richbeans.api.generator.RichbeansAnnotations.MinimumValue;
import org.eclipse.richbeans.api.generator.RichbeansAnnotations.Units;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.propertystyle.Property;

/**
 * This inspector checks for the annotations in {@link RichbeansAnnotations} and adds them to the Metawidget
 * pipeline so they can be applied by the {@link RichbeansDecoratorWidgetProcessor}.
 *
 * @author James Mudd
 */
public class RichbeansAnnotationsInspector extends BaseObjectInspector {

	// We'd like to be able to use "maximum-value" and "minimum-value" here, since they're already defined as standard
	// attribute names in Metawidget. Unfortunately, though, if both of them are applied to a numeric field,
	// SwtMetawidget uses a Scale slider widget which is inappropriate since it doesn't actually show the number!
	public static final String MINIMUM_VALUE = "minimumValue";
	public static final String MAXIMUM_VALUE = "maximumValue";
	public static final String UNITS = "units";

	@Override
	protected Map<String, String> inspectProperty(Property property) throws Exception {
		Map<String, String> attributes = new HashMap<String, String>();

		// Check the minimum value annotation
		MinimumValue minmiumValue = property.getAnnotation(MinimumValue.class);
		if (minmiumValue != null) {
			attributes.put(MINIMUM_VALUE, minmiumValue.value());
		}

		// Check the maximum value annotation
		MaximumValue maximumValue = property.getAnnotation(MaximumValue.class);
		if (maximumValue != null) {
			attributes.put(MAXIMUM_VALUE, maximumValue.value());
		}

		// Check the maximum value annotation
		Units units = property.getAnnotation(Units.class);
		if (units != null) {
			attributes.put(UNITS, units.value());
		}

		return attributes;
	}
}
