package com.bichler.astudio.opcua.widget;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
// import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.OPCUASharedImages;
import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUABrowseUtils;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUANodeDialog;
import com.bichler.astudio.opcua.dnd.OPCUADropInDeviceMappingTextNodeAdapter;
import com.bichler.astudio.opcua.dnd.OPCUADropInDeviceMappingViewAdapter;
import com.bichler.astudio.opcua.dnd.OPCUADropInDeviceStateViewAdapter;
import com.bichler.astudio.opcua.dnd.OPCUADropInDeviceconfigViewAdapter;
import com.bichler.astudio.opcua.dnd.OPCUADropInDevicegroupViewAdapter;
import com.bichler.astudio.opcua.dnd.OPCUADropInStartconfigViewAdapter;
import com.bichler.astudio.opcua.dnd.OPCUADropInVariableAdapter;
import com.bichler.astudio.opcua.driver.IOPCDriverConfigEditPart;
import com.bichler.astudio.opcua.editor.input.OPCUAAdvancedDriverEditorInput;
import com.bichler.astudio.opcua.handlers.events.AdvancedDriverPersister;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.model.AbstractConfigNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNodeParser;
import com.bichler.astudio.opcua.widget.model.AdvancedGroupNodeParser;
import com.bichler.astudio.opcua.widget.model.AdvancedRootConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedSectionType;
import com.bichler.astudio.opcua.widget.model.ConsumptionImportHandler;
import com.bichler.astudio.opcua.widget.model.DeviceConsumption;
import com.bichler.astudio.opcua.wizard.NewAdvancedCounterTreeItemWizard;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.UtilsUIActivator;
import com.bichler.astudio.utils.ui.images.SharedImages;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.utils.ui.swt.DoubleCellEditor;
import com.bichler.astudio.utils.ui.swt.IntegerCellEditor;
import com.bichler.astudio.utils.ui.swt.NumericText;

import opc.sdk.server.core.UAServerApplicationInstance;

public class AdvancedDriverConfigWidget implements ISelectionProvider {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Section sectionStartConfig;
	private Section sectionDeviceConfig;
	private Section sectionDeviceMapping;
	private Section sectionDeviceGroup;
	private Section sectionDeviceConsumption;
	private TableViewer tv_StartconfigNodes;
	private IOPCDriverConfigEditPart editor;
	private Text txt_devConfig;
	private TableViewer tv_devconfigNodes;
	private TreeViewer tv_devcounterNodes;
	private Button btn_devConfig;
	private UAServerApplicationInstance opcServer;
	private Composite parentComposite;
	/**
	 * Device mapping
	 */
	private TableViewer tv_devMappings;
	private Text txt_mapping1;
	private Text txt_mapping2;
	private Text txt_mapping3;
	private Text txt_mapping4;
	private Text txt_mapping5;
	private ComputeSectionsHandler computeSectionHandler;
	private CheckBoxButton cb_ActiveStartConfig;
	private CheckBoxButton cb_ActiveDeviceCounter;
	private CheckBoxButton cb_ActiveDeviceConfig;
	private CheckBoxButton cb_ActiveDeviceMapping;
	private CheckBoxButton cb_ActiveDeviceConsumption;
	/**
	 * current selection
	 */
	private AbstractConfigNode currentSelection = null;
	private Text txt_counterConfig;
	private Button btn_counterConfig;
	private String drvType;
	private CTabFolder tabFolder;
	ISelectionChangedListener listener = null;

	enum StateAttributes {
		Name, NodeId, Browsepath, Devicename, Datatype;
	}

	enum DirtyListenerProperties {
		config, db, start, addon, group, meterid;
	}

	public AdvancedDriverConfigWidget() {
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param editor
	 * @param opcServer
	 * @param string
	 * @param computeSectionsHandler
	 */
	public AdvancedDriverConfigWidget(Composite parent, int style, IOPCDriverConfigEditPart editor,
			UAServerApplicationInstance opcServer, String drvType, ComputeSectionsHandler computeSectionsHandler) {
		this.editor = editor;
		this.opcServer = opcServer;
		this.parentComposite = parent;
		this.computeSectionHandler = computeSectionsHandler;
		this.drvType = drvType;
		createAdvancedWidget(parent);
		fillControlls();
	}

	public void doSave(IFileSystem fs, String path) {
		NamespaceTable nsTable = opcServer.getServerInstance().getNamespaceUris();
		AdvancedDriverPersister exporter = new AdvancedDriverPersister();
		// save start config
		if (tv_StartconfigNodes != null) {
			AdvancedRootConfigurationNode input = (AdvancedRootConfigurationNode) tv_StartconfigNodes.getInput();
			exporter.exportStartConfig(fs, nsTable, path, input);
		}
		// save device config
		if (tv_devconfigNodes != null) {
			AdvancedRootConfigurationNode input = (AdvancedRootConfigurationNode) tv_devconfigNodes.getInput();
			exporter.exportDeviceConfig(fs, nsTable, path, input);
		}
		// save device mapping
		if (tv_devMappings != null) {
			AdvancedRootConfigurationNode input = (AdvancedRootConfigurationNode) tv_devMappings.getInput();
			exporter.exportDeviceMapping(fs, nsTable, path, input);
		}
		// save device counter
		if (tv_devcounterNodes != null) {
			AdvancedRootConfigurationNode input = (AdvancedRootConfigurationNode) tv_devcounterNodes.getInput();
			exporter.exportCounters(fs, nsTable, path, input);
		}
		{
			if (cb_ActiveDeviceConsumption != null) {
				String checked = "" + cb_ActiveDeviceConsumption.isChecked();
				@SuppressWarnings("unchecked")
				List<ConsumptionTemplate> data = (List<ConsumptionTemplate>) this.tabFolder.getData();
				exporter.exportConsumptionConfig(fs, nsTable, path, checked, data);
			}
		}
	}

	public Point computeSize() {
		if (this.sectionStartConfig != null) {
			this.sectionStartConfig.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		}
		if (this.sectionDeviceConfig != null) {
			this.sectionDeviceConfig.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		}
		if (this.sectionDeviceMapping != null) {
			this.sectionDeviceMapping.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		}
		if (this.sectionDeviceGroup != null) {
			this.sectionDeviceGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		}
		if (this.sectionDeviceConsumption != null) {
			this.sectionDeviceConsumption.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		}
		return this.parentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	public void setConfigNode(NodeId id, String name) {
		AdvancedRootConfigurationNode root = (AdvancedRootConfigurationNode) tv_devconfigNodes.getInput();
		if (root == null) {
			root = new AdvancedRootConfigurationNode(AdvancedSectionType.DeviceConfig);
			tv_devconfigNodes.setInput(root);
		}
		root.setRefNodeName(name);
		root.setRefNodeId(id);
		txt_devConfig.setText(name);
		txt_devConfig.setData(id);
		editor.setDirty(true);
	}

	public void setCounterConfigNode(NodeId id, String name) {
		AdvancedRootConfigurationNode root = (AdvancedRootConfigurationNode) tv_devcounterNodes.getInput();
		if (root == null) {
			root = new AdvancedRootConfigurationNode(AdvancedSectionType.Counter);
			tv_devcounterNodes.setInput(root);
		}
		root.setRefNodeName(name);
		root.setRefNodeId(id);
		txt_counterConfig.setText(name);
		txt_counterConfig.setData(id);
		editor.setDirty(true);
	}

	public void setExpansionListener(ExpansionAdapter adapter) {
		if (this.sectionStartConfig != null) {
			this.sectionStartConfig.addExpansionListener(adapter);
		}
		if (this.sectionDeviceConfig != null) {
			this.sectionDeviceConfig.addExpansionListener(adapter);
		}
		if (this.sectionDeviceGroup != null) {
			this.sectionDeviceGroup.addExpansionListener(adapter);
		}
		if (this.sectionDeviceMapping != null) {
			this.sectionDeviceMapping.addExpansionListener(adapter);
		}
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.listener = listener;
	}

	@Override
	public ISelection getSelection() {
		if (this.currentSelection != null) {
			return new StructuredSelection(this.currentSelection);
		}
		return StructuredSelection.EMPTY;
	}

	public void refresh() {
		fill();
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listener = null;
	}

	@Override
	public void setSelection(ISelection selection) {
		setSelectionToWidget();
	}

	protected void selectionChanged() {
		listener.selectionChanged(new SelectionChangedEvent(this, getSelection()));
	}

	void createStartConfig(Composite parent) {
		if ("advanced".equals(this.drvType)) {
			/**
			 * Section with a client composite
			 */
			this.sectionStartConfig = formToolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
			formToolkit.paintBordersFor(sectionStartConfig);
			sectionStartConfig.setText(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.startconfig"));
			sectionStartConfig.setExpanded(true);
			GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			sectionStartConfig.setLayoutData(gd_table_1);
			Composite sctnComposite = new Composite(sectionStartConfig, SWT.NONE);
			formToolkit.adapt(sctnComposite);
			formToolkit.paintBordersFor(sctnComposite);
			sectionStartConfig.setClient(sctnComposite);
			sctnComposite.setLayout(new GridLayout(3, false));
			Composite updown = new Composite(sctnComposite, SWT.NONE);
			updown.setLayout(new GridLayout(6, false));
			updown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
			/**
			 * Startconfig
			 */
			this.tv_StartconfigNodes = new TableViewer(sctnComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
			this.tv_StartconfigNodes.setContentProvider(new AdvancedTableContentProvider());
			/**
			 * Drop
			 */
			int operations = DND.DROP_COPY;
			Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
			this.tv_StartconfigNodes.addDropSupport(operations, transferTypes,
					new OPCUADropInStartconfigViewAdapter(this.tv_StartconfigNodes, this.editor));
			/**
			* 
			*/
			Table table_sc = tv_StartconfigNodes.getTable();
			table_sc.setHeaderVisible(true);
			table_sc.setLinesVisible(true);
			gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			gd_table_1.widthHint = 478;
			gd_table_1.heightHint = 150;
			table_sc.setLayoutData(gd_table_1);
			createStartConfigTable(tv_StartconfigNodes);
			table_sc.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					super.mouseDoubleClick(e);
					Point p = new Point(e.x, e.y);
					ViewerCell cell = tv_StartconfigNodes.getCell(p);
					int columnIndex = cell.getColumnIndex();
					switch (columnIndex) {
					case 0:
					case 1:
					case 2:
						AdvancedConfigurationNode element = (AdvancedConfigurationNode) cell.getElement();
						OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
						dialog.setInternalServer(opcServer.getServerInstance());
						switch (columnIndex) {
						case 1:
							dialog.setSelectedNodeId(element.getDeviceId());
							break;
						case 2:
							dialog.setSelectedNodeId(element.getConfigNodeId());
							break;
						}
						int ok = dialog.open();
						if (ok == Dialog.OK) {
							NodeId id = dialog.getSelectedNodeId();
							switch (columnIndex) {
							case 1:
								element.setDeviceId(id);
								element.setDeviceName(dialog.getSelectedDisplayName());
								break;
							case 2:
								element.setConfigId(id);
								element.setConfigNodeName(dialog.getSelectedDisplayName());
								break;
							}
							tv_StartconfigNodes.update(element, null);
							editor.setDirty(true);
						}
						break;
					}
				}
			});
			this.cb_ActiveStartConfig = new CheckBoxButton(sectionStartConfig, SWT.NONE);
			formToolkit.adapt(cb_ActiveStartConfig, true, true);
			sectionStartConfig.setTextClient(cb_ActiveStartConfig);
			cb_ActiveStartConfig
					.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"));
			cb_ActiveStartConfig.setAlignment(SWT.LEFT);
			this.cb_ActiveStartConfig.setBackground(new Color(Display.getDefault(), 235, 235, 235));

			cb_ActiveStartConfig.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdvancedRootConfigurationNode input = (AdvancedRootConfigurationNode) tv_StartconfigNodes
							.getInput();
					if (input == null) {
						input = new AdvancedRootConfigurationNode(AdvancedSectionType.StartConfig);
						tv_StartconfigNodes.setInput(input);
					}
					boolean isChecked = ((CheckBoxButton) e.getSource()).isChecked();
					input.setActive(isChecked);
					sectionStartConfig.setExpanded(isChecked);
					editor.setDirty(true);
					editor.computeSection();
				}
			});
			// up down button
			Button btn_up = new Button(updown, SWT.PUSH);
			GridData gd_btn_up = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gd_btn_up.heightHint = 48;
			gd_btn_up.widthHint = 48;
			btn_up.setLayoutData(gd_btn_up);
			// btn_up.setText("Up");
			btn_up.setImage(StudioImageActivator.getImage(StudioImages.ICON_ARROW_UP));
			// up down button
			Button btn_down = new Button(updown, SWT.PUSH);
			GridData gd_btn_down = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			gd_btn_down.heightHint = 48;
			gd_btn_down.widthHint = 48;
			btn_down.setLayoutData(gd_btn_down);
			// btn_down.setText("Down");
			btn_down.setImage(StudioImageActivator.getImage(StudioImages.ICON_ARROW_DOWN));
			Button btnAddStartConfig = new Button(updown, SWT.NONE);
			btnAddStartConfig.setText("");
			GridData gd_btn_scadd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_btn_scadd.heightHint = 48;
			gd_btn_scadd.widthHint = 48;
			btnAddStartConfig.setLayoutData(gd_btn_scadd);
			btnAddStartConfig.setImage(CommonImagesActivator.getDefault()
					.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.ADD));
			btnAddStartConfig.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdvancedRootConfigurationNode root = (AdvancedRootConfigurationNode) tv_StartconfigNodes.getInput();
					if (root == null) {
						root = new AdvancedRootConfigurationNode(AdvancedSectionType.StartConfig);
						tv_StartconfigNodes.setInput(root);
					}
					AdvancedConfigurationNode newChild = new AdvancedConfigurationNode(AdvancedSectionType.StartConfig);
					root.addChild(newChild);
					tv_StartconfigNodes.refresh(root);
					tv_StartconfigNodes.setSelection(new StructuredSelection(newChild));
					computeSectionHandler.compute();
					editor.setDirty(true);
				}
			});
			Button btnRemoveStartConfg = new Button(updown, SWT.NONE);
			btnRemoveStartConfg.setText("");
			GridData gd_btn_scdelete = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_btn_scdelete.heightHint = 48;
			gd_btn_scdelete.widthHint = 48;
			btnRemoveStartConfg.setLayoutData(gd_btn_scdelete);
			btnRemoveStartConfg.setImage(CommonImagesActivator.getDefault()
					.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.DELETE));
			btnRemoveStartConfg.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdvancedRootConfigurationNode root = (AdvancedRootConfigurationNode) tv_StartconfigNodes.getInput();
					Object[] selection = ((StructuredSelection) tv_StartconfigNodes.getSelection()).toArray();
					if (selection == null) {
						return;
					}
					for (Object node : selection) {
						root.removeChild((AdvancedConfigurationNode) node);
					}
					computeSectionHandler.compute();
					tv_StartconfigNodes.refresh(root);
					editor.setDirty(true);
				}
			});
		}
	}

	void createDeviceConfig(Composite parent) {
		if ("siemens".equals(this.drvType) || "allenbradley".equals(this.drvType) || "xml_da".equals(this.drvType)) {
			/**
			 * Section with a client composite
			 */
			this.sectionDeviceConfig = formToolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
			formToolkit.paintBordersFor(sectionDeviceConfig);
			sectionDeviceConfig.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"widget.advanced.deviceconfig"));
			sectionDeviceConfig.setExpanded(false);
			GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			sectionDeviceConfig.setLayoutData(gd_table_1);
			Composite sctnComposite = new Composite(sectionDeviceConfig, SWT.NONE);
			formToolkit.adapt(sctnComposite);
			formToolkit.paintBordersFor(sctnComposite);
			sectionDeviceConfig.setClient(sctnComposite);
			sctnComposite.setLayout(new GridLayout(3, false));
			// config node section
			Label lbl_devConfig = new Label(sctnComposite, SWT.NONE);
			GridData gd_lbl = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			lbl_devConfig.setLayoutData(gd_lbl);
			lbl_devConfig.setAlignment(SWT.RIGHT);
			lbl_devConfig.setText(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.confignode"));
			this.txt_devConfig = new Text(sctnComposite, SWT.BORDER | SWT.READ_ONLY);
			GridData gd_txt = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			txt_devConfig.setLayoutData(gd_txt);
			DropTarget dropTarget = new DropTarget(this.txt_devConfig, DND.DROP_COPY);
			dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
			dropTarget.addDropListener(new OPCUADropInVariableAdapter(this));
			// this.txt_devConfig.
			this.btn_devConfig = new Button(sctnComposite, SWT.PUSH);
			GridData gd_btn = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			this.btn_devConfig.setLayoutData(gd_btn);
			this.btn_devConfig.setImage(CommonImagesActivator.getDefault()
					.getRegisteredImage(CommonImagesActivator.IMG_16, CommonImagesActivator.DATACHANGE));
			Listener findOPCNode = new Listener() {
				@Override
				public void handleEvent(Event e) {
					NodeId nodeId = (NodeId) txt_devConfig.getData();
					OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
					dialog.setInternalServer(opcServer.getServerInstance());
					dialog.setSelectedNodeId(nodeId);
					int ok = dialog.open();
					if (ok == Dialog.OK) {
						NodeId id = dialog.getSelectedNodeId();
						String name = dialog.getSelectedDisplayName();
						setConfigNode(id, name);
					}
				}
			};
			// this.txt_devConfig.addKeyListener(enw StopKeyInputListener());
			this.txt_devConfig.addListener(SWT.MouseDoubleClick, findOPCNode);
			this.txt_devConfig.addListener(SWT.MouseDown, new Listener() {
				@Override
				public void handleEvent(Event event) {
					AdvancedRootConfigurationNode input = (AdvancedRootConfigurationNode) tv_devconfigNodes.getInput();
					if (input == null) {
						input = new AdvancedRootConfigurationNode(AdvancedSectionType.DeviceConfig);
						tv_devconfigNodes.setInput(input);
						boolean isActive = cb_ActiveDeviceConfig.isChecked();
						input.setActive(isActive);
					}
					currentSelection = input;
					selectionChanged();
				}
			});
			this.btn_devConfig.addListener(SWT.Selection, findOPCNode);
			Composite updown = new Composite(sctnComposite, SWT.NONE);
			updown.setLayout(new GridLayout(4, false));
			updown.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
			/**
			 * Deviceconfig
			 */
			this.tv_devconfigNodes = new TableViewer(sctnComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
			this.tv_devconfigNodes.setContentProvider(new AdvancedTableContentProvider());
			Table table_sc = tv_devconfigNodes.getTable();
			table_sc.setHeaderVisible(true);
			table_sc.setLinesVisible(true);
			gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_table_1.widthHint = 478;
			gd_table_1.heightHint = 150;
			table_sc.setLayoutData(gd_table_1);
			createDeviceConfigTable(tv_devconfigNodes);
			/**
			 * Drop
			 */
			int operations = DND.DROP_COPY;
			Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
			this.tv_devconfigNodes.addDropSupport(operations, transferTypes,
					new OPCUADropInDeviceconfigViewAdapter(this.tv_devconfigNodes, this.editor));
			this.tv_devconfigNodes.getTable().addMouseListener(new MouseAdapter() {
				boolean isOpen = false;

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					if (!this.isOpen) {
						Point p = new Point(e.x, e.y);
						ViewerCell cell = tv_devconfigNodes.getCell(p);
						if (cell == null) {
							return;
						}
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 1:
							AdvancedConfigurationNode element = (AdvancedConfigurationNode) cell.getElement();
							OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
							dialog.setInternalServer(opcServer.getServerInstance());
							dialog.setSelectedNodeId(element.getDeviceId());
							this.isOpen = true;
							int ok = dialog.open();
							if (ok == Dialog.OK) {
								NodeId id = dialog.getSelectedNodeId();
								element.setDeviceId(id);
								element.setDeviceName(dialog.getSelectedDisplayName());
								tv_devconfigNodes.update(element, null);
								editor.setDirty(true);
							}
							this.isOpen = false;
							break;
						}
					}
				}
			});
			this.cb_ActiveDeviceConfig = new CheckBoxButton(sectionDeviceConfig, SWT.NONE);
			formToolkit.adapt(cb_ActiveDeviceConfig, true, true);
			sectionDeviceConfig.setTextClient(cb_ActiveDeviceConfig);
			cb_ActiveDeviceConfig
					.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"));
			cb_ActiveDeviceConfig.setAlignment(SWT.LEFT);
			cb_ActiveDeviceConfig.setBackground(new Color(Display.getDefault(), 235, 235, 235));

			cb_ActiveDeviceConfig.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdvancedRootConfigurationNode input = (AdvancedRootConfigurationNode) tv_devconfigNodes.getInput();
					if (input == null) {
						input = new AdvancedRootConfigurationNode(AdvancedSectionType.DeviceConfig);
						tv_devconfigNodes.setInput(input);
					}
					boolean isActive = ((CheckBoxButton) e.getSource()).isChecked();
					input.setActive(isActive);
					sectionDeviceConfig.setExpanded(isActive);
					editor.setDirty(true);
					editor.computeSection();
				}
			});
			Button btnAddDeviceConfig = new Button(updown, SWT.NONE);
			btnAddDeviceConfig.setText("");
			GridData gd_btn_scadd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
			gd_btn_scadd.heightHint = 48;
			gd_btn_scadd.widthHint = 48;
			btnAddDeviceConfig.setLayoutData(gd_btn_scadd);
			btnAddDeviceConfig.setImage(CommonImagesActivator.getDefault()
					.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.ADD));
			btnAddDeviceConfig.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdvancedRootConfigurationNode root = (AdvancedRootConfigurationNode) tv_devconfigNodes.getInput();
					if (root == null) {
						root = new AdvancedRootConfigurationNode(AdvancedSectionType.DeviceConfig);
						tv_devconfigNodes.setInput(root);
					}
					AdvancedConfigurationNode newChild = new AdvancedConfigurationNode(
							AdvancedSectionType.DeviceConfig);
					root.addChild(newChild);
					tv_devconfigNodes.refresh(root);
					tv_devconfigNodes.setSelection(new StructuredSelection(newChild));
					computeSectionHandler.compute();
					editor.setDirty(true);
				}
			});
			Button btnRemoveStartConfg = new Button(updown, SWT.NONE);
			btnRemoveStartConfg.setText("");
			GridData gd_btn_scdelete = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
			gd_btn_scdelete.heightHint = 48;
			gd_btn_scdelete.widthHint = 48;
			btnRemoveStartConfg.setLayoutData(gd_btn_scdelete);
			btnRemoveStartConfg.setImage(CommonImagesActivator.getDefault()
					.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.DELETE));
			btnRemoveStartConfg.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdvancedRootConfigurationNode root = (AdvancedRootConfigurationNode) tv_devconfigNodes.getInput();
					Object[] selection = ((StructuredSelection) tv_devconfigNodes.getSelection()).toArray();
					if (selection == null) {
						return;
					}
					for (Object node : selection) {
						root.removeChild((AdvancedConfigurationNode) node);
					}
					tv_devconfigNodes.refresh(root);
					computeSectionHandler.compute();
					editor.setDirty(true);
				}
			});
		}
	}

	void createDeviceMapping(Composite parent) {
		if ("siemens".equals(this.drvType)) {
			/**
			 * Section with a client composite
			 */
			this.sectionDeviceMapping = formToolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
			formToolkit.paintBordersFor(sectionDeviceMapping);
			sectionDeviceMapping.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"widget.advanced.devicemapping"));
			sectionDeviceMapping.setExpanded(true);
			GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			sectionDeviceMapping.setLayoutData(gd_table_1);
			Composite sctnComposite = new Composite(sectionDeviceMapping, SWT.NONE);
			formToolkit.adapt(sctnComposite);
			formToolkit.paintBordersFor(sctnComposite);
			sectionDeviceMapping.setClient(sctnComposite);
			sctnComposite.setLayout(new GridLayout(3, false));
			// text boxes
			Label lbl_db = new Label(sctnComposite, SWT.NONE);
			lbl_db.setText(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.datapoint"));
			GridData gd = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			lbl_db.setLayoutData(gd);
			this.txt_mapping1 = new NumericText(sctnComposite, SWT.BORDER);
			gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
			txt_mapping1.setLayoutData(gd);
			txt_mapping1.addModifyListener(new DeviceMappingEditorDirtyListener(DirtyListenerProperties.db));
			Label lbl_start = new Label(sctnComposite, SWT.NONE);
			lbl_start.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"widget.advanced.startaddress"));
			gd = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			lbl_start.setLayoutData(gd);
			this.txt_mapping2 = new NumericText(sctnComposite, SWT.BORDER);
			gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
			txt_mapping2.setLayoutData(gd);
			txt_mapping2.addModifyListener(new DeviceMappingEditorDirtyListener(DirtyListenerProperties.start));
			Label lbl_addon = new Label(sctnComposite, SWT.NONE);
			lbl_addon.setText(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.addonrange"));
			gd = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			lbl_addon.setLayoutData(gd);
			this.txt_mapping3 = new NumericText(sctnComposite, SWT.BORDER);
			gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
			txt_mapping3.setLayoutData(gd);
			txt_mapping3.addModifyListener(new DeviceMappingEditorDirtyListener(DirtyListenerProperties.addon));
			Label lbl_group = new Label(sctnComposite, SWT.NONE);
			lbl_group.setText(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.grouprange"));
			gd = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			lbl_group.setLayoutData(gd);
			this.txt_mapping4 = new NumericText(sctnComposite, SWT.BORDER);
			gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
			txt_mapping4.setLayoutData(gd);
			txt_mapping4.addModifyListener(new DeviceMappingEditorDirtyListener(DirtyListenerProperties.group));
			Label lbl_metaId = new Label(sctnComposite, SWT.NONE);
			lbl_metaId.setText(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.meterid"));
			gd = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			lbl_metaId.setLayoutData(gd);
			this.txt_mapping5 = new NumericText(sctnComposite, SWT.BORDER);
			gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
			txt_mapping5.setLayoutData(gd);
			txt_mapping5.addModifyListener(new DeviceMappingEditorDirtyListener(DirtyListenerProperties.meterid));
			// active section
			Composite updown = new Composite(sctnComposite, SWT.NONE);
			updown.setLayout(new GridLayout(4, false));
			updown.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
			// device mapping table
			this.tv_devMappings = new TableViewer(sctnComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
			this.tv_devMappings.setContentProvider(new AdvancedTableContentProvider());
			Table table_sc = tv_devMappings.getTable();
			table_sc.setHeaderVisible(true);
			table_sc.setLinesVisible(true);
			gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_table_1.widthHint = 478;
			gd_table_1.heightHint = 150;
			table_sc.setLayoutData(gd_table_1);
			createDeviceMappingTable(tv_devMappings);
			int operations = DND.DROP_COPY;
			Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
			this.tv_devMappings.addDropSupport(operations, transferTypes,
					new OPCUADropInDeviceMappingViewAdapter(this.tv_devMappings, this.editor));
			this.tv_devMappings.getTable().addMouseListener(new MouseAdapter() {
				boolean isOpen = false;

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					if (!this.isOpen) {
						Point p = new Point(e.x, e.y);
						ViewerCell cell = tv_devMappings.getCell(p);
						if (cell == null) {
							return;
						}
						int columnIndex = cell.getColumnIndex();
						AdvancedConfigurationNode element = (AdvancedConfigurationNode) cell.getElement();
						NodeId selectedId = null;
						switch (columnIndex) {
						case 1:
							selectedId = element.getDeviceId();
							break;
						case 2:
							selectedId = element.getEnableId();
							break;
						case 3:
							selectedId = element.getAddonId();
							break;
						case 4:
							selectedId = element.getGroupId();
							break;
						case 5:
							selectedId = element.getMeterId();
							break;
						default:
							return;
						}
						OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
						dialog.setInternalServer(opcServer.getServerInstance());
						dialog.setSelectedNodeId(selectedId);
						this.isOpen = true;
						int ok = dialog.open();
						if (ok == Dialog.OK) {
							NodeId id = dialog.getSelectedNodeId();
							switch (columnIndex) {
							case 0:
								// Index value
								break;
							case 1:
								element.setDeviceId(id);
								element.setDeviceName(dialog.getSelectedDisplayName());
								break;
							case 2:
								element.setEnableId(id);
								element.setEnableName(dialog.getSelectedDisplayName());
								break;
							case 3:
								element.setAddonId(id);
								element.setAddonName(dialog.getSelectedDisplayName());
								break;
							case 4:
								element.setGroupId(id);
								element.setGroupName(dialog.getSelectedDisplayName());
								break;
							case 5:
								element.setMeterId(id);
								element.setMeterName(dialog.getSelectedDisplayName());
								break;
							default:
								break;
							}
							tv_devMappings.update(element, null);
							editor.setDirty(true);
						}
						this.isOpen = false;
					}
				}
			});
			Button btnAddDeviceConfig = new Button(updown, SWT.NONE);
			btnAddDeviceConfig.setText("");
			GridData gd_btn_scadd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_btn_scadd.heightHint = 48;
			gd_btn_scadd.widthHint = 48;
			btnAddDeviceConfig.setLayoutData(gd_btn_scadd);
			btnAddDeviceConfig.setImage(CommonImagesActivator.getDefault()
					.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.ADD));
			btnAddDeviceConfig.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdvancedRootConfigurationNode root = (AdvancedRootConfigurationNode) tv_devMappings.getInput();
					if (root == null) {
						root = new AdvancedRootConfigurationNode(AdvancedSectionType.DeviceMapping);
						tv_devMappings.setInput(root);
					}
					AdvancedConfigurationNode newChild = new AdvancedConfigurationNode(
							AdvancedSectionType.DeviceMapping);
					root.addChild(newChild);
					tv_devMappings.refresh(root);
					tv_devMappings.setSelection(new StructuredSelection(newChild));
					computeSectionHandler.compute();
					editor.setDirty(true);
				}
			});
			Button btnRemoveStartConfg = new Button(updown, SWT.NONE);
			btnRemoveStartConfg.setText("");
			GridData gd_btn_scdelete = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_btn_scdelete.heightHint = 48;
			gd_btn_scdelete.widthHint = 48;
			btnRemoveStartConfg.setLayoutData(gd_btn_scdelete);
			btnRemoveStartConfg.setImage(CommonImagesActivator.getDefault()
					.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.DELETE));
			this.cb_ActiveDeviceMapping = new CheckBoxButton(sectionDeviceMapping, SWT.NONE);
			formToolkit.adapt(cb_ActiveDeviceMapping, true, true);
			sectionDeviceMapping.setTextClient(cb_ActiveDeviceMapping);
			cb_ActiveDeviceMapping
					.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"));
			cb_ActiveDeviceMapping.setAlignment(SWT.LEFT);
			cb_ActiveDeviceMapping.setBackground(new Color(Display.getDefault(), 235, 235, 235));

			cb_ActiveDeviceMapping.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdvancedRootConfigurationNode input = (AdvancedRootConfigurationNode) tv_devMappings.getInput();
					if (input == null) {
						input = new AdvancedRootConfigurationNode(AdvancedSectionType.DeviceMapping);
						tv_devMappings.setInput(input);
					}
					boolean isChecked = ((CheckBoxButton) e.getSource()).isChecked();
					input.setActive(isChecked);
					sectionDeviceMapping.setExpanded(isChecked);
					editor.setDirty(true);
					editor.computeSection();
				}
			});
			btnRemoveStartConfg.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdvancedRootConfigurationNode root = (AdvancedRootConfigurationNode) tv_devMappings.getInput();
					Object[] selection = ((StructuredSelection) tv_devMappings.getSelection()).toArray();
					if (selection == null) {
						return;
					}
					for (Object node : selection) {
						root.removeChild((AdvancedConfigurationNode) node);
					}
					tv_devMappings.refresh(root);
					computeSectionHandler.compute();
					editor.setDirty(true);
				}
			});
		}
	}

	void createDeviceCounter(final Composite parent) {
		if ("advanced".equals(this.drvType)) {
			/**
			 * Section with a client composite
			 */
			this.sectionDeviceGroup = formToolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
			formToolkit.paintBordersFor(sectionDeviceGroup);
			sectionDeviceGroup.setText("Zähler");
			sectionDeviceGroup.setExpanded(true);
			GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			sectionDeviceGroup.setLayoutData(gd_table_1);
			Composite sctnComposite = new Composite(sectionDeviceGroup, SWT.NONE);
			formToolkit.adapt(sctnComposite);
			formToolkit.paintBordersFor(sctnComposite);
			sectionDeviceGroup.setClient(sctnComposite);
			sctnComposite.setLayout(new GridLayout(3, false));
			// config counter node
			Label lbl_devConfig = new Label(sctnComposite, SWT.NONE);
			GridData gd_lbl = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			lbl_devConfig.setLayoutData(gd_lbl);
			// lbl_devConfig.setAlignment(SWT.RIGHT);
			lbl_devConfig.setText(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.confignode"));
			this.txt_counterConfig = new Text(sctnComposite, SWT.BORDER);
			GridData gd_txt = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			txt_counterConfig.setLayoutData(gd_txt);
			DropTarget dropTarget = new DropTarget(this.txt_counterConfig, DND.DROP_COPY);
			dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
			dropTarget.addDropListener(new OPCUADropInDeviceMappingTextNodeAdapter(this));
			this.btn_counterConfig = new Button(sctnComposite, SWT.PUSH);
			GridData gd_btn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			this.btn_counterConfig.setLayoutData(gd_btn);
			this.btn_counterConfig.setImage(CommonImagesActivator.getDefault()
					.getRegisteredImage(CommonImagesActivator.IMG_16, CommonImagesActivator.DATACHANGE));
			Listener findOPCNode = new Listener() {
				@Override
				public void handleEvent(Event e) {
					NodeId nodeId = (NodeId) txt_counterConfig.getData();
					OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
					dialog.setInternalServer(opcServer.getServerInstance());
					dialog.setSelectedNodeId(nodeId);
					int ok = dialog.open();
					if (ok == Dialog.OK) {
						NodeId id = dialog.getSelectedNodeId();
						String name = dialog.getSelectedDisplayName();
						setCounterConfigNode(id, name);
					}
				}
			};
			// this.txt_devMapping.addKeyListener(new StopKeyInputListener());
			this.txt_counterConfig.addListener(SWT.MouseDoubleClick, findOPCNode);
			this.txt_counterConfig.addListener(SWT.MouseDown, new Listener() {
				@Override
				public void handleEvent(Event event) {
					AdvancedRootConfigurationNode input = (AdvancedRootConfigurationNode) tv_devcounterNodes.getInput();
					if (input == null) {
						input = new AdvancedRootConfigurationNode(AdvancedSectionType.Counter);
						tv_devcounterNodes.setInput(input);
						boolean isChecked = cb_ActiveDeviceCounter.isChecked();
						input.setActive(isChecked);
					}
					currentSelection = input;
					selectionChanged();
				}
			});
			this.btn_counterConfig.addListener(SWT.Selection, findOPCNode);
			// active section
			Composite updown = new Composite(sctnComposite, SWT.NONE);
			updown.setLayout(new GridLayout(6, false));
			updown.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
			Label lblActiveDevCounter = new Label(updown, SWT.NONE);
			lblActiveDevCounter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblActiveDevCounter
					.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"));
			this.cb_ActiveDeviceCounter = new CheckBoxButton(sectionDeviceGroup, SWT.NONE);
			formToolkit.adapt(cb_ActiveDeviceCounter, true, true);
			sectionDeviceGroup.setTextClient(cb_ActiveDeviceCounter);
			cb_ActiveDeviceCounter
					.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"));
			cb_ActiveDeviceCounter.setAlignment(SWT.LEFT);
			this.cb_ActiveDeviceCounter.setBackground(new Color(Display.getDefault(), 235, 235, 235));

			cb_ActiveDeviceCounter.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdvancedRootConfigurationNode input = (AdvancedRootConfigurationNode) tv_devcounterNodes.getInput();
					if (input == null) {
						input = new AdvancedRootConfigurationNode(AdvancedSectionType.Counter);
						tv_devcounterNodes.setInput(input);
					}
					boolean isChecked = ((CheckBoxButton) e.getSource()).isChecked();
					input.setActive(isChecked);
					sectionDeviceGroup.setExpanded(isChecked);
					editor.setDirty(true);
					editor.computeSection();
				}
			});
			// up down button
			Button btn_up = new Button(updown, SWT.PUSH);
			GridData gd_btn_up = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gd_btn_up.heightHint = 48;
			gd_btn_up.widthHint = 48;
			btn_up.setLayoutData(gd_btn_up);
			// btn_up.setText("Up");
			btn_up.setImage(StudioImageActivator.getImage(StudioImages.ICON_ARROW_UP));
			btn_up.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					ISelection selection = tv_devcounterNodes.getSelection();
					if (selection == null || selection.isEmpty()) {
						return;
					}
					AdvancedConfigurationNode element = (AdvancedConfigurationNode) ((StructuredSelection) selection)
							.getFirstElement();
					AbstractConfigNode parentConfig = element.getParent();
					AdvancedConfigurationNode[] children2sort = parentConfig.getChildren();
				}
			});
			// up down button
			Button btn_down = new Button(updown, SWT.PUSH);
			GridData gd_btn_down = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gd_btn_down.heightHint = 48;
			gd_btn_down.widthHint = 48;
			btn_down.setLayoutData(gd_btn_down);
			// btn_down.setText("Down");
			btn_down.setImage(StudioImageActivator.getImage(StudioImages.ICON_ARROW_DOWN));
			btn_down.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
				}
			});
			// device counter tree
			this.tv_devcounterNodes = new TreeViewer(sctnComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
			this.tv_devcounterNodes.setContentProvider(new AdvancedTreeContentProvider());
			Tree table_sc = tv_devcounterNodes.getTree();
			table_sc.setHeaderVisible(true);
			table_sc.setLinesVisible(true);
			gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			gd_table_1.widthHint = 478;
			gd_table_1.heightHint = 150;
			table_sc.setLayoutData(gd_table_1);
			createDeviceCounterTable(tv_devcounterNodes);
			int operations = DND.DROP_COPY;
			Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
			this.tv_devcounterNodes.addDropSupport(operations, transferTypes,
					new OPCUADropInDevicegroupViewAdapter(this.tv_devcounterNodes, this.editor));
			Button btnAddStartConfig = new Button(updown, SWT.NONE);
			btnAddStartConfig.setText("");
			GridData gd_btn_scadd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_btn_scadd.heightHint = 48;
			gd_btn_scadd.widthHint = 48;
			btnAddStartConfig.setLayoutData(gd_btn_scadd);
			btnAddStartConfig.setImage(CommonImagesActivator.getDefault()
					.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.ADD));
			btnAddStartConfig.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection source = (IStructuredSelection) tv_devcounterNodes.getSelection();
					Object fe = source.getFirstElement();
					NewAdvancedCounterTreeItemWizard wizard = new NewAdvancedCounterTreeItemWizard();
					wizard.setSelection(fe);
					boolean isNew = wizard.setRoot(tv_devcounterNodes.getInput());
					WizardDialog dialog = new WizardDialog(parent.getShell(), wizard);
					if (WizardDialog.OK == dialog.open()) {
						Object obj = wizard.getResult();
						if (!isNew) {
							Object parent = wizard.getResultParent();
							tv_devcounterNodes.expandToLevel(obj, 0);
							tv_devcounterNodes.refresh(parent);
						} else {
							Object newRoot = wizard.getRoot();
							tv_devcounterNodes.setInput(newRoot);
						}
						tv_devcounterNodes.setSelection(new StructuredSelection(obj));
						computeSectionHandler.compute();
						editor.setDirty(true);
					}
				}
			});
			Button btnRemoveStartConfg = new Button(updown, SWT.NONE);
			btnRemoveStartConfg.setText("");
			GridData gd_btn_scdelete = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_btn_scdelete.heightHint = 48;
			gd_btn_scdelete.widthHint = 48;
			btnRemoveStartConfg.setLayoutData(gd_btn_scdelete);
			btnRemoveStartConfg.setImage(CommonImagesActivator.getDefault()
					.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.DELETE));
			btnRemoveStartConfg.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdvancedRootConfigurationNode root = (AdvancedRootConfigurationNode) tv_devcounterNodes.getInput();
					Object[] selection = ((StructuredSelection) tv_devcounterNodes.getSelection()).toArray();
					if (selection == null) {
						return;
					}
					for (Object node : selection) {
						Object nParent = ((AdvancedConfigurationNode) node).getParent();
						if (nParent instanceof AdvancedRootConfigurationNode) {
							((AdvancedRootConfigurationNode) nParent).removeChild((AdvancedConfigurationNode) node);
						} else if (nParent instanceof AdvancedConfigurationNode) {
							((AdvancedConfigurationNode) nParent).removeChild((AdvancedConfigurationNode) node);
						}
					}
					tv_devcounterNodes.refresh(root);
					computeSectionHandler.compute();
					editor.setDirty(true);
				}
			});
			this.tv_devcounterNodes.getTree().addMouseListener(new MouseAdapter() {
				boolean isOpen = false;

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					IStructuredSelection selection = (IStructuredSelection) tv_devcounterNodes.getSelection();
					if (!this.isOpen && ((AdvancedConfigurationNode) selection.getFirstElement()).isState()) {
						Point p = new Point(e.x, e.y);
						ViewerCell cell = tv_devcounterNodes.getCell(p);
						if (cell == null) {
							return;
						}
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 2:
							AdvancedConfigurationNode element = (AdvancedConfigurationNode) cell.getElement();
							OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
							dialog.setInternalServer(opcServer.getServerInstance());
							dialog.setSelectedNodeId(element.getDeviceId());
							this.isOpen = true;
							int ok = dialog.open();
							if (ok == Dialog.OK) {
								NodeId id = dialog.getSelectedNodeId();
								element.setDeviceId(id);
								element.setCounter(dialog.getSelectedDisplayName());
								tv_devcounterNodes.update(element, null);
								editor.setDirty(true);
							}
							this.isOpen = false;
							break;
						}
					}
				}
			});
		}
	}

	void createDeviceConsumption(final Composite parent) {
		// String[] datatypes = null;
		if ("siemens".equals(this.drvType)) {
			/**
			 * Section with a client composite
			 */
			this.sectionDeviceConsumption = formToolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
			formToolkit.paintBordersFor(sectionDeviceConsumption);
			sectionDeviceConsumption.setText(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.consumption"));
			sectionDeviceConsumption.setExpanded(true);
			GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			sectionDeviceConsumption.setLayoutData(gd_table_1);
			this.cb_ActiveDeviceConsumption = new CheckBoxButton(sectionDeviceConsumption, SWT.NONE);
			formToolkit.adapt(cb_ActiveDeviceConsumption, true, true);
			sectionDeviceConsumption.setTextClient(cb_ActiveDeviceConsumption);
			cb_ActiveDeviceConsumption
					.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"));
			cb_ActiveDeviceConsumption.setAlignment(SWT.LEFT);
			this.cb_ActiveDeviceConsumption.setBackground(new Color(Display.getDefault(), 235, 235, 235));

			cb_ActiveDeviceConsumption.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean isChecked = ((CheckBoxButton) e.getSource()).isChecked();
					// input.setActive(isChecked);
					sectionDeviceConsumption.setExpanded(isChecked);
					editor.setDirty(true);
					editor.computeSection();
				}
			});
			Composite container = new Composite(sectionDeviceConsumption, SWT.NONE);
			formToolkit.adapt(container);
			formToolkit.paintBordersFor(container);
			sectionDeviceConsumption.setClient(container);
			container.setLayout(new GridLayout(1, false));
			container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			Composite sctnProp = new Composite(container, SWT.NONE);
			formToolkit.adapt(sctnProp);
			formToolkit.paintBordersFor(sctnProp);
			sctnProp.setLayout(new GridLayout(3, false));
			this.tabFolder = new CTabFolder(container, SWT.BORDER);
			tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			tabFolder.setSelectionBackground(
					Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		}
	}

	/**
	 * try to fill all sections
	 */
	private void fillControlls() {
		fill();
		initExpansionState();
		this.editor.setDirty(false);
	}

	private void fill() {
		if (sectionStartConfig != null) {
			fillStartConfigurationNodes();
		}
		if (sectionDeviceConfig != null) {
			fillDeviceConfigurationNodes();
		}
		if (sectionDeviceMapping != null) {
			fillExtendedMappingConfiguration();
		}
		if (sectionDeviceGroup != null) {
			fillDeviceCounterNodes();
		}
		if (sectionDeviceConsumption != null) {
			fillDeviceConsumption();
		}
	}

	private void initExpansionState() {
		if (sectionStartConfig != null) {
			sectionStartConfig.setExpanded(cb_ActiveStartConfig.isChecked());
		}
		if (sectionDeviceConfig != null) {
			sectionDeviceConfig.setExpanded(cb_ActiveDeviceConfig.isChecked());
		}
		if (sectionDeviceMapping != null) {
			sectionDeviceMapping.setExpanded(cb_ActiveDeviceMapping.isChecked());
		}
		if (sectionDeviceGroup != null) {
			sectionDeviceGroup.setExpanded(cb_ActiveDeviceCounter.isChecked());
		}
		// only visible when siemens driver is selected
		if (sectionDeviceConsumption != null) {
			sectionDeviceConsumption.setExpanded(cb_ActiveDeviceConsumption.isChecked());
		}
		computeSize();
	}

	private void fillDeviceCounterNodes() {
		IFileSystem fileSystem = ((OPCUAAdvancedDriverEditorInput) ((EditorPart) this.editor).getEditorInput())
				.getFileSystem();
		String startconfigurationNodes = new Path(
				((OPCUAAdvancedDriverEditorInput) ((EditorPart) this.editor).getEditorInput()).getDriverPath())
						.append("counter.com").toOSString();
		AdvancedDriverPersister importer = new AdvancedDriverPersister();
		AdvancedGroupNodeParser saxHandler = new AdvancedGroupNodeParser(
				this.opcServer.getServerInstance().getNamespaceUris(), AdvancedSectionType.Counter);
		AdvancedRootConfigurationNode root = importer.importAdvancedSettings(
				opcServer.getServerInstance().getNamespaceUris(), fileSystem, startconfigurationNodes, saxHandler,
				AdvancedSectionType.Counter);
		this.tv_devcounterNodes.setInput(root);
		if (root.isActive()) {
			this.cb_ActiveDeviceCounter.setChecked(root.isActive());
		}
		this.tv_devcounterNodes.expandAll();
	}

	private void fillDeviceConsumption() {
		IFileSystem fileSystem = ((OPCUAAdvancedDriverEditorInput) ((EditorPart) this.editor).getEditorInput())
				.getFileSystem();
		String consumptionconfiguration = new Path(
				((OPCUAAdvancedDriverEditorInput) ((EditorPart) this.editor).getEditorInput()).getDriverPath())
						.append("consumptionconfiguration.com").toOSString();
		DeviceConsumption state = new DeviceConsumption();
		ConsumptionImportHandler handler = new ConsumptionImportHandler(state,
				this.opcServer.getServerInstance().getNamespaceUris());
		AdvancedDriverPersister importer = new AdvancedDriverPersister();
		importer.importDeviceConsumption(fileSystem, consumptionconfiguration, handler);
		this.tabFolder.setData(state.getTemplates());
		if (state.getTemplates().isEmpty()) {
			ConsumptionTemplate template1 = new ConsumptionTemplate(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.electricity"));
			state.addTemplate(template1);
			ConsumptionTemplate template2 = new ConsumptionTemplate(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.gas"));
			state.addTemplate(template2);
			ConsumptionTemplate template3 = new ConsumptionTemplate(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.air"));
			state.addTemplate(template3);
			ConsumptionTemplate template4 = new ConsumptionTemplate(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.dough"));
			state.addTemplate(template4);
		}
		CTabItem[] items = tabFolder.getItems();
		if (items != null && items.length > 0) {
			for (CTabItem item : items) {
				item.dispose();
			}
		}
		for (int i = 0; i < state.getTemplates().size(); i++) {
			ConsumptionTemplate t = (ConsumptionTemplate) state.getTemplates().get(i);
			CTabItem item = new CTabItem(tabFolder, SWT.FILL);
			item.setText(t.getCategory());
			createTabItemControls(tabFolder, item, t);
			if (i == 0) {
				tabFolder.setSelection(item);
			}
		}
		if (state.isActive()) {
			this.cb_ActiveDeviceConsumption.setChecked(state.isActive());
		}
	}

	private void fillExtendedMappingConfiguration() {
		IFileSystem fileSystem = ((OPCUAAdvancedDriverEditorInput) ((EditorPart) this.editor).getEditorInput())
				.getFileSystem();
		String startconfigurationNodes = new Path(
				((OPCUAAdvancedDriverEditorInput) ((EditorPart) this.editor).getEditorInput()).getDriverPath())
						.append("devicemapping.com").toOSString();
		AdvancedDriverPersister importer = new AdvancedDriverPersister();
		AdvancedConfigurationNodeParser saxHandler = new AdvancedConfigurationNodeParser(
				this.opcServer.getServerInstance().getNamespaceUris(), AdvancedSectionType.DeviceMapping);
		AdvancedRootConfigurationNode root = importer.importAdvancedSettings(
				opcServer.getServerInstance().getNamespaceUris(), fileSystem, startconfigurationNodes, saxHandler,
				AdvancedSectionType.DeviceMapping);
		this.tv_devMappings.setInput(root);
		if (root != null) {
			if (root.getDatablock() != null) {
				txt_mapping1.setText(root.getDatablock());
			}
			if (root.getStartAddress() != null) {
				txt_mapping2.setText(root.getStartAddress());
			}
			if (root.getRangeAddon() != null) {
				txt_mapping3.setText(root.getRangeAddon());
			}
			if (root.getRangeGroup() != null) {
				txt_mapping4.setText(root.getRangeGroup());
			}
			if (root.getMeterId() != null) {
				txt_mapping5.setText(root.getMeterId());
			}
			if (root.isActive()) {
				this.cb_ActiveDeviceMapping.setChecked(root.isActive());
			}
		}
	}

	private void fillDeviceConfigurationNodes() {
		IFileSystem fileSystem = ((OPCUAAdvancedDriverEditorInput) ((EditorPart) this.editor).getEditorInput())
				.getFileSystem();
		String startconfigurationNodes = new Path(
				((OPCUAAdvancedDriverEditorInput) ((EditorPart) this.editor).getEditorInput()).getDriverPath())
						.append("deviceconfig.com").toOSString();
		AdvancedDriverPersister importer = new AdvancedDriverPersister();
		AdvancedConfigurationNodeParser saxHandler = new AdvancedConfigurationNodeParser(
				this.opcServer.getServerInstance().getNamespaceUris(), AdvancedSectionType.DeviceConfig);
		AdvancedRootConfigurationNode root = importer.importAdvancedSettings(
				opcServer.getServerInstance().getNamespaceUris(), fileSystem, startconfigurationNodes, saxHandler,
				AdvancedSectionType.DeviceConfig);
		// this.tv_devcounterNodes.setInput(root);
		// if (root.isActive()) {
		// this.cb_ActiveDeviceCounter.setChecked(root.isActive());
		// }
		// this.tv_devcounterNodes.expandAll();
		this.tv_devconfigNodes.setInput(root);
		setConfigNode(root.getRefNodeId(), root.getRefNodeName());
		if (root.isActive()) {
			this.cb_ActiveDeviceConfig.setChecked(root.isActive());
		}
	}

	private void fillStartConfigurationNodes() {
		if (sectionStartConfig != null) {
			IFileSystem fileSystem = ((OPCUAAdvancedDriverEditorInput) ((EditorPart) this.editor).getEditorInput())
					.getFileSystem();
			String startconfigurationNodes = new Path(
					((OPCUAAdvancedDriverEditorInput) ((EditorPart) this.editor).getEditorInput()).getDriverPath())
							.append("startconfignodes.com").toOSString();
			AdvancedDriverPersister importer = new AdvancedDriverPersister();
			AdvancedConfigurationNodeParser saxHandler = new AdvancedConfigurationNodeParser(
					this.opcServer.getServerInstance().getNamespaceUris(), AdvancedSectionType.StartConfig);
			AdvancedRootConfigurationNode root = importer.importAdvancedSettings(
					opcServer.getServerInstance().getNamespaceUris(), fileSystem, startconfigurationNodes, saxHandler,
					AdvancedSectionType.StartConfig);
			this.tv_StartconfigNodes.setInput(root);
			if (root.isActive()) {
				this.cb_ActiveStartConfig.setChecked(root.isActive());
			}
		}
	}

	private void createAdvancedWidget(Composite parent) {
		createStartConfig(parent);
		createDeviceConfig(parent);
		createDeviceMapping(parent);
		createDeviceCounter(parent);
		createDeviceConsumption(parent);
	}

	private void createTabItemControls(Composite tabFolder, CTabItem item, final ConsumptionTemplate template) {
		Composite tableRootComposite = new Composite(tabFolder, SWT.NONE);
		item.setControl(tableRootComposite);
		tableRootComposite.setLayout(new GridLayout(2, false));
		// Composite composite = new Composite(tableRootComposite, SWT.FILL);
		// // item.setControl(composite);
		// composite.setLayout(new GridLayout(1, false));
		// composite.setBackground(new Color(Display.getCurrent(), 255, 0, 0));
		Label lbl_name = new Label(tableRootComposite, SWT.NONE);
		lbl_name.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_name.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.db"));
		lbl_name.setToolTipText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.tooltip.dp"));
		NumericText txt_db = new NumericText(tableRootComposite, SWT.BORDER);
		// formToolkit.createText(tableRootComposite, "New Text",
		// SWT.NONE);
		txt_db.setText("");
		txt_db.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_db.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String text = ((Text) e.getSource()).getText();
				template.setDB(text);
				editor.setDirty(true);
			}
		});
		Label lblNewLabel = new Label(tableRootComposite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.startaddress"));
		lblNewLabel.setToolTipText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"widget.advanced.tooltip.consumption"));
		Text txt_startaddress = new NumericText(tableRootComposite, SWT.BORDER);
		txt_startaddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txt_startaddress, true, true);
		txt_startaddress.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String text = ((Text) e.getSource()).getText();
				template.setStartAddress(text);
				editor.setDirty(true);
			}
		});
		Label lblStructLength = new Label(tableRootComposite, SWT.NONE);
		lblStructLength.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStructLength.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.structlength"));
		lblStructLength.setToolTipText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"widget.advanced.tooltip.structlength"));
		// add
		// multi
		// language
		// support
		NumericText txt_StructLength = new NumericText(tableRootComposite, SWT.BORDER);
		txt_StructLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txt_StructLength, true, true);
		txt_StructLength.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String text = ((Text) e.getSource()).getText();
				template.setStructLength(text);
				editor.setDirty(true);
			}
		});
		Composite bc = new Composite(tableRootComposite, SWT.FILL);
		bc.setLayout(new GridLayout(2, false));
		final TableViewer tableViewer = new TableViewer(tableRootComposite,
				SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		Button btnAddItem = new Button(bc, SWT.NONE);
		// btn_add.setToolTipText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
		// "AggregatedDPEditor.addmapping"));
		GridData gd_btn_add = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btn_add.widthHint = 48;
		gd_btn_add.heightHint = 48;
		btnAddItem.setLayoutData(gd_btn_add);
		btnAddItem.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.ADD));
		btnAddItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ConsumptionTemplate input = (ConsumptionTemplate) tableViewer.getInput();
				AdvancedConfigurationNode node = new AdvancedConfigurationNode(AdvancedSectionType.State);
				// node.setDataType(SIEMENS_DATA_TYPE.UNDEFINED.name());
				node.setMapping(DATATYPE_MAPPING_TYPE.SCALAR);
				node.setRefStartAddress("0.0");
				node.setDeviceName("unknown");
				input.addItem(node);
				tableViewer.refresh();
				editor.setDirty(true);
			}
		});
		Button btnRemoveItem = new Button(bc, SWT.NONE);
		GridData gd_btnRemoveItem = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btnRemoveItem.widthHint = 48;
		gd_btnRemoveItem.heightHint = 48;
		btnRemoveItem.setLayoutData(gd_btn_add);
		btnRemoveItem.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.DELETE));
		btnRemoveItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ConsumptionTemplate input = (ConsumptionTemplate) tableViewer.getInput();
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				AbstractConfigNode node = (AbstractConfigNode) selection.getFirstElement();
				input.removeItem(node);
				tableViewer.refresh();
				editor.setDirty(true);
			}
		});
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TableItem[] selection = table.getSelection();
				// check selection
				if (selection != null && selection.length > 0) {
					// check cell index
					Point p = new Point(e.x, e.y);
					ViewerCell cell = tableViewer.getCell(p);
					if (cell == null) {
						return;
					}
					int columnIndex = cell.getColumnIndex();
					switch (columnIndex) {
					case 0:
					case 1:
					case 2:
						TableItem item = selection[0];
						Object data = item.getData();
						if (data != null && data instanceof AdvancedConfigurationNode) {
							NodeId nodeid = ((AdvancedConfigurationNode) data).getConfigNodeId();
							OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
							dialog.setInternalServer(opcServer.getServerInstance());
							dialog.setSelectedNodeId(nodeid);
							// dialog.setStartNodeId(Identifiers.ObjectsFolder);
							dialog.setFormTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"widget.advanced.opcuanodes"));
							if (dialog.open() == Dialog.OK) {
								editor.setDirty(true);
								if (dialog.getSelectedNodeId() != null)
									((AdvancedConfigurationNode) data).setConfigId(dialog.getSelectedNodeId());
								((AdvancedConfigurationNode) data).setConfigNodeName(dialog.getSelectedDisplayName());
								((AdvancedConfigurationNode) data)
										.setBrowsePath(getBrowsePath(dialog.getSelectedNodeId()));
								tableViewer.refresh();
								// updateTriggerNodes();
							}
						}
						break;
					}
				}
			}
		});
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd.minimumHeight = 300;
		gd.widthHint = 478;
		gd.heightHint = 150;
		table.setLayoutData(gd);
		tableViewer.setContentProvider(new StateContentProvider());
		tableViewer.setLabelProvider(new StateLabelProvider());
		/**
		 * Drop
		 */
		int operations = DND.DROP_COPY;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		tableViewer.addDropSupport(operations, transferTypes,
				new OPCUADropInDeviceStateViewAdapter(tableViewer, this.editor));
		TableViewerColumn tvcName = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.displayname"), 150);
		tvcName.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					return ((AdvancedConfigurationNode) element).getConfigNodeName();
				} else
					return "";
			}
		});
		tvcName.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				((AdvancedConfigurationNode) element).setGroupName((String) value);
				tableViewer.refresh(element);
				editor.setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return "" + ((AdvancedConfigurationNode) element).getGroupName();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (((AdvancedConfigurationNode) element).isGroup()) {
					TextCellEditor editor = new TextCellEditor(tableViewer.getTable());
					return editor;
				}
				return null;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tableViewerColumn = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.nodeid"), 100);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					NodeId nodeId = ((AdvancedConfigurationNode) element).getConfigNodeId();
					if (NodeId.isNull(nodeId)) {
						return "";
					}
					return nodeId.toString();
				} else
					return "";
			}
		});
		// TableViewerColumn tableViewerColumn = new TableViewerColumn(
		// tableViewer, SWT.NONE);
		// TableColumn tblclmnNodeid = tableViewerColumn.getColumn();
		// tblclmnNodeid.setWidth(100);
		// tblclmnNodeid.setText("NodeId");
		TableViewerColumn tvcBrowsepath = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.browsepath"), 100);
		tvcBrowsepath.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					return ((AdvancedConfigurationNode) element).getBrowsepath();
				} else
					return "";
			}
		});
		// TableViewerColumn tvcAddress = new TableViewerColumn(tableViewer,
		// SWT.NONE);
		// TableColumn tblclmnNewColumn = tvcAddress.getColumn();
		// tblclmnNewColumn.setWidth(100);
		// tblclmnNewColumn.setText("BrowsePath");
		TableViewerColumn tvc_siemensname = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.name"), 100);
		tvc_siemensname.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					return ((AdvancedConfigurationNode) element).getDeviceName();
				} else
					return "";
			}
		});
		// TableViewerColumn tvc_siemensname = new
		// TableViewerColumn(tableViewer,
		// SWT.NONE);
		// TableColumn tblclmnSiemensName = tvc_siemensname.getColumn();
		// tblclmnSiemensName.setWidth(100);
		// tblclmnSiemensName.setText("Siemens Name");
		tvc_siemensname.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((AdvancedConfigurationNode) element).getDeviceName();
			}

			protected void setValue(Object element, Object value) {
				if (((AdvancedConfigurationNode) element).getDeviceName().compareTo(value.toString()) == 0) {
					return;
				}
				((AdvancedConfigurationNode) element).setDeviceName(value.toString());
				tableViewer.update(element, null);
				editor.setDirty(true);
			}
		});
		TableViewerColumn tvc_address = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.index"), 150);
		tvc_address.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					return ((AdvancedConfigurationNode) element).getRefStartAddress();
				} else
					return "";
			}
		});
		// TableViewerColumn tvc_addressType = new
		// TableViewerColumn(tableViewer,
		// SWT.NONE);
		// TableColumn columnAddress = tvc_addressType.getColumn();
		// columnAddress.setWidth(150);
		// columnAddress.setText("Address");
		tvc_address.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new DoubleCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((AdvancedConfigurationNode) element).getRefStartAddress();
			}

			protected void setValue(Object element, Object value) {
				if (((AdvancedConfigurationNode) element).getRefStartAddress().compareTo(value.toString()) == 0) {
					return;
				}
				((AdvancedConfigurationNode) element).setRefStartAddress(value.toString());
				tableViewer.update(element, null);
				editor.setDirty(true);
			}
		});
		TableViewerColumn tvc_mapping = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.mapping"), 100);
		tvc_mapping.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					return ((AdvancedConfigurationNode) element).getMapping().name();
				} else
					return "";
			}
		});
		tvc_mapping.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				if (value == null) {
					value = DATATYPE_MAPPING_TYPE.SCALAR;
				}
				// if (value instanceof String) {
				// ((AdvancedConfigurationNode) element)
				// .setMapping((String) value);
				// } else
				if (value instanceof DATATYPE_MAPPING_TYPE) {
					((AdvancedConfigurationNode) element).setMapping((DATATYPE_MAPPING_TYPE) value);
				}
				tableViewer.update(element, null);
				editor.setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return ((AdvancedConfigurationNode) element).getMapping();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				ComboBoxViewerCellEditor cellEditor = new ComboBoxViewerCellEditor(tableViewer.getTable(),
						SWT.READ_ONLY);
				cellEditor.setLabelProvider(new LabelProvider());
				cellEditor.setContentProvider(new ArrayContentProvider());
				cellEditor.setInput(DATATYPE_MAPPING_TYPE.values());
				return cellEditor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		final ComboBoxViewerCellEditor ceditor = new ComboBoxViewerCellEditor(tableViewer.getTable());
		ceditor.setContentProvider(new ArrayContentProvider());
		ceditor.setLabelProvider(new LabelProvider());
		List<String> v = new ArrayList<String>();
		// for (SIEMENS_DATA_TYPE d : SIEMENS_DATA_TYPE.values())
		// {
		// v.add(d.name());
		// }
		ceditor.setInput(v.toArray(new String[v.size()]));
		TableViewerColumn tvc_datatype = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.datatype"), 75,
				new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((AdvancedConfigurationNode) element).getDataType();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		tvc_datatype.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (!(element instanceof AdvancedConfigurationNode)) {
					return "";
				}
				if (((AdvancedConfigurationNode) element).getDataType() == null) {
					return "";
				}
				return ((AdvancedConfigurationNode) element).getDataType();
			}
		});
		tvc_datatype.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				if (value == null) {
					String el = ((CCombo) ceditor.getControl()).getText();
					if (el.isEmpty()) {
						return;
					}
					boolean found = false;
					for (String item : ((CCombo) ceditor.getControl()).getItems()) {
						if (el.compareTo(item) == 0) {
							found = true;
							break;
						}
					}
					if (!found) {
						Object i = ceditor.getViewer().getInput();
						if (i instanceof String[]) {
							List<String> l = new ArrayList<String>();
							for (String s : (String[]) i) {
								l.add(s);
							}
							l.add(el.toUpperCase().replace(" ", ""));
							ceditor.getViewer().setInput(l.toArray(new String[l.size()]));
							ceditor.getViewer().refresh();
						}
					}
					((AdvancedConfigurationNode) element).setDataType(el.toUpperCase().replace(" ", ""));
					tableViewer.refresh();
					editor.setDirty(true);
					return;
				}
				((AdvancedConfigurationNode) element).setDataType(value.toString());
				tableViewer.refresh();
				editor.setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				if (element == null || ((AdvancedConfigurationNode) element).getDataType() == null) {
					return "";
				}
				// TODO Auto-generated method stub
				return ((AdvancedConfigurationNode) element).getDataType();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return ceditor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tvc_active = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"), 35);
		// TableColumn trclmn_active = tvc_active.getColumn();
		// trclmn_active.setWidth(35);
		// trclmn_active.setText("active");
		tvc_active.setLabelProvider(new ColumnLabelProvider() {
			// @Override
			// public Color getBackground(Object element) {
			// return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			// }
			@Override
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				if (((AdvancedConfigurationNode) element).isEnable()) {
					return OPCUASharedImages.getImage(OPCUASharedImages.ICON_CHECKED_1);
				} else {
					return OPCUASharedImages.getImage(OPCUASharedImages.ICON_CHECKED_0);
				}
			}
		});
		tvc_active.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((AdvancedConfigurationNode) element).isEnable();
			}

			protected void setValue(Object element, Object value) {
				((AdvancedConfigurationNode) element).setEnable(!((AdvancedConfigurationNode) element).isEnable());
				// ((OmronTableItem)element).active = value.toString();
				editor.setDirty(true);
				tableViewer.update(element, null);
			}
		});
		TableViewerColumn tvc_cycletime = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.cycletime"), 50,
				new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((AdvancedConfigurationNode) element).getCycletime();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((Long) o1).compareTo((Long) o2);
					}
				});
		// TableColumn trclmnCycletime = tvc_cycletime.getColumn();
		// trclmnCycletime.setWidth(55);
		// trclmnCycletime.setText("cycletime");
		tvc_cycletime.setLabelProvider(new ColumnLabelProvider() {
			// @Override
			// public Color getBackground(Object element) {
			// return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			// }
			@Override
			public String getText(Object element) {
				return ((AdvancedConfigurationNode) element).getCycletime() + "";
			}
		});
		tvc_cycletime.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new IntegerCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((AdvancedConfigurationNode) element).getCycletime();
			}

			protected void setValue(Object element, Object value) {
				try {
					int cycletime = Integer.parseInt(value.toString());
					if (((AdvancedConfigurationNode) element).getCycletime() == cycletime) {
						return;
					}
					((AdvancedConfigurationNode) element).setCycletime(cycletime);
					editor.setDirty(true);
				} catch (NumberFormatException ex) {
					// TODO parse number format exception
				}
				tableViewer.update(element, null);
			}
		});
		TableViewerColumn tvc_trigger = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.trigger"), 100);
		// TableColumn trclmn_historical = tvc_trigger.getColumn();
		// trclmn_historical.setWidth(90);
		// trclmn_historical.setText("trigger");
		tvc_trigger.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((AdvancedConfigurationNode) element).getTrigger() != null) {
					// check if nodeid is null for dummy trigger event,
					// to show an empty line
					if (((AdvancedConfigurationNode) element).getTrigger().nodeId == null)
						return "";
					return ((AdvancedConfigurationNode) element).getTrigger().triggerName + " "
							+ ((AdvancedConfigurationNode) element).getTrigger().nodeId.toString() + " "
							+ ((AdvancedConfigurationNode) element).getTrigger().displayname;
				}
				return "";
			}

			@Override
			public Color getForeground(Object element) {
				NodeToTrigger obj = ((AdvancedConfigurationNode) element).getTrigger();
				boolean good = editor.isTriggerNodeValid(obj);
				if (!good) {
					Display display = Display.getCurrent();
					Color red = display.getSystemColor(SWT.COLOR_RED);
					return red;
				}
				return null;
			}
		});
		// create a new Trigger editor viewer
		final ComboBoxViewerCellEditor triggerEditor = new ComboBoxViewerCellEditor(tableViewer.getTable(),
				SWT.READ_ONLY);
		triggerEditor.setContentProvider(new ArrayContentProvider());
		final ControlDecoration controlDecoration = new ControlDecoration(triggerEditor.getControl(),
				SWT.LEFT | SWT.TOP);
		controlDecoration.setDescriptionText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"widget.advanced.trigger.notavailable"));
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
		triggerEditor.setValidator(new ICellEditorValidator() {
			@Override
			public String isValid(Object element) {
				if (!(element instanceof NodeToTrigger)) {
					return null;
				}
				boolean good = editor.isTriggerNodeValid((NodeToTrigger) element);
				if (good) {
					controlDecoration.hide();
					return null;
				} else {
					controlDecoration.show();
					return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
							"widget.advanced.trigger.notavailable");
				}
			}
		});
		triggerEditor.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (((NodeToTrigger) element) != null) {
					// check if nodeid is null for dummy trigger event,
					// to show an empty line
					if (((NodeToTrigger) element).nodeId == null)
						return "";
					return ((NodeToTrigger) element).triggerName + " " + ((NodeToTrigger) element).nodeId.toString()
							+ " " + ((NodeToTrigger) element).displayname;
				}
				return super.getText(element);
			}
		});
		// set the correct trigger node to each config node
		for (AbstractConfigNode n : template.getItems()) {
			if (n instanceof AdvancedConfigurationNode) {
				// find trigger from possible triggers
				String tname = ((AdvancedConfigurationNode) n).getTrigger().triggerName;
				for (NodeToTrigger tt : editor.getPossibleTriggerNodes()) {
					if (tt.triggerName.compareTo(tname) == 0) {
						((AdvancedConfigurationNode) n).setTrigger(tt);
					}
				}
			}
		}
		triggerEditor.setInput(editor.getPossibleTriggerNodes());
		tvc_trigger.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return triggerEditor;
			}

			protected Object getValue(Object element) {
				if (((AdvancedConfigurationNode) element).getTrigger() != null) {
					return ((AdvancedConfigurationNode) element).getTrigger();
				}
				return null;
			}

			protected void setValue(Object element, Object value) {
				if (value == null || !(value instanceof NodeToTrigger))
					return;
				((AdvancedConfigurationNode) element).setTrigger((NodeToTrigger) value);
				editor.setDirty(true);
				tableViewer.update(element, null);
			}
		});
		tableViewer.setInput(template);
		txt_db.setText(template.getDB());
		txt_startaddress.setText(template.getStartAddress());
		txt_StructLength.setText(template.getStructLength());
	}

	private void createDeviceCounterTable(final TreeViewer viewer) {
		TreeViewerColumn tvc_gruppe = TableWidgetUtil.createTreeColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.group"), 100,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((AdvancedConfigurationNode) element).getGroupName();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		// TreeColumn tblclmnDevice = tvc_gruppe.getColumn();
		// tblclmnDevice.setWidth(100);
		// tblclmnDevice.setText("Gruppe");
		tvc_gruppe.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					return ((AdvancedConfigurationNode) element).getGroupName();
				}
				return super.getText(element);
			}
		});
		tvc_gruppe.setEditingSupport(new EditingSupport(viewer) {
			@Override
			protected void setValue(Object element, Object value) {
				((AdvancedConfigurationNode) element).setGroupName((String) value);
				viewer.refresh(element);
				editor.setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return "" + ((AdvancedConfigurationNode) element).getGroupName();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (((AdvancedConfigurationNode) element).isGroup()) {
					TextCellEditor editor = new TextCellEditor(viewer.getTree());
					return editor;
				}
				return null;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TreeViewerColumn tvc_device = TableWidgetUtil.createTreeColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.device"), 100);
		// TreeColumn tblclmnNodedevice = tvc_device.getColumn();
		// tblclmnNodedevice.setWidth(100);
		// tblclmnNodedevice.setText("Device");
		tvc_device.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					return ((AdvancedConfigurationNode) element).getDeviceName();
				}
				return super.getText(element);
			}
		});
		tvc_device.setEditingSupport(new EditingSupport(viewer) {
			@Override
			protected void setValue(Object element, Object value) {
				((AdvancedConfigurationNode) element).setDeviceName((String) value);
				viewer.refresh(element);
				editor.setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return "" + ((AdvancedConfigurationNode) element).getDeviceName();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (((AdvancedConfigurationNode) element).isDevice()) {
					TextCellEditor editor = new TextCellEditor(viewer.getTree());
					return editor;
				}
				return null;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TreeViewerColumn tvc_counter = TableWidgetUtil.createTreeColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.counter"), 100);
		// TreeColumn tblclmnNodecounter = tvc_counter.getColumn();
		// tblclmnNodecounter.setWidth(100);
		// tblclmnNodecounter.setText("Counter");
		tvc_counter.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					return ((AdvancedConfigurationNode) element).getCounter();
				}
				return super.getText(element);
			}
		});
		/**
		 * selection change
		 */
		viewer.getTree().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentSelection = (AbstractConfigNode) e.item.getData();
				selectionChanged();
			}
		});
	}

	private void createDeviceMappingTable(final TableViewer viewer) {
		TableViewerColumn tvc_identification = TableWidgetUtil.createTableColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.identification"),
				100);
		// TableColumn tblclmnDevice_1 = tvc_identification.getColumn();
		// tblclmnDevice_1.setWidth(100);
		// tblclmnDevice_1.setText("Identifikation");
		tvc_identification.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					Integer value = ((AdvancedConfigurationNode) element).getValue();
					if (value != null) {
						return value.toString();
					}
					return "";
				}
				return super.getText(element);
			}
		});
		tvc_identification.setEditingSupport(new EditingSupport(viewer) {
			@Override
			protected void setValue(Object element, Object value) {
				((AdvancedConfigurationNode) element).setValue((Integer) value);
				viewer.refresh(element);
				editor.setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return "" + ((AdvancedConfigurationNode) element).getValue();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				IntegerCellEditor editor = new IntegerCellEditor(viewer.getTable());
				// editor.setValidator(new ICellEditorValidator() {
				//
				// @Override
				// public String isValid(Object value) {
				// try {
				// Integer.parseInt((String) value);
				// return null;
				// } catch (NumberFormatException nfe) {
				// nfe.printStackTrace();
				// }
				//
				// return "error";
				// }
				// });
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tvc_device = TableWidgetUtil.createTableColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.device"), 150,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}

					@Override
					public Object getComparableObject(Object element) {
						return ((AdvancedConfigurationNode) element).getDeviceName();
					}
				});
		tvc_device.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					return ((AdvancedConfigurationNode) element).getDeviceName();
				}
				return super.getText(element);
			}
		});
		TableViewerColumn tvc_enable = TableWidgetUtil.createTableColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.enable"), 150,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((AdvancedConfigurationNode) element).getEnableId();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((NodeId) o1).compareTo((NodeId) o2);
					}
				});
		tvc_enable.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					NodeId enableId = ((AdvancedConfigurationNode) element).getEnableId();
					if (!NodeId.isNull(enableId)) {
						return enableId.toString();
					} else {
						return "";
					}
				}
				return super.getText(element);
			}
		});
		tvc_enable.setEditingSupport(new EditingSupport(viewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(viewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((AdvancedConfigurationNode) element).isEnable();
			}

			protected void setValue(Object element, Object value) {
				editor.setDirty(true);
				((AdvancedConfigurationNode) element).setEnable(!((AdvancedConfigurationNode) element).isEnable());
				viewer.refresh(element);
			}
		});
		TableViewerColumn tvc_addon = TableWidgetUtil.createTableColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.addon"), 150,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((AdvancedConfigurationNode) element).getAddonId();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((NodeId) o1).compareTo((NodeId) o2);
					}
				});
		tvc_addon.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					NodeId addonId = ((AdvancedConfigurationNode) element).getAddonId();
					if (!NodeId.isNull(addonId)) {
						return addonId.toString();
					} else {
						return "";
					}
				}
				return super.getText(element);
			}
		});
		TableViewerColumn tableViewerColumn_4 = TableWidgetUtil.createTableColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.group"), 150,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((AdvancedConfigurationNode) element).getGroupId();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((NodeId) o1).compareTo((NodeId) o2);
					}
				});
		// TableColumn tblclmnNodeAddon = tableViewerColumn_4.getColumn();
		// tblclmnNodeAddon.setWidth(150);
		// tblclmnNodeAddon.setText("Gruppe");
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					NodeId groupId = ((AdvancedConfigurationNode) element).getGroupId();
					if (!NodeId.isNull(groupId)) {
						return groupId.toString();
					} else {
						return "";
					}
				}
				return super.getText(element);
			}
		});
		TableViewerColumn tvc_meter = TableWidgetUtil.createTableColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.meter"), 150,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((AdvancedConfigurationNode) element).getMeterId();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((NodeId) o1).compareTo((NodeId) o2);
					}
				});
		// TableColumn tblclmnMeter = tableViewerColumn_5.getColumn();
		// tblclmnMeter.setWidth(150);
		// tblclmnMeter.setText("Meter");
		tvc_meter.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					NodeId meterId = ((AdvancedConfigurationNode) element).getMeterId();
					if (!NodeId.isNull(meterId)) {
						return meterId.toString();
					} else {
						return "";
					}
				}
				return super.getText(element);
			}
		});
		/**
		 * selection change
		 */
		viewer.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentSelection = (AbstractConfigNode) e.item.getData();
				selectionChanged();
			}
		});
	}

	private void createDeviceConfigTable(final TableViewer viewer) {
		TableViewerColumn tvc_active = TableWidgetUtil.createTableColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"), 35);
		// TableColumn tblclmnActive_1 = tvc_active.getColumn();
		// tblclmnActive_1.setWidth(35);
		// tblclmnActive_1.setText("Active");
		tvc_active.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				if (!(element instanceof AdvancedConfigurationNode)) {
					return super.getImage(element);
				}
				if (((AdvancedConfigurationNode) element).isActive()) {
					return UtilsUIActivator.getDefault().getImageRegistry().get(SharedImages.ICON_CHECKED_1);
				} else {
					return UtilsUIActivator.getDefault().getImageRegistry().get(SharedImages.ICON_CHECKED_0);
				}
			}
		});
		tvc_active.setEditingSupport(new EditingSupport(viewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(viewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((AdvancedConfigurationNode) element).isActive();
			}

			protected void setValue(Object element, Object value) {
				editor.setDirty(true);
				((AdvancedConfigurationNode) element).setActive(!((AdvancedConfigurationNode) element).isActive());
				viewer.refresh(element);
			}
		});
		TableViewerColumn tvc_device = TableWidgetUtil.createTableColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.device"), 250,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((AdvancedConfigurationNode) element).getDeviceName();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		// TableColumn tblclmnDevice = tvc_device.getColumn();
		// tblclmnDevice.setWidth(250);
		// tblclmnDevice.setText("Device");
		tvc_device.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					return ((AdvancedConfigurationNode) element).getDeviceName();
				}
				return super.getText(element);
			}
		});
		TableViewerColumn tvc_configuration = TableWidgetUtil.createTableColumn(viewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.configuration"), 250,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((AdvancedConfigurationNode) element).getValue();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((Integer) o1).compareTo((Integer) o2);
					}
				});
		// TableColumn tblclmnNodeid = tvc_configuration.getColumn();
		// tblclmnNodeid.setWidth(250);
		// tblclmnNodeid.setText("Konfiguration");
		tvc_configuration.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					Integer value = ((AdvancedConfigurationNode) element).getValue();
					if (value != null) {
						return value.toString();
					}
					return "";
				}
				return super.getText(element);
			}
		});
		tvc_configuration.setEditingSupport(new EditingSupport(viewer) {
			@Override
			protected void setValue(Object element, Object value) {
				((AdvancedConfigurationNode) element).setValue((Integer) value);
				viewer.refresh(element);
				editor.setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return "" + ((AdvancedConfigurationNode) element).getValue();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				IntegerCellEditor editor = new IntegerCellEditor(viewer.getTable());
				// editor.setValidator(new ICellEditorValidator() {
				//
				// @Override
				// public String isValid(Object value) {
				// try {
				// Integer.parseInt((String) value);
				// return null;
				// } catch (NumberFormatException nfe) {
				// // nfe.printStackTrace();
				// }
				//
				// return "error";
				// }
				// });
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		/**
		 * selection change
		 */
		viewer.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentSelection = (AbstractConfigNode) e.item.getData();
				selectionChanged();
			}
		});
	}

	private void createStartConfigTable(final TableViewer tableViewer) {
		TableViewerColumn tvc_active = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"), 35);
		// TableColumn tblclmnActive_1 = tvc_active.getColumn();
		// tblclmnActive_1.setWidth(35);
		// tblclmnActive_1.setText("Active");
		tvc_active.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				if (!(element instanceof AdvancedConfigurationNode)) {
					return super.getImage(element);
				}
				if (((AdvancedConfigurationNode) element).isActive()) {
					return UtilsUIActivator.getDefault().getImageRegistry().get(SharedImages.ICON_CHECKED_1);
				} else {
					return UtilsUIActivator.getDefault().getImageRegistry().get(SharedImages.ICON_CHECKED_0);
				}
			}
		});
		tvc_active.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((AdvancedConfigurationNode) element).isActive();
			}

			protected void setValue(Object element, Object value) {
				editor.setDirty(true);
				((AdvancedConfigurationNode) element).setActive(!((AdvancedConfigurationNode) element).isActive());
				// ((OmronTableItem)element).active = value.toString();
				tableViewer.refresh(element);
			}
		});
		TableViewerColumn tvc_device = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.device"), 100,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((AdvancedConfigurationNode) element).getDeviceName();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		// TableColumn tblclmnDevice = tvc_device.getColumn();
		// tblclmnDevice.setWidth(100);
		// tblclmnDevice.setText("Device");
		tvc_device.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					// ((SiemensStartConfigurationNode)element)
					return ((AdvancedConfigurationNode) element).getDeviceName();
				}
				return super.getText(element);
			}
		});
		TableViewerColumn tvc_configuration = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.configuration"), 100,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((AdvancedConfigurationNode) element).getConfigNodeName();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		// TableColumn tblclmnNodeid = tvc_configuration.getColumn();
		// tblclmnNodeid.setWidth(100);
		// tblclmnNodeid.setText("Konfiguration");
		tvc_configuration.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					// ((SiemensStartConfigurationNode)element)
					return ((AdvancedConfigurationNode) element).getConfigNodeName();
				}
				return super.getText(element);
			}
		});
		TableViewerColumn tvc_index = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.index"), 100);
		// TableColumn tblclmnIndex = tvc_index.getColumn();
		// tblclmnIndex.setWidth(100);
		// tblclmnIndex.setText("Index");
		tvc_index.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					// ((SiemensStartConfigurationNode)element)
					return ((AdvancedConfigurationNode) element).getIndex().toString();
				}
				return super.getText(element);
			}
		});
		tvc_index.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				((AdvancedConfigurationNode) element).setIndex(Integer.parseInt((String) value));
				tableViewer.refresh(element);
				editor.setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return "" + ((AdvancedConfigurationNode) element).getIndex();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor editor = new TextCellEditor(tableViewer.getTable());
				editor.setValidator(new ICellEditorValidator() {
					@Override
					public String isValid(Object value) {
						try {
							Integer.parseInt((String) value);
							return null;
						} catch (NumberFormatException nfe) {
							nfe.printStackTrace();
						}
						return "error";
					}
				});
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tvc_value = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.value"), 100);
		// TableColumn tblclmnValue = tvc_value.getColumn();
		// tblclmnValue.setWidth(100);
		// tblclmnValue.setText("Value");
		tvc_value.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof AdvancedConfigurationNode) {
					// ((SiemensStartConfigurationNode)element)
					return ((AdvancedConfigurationNode) element).getValue().toString();
				}
				return super.getText(element);
			}
		});
		tvc_value.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				((AdvancedConfigurationNode) element).setValue(Integer.parseInt((String) value));
				tableViewer.refresh(element);
				editor.setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return "" + ((AdvancedConfigurationNode) element).getValue();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor editor = new TextCellEditor(tableViewer.getTable());
				editor.setValidator(new ICellEditorValidator() {
					@Override
					public String isValid(Object value) {
						try {
							Integer.parseInt((String) value);
							return null;
						} catch (NumberFormatException nfe) {
							nfe.printStackTrace();
						}
						return "error";
					}
				});
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		/**
		 * selection change
		 */
		tableViewer.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentSelection = (AbstractConfigNode) e.item.getData();
				selectionChanged();
			}
		});
	}

	private void setSelectionToWidget() {
	}

	class AdvancedTableContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof AdvancedRootConfigurationNode) {
				return ((AdvancedRootConfigurationNode) inputElement).getChildren();
			}
			return new Object[0];
		}
	}

	class AdvancedTreeContentProvider implements ITreeContentProvider {
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object element) {
			if (element instanceof AdvancedRootConfigurationNode) {
				return ((AdvancedRootConfigurationNode) element).getChildren();
			}
			if (element instanceof AdvancedConfigurationNode) {
				return ((AdvancedConfigurationNode) element).getChildren();
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof AdvancedConfigurationNode) {
				return ((AdvancedConfigurationNode) element).getParent();
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
	}

	class DeviceMappingEditorDirtyListener implements ModifyListener {
		private DirtyListenerProperties property;

		public DeviceMappingEditorDirtyListener(DirtyListenerProperties property) {
			this.property = property;
		}

		@Override
		public void modifyText(ModifyEvent e) {
			Text src = (Text) e.getSource();
			AdvancedRootConfigurationNode root = (AdvancedRootConfigurationNode) tv_devMappings.getInput();
			if (root == null) {
				root = new AdvancedRootConfigurationNode(AdvancedSectionType.DeviceMapping);
				tv_devMappings.setInput(root);
			}
			switch (this.property) {
			case db:
				root.setDatablock(src.getText());
				break;
			case start:
				root.setStartAddress(src.getText());
				break;
			case group:
				root.setGroupRange(src.getText());
				break;
			case addon:
				root.setAddonRange(src.getText());
				break;
			case meterid:
				root.setMetaId(src.getText());
				break;
			}
			editor.setDirty(true);
		}
	};

	class StateContentProvider extends ArrayContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ConsumptionTemplate) {
				return ((ConsumptionTemplate) inputElement).getItems();
			}
			return new Object[0];
		}
	}

	class StateLabelProvider implements ITableLabelProvider {
		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (!(element instanceof AdvancedConfigurationNode)) {
				return "";
			}
			switch (columnIndex) {
			case 0:
				return ((AdvancedConfigurationNode) element).getConfigNodeName();
			case 1:
				NodeId nodeId = ((AdvancedConfigurationNode) element).getConfigNodeId();
				if (NodeId.isNull(nodeId)) {
					return NodeId.NULL.toString();
				}
				return nodeId.toString();
			case 2:
				return ((AdvancedConfigurationNode) element).getBrowsepath();
			case 3:
				return ((AdvancedConfigurationNode) element).getDeviceName();
			case 4:
				return ((AdvancedConfigurationNode) element).getRefStartAddress();
			case 5:
				return ((AdvancedConfigurationNode) element).getMapping().name();
			case 6:
				return ((AdvancedConfigurationNode) element).getDataType();
			}
			return "";
		}
	}

	protected static String getBrowsePath(NodeId nodeId) {
		Deque<BrowsePathElement> browsepathelems = OPCUABrowseUtils.getFullBrowsePath(nodeId,
				ServerInstance.getInstance().getServerInstance(), Identifiers.ObjectsFolder);
		String browsepath = "";
		for (BrowsePathElement element : browsepathelems) {
			if (element.getId().equals(Identifiers.ObjectsFolder)) {
				continue;
			}
			browsepath += "//" + element.getBrowsename().getName();
		}
		return browsepath;
	}

	public class ComputeSectionsHandler {
		private IOPCDriverConfigEditPart part;

		public ComputeSectionsHandler(IOPCDriverConfigEditPart part) {
			this.part = part;
		}

		public void compute() {
			part.computeSection();
		}
	}
}
