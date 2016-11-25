package org.eclipse.richbeans.test.shuffle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.richbeans.widgets.shuffle.ShuffleConfiguration;
import org.eclipse.richbeans.widgets.shuffle.ShuffleViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class ShuffleViewerTest extends ShellTest {

	private static ShuffleConfiguration conf;

	@Override
	protected Shell createShell(Display display) {
		
		Shell parent = new Shell(display);
		parent.setText("Test");
		parent.setLayout(new FillLayout());
		
		// Provide some data to shuffle
		conf = new ShuffleConfiguration();
		conf.setFromToolipText("left");
		conf.setToToolipText("right");
		
		// Create the widget
		ShuffleViewer viewer = new ShuffleViewer(conf);
		viewer.createPartControl(parent);
		parent.pack();
		parent.open();
		return parent;
	}
	
	@Test
	public void checkShell() throws Exception {
		assertNotNull(bot.shell("Test"));
	}
	
	@Test
	public void checkButtons() throws Exception {
		assertNotNull(bot.arrowButton(0));
		assertNotNull(bot.arrowButton(1));
	}
	
	@Test
	public void checkButtonsDisabledWhenEmpty() throws Exception {
		assertTrue(!bot.arrowButton(0).isEnabled());
		assertTrue(!bot.arrowButton(1).isEnabled());
	}

	@Test
	public void checkTables() throws Exception {
		assertNotNull(bot.table(0)); // Left
		assertNotNull(bot.table(1)); // Right
		assertTrue(bot.table(0).getToolTipText().equals("left")); // Left
		assertTrue(bot.table(1).getToolTipText().equals("right")); // Right
	}

	@Test
	public void checkAddFromContent() throws Exception {
		
		SWTBotTable table = bot.table(0);
		assertTrue(table.rowCount()==0);
		
		conf.setFromList(Arrays.asList("one", "two", "three"));
		assertEquals(3, table.rowCount());
		assertTrue(bot.arrowButton(0).isEnabled());
		assertTrue(!bot.arrowButton(1).isEnabled());
	}
	
	@Test
	public void checkAddToContent() throws Exception {
		
		SWTBotTable table = bot.table(1);
		assertTrue(table.rowCount()==0);
		
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		assertEquals(4, table.rowCount());
		assertTrue(!bot.arrowButton(0).isEnabled());
		assertTrue(bot.arrowButton(1).isEnabled());
	}

	@Test
	public void checkMoveOneRight() throws Exception {
		
		assertTrue(!bot.arrowButton(0).isEnabled());
		assertTrue(!bot.arrowButton(1).isEnabled());
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		assertTrue(bot.arrowButton(0).isEnabled());
		assertTrue(bot.arrowButton(1).isEnabled());
		
		SWTBotTable table = bot.table(0);
		table.click(0, 0);

		bot.arrowButton(0).click();
		
		table = bot.table(1);
		assertEquals(5, table.rowCount());
		assertTrue(bot.arrowButton(0).isEnabled());
		assertTrue(bot.arrowButton(1).isEnabled());

		// We check that one is selected
		assertEquals("one", table.getTableItem(4).getText());
		assertEquals(1, table.selectionCount());
		// TODO How to check items selected
	}
	
	@Test
	public void checkMoveOneRightNoSelection() throws Exception {
		
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		assertTrue(bot.arrowButton(0).isEnabled());
		assertTrue(bot.arrowButton(1).isEnabled());
		
		SWTBotTable table = bot.table(0);

		bot.arrowButton(0).click();
		
		table = bot.table(1);
		assertEquals(5, table.rowCount());

	}


	@Test
	public void checkMoveOneLeftNoSelection() throws Exception {
		
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		assertTrue(bot.arrowButton(0).isEnabled());
		assertTrue(bot.arrowButton(1).isEnabled());
		
		bot.table(0).click(2, 0);
		bot.table(1).click(0, 0);
		SWTBotTable table = bot.table(1);
		bot.arrowButton(1).click();
		
		table = bot.table(0);
		assertEquals(4, table.rowCount());
		assertEquals("four", table.getTableItem(3).getText());
		assertEquals(1, table.selectionCount());
		// TODO How to check items selected

	}

	@Test
	public void checkMoveThreeRight() throws Exception {
		
		assertTrue(!bot.arrowButton(0).isEnabled());
		assertTrue(!bot.arrowButton(1).isEnabled());
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		assertTrue(bot.arrowButton(0).isEnabled());
		assertTrue(bot.arrowButton(1).isEnabled());
		
		SWTBotTable table = bot.table(0);
		bot.arrowButton(0).click();
		bot.arrowButton(0).click();
		bot.arrowButton(0).click();
		
		table = bot.table(1);
		assertEquals(7, table.rowCount());
		assertTrue(!bot.arrowButton(0).isEnabled());
		assertTrue(bot.arrowButton(1).isEnabled());
		
		table = bot.table(0);
		assertEquals(0, table.rowCount());
	}
	
	@Test
	public void checkMoveFourLeft() throws Exception {
		
		assertTrue(!bot.arrowButton(0).isEnabled());
		assertTrue(!bot.arrowButton(1).isEnabled());
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		assertTrue(bot.arrowButton(0).isEnabled());
		assertTrue(bot.arrowButton(1).isEnabled());
		
		SWTBotTable table = bot.table(1);
		bot.arrowButton(1).click();
		bot.arrowButton(1).click();
		bot.arrowButton(1).click();
		bot.arrowButton(1).click();
		
		table = bot.table(0);
		assertEquals(7, table.rowCount());
		assertTrue(bot.arrowButton(0).isEnabled());
		assertTrue(!bot.arrowButton(1).isEnabled());
		
		table = bot.table(1);
		assertEquals(0, table.rowCount());
	}

	@Test
	public void checkMoveThreeRightCheckOrder() throws Exception {
		
		assertTrue(!bot.arrowButton(0).isEnabled());
		assertTrue(!bot.arrowButton(1).isEnabled());
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		assertTrue(bot.arrowButton(0).isEnabled());
		assertTrue(bot.arrowButton(1).isEnabled());
		
		SWTBotTable table = bot.table(1);
		table.click(0, 0); 
		bot.arrowButton(0).click(); // Add one after four
		
		table.click(2, 0); 
		bot.arrowButton(0).click();// Add two after five
		
		table.click(4, 0); 
		bot.arrowButton(0).click();// Add three after six
		
		assertEquals(7, table.rowCount());
		assertTrue(!bot.arrowButton(0).isEnabled());
		assertTrue(bot.arrowButton(1).isEnabled());
		
		List<String> values = Arrays.asList("four", "one", "five", "two", "six", "three", "seven");
		for (int i = 0; i < values.size(); i++) {
			assertEquals(values.get(i), table.getTableItem(i).getText(0));
		}
	}

}
