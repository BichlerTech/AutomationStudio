package com.bichler.astudio.view.drivermodel.dnd;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

import com.bichler.astudio.view.drivermodel.browser.listener.IDragConverter;

public class DriverModelDragListener implements DragSourceListener {

	private TreeViewer viewer;
	private IDragConverter converter;

	public DriverModelDragListener(TreeViewer viewer, IDragConverter converter) {
		this.viewer = viewer;
		this.converter = converter;
	}


	@Override
	public void dragStart(DragSourceEvent event) {

	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		// Here you do the convertion to the type which is expected.
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		Object firstElement = selection.getFirstElement();

		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			String text = this.converter.getConverter().dragTextTransfer(
					firstElement);
			event.data = text;
		}
	}

	@Override
	public void dragFinished(DragSourceEvent event) {

	}
}
