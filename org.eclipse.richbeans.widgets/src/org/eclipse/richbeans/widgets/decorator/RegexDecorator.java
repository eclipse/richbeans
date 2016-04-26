/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.richbeans.widgets.decorator;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * This class is a number decorator. Which gives formatting to a Text object including
 * bounds. If the bounds are exceeded the Text object is given a red foreground.
 * 
 * By simply changing the pattern it can become another decorator (e.g. a DateDecorator).
 */
public class RegexDecorator implements IDecoratorValidator, VerifyListener {

	protected Text    text;
	protected Pattern pattern;
	protected boolean allowInvalidValues = false;
	protected boolean isError = false;

	/**
	 * May be used to create an unattached decorator.
	 * This can be used as a delegate decorator for instance on BoundsDecorator
	 */
	public RegexDecorator() {

	}
	
	/**
	 * 
	 * @param text
	 * @param pattern - each character entered must match this pattern NOT the whole string.
	 */
	public RegexDecorator(Text text, final String stringPattern) {
		this.text    = text;
		this.pattern = Pattern.compile(stringPattern);
		attachListeners();
	}

	private void attachListeners() {	
		text.addVerifyListener(this);
	}
	
	@Override
	public void verifyText(VerifyEvent e) {
		
		final String contents  = text.getText()+e.text;
		boolean allStringMatch = pattern.matcher(contents).matches();
		boolean changeMatch    = pattern.matcher(e.text).matches();
		
		if (!"".equals(e.text) && !allStringMatch && !changeMatch && !allowInvalidValues) {
			e.doit = false;
			return;
		}
		
		final String oldS = text.getText();
        String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);

		if (!check(newS, e.text)) {
			e.doit = false;
			return;
		}
	}
	
	public void dispose() {
		if (text!=null) text.removeVerifyListener(this);
	}

	/**
	 * Checks the current value against the expression to see if it
	 * matches.
	 * 
	 * @return true if error.
	 */
    public boolean isError() {
    	if (text!=null) return isError;
    	Matcher matcher = pattern.matcher(text.getText());
    	if (matcher.matches()) return false;
    	return true;
    }
	
	public boolean isAllowInvalidValues() {
		return allowInvalidValues;
	}

	/**
	 * You can set the bounds checker not to accept invalid values or
	 * to accept them and color them red. Coloring red is the default.
	 * @param allowInvalidValues
	 */
	public void setAllowInvalidValues(boolean allowInvalidValues) {
		this.allowInvalidValues = allowInvalidValues;
	}
	public String getExpression() {
		return pattern.pattern();
	}

}
