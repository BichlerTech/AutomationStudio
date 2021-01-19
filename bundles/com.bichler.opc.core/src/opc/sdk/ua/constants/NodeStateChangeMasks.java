package opc.sdk.ua.constants;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Enum of the nodestate changes.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public enum NodeStateChangeMasks {
	/**
	 * Indicates what has changed in a node.
	 */
	/**
	 * None has changed
	 */
	NONE(0x00),
	/**
	 * One or more children have been added, removed or replaced.
	 */
	CHILDREN(0x01),
	/**
	 * One or more references have been added or removed.
	 */
	REFERENCES(0x02),
	/**
	 * The value attribute has changed.
	 */
	VALUE(0x04),
	/**
	 * One or more non-value attribute has changed.
	 */
	NONVALUE(0x08),
	/**
	 * The node has been deleted.
	 */
	DELETED(0x10);

	int value = -1;

	private NodeStateChangeMasks(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	public static Set<NodeStateChangeMasks> getSet(int mask) {
		List<NodeStateChangeMasks> res = new ArrayList<>();
		for (NodeStateChangeMasks l : NodeStateChangeMasks.values())
			if ((mask & l.value) == l.value)
				res.add(l);
		return EnumSet.copyOf(res);
	}
}
