package com.bichler.astudio.opcua.opcmodeler.commands.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

public class IsValidationTester extends PropertyTester {
	public IsValidationTester() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof BrowserModelNode) {
			NodeClass nodeClass = ((BrowserModelNode) receiver).getNode().getNodeClass();
			switch (nodeClass) {
			case Object:
			case Variable:
				return true;
			}
		}
		return false;
	}
}
