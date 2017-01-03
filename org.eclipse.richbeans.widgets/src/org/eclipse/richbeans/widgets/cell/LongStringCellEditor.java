package org.eclipse.richbeans.widgets.cell;

import java.text.MessageFormat;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.richbeans.widgets.internal.GridUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Opens a Model Table on an additional popup for those
 * fields which are an almagom of values. For instance BoundingBox.
 * 
 * @author Matthew Gerring
 *
 */
public class LongStringCellEditor extends CellEditor {

	private static final Logger logger = LoggerFactory.getLogger(LongStringCellEditor.class);

	/**
	 * Image registry key for three dot image (value <code>"cell_editor_dots_button_image"</code>).
	 */
	public static final String CELL_EDITOR_IMG_DOTS_BUTTON = "cell_editor_dots_button_image";//$NON-NLS-1$
	
	static {
		ImageRegistry reg = JFaceResources.getImageRegistry();
		reg.put(CELL_EDITOR_IMG_DOTS_BUTTON, ImageDescriptor.createFromFile(
				DialogCellEditor.class, "images/dots_button.png"));//$NON-NLS-1$
	}

	/**
	 * The area control.
	 */
	private Composite area;

	/**
	 * The current contents.
	 */
	private Control contents;

	/**
	 * The label that gets reused by <code>updateLabel</code>.
	 */
	private Text defaultLabel;

	// Data
	private String     value;

	// UI 
	private ILabelProvider labelProv;

	public LongStringCellEditor(Composite      parent, 
			                    ILabelProvider labelProv) {
		super();
		this.labelProv = labelProv;
		create(parent);
	}

    /**
     * Default DialogCellEditor style
     */
    private static final int defaultStyle = SWT.NONE;

     /**
     * Creates the button for this cell area under the given parent control.
     * <p>
     * The default implementation of this framework method creates the button
     * display on the right hand side of the dialog cell area. Subclasses
     * may extend or reimplement.
     * </p>
     *
     * @param parent the parent control
     * @return the new button control
     */
    protected Button createButton(Composite parent) {
        Button result = new Button(parent, SWT.DOWN);
        result.setText("..."); //$NON-NLS-1$
        GridData gdata = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        gdata.heightHint = 22;
        result.setLayoutData(gdata);
        return result;
    }


    @Override
	protected Control createControl(Composite parent) {

        Font font = parent.getFont();
        Color bg = parent.getBackground();

        area = new Composite(parent, getStyle());
        area.setFont(font);
        area.setBackground(bg);
        area.setLayout(new GridLayout(3, false));
        GridUtils.removeMargins(area);

        contents = createTextContents(area);
        updateContents(value);
        
        KeyListener exit = new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent e) {
        		if (e.character == '\u001b') { // Escape
        			fireCancelEditor();
        		}
        	}
        };
        
        Button button = createButton(area);
        button.setFont(font);

        button.addKeyListener(exit);

        button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
 
            	Object newValue = openDialogBox(area);

            	if (newValue != null) {
                    boolean newValidState = isCorrect(newValue);
                    if (newValidState) {
                        markDirty();
                        doSetValue(newValue);
                    } else {
                        // try to insert the current value into the error message.
                        setErrorMessage(MessageFormat.format(getErrorMessage(),
                                new Object[] { newValue.toString() }));
                    }
                    fireApplyEditorValue();
                }
            }
        });

        setValueValid(true);

        return area;
    }
    
    /**
     * Creates the controls used to show the value of this cell area.
     * <p>
     * The default implementation of this framework method creates
     * a label widget, using the same font and background color as the parent control.
     * </p>
     * <p>
     * Subclasses may reimplement.  If you reimplement this method, you
     * should also reimplement <code>updateContents</code>.
     * </p>
     *
     * @param cell the control for this cell area
     * @return the underlying control
     */
    protected Control createTextContents(Composite cell) {
    	Text txt = new Text(cell, SWT.LEFT);
    	txt.setFont(cell.getFont());
    	txt.setBackground(cell.getBackground());
    	GridData gData = new GridData(SWT.FILL, SWT.FILL, true, true);
    	gData.widthHint = 100;
    	txt.setLayoutData(gData);
    	txt.addModifyListener((e) -> this.value = txt.getText());
       	defaultLabel = txt;
        return txt;
    }

	@Override
	public void deactivate() {
		super.deactivate();
	}

    @Override
	protected Object doGetValue() {
        return value;
    }

    @Override
	protected void doSetFocus() {
        defaultLabel.setFocus();
    }


    @Override
	protected void doSetValue(Object value) {
        this.value = (String)value;
        updateContents(value);
    }

    /**
     * Opens a dialog box under the given parent control and returns the
     * dialog's value when it closes, or <code>null</code> if the dialog
     * was canceled or no selection was made in the dialog.
     * <p>
     * This framework method must be implemented by concrete subclasses.
     * It is called when the user has pressed the button and the dialog
     * box must pop up.
     * </p>
     *
     * @param cellEditorWindow the parent control cell area's window
     *   so that a subclass can adjust the dialog box accordingly
     * @return the selected value, or <code>null</code> if the dialog was
     *   canceled or no selection was made in the dialog
     */
	protected Object openDialogBox(Control cellEditorWindow) {
		
		try {
			
			final LongStringDialog dialog = new LongStringDialog(cellEditorWindow.getShell()); // extends BeanDialog
			dialog.create();
			dialog.setSize(550,450); // As needed
		
			dialog.setValue((String)getValue());
			
	        final int ok = dialog.open();
	        if (ok == Dialog.OK) {
	        	this.value = dialog.getValue();
	        	defaultLabel.setText(value);
	            return value;
	        }
		} catch (Exception ne) {
			logger.error("Problem editing model!", ne);
		} 
		return null;
	}

	protected void updateContents(Object value) {
		if ( defaultLabel == null) {
			return;
		}
		if (value == null ) return;
		defaultLabel.setText(labelProv.getText(value));
	}

}
