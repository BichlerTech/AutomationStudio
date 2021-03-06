package opc.sdk.server.service.opc.browse;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.node.Node;
import opc.sdk.core.types.TypeTable;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.ViewDescription;

public class OPCBrowseSet {
	/** Direction of the browse */
	private BrowseDirection browseDirection = null;
	/** Index */
	private int index = -1;
	/** Include all Subtypes from the References */
	private boolean includeSubtypes;
	/** BrowseName of the starting Node to browse */
	private Node node = null;
	/** Pushback reference node */
	private ReferenceNode pushBack = null;
	/** Browsed References */
	private List<ReferenceNode> references = null;
	/** References to browse */
	private NodeId referenceTypeId = null;
	/** View to browse */
	private ViewDescription view = null;
	private TypeTable typeTable;

	public OPCBrowseSet(NodeId referenceTypeId, boolean includeSubtypes, BrowseDirection browseDirection, Node node,
			TypeTable typeTable, ViewDescription viewDescription) {
		this.view = viewDescription;
		this.referenceTypeId = referenceTypeId;
		this.includeSubtypes = includeSubtypes;
		this.browseDirection = browseDirection;
		this.node = node;
		this.references = new ArrayList<ReferenceNode>();
		this.index = 0;
		this.typeTable = typeTable;
		populate();
	}

	/**
	 * Browses the next Reference.
	 * 
	 * @return The next Reference.
	 */
	public ReferenceNode next() {
		ReferenceNode reference = null;
		// always return the previous pushed reference first
		if (this.pushBack != null) {
			reference = this.pushBack;
			this.pushBack = null;
			return reference;
		}
		if (this.index < this.references.size()) {
			return this.references.get(this.index++);
		}
		return null;
	}

	/**
	 * Populates the browser with references that meet the criteria of the browse
	 * request.
	 * 
	 * @param context   The Context from the Browse operation.
	 * @param browser   The browser used to execute the service.
	 * @param typeTable
	 */
	public void populate() {
		// get the reference type being browsed
		NodeId referenceTypeId = getReferenceTypeId();
		if (NodeId.isNull(referenceTypeId) || Identifiers.References.equals(referenceTypeId)) {
			referenceTypeId = null;
		}
		// add any references
		if (this.node.getReferences() != null) {
			if (referenceTypeId == null) {
				for (ReferenceNode reference : this.node.getReferences()) {
					if (reference.getIsInverse()) {
						if (BrowseDirection.Forward.equals(getBrowseDirection())) {
							continue;
						}
					} else {
						if (BrowseDirection.Inverse.equals(getBrowseDirection())) {
							continue;
						}
					}
					add(reference);
				}
			} else {
				List<ReferenceNode> references = null;
				if (BrowseDirection.Inverse.equals(getBrowseDirection())
						|| BrowseDirection.Both.equals(getBrowseDirection())) {
					// with subtypes
					if (isIncludeSubtypes()) {
						references = findReferences(this.node, typeTable, true);
					}
					// without subtypes
					else {
						references = findReferences(this.node, true);
					}
					for (int ii = 0; ii < references.size(); ii++) {
						add(references.get(ii));
					}
				}
				if (BrowseDirection.Forward.equals(getBrowseDirection())
						|| BrowseDirection.Both.equals(getBrowseDirection())) {
					// with subtypes
					if (isIncludeSubtypes()) {
						references = findReferences(this.node, typeTable, false);
					}
					// without subtypes
					else {
						references = findReferences(this.node, false);
					}
					for (int ii = 0; ii < references.size(); ii++) {
						add(references.get(ii));
					}
				}
			}
		}
	}

	public void push(ReferenceNode reference) {
		this.pushBack = reference;
	}

	/**
	 * Adds a ReferenceNode to the BrowseResult.
	 * 
	 * @param reference
	 */
	private void add(ReferenceNode reference) {
		if (reference == null) {
			return;
		}
		this.references.add(reference);
	}

	/**
	 * Returns a list of references that match the direction and reference type
	 * 
	 * @param Node      Node to start the browse.
	 * @param IsInverse Direction of the References.
	 * @return List of all browsed ReferenceNodes.
	 */
	private List<ReferenceNode> findReferences(Node node, boolean isInverse) {
		List<ReferenceNode> hits = new ArrayList<ReferenceNode>();
		// check for null
		if (NodeId.isNull(this.referenceTypeId)) {
			return hits;
		}
		// ReferenceNode entry = null;
		for (ReferenceNode entry2 : node.getReferences()) {
			if (!this.referenceTypeId.equals(entry2.getReferenceTypeId())) {
				continue;
			}
			find(entry2, isInverse, hits);
		}
		// if (entry == null) {
		// return hits;
		// }
		// find the reference
		return hits;
	}

	/**
	 * Browse all References from the Node argument and Direction argument, used by
	 * the Browser.
	 * 
	 * @param Node      Node to start the browse.
	 * @param TypeTable TypeTable from the Server, to know the ReferenceTypes.
	 * @param IsInverse Direction of the References.
	 * @return List of all browsed ReferenceNodes.
	 */
	private List<ReferenceNode> findReferences(Node node, TypeTable typeTable, boolean isInverse) {
		if (typeTable == null) {
			throw new IllegalArgumentException("TypeTable");
		}
		List<ReferenceNode> hits = new ArrayList<ReferenceNode>();
		// check for null
		if (NodeId.isNull(this.referenceTypeId)) {
			return hits;
		}
		for (ReferenceNode entry : node.getReferences()) {
			if (typeTable.isTypeOf(entry.getReferenceTypeId(), this.referenceTypeId)) {
				find(entry, isInverse, hits);
			}
		}
		return hits;
	}

	/**
	 * Find the ReferenceNodes and store it in the Hits argument.
	 * 
	 * @param Entry     Reference to store.
	 * @param IsInverse Direction of the Reference.
	 * @param Hits      List to collect all References.
	 */
	private void find(ReferenceNode entry, boolean isInverse, List<ReferenceNode> hits) {
		if (isInverse) {
			if (entry.getIsInverse()) {
				hits.add(entry);
			}
		} else {
			if (!entry.getIsInverse()) {
				hits.add(entry);
			}
		}
	}

	public BrowseDirection getBrowseDirection() {
		return browseDirection;
	}

	public void setBrowseDirection(BrowseDirection browseDirection) {
		this.browseDirection = browseDirection;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isIncludeSubtypes() {
		return includeSubtypes;
	}

	public void setIncludeSubtypes(boolean includeSubtypes) {
		this.includeSubtypes = includeSubtypes;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
	// public ReferenceNode getPushBack() {
	// return pushBack;
	// }

	// public void setPushBack(ReferenceNode pushBack) {
	// this.pushBack = pushBack;
	// }
	public List<ReferenceNode> getReferences() {
		return references;
	}

	public void setReferences(List<ReferenceNode> references) {
		this.references = references;
	}

	public NodeId getReferenceTypeId() {
		return referenceTypeId;
	}

	public void setReferenceTypeId(NodeId referenceTypeId) {
		this.referenceTypeId = referenceTypeId;
	}

	public ViewDescription getView() {
		return view;
	}

	public void setView(ViewDescription view) {
		this.view = view;
	}
}
