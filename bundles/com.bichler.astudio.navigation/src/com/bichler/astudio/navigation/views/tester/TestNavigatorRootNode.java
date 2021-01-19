package com.bichler.astudio.navigation.views.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.navigation.nodes.RootStudioModelNode;


public class TestNavigatorRootNode extends PropertyTester {

	public TestNavigatorRootNode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		if (receiver != null && receiver instanceof RootStudioModelNode) {
			return true;
		}

		return false;
	}

}
