package opc.sdk.server.core.managers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Structure;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AggregateFilter;
import org.opcfoundation.ua.core.AggregateFilterResult;
import org.opcfoundation.ua.core.AttributeOperand;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.ContentFilter;
import org.opcfoundation.ua.core.ContentFilterElement;
import org.opcfoundation.ua.core.DataChangeFilter;
import org.opcfoundation.ua.core.DataChangeTrigger;
import org.opcfoundation.ua.core.DeadbandType;
import org.opcfoundation.ua.core.ElementOperand;
import org.opcfoundation.ua.core.EventFilter;
import org.opcfoundation.ua.core.FilterOperand;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.LiteralOperand;
import org.opcfoundation.ua.core.MonitoredItemCreateRequest;
import org.opcfoundation.ua.core.MonitoringFilter;
import org.opcfoundation.ua.core.MonitoringFilterResult;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.MonitoringParameters;
import org.opcfoundation.ua.core.SimpleAttributeOperand;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.encoding.DecodingException;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;
import org.opcfoundation.ua.encoding.IEncodeable;
import org.opcfoundation.ua.utils.AttributesUtil;
import org.opcfoundation.ua.utils.NumericRange;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.types.TypeInfo;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.service.filter.ContentFilterValidationResult;
import opc.sdk.server.service.filter.ElementResult;
import opc.sdk.server.service.filter.EventFilterValidationResult;

public class OPCValidationFramework {
	/**
	 * Applies the index range and the data encoding to the value.
	 * 
	 * @param node
	 * 
	 * @param typeTable
	 * @param addressSpace
	 * 
	 * @param Context      ReadContext from a read operation.
	 * @param IndexRange   IndexRange from a read operation.
	 * @param DataEncoding DataEncoding from a read operation.
	 * @param Value        Read Value.
	 * @return Read Result.
	 */
	public static StatusCode applyIndexRangeAndDataEncoding(Node node, TypeTable typeTree, String indexRange,
			QualifiedName dataEncoding, DataValue value) {
		/** parses the string to the index range */
		StatusCode error = StatusCode.GOOD;
		try {
			opc.sdk.core.utils.NumericRange range = opc.sdk.core.utils.NumericRange.parse(indexRange);
			/** apply the index range */
			if (!range.isEmpty()) {
				error = applyRange(node, typeTree, value, range);
				if (error == null)
					return StatusCode.BAD;
				if (error.isBad()) {
					return error;
				}
			}
			/** apply the data encoding */
			if (!QualifiedName.isNull(dataEncoding)) {
				error = applyDataEncoding(node, value, dataEncoding);
			}
		} catch (ServiceResultException e) {
			error = e.toServiceResult().getCode();
		}
		return error;
	}

	protected static StatusCode validateDataEncoding(TypeTable typeTable, Variant value, QualifiedName dataEncoding) {
		/** check if nothing to do */
		if (QualifiedName.isNull(dataEncoding)) {
			return StatusCode.GOOD;
		}
		/** check for supported encoding type */
		if (dataEncoding.getNamespaceIndex() != 0) {
			return new StatusCode(StatusCodes.Bad_DataEncodingUnsupported);
		}
		// typeTable.findDataEncoding(dataEncoding);
		boolean useXML = dataEncoding.equals(QualifiedName.DEFAULT_XML_ENCODING);
		if (!useXML && !dataEncoding.equals(QualifiedName.DEFAULT_BINARY_ENCODING)) {
			return new StatusCode(StatusCodes.Bad_DataEncodingUnsupported);
			// return new StatusCode(StatusCodes.Bad_DataEncodingInvalid);
		}
		/** check for array of encodable (value has to be checkded) */
		IEncodeable[] encodeables = null;
		if (value.getValue() instanceof IEncodeable[]) {
			encodeables = (IEncodeable[]) value.getValue();
		}
		if (encodeables == null) {
			/** check for array of extension objects */
			ExtensionObject[] extensions = null;
			if (value.getValue() instanceof ExtensionObject[]) {
				extensions = (ExtensionObject[]) value.getValue();
			}
			if (extensions != null) {
				/** convert extension objects to encodeables */
				encodeables = new IEncodeable[extensions.length];
				for (int ii = 0; ii < encodeables.length; ii++) {
					if (extensions[ii] == null) {
						encodeables[ii] = null;
						continue;
					}
					IEncodeable element = null;
					try {
						element = extensions[ii].decode(EncoderContext.getDefaultInstance());
					} catch (DecodingException e) {
						Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
					}
					if (element == null) {
						return new StatusCode(StatusCodes.Bad_TypeMismatch);
					}
					encodeables[ii] = element;
				}
			}
		}
		/** apply array data encoding */
		if (encodeables != null) {
			ExtensionObject[] extensions = new ExtensionObject[encodeables.length];
			for (int ii = 0; ii < extensions.length; ii++) {
				try {
					extensions[ii] = encodeValue(encodeables[ii], useXML);
				} catch (EncodingException e) {
					Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
			// Variant newValue = new Variant(extensions);
			// value.setValue(newValue);
			return StatusCode.GOOD;
		}
		/** check for scalar (non-dimension) value */
		IEncodeable encodeable = null;
		if (value.getValue() instanceof IEncodeable) {
			encodeable = (IEncodeable) value.getValue();
		}
		if (encodeable == null) {
			ExtensionObject extension = null;
			if (value.getValue() instanceof ExtensionObject) {
				extension = (ExtensionObject) value.getValue();
			}
			if (extension == null) {
				return new StatusCode(StatusCodes.Bad_DataEncodingUnsupported);
			}
			// decode
			try {
				encodeable = extension.decode(EncoderContext.getDefaultInstance());
			} catch (DecodingException e) {
				Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		if (encodeable == null) {
			return new StatusCode(StatusCodes.Bad_DataEncodingUnsupported);
		}
		Variant newValue = null;
		try {
			if (!useXML) {
				Object encodedValue = encodeValue(encodeable, useXML);
				newValue = new Variant(encodedValue);
			} else {
				return new StatusCode(StatusCodes.Bad_DataEncodingUnsupported);
			}
		} catch (EncodingException e) {
			Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		// value.setValue(newValue);
		return StatusCode.GOOD;
	}

	/**
	 * Applies the data encoding for the value.
	 * 
	 * @param node
	 * 
	 * @param Value        The value thats data encoding is checked.
	 * @param DataEncoding DataEncoding from the value.
	 * @return StatusCode Result of the DataEncoding.
	 */
	private static StatusCode applyDataEncoding(Node node, DataValue value, QualifiedName dataEncoding) {
		/** check if nothing to do */
		if (QualifiedName.isNull(dataEncoding) || value.getValue().getValue() == null) {
			return StatusCode.GOOD;
		}
		QualifiedName empty = new QualifiedName(UnsignedShort.valueOf(0), "");
		if (empty.equals(dataEncoding)) {
			return StatusCode.GOOD;
		}
		/** check for supported encoding type */
		if (dataEncoding.getNamespaceIndex() != 0) {
			return new StatusCode(StatusCodes.Bad_DataEncodingUnsupported);
		}
		boolean useXML = dataEncoding.equals(QualifiedName.DEFAULT_XML_ENCODING);
		if (!useXML && !dataEncoding.equals(QualifiedName.DEFAULT_BINARY_ENCODING)) {
			return new StatusCode(StatusCodes.Bad_DataEncodingInvalid);
		}
		/** check for array of encodable (value has to be checkded) */
		IEncodeable[] encodeables = null;
		if (value.getValue().getValue() instanceof IEncodeable[]) {
			encodeables = (IEncodeable[]) value.getValue().getValue();
		}
		if (encodeables == null) {
			/** check for array of extension objects */
			ExtensionObject[] extensions = null;
			if (value.getValue().getValue() instanceof ExtensionObject[]) {
				extensions = (ExtensionObject[]) value.getValue().getValue();
			}
			if (extensions != null) {
				/** convert extension objects to encodeables */
				encodeables = new IEncodeable[extensions.length];
				for (int ii = 0; ii < encodeables.length; ii++) {
					if (extensions[ii] == null) {
						encodeables[ii] = null;
						continue;
					}
					IEncodeable element = null;
					try {
						element = extensions[ii].decode(EncoderContext.getDefaultInstance());
					} catch (DecodingException e) {
						Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
					}
					if (element == null) {
						return new StatusCode(StatusCodes.Bad_TypeMismatch);
					}
					encodeables[ii] = element;
				}
			}
		}
		/** apply array data encoding */
		if (encodeables != null) {
			ExtensionObject[] extensions = new ExtensionObject[encodeables.length];
			for (int ii = 0; ii < extensions.length; ii++) {
				try {
					extensions[ii] = encodeValue(encodeables[ii], useXML);
				} catch (EncodingException e) {
					Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
			Variant newValue = new Variant(extensions);
			value.setValue(newValue);
			return null;
		}
		/** check for scalar (non-dimension) value */
		IEncodeable encodeable = null;
		if (value.getValue().getValue() instanceof IEncodeable) {
			encodeable = (IEncodeable) value.getValue().getValue();
		}
		if (encodeable == null) {
			ExtensionObject extension = null;
			if (value.getValue().getValue() instanceof ExtensionObject) {
				extension = (ExtensionObject) value.getValue().getValue();
			}
			if (extension == null) {
				return new StatusCode(StatusCodes.Bad_DataEncodingUnsupported);
			}
			// decode
			try {
				encodeable = extension.decode(EncoderContext.getDefaultInstance());
			} catch (DecodingException e) {
				Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		if (encodeable == null) {
			return new StatusCode(StatusCodes.Bad_DataEncodingUnsupported);
		}
		Variant newValue = null;
		try {
			if (!useXML) {
				Object encodedValue = encodeValue(encodeable, useXML);
				newValue = new Variant(encodedValue);
			} else {
				return new StatusCode(StatusCodes.Bad_DataEncodingUnsupported);
			}
		} catch (EncodingException e) {
			Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		value.setValue(newValue);
		return StatusCode.GOOD;
	}

	/**
	 * Applies the IndexRange to an array value. Replaces the value.
	 * 
	 * TODO: Dies ist die Section zum Read Service mit Index Range, die Methode
	 * funktioniert, aber sie ist sehr un�bersichlich und geh�rt
	 * �bersichtlicher gemacht!
	 * 
	 * @param node
	 * 
	 * @param Value Value to check the Range. If it will return StatusCode.Good, the
	 *              value has replaced with the applied IndexRange.
	 * @param Range Numeric Range for a value.
	 * @return StatusCode if the IndexRange applies to the value.
	 */
	private static StatusCode applyRange(Node node, TypeTable typeTree, DataValue value, NumericRange r) {
		opc.sdk.core.utils.NumericRange range = (opc.sdk.core.utils.NumericRange) r;
		// nothing to do, but at applyindexrange there is the same check
		if (range.isEmpty()) {
			return StatusCode.GOOD;
		}
		// nothing to do for null values
		if (value.getValue().isEmpty()) {
			return StatusCode.GOOD;
		}
		StatusCode result = null;
		DataValue value2index = new DataValue(value.getValue());
		NumericRange[] subranges = range.getSubranges();
		if (subranges != null) {
			for (int i = 0; i < subranges.length; i++) {
				NumericRange subrange = subranges[i];
				result = applyRangeOnValue(subrange, node, value2index, typeTree, i);
			}
		} else {
			result = applyRangeOnValue(range, node, value2index, typeTree, 0);
		}
		if (result != null && result.isGood()) {
			value.setValue(value2index.getValue());
		}
		return result;
	}

	private static StatusCode applyRangeOnValue(NumericRange range, Node node, DataValue value, TypeTable typeTree,
			int dimension) {
		Object[] obj = null;
		try {
			obj = (Object[]) value.getValue().getValue();
		} catch (Exception e) {
			// Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE,
			// e.getMessage(), e);
		}
		if (dimension > 0) {
			StatusCode result = StatusCode.GOOD;
			for (int i = 0; i < obj.length; i++) {
				result = applyRangeOnDimensionValue(node, range, obj[i], value, typeTree, i);
			}
			return result;
		}
		char[] buffer = null;
		byte[] array = null;
		TypeInfo info = TypeInfo.Unknown;
		if (obj == null) {
			try {
				info = TypeInfo.construct(value, typeTree);
			} catch (Exception e) {
				Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		boolean isScalarArray = false;
		boolean isString = false;
		// check for string
		switch (info.getBuiltInsType()) {
		case ByteString:
			array = ByteString.asByteArray(((ByteString) value.getValue().getValue()));
			isScalarArray = true;
			break;
		case String:
			try {
				String chars = (String) value.getValue().getValue();
				buffer = chars.toCharArray();
				// byte[] b = new byte[buffer.length << 1];
				// CharBuffer cBuffer = ByteBuffer.wrap(b).asCharBuffer();
				// for (int i = 0; i < buffer.length; i++) {
				// cBuffer.put(buffer[i]);
				// }
				isString = true;
				isScalarArray = true;
			} catch (Exception e) {
				Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
			break;
		case Boolean:
			break;
		case Byte:
			break;
		case DataValue:
			break;
		case DateTime:
			break;
		case DiagnosticInfo:
			break;
		case Double:
			break;
		case Enumeration:
			break;
		case ExpandedNodeId:
			break;
		case ExtensionObject:
			break;
		case Float:
			break;
		case Guid:
			break;
		case Int16:
			break;
		case Int32:
			break;
		case Int64:
			break;
		case Integer:
			break;
		case LocalizedText:
			break;
		case NodeId:
			break;
		case Null:
			break;
		case Number:
			break;
		case QualifiedName:
			break;
		case SByte:
			break;
		case StatusCode:
			break;
		case UInt16:
			break;
		case UInt32:
			break;
		case UInt64:
			break;
		case UInteger:
			break;
		case Variant:
			break;
		case XmlElement:
			break;
		default:
			break;
		}
		if (isScalarArray && array == null && buffer == null) {
			return new StatusCode(StatusCodes.Bad_IndexRangeInvalid);
		}
		// }
		// check multidimensional arrays
		int length = -1;
		if (obj != null) {
			length = obj.length;
		} else if (array != null) {
			length = array.length;
		} else if (buffer != null) {
			length = buffer.length;
		}
		int begin = range.getBegin();
		// choose a default start
		if (begin == -1) {
			begin = 0;
		}
		// return an empty array if begin is beyond the end of the array
		if (begin >= length) {
			return new StatusCode(StatusCodes.Bad_IndexRangeNoData);
		}
		ValueRanks nodeValueRankDimension = ValueRanks.getValueRanks(((VariableNode) node).getValueRank());
		// check dimension value rank (look for scalar)
		if (nodeValueRankDimension == ValueRanks.Scalar && !isScalarArray) {
			return new StatusCode(StatusCodes.Bad_IndexRangeNoData);
		}
		// only copy if actually asking for a subset
		int end = range.getEnd();
		// check if looking for a single element
		if (end == -1) {
			end = begin;
		}
		// ensure end of array is not exceeded
		else if (end >= length - 1) {
			end = length - 1;
		}
		Object clone = null;
		int sublength = end - begin + 1;
		// maybe check for collection list
		// TODO: but not now at this version built
		if (obj != null && info != null) {
			// System.out.println("Check collection");
		}
		// handle array of strings
		if (isString) {
			char[] iBuffer = Arrays.copyOfRange(buffer, begin, begin + sublength);
			clone = new String(iBuffer);
			// clone = new Character[sublength];
		} else if (obj != null) {
			try {
				clone = Arrays.copyOfRange(obj, begin, begin + sublength);
			} catch (Exception e) {
				Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		} else if (array != null) {
			try {
				clone = Arrays.copyOfRange(array, begin, begin + sublength);
			} catch (Exception e) {
				Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		value.setValue(new Variant(clone));
		return StatusCode.GOOD;
	}

	private static StatusCode applyRangeOnDimensionValue(Node node, NumericRange range, Object objValue,
			DataValue value, TypeTable typeTree, int index) {
		DataValue dv = new DataValue(new Variant(objValue));
		TypeInfo info = TypeInfo.construct(dv, typeTree);
		char[] buffer = null;
		byte[] array = null;
		boolean isScalarArray = false;
		boolean isString = false;
		// check for string
		// if (obj == null) {
		switch (info.getBuiltInsType()) {
		case ByteString:
			array = (byte[]) objValue;
			isScalarArray = true;
			break;
		case String:
			try {
				String chars = (String) objValue;
				buffer = chars.toCharArray();
				// byte[] b = new byte[buffer.length << 1];
				// CharBuffer cBuffer = ByteBuffer.wrap(b).asCharBuffer();
				// for (int i = 0; i < buffer.length; i++) {
				// cBuffer.put(buffer[i]);
				// }
				isString = true;
				isScalarArray = true;
			} catch (Exception e) {
				Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
			break;
		case Boolean:
			break;
		case Byte:
			break;
		case DataValue:
			break;
		case DateTime:
			break;
		case DiagnosticInfo:
			break;
		case Double:
			break;
		case Enumeration:
			break;
		case ExpandedNodeId:
			break;
		case ExtensionObject:
			break;
		case Float:
			break;
		case Guid:
			break;
		case Int16:
			break;
		case Int32:
			break;
		case Int64:
			break;
		case Integer:
			break;
		case LocalizedText:
			break;
		case NodeId:
			break;
		case Null:
			break;
		case Number:
			break;
		case QualifiedName:
			break;
		case SByte:
			break;
		case StatusCode:
			break;
		case UInt16:
			break;
		case UInt32:
			break;
		case UInt64:
			break;
		case UInteger:
			break;
		case Variant:
			break;
		case XmlElement:
			break;
		default:
			break;
		}
		if (array == null && buffer == null) {
			return new StatusCode(StatusCodes.Bad_IndexRangeInvalid);
		}
		// }
		// check multidimensional arrays
		int length = -1;
		// if (obj != null) {
		// length = obj.length;
		// } else
		if (array != null) {
			length = array.length;
		} else if (buffer != null) {
			length = buffer.length;
		}
		int begin = range.getBegin();
		// choose a default start
		if (begin == -1) {
			begin = 0;
		}
		// return an empty array if begin is beyond the end of the array
		if (begin >= length) {
			return new StatusCode(StatusCodes.Bad_IndexRangeNoData);
		}
		ValueRanks nodeValueRankDimension = ValueRanks.getValueRanks(((VariableNode) node).getValueRank());
		// check dimension value rank (look for scalar)
		if (nodeValueRankDimension == ValueRanks.Scalar && !isScalarArray) {
			return new StatusCode(StatusCodes.Bad_IndexRangeNoData);
		}
		// only copy if actually asking for a subset
		int end = range.getEnd();
		// check if looking for a single element
		if (end == -1) {
			end = begin;
		}
		// ensure end of array is not exceeded
		else if (end >= length - 1) {
			end = length - 1;
		}
		Object clone = null;
		int sublength = end - begin + 1;
		// maybe check for collection list TODO: but not now at this version
		// built
		// if (obj != null && info != null) {
		// System.out.println("Check collection");
		// }
		// handle array of strings
		if (isString) {
			char[] iBuffer = Arrays.copyOfRange(buffer, begin, begin + sublength);
			clone = new String(iBuffer);
			// clone = new Character[sublength];
		}
		// else if (obj != null) {
		// try {
		// clone = Arrays.copyOfRange(obj, begin, begin + sublength);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		else if (array != null) {
			try {
				clone = Arrays.copyOfRange(array, begin, begin + sublength);
			} catch (Exception e) {
				Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		Object[] origin = (Object[]) value.getValue().getValue();
		origin[index] = clone;
		return StatusCode.GOOD;
	}

	/**
	 * Encodes the object in XML or Binary
	 * 
	 * @param Encodeable Object that will be encoded.
	 * @param UseXML     Uses the xml encoding.
	 * @return Encoded value.
	 * 
	 * @throws EncodingException
	 */
	private static ExtensionObject encodeValue(IEncodeable encodeable, boolean useXML) throws EncodingException {
		// XML is not supported in the java stack at the moment
		if (useXML) {
			ExtensionObject encodedValue = ExtensionObject.xmlEncode((Structure) encodeable);
			return encodedValue;
		} else {
			ExtensionObject encodedValue = ExtensionObject.binaryEncode((Structure) encodeable,
					EncoderContext.getDefaultInstance());
			return encodedValue;
		}
	}

	/**
	 * Applies the index range and the data encoding.
	 * 
	 * @param node
	 * 
	 * @param typeTable
	 * @param addressSpace
	 * 
	 * @param Context      ReadContext from a read operation.
	 * @param IndexRange   IndexRange from a read operation.
	 * @param DataEncoding DataEncoding from a read operation.
	 * @param Value        Read Value.
	 * @return Read Result.
	 */
	public static StatusCode validateIndexRangeAndDataEncoding(Node node, String indexRange,
			QualifiedName dataEncoding) {
		StatusCode error = StatusCode.GOOD;
		/** parses the string to the index range */
		try {
			/** NumericRange range = */
			NumericRange.parse(indexRange);
			// validateNumericRange(indexRange);
		} catch (ServiceResultException e) {
			error = e.getStatusCode();
		}
		return error;
	}
	// private static void validateNumericRange(String textToParse) throws
	// ServiceResultException {
	// NumericRange range = NumericRange.getEmpty(); // new NumericRange(-1,
	// // -1);
	// if (textToParse == null || textToParse.length() == 0) {
	// return;
	// }
	//
	// int index = textToParse.indexOf(",");
	//
	// if (index >= 0) {
	// int start = 0;
	// List<NumericRange> subranges = new ArrayList<>();
	//
	// for (int i = 0; i < textToParse.length(); i++) {
	// char ch = textToParse.charAt(i);
	//
	// if (ch == ',' || i == textToParse.length() - 1) {
	// String subtext = (ch == ',') ? textToParse.substring(start, i - start)
	// : textToParse.substring(start);
	// NumericRange subrange = NumericRange.parse(subtext);
	// subranges.add(subrange);
	// start = i + 1;
	// }
	// }
	//
	// // must have at least two entries
	// if (subranges.size() < 2) {
	// throw new ServiceResultException(StatusCodes.Bad_IndexRangeInvalid);
	// }
	//
	// range.setBegin(subranges.get(0).getBegin());
	// range.setEnd(subranges.get(0).getEnd());
	// range.setSubranges(subranges.toArray(new NumericRange[0]));
	// return;
	// }
	//
	// try {
	// index = textToParse.indexOf(':');
	// if (index != -1) {
	// range.setBegin(Integer.parseInt(textToParse.substring(0, index)));
	// range.setEnd(Integer.parseInt(textToParse.substring(index + 1)));
	//
	// if (range.getEnd() < 0)
	// throw new IllegalArgumentException("End");
	//
	// if (range.getBegin() >= range.getEnd())
	// throw new IllegalArgumentException("Begin > End");
	//
	// if (range.getBegin() < 0)
	// throw new IllegalArgumentException("Begin");
	// } else {
	// int parseBegin = Integer.parseInt(textToParse);
	// setBegin(range, parseBegin);
	// setEnd(range, -1);
	// if (parseBegin < 0) {
	// throw new IllegalArgumentException("Begin");
	// }
	// // range.setEnd(-1);
	// }
	//
	// } catch (Exception e) {
	// throw new ServiceResultException(StatusCodes.Bad_IndexRangeInvalid,
	// "Cannot parse numeric range: " + textToParse + ".");
	// }
	//
	// // return ;
	// }
	// private static void setBegin(NumericRange range, int value) {
	// if (value < -1) {
	// throw new IllegalArgumentException("Begin");
	// }
	//
	// if (range.getBegin() != -1 && (range.getBegin() > range.getEnd() ||
	// range.getBegin() < 0)) {
	// throw new IllegalArgumentException("Begin < End");
	// }
	//
	// // range.setBegin(value);
	// }

	// private static void setEnd(NumericRange range, int value) {
	// if (value < -1) {
	// throw new IllegalArgumentException("End");
	// }
	//
	// if (range.getEnd() != -1 && (range.getBegin() > range.getEnd() ||
	// range.getBegin() < 0)) {
	// throw new IllegalArgumentException("Begin > End");
	// }
	//
	// // this.end_ = value;
	// }
	public static ServiceResult validateFilter(UnsignedInteger attributeId, Node node, MonitoringFilter filter,
			TypeTable typeTable) {
		if (filter == null) {
			return null;
		}
		MonitoringFilterResult filterResult = null;
		if (filter instanceof DataChangeFilter) {
			if (!Attributes.Value.equals(attributeId)) {
				return new ServiceResult(StatusCodes.Bad_FilterNotAllowed);
			}
			if (node == null) {
				return new ServiceResult(StatusCodes.Bad_FilterNotAllowed);
			}
			/** check for status filter */
			if (DeadbandType.None.getValue() == ((DataChangeFilter) filter).getDeadbandType().getValue()) {
				return null;
			}
			/** deadbands are only for numerics */
			if (!typeTable.isTypeOf(((VariableNode) node).getDataType(), Identifiers.Number)) {
				return new ServiceResult(StatusCodes.Bad_FilterNotAllowed);
			}
			/** nothing more to do for absulute filter */
			if (((DataChangeFilter) filter).getDeadbandType().getValue() == DeadbandType.Absolute.getValue()) {
				return null;
			}
			/**
			 * need to look up the EU range if a percent filter is requested.
			 */
			if (((DataChangeFilter) filter).getDeadbandType().getValue() == DeadbandType.Percent.getValue()) {
				// get child with browsename "EURANGE"
			}
		} else if (filter instanceof EventFilter) {
		} else if (filter instanceof AggregateFilter) {
			if (!Attributes.Value.equals(attributeId)) {
				return new ServiceResult(StatusCodes.Bad_FilterNotAllowed);
			}
			AggregateFilter revisedFilter = new AggregateFilter();
			revisedFilter.setAggregateType(((AggregateFilter) filter).getAggregateType());
			revisedFilter.setStartTime(((AggregateFilter) filter).getStartTime());
			revisedFilter.setProcessingInterval(((AggregateFilter) filter).getProcessingInterval());
			revisedFilter.setAggregateConfiguration(((AggregateFilter) filter).getAggregateConfiguration());
			filterResult = new AggregateFilterResult();
			((AggregateFilterResult) filterResult)
					.setRevisedProcessingInterval(((AggregateFilter) filter).getProcessingInterval());
			((AggregateFilterResult) filterResult).setRevisedStartTime(((AggregateFilter) filter).getStartTime());
			((AggregateFilterResult) filterResult)
					.setRevisedAggregateConfiguration(((AggregateFilter) filter).getAggregateConfiguration());
		}
		return null;
	}

	public static StatusCode validateMonitoredItem(MonitoredItemCreateRequest item) {
		/** check for null structure. */
		if (item == null) {
			return new StatusCode(StatusCodes.Bad_StructureMissing);
		}
		/** validate read value id component. */
		ServiceResult error = opc.sdk.core.utils.ReadValueId.validate(item.getItemToMonitor());
		if (error != null) {
			return error.getCode();
		}
		/** validate numeric range */
		try {
			NumericRange.parse(item.getItemToMonitor().getIndexRange());
		} catch (Exception e) {
			return new StatusCode(StatusCodes.Bad_IndexRangeInvalid);
		}
		/** check for valid monitoring mode. */
		if (item.getMonitoringMode().getValue() < 0
				|| item.getMonitoringMode().getValue() > MonitoringMode.Reporting.getValue()) {
			return new StatusCode(StatusCodes.Bad_MonitoringModeInvalid);
		}
		/** check for null structure. */
		MonitoringParameters attributes = item.getRequestedParameters();
		error = validateMonitoringParameters(attributes);
		if (error != null) {
			return error.getCode();
		}
		/** check that no filter is specified for non-value attributes. */
		if (!Attributes.Value.equals(item.getItemToMonitor().getAttributeId())
				&& !Attributes.EventNotifier.equals(item.getItemToMonitor().getAttributeId())) {
			if (attributes.getFilter() != null) {
				return new StatusCode(StatusCodes.Bad_FilterNotAllowed);
			}
		} else {
			error = validateMonitoringFilter(attributes.getFilter());
			if (error != null) {
				return error.getCode();
			}
		} /** passed basic validation. */
		return null;
	}

	private static ServiceResult validateMonitoringFilter(ExtensionObject filter) {
		/** check that no filter is specified for non-value attributes. */
		ServiceResult result = null;
		if (filter != null) {
			try {
				IEncodeable decoded = filter.decode(EncoderContext.getDefaultInstance());
				if (decoded instanceof DataChangeFilter) {
					result = validateDataChangeFilter((DataChangeFilter) decoded);
				} else if (decoded instanceof EventFilter) {
					result = validateEventFilter((EventFilter) decoded).getStatus();
				}
			} catch (DecodingException e) {
				result = new ServiceResult(e.getStatusCode());
			}
		}
		return result;
	}

	/**
	 * Validates a DataChange filter.
	 * 
	 * @param Decoded Filter to validate.
	 * @return service error.
	 */
	private static ServiceResult validateDataChangeFilter(DataChangeFilter filter) {
		/** check deadbandtype enum */
		UnsignedInteger dbType = filter.getDeadbandType();
		if (DeadbandType.None.getValue() > dbType.getValue() || dbType.getValue() > DeadbandType.Percent.getValue()) {
			return new ServiceResult(StatusCodes.Bad_DeadbandFilterInvalid);
		}
		/** check datachange trigger enum */
		DataChangeTrigger trigger = filter.getTrigger();
		if (DataChangeTrigger.Status.getValue() > trigger.getValue()
				|| trigger.getValue() > DataChangeTrigger.StatusValueTimestamp.getValue()) {
			return new ServiceResult(StatusCodes.Bad_DeadbandFilterInvalid);
		}
		/** deadbandvalue greater than 0 */
		Double dbValue = filter.getDeadbandValue();
		if (dbValue < 0) {
			return new ServiceResult(StatusCodes.Bad_DeadbandFilterInvalid);
		}
		/** deadband percent less than 100 */
		if (dbType.getValue() == DeadbandType.Percent.getValue()) {
			if (dbValue > 100) {
				return new ServiceResult(StatusCodes.Bad_DeadbandFilterInvalid);
			}
		}
		return null;
	}

	/**
	 * Validates a event filter for monitored items.
	 * 
	 * @param EventFilterContext Filter context for an event filter.
	 * @return Result of the validation.
	 */
	private static EventFilterValidationResult validateEventFilter(EventFilter filter) {
		EventFilterValidationResult result = new EventFilterValidationResult();
		if (filter.getSelectClauses() == null || filter.getSelectClauses().length == 0) {
			result.setStatus(new ServiceResult(StatusCodes.Bad_StructureMissing));
			return result;
		}
		if (filter.getWhereClause() == null) {
			result.setStatus(new ServiceResult(StatusCodes.Bad_StructureMissing));
			return result;
		}
		result.setStatus(new ServiceResult(StatusCode.GOOD));
		/** validate select clause */
		boolean error = false;
		Map<SimpleAttributeOperand, Boolean> isValidated = new HashMap<SimpleAttributeOperand, Boolean>();
		for (SimpleAttributeOperand clause : filter.getSelectClauses()) {
			ServiceResult clauseResult = null;
			if (clause == null) {
				clauseResult = new ServiceResult(StatusCodes.Bad_StructureMissing);
				result.getSelecteClauseResults().add(clauseResult);
				error = true;
				continue;
			}
			/** validate clause */
			clauseResult = validateSimpleAttributeOperand(filter, 0, isValidated);
			if (clauseResult.isBad()) {
				result.addSelecteClauseResults(clauseResult);
				error = true;
				continue;
			}
			result.addSelecteClauseResults(clauseResult);
		}
		if (error) {
			result.setStatus(new ServiceResult(StatusCodes.Bad_EventFilterInvalid));
		} else {
			result.getSelecteClauseResults().clear();
		}
		/** validate where clause */
		result.setWhereClauseResults(validateWhereClause(filter, filter.getWhereClause()));
		if (result.getWhereClauseResults().getStatus().isBad()) {
			result.setStatus(new ServiceResult(StatusCodes.Bad_EventFilterInvalid));
		}
		return result;
	}

	/**
	 * Validates the element result of the event filter.
	 * 
	 * @param FilterContext Context of the filter
	 * @param Element       Content element of the filter
	 * @param Index         Index of the content element
	 * @return Element result of the validation.
	 */
	private static ElementResult validateElementResult(EventFilter filter, ContentFilterElement element, int index) {
		ElementResult result = new ElementResult(null);
		int operandCount = -1;
		switch (element.getFilterOperator()) {
		case Not:
		case IsNull:
		case InView:
		case OfType:
			operandCount = 1;
			break;
		case And:
		case Or:
		case Equals:
		case GreaterThan:
		case GreaterThanOrEqual:
		case LessThan:
		case LessThanOrEqual:
		case Like:
		case Cast:
			operandCount = 2;
			break;
		case Between:
			operandCount = 3;
			break;
		case RelatedTo:
			operandCount = 4;
			break;
		case InList:
			operandCount = -1;
			break;
		default:
			break;
		}
		if (operandCount != -1) {
			if (operandCount != element.getFilterOperands().length) {
				result.setStatus(new ServiceResult(StatusCodes.Bad_EventFilterInvalid));
				return result;
			}
		} else {
			if (element.getFilterOperands().length < 2) {
				result.setStatus(new ServiceResult(StatusCodes.Bad_EventFilterInvalid));
				return result;
			}
		}
		/** validate the operands of the element */
		boolean error = false;
		for (int ii = 0; ii < element.getFilterOperands().length; ii++) {
			ServiceResult operandResult = null;
			ExtensionObject operand = element.getFilterOperands()[ii];
			// check for null
			if (operand == null) {
				operandResult = new ServiceResult(StatusCodes.Bad_EventFilterInvalid);
				result.addOperandResult(operandResult);
				error = true;
				continue;
			}
			// check that the extension object contains a filter operand
			FilterOperand filterOperand = null;
			try {
				filterOperand = operand.decode(EncoderContext.getDefaultInstance());
			} catch (DecodingException e) {
				Logger.getLogger(OPCValidationFramework.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
			if (filterOperand == null) {
				operandResult = new ServiceResult(StatusCodes.Bad_EventFilterInvalid);
				result.addOperandResult(operandResult);
				error = true;
				continue;
			}
			/** validate the operand */
			operandResult = validateFilterOperand(filter, filterOperand, index);
			if (operandResult != null && operandResult.isBad()) {
				result.addOperandResult(operandResult);
				error = true;
				continue;
			}
			result.addOperandResult(null);
		}
		/** ensure the global error code */
		if (error) {
			result.setStatus(new ServiceResult(StatusCodes.Bad_ContentFilterInvalid));
		} else {
			result.getOperandResults().clear();
		}
		return result;
	}

	private static ServiceResult validateFilterOperand(EventFilter filter, FilterOperand filterOperand, int index) {
		/** validates the operand */
		if (filterOperand instanceof LiteralOperand) {
			if (((LiteralOperand) filterOperand).getValue() == null) {
				return new ServiceResult(StatusCodes.Bad_EventFilterInvalid);
			}
			return new ServiceResult(StatusCode.GOOD);
		} else if (filterOperand instanceof ElementOperand) {
			if (((ElementOperand) filterOperand).getIndex().intValue() < 0) {
				return new ServiceResult(StatusCodes.Bad_FilterOperandInvalid);
			}
			if (((ElementOperand) filterOperand).getIndex().intValue() <= index) {
			}
			// if(((ElementOperand)filterOperand).getIndex().intValue() >= )
			// TODO
			System.out
					.println("TODO: [createMonitoredItemServiceImpl.validateFilterOperand] ElementOperand validation");
			return new ServiceResult(StatusCode.GOOD);
		} else if (filterOperand instanceof AttributeOperand) {
			System.out.println(
					"TODO: [createMonitoredItemServiceImpl.validateFilterOperand] AttributeOperand validation");
			return new ServiceResult(StatusCode.GOOD);
		} else if (filterOperand instanceof SimpleAttributeOperand) {
			System.out.println(
					"TODO: [createMonitoredItemServiceImpl.validateFilterOperand] SimpleAttributeOperand validation");
			return new ServiceResult(StatusCode.GOOD);
		}
		return null;
	}

	/**
	 * Validates a simple attribute operand for an event filter
	 * 
	 * @param filter
	 * 
	 * @param FilterContext Context of the event filter.
	 * @param Index         Index of the operand.
	 * @param IsValidated   Collection of the operands, true means validated, false
	 *                      needs to validate.
	 * @return Result of the validation.
	 */
	private static ServiceResult validateSimpleAttributeOperand(EventFilter filter, int index,
			Map<SimpleAttributeOperand, Boolean> isValidated) {
		boolean validated = false;
		/** verify attribute id */
		if (!(AttributesUtil.isValid(filter.getSelectClauses()[index].getAttributeId()))) {
			return new ServiceResult(StatusCodes.Bad_AttributeIdInvalid);
		}
		/** parse the index range */
		if (!(filter.getSelectClauses()[index].getIndexRange() == null
				|| filter.getSelectClauses()[index].getIndexRange().isEmpty())) {
			try {
				NumericRange.parse(filter.getSelectClauses()[index].getIndexRange());
			} catch (Exception e) {
				return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
			}
		}
		if (!filter.getSelectClauses()[index].getAttributeId().equals(Attributes.Value)) {
			return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
		}
		validated = true;
		/** store in a collection */
		isValidated.put(filter.getSelectClauses()[index], validated);
		return new ServiceResult(StatusCode.GOOD);
	}

	/**
	 * SubMethod of validateMonitoredItemCreateResult TODO: NOT IMPLEMENTED
	 * 
	 * 
	 * @param Attributes
	 * @return ServiceResult GOOD.
	 */
	private static ServiceResult validateMonitoringParameters(MonitoringParameters attributes) {
		/** null structure */
		if (attributes == null) {
			return new ServiceResult(StatusCodes.Bad_StructureMissing);
		}
		/** check for known filter */
		if (attributes.getFilter() != null) {
			try {
				MonitoringFilter filter = attributes.getFilter().decode(EncoderContext.getDefaultInstance());
				if (filter == null) {
					return new ServiceResult(StatusCodes.Bad_MonitoredItemFilterInvalid);
				}
			} catch (DecodingException e) {
				return new ServiceResult(StatusCodes.Bad_MonitoredItemFilterInvalid);
			}
		}
		/** passed basic validation */
		return null;
	}

	/**
	 * Validates the WhereClause of the event filter.
	 * 
	 * @param FilterContext Context of the event filter.
	 * @param WhereClause   Wherechlause of the filter.
	 * @return Result for the validation
	 */
	private static ContentFilterValidationResult validateWhereClause(EventFilter filter, ContentFilter whereClause) {
		ContentFilterValidationResult result = new ContentFilterValidationResult(null);
		/** check for empty filter */
		if (whereClause.getElements() == null || whereClause.getElements().length == 0) {
			return result;
		}
		boolean error = false;
		for (int ii = 0; ii < whereClause.getElements().length; ii++) {
			ContentFilterElement element = whereClause.getElements()[ii];
			/** check for null */
			if (element == null) {
				ServiceResult nullResult = new ServiceResult(StatusCodes.Bad_StructureMissing);
				result.getElementResults().add(new ElementResult(nullResult));
				error = true;
				continue;
			}
			// element.setParent(contentfilter...);
			ElementResult elementResult = validateElementResult(filter, element, ii);
			if (elementResult.getStatus().isBad()) {
				result.getElementResults().add(elementResult);
				error = true;
				continue;
			}
			result.getElementResults().add(null);
		}
		/** ensure the global error code */
		if (error) {
			result.setStatus(new ServiceResult(StatusCodes.Bad_ContentFilterInvalid));
		} else {
			result.getElementResults().clear();
		}
		return result;
	}
}
