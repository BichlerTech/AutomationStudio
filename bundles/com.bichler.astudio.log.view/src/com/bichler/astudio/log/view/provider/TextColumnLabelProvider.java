package com.bichler.astudio.log.view.provider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.bichler.astudio.log.server.core.ASLog;
import com.bichler.astudio.log.server.util.ASLogConstants;
import com.bichler.astudio.log.server.util.ASLogConverter;


public class TextColumnLabelProvider extends ColumnLabelProvider {

	private static final int TEXT_LENGTH_LIMIT = 200;

	private String fieldName;

	public TextColumnLabelProvider(String fieldName) {
		super();
		this.fieldName = fieldName;
	}

	@Override
	public String getText(Object element) {
		ASLog log = (ASLog) element;
		String value = ASLogConverter.getValue(log, fieldName);

		switch (this.fieldName) {
		case ASLogConstants.THROWABLE_FIELD_NAME:
			break;
		default:
			value = value.replaceAll("\\r|\\n", " ");
			break;
		}

//		value = value.replaceAll("\\r|\\n", " ");

		if (value.length() > TEXT_LENGTH_LIMIT) {
			value = value.substring(0, TEXT_LENGTH_LIMIT) + "...";
		}
		return value;
	}

}
