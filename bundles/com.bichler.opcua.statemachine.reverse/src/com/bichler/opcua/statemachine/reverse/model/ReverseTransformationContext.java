package com.bichler.opcua.statemachine.reverse.model;

import java.util.HashMap;
import java.util.Map;

import com.bichler.opcua.statemachine.reverse.engineering.IReverseTransformationContext;

public class ReverseTransformationContext implements IReverseTransformationContext{
	
	private String directoryPath;
	private Map<String, String> umlPackageMapping;

	public ReverseTransformationContext(String directoryPath) {
		if(directoryPath == null) {
			throw new IllegalArgumentException("Cannot reverse engineer OPC UA model");
		}
		this.directoryPath = directoryPath;
		this.umlPackageMapping = new HashMap<>();
	}

	@Override
	public String getDirectoryPath() {
		return this.directoryPath;
	}

	@Override
	public void addUMLPackageMapping(String namespaceUri, String packageId) {
		this.umlPackageMapping.put(namespaceUri, packageId);
	}

	@Override
	public String mapPackage(String namespaceUri) {
		return this.umlPackageMapping.get(namespaceUri);
	}
}
