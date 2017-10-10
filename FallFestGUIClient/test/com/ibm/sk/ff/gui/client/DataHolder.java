package com.ibm.sk.ff.gui.client;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class DataHolder<T> implements ComboBoxModel<T>, ListModel<T> {
	
	private final List<ListDataListener> listeners = new ArrayList<>();
	private final List<T> values = new ArrayList<T>();
	
	private int selected = -1;
	
	public void add(T toAdd) {
		values.add(toAdd);
		listeners.stream().forEach(l -> l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, values.size() - 1, values.size() - 1)));
	}
	
	public void remove(T toRemove) {
		int location = indexOf(toRemove);
		values.remove(toRemove);
		listeners.stream().forEach(l -> l.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, location, location)));
	}
	
	public boolean contains(T data) {
		for (T it : values) {
			if (it.equals(data)) {
				return true;
			}
		}
		return false;
	}
	
	public int indexOf(T data) {
		int ret = -1;
		for (int i = 0; i < values.size() && ret == -1; i++) {
			if (values.get(i).equals(data)) {
				ret = -1;
			}
		}
		return ret;
	}
	
	public void set(int order, T data) {
		values.set(order, data);
		listeners.stream().forEach(l -> l.contentsChanged(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, order, order)));
	}
	
	public void addElement(T data) {
		add(data);
	}
	
	public void removeElement(T data) {
		remove(data);
	}
	
	public int size() {
		return getSize();
	}

	@Override
	public int getSize() {
		return values.size();
	}

	@Override
	public T getElementAt(int index) {
		T ret = null;
		
		if (index >= 0 && index < values.size()) {
			ret = values.get(index);
		}
		
		return ret;
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selected = -1;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).equals(anItem)) {
				selected = i;
			}
		}
	}

	@Override
	public Object getSelectedItem() {
		Object ret = null;
		
		if (selected != -1 && selected >= 0 && selected < values.size()) {
			ret = values.get(selected);
		}
		
		return ret;
	}

}
