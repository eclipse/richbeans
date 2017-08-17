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

import java.util.EventListener;

public interface IShuffleListener<T> extends EventListener {

	/**
	 * This event is called prior to a shuffle happening.
	 * It allows the listener to intercept a forthcoming shuffle
	 * event and change the order of the list of items.
	 * 
	 * For instance an item may be moved from right to left but the
	 * left list may reflect an order from a database. Therefore the
	 * user can move the item but its order may not be modified. In this
	 * case the event can be intercepted and the order of the items
	 * set before the shuffle completes.
	 * 
	 * Setting the list of items in the event changes the list post shuffle.
	 * It is not advised to change the content just the order of the items list.
	 * 
	 * @return true if shuffle may happen, false if it should be aborted.
	 */
	default boolean preShuffle(ShuffleEvent<T> evt) {
		return true;
	}
	
	
	/**
	 * Notify that a shuffle happened, provide the direction and list of
	 * items resulting from the shuffle.
	 * 
	 * @param evt
	 */
	default void postShuffle(ShuffleEvent<T> evt) {
		
	}
}
