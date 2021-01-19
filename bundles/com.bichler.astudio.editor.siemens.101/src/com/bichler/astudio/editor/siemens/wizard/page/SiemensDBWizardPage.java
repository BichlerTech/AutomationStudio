package com.bichler.astudio.editor.siemens.wizard.page;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.editor.siemens.SiemensActivator;
import com.bichler.astudio.editor.siemens.SiemensSharedImages;
import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.model.AbstractSiemensNode;
import com.bichler.astudio.editor.siemens.model.SiemensNodeFactory;
import com.bichler.astudio.editor.siemens.model.SiemensNodeFactory2;
import com.bichler.astudio.editor.siemens.wizard.ExportType;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class SiemensDBWizardPage extends WizardPage {
	private static final String[] FILTER_NAME = { "Siemens Datapoints Files (*.csv)",
			"Siemens Datapoints Files (*.txt)" };
	public static final String[] FILTER_EXTS = { "*.csv", "*.txt" };
	private Text txt_path;
	private IFileSystem filesystem;
	private SiemensDBResourceManager structManager;
	private SiemensImportWizardPage2 importPage;
	private String path;
	private SiemensImportTypeWizardPage importType;
	private SiemensNamespacePage namespacePage;
	private boolean success = true;

	/**
	 * Create the wizard.
	 * 
	 * @param structManager
	 * @param filesystem
	 */
	public SiemensDBWizardPage(IFileSystem filesystem, SiemensDBResourceManager structManager) {
		super("wizardPage");
		setTitle(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"siemens.wizard.datapoints.title"));
		setDescription(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"siemens.wizard.datapionts.description"));
		this.filesystem = filesystem;
		this.structManager = structManager;
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));
		Label lblUDT = new Label(container, SWT.NONE);
		lblUDT.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUDT.setText("DB");
		txt_path = new Text(container, SWT.BORDER);
		txt_path.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Button btnBrowse = new Button(container, SWT.NONE);
		btnBrowse.setToolTipText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.wizard.import.page.importdpfile"));
		btnBrowse.setImage(SiemensSharedImages.getImage(SiemensSharedImages.ICON_SEARCH));
		btnBrowse.addSelectionListener(new SelectionAdapter() {
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
				txt_path.setText(csv);
				path = csv;
				// IEditorPart activeEditor =
				// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				// .getActiveEditor();
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
								ExportType type = importType.getType();
								AbstractSiemensNode root = null;
								switch (type) {
								case Default:
									SiemensNodeFactory nodeFactory = new SiemensNodeFactory();
									root = nodeFactory.parseCSV(
											new BufferedReader(new InputStreamReader(symboTableFile)), true,
											structManager);
									break;
								case Tia:
									SiemensNodeFactory2 nodeFactory2 = new SiemensNodeFactory2();
									root = nodeFactory2.parseCSV(csv,
											new BufferedReader(new InputStreamReader(symboTableFile)), true,
											structManager);
									break;
								}
								monitor.worked(6);
								final AbstractSiemensNode root2 = root;
								Display.getDefault().syncExec(new Runnable() {
									@Override
									public void run() {
										importPage.setTreeRoot(root2);
										Path nname = new Path(csv);
										String ls = nname.lastSegment();
										String fext = nname.getFileExtension();
										int index = ls.indexOf(fext) - 1;
										namespacePage.setStartNodeName(ls.substring(0, index));
										success = true;
									}
								});
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e2) {
								e2.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
								Display.getDefault().syncExec(new Runnable() {
									@Override
									public void run() {
										txt_path.setText("");
									}
								});
								path = "";
								success = false;
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
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});
	}

	@Override
	public boolean isPageComplete() {
		if (path == null) {
			return false;
		}
		if (path.isEmpty()) {
			return false;
		}
		if (!success) {
			return false;
		}
		return true;
	}

	public String getPath() {
		return this.path;
	}

	public void setImportPage(SiemensImportWizardPage2 page) {
		this.importPage = page;
	}

	public void setImportTypePage(SiemensImportTypeWizardPage page) {
		this.importType = page;
	}

	public void setNamespacePage(SiemensNamespacePage page) {
		this.namespacePage = page;
	}
}
