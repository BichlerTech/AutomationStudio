package com.bichler.astudio.opcua.wizard.page;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NewOPCUAServerCertificateWizardPage1 extends WizardPage {

	private IWorkbench workbench = null;
	private IStructuredSelection selection = null;

	/**
	 * Create the wizard.
	 */
	public NewOPCUAServerCertificateWizardPage1(IWorkbench workbench, IStructuredSelection selection) {
		this();
		this.workbench = workbench;
		this.selection = selection;
	}

	/**
	 * Create the wizard.
	 */
	public NewOPCUAServerCertificateWizardPage1() {
		super("wizardPage");
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.certificate.title"));
		setDescription(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.certificate.description"));
		ImageDescriptor desc = new ImageDescriptor() {

			@Override
			public ImageData getImageData() {
				return StudioImageActivator.getImage(StudioImages.ICON_WIZARD_CERTIFICATE_ADD).getImageData();
			}
		};
		setImageDescriptor(desc);
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);

		Label lblServername = new Label(container, SWT.NONE);
		lblServername.setBounds(30, 27, 105, 14);
		lblServername.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.certificate.certificatename"));
	}
}
