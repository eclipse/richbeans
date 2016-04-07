package org.eclipse.richbeans.generator.example;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.richbeans.generator.GuiGeneratorService;
import org.eclipse.richbeans.generator.RichbeansAnnotationsInspector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;

public class GuiGeneratorRunner {

	private static Display display;
	private static Shell shell;
	private static IGuiGeneratorService guiGenerator;

	public static void main(String[] args) {
		initializeShell();
		initializeGuiGenerator();
		generateGui();
		runEventLoop();
		destroyShell();
	}

	private static void initializeShell() {
		display = Display.getDefault();
		shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		shell.open();
	}

	private static void initializeGuiGenerator() {
		GuiGeneratorService.addDomInspector(new RichbeansAnnotationsInspector());
		GuiGeneratorService.addDomInspector(new MetawidgetAnnotationInspector());
		guiGenerator = new GuiGeneratorService();
	}

	private static void generateGui() {
		TestBean testBean = new TestBean();
		testBean.setStringField("String field value");
		testBean.setUiReadOnlyStringField("UiReadOnly string field value");
		testBean.setIntField(5);

		guiGenerator.generateGui(testBean, shell);
	}

	private static void runEventLoop() {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private static void destroyShell() {
		if (shell != null) {
			shell.dispose();
			shell = null;
		}
		if (display != null) {
			display.dispose();
			display = null;
		}
	}
}
