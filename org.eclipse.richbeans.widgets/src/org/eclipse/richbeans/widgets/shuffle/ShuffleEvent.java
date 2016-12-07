package org.eclipse.richbeans.widgets.shuffle;

import java.util.EventObject;
import java.util.List;

public class ShuffleEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5544505644622339356L;
	
	private List<?>          items;
	private ShuffleDirection direction;

	private boolean itemsSet;

	public ShuffleEvent(ShuffleViewer<?> source, ShuffleDirection direction, List<?> items) {
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

	public List<?> getItems() {
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
	public void setItems(List<?> items) {
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
	public ShuffleConfiguration getConfiguration() {
		ShuffleConfiguration configuration = ((ShuffleViewer)getSource()).getShuffleConfiguration();
		return new ImmutableShuffleConfiguration(configuration);
	}
}
