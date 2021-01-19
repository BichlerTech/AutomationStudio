package com.bichler.astudio.opcua.editor.security;

import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.editor.input.OPCUACertificateEditorInput;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUACertificateEditor extends EditorPart {

	public static final String ID = "com.bichler.astudio.opcua.editor.security.OPCUACertificateEditor"; //$NON-NLS-1$

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private ScrolledComposite scrolledComposite;
	private Composite composite;
	private Text txt_cn;
	private Text txt_organisation;
	private Text txt_algorithm;

	public OPCUACertificateEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		createControl(container);

		fillControl();

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void fillControl() {
		X509Certificate cert = (X509Certificate) getEditorInput().getCertificate();
		// X500Principal issuer = cert.getIssuerX500Principal();
		X500Principal subject = cert.getSubjectX500Principal();
		this.txt_cn.setText(subject.getName().split(",")[0].split("=")[1].trim());
		this.txt_organisation.setText(subject.getName().split(",")[1].split("=")[1].trim());
		this.txt_algorithm.setText(cert.getSigAlgName());
	}

	private void createControl(Composite parent) {
		Form frmNewForm = formToolkit.createForm(parent);
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.certificate"));
		frmNewForm.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));

		this.scrolledComposite = new ScrolledComposite(frmNewForm.getBody(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		formToolkit.adapt(scrolledComposite);
		formToolkit.paintBordersFor(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		this.composite = new Composite(scrolledComposite, SWT.NONE);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(2, false));

		Label lblCn = new Label(composite, SWT.NONE);
		lblCn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblCn, true, true);
		lblCn.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.cn"));

		txt_cn = formToolkit.createText(composite, "New Text", SWT.NONE);
		txt_cn.setText("");
		txt_cn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblOrganisation = new Label(composite, SWT.NONE);
		lblOrganisation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOrganisation.setText("Organisation");
		formToolkit.adapt(lblOrganisation, true, true);

		txt_organisation = formToolkit.createText(composite, "New Text", SWT.NONE);
		txt_organisation.setText("");
		txt_organisation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblAlgorithm = new Label(composite, SWT.NONE);
		lblAlgorithm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAlgorithm
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.algorithm"));
		formToolkit.adapt(lblAlgorithm, true, true);

		txt_algorithm = formToolkit.createText(composite, "New Text", SWT.NONE);
		txt_algorithm.setText("");
		txt_algorithm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label = formToolkit.createSeparator(composite, SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		new Label(composite, SWT.NONE);
		scrolledComposite.setContent(composite);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// Initialize the editor part
		setSite(site);
		setInput(input);

		setPartName(((X509Certificate) ((OPCUACertificateEditorInput) input).getCertificate()).getSubjectX500Principal()
				.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public OPCUACertificateEditorInput getEditorInput() {
		return (OPCUACertificateEditorInput) super.getEditorInput();
	}

}
