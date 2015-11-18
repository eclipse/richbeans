package org.eclipse.richbeans.generator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.metawidget.inspector.InspectionResultConstants.NAME;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GuiGeneratorTest extends SWTTestBase {

	private static final String STRING_FIELD_VALUE = "String field";

	private IGuiGeneratorService guiGenerator;
	private TestBean testBean;
	private Composite metawidget;

	@Before
	public void setUp() throws Exception {
		guiGenerator = new GuiGeneratorService();
		testBean = new TestBean();
		testBean.setStringField(STRING_FIELD_VALUE);
		testBean.setUiReadOnlyStringField(STRING_FIELD_VALUE);
		metawidget = (Composite) guiGenerator.generateGui(testBean, shell);
	}

	@After
	public void tearDown() throws Exception {
		guiGenerator = null;
	}

	@Test
	public void testStringField() throws Exception {
		Control control = getControl("stringField");
		assertThat(control, is(instanceOf(Text.class)));
		assertThat(((Text) control).getText(), is(equalTo(STRING_FIELD_VALUE)));
	}

	@Test
	public void testStringFieldWithGetterOnly() throws Exception {
		Control control = getControl("stringFieldWithGetterOnly");
		assertThat(control, is(instanceOf(Label.class)));
		assertThat(((Label) control).getText(), is(equalTo(testBean.getStringFieldWithGetterOnly())));
	}

	@Test
	public void testUiReadOnlyStringField() throws Exception {
		Control control = getControl("uiReadOnlyStringField");
		assertThat(control, is(instanceOf(Label.class)));
		assertThat(((Label) control).getText(), is(equalTo(testBean.getUiReadOnlyStringField())));
	}

	@Test
	public void testStringFieldDataBinding() throws Exception {
		String newValue = "New value";
		testBean.setStringField(newValue);
		Control control = getControl("stringField");
		assertThat(((Text) control).getText(), is(equalTo(newValue)));
	}

	private Control getControl(String name) {
		return getControl(metawidget, name);
	}

	private static Control getControl(Composite container, String name) {
		for (Control child : container.getChildren()) {
			if (child.getData(NAME) == null && child instanceof Composite) {
				Control control = getControl((Composite) child, name);
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
