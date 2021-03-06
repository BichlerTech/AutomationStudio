package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.core.context.ISystemContext;
import opc.sdk.ua.constants.BrowseNames;

public class SessionDiagnosticsObjectType extends BaseObjectType {
	protected SessionDiagnosticsVariableType sessionDiagnostics = null;
	protected SessionSecurityDiagnosticsType sessionSecurityDiagnostics = null;
	protected SubscriptionDiagnosticsArrayType subscriptionDiagnosticsArray = null;

	/**
	 * Initializes the type with its default attribute values.
	 * 
	 * @param parent
	 */
	public SessionDiagnosticsObjectType(BaseNode parent) {
		super(parent);
	}

	/**
	 * Returns the id of the default type definition node for the instance.
	 */
	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.SessionDiagnosticsObjectType;
	}

	@Override
	public BaseInstance createChild(QualifiedName browseName) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		return findChild(browseName, true, null);
	}

	@Override
	public void create(ISystemContext context, NodeId nodeId, QualifiedName browseName, LocalizedText displayname,
			boolean assignNodeIds) {
		setNodeId(Identifiers.SessionDiagnosticsObjectType);
		super.create(context, nodeId, browseName, displayname, assignNodeIds);
	}

	private SessionDiagnosticsVariableType creatSessionDiagnostics(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.sessionDiagnostics == null) {
			if (replacement == null) {
				this.sessionDiagnostics = new SessionDiagnosticsVariableType(this);
			} else {
				this.sessionDiagnostics = (SessionDiagnosticsVariableType) replacement;
			}
		}
		return this.sessionDiagnostics;
	}

	private SessionSecurityDiagnosticsType creatSessionSecurityDiagnostics(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.sessionSecurityDiagnostics == null) {
			if (replacement == null) {
				this.sessionSecurityDiagnostics = new SessionSecurityDiagnosticsType(this);
			} else {
				this.sessionSecurityDiagnostics = (SessionSecurityDiagnosticsType) replacement;
			}
		}
		return this.sessionSecurityDiagnostics;
	}

	private SubscriptionDiagnosticsArrayType creatSubscriptionDiagnosticsArray(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace) {
			if (replacement == null) {
				this.subscriptionDiagnosticsArray = new SubscriptionDiagnosticsArrayType(this);
			} else {
				this.subscriptionDiagnosticsArray = (SubscriptionDiagnosticsArrayType) replacement;
			}
		}
		return this.subscriptionDiagnosticsArray;
	}

	/**
	 * Finds the child with the specified browsename.
	 */
	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.SESSIONDIAGNOSTICS:
			instance = creatSessionDiagnostics(createOrReplace, replacement);
			break;
		case BrowseNames.SESSIONSECURITYDIAGNOSTICS:
			instance = creatSessionSecurityDiagnostics(createOrReplace, replacement);
			break;
		case BrowseNames.SUBSCRIPTIONDIAGNOSTICSARRAY:
			instance = creatSubscriptionDiagnosticsArray(createOrReplace, replacement);
			break;
		default:
			break;
		}
		if (instance != null) {
			return instance;
		}
		return super.findChild(browseName, createOrReplace, replacement);
	}
}
