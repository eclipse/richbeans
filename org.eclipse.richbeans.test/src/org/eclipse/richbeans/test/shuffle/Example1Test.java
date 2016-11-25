package org.eclipse.richbeans.test.shuffle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
public class Example1Test extends ShellTest {

	@Override
	protected Shell createShell(Display display) throws Exception {
		IShellCreator runner = ExampleFactory.createExample1();
		Shell shell = runner.createShell(display);
		shell.pack();
		shell.open();
		return shell;
	}
	
	@Test
	public void testUI() throws Exception {
		assertNotNull(bot.shell("Change a value to see bean as JSON"));
	}
	
	@Test
	public void testBoxes() throws Exception {
		assertNotNull(bot.styledText(0)); // x
	}

	@Test
	public void testBoxInitialValues() throws Exception {
		
		SWTBotStyledText x = bot.styledText(0); // x
		assertEquals("10.00 °", x.getText());

		SWTBotStyledText y = bot.styledText(1); // y
		assertEquals("5 m", y.getText());
	}

	@Test
	public void testChangeValue() throws Exception {
		
		SWTBotStyledText x = bot.styledText(0); // x
		x.setText("11");
		assertEquals("11 °", x.getText());

		SWTBotStyledText y = bot.styledText(1); // x
		y.setText("6.5");
		assertEquals("6.5 m", y.getText()); // Because programatic does not round 7 TODO Fix this?

		SWTBotLabel json = bot.label(2);
		assertEquals("SimpleBean [x=11.0, y=7]", json.getText()); // Rounding should be active now
		
	}

}
