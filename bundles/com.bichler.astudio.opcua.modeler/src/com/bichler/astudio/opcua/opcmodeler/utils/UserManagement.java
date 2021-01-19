package com.bichler.astudio.opcua.opcmodeler.utils;

public class UserManagement {
	private static UserManagement management = null;
	private String actUsername = "";

	private UserManagement() {
	}

	public static UserManagement getInstance() {
		if (management == null) {
			management = new UserManagement();
		}
		return management;
	}

	public String getActUsername() {
		return this.actUsername;
	}

	public void setActUsername(String username) {
		this.actUsername = username;
	}
}
