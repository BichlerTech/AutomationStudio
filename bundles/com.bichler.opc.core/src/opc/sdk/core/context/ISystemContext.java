package opc.sdk.core.context;

import java.util.List;

import opc.sdk.core.classes.ua.NodeStateFactory;
import opc.sdk.core.session.UserIdentity;
import opc.sdk.core.types.TypeTable;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

/**
 * An Interface definded the system.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public interface ISystemContext {
	/**
	 * An application defined handle for the system.
	 * 
	 * @return the SystemHandle
	 */
	Object getSystemHandle();

	/**
	 * The identifier for the session (null if multiple sessions are associated with
	 * the operation).
	 * 
	 * @return The session identifier.
	 */
	NodeId getSessionId();

	/**
	 * The identity of the user (null if not available).
	 * 
	 * @return The user identity.
	 */
	UserIdentity getUserIdentity();

	/**
	 * The locales to use if available.
	 * 
	 * @return The preferred locales.
	 */
	List<String> getPreferredLocales();

	/**
	 * The audit log entry associated with the operation (null if not available).
	 * 
	 * @return The audit entry identifier.
	 */
	String getAuditEntryId();

	/**
	 * The table of namespace uris to use when accessing the system.
	 * 
	 * @return The namespace URIs.
	 */
	NamespaceTable getNamespaceUris();

	/**
	 * The table of server uris to use when accessing the system.
	 * 
	 * @return The server URIs.
	 */
	StringTable getServerUris();

	/**
	 * A table containing the types that are to be used when accessing the system.
	 * 
	 * @return The type table.
	 */
	TypeTable getTypeTable();

	/**
	 * A factory that can be used to create node ids.
	 * 
	 * @return The node identifiers factory.
	 */
	INodeIdFactory getNodeIdFactory();

	void setNodeIdFactory(INodeIdFactory factory);

	/**
	 * A factory that can be used to create encodeable types.
	 * 
	 * @return The encodeable factory
	 */
	NodeStateFactory getNodeStateFactory();
}
