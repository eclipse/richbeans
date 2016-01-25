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

	@Override
	protected Map<String, String> inspectProperty(Property property) throws Exception {
		Map<String, String> attributes = new HashMap<String, String>();

		// Check the minimum value annotation
		MinimumValue minmiumValue = property.getAnnotation(MinimumValue.class);
		if (minmiumValue != null) {
			attributes.put("minimumValue", minmiumValue.value());
		}

		// Check the maximum value annotation
		MaximumValue maximumValue = property.getAnnotation(MaximumValue.class);
		if (maximumValue != null) {
			attributes.put("maximumValue", maximumValue.value());
		}

		// Check the maximum value annotation
		Units units = property.getAnnotation(Units.class);
		if (units != null) {
			attributes.put("units", units.value());
		}

		return attributes;
	}

}
