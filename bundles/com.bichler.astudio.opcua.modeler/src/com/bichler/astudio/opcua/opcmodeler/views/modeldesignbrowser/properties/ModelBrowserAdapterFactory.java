package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.properties;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

public class ModelBrowserAdapterFactory implements IAdapterFactory {
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		/**
		 * properties of model browser view
		 */
		// opc ua model browser view
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject instanceof BrowserModelNode) {
			NodeClass nodeClass = ((BrowserModelNode) adaptableObject).getNode().getNodeClass();
			switch (nodeClass) {
			case Object:
				return new ObjectNodeModelPropertySource((BrowserModelNode) adaptableObject);
			case ObjectType:
				return new ObjectTypeNodeModelPropertySource((BrowserModelNode) adaptableObject);
			case Variable:
				return new VariableNodeModelPropertySource((BrowserModelNode) adaptableObject);
			case VariableType:
				return new VariableTypeNodeModelPropertySource((BrowserModelNode) adaptableObject);
			case Method:
				return new MethodNodeModelPropertySource((BrowserModelNode) adaptableObject);
			case ReferenceType:
				return new ReferenceTypeNodeModelPropertySource((BrowserModelNode) adaptableObject);
			case DataType:
				return new DatatypeNodeModelPropertySource((BrowserModelNode) adaptableObject);
			case View:
				return new ViewNodeModelPropertySource((BrowserModelNode) adaptableObject);
			}
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}
}
