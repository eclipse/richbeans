package org.eclipse.richbeans.widgets.wrappers;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.richbeans.api.event.ValueEvent;
import org.eclipse.richbeans.api.widget.IFieldWidget;
import org.eclipse.richbeans.widgets.ColorSelectorComposite;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;



/**
 * Used to wrap the Color Selector object to allow then to work with BeanUI.
 * 
 * @author Joel Ogden
 */
public class ColorSelectorWrapper extends ColorSelectorComposite implements IFieldWidget
{

	protected IPropertyChangeListener changeListener;
	
	public ColorSelectorWrapper(Composite parent, int style)
	{
		super(parent, style);
		
		// set the layout of the composite
		this.setLayout(new GridLayout(1,false));
		
		// create the colourSelection
		this.colourSelector = new ColorSelector(this);
		
		// create the event listener - detects when a new colour is choosen
		this.pack();
		this.changeListener = new IPropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent event)
			{				
				final ValueEvent evt = new ValueEvent(colourSelector, getFieldName());;
				evt.setValue(getValue());
				eventDelegate.notifyValueListeners(evt);
			}
		};
		
		// add the listener
		colourSelector.addListener(changeListener);
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
	}
	
	/**
	 * @return RGB - The current Color in RGB object format.
	 */
	@Override
	public Object getValue()
	{
		return colourSelector.getColorValue();
	}

	
	/**
	 * Set the color value of the object
	 * @param value - RGB object preferred.
	 */
	@Override
	public void setValue(Object value)
	{
		if (value instanceof RGB) {
			colourSelector.setColorValue((RGB) value);
		}
		else 
		{
			colourSelector.setColorValue(new RGB(0,0,0));
			
		}
				
		if (this.notifyType!=null&&(notifyType==NOTIFY_TYPE.ALWAYS||notifyType==NOTIFY_TYPE.VALUE_CHANGED)) {
			final ValueEvent evt = new ValueEvent(colourSelector, getFieldName());
			evt.setValue(getValue());
			eventDelegate.notifyValueListeners(evt);
		}
		return;
		
	}
	
}
