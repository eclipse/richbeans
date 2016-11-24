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

package org.eclipse.richbeans.widgets.scalebox;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;

import org.eclipse.richbeans.api.event.ValueAdapter;
import org.eclipse.richbeans.api.event.ValueEvent;
import org.eclipse.richbeans.api.expression.IRulesEngine;
import org.eclipse.richbeans.api.expression.IRulesService;
import org.eclipse.richbeans.api.reflection.RichBeanUtils;
import org.eclipse.richbeans.api.widget.IFieldWidget;
import org.eclipse.richbeans.widgets.Activator;
import org.eclipse.richbeans.widgets.internal.GridUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class for entering a value, also automatically re-evaluates the expression.
 * 
 * You cannot call setExpressionManager on this class.
 * 
 * @author Matthew Gerring
 *
 */
public class ScaleBoxAndFixedExpression extends ScaleBox{

	public interface ExpressionProvider {
        double getValue(double val);

		IFieldWidget[] getPrecedents();
	}

	private static Logger logger = LoggerFactory.getLogger(ScaleBoxAndFixedExpression.class);
	
	private Label       fixedExpressionLabel;
	private Object      dataProvider;
	private String      expression;
	private String      thisVariable;
	private String      labelUnit;
	private String      prefix;

	private ExpressionProvider provider;

	private NumberFormat labelNumberFormat;

	private IRulesEngine engine;

    
	/**
	 */
	public ScaleBoxAndFixedExpression(final Composite parent, 
                                 final int       style) {
		super(parent, style);

		createFixedExpressionLabel();
	}
	/**
	 */
	public ScaleBoxAndFixedExpression(final Composite parent, 
                                 final int       style,
                                 final ExpressionProvider ep) {
		super(parent, style);


		createFixedExpressionLabel();
	    this.provider     = ep;
	    
	    final IFieldWidget[] fw = provider.getPrecedents();
	    if (fw!=null) for (int i = 0; i < fw.length; i++) {
			fw[i].addValueListener(new ValueAdapter(fw[i].toString() + " Listener") {
				@Override
				public void valueChangePerformed(ValueEvent e) {
					updateLabel();
				}
			});
	    }
	}
	/**
	 * 
	 * @param parent
	 * @param style
	 * @param expression
	 * @param dataProvider
	 * @throws ParseException 
	 */
	public ScaleBoxAndFixedExpression(final Composite parent, 
			                     final int       style, 
			                     final String    thisVariable, 
			                     final String    expression, 
			                     final Object    dataProvider) throws Exception {
		
		super(parent, style);

		createFixedExpressionLabel();
	    
	    this.thisVariable = thisVariable;
	    this.expression   = expression;
	    this.engine  = createEngine(expression);
	    this.dataProvider = dataProvider;
	}
	
	private void createFixedExpressionLabel() {

		if (!label.isVisible()) {
			final GridData data = (GridData) label.getLayoutData();
			data.exclude = true;
		}

		// We allow user expressions
    	createExpressionLabel(-1);
    	
    	final GridLayout gridLayout = new GridLayout(4,false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);

		if (fixedExpressionLabel!=null) return;
	    this.fixedExpressionLabel = new Label(this, SWT.RIGHT);
	    
	    final GridData gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
	    gd.widthHint = 80;
		this.fixedExpressionLabel.setLayoutData(gd);
		
		layout();
	}
    
 	public void setFixedExpressionValue(final double numericalValue) {
		final String labelText = getLabel(numericalValue);
		fixedExpressionLabel.setText(labelText);
		if (labelText==null||"".equals(labelText.trim())||"-".equals(labelText.trim())) {
		    GridUtils.setVisible(this.fixedExpressionLabel, false);
		} else {
			GridUtils.setVisible(this.fixedExpressionLabel, true);
		}
		getShell().layout();
    }
    
	public void setExpressionLabelTooltip(final String tip) {
    	fixedExpressionLabel.setToolTipText(tip);
    }
	/**
	 * Recalculates expression
	 */
	@Override
	protected boolean checkValue(final String txt) {
        
		boolean ok = super.checkValue(txt);
		if (txt==null||"".equals(txt.trim())||"-".equals(txt.trim())) {
			GridUtils.setVisible(this.fixedExpressionLabel, false);
			getShell().layout();
			return false;
		}
		final double num = getNumericValue();
        updateLabel(num);
        return ok;
	}
	
	private void updateLabel() {
		updateLabel(getNumericValue());
	}
	
	private void updateLabel(final double value) {
		
		try {
			if (provider==null&&engine==null)  return;
			double labelVal = Double.NaN;
			if (provider!=null) {
				labelVal = provider.getValue(value);
			} else {
				Collection<String> vars = engine.getLazyVariableNamesFromExpression();
				for (String name : vars) {
		    		final Double val = (Double) RichBeanUtils.getBeanValue(dataProvider, name);
		    		engine.addLoadedVariable(name, val);
				}
			    
				engine.addLoadedVariable(thisVariable, value);
			    
			    labelVal = ((Double)engine.evaluate()).doubleValue();
			}
		    
            setFixedExpressionValue(labelVal);
		    layout();
		    
		} catch (Exception ne) {
			logger.error("Cannot compute value "+expression, ne);
			fixedExpressionLabel.setText("");
		}
 		
	}

	private String getLabel(double labelVal) {
		final StringBuilder buf = new StringBuilder();
		if (getPrefix()!=null) {
			buf.append(getPrefix());
			buf.append(" ");
		}
		
		String text = labelNumberFormat!=null
		            ? labelNumberFormat.format(labelVal)
		            : numberFormat.format(labelVal);
		
		buf.append(text);
		
		if (getLabelUnit()!=null) {
			buf.append(" ");
			buf.append(getLabelUnit());
		}
		return buf.toString();
	}
	
	public void setLabelDecimalPlaces(int decimalPlaces) {
		if (labelNumberFormat==null) {
			labelNumberFormat = NumberFormat.getInstance();
		}
		labelNumberFormat.setMaximumFractionDigits(decimalPlaces);
	}
	
	private IRulesEngine createEngine(final String expression) throws Exception {
		IRulesService service = (IRulesService)Activator.getService(IRulesService.class);
		IRulesEngine  engine  = service.createRulesEngine();
		engine.createExpression(expression);
		return engine;
	}

	/**
	 * @return Returns the dataProvider.
	 */
	public Object getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider The dataProvider to set.
	 */
	public void setDataProvider(Object dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @return Returns the expression.
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * @param expression The expression to set.
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * @return Returns the thisVariable.
	 */
	public String getThisVariable() {
		return thisVariable;
	}

	/**
	 * @param thisVariable The thisVariable to set.
	 */
	public void setThisVariable(String thisVariable) {
		this.thisVariable = thisVariable;
	}

	/**
	 * @return Returns the labelUnit.
	 */
	public String getLabelUnit() {
		return labelUnit;
	}

	/**
	 * @param labelUnit The labelUnit to set.
	 */
	public void setLabelUnit(String labelUnit) {
		this.labelUnit = labelUnit;
	}
	/**
	 * @return Returns the prefix.
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * @param prefix The prefix to set.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}

	