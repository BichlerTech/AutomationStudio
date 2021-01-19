package com.bichler.astudio.opcua.opcmodeler.views.namespacebrowser;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.opcua.opcmodeler.views.namespacebrowser.properties.NamespacePropertySource;

public class NamespaceModelNodeAdapterFactory implements IAdapterFactory {
	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject instanceof NamespaceModelNode) {
			return (T) new NamespacePropertySource(((NamespaceModelNode) adaptableObject).getNode());
			// NodeClass nodeClass =
			// ((NamespaceModelNode)adaptableObject).getNode().getNodeClass();
			//
			// switch(nodeClass){
			// case Object:
			// return new ObjectNodeModelPropertySource((BrowserModelNode)
			// adaptableObject);
			// case ObjectType:
			// return new ObjectTypeNodeModelPropertySource((BrowserModelNode)
			// adaptableObject);
			// case Variable:
			// return new VariableNodeModelPropertySource((BrowserModelNode)
			// adaptableObject);
			// case VariableType:
			// return new VariableTypeNodeModelPropertySource((BrowserModelNode)
			// adaptableObject);
			// case Method:
			// return new MethodNodeModelPropertySource((BrowserModelNode)
			// adaptableObject);
			// case ReferenceType:
			// return new ReferenceTypeNodeModelPropertySource((BrowserModelNode)
			// adaptableObject);
			// case DataType:
			// return new DatatypeNodeModelPropertySource((BrowserModelNode)
			// adaptableObject);
			// case View:
			// return new ViewNodeModelPropertySource((BrowserModelNode)
			// adaptableObject);
			//
			// }
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}
}
