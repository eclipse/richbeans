package org.eclipse.richbeans.generator;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.swt.widgets.Composite;

public class GuiGeneratorRunner extends SWTTestBase {

	private IGuiGeneratorService guiGenerator;
	private TestBean testBean;

	@SuppressWarnings("unused")
	private Composite metawidget;

	public static void main(String[] args) throws Exception {
		initializeDisplay();
		GuiGeneratorRunner runner = new GuiGeneratorRunner();
		runner.initializeShell();
		runner.setUp();
		runner.runEventLoop();
		runner.tearDown();
		runner.destroyShell();
		destroyDisplay();
	}

	public void setUp() {
		testBean = new TestBean();
		testBean.setStringField("String field value");
		testBean.setUiReadOnlyStringField("UiReadOnly string field value");
		testBean.setIntField(5);

		guiGenerator = new GuiGeneratorService();
		metawidget = (Composite) guiGenerator.generateGui(testBean, shell);
	}

	public void runEventLoop() throws Exception {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}

	public void tearDown() throws Exception {
		guiGenerator = null;
	}
}
