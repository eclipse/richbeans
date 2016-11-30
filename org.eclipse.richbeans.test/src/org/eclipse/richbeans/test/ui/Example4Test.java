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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.richbeans.examples.ExampleFactory;
import org.eclipse.richbeans.examples.IShellCreator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.junit.Test;

/**
 * 
 * Basic test running all the examples.
 * 
 * @author Matthew Gerring
 *
 */
public class Example4Test extends ShellTest {

	@Override
	protected Shell createShell(Display display) throws Exception {
		IShellCreator runner = ExampleFactory.createExample4();
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
		assertNotNull(bot.styledText(0)); // x
		assertNotNull(bot.styledText(1)); // y
	}
	
	@Test
	public void testButtons() throws Exception {
		assertNotNull(bot.button(0)); // Add
		assertNotNull(bot.button(1)); // Delete
		assertEquals("Add", bot.button(0).getText()); // Add
		assertEquals("Delete", bot.button(1).getText()); // Delete
		assertNotNull(bot.arrowButton(0)); // Up
		assertNotNull(bot.arrowButton(1)); // Down

		assertNotNull(bot.button(2)); // Add
		assertNotNull(bot.button(3)); // Delete
		assertEquals("Add", bot.button(2).getText()); // Add
		assertEquals("Delete", bot.button(3).getText()); // Delete
		assertNotNull(bot.arrowButton(2)); // Up
		assertNotNull(bot.arrowButton(3)); // Down
	}
	
	@Test
	public void testTables() throws Exception {
		assertEquals(3, bot.table(0).rowCount());
		assertEquals(1, bot.table(1).rowCount());
		bot.table(0).click(1, 0);
		assertEquals(2, bot.table(1).rowCount());
		bot.table(0).click(2, 0);
		assertEquals(0, bot.table(1).rowCount());
	}

	@Test
	public void testInitialValues() throws Exception {
		assertEquals(3, bot.table(0).rowCount());
		
		bot.table(0).click(0, 0);
		checkComposite("Item 1", "X-Y Graph", "1.00 ", "2.00 m", new String[]{"Option 1"}, new boolean[]{true,true,true,true});
		
		bot.table(0).click(1, 0);
		checkComposite("Item 2", "Polar", "2.00 ", "3.00 rad", new String[]{"a","b"}, new boolean[]{true,false,true,false});
		
		bot.table(0).click(2, 0);
		checkComposite("Item 3", "X-Y Graph", "3.00 m", "4.00 m", new String[]{}, new boolean[]{});
	}
	
	@Test
	public void testAddRemove() throws Exception {
		
		assertEquals(3, bot.table(0).rowCount());
		
		bot.table(0).click(0, 0);
		checkComposite("Item 1", "X-Y Graph", "1.00 m", "2.00 m", new String[]{"Option 1"}, new boolean[]{true,true,true,true});
		
		bot.button(0).click(); // Add
		checkComposite("Item 4", "X-Y Graph", "1.00 m", "1.00 m", new String[]{}, new boolean[]{});
		
		bot.button(2).click();// Add
		checkComposite("Item 4", "X-Y Graph", "1.00 m", "1.00 m", new String[]{"Option 1"}, new boolean[]{false, false, false, false});
	    bot.checkBox(0).click(); // check
	    bot.checkBox(1).click(); // check
		checkComposite("Item 4", "X-Y Graph", "1.00 m", "1.00 m", new String[]{"Option 1"}, new boolean[]{true, true, false, false});
		
	}


	private void checkComposite(String tname, String cvalue, String tx, String ty, String[] subnames, boolean[] firstBooleans) {
		
		SWTBotStyledText name = bot.styledText(0); 
		assertEquals(tname, name.getText());
		
		SWTBotCombo combo = bot.comboBox(0);
		assertEquals(cvalue, combo.getText());

		SWTBotStyledText x = bot.styledText(1); 
		assertTrue(x.getText().startsWith(tx));

		SWTBotStyledText y = bot.styledText(2); 
		assertEquals(ty, y.getText());
		
		assertEquals(subnames.length, bot.table(1).rowCount());
		for (int i = 0; i < subnames.length; i++) {
			assertEquals(subnames[i], bot.table(1).cell(i, 0));
		}
		
		for (int i = 0; i < firstBooleans.length; i++) {
			assertEquals(firstBooleans[i], bot.checkBox(i).isChecked());
		}

	}

}
