package org.eclipse.richbeans.widgets.table.event;

import java.util.EventListener;

public interface SeriesItemListener extends EventListener {

	/**
	 * Called when a new item is added.
	 * @param evt
	 */
	void itemAdded(SeriesItemEvent evt);

	/**
	 * Called when an existing item is removed
	 * @param evt
	 */
	void itemRemoved(SeriesItemEvent evt);
}
