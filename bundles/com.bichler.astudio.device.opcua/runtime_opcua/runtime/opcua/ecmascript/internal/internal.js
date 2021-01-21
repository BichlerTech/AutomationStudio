load("nashorn:mozilla_compat.js");
importPackage(org.opcfoundation.ua.builtintypes);
importPackage(java.lang);
importPackage(com.hbsoft.comdrv);
importPackage(org.opcfoundation.ua.core);
importPackage(java.util);
importPackage(Packages.opc.sdk.core.node);
importPackage(org.opcfoundation.ua.encoding);

/**
 * don't change that functions, these are part of the Automation Studio
 */
var driverIndex = -1;
/**
 * Appends one namespace uri to the servers namespace array.
 *
 * @author	Hannes Bichler
 * @param 	(String)
 *            	nsUri namespace uri which should be inserted.
 * @return 	(java.lang.Integer) The index of that new namespace or -1 if an error
 *         		occurs.
 */
function appendNS(nsUri) {
	/** first we add namespaceuri */
	// var response = ComDRVManager.getDRVManager().readValue(
	// Identifiers.Server_NamespaceArray);
	var response = ComDRVManager.getDRVManager().readFromDriver(
			Identifiers.Server_NamespaceArray, Attributes.Value, null, null,
			-1, 0.0, TimestampsToReturn.Both);

	/* check the namespace object */
	if (response == null) {
		return -1;
	}

	/**
	 * array with all already existing namespace uris
	 */
	var namespaces = response.getValue().getValue();

	/**
	 * create a new array where we insert all namespaces inclusive the new one.
	 */
	var StringArray = Java.type("java.lang.String[]");

	var ns = new StringArray(namespaces.length + 1);

	/**
	 * fill the new namespace array with the existing values
	 */
	for (var i = 0; i < namespaces.length; i++) {
		ns[i] = namespaces[i];
	}

	/**
	 * add the new namespace uri
	 */
	ns[ns.length - 1] = nsUri;

	/**
	 * create the new namespace index.
	 */
	var nsIndex = new java.lang.Integer(ns.length - 1);

	/**
	 * create a value to write back to opc ua address space.
	 */
	var variable = new Variant(ns);
	var newNamespaces = new DataValue(variable);

	/**
	 * write the new array back to address space
	 */
	ComDRVManager.getDRVManager().writeFromDriver(
			Identifiers.Server_NamespaceArray, Attributes.Value, null,
			newNamespaces, 0);

	driverIndex = nsIndex;

	return nsIndex;
}

/**
 * Creates a new Object as defined type and suspends it to the parent node.
 * 
 * @param (NodeId)
 *            parentNodeid Nodeid of parent.
 * @param (NodeId)
 *            reqNodeId Nodeid of requested new node.
 * @param (int)
 *            browseName index to browse this object node. e.g.
 *            "demo.myfolders.folderinterval"
 * @param (String)
 *            browseName Name to browse this object node. e.g.
 *            "demo.myfolders.folderinterval"
 * @param (String)
 *            description Description of this object.
 * @param (String)
 *            displayName Name which will be display in our BTech OPC UA
 *            Browser.
 * @param (NodeId)
 *            referenceType Type of reference from parent to this new object node.
 * @param (NodeId)
 *            referenceType Type of new object node to create.
 * @param (byte)
 *            eventNotifier Notifier type which should raise an event for object
 *            folder.
 * @param (int)
 *            writeMask Mask to write from system to this node.
 * @param (int)
 *            userWriteMask Mask to write as a user connection to this node.
 */
function addObjectNode(parNodeId, reqNodeId,
		browseIndex, browseName, description, displayName, referenceType, typeDefinition,
		eventNotifier, writeMask, userWriteMask) {

	var AddNodesItemArray = Java.type("org.opcfoundation.ua.core.AddNodesItem[]");
	var addNodesItems = new AddNodesItemArray(1);
	/**
	 * new nodes item to insert into address space
	 */
	var nodesitem = new AddNodesItem();
	addNodesItems[0] = nodesitem;

	/**
	 * set the brownse name
	 */
	var qName = new QualifiedName(new java.lang.Integer(browseIndex),
			new java.lang.String(browseName));
	nodesitem.setBrowseName(qName);

	/**
	 * class is object
	 */
	nodesitem.setNodeClass(NodeClass.Object);
	/**
	 * set node ids
	 */
	nodesitem.setParentNodeId(new ExpandedNodeId(parNodeId));
	nodesitem.setRequestedNewNodeId(new ExpandedNodeId(reqNodeId));

	/**
	 * set types for new node
	 */
	nodesitem.setTypeDefinition(new ExpandedNodeId(typeDefinition));
	nodesitem.setReferenceTypeId(referenceType);

	/**
	 * set object attributes
	 */
	try {
		var attributes = new ObjectAttributes();
		attributes.setDescription(new LocalizedText(description, Locale
				.getDefault()));
		attributes.setDisplayName(new LocalizedText(displayName, Locale
				.getDefault()));

		// attributes.setEventNotifier(UnsignedByte.ZERO);
		attributes.setEventNotifier(UnsignedByte.getFromBits(eventNotifier));

		// attributes.setWriteMask(UnsignedInteger.ZERO);
		attributes.setWriteMask(UnsignedInteger.getFromBits(writeMask));

		// attributes.setUserWriteMask(UnsignedInteger.ZERO);
		attributes.setUserWriteMask(UnsignedInteger.getFromBits(userWriteMask));

		var attr = ExtensionObject.binaryEncode(attributes, EncoderContext
				.getDefaultInstance());
		nodesitem.setNodeAttributes(attr);

		/**
		 * now load the new node into the address space
		 */
		ComDRVManager.getDRVManager().getResourceManager()
				.getDriverConnection().loadNodes(addNodesItems);

	} catch (e) {
		/**
		 * print out exception
		 */
		System.out.println(e);
	}
}

/**
 * Creates a new Object as Folder and suspends it to the parent node.
 * 
 * @param (int)
 *            parentNsIndex Namespace index of parent node.
 * @param (String)
 *            parentNodeid Nodeid of parent in form ("i=1" or "s=demo")
 * @param (int)
 *            newNsIndex Namespace index of requested new node.
 * @param (String)
 *            newNsIndex Nodeid of requested new node. e.g. "i=1" or "s=demo"
 * @param (String)
 *            browseName Name to browse this folder node. e.g.
 *            "demo.myfolders.folderinterval"
 * @param (String)
 *            description Description of this folder.
 * @param (String)
 *            displayName Name which will be display in our Comet OPC UA
 *            Browser.
 * @param (byte)
 *            eventNotifier Notifier type which should raise an event for the
 *            folder.
 * @param (int)
 *            writeMask Mask to write from system to this node.
 * @param (int)
 *            userWriteMask Mask to write as a user connection to this node.
 */
function addFolderNode(parentNsIndex, parentNodeid, newNsIndex, newNodeId,
		browseName, description, displayName, eventNotifier, writeMask,
		userWriteMask) {

	var AddNodesItemArray = Java.type("org.opcfoundation.ua.core.AddNodesItem[]");
	var addNodesItems = new AddNodesItemArray(1);
	/**
	 * new nodes item to insert into address space
	 */
	var nodesitem = new AddNodesItem();
	addNodesItems[0] = nodesitem;

	/**
	 * create parent and new requested NodeIds
	 */
	var parNodeId = NodeId.parseNodeId("ns=" + parentNsIndex + ";" + parentNodeid);
	var reqNodeId = NodeId.parseNodeId("ns=" + newNsIndex + ";" + newNodeId);

	/**
	 * set the brownse name
	 */
	var qName = new QualifiedName(new java.lang.Integer(newNsIndex),
			new java.lang.String(browseName));
	nodesitem.setBrowseName(qName);

	/**
	 * class of folder is object
	 */
	nodesitem.setNodeClass(NodeClass.Object);
	/**
	 * set node ids
	 */
	nodesitem.setParentNodeId(new ExpandedNodeId(parNodeId));
	nodesitem.setRequestedNewNodeId(new ExpandedNodeId(reqNodeId));

	/**
	 * set types for new node
	 */
	nodesitem.setTypeDefinition(new ExpandedNodeId(Identifiers.FolderType));
	nodesitem.setReferenceTypeId(Identifiers.Organizes);

	/**
	 * set object attributes
	 */
	try {
		var attributes = new ObjectAttributes();
		attributes.setDescription(new LocalizedText(description, Locale
				.getDefault()));
		attributes.setDisplayName(new LocalizedText(displayName, Locale
				.getDefault()));

		// attributes.setEventNotifier(UnsignedByte.ZERO);
		attributes.setEventNotifier(UnsignedByte.getFromBits(eventNotifier));

		// attributes.setWriteMask(UnsignedInteger.ZERO);
		attributes.setWriteMask(UnsignedInteger.getFromBits(writeMask));

		// attributes.setUserWriteMask(UnsignedInteger.ZERO);
		attributes.setUserWriteMask(UnsignedInteger.getFromBits(userWriteMask));

		var attr = ExtensionObject.binaryEncode(attributes, EncoderContext
				.getDefaultInstance());
		nodesitem.setNodeAttributes(attr);

		/**
		 * now load the new node into the address space
		 */
		ComDRVManager.getDRVManager().getResourceManager()
				.getDriverConnection().loadNodes(addNodesItems);

	} catch (e) {
		/**
		 * print out exception
		 */
		System.out.println(e);
	}
}

/**
 * Creates a new Object as Folder and suspends it to the parent node.
 * 
 * @param (int)
 *            parentNsIndex Namespace index of parent node.
 * @param (String)
 *            parentNodeid Nodeid of parent in form ("i=1" or "s=demo")
 * @param (int)
 *            newNsIndex Namespace index of requested new node.
 * @param (String)
 *            newNsIndex Nodeid of requested new node. e.g. "i=1" or "s=demo"
 * @param (String)
 *            browseName Name to browse this folder node. e.g.
 *            "demo.myfolders.folderinterval"
 * @param (String)
 *            description Description of this folder.
 * @param (String)
 *            displayName Name which will be display in our Comet OPC UA
 *            Browser.
 * @param (byte)
 *            eventNotifier Notifier type which should raise an event for the
 *            folder.
 * @param (int)
 *            writeMask Mask to write from system to this node.
 * @param (int)
 *            userWriteMask Mask to write as a user connection to this node.
 */
function addVariableNode(parentNsIndex, parentNodeid, newNsIndex, newNodeId,
		browseName, description, displayName, writeMask, userWriteMask,
		accesslevel, arraydimension, datatypeNsIndex, datatypeNodeid,
		historizing, minSampleInterval, useraccesslevel, value, valueRank) {

	var AddNodesItemArray = Java.type("org.opcfoundation.ua.core.AddNodesItem[]");
	var addNodesItems = new AddNodesItemArray(1);
	/**
	 * new nodes item to insert into address space
	 */
	var nodesitem = new AddNodesItem();
	addNodesItems[0] = nodesitem;

	/**
	 * create parent and new requested NodeIds
	 */
	var parNodeId = NodeId.parseNodeId("ns=" + parentNsIndex + ";" + parentNodeid);
	var reqNodeId = NodeId.parseNodeId("ns=" + newNsIndex + ";" + newNodeId);

	/**
	 * set the brownse name
	 */
	var qName = new QualifiedName(new java.lang.Integer(newNsIndex),
			new java.lang.String(browseName));
	nodesitem.setBrowseName(qName);

	/**
	 * class of folder is object
	 */
	nodesitem.setNodeClass(NodeClass.Variable);
	/**
	 * set node ids
	 */
	nodesitem.setParentNodeId(new ExpandedNodeId(parNodeId));
	nodesitem.setRequestedNewNodeId(new ExpandedNodeId(reqNodeId));

	/**
	 * set types for new node
	 */
	nodesitem.setTypeDefinition(new ExpandedNodeId(
			Identifiers.BaseDataVariableType));
	nodesitem.setReferenceTypeId(Identifiers.Organizes);

	/**
	 * set variable attributes
	 */
	var datatypeNodeId = NodeId.parseNodeId("ns=" + datatypeNsIndex + ";"
			+ datatypeNodeid);
	try {
		var attributes = new VariableAttributes();
		attributes.setAccessLevel(AccessLevel.getMask(accesslevel));
		attributes.setArrayDimensions(arraydimension);
		attributes.setDataType(datatypeNodeId);
		attributes.setDescription(new LocalizedText(description, Locale
				.getDefault()));
		attributes.setDisplayName(new LocalizedText(displayName, Locale
				.getDefault()));
		attributes.setHistorizing(historizing);
		attributes.setMinimumSamplingInterval(minSampleInterval);
		attributes.setUserAccessLevel(AccessLevel.getMask(useraccesslevel));
		attributes.setUserWriteMask(UnsignedInteger.getFromBits(userWriteMask));
		attributes.setValue(new Variant(value));
		attributes.setValueRank(valueRank);
		attributes.setWriteMask(UnsignedInteger.getFromBits(writeMask));

		var attr = ExtensionObject.binaryEncode(attributes, EncoderContext
				.getDefaultInstance());
		nodesitem.setNodeAttributes(attr);

		/**
		 * now load the new node into the address space
		 */
		ComDRVManager.getDRVManager().getResourceManager()
				.getDriverConnection().loadNodes(addNodesItems);

	} catch (e) {
		/**
		 * print out exception
		 */
		System.out.println(e);
	}
}

function addReference(parentNsIndex, parentNodeid, newNsIndex, newNodeId,
		referenceTypeId) {
}

/**
 * 	Translates a full qulified browsepath into a nodeid if the node could be found.
 *
 * @param (NodeId) 
 *            startNode NodeId of node to start translate from.
 * 						If NodeId couldn't be found, so no translate is possible
 * @param (int) 
 *            browseIndex Default index to browse from startnode to leaf, if all nodes habe the same namespaceindex.
 *				In this case, the namespaceindex in the browsepath is not required.
 *				If the browsepath have different namespaceindex, so this browseindex is not used.
 *
 * @param (String) 
 *            path	Browsepath from startnode to leaf. It can contain a full qualified browsename or only the 
 *					textcomponents and use the browseIndex for each browsename.
*/
function translateBrowsePathToNodeid(startNode, browseIndex, path) {
	// get the internal server instance from driver manager
	var internalServer = ComDRVManager.getDRVManager().getServer();
	// call internal translate function from drivermanager
	return ComDRVManager.translatBrowsePath2NodeId(internalServer, startNode, browseIndex, path);
}

/**
 * 	Get the next nodeid from server.
 *
 * @param (int) 
 *            nsIndex namespace index to get the next nodeid.
 * 						If namespace index couldn't be found, so no nodeid is created.
 *
*/
function getNextNodeId(nsIndex) {
	// get the internal server instance from driver manager
	var internalServer = ComDRVManager.getDRVManager().getServer();
	// get the next nodeid from internal server
	return internalServer.getAddressSpaceManager().getNodeFactory().getNextNodeId(nsIndex, null, IdType.Numeric, new ArrayList(), NodeIdMode.FILL);
}