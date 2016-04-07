package org.eclipse.richbeans.generator.example;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.richbeans.generator.GuiGeneratorService;
import org.eclipse.richbeans.generator.RichbeansAnnotationsInspector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;

public abstract class GuiGeneratorRunnerBase {

	private Display display;
	private Shell shell;
	private IGuiGeneratorService guiGenerator;

	protected final void run() {
		initializeShell();
		initializeGuiGenerator();

		Object testObject = createTestObject();
		guiGenerator.generateGui(testObject, shell);

		resizeAndDisplayShell();
		runEventLoop();
		destroyShell();
	}

	protected abstract Object createTestObject();

	private void initializeShell() {
		display = Display.getDefault();
		shell = new Shell(display, SWT.SHELL_TRIM);
		FillLayout layout = new FillLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		shell.setLayout(layout);
	}

	private void initializeGuiGenerator() {
		GuiGeneratorService.addDomInspector(new RichbeansAnnotationsInspector());
		GuiGeneratorService.addDomInspector(new MetawidgetAnnotationInspector());
		guiGenerator = new GuiGeneratorService();
	}

	private void resizeAndDisplayShell() {
		shell.pack();
		int xSize = Math.max(shell.getSize().x, 300);
		int ySize = Math.max(shell.getSize().y, 300);
		shell.setSize(xSize, ySize);

		Rectangle displaySize = display.getPrimaryMonitor().getBounds();
		int xLocation = displaySize.x + (displaySize.width - xSize) / 2;
		int yLocation = displaySize.y + (displaySize.height - ySize) / 2;
		shell.setLocation(xLocation, yLocation);

		shell.open();
	}

	private void runEventLoop() {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void destroyShell() {
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
