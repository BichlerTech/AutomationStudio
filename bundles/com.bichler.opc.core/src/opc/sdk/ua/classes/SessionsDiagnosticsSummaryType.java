package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

public class SessionsDiagnosticsSummaryType extends BaseObjectType {
	protected SessionDiagnosticsArrayType sessionDiagnosticsArray = null;
	protected SessionSecurityDiagnosticsArrayType sessionSecurityDiagnosticsArray = null;

	public SessionsDiagnosticsSummaryType(BaseNode parent) {
		super(parent);
	}

	/**
	 * Returns the id of the default type definition node for the instance.
	 */
	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.SessionsDiagnosticsSummaryType;
	}
}
