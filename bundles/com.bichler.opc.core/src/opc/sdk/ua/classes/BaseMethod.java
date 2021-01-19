package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import opc.sdk.core.classes.ua.BinaryImporterDecoder;
import opc.sdk.core.context.ISystemContext;
import opc.sdk.ua.AttributesToSave;
import opc.sdk.ua.ICallHandler;
import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.constants.BrowseNames;
import opc.sdk.ua.constants.NodeStateChangeMasks;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.encoding.DecodingException;

public class BaseMethod extends BaseInstance {
	private ICallHandler handlerCall = null;
	private boolean executeable;
	private boolean userExecuteable;
	private PropertyVariableType<Argument[]> inputArguments = null;
	private PropertyVariableType<Argument[]> outputArguments = null;

	/**
	 * Initializes the instance with its defalt attribute values.
	 * 
	 * @param nodeClass
	 * @param parent
	 */
	public BaseMethod(BaseNode parent) {
		super(NodeClass.Method, parent);
	}

	public void setCallHandler(ICallHandler handler) {
		this.handlerCall = handler;
	}

	/**
	 * Populates a list with the children that belong to the node.
	 */
	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.inputArguments != null) {
			children.add(this.inputArguments);
		}
		if (this.outputArguments != null) {
			children.add(this.outputArguments);
		}
		children.addAll(super.getChildren());
		return children;
	}

	public ServiceResult call(IOPCContext context, Variant[] inputArguments, List<StatusCode> inputArgumentResults,
			List<Variant> outputArguments) {
		if (this.handlerCall != null) {
			return this.handlerCall.call(context, inputArguments, inputArgumentResults, outputArguments);
		}
		return new ServiceResult(StatusCodes.Bad_NotImplemented);
	}

	@Override
	public void update(ISystemContext context, BinaryImporterDecoder decoder, Set<AttributesToSave> attributesToLoad)
			throws DecodingException {
		super.update(context, decoder, attributesToLoad);
		if (attributesToLoad.contains(AttributesToSave.Executable)) {
			setExecuteable(decoder.getBoolean(null));
		}
		if (attributesToLoad.contains(AttributesToSave.UserExecutable)) {
			setUserExecuteable(decoder.getBoolean(null));
		}
	}

	@Override
	protected void initialize(IOPCContext context) {
		setExecuteable(true);
		setUserExecuteable(true);
	}

	@Override
	protected void initialize(ISystemContext context, BaseNode source) {
		if (source != null && source instanceof BaseMethod) {
			setUserExecuteable(((BaseMethod) source).getUserExecuteable());
			setExecuteable(((BaseMethod) source).getExecuteable());
		}
		super.initialize(context, source);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.MethodNode;
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
	}

	/**
	 * Finds the child with the specified browse name.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		if (BrowseNames.INPUTARGUMENTS.equalsIgnoreCase(browseName.getName())) {
			if (createOrReplace && this.inputArguments == null) {
				if (replacement == null) {
					this.inputArguments = new PropertyVariableType<>(this);
				} else {
					this.inputArguments = (PropertyVariableType<Argument[]>) replacement;
				}
			}
			instance = this.inputArguments;
		} else if (BrowseNames.OUTPUTARGUMENTS.equalsIgnoreCase(browseName.getName())) {
			if (createOrReplace && this.outputArguments == null) {
				if (replacement == null) {
					this.outputArguments = new PropertyVariableType<>(this);
				} else {
					this.outputArguments = (PropertyVariableType<Argument[]>) replacement;
				}
			}
			instance = this.outputArguments;
		}
		if (instance != null) {
			return instance;
		}
		return super.findChild(browseName, createOrReplace, replacement);
	}

	public boolean getExecuteable() {
		return this.executeable;
	}

	public boolean getUserExecuteable() {
		return this.userExecuteable;
	}

	public void setUserExecuteable(boolean value) {
		if (this.userExecuteable != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.userExecuteable = value;
	}

	public void setExecuteable(boolean value) {
		if (this.executeable != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.executeable = value;
	}

	public PropertyVariableType<Argument[]> getInputArguments() {
		return inputArguments;
	}

	public void setInputArguments(PropertyVariableType<Argument[]> inputArguments) {
		if (this.inputArguments != inputArguments) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.inputArguments = inputArguments;
	}

	public PropertyVariableType<Argument[]> getOutputArguments() {
		return outputArguments;
	}

	public void setOutputArguments(PropertyVariableType<Argument[]> outputArguments) {
		if (this.outputArguments != outputArguments) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.outputArguments = outputArguments;
	}

	/**
	 * Get the identifier for the declaration of the method in the type model.
	 * 
	 * @return
	 */
	public NodeId getMethodDeclarationId() {
		return getTypeDefinitionId();
	}

	/**
	 * Set the identifier for the declaration of the method in the type model.
	 * 
	 * @param id
	 */
	public void setMethodDeclarationId(NodeId id) {
		setTypeDefinitionId(id);
	}
}
