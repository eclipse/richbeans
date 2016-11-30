package org.eclipse.richbeans.test.shuffle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.richbeans.examples.ExampleFactory;
import org.eclipse.richbeans.examples.IShellCreator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
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
		assertEquals("Edge", bot.label(1).getText()); // Edge
		assertEquals("Start", bot.label(2).getText()); // Start
		assertEquals("Stop", bot.label(3).getText()); // Stop
		assertEquals("Name", bot.label(4).getText()); // Name
		assertEquals("x", bot.label(5).getText()); // x
		assertEquals("y", bot.label(6).getText()); // y
	}
	
	@Test
	public void testButtons() throws Exception {
		assertNotNull(bot.button(0)); // Add
		assertNotNull(bot.button(1)); // Delete
		assertEquals("Add", bot.button(0).getText()); // Add
		assertEquals("Delete", bot.button(1).getText()); // Delete
		assertNotNull(bot.arrowButton(0)); // Up
		assertNotNull(bot.arrowButton(1)); // Down
	}


	@Test
	public void testBoxInitialValues() throws Exception {
		
		SWTBotStyledText element = bot.styledText(0);
		assertEquals("Fe", element.getText());
		
		SWTBotStyledText edge = bot.styledText(1); 
		assertEquals("K", edge.getText());

		SWTBotStyledText start = bot.styledText(2); 
		assertEquals("100.00", start.getText());
		
		SWTBotStyledText stop = bot.styledText(3); 
		assertEquals("200.00", stop.getText());
		
		checkComposite("Fred 1", "1.00 ", "2.00 m");
	}
	
	@Test
	public void testAddRemove() throws Exception {
		
		checkComposite("Fred 1", "1.00 ", "2.00 m");

		bot.button(0).click(); // add
		
		checkComposite("Fred 2", "1.00 ", "1.00 m");

		bot.button(1).click(); // remove

		checkComposite("Fred 1", "1.00 ", "2.00 m");
	}
	
	@Test
	public void testRemove() throws Exception {
		
		checkComposite("Fred 1", "1.00 ", "2.00 m");

		bot.button(1).click(); // remove
		
		assertTrue(bot.table(0).rowCount()<1);

	}

	@Test
	public void addMany() throws Exception {
		
		checkComposite("Fred 1", "1.00 ", "2.00 m");

		for (int i = 0; i < 9; i++) bot.button(0).click(); // add
		
		assertFalse(bot.button(0).isEnabled());
		assertEquals(10, bot.table(0).rowCount());

		for (int i = 0; i < 9; i++) bot.button(1).click(); // delete

		assertTrue(bot.button(0).isEnabled());
		assertEquals(1, bot.table(0).rowCount());
	}
	
	private void checkComposite(String tname, String tx, String ty) {
		SWTBotStyledText name = bot.styledText(4); 
		assertEquals(tname, name.getText());

		SWTBotStyledText x = bot.styledText(5); 
		assertTrue(x.getText().startsWith(tx));

		SWTBotStyledText y = bot.styledText(6); 
		assertEquals(ty, y.getText());
	}

}
