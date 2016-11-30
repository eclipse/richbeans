package org.eclipse.richbeans.test.shuffle;


import java.util.concurrent.BrokenBarrierException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * 
 * Test which starts a static system for creating shells from the current display
 * 
 * @author Matthew Gerring
 *
 */
public abstract class ShellTest {

	private static TestUI testUI;
	
	@BeforeClass
	public static void startUI() {
		testUI = new TestUI();
		testUI.start();
	}
	
	@AfterClass
	public static void stopUI() {
		testUI.stop();
	}

	@Before
	public void setup() throws InterruptedException, BrokenBarrierException {
		testUI.createBot(this);
	}

	@After
	public void teardown() throws InterruptedException {
		testUI.disposeBot(this);
	}

	/**
	 * Override to create the shell. The calling thread is the thread used for the UI
	 * in this test. Called once per test class.
	 */
	protected abstract Shell createShell(Display display) throws Exception;

	
	protected SWTBot bot;
	public SWTBot getBot() {
		return bot;
	}

	public void setBot(SWTBot bot) {
		this.bot = bot;
	}

}