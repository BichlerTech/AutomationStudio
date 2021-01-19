package com.bichler.astudio.opcua.handlers.tester;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.OPCUADriverRegistry;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.utils.activator.InternationalActivator;

public class IsNavigationOPCUAServerDriverNode2UpgradePropertyTester extends PropertyTester {
	public IsNavigationOPCUAServerDriverNode2UpgradePropertyTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof OPCUAServerDriverModelNode) {
			OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) receiver;
			int nodeversion = 0;
			try {
				if (node.getDriverVersion().isEmpty())
					return true;
				nodeversion = Integer.parseInt(node.getDriverVersion().replaceAll("\\.", ""));
			} catch (NumberFormatException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				return false;
			}
			for (InternationalActivator act : OPCUADriverRegistry.drivers.values()) {
				if (act.getBundleName().startsWith("com.bichler.astudio.editor." + node.getDriverType())) {
					try {
						String version = act.getBundle().getVersion().toString();
						version = version.substring(0, version.lastIndexOf('.'));
						if (nodeversion < Integer.parseInt(version.replaceAll("\\.", "")))
							return true;
					} catch (NumberFormatException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
						return false;
					}
				}
			}
		}
		return false;
	}
}
