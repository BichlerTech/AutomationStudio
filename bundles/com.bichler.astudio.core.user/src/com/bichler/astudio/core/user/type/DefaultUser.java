package com.bichler.astudio.core.user.type;

public class DefaultUser extends AbstractStudioUser {

	public DefaultUser() {
		super();
	}

	@Override
	public boolean hasRights(int field) {
		if(field > 0) {
			return false;
		}
		return true;
	}

	@Override
	public int getUserType() {
		return TYPE_USER_DEFAULT;
	}
	
}
