package com.bichler.astudio.opcua.javacommand.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.opcua.javacommand.wizard.page.CommandSkriptWizardPage;

public class AddCommandWizard extends Wizard {

	private CommandSkriptWizardPage commandPage;
	private String skript;
	private String description;
	private String skriptname;
	
	private Preferences edit;

	public AddCommandWizard() {
		setWindowTitle("Neues OPC UA Serverstartskript");
		this.commandPage = new CommandSkriptWizardPage();
	}
	
	public AddCommandWizard(Preferences prefernence){
		setWindowTitle("Bearbeite OPC UA Serverstartskript");
		this.edit = prefernence;
		this.commandPage = new CommandSkriptWizardPage(this.edit);
	}

	@Override
	public void addPages() {
		
		addPage(this.commandPage);
	}

	@Override
	public boolean performFinish() {
		this.description = this.commandPage.getDescription();
		this.skriptname = this.commandPage.getSkriptName();
		this.skript = this.commandPage.getSkript();
		
		return true;
	}

	public String getSkript() {
		return skript;
	}

	public String getDescription() {
		return description;
	}

	public String getSkriptname() {
		return skriptname;
	}
	
	

}
