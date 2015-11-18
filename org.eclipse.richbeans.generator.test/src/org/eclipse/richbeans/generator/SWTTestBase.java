package org.eclipse.richbeans.generator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class SWTTestBase {

	protected static Display display;
	protected Shell shell;

	@BeforeClass
	public static void initializeDisplay() throws Exception {
		display = Display.getDefault();
	}

	@Before
	public void initializeShell() throws Exception {
		disposeShell();
		shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		shell.open();
	}

	@After
	public void destroyShell() throws Exception {
		display.syncExec(new Runnable() {
			public void run() {
				disposeShell();
			}
		});
	}

	@AfterClass
	public static void destroyDisplay() throws Exception {
		if (display != null) {
			display.dispose();
			display = null;
		}
	}

	protected void newShell() {
		disposeShell();
		shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		shell.open();
	}

	private void disposeShell() {
		if (shell != null) {
			shell.dispose();
			shell = null;
		}
	}
}
