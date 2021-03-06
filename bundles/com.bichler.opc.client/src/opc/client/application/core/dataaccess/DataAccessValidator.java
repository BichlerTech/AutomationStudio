package opc.client.application.core.dataaccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.EUInformation;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.Range;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.StatusCodes;

import opc.client.service.Browser;
import opc.client.service.ClientSession;
import opc.sdk.core.application.OPCEntry;
import opc.sdk.core.enums.RequestType;

/**
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 *
 */
public class DataAccessValidator {
	public static final String ID = "opc.client.application.core.dataaccess.DataAccessValidator";
	public final List<NodeId> daTypes = Arrays.asList(Identifiers.DataItemType, Identifiers.AnalogItemType,
			Identifiers.DiscreteItemType, Identifiers.MultiStateDiscreteType, Identifiers.TwoStateDiscreteType,
			new NodeId(0, 11238));
	public final List<NodeId> baseTypes = Arrays.asList(Identifiers.BaseVariableType, Identifiers.BaseDataVariableType);
	private static final BrowseRequest BROWSESUBTYPE = new BrowseRequest(null, null, null,
			new BrowseDescription[] { new BrowseDescription(null, BrowseDirection.Inverse, Identifiers.HasSubtype, true,
					NodeClass.getMask(NodeClass.VariableType), BrowseResultMask.getMask(BrowseResultMask.ALL)) });
	private DataAccessPropertyValidator propertyValidator;
	private DataAccessItemTypeValidator itemTypeValdiator;

	/**
	 * Validates DataAccess Information Model.
	 * 
	 * @param logger
	 */
	public DataAccessValidator() {
		this.propertyValidator = new DataAccessPropertyValidator();
		this.itemTypeValdiator = new DataAccessItemTypeValidator();
	}

	/**
	 * Validates the child node properties of DataAccess type nodes.
	 * 
	 * @param entry
	 * 
	 * @param Session        Session to use.
	 * @param ExpandedNodeId Id of the node.
	 * @param DisplayName    Validates the propertie with the displayname
	 *                       (LocalizedText)
	 */
	public StatusCode validateProperty(ClientSession session, ExpandedNodeId expandedNodeId, LocalizedText displayName,
			Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
		if (entry.getValue() == null) {
			entry.setValue(new ArrayList<Entry<NodeId, LocalizedText>>());
		}
		return this.propertyValidator.validate(session, expandedNodeId, displayName, entry);
	}

	/**
	 * Validates the node to read with the DataAccess type. Uses Services to fetch
	 * it dynamicaly from the server. TODO: Some service improvements?
	 * 
	 * @param index
	 * 
	 * @param entry
	 * 
	 * @param Session     Session to use.
	 * @param ReadValueId NodeId to validate for read.
	 * @return StatusCode
	 */
	public StatusCode validateType(ClientSession session, NodeId readValueId, int index,
			OPCEntry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
		return this.itemTypeValdiator.validate(session, readValueId, index, entry);
	}

	public StatusCode validateDefinition(Variant value) {
		if (value.isEmpty()) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		}
		return StatusCode.GOOD;
	}

	public StatusCode validateEngineeringUnits(Variant value) {
		if (value.isEmpty()) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		}
		EUInformation engineeringUnits = (EUInformation) value.getValue();
		LocalizedText displayname = engineeringUnits.getDisplayName();
		String namespaceUri = engineeringUnits.getNamespaceUri();
		if (displayname == null || displayname.getText() == null) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		}
		if (engineeringUnits.getDescription() == null) {
			engineeringUnits.setDescription(LocalizedText.EMPTY);
		}
		if (namespaceUri == null || namespaceUri.isEmpty()) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		}
		return StatusCode.GOOD;
	}

	public StatusCode validateEURange(RequestType requestType, Variant euRange, DataValue value) {
		if (euRange.isEmpty()) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		}
		Range range = (Range) euRange.getValue();
		Double high = range.getHigh();
		Double low = range.getLow();
		// check for incomplete
		if ((high == null || high.compareTo(Double.NaN) == 0) || (low == null || low.compareTo(Double.NaN) == 0)
				|| high.compareTo(low) == 0) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		}
		if (high < low) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "{0} EURange Low: {1} High: {2} is tansposed!",
					new Object[] { requestType.name(), low, high });
			Double x = high;
			high = low;
			low = x;
		}
		// no value no validation
		if (value == null || value.getValue() == null || value.getValue().getValue() == null) {
			return StatusCode.GOOD;
		}
		// validate
		if ((value.getValue().getValue() instanceof Number)
				&& ((Double.parseDouble(((Number) value.getValue().getValue()).toString()) >= low)
						&& (Double.parseDouble(((Number) value.getValue().getValue()).toString()) <= high))) {
			return StatusCode.GOOD;
		}
		return StatusCode.BAD;
	}

	public StatusCode validateInstrumentRange(RequestType requestType, Variant instrumentRange, DataValue value) {
		if (instrumentRange.isEmpty()) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		}
		Range range = (Range) instrumentRange.getValue();
		Double high = range.getHigh();
		Double low = range.getLow();
		// check for incomplete
		if ((high == null || high.compareTo(Double.NaN) == 0) || (low == null || low.compareTo(Double.NaN) == 0)
				|| high.compareTo(low) == 0) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		}
		if (high < low) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} InstrumentRange Low: {1} High: {2} is tansposed!",
					new Object[] { requestType.name(), low, high });
			Double x = high;
			high = low;
			low = x;
		}
		// no value no validation
		if (value == null || value.getValue() == null || value.getValue().getValue() == null) {
			return StatusCode.GOOD;
		}
		if (value.getValue().getValue() instanceof Number
				&& (Double.parseDouble(((Number) value.getValue().getValue()).toString()) >= low)
				&& (Double.parseDouble(((Number) value.getValue().getValue()).toString()) <= high)) {
			return StatusCode.GOOD;
		}
		return StatusCode.BAD;
	}

	public StatusCode validateValuePrecision(Variant valuePrecision) {
		if (valuePrecision.isEmpty()) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		}
		Object precision = valuePrecision.getValue();
		if (!(precision instanceof Double || precision instanceof Float || precision instanceof DateTime)) {
			return StatusCode.BAD;
		}
		if (precision instanceof Double
				&& (((Double) precision) < 0 || ((Double) precision).compareTo(Double.NaN) == 0)) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		}
		if (precision instanceof Float && (((Float) precision) < 0 || ((Float) precision).compareTo(Float.NaN) == 0)) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		} else if (precision instanceof DateTime && DateTime.MIN_VALUE.compareTo((DateTime) precision) == 0) {
			return new StatusCode(StatusCodes.Bad_ArgumentsMissing);
		}
		return StatusCode.GOOD;
	}

	class DataAccessPropertyValidator {
		public StatusCode validate(ClientSession session, ExpandedNodeId nodeId, LocalizedText result,
				Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
			if (result.getText() == null) {
				return StatusCode.BAD;
			} else if (DADisplayNames.DEFINITION.name().equals(result.getText())) {
				return validateDefinition(session, nodeId, result, entry);
			} else if (DADisplayNames.VALUEPRECISION.name().equals(result.getText())) {
				return validateValuePrecision(session, nodeId, result, entry);
			} else if (DADisplayNames.ENGINEERINGUNITS.name().equals(result.getText())) {
				return validateEngineeringUnit(session, nodeId, result, entry);
			} else if (DADisplayNames.EURANGE.name().equals(result.getText())) {
				return validateEURange(session, nodeId, result, entry);
			} else if (DADisplayNames.INSTRUMENTRANGE.name().equals(result.getText())) {
				return validateInstrumentalRange(session, nodeId, result, entry);
			} else if (DADisplayNames.ENUMSTRINGS.name().equals(result.getText())) {
				return validateEnumString(session, nodeId, result, entry);
			} else if (DADisplayNames.FALSESTATE.name().equals(result.getText())) {
				return validateFalseState(session, nodeId, result, entry);
			} else if (DADisplayNames.TRUESTATE.name().equals(result.getText())) {
				return validateTrueState(session, nodeId, result, entry);
			}
			return StatusCode.BAD;
		}

		private StatusCode validateDefinition(ClientSession session, ExpandedNodeId nodeId, LocalizedText result,
				Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
			try {
				NodeId id = session.getNamespaceUris().toNodeId(nodeId);
				entry.getValue().add(new OPCEntry<NodeId, LocalizedText>(id, result));
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return StatusCode.BAD;
			}
			return StatusCode.GOOD;
		}

		private StatusCode validateValuePrecision(ClientSession session, ExpandedNodeId nodeId, LocalizedText result,
				Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
			try {
				NodeId id = session.getNamespaceUris().toNodeId(nodeId);
				entry.getValue().add(new OPCEntry<NodeId, LocalizedText>(id, result));
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return StatusCode.BAD;
			}
			return StatusCode.GOOD;
		}

		private StatusCode validateEngineeringUnit(ClientSession session, ExpandedNodeId nodeId, LocalizedText result,
				Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
			try {
				NodeId id = session.getNamespaceUris().toNodeId(nodeId);
				entry.getValue().add(new OPCEntry<NodeId, LocalizedText>(id, result));
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return StatusCode.BAD;
			}
			return StatusCode.GOOD;
		}

		private StatusCode validateEURange(ClientSession session, ExpandedNodeId nodeId, LocalizedText result,
				Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
			try {
				NodeId id = session.getNamespaceUris().toNodeId(nodeId);
				entry.getValue().add(new OPCEntry<NodeId, LocalizedText>(id, result));
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return StatusCode.BAD;
			}
			return StatusCode.GOOD;
		}

		private StatusCode validateInstrumentalRange(ClientSession session, ExpandedNodeId nodeId, LocalizedText result,
				Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
			try {
				NodeId id = session.getNamespaceUris().toNodeId(nodeId);
				entry.getValue().add(new OPCEntry<NodeId, LocalizedText>(id, result));
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return StatusCode.BAD;
			}
			return StatusCode.GOOD;
		}

		private StatusCode validateEnumString(ClientSession session, ExpandedNodeId nodeId, LocalizedText result,
				Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
			try {
				NodeId id = session.getNamespaceUris().toNodeId(nodeId);
				entry.getValue().add(new OPCEntry<NodeId, LocalizedText>(id, result));
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return StatusCode.BAD;
			}
			return StatusCode.GOOD;
		}

		private StatusCode validateFalseState(ClientSession session, ExpandedNodeId nodeId, LocalizedText result,
				Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
			try {
				NodeId id = session.getNamespaceUris().toNodeId(nodeId);
				entry.getValue().add(new OPCEntry<NodeId, LocalizedText>(id, result));
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return StatusCode.BAD;
			}
			return StatusCode.GOOD;
		}

		private StatusCode validateTrueState(ClientSession session, ExpandedNodeId nodeId, LocalizedText result,
				Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
			try {
				NodeId id = session.getNamespaceUris().toNodeId(nodeId);
				entry.getValue().add(new OPCEntry<NodeId, LocalizedText>(id, result));
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return StatusCode.BAD;
			}
			return StatusCode.GOOD;
		}
	}

	class DataAccessItemTypeValidator {
		public StatusCode validate(ClientSession session, NodeId readValueId, int index,
				OPCEntry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
			BrowseRequest browseType = new BrowseRequest();
			BrowseDescription type2Browse = new BrowseDescription();
			type2Browse.setBrowseDirection(BrowseDirection.Forward);
			type2Browse.setIncludeSubtypes(true);
			type2Browse.setNodeClassMask(NodeClass.ALL);
			type2Browse.setReferenceTypeId(Identifiers.HasTypeDefinition);
			type2Browse.setResultMask(BrowseResultMask.ALL);
			type2Browse.setNodeId(readValueId);
			browseType.setNodesToBrowse(new BrowseDescription[] { type2Browse });
			try {
				Browser browser = new Browser(session);
				BrowseResponse typeResponse = browser.browse(browseType, true, true);
				if (typeResponse != null && typeResponse.getResults() != null && typeResponse.getResults().length > 0
						&& typeResponse.getResults()[0].getReferences() != null
						&& typeResponse.getResults()[0].getReferences().length > 0) {
					NodeId typeId = session.getNamespaceUris()
							.toNodeId(typeResponse.getResults()[0].getReferences()[0].getNodeId());
					// data accesss type
					return validateType(session, readValueId, typeId, index, entry);
				} else {
					// No data access type
					return StatusCode.BAD;
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return StatusCode.BAD;
			}
		}

		private StatusCode validateType(ClientSession session, NodeId nodeId, NodeId typeId, int index,
				OPCEntry<NodeId, List<Entry<NodeId, LocalizedText>>> entry) {
			if (NodeId.isNull(typeId)) {
				return new StatusCode(StatusCodes.Bad_NodeIdInvalid);
			}
			if (daTypes.contains(typeId)) {
				entry.setKey(typeId);
				return StatusCode.GOOD;
			} else if (baseTypes.contains(typeId)) {
				return StatusCode.BAD;
			}
			Browser browser = new Browser(session);
			BrowseRequest request = BROWSESUBTYPE.clone();
			request.getNodesToBrowse()[0].setNodeId(typeId);
			try {
				BrowseResponse typeResponse = browser.browse(request, true, true);
				if (typeResponse == null || typeResponse.getResults() == null
						|| typeResponse.getResults()[0].getReferences() == null) {
					return StatusCode.BAD;
				}
				for (ReferenceDescription reference : typeResponse.getResults()[0].getReferences()) {
					NodeId typeIdnew = session.getNamespaceUris().toNodeId(reference.getNodeId());
					return validateType(session, nodeId, typeIdnew, index, entry);
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return new StatusCode(StatusCodes.Bad_InternalError);
			}
			return StatusCode.BAD;
		}
	}
}
