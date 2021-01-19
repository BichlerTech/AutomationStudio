package opc.sdk.core.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import opc.sdk.core.node.mapper.NodeIdMapper;

public class ComplexTypes {
	private Map<NodeId, Class<?>> typeClasses = null;

	public ComplexTypes() {
		this.typeClasses = new HashMap<>();
	}

	/**
	 * unused implementation
	 */
	public void initialize() {
		// do nothing at the moment
	}

	public void add(NodeId datatype, Class<?> class2add) {
		this.typeClasses.put(datatype, class2add);
	}

	public Class<?> getClassFromDataType(NodeId datatype) {
		return this.typeClasses.get(datatype);
	}

	public Class<?> remove(NodeId datatype) {
		return this.typeClasses.remove(datatype);
	}

	/**
	 * Change server's namespace table.
	 * 
	 * @param namespaceTable PREPARED FOR EXPANDEDNODEID
	 * @param namespaceTable
	 * @param mapping
	 */
	public void doChangeNamespaceTable(NamespaceTable namespaceTable, Map<Integer, Integer> mapping) {
		Map<NodeId, Class<?>> newTypes = new HashMap<>();
		for (Entry<NodeId, Class<?>> type : this.typeClasses.entrySet()) {
			NodeId newId = NodeIdMapper.mapNamespaceIndex(type.getKey(), mapping);
			newTypes.put(newId, type.getValue());
		}
		this.typeClasses = newTypes;
	}
}
