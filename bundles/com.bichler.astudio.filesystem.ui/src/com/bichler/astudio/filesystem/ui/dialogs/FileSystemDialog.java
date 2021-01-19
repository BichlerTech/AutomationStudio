package com.bichler.astudio.filesystem.ui.dialogs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.ui.FilesystemUIActivator;
import com.bichler.astudio.filesystem.ui.FileSystemSharedImages;


public class FileSystemDialog extends Dialog {
	
	private IFileSystem filesystem = null;
	private String filepath = "";
	private boolean link = true;
	
	public void setFileSystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}
	@Override
	protected void okPressed() {
		super.okPressed();
	}

	private class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return FilesystemUIActivator.getDefault().getImage(FileSystemSharedImages.ICON_PALETTE_FOLDER);
		}
		public String getText(Object element) {
			return element.toString();
		}
	}
	private class TreeContentProvider implements ITreeContentProvider {
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}
		public Object[] getChildren(Object parentElement) {
			List<String> files = new ArrayList<String>();
			
			if(filesystem != null) {
				String[] tmpFiles;
				try {
					tmpFiles = filesystem.listDirs(parentElement.toString());
					if(tmpFiles != null) {
						for(String f : tmpFiles) {
							if(f.startsWith("..") || f.startsWith("."))
								continue;
							
							files.add(f);
						}
					}
				} catch (FileNotFoundException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				} catch (IOException e) {
				  Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
				
			}
			return files.toArray();
		}
		public Object getParent(Object element) {
			return null;
		}
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
	}

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public FileSystemDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.RESIZE);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
//		GridLayout gridLayout = (GridLayout) container.getLayout();
		
		TreeViewer treeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		
		GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
		gd_tree.widthHint = 177;
		tree.setLayoutData(gd_tree);
		
		treeViewer.setLabelProvider(new ViewerLabelProvider());
		treeViewer.setContentProvider(new TreeContentProvider());
		treeViewer.setInput("/");
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				TreeSelection selection = (TreeSelection) event.getSelection();

				String file = (String)selection.getFirstElement();
				filepath = file;
			}
		});

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(542, 483);
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public boolean isLink() {
		return link;
	}

	public void setLink(boolean link) {
		this.link = link;
	}
}
