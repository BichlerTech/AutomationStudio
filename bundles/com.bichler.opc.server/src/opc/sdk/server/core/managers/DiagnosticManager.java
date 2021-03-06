package opc.sdk.server.core.managers;

import org.opcfoundation.ua.common.NamespaceTable;

/**
 * Manager to analyze opc ua diagnostics
 * 
 * @author Thomas Z�chbauer
 * 
 */
public class DiagnosticManager {
	public DiagnosticManager() {
	}

	public void addDiagnosticsNamespace(NamespaceTable nsTable) {
		// opc ua default diagnostics namespace
		String NS_DIAGNOSTICS = NamespaceTable.OPCUA_NAMESPACE + "Diagnostics";
		// add namespace to server
		int index = nsTable.getIndex(NS_DIAGNOSTICS);
		// insert namespace if not in table
		if (index < 0) {
			nsTable.add(NS_DIAGNOSTICS);
		}
	}
}
