package com.bichler.astudio.javafx.component.editor;

import com.bichler.astudio.filesystem.IFileSystem;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import javafx.scene.Node;

public class FXEditorInput implements IEditorInput {

	private  String iecProjectName = "";

	private String iecFBName = "";
	
	private Map<Node, Object> buisnessObjects = new HashMap<>();
	
	private IFileSystem filesystem;
	
	public FXEditorInput() {

	}
	
	public IFileSystem getFilesystem() {
    return filesystem;
  }

  public void setFilesystem(IFileSystem filesystem) {
    this.filesystem = filesystem;
  }
  
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		
		FXEditorInput iobj = (FXEditorInput) obj;
		return (iecProjectName + " - " + this.iecFBName)
				.compareTo(iobj.iecProjectName + " - " + iobj.iecFBName) == 0;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "";
	}

	public Object getBuisnessObjectFromNode(Node node) {
		return this.buisnessObjects.get(node);
	}

	public Node getNodeFromBuisnessObject(Object bo) {
		for (Entry<Node, Object> e : this.buisnessObjects.entrySet()) {
			if (bo == e.getValue()) {
				return e.getKey();
			}
		}
		return null;
	}

	public void addBuilsnessObjectAndVisual(Node node, Object bo) {
		this.buisnessObjects.put(node, bo);
	}

	public String getIecFBName() {
		return iecFBName;
	}

	public void setIecFBName(String iecFBName) {
		this.iecFBName = iecFBName;
	}
	
	public String getIecProjectName() {
		return iecProjectName;
	}

	public void setIecProjectName(String iecProjectName) {
		this.iecProjectName = iecProjectName;
	}

	public void setName(String name) {
		this.iecFBName = name;
	}
	
	@Override
	public String getName() {
		return iecFBName;
	}
}
