package opc.sdk.core.classes.ua.core;

public class BuildInfoType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.BuildInfoType;
	private BaseDataVariableType productName;
	private BaseDataVariableType manufacturerName;
	private BaseDataVariableType buildNumber;
	private BaseDataVariableType softwareVersion;
	private BaseDataVariableType buildDate;
	private BaseDataVariableType productUri;

	public BuildInfoType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.core.BuildInfo getValue() {
		return getVariant() != null ? (org.opcfoundation.ua.core.BuildInfo) getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public BaseDataVariableType getProductName() {
		return productName;
	}

	public void setProductName(BaseDataVariableType value) {
		productName = value;
	}

	public BaseDataVariableType getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(BaseDataVariableType value) {
		manufacturerName = value;
	}

	public BaseDataVariableType getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(BaseDataVariableType value) {
		buildNumber = value;
	}

	public BaseDataVariableType getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(BaseDataVariableType value) {
		softwareVersion = value;
	}

	public BaseDataVariableType getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(BaseDataVariableType value) {
		buildDate = value;
	}

	public BaseDataVariableType getProductUri() {
		return productUri;
	}

	public void setProductUri(BaseDataVariableType value) {
		productUri = value;
	}

	@Override
	public String toString() {
		return "BuildInfoType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
