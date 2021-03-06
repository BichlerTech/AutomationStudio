package opc.sdk.server.service.opc.browse;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.user.AuthorityRule;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.managers.OPCAddressSpaceManager;
import opc.sdk.server.core.managers.OPCUserAuthentificationManager;
import opc.sdk.server.service.session.OPCServerSession;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.ViewDescription;

public class BrowseContinuationPoint {
	/** The id of the continuation point. */
	private UUID id;
	/** The view, which uses the continuation point. */
	private ViewDescription view;
	/** The node, which will be browsed. */
	private Node nodeToBrowse;
	/** The value of the maximal results to return. */
	private UnsignedInteger maxResultsToReturn;
	/** The current browse direction. */
	private BrowseDirection browseDirection;
	/** The reference type id for the browsed item. */
	private NodeId referenceTypeId;
	/**
	 * Includes all subtybes for the reference type id included when its true,
	 * otherwise just the specified type will be included .
	 */
	private boolean includeSubtypes;
	/** The filter nodeclass mask. */
	private EnumSet<NodeClass> nodeClassMask;
	/** The result mask for nodes to filter their attribute values. */
	private EnumSet<BrowseResultMask> resultMask;
	/** The index value. */
	private int index;
	/** The browse data. */
	private OPCBrowseSet data = null;
	/**
	 * Is the continuation point released after creating a result or can he be used
	 * later.
	 */
	private boolean isReleased = false;
	/** server session */
	private OPCServerSession session = null;
	/** opc type table */
	private TypeTable typeTable;

	public BrowseContinuationPoint() {
	}

	public void /* StatusCode */ dispose() {
		// StatusCode result = StatusCode.GOOD;
		// if (this.data == null) {
		// result = new StatusCode(StatusCodes.Bad_NoContinuationPoints);
		// } else if (this.data.getIndex() < this.data.getReferences().size()) {
		// result = new StatusCode(StatusCodes.Bad_ContinuationPointInvalid);
		// }
		this.data = null;
		// return result;
	}

	public StatusCode fetchReferences(OPCAddressSpaceManager manager, List<ReferenceDescription> references)
			throws ServiceResultException {
		StatusCode result = StatusCode.GOOD;
		while (!this.isReleased) {
			result = browse(references, manager);
			if (result.isBad()) {
				return result;
			}
			List<ReferenceDescription> filtered = new ArrayList<ReferenceDescription>();
			// check for incomplete reference descriptions
			for (int i = 0; i < references.size(); i++) {
				// filter nodeclass mask
				if (references.get(i).getNodeClass() == null && this.nodeClassMask.contains(NodeClass.Unspecified)) {
					continue;
				}
				if (!this.nodeClassMask.contains(references.get(i).getNodeClass())) {
					filtered.add(references.get(i));
				}
			}
			references.removeAll(filtered);
			// check if browse limit is reached
			if (!this.isReleased && references.size() >= this.maxResultsToReturn.longValue()) {
				setId(UUID.randomUUID());
				result = save();
				break;
			}
		}
		return result;
	}

	private boolean isDefaultViewDescription(ViewDescription view) {
		if (view == null) {
			return true;
		}
		if (NodeId.isNull(view.getViewId()) && view.getViewVersion().intValue() == 0
				&& view.getTimestamp().equals(DateTime.MIN_VALUE)) {
			return true;
		}
		Node viewNode = this.session.getServer().getAddressSpaceManager().getNodeById(view.getViewId());
		if (viewNode == null) {
			return false;
		}
		switch (viewNode.getNodeClass()) {
		case View:
			return true;
		default:
			return false;
		}
	}

	private StatusCode browse(List<ReferenceDescription> references, OPCAddressSpaceManager manager)
			throws ServiceResultException {
		// Check for view
		if (!isDefaultViewDescription(getView())) {
			throw new ServiceResultException(StatusCodes.Bad_ViewIdUnknown);
		}
		// check for valid handle
		if (this.nodeToBrowse == null) {
			return new StatusCode(StatusCodes.Bad_NodeIdUnknown);
		}
		OPCBrowseSet browser = (OPCBrowseSet) getData();
		// create browser instance
		if (browser == null) {
			browser = new OPCBrowseSet(this.referenceTypeId, this.includeSubtypes, this.browseDirection,
					this.nodeToBrowse, this.typeTable, this.view);
			setData(browser);
		}
		// apply references
		ReferenceNode reference = null;
		while ((reference = browser.next()) != null) {
			// create the type definition reference
			ReferenceDescription description = createReferenceDescription(reference, manager);
			Node referncedNode = manager.getNodeById(description.getNodeId());
			OPCUserAuthentificationManager userManager = manager.getUserAuthentificationManager();
			ServiceResult code = userManager.checkAuthorityFromNode(AuthorityRule.Browse, session, referncedNode);
			if (code.isBad()) {
				// skip
				continue;
			}
			// check if limit is reached
			if (this.maxResultsToReturn.longValue() != 0 && references.size() >= this.maxResultsToReturn.longValue()) {
				browser.push(reference);
				return StatusCode.GOOD;
				// return new StatusCode(StatusCodes.Good_MoreData);
			}
			references.add(description);
		}
		// release the continuation point if all done
		dispose();
		setReleased(true);
		return StatusCode.GOOD;
	}

	/**
	 * Creates a ReferenceDescription from a browsed Reference.
	 * 
	 * @param manager
	 * 
	 * @param Context           Browse Context.
	 * @param Cache             TODO: NOT USED.
	 * @param Reference         The Reference used to create the reference
	 *                          description.
	 * @param ContinuationPoint The ContinuationPoint created from the browse
	 *                          service.
	 * @param AddressSpace      AddressSpace that is browsed.
	 * @return The RefernceDescription from the ReferenceNode argument.
	 */
	private ReferenceDescription createReferenceDescription(ReferenceNode reference, OPCAddressSpaceManager manager) {
		ReferenceDescription description = new ReferenceDescription();
		// create expandedNodid with namespaceindex instead of namespaceuri
		ExpandedNodeId refnid = reference.getTargetId();
		if (refnid != null)
			refnid = new ExpandedNodeId(UnsignedInteger.ZERO, refnid.getNamespaceIndex(), refnid.getValue());
		description.setNodeId(refnid);
		// Set the following Attributs: [ReferenceType, IsForward] for the
		// ReferenceDescription
		if (this.resultMask.contains(BrowseResultMask.ReferenceTypeId)) {
			description.setReferenceTypeId(reference.getReferenceTypeId());
		} else {
			description.setReferenceTypeId(null);
		}
		if (this.resultMask.contains(BrowseResultMask.IsForward)) {
			description.setIsForward(!reference.getIsInverse());
		} else {
			description.setIsForward(null);
		}
		// do not cache target parameters for remote nodes
		// if (reference.getTargetId().isAbsolute()) {
		// // only return remote references if no node class filter is speciied
		// if (!this.nodeClassMask.equals(NodeClass.ALL)) {
		// return null;
		// }
		// return description;
		// }
		// check for reference
		Node target = manager.getNodeById(reference.getTargetId());
		if (target == null) {
			return description;
		}
		// lookup the typedefinintion
		ExpandedNodeId typeDefinition = null;
		if (target != null) {
			typeDefinition = target.findTarget(Identifiers.HasTypeDefinition, false);
			if (typeDefinition != null)
				typeDefinition = new ExpandedNodeId(UnsignedInteger.ZERO, typeDefinition.getNamespaceIndex(),
						typeDefinition.getValue());
		}
		// target attributes
		if (resultMask.contains(BrowseResultMask.NodeClass)) {
			description.setNodeClass(target.getNodeClass());
		} else {
			description.setNodeClass(null);
		}
		if (resultMask.contains(BrowseResultMask.BrowseName)) {
			description.setBrowseName(target.getBrowseName());
		} else {
			description.setBrowseName(null);
		}
		if (resultMask.contains(BrowseResultMask.DisplayName)) {
			description.setDisplayName(target.getDisplayName());
		} else {
			description.setDisplayName(null);
		}
		if (resultMask.contains(BrowseResultMask.TypeDefinition)) {
			description.setTypeDefinition(typeDefinition);
		} else {
			description.setTypeDefinition(null);
		}
		return description;
	}

	private StatusCode save() {
		StatusCode result = StatusCode.GOOD;
		if (this.session != null) {
			result = this.session.saveContinuationPoint(this);
		}
		return result;
	}

	public void setServerTypeTable(TypeTable typeTable) {
		this.typeTable = typeTable;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ViewDescription getView() {
		return view;
	}

	public void setView(ViewDescription view) {
		this.view = view;
	}

	public Node getNodeToBrowse() {
		return nodeToBrowse;
	}

	public void setNodeToBrowse(Node nodeToBrowse) {
		this.nodeToBrowse = nodeToBrowse;
	}

	public UnsignedInteger getMaxResultsToReturn() {
		return maxResultsToReturn;
	}

	public void setMaxResultsToReturn(UnsignedInteger maxResultsToReturn) {
		this.maxResultsToReturn = maxResultsToReturn;
	}

	public BrowseDirection getBrowseDirection() {
		return browseDirection;
	}

	public void setBrowseDirection(BrowseDirection browseDirection) {
		this.browseDirection = browseDirection;
	}

	public NodeId getReferenceTypeId() {
		return referenceTypeId;
	}

	public void setReferenceTypeId(NodeId referenceTypeId) {
		this.referenceTypeId = referenceTypeId;
	}

	public boolean isIncludeSubtypes() {
		return includeSubtypes;
	}

	public void setIncludeSubtypes(boolean includeSubtypes) {
		this.includeSubtypes = includeSubtypes;
	}

	public EnumSet<NodeClass> getNodeClassMask() {
		return nodeClassMask;
	}

	public void setNodeClassMask(UnsignedInteger nodeClassMask) {
		if (nodeClassMask.longValue() != 0) {
			this.nodeClassMask = NodeClass.getSet(nodeClassMask);
		} else {
			this.nodeClassMask = NodeClass.ALL;
		}
	}

	public EnumSet<BrowseResultMask> getResultMask() {
		return resultMask;
	}

	public void setResultMask(EnumSet<BrowseResultMask> resultMask) {
		this.resultMask = resultMask;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public OPCBrowseSet getData() {
		return data;
	}

	public void setData(OPCBrowseSet data) {
		this.data = data;
	}

	public boolean isReleased() {
		return isReleased;
	}

	public void setReleased(boolean isReleased) {
		this.isReleased = isReleased;
	}

	public OPCServerSession getSession() {
		return session;
	}

	public void setSession(OPCServerSession session) {
		this.session = session;
	}

	public void setViewDescription(ViewDescription view) {
		this.view = view;
	}
}
