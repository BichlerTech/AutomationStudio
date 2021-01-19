package com.bichler.astudio.editor.pubsub.wizard.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractDetailWizardPage extends WizardPage {

	protected Button btn_apply;

	protected AbstractDetailWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	public boolean isPageComplete() {
		boolean currentPage = isCurrentPage();
		return !currentPage;
	}

	void createApply(Composite container) {		
		//new Label(container, SWT.NONE);

		btn_apply = new Button(container, SWT.CENTER);
		GridData gdApply = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 3, 1);
		gdApply.widthHint = 125;
		btn_apply.setLayoutData(gdApply);
		btn_apply.setText("Apply");
	}
}
