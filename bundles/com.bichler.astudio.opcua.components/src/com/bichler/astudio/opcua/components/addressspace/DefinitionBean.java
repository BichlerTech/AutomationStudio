package com.bichler.astudio.opcua.components.addressspace;

import java.util.ArrayList;

public class DefinitionBean implements Cloneable {

	private String definitionName = null;

	private ArrayList<DefinitionFieldBean> fields = new ArrayList<>();

	public DefinitionBean() {
	}

	public String getDefinitionName() {
		return definitionName;
	}

	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	public void addField(DefinitionFieldBean field) {
		this.fields.add(field);
	}

	public DefinitionFieldBean[] getFields() {
		return this.fields.toArray(new DefinitionFieldBean[0]);
	}

	public void removeField(DefinitionFieldBean field) {
		this.fields.remove(field);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DefinitionBean)) {
			return false;
		} 
		
		if(this.definitionName != null) {
			if(((DefinitionBean) obj).definitionName == null) {
				return false;
			}else if(!this.definitionName.equals(((DefinitionBean) obj).definitionName)){
				return false;
			}
		} else {
			if(((DefinitionBean) obj).definitionName != null) {
				return false;
			}
		}

		DefinitionFieldBean[] fields1 = getFields();
		DefinitionFieldBean[] fields2 = ((DefinitionBean) obj).getFields();

		if(fields1.length != fields2.length) {
			return false;
		}
		
		for (DefinitionFieldBean field : fields1) {
			boolean found = false;
			for (DefinitionFieldBean lookup : fields2) {
				if (field.equals(lookup)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}

		return true;
	}

	@Override
	public DefinitionBean clone() {
		DefinitionBean cloned = new DefinitionBean();
		cloned.definitionName = this.definitionName;

		for (DefinitionFieldBean field : getFields()) {
			cloned.addField(field.clone());
		}

		return cloned;
	}

}
