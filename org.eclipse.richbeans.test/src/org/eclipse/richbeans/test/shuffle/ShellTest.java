package org.eclipse.richbeans.test.shuffle;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.junit.After;
import org.junit.Before;

/**
 * 
 * Test which starts a static system for creating shells from the current display
 * 
 * @author Matthew Gerring
 *
 */
public abstract class ShellTest {


	private final static CyclicBarrier swtBarrier = new CyclicBarrier(2);
	private static volatile Thread uiThread;
	
	private static ReentrantLock     currentTestLock;
	private static ShellTest currentTest;
	static  {
		currentTestLock = new ReentrantLock();
		currentTestLock.lock();
		uiThread = new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println("Starting "+Thread.currentThread().getName());
					while (true) {
						currentTestLock.lock();
						currentTestLock.unlock();
						
						final Display display = Display.getDefault();
						appShell = currentTest.createShell();
						bot = new SWTBot(appShell);
						swtBarrier.await();
						System.out.println(Thread.currentThread().getName()+" entering readAndDespatch for test");
						while (!appShell.isDisposed()) {
							if (!display.readAndDispatch()) {
								display.sleep();
							}
						}
						System.out.println(Thread.currentThread().getName()+" test finished");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "SWTBot UI Thread");
		uiThread.setDaemon(true);
		uiThread.start();
	}

	protected static SWTBot bot;
	private static Shell appShell;

	@Before
	public void setup() throws InterruptedException, BrokenBarrierException {
		currentTest = this;
		currentTestLock.unlock();
		swtBarrier.await();
	}

	@After
	public void teardown() throws InterruptedException {
		currentTestLock.lock();
		currentTest = null;
		Display.getDefault().syncExec(() -> {appShell.close();});
	}

	/**
	 * This method must be overridden by users. It should return the
	 * {@link Shell} to be tested, after being opened and laid out. This class
	 * will take care of running its event loop afterwards, until the test ends:
	 * at this point, this class will close the {@link Shell} automatically.
	 */
	protected abstract Shell createShell() throws Exception;
}