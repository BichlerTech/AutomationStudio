package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class NamespaceIntegerStringConverter implements IContentsStringConverter<Integer> {
	@Override
	public Integer convertFromString(String value) {
		return ServerInstance.getInstance().getServerInstance().getNamespaceUris().getIndex(value);
	}

	@Override
	public String convertToString(Integer value) {
		return ServerInstance.getInstance().getServerInstance().getNamespaceUris().getUri(value);
	}
}
