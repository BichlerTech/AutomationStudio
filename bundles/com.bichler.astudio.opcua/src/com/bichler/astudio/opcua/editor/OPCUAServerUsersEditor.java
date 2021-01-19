package com.bichler.astudio.opcua.editor;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import opc.sdk.core.node.user.DBAuthority;
import opc.sdk.core.node.user.DBRole;
import opc.sdk.core.node.user.DBUser;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wb.swt.ResourceManager;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.editor.input.OPCUAServerUsersEditorInput;
import com.bichler.astudio.opcua.opcmodeler.singletons.type.INamespaceTableChange;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.OPCUAUserAccountWizard;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.OPCUAUserRoleWizard;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.UserRoleWizardDialog;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.addressspace.model.nosql.userauthority.NoSqlUtil;

public class OPCUAServerUsersEditor extends EditorPart implements INamespaceTableChange {
	public static final String ID = "com.bichler.astudio.opcua.editor.OPCUAServerUsersEditor"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	/**
	 * flag dirty
	 */
	private boolean isDirty = false;
	// main scroll editor composite
	private ScrolledComposite scrolledComposite;
	// main composite
	private Composite composite;
	private Section sctnRoles;
	private Section sctnUsers;
	private Section sctnConfiguration;
	private Composite composite_configuration;
	private Composite composite_users;
	private Composite composite_roles;
	private TableViewer tv_roles;
	private Button btn_remUser;
	private Button btn_addUser;
	private Button btn_addRole;
	private Button btn_remRole;
	private TableViewer tv_users;
	private Connection dbConnection;
	private Button btn_editRole;
	private Button btn_editUser;
	private Button cb_anonymous;
	private Button cb_unsecure;
	private Connection localConnection;

	public OPCUAServerUsersEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		// scrolled composite
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setMinWidth(700);
		scrolledComposite.setMinHeight(750);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		// main composite
		composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setMinSize(new Point(300, 600));
		scrolledComposite.setContent(this.composite);
		composite.setLayout(new GridLayout(1, false));
		// sections
		createPartConfiguration();
		createPartUser();
		createPartRole();
		// fill editor
		fillEditor();
		// set handlers
		setHandler();
		// compute size of sections
		computeSize();
		// fill default selection
		this.tv_roles.getTable().notifyListeners(SWT.Selection, new Event());
		this.tv_users.getTable().notifyListeners(SWT.Selection, new Event());
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doSave(IProgressMonitor monitor2) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(getSite().getShell());
		final boolean isAllowAnonymous = cb_anonymous.getSelection();
		final boolean isAllowUnsecure = cb_unsecure.getSelection();
		// save dirty roles
		final List<DBRole> roles = (List<DBRole>) tv_roles.getInput();
		final List<DBUser> users = (List<DBUser>) tv_users.getInput();
		IRunnableWithProgress run = new IRunnableWithProgress() {
			@Override
			public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					monitor.beginTask(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
							"editor.user.monitor.openconfig"), IProgressMonitor.UNKNOWN);
					monitor.setTaskName(
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.monitor.save")
									+ "...");
					persistConfig(isAllowAnonymous, isAllowUnsecure);
					final List<DBRole> deletedRoles = new ArrayList<>();
					final List<DBUser> deletedUsers = new ArrayList<>();
					doSaveRoles(roles, deletedRoles);
					doSaveUsers(users, deletedUsers);
					// update gui
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							setDirty(false);
							removeRefreshViewerRole(deletedRoles);
							removeRefreshViewerUser(deletedUsers);
						}
					});
				} finally {
					monitor.done();
				}
			}

			private void doSaveRoles(List<DBRole> roles, List<DBRole> deletedRoles) {
				// collect deleted roles
				for (DBRole role : roles) {
					if (role.isDeleted()) {
						// collect
						deletedRoles.add(role);
					}
				}
				// some roles are deleted
				if (!deletedRoles.isEmpty()) {
					try {
						// restart role ids
						NoSqlUtil.removeRolesFromDatabase(dbConnection);
						// remove node from nodes table for delete roles
						for (DBRole role : deletedRoles) {
							try {
								NoSqlUtil.removeNodesFromDatabase(dbConnection, role.getId());
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					// add all roles which are not deleted to the database
					for (DBRole role : roles) {
						if (!role.isDeleted()) {
							persistModifyRole(role, true);
						}
					}
				}
				// check for updated roles
				else {
					for (DBRole role : roles) {
						if (!role.isDeleted() && role.isModified()) {
							persistModifyRole(role, false);
						}
					}
				}
				// remove node from nodes table for delete roles
				// for (DBRole role : deletedRoles) {
				// try {
				// NoSqlUtil.removeNodesFromDatabase(dbConnection,
				// role.getId());
				// } catch (SQLException e) {
				// e.printStackTrace();
				// }
				// }
			}

			private void doSaveUsers(List<DBUser> users, List<DBUser> deletedUsers) {
				// save users
				for (DBUser user : users) {
					if (user.isDeleted()) {
						persistDeleteUser(user);
						deletedUsers.add(user);
					} else if (user.isModified()) {
						persistModifyUser(user);
					}
				}
			}
		};
		try {
			dialog.run(true, false, run);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// Initialize the editor part
		this.setSite(site);
		this.setInput(input);
		// init sqlite database file
		String dbUserPath = new Path(((OPCUAServerUsersEditorInput) input).getNode().getFilesystem().getRootPath())
				.append("users").append(NoSqlUtil.DB_USER).toOSString();
		String dbLocalPath = new Path(((OPCUAServerUsersEditorInput) input).getNode().getFilesystem().getRootPath())
				.append("users").append(NoSqlUtil.DB_LOCAL).toOSString();
		try {
			this.dbConnection = NoSqlUtil.createConnection(dbUserPath);
			NoSqlUtil.initializeUserManagementTables(this.dbConnection);
			this.localConnection = NoSqlUtil.createConnection(dbLocalPath);
			NoSqlUtil.initializeLocalUserManagementTables(this.localConnection);
		} catch (ClassNotFoundException e) {
			if (this.dbConnection != null) {
				try {
					this.dbConnection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (this.localConnection != null) {
				try {
					this.localConnection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw new PartInitException("Cannot open database driver class!", e);
		} catch (SQLException e) {
			if (this.dbConnection != null) {
				try {
					this.dbConnection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (this.localConnection != null) {
				try {
					this.localConnection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw new PartInitException("Cannot open database connection!", e);
		}
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void dispose() {
		NoSqlUtil.disconnect(this.dbConnection, this.localConnection);
		super.dispose();
	}

	private void fillEditor() {
		try {
			loadConfig();
			List<DBRole> roles = loadRoles();
			List<DBUser> users = loadUsers();
			NoSqlUtil.linkUsersWithRole(users, roles);
			tv_roles.setInput(roles);
			tv_users.setInput(users);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadConfig() throws SQLException {
		Map<String, String> attributes = NoSqlUtil.readConfiguration(this.dbConnection);
		String allowAnnonymous = attributes.get("ALLOW_ANNONYMOUS");
		if (allowAnnonymous != null) {
			this.cb_anonymous.setSelection(Boolean.parseBoolean(allowAnnonymous));
		}
		String allowUnsecure = attributes.get("ALLOW_UNSECURE");
		if (allowUnsecure != null) {
			this.cb_unsecure.setSelection(Boolean.parseBoolean(allowUnsecure));
		}
	}

	private List<DBRole> loadRoles() throws SQLException {
		return NoSqlUtil.readRoleTable(this.dbConnection);
	}

	private List<DBUser> loadUsers() throws SQLException {
		return NoSqlUtil.readUserTable(this.localConnection);
	}

	private void setHandler() {
		// initialize expansion listeners
		this.sctnConfiguration.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				computeSection();
			}
		});
		this.sctnRoles.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				computeSection();
			}
		});
		this.sctnUsers.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				computeSection();
			}
		});
		// initialize button handlers
		this.btn_addRole.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OPCUAUserRoleWizard wizard = new OPCUAUserRoleWizard();
				UserRoleWizardDialog dialog = new UserRoleWizardDialog(getSite().getShell(), wizard);
				int open = dialog.open();
				if (WizardDialog.OK == open) {
					String roleName = wizard.getRoleName();
					String description = wizard.getRoleDescription();
					Map<NodeId, DBAuthority> nodeAuthority = wizard.getNodeAuthorities();
					doAddRole(roleName, description, nodeAuthority);
				}
			}
		});
		this.btn_editRole.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = tv_roles.getSelection();
				DBRole role = (DBRole) ((IStructuredSelection) selection).getFirstElement();
				// is local stored
				Map<NodeId, DBAuthority> mapping = ((OPCUAServerUsersEditorInput) getEditorInput())
						.getDirtyRoleNodes(role);
				OPCUAUserRoleWizard wizard = new OPCUAUserRoleWizard(role, dbConnection, mapping);
				UserRoleWizardDialog dialog = new UserRoleWizardDialog(getSite().getShell(), wizard);
				int open = dialog.open();
				if (WizardDialog.OK == open) {
					Map<NodeId, DBAuthority> nodeAuthority = wizard.getNodeAuthorities();
					String roleName = wizard.getRoleName();
					String description = wizard.getRoleDescription();
					doEditRole(role, roleName, description, nodeAuthority);
				}
			}
		});
		this.btn_remRole.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isConfirm = MessageDialog.openConfirm(getSite().getShell(),
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.monitor.delete"),
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
								"editor.dialog.delete.role"));
				if (isConfirm) {
					IStructuredSelection selection = (IStructuredSelection) tv_roles.getSelection();
					Object[] dbRoles = selection.toArray();
					doRemoveRole(dbRoles);
				}
			}
		});
		this.btn_addUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				@SuppressWarnings("unchecked")
				List<DBRole> roles = (List<DBRole>) tv_roles.getInput();
				OPCUAUserAccountWizard wizard = new OPCUAUserAccountWizard(roles.toArray(new DBRole[0]));
				WizardDialog dialog = new WizardDialog(getSite().getShell(), wizard);
				int open = dialog.open();
				if (WizardDialog.OK == open) {
					DBRole[] role = wizard.getRoles();
					String username = wizard.getUsername();
					String password = wizard.getPassword();
					String description = wizard.getDescription();
					doAddUser(role, username, password, description);
				}
			}
		});
		this.btn_editUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				@SuppressWarnings("unchecked")
				List<DBRole> roles = (List<DBRole>) tv_roles.getInput();
				IStructuredSelection selection = (IStructuredSelection) tv_users.getSelection();
				DBUser user = (DBUser) selection.getFirstElement();
				OPCUAUserAccountWizard wizard = new OPCUAUserAccountWizard(roles.toArray(new DBRole[0]), user);
				UserRoleWizardDialog dialog = new UserRoleWizardDialog(getSite().getShell(), wizard);
				int open = dialog.open();
				if (WizardDialog.OK == open) {
					DBRole[] role = wizard.getRoles();
					String username = wizard.getUsername();
					String password = wizard.getPassword();
					String description = wizard.getDescription();
					doEditUser(user, role, username, password, description);
				}
			}
		});
		this.btn_remUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isConfirm = MessageDialog.openConfirm(getSite().getShell(),
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.monitor.delete"),
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
								"editor.dialog.delete.user"));
				if (isConfirm) {
					IStructuredSelection selection = (IStructuredSelection) tv_users.getSelection();
					Object[] dbUsers = selection.toArray();
					doRemoveUser(dbUsers);
				}
			}
		});
		this.tv_roles.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int count = ((Table) e.getSource()).getSelectionCount();
				if (count > 0) {
					btn_editRole.setEnabled(true);
					btn_remRole.setEnabled(true);
				} else {
					btn_editRole.setEnabled(false);
					btn_remRole.setEnabled(false);
				}
			}
		});
		this.tv_users.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int count = ((Table) e.getSource()).getSelectionCount();
				if (count > 0) {
					btn_editUser.setEnabled(true);
					btn_remUser.setEnabled(true);
				} else {
					btn_editUser.setEnabled(false);
					btn_remUser.setEnabled(false);
				}
			}
		});
		this.cb_anonymous.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
			}
		});
		this.cb_unsecure.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
			}
		});
	}

	/**
	 * Layouts the scroll bars
	 */
	private void computeSection() {
		Point minSize = computeSize();
		this.scrolledComposite.setMinSize(minSize);
		this.composite.layout(true);
	}

	/**
	 * Computes the size of the editor
	 * 
	 * @return min size for scroll bars
	 */
	private Point computeSize() {
		this.sctnConfiguration.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.sctnRoles.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.sctnUsers.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		return this.composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	private void createPartConfiguration() {
		// section users
		sctnConfiguration = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnConfiguration);
		sctnConfiguration.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.configuration"));
		sctnConfiguration.setExpanded(true);
		// composite users
		composite_configuration = new Composite(sctnConfiguration, SWT.NONE);
		formToolkit.adapt(composite_configuration);
		formToolkit.paintBordersFor(composite_configuration);
		sctnConfiguration.setClient(composite_configuration);
		composite_configuration.setLayout(new GridLayout(1, false));
		cb_anonymous = new Button(composite_configuration, SWT.CHECK);
		cb_anonymous.setSelection(true);
		formToolkit.adapt(cb_anonymous, true, true);
		cb_anonymous.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.allowanonym"));
		cb_unsecure = new Button(composite_configuration, SWT.CHECK);
		cb_unsecure.setSelection(true);
		formToolkit.adapt(cb_unsecure, true, true);
		cb_unsecure.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.allowunsecure"));
		// this.sctnConfiguration.setEnabled(false);
	}

	private void createPartUser() {
		// section users
		sctnUsers = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnUsers.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnUsers);
		sctnUsers.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.user"));
		sctnUsers.setExpanded(true);
		// composite users
		composite_users = new Composite(sctnUsers, SWT.NONE);
		formToolkit.adapt(composite_users);
		formToolkit.paintBordersFor(composite_users);
		sctnUsers.setClient(composite_users);
		composite_users.setLayout(new GridLayout(2, false));
		this.tv_users = new TableViewer(composite_users, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tv_users.getTable();
		table.setHeaderVisible(true);
		GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table_1.minimumHeight = 96;
		table.setLayoutData(gd_table_1);
		formToolkit.paintBordersFor(table);
		tv_users.setContentProvider(new UserContentProvider());
		// tv_users.setLabelProvider(new UserLabelProvider());
		TableViewerColumn tableViewerColumn_name = new TableViewerColumn(tv_users, SWT.NONE);
		TableColumn tblclmnName_1 = tableViewerColumn_name.getColumn();
		tblclmnName_1.setWidth(200);
		tblclmnName_1.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.name"));
		tableViewerColumn_name.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				super.update(cell);
			}

			@Override
			public String getText(Object element) {
				String name = ((DBUser) element).getModifiedUserName();
				if (name == null || name.isEmpty()) {
					name = ((DBUser) element).getUsername();
				}
				return name;
			}
		});
		TableViewerColumn tableViewerColumn_role = new TableViewerColumn(tv_users, SWT.NONE);
		TableColumn tblclmnRolle = tableViewerColumn_role.getColumn();
		tblclmnRolle.setWidth(200);
		tblclmnRolle.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.description"));
		tableViewerColumn_role.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				super.update(cell);
			}

			@Override
			public String getText(Object element) {
				return "" + ((DBUser) element).getDescription();
			}
		});
		Composite composite_2 = new Composite(composite_users, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		composite_2.setBounds(0, 0, 64, 64);
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		this.btn_addUser = new Button(composite_2, SWT.NONE);
		GridData gd_btn_addUser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_addUser.heightHint = 48;
		gd_btn_addUser.widthHint = 48;
		btn_addUser.setLayoutData(gd_btn_addUser);
		formToolkit.adapt(btn_addUser, true, true);
		btn_addUser.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.ADD));
		btn_editUser = new Button(composite_2, SWT.NONE);
		GridData gd_btn_editUser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_editUser.widthHint = 48;
		gd_btn_editUser.heightHint = 48;
		btn_editUser.setLayoutData(gd_btn_editUser);
		btn_editUser
				.setImage(ResourceManager.getPluginImage(CommonImagesActivator.PLUGIN_ID, "icons/common/32/edit.png"));
		formToolkit.adapt(btn_editUser, true, true);
		this.btn_remUser = new Button(composite_2, SWT.NONE);
		btn_remUser.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.DELETE));
		GridData gd_btn_remUser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_remUser.widthHint = 48;
		gd_btn_remUser.heightHint = 48;
		btn_remUser.setLayoutData(gd_btn_remUser);
		formToolkit.adapt(btn_remUser, true, true);
	}

	private void createPartRole() {
		// section role
		sctnRoles = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnRoles.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnRoles);
		sctnRoles.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.role"));
		sctnRoles.setExpanded(true);
		// composite role
		composite_roles = new Composite(sctnRoles, SWT.NONE);
		formToolkit.adapt(composite_roles);
		formToolkit.paintBordersFor(composite_roles);
		sctnRoles.setClient(composite_roles);
		composite_roles.setLayout(new GridLayout(2, false));
		tv_roles = new TableViewer(composite_roles, SWT.BORDER | SWT.FULL_SELECTION);
		Table table_roles = tv_roles.getTable();
		table_roles.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.minimumHeight = 96;
		table_roles.setLayoutData(gd_table);
		formToolkit.paintBordersFor(table_roles);
		tv_roles.setContentProvider(new RoleContentProvider());
		TableViewerColumn tableViewerColumn_name = new TableViewerColumn(tv_roles, SWT.NONE);
		TableColumn tblclmn_Name = tableViewerColumn_name.getColumn();
		tblclmn_Name.setWidth(250);
		tblclmn_Name.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.name"));
		tableViewerColumn_name.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				super.update(cell);
			}

			@Override
			public String getText(Object element) {
				return ((DBRole) element).getRolename();
			}
		});
		TableViewerColumn tableViewerColumn_description = new TableViewerColumn(tv_roles, SWT.NONE);
		TableColumn tblclmn_Description = tableViewerColumn_description.getColumn();
		tblclmn_Description.setWidth(250);
		tblclmn_Description.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.description"));
		tableViewerColumn_description.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				super.update(cell);
			}

			@Override
			public String getText(Object element) {
				return ((DBRole) element).getDescription();
			}
		});
		Composite composite_1 = new Composite(composite_roles, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		composite_1.setBounds(0, 0, 64, 64);
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		this.btn_addRole = new Button(composite_1, SWT.NONE);
		btn_addRole.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.ADD));
		GridData gd_btn_addRole = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_addRole.heightHint = 48;
		gd_btn_addRole.widthHint = 48;
		btn_addRole.setLayoutData(gd_btn_addRole);
		formToolkit.adapt(btn_addRole, true, true);
		btn_editRole = new Button(composite_1, SWT.NONE);
		GridData gd_btn_editRole = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_editRole.heightHint = 48;
		gd_btn_editRole.widthHint = 48;
		btn_editRole.setLayoutData(gd_btn_editRole);
		formToolkit.adapt(btn_editRole, true, true);
		btn_editRole
				.setImage(ResourceManager.getPluginImage(CommonImagesActivator.PLUGIN_ID, "icons/common/32/edit.png"));
		this.btn_remRole = new Button(composite_1, SWT.NONE);
		btn_remRole.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.DELETE));
		GridData gd_btn_remRole = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_remRole.heightHint = 48;
		gd_btn_remRole.widthHint = 48;
		btn_remRole.setLayoutData(gd_btn_remRole);
		formToolkit.adapt(btn_remRole, true, true);
	}

	void removeRefreshViewerRole(List<DBRole> roles) {
		for (DBRole role : roles) {
			((ArrayList<DBRole>) tv_roles.getInput()).remove(role);
			// tv_roles.remove(role);
		}
		tv_roles.getTable().notifyListeners(SWT.Selection, new Event());
	}

	void removeRefreshViewerUser(List<DBUser> users) {
		for (DBUser user : users) {
			((ArrayList<DBUser>) tv_users.getInput()).remove(user);
		}
		tv_users.getTable().notifyListeners(SWT.Selection, new Event());
	}

	protected void doAddRole(String roleName, String description, Map<NodeId, DBAuthority> nodeAuthority) {
		DBRole newRole = new DBRole(roleName);
		newRole.setDescription(description);
		@SuppressWarnings("unchecked")
		List<DBRole> input = (List<DBRole>) this.tv_roles.getInput();
		input.add(newRole);
		this.tv_roles.refresh();
		setDirty(true);
		// cache node information to the role
		OPCUAServerUsersEditorInput editorInput = (OPCUAServerUsersEditorInput) getEditorInput();
		editorInput.addDirtyRoleNodes(newRole, nodeAuthority);
	}

	protected void doAddUser(DBRole[] roles, String username, String password, String description) {
		DBUser newUser = new DBUser(username, password, roles);
		newUser.setDescription(description);
		@SuppressWarnings("unchecked")
		List<DBUser> input = (List<DBUser>) this.tv_users.getInput();
		input.add(newUser);
		this.tv_users.refresh();
		setDirty(true);
	}

	protected void doEditRole(DBRole role, String roleName, String description,
			Map<NodeId, DBAuthority> nodeAuthority) {
		role.setRolename(roleName);
		role.setDescription(description);
		role.setModified(true);
		this.tv_roles.refresh(true);
		setDirty(true);
		// cache node information to the role
		OPCUAServerUsersEditorInput editorInput = (OPCUAServerUsersEditorInput) getEditorInput();
		editorInput.addDirtyRoleNodes(role, nodeAuthority);
	}

	protected void doEditUser(DBUser user, DBRole[] roles, String username, String password, String description) {
		user.setModifiedUserName(username);
		// keep original username
		// user.setUsername(username);
		user.setPassword(password);
		user.setRoles(roles);
		user.setDescription(description);
		user.setModified(true);
		this.tv_users.refresh(true);
		setDirty(true);
	}

	protected void doRemoveRole(Object[] dbRoles) {
		for (Object role : dbRoles) {
			((DBRole) role).setDeleted(true);
		}
		tv_roles.refresh();
		setDirty(true);
		// do we have additional users
		if (tv_roles.getTable().getItems().length == 0) {
			// deaktivate edit and delete buttons
			btn_editRole.setEnabled(false);
			btn_remRole.setEnabled(false);
		}
	}

	protected void doRemoveUser(Object[] dbUsers) {
		for (Object user : dbUsers) {
			((DBUser) user).setRemoved(true);
		}
		tv_users.refresh();
		setDirty(true);
		// do we have additional users
		if (tv_users.getTable().getItems().length == 0) {
			// deaktivate edit and delete buttons
			btn_editUser.setEnabled(false);
			btn_remUser.setEnabled(false);
		}
	}

	protected void persistConfig(boolean isAllowAnonymous, boolean isAllowSecure) {
		try {
			NoSqlUtil.writeConfiguration(this.dbConnection, isAllowAnonymous, isAllowSecure);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes an user from the LOCAL and CONFIGURATION database.
	 * 
	 * @param user
	 */
	protected void persistDeleteUser(DBUser user) {
		try {
			NoSqlUtil.removeUserFromDatabase(localConnection, user);
			NoSqlUtil.removeUserFromDatabase(dbConnection, user);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Adds or updates a role to/from a database.
	 * 
	 * @param Role Role to add/update.
	 */
	protected void persistModifyRole(DBRole role, boolean force) {
		// add role to database and set the id
		OPCUAServerUsersEditorInput input = (OPCUAServerUsersEditorInput) getEditorInput();
		int roleid = role.getId();
		try {
			int result = NoSqlUtil.writeOrUpdateRoleToDatabase(dbConnection, role, force);
			// update role
			if (result > 0) {
				role.setModified(false);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// use dirty input
		// add opc ua nodes with the authority
		Map<NodeId, DBAuthority> nodes2add = input.getDirtyRoleNodes(role);
		try {
			// no add/edit
			if (nodes2add == null) {
				// replace
				nodes2add = NoSqlUtil.readOPCNodesFromRoles(dbConnection,
						ServerInstance.getInstance().getServerInstance().getNamespaceUris(), roleid);
				// skip no nodes to add
				if (nodes2add == null) {
					return;
				}
				// remove
				NoSqlUtil.removeNodesFromDatabase(dbConnection, roleid);
			}
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			// add nodes to database
			NoSqlUtil.writeNodeWithAuthorityToDatabase(dbConnection, nsTable, role.getId(), nodes2add);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// remove nodes from cache
		input.removeDirtyRoleNodes(role);
	}

	protected void persistModifyUser(DBUser user) {
		try {
			int result = NoSqlUtil.writeOrUpdateUserToDatabase(this.localConnection, this.dbConnection, user);
			// result > 0 means that the sql operation was successful
			if (result > 0) {
				user.setModified(false);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	protected void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	class RoleContentProvider extends ArrayContentProvider {
		@SuppressWarnings("rawtypes")
		@Override
		public Object[] getElements(Object inputElement) {
			List<Object> elements = new ArrayList<>();
			for (Object obj : ((List) inputElement)) {
				if (((DBRole) obj).isDeleted()) {
					continue;
				}
				elements.add(obj);
			}
			return elements.toArray();
		}
	}

	class UserContentProvider extends ArrayContentProvider {
		@SuppressWarnings("rawtypes")
		@Override
		public Object[] getElements(Object inputElement) {
			List<Object> elements = new ArrayList<>();
			for (Object obj : ((List) inputElement)) {
				if (((DBUser) obj).isDeleted()) {
					continue;
				}
				elements.add(obj);
			}
			return elements.toArray();
		}
	}

	@Override
	public void onNamespaceChange(NamespaceTableChangeParameter trigger) {
	}
}
