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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

import javax.swing.event.EventListenerList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * A regexp decorator which will only accept numbers and 
 * will color invalid bounds red.
 * 
 * @author Matthew Gerring
 *
 */
public class BoundsDecorator extends RegexDecorator {
	
	private enum BoundsType {
		MINIMUM, MAXIMUM; // Other types like non-inclusive bound possible
	}

    private Object       maximum;
    private Object       minimum;
    private NumberFormat numberFormat;
	private boolean      isError=false;
	private IDecoratorValidator delegate;

	public BoundsDecorator(Text text, String stringPattern, NumberFormat numFormat) {
		super(text, stringPattern);
		numberFormat = numFormat;
		setAllowInvalidValues(true);
	}
	
	@Override
	public boolean check(String totalString, String delta) {
		
		if ("".equals(totalString)) return true;
		Number val = null;
		try {
			val = parseValue(totalString);
		} catch (Throwable ne) {
			return "".equals(delta) || delta==null || "-".equals(totalString); // Will not allow current value to proceed.
		}
		if (val==null) return false;
		
		boolean ok = checkBounds(val, true); // Colors red not unacceptable value.
		if (ok && delegate!=null) {
			ok = delegate.check(totalString, delta);
			if (!ok) {
				setError(true, createToolTipTextFromBounds(delegate.getExpression()));
				fireValueChangedListeners(new ValueChangeEvent(BoundsDecorator.this, val));
			}
		}
		return allowInvalidValues||ok;
	}

	private String createToolTipTextFromBounds(String expression) {
		return "Expression '"+expression+"' is false";
	}

	public Number getValue() {
		return parseValue(text.getText());
	}
	
	public void setValue(Number value) {
		text.setText(format(value));
	}
	
	public String format(Number value) {
		if (value.doubleValue() == Double.POSITIVE_INFINITY) {
		    return "∞";	
		} else if (value.doubleValue() == Double.NEGATIVE_INFINITY) {
			return "-∞";	
		} else if (Double.isInfinite(value.doubleValue()) || Double.isNaN(value.doubleValue())) {
			return "";
		} else {
			return(numberFormat.format(value));
		}
	}

	public boolean check(final Number value) {
		return checkBounds(value, true);
	}

	protected boolean checkBounds(final Number value, boolean fireListeners) {
		return checkBounds(value, getMinimum(), getMaximum(), fireListeners);
	}
	
	protected boolean checkBounds(final Number value, Number min, Number max, boolean fireListeners) {

		boolean ok = true;
		try {
			if (Double.isInfinite(value.doubleValue())) {
				setError(false, createToolTipTextFromBounds(value, getMinimum(), getMaximum()));
				ok = false;
			} else if (!checkValue(value, min, BoundsType.MINIMUM)) {
				setError(true, value+" is less than the minimum value of "+getMinimum());
				ok = false;
			} else if (!checkValue(value, max, BoundsType.MAXIMUM)) {
				setError(true, value+" is greater than the maximum value of "+getMaximum());
				ok = false;
			} else {
				setError(false, createToolTipTextFromBounds(value, getMinimum(), getMaximum()));
				ok = true;
			}
			
		} finally {
			// We let this current value take then fire a check later.
			if (allowInvalidValues||ok) {
				fireValueChangedListeners(new ValueChangeEvent(BoundsDecorator.this, value));
			}
		}
		return ok;
	}

	private boolean checkValue(Number value, Number bound, BoundsType type) {
		if (value == null) return false;
		if (bound == null) return true;
		if (Double.isNaN(bound.doubleValue())) return true;
		
		switch (type){
		case MINIMUM:
			return value.doubleValue()>=bound.doubleValue();
		case MAXIMUM:
			return value.doubleValue()<=bound.doubleValue();
		}
		throw new RuntimeException("Invalid value!");
	}

	private EventListenerList listeners;
	
	protected void fireValueChangedListeners(final ValueChangeEvent evt) {
		if (listeners == null) return;
		
		final IValueChangeListener[] ls = listeners.getListeners(IValueChangeListener.class);
		if (ls==null || ls.length<1) return;
		
		for (IValueChangeListener l : ls) {
			l.valueValidating(evt);
		}

	}
	public void addValueChangeListener(IValueChangeListener l) {
		if (listeners == null) listeners = new EventListenerList();
		listeners.add(IValueChangeListener.class, l);
	}
	public void removeValueChangeListener(IValueChangeListener l) {
		if (listeners == null) return;
		listeners.remove(IValueChangeListener.class, l);
	}

	protected String createToolTipTextFromBounds(Number value, Number min, Number max) {

		if (min==null && max==null) return "Please enter a number.";
		
		final StringBuilder buf = new StringBuilder();

		if (min!=null) {
			if (Double.isInfinite(min.doubleValue())) {
				buf.append("-∞");
			} else {
				buf.append(numberFormat.format(min));
			}
			buf.append(" <= ");
		}
		
		buf.append(numberFormat.format(value));

		if (max!=null) {
			
			buf.append(" <= ");

			if (Double.isInfinite(max.doubleValue())) {
				buf.append("∞");
			} else {
				buf.append(numberFormat.format(max));
			}
		}
        return buf.toString();
	}

	private void setError(boolean isError, String toolTip) {
		if (!allowInvalidValues) {
			this.isError = false;
			text.setForeground(getForeground()); 
			return;
		}
		this.isError = isError;
		text.setToolTipText(toolTip);
		if (isError) {
			text.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED)); 
		} else {
			text.setForeground(getForeground()); 
		}
	}
	
	private Color getForeground() {
		if (!text.getEditable() || !text.isEnabled()) {
			return text.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
		} else {
		    return text.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		}
	}

	public Number getMaximum() {
		if (maximum == null) return null;
		return maximum instanceof Number ? (Number)maximum : ((BoundsDecorator)maximum).getValue();
	}

	public void setMaximum(Number maximum) {
		if (maximum != null) {
			double dbl = ((Number)maximum).doubleValue();
			if (Double.isNaN(dbl) || Double.isInfinite(dbl)) {
				this.maximum = null;
				return;
			}
		}
		this.maximum = maximum;
		if (text.getText()==null || "".equals(text.getText())) return;
		checkBounds(getValue(), false);
	}
	
	public void setMaximum(BoundsDecorator maximum) {
		registerBoundsChanger(maximum, BoundsType.MAXIMUM);
		this.maximum = maximum;
		if (text.getText()==null || "".equals(text.getText())) return;
		checkBounds(getValue(), false);
	}

	private void registerBoundsChanger(Object object, final BoundsType type) {
		if (object == this) return;
		if (object instanceof BoundsDecorator) {
		    ((BoundsDecorator)object).addValueChangeListener(new IValueChangeListener() {
				@Override
				public void valueValidating(ValueChangeEvent evt) {
					if (evt.getSource()==this) return;
					
					Number value = getValue();
					boolean isOk = checkValue(value, evt.getValue(), type);
					if (!isOk) {
						if (type==BoundsType.MINIMUM) {
						    setError(true, value+" is less than the minimum value of "+evt.getValue());
						} else if (type == BoundsType.MAXIMUM) {
							setError(true, value+" is greater than the maximum value of "+evt.getValue());
						}
						return;
					} else {
						if (type==BoundsType.MINIMUM) {
							setError(false, createToolTipTextFromBounds(value, evt.getValue(), getMaximum())); 
						} else if (type == BoundsType.MAXIMUM) {
							setError(false, createToolTipTextFromBounds(value, getMinimum(), evt.getValue())); 
						}
					}
				}
		    });
		}
	}

	public Number getMinimum() {
		if (minimum == null) return null;
		return minimum instanceof Number ? (Number)minimum : ((BoundsDecorator)minimum).getValue();
	}

	public void setMinimum(Number minimum) {
		if (minimum != null) {
			double dbl = ((Number)minimum).doubleValue();
			if (Double.isNaN(dbl) || Double.isInfinite(dbl)) {
				this.minimum = null;
				return;
			}
		}
		this.minimum = minimum;
		if (text.getText()==null || "".equals(text.getText())) return;
		checkBounds(getValue(), false);
	}
	
	public void setMinimum(BoundsDecorator minimum) {
		registerBoundsChanger(minimum, BoundsType.MINIMUM);
		this.minimum = minimum;
		if (text.getText()==null || "".equals(text.getText())) return;
		checkBounds(getValue(), false);
	}


	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	public void setDecimalFormat(String numberFormat) {
		setNumberFormat(numberFormat, new DecimalFormat(numberFormat));
	}
	
	/**
	 * If numberFormatString is set to null, the regular expression will not be
	 * automatically adjusted to allow E in it
	 * @param numberFormatString
	 * @param format
	 */
	public void setNumberFormat(String numberFormatString, NumberFormat format) {

		this.numberFormat = format;	
		
		if (numberFormatString!=null && (numberFormatString.contains("e")||numberFormatString.contains("E"))) {
			final String spattern = pattern.pattern();
			if (spattern.endsWith("]+") && !spattern.contains("eE ")) {
				String pat = spattern.substring(0, spattern.lastIndexOf(']'));
				pat += "eE ";
				pat += "]+";
				this.pattern = Pattern.compile(pat);
			}
		}
	}

	public boolean isError() {
		return isError;
	}

	public IDecoratorValidator getDelegate() {
		return delegate;
	}

	public void setDelegate(IDecoratorValidator delegate) {
		this.delegate = delegate;
	}
}
