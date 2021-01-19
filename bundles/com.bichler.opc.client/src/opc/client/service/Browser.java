package opc.client.service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.BuiltinsMap;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseNextRequest;
import org.opcfoundation.ua.core.BrowseNextResponse;
import org.opcfoundation.ua.core.BrowsePath;
import org.opcfoundation.ua.core.BrowsePathTarget;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.RelativePath;
import org.opcfoundation.ua.core.RelativePathElement;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsRequest;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsResponse;
import org.opcfoundation.ua.core.ViewDescription;

import opc.sdk.core.enums.RequestType;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.ua.constants.BrowseNames;

/**
 * Browse the address space and stores the operation.
 *
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class Browser {
	public static final String ID = "opc.client.service.Browser";
	private ClientSession session = null;
	private UnsignedInteger maxReferencesReturned = null;
	private BrowseDirection browseDirection = null;
	private NodeId referenceTypeId = null;
	private Boolean includeSubtypes = null;
	private UnsignedInteger nodeClassMask = null;
	private UnsignedInteger resultMask = null;
	private BrowseDescription[] currentBrowseDescriptions = null;
	private static final int MAX_CHARACTERS = 500;

	/**
	 * Creates an unattached instance of a browser
	 */
	Browser() {
		initialize();
	}

	public Browser(ClientSession session) {
		initialize();
		this.session = session;
	}

	public BrowseResponse browse(BrowseRequest request, boolean async, boolean validateResults)
			throws ServiceResultException {
		BrowseResponse response;
		if (!async)
			response = this.session.browse(request);
		else
			response = this.session.beginBrowse(request, Identifiers.BrowseRequest);
		if (response == null)
			throw new NullPointerException("Browse response is null!");
		endBrowse(request, response, validateResults);
		setResults(response.getResults());
		return response;
	}

	public BrowseNextResponse beginBrowseNext(BrowseNextRequest request) throws ServiceResultException {
		NodeId requestId = Identifiers.BrowseNextRequest;
		BrowseNextResponse response = this.session.beginBrowseNext(request, requestId);
		if (response.getResults() != null) {
			setResults(response.getResults());
		}
		return response;
	}

	public BrowseNextResponse browseNext(BrowseNextRequest request) throws ServiceResultException {
		BrowseNextResponse response = this.session.browseNext(request);
		setResults(response.getResults());
		return response;
	}

	public ReferenceDescription[] browseSuperTypes(NodeId typeId) throws ServiceResultException {
		List<ReferenceDescription> superTypes = new ArrayList<>();
		BrowseDescription nodeToBrowse = new BrowseDescription();
		nodeToBrowse.setNodeId(typeId);
		nodeToBrowse.setBrowseDirection(BrowseDirection.Inverse);
		nodeToBrowse.setReferenceTypeId(Identifiers.HasSubtype);
		nodeToBrowse.setIncludeSubtypes(true);
		nodeToBrowse.setNodeClassMask(UnsignedInteger.ZERO);
		nodeToBrowse.setResultMask(BrowseResultMask.ALL);
		BrowseRequest request = new BrowseRequest();
		request.setNodesToBrowse(new BrowseDescription[] { nodeToBrowse });
		request.setRequestedMaxReferencesPerNode(new UnsignedInteger(100));
		ReferenceDescription[] fields = browse(request, true, true).getResults()[0].getReferences();
		browseSuperTypes(nodeToBrowse, fields, superTypes);
		return superTypes.toArray(new ReferenceDescription[superTypes.size()]);
	}

	private void browseSuperTypes(BrowseDescription nodeToBrowse, ReferenceDescription[] fields,
			List<ReferenceDescription> superTypes) throws ServiceResultException {
		if (fields == null) {
			return;
		}
		for (ReferenceDescription ref : fields) {
			superTypes.add(ref);
			// only follow on this server
			if (ref.getNodeId().isAbsolute()) {
				break;
			}
			// get the references for the next level up
			nodeToBrowse.setNodeId(session.getNamespaceUris().toNodeId(ref.getNodeId()));
			BrowseRequest request = new BrowseRequest();
			request.setNodesToBrowse(new BrowseDescription[] { nodeToBrowse });
			request.setRequestedMaxReferencesPerNode(new UnsignedInteger(100));
			BrowseResult[] results = browse(request, true, true).getResults();
			ReferenceDescription[] fields2 = results[0].getReferences();
			if (fields2 == null) {
				fields2 = new ReferenceDescription[0];
			}
			browseSuperTypes(nodeToBrowse, fields2, superTypes);
		}
	}

	public Variant browseMethodInputArguments(NodeId objectId) throws ServiceResultException {
		BrowseRequest request = new BrowseRequest();
		BrowseDescription nodeToBrowse = new BrowseDescription();
		nodeToBrowse.setNodeId(objectId);
		nodeToBrowse.setBrowseDirection(BrowseDirection.Forward);
		nodeToBrowse.setReferenceTypeId(Identifiers.HasProperty);
		nodeToBrowse.setIncludeSubtypes(true);
		nodeToBrowse.setNodeClassMask(NodeClass.getMask(NodeClass.Variable));
		nodeToBrowse.setResultMask(BrowseResultMask.ALL);
		request.setNodesToBrowse(new BrowseDescription[] { nodeToBrowse });
		request.setRequestedMaxReferencesPerNode(new UnsignedInteger(10));
		ReferenceDescription[] argumentReferences = browse(request, true, true).getResults()[0].getReferences();
		// no result or no input arguments
		if (argumentReferences != null && argumentReferences.length > 0) {
			for (ReferenceDescription reference : argumentReferences) {
				if (BrowseNames.INPUTARGUMENTS.equalsIgnoreCase(reference.getBrowseName().getName())) {
					ReadRequest read = new ReadRequest();
					read.setNodesToRead(new ReadValueId[] {
							new ReadValueId(session.getNamespaceUris().toNodeId(reference.getNodeId()),
									Attributes.Value, null, null) });
					DataValue[] readValue = session.read(read).getResults();
					if (readValue != null && readValue.length > 0 && readValue[0] != null) {
						return readValue[0].getValue();
					}
					break;
				}
			}
		}
		return null;
	}

	public Class<?> fetchBuildinType(NodeId nodeId) throws ServiceResultException {
		if (NodeId.isNull(nodeId)) {
			return null;
		}
		Class<?> class2build = BuiltinsMap.ID_CLASS_MAP.getRight(nodeId);
		if (class2build == null) {
			ReferenceDescription[] superType = browseSuperTypes(nodeId);
			if (superType != null && superType.length > 0 && superType[0] != null) {
				return fetchBuildinType(this.session.getNamespaceUris().toNodeId(superType[0].getNodeId()));
			}
		}
		return class2build;
	}

	/**
	 * @param nodesToBrowse
	 * @return
	 */
	public BrowseDescription[] buildBrowseDescriptions(NodeId[] nodesToBrowse) {
		if (nodesToBrowse == null || nodesToBrowse.length < 1) {
			throw new IllegalArgumentException("BrowseDescription");
		}
		this.currentBrowseDescriptions = new BrowseDescription[nodesToBrowse.length];
		for (int ii = 0; ii < nodesToBrowse.length; ii++) {
			this.currentBrowseDescriptions[ii] = new BrowseDescription();
			this.currentBrowseDescriptions[ii].setBrowseDirection(this.browseDirection);
			this.currentBrowseDescriptions[ii].setIncludeSubtypes(this.includeSubtypes);
			this.currentBrowseDescriptions[ii].setNodeClassMask(this.nodeClassMask);
			this.currentBrowseDescriptions[ii].setNodeId(nodesToBrowse[ii]);
			this.currentBrowseDescriptions[ii].setReferenceTypeId(this.referenceTypeId);
			this.currentBrowseDescriptions[ii].setResultMask(this.resultMask);
		}
		return this.currentBrowseDescriptions;
	}

	/**
	 * Builds browseDescription with parameters
	 *
	 * @param nodesToBrowse
	 * @return
	 */
	public BrowseDescription[] buildBrowseDescriptions(NodeId[] nodesToBrowse, BrowseDirection[] browseDirection,
			boolean[] includeSubtypes, UnsignedInteger[] nodeClassMask, NodeId[] referenceType,
			UnsignedInteger[] resultMask) {
		if (nodesToBrowse == null || nodesToBrowse.length < 1) {
			throw new IllegalArgumentException("BrowseDescription");
		}
		this.currentBrowseDescriptions = new BrowseDescription[nodesToBrowse.length];
		for (int ii = 0; ii < nodesToBrowse.length; ii++) {
			this.currentBrowseDescriptions[ii] = new BrowseDescription();
			this.currentBrowseDescriptions[ii].setBrowseDirection(browseDirection[ii]);
			this.currentBrowseDescriptions[ii].setIncludeSubtypes(includeSubtypes[ii]);
			this.currentBrowseDescriptions[ii].setNodeClassMask(nodeClassMask[ii]);
			this.currentBrowseDescriptions[ii].setNodeId(nodesToBrowse[ii]);
			this.currentBrowseDescriptions[ii].setReferenceTypeId(referenceType[ii]);
			this.currentBrowseDescriptions[ii].setResultMask(resultMask[ii]);
		}
		return this.currentBrowseDescriptions;
	}

	public UnsignedInteger getRequestedMaxReferencesPerNode() {
		return this.maxReferencesReturned;
	}

	public ViewDescription getView() {
		return null;
	}

	public void setBrowseDirection(BrowseDirection browseDirection) {
		this.browseDirection = browseDirection;
	}

	public void setReferenceTypeId(NodeId referenceTypeId) {
		this.referenceTypeId = referenceTypeId;
	}

	public void setIncludeSubtypes(Boolean includeSubtypes) {
		this.includeSubtypes = includeSubtypes;
	}

	public void setNodeClassMask(UnsignedInteger nodeClassMask) {
		this.nodeClassMask = nodeClassMask;
	}

	public void setResultMask(UnsignedInteger resultMask) {
		this.resultMask = resultMask;
	}

	/**
	 * Sets all private instance variables to default values.
	 */
	private void initialize() {
		this.session = null;
		this.maxReferencesReturned = new UnsignedInteger(1000);
		this.browseDirection = BrowseDirection.Both;
		this.referenceTypeId = null;
		this.includeSubtypes = true;
		this.nodeClassMask = new UnsignedInteger(0);
		this.resultMask = new UnsignedInteger(BrowseResultMask.All.getValue());
	}

	private void endBrowse(BrowseRequest request, BrowseResponse response, boolean validateResults) {
		// CTT8.1- 007
		if (response == null || response.getResults() == null || response.getResults().length == 0) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "{0} Response has empty results!",
					new String[] { RequestType.Browse.name() });
			return;
		}
		for (int i = 0; i < response.getResults().length; i++) {
			if (response.getResults()[i].getReferences() == null
					|| response.getResults()[i].getReferences().length == 0) {
				continue;
			}
			if (validateResults)
				validateBrowseResults(response.getResults()[i].getReferences(), request.getNodesToBrowse()[i]);
		}
	}

	private void setResults(BrowseResult[] results) {
		if (results == null || results.length == 0) {
			return;
		}
		List<byte[]> continuationPoints = new ArrayList<>();
		for (BrowseResult result : results) {
			// check for error
			if (result.getStatusCode().isBad()) {
				// this error indicates that the server does not have enough
				// simultaneously active
				// continuation points. This request will need to be resent
				// after th other operations
				// have been completed and their continuation points released.
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "{0} {1}",
						new String[] { RequestType.Browse.name(), result.getStatusCode().getDescription() });
				continue;
			}
			if (result.getReferences() == null || result.getReferences().length == 0) {
				continue;
			}
			// save
			List<ReferenceDescription> validatedReferences = new ArrayList<>();
			for (ReferenceDescription refDesc : result.getReferences()) {
				if (refDesc != null) {
					validatedReferences.add(refDesc);
				}
			}
			result.setReferences(validatedReferences.toArray(new ReferenceDescription[0]));
			// check for continuation points
			if (result.getContinuationPoint() != null) {
				continuationPoints.add(ByteString.asByteArray(result.getContinuationPoint()));
			}
		}
	}

	private void validateBrowseResults(ReferenceDescription[] references, BrowseDescription description) {
		BrowseDirection direction = description.getBrowseDirection();
		EnumSet<BrowseResultMask> brm = BrowseResultMask.getSet(description.getResultMask());
		for (int i = 0; i < references.length; i++) {
			// REFTYPID
			NodeId refType = references[i].getReferenceTypeId();
			if (brm.contains(BrowseResultMask.ReferenceTypeId) && NodeId.isNull(refType)) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Incorrect information returned in ReferenceDescription.ReferenceTypeId!",
						new String[] { RequestType.Browse.name() });
			}
			// ISFORWARD
			boolean isForward = references[i].getIsForward();
			if (brm.contains(BrowseResultMask.IsForward) && direction != null) {
				if (direction == BrowseDirection.Forward && !isForward) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} Incorrect information returned in ReferenceDescription.IsForward! {1}",
							new Object[] { RequestType.Browse.name(),
									new StatusCode(StatusCodes.Bad_ViewParameterMismatch) }/*
																							 * , request, response, null
																							 */);
				} else if (direction == BrowseDirection.Inverse && isForward) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} Incorrect information returned in ReferenceDescription.IsForward! {1}", new Object[] {
									RequestType.Browse.name(), new StatusCode(StatusCodes.Bad_ViewParameterMismatch) });
				}
			}
			boolean isNodeIdValid = true;
			// NODEID
			try {
				NodeId nodeId = session.getNamespaceUris().toNodeId(references[i].getNodeId());
				if (NodeId.isNull(nodeId)) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} Incorrect information returned in ReferenceDescription.NodeId! {1}",
							new Object[] { RequestType.Browse.name(),
									new StatusCode(StatusCodes.Bad_ViewParameterMismatch) }/*
																							 * , request, response, null
																							 */);
					isNodeIdValid = false;
				} else if (references[i].getNodeId().isAbsolute()) {
					Logger.getLogger(getClass().getName()).log(Level.FINE,
							"{0} The node {1} references to a remote server!",
							new Object[] { RequestType.Browse.name(), nodeId });
				}
				// NodeId nodeId2Check =
				// NodeIdUtil.createNodeId(nodeId.getNamespaceIndex(),
				// nodeId.getValue());
				if (Identifiers.ObjectNode.equals(nodeId) || Identifiers.VariableNode.equals(nodeId)
						|| Identifiers.ObjectTypeNode.equals(nodeId) || Identifiers.VariableTypeNode.equals(nodeId)
						|| Identifiers.MethodNode.equals(nodeId) || Identifiers.ReferenceTypeNode.equals(nodeId)
						|| Identifiers.ReferenceNode.equals(nodeId)) {
					QualifiedName bName = references[i].getBrowseName();
					if (brm.contains(BrowseResultMask.BrowseName) && QualifiedName.isNull(bName)) {
						bName = new QualifiedName(references[i].getDisplayName().getText());
					}
					if (brm.contains(BrowseResultMask.TypeDefinition)
							&& !ExpandedNodeId.isNull(references[i].getTypeDefinition())) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								RequestType.Browse.name()
										+ " Incorrect information returned in ReferenceDescription.NodeId for node "
										+ (bName == null ? "" : bName) + ", but the typedefinition NodeId is returned!",
								new StatusCode(StatusCodes.Bad_NodeIdInvalid)/*
																				 * , request, response, null
																				 */);
						isNodeIdValid = false;
					} else if (brm.contains(BrowseResultMask.TypeDefinition)
							&& ExpandedNodeId.isNull(references[i].getTypeDefinition())) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								RequestType.Browse.name()
										+ " Incorrect information returned in ReferenceDescription.NodeId for node "
										+ bName + " and the typedefinition is invalid!",
								new StatusCode(StatusCodes.Bad_NodeIdInvalid));
						isNodeIdValid = false;
					}
				}
				// BROWSENAME
				QualifiedName browseName = references[i].getBrowseName();
				if (brm.contains(BrowseResultMask.BrowseName)) {
					if (QualifiedName.isNull(browseName)) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"{0} Incorrect information returned in ReferenceDescription.BrowseName! {1}",
								new Object[] { RequestType.Browse.name(),
										new StatusCode(StatusCodes.Bad_ViewParameterMismatch) });
					}
					if (!QualifiedName.isNull(browseName) && !browseName.getName().isEmpty()
							&& browseName.getName().length() > MAX_CHARACTERS) {
						Logger.getLogger(getClass().getName()).log(Level.WARNING,
								"{0} Too long browsename {1} for ReferenceDescription.BrowseName!",
								new String[] { RequestType.Browse.name(), browseName.getName() });
						TranslateBrowsePathsToNodeIdsRequest bn2nodeId = new TranslateBrowsePathsToNodeIdsRequest();
						BrowsePath[] path = new BrowsePath[] { new BrowsePath(description.getNodeId(),
								new RelativePath(new RelativePathElement[] { new RelativePathElement(
										description.getReferenceTypeId(), !references[i].getIsForward(),
										description.getIncludeSubtypes(), browseName) })) };
						bn2nodeId.setBrowsePaths(path);
						try {
							TranslateBrowsePathsToNodeIdsResponse result = this.session
									.translateBrowsePathToNodeIds(bn2nodeId);
							if (result != null && result.getResults() != null && result.getResults().length > 0) {
								BrowsePathTarget[] targets = result.getResults()[0].getTargets();
								// translateborwsepath to nodeid
								if (targets == null || targets.length <= 0
										|| !targets[0].getTargetId().equals(references[i].getNodeId())) {
									Logger.getLogger(getClass().getName()).log(Level.SEVERE, RequestType.Browse.name()
											+ " Reject the results for node " + browseName
											+ " because the browsename attribute is too long and the child node could not be found with the TranslateBrowsePathToNodeId service!",
											new StatusCode(
													StatusCodes.Bad_BrowseNameInvalid)/*
																						 * , request, response, null
																						 */);
									references[i] = null;
									continue;
								}
							}
						} catch (ServiceResultException e) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE,
									RequestType.Browse.name() + " Reject the results for node " + browseName
											+ " because the browsename attribute is too long!",
									e);
							references[i] = null;
							continue;
						}
					}
				}
				// DISPLAYNAME
				if (brm.contains(BrowseResultMask.DisplayName)) {
					if (references[i].getDisplayName() == null || references[i].getDisplayName().getText() == null) {
						LocalizedText newDisplayName;
						if (references[i].getBrowseName() != null && references[i].getBrowseName().getName() != null
								&& references[i].getBrowseName().getName().isEmpty()) {
							newDisplayName = new LocalizedText(references[i].getBrowseName().getName(),
									LocalizedText.NO_LOCALE);
						} else {
							newDisplayName = new LocalizedText("noname", LocalizedText.NO_LOCALE);
						}
						references[i].setDisplayName(newDisplayName);
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, RequestType.Browse.name()
								+ " Incorrect information returned in ReferenceDescription.DisplayName! The Browsename "
								+ newDisplayName.getText() + " is used instead!",
								new StatusCode(StatusCodes.Bad_ViewParameterMismatch)/*
																						 * , request, response, null
																						 */);
					} else if (references[i].getDisplayName() != null
							&& references[i].getDisplayName().getText() != null
							&& references[i].getDisplayName().getText().length() > MAX_CHARACTERS) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"{0} Too long Displayname for ReferenceDescription.DisplayName {1}!",
								new Object[] { RequestType.Browse.name(), references[i].getDisplayName() });
					}
				}
				// NODECLASS
				NodeClass nodeClass = references[i].getNodeClass();
				if (brm.contains(BrowseResultMask.NodeClass)) {
					if (nodeClass == null || !NodeClass.ALL.contains(nodeClass)) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"{0} Incorrect information returned in ReferenceDescription.NodeClass! {1}",
								new Object[] { RequestType.Browse.name(),
										new StatusCode(StatusCodes.Bad_ViewParameterMismatch) });
						references[i] = null;
						continue;
					} else if (!NodeClass.getSet(description.getNodeClassMask()).contains(nodeClass)) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"{0} Incorrect information returned in ReferenceDescription.NodeClass! {1}",
								new Object[] { RequestType.Browse.name(),
										new StatusCode(StatusCodes.Bad_ViewParameterMismatch) });
					}
				}
				if (isNodeIdValid) {
					NodeClass nodeClassRead = null;
					ReadRequest request2read = new ReadRequest();
					request2read.setTimestampsToReturn(TimestampsToReturn.Neither);
					request2read.setMaxAge(0.0);
					ReadValueId readValue = new ReadValueId();
					try {
						readValue.setNodeId(nodeId);
						readValue.setAttributeId(Attributes.NodeClass);
						request2read.setNodesToRead(new ReadValueId[] { readValue });
						ReadResponse response4read = this.session.beginRead(request2read, Identifiers.ReadRequest);
						if (response4read != null && response4read.getResults() != null
								&& response4read.getResults().length > 0) {
							Object v = response4read.getResults()[0].getValue().getValue();
							if (v instanceof Integer) {
								nodeClassRead = NodeClass.valueOf((Integer) v);
								if (nodeClassRead != nodeClass) {
									// discard
									references[i] = null;
									Logger.getLogger(getClass().getName()).log(Level.SEVERE, RequestType.Browse.name()
											+ " Rejected the result, because incorrect information returned in ReferenceDescription.NodeClass from the server!",
											new StatusCode(StatusCodes.Bad_ViewParameterMismatch));
								}
							}
						}
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, RequestType.Browse.name()
								+ " Error validating the correct nodeclasses in the browse response!", e);
						references[i] = null;
					}
				}
			} catch (ServiceResultException e1) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Could not convert from ExpandedNodeId to NodeId {0}", references[i].getNodeId());
			}
		}
	}
}
