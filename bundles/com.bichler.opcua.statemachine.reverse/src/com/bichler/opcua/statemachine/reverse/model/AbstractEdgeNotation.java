package com.bichler.opcua.statemachine.reverse.model;

import com.bichler.opcua.statemachine.reverse.engineering.IUMLTranslation;

public abstract class AbstractEdgeNotation implements IEdgeNotation {

	private String id = "";
	private IUMLTranslation source = null;
	private IUMLTranslation target = null;

	public AbstractEdgeNotation(String id, IUMLTranslation source, IUMLTranslation target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public IUMLTranslation getSource() {
		return this.source;
	}

	@Override
	public IUMLTranslation getTarget() {
		return this.target;
	}

	@Override
	public boolean isLocalTarget() {
		String sourceModel = this.source.getModelname();
		String targetModel = this.source.getModelname();

		if (this.target != null) {
			targetModel = this.target.getModelname();
		}

		boolean sameModel = sourceModel.equals(targetModel);
		if (!sameModel) {
			return false;
		}

		String sourcePackagename = this.source.getPackageName();
		String targetPackagename = this.source.getPackageName();
		if (this.target != null) {
			targetPackagename = this.target.getPackageName();
		}
		return sourcePackagename.equals(targetPackagename);
	}

}
