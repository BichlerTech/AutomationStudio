package opc.sdk.core.classes.ua;

import org.opcfoundation.ua.builtintypes.NodeId;

/**
 * Stores a refernece between NodeManagers that needs to be created or deleted.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class LocalReference {
	private NodeId sourceId = null;
	private NodeId referenceTypeId = null;
	private Boolean isInverse = null;
	private NodeId targetId = null;

	/**
	 * Initializes the the reference.
	 * 
	 * @param sourceId
	 * @param referenceTypeId
	 * @param isInverse
	 * @param targetId
	 */
	public LocalReference(NodeId sourceId, NodeId referenceTypeId, boolean isInverse, NodeId targetId) {
		this.sourceId = sourceId;
		this.referenceTypeId = referenceTypeId;
		this.isInverse = isInverse;
		this.targetId = targetId;
	}

	/**
	 * The source of the reference.
	 * 
	 * @return SourceId
	 */
	public NodeId getSourceId() {
		return sourceId;
	}

	/**
	 * The type of reference.
	 * 
	 * @return ReferenceTypeId
	 */
	public NodeId getReferenceTypeId() {
		return referenceTypeId;
	}

	/**
	 * True if the reference is an inverse reference.
	 * 
	 * @return IsInverse
	 */
	public Boolean getIsInverse() {
		return isInverse;
	}

	/**
	 * The target of the reference.
	 * 
	 * @return TargetId
	 */
	public NodeId getTargetId() {
		return targetId;
	}
}
