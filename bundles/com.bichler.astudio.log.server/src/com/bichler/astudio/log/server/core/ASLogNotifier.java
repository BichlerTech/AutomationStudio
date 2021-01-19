package com.bichler.astudio.log.server.core;

import java.util.HashSet;
import java.util.Set;

import com.bichler.astudio.log.server.listener.ASLogEventListener;

public class ASLogNotifier {

	public final static Set<ASLogEventListener> LISTENERS = new HashSet<>();

	public static void notifyListeners(ASLog logEvent) {
		for (ASLogEventListener listener : LISTENERS) {
			listener.handleLogEvent(logEvent);
		}
	}

	public static void addListener(final ASLogEventListener listener) {
		if (listener != null) {
			LISTENERS.add(listener);
		}
	}

	public static void removeListener(final ASLogEventListener listener) {
		LISTENERS.remove(listener);
	}
}
