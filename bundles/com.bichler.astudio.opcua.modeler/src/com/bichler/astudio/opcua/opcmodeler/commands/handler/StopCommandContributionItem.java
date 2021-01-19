package com.bichler.astudio.opcua.opcmodeler.commands.handler;

import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.osgi.framework.Bundle;

public class StopCommandContributionItem extends CommandContributionItem {
	private Bundle bundle = null;

	public StopCommandContributionItem(CommandContributionItemParameter contributionParameters) {
		super(contributionParameters);
		// TODO Auto-generated constructor stub
	}

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
}
