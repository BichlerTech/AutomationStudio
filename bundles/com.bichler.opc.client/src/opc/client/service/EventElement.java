package opc.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.CallMethodRequest;
import org.opcfoundation.ua.core.CallMethodResult;
import org.opcfoundation.ua.core.CallRequest;
import org.opcfoundation.ua.core.EventFieldList;
import org.opcfoundation.ua.core.EventFilter;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.SimpleAttributeOperand;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.encoding.DecodingException;
import org.opcfoundation.ua.encoding.EncoderContext;

import opc.client.application.listener.EventChangeListener;
import opc.sdk.ua.classes.AlarmConditionType;
import opc.sdk.ua.classes.AuditEventType;
import opc.sdk.ua.classes.AuditUpdateMethodEventType;
import opc.sdk.ua.classes.BaseEventType;
import opc.sdk.ua.classes.BaseInstance;
import opc.sdk.ua.classes.ConditionType;
import opc.sdk.ua.classes.DialogConditionType;
import opc.sdk.ua.classes.ExclusiveLimitAlarmType;
import opc.sdk.ua.classes.NonExclusiveLimitAlarmType;
import opc.sdk.ua.constants.BrowseNames;

/**
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class EventElement {
	private Map<NodeId, NodeId> eventTypeMapping = new HashMap<>();
	private EventFilter filter = null;
	private ClientSession session = null;
	private NodeId elementId = null;
	private Object eventId = null;
	private Object branchId = null;
	private EventFieldList event = null;
	private Queue<EventChangeListener> listeners = null;
	private NodeId[] knownEventTypes = new NodeId[] { Identifiers.BaseEventType, Identifiers.ConditionType,
			Identifiers.DialogConditionType, Identifiers.AlarmConditionType, Identifiers.ExclusiveLimitAlarmType,
			Identifiers.NonExclusiveLimitAlarmType, Identifiers.AuditEventType,
			Identifiers.AuditUpdateMethodEventType };
	private static final String SHELFNOTSUPPORTED = "ShelvingState not supported!";

	public EventElement(MonitoredItem item, EventFieldList eventFields) {
		this.session = item.getSubscription().getSession();
		try {
			this.filter = item.getFilter().decode(EncoderContext.getDefaultInstance());
		} catch (DecodingException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		this.event = eventFields;
		this.elementId = findElementId(eventFields);
		Variant tmpBranchId = findEventFieldValue(new QualifiedName(BrowseNames.BRANCHID));
		if (tmpBranchId != null && !tmpBranchId.isEmpty()) {
			this.branchId = tmpBranchId.getValue();
		}
		Variant tmpEventId = findEventFieldValue(new QualifiedName(BrowseNames.EVENTID));
		if (tmpEventId != null && !tmpEventId.isEmpty()) {
			this.eventId = tmpEventId.getValue();
		}
		this.listeners = new ConcurrentLinkedQueue<>();
	}

	public boolean addEventChangeListener(EventChangeListener listener) {
		return this.listeners.add(listener);
	}

	public boolean removeEventChagneListener(EventChangeListener listener) {
		return this.listeners.remove(listener);
	}

	public CallMethodResult acknowledge(LocalizedText comment) throws ServiceResultException {
		NodeId objectId = this.elementId;
		NodeId methodId = Identifiers.AcknowledgeableConditionType_Acknowledge;
		List<Variant> inputArguments = new ArrayList<>();
		if (comment != null) {
			inputArguments.add(new Variant(this.eventId));
			inputArguments.add(new Variant(comment));
		}
		CallMethodRequest methodToCall = new CallMethodRequest();
		methodToCall.setObjectId(objectId);
		methodToCall.setMethodId(methodId);
		methodToCall.setInputArguments(inputArguments.toArray(new Variant[inputArguments.size()]));
		CallRequest request = new CallRequest();
		request.setMethodsToCall(new CallMethodRequest[] { methodToCall });
		return this.session.callMethod(request).getResults()[0];
	}

	public CallMethodResult addComment(LocalizedText comment) throws ServiceResultException {
		NodeId objectId = this.elementId;
		NodeId methodId = Identifiers.ConditionType_AddComment;
		List<Variant> inputArguments = new ArrayList<>();
		if (comment != null) {
			inputArguments.add(new Variant(this.eventId));
			inputArguments.add(new Variant(comment));
		}
		CallMethodRequest methodToCall = new CallMethodRequest();
		methodToCall.setObjectId(objectId);
		methodToCall.setMethodId(methodId);
		methodToCall.setInputArguments(inputArguments.toArray(new Variant[inputArguments.size()]));
		CallRequest request = new CallRequest();
		request.setMethodsToCall(new CallMethodRequest[] { methodToCall });
		return this.session.callMethod(request).getResults()[0];
	}

	public CallMethodResult confirm(LocalizedText comment) throws ServiceResultException {
		NodeId objectId = this.elementId;
		NodeId methodId = Identifiers.AcknowledgeableConditionType_Confirm;
		List<Variant> inputArguments = new ArrayList<>();
		if (comment != null) {
			inputArguments.add(new Variant(this.eventId));
			inputArguments.add(new Variant(comment));
		}
		CallMethodRequest methodToCall = new CallMethodRequest();
		methodToCall.setObjectId(objectId);
		methodToCall.setMethodId(methodId);
		methodToCall.setInputArguments(inputArguments.toArray(new Variant[inputArguments.size()]));
		CallRequest request = new CallRequest();
		request.setMethodsToCall(new CallMethodRequest[] { methodToCall });
		return this.session.callMethod(request).getResults()[0];
	}

	public BaseEventType constructEvent() throws ServiceResultException {
		NodeId eventType = findEventType();
		if (eventType == null || NodeId.isNull(eventType)) {
			return null;
		}
		NodeId knownType = eventTypeMapping.get(eventType);
		// check for known type
		if (knownType == null) {
			for (int i = 0; i < knownEventTypes.length; i++) {
				if (knownEventTypes[i].equals(eventType)) {
					knownType = eventType;
					eventTypeMapping.put(eventType, eventType);
					break;
				}
			}
			// browse for supertypes
			if (knownType == null) {
				Browser browser = new Browser(this.session);
				ReferenceDescription[] superTypes = browser.browseSuperTypes(eventType);
				// nothing to do
				if (superTypes == null || superTypes.length <= 0) {
					return null;
				}
				for (int j = 0; j < superTypes.length; j++) {
					for (int i = 0; i < knownEventTypes.length; i++) {
						if (knownEventTypes[i].equals(session.getNamespaceUris().toNodeId(superTypes[j].getNodeId()))) {
							knownType = knownEventTypes[i];
							eventTypeMapping.put(eventType, knownType);
							break;
						}
					}
					if (knownType != null) {
						break;
					}
				}
			}
		}
		if (knownType == null) {
			return null;
		}
		// all of the known event types have a UInt32 as Identifiers
		UnsignedInteger id = (UnsignedInteger) knownType.getValue();
		if (id == null) {
			return null;
		}
		// construct the event based on the known event type
		BaseEventType e;
		if (Identifiers.ConditionType.equals(knownType)) {
			e = new ConditionType(null);
		} else if (Identifiers.DialogConditionType.equals(knownType)) {
			e = new DialogConditionType(null);
		} else if (Identifiers.AlarmConditionType.equals(knownType)) {
			e = new AlarmConditionType(null);
		} else if (Identifiers.ExclusiveLimitAlarmType.equals(knownType)) {
			e = new ExclusiveLimitAlarmType(null);
		} else if (Identifiers.NonExclusiveLimitAlarmType.equals(knownType)) {
			e = new NonExclusiveLimitAlarmType(null);
		} else if (Identifiers.AuditEventType.equals(knownType)) {
			e = new AuditEventType(null);
		} else if (Identifiers.AuditUpdateMethodEventType.equals(knownType)) {
			e = new AuditUpdateMethodEventType(null);
		}
		// default
		else {
			e = new BaseEventType(null);
		}
		e.update(filter.getSelectClauses(), this.event);
		e.setHandle(this.event);
		return e;
	}

	public CallMethodResult disable(/* LocalizedText comment */) throws ServiceResultException {
		NodeId objectId = this.elementId;
		NodeId methodId = Identifiers.ConditionType_Disable;
		List<Variant> inputArguments = new ArrayList<>();
		CallMethodRequest methodToCall = new CallMethodRequest();
		methodToCall.setObjectId(objectId);
		methodToCall.setMethodId(methodId);
		methodToCall.setInputArguments(inputArguments.toArray(new Variant[inputArguments.size()]));
		CallRequest request = new CallRequest();
		request.setMethodsToCall(new CallMethodRequest[] { methodToCall });
		return this.session.callMethod(request).getResults()[0];
	}

	public CallMethodResult enable() throws ServiceResultException {
		NodeId objectId = this.elementId;
		NodeId methodId = Identifiers.ConditionType_Enable;
		List<Variant> inputArguments = new ArrayList<>();
		CallMethodRequest methodToCall = new CallMethodRequest();
		methodToCall.setObjectId(objectId);
		methodToCall.setMethodId(methodId);
		methodToCall.setInputArguments(inputArguments.toArray(new Variant[inputArguments.size()]));
		CallRequest request = new CallRequest();
		request.setMethodsToCall(new CallMethodRequest[] { methodToCall });
		return this.session.callMethod(request).getResults()[0];
	}

	/**
	 * Finds the type of the event for the notification.
	 * 
	 * @param MonitoredItem
	 * @param Event
	 * @return
	 */
	public NodeId findEventType() {
		for (int i = 0; i < filter.getSelectClauses().length; i++) {
			SimpleAttributeOperand clause = filter.getSelectClauses()[i];
			if (clause.getBrowsePath().length == 1
					&& BrowseNames.EVENTTYPE.equalsIgnoreCase(clause.getBrowsePath()[0].getName())) {
				return (NodeId) event.getEventFields()[i].getValue();
			}
		}
		return null;
	}

	public Variant findEventFieldValue(QualifiedName... fieldName) {
		int fieldLength = fieldName.length;
		for (int i = 0; i < this.filter.getSelectClauses().length; i++) {
			SimpleAttributeOperand clause = this.filter.getSelectClauses()[i];
			if (clause.getBrowsePath().length == fieldLength) {
				boolean isValid = true;
				for (int j = 0; j < fieldLength; j++) {
					if (!fieldName[j].getName().equalsIgnoreCase(clause.getBrowsePath()[j].getName())) {
						isValid = false;
						break;
					}
				}
				if (isValid) {
					return this.event.getEventFields()[i];
				}
			}
		}
		return null;
	}

	public CallMethodResult refresh(UnsignedInteger subscriptionId) throws ServiceResultException {
		NodeId objectId = Identifiers.ConditionType;
		NodeId methodId = Identifiers.ConditionType_ConditionRefresh;
		List<Variant> inputArguments = new ArrayList<>();
		inputArguments.add(new Variant(subscriptionId));
		CallMethodRequest methodToCall = new CallMethodRequest();
		methodToCall.setObjectId(objectId);
		methodToCall.setMethodId(methodId);
		methodToCall.setInputArguments(inputArguments.toArray(new Variant[inputArguments.size()]));
		CallRequest request = new CallRequest();
		request.setMethodsToCall(new CallMethodRequest[] { methodToCall });
		return this.session.callMethod(request).getResults()[0];
	}

	public CallMethodResult respond(int selectedResponse) throws ServiceResultException {
		// check type for dialogCondition
		BaseEventType dialogConditionState = constructEvent();
		if (!(dialogConditionState instanceof DialogConditionType)) {
			throw new ServiceResultException(StatusCodes.Bad_MethodInvalid,
					"Responding only supported on DialogConditions!");
		}
		NodeId objectId = this.elementId;
		NodeId methodId = Identifiers.DialogConditionType_Respond;
		List<Variant> inputArguments = new ArrayList<>();
		inputArguments.add(new Variant(selectedResponse));
		CallMethodRequest methodToCall = new CallMethodRequest();
		methodToCall.setObjectId(objectId);
		methodToCall.setMethodId(methodId);
		methodToCall.setInputArguments(inputArguments.toArray(new Variant[inputArguments.size()]));
		CallRequest request = new CallRequest();
		request.setMethodsToCall(new CallMethodRequest[] { methodToCall });
		return this.session.callMethod(request).getResults()[0];
	}

	public CallMethodResult timeShelve(double shelvingTime) throws ServiceResultException {
		BaseEventType conditionState = constructEvent();
		BaseInstance shelvingState = conditionState.findChild(new QualifiedName(BrowseNames.SHELVINGSTATE));
		if (shelvingState == null) {
			throw new ServiceResultException(StatusCodes.Bad_MethodInvalid, SHELFNOTSUPPORTED);
		}
		NodeId objectId = shelvingState.getNodeId();
		NodeId methodId = Identifiers.ShelvedStateMachineType_TimedShelve;
		List<Variant> inputArguments = new ArrayList<>();
		inputArguments.add(new Variant(shelvingTime));
		CallMethodRequest methodToCall = new CallMethodRequest();
		methodToCall.setObjectId(objectId);
		methodToCall.setMethodId(methodId);
		methodToCall.setInputArguments(inputArguments.toArray(new Variant[inputArguments.size()]));
		CallRequest request = new CallRequest();
		request.setMethodsToCall(new CallMethodRequest[] { methodToCall });
		return this.session.callMethod(request).getResults()[0];
	}

	public CallMethodResult oneShotShelve() throws ServiceResultException {
		BaseEventType conditionState = constructEvent();
		BaseInstance shelvingState = conditionState.findChild(new QualifiedName(BrowseNames.SHELVINGSTATE));
		if (shelvingState == null) {
			throw new ServiceResultException(StatusCodes.Bad_MethodInvalid, SHELFNOTSUPPORTED);
		}
		NodeId objectId = shelvingState.getNodeId();
		NodeId methodId = Identifiers.ShelvedStateMachineType_OneShotShelve;
		List<Variant> inputArguments = new ArrayList<>();
		CallMethodRequest methodToCall = new CallMethodRequest();
		methodToCall.setObjectId(objectId);
		methodToCall.setMethodId(methodId);
		methodToCall.setInputArguments(inputArguments.toArray(new Variant[inputArguments.size()]));
		CallRequest request = new CallRequest();
		request.setMethodsToCall(new CallMethodRequest[] { methodToCall });
		return this.session.callMethod(request).getResults()[0];
	}

	public CallMethodResult unShelve() throws ServiceResultException {
		BaseEventType conditionState = constructEvent();
		BaseInstance shelvingState = conditionState.findChild(new QualifiedName(BrowseNames.SHELVINGSTATE));
		if (shelvingState == null) {
			throw new ServiceResultException(StatusCodes.Bad_MethodInvalid, SHELFNOTSUPPORTED);
		}
		NodeId objectId = shelvingState.getNodeId();
		NodeId methodId = Identifiers.ShelvedStateMachineType_Unshelve;
		List<Variant> inputArguments = new ArrayList<>();
		CallMethodRequest methodToCall = new CallMethodRequest();
		methodToCall.setObjectId(objectId);
		methodToCall.setMethodId(methodId);
		methodToCall.setInputArguments(inputArguments.toArray(new Variant[inputArguments.size()]));
		CallRequest request = new CallRequest();
		request.setMethodsToCall(new CallMethodRequest[] { methodToCall });
		return this.session.callMethod(request).getResults()[0];
	}

	@Override
	public boolean equals(Object obj) {
		boolean equalsBranchId = false;
		if (obj == null)
			throw new NullPointerException("Object to compare is null");
		if (!(obj instanceof EventElement))
			throw new ClassCastException("Object is not type of EventElement");
		EventElement e = (EventElement) obj;
		if (branchId != null && branchId.equals(e.branchId)) {
			equalsBranchId = true;
		}
		if (NodeId.isNull(elementId) && NodeId.isNull(e.elementId)) {
			return true;
		}
		if (!NodeId.isNull(elementId) && elementId.equals(e.elementId)) {
			if (branchId == null || equalsBranchId) {
				return true;
			}
			return false;
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventTypeMapping == null) ? 0 : eventTypeMapping.hashCode());
		result = prime * result + ((filter == null) ? 0 : filter.hashCode());
		result = prime * result + ((session == null) ? 0 : session.hashCode());
		result = prime * result + ((elementId == null) ? 0 : elementId.hashCode());
		result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
		result = prime * result + ((branchId == null) ? 0 : branchId.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((listeners == null) ? 0 : listeners.hashCode());
		result = prime * result + ((knownEventTypes == null) ? 0 : Arrays.hashCode(knownEventTypes));
		return result;
	}

	public boolean isAcknowledgeable() {
		BaseEventType dialogConditionState = null;
		try {
			dialogConditionState = constructEvent();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return false;
		}
		if (!(dialogConditionState instanceof ConditionType)) {
			return false;
		}
		return true;
	}

	public boolean isAddComment() {
		BaseEventType dialogConditionState = null;
		try {
			dialogConditionState = constructEvent();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return false;
		}
		if (!(dialogConditionState instanceof ConditionType)) {
			return false;
		}
		return true;
	}

	public boolean isConfirm() {
		BaseEventType dialogConditionState = null;
		try {
			dialogConditionState = constructEvent();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return false;
		}
		if (!(dialogConditionState instanceof ConditionType)) {
			return false;
		}
		return true;
	}

	public boolean isEnable() {
		BaseEventType dialogConditionState = null;
		try {
			dialogConditionState = constructEvent();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return false;
		}
		if (!(dialogConditionState instanceof ConditionType)) {
			return false;
		}
		return true;
	}

	public boolean isDisable() {
		BaseEventType dialogConditionState = null;
		try {
			dialogConditionState = constructEvent();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return false;
		}
		if (!(dialogConditionState instanceof ConditionType)) {
			return false;
		}
		return true;
	}

	public boolean isRefresh() {
		BaseEventType dialogConditionState = null;
		try {
			dialogConditionState = constructEvent();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return false;
		}
		if (!(dialogConditionState instanceof ConditionType)) {
			return false;
		}
		return true;
	}

	public boolean isRespond() {
		BaseEventType dialogConditionState = null;
		try {
			dialogConditionState = constructEvent();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return false;
		}
		if (!(dialogConditionState instanceof DialogConditionType)) {
			return false;
		}
		return true;
	}

	public boolean isShelving() {
		BaseEventType conditionState = null;
		try {
			conditionState = constructEvent();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return false;
		}
		BaseInstance shelvingState = conditionState.findChild(new QualifiedName(BrowseNames.SHELVINGSTATE));
		return shelvingState != null;
	}

	public NodeId getElementId() {
		return elementId;
	}

	public Object getBranchId() {
		return branchId;
	}

	public EventFieldList getEvent() {
		return event;
	}

	public void setEvent(EventFieldList event) {
		this.event = event;
		// fetch new eventid
		Variant tmpEventId = findEventFieldValue(new QualifiedName(BrowseNames.EVENTID));
		if (tmpEventId != null && !tmpEventId.isEmpty()) {
			this.eventId = tmpEventId.getValue();
		}
		// fetch new eventid
		Variant tmpBranchId = findEventFieldValue(new QualifiedName(BrowseNames.BRANCHID));
		if (tmpBranchId != null && !tmpBranchId.isEmpty()) {
			this.branchId = tmpBranchId.getValue();
		}
		for (EventChangeListener listener : this.listeners) {
			listener.eventChanged();
		}
	}

	private NodeId findElementId(EventFieldList newevent) {
		for (int i = 0; i < filter.getSelectClauses().length; i++) {
			SimpleAttributeOperand clause = filter.getSelectClauses()[i];
			if (Attributes.NodeId.equals(clause.getAttributeId())) {
				return (NodeId) newevent.getEventFields()[i].getValue();
			}
		}
		return null;
	}
}
