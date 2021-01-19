package com.bichler.astudio.opcua.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.opcua.nodes.OPCUAAdvancedServerDriversModelNode;
import com.bichler.astudio.opcua.nodes.OPCUARootModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerConfigModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverConfigModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverDPsModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriversModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerEcmaIntervalScriptsModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerEcmaScriptModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerEcmaScriptsModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerEcmaSingleScriptsModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelsNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerPostShellScriptModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerPostShellScriptsModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerPreShellScriptModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerPreShellScriptsModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerScriptsModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerShellScriptsModelNode;
import com.bichler.astudio.opcua.nodes.security.AbstractOPCUAServerCertificateStoreModelNode;
import com.bichler.astudio.opcua.nodes.security.OPCUAKeyPairModelNode;
import com.bichler.astudio.opcua.nodes.security.OPCUAServerSecurityModelNode;
import com.bichler.astudio.opcua.properties.InfoModelPropertySource;
import com.bichler.astudio.opcua.properties.InfoModelsPropertySource;
import com.bichler.astudio.opcua.properties.OPCUARootModelPropertySource;
import com.bichler.astudio.opcua.properties.OPCUAServerModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerAdvancedDriverModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerCertificateModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerCertificateTypeModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerConfigModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerDriverConfigModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerDriverDPsModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerDriverModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerDriversModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerEcmaIntervalScriptsModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerEcmaScriptModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerEcmaScriptsModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerEcmaSingleScriptsModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerPostShellScriptModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerPostShellScriptsModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerPreShellScriptModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerPreShellScriptsModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerScriptsModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerSecurityModelPropertySource;
import com.bichler.astudio.opcua.properties.ServerShellScriptsModelPropertySource;

public class OPCPropertyAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		/**
		 * properties of opc ua navigation view
		 */
		// opc ua server root navigation node
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUARootModelNode.class) {
			return new OPCUARootModelPropertySource((OPCUARootModelNode) adaptableObject);
		}
		// opc ua server navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerModelNode.class) {
			return new OPCUAServerModelPropertySource((OPCUAServerModelNode) adaptableObject);
		}
		// opc ua information models root navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerInfoModelsNode.class) {
			return new InfoModelsPropertySource((OPCUAServerInfoModelsNode) adaptableObject);
		}
		// opc ua information model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerInfoModelNode.class) {
			return new InfoModelPropertySource((OPCUAServerInfoModelNode) adaptableObject);
		}
		// opc ua server configuration model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerConfigModelNode.class) {
			return new ServerConfigModelPropertySource((OPCUAServerConfigModelNode) adaptableObject);
		}
		// opc ua server scripts root model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerScriptsModelNode.class) {
			return new ServerScriptsModelPropertySource((OPCUAServerScriptsModelNode) adaptableObject);
		}
		// opc ua ecma scripts root model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerEcmaScriptsModelNode.class) {
			return new ServerEcmaScriptsModelPropertySource((OPCUAServerEcmaScriptsModelNode) adaptableObject);
		}
		// opc ua ecma single scripts root model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerEcmaSingleScriptsModelNode.class) {
			return new ServerEcmaSingleScriptsModelPropertySource(
					(OPCUAServerEcmaSingleScriptsModelNode) adaptableObject);
		}
		// opc ua ecma interval scripts root model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerEcmaIntervalScriptsModelNode.class) {
			return new ServerEcmaIntervalScriptsModelPropertySource(
					(OPCUAServerEcmaIntervalScriptsModelNode) adaptableObject);
		}

		// opc ua ecma script model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerEcmaScriptModelNode.class) {
			return new ServerEcmaScriptModelPropertySource((OPCUAServerEcmaScriptModelNode) adaptableObject);
		}
		// opc ua shell scripts root model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerShellScriptsModelNode.class) {
			return new ServerShellScriptsModelPropertySource((OPCUAServerShellScriptsModelNode) adaptableObject);
		}
		// opc ua pre shell scripts root model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerPreShellScriptsModelNode.class) {
			return new ServerPreShellScriptsModelPropertySource((OPCUAServerPreShellScriptsModelNode) adaptableObject);
		}
		// opc ua post shell scripts root model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerPostShellScriptsModelNode.class) {
			return new ServerPostShellScriptsModelPropertySource(
					(OPCUAServerPostShellScriptsModelNode) adaptableObject);
		}
		// opc ua pre shell script model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerPreShellScriptModelNode.class) {
			return new ServerPreShellScriptModelPropertySource((OPCUAServerPreShellScriptModelNode) adaptableObject);
		}
		// opc ua post shell script model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerPostShellScriptModelNode.class) {
			return new ServerPostShellScriptModelPropertySource((OPCUAServerPostShellScriptModelNode) adaptableObject);
		}
		// opc ua server security root model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerSecurityModelNode.class) {
			return new ServerSecurityModelPropertySource((OPCUAServerSecurityModelNode) adaptableObject);
		}
		// opc ua server security certificate type model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == AbstractOPCUAServerCertificateStoreModelNode.class) {
			return new ServerCertificateTypeModelPropertySource(
					(AbstractOPCUAServerCertificateStoreModelNode) adaptableObject);
		}
		// opc ua server security certificate model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAKeyPairModelNode.class) {
			return new ServerCertificateModelPropertySource((OPCUAKeyPairModelNode) adaptableObject);
		}
		// opc ua server drivers root model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerDriversModelNode.class) {
			return new ServerDriversModelPropertySource((OPCUAServerDriversModelNode) adaptableObject);
		}
		// opc ua server driver model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerDriverModelNode.class) {
			return new ServerDriverModelPropertySource((OPCUAServerDriverModelNode) adaptableObject);
		}
		// opc ua server driver dps model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerDriverDPsModelNode.class) {
			return new ServerDriverDPsModelPropertySource((OPCUAServerDriverDPsModelNode) adaptableObject);
		}
		// opc ua advanced server driver model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAAdvancedServerDriversModelNode.class) {
			return new ServerAdvancedDriverModelPropertySource((OPCUAAdvancedServerDriversModelNode) adaptableObject);
		}
		// opc ua server driver config model navigation node
		else if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == OPCUAServerDriverConfigModelNode.class) {
			return new ServerDriverConfigModelPropertySource((OPCUAServerDriverConfigModelNode) adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
