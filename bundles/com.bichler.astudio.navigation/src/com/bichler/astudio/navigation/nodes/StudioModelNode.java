package com.bichler.astudio.navigation.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.filesystem.IFileSystem;

public abstract class StudioModelNode {

	protected IFileSystem filesystem = null;
	protected IFileSystem targetFilesystem = null;

	private List<StudioModelNode> childrenList = null;

	private StudioModelNode parent = null;

	/**
	 * build version of that studio model node
	 */
	private String version = "1.0.1";
	private String buildDate = "01.01.2012";

	// private String license = ""; // TODO not used now

	public StudioModelNode() {
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	public List<StudioModelNode> getChildrenList() {
		return childrenList;
	}

	public void setChildren(List<StudioModelNode> childrenList) {
		this.childrenList = childrenList;

		if (childrenList == null) {
			return;
		}
		// set parent for each child
		for (StudioModelNode c : childrenList) {
			c.setParent(this);
		}
	}

	public void addChild(StudioModelNode child) {
		if (this.childrenList == null) {
			this.childrenList = new ArrayList<StudioModelNode>();
		}
		child.setParent(this);
		this.childrenList.add(child);
	}

	public StudioModelNode getParent() {
		return parent;
	}

	public void setParent(StudioModelNode parent) {
		this.parent = parent;
	}

	/**
	 * This function will be called after a doubleclick on an navigation item was
	 * performed.
	 */
	public abstract void nodeDBLClicked();

	/**
	 * Get the image for that navigation item.
	 * 
	 * @return
	 */
	public abstract Image getLabelImage();

	/**
	 * Get the label text for that navigation item.
	 * 
	 * @return
	 */
	public abstract String getLabelText();

	/**
	 * Get all children of that navigation item. If it is the first call, so we
	 * create all children, otherwise only get back the previously created ones.
	 * 
	 * @param parentElement
	 * @return
	 */
	public abstract Object[] getChildren();

	/**
	 * Clear all previously created children
	 */
	public void clearChildren() {
		if (this.childrenList == null) {
			return;
		}
		this.childrenList.clear();
	}

	public IFileSystem getFilesystem() {
		return filesystem;
	}

	public void setFilesystem(IFileSystem fileSystem) {
		this.filesystem = fileSystem;
	}

	public IFileSystem getTargetFilesystem() {
		return targetFilesystem;
	}

	public void setTargetFilesystem(IFileSystem fileSystem) {
		this.targetFilesystem = fileSystem;
	}

	public abstract void refresh();

	public boolean useNavigationComperator() {
		return true;
	}

	public boolean showChildren(IContentProvider cp) {
		return true;
	}

	public void rename(String newName) {

	}
}
