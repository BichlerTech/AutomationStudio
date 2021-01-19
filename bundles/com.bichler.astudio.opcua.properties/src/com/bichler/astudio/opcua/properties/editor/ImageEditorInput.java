package com.bichler.astudio.opcua.properties.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class ImageEditorInput implements IEditorInput {

	private String opcuaProjectName;

	public ImageEditorInput(String proj) {
		this.opcuaProjectName = proj;
	}
	
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}

	// equals und hashCode müssen implementiert werden, damit nur ein
	// Editor für dasselbe Dokumente geöffnet werden kann
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		ImageEditorInput iobj = (ImageEditorInput) obj;
		return (opcuaProjectName).compareTo(iobj.opcuaProjectName) == 0;
	}

	@Override
	public int hashCode() {
		// if (checkEquals) {
		return super.hashCode();
	}

}
