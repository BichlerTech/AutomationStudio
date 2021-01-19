package com.bichler.astudio.core.user.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.core.user.UserActivator;
import com.bichler.astudio.core.user.type.AbstractStudioUser;

public class UserRightsPropertyTester extends PropertyTester {

	public UserRightsPropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		AbstractStudioUser user = UserActivator.getDefault().getUser();
		
		return user.hasRights((Integer) expectedValue);
	}

}
