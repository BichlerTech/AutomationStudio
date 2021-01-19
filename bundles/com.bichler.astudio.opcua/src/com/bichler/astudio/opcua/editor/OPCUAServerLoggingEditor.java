package com.bichler.astudio.opcua.editor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.editor.input.OPCUAServerLoggingEditorInput;
import com.bichler.astudio.opcua.log.LoggingModelManager;
import com.bichler.astudio.opcua.log.LoggingModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;

public class OPCUAServerLoggingEditor extends EditorPart {
	public static final String ID = "com.bichler.astudio.opcua.editor.OPCUAServerLoggingEditor"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	/**
	 * flag dirty
	 */
	private boolean isDirty = false;
	// main scroll editor composite
	private ScrolledComposite scrolledComposite;
	// main composite
	private Composite composite;
	private TreeViewer treeViewer;
	/** Managing all logging resources */
	private LoggingModelManager manager;
	private Button btnAdd;
	private Button btnRemove;

	public OPCUAServerLoggingEditor() {
		this.manager = new LoggingModelManager();
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Form frmNewForm = formToolkit.createForm(parent);
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.log.form.title"));
		frmNewForm.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));
		// scrolled composite
		scrolledComposite = new ScrolledComposite(frmNewForm.getBody(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setMinWidth(700);
		scrolledComposite.setMinHeight(750);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		// main composite
		composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scrolledComposite.setMinSize(new Point(300, 400));
		scrolledComposite.setContent(this.composite);
		composite.setLayout(new GridLayout(1, false));
		Group group = new Group(composite, SWT.NONE);
		formToolkit.adapt(group);
		formToolkit.paintBordersFor(group);
		group.setLayout(new GridLayout(2, false));
		this.btnAdd = new Button(group, SWT.NONE);
		GridData gd_btnAdd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAdd.heightHint = 48;
		gd_btnAdd.widthHint = 48;
		btnAdd.setLayoutData(gd_btnAdd);
		formToolkit.adapt(btnAdd, true, true);
		// btnAdd.setText("+");
		btnAdd.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.ADD));
		this.btnRemove = new Button(group, SWT.NONE);
		GridData gd_btnRemove = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRemove.widthHint = 48;
		gd_btnRemove.heightHint = 48;
		btnRemove.setLayoutData(gd_btnRemove);
		formToolkit.adapt(btnRemove, true, true);
		// btnRemove.setText("-");
		btnRemove.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.DELETE));
		Label seperator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		seperator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(seperator, true, true);
		this.treeViewer = new TreeViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		Tree tableProperties = treeViewer.getTree();
		tableProperties.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(tableProperties);
		TreeViewerColumn tableViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn tblclmnKey = tableViewerColumn.getColumn();
		tblclmnKey.setWidth(250);
		tblclmnKey.setText("Keys");
		TreeViewerColumn tableViewerColumn_1 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn tblclmnValue = tableViewerColumn_1.getColumn();
		tblclmnValue.setWidth(250);
		tblclmnValue.setText("Values");
		treeViewer.setLabelProvider(new TableLabelProvider());
		treeViewer.setContentProvider(new LogContentProvider());
		// fill editor
		fillEditor();
		// set handlers
		setHandler();
		// compute size of sections
		computeSize();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor2) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(getSite().getShell());
		IRunnableWithProgress run = new IRunnableWithProgress() {
			@Override
			public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					// save logging properties
					manager.doSaveEntry(monitor);
					// update viewer
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							setDirty(false);
							// removeRefreshViewerRole(deletedRoles);
							// removeRefreshViewerUser(deletedUsers);
						}
					});
				} finally {
					monitor.done();
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
		// // init log.properties file
		IFileSystem filesystem = ((OPCUAServerLoggingEditorInput) input).getNode().getFilesystem();
		IPath path = new Path(filesystem.getRootPath()).append("log.properties");
		if (!filesystem.isFile(path.toOSString())) {
			throw new PartInitException("Cannot open logging properties!");
		}
		// initialize all logging resources
		this.manager.initialize(filesystem, path);
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
		super.dispose();
	}

	private void fillEditor() {
		@SuppressWarnings("unused")
		OPCUAServerLoggingEditorInput input = (OPCUAServerLoggingEditorInput) getEditorInput();
		this.treeViewer.setInput(this.manager);
	}

	private void setHandler() {
		this.btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
			}
		});
		this.btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
			}
		});
	}

	/**
	 * Computes the size of the editor
	 * 
	 * @return min size for scroll bars
	 */
	private Point computeSize() {
		return this.composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	protected void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	class LogContentProvider extends ArrayContentProvider implements ITreeContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getChildren(Object parentElement) {
			List<Object> elements = new ArrayList<>();
			if (parentElement instanceof LoggingModelManager) {
				Map<String, LoggingModelNode> model = ((LoggingModelManager) parentElement).getModel();
				for (Entry<String, LoggingModelNode> entry : model.entrySet()) {
					elements.add(entry);
				}
				return elements.toArray(new Entry[0]);
			} else if (parentElement instanceof Entry) {
				return ((Entry<String, LoggingModelNode>) parentElement).getValue().getChildren();
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
	}

	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@SuppressWarnings("unchecked")
		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				if (element instanceof Entry) {
					return ((Entry<String, LoggingModelNode>) element).getKey();
				} else if (element instanceof LoggingModelNode) {
					return ((LoggingModelNode) element).getKey();
				}
				break;
			case 1:
				if (element instanceof Entry) {
					return ((Entry<String, LoggingModelNode>) element).getValue().getValue();
				} else if (element instanceof LoggingModelNode) {
					return ((LoggingModelNode) element).getValue();
				}
				break;
			default:
				break;
			}
			return "-";
		}
	}
}
