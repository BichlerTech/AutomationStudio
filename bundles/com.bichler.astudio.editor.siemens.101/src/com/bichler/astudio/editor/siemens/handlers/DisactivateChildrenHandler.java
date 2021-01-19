package com.bichler.astudio.editor.siemens.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.editor.siemens.model.AbstractSiemensNode;

public class DisactivateChildrenHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveMenuSelection(event);
		if (selection == null) {
			return null;
		}
		Object[] items = ((StructuredSelection) selection).toArray();
		List<Object> items2update = new ArrayList<>();
		for (Object item : items) {
			if (item instanceof AbstractSiemensNode) {
				boolean isActive = ((AbstractSiemensNode) item).isActive();
				if (isActive) {
					((AbstractSiemensNode) item).setActiveAll(false);
					items2update.add(item);
				}
			}
		}

		return null;
	}
}
