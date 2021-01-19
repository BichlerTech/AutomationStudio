package com.bichler.astudio.opcua.editor.input;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;

public class OPCUAHistoryPathEditorInput implements IPathEditorInput {
	private IPath fPath;
	private String projectName;

	public OPCUAHistoryPathEditorInput(IPath path) {
		if (path == null) {
			throw new IllegalArgumentException();
		}
		this.fPath = path;
	}

	@Override
	public int hashCode() {
		return fPath.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof OPCUAHistoryPathEditorInput))
			return false;
		OPCUAHistoryPathEditorInput other = (OPCUAHistoryPathEditorInput) obj;
		return fPath.equals(other.fPath);
	}

	public boolean exists() {
		return fPath.toFile().exists();
	}

	public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor(fPath.toString());
	}

	public String getName() {
		return projectName + " - Historie";
	}

	public String getToolTipText() {
		return fPath.makeRelative().toOSString();
	}

	public IPath getPath() {
		return fPath;
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
