package com.bichler.astudio.editor.pubsub.nodes;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.core.ConfigurationVersionDataType;
import org.opcfoundation.ua.core.PublishedVariableDataType;

import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConfigurationVersion;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperPublishedVariableParameter;

public class DataSetVariable implements Cloneable {

	private WrapperConfigurationVersion configurationVersion;
	private String fieldNameAlias;
	private Boolean promotedField;
	private WrapperPublishedVariableParameter publishParameters;
	
	//private PublishedVariableDataType publishParameters;
	
	/* non std. field */
	private Boolean staticValueSourceEnabled;
	private DataValue staticValueSource;

	public void setFieldNameAlias(String value) {
		this.fieldNameAlias = value;
	}

	public String getFieldNameAlias() {
		return this.fieldNameAlias;
	}

	@Override
	public DataSetVariable clone() {
		DataSetVariable clone = new DataSetVariable();
		if (this.configurationVersion != null) {
			clone.configurationVersion = this.configurationVersion.clone();
		}
		clone.fieldNameAlias = this.fieldNameAlias;
		clone.promotedField = this.promotedField;
		if (this.publishParameters != null) {
			clone.publishParameters = this.publishParameters.clone();
		}
		clone.staticValueSourceEnabled = this.staticValueSourceEnabled;
		if (this.staticValueSource != null) {
			clone.staticValueSource = (DataValue) this.staticValueSource.clone();
		}
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		DataSetVariable other = (DataSetVariable) obj;
		if (this.configurationVersion == null) {
			if (other.configurationVersion != null) {
				return false;
			}
		} else if (!this.configurationVersion.equals(other.configurationVersion)) {
			return false;
		}

		if (this.fieldNameAlias == null) {
			if (other.fieldNameAlias != null) {
				return false;
			}
		} else if (!this.fieldNameAlias.equals(other.fieldNameAlias)) {
			return false;
		}

		if (this.promotedField == null) {
			if (other.promotedField != null) {
				return false;
			}
		} else if (!this.promotedField.equals(other.promotedField)) {
			return false;
		}

		if (this.staticValueSourceEnabled == null) {
			if (other.staticValueSourceEnabled != null) {
				return false;
			}
		} else if (!this.staticValueSourceEnabled.equals(other.staticValueSourceEnabled)) {
			return false;
		}

		if (this.staticValueSource == null) {
			if (other.staticValueSource != null) {
				return false;
			}
		} else if (!this.staticValueSource.equals(other.staticValueSource)) {
			return false;
		}

		return true;
	}

	public WrapperConfigurationVersion getConfigurationVersion() {
		return configurationVersion;
	}

	public void setConfigurationVersion(WrapperConfigurationVersion configurationVersion) {
		this.configurationVersion = configurationVersion;
	}

	public Boolean getPromotedField() {
		return promotedField;
	}

	public void setPromotedField(Boolean promotedField) {
		this.promotedField = promotedField;
	}

	public WrapperPublishedVariableParameter getPublishParameters() {
		return publishParameters;
	}

	public void setPublishParameters(WrapperPublishedVariableParameter publishParameters) {
		this.publishParameters = publishParameters;
	}

	public Boolean getStaticValueSourceEnabled() {
		return staticValueSourceEnabled;
	}

	public void setStaticValueSourceEnabled(Boolean staticValueSourceEnabled) {
		this.staticValueSourceEnabled = staticValueSourceEnabled;
	}

	public DataValue getStaticValueSource() {
		return staticValueSource;
	}

	public void setStaticValueSource(DataValue staticValueSource) {
		this.staticValueSource = staticValueSource;
	}
}
