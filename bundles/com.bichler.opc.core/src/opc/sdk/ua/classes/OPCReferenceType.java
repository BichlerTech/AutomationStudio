package opc.sdk.ua.classes;

import java.util.Set;

import opc.sdk.core.classes.ua.BinaryImporterDecoder;
import opc.sdk.core.context.ISystemContext;
import opc.sdk.ua.AttributesToSave;
import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.constants.NodeStateChangeMasks;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.encoding.DecodingException;

public class OPCReferenceType extends BaseType {
	private LocalizedText inverseName;
	private boolean symmetric;

	public OPCReferenceType(BaseNode parent) {
		super(NodeClass.ReferenceType, parent);
		this.inverseName = null;
		this.symmetric = false;
	}

	@Override
	public void update(ISystemContext context, BinaryImporterDecoder decoder, Set<AttributesToSave> attributesToLoad)
			throws DecodingException {
		super.update(context, decoder, attributesToLoad);
		if (attributesToLoad.contains(AttributesToSave.InverseName)) {
			setInverseName(decoder.getLocalizedText(null));
		}
		if (attributesToLoad.contains(AttributesToSave.Symmetric)) {
			setSymmetric(decoder.getBoolean(null));
		}
	}

	/**
	 * Initializes the instance with the default values.
	 */
	@Override
	protected void initialize(IOPCContext context) {
		super.initialize(context);
		setInverseName(null);
		setSymmetric(false);
	}

	@Override
	protected void initialize(ISystemContext context, BaseNode source) {
		if (source != null && source instanceof OPCReferenceType) {
			setInverseName(((OPCReferenceType) source).getInverseName());
			setSymmetric(((OPCReferenceType) source).isSymmetric());
		}
		super.initialize(context, source);
	}

	public LocalizedText getInverseName() {
		return inverseName;
	}

	public void setInverseName(LocalizedText inverseName) {
		if (this.inverseName != inverseName) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.inverseName = inverseName;
	}

	public boolean isSymmetric() {
		return symmetric;
	}

	public void setSymmetric(boolean symmetric) {
		if (this.symmetric != symmetric) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.symmetric = symmetric;
	}
}
