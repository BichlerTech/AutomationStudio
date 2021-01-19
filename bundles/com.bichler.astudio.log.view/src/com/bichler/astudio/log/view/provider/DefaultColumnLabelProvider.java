package com.bichler.astudio.log.view.provider;

import com.bichler.astudio.log.server.core.ASLog;
import com.bichler.astudio.log.server.util.ASLogConverter;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;


public class DefaultColumnLabelProvider extends ColumnLabelProvider {

	private String fieldName;

	public DefaultColumnLabelProvider(String fieldName) {
		super();
		this.fieldName = fieldName;
	}

	@Override
	public String getText(Object element) {
		ASLog log = (ASLog) element;
		return ASLogConverter.getValue(log, fieldName);
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

}
