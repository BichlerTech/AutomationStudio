package opc.client.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;

import opc.client.application.core.dataaccess.DADisplayNames;
import opc.client.application.core.dataaccess.DataAccessValidator;
import opc.sdk.core.application.OPCEntry;
import opc.sdk.core.enums.RequestType;

/**
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class DataAccessManager {
	public static final String ID = "opc.client.service.DataAccessManager";
	private DataAccessValidator daValidator;

	public DataAccessManager() {
		this.daValidator = new DataAccessValidator();
	}

	/**
	 * Returns the value of the definition child node or an empty string if there
	 * are no values available. <Definition Node>
	 * 
	 * @param Session Session to look up for the items.
	 * @param NodeId  NodeId of the DA item.
	 * 
	 * @return String definition
	 */
	public Variant readDefinition(ClientSession session, NodeId nodeId) {
		ReferenceDescription[] children = browseChild(session, nodeId);
		for (ReferenceDescription child : children) {
			if (child.getDisplayName() == null || child.getDisplayName().getText() == null) {
				continue;
			}
			if (DADisplayNames.DEFINITION.name().equals(child.getDisplayName().getText())) {
				NodeId definition2readId;
				try {
					definition2readId = session.getNamespaceUris().toNodeId(child.getNodeId());
					return readPropertyValue(session, definition2readId);
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
		}
		return Variant.NULL;
	}

	/**
	 * Returns the value of the engineering units child node. <EngineeringUnits
	 * Node>
	 * 
	 * @param Session Session to look up for the items.
	 * @param NodeId  NodeId of the DA item.
	 * 
	 * @return LocalizedText, Description
	 */
	public DataValue readEngineeringUnits(ClientSession session, NodeId nodeId) {
		ReferenceDescription[] children = browseChild(session, nodeId);
		for (ReferenceDescription child : children) {
			if (child.getDisplayName() == null || child.getDisplayName().getText() == null) {
				continue;
			}
			if (DADisplayNames.ENGINEERINGUNITS.name().equals(child.getDisplayName().getText())) {
				try {
					NodeId engineeringUnits2readId = session.getNamespaceUris().toNodeId(child.getNodeId());
					ReadRequest request = new ReadRequest(null, 0.0, TimestampsToReturn.Neither, new ReadValueId[] {
							new ReadValueId(engineeringUnits2readId, Attributes.Value, null, null) });
					ReadResponse response = session.beginRead(request, Identifiers.ReadRequest);
					if (response.getResults() == null || response.getResults().length <= 0) {
						continue;
					}
					return response.getResults()[0];
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
		}
		return null;
	}

	/**
	 * Returns the value of the EURange child node. <EURange Node>
	 * 
	 * @param Session Session to look up for the items.
	 * @param NodeId  NodeId of the DA item.
	 * 
	 * @return String definition
	 */
	public DataValue readEURange(ClientSession session, NodeId nodeId) {
		ReferenceDescription[] children = browseChild(session, nodeId);
		for (ReferenceDescription child : children) {
			if (child.getDisplayName() == null || child.getDisplayName().getText() == null) {
				continue;
			}
			if (DADisplayNames.EURANGE.name().equals(child.getDisplayName().getText())) {
				try {
					NodeId definition2readId = session.getNamespaceUris().toNodeId(child.getNodeId());
					ReadRequest request = new ReadRequest(null, 0.0, TimestampsToReturn.Neither,
							new ReadValueId[] { new ReadValueId(definition2readId, Attributes.Value, null, null) });
					ReadResponse response = session.beginRead(request, Identifiers.ReadRequest);
					if (response.getResults() == null || response.getResults().length <= 0) {
						continue;
					}
					return response.getResults()[0];
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
		}
		return null;
	}

	/**
	 * Returns the value of the instrument range child node. <InstrumentRange Node>
	 * 
	 * @param Session Session to look up for the items.
	 * @param NodeId  NodeId of the DA item.
	 * 
	 * @return String definition
	 */
	public DataValue readInstrumentRange(ClientSession session, NodeId nodeId) {
		ReferenceDescription[] children = browseChild(session, nodeId);
		for (ReferenceDescription child : children) {
			if (child.getDisplayName() == null || child.getDisplayName().getText() == null) {
				continue;
			}
			if (DADisplayNames.INSTRUMENTRANGE.name().equals(child.getDisplayName().getText())) {
				try {
					NodeId definition2readId = session.getNamespaceUris().toNodeId(child.getNodeId());
					ReadRequest request = new ReadRequest(null, 0.0, TimestampsToReturn.Neither,
							new ReadValueId[] { new ReadValueId(definition2readId, Attributes.Value, null, null) });
					ReadResponse response = session.beginRead(request, Identifiers.ReadRequest);
					if (response.getResults() == null || response.getResults().length <= 0) {
						continue;
					}
					return response.getResults()[0];
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
		}
		return null;
	}

	/**
	 * Returns the value of the valueprecision child node. <ValuePrecision Node>
	 * 
	 * @param Session Session to look up for the items.
	 * @param NodeId  NodeId of the DA item.
	 * 
	 * @return Float, Double, Timestamp
	 */
	public DataValue readValuePrecision(ClientSession session, NodeId nodeId) {
		ReferenceDescription[] children = browseChild(session, nodeId);
		for (ReferenceDescription child : children) {
			if (child.getDisplayName() == null || child.getDisplayName().getText() == null) {
				continue;
			}
			if (DADisplayNames.VALUEPRECISION.name().equals(child.getDisplayName().getText())) {
				try {
					NodeId definition2readId = session.getNamespaceUris().toNodeId(child.getNodeId());
					ReadRequest request = new ReadRequest(null, 0.0, TimestampsToReturn.Neither,
							new ReadValueId[] { new ReadValueId(definition2readId, Attributes.Value, null, null) });
					ReadResponse response = session.beginRead(request, Identifiers.ReadRequest);
					if (response.getResults() == null || response.getResults().length <= 0) {
						continue;
					}
					return response.getResults()[0];
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
		}
		return null;
	}

	/**
	 * Returns an array of MultiStateDiscreteValues or an empty array if there are
	 * no values available. <EnumStrings Node>
	 * 
	 * @param Session Session to look up for the items.
	 * @param NodeId  NodeId of the item.
	 * 
	 * @return MultiStateDiscreteValues
	 */
	public Object[] readMultiStateDiscreteValue(ClientSession session, NodeId nodeId) {
		ReferenceDescription[] result = browseChild(session, nodeId);
		for (int i = 0; i < result.length; i++) {
			ReferenceDescription reference = result[i];
			Object[] values = null;
			try {
				values = readMultiStateDiscreteValue(session,
						session.getNamespaceUris().toNodeId(reference.getNodeId()), reference.getDisplayName());
				if (values != null && values.length > 0) {
					return values;
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
		return new Object[0];
	}

	/**
	 * Reads the value of a TwoStateDiscrete item.
	 * 
	 * @param Session Session to look up for the items.
	 * @param NodeId  NodeId of the item.
	 * @param State   State of the value item.
	 * @return Result
	 */
	public Object readTwoStateDiscreteValue(ClientSession session, NodeId nodeId, Object state) {
		ReferenceDescription[] result = browseChild(session, nodeId);
		for (int i = 0; i < result.length; i++) {
			ReferenceDescription reference = result[i];
			try {
				Object res = readTwoStateDiscreteValue(session,
						session.getNamespaceUris().toNodeId(reference.getNodeId()), reference.getDisplayName(), state);
				if (res != null) {
					return res;
				}
			} catch (ServiceResultException sere) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, sere);
			}
		}
		return state;
	}

	protected Boolean checkTwoState(Object state) {
		boolean isTrue;
		if (state instanceof Boolean) {
			isTrue = (Boolean) state;
		} else {
			if (((Number) state).intValue() == 0) {
				isTrue = false;
			} else {
				isTrue = true;
			}
		}
		return isTrue;
	}

	/**
	 * StatusCodes are equal with Request.NodeToRead
	 * 
	 * @param isEqualResult
	 * @param validEUInformation
	 * 
	 * @param dynamicNode2DataAccessType
	 * 
	 * @param Session
	 * @param Request
	 * @param Response
	 * @param Codes
	 * @param EnableValueChange
	 */
	protected void evaluateDARead(ClientSession session, ReadRequest request, ReadResponse response, StatusCode[] codes,
			boolean enableValueChange, Boolean isEqualResult, boolean validEURange, boolean validInstrumentRange,
			boolean validEUInformation,
			Map<Integer, Entry<NodeId, List<Entry<NodeId, LocalizedText>>>> dynamicNode2DataAccessType) {
		// changes the value to the DataItem types one.
		if (enableValueChange && isEqualResult) {
			for (int i = 0; i < request.getNodesToRead().length; i++) {
				StatusCode error = codes[i];
				// if not a
				if (error.isBad()) {
					continue;
				}
				NodeId dataAccessTypeId = dynamicNode2DataAccessType.get(i).getKey();
				List<Entry<NodeId, LocalizedText>> properties = dynamicNode2DataAccessType.get(i).getValue();
				DataValue result = response.getResults()[i];
				if (properties == null || properties.isEmpty()) {
					continue;
				}
				// validate analog item, or change values from discret items
				for (Entry<NodeId, LocalizedText> property : properties) {
					Object wrappedValue = null;
					// validate DataItem properties (used for both Analog and
					// Discret items
					if (DADisplayNames.DEFINITION.name().equals(property.getValue().getText())) {
						Variant defVal = readPropertyValue(session, property.getKey());
						this.daValidator.validateValuePrecision(defVal);
					} else if (DADisplayNames.VALUEPRECISION.name().equals(property.getValue().getText())) {
						Variant valuePrec = readPropertyValue(session, property.getKey());
						this.daValidator.validateValuePrecision(valuePrec);
					} else if (dataAccessTypeId.equals(Identifiers.AnalogItemType)) {
						StatusCode propertyError = null;
						if (DADisplayNames.EURANGE.name().equals(property.getValue().getText())) {
							// validate result against EURANGE
							Variant euRangeVal = readPropertyValue(session, property.getKey());
							propertyError = this.daValidator.validateEURange(RequestType.Read, euRangeVal, result);
						} else if (DADisplayNames.INSTRUMENTRANGE.name().equals(property.getValue().getText())) {
							// validate result against INSTRUMENTRANGE
							Variant instRangeVal = readPropertyValue(session, property.getKey());
							propertyError = this.daValidator.validateInstrumentRange(RequestType.Read, instRangeVal,
									result);
						} else if (DADisplayNames.ENGINEERINGUNITS.name().equals(property.getValue().getText())) {
							// validate result against EURANGE
							Variant engUntVal = readPropertyValue(session, property.getKey());
							propertyError = this.daValidator.validateEngineeringUnits(engUntVal);
						}
						if (propertyError != null && propertyError.isBad()) {
							// property is invalid => ignore
							if (propertyError.getValue().equals(StatusCodes.Bad_ArgumentsMissing)) {
								Logger.getLogger(getClass().getName()).log(Level.WARNING,
										"{0} An warning has occurred while validating the properties of a DataAccess Item! Property is ignored! Property - {1} Id - {2}",
										new Object[] { RequestType.Read.name(), property.getValue(),
												property.getKey() });
							} else {
								Logger.getLogger(getClass().getName()).log(Level.WARNING,
										"{0} The value {1} is invalid through the DA validation in property - {2} ID - {3}",
										new Object[] { RequestType.Read.name(), result.getValue(),
												property.getValue().getText(), property.getKey() });
								// when just valid values should be returned
								if ((DADisplayNames.INSTRUMENTRANGE.name().equals(property.getValue().getText())
										&& validInstrumentRange)
										|| (DADisplayNames.EURANGE.name().equals(property.getValue().getText())
												&& validEURange)
										|| (DADisplayNames.ENGINEERINGUNITS.name()
												.equals(property.getValue().getText()))) {
									result.setValue(Variant.NULL);
								}
							}
						}
					} else if (dataAccessTypeId.equals(Identifiers.MultiStateDiscreteType)) {
						Object[] values = readMultiStateDiscreteValue(session, property.getKey(), property.getValue());
						if (values.length > 0) {
							Variant variantContainer = null;
							try {
								Number number = (Number) result.getValue().getValue();
								wrappedValue = values[number.intValue()];
								if (wrappedValue == null || (wrappedValue instanceof LocalizedText
										&& (((LocalizedText) wrappedValue).getText() == null
												|| ((LocalizedText) wrappedValue).getText().isEmpty()))) {
									variantContainer = response.getResults()[i].getValue();
								}
							} catch (IndexOutOfBoundsException | NumberFormatException ioobe) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ioobe);
								variantContainer = response.getResults()[i].getValue();
							}
							if (variantContainer != null && !variantContainer.isEmpty()) {
								wrappedValue = variantContainer.getValue();
							}
						}
					} else if (dataAccessTypeId.equals(Identifiers.TwoStateDiscreteType)) {
						wrappedValue = readTwoStateDiscreteValue(session, property.getKey(), property.getValue(),
								result.getValue().getValue());
						if (wrappedValue == null || (wrappedValue instanceof LocalizedText
								&& (((LocalizedText) wrappedValue).getText() == null
										|| ((LocalizedText) wrappedValue).getText().isEmpty()))) {
							continue;
						}
					}
					if (wrappedValue != null) {
						result.setValue(new Variant(wrappedValue));
						break;
					}
				}
			}
		}
	}

	protected void validateDAWrite(ClientSession session, NodeId[] nodeId, DataValue[] values, boolean validEURange,
			boolean validInstrumentRange, boolean validEUInformation,
			Map<Integer, Entry<NodeId, List<Entry<NodeId, LocalizedText>>>> dynamicNode2DataAccessType) {
		if (dynamicNode2DataAccessType.isEmpty()) {
			return;
		}
		for (int i = 0; i < nodeId.length; i++) {
			Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry = dynamicNode2DataAccessType.get(i);
			if (entry == null || entry.getValue() == null) {
				continue;
			}
			DataValue value = values[i];
			for (Entry<NodeId, LocalizedText> property : entry.getValue()) {
				StatusCode code = validateWriteDAProperties(session, value, property.getKey(), property.getValue());
				if (code != null && code.isBad() && StatusCodes.Bad_ArgumentsMissing.equals(code.getValue())) {
					Logger.getLogger(getClass().getName()).log(Level.WARNING,
							"{0} An warning has occurred while validating the properties of a DataAccess Item for value {1}! Property is ignored! Property - {2} Id - {3}!",
							new Object[] { RequestType.Write.name(), value.getValue(), property.getValue(),
									property.getKey() });
				} else if (code != null && code.isBad()) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							RequestType.Write.name() + " The value " + value.getValue().toString()
									+ " is invalid through the DA validation in property - "
									+ property.getValue().getText() + " ID - " + property.getKey().toString(),
							code);
					if ((DADisplayNames.INSTRUMENTRANGE.name().equals(property.getValue().getText())
							&& validInstrumentRange)
							|| (DADisplayNames.EURANGE.name().equals(property.getValue().getText()) && validEURange)
							|| (DADisplayNames.ENGINEERINGUNITS.name().equals(property.getValue().getText())
									&& validEUInformation)) {
						value.setValue(Variant.NULL);
					}
				}
			}
		}
	}

	protected StatusCode validateType(ClientSession session, NodeId readValueId, int index,
			OPCEntry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
		return this.daValidator.validateType(session, readValueId, index, entry);
	}

	protected StatusCode validateProperty(ClientSession session, ExpandedNodeId nodeId, LocalizedText displayName,
			Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
		return this.daValidator.validateProperty(session, nodeId, displayName, entry);
	}

	private ReferenceDescription[] browseChild(ClientSession session, NodeId nodeId) {
		try {
			BrowseRequest browseRequest = new BrowseRequest();
			browseRequest.setNodesToBrowse(new BrowseDescription[] {
					new BrowseDescription(nodeId, BrowseDirection.Forward, Identifiers.HasProperty, true,
							NodeClass.getMask(NodeClass.Variable), BrowseResultMask.getMask(BrowseResultMask.ALL)) });
			browseRequest.setRequestedMaxReferencesPerNode(UnsignedInteger.ZERO);
			BrowseResult[] result = session.beginBrowse(browseRequest, Identifiers.BrowseRequest).getResults();
			if (result != null && result.length > 0 && result[0] != null && result[0].getStatusCode().isGood()) {
				return result[0].getReferences();
			}
		} catch (ServiceResultException sre) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, sre);
		}
		return new ReferenceDescription[0];
	}

	/**
	 * Returns an array of MultiStateDiscreteValues or an empty array if there are
	 * no values available.
	 * 
	 * @param Session Session to look up for the items.
	 * @param NodeId  NodeId of the item.
	 * @param TypeId  Type of the item
	 * @return MultiStateDiscreteValues
	 */
	private Object[] readMultiStateDiscreteValue(ClientSession session, NodeId nodeId, LocalizedText propertyName) {
		NodeId id2lookup = null;
		if (propertyName.getText().contains(DADisplayNames.ENUMSTRINGS.name())) {
			id2lookup = nodeId;
		}
		if (id2lookup != null) {
			ReadRequest readValues = new ReadRequest();
			readValues.setMaxAge(0.0);
			readValues.setTimestampsToReturn(TimestampsToReturn.Neither);
			readValues.setNodesToRead(new ReadValueId[] { new ReadValueId(id2lookup, Attributes.Value, null, null) });
			try {
				DataValue res = session.beginRead(readValues, Identifiers.ReadRequest).getResults()[0];
				if (res.getValue().getValue() instanceof Object[]) {
					return (Object[]) res.getValue().getValue();
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return new Object[0];
			}
		} else {
			return new Object[0];
		}
		return new Object[0];
	}

	private Variant readPropertyValue(ClientSession session, NodeId definition2readId) {
		ReadRequest request = new ReadRequest(null, 0.0, TimestampsToReturn.Neither,
				new ReadValueId[] { new ReadValueId(definition2readId, Attributes.Value, null, null) });
		ReadResponse response = null;
		try {
			response = session.beginRead(request, Identifiers.ReadRequest);
			if (response.getResults() == null || response.getResults().length <= 0) {
				return Variant.NULL;
			}
			return response.getResults()[0].getValue();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		return Variant.NULL;
	}

	private Object readTwoStateDiscreteValue(ClientSession session, NodeId nodeId, LocalizedText property,
			Object state) {
		boolean isTrue = checkTwoState(state);
		NodeId id2lookup;
		if ((property.getText().contains(DADisplayNames.TRUESTATE.name()) && isTrue)
				|| property.getText().contains(DADisplayNames.FALSESTATE.name()) && !isTrue) {
			id2lookup = nodeId;
		} else {
			return null;
		}
		if (id2lookup != null) {
			ReadRequest readValues = new ReadRequest();
			readValues.setMaxAge(0.0);
			readValues.setTimestampsToReturn(TimestampsToReturn.Neither);
			readValues.setNodesToRead(new ReadValueId[] { new ReadValueId(id2lookup, Attributes.Value, null, null) });
			try {
				DataValue res = session.beginRead(readValues, Identifiers.ReadRequest).getResults()[0];
				return res.getValue().getValue();
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return state;
			}
		}
		return null;
	}

	private StatusCode validateWriteDAProperties(ClientSession session, DataValue value, NodeId propertyId,
			LocalizedText propertyName) {
		if (propertyName == null || propertyName.getText() == null) {
			return StatusCode.GOOD;
		}
		DADisplayNames name = DADisplayNames.getFromName(propertyName);
		switch (name) {
		case DEFINITION:
			Variant definition = readPropertyValue(session, propertyId);
			return this.daValidator.validateDefinition(definition);
		case VALUEPRECISION:
			Variant valuePrecision = readPropertyValue(session, propertyId);
			return this.daValidator.validateValuePrecision(valuePrecision);
		case EURANGE:
			Variant euRange = readPropertyValue(session, propertyId);
			return this.daValidator.validateEURange(RequestType.Write, euRange, value);
		case ENGINEERINGUNITS:
			Variant engineeringUnits = readPropertyValue(session, propertyId);
			return this.daValidator.validateEngineeringUnits(engineeringUnits);
		case INSTRUMENTRANGE:
			Variant instrumentRange = readPropertyValue(session, propertyId);
			return this.daValidator.validateInstrumentRange(RequestType.Write, instrumentRange, value);
		default:
			switch (name) {
			case TRUESTATE:
			case FALSESTATE:
			case ENUMSTRINGS:
				return StatusCode.GOOD;
			default:
			}
			break;
		}
		return StatusCode.GOOD;
	}
}
