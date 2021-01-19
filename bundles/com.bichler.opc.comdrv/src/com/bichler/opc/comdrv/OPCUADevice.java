package com.bichler.opc.comdrv;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.UserIdentityToken;

import opc.client.application.UAClient;

public class OPCUADevice extends AComDevice {
	public String getUaServerName() {
		return uaServerName;
	}

	public void setUaServerName(String uaServerName) {
		this.uaServerName = uaServerName;
	}

	public String getUaServerUri() {
		return uaServerUri;
	}

	public void setUaServerUri(String uaServerUri) {
		this.uaServerUri = uaServerUri;
	}

	public String getUaSecurityPolicy() {
		return uaSecurityPolicy;
	}

	public void setUaSecurityPolicy(String uaSecurityPolicy) {
		this.uaSecurityPolicy = uaSecurityPolicy;
	}

	public String getUaSecurityMode() {
		return uaSecurityMode;
	}

	public void setUaSecurityMode(String uaSecurityMode) {
		this.uaSecurityMode = uaSecurityMode;
	}

	public int getUaSecurityType() {
		return uaSecurityType;
	}

	public void setUaSecurityType(int uaSecurityType) {
		this.uaSecurityType = uaSecurityType;
	}

	public String getUaUserName() {
		return uaUserName;
	}

	public void setUaUserName(String uaUserName) {
		this.uaUserName = uaUserName;
	}

	public String getUaPassword() {
		return uaPassword;
	}

	public void setUaPassword(String uaPassword) {
		this.uaPassword = uaPassword;
	}

	public String getUaCertificate() {
		return uaCertificate;
	}

	public void setUaCertificate(String uaCertificate) {
		this.uaCertificate = uaCertificate;
	}

	public UAClient getUaclient() {
		return uaclient;
	}

	public void setUaclient(UAClient uaclient) {
		this.uaclient = uaclient;
	}

	public UserIdentityToken getUaIdentityToken() {
		return uaIdentityToken;
	}

	public void setUaIdentityToken(UserIdentityToken uaIdentityToken) {
		this.uaIdentityToken = uaIdentityToken;
	}

	public EndpointDescription getUaEndpointDescription() {
		return uaEndpointDescription;
	}

	public void setUaEndpointDescription(EndpointDescription uaEndpointDescription) {
		this.uaEndpointDescription = uaEndpointDescription;
	}

	protected String uaServerName = "";
	protected String uaServerUri = "";
	protected String uaSecurityPolicy = "";
	protected String uaSecurityMode = "";
	protected int uaSecurityType = 0;
	protected String uaUserName = "";
	protected String uaPassword = "";
	protected String uaCertificate = "";
	protected UAClient uaclient = null;
	protected UserIdentityToken uaIdentityToken = null;
	protected EndpointDescription uaEndpointDescription = null;

	@Override
	public boolean addDP(NodeId nodeId) {
		// implementation not required
		return false;
	}

	@Override
	public ComDP addDP(NodeId nodeId, Object additional) {
		// implementation not required
		return null;
	}

	@Override
	public boolean addDPs(NodeId nodeId, Object additional) {
		// implementation not required
		return false;
	}

	@Override
	public void checkCommunication() {
		// implementation not required
	}

	@Override
	public void storeReadReq(ComDP dp) {
		// implementation not required
	}

	@Override
	public byte[] read() {
		// implementation not required
		return new byte[0];
	}

	@Override
	public int write(byte[] data) {
		// implementation not required
		return 0;
	}

	@Override
	public long connect() {
		// implementation not required
		return 0;
	}
}
