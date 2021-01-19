package com.bichler.astudio.opcua.components.addressspace;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;

public class DefinitionFieldBean implements Cloneable{
	private String name = null;
	private LocalizedText description = null;
	private NodeId datatype = null;
	private Integer valueRank = null;
	private Integer value = null;

	public DefinitionFieldBean() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalizedText getDescription() {
		return description;
	}

	public void setDescription(LocalizedText description) {
		this.description = description;
	}

	public NodeId getDatatype() {
		return datatype;
	}

	public void setDatatype(NodeId datatype) {
		this.datatype = datatype;
	}

	public Integer getValueRank() {
		return valueRank;
	}

	public void setValueRank(Integer valueRank) {
		this.valueRank = valueRank;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DefinitionFieldBean)) {
			return false;
		} 
		
		if(this.name != null) {
			if(((DefinitionFieldBean) obj).name == null) {
				return false;
			}else if(!this.name.equals(((DefinitionFieldBean) obj).name)){
				return false;
			}
		} else {
			if(((DefinitionFieldBean) obj).name != null) {
				return false;
			}
		}
		
		if(this.description != null) {
			if(((DefinitionFieldBean) obj).description == null) {
				return false;
			}else if(!this.description.equals(((DefinitionFieldBean) obj).description)){
				return false;
			}
		} else {
			if(((DefinitionFieldBean) obj).description != null) {
				return false;
			}
		}		
		
		if(this.value != null) {
			if(((DefinitionFieldBean) obj).value == null) {
				return false;
			}else if(!this.value.equals(((DefinitionFieldBean) obj).value)){
				return false;
			}
		} else {
			if(((DefinitionFieldBean) obj).value != null) {
				return false;
			}
		}
		
		if(this.valueRank != null) {
			if(((DefinitionFieldBean) obj).valueRank == null) {
				return false;
			}else if(!this.valueRank.equals(((DefinitionFieldBean) obj).valueRank)){
				return false;
			}
		} else {
			if(((DefinitionFieldBean) obj).valueRank != null) {
				return false;
			}
		}	
		
		if(this.datatype != null) {
			if(((DefinitionFieldBean) obj).datatype == null) {
				return false;
			}else if(!this.datatype.equals(((DefinitionFieldBean) obj).datatype)){
				return false;
			}
		} else {
			if(((DefinitionFieldBean) obj).datatype != null) {
				return false;
			}
		}	

		return true;
	}

	@Override
	protected DefinitionFieldBean clone()  {
		DefinitionFieldBean field = new DefinitionFieldBean();
		field.datatype = this.datatype;
		field.description = this.description;
		field.name = this.name;
		field.value = this.value;
		field.valueRank = this.valueRank;
		
		return field;
	}
	
	

}
