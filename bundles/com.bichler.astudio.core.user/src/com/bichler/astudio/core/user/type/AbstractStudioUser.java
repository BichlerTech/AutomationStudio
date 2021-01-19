package com.bichler.astudio.core.user.type;

public abstract class AbstractStudioUser {

	public static final int TYPE_USER_DEFAULT = 0;
	public static final int TYPE_USER_SUPER = 1;
	
	public AbstractStudioUser() {
		
	}
	
	public abstract int getUserType();

	public abstract boolean hasRights(int field);
}
