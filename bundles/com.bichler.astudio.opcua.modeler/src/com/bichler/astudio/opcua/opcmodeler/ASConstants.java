package com.bichler.astudio.opcua.opcmodeler;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;

public class ASConstants {
	public static final String AS_NS = "http://bichler.tech/as";
	public static int AS_NS_Index = -1;
	public static final int AS_PLC_FOLDER_ID = 500;
	public static NodeId AS_PLC_Folder = null;
	public static final int AS_CPU_NAME_ID = 51;
	public static ExpandedNodeId AS_CPU_Name = null;
	public static final int AS_CPU_TYPE_ID = 52;
	public static ExpandedNodeId AS_CPU_Type = null;
	public static final int AS_CONNECTED_TYPE_ID = 2;
	public static ExpandedNodeId AS_Connected_Type = null;
	public static final int AS_RUNNING_TYPE_ID = 3;
	public static ExpandedNodeId AS_Running_Type = null;

	/**
	 * initialize all comet node constants
	 * 
	 * CometConstants.Comet_NS_Index must be initialized previously
	 */
	public static void init() {
		AS_PLC_Folder = new NodeId(AS_NS_Index, AS_PLC_FOLDER_ID);
		AS_Connected_Type = new ExpandedNodeId(new NodeId(AS_NS_Index, AS_CONNECTED_TYPE_ID));
		AS_Running_Type = new ExpandedNodeId(new NodeId(AS_NS_Index, AS_RUNNING_TYPE_ID));
		AS_CPU_Type = new ExpandedNodeId(new NodeId(AS_NS_Index, AS_CPU_TYPE_ID));
		AS_CPU_Name = new ExpandedNodeId(new NodeId(AS_NS_Index, AS_CPU_NAME_ID));
	}
}
