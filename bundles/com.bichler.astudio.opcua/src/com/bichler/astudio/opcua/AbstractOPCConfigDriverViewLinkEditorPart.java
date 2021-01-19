package com.bichler.astudio.opcua;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.EditorPart;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.DeleteNodesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.log.ASLogActivator;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUANodeDialog;
import com.bichler.astudio.opcua.driver.IOPCDriverConfigEditPart;
import com.bichler.astudio.opcua.handlers.opcua.resource.DisposeOPCUAEditorsHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.wizard.util.OPCWizardUtil;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;

import opc.sdk.core.node.Node;

public abstract class AbstractOPCConfigDriverViewLinkEditorPart extends EditorPart implements IOPCDriverConfigEditPart {

	private Text txt_opcdrivermodel;
	private Button btn_setOPCDriverModel;
	private Button btn_removeOPCDriverModel;
	private CheckBoxButton cb_ActiveStartConfig;
	private Section sctnNewSection;

	public AbstractOPCConfigDriverViewLinkEditorPart() {
		super();
	}

	@Override
	public void setFocus() {
		onFocusRemoteView();
	}

	@Override
	public void dispose() {
		onDisposeRemoteView();

		super.dispose();
	}

	@Override
	public void onDisposeRemoteView() {
		IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
		try {
			handlerService.executeCommand(DisposeOPCUAEditorsHandler.ID, null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (NotDefinedException e) {
			e.printStackTrace();
		} catch (NotEnabledException e) {
			e.printStackTrace();
		} catch (NotHandledException e) {
			e.printStackTrace();
		}
	}

	protected void addHandler(final String drivername, final List<NodeId> driverstatusnodes) {
		this.txt_opcdrivermodel.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				e.doit = false;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				e.doit = false;
			}
		});

		this.btn_removeOPCDriverModel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO: REFRESH parent
				NodeId first = driverstatusnodes.get(0);

				try {
					deleteDriverModel(driverstatusnodes.toArray(new NodeId[0]));
					setDirty(true);
				} catch (ServiceResultException e1) {
					e1.printStackTrace();
				}

				driverstatusnodes.clear();
				txt_opcdrivermodel.setText("");

				ModelBrowserView opcBrowser = (ModelBrowserView) getSite().getWorkbenchWindow().getActivePage()
						.findView(ModelBrowserView.ID);
				opcBrowser.refresh();
				opcBrowser.setDirty(true);
			}

		});

		this.btn_setOPCDriverModel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				OPCUANodeDialog dialog = new OPCUANodeDialog(getSite().getShell());
				dialog.setInternalServer(ServerInstance.getInstance().getServerInstance());
				dialog.setStartId(Identifiers.RootFolder);

				NodeId data = (NodeId) txt_opcdrivermodel.getData();
				if (!NodeId.isNull(data)) {
					dialog.setSelectedNodeId(data);
				}

				dialog.setFormTitle(
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "dialog.nodes.browse"));

				if (dialog.open() == Dialog.OK) {
					// delete changed nodes
					List<DeleteNodesItem> nodes2delete = new ArrayList<>();
					if (!NodeId.isNull(data)) {

						if (driverstatusnodes.contains(data)) {
							return;
						}

						for (NodeId driverstatus : driverstatusnodes) {
							DeleteNodesItem node2delete = new DeleteNodesItem();
							node2delete.setDeleteTargetReferences(true);
							node2delete.setNodeId(driverstatus);
							nodes2delete.add(node2delete);
						}
					}

					try {
						ServerInstance.getInstance().getServerInstance().getMaster()
								.deleteNodes(nodes2delete.toArray(new DeleteNodesItem[0]), null);
					} catch (ServiceResultException sre) {
						sre.printStackTrace();
					}

					NodeId rootId = dialog.getSelectedNodeId();
					// OPCUADriverEditorInput ei = getEditorInput();
					try {
						NodeId startId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.toNodeId(OPCWizardUtil.newOPCUADriverModel(
										PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), rootId,
										drivername, 1));

						Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
								.getNodeById(startId);

						Map<String, NodeId> nodeMapping = new LinkedHashMap<>();
						nodeMapping.put(node.getBrowseName().getName(), startId);
						createOPCUADriverModelStructureMapping(startId, nodeMapping);

						driverstatusnodes.clear();
						driverstatusnodes.addAll(nodeMapping.values());

						txt_opcdrivermodel.setData(startId);
						txt_opcdrivermodel.setText(node.getBrowseName().getName());

						ModelBrowserView opcBrowser = (ModelBrowserView) getSite().getWorkbenchWindow().getActivePage()
								.findView(ModelBrowserView.ID);
						opcBrowser.refresh(rootId);
						opcBrowser.setDirty(true);

						setDirty(true);
						// }
					} catch (ServiceResultException sre) {
						sre.printStackTrace();
					}
				}
			}
		});
	}

	protected void createOPCDriverModelSection(Composite parent) {
		final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

		this.sctnNewSection = formToolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
		sctnNewSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.drv.opcmodel.title"));
		sctnNewSection.setExpanded(true);

		Composite sectionParent = formToolkit.createComposite(sctnNewSection, SWT.NONE);
		formToolkit.paintBordersFor(sectionParent);
		sectionParent.setLayout(new GridLayout(4, false));

		sctnNewSection.setClient(sectionParent);

		this.cb_ActiveStartConfig = new CheckBoxButton(sctnNewSection, SWT.NONE);
		formToolkit.adapt(cb_ActiveStartConfig, true, true);
		sctnNewSection.setTextClient(cb_ActiveStartConfig);
		cb_ActiveStartConfig
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"));
		cb_ActiveStartConfig.setAlignment(SWT.LEFT);
		cb_ActiveStartConfig.setBackground(new Color(Display.getDefault(), 235, 235, 235));
		cb_ActiveStartConfig.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isChecked = ((CheckBoxButton) e.getSource()).isChecked();
				sctnNewSection.setExpanded(isChecked);
				setDirty(true);
			}

		});

		Label lblDstartnodeid = new Label(sectionParent, SWT.NONE);
		lblDstartnodeid.setBounds(0, 0, 55, 15);
		formToolkit.adapt(lblDstartnodeid, true, true);
		lblDstartnodeid.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.nodeid"));

		this.txt_opcdrivermodel = new Text(sectionParent, SWT.BORDER);
		txt_opcdrivermodel.setText("");
		txt_opcdrivermodel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txt_opcdrivermodel, true, true);

		this.btn_setOPCDriverModel = new Button(sectionParent, SWT.NONE);
		formToolkit.adapt(btn_setOPCDriverModel, true, true);
		btn_setOPCDriverModel.setImage(StudioImageActivator.getImage(StudioImages.ICON_ADD));

		this.btn_removeOPCDriverModel = new Button(sectionParent, SWT.NONE);
		formToolkit.adapt(btn_removeOPCDriverModel, true, true);
		btn_removeOPCDriverModel.setImage(StudioImageActivator.getImage(StudioImages.ICON_DELETE));
	}

	protected void fillDriverStatus(boolean driverstatusflag, List<NodeId> driverstatusnodes) {

		this.cb_ActiveStartConfig.setChecked(driverstatusflag);
		this.sctnNewSection.setExpanded(driverstatusflag);

		if (!driverstatusnodes.isEmpty()) {
			NodeId rootId = driverstatusnodes.get(0);
			Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(rootId);
			if (node != null) {
				txt_opcdrivermodel.setText(node.getBrowseName().getName());
				txt_opcdrivermodel.setData(rootId);
			} else {
				ASLogActivator.getDefault().getLogger().log(Level.WARNING,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "driver.info.nomodel"));
			}
		}
	}

	public boolean isDriverStatusModel() {
		return this.cb_ActiveStartConfig.isChecked();
	}

	private void createOPCUADriverModelStructureMapping(NodeId nodeId, Map<String, NodeId> nodeMapping)
			throws ServiceResultException {

		BrowseDescription description = new BrowseDescription();
		description.setBrowseDirection(BrowseDirection.Forward);
		description.setIncludeSubtypes(true);
		description.setNodeClassMask(NodeClass.getMask(NodeClass.ALL));
		description.setNodeId(nodeId);
		description.setReferenceTypeId(Identifiers.HierarchicalReferences);
		description.setResultMask(BrowseResultMask.getMask(BrowseResultMask.ALL));

		BrowseResult[] browseResults = ServerInstance.getInstance().getServerInstance().getMaster()
				.browse(new BrowseDescription[] { description }, UnsignedInteger.ZERO, null, null);
		if (browseResults != null && browseResults.length > 0) {
			for (ReferenceDescription referenceDescription : browseResults[0].getReferences()) {
				NodeId id = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
						.toNodeId(referenceDescription.getNodeId());
				nodeMapping.put(referenceDescription.getBrowseName().getName(), id);
				createOPCUADriverModelStructureMapping(id, nodeMapping);
			}
		}
	}

	void deleteDriverModel(NodeId[] nodeids) throws ServiceResultException {
		List<DeleteNodesItem> nodes2delete = new ArrayList<>();

		for (NodeId node2delete : nodeids) {
			Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(node2delete);
			if (node == null) {
				continue;
			}

			DeleteNodesItem dni = new DeleteNodesItem();
			dni.setDeleteTargetReferences(true);
			dni.setNodeId(node2delete);
			nodes2delete.add(dni);
		}

		ServerInstance.getInstance().getServerInstance().getMaster()
				.deleteNodes(nodes2delete.toArray(new DeleteNodesItem[0]), null);
	}
}
