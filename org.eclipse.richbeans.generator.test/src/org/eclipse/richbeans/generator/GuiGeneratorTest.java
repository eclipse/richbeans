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

	private IGuiGeneratorService guiGenerator;
	private TestBean testBean;
	private Composite metawidget;

	@Before
	public void setUp() throws Exception {
		guiGenerator = new GuiGeneratorService();
		testBean = new TestBean();
		metawidget = (Composite) guiGenerator.generateGui(testBean, shell);
	}

	@After
	public void tearDown() throws Exception {
		guiGenerator = null;
	}

	@Test
	public void testStringField() throws Exception {
		Control stringField = getControl(metawidget, "stringField");
		assertThat(stringField, is(instanceOf(Text.class)));
		assertThat(((Text) stringField).getText(), is(equalTo("String field")));
	}

	@Test
	public void testStringFieldWithGetterOnly() throws Exception {
		Control stringField = getControl(metawidget, "stringFieldWithGetterOnly");
		assertThat(stringField, is(instanceOf(Label.class)));
		assertThat(((Label) stringField).getText(), is(equalTo("String field with getter only")));
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

class TestBean {

	private String stringField = "String field";
	private String stringFieldWithGetterOnly = "String field with getter only";

	public String getStringField() {
		return stringField;
	}
	public void setStringField(String stringField) {
		this.stringField = stringField;
	}
	public String getStringFieldWithGetterOnly() {
		return stringFieldWithGetterOnly;
	}
}
