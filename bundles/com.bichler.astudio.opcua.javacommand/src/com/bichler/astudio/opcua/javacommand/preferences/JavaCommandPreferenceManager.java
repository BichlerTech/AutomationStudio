package com.bichler.astudio.opcua.javacommand.preferences;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class JavaCommandPreferenceManager {

	private static String COMMAND_COMBOX = "Combox";
	private static String COMMAND_DEFAULT = "Default";

	public static void inititialize() {
		String[] commands = getAllCommandNames();
		if (commands != null) {
			boolean hasDefaultCombox = false;
			boolean hasDefaultWindows = false;
			for (String cmd : commands) {
				if (COMMAND_COMBOX.equals(cmd)) {
					hasDefaultCombox = true;
				}

				else if (COMMAND_DEFAULT.equals(cmd)) {
					hasDefaultWindows = true;
				}
			}

			if (!hasDefaultCombox) {
				StringBuilder builder = new StringBuilder();
				builder.append("#!/bin/sh\n");
				builder.append("cd\n");
				builder.append("/hbin/javaOPC -Xms32m -Xmx256m -XX:MaxPermSize=192m -jar ../../runtime/OPC_Server.jar\n");
				addCommand(COMMAND_COMBOX, "Default ComBox start script", builder.toString());
			}
			if (!hasDefaultWindows) {
				StringBuilder builder = new StringBuilder();
				builder.append("#!/bin/sh\n");
				builder.append("cd\n");
				builder.append("java -Xms32m -Xmx256m -XX:MaxPermSize=192m -jar ../../runtime/OPC_Server.jar\n");
				addCommand(COMMAND_DEFAULT, "Default windows start script", builder.toString());
			}
		}

	}

	public Preferences getRoot() {
		Preferences devlist = getRootPreference();
		return devlist;
	}

	public static Preferences addCommand(String name, String description,
			String command) {
		Preferences cmdlist = getRootPreference();

		Preferences node = cmdlist.node(name);

		node.put(JavaCommandPreferenceConstants.PREFERENCE_COMMAND_DESCRIPTION,
				description);
		node.put(JavaCommandPreferenceConstants.PREFERENCE_COMMAND_COMMAND,
				command);
		try {
			cmdlist.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

		return node;
	}

	public static Preferences removeCommand(String name) {
		Preferences cmdlist = getRootPreference();

		Preferences node = cmdlist.node(name);
		if (node != null) {
			try {
				node.removeNode();
				cmdlist.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
		return node;
	}

	public static Preferences getCommand(String name) {
		Preferences cmdlist = getRootPreference();
		return cmdlist.node(name);
	}

	public static String[] getAllCommandNames() {
		Preferences cmdlist = getRootPreference();
		try {
			String[] commands = cmdlist.childrenNames();
			return commands;
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		return new String[0];
	}

	private static Preferences getRootPreference() {
		IEclipsePreferences root = InstanceScope.INSTANCE
				.getNode(JavaCommandPreferenceConstants.PREFERENCE_COMMAND_LIST);
		return root;
	}
}
