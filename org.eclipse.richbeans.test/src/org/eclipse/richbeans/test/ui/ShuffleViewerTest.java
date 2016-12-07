/*-
 *******************************************************************************
 * Copyright (c) 2011, 2016 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.richbeans.test.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.richbeans.widgets.shuffle.IShuffleListener;
import org.eclipse.richbeans.widgets.shuffle.ShuffleConfiguration;
import org.eclipse.richbeans.widgets.shuffle.ShuffleDirection;
import org.eclipse.richbeans.widgets.shuffle.ShuffleEvent;
import org.eclipse.richbeans.widgets.shuffle.ShuffleViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class ShuffleViewerTest extends ShellTest {

	private static ShuffleConfiguration<String> conf;
	private ShuffleViewer<String> viewer;

	@Override
	protected Shell createShell(Display display) {
		
		Shell parent = new Shell(display);
		parent.setText("Test");
		parent.setLayout(new FillLayout());
		
		// Provide some data to shuffle
		conf = new ShuffleConfiguration<String>();
		conf.setFromToolipText("left");
		conf.setToToolipText("right");
		conf.setFromLabel("Available Experiments");
		conf.setToToolipText("right");
		conf.setToLabel("Submission List");
		conf.setFromReorder(true);
		conf.setToReorder(true);
		
		// Create the widget
		this.viewer = new ShuffleViewer<String>(conf);
		viewer.createPartControl(parent);
		parent.pack();
		parent.open();
		return parent;
	}
	
	private IShuffleListener       listener;
	private List<ShuffleDirection> directions = new ArrayList<>(7);
	
	@Before
	public void addListener() {
		directions.clear();
		listener = new IShuffleListener() {				
			@Override
			public void postShuffle(ShuffleEvent evt) {
				directions.add(evt.getDirection());
			}
		};
		viewer.addShuffleListener(listener);
	}
	@After
	public void removeListener() {
		directions.clear();
		viewer.removeShuffleListener(listener);
	}
	
	
	@Test
	public void checkShell() throws Exception {
		assertNotNull(bot.shell("Test"));
	}
	
	@Test
	public void checkButtons() throws Exception {
		assertNotNull(bot.arrowButton(0)); // Down
		assertNotNull(bot.arrowButton(1)); // Up
		assertNotNull(bot.arrowButton(2)); // Right
		assertNotNull(bot.arrowButton(3)); // Left
		assertNotNull(bot.arrowButton(4)); // Down
		assertNotNull(bot.arrowButton(5)); // Up
	}
	
	@Test
	public void checkButtonsDisabledWhenEmpty() throws Exception {
		assertFalse(bot.arrowButton(0).isEnabled());
		assertFalse(bot.arrowButton(1).isEnabled());
		assertFalse(bot.arrowButton(2).isEnabled());
		assertFalse(bot.arrowButton(3).isEnabled());
		assertFalse(bot.arrowButton(4).isEnabled());
		assertFalse(bot.arrowButton(5).isEnabled());
	}

	@Test
	public void checkLabels() throws Exception {
		assertNotNull(bot.label(0)); // Left
		assertNotNull(bot.label(1)); // Right
		assertTrue(bot.label(0).getText().equals("Available Experiments")); // Left
		assertTrue(bot.label(1).getText().equals("Submission List")); // Right
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
		assertTrue(bot.arrowButton(2).isEnabled());
		assertFalse(bot.arrowButton(3).isEnabled());
	}
	
	@Test
	public void checkAddToContent() throws Exception {
		
		SWTBotTable table = bot.table(1);
		assertTrue(table.rowCount()==0);
		
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		assertEquals(4, table.rowCount());
		assertFalse(bot.arrowButton(2).isEnabled());
		assertTrue(bot.arrowButton(3).isEnabled());
	}

	@Test
	public void checkMoveOneRight() throws Exception {
		
		assertFalse(bot.arrowButton(2).isEnabled());
		assertFalse(bot.arrowButton(3).isEnabled());
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		assertTrue(bot.arrowButton(2).isEnabled());
		assertTrue(bot.arrowButton(3).isEnabled());
		
		SWTBotTable table = bot.table(0);
		table.click(0, 0);

		bot.arrowButton(2).click();
		
		table = bot.table(1);
		assertEquals(5, table.rowCount());
		assertTrue(bot.arrowButton(2).isEnabled());
		assertTrue(bot.arrowButton(3).isEnabled());

		// We check that one is selected
		assertEquals("one", table.getTableItem(4).getText());
		assertEquals(1, table.selectionCount());
		// TODO How to check items selected
	}
	
	@Test
	public void checkMoveOneRightNoSelection() throws Exception {
		
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		assertTrue(bot.arrowButton(2).isEnabled());
		assertTrue(bot.arrowButton(3).isEnabled());
		
		SWTBotTable table = bot.table(0);

		bot.arrowButton(2).click();
		
		table = bot.table(1);
		assertEquals(5, table.rowCount());

		assertEquals(2, directions.size());
		assertEquals(directions.get(1), ShuffleDirection.LEFT_TO_RIGHT);
	}

	@Test
	public void checkMoveOneLeftNoSelection() throws Exception {
		
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		assertTrue(bot.arrowButton(2).isEnabled());
		assertTrue(bot.arrowButton(3).isEnabled());
		
		bot.table(0).click(2, 0);
		bot.table(1).click(0, 0);
		SWTBotTable table = bot.table(1);
		bot.arrowButton(3).click();
		
		table = bot.table(0);
		assertEquals(4, table.rowCount());
		assertEquals("four", table.getTableItem(3).getText());
		assertEquals(1, table.selectionCount());
		// TODO How to check items selected
		
		assertEquals(2, directions.size());
		assertEquals(directions.get(0), ShuffleDirection.DELETE);
		assertEquals(directions.get(1), ShuffleDirection.RIGHT_TO_LEFT);
	}

	@Test
	public void checkMoveThreeRight() throws Exception {
		
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		
		SWTBotTable table = bot.table(0);
		bot.arrowButton(2).click();
		bot.arrowButton(2).click();
		bot.arrowButton(2).click();
		
		table = bot.table(1);
		assertEquals(7, table.rowCount());
		assertFalse(bot.arrowButton(2).isEnabled());
		assertTrue(bot.arrowButton(3).isEnabled());
		
		table = bot.table(0);
		assertEquals(0, table.rowCount());
	}
	
	@Test
	public void checkMoveFourLeft() throws Exception {
		
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		
		SWTBotTable table = bot.table(1);
		bot.arrowButton(3).click();
		bot.arrowButton(3).click();
		bot.arrowButton(3).click();
		bot.arrowButton(3).click();
		
		table = bot.table(0);
		assertEquals(7, table.rowCount());
		assertTrue(bot.arrowButton(2).isEnabled());
		assertFalse(bot.arrowButton(3).isEnabled());
		
		table = bot.table(1);
		assertEquals(0, table.rowCount());
	}

	@Test
	public void checkMoveThreeRightCheckOrder() throws Exception {
		
		conf.setFromList(Arrays.asList("one", "two", "three"));
		conf.setToList(Arrays.asList("four", "five", "six", "seven"));
		
		SWTBotTable table = bot.table(1);
		table.click(0, 0); 
		bot.arrowButton(2).click(); // Add one after four
		
		table.click(2, 0); 
		bot.arrowButton(2).click();// Add two after five
		
		table.click(4, 0); 
		bot.arrowButton(2).click();// Add three after six
		
		assertEquals(7, table.rowCount());
		assertFalse(bot.arrowButton(2).isEnabled());
		assertTrue(bot.arrowButton(3).isEnabled());
		
		List<String> values = Arrays.asList("four", "one", "five", "two", "six", "three", "seven");
		for (int i = 0; i < values.size(); i++) {
			assertEquals(values.get(i), table.getTableItem(i).getText(0));
		}
	}

	
	@Test
	public void checkReorderInsideRight() throws Exception {

		assertFalse(bot.arrowButton(4).isEnabled());
		assertFalse(bot.arrowButton(5).isEnabled());
		conf.setToList(Arrays.asList("one", "two", "three", "four", "five", "six", "seven"));		
		assertTrue(bot.arrowButton(4).isEnabled());
		assertTrue(bot.arrowButton(5).isEnabled());
        
		SWTBotTable table = bot.table(1);
		table.click(3, 0); // Select "four"
		
		bot.arrowButton(5).click(); // Up
		bot.arrowButton(5).click(); // Up
		bot.arrowButton(5).click(); // Up


		List<String> values = Arrays.asList("four", "one", "two", "three", "five", "six", "seven");
		for (int i = 0; i < values.size(); i++) {
			assertEquals(values.get(i), table.getTableItem(i).getText(0));
		}

		bot.arrowButton(4).click(); // Down
		bot.arrowButton(4).click(); // Down
		bot.arrowButton(4).click(); // Down
		
		values = Arrays.asList("one", "two", "three", "four", "five", "six", "seven");
		for (int i = 0; i < values.size(); i++) {
			assertEquals(values.get(i), table.getTableItem(i).getText(0));
		}
	}
	
	@Test
	public void checkReorderUpDownExtents() throws Exception {

		assertFalse(bot.arrowButton(4).isEnabled());
		assertFalse(bot.arrowButton(5).isEnabled());
		conf.setToList(Arrays.asList("one", "two", "three", "four", "five", "six", "seven"));		
		assertTrue(bot.arrowButton(4).isEnabled());
		assertTrue(bot.arrowButton(5).isEnabled());
        
		SWTBotTable table = bot.table(1);
		table.click(6, 0); // Select "seven"
		
		// Go off the top
		for (int i = 0; i < 10; i++) bot.arrowButton(5).click(); // Up

		assertEquals("seven", table.getTableItem(0).getText(0));

		// Go off the Bottom
		for (int i = 0; i < 10; i++) bot.arrowButton(4).click(); // Down
		
		// Go off the Bottom
		assertEquals("seven", table.getTableItem(6).getText(0));
	}

}
