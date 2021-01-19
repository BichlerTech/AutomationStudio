package com.bichler.astudio.opcua.editor.security;

import java.util.ArrayList;

import opc.sdk.core.application.ApplicationConfiguration;
import opc.sdk.core.application.ServerSecurityPolicy;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.core.UserTokenType;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.transport.security.SecurityPolicyUri;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.editor.input.OPCUAServerSecurityEditorInput;
import com.bichler.astudio.opcua.editor.server.ServerConfigUtil;
import com.bichler.astudio.opcua.IOPCPerspectiveEditor;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.ValidationToolkit;
import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;
import org.eclipse.swt.widgets.Text;

public class OPCUASecurityConfigEditor extends EditorPart implements IOPCPerspectiveEditor {

	public static final String ID = "com.bichler.astudio.opcua.editor.opcuasecuirty";

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	private Composite composite;

	private ScrolledComposite scrolledComposite;

	private Section sctnConfiguration;
	private Section sctnSecurityType;
	private Section sctnSecurityPolicies;

	// private Combo combo_securitytype;
	// private Combo combo_securitypolicy;
	// private Combo combo_messageSecurityMode;

	private boolean initState;

	private ListViewer lv_securitytype;

	private ListViewer lv_endpoint;

	private boolean dirty = false;

	private ValidatingField<String> securityTypeCombo;
	private ValidatingField<String> securityMessageCombo;
	private ValidatingField<String> securityPolicyCombo;

	private Button btn_addSecurityPolicy;
	private Button btn_addSecurityType;
	private Button btn_deleteSecurityPolicy;
	private Button btn_deleteSecurityType;

	private CheckBoxButton cb_useCertificatestore;
	private Text txt_certificatestorePath;

	private Button btn_certificatestoreFile;

	public OPCUASecurityConfigEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		ApplicationConfiguration appConfig = ((OPCUAServerSecurityEditorInput) getEditorInput()).getAppConfig();
		preDoSave(appConfig);

		ServerConfigUtil.doSaveServerConfiguration(
				((OPCUAServerSecurityEditorInput) getEditorInput()).getNode().getFilesystem(), appConfig);

		setDirty(false);
	}

	private void preDoSave(ApplicationConfiguration appConfig) {
		java.util.List<ServerSecurityPolicy> app_securityPolicy = appConfig.getSecurityPolicy();
		java.util.List<UserTokenPolicy> app_userToken = appConfig.getUserTokenPolicies();

		java.util.List<ServerSecurityPolicy> newPolicies = (java.util.List<ServerSecurityPolicy>) lv_endpoint
				.getInput();
		java.util.List<UserTokenPolicy> newTokens = filterSecurityTypes(
				(java.util.List<UserTokenPolicy>) lv_securitytype.getInput());

		boolean useCertStore = this.cb_useCertificatestore.isChecked();

		String certStore = this.txt_certificatestorePath.getText();
		appConfig.getSecurityConfiguration().getApplicationCertificate().setStorePath(certStore);
		appConfig.getServerConfiguration().setUseServerCertificateStore(useCertStore);
		appConfig.getServerConfiguration().setSecurityPolicies(newPolicies);
		appConfig.getServerConfiguration().setUserTokenPolicies(newTokens);
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		this.setSite(site);
		this.setInput(input);
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isDirty() {
		return this.dirty;
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
		composite.setLayout(new GridLayout(1, false));

		createSectionConfiguration();

		createSectionSecurityPolicy();

		createSectionSecurityType();

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		fillControls();

		setHandler();

		validateControls();

		computeSize();

		this.initState = false;

	}

	private void createSectionConfiguration() {
		this.sctnConfiguration = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnConfiguration);
		sctnConfiguration.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.configuration"));
		sctnConfiguration.setExpanded(true);

		Composite composite_2 = formToolkit.createComposite(sctnConfiguration, SWT.NONE);
		formToolkit.paintBordersFor(composite_2);
		sctnConfiguration.setClient(composite_2);
		composite_2.setLayout(new GridLayout(3, false));

		this.cb_useCertificatestore = new CheckBoxButton(composite_2, SWT.CHECK);
		cb_useCertificatestore.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		formToolkit.adapt(cb_useCertificatestore, true, true);
		cb_useCertificatestore.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.usecertstore"));

		Label lbl_certificatestorePath = new Label(composite_2, SWT.NONE);
		lbl_certificatestorePath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lbl_certificatestorePath, true, true);
		lbl_certificatestorePath.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.certstorepath"));

		this.txt_certificatestorePath = new Text(composite_2, SWT.BORDER);
		txt_certificatestorePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txt_certificatestorePath, true, true);

		this.btn_certificatestoreFile = new Button(composite_2, SWT.NONE);
		this.btn_certificatestoreFile.setToolTipText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.view.browse"));
		this.btn_certificatestoreFile.setImage(CommonImagesActivator.getDefault()
				.getRegisteredImage(CommonImagesActivator.IMG_16, CommonImagesActivator.LOG));
		formToolkit.adapt(txt_certificatestorePath, true, true);
	}

	protected void computeSize() {
		Point minSize = computeSection();
		this.scrolledComposite.setMinSize(minSize);
		this.composite.layout(true);
	}

	private void addSecurityPolicy() {
		String policy = securityPolicyCombo.getContents();
		String msg = securityMessageCombo.getContents();

		java.util.List<ServerSecurityPolicy> policies = (java.util.List<ServerSecurityPolicy>) lv_endpoint.getInput();

		SecurityPolicy securityPolicy = null;
		try {
			securityPolicy = SecurityPolicy.getSecurityPolicy(policy);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		MessageSecurityMode messageSecurityMode = MessageSecurityMode.valueOf(msg);
		SecurityMode securityMode = new SecurityMode(securityPolicy, messageSecurityMode);
		ServerSecurityPolicy newPolicy = new ServerSecurityPolicy(securityMode, "0");
		policies.add(newPolicy);
		lv_endpoint.refresh();

		securityPolicyCombo.validate();
		securityMessageCombo.validate();

		setDirty(true);
	}

	private void addSecurityType() {
		String value = securityTypeCombo.getContents();
		UserTokenType newTokenType = UserTokenType.valueOf(value);

		java.util.List<UserTokenPolicy> tokens = (java.util.List<UserTokenPolicy>) lv_securitytype.getInput();

		UserTokenPolicy token = null;

		switch (newTokenType) {
		case Anonymous:
			token = UserTokenPolicy.ANONYMOUS;
			break;
		case UserName:
			token = UserTokenPolicy.SECURE_USERNAME_PASSWORD;
			break;
		case Certificate:
			token = UserTokenPolicy.SECURE_CERTIFICATE;
			break;
		case IssuedToken:
			token = UserTokenPolicy.SECURE_CERTIFICATE;
			break;
//		case Kerberos:
//			token = UserTokenPolicy.SECURE_CERTIFICATE;
//			break;
		}
		tokens.add(token);
		lv_securitytype.refresh();
		securityTypeCombo.validate();

		setDirty(true);
	}

	private Point computeSection() {
		this.sctnSecurityPolicies.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		return this.composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	private void createSectionSecurityPolicy() {
		this.sctnSecurityPolicies = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnSecurityPolicies.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnSecurityPolicies);
		sctnSecurityPolicies.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.securitypolicies"));
		sctnSecurityPolicies.setExpanded(true);

		Composite composite_1 = formToolkit.createComposite(sctnSecurityPolicies, SWT.NONE);
		formToolkit.paintBordersFor(composite_1);
		sctnSecurityPolicies.setClient(composite_1);
		composite_1.setLayout(new GridLayout(3, false));

		Group group = new Group(composite_1, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		formToolkit.adapt(group);
		formToolkit.paintBordersFor(group);

		Label lblSecurityPolicy = new Label(group, SWT.NONE);
		formToolkit.adapt(lblSecurityPolicy, true, true);
		lblSecurityPolicy.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.securitypolicy"));

		// combo_securitypolicy = new Combo(group, SWT.BORDER);
		// combo_securitypolicy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
		// true, false, 1, 1));
		// formToolkit.adapt(combo_securitypolicy, true, true);

		createValidationComboSecurityPolicy(group);

		Label lbl_messageSecurityMode = new Label(group, SWT.NONE);
		formToolkit.adapt(lbl_messageSecurityMode, true, true);
		lbl_messageSecurityMode.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.messagesecurity"));

		// this.combo_messageSecurityMode = new Combo(group, SWT.BORDER);
		// combo_messageSecurityMode.setLayoutData(new GridData(SWT.FILL,
		// SWT.CENTER, true, false, 1, 1));
		// formToolkit.adapt(combo_messageSecurityMode, true, true);

		createValidationComboMessageSecurity(group);

		Group group_addRemove = new Group(composite_1, SWT.NONE);
		group_addRemove.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 2));
		formToolkit.paintBordersFor(group_addRemove);
		GridLayout gl_group_addRemove = new GridLayout(1, false);
		gl_group_addRemove.marginHeight = 0;
		group_addRemove.setLayout(gl_group_addRemove);
		this.btn_addSecurityPolicy = formToolkit.createButton(group_addRemove, "", SWT.NONE);
		this.btn_addSecurityPolicy
				.setToolTipText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.add"));
		this.btn_addSecurityPolicy.setImage(CommonImagesActivator.getDefault()
				.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.ADD));

		GridData gd_btn_addSecurityPolicy = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
		gd_btn_addSecurityPolicy.widthHint = 48;
		gd_btn_addSecurityPolicy.heightHint = 48;
		btn_addSecurityPolicy.setLayoutData(gd_btn_addSecurityPolicy);
		this.btn_deleteSecurityPolicy = formToolkit.createButton(group_addRemove, "", SWT.NONE);
		this.btn_deleteSecurityPolicy.setToolTipText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.delete"));
		this.btn_deleteSecurityPolicy.setImage(CommonImagesActivator.getDefault()
				.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.DELETE));

		GridData gd_btn_deleteSecurityPolicy = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
		gd_btn_deleteSecurityPolicy.heightHint = 48;
		gd_btn_deleteSecurityPolicy.widthHint = 48;
		btn_deleteSecurityPolicy.setLayoutData(gd_btn_deleteSecurityPolicy);

		Label lblEndpoint = new Label(composite_1, SWT.NONE);
		lblEndpoint.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		formToolkit.adapt(lblEndpoint, true, true);
		lblEndpoint
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.endpoint"));

		this.lv_endpoint = new ListViewer(composite_1, SWT.BORDER | SWT.V_SCROLL);
		this.lv_endpoint.setContentProvider(new EndpointContentProvider());
		this.lv_endpoint.setLabelProvider(new EndpointLabelProvider());
		this.lv_endpoint.setComparator(new ViewerComparator() {

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {

				SecurityMode m1 = ((ServerSecurityPolicy) e1).getSecurityMode();
				SecurityMode m2 = ((ServerSecurityPolicy) e2).getSecurityMode();

				int uri1 = comparePolicyUri(m1.getSecurityPolicy().getPolicyUri());
				int uri2 = comparePolicyUri(m2.getSecurityPolicy().getPolicyUri());

				// same uri check mode
				if (uri1 == uri2) {
					MessageSecurityMode msg1 = m1.getMessageSecurityMode();
					MessageSecurityMode msg2 = m2.getMessageSecurityMode();
					return msg1.compareTo(msg2);
				}

				else if (uri1 > uri2) {
					return 1;
				} else {
					return -1;
				}

				// return super.compare(viewer, e1, e2);
			}

			private int comparePolicyUri(String uri) {
				int i = 0;

				switch (uri) {
				case SecurityPolicyUri.URI_BINARY_NONE:
					i = 0;
					break;
				case SecurityPolicyUri.URI_BINARY_BASIC128RSA15:
					i = 2;
					break;
				case SecurityPolicyUri.URI_BINARY_BASIC256:
					i = 3;
					break;
				case SecurityPolicyUri.URI_BINARY_BASIC256SHA256:
					i = 4;
					break;
				}

				return i;
			}

		});

		List list_securityPolicy = lv_endpoint.getList();
		GridData gd_list_securityPolicy = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_list_securityPolicy.heightHint = 100;
		list_securityPolicy.setLayoutData(gd_list_securityPolicy);
	}

	private void createSectionSecurityType() {

		this.sctnSecurityType = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnSecurityType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnSecurityType);
		sctnSecurityType.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.securitytype"));
		sctnSecurityType.setExpanded(true);

		Composite cmp_securityType = new Composite(sctnSecurityType, SWT.NONE);
		formToolkit.adapt(cmp_securityType);
		formToolkit.paintBordersFor(cmp_securityType);
		sctnSecurityType.setClient(cmp_securityType);
		cmp_securityType.setLayout(new GridLayout(3, false));

		Group group_securitytype = new Group(cmp_securityType, SWT.NONE);
		group_securitytype.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		formToolkit.adapt(group_securitytype);
		formToolkit.paintBordersFor(group_securitytype);
		group_securitytype.setLayout(new GridLayout(2, false));

		Label lblSecurityLevel = new Label(group_securitytype, SWT.NONE);
		formToolkit.adapt(lblSecurityLevel, true, true);
		lblSecurityLevel.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.securitytype"));

		createValidationComboSecurityType(group_securitytype);
		new Label(group_securitytype, SWT.NONE);

		// combo_securitytype = new Combo(group_securitytype, SWT.BORDER);
		// combo_securitytype.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
		// true, false, 1, 1));
		// formToolkit.adapt(combo_securitytype, true, true);

		Group group_addRemove = new Group(cmp_securityType, SWT.NONE);
		group_addRemove.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 2));
		formToolkit.paintBordersFor(group_addRemove);
		GridLayout gl_group_addRemove = new GridLayout(1, false);
		gl_group_addRemove.marginHeight = 0;
		group_addRemove.setLayout(gl_group_addRemove);

		this.btn_addSecurityType = formToolkit.createButton(group_addRemove, "", SWT.NONE);
		this.btn_addSecurityType
				.setToolTipText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.add"));
		this.btn_addSecurityType.setImage(CommonImagesActivator.getDefault()
				.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.ADD));
		GridData gd_btn_addSecurityType = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
		gd_btn_addSecurityType.widthHint = 48;
		gd_btn_addSecurityType.heightHint = 48;
		btn_addSecurityType.setLayoutData(gd_btn_addSecurityType);

		this.btn_deleteSecurityType = formToolkit.createButton(group_addRemove, "", SWT.NONE);
		this.btn_deleteSecurityType.setToolTipText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.delete"));
		this.btn_deleteSecurityType.setImage(CommonImagesActivator.getDefault()
				.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.DELETE));
		GridData gd_btn_deleteSecurityType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btn_deleteSecurityType.heightHint = 48;
		gd_btn_deleteSecurityType.widthHint = 48;
		btn_deleteSecurityType.setLayoutData(gd_btn_deleteSecurityType);

		Label lblIdentityToken = new Label(cmp_securityType, SWT.NONE);
		lblIdentityToken.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		formToolkit.adapt(lblIdentityToken, true, true);
		lblIdentityToken.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.identitytoken"));

		this.lv_securitytype = new ListViewer(cmp_securityType, SWT.BORDER | SWT.V_SCROLL);
		this.lv_securitytype.setContentProvider(new SecurityTypeContentProvider());
		this.lv_securitytype.setComparator(new ViewerComparator() {

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				int type1 = compareTokenType((UserTokenPolicy) e1);
				int type2 = compareTokenType((UserTokenPolicy) e2);

				if (type1 == type2) {
					return 0;
				} else if (type1 < type2) {
					return -1;
				} else {
					return 1;
				}

				// return super.compare(viewer, e1, e2);
			}

			private int compareTokenType(UserTokenPolicy type) {
				int i = 0;

				switch (type.getTokenType()) {
				case Anonymous:
					i = 0;
					break;
				case UserName:
					i = 1;
					break;
				case Certificate:
					i = 2;
					break;
				case IssuedToken:
					i = 3;
					break;
//				case Kerberos:
//					i = 4;
//					break;
				}

				return i;
			}

		});

		this.lv_securitytype.setLabelProvider(new SecurityTypeLabelProvider());
		List list_securityType = lv_securitytype.getList();
		GridData gd_list_securityType = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_list_securityType.heightHint = 100;
		gd_list_securityType.minimumHeight = 200;
		list_securityType.setLayoutData(gd_list_securityType);
	}

	private void createValidationComboMessageSecurity(Composite parent) {
		// fill security type
		String[] messageSecrity = new String[MessageSecurityMode.values().length - 1];

		for (int i = 1; i < MessageSecurityMode.values().length; i++) {
			messageSecrity[i - 1] = (MessageSecurityMode.valueOf(i).name());
		}

		// validationFramework
		ValidationToolkit<String> uri = new ValidationToolkit<String>(new IContentsStringConverter<String>() {

			@Override
			public String convertFromString(String value) {
				return value;
			}

			@Override
			public String convertToString(String value) {
				return value;
			}

		});

		IFieldValidator<String> fieldValidator = new IFieldValidator<String>() {

			@Override
			public String getErrorMessage() {
				return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.error.messageexist");
			}

			@Override
			public String getWarningMessage() {
				return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.error.messageexist");
			}

			@Override
			public boolean isValid(String value) {
				return validateMessageSecurity(value);
			}

			@Override
			public boolean warningExist(String value) {
				return false;
			}

		};
		// create swt
		this.securityMessageCombo = uri.createCComboField(parent, fieldValidator, false,
				MessageSecurityMode.values()[1].name(), messageSecrity);
		this.securityMessageCombo.setErrorMessageHandler(new IFieldErrorMessageHandler() {

			boolean validate = false;

			@Override
			public void handleWarningMessage(String arg0, String arg1) {

			}

			@Override
			public void handleErrorMessage(String arg0, String value) {
				if (btn_addSecurityPolicy == null) {
					return;
				}
				btn_addSecurityPolicy.setEnabled(false);

				if (!this.validate) {
					this.validate = true;
					securityPolicyCombo.validate();
					this.validate = false;
				}
			}

			@Override
			public void clearMessage() {
				// enables button OK
				if (btn_addSecurityPolicy == null) {
					return;
				}
				btn_addSecurityPolicy.setEnabled(true);

				if (!this.validate) {
					this.validate = true;
					securityPolicyCombo.validate();
					this.validate = false;
				}
			}
		});

		this.securityMessageCombo.getControl().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(this.securityMessageCombo.getControl(), true, true);
	}

	private void createValidationComboSecurityPolicy(Composite parent) {
		// fill security type
		String[] securityPolicies = new String[] { SecurityPolicyUri.URI_BINARY_NONE,
				SecurityPolicyUri.URI_BINARY_BASIC128RSA15, SecurityPolicyUri.URI_BINARY_BASIC256,
				SecurityPolicyUri.URI_BINARY_BASIC256SHA256 };

		// validationFramework
		// .createCombo(parent, UserTokenType.Anonymous.name(),
		// securityTypes);
		ValidationToolkit<String> uri = new ValidationToolkit<String>(new IContentsStringConverter<String>() {

			@Override
			public String convertFromString(String value) {
				return value;
			}

			@Override
			public String convertToString(String value) {
				return value;
			}

		});

		IFieldValidator<String> fieldValidator = new IFieldValidator<String>() {

			@Override
			public String getErrorMessage() {
				return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
						"editor.error.securityexist");
			}

			@Override
			public String getWarningMessage() {
				return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
						"editor.error.securityexist");
			}

			@Override
			public boolean isValid(String value) {
				return validateSecurityPolicyUri(value);
			}

			@Override
			public boolean warningExist(String value) {
				return false;
			}

		};
		// create swt
		this.securityPolicyCombo = uri.createCComboField(parent, fieldValidator, false,
				SecurityPolicyUri.URI_BINARY_NONE, securityPolicies);
		this.securityPolicyCombo.setErrorMessageHandler(new IFieldErrorMessageHandler() {

			private boolean validate = false;

			@Override
			public void handleWarningMessage(String arg0, String arg1) {

			}

			@Override
			public void handleErrorMessage(String message, String value) {
				if (btn_addSecurityPolicy == null) {
					return;
				}
				btn_addSecurityPolicy.setEnabled(false);

				if (!this.validate) {
					this.validate = true;
					securityMessageCombo.validate();
					this.validate = false;
				}
			}

			@Override
			public void clearMessage() {
				if (btn_addSecurityPolicy == null) {
					return;
				}
				btn_addSecurityPolicy.setEnabled(true);

				if (!this.validate) {
					this.validate = true;
					securityMessageCombo.validate();
					this.validate = false;
				}
			}
		});

		this.securityPolicyCombo.getControl().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(this.securityPolicyCombo.getControl(), true, true);
	}

	private void createValidationComboSecurityType(Composite parent) {
		// fill security type
		String[] securityTypes = new String[] { UserTokenType.Anonymous.name(), UserTokenType.UserName.name(),
				UserTokenType.Certificate.name() };

		// validationFramework
		// .createCombo(parent, UserTokenType.Anonymous.name(),
		// securityTypes);
		ValidationToolkit<String> uri = new ValidationToolkit<String>(new IContentsStringConverter<String>() {

			@Override
			public String convertFromString(String value) {
				return value;
			}

			@Override
			public String convertToString(String value) {
				return value;
			}

		});

		IFieldValidator<String> fieldValidator = new IFieldValidator<String>() {

			@Override
			public String getErrorMessage() {
				return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
						"editor.error.securitytypeexist");
			}

			@Override
			public String getWarningMessage() {
				return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
						"editor.error.securitytypeexist");
			}

			@Override
			public boolean isValid(String value) {
				return validateSecurityType(value);
			}

			@Override
			public boolean warningExist(String value) {
				return false;
			}

		};
		// create swt
		this.securityTypeCombo = uri.createCComboField(parent, fieldValidator, false, UserTokenType.Anonymous.name(),
				securityTypes);
		this.securityTypeCombo.setErrorMessageHandler(new IFieldErrorMessageHandler() {

			@Override
			public void handleWarningMessage(String arg0, String arg1) {

			}

			@Override
			public void handleErrorMessage(String arg0, String value) {
				if (btn_addSecurityType == null) {
					return;
				}
				btn_addSecurityType.setEnabled(false);
			}

			@Override
			public void clearMessage() {
				if (btn_addSecurityType == null) {
					return;
				}
				btn_addSecurityType.setEnabled(true);
			}
		});

		this.securityTypeCombo.getControl().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(this.securityTypeCombo.getControl(), true, true);
	}

	private void fillControls() {
		// fill security policy
		// this.combo_securitypolicy.add(SecurityPolicy.URI_BINARY_NONE);
		// this.combo_securitypolicy.add(SecurityPolicy.URI_BINARY_BASIC128RSA15);
		// this.combo_securitypolicy.add(SecurityPolicy.URI_BINARY_BASIC256);
		// this.combo_securitypolicy.add(SecurityPolicy.URI_BINARY_BASIC256SHA256);
		// this.combo_securitypolicy.select(0);

		// fill message security mode TODO: REPLACED
		// for (int i = 1; i < MessageSecurityMode.values().length; i++) {
		// this.combo_messageSecurityMode.add(MessageSecurityMode.valueOf(i)
		// .name());
		// }
		// this.combo_messageSecurityMode.select(0);

		// fill security type TODO: REPLACED
		// this.combo_securitytype.add(UserTokenType.Anonymous.name());
		// this.combo_securitytype.add(UserTokenType.UserName.name());
		// this.combo_securitytype.add(UserTokenType.Certificate.name());
		// this.combo_securitytype.select(0);

		ApplicationConfiguration applicationConfiguration = ((OPCUAServerSecurityEditorInput) getEditorInput())
				.getAppConfig();

		java.util.List<ServerSecurityPolicy> secpoli = applicationConfiguration.getSecurityPolicy();
		java.util.List<UserTokenPolicy> userToken = applicationConfiguration.getUserTokenPolicies();

		boolean useCertStore = applicationConfiguration.getServerConfiguration().isUseServerCertificateStore();
		this.cb_useCertificatestore.setChecked(useCertStore);

		String path = applicationConfiguration.getApplicationCertificateStorePath();
		this.txt_certificatestorePath.setText(path);

		// fill endpoint list
		this.lv_endpoint.setInput(new ArrayList<ServerSecurityPolicy>(secpoli));

		// fill security type list
		this.lv_securitytype.setInput(new ArrayList<UserTokenPolicy>(userToken));
	}

	private void removeSecurityPolicy() {
		java.util.List<ServerSecurityPolicy> policies = (java.util.List<ServerSecurityPolicy>) lv_endpoint.getInput();

		ServerSecurityPolicy policy = (ServerSecurityPolicy) ((IStructuredSelection) lv_endpoint.getSelection())
				.getFirstElement();

		policies.remove(policy);
		lv_endpoint.refresh();

		securityPolicyCombo.validate();
		securityMessageCombo.validate();

		setDirty(true);
	}

	/**
	 * TODO: filterEndpoints
	 * 
	 * @param tokens
	 * @return
	 */
	private java.util.List<UserTokenPolicy> filterSecurityTypes(java.util.List<UserTokenPolicy> tokens) {
		// filter elements to display
		java.util.List<UserTokenPolicy> elements = new ArrayList<>();
		for (UserTokenPolicy policy : tokens) {
			boolean insert = false;
			for (UserTokenPolicy element : elements) {
				if (element.equals(policy)) {
					insert = true;
					break;
				}
			}
			if (insert) {
				continue;
			}
			elements.add(policy);
		}

		return elements;
	}

	private void removeSecurityType() {
		java.util.List<UserTokenPolicy> tokens = (java.util.List<UserTokenPolicy>) lv_securitytype.getInput();

		UserTokenPolicy token = (UserTokenPolicy) ((IStructuredSelection) lv_securitytype.getSelection())
				.getFirstElement();

		tokens.remove(token);
		lv_securitytype.refresh();

		securityTypeCombo.validate();

		setDirty(true);
	}

	private void setHandler() {
		ExpansionAdapter adapter = new ExpansionAdapter() {

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				computeSize();
			}

		};

		this.sctnSecurityPolicies.addExpansionListener(adapter);
		this.sctnSecurityType.addExpansionListener(adapter);

		this.btn_addSecurityPolicy.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addSecurityPolicy();
			}

		});

		this.btn_addSecurityType.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addSecurityType();
			}

		});

		this.btn_deleteSecurityPolicy.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				removeSecurityPolicy();
			}

		});

		this.btn_deleteSecurityType.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				removeSecurityType();
			}

		});

		this.cb_useCertificatestore.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// boolean selection = ((Button) e.getSource()).getSelection();
				//
				// ApplicationConfiguration applicationConfiguration =
				// ((OPCUAServerSecurityEditorInput) getEditorInput())
				// .getAppConfig();
				//
				// if (applicationConfiguration.getServerConfiguration()
				// .isUseServerCertificateStore() != selection) {
				//
				// }
				setDirty(true);
			}

		});

		this.txt_certificatestorePath.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				setDirty(true);
			}
		});

		this.txt_certificatestorePath.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
			}

		});

		this.btn_deleteSecurityPolicy.setEnabled(false);
		this.lv_endpoint.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				btn_deleteSecurityPolicy.setEnabled(true);
			}
		});

		this.btn_deleteSecurityType.setEnabled(false);
		this.lv_securitytype.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				btn_deleteSecurityType.setEnabled(true);

			}
		});
	}

	private void validateControls() {
		this.securityTypeCombo.validate();
		this.securityPolicyCombo.validate();
		this.securityMessageCombo.validate();
	}

	private boolean validateMessageSecurity(String value) {
		if (securityPolicyCombo == null) {
			return true;
		}
		if (lv_endpoint == null) {
			return true;
		}
		java.util.List<ServerSecurityPolicy> policies = (java.util.List<ServerSecurityPolicy>) lv_endpoint.getInput();
		String policy = securityPolicyCombo.getContents();

		if (policies == null) {
			return true;
		}

		// validate
		boolean isValid = true;
		for (ServerSecurityPolicy p : policies) {
			SecurityMode securityMode = p.getSecurityMode();
			SecurityPolicy securityPolicy = securityMode.getSecurityPolicy();
			String pUri = securityPolicy.getPolicyUri();

			if (!pUri.equals(policy)) {
				continue;
			}

			MessageSecurityMode securityMessage = securityMode.getMessageSecurityMode();
			if (securityMessage.name().equals(value)) {
				isValid = false;
				break;
			}
		}

		return isValid;
	}

	private boolean validateSecurityPolicyUri(String value) {
		if (securityMessageCombo == null) {
			return true;
		}
		if (lv_endpoint == null) {
			return true;
		}
		java.util.List<ServerSecurityPolicy> policies = (java.util.List<ServerSecurityPolicy>) lv_endpoint.getInput();
		String messageSecurity = securityMessageCombo.getContents();

		if (policies == null) {
			return true;
		}
		// security mode none allows only message security none
		CCombo combo = (CCombo) securityMessageCombo.getControl();
		if (SecurityPolicy.NONE.getPolicyUri().equals(value)) {
			combo.setItems(new String[] { MessageSecurityMode.None.name() });
			securityMessageCombo.setContents(MessageSecurityMode.None.name());
			// combo.setEnabled(false);
		} else {
			if (securityMessageCombo.getContents().equals(MessageSecurityMode.None.name())) {
				combo.setItems(
						new String[] { MessageSecurityMode.Sign.name(), MessageSecurityMode.SignAndEncrypt.name() });
				securityMessageCombo.setContents(MessageSecurityMode.Sign.name());
			}

			// securityMessageCombo.setContents(MessageSecurityMode.None.name());
			// combo.setEnabled(true);
		}

		// validate
		boolean isValid = true;
		for (ServerSecurityPolicy p : policies) {
			SecurityMode securityMode = p.getSecurityMode();
			SecurityPolicy securityPolicy = securityMode.getSecurityPolicy();
			String pUri = securityPolicy.getPolicyUri();

			if (!pUri.equals(value)) {
				continue;
			}

			MessageSecurityMode securityMessage = securityMode.getMessageSecurityMode();
			if (securityMessage.name().equals(messageSecurity)) {
				isValid = false;
				break;
			}
		}

		return isValid;
	}

	private boolean validateSecurityType(String value) {
		if (lv_securitytype == null) {
			return true;
		}

		Object input_sc = lv_securitytype.getInput();
		if (input_sc == null) {
			return true;
		}

		java.util.List<UserTokenPolicy> policies = (java.util.List<UserTokenPolicy>) input_sc;
		if (policies.size() == 0) {
			return true;
		}

		UserTokenType newToken = UserTokenType.valueOf(value);

		boolean isValid = true;
		for (UserTokenPolicy policy : policies) {
			UserTokenType tokenType = policy.getTokenType();
			if (newToken == tokenType) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	@Override
	public void setFocus() {
		this.scrolledComposite.setFocus();
	}

	class EndpointContentProvider extends ArrayContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			java.util.List<ServerSecurityPolicy> elements = new ArrayList<>();
			for (ServerSecurityPolicy policy : (java.util.List<ServerSecurityPolicy>) inputElement) {
				boolean insert = false;
				for (ServerSecurityPolicy element : elements) {
					if (element.equals(policy)) {
						insert = true;
						break;
					}
				}
				if (insert) {
					continue;
				}
				elements.add(policy);
			}

			return elements.toArray(new ServerSecurityPolicy[0]);
		}
	}

	class EndpointLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof ServerSecurityPolicy) {
				SecurityMode mode = ((ServerSecurityPolicy) element).getSecurityMode();
				return mode.getSecurityPolicy() + " - " + mode.getMessageSecurityMode();
			}
			return super.getText(element);
		}

	}

	class SecurityTypeContentProvider extends ArrayContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			java.util.List<UserTokenPolicy> origin = (java.util.List<UserTokenPolicy>) inputElement;
			java.util.List<UserTokenPolicy> elements = filterSecurityTypes(
					(java.util.List<UserTokenPolicy>) inputElement);

			// something changed
			// if (origin.size() != elements.size()) {
			// setDirty(true);
			// }

			return elements.toArray(new UserTokenPolicy[0]);
		}

	}

	class SecurityTypeLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			if (element instanceof UserTokenPolicy) {
				UserTokenType token = ((UserTokenPolicy) element).getTokenType();
				return token.name();
			}

			return super.getText(element);
		}
	}
}
