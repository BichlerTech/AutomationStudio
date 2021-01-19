package com.bichler.astudio.opcua.opcmodeler.wizards.opc.load;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.SharedImages;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ModelSelectionPage extends WizardPage {
	private static final String NAME = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
			"wizard.selection.title");
	private static final String TITLE = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
			"wizard.selection.title");
	private static final String DESCRIPTION = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
			"wizard.selection.description");
	private CheckboxTableViewer projectTree;
	private Object[] checked = new Object[0];
	private ProjectSelectionPage _pageOne;
	private Image img_model;

	public ModelSelectionPage() {
		super(NAME);
		setTitle(TITLE);
		setDescription(DESCRIPTION);
		this.img_model = Activator.getImageDescriptor(SharedImages.ICON_MODEL_OPC_16).createImage();
	}

	public void setPageOne(ProjectSelectionPage pageOne) {
		_pageOne = pageOne;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (this.img_model != null) {
			this.img_model.dispose();
		}
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout());
		setControl(container);
		this.projectTree = CheckboxTableViewer.newCheckList(container, SWT.BORDER);
		this.projectTree.setContentProvider(new ModelTableContentProvider());
		this.projectTree.setLabelProvider(new ModelTableLabelProvider());
		/** add check listener */
		this.projectTree.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				CheckboxTableViewer viewer = (CheckboxTableViewer) event.getSource();
				checked = viewer.getCheckedElements();
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		IStructuredSelection project = _pageOne.getSelection();
		this.projectTree.setInput(project);
	}

	@Override
	public boolean isPageComplete() {
		if (this.checked != null && this.checked.length > 0) {
			return true;
		}
		return false;
	}

	public void inputChange() {
		IStructuredSelection project = _pageOne.getSelection();
		this.checked = new Object[0];
		this.projectTree.setInput(project);
	}

	public StructuredSelection getSelection() {
		return new StructuredSelection(this.checked);
	}

	class ModelTableContentProvider extends ArrayContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IStructuredSelection) {
				Object object = ((IStructuredSelection) inputElement).getFirstElement();
				if (object instanceof IProject) {
					IFolder srcgen = ((IProject) object).getFolder("src-gen");
					try {
						if (srcgen != null) {
							IResource[] files = srcgen.members();
							// filter only xml files, which are model files
							if (files != null) {
								List<IResource> models = new ArrayList<>();
								for (IResource file : files) {
									String extension = file.getFileExtension();
									if (extension != null && "xml".equals(extension)) {
										models.add(file);
									}
								}
								return models.toArray();
							}
						}
					} catch (CoreException e) {
						// ignore
					}
				}
			}
			return new Object[0];
		}
	}

	class ModelTableLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			if (element instanceof IResource) {
				return img_model;
			}
			return super.getImage(element);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof IResource) {
				return ((IResource) element).getName();
			}
			return super.getText(element);
		}
	}
}
