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
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.editor.siemens.SiemensActivator;
import com.bichler.astudio.editor.siemens.SiemensSharedImages;
import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.model.AbstractSiemensNode;
import com.bichler.astudio.editor.siemens.model.SiemensNodeFactory;
import com.bichler.astudio.editor.siemens.model.SiemensNodeFactory2;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class SiemensUDTWizardPage extends WizardPage {
	private Text txt_path;
	private IFileSystem filesystem;
	private SiemensDBResourceManager structManager;
	private String directory;
	private SiemensImportTypeWizardPage importPage;
	private List udtList;

	/**
	 * Create the wizard.
	 * 
	 * @param structManager
	 * @param filesystem
	 */
	public SiemensUDTWizardPage(IFileSystem filesystem, SiemensDBResourceManager structManager) {
		super("wizardPage");
		setTitle(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE, "siemens.wizard.udt.title"));
		setDescription(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"siemens.wizard.udt.description"));
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
		lblUDT.setText("UDT");
		txt_path = new Text(container, SWT.BORDER);
		txt_path.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Button btnBrowse = new Button(container, SWT.NONE);
		btnBrowse.setToolTipText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.wizard.import.page.importstructuresfile"));
		btnBrowse.setImage(SiemensSharedImages.getImage(SiemensSharedImages.ICON_SEARCH));
		this.udtList = new List(container, SWT.BORDER);
		udtList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				final String directory = dialog.open();
				if (directory == null || directory.isEmpty()) {
					/** do nothing, because the import command was canceled */
					return;
				}
				txt_path.setText(directory);
				SiemensUDTWizardPage.this.directory = directory;
				Job importJob = new Job("Importiere Strukturen") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						if (filesystem.isDir(directory)) {
							File directoryFile = new File(directory);
							final String[] files = directoryFile.list(new FilenameFilter() {
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
								// for (int i = 0; i < 2; i++) {
								for (String file : files) {
									Map<String, AbstractSiemensNode> structures = new HashMap<String, AbstractSiemensNode>();
									String csv = directory + File.separator + file;
									if (filesystem.isFile(csv)) {
										InputStream symboTableFile = null;
										try {
											symboTableFile = filesystem.readFile(csv);
											AbstractSiemensNode root = null;
											switch (importPage.getType()) {
											case Default:
												SiemensNodeFactory nodeFactory = new SiemensNodeFactory();
												root = nodeFactory.parseCSV(
														new BufferedReader(new InputStreamReader(symboTableFile)),
														false, structManager);
												break;
											case Tia:
												SiemensNodeFactory2 nodeFactory2 = new SiemensNodeFactory2();
												root = nodeFactory2.parseCSVUDT(csv,
														new BufferedReader(new InputStreamReader(symboTableFile)),
														false, structManager);
												break;
											}
											structures.put(root.getName().toUpperCase(), root);
											structManager.addStructures(structures);
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
							}
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									udtList.removeAll();
									if (files != null) {
										for (String file : files) {
											int index = file.lastIndexOf(".");
											String substring = file.substring(0, index);
											udtList.add(substring);
										}
									}
								}
							});
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

	public String getDirectory() {
		return this.directory;
	}

	public void setImportTypePage(SiemensImportTypeWizardPage page) {
		this.importPage = page;
	}
}
