package com.bichler.astudio.opcua.opcmodeler.wizards.opc.node;

import org.eclipse.jface.wizard.Wizard;
import org.opcfoundation.ua.core.Argument;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.MethodArgumentPage;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAEditMethodArgumentWizard extends Wizard {
	private MethodArgumentPage pageArguments;
	private Argument[] inputArgs = new Argument[0];
	private Argument[] outputArgs = new Argument[0];

	public OPCUAEditMethodArgumentWizard() {
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.argument.title"));
	}

	@Override
	public void addPages() {
		this.pageArguments = new MethodArgumentPage();
		this.pageArguments.setInputArguments(this.inputArgs);
		this.pageArguments.setOutputArguments(this.outputArgs);
		addPage(this.pageArguments);
	}

	@Override
	public boolean performFinish() {
		this.outputArgs = this.pageArguments.getOutputArguments();
		this.inputArgs = this.pageArguments.getInputArguments();
		return true;
	}

	public Argument[] getInputArgs() {
		return this.inputArgs;
	}

	public Argument[] getOutputArgs() {
		return this.outputArgs;
	}

	public void setInputArgs(Argument... inputArgs) {
		this.inputArgs = inputArgs;
	}

	public void setOutputArgs(Argument... outputArgs) {
		this.outputArgs = outputArgs;
	}
}
