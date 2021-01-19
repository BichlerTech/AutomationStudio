package com.bichler.astudio.opcua.opcmodeler.wizards.opc.load;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.SharedImages;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ProjectSelectionPage extends WizardPage {
	private static final String NAME = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
			"wizard.selection.project.title");
	private static final String TITLE = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
			"wizard.selection.project.title");
	private static final String DESCRIPTION = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
			"wizard.selection.project.description");
	private TableViewer projectTree;
	private ISelection selection;
	private ModelSelectionPage _pageTwo;
	private Image img_proj;

	public ProjectSelectionPage() {
		super(NAME);
		setTitle(TITLE);
		setDescription(DESCRIPTION);
		this.img_proj = Activator.getImageDescriptor(SharedImages.ICON_FOLDER_PROJECT_16).createImage();
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout());
		setControl(container);
		this.projectTree = new TableViewer(container);
		this.projectTree.setContentProvider(new ProjectTableContentProvider());
		this.projectTree.setLabelProvider(new ProjectTableLabelProvider());
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		this.projectTree.setInput(root);
		initListeners();
	}

	private void initListeners() {
		this.projectTree.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
	}

	@Override
	public boolean isPageComplete() {
		ISelection selection = this.projectTree.getSelection();
		if (selection != null && !selection.isEmpty()) {
			if (!selection.equals(this.selection)) {
				this.selection = selection;
				this._pageTwo.inputChange();
			}
			return true;
		}
		return false;
	}

	public IStructuredSelection getSelection() {
		return (IStructuredSelection) this.selection;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (this.img_proj != null) {
			this.img_proj.dispose();
		}
	}

	class ProjectTableContentProvider extends ArrayContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IWorkspaceRoot) {
				return ((IWorkspaceRoot) inputElement).getProjects();
			}
			return new Object[0];
		}
	}

	class ProjectTableLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			if (element instanceof IProject) {
				return img_proj;
			}
			return super.getImage(element);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof IProject) {
				return ((IProject) element).getName();
			}
			return super.getText(element);
		}
	}

	public void setPageTwo(ModelSelectionPage pageTwo) {
		this._pageTwo = pageTwo;
	}
}
