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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.junit.Test;

/**
 * 
 * Basic test running all the examples.
 * 
 * @author Matthew Gerring
 *
 */
public class Example5Test extends ShellTest {

	@Override
	protected Shell createShell(Display display) throws Exception {
		IShellCreator runner = ExampleFactory.createExample5();
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
		assertNotNull(bot.button(0)); // Range1
		assertNotNull(bot.button(1)); // Range2
	}
	
	@Test
	public void range1Test() throws Exception {
		

		checkListRange("Range", new String[]{"10.0", "50.0", "1.0"});
		assertTrue(bot.styledText(0).getText().startsWith("10.0, 50.0, 1.0"));
		
		bot.styledText(0).setText("0,1,2,3,4,5,6,7,8,9");
		checkListRange("Range", new String[]{"0.0", "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0", "8.0", "9.0"});

		bot.styledText(0).setText("0.1,1.2,2.3,3.4");
		checkListRange("Range", new String[]{"0.1", "1.2", "2.3", "3.4"});

	}

	private void checkListRange(String name, String[] values) {
		
		bot.button(0).click();
		assertNotNull(bot.shell("Range"));
		SWTBot botRange = bot.shell(name).bot();
		assertNotNull(botRange.table(0));
		assertEquals(values.length, botRange.table(0).rowCount());
		for (int i = 0; i < values.length; i++) {
			assertEquals(values[i], botRange.table(0).cell(i, 0));
		}
		botRange.button(2).click(); // ok
	}

	@Test
	public void range2Test() throws Exception {
		

		bot.button(1).click();
		assertNotNull(bot.shell("Range"));

		// Open range
		SWTBot botRange = bot.shell("Range").bot();
		assertEquals("Single Value", botRange.comboBox(0).getText());
		
		// Set to range
		botRange.comboBox(0).setSelection(2);
		assertEquals("Range", botRange.comboBox(0).getText());
		
		// Enter a few ranges
		checkValues(botRange, 0,10,1,11);
		checkValues(botRange, 2,8,1,7);
		checkValues(botRange, -30,4000,100,41);
		checkValues(botRange, -30,4000,600,7);
		botRange.button(0).click(); // ok
		
		// Left invalid, check red
		Color black = new Color(bot.getDisplay(), 0, 0, 0, 255);
		Color red = new Color(bot.getDisplay(), 255, 0, 0, 255);
		assertEquals(red, bot.styledText(1).foregroundColor());
		
		// Open range
		bot.button(1).click();
		assertNotNull(bot.shell("Range"));

		// Set to valid range
		botRange = bot.shell("Range").bot();
		assertEquals("Range", botRange.comboBox(0).getText());
		assertEquals(String.valueOf(7), botRange.label(5).getText());
		checkValues(botRange, 0,10,1,11);
		botRange.button(0).click(); // ok

		// Check black
		assertEquals(black, bot.styledText(1).foregroundColor());

		// Open range
		bot.button(1).click();
		assertNotNull(bot.shell("Range"));

		// Set to step larger than stop-start
		botRange = bot.shell("Range").bot();
		assertEquals("Range", botRange.comboBox(0).getText());
		assertEquals(String.valueOf(11), botRange.label(5).getText());
		checkValues(botRange, 0,10,600,1);
		
		// Check black.
		botRange.button(0).click(); // ok
		assertEquals(black, bot.styledText(1).foregroundColor());

		// Open range
		bot.button(1).click();
		assertNotNull(bot.shell("Range"));

		// Set to stop smaller than start
		botRange = bot.shell("Range").bot();
		assertEquals("Range", botRange.comboBox(0).getText());
		assertEquals(String.valueOf(1), botRange.label(5).getText());
		checkValues(botRange, 0,-10,600,0);
		
		// Check red (invalid range)
		botRange.button(0).click(); // ok
		assertEquals(red, bot.styledText(1).foregroundColor());

	}

	private void checkValues(SWTBot botRange, double start, double stop, double step, int steps) {
		botRange.styledText(0).setText(String.valueOf(start));
		botRange.styledText(1).setText(String.valueOf(stop));
		botRange.styledText(2).setText(String.valueOf(step));
		assertEquals(String.valueOf(steps), botRange.label(5).getText());
	}

}
