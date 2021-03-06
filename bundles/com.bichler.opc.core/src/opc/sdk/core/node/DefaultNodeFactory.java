package opc.sdk.core.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.utils.BijectionMap;

/**
 * Default NodeId Factory generates Unique ids within an AddressSpace
 * 
 * @author Kofi-Eagle
 * 
 */
public class DefaultNodeFactory extends AbstractNodeFactory {
	/**
	 * mapping for last nodeIds
	 */
	private BijectionMap<Integer, NodeId> nodeIdMapping = null;

	public DefaultNodeFactory() {
		super();
		this.nodeIdMapping = new BijectionMap<>();
	}

	@Override
	public NodeId getNextNodeId(int namespaceIndex, Object value, IdType type, NodeIdMode mode) {
		NodeId nextNumber = showNextNodeId(namespaceIndex, value, type, mode);
		asLastIfPossibleId(nextNumber);
		return nextNumber;
	}

	/**
	 * Shows the next available UNSIGNEDINTEGER nodeId
	 * 
	 * @param namespaceIndex
	 * @return
	 */
	@Override
	public NodeId showNextNodeId(int namespaceIndex, IdType type, NodeIdMode mode) {
		return showNextNodeId(namespaceIndex, null, type, mode);
	}

	private void addNumericNodeId(List<?> nodeIdList, Map<IdType, List<? extends Object>> nodeIds2, Object nodeIdValue,
			NodeId nodeId) {
		if (nodeIdList == null) {
			nodeIdList = new ArrayList<>();
			nodeIds2.put(nodeId.getIdType(), nodeIdList);
		}
		if (nodeId.getIdType() != IdType.Numeric) {
			return;
		}
		if (nodeIdList.contains(nodeIdValue)) {
			return;
		}
		((List<UnsignedInteger>) nodeIdList).add((UnsignedInteger) nodeIdValue);
	}

	private void addOpaqueNodeId(List<?> nodeIdList, Map<IdType, List<? extends Object>> nodeIds2, Object nodeIdValue,
			NodeId nodeId) {
		List<?> tmpNodeIdList = nodeIdList;
		if (tmpNodeIdList == null) {
			tmpNodeIdList = new ArrayList<>();
			nodeIds2.put(nodeId.getIdType(), tmpNodeIdList);
		}
		if (nodeId.getIdType() != IdType.Opaque) {
			return;
		}
		if (tmpNodeIdList.contains(nodeIdValue)) {
			return;
		}
		((List<byte[]>) tmpNodeIdList).add((byte[]) nodeIdValue);
	}

	private void addStringNodeId(List<?> nodeIdList, Map<IdType, List<? extends Object>> nodeIds2, Object nodeIdValue,
			NodeId nodeId) {
		List<?> tmpNodeIdList = nodeIdList;
		if (tmpNodeIdList == null) {
			tmpNodeIdList = new ArrayList<>();
			nodeIds2.put(nodeId.getIdType(), tmpNodeIdList);
		}
		if (nodeId.getIdType() != IdType.String) {
			return;
		}
		if (tmpNodeIdList.contains(nodeIdValue)) {
			return;
		}
		((List<String>) tmpNodeIdList).add((String) nodeIdValue);
	}

	private NodeId showNextNodeId(int namespaceIndex, Object value, IdType type, NodeIdMode mode) {
		boolean missing = true;
		switch (type) {
		case Numeric:
			int index = 1;
			if (value == null) {
				value = UnsignedInteger.ZERO;
			}
			while (missing) {
				UnsignedInteger id = new UnsignedInteger(index);
				// find gaping ids (ids in the middle)
				switch (mode) {
				case FILL: {
					// existing address space nodeids
					Map<IdType, List<? extends Object>> nodeIds2 = findNodeIds(namespaceIndex);
					if (((List<UnsignedInteger>) nodeIds2.get(type)).contains(id)) {
						index++;
						continue;
					}
					// parent id is the newest
					if (((UnsignedInteger) value).intValue() >= index) {
						index++;
						continue;
					}
					// type node structure
					NodeId lId = getLastNodeId(namespaceIndex);
					if (lId == null) {
						lId = new NodeId(namespaceIndex, 0);
						asLastIfPossibleId(lId);
					}
					int lastIndex = ((UnsignedInteger) lId.getValue()).inc().intValue();
					// use last index
					if (lastIndex > index) {
						index = lastIndex;
						continue;
					}
					// new last
					missing = false;
				}
					break;
				case APPEND:
					NodeId lastId = getLastNodeId(namespaceIndex);
					if (lastId == null) {
						lastId = new NodeId(namespaceIndex, 0);
						asLastIfPossibleId(lastId);
					}
					UnsignedInteger lId = (UnsignedInteger) lastId.getValue();
					index = lId.inc().intValue();
					missing = false;
					break;
				case CONTINUE: {
					// existing address space nodeids
					Map<IdType, List<? extends Object>> nodeIds2 = findNodeIds(namespaceIndex);
					if (((List<UnsignedInteger>) nodeIds2.get(type)).contains(id)
							|| ((UnsignedInteger) value).longValue() > id.longValue()) {
						index++;
						continue;
					}
					// new last
					missing = false;
				}
					break;
				default:
					break;
				}
			}
			return new NodeId(namespaceIndex, index);
		case Guid:
			return new NodeId(namespaceIndex, UUID.randomUUID());
		case Opaque: {
			Random random = new Random();
			byte[] opaque = new byte[8];
			// existing address space nodeids
			Map<IdType, List<? extends Object>> nodeIds2 = findNodeIds(namespaceIndex);
			while (missing) {
				random.nextBytes(opaque);
				if (((List<byte[]>) nodeIds2.get(type)).contains(opaque)) {
					continue;
				}
				// new last
				missing = false;
			}
			return new NodeId(namespaceIndex, opaque);
		}
		case String: {
			String textual = (value instanceof String) ? (String) value : "1";
			// existing address space nodeids
			Map<IdType, List<? extends Object>> nodeIds2 = findNodeIds(namespaceIndex);
			while (missing) {
				if (((List<String>) nodeIds2.get(type)).contains(textual)) {
					textual += "1";
					// textual = Integer.toString(Integer.parseInt(textual) + 1);
					continue;
				}
				// new last
				missing = false;
			}
			return new NodeId(namespaceIndex, textual);
		}
		}
		return null;
	}

	public void asLastIfPossibleId(NodeId newNodeId) {
		NodeId lastId = getLastNodeId(newNodeId.getNamespaceIndex());
		if (lastId == null) {
			if (newNodeId.getValue() instanceof UnsignedInteger) {
				this.nodeIdMapping.map(newNodeId.getNamespaceIndex(), newNodeId);
			}
		} else {
			if (newNodeId.getValue() instanceof UnsignedInteger && ((UnsignedInteger) newNodeId.getValue())
					.intValue() > ((UnsignedInteger) lastId.getValue()).intValue()) {
				this.nodeIdMapping.map(newNodeId.getNamespaceIndex(), newNodeId);
			}
		}
	}

	private NodeId getLastNodeId(Integer index) {
		return this.nodeIdMapping.getRight(index);
	}

	protected Map<IdType, List<? extends Object>> findNodeIds(int namespaceIndex) {
		return new HashMap<>();
	}

	@Override
	void onCreate(Node node) {
		asLastIfPossibleId(node.getNodeId());
	}

	@Override
	public void onRemove(Integer[] indizes2refresh) {
		for (Integer index : indizes2refresh) {
			Map<IdType, List<? extends Object>> nodeids = findNodeIds(0);
			List<? extends Object> numericIds = nodeids.get(IdType.Numeric);
			UnsignedInteger last = UnsignedInteger.ZERO;
			for (Object u : numericIds) {
				if (last.longValue() < ((UnsignedInteger) u).longValue()) {
					last = (UnsignedInteger) u;
				}
			}
			if (UnsignedInteger.ZERO.equals(last)) {
				nodeIdMapping.removeWithLeft(index);
			} else {
				nodeIdMapping.map(index, new NodeId(index, last));
			}
		}
	}
}
