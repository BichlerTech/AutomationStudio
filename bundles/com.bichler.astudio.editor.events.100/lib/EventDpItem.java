package com.hbsoft.studio.editor.events.xml;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.hbsoft.studio.opcua.properties.driver.IDriverNode;

public class EventDpItem extends AbstractEventModel implements IDriverNode {

	private List<EventDpSyntax> children = null;

	public EventDpItem() {
		super();
		this.children = new ArrayList<>();
	}

	public void addChild(EventDpSyntax child) {
		child.setParent(this);
		this.children.add(child);
	}

	public EventDpSyntax[] getChildren() {
		return this.children.toArray(new EventDpSyntax[0]);
	}

	@Override
	public String getDname() {
		return null;
	}

	@Override
	public String getDesc() {
		return null;
	}

	@Override
	public String getDtype() {
		return null;
	}

	@Override
	public NodeId getNId() {
		return null;
	}

	@Override
	public boolean isValid() {
		return false;
	}

	@Override
	public String getBrowsepath() {
		return this.browsepath;
	}
}
