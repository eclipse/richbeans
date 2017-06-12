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
package org.eclipse.richbeans.widgets.file;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class FileSelectionDialog extends Dialog {

	private String path;
	private boolean newFile = false;
	private boolean isFolderSelector = true;
	private boolean hasResourceButton = true;
	private String[] files = new String[] {"All Files"};
	private String[] extensions = null;
	
	private SelectorWidget selector;

	public FileSelectionDialog(Shell parentShell) {
		this(parentShell, System.getProperty("user.home"));
	}

	public FileSelectionDialog(Shell parentShell, String path) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.path = path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		this.selector = new SelectorWidget(parent, isFolderSelector, hasResourceButton, files, getExtensions()){

			@Override
			public void pathChanged(String path, TypedEvent event) {

				Button button = FileSelectionDialog.this.getButton(OK);
				if (button != null) {
					boolean canOK = true;
					if (isFolderSelector) canOK = this.checkDirectory(path, false);
					button.setEnabled(canOK);
					FileSelectionDialog.this.path = path;
				}

			}
		};
		selector.setNewFile(newFile);
		selector.setText(path);
		return parent;
	}
	
	@Override
	public int open() {
		int ret = super.open();
		if (ret == Dialog.OK) {
			
			if (isNewFile() && selector.getChosenResources()!=null) { // Perhaps we should refresh the project because they just wrote a new file.
				final IResource file = selector.getChosenResources()[0];
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (file.getParent()!=null) {
							try {
								file.getParent().refreshLocal(1, new NullProgressMonitor());
							} catch (CoreException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		}
		return ret;
	}


	public String getPath() {
		return path;
	}


	public boolean isNewFile() {
		return newFile;
	}

	public void setNewFile(boolean newFile) {
		this.newFile = newFile;
	}

	public boolean isFolderSelector() {
		return isFolderSelector;
	}

	public void setFolderSelector(boolean isFolderSelector) {
		this.isFolderSelector = isFolderSelector;
	}

	public boolean hasResourceButton() {
		return hasResourceButton;
	}

	public void setHasResourceButton(boolean hasResourceButton) {
		this.hasResourceButton = hasResourceButton;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		String post = isFolderSelector ? "directory" : "file";
		newShell.setText("Please select a " + post);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 180);
	}

	public String[] getExtensions() {
		return extensions;
	}

	/**
	 * This method would be more accurately called getLabels()
	 * It is the labels for the file dialog
	 * @return
	 */
	public String[] getFiles() {
		return files;
	}

	/**
	 * This method would be more accurately called setLabels(...)
	 * It is the labels for the file dialog
	 * @return
	 */
	public void setFiles(String... files) {
		this.files = files;
	}

	public void setExtensions(String... extensions) {
		this.extensions = extensions;
	}

}
