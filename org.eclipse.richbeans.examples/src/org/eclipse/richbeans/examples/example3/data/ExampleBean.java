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

package org.eclipse.richbeans.examples.example3.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Example bean.
 * @author Matthew Gerring
 *
 */
public class ExampleBean {

	private List<ExampleItem> items;
	
	/**
	 * 
	 */
	public ExampleBean() {
		items = new ArrayList<ExampleItem>(31);
	}
	/**
	 * Must implement clear() method on beans being used with BeanUI.
	 */
	public void clear() {
		items.clear();
	}
	/**
	 * @return the items
	 */
	public List<ExampleItem> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List<ExampleItem> items) {
		this.items = items;
	}
	/**
	 * 
	 * @param item
	 */
	public void addItem(ExampleItem item) {
		items.add(item);
	}
	
	@Override
	public String toString() {
		return "ExampleBean [items=" + items + "]";
	}


}

	