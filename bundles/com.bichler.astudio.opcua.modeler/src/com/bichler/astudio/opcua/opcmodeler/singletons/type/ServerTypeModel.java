package com.bichler.astudio.opcua.opcmodeler.singletons.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import opc.sdk.core.node.mapper.NodeIdMapper;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.addressspace.xml.exporter.IServerTypeModel;

public class ServerTypeModel implements IServerTypeModel {
	/**
	 * Object Id - Type Id
	 * 
	 * Object Mapping to export
	 */
	private Map<ExpandedNodeId, ExpandedNodeId> objectMapping = new HashMap<ExpandedNodeId, ExpandedNodeId>();
	/**
	 * Type Id - Object Id's
	 */
	private Map<ExpandedNodeId, List<ExpandedNodeId>> typeMapping = new HashMap<>();

	public ServerTypeModel() {
	}

	public void addModelMapping(ExpandedNodeId sourceId, ExpandedNodeId typeTargetId) {
		ExpandedNodeId replace = objectMapping.put(sourceId, typeTargetId);
		List<ExpandedNodeId> sources = typeMapping.get(typeTargetId);
		if (replace != null) {
			List<ExpandedNodeId> replacedType = typeMapping.get(replace);
			if (replacedType != null) {
				replacedType.remove(sourceId);
			}
		}
		if (sources == null) {
			sources = new ArrayList<>();
			typeMapping.put(typeTargetId, sources);
		}
		sources.add(sourceId);
	}

	public void removeModelMapping(ExpandedNodeId sourceId) {
		ExpandedNodeId targetId = objectMapping.remove(sourceId);
		if (ExpandedNodeId.isNull(targetId)) {
			return;
		}
		// get sources from targe
		List<ExpandedNodeId> sources = typeMapping.get(targetId);
		if (sources == null) {
			return;
		}
		sources.remove(sourceId);
		// remove entry if empty
		if (sources.isEmpty()) {
			typeMapping.remove(targetId);
		}
	}

	@Override
	public ExpandedNodeId getTypeIdFromObject(ExpandedNodeId objectId) {
		return this.objectMapping.get(objectId);
	}

	@Override
	public List<ExpandedNodeId> getObjectsFromType(ExpandedNodeId typeId) {
		return this.typeMapping.get(typeId);
	}

	@Override
	public HashMap<ExpandedNodeId, ExpandedNodeId> getObjectMapping() {
		return new HashMap<>(this.objectMapping);
	}

	@Override
	public HashMap<ExpandedNodeId, List<ExpandedNodeId>> getTypeMapping() {
		return new HashMap<>(this.typeMapping);
	}

	/**
	 * Change a opc ua namespacetable which affects maybe nodeids.
	 * 
	 * @param namespaceTable
	 * @param mapping
	 */
	public void doChangeNamespaceTable(NamespaceTable newNsTable, NamespaceTable namespaceTable,
			Map<Integer, Integer> mapping) {
		// change object mapping nodeids
		Map<ExpandedNodeId, ExpandedNodeId> newObjMapping = new HashMap<>();
		for (Entry<ExpandedNodeId, ExpandedNodeId> objMapping : this.objectMapping.entrySet()) {
			ExpandedNodeId key = NodeIdMapper.mapNamespaceIndex(namespaceTable, objMapping.getKey(), mapping,
					newNsTable);
			ExpandedNodeId value = NodeIdMapper.mapNamespaceIndex(namespaceTable, objMapping.getValue(), mapping,
					newNsTable);
			newObjMapping.put(key, value);
		}
		this.objectMapping = newObjMapping;
		// change type mapping nodeids
		Map<ExpandedNodeId, List<ExpandedNodeId>> newTypeMapping = new HashMap<>();
		for (Entry<ExpandedNodeId, List<ExpandedNodeId>> typeMapping : this.typeMapping.entrySet()) {
			ExpandedNodeId key = NodeIdMapper.mapNamespaceIndex(namespaceTable, typeMapping.getKey(), mapping,
					newNsTable);
			List<ExpandedNodeId> entryIds = typeMapping.getValue();
			List<ExpandedNodeId> newValue = new ArrayList<>();
			for (ExpandedNodeId id : entryIds) {
				ExpandedNodeId newId = NodeIdMapper.mapNamespaceIndex(namespaceTable, id, mapping, newNsTable);
				newValue.add(newId);
			}
			newTypeMapping.put(key, newValue);
		}
		this.typeMapping = newTypeMapping;
	}
}
