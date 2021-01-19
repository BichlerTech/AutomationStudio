package com.bichler.astudio.opcua.editor.security;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bichler.astudio.opcua.IOPCPerspectiveEditor;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAServerCertificatesEditor extends EditorPart implements IOPCPerspectiveEditor {

	public static final String ID = "com.bichler.astudio.opcua.editor.opcuaservercerts";

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	private Composite composite;

	private ScrolledComposite scrolledComposite;

	private boolean initState;

	public OPCUAServerCertificatesEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		this.setSite(site);
		this.setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		this.scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		this.composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Form frm_servercertificates = formToolkit.createForm(composite);
		formToolkit.paintBordersFor(frm_servercertificates);
		frm_servercertificates.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.certificate"));
		frm_servercertificates.getBody().setLayout(new GridLayout(1, false));

		Group group_menu = new Group(frm_servercertificates.getBody(), SWT.NONE);
		group_menu.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(group_menu);
		formToolkit.paintBordersFor(group_menu);
		group_menu.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button btn_add = new Button(group_menu, SWT.NONE);
		btn_add.setBounds(0, 0, 75, 25);
		formToolkit.adapt(btn_add, true, true);
		btn_add.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.add"));

		Button btn_remove = new Button(group_menu, SWT.NONE);
		btn_remove.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.delete"));
		formToolkit.adapt(btn_remove, true, true);

		Button btn_openLocation = new Button(group_menu, SWT.NONE);
		btn_openLocation.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.openlocation"));
		formToolkit.adapt(btn_openLocation, true, true);

		ListViewer listViewer = new ListViewer(frm_servercertificates.getBody(), SWT.BORDER | SWT.V_SCROLL);
		List list_certificates = listViewer.getList();
		list_certificates.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		fillControls();

		setHandler();

		computeSize();

		this.initState = false;

	}

	private void fillControls() {
		// java.util.List<ServerSecurityPolicy> policies =
		// ((OPCUAServerSecurityEditorInput) getEditorInput())
		// .getAppConfig().getSecurityPolicy();
		//
		// for (ServerSecurityPolicy policy : policies) {
		// this.listViewer.getList().add(policy.getSecurityMode()
		// .getMessageSecurityMode().name());
		// }
	}

	private void setHandler() {
		ExpansionAdapter adapter = new ExpansionAdapter() {

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
			}

		};
	}

	protected void computeSize() {
		Point minSize = computeSection();
		this.scrolledComposite.setMinSize(minSize);
		this.composite.layout(true);
	}

	private Point computeSection() {
		return this.composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	@Override
	public void setFocus() {
		this.scrolledComposite.setFocus();
	}
}
