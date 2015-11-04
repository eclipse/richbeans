package org.eclipse.richbeans.generator;

import gda.device.Scannable;
import gda.factory.Finder;
import gda.rcp.views.NudgePositionerComposite;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtValuePropertyProvider;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

public class ScannableWidgetBuilder implements WidgetBuilder<Control, SwtMetawidget>, SwtValuePropertyProvider{

	@Override
	public String getValueProperty(Control control) {
		// As there wont actually be any data binding to this control return null
		return null;
	}

	@Override
	public Control buildWidget(String elementName, Map<String, String> attributes, SwtMetawidget metawidget) {

		// Lookup by string, not good, but Corba gives the actual class as ScannableAdaptor which isn't useful here
		if (attributes.get("type").contentEquals("gda.device.Scannable")) {
			// Get the scannable with the finder (Probably bad!). Need a way to get the actual underlying object here,
			// this is usually done by the data binding which calls the getter to initialise the field but after control
			// creation.
			Scannable scannable = Finder.getInstance().find(attributes.get("name"));
			// Create the control for it
			return new NudgePositionerComposite(metawidget.getCurrentLayoutComposite(), SWT.NONE, scannable);
		}

		return null;
	}

}
