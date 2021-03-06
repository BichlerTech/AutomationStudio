package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.NodeClass;

import opc.sdk.core.enums.EventNotifiers;
import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class ViewType extends BaseNode {
	private byte eventNotifier;
	private boolean containsNoLoops;

	public ViewType() {
		super(NodeClass.View);
	}

	@Override
	protected void initialize(IOPCContext context) {
		setSymbolicName("View1");
		setNodeId(null);
		setBrowseName(new QualifiedName(getSymbolicName()));
		setDisplayName(new LocalizedText(getSymbolicName(), ""));
		setDescription(null);
		setWriteMask(AttributeWriteMask.None);
		setUserWriteMask(AttributeWriteMask.None);
		setEventNotifier(EventNotifiers.None.getValue());
		setContainsNoLoops(false);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		// default implemenation with no effect
	}

	public byte getEventNotifier() {
		return eventNotifier;
	}

	public void setEventNotifier(byte eventNotifier) {
		if (this.eventNotifier != eventNotifier) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.eventNotifier = eventNotifier;
	}

	public boolean isContainsNoLoops() {
		return containsNoLoops;
	}

	public void setContainsNoLoops(boolean containsNoLoops) {
		if (this.containsNoLoops != containsNoLoops) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.containsNoLoops = containsNoLoops;
	}
}
