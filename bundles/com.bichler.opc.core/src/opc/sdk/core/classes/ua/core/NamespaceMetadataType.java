package opc.sdk.core.classes.ua.core;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.utils.NumericRange;

public class NamespaceMetadataType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = Identifiers.NamespaceMetadataType;
	private String NamespaceUri;
	private String NamespaceVersion;
	private DateTime NamespacePublicationDate;
	private Boolean IsNamespaceSubset;
	private IdType[] StaticNodeIdTypes;
	private NumericRange[] StaticNumericNodeIdRange;
	private String StaticStringNodeIdPattern;

	public NamespaceMetadataType() {
		super();
	}

	public String getNamespaceUri() {
		return NamespaceUri;
	}

	public void setNamespaceUri(String namespaceUri) {
		NamespaceUri = namespaceUri;
	}

	public String getNamespaceVersion() {
		return NamespaceVersion;
	}

	public void setNamespaceVersion(String namespaceVersion) {
		NamespaceVersion = namespaceVersion;
	}

	public DateTime getNamespacePublicationDate() {
		return NamespacePublicationDate;
	}

	public void setNamespacePublicationDate(DateTime namespacePublicationDate) {
		NamespacePublicationDate = namespacePublicationDate;
	}

	public Boolean getIsNamespaceSubset() {
		return IsNamespaceSubset;
	}

	public void setIsNamespaceSubset(Boolean isNamespaceSubset) {
		IsNamespaceSubset = isNamespaceSubset;
	}

	public IdType[] getStaticNodeIdTypes() {
		return StaticNodeIdTypes;
	}

	public void setStaticNodeIdTypes(IdType[] staticNodeIdTypes) {
		StaticNodeIdTypes = staticNodeIdTypes;
	}

	public NumericRange[] getStaticNumericNodeIdRange() {
		return StaticNumericNodeIdRange;
	}

	public void setStaticNumericNodeIdRange(NumericRange[] staticNumericNodeIdRange) {
		StaticNumericNodeIdRange = staticNumericNodeIdRange;
	}

	public String getStaticStringNodeIdPattern() {
		return StaticStringNodeIdPattern;
	}

	public void setStaticStringNodeIdPattern(String staticStringNodeIdPattern) {
		StaticStringNodeIdPattern = staticStringNodeIdPattern;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "NamespaceMetadataType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
