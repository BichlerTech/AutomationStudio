package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import java.util.UUID;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class GuidStringConverter implements IContentsStringConverter<UUID> {
	@Override
	public UUID convertFromString(String guid) {
		try {
			UUID guid_ = UUID.fromString(guid);
			return guid_;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	@Override
	public String convertToString(UUID guid) {
		return guid.toString();
	}
}
