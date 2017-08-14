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
package org.eclipse.richbeans.widgets.shuffle;

import java.util.EventObject;
import java.util.List;

public class ShuffleEvent<T> extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5544505644622339356L;
	
	private List<T>          items;
	private ShuffleDirection direction;

	private boolean itemsSet;

	public ShuffleEvent(ShuffleViewer<T> source, ShuffleDirection direction, List<T> items) {
		super(source);
		this.direction = direction;
		this.items = items;
	}

	public ShuffleDirection getDirection() {
		return direction;
	}

	public void setDirection(ShuffleDirection direction) {
		this.direction = direction;
	}

	public List<T> getItems() {
		return items;
	}

	/**
	 * This method is for listener implementers to provide an
	 * alternative list of items which overrides the items on preShuffle.
	 * 
	 * NOTE If multiple listeners are added and all change the item list,
	 * the last listener to run takes precedence. The last listener will be
	 * the last listener added because the collection is backed by a List.
	 * 
	 * @param items
	 */
	public void setItems(List<T> items) {
		itemsSet = true;
		this.items = items;
	}
	
	public boolean isItemsSet() {
		return itemsSet;
	}

	/**
	 * 
	 * @return an immutable shuffle configuration which also does not change if the shuffle does in future (it makes inactive copies)
	 */
	public ShuffleConfiguration<T> getConfiguration() {
		@SuppressWarnings("unchecked")
		ShuffleConfiguration<T> configuration = ((ShuffleViewer<T>) getSource()).getShuffleConfiguration();
		return new ImmutableShuffleConfiguration<T>(configuration);
	}
}
