package opc.sdk.core.application;

import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.transport.security.SecurityPolicy;

/**
 * Security Policy for the Server
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class ServerSecurityPolicy {
	private String securityPolicyUri = "";
	private SecurityMode securityMode = null;
	private String securityLevel = "";

	public ServerSecurityPolicy(SecurityMode mode, String level) {
		this.securityLevel = level;
		this.securityMode = mode;
	}

	public ServerSecurityPolicy() {
		initialize();
	}

	public SecurityMode getSecurityMode() {
		return securityMode;
	}

	public String getSecurityLevel() {
		return securityLevel;
	}

	public String getSecurityPolicyUri() {
		return this.securityPolicyUri;
	}

	private void initialize() {
		this.securityMode = SecurityMode.NONE;
		this.securityLevel = "0";
		this.securityPolicyUri = SecurityPolicy.NONE.getPolicyUri();
	}
}
