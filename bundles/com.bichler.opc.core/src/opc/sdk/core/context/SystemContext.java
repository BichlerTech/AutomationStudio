package opc.sdk.core.context;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.classes.ua.NodeStateFactory;
import opc.sdk.core.enums.DiagnosticsMasks;
import opc.sdk.core.session.UserIdentity;
import opc.sdk.core.types.TypeTable;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.common.NamespaceTable;

/**
 * A class that defines the current system.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class SystemContext implements ISystemContext, IOperationContext, Cloneable {
	private Object systemHandle = null;
	private NodeId sessionId = null;
	private List<String> preferredLocales = null;
	private UserIdentity userIdentity = null;
	private NamespaceTable namespaceUris = null;
	private StringTable serverUris = null;
	private TypeTable typeTable = null;
	private INodeIdFactory nodeIdFactory = null;
	private IOperationContext operationContext = null;
	private NodeStateFactory nodeStateFactory = null;

	/**
	 * Initializes an empty System Context
	 */
	public SystemContext() {
		this.nodeStateFactory = new NodeStateFactory();
	}

	/**
	 * Initializes a new System Context from an Operation
	 * 
	 * @param OperationContext Context for an Operation.
	 */
	public SystemContext(IOperationContext operationContext) {
		this();
		this.operationContext = operationContext;
	}

	/**
	 * Clones the SystemContext.
	 * 
	 * @return Clone from the SystemContext
	 */
	@Override
	public ISystemContext clone() {
		ISystemContext newObject = null;
		try {
			newObject = (ISystemContext) super.clone();
		} catch (CloneNotSupportedException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		return newObject;
	}

	@Override
	public String getAuditEntryId() {
		return null;
	}

	/**
	 * Getter from this ServerNamespaceUris.
	 * 
	 * @return NamespaceUris
	 */
	@Override
	public NamespaceTable getNamespaceUris() {
		return this.namespaceUris;
	}

	/**
	 * Getter from the PreferredLocales
	 * 
	 * @return PreferredLocales
	 */
	@Override
	public List<String> getPreferredLocales() {
		return this.preferredLocales;
	}

	/**
	 * Getter from the ServerUris.
	 * 
	 * @return ServerUris
	 */
	@Override
	public StringTable getServerUris() {
		return this.serverUris;
	}

	/**
	 * Getter from the SessionId
	 * 
	 * @return SessionId
	 */
	@Override
	public NodeId getSessionId() {
		return this.sessionId;
	}

	/**
	 * Getter from the SystemHandle.
	 * 
	 * @return SystemHandle
	 */
	@Override
	public Object getSystemHandle() {
		return this.systemHandle;
	}

	/**
	 * Getter from the User Identity.
	 * 
	 * @return UserIdentity
	 */
	@Override
	public UserIdentity getUserIdentity() {
		return this.userIdentity;
	}

	@Override
	public DiagnosticsMasks getDiagnosticsMask() {
		return null;
	}

	@Override
	public DateTime getOperationDeadline() {
		return null;
	}

	@Override
	public StatusCode getOperationStatus() {
		return null;
	}

	/**
	 * Getter from the NodeId Factory.
	 * 
	 * @return NodeIdFactory
	 */
	@Override
	public INodeIdFactory getNodeIdFactory() {
		return this.nodeIdFactory;
	}

	/**
	 * Setter for a NodeId Factory.
	 * 
	 * @param NodeIdFactory Create new unique NodeIds for OPC Nodes.
	 */
	@Override
	public void setNodeIdFactory(INodeIdFactory factory) {
		this.nodeIdFactory = factory;
	}

	/**
	 * Getter from the Operation Context.
	 * 
	 * @return OperationContext
	 */
	public IOperationContext getOperationContext() {
		return this.operationContext;
	}

	/**
	 * Setter from the Session SenderId.
	 * 
	 * @param SessionId
	 */
	public void setSessionId(NodeId sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Setter for an UserIdentity.
	 * 
	 * @param UserIdentity
	 */
	public void setUserIdentity(UserIdentity userIdentity) {
		this.userIdentity = userIdentity;
	}

	/**
	 * Setter for a NamespaceUri Table.
	 * 
	 * @param NamespaceUris
	 */
	public void setNamespaceUris(NamespaceTable namespaceUris) {
		this.namespaceUris = namespaceUris;
	}

	/**
	 * Setter for the ServerUri Table
	 * 
	 * @param ServerUris
	 */
	public void setServerUris(StringTable serverUris) {
		this.serverUris = serverUris;
	}

	/**
	 * Setter of an OperationContext.
	 * 
	 * @param OperationContext OperationContext used for this SystemContext
	 */
	public void setOperationContext(IOperationContext operationContext) {
		this.operationContext = operationContext;
	}

	/**
	 * Setter for the PreferredLocals.
	 * 
	 * @param PreferredLocales Locales for the Language
	 */
	public void setPreferredLocales(List<String> preferredLocales) {
		this.preferredLocales = preferredLocales;
	}

	/**
	 * Getter for the ServerType Table.
	 * 
	 * @return TypeTable
	 */
	@Override
	public TypeTable getTypeTable() {
		return this.typeTable;
	}

	public void setTypeTable(TypeTable typeTable) {
		this.typeTable = typeTable;
	}

	@Override
	public NodeStateFactory getNodeStateFactory() {
		return this.nodeStateFactory;
	}
}
