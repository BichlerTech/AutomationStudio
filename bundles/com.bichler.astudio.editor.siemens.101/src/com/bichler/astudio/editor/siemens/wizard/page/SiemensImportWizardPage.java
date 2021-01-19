package com.bichler.astudio.editor.siemens.wizard.page;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;

import com.bichler.astudio.editor.siemens.SiemensActivator;
import com.bichler.astudio.editor.siemens.SiemensModelLabelProvider;
import com.bichler.astudio.editor.siemens.SiemensModelTreeContentProvider;
import com.bichler.astudio.editor.siemens.SiemensSharedImages;
import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.model.AbstractSiemensNode;
import com.bichler.astudio.editor.siemens.model.SiemensArrayNode;
import com.bichler.astudio.editor.siemens.model.SiemensNodeFactory;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.opc.driver.siemens.communication.SiemensAreaCode;

public class SiemensImportWizardPage extends WizardPage {
	private static final String[] FILTER_NAME = { "Siemens Datapoints Files (*.txt)",
			"Siemens Datapoints Files (*.csv)" };
	public static final String[] FILTER_EXTS = { "*.txt", "*.csv" };
	private Combo comboAddressType;
	private Text textAddress;
	private TreeViewer treeViewer;
	private IFileSystem filesystem = null;
	private SiemensNodeFactory nodeFactory;
	private AbstractSiemensNode input;
	private SiemensDBResourceManager structManager;

	public SiemensImportWizardPage(IFileSystem filesystem, SiemensDBResourceManager structManager) {
		super("siemens wizard page");
		this.filesystem = filesystem;
		this.structManager = structManager;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		composite_1.setLayout(new GridLayout(3, false));
		createImportSymTabButton(composite_1);
		Label lblAddressType = new Label(composite_1, SWT.NONE);
		lblAddressType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAddressType.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.addresstype"));
		this.comboAddressType = new Combo(composite_1, SWT.READ_ONLY);
		comboAddressType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblAddress = new Label(composite_1, SWT.NONE);
		lblAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAddress.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.address"));
		this.textAddress = new Text(composite_1, SWT.BORDER);
		textAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		createImportUDTButton(composite_1);
		ScrolledComposite scrolledComposite = new ScrolledComposite(container,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		treeViewer = new TreeViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		Tree tree = treeViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		treeViewer.setLabelProvider(new SiemensModelLabelProvider());
		treeViewer.setContentProvider(new SiemensModelTreeContentProvider());
		TreeViewerColumn trvclmn_selected = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmn_active = trvclmn_selected.getColumn();
		trclmn_active.setWidth(35);
		trclmn_active.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.active"));
		trvclmn_selected.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				if (((AbstractSiemensNode) element).isActive()) {
					return SiemensSharedImages.getImage(SiemensSharedImages.ICON_CHECKED_1);
				} else {
					return SiemensSharedImages.getImage(SiemensSharedImages.ICON_CHECKED_0);
				}
			}

			@Override
			public String getText(Object element) {
				return "";
			}
		});
		trvclmn_selected.setEditingSupport(new EditingSupport(treeViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(treeViewer.getTree());
			}

			protected Object getValue(Object element) {
				return ((AbstractSiemensNode) element).isActive();
			}

			protected void setValue(Object element, Object value) {
				((AbstractSiemensNode) element).setActive(!((AbstractSiemensNode) element).isActive());
				/** we need to set all child elements */
				// AbstractSiemensModelNode node = (AbstractSiemensModelNode)
				// element;
				// if (node.getChildren() != null &&
				// !node.getChildren().isEmpty()) {
				// // for(SiemensModelNode nnode.getChildren()
				// }
				// ((OmronTableItem)element).active = value.toString();
				treeViewer.refresh(element);
			}
		});
		TreeViewerColumn trvclmn_symbolname = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmn_symbolname = trvclmn_symbolname.getColumn();
		trclmn_symbolname.setWidth(167);
		trclmn_symbolname.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.dialog.validation.symbolname"));
		trvclmn_symbolname.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return ((AbstractSiemensNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((AbstractSiemensNode) element).getSymbolName();
			}
		});
		trvclmn_symbolname.setEditingSupport(new EditingSupport(treeViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(treeViewer.getTree());
			}

			protected Object getValue(Object element) {
				return ((AbstractSiemensNode) element).getSymbolName();
			}

			protected void setValue(Object element, Object value) {
				if (((AbstractSiemensNode) element).getSymbolName().compareTo(value.toString()) == 0) {
					return;
				}
				((AbstractSiemensNode) element).setSymbolName(value.toString());
				treeViewer.refresh(element);
			}
		});
		TreeViewerColumn trvclmn_address = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmn_address = trvclmn_address.getColumn();
		trclmn_address.setWidth(55);
		trclmn_address.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.index"));
		trvclmn_address.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((AbstractSiemensNode) element).getAddressIndex() + "";
			}
		});
		trvclmn_address.setEditingSupport(new EditingSupport(treeViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(treeViewer.getTree());
			}

			protected Object getValue(Object element) {
				return "" + ((AbstractSiemensNode) element).getAddressIndex();
			}

			protected void setValue(Object element, Object value) {
				if ((((AbstractSiemensNode) element).getAddressIndex() + "").compareTo(value.toString()) == 0) {
					/** do nothing */
					return;
				}
				try {
					((AbstractSiemensNode) element).setIndex((Float.parseFloat(value.toString())));
				} catch (NumberFormatException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				treeViewer.refresh(element);
			}
		});
		TreeViewerColumn trvclmn_datatype = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmn_datatype = trvclmn_datatype.getColumn();
		trclmn_datatype.setWidth(82);
		trclmn_datatype.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.datatype"));
		// final ComboBoxViewerCellEditor editor = new ComboBoxViewerCellEditor(
		// treeViewer.getTree());
		// editor.setContentProvider(new ArrayContentProvider());
		//
		// editor.setLabelProvider(new LabelProvider());
		//
		// List<String> v = new ArrayList<String>();
		// for (DATATYPE d : DATATYPE.values()) {
		// v.add(d.name());
		// }
		// editor.setInput(v.toArray(new String[v.size()]));
		trvclmn_datatype.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element == null || ((AbstractSiemensNode) element).getDataType() == null) {
					return "";
				}
				if (element instanceof SiemensArrayNode) {
					return ((SiemensArrayNode) element).getDataType();
				}
				return ((AbstractSiemensNode) element).getDataType();
			}
		});
		TreeViewerColumn trvclmn_description = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmn_description = trvclmn_description.getColumn();
		trclmn_description.setWidth(192);
		trclmn_description.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.description"));
		trvclmn_description.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((AbstractSiemensNode) element).getDescription();
			}
		});
		trvclmn_description.setEditingSupport(new EditingSupport(treeViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(treeViewer.getTree());
			}

			protected Object getValue(Object element) {
				return ((AbstractSiemensNode) element).getDescription();
			}

			protected void setValue(Object element, Object value) {
				if (((AbstractSiemensNode) element).getDescription().compareTo(value.toString()) == 0) {
					/** try to parse it to float */
					return;
				}
				try {
					Float.parseFloat(value.toString());
				} catch (NumberFormatException ex) {
					treeViewer.refresh(element);
					return;
				}
				((AbstractSiemensNode) element).setDescription(value.toString());
				treeViewer.refresh(element);
			}
		});
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		this.nodeFactory = new SiemensNodeFactory();
		this.registerPlatformTools();
		Composite bottomParent = new Composite(container, SWT.NONE);
		bottomParent.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		bottomParent.setLayout(new GridLayout(4, false));
		createButtonBar(bottomParent);
		// new Label(topParent, SWT.NONE);
	}

	private void createImportUDTButton(Composite parent) {
		Label lblAddressType = new Label(parent, SWT.NONE);
		lblAddressType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAddressType.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.wizard.import.page.structures"));
		final Text txtImportStruct = new Text(parent, SWT.BORDER);
		txtImportStruct.setEditable(false);
		txtImportStruct.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		Button importStructUdt = new Button(parent, SWT.PUSH);
		importStructUdt.setToolTipText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.wizard.import.page.importstructuresfile"));
		importStructUdt.setImage(SiemensSharedImages.getImage(SiemensSharedImages.ICON_GEARS));
		importStructUdt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				final String directory = dialog.open();
				if (directory == null || directory.isEmpty()) {
					/** do nothing, because the import command was canceled */
					return;
				}
				txtImportStruct.setText(directory);
				Job importJob = new Job("Importiere Strukturen") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						if (filesystem.isDir(directory)) {
							File directoryFile = new File(directory);
							String[] files = directoryFile.list(new FilenameFilter() {
								@Override
								public boolean accept(File file, String filename) {
									if (filename != null) {
										int index = filename.lastIndexOf(".");
										String extension = filename.substring(index + 1);
										if ("csv".equalsIgnoreCase(extension)) {
											return true;
										}
									}
									return false;
								}
							});
							if (files != null) {
								monitor.beginTask(
										CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
												"com.bichler.astudio.editor.siemens.wizard.import.page.import"),
										files.length);
								// cache structures
								Map<String, AbstractSiemensNode> structures = new HashMap<String, AbstractSiemensNode>();
								for (String file : files) {
									String csv = directory + File.separator + file;
									if (filesystem.isFile(csv)) {
										InputStream symboTableFile = null;
										try {
											symboTableFile = filesystem.readFile(csv);
											AbstractSiemensNode root = nodeFactory.parseCSV(
													new BufferedReader(new InputStreamReader(symboTableFile)), false,
													structManager);
											structures.put(root.getName(), root);
											monitor.worked(1);
										} catch (FileNotFoundException e1) {
											e1.printStackTrace();
										} catch (IOException e2) {
											e2.printStackTrace();
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											if (symboTableFile != null) {
												try {
													symboTableFile.close();
												} catch (IOException e1) {
													e1.printStackTrace();
												}
											}
										}
									}
								}
								structManager.addStructures(structures);
							}
						}
						monitor.done();
						return Status.OK_STATUS;
					}
				};
				importJob.setUser(true);
				importJob.schedule();
			}
		});
	}

	/**
	 * Creates a new button with the given id.
	 * <p>
	 * The <code>Dialog</code> implementation of this framework method creates a
	 * standard push button, registers it for selection events including button
	 * presses, and registers default buttons with its shell. The button id is
	 * stored as the button's client data. If the button id is
	 * <code>IDialogConstants.CANCEL_ID</code>, the new button will be accessible
	 * from <code>getCancelButton()</code>. If the button id is
	 * <code>IDialogConstants.OK_ID</code>, the new button will be accesible from
	 * <code>getOKButton()</code>. Note that the parent's layout is assumed to be a
	 * <code>GridLayout</code> and the number of columns in this layout is
	 * incremented. Subclasses may override.
	 * </p>
	 * 
	 * @param parent        the parent composite
	 * @param id            the id of the button (see
	 *                      <code>IDialogConstants.*_ID</code> constants for
	 *                      standard dialog button ids)
	 * @param label         the label from the button
	 * @param defaultButton <code>true</code> if the button is to be the default
	 *                      button, and <code>false</code> otherwise
	 * 
	 * @return the new button
	 * 
	 * @see #getCancelButton
	 * @see #getOKButton()
	 */
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		// increment the number of columns in the button bar
		// ((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		return button;
	}

	private void createButtonBar(Composite parentBottomBar) {
		createDeselectAllButton(parentBottomBar);
		createSelectAllButton(parentBottomBar);
	}

	private void createSelectAllButton(Composite parent) {
		Button selectAll = createButton(parent, 4096,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.wizard.import.page.selectall"),
				true);
		selectAll.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Object input = treeViewer.getInput();
				if (input == null) {
					return;
				}
				if (input instanceof AbstractSiemensNode) {
					((AbstractSiemensNode) input).setActiveAll(true);
				}
				treeViewer.refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void createDeselectAllButton(Composite parent) {
		Button deselectAll = createButton(parent, 4096,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.wizard.import.page.deselectall"),
				true);
		deselectAll.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Object input = treeViewer.getInput();
				if (input == null) {
					return;
				}
				if (input instanceof AbstractSiemensNode) {
					((AbstractSiemensNode) input).setActiveAll(false);
				}
				treeViewer.refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void createImportSymTabButton(Composite parent) {
		Button importdb = new Button(parent, SWT.PUSH);
		importdb.setToolTipText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.wizard.import.page.importdpfile"));
		importdb.setImage(SiemensSharedImages.getImage(SiemensSharedImages.ICON_IMPORT));
		// SiemensSharedImages.getImage(SiemensSharedImages.ICON_IMPORT));
		final Text txtImportDB = new Text(parent, SWT.BORDER);
		txtImportDB.setEditable(false);
		GridData gd_txtImportDB = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_txtImportDB.horizontalSpan = 2;
		txtImportDB.setLayoutData(gd_txtImportDB);
		importdb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/** open file to import */
				FileDialog dialog = new FileDialog(getShell());
				dialog.setFilterNames(FILTER_NAME);
				dialog.setFilterExtensions(FILTER_EXTS);
				final String csv = dialog.open();
				if (csv == null) {
					/** do nothing, because the import command was canceled */
					return;
				}
				txtImportDB.setText(csv);
				IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				Job importJob = new Job("Importiere Datenbaustein") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.siemens.wizard.import.page.import"), 10);
						if (filesystem.isFile(csv)) {
							monitor.worked(2);
							InputStream symboTableFile = null;
							try {
								symboTableFile = filesystem.readFile(csv);
								monitor.worked(1);
								final AbstractSiemensNode root = nodeFactory.parseCSV(
										new BufferedReader(new InputStreamReader(symboTableFile)), true, structManager);
								monitor.worked(6);
								Display.getDefault().syncExec(new Runnable() {
									@Override
									public void run() {
										String symbolName = root.getName();
										String symbolAddress = symbolName.replaceFirst(SiemensAreaCode.DB.name(), "")
												.trim();
										comboAddressType.removeAll();
										comboAddressType.add(SiemensAreaCode.DB.name());
										comboAddressType.select(0);
										textAddress.setText(symbolAddress);
										input = root;
										treeViewer.setInput(root);
									}
								});
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e2) {
								e2.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								if (symboTableFile != null) {
									try {
										symboTableFile.close();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}
							}
						}
						monitor.worked(1);
						monitor.done();
						return Status.OK_STATUS;
					}
				};
				importJob.setUser(true);
				importJob.schedule();
			}
		});
	}

	/**
	 * Context menu & registrate treeviewer as selectionprovider
	 */
	private void registerPlatformTools() {
		MenuManager menuManager = new MenuManager();
		final Menu menu = menuManager.createContextMenu(treeViewer.getTree());
		// Set the MenuManager
		/**
		 * As menu manager
		 */
		treeViewer.getTree().setMenu(menu);
		/**
		 * defined in plugin xml
		 */
		IMenuService mSvc = (IMenuService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(IMenuService.class);
		mSvc.populateContributionManager(menuManager, "siemensdpeditor.table.contextmenu");
		IWorkbenchPartSite site = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart()
				.getSite();
		site.registerContextMenu(menuManager, treeViewer);
		site.setSelectionProvider(treeViewer);
		// make the selection available
		// getSite().setSelectionProvider(tableViewer);
	}

	public AbstractSiemensNode getInput() {
		return this.input;
	}
}
