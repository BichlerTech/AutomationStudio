package com.bichler.astudio.licensemanagement.editor.inputs;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

//import com.hbsoft.visu.Comet_ResourceManager;

public class LicenseManagerEditorInput implements IEditorInput {

	// private Comet_ResourceManager manager = null;
	
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
		return "license input";
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "license manager input";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

//	public Comet_ResourceManager getManager() {
//		return manager;
//	}
//
//	public void setManager(Comet_ResourceManager manager) {
//		this.manager = manager;
//	}
}
