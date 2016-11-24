package org.eclipse.richbeans.widgets.shuffle;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.richbeans.widgets.internal.GridUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

/**
 * 
 * A class for shuffling between lists
 * 
 * @author Matthew Gerring
 *
 */
public class ShuffleViewer {

	
	private ShuffleConfiguration conf;

	public ShuffleViewer(ShuffleConfiguration data) {
		this.conf = data;
	}
	
	public Composite createPartControl(Composite parent) {
		
		final Composite content = new Composite(parent, SWT.NONE);
		content.setLayout(new GridLayout(3, false));
		GridUtils.removeMargins(content);
		
		createTable(content, "left", conf.getFromList());		
		createButtons(content);
		createTable(content, "right", conf.getToList());		

		return content;
	}

	private static final Composite createButtons(Composite content) {
		
        final Composite ret = new Composite(content, SWT.NONE);
        ret.setLayout(new RowLayout(SWT.VERTICAL));
        new Label(ret, SWT.NONE);
        final Button right = new Button(ret, SWT.ARROW |SWT.RIGHT);
        right.setToolTipText("Move item right");
        new Label(ret, SWT.NONE);
        final Button left = new Button(ret, SWT.ARROW |SWT.LEFT);
        left.setToolTipText("Move item left");
        new Label(ret, SWT.NONE);
      
        return ret;
	}

	private static final TableViewer createTable(Composite parent, String tooltip, List<Object> items) {
		
		TableViewer ret = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		ret.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));	
		((Table)ret.getControl()).setToolTipText(tooltip); // NOTE This can get clobbered if we used tooltips inside the table.
		
		
		
		return ret;
	}
}
