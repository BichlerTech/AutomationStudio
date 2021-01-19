package com.bichler.astudio.opcua;

import java.util.SortedMap;
import java.util.TreeMap;

import com.bichler.astudio.utils.activator.InternationalActivator;

public class OPCUAModuleRegistry {
	private OPCUAModuleRegistry() {
		// private constructor to hide the public one
	}

	public static SortedMap<String, InternationalActivator> modules = new TreeMap<>();
}
