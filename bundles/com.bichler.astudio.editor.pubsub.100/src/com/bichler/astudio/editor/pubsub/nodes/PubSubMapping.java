package com.bichler.astudio.editor.pubsub.nodes;

import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.ContentFilter;
import org.opcfoundation.ua.core.DataSetMetaDataType;
import org.opcfoundation.ua.core.KeyValuePair;
import org.opcfoundation.ua.core.PublishedVariableDataType;
import org.opcfoundation.ua.core.SimpleAttributeOperand;

// TODO move class to pubsub bundle

public class PubSubMapping {



	class UA_PublishedDataItemsTemplateConfig {
		DataSetMetaDataType metaData;
		private int variablesToAddSize;
		private PublishedVariableDataType variablesToAdd;
	}

	class UA_PublishedEventConfig {
		private NodeId eventNotfier;
		private ContentFilter filter;
	}

	class UA_PublishedEventTemplateConfig {
		private DataSetMetaDataType metaData;
		private NodeId eventNotfier;
		private int selectedFieldsSize;
		private SimpleAttributeOperand selectedFields;
		private ContentFilter filter;
	}

	class UA_PublishedDataSetConfig {
		
	}

}
