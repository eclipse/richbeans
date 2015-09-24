package org.eclipse.richbeans.generator.builder;

import java.util.Map;

import org.eclipse.richbeans.api.generator.RichbeansUiAnnotations;
import org.eclipse.richbeans.generator.RichbeansDecoratorWidgetProcessor;
import org.eclipse.richbeans.generator.RichbeansUiAnnotationsInspector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtValuePropertyProvider;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * This class will check for fields of types <code>int</code> <code>float</code>
 * and <code>double</code> and return a plain SWT {@link Text} widget. This can
 * then be decorated by the {@link RichbeansDecoratorWidgetProcessor} to add
 * limits. The bean annotations to specify the limits are in {@link RichbeansUiAnnotations}
 * 
 * @see RichbeansDecoratorWidgetProcessor
 * @see RichbeansUiAnnotations
 * @see RichbeansUiAnnotationsInspector
 * 
 * @author James Mudd
 */
public class RichbeansWidgetBuilder implements
		WidgetBuilder<Control, SwtMetawidget>, SwtValuePropertyProvider {

	@Override
	public String getValueProperty(Control control) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Control buildWidget(String elementName,
			Map<String, String> attributes, SwtMetawidget metawidget) {

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType(attributes,
				String.class);

		if (clazz != null) {
			// Check if the lookup annotation is added in this case need a combo
			// box so leave to SWT builder
			if (attributes.containsKey("lookup")) {
				return null;
			}
			// Cases handled by Richbeans decorators
			if (int.class.equals(clazz) ||
				float.class.equals(clazz) ||
				double.class.equals(clazz)) {
				return new Text(metawidget.getCurrentLayoutComposite(), SWT.BORDER);
			}
		}
		return null;
	}

}
