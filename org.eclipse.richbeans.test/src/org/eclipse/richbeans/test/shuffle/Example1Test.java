package org.eclipse.richbeans.test.shuffle;

import static org.junit.Assert.assertNotNull;

import org.eclipse.richbeans.examples.ExampleFactory;
import org.eclipse.richbeans.examples.IExampleRunner;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

/**
 * 
 * Basic test running all the examples.
 * 
 * @author Matthew Gerring
 *
 */
public class Example1Test extends IsolatedShellTest {

	@Test
	public void testUI() throws Exception {
		assertNotNull(bot.shell("Change a value to see bean as JSON"));
	}

	@Override
	protected Shell createShell() throws Exception {
		IExampleRunner runner = ExampleFactory.createExample1();
		return runner.createShell();
	}
	
}
