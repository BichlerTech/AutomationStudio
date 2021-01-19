package com.bichler.astudio.opcua.nodes;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;

public class OPCUAServerPreShellScriptModelNode extends OPCUAServerPreShellScriptsModelNode {
	private String scriptName = "";
	private int interval = 0;

	public OPCUAServerPreShellScriptModelNode() {
		super();
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_SCRIPTS);
	}

	@Override
	public String getLabelText() {
		return this.getScriptName();
	}

	@Override
	public Object[] getChildren() {
		return new Object[0];
	}
}
