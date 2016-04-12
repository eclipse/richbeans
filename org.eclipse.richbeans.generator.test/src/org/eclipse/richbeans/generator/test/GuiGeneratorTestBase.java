package org.eclipse.richbeans.generator.test;

import static org.metawidget.inspector.InspectionResultConstants.NAME;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.richbeans.generator.GuiGeneratorService;
import org.eclipse.richbeans.generator.RichbeansAnnotationsInspector;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;

/**
 * Helper class for unit tests of the GUI generator, which initialises the generator and provides a utility method for
 * getting controls associated with named fields from Metawidgets.
 */
public class GuiGeneratorTestBase extends SWTTestBase {

	protected IGuiGeneratorService guiGenerator;
	protected Composite metawidget;

	@BeforeClass
	public static void initialiseGuiGeneratorService() {
		GuiGeneratorService.addDomInspector(new RichbeansAnnotationsInspector());
		GuiGeneratorService.addDomInspector(new MetawidgetAnnotationInspector());
	}

	@Before
	public void initialiseGuiGenerator() throws Exception {
		guiGenerator = new GuiGeneratorService();
	}

	@After
	public void destroyGuiGenerator() throws Exception {
		metawidget = null;
		guiGenerator = null;
	}

	protected Control getNamedControl(String name) {
		return getNamedControl(metawidget, name);
	}

	protected static Control getNamedControl(Composite container, String name) {
		for (Control child : container.getChildren()) {
			// TODO investigate this - is it necessary to check for name == null, and does this code work for nested Metawidgets?
			if (child.getData(NAME) == null && child instanceof Composite) {
				Control control = getNamedControl((Composite) child, name);
				if (control != null) {
					return control;
				}
			}
			if (name.equals(child.getData(NAME))) {
				return child;
			}
		}
		return null; // not found
	}
}
