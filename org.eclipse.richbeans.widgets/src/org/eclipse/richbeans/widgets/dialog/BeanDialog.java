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

package org.eclipse.richbeans.widgets.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.richbeans.api.binding.IBeanController;
import org.eclipse.richbeans.api.binding.IBeanService;
import org.eclipse.richbeans.widgets.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This dialog can be used for UI to edit a bean. Inherit from it and
 * override the method:
 * 
 * 
 * 	protected Composite createDialogArea(Composite ancestor) {
 
    }
    
    You must be running the Dialog in an OSGi environment for it to work.
    
    Then simply expose the bean fields with getters of the same name in this dialog as usual.
    
    The dialog is then uses as follows:
    <code>
        MyDialog dialog = new MyDialog(getSite().getShell()); // extends BeanDialog
		dialog.create();
		dialog.getShell().setSize(400,435); // As needed
		
		dialog.setBean(bean);
        final int ok = dialog.open();
        
        if (ok == Dialog.OK) {
            bean = dialog.getBean();
            // etc
        }
    </code>
 * 
 * 
 */
public abstract class BeanDialog<T> extends Dialog {
	
	private static final Logger logger = LoggerFactory.getLogger(BeanDialog.class);
	
	protected IBeanController<T> controller;
	

	protected BeanDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.RESIZE|SWT.TITLE);
	}
	
	@Override
	public void create() {
		super.create();
		getShell().setText("Configure User Input");
	}

	@Override
	public boolean close() {	
		if (getReturnCode()==OK) {
			try {
				controller.switchState(false);
				controller.uiToBean();			
			} catch (Exception e) {
				logger.error("Cannot get "+controller.getBean()+" from dialog!", e);
			}	
		}
		return super.close();
	}
	
	public T getBean() {
		return controller.getBean();
	}

	public void setBean(T bean) {
		try {
			IBeanService service = (IBeanService)Activator.getService(IBeanService.class);
			this.controller = service.createController(this, bean);
			controller.beanToUI();
			controller.switchState(true);
			controller.fireValueListeners();
		} catch (Exception e) {
			logger.error("Cannot send "+bean+" to dialog!", e);
		}		
	}

}
