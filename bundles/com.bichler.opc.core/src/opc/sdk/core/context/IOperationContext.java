package opc.sdk.core.context;

import java.util.List;

import opc.sdk.core.enums.DiagnosticsMasks;
import opc.sdk.core.session.UserIdentity;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;

/**
 * An interface to an object that describes a node local to the server.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public interface IOperationContext {
	/**
	 * The identifier for the session (null if multiple sessions are associated with
	 * the operation).
	 * 
	 * @return The session identifier.
	 */
	NodeId getSessionId();

	/**
	 * The identity of the user.
	 * 
	 * @return
	 */
	UserIdentity getUserIdentity();

	/**
	 * The locales to use if available.
	 * 
	 * @return The preferred locales.
	 */
	List<String> getPreferredLocales();

	/**
	 * The mask to use when collecting any diagnostic information.
	 * 
	 * @return The diagnostics mask.
	 */
	DiagnosticsMasks getDiagnosticsMask();

	/**
	 * When the operation times out.
	 * 
	 * @return The operation deadline.
	 */
	DateTime getOperationDeadline();

	/**
	 * The current status of the the operation (bad if the operation has been
	 * aborted).
	 * 
	 * @return The operation status.
	 */
	StatusCode getOperationStatus();

	/**
	 * The audit identifier associated with the operation.
	 * 
	 * @return The audit entry identifier.
	 */
	String getAuditEntryId();
}
