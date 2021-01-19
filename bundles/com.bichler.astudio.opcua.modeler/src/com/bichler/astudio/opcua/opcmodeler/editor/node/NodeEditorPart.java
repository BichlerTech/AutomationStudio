package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wb.swt.ResourceManager;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.images.opcua.OPCImagesActivator;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.extern.UpdateExternNodeIdHandler;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.MCPreferenceStoreUtil;
import com.bichler.astudio.opcua.opcmodeler.editor.node.language.ListenerDesciptionLanguage;
import com.bichler.astudio.opcua.opcmodeler.editor.node.language.ListenerDisplaynameLanguage;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.NodeIdFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.UInt32FieldValidator;
import com.bichler.astudio.opcua.opcmodeler.preferences.ShowDialogPreferencePage;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.nodeid.NodeIdWizard;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;

public abstract class NodeEditorPart extends EditorPart {
	private static final String DATA_CURRENT_TYPE = "currentnodeidtype";
	protected Map<String, String> usedNodeIds = null;
	private boolean dirty = false;
	protected final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	protected DesignerFormToolkit controllCreationToolkit = null;
	private UInt32FieldValidator uint32Validator = null;
	private NodeIdFieldValidator<String> nodeidvalidator = null;
	protected Section sctn_general = null;
	protected Form frm_mainForm = null;
	protected Label lbl_browseName = null;
	protected Text txt_browseNameIndex = null;
	protected ControlDecoration cd_browseNameIndex = null;
	protected Text txt_browseName = null;
	protected ControlDecoration cd_browseName = null;
	protected Label lbl_description = null;
	protected CometCombo cmb_descriptionLocale = null;
	protected Text txt_description = null;
	protected ControlDecoration cd_description = null;
	protected Label lbl_displayName = null;
	protected CometCombo cmb_displayNameLocale = null;
	protected Text txt_displayName = null;
	protected ControlDecoration cd_displayName = null;
	protected Label lbl_nodeId = null;
	protected CometCombo cmb_nodeId = null;
	protected ControlDecoration cd_nodeIdIndex = null;
	protected Text txt_nodeId = null;
	protected ControlDecoration cd_nodeId = null;
	protected Section sctn_extended = null;
	protected Label lbl_writeMask = null;
	protected Text txt_writeMask = null;
	protected Label lbl_userWriteMask = null;
	protected Text txt_userWriteMask = null;
	protected Section sctn_references = null;
	protected TableViewer tv_references = null;
	protected Table tb_references = null;
	protected Composite cpt_edit = null;
	private Button btn_refAdd = null;
	private Button btn_refDelete = null;
	private Composite cmpt_extended = null;
	private Composite comp_general = null;
	private Button btn_refEdit = null;
	private boolean isModifyNodeIdText = false;
	private ScrolledComposite scrolledCompositeRoot;
	
	private Button btn_localDescription;
	private Button btn_localDisplayname;
	protected Image img_book;
	protected Composite compositeContainer;
	
	public NodeEditorPart() {
		super();
		this.uint32Validator = new UInt32FieldValidator();
		this.controllCreationToolkit = new DesignerFormToolkit();
		this.usedNodeIds = new HashMap<>();
		this.img_book = OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
				OPCImagesActivator.BOOK);
	}

	protected void doSaveFinish() {
		Node node = getEditorInput().getNode().getNode();
		NodeId nodeId = createNodeId();
		NodeId oldId = node.getNodeId();
		// NodeId newId = nodeId;
		// change nodeId
		boolean hasChanged = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
				.changeNodeId(ServerInstance.getInstance().getServerInstance().getNamespaceUris(), node, nodeId);
		// update used nodeids
		if (hasChanged) {
			this.usedNodeIds.put(this.cmb_nodeId.getText(), nodeId.getValue().toString());
		}
		// Node Attributes modelchange!
		// update monitor view after saving
		DesignerUtils.updateBrowserNode(getEditorInput().getNode());
		DesignerUtils.refreshEditors();
		this.controllCreationToolkit.log(Status.OK,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.save.edit.ok"),
				getEditorInput().getNode().getNode());
		setDirty(false);
		// update nodeid in other plugins
		if (hasChanged) {
			ModelBrowserView opcuaModel = (ModelBrowserView) getSite().getWorkbenchWindow().getActivePage()
					.findView(ModelBrowserView.ID);
			opcuaModel.doSave();
			// command service
			IHandlerService handlerService = getSite().getService(IHandlerService.class);
			ICommandService cmdService = getSite().getService(ICommandService.class);
			Command command = cmdService.getCommand(UpdateExternNodeIdHandler.ID);
			// updates nodeid event
			OPCUAUpdateNodeIdEvent evt = new OPCUAUpdateNodeIdEvent();
			evt.setOldId(oldId);
			evt.setNewId(nodeId);
			// send event
			ExecutionEvent event = handlerService.createExecutionEvent(command, null);
			IEvaluationContext evalCtx = (IEvaluationContext) event.getApplicationContext();
			evalCtx.getParent().addVariable(UpdateExternNodeIdHandler.PARAMETER_ID, evt);
			try {
				command.executeWithChecks(event);
			} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
	}

	/**
	 * Saves the base node attributes
	 * 
	 * @param node
	 */
	public void doSave(IProgressMonitor monitor) {
		Node node = getEditorInput().getNode().getNode();
		QualifiedName browseName = createBrowseName();
		LocalizedText description = createDescription(node);
		LocalizedText displayName = createDisplayName(node);
		ReferenceNode[] references = createReferenceNodes(node);
		UnsignedInteger userWriteMask = createUserWriteMask();
		UnsignedInteger writeMask = createWriteMask();
		NodeId nodeId = createNodeId();
		NodeId oldId = node.getNodeId();
		// NodeId newId = nodeId;
		boolean hasChanged = !oldId.equals(nodeId);
		boolean confirm = true;
		if (hasChanged) {
			confirm = MessageDialog.openConfirm(getSite().getShell(),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.refactor"),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.closed"));
			if (!confirm) {
				monitor.setCanceled(true);
				return;
			}
		}
		// change node attributes
		node.setBrowseName(browseName);
		node.setDescription(description);
		node.setDisplayName(displayName);
		node.setReferences(references);
		node.setUserWriteMask(userWriteMask);
		node.setWriteMask(writeMask);
		// save localization
		doSaveLocalization(node.getNodeId());
		this.setPartName(displayName.getText());
	}

	private void doSaveLocalization(NodeId nodeId) {
		// create new instance
		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command cmdExportLanguage = commandService.getCommand("com.xcontrol.modeler.opc.extern.exportlanguage");
		try {
			IParameter params = cmdExportLanguage.getParameter("com.xcontrol.modeler.opc.extern.exportlang.files");
			List<Parameterization> parameterizations = new ArrayList<Parameterization>();
			String infoResource = Studio_ResourceManager.getInfoModellerResource();
			if (infoResource != null) {
				IPath path = new Path(infoResource).removeLastSegments(1);
				IPath local = path.append("localization").append("international.csv");
				parameterizations.add(new Parameterization(params, local.toOSString()));
				ParameterizedCommand pCmd = new ParameterizedCommand(cmdExportLanguage,
						parameterizations.toArray(new Parameterization[0]));
				handlerService.executeCommand(pCmd, null);
			}
		} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * Constructs a browsename attribute.
	 * 
	 * @return browsename
	 */
	private QualifiedName createBrowseName() {
		// get the index from the textbox browsename
		int namespaceIndex = Integer.parseInt(this.txt_browseNameIndex.getText());
		// get the text from the textbox browsename
		String name = this.txt_browseName.getText();
		// constructs a new browsename from the controls
		QualifiedName browseName = new QualifiedName(namespaceIndex, name);
		return browseName;
	}

	/**
	 * Constructs a description attribute.
	 * 
	 * @param node
	 * 
	 * @return description
	 */
	private LocalizedText createDescription(Node node) {
		String text = this.txt_description.getText();
		Locale locale = new Locale(this.cmb_descriptionLocale.getText());
		LocalizedText description = new LocalizedText(text, locale);
		return description;
	}

	/**
	 * Constructs a displayname attribute.
	 * 
	 * @return displayname
	 */
	private LocalizedText createDisplayName(Node node) {
		String text = this.txt_displayName.getText();
		Locale locale = new Locale(this.cmb_displayNameLocale.getText());
		LocalizedText displayname = new LocalizedText(text, locale);
		return displayname;
	}

	/**
	 * Constructs a nodeid attribute.
	 * 
	 * @param node
	 * 
	 * @return nodeid
	 */
	protected NodeId createNodeId() {
		return (NodeId) this.txt_nodeId.getData();
	}

	/**
	 * Constructs a reference attribute. TODO: CHANGEREFERENCES
	 * 
	 * @param node
	 * 
	 * @return references
	 */
	private ReferenceNode[] createReferenceNodes(Node node) {
		return node.getReferences();
	}

	/**
	 * Constructs an userwritemask attribute.
	 * 
	 * @return userwritemask
	 */
	private UnsignedInteger createUserWriteMask() {
		return new UnsignedInteger(this.txt_userWriteMask.getText());
	}

	/**
	 * Constructs a writemask attribute.
	 * 
	 * @return writemask
	 */
	private UnsignedInteger createWriteMask() {
		return new UnsignedInteger(this.txt_writeMask.getText());
	}

	@Override
	public void doSaveAs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		// if dirty, mark informationmodel to save in preference store
		if (this.dirty) {
			MCPreferenceStoreUtil.setHasInformationModelChanged(getSite().getShell(), getEditorInput());
		}
		return this.dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		this.scrolledCompositeRoot = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		this.scrolledCompositeRoot.setLayout(new FillLayout(SWT.HORIZONTAL));
		formToolkit.adapt(scrolledCompositeRoot);
		formToolkit.paintBordersFor(scrolledCompositeRoot);
		scrolledCompositeRoot.setExpandHorizontal(true);
		scrolledCompositeRoot.setExpandVertical(true);
		this.frm_mainForm = formToolkit.createForm(scrolledCompositeRoot);
		formToolkit.paintBordersFor(frm_mainForm);
		frm_mainForm.setText("New Form");
		frm_mainForm.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));
		formToolkit.decorateFormHeading(frm_mainForm);
		scrolledCompositeRoot.setContent(frm_mainForm);
		this.compositeContainer = frm_mainForm.getBody();
		formToolkit.adapt(compositeContainer);
		formToolkit.paintBordersFor(compositeContainer);
		compositeContainer.setLayout(new GridLayout(1, false));
		createGeneralSection();
		createExtendedSection();
		
		createReferenceSection();
		this.createExtensionSection();
		// Analog zu ViewParts sind in createPartControl die UI-Inhalte des
		// Editors zu erzeugen
		// title of the editor is the displayname of the node
		setPartName(getEditorInput().getName());
		// creates the editor content for the node
		this.createExtendedSection(cmpt_extended);

		this.setInputs();
		isModifyNodeIdText = false;
		this.setHandlers();
		computeScroll();
	}


	
	private void createReferenceSection() {
		sctn_references = formToolkit.createSection(this.compositeContainer, Section.TWISTIE | Section.TITLE_BAR);
		formToolkit.paintBordersFor(sctn_references);
		sctn_references.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"CreateVariableDialog.lbl_references.text"));
		sctn_references.setExpanded(true);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(false, false).applyTo(sctn_references);
		Composite composite_3 = new Composite(sctn_references, SWT.NONE);
		sctn_references.setClient(composite_3);
		formToolkit.adapt(composite_3);
		formToolkit.paintBordersFor(composite_3);
		composite_3.setLayout(new GridLayout(2, false));
//		Label lbl_references = new Label(composite_3, SWT.NONE);
//		GridData gd_lbl_references = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//		gd_lbl_references.widthHint = 120;
//		lbl_references.setLayoutData(gd_lbl_references);
//		lbl_references.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
//				"CreateVariableDialog.lbl_references.text")); //$NON-NLS-1$
//		formToolkit.adapt(lbl_references, true, true);
		
		cpt_edit = new Composite(composite_3, SWT.NONE);
		cpt_edit.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		formToolkit.adapt(cpt_edit);
		formToolkit.paintBordersFor(cpt_edit);
		cpt_edit.setLayout(new GridLayout(1, false));
		Composite composite_5 = new Composite(cpt_edit, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		formToolkit.adapt(composite_5);
		formToolkit.paintBordersFor(composite_5);
		composite_5.setLayout(new GridLayout(1, false));
		btn_refAdd = new Button(composite_5, SWT.NONE);
		btn_refAdd.setToolTipText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.btn_add.toolTipText")); //$NON-NLS-1$
		GridData gd_btn_add = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add.widthHint = 60;
		gd_btn_add.heightHint = 40;
		btn_refAdd.setLayoutData(gd_btn_add);
		btn_refAdd.setText(""); //$NON-NLS-1$
		btn_refAdd.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/add_32.png"));
		formToolkit.adapt(btn_refAdd, true, true);
		btn_refEdit = new Button(composite_5, SWT.NONE);
		btn_refEdit.setToolTipText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.btn_edit.toolTipText"));
		GridData gd_btn_edit = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_edit.widthHint = 60;
		gd_btn_edit.heightHint = 40;
		btn_refEdit.setLayoutData(gd_btn_edit);
		btn_refEdit.setText(""); //$NON-NLS-1$
		btn_refEdit.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/edit_32.png"));
		formToolkit.adapt(btn_refEdit, true, true);
		btn_refDelete = new Button(composite_5, SWT.NONE);
		btn_refDelete.setToolTipText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorPart.btn_delete.toolTipText"));
		GridData gd_btn_delete = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_delete.widthHint = 60;
		gd_btn_delete.heightHint = 40;
		btn_refDelete.setLayoutData(gd_btn_delete);
		btn_refDelete.setText(""); //$NON-NLS-1$
		btn_refDelete
				.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/delete_32.png"));
		formToolkit.adapt(btn_refDelete, true, true);
		
		tv_references = new TableViewer(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		tb_references = tv_references.getTable();
		tb_references.setHeaderVisible(true);
		GridData tvgd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
		tvgd.heightHint = 175;
		tb_references.setLayoutData(tvgd);
		formToolkit.paintBordersFor(tb_references);
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tv_references, SWT.NONE);
		TableColumn tblclmn_reference = tableViewerColumn.getColumn();
		tblclmn_reference.setWidth(150);
		tblclmn_reference.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorPart.tblclmn_reference.text")); //$NON-NLS-1$
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tv_references, SWT.NONE);
		TableColumn tblclmn_isinverse = tableViewerColumn_1.getColumn();
		tblclmn_isinverse.setWidth(100);
		tblclmn_isinverse.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorPart.tblclmn_isinverse.text")); //$NON-NLS-1$
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tv_references, SWT.NONE);
		TableColumn tblclmn_target = tableViewerColumn_2.getColumn();
		tblclmn_target.setWidth(200);
		tblclmn_target.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.tblclmn_target.text")); //$NON-NLS-1$
		
		boolean isAdmin = UserUtils.testUserRights(1);
		if(!isAdmin) {
			btn_refAdd.setEnabled(false);
			btn_refDelete.setEnabled(false);
			btn_refEdit.setEnabled(false);
		}
	}

	private void createExtendedSection() {
		sctn_extended = formToolkit.createSection(this.compositeContainer, Section.TWISTIE | Section.TITLE_BAR);
		formToolkit.paintBordersFor(sctn_extended);
		sctn_extended.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.sctn_extended.text")); //$NON-NLS-1$
		sctn_extended.setExpanded(true);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(false, false).applyTo(sctn_extended);
		cmpt_extended = new Composite(sctn_extended, SWT.NONE);
		formToolkit.adapt(cmpt_extended);
		formToolkit.paintBordersFor(cmpt_extended);
		sctn_extended.setClient(cmpt_extended);
		GridLayout gl_composite_2 = new GridLayout(2, false);
		gl_composite_2.verticalSpacing = 3;
		gl_composite_2.horizontalSpacing = 10;
		cmpt_extended.setLayout(gl_composite_2);
	}

	private void createGeneralSection() {
		sctn_general = formToolkit.createSection(this.compositeContainer, Section.TWISTIE | Section.TITLE_BAR);
		formToolkit.paintBordersFor(sctn_general);
		sctn_general.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.sctn_general.text"));
		sctn_general.setExpanded(true);
		comp_general = new Composite(sctn_general, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(false, false).applyTo(sctn_general);
		formToolkit.adapt(comp_general);
		formToolkit.paintBordersFor(comp_general);
		sctn_general.setClient(comp_general);
		GridLayout gl_composite_1 = new GridLayout(4, false);
		gl_composite_1.marginTop = 2;
		gl_composite_1.marginHeight = 3;
		gl_composite_1.verticalSpacing = 1;
		gl_composite_1.horizontalSpacing = 10;
		comp_general.setLayout(gl_composite_1);
		lbl_browseName = new Label(comp_general, SWT.NONE);
		GridData gd_lbl_browseName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_browseName.widthHint = 120;
		lbl_browseName.setLayoutData(gd_lbl_browseName);
		lbl_browseName.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_browseName.text"));
		formToolkit.adapt(lbl_browseName, true, true);
		txt_browseNameIndex = new Text(comp_general, SWT.BORDER);
		GridData gd_txt_browseNameIndex = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txt_browseNameIndex.widthHint = 50;
		txt_browseNameIndex.setLayoutData(gd_txt_browseNameIndex);
		txt_browseNameIndex.setTouchEnabled(true);
		txt_browseNameIndex.setText("0");
		formToolkit.adapt(txt_browseNameIndex, true, true);
		cd_browseNameIndex = new ControlDecoration(txt_browseNameIndex, SWT.RIGHT | SWT.TOP);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(txt_browseNameIndex, "browsenameindex");
		cd_browseNameIndex.setDescriptionText("OPC Browsename attribute");
		txt_browseName = new Text(comp_general, SWT.BORDER);
		GridData gd_txt_browseName = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_txt_browseName.widthHint = 200;
		txt_browseName.setLayoutData(gd_txt_browseName);
		formToolkit.adapt(txt_browseName, true, true);
		cd_browseName = new ControlDecoration(txt_browseName, SWT.RIGHT | SWT.TOP);
		cd_browseName.setDescriptionText("Some description");
		lbl_description = new Label(comp_general, SWT.NONE);
		GridData gd_lbl_description = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_description.widthHint = 120;
		lbl_description.setLayoutData(gd_lbl_description);
		lbl_description.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_description.text"));
		formToolkit.adapt(lbl_description, true, true);
		cmb_descriptionLocale = new CometCombo(comp_general, SWT.READ_ONLY);
		cmb_descriptionLocale.setTouchEnabled(true);
		GridData gd_cmb_descriptionLocale = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_cmb_descriptionLocale.widthHint = 50;
		cmb_descriptionLocale.setLayoutData(gd_cmb_descriptionLocale);
		formToolkit.adapt(cmb_descriptionLocale);
		formToolkit.paintBordersFor(cmb_descriptionLocale);
		txt_description = new Text(comp_general, SWT.BORDER);
		GridData gd_txt_description = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txt_description.widthHint = 200;
		txt_description.setLayoutData(gd_txt_description);
		formToolkit.adapt(txt_description, true, true);
		cd_description = new ControlDecoration(txt_description, SWT.RIGHT | SWT.TOP);
		cd_description.setDescriptionText("Some description");
		this.btn_localDescription = new Button(comp_general, SWT.PUSH);
		GridData gd_btn_description = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btn_description.widthHint = 25;
		btn_localDescription.setLayoutData(gd_btn_description);
		formToolkit.adapt(btn_localDescription, true, true);
		btn_localDescription.setImage(img_book);
		lbl_displayName = new Label(comp_general, SWT.NONE);
		GridData gd_lbl_displayName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_displayName.widthHint = 120;
		lbl_displayName.setLayoutData(gd_lbl_displayName);
		lbl_displayName.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_displayName.text"));
		formToolkit.adapt(lbl_displayName, true, true);
		cmb_displayNameLocale = new CometCombo(comp_general, SWT.READ_ONLY);
		cmb_displayNameLocale.setTouchEnabled(true);
		GridData gd_cmb_displayNameLocale = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_cmb_displayNameLocale.widthHint = 50;
		cmb_displayNameLocale.setLayoutData(gd_cmb_displayNameLocale);
		formToolkit.adapt(cmb_displayNameLocale);
		formToolkit.paintBordersFor(cmb_displayNameLocale);
		txt_displayName = new Text(comp_general, SWT.BORDER);
		GridData gd_txt_displayName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txt_displayName.widthHint = 200;
		txt_displayName.setLayoutData(gd_txt_displayName);
		formToolkit.adapt(txt_displayName, true, true);
		this.btn_localDisplayname = new Button(comp_general, SWT.PUSH);
		GridData gd_btn_displayname = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btn_displayname.widthHint = 25;
		btn_localDisplayname.setLayoutData(gd_btn_displayname);
		formToolkit.adapt(btn_localDisplayname, true, true);
		btn_localDisplayname.setImage(img_book);
		cd_displayName = new ControlDecoration(txt_displayName, SWT.RIGHT | SWT.TOP);
		cd_displayName.setDescriptionText("Some description");
		lbl_nodeId = new Label(comp_general, SWT.NONE);
		GridData gd_lbl_nodeId = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_nodeId.widthHint = 120;
		lbl_nodeId.setLayoutData(gd_lbl_nodeId);
		lbl_nodeId.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_nodeId.text"));
		formToolkit.adapt(lbl_nodeId, true, true);
		cmb_nodeId = new CometCombo(comp_general, SWT.NONE);
		cmb_nodeId.setTouchEnabled(true);
		GridData gd_cmb_nodeId = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_cmb_nodeId.widthHint = 250;
		cmb_nodeId.setLayoutData(gd_cmb_nodeId);
		formToolkit.adapt(cmb_nodeId);
		formToolkit.paintBordersFor(cmb_nodeId);
		cd_nodeIdIndex = new ControlDecoration(cmb_nodeId, SWT.RIGHT | SWT.TOP);
		cd_nodeIdIndex.setDescriptionText("Some description");
		txt_nodeId = new Text(comp_general, SWT.BORDER);
		GridData gd_txt_nodeId = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_txt_nodeId.widthHint = 200;
		txt_nodeId.setLayoutData(gd_txt_nodeId);
		formToolkit.adapt(txt_nodeId, true, true);
		cd_nodeId = new ControlDecoration(txt_nodeId, SWT.RIGHT | SWT.TOP);
		cd_nodeId.setDescriptionText("Some description");
		btn_localDescription
				.addSelectionListener(new ListenerDesciptionLanguage(this.txt_nodeId, this.txt_description, this));
		btn_localDisplayname
				.addSelectionListener(new ListenerDisplaynameLanguage(this.txt_nodeId, this.txt_displayName, this));
	}

	protected abstract void createExtendedSection(Composite extended);
	protected abstract void createExtensionSection();
	
	protected void setInputs() {
		Node node = this.getEditorInput().getNode().getNode();
		// is edit internal elements allowed?
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		if (node.getNodeId().getNamespaceIndex() == 0) {
			this.comp_general.setEnabled(false);
			this.sctn_extended.setEnabled(false);
			cpt_edit.setEnabled(false);
		}
		if (preferenceStore.getBoolean(ShowDialogPreferencePage.PREFERENCE_OPCUA_EDIT_INTERNAL)) {
			this.sctn_extended.setEnabled(true);
			cpt_edit.setEnabled(true);
		}
		preDisableWidgets();
		String browseName = "";
		String browseNameIndex = "";
		if (node.getBrowseName() != null) {
			browseName = node.getBrowseName().getName();
			browseNameIndex = "" + node.getBrowseName().getNamespaceIndex();
		}
		String descriptionLocaleId = "";
		String description = "";
		if (node.getDescription() != null) {
			descriptionLocaleId = node.getDescription().getLocaleId();
			description = node.getDescription().getText();
		}
		String displaynameLocaleId = "";
		String displayname = "";
		if (node.getDisplayName() != null) {
			displaynameLocaleId = node.getDisplayName().getLocaleId();
			displayname = node.getDisplayName().getText();
		}
		String writeMask = node.getWriteMask().toString();
		String userWriteMask = node.getUserWriteMask().toString();
		browseName = browseName == null ? "" : browseName;
		browseNameIndex = browseNameIndex == null ? "" : browseNameIndex;
		description = description == null ? "" : description;
		descriptionLocaleId = descriptionLocaleId == null ? Locale.ENGLISH.toString() : descriptionLocaleId;
		displayname = displayname == null ? "" : displayname;
		displaynameLocaleId = displaynameLocaleId == null ? Locale.ENGLISH.toString() : displaynameLocaleId;
		writeMask = writeMask == null ? "0" : writeMask;
		userWriteMask = userWriteMask == null ? "0" : userWriteMask;
		this.txt_browseNameIndex.setText(browseNameIndex);
		this.txt_browseName.setText(browseName);
		this.controllCreationToolkit.setComboLocale(cmb_descriptionLocale, null);
		this.cmb_descriptionLocale.setText(descriptionLocaleId);
		this.txt_description.setText(description);
		this.txt_displayName.setText(displayname);
		this.controllCreationToolkit.setComboLocale(cmb_displayNameLocale, null);
		this.cmb_displayNameLocale.setText(displaynameLocaleId);
		this.controllCreationToolkit.setNamespaceCombobox(this.cmb_nodeId);
		this.cmb_nodeId.setToolTipText(this.cmb_nodeId.getText());
		setNodeIdInputs(node.getNodeId());
		this.txt_writeMask.setText(writeMask);
		this.txt_userWriteMask.setText(userWriteMask);
		this.controllCreationToolkit.setDataTypeReferenceTable(tv_references, node);
	}

	public void setNodeIdInputs(NodeId nodeId) {
		String nodeIdValue = null;
		switch (nodeId.getIdType()) {
		case String:
		case Guid:
		case Numeric:
			nodeIdValue = nodeId.getValue() == null ? "" : nodeId.getValue().toString();
			break;
		case Opaque:
			nodeIdValue = ((byte[]) nodeId.getValue() == null) ? "" : Arrays.toString((byte[]) nodeId.getValue());
			break;
		}
		setChangeNodeId();
		this.cmb_nodeId.select(nodeId.getNamespaceIndex());
		this.txt_nodeId.setData(nodeId);
		this.txt_nodeId.setText(nodeIdValue);
	}

	class ScrolledExpansionAdapter extends ExpansionAdapter {
		@Override
		public void expansionStateChanged(ExpansionEvent e) {
			computeScroll();
		}
	};

	void computeScroll() {
		// this.sctn_references.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p = this.frm_mainForm.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.scrolledCompositeRoot.setMinSize(p);
		this.compositeContainer.getParent().layout(true);
	}

	protected abstract void preDisableWidgets();

	protected void setHandlers() {
		this.controllCreationToolkit.setTextUInt32Validation(cd_browseNameIndex, uint32Validator,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "BrowseNameIndexValidator.error"),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "BrowseNameIndexValidator.warning"),
				this);
		this.controllCreationToolkit.setTextStringValidation(cd_browseName,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "BrowseNameValidator.error"),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "BrowseNameValidator.warning"), this);
		this.controllCreationToolkit.setTextStringValidation(cd_description,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "DescriptionValidator.error"),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "DescriptionValidator.warning"), this);
		this.controllCreationToolkit.setTextStringValidation(cd_displayName,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "DisplayNameValidator.error"),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "DisplayNameValidator.warning"), this);
		this.controllCreationToolkit.setDirtyListener(lbl_description, cmb_descriptionLocale, this);
		this.controllCreationToolkit.setDirtyListener(lbl_displayName, cmb_displayNameLocale, this);
		this.nodeidvalidator = new NodeIdFieldValidator<String>(cmb_nodeId, (NodeId) this.txt_nodeId.getData());
		this.nodeidvalidator.setTextfield(this.txt_nodeId);
		this.sctn_general.addExpansionListener(new ScrolledExpansionAdapter());
		this.sctn_extended.addExpansionListener(new ScrolledExpansionAdapter());
		this.sctn_references.addExpansionListener(new ScrolledExpansionAdapter());
		this.controllCreationToolkit.setTextNodeId(cd_nodeId, lbl_nodeId, cmb_nodeId, this.nodeidvalidator, this);
		this.controllCreationToolkit.setWriteMask(lbl_writeMask, txt_writeMask, this);
		this.controllCreationToolkit.setWriteMask(lbl_userWriteMask, txt_userWriteMask, this);
		this.controllCreationToolkit.setReferenceDBClick(tv_references, this);
		this.controllCreationToolkit.setAddReferenceButton(btn_refAdd, tv_references, this);
		this.controllCreationToolkit.setEditReferenceButton(btn_refEdit, tv_references, this);
		this.controllCreationToolkit.setDeleteReferenceButton(btn_refDelete, tv_references, this);
		this.controllCreationToolkit.setReferenceTableSelection(tv_references, btn_refDelete, btn_refEdit);
		
		ControlDecoration decorator = new ControlDecoration(this.txt_nodeId, SWT.RIGHT);
		decorator.setShowOnlyOnFocus(true);
		Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION)
				.getImage();
		decorator.setImage(image);
		decorator.setDescriptionText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.tooltip.dialog"));
		txt_nodeId.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				NodeIdWizard wizard = new NodeIdWizard(getEditorInput().getNode().getNode(),
						(NodeId) txt_nodeId.getData());
				WizardDialog dialog = new WizardDialog(getEditorSite().getShell(), wizard);
				if (Dialog.OK == dialog.open()) {
					NodeId newId = wizard.getNewNodeId();
					NodeId initId = wizard.getInitId();
					// nodeid has not changed
					if (initId.equals(newId)) {
						return;
					}
					setNodeIdInputs(newId);
				}
			}
		});
		this.cmb_nodeId.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				if (!isModifyNodeIdText) {
					e.doit = false;
				}
			}
		});
		this.txt_nodeId.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				if (!isModifyNodeIdText) {
					e.doit = false;
				}
				isModifyNodeIdText = false;
			}
		});
	}

	@Override
	public void setFocus() {
		// this.composite.setFocus();
		// this.contentProvider.refreshReferenceTable();
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		ModelBrowserView view = (ModelBrowserView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ModelBrowserView.ID);
		if (view != null) {
			view.setDirty(true);
		}
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public NodeEditorInput getEditorInput() {
		return (NodeEditorInput) super.getEditorInput();
	}

	protected boolean valid() {
		if (this.cd_browseNameIndex.isVisible()) {
			this.controllCreationToolkit
					.log(Status.ERROR,
							CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "SaveProblem.error") + " "
									+ this.cd_browseNameIndex.getDescriptionText(),
							getEditorInput().getNode().getNode());
			return false;
		}
		if (this.cd_nodeId.isVisible()) {
			this.controllCreationToolkit.log(Status.ERROR,
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "SaveProblem.error") + " "
							+ this.cd_nodeId.getDescriptionText(),
					getEditorInput().getNode().getNode());
			return false;
		}
		return true;
	}

	protected void validate() {
	}

	public void refreshReferenceTable() {
		this.tv_references.setInput(getEditorInput().getNode().getNode().getReferences());
	}

	public void setChangeNodeId() {
		this.isModifyNodeIdText = true;
	}
}
