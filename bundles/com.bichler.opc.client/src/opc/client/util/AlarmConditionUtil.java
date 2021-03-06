package opc.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.client.service.Browser;
import opc.client.service.ClientSession;
import opc.sdk.ua.constants.BrowseNames;
import opc.sdk.ua.constants.EventSeverity;

import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.ContentFilter;
import org.opcfoundation.ua.core.ContentFilterElement;
import org.opcfoundation.ua.core.ElementOperand;
import org.opcfoundation.ua.core.EventFilter;
import org.opcfoundation.ua.core.FilterOperand;
import org.opcfoundation.ua.core.FilterOperator;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.LiteralOperand;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.SimpleAttributeOperand;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.IEncodeable;

/**
 * Util to create and opperate with Alarms & Condition.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class AlarmConditionUtil {
	/**
	 * private constructor to hide default constructor
	 */
	private AlarmConditionUtil() {
	}

	/**
	 * Creates the EventFilter for an event MonitoredItem.
	 * 
	 * @param Session                   Session to get the information about the
	 *                                  event.
	 * @param Severity                  Defined event severity.
	 * @param IgnoreSuppressedOrShelved Boolean to ignore suppressed or shelved
	 *                                  alarms.
	 * @param EventTypeIds              Types of the events to create.
	 * @return EventFilter
	 * 
	 * @throws ServiceResultException
	 */
	public static EventFilter createEventFilter(ClientSession session, EventSeverity severity,
			boolean ignoreSuppressedOrShelved, NodeId[] eventTypeIds) throws ServiceResultException {
		EventFilter filter = new EventFilter();
		// select the clause specify the values returned with each event
		// notification
		SimpleAttributeOperand[] selectClause = createSelectClause(session, eventTypeIds);
		filter.setSelectClauses(selectClause);
		// the where clause restricts the events returned by the server.
		// it works a lot like the WHERE clause in a SQL statement and supports
		// arbitrary expession trees where the operands are literals or event
		// fields.
		ContentFilter whereClause = new ContentFilter();
		filter.setWhereClause(whereClause);
		// the code below constructs a filter that looks like this:
		// (Severity >= X OR LastSeverity >= X) AND (SuppressedOrShelved ==
		// False) AND (OfType(A) OR OfType(B))
		List<ContentFilterElement> elements = new ArrayList<>();
		ContentFilterElement element1 = null;
		ContentFilterElement element2;
		// add the severity.
		if (severity.severity().intValue() > EventSeverity.MIN.severity().intValue()) {
			// select the severity property of the event
			SimpleAttributeOperand operand1 = new SimpleAttributeOperand();
			operand1.setTypeDefinitionId(Identifiers.BaseEventType);
			operand1.setBrowsePath(new QualifiedName[] { new QualifiedName(BrowseNames.SEVERITY) });
			operand1.setAttributeId(Attributes.Value);
			// specify the value to compare the severity property with
			LiteralOperand operand2 = new LiteralOperand();
			operand2.setValue(new Variant(new UnsignedShort(severity.severity())));
			// specify that the Severity property must be GreaterThanOrEqual the
			// value specified.
			element1 = createContentFilter(FilterOperator.GreaterThanOrEqual, elements, operand1, operand2);
		}
		// add the suppressed or shelved
		if (!ignoreSuppressedOrShelved) {
			// select the SuppressedOrShelved property of the event
			SimpleAttributeOperand operand1 = new SimpleAttributeOperand();
			operand1.setTypeDefinitionId(Identifiers.BaseEventType);
			operand1.setBrowsePath(new QualifiedName[] { new QualifiedName(BrowseNames.SUPPRESSEDORSHELVED) });
			operand1.setAttributeId(Attributes.Value);
			// specify the value to compare the Severity property with
			LiteralOperand operand2 = new LiteralOperand();
			operand2.setValue(new Variant(false));
			// specify that the Severity property must Equal the value specified
			element2 = createContentFilter(FilterOperator.Equals, elements, operand1, operand2);
			if (element1 != null) {
				element1 = createContentFilter(FilterOperator.And, elements, element1, element2);
			} else {
				element1 = element2;
			}
		}
		// add the event types
		if (eventTypeIds != null && eventTypeIds.length > 0) {
			element2 = null;
			for (int i = 0; i < eventTypeIds.length; i++) {
				LiteralOperand operand1 = new LiteralOperand();
				operand1.setValue(new Variant(eventTypeIds[i]));
				ContentFilterElement element3 = createContentFilter(FilterOperator.OfType, elements, operand1);
				// need to chain multiple types together with an OR clause
				if (element2 != null) {
					element2 = createContentFilter(FilterOperator.Or, elements, element2, element3);
				} else {
					element2 = element3;
				}
			}
			// need to link the set of event types with the previous filters
			if (element1 != null) {
				createContentFilter(FilterOperator.And, elements, element1, element2);
			}
		}
		whereClause.setElements(elements.toArray(new ContentFilterElement[elements.size()]));
		return filter;
	}

	/**
	 * Creates a select clause for an Event MonitoredItem.
	 * 
	 * @param Session      Session to fetch the select attributes from the server.
	 * @param EventTypeIds EventTypes for the select clause.
	 * @return AttributeOperand
	 * 
	 * @throws ServiceResultException
	 */
	public static SimpleAttributeOperand[] createSelectClause(ClientSession session, NodeId... eventTypeIds)
			throws ServiceResultException {
		// browse the type model in the server address space to find the fields
		// available for the event type.
		List<SimpleAttributeOperand> selectClause = new ArrayList<>();
		// must always request the NodeId for the condition instances.
		// this can be done by specifying an operand with an empty browse path.
		SimpleAttributeOperand operand = new SimpleAttributeOperand();
		operand.setTypeDefinitionId(Identifiers.BaseEventType);
		operand.setAttributeId(Attributes.NodeId);
		operand.setBrowsePath(new QualifiedName[] {});
		selectClause.add(operand);
		// add the fields
		if (eventTypeIds != null) {
			for (int i = 0; i < eventTypeIds.length; i++) {
				collectSelectFields(session, eventTypeIds[i], selectClause);
			}
		}
		// use BaseEventType as the default if no EventTypes specified.
		else {
			collectSelectFields(session, Identifiers.BaseEventType, selectClause);
		}
		return selectClause.toArray(new SimpleAttributeOperand[selectClause.size()]);
	}

	public static NodeId[] fetchEventIdsFromFilter(ContentFilter filter) {
		if (filter == null || filter.getElements() == null) {
			return new NodeId[0];
		}
		List<NodeId> eventIds = new ArrayList<>();
		for (ContentFilterElement element : filter.getElements()) {
			if (FilterOperator.OfType.equals(element.getFilterOperator())) {
				addEventIds(element, eventIds);
			}
		}
		return eventIds.toArray(new NodeId[0]);
	}

	private static void addEventIds(ContentFilterElement element, List<NodeId> eventIds) {
		for (ExtensionObject obj : element.getFilterOperands()) {
			try {
				IEncodeable operand = obj.decode(EncoderContext.getDefaultInstance());
				if (operand instanceof LiteralOperand) {
					NodeId typeId = (NodeId) ((LiteralOperand) operand).getValue().getValue();
					eventIds.add(typeId);
				}
			} catch (Exception e) {
				Logger.getLogger(AlarmConditionUtil.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	public static EventSeverity fetchEventSeverityFromFilter(ContentFilter filter) {
		if (filter == null || filter.getElements() == null) {
			return null;
		}
		EventSeverity severity = EventSeverity.MIN;
		boolean isContent = false;
		for (ContentFilterElement element : filter.getElements()) {
			for (ExtensionObject obj : element.getFilterOperands()) {
				try {
					IEncodeable operand = obj.decode(EncoderContext.getDefaultInstance());
					if (operand instanceof SimpleAttributeOperand) {
						if (((SimpleAttributeOperand) operand).getBrowsePath() == null
								|| ((SimpleAttributeOperand) operand).getBrowsePath().length == 0) {
							return EventSeverity.MIN;
						}
						if (BrowseNames.SEVERITY
								.equalsIgnoreCase(((SimpleAttributeOperand) operand).getBrowsePath()[0].getName())) {
							isContent = true;
						}
					}
					if (operand instanceof LiteralOperand && isContent) {
						for (EventSeverity s : EventSeverity.values()) {
							Number v = (Number) ((LiteralOperand) operand).getValue().getValue();
							if (s.severity().intValue() == v.intValue()) {
								severity = s;
								break;
							}
						}
					}
				} catch (Exception e) {
					Logger.getLogger(AlarmConditionUtil.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
		return severity;
	}

	public static Boolean fetchShelvingFromFilter(ContentFilter filter) {
		if (filter == null || filter.getElements() == null) {
			throw new NullPointerException("Content filter parameter or its element is null");
		}
		Boolean isShelving = true;
		for (ContentFilterElement element : filter.getElements()) {
			boolean isContent = false;
			for (ExtensionObject obj : element.getFilterOperands()) {
				try {
					IEncodeable operand = obj.decode(EncoderContext.getDefaultInstance());
					if (operand instanceof SimpleAttributeOperand) {
						if (((SimpleAttributeOperand) operand).getBrowsePath() == null
								|| ((SimpleAttributeOperand) operand).getBrowsePath().length == 0) {
							return false;
						}
						if (BrowseNames.SUPPRESSEDORSHELVED
								.equalsIgnoreCase(((SimpleAttributeOperand) operand).getBrowsePath()[0].getName())) {
							isContent = true;
						}
					} else if (operand instanceof LiteralOperand && isContent) {
						isShelving = (Boolean) ((LiteralOperand) operand).getValue().getValue();
						break;
					}
				} catch (Exception e) {
					Logger.getLogger(AlarmConditionUtil.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
		return isShelving;
	}

	/**
	 * Collect the Fields for the Event Type
	 * 
	 * @param Session      Session to fetch the fields of the event.
	 * @param TypeId       TypeId of the event.
	 * @param SelectClause Fields of the select clause.
	 * 
	 * @throws ServiceResultException
	 */
	private static void collectSelectFields(ClientSession session, NodeId typeId,
			List<SimpleAttributeOperand> eventFields) throws ServiceResultException {
		Browser browser = new Browser(session);
		ReferenceDescription[] superTypes = browser.browseSuperTypes(typeId);
		if (superTypes == null || superTypes.length <= 0) {
			return;
		}
		Map<NodeId, List<QualifiedName>> foundNodes = new HashMap<>();
		List<QualifiedName> parentPath = new ArrayList<>();
		for (int i = superTypes.length - 1; i >= 0; i--) {
			collectFields(session, session.getNamespaceUris().toNodeId(superTypes[i].getNodeId()), parentPath,
					eventFields, foundNodes);
		}
		collectFields(session, typeId, parentPath, eventFields, foundNodes);
	}

	private static void collectFields(ClientSession session, NodeId nodeId, List<QualifiedName> parentPath,
			List<SimpleAttributeOperand> eventFields, Map<NodeId, List<QualifiedName>> foundNodes)
			throws ServiceResultException {
		// find all of the children of the field
		BrowseDescription nodeToBrowse = new BrowseDescription();
		nodeToBrowse.setNodeId(nodeId);
		nodeToBrowse.setBrowseDirection(BrowseDirection.Forward);
		nodeToBrowse.setReferenceTypeId(Identifiers.Aggregates);
		nodeToBrowse.setIncludeSubtypes(true);
		nodeToBrowse.setNodeClassMask(NodeClass.Object, NodeClass.Variable);
		nodeToBrowse.setResultMask(BrowseResultMask.ALL);
		BrowseRequest request = new BrowseRequest();
		request.setNodesToBrowse(new BrowseDescription[] { nodeToBrowse });
		request.setRequestedMaxReferencesPerNode(new UnsignedInteger(100));
		Browser browser = new Browser(session);
		BrowseResponse response = browser.browse(request, true, true);
		ReferenceDescription[] children = response.getResults()[0].getReferences();
		if (children == null) {
			return;
		}
		for (int i = 0; i < children.length; i++) {
			ReferenceDescription child = children[i];
			if (child.getNodeId().isAbsolute()) {
				continue;
			}
			List<QualifiedName> browsePath = new ArrayList<>(parentPath);
			browsePath.add(child.getBrowseName());
			// check if the browse path is already in the list
			if (!containsPath(eventFields, browsePath)) {
				SimpleAttributeOperand field = new SimpleAttributeOperand();
				field.setTypeDefinitionId(Identifiers.BaseEventType);
				field.setBrowsePath(browsePath.toArray(new QualifiedName[browsePath.size()]));
				field.setAttributeId(
						NodeClass.Variable.equals(child.getNodeClass()) ? Attributes.Value : Attributes.NodeId);
				eventFields.add(field);
			}
			// recusively fidn all of gthe chidlren
			NodeId targetId = session.getNamespaceUris().toNodeId(child.getNodeId());
			if (!foundNodes.containsKey(targetId)) {
				foundNodes.put(targetId, browsePath);
				collectFields(session, targetId, browsePath, eventFields, foundNodes);
			}
		}
	}

	/**
	 * Determines whether the specified select clause contains the browse path.
	 * 
	 * @param EventFields
	 * @param BrowsePath
	 * @return
	 */
	private static boolean containsPath(List<SimpleAttributeOperand> selectClause, List<QualifiedName> browsePath) {
		for (int i = 0; i < selectClause.size(); i++) {
			SimpleAttributeOperand field = selectClause.get(i);
			if (field.getBrowsePath().length != browsePath.size()) {
				continue;
			}
			boolean match = true;
			for (int j = 0; j < field.getBrowsePath().length; j++) {
				if (!field.getBrowsePath()[j].equals(browsePath.get(j))) {
					match = false;
					break;
				}
			}
			if (match) {
				return true;
			}
		}
		return false;
	}

	private static ContentFilterElement createContentFilter(FilterOperator op, List<ContentFilterElement> elements,
			Object... operands) throws ServiceResultException {
		if (operands == null || operands.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_InvalidArgument, "No FilterOperands!");
		}
		ContentFilterElement element = new ContentFilterElement();
		element.setFilterOperator(op);
		List<ExtensionObject> filterOperands = new ArrayList<>();
		for (int i = 0; i < operands.length; i++) {
			// check for FilterOperand
			if (operands[i] instanceof FilterOperand) {
				filterOperands.add(
						ExtensionObject.binaryEncode((FilterOperand) operands[i], EncoderContext.getDefaultInstance()));
			}
			// check for reference to another contentfilterelement
			else if (operands[i] instanceof ContentFilterElement) {
				int index = findElementIndex(elements, operands[i]);
				if (index == -1) {
					throw new ServiceResultException(StatusCodes.Bad_InvalidArgument,
							"ContentFilterElement is not part of the ContentFilter");
				}
				ElementOperand operand = new ElementOperand();
				operand.setIndex(new UnsignedInteger(index));
				filterOperands.add(ExtensionObject.binaryEncode(operand, EncoderContext.getDefaultInstance()));
			} else {
				// assume a literal operand
				LiteralOperand literaloOperand = new LiteralOperand();
				literaloOperand.setValue(new Variant(operands[i]));
				filterOperands.add(ExtensionObject.binaryEncode(literaloOperand, EncoderContext.getDefaultInstance()));
			}
		}
		element.setFilterOperands(filterOperands.toArray(new ExtensionObject[filterOperands.size()]));
		// insert new element at the beginning of the list
		elements.add(0, element);
		// re-number ElementOperands since all element were shifted up.
		for (int i = 0; i < elements.size(); i++) {
			for (int j = 0; j < elements.get(i).getFilterOperands().length; j++) {
				FilterOperand decodedObj = elements.get(i).getFilterOperands()[j]
						.decode(EncoderContext.getDefaultInstance());
				if (decodedObj instanceof ElementOperand) {
					UnsignedInteger index2set = ((ElementOperand) decodedObj).getIndex().add(1);
					((ElementOperand) decodedObj).setIndex(index2set);
					elements.get(i).getFilterOperands()[j] = ExtensionObject.binaryEncode(decodedObj,
							EncoderContext.getDefaultInstance());
				}
			}
		}
		return element;
	}

	private static int findElementIndex(List<ContentFilterElement> operandsContent, Object target) {
		for (int i = 0; i < operandsContent.size(); i++) {
			if (operandsContent.get(i) == target) {
				return i;
			}
		}
		return -1;
	}
}
