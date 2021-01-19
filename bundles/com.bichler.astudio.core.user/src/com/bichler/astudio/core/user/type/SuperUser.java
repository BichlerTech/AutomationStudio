package com.bichler.astudio.core.user.type;

public class SuperUser extends AbstractStudioUser {

	public SuperUser() {
		super();
	}

	@Override
	public boolean hasRights(int field) {
		if(field > 1) {
			return false;
		}
		return true;
	}
	
	@Override
	public int getUserType() {
		return TYPE_USER_SUPER;
	}
}
