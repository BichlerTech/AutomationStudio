package com.bichler.opc.client.structures;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Structure;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import opc.client.service.ClientSession;

public class E83ProductionDatasetReadOptionsType implements Structure, Cloneable

{

	public static ExpandedNodeId ID = null;

	public static ExpandedNodeId BINARY = null;

	public static ExpandedNodeId XML = null;

	private int storage = 0;

	private String name = "";

	/**
	 * 
	 * initialize ID, BINARY- and XML Encoding nodeid
	 * 
	 * 
	 * 
	 * @param opcServer
	 * 
	 */

	public static void init(ClientSession session)

	{

		Logger.getLogger(E83ProductionDatasetReadOptionsType.class.getName()).log(Level.INFO,

				"Initialize datatype binary structure.");

		if (ID == null)

		{

			// translate browsepath to nodeid to get id

			NodeId nid = new NodeId(3, 3007);
			/*
			 * E77Utils.translatBrowsePath2NodeId(opcServer, Identifiers.Structure,
			 * E77Utils.E83_BROWSE_INDEX,
			 * 
			 * "ProductionDatasetReadOptionsType", false);
			 */

			ID = session.getNamespaceUris().toExpandedNodeId(nid);

		}

		NodeId bnid = new NodeId(3, 5012);

		BINARY = session.getNamespaceUris().toExpandedNodeId(bnid);

		NodeId xmlnid = new NodeId(3, 5013);

		XML = session.getNamespaceUris().toExpandedNodeId(xmlnid);
	}

	public E83ProductionDatasetReadOptionsType()

	{

	}

	/**
	 * 
	 * Deep clone
	 *
	 * 
	 * 
	 * @return cloned FileReadOptions
	 * 
	 */

	@Override

	public E83ProductionDatasetReadOptionsType clone()

	{

		E83ProductionDatasetReadOptionsType result = new E83ProductionDatasetReadOptionsType();

		result.storage = storage;

		result.name = name;

		return result;

	}

	@Override

	public boolean equals(Object obj)

	{

		if (this == obj)

			return true;

		if (obj == null)

			return false;

		if (getClass() != obj.getClass())

			return false;

		E83ProductionDatasetReadOptionsType other = (E83ProductionDatasetReadOptionsType) obj;

		if (name == null)

		{

			if (other.name != null)

				return false;

		}

		else if (!name.equals(other.name))

			return false;

		return storage == other.storage;

	}

	@Override

	public int hashCode()

	{

		final int prime = 31;

		int result = 1;

		result = prime * result + ((name == null) ? 0 : name.hashCode());

		result = prime * result + storage;

		return result;

	}

	@Override

	public ExpandedNodeId getTypeId()

	{

		return ID;

	}

	@Override

	public ExpandedNodeId getXmlEncodeId()

	{

		return XML;

	}

	@Override

	public ExpandedNodeId getBinaryEncodeId()

	{

		return BINARY;

	}

	/**
	 * 
	 * @return the storage
	 * 
	 */

	public int getStorage()

	{

		return storage;

	}

	/**
	 * 
	 * @param storage
	 * 
	 *                the storage to set
	 * 
	 */

	public void setStorage(int storage)

	{

		this.storage = storage;

	}

	/**
	 * 
	 * @return the name
	 * 
	 */

	public String getName()

	{

		return name;

	}

	/**
	 * 
	 * @param name
	 * 
	 *             the name to set
	 * 
	 */

	public void setName(String name)

	{

		this.name = name;

	}

	@Override

	public ExpandedNodeId getJsonEncodeId()

	{

		// TODO Auto-generated method stub

		return null;

	}

}
