package opc.sdk.core.node;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.DataTypeAttributes;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ObjectTypeAttributes;
import org.opcfoundation.ua.core.ReferenceTypeAttributes;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.core.VariableTypeAttributes;
import org.opcfoundation.ua.core.ViewAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

/**
 * Factory to create node attributes.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class NodeAttributeFactory {
	public static ExtensionObject createNodeAttributes(NodeClass nodeClass, DataValue[] results)
			throws ServiceResultException {
		if (results == null) {
			throw new ServiceResultException(StatusCodes.Bad_UnexpectedError);
		}
		ExtensionObject attributes = createInstance(nodeClass, results);
		return attributes;
	}

	private static ExtensionObject createInstance(NodeClass nodeClass, DataValue[] results) throws EncodingException {
		NodeAttributes attributes = null;
		ExtensionObject encodedAttributes = null;
		Variant value = null;
		if (nodeClass != null) {
			switch (nodeClass) {
			case Object:
				attributes = new ObjectAttributes();
				value = results[Attributes.Description.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ObjectAttributes) attributes).setDescription((LocalizedText) value.getValue());
				}
				value = results[Attributes.DisplayName.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ObjectAttributes) attributes).setDisplayName((LocalizedText) value.getValue());
				}
				value = results[Attributes.WriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ObjectAttributes) attributes).setWriteMask((UnsignedInteger) value.getValue());
				}
				value = results[Attributes.UserWriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ObjectAttributes) attributes).setUserWriteMask((UnsignedInteger) value.getValue());
				}
				value = results[7].getValue();
				if (value != null && !value.isEmpty()) {
					((ObjectAttributes) attributes).setEventNotifier((UnsignedByte) value.getValue());
				}
				break;
			case ObjectType:
				attributes = new ObjectTypeAttributes();
				value = results[Attributes.Description.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ObjectTypeAttributes) attributes).setDescription((LocalizedText) value.getValue());
				}
				value = results[Attributes.DisplayName.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ObjectTypeAttributes) attributes).setDisplayName((LocalizedText) value.getValue());
				}
				value = results[Attributes.IsAbstract.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ObjectTypeAttributes) attributes).setIsAbstract((Boolean) value.getValue());
				}
				value = results[Attributes.WriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ObjectTypeAttributes) attributes).setWriteMask((UnsignedInteger) value.getValue());
				}
				value = results[Attributes.UserWriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ObjectTypeAttributes) attributes).setUserWriteMask((UnsignedInteger) value.getValue());
				}
				break;
			case Variable:
				attributes = new VariableAttributes();
				value = results[Attributes.Description.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableAttributes) attributes).setDescription((LocalizedText) value.getValue());
				}
				value = results[Attributes.DisplayName.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableAttributes) attributes).setDisplayName((LocalizedText) value.getValue());
				}
				value = results[Attributes.WriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableAttributes) attributes).setWriteMask((UnsignedInteger) value.getValue());
				}
				value = results[Attributes.UserWriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableAttributes) attributes).setUserWriteMask((UnsignedInteger) value.getValue());
				}
				/** access level */
				value = results[11].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableAttributes) attributes).setAccessLevel((UnsignedByte) value.getValue());
				}
				/** array dimensions */
				value = results[10].getValue();
				if (value != null && !value.isEmpty()) {
					if (value.getValue() instanceof UnsignedInteger[]) {
						((VariableAttributes) attributes).setArrayDimensions((UnsignedInteger[]) value.getValue());
					} else if (value.getValue() instanceof UnsignedInteger) {
						UnsignedInteger[] vr = new UnsignedInteger[1];
						vr[0] = (UnsignedInteger) value.getValue();
						((VariableAttributes) attributes).setArrayDimensions(vr);
					}
				}
				/** datatype */
				value = results[8].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableAttributes) attributes).setDataType((NodeId) value.getValue());
				}
				/** historizing */
				value = results[14].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableAttributes) attributes).setHistorizing((Boolean) value.getValue());
				}
				/** minimum sampling interval */
				value = results[13].getValue();
				if (value != null && !value.isEmpty()) {
					Number msi = (Number) value.getValue();
					((VariableAttributes) attributes).setMinimumSamplingInterval(msi.doubleValue());
				}
				/** user access level */
				value = results[12].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableAttributes) attributes).setUserAccessLevel((UnsignedByte) value.getValue());
				}
				/** value */
				value = results[7].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableAttributes) attributes).setValue(value);
				}
				/** value rank */
				value = results[9].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableAttributes) attributes).setValueRank((Integer) value.getValue());
				}
				break;
			case VariableType:
				attributes = new VariableTypeAttributes();
				// Utils.VARIABLETYPEATTRIBUTES;
				value = results[Attributes.Description.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableTypeAttributes) attributes).setDescription((LocalizedText) value.getValue());
				}
				value = results[Attributes.DisplayName.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableTypeAttributes) attributes).setDisplayName((LocalizedText) value.getValue());
				}
				value = results[Attributes.WriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableTypeAttributes) attributes).setWriteMask((UnsignedInteger) value.getValue());
				}
				value = results[Attributes.UserWriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableTypeAttributes) attributes).setUserWriteMask((UnsignedInteger) value.getValue());
				}
				/** array dimensions */
				value = results[10].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableTypeAttributes) attributes).setArrayDimensions((UnsignedInteger[]) value.getValue());
				}
				/** data type */
				value = results[8].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableTypeAttributes) attributes).setDataType((NodeId) value.getValue());
				}
				/** is abstract */
				value = results[11].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableTypeAttributes) attributes).setIsAbstract((Boolean) value.getValue());
				}
				/** value */
				value = results[7].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableTypeAttributes) attributes).setValue(value);
				}
				/** value rank */
				value = results[9].getValue();
				if (value != null && !value.isEmpty()) {
					((VariableTypeAttributes) attributes).setValueRank((Integer) value.getValue());
				}
				break;
			case DataType:
				attributes = new DataTypeAttributes();
				value = results[Attributes.Description.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((DataTypeAttributes) attributes).setDescription((LocalizedText) value.getValue());
				}
				value = results[Attributes.DisplayName.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((DataTypeAttributes) attributes).setDisplayName((LocalizedText) value.getValue());
				}
				value = results[Attributes.WriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((DataTypeAttributes) attributes).setWriteMask((UnsignedInteger) value.getValue());
				}
				value = results[Attributes.UserWriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((DataTypeAttributes) attributes).setUserWriteMask((UnsignedInteger) value.getValue());
				}
				/** is abstract */
				value = results[7].getValue();
				if (value != null && !value.isEmpty()) {
					((DataTypeAttributes) attributes).setIsAbstract((Boolean) value.getValue());
				}
				break;
			case Method:
				attributes = new MethodAttributes();
				value = results[Attributes.Description.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((MethodAttributes) attributes).setDescription((LocalizedText) value.getValue());
				}
				value = results[Attributes.DisplayName.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((MethodAttributes) attributes).setDisplayName((LocalizedText) value.getValue());
				}
				value = results[Attributes.WriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((MethodAttributes) attributes).setWriteMask((UnsignedInteger) value.getValue());
				}
				value = results[Attributes.UserWriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((MethodAttributes) attributes).setUserWriteMask((UnsignedInteger) value.getValue());
				}
				/** executeable */
				value = results[7].getValue();
				if (value != null && !value.isEmpty()) {
					((MethodAttributes) attributes).setExecutable((Boolean) value.getValue());
				}
				/** user executeable */
				value = results[8].getValue();
				if (value != null && !value.isEmpty()) {
					((MethodAttributes) attributes).setUserExecutable((Boolean) value.getValue());
				}
				break;
			case ReferenceType:
				attributes = new ReferenceTypeAttributes();
				value = results[Attributes.Description.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ReferenceTypeAttributes) attributes).setDescription((LocalizedText) value.getValue());
				}
				value = results[Attributes.DisplayName.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ReferenceTypeAttributes) attributes).setDisplayName((LocalizedText) value.getValue());
				}
				value = results[Attributes.WriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ReferenceTypeAttributes) attributes).setWriteMask((UnsignedInteger) value.getValue());
				}
				value = results[Attributes.UserWriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ReferenceTypeAttributes) attributes).setUserWriteMask((UnsignedInteger) value.getValue());
				}
				/** inverse name */
				value = results[9].getValue();
				if (value != null && !value.isEmpty()) {
					((ReferenceTypeAttributes) attributes).setInverseName((LocalizedText) value.getValue());
				}
				/** is abstract */
				value = results[7].getValue();
				if (value != null && !value.isEmpty()) {
					((ReferenceTypeAttributes) attributes).setIsAbstract((Boolean) value.getValue());
				}
				/** symmetric */
				value = results[8].getValue();
				if (value != null && !value.isEmpty()) {
					((ReferenceTypeAttributes) attributes).setSymmetric((Boolean) value.getValue());
				}
				break;
			case View:
				attributes = new ViewAttributes();
				value = results[Attributes.Description.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ViewAttributes) attributes).setDescription((LocalizedText) value.getValue());
				}
				value = results[Attributes.DisplayName.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ViewAttributes) attributes).setDisplayName((LocalizedText) value.getValue());
				}
				value = results[Attributes.WriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ViewAttributes) attributes).setWriteMask((UnsignedInteger) value.getValue());
				}
				value = results[Attributes.UserWriteMask.intValue() - 1].getValue();
				if (value != null && !value.isEmpty()) {
					((ViewAttributes) attributes).setUserWriteMask((UnsignedInteger) value.getValue());
				}
				/** contains no loops */
				value = results[7].getValue();
				if (value != null && !value.isEmpty()) {
					((ViewAttributes) attributes).setContainsNoLoops((Boolean) value.getValue());
				}
				/** event notifiers */
				value = results[8].getValue();
				if (value != null && !value.isEmpty()) {
					((ViewAttributes) attributes).setEventNotifier((UnsignedByte) value.getValue());
				}
				break;
			default:
				// TODO add default value
				break;
			}
		}
		// if attributes available encode them
		if (attributes != null) {
			encodedAttributes = ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance());
		}
		return encodedAttributes;
	}
}
