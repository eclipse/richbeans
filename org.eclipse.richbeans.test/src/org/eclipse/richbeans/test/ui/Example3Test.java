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
public class Example3Test extends ShellTest {

	@Override
	protected Shell createShell(Display display) throws Exception {
		IShellCreator runner = ExampleFactory.createExample3();
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
	}

	@Test
	public void testBoxInitialValues() throws Exception {
		
		checkComposite("Item 1", "X-Y Graph", "1.00 ", "2.00 m");
	}
	
	@Test
	public void testTableClick() throws Exception {
		
		checkComposite("Item 1", "X-Y Graph", "1.00 ", "2.00 m");
        bot.table(0).click(1, 0);
		checkComposite("Item 2", "Polar", "2.00 ", "3.00 rad");
        bot.table(0).click(0, 0);
		checkComposite("Item 1", "X-Y Graph", "1.00 ", "2.00 m");
	}
	
	@Test
	public void test100() throws Exception {
		
		checkComposite("Item 1", "X-Y Graph", "1.00 ", "2.00 m");
        for (int i = 0; i < 100; i++) {
        	bot.button(0).click(); // Add 100. Because rich beans reuses widgets, widget count is not an issue.
        }
        bot.table(0).click(101, 0); // We added 100 between 1 and 2
		checkComposite("Item 2", "Polar", "2.00 ", "3.00 rad");
		assertEquals(102, bot.table(0).rowCount());
		
        bot.table(0).click(0, 0);
        for (int i = 0; i < 100; i++) bot.arrowButton(1); // Down

		
		bot.table(0).click(0, 0); // We added 100 between 1 and 2
		for (int i = 0; i < 100; i++) {
			bot.button(1).click(); // Remove 100.
		}
        bot.table(0).click(1, 0); // We added 100 between 1 and 2
		checkComposite("Item 2", "Polar", "2.00 ", "3.00 rad");
		assertEquals(2, bot.table(0).rowCount());
	 
	}

	
	private void checkComposite(String tname, String cvalue, String tx, String ty) {
		SWTBotStyledText name = bot.styledText(0); 
		assertEquals(tname, name.getText());
		
		SWTBotCombo combo = bot.comboBox(0);
		assertEquals(cvalue, combo.getText());

		SWTBotStyledText x = bot.styledText(1); 
		assertTrue(x.getText().startsWith(tx));

		SWTBotStyledText y = bot.styledText(2); 
		assertEquals(ty, y.getText());
	}

}
