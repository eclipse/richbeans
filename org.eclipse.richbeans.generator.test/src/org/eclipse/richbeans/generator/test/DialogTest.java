package org.eclipse.richbeans.generator.test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.metawidget.inspector.InspectionResultConstants.NAME;

import java.util.concurrent.CountDownLatch;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.richbeans.generator.GuiGeneratorService;
import org.eclipse.richbeans.generator.RichbeansAnnotationsInspector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;

public class DialogTest {

	protected static Display display;

	@BeforeClass
	public static void initializeStatics() throws Exception {
		initializeDisplay();
		initialiseGuiGeneratorService();
	}

	private static void initializeDisplay() throws Exception {
		final CountDownLatch displayInitializedLatch = new CountDownLatch(1);
		new Thread(() -> {
			display = Display.getDefault();
			displayInitializedLatch.countDown();
			while (!display.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}).start();
		displayInitializedLatch.await();
	}

	private static void initialiseGuiGeneratorService() {
		GuiGeneratorService.addDomInspector(new RichbeansAnnotationsInspector());
		GuiGeneratorService.addDomInspector(new MetawidgetAnnotationInspector());
	}

	@AfterClass
	public static void destroyDisplay() throws Exception {
		if (display != null) {
			display.syncExec(() -> display.dispose());
		}
	}

	protected IGuiGeneratorService guiGenerator;
	protected Composite metawidget;
	private CountDownLatch dialogClosedLatch;
	private Button okButton;

	private TestBean testBean;

	@Before
	public void setUp() throws Exception {
		guiGenerator = new GuiGeneratorService();

		testBean = new TestBean();
		testBean.setStringField("String field value");
		testBean.setUiReadOnlyStringField("UiReadOnly string field value");
		testBean.setIntField(5);

		dialogClosedLatch = new CountDownLatch(1);

		// The details are important here! If the dialog is opened with syncExec(), the call will only return when the
		// user closes the dialog (which might be impossible in headless tests). So, we open the dialog with
		// asyncExec(), which somehow works and doesn't block the UI thread.
		display.asyncExec(() -> {
			guiGenerator.openDialog(testBean, null, "Test dialog");
			dialogClosedLatch.countDown();
		});
		// Then, we get the metawidget from the dialog. We use syncExec() to ensure that the metawidget field is
		// properly initialised before the test method is called.
		display.syncExec(() -> {
			// Get the dialog shell
			// (We need to get all shells, confirm there is only one and then use it, because display.getActiveShell()
			// returns null when the tests are run via ant in headless mode.)
			assertThat(display.getShells().length, is(equalTo(1)));
			Shell dialog = display.getShells()[0];

			// There is one child Composite in the shell. The first of its children should be the generated metawidget.
			assertThat(dialog.getChildren().length, is(equalTo(1)));
			Control[] windowContents = ((Composite) dialog.getChildren()[0]).getChildren();
			metawidget = (Composite) windowContents[0];
			// Confirm that the composite is the Metawidget before we try and run any tests
			assertEquals("org.metawidget.swt.SwtMetawidget", metawidget.getClass().getName());

			// Get the OK button, confirm its identity and keep a reference to it.
			Composite buttonComposite = (Composite) windowContents[1];
			Control[] buttons = buttonComposite.getChildren();
			assertThat(buttons[0], is(instanceOf(Button.class)));
			okButton = (Button) buttons[0];
			assertThat(okButton.getText(), is(equalTo("OK")));
		});
	}

	@After
	public void tearDown() throws Exception {
		// Programmatically simulate pressing the OK button to close the dialog, by sending a selection event.
		display.syncExec(() -> {
			okButton.notifyListeners(SWT.Selection, new Event());
		});
		// Wait for the dialog to close (if this fails, any subsequent tests will probably fail during setup)
		dialogClosedLatch.await(2, SECONDS);

		testBean = null;
		metawidget = null;
		guiGenerator = null;
	}

	// Carry out a few of the tests from GuiGeneratorTest, wrapped in syncExec() calls to ensure JUnit waits for the
	// test code to finish before calling tearDown()

	@Test
	public void testStringFieldIsText() throws Exception {
		// Important to use syncExec() here
		display.syncExec(() -> {
			Control control = getNamedControl("stringField");
			assertThat(control, is(instanceOf(Text.class)));
		});
	}

	@Test
	public void testDoubleFieldInitalValue() throws Exception {
		display.syncExec(() -> {
			Control control = getNamedControl("doubleField");
			assertThat(((Text) control).getText(), is(equalTo(String.valueOf(testBean.getDoubleField()))));
		});
	}

	@Test
	public void testDoubleFieldDataBinding() throws Exception {
		display.syncExec(() -> {
			Text control = (Text) getNamedControl("doubleField");
			// Change the value in the GUI box
			control.setText("655.4");
			// Check the bean is updated
			assertEquals("doubleField not updated", 655.4, testBean.getDoubleField(), Double.MIN_VALUE);
		});
	}

	protected Control getNamedControl(String name) {
		return getNamedControl(metawidget, name);
	}

	/**
	 * Get a named control from a composite. (A control is regarded as named if getData("name") returns a string.)
	 * <p>
	 * Nested Metawidgets are named like other controls, so to find a particular nested field correctly, this method
	 * can be called repeatedly to find each Metawidget in the tree in turn until the required one is found.
	 * <p>
	 * <em>Unlike</em> the getControl() method in SwtMetawidget, this method will not recurse into any Composite
	 * children of the given Composite, to avoid issues around searching for non-unique field names in nested objects.
	 *
	 * @param container a composite to search
	 * @param name the name to search for
	 * @return the named control, or <code>null</code> if not found
	 */
	protected static Control getNamedControl(Composite container, String name) {
		for (Control child : container.getChildren()) {
			if (name.equals(child.getData(NAME))) {
				return child;
			}
		}
		return null; // not found
	}
}
