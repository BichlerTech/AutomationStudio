package com.bichler.opcua.statemachine.reverse.engineering;

public interface IReverseTransformationContext {

	public String getDirectoryPath();
	
	public void addUMLPackageMapping(String namespaceUri, String packageId);

	public String mapPackage(String namespaceUri);
}
