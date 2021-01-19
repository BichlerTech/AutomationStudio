package com.bichler.opc.comdrv;

public interface ILoginListener {
	/**
	 * Prepares a node for a write request. We set flags to the node if it should be
	 * written or not.
	 * 
	 * @param userName username to verify.
	 * @return true if the nodeid was found and could be set otherwise false.
	 */
	boolean loginUser(String userName, String passwd);
}
