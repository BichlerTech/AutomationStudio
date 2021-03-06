package opc.sdk.ua;

public interface IOPCOperation {
	/**
	 * The identifier for the session (null if multiple sessions are associated with
	 * the operation).
	 * 
	 * @return The session identifier.
	 */
	IOPCSession getSession();
}
