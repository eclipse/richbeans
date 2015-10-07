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

package org.eclipse.richbeans.widgets.wrappers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.dawnsci.analysis.api.expressions.IExpressionEngine;
import org.eclipse.dawnsci.analysis.api.expressions.IExpressionService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.richbeans.api.event.ValueEvent;
import org.eclipse.richbeans.widgets.Activator;
import org.eclipse.richbeans.widgets.ButtonComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Designed to wrap Text objects to allow then to work with BeanUI
 * @author Matthew Gerring
 *
 */
public class TextWrapper extends ButtonComposite {

	protected static final Color BLUE      = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
	protected static final Color RED       = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	protected static final Color BLACK     = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	protected static final Color DARK_RED  = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);

	/**
	 * The text type, effects how the text is checked.
	 */
	public enum TEXT_TYPE {
		/**
		 * Any text
		 */
		FREE_TXT,
		/**
		 * Legal expressions
		 */
		EXPRESSION,
		/**
		 * Legal filenames
		 */
		FILENAME
	}

	private TEXT_TYPE textType = TEXT_TYPE.FREE_TXT;
	
	/**
	 * @return Returns the textType.
	 */
	public TEXT_TYPE getTextType() {
		return textType;
	}

	/**
	 * @param textType The textType to set.
	 */
	public void setTextType(TEXT_TYPE textType) {
		this.textType = textType;
	}

	protected StyledText text;
	private ModifyListener modifyListener;

	private IExpressionEngine createEngine(final String expression) throws Exception {
		IExpressionService service = (IExpressionService)Activator.getService(IExpressionService.class);
		IExpressionEngine  engine  = service.getExpressionEngine();
		engine.createExpression(expression);
		return engine;
	}

	/**
	 * Simply calls super and adds some listeners.
	 * @param parent
	 * @param style
	 */
	public TextWrapper(Composite parent, int style) {

		super(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().applyTo(this);

		this.text = new StyledText(this, style);

		// Fill available space so the layout can be properly controlled by the parent
		GridDataFactory.fillDefaults().grab(true, true).applyTo(text);

		mainControl = text;

		this.modifyListener = new ModifyListener() {
			private IExpressionEngine jexl;

			@Override
			public void modifyText(ModifyEvent e) {

				final Object newValue = getValue();

				if (textType==TEXT_TYPE.EXPRESSION) {

					try {
						String expression = newValue.toString().trim();
						jexl = createEngine(expression);
						jexl.createExpression(expression);
						text.setForeground(BLUE);
					} catch (Throwable ne) {
						text.setForeground(RED);
					}
				} else if (textType == TEXT_TYPE.FILENAME) {

					// TODO the set of characters currently excluded doesn't really match the actual forbidden characters on Windows or Linux systems
					String testString = newValue.toString().trim();
					if (testString.contains(" ") || testString.startsWith("-")
							|| testString.contains(";") || testString.contains("<") || testString.contains("\t")
							|| testString.contains("'") || testString.contains("\"") || testString.contains("\\")
							|| testString.contains("\n")|| testString.contains("..") || testString.contains("/")) {
						if (!RED.isDisposed()) {
							text.setForeground(RED);
						}
						text.setToolTipText("Expression has invalid syntax");

					} else {
						text.setToolTipText("Enter a valid filename. Do NOT use spaces, commas, backslash etc.");
						if (!BLACK.isDisposed()) {
							text.setForeground(BLACK);
						}
					}
				}

				final ValueEvent evt = new ValueEvent(text,getFieldName());
				evt.setValue(newValue);
				eventDelegate.notifyValueListeners(evt);
			}
		};
		text.addModifyListener(modifyListener);
	}

	@Override
	public void setToolTipText(String text) {
		this.text.setToolTipText(text);
	}

	@Override
	public void dispose() {
		if (text!=null&&!text.isDisposed()) text.removeModifyListener(modifyListener);
		super.dispose();
	}

	private boolean multiLineMode = false;

	@Override
	public Object getValue() {
		if (multiLineMode) {
			final String [] sa = getText().split(text.getLineDelimiter());
			return Arrays.asList(sa);
		}

		return getText();
	}

	/**
	 * @return text
	 */
	public String getText() {
		if (text.isDisposed()) {
			return null;
		}
		return text.getText();
	}

	@Override
	public void setValue(Object value) {
		if (isDisposed()) return;
		if (value instanceof List<?>) {
			multiLineMode = true;
			final List<?> lines = (List<?>)value;
			final StringBuilder buf  = new StringBuilder();
			for (Object line : lines) {
				buf.append(line.toString());
				buf.append(text.getLineDelimiter());
			}
			text.setText(buf.toString());
		} else {
			multiLineMode = false;
			text.setText(value!=null?value.toString():"");
		}
	}
	/*******************************************************************/
	/**        This section will be the same for many wrappers.       **/
	/*******************************************************************/
	@Override
	protected void checkSubclass () {
	}

	/**
	 * @param active the active to set
	 */
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		setVisible(active);
	}

	/**
	 * @param i
	 */
	public void setTextLimit(int i) {
		text.setTextLimit(i);
	}

	/**
	 * If you have a variable set with values which the box in expression
	 * mode should check, send them here. Otherwise the expression box simply
	 * checks legal syntax.
	 * 
	 * Variables are Jep ones therefore Strings or Numbers
	 * 
	 * TODO: Not implemented yet.
	 *
	 * @param vars
	 */
	public void setExpressionVariables(final Map<String, Object> vars) {
	}

	/*******************************************************************/
}
