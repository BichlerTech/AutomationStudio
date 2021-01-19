package com.bichler.astudio.opcua.modeller.controls;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * Abstract base class for viewers that contain lists of items (such as a combo
 * or list). Most of the viewer implementation is in this base class, except for
 * the minimal code that actually communicates with the underlying widget.
 * 
 * @see org.eclipse.jface.viewers.ListViewer
 * @see org.eclipse.jface.viewers.ComboViewer
 * 
 * @since 3.0
 */
public abstract class AbstractListViewerwithImage extends AbstractListViewer {
	/**
	 * A list of viewer elements (element type: <code>Object</code>).
	 */
	private java.util.List<Object> listMap = new ArrayList<Object>();

	/**
	 * Adds the given string to the underlying widget at the given index
	 * 
	 * @param string the string to add
	 * @param index  position to insert the string into
	 */
	protected abstract void listAdd(String string, Image image, int index);

	/**
	 * Sets the text of the item at the given index in the underlying widget.
	 * 
	 * @param index  index to modify
	 * @param string new text
	 */
	protected abstract void listSetItem(int index, String string, Image image);

	/**
	 * Sets the underlying widget's items to be the given array of items.
	 *
	 * @param labels the array of label text
	 */
	protected abstract void listSetItems(String[] labels, Image[] images);

	/**
	 * Adds the given elements to this list viewer. If this viewer does not have a
	 * sorter, the elements are added at the end in the order given; otherwise the
	 * elements are inserted at appropriate positions.
	 * <p>
	 * This method should be called (by the content provider) when elements have
	 * been added to the model, in order to cause the viewer to accurately reflect
	 * the model. This method only affects the viewer, not the model.
	 * </p>
	 *
	 * @param elements the elements to add
	 */
	public void add(Object[] elements) {
		assertElementsNotNull(elements);
		Object[] filtered = filter(elements);
		ILabelProvider labelProvider = (ILabelProvider) getLabelProvider();
		for (int i = 0; i < filtered.length; i++) {
			Object element = filtered[i];
			int ix = indexForElement(element);
			insertItem(labelProvider, element, ix);
		}
	}

	private void insertItem(ILabelProvider labelProvider, Object element, int index) {
		listAdd(getLabelProviderText(labelProvider, element), index);
		listMap.add(index, element);
		mapElement(element, getControl()); // must map it, since findItem only looks
											// in map, if enabled
	}

	/**
	 * Return the text for the element from the labelProvider. If it is null then
	 * return the empty String.
	 * 
	 * @param labelProvider ILabelProvider
	 * @param element
	 * @return String. Return the emptyString if the labelProvider returns null for
	 *         the text.
	 * 
	 * @since 3.1
	 */
	private String getLabelProviderText(ILabelProvider labelProvider, Object element) {
		String text = labelProvider.getText(element);
		if (text == null) {
			return "";//$NON-NLS-1$
		}
		return text;
	}

	/**
	 * Return the text for the element from the labelProvider. If it is null then
	 * return the empty String.
	 * 
	 * @param labelProvider ILabelProvider
	 * @param element
	 * @return String. Return the emptyString if the labelProvider returns null for
	 *         the text.
	 * 
	 * @since 3.1
	 */
	private Image getLabelProviderImage(ILabelProvider labelProvider, Object element) {
		return labelProvider.getImage(element);
	}

	/*
	 * (non-Javadoc) Method declared on StructuredViewer.
	 */
	protected void doUpdateItem(Widget data, Object element, boolean fullMap) {
		if (element != null) {
			int ix = getElementIndex(element);
			if (ix >= 0) {
				ILabelProvider labelProvider = (ILabelProvider) getLabelProvider();
				listSetItem(ix, getLabelProviderText(labelProvider, element),
						getLabelProviderImage(labelProvider, element));
			}
		}
	}

	/*
	 * (non-Javadoc) Method declared on Viewer.
	 */
	/*
	 * (non-Javadoc) Method declared on StructuredViewer.
	 *
	 * protected List<Object> getSelectionFromWidget() { int[] ixs =
	 * listGetSelectionIndices(); ArrayList<Object> list = new
	 * ArrayList<Object>(ixs.length); for (int i = 0; i < ixs.length; i++) { Object
	 * e = getElementAt(ixs[i]); if (e != null) { list.add(e); } } return list; }
	 */
	/**
	 * @param element the element to insert
	 * @return the index where the item should be inserted.
	 *
	 *         protected int indexForElement(Object element) { ViewerComparator
	 *         comparator = getComparator(); if (comparator == null) { return
	 *         listGetItemCount(); } int count = listGetItemCount(); int min = 0,
	 *         max = count - 1; while (min <= max) { int mid = (min + max) / 2;
	 *         Object data = listMap.get(mid); int compare =
	 *         comparator.compare(this, data, element); if (compare == 0) { // find
	 *         first item > element while (compare == 0) { ++mid; if (mid >= count)
	 *         { break; } data = listMap.get(mid); compare =
	 *         comparator.compare(this, data, element); } return mid; } if (compare
	 *         < 0) { min = mid + 1; } else { max = mid - 1; } } return min; }
	 */
	/*
	 * (non-Javadoc) Method declared on Viewer.
	 */
	protected void inputChanged(Object input, Object oldInput) {
		listMap.clear();
		Object[] children = getSortedChildren(getRoot());
		int size = children.length;
		listRemoveAll();
		String[] labels = new String[size];
		Image[] images = new Image[size];
		for (int i = 0; i < size; i++) {
			Object el = children[i];
			labels[i] = getLabelProviderText((ILabelProvider) getLabelProvider(), el);
			images[i] = getLabelProviderImage((ILabelProvider) getLabelProvider(), el);
			listMap.add(el);
			mapElement(el, getControl()); // must map it, since findItem only looks in
											// map, if enabled
		}
		listSetItems(labels, images);
	}

	/*
	 * (non-Javadoc) Method declared on StructuredViewer.
	 */
	protected void internalRefresh(Object element) {
		Control list = getControl();
		if (element == null || equals(element, getRoot())) {
			// the parent
			if (listMap != null) {
				listMap.clear();
			}
			unmapAllElements();
			List<?> selection = getSelectionFromWidget();
			int topIndex = -1;
			if (selection == null || selection.isEmpty()) {
				topIndex = listGetTopIndex();
			}
			Object[] children = null;
			list.setRedraw(false);
			try {
				listRemoveAll();
				children = getSortedChildren(getRoot());
				String[] items = new String[children.length];
				Image[] images = new Image[children.length];
				ILabelProvider labelProvider = (ILabelProvider) getLabelProvider();
				for (int i = 0; i < items.length; i++) {
					Object el = children[i];
					items[i] = getLabelProviderText(labelProvider, el);
					images[i] = getLabelProviderImage(labelProvider, el);
					listMap.add(el);
					mapElement(el, list); // must map it, since findItem only looks in
											// map, if enabled
				}
				listSetItems(items);
			} finally {
				list.setRedraw(true);
			}
			if (topIndex == -1) {
				setSelectionToWidget(selection, false);
			} else {
				listSetTopIndex(Math.min(topIndex, children.length));
			}
		} else {
			doUpdateItem(list, element, true);
		}
	}

	/**
	 * Removes the given elements from this list viewer.
	 *
	 * @param elements the elements to remove
	 *
	 *                 private void internalRemove(final Object[] elements) { Object
	 *                 input = getInput(); for (int i = 0; i < elements.length; ++i)
	 *                 { if (equals(elements[i], input)) { setInput(null); return; }
	 *                 int ix = getElementIndex(elements[i]); if (ix >= 0) {
	 *                 listRemove(ix); listMap.remove(ix); unmapElement(elements[i],
	 *                 getControl()); } } }
	 */
	/**
	 * Returns the index of the given element in listMap, or -1 if the element
	 * cannot be found. As of 3.3, uses the element comparer if available.
	 * 
	 * @param element
	 * @return the index
	 */
	int getElementIndex(Object element) {
		IElementComparer comparer = getComparer();
		if (comparer == null) {
			return listMap.indexOf(element);
		}
		int size = listMap.size();
		for (int i = 0; i < size; i++) {
			if (comparer.equals(element, listMap.get(i)))
				return i;
		}
		return -1;
	}
}
