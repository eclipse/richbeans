package org.eclipse.richbeans.widgets.wrappers;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.richbeans.api.event.ValueEvent;
import org.eclipse.richbeans.api.widget.IFieldWidget;
import org.eclipse.richbeans.widgets.ColorSelectorComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ColorSelectorWrapper extends ColorSelectorComposite implements IFieldWidget
{
	protected ColorSelector colourSelector;
	protected IPropertyChangeListener changeListener;
	
	public ColorSelectorWrapper(Composite parent, int style)
	{
		super(parent, style);
		
		this.setLayout(new GridLayout(1,false));		
		colourSelector = new ColorSelector(this);
		
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
		
		colourSelector.addListener(changeListener);
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
	}
	
	@Override
	public Object getValue()
	{
		return colourSelector.getColorValue();
	}

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
