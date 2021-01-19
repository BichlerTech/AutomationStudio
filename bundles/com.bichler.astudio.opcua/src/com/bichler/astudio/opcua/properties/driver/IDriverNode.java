package com.bichler.astudio.opcua.properties.driver;

import org.opcfoundation.ua.builtintypes.NodeId;

public interface IDriverNode {

	/**
	 * Displayname
	 */
	public String getDname();

	/**
	 * Description
	 */
	public String getDesc();

	/**
	 * Datatype
	 */
	public String getDtype();

	/**
	 * NodeId
	 */
	public NodeId getNId();

	/**
	 * Valid
	 */
	public boolean isValid();

	/**
	 * Browsepath
	 */
	public String getBrowsepath();
}
