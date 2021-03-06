package com.bichler.opc.driver.base.context;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ViewDescription;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.IOPCServerSession;

import opc.sdk.core.node.Node;
import opc.sdk.ua.FileHandle;
import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.IOPCSession;

public class DriverContext implements IOPCContext {
	private long driverId;
	private IOPCSession session;

	public DriverContext(long driverId) {
		this.driverId = driverId;
	}

	public DriverContext(long driverId, IOPCSession session) {
		this(driverId);
		this.session = session;
	}

	@Override
	public ReferenceDescription[] browse(NodeId node2browse, BrowseDirection direction) {
		BrowseDescription[] nodesToBrowse = new BrowseDescription[] {
				new BrowseDescription(node2browse, direction, Identifiers.HierarchicalReferences, true,
						NodeClass.getMask(NodeClass.ALL), BrowseResultMask.getMask(BrowseResultMask.ALL)) };
		UnsignedInteger requestedMaxReferencesPerNode = UnsignedInteger.ZERO;
		ViewDescription view = null;
		IOPCServerSession session = null;
		try {
			BrowseResult[] result = ComDRVManager.getDRVManager().getServer().getMaster().browse(nodesToBrowse,
					requestedMaxReferencesPerNode, view, session);
			if (result != null && result.length > 0 && result[0].getReferences() != null) {
				return result[0].getReferences();
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return new ReferenceDescription[0];
	}

	@Override
	public NodeId expandedNodeIdToNodeId(ExpandedNodeId expandedNodeId) {
		try {
			return ComDRVManager.getDRVManager().getServer().getNamespaceUris().toNodeId(expandedNodeId);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return NodeId.NULL;
	}

	@Override
	public void syncValueFromDriver(NodeId nodeId, Variant variant, long state) {
		ComDRVManager.getDRVManager().writeFromDriver(new NodeId[] { nodeId },
				new UnsignedInteger[] { Attributes.Value }, null, new DataValue[] { new DataValue(variant) },
				new Long[] { state });
	}

	@Override
	public long getDriverId() {
		return this.driverId;
	}

	IOPCSession getSession() {
		return this.session;
	}

	@Override
	public NodeId getSessionId() {
		if (this.session == null) {
			return NodeId.NULL;
		}
		return this.session.getSessionId();
	}

	@Override
	public AtomicInteger getSeqNrFilehandles() {
		if (this.session == null) {
			throw new IllegalArgumentException();
		}
		return this.session.getSeqNrFilehandles();
	}

	@Override
	public List<FileHandle> getFileHandles() {
		if (this.session == null) {
			throw new IllegalArgumentException();
		}
		return this.session.getFileHandles();
	}

	@Override
	public Node getNode(NodeId nodeIdId) {
		return ComDRVManager.getDRVManager().getServer().getAddressSpaceManager().getNodeById(nodeIdId);
	}

	@Override
	public NamespaceTable getNamespaceUris() {
		return ComDRVManager.getDRVManager().getServer().getNamespaceUris();
	}
}
