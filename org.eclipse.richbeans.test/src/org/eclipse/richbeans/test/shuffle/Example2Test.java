package org.eclipse.richbeans.test.shuffle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.richbeans.examples.ExampleFactory;
import org.eclipse.richbeans.examples.IShellCreator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.junit.Test;

/**
 * 
 * Basic test running all the examples.
 * 
 * @author Matthew Gerring
 *
 */
public class Example2Test extends ShellTest {

	@Override
	protected Shell createShell(Display display) throws Exception {
		IShellCreator runner = ExampleFactory.createExample2();
		Shell shell = runner.createShell(display);
		shell.pack();
		shell.open();
		return shell;
	}
	
	@Test
	public void testShell() throws Exception {
		assertNotNull(bot.shell("Change a value to see bean as JSON"));
	}
	
	@Test
	public void testBoxes() throws Exception {
		assertNotNull(bot.styledText(0)); // Element
		assertNotNull(bot.styledText(1)); // Edge
		assertNotNull(bot.styledText(2)); // Start
		assertNotNull(bot.styledText(3)); // Stop
		assertNotNull(bot.styledText(4)); // Name
		assertNotNull(bot.styledText(5)); // x
		assertNotNull(bot.styledText(6)); // y
	}
	
	@Test
	public void testLabels() throws Exception {
		assertNotNull(bot.label(0)); // Element
		assertNotNull(bot.label(1)); // Edge
		assertNotNull(bot.label(2)); // Start
		assertNotNull(bot.label(3)); // Stop
		assertNotNull(bot.label(4)); // Name
		assertNotNull(bot.label(5)); // x
		assertNotNull(bot.label(6)); // y
	}

	@Test
	public void testLabelText() throws Exception {
		assertEquals("Element", bot.label(0).getText()); // Element
		assertEquals("Edge", bot.label(1)); // Edge
		assertEquals("Start", bot.label(2)); // Start
		assertEquals("Stop", bot.label(3)); // Stop
		assertEquals("Element", bot.label(4)); // Name
		assertEquals("Element", bot.label(5)); // x
		assertEquals("Element", bot.label(6)); // y
	}

	@Test
	public void testBoxInitialValues() throws Exception {
		
//		SWTBotStyledText x = bot.styledText(0); // x
//		assertTrue(x.getText().startsWith("10.00 "));
//
//		SWTBotStyledText y = bot.styledText(1); // y
//		assertEquals("5 m", y.getText());
	}

}
