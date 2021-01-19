package com.bichler.astudio.opcua.addressspace.xml.exporter;

import java.util.HashMap;
import java.util.List;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;

public interface IServerTypeModel {

	public HashMap<ExpandedNodeId, ExpandedNodeId> getObjectMapping();

	public List<ExpandedNodeId> getObjectsFromType(ExpandedNodeId typeId);
	
	public ExpandedNodeId getTypeIdFromObject(ExpandedNodeId objectId);
	
	public HashMap<ExpandedNodeId, List<ExpandedNodeId>> getTypeMapping();
}
