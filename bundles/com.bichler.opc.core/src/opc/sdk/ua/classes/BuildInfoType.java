package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.BuildInfo;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.IOPCContext;

public class BuildInfoType extends BaseDataVariableType<BuildInfo> {
	protected BaseDataVariableType<String> productUri;
	protected BaseDataVariableType<String> manufacturerName;
	protected BaseDataVariableType<String> productName;
	protected BaseDataVariableType<String> softwareVersion;
	protected BaseDataVariableType<String> buildNumber;
	protected BaseDataVariableType<DateTime> buildDate;

	public BuildInfoType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.productUri = new BaseDataVariableType<>(this);
		this.manufacturerName = new BaseDataVariableType<>(this);
		// this.ServerStatus = new serverstatus
		this.productName = new BaseDataVariableType<>(this);
		this.softwareVersion = new BaseDataVariableType<>(this);
		this.buildNumber = new BaseDataVariableType<>(this);
		this.buildDate = new BaseDataVariableType<>(this);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.BuildInfoType;
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.productUri != null) {
			children.add(this.productUri);
		}
		if (this.manufacturerName != null) {
			children.add(this.manufacturerName);
		}
		if (this.productName != null) {
			children.add(this.productName);
		}
		if (this.softwareVersion != null) {
			children.add(this.softwareVersion);
		}
		if (this.buildNumber != null) {
			children.add(this.buildNumber);
		}
		if (this.buildDate != null) {
			children.add(this.buildDate);
		}
		children.addAll(super.getChildren());
		return children;
	}
}
