package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.SignedSoftwareCertificate;

import opc.sdk.ua.IOPCContext;

public class ServerCapabilitiesType extends BaseObjectType {
	protected PropertyVariableType<String[]> serverProfileArray;
	protected PropertyVariableType<String[]> localeIdArray;
	protected PropertyVariableType<Double> minSupportedSampleRate;
	protected PropertyVariableType<UnsignedShort> maxBrowseContinuationPoints;
	protected PropertyVariableType<UnsignedShort> maxQueryContinuationPoints;
	protected PropertyVariableType<UnsignedShort> maxHistoryContinuationPoints;
	protected PropertyVariableType<SignedSoftwareCertificate[]> softwareCertificates;
	protected PropertyVariableType<UnsignedInteger> maxArrayLength;
	protected PropertyVariableType<UnsignedInteger> maxStringLength;
	protected OperationLimitsType operationLimits;
	protected FolderType modellingRules;
	protected FolderType aggregateFunctions;

	public ServerCapabilitiesType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.serverProfileArray = new PropertyVariableType<>(this);
		this.localeIdArray = new PropertyVariableType<>(this);
		this.minSupportedSampleRate = new PropertyVariableType<>(this);
		this.maxBrowseContinuationPoints = new PropertyVariableType<>(this);
		this.maxQueryContinuationPoints = new PropertyVariableType<>(this);
		this.maxHistoryContinuationPoints = new PropertyVariableType<>(this);
		this.softwareCertificates = new PropertyVariableType<>(this);
		this.maxArrayLength = new PropertyVariableType<>(this);
		this.maxStringLength = new PropertyVariableType<>(this);
		this.operationLimits = new OperationLimitsType(this);
		this.modellingRules = new FolderType(this);
		this.aggregateFunctions = new FolderType(this);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ServerCapabilitiesType;
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.serverProfileArray != null) {
			children.add(this.serverProfileArray);
		}
		if (this.localeIdArray != null) {
			children.add(this.localeIdArray);
		}
		if (this.minSupportedSampleRate != null) {
			children.add(this.minSupportedSampleRate);
		}
		if (this.maxBrowseContinuationPoints != null) {
			children.add(this.maxBrowseContinuationPoints);
		}
		if (this.maxQueryContinuationPoints != null) {
			children.add(this.maxQueryContinuationPoints);
		}
		if (this.maxHistoryContinuationPoints != null) {
			children.add(this.maxHistoryContinuationPoints);
		}
		if (this.softwareCertificates != null) {
			children.add(this.softwareCertificates);
		}
		if (this.maxArrayLength != null) {
			children.add(this.maxArrayLength);
		}
		if (this.maxStringLength != null) {
			children.add(this.maxStringLength);
		}
		if (this.operationLimits != null) {
			children.add(this.operationLimits);
		}
		if (this.modellingRules != null) {
			children.add(this.modellingRules);
		}
		if (this.aggregateFunctions != null) {
			children.add(this.aggregateFunctions);
		}
		children.addAll(super.getChildren());
		return children;
	}
}
