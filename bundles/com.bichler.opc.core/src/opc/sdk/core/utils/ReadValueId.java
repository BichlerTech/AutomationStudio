package opc.sdk.core.utils;

import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.utils.AttributesUtil;

public class ReadValueId extends org.opcfoundation.ua.core.ReadValueId {
	/**
	 * 
	 * @param valueId
	 * @return
	 */
	public static ServiceResult validate(org.opcfoundation.ua.core.ReadValueId valueId) {
		// Check for null structure.
		if (valueId == null) {
			return new ServiceResult(StatusCodes.Bad_StructureMissing);
		}
		// Null node ids are always invalid.
		if (valueId.getNodeId() == null) {
			return new ServiceResult(StatusCodes.Bad_NodeIdInvalid);
		}
		// must be a legitimate attribute value
		if (!AttributesUtil.isValid(valueId.getAttributeId())) {
			if (valueId.getAttributeId().compareTo(Attributes.DataTypeDefinition) != 0)
				return new ServiceResult(StatusCodes.Bad_AttributeIdInvalid);
		}
		// data encoding AND index range is only valid for value attributes
		if (!valueId.getAttributeId().equals(Attributes.Value)) {
			if (!QualifiedName.isNull(valueId.getDataEncoding())) {
				return new ServiceResult(StatusCodes.Bad_DataEncodingInvalid);
			}
			if (!(valueId.getIndexRange() == null || valueId.getIndexRange().isEmpty())) {
				return new ServiceResult(StatusCodes.Bad_IndexRangeNoData);
			}
		}
		// passed basic validation
		return null;
	}
}
