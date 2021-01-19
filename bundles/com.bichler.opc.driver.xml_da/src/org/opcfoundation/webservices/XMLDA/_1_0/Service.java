/**
 * Service.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public interface Service extends java.rmi.Remote {
	public org.opcfoundation.webservices.XMLDA._1_0.GetStatusResponse getStatus(
			org.opcfoundation.webservices.XMLDA._1_0.GetStatus parameters) throws java.rmi.RemoteException;

	// Siemens ThH: false generated code
	// public void read(org.opcfoundation.webservices.XMLDA._1_0.RequestOptions
	// options, org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList
	// itemList, org.opcfoundation.webservices.XMLDA._1_0.holders.ReplyBaseHolder
	// readResult,
	// org.opcfoundation.webservices.XMLDA._1_0.holders.ReplyItemListHolder
	// RItemList,
	// org.opcfoundation.webservices.XMLDA._1_0.holders.OPCErrorArrayHolder
	// errors) throws java.rmi.RemoteException;
	public org.opcfoundation.webservices.XMLDA._1_0.ReadResponse read(
			org.opcfoundation.webservices.XMLDA._1_0.RequestOptions options,
			org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList itemList/*
																					 * , org . opcfoundation .
																					 * webservices . XMLDA . _1_0 .
																					 * holders . ReplyBaseHolder
																					 * readResult, org . opcfoundation .
																					 * webservices . XMLDA . _1_0 .
																					 * holders . ReplyItemListHolder
																					 * RItemList, org . opcfoundation .
																					 * webservices . XMLDA . _1_0 .
																					 * holders . OPCErrorArrayHolder
																					 * errors
																					 */)
			throws java.rmi.RemoteException;

	public org.opcfoundation.webservices.XMLDA._1_0.WriteResponse write(
			org.opcfoundation.webservices.XMLDA._1_0.Write parameters) throws java.rmi.RemoteException;

	public org.opcfoundation.webservices.XMLDA._1_0.SubscribeResponse subscribe(
			org.opcfoundation.webservices.XMLDA._1_0.Subscribe parameters) throws java.rmi.RemoteException;

	public org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefreshResponse subscriptionPolledRefresh(
			org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefresh parameters)
			throws java.rmi.RemoteException;

	public org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancelResponse subscriptionCancel(
			org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancel parameters) throws java.rmi.RemoteException;

	public org.opcfoundation.webservices.XMLDA._1_0.BrowseResponse browse(
			org.opcfoundation.webservices.XMLDA._1_0.Browse parameters) throws java.rmi.RemoteException;

	public org.opcfoundation.webservices.XMLDA._1_0.GetPropertiesResponse getProperties(
			org.opcfoundation.webservices.XMLDA._1_0.GetProperties parameters) throws java.rmi.RemoteException;
}
