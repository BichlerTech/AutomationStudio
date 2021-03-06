package opc.sdk.core.classes.ua;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.context.ISystemContext;
import opc.sdk.ua.classes.BaseDataVariableType;
import opc.sdk.ua.classes.OPCBaseDataVariableType;
import opc.sdk.ua.classes.BaseObjectType;
import opc.sdk.ua.classes.OPCObjectType;
import opc.sdk.ua.classes.OPCDataType;
import opc.sdk.ua.classes.BaseMethod;
import opc.sdk.ua.classes.BaseNode;
import opc.sdk.ua.classes.PropertyVariableType;
import opc.sdk.ua.classes.OPCReferenceType;
import opc.sdk.ua.classes.ViewType;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

/**
 * Factory to create nodestates.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class NodeStateFactory {
	private Map<NodeId, Class<?>> types = null;

	public BaseNode createInstance(ISystemContext context, BaseNode parent, NodeClass nodeClass, NodeId refernceTypeId,
			NodeId typeDefinitionId) {
		BaseNode child = null;
		if (this.types != null && this.types.containsKey(typeDefinitionId)) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "CREATECLASS");
			return null;
		}
		switch (nodeClass) {
		case Variable:
			if (context.getTypeTable() != null
					&& context.getTypeTable().isTypeOf(refernceTypeId, Identifiers.HasProperty))
				child = new PropertyVariableType<>(parent);
			else
				child = new BaseDataVariableType<>(parent);
			break;
		case Object:
			child = new BaseObjectType(parent);
			break;
		case ObjectType:
			child = new OPCObjectType(null);
			break;
		case Method:
			child = new BaseMethod(parent);
			break;
		case ReferenceType:
			child = new OPCReferenceType(null);
			break;
		case VariableType:
			child = new OPCBaseDataVariableType(null);
			break;
		case DataType:
			child = new OPCDataType(null);
			break;
		case View:
			child = new ViewType();
			break;
		default:
			break;
		}
		return child;
	}
}
