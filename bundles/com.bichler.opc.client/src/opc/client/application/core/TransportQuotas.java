package opc.client.application.core;

/**
 * A clients configuration for the transport channels.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class TransportQuotas {
	/** Operation Timeout */
	private String operationTimeout = null;
	/** Maximum String Length */
	private String maxStringLength = null;
	/** Maximum Byte String Length */
	private String maxByteStringLength = null;
	/** Maximum Array Length */
	private String maxArrayLength = null;
	/** Maximum Message Size */
	private String maxMessageSize = null;
	/** Maximum Buffer Size */
	private String maxBufferSize = null;
	/** Transport Channel Lifetime */
	private String channelLifetime = null;
	/** Security Token Lifetime */
	private String securityTokenLifetime = null;

	/*
	 * default empty constructor to create transportquotas from scratch
	 */
	public TransportQuotas() {
		// do nothing
	}

	public TransportQuotas(String operationTimeout, String maxStringLength, String maxByteStringLength,
			String maxArrayLength, String maxMessageSize, String maxBufferSize, String channelLifetime,
			String securityTokenLifetime) {
		this.operationTimeout = operationTimeout;
		this.maxStringLength = maxStringLength;
		this.maxByteStringLength = maxByteStringLength;
		this.maxArrayLength = maxArrayLength;
		this.maxMessageSize = maxMessageSize;
		this.maxBufferSize = maxBufferSize;
		this.channelLifetime = channelLifetime;
		this.securityTokenLifetime = securityTokenLifetime;
	}

	/**
	 * Returns the Channel Lifetime.
	 * 
	 * @return ChannelLifetime
	 */
	public Integer getChannelLifetime() {
		return new Integer(this.channelLifetime);
	}

	/**
	 * Returns the Max Array Length.
	 * 
	 * @return MaxArrayLength
	 */
	public Integer getMaxArrayLength() {
		return new Integer(this.maxArrayLength);
	}

	/**
	 * Returns the Max Buffer Size
	 * 
	 * @return MaxBufferSize
	 */
	public Integer getMaxBufferSize() {
		return new Integer(this.maxBufferSize);
	}

	/**
	 * Returns the Max Byte String Length.
	 * 
	 * @return MaxByteStringLength
	 */
	public Integer getMaxByteStringLength() {
		return new Integer(this.maxByteStringLength);
	}

	/**
	 * Returns the Max Message Size.
	 * 
	 * @return MaxMessageSize
	 */
	public Integer getMaxMessageSize() {
		return new Integer(this.maxMessageSize);
	}

	/**
	 * Returns the Max String Length.
	 * 
	 * @return MaxStringLength
	 */
	public Integer getMaxStringLength() {
		return new Integer(this.maxStringLength);
	}

	/**
	 * Returns the Operation Timeout.
	 * 
	 * @return OperationTimeout
	 */
	public Integer getOperationTimeout() {
		return new Integer(this.operationTimeout);
	}

	/**
	 * Returns the Security Token Lifetime
	 * 
	 * @return SecurityTokenLifetime
	 */
	public Integer getSecurityTokenLifetime() {
		return new Integer(this.securityTokenLifetime);
	}

	/**
	 * Sets the Operation Timeout.
	 * 
	 * @param OperationTimeout
	 */
	public void setOperationTimeout(int operationTimeout) {
		this.operationTimeout = Integer.toString(operationTimeout);
	}

	/**
	 * Sets the Max String Length.
	 * 
	 * @param MaxStringLength
	 */
	public void setMaxStringLength(int maxStringLength) {
		this.maxStringLength = Integer.toString(maxStringLength);
	}

	/**
	 * Sets the Max Byte String Length
	 * 
	 * @param MaxByteStringLength
	 */
	public void setMaxByteStringLength(int maxByteStringLength) {
		this.maxByteStringLength = Integer.toString(maxByteStringLength);
	}

	/**
	 * Sets the Max Array Length.
	 * 
	 * @param MaxArrayLength
	 */
	public void setMaxArrayLength(int maxArrayLength) {
		this.maxArrayLength = Integer.toString(maxArrayLength);
	}

	/**
	 * Sets the Max Message Size.
	 * 
	 * @param MaxMessageSize
	 */
	public void setMaxMessageSize(int maxMessageSize) {
		this.maxMessageSize = Integer.toString(maxMessageSize);
	}

	/**
	 * Sets the Max Buffer Size.
	 * 
	 * @param MaxBufferSize
	 */
	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = Integer.toString(maxBufferSize);
	}

	/**
	 * Sets the Channel Lifetime.
	 * 
	 * @param ChannelLifetime
	 */
	public void setChannelLifetime(int channelLifetime) {
		this.channelLifetime = Integer.toString(channelLifetime);
	}

	/**
	 * Sets the Security Token Lifetime.
	 * 
	 * @param SecurityTokenLifetime
	 */
	public void setSecurityTokenLifetime(int securityTokenLifetime) {
		this.securityTokenLifetime = Integer.toString(securityTokenLifetime);
	}
}
