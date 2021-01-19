package com.bichler.astudio.opcua.opcmodeler.views.modeldesignedit;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.jface.viewers.TreeViewerFocusCellManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ModelEditView extends ViewPart implements ISelectionListener {
	private TreeViewer treeViewer = null;

	@Override
	public void createPartControl(Composite parent) {
		createView(parent);
	}

	private void createView(Composite parent) {
		// GridLayoutFactory.fillDefaults().applyTo(parent);
		createTreeViewer(parent);
		// GridDataFactory.fillDefaults().applyTo(this.treeViewer.getControl());
	}

	private Composite createTreeViewer(Composite parent) {
		// selection
		getViewSite().getPage().addSelectionListener(this);
		// tree composite which contains the tree viewer
		Composite treeComposite = new Composite(parent, SWT.BORDER);
		// treelayout to set the tree viewer columns in %
		TreeColumnLayout treeLayout = new TreeColumnLayout();
		treeComposite.setLayout(treeLayout);
		// create the tree
		Tree attributesTree = new Tree(treeComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		attributesTree.setHeaderVisible(false);
		attributesTree.setLinesVisible(true);
		// create the treeviewer with its tree
		this.treeViewer = new TreeViewer(attributesTree);
		// make name column of the tree
		TreeViewerColumn columnName = new TreeViewerColumn(this.treeViewer, SWT.NONE);
		columnName.getColumn().setAlignment(SWT.LEFT);
		columnName.getColumn().setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.name"));
		treeLayout.setColumnData(columnName.getColumn(), new ColumnWeightData(40));
		// make value column of the teee
		TreeViewerColumn columnValue = new TreeViewerColumn(this.treeViewer, SWT.NONE);
		columnValue.getColumn().setAlignment(SWT.LEFT);
		columnValue.getColumn().setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariablePart.lbl_value.text"));
		treeLayout.setColumnData(columnValue.getColumn(), new ColumnWeightData(60));
		// editing support to the column
		columnValue.setEditingSupport(new ModelViewEditingSupport(this.treeViewer, 2));
		// making the activation strategy for the editors
		TreeViewerEditor.create(this.treeViewer, new ColumnViewerEditorActivationStrategy(this.treeViewer),
				ColumnViewerEditor.DEFAULT);
		// making cellFocus manger that reacts on key, mouse etc.
		TreeViewerFocusCellManager focusCellManager = new TreeViewerFocusCellManager(this.treeViewer,
				new FocusCellOwnerDrawHighlighter(this.treeViewer));
		// activation strategy for editor in cell which events he react
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(this.treeViewer) {
			protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};
		// create the editors with defined cellmanger and activation
		// strategy
		TreeViewerEditor.create(this.treeViewer, focusCellManager, actSupport, TreeViewerEditor.DEFAULT);
		this.treeViewer.setContentProvider(new NodeContentProvider());
		this.treeViewer.setLabelProvider(new TableLabelProvider());
		this.treeViewer.getTree().addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				treeViewer.editElement(((ITreeSelection) treeViewer.getSelection()).getFirstElement(), 1);
			}
		});
		return treeComposite;
	}

	@Override
	public void setFocus() {
		this.treeViewer.getControl().setFocus();
	}

	public void refresh() {
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!selection.isEmpty()) {
			treeViewer.setInput(((TreeSelection) selection).getFirstElement());
		}
	}
}
