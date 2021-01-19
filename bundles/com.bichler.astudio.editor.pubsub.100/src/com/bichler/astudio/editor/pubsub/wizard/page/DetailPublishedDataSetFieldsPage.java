package com.bichler.astudio.editor.pubsub.wizard.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DetailPublishedDataSetFieldsPage extends AbstractDetailWizardPage {
	private Text txt_name;
	private Text txt_description;

	protected DetailPublishedDataSetFieldsPage() {
		super("metadatafields");
		setTitle("Metadata Fields");
		setDescription("MetaDataFields");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(3, false));		
	}

}
