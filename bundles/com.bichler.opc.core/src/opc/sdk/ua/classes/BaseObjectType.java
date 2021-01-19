package opc.sdk.ua.classes;

import java.util.Set;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.encoding.DecodingException;

import opc.sdk.core.classes.ua.BinaryImporterDecoder;
import opc.sdk.core.context.ISystemContext;
import opc.sdk.core.enums.EventNotifiers;
import opc.sdk.ua.AttributesToSave;
import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class BaseObjectType extends BaseInstance {
	private byte eventNotifier;

	/**
	 * Initializes the instance with its defalt attribute values.
	 * 
	 * @param parent
	 */
	public BaseObjectType(BaseNode parent) {
		super(NodeClass.Object, parent);
		this.eventNotifier = EventNotifiers.None.getValue();
		if (parent != null) {
			setReferenceTypeId(Identifiers.HasComponent);
		}
	}

	@Override
	protected void initialize(IOPCContext context) {
		setSymbolicName(Identifiers.BaseObjectType.toString() + "_Instance");
		setBrowseName(new QualifiedName(0, getSymbolicName()));
		setDisplayName(new LocalizedText(getSymbolicName(), ""));
		setDescription(LocalizedText.EMPTY);
		setWriteMask(AttributeWriteMask.None);
		setUserWriteMask(AttributeWriteMask.None);
		setTypeDefinitionId(getDefaultTypeDefinitionId());
		setEventNotifier(EventNotifiers.None.getValue());
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		// default implementation, no effect
	}

	@Override
	public void update(ISystemContext context, BinaryImporterDecoder decoder, Set<AttributesToSave> attributesToLoad)
			throws DecodingException {
		super.update(context, decoder, attributesToLoad);
		if (attributesToLoad.contains(AttributesToSave.EventNotifier)) {
			this.eventNotifier = decoder.getByte(null).byteValue();
		}
	}

	public byte getEventNotifier() {
		return this.eventNotifier;
	}

	public void setEventNotifier(byte eventNotifier) {
		if (this.eventNotifier != eventNotifier) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.eventNotifier = eventNotifier;
	}

	/**
	 * Returns the id of the default type definition node for the instance.
	 */
	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.BaseObjectType;
	}

	@Override
	protected void onAfterCreate(ISystemContext context, BaseNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onBeforeAssignNodeIds() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onBeforeCreate(ISystemContext context, BaseNode node) {
		// TODO Auto-generated method stub
	}
}
