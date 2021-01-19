package com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTypDef;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.ModelValidationWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.AbstractContentValidationModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.MissingValidationModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.ObjectValidationModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.TypeValidationModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.ValidationModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.ValidationModelContentFactory;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ValidationModelPage extends WizardPage {
	private TreeViewer tvObjectModel;
	private TreeViewer tvTypeModel;
	private Text txt_objId;
	private Text txt_typeId;
	private Button btnMapping;
	private Button btnResetMapping;
	private Button btnRemoveMapping;
	private Button btnNewNode;

	/**
	 * Create the wizard.
	 */
	public ValidationModelPage() {
		super("wizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.validation"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.validation"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setBounds(0, 0, 64, 64);
		composite.setLayout(new GridLayout(2, false));
		txt_objId = new Text(composite, SWT.BORDER);
		txt_objId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_typeId = new Text(composite, SWT.BORDER);
		txt_typeId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.tvObjectModel = new TreeViewer(composite, SWT.BORDER);
		Tree treeObject = tvObjectModel.getTree();
		treeObject.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.tvTypeModel = new TreeViewer(composite, SWT.BORDER);
		Tree treeModel = tvTypeModel.getTree();
		treeModel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeModel.setBounds(0, 0, 85, 85);
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(4, true));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		this.btnMapping = new Button(composite_1, SWT.NONE);
		btnMapping.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnMapping.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.validation.connect"));
		this.btnNewNode = new Button(composite_1, SWT.NONE);
		btnNewNode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnNewNode.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.validation.newnode"));
		this.btnResetMapping = new Button(composite_1, SWT.NONE);
		btnResetMapping.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnResetMapping
				.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.validation.recover"));
		this.btnRemoveMapping = new Button(composite_1, SWT.NONE);
		btnRemoveMapping.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnRemoveMapping.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.delete"));
		this.tvObjectModel.setLabelProvider(new VMPLabelProvider());
		this.tvTypeModel.setLabelProvider(new VMPLabelProvider());
		this.tvObjectModel.setContentProvider(new VMPContentProvider(true));
		this.tvTypeModel.setContentProvider(new VMPContentProvider(false));
		setHandler();
	}

	private ModelValidationWizard getMVWizard() {
		return (ModelValidationWizard) getWizard();
	}

	private void init() {
		ModelValidationWizard wizard2 = (ModelValidationWizard) getWizard();
		ModelTypDef elementRoot = wizard2.getRootElementTypeDef();
		ModelTypDef typeDef = wizard2.getElementTypeDef();
		this.tvObjectModel.setInput(elementRoot);
		this.tvTypeModel.setInput(typeDef);
		// AbstractContentValidationModel rootObj = ((VMPContentProvider)
		// this.tvObjectModel
		// .getContentProvider()).getRootModel();
		// AbstractContentValidationModel rootType = ((VMPContentProvider)
		// this.tvTypeModel
		// .getContentProvider()).getRootModel();
		// this.tvObjectModel.expandAll();
		// this.tvTypeModel.expandAll();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			init();
		}
	}

	private void setHandler() {
		this.tvObjectModel.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				AbstractContentValidationModel selection = (AbstractContentValidationModel) ((StructuredSelection) event
						.getSelection()).getFirstElement();
				if (selection == null) {
					return;
				}
				txt_objId.setText(selection.getNodeId().toString());
				ExpandedNodeId mappingId = selection.getMappingId();
				if (mappingId != null) {
					AbstractContentValidationModel rootType = ((VMPContentProvider) tvTypeModel.getContentProvider())
							.getRootModel();
					try {
						ValidationModel typeSelection = rootType.findDeep(ServerInstance.getInstance()
								.getServerInstance().getNamespaceUris().toNodeId(mappingId));
						if (typeSelection != null) {
							tvTypeModel.setSelection(new StructuredSelection(typeSelection));
						} else {
							tvTypeModel.setSelection(null);
						}
					} catch (ServiceResultException e) {
						e.printStackTrace();
					}
				}
				setButtonMappingEnable();
				// setButtonNewNodeEnable();
			}
		});
		this.tvTypeModel.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				AbstractContentValidationModel selection = (AbstractContentValidationModel) ((StructuredSelection) event
						.getSelection()).getFirstElement();
				if (selection == null) {
					return;
				}
				txt_typeId.setText(selection.getNodeId().toString());
				setButtonMappingEnable();
			}
		});
		this.btnMapping.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ValidationModel objModel = (ValidationModel) ((IStructuredSelection) tvObjectModel.getSelection())
						.getFirstElement();
				ValidationModel typeModel = (ValidationModel) ((IStructuredSelection) tvTypeModel.getSelection())
						.getFirstElement();
				if (objModel instanceof MissingValidationModel) {
					objModel = redrawMissingValidationNode(objModel, typeModel);
				}
				NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
				objModel.setMappingId(new ExpandedNodeId(nsTable.getUri(typeModel.getNodeId().getNamespaceIndex()),
						typeModel.getNodeId().getValue(), nsTable));
				// objModel.setMappingId(nsTable.toExpandedNodeId(typeModel.getNodeId()));
				getMVWizard().getRuntimeMapping()
						.put(new ExpandedNodeId(nsTable.getUri(objModel.getNodeId().getNamespaceIndex()),
								objModel.getNodeId().getValue(), nsTable),
								new ExpandedNodeId(nsTable.getUri(typeModel.getNodeId().getNamespaceIndex()),
										typeModel.getNodeId().getValue(), nsTable));
				// getMVWizard().getRuntimeMapping().put(nsTable.toExpandedNodeId(objModel.getNodeId()),
				// nsTable.toExpandedNodeId(typeModel.getNodeId()));
				tvObjectModel.update(objModel, null);
				tvObjectModel.refresh(objModel.getParent());
				// setButtonNewNodeEnable();
			}
		});
		// this.btnNewNode.addSelectionListener(new SelectionAdapter() {
		//
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// ValidationModel objModel = (ValidationModel) ((IStructuredSelection)
		// tvObjectModel
		// .getSelection()).getFirstElement();
		//
		// MissingValidationModel missing = new MissingValidationModel(
		// NodeId.ZERO, NodeClass.Unspecified, LocalizedText
		// .english("Fehlender OPC Knoten"));
		// objModel.addChild(missing);
		//
		// tvObjectModel.refresh(objModel);
		//
		// setButtonNewNodeEnable();
		// }
		// });
		this.btnRemoveMapping.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		this.btnResetMapping.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
	}

	private ValidationModel redrawMissingValidationNode(ValidationModel objMissingModel, ValidationModel typeModel) {
		ObjectValidationModel objValidationModel = typeModel.redrawMissing((MissingValidationModel) objMissingModel,
				getMVWizard());
		ValidationModel parent = objMissingModel.getParent();
		int index = parent.getChildrenList().indexOf(objMissingModel);
		boolean removed = parent.getChildrenList().remove(objMissingModel);
		parent.addChild(index, objValidationModel);
		return objValidationModel;
	}

	private void setButtonNewNodeEnable() {
		AbstractContentValidationModel fElement = (AbstractContentValidationModel) ((IStructuredSelection) this.tvObjectModel
				.getSelection()).getFirstElement();
		ExpandedNodeId mapping = fElement.getMappingId();
		if (mapping != null) {
			AbstractContentValidationModel mr = ((VMPContentProvider) tvTypeModel.getContentProvider()).getRootModel();
			try {
				// get all children object nodes
				ValidationModel found = mr.findDeep(
						ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(mapping));
				if (found != null) {
					if (found.getChildren().length > fElement.getChildren().length) {
						this.btnNewNode.setEnabled(true);
						return;
					}
				}
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		// no mappng
		this.btnNewNode.setEnabled(false);
	}

	private void setButtonMappingEnable() {
		boolean emptyType = this.tvTypeModel.getSelection().isEmpty();
		boolean emptyObject = this.tvObjectModel.getSelection().isEmpty();
		if (!emptyType && !emptyObject) {
			this.btnMapping.setEnabled(true);
		} else {
			this.btnMapping.setEnabled(false);
		}
	}

	class VMPContentProvider implements ITreeContentProvider {
		private boolean isObject;
		private AbstractContentValidationModel model;

		public VMPContentProvider(boolean isObject) {
			this.isObject = isObject;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (isObject) {
				if (inputElement instanceof ModelTypDef) {
					try {
						ExpandedNodeId nodeId = ((ModelTypDef) inputElement).nodeId;
						AbstractContentValidationModel root = getMVWizard().getRuntimeModel().get(nodeId);
						// create existing model
						if (root == null) {
							root = new ObjectValidationModel(
									ServerInstance.getInstance().getServerInstance().getNamespaceUris()
											.toNodeId(nodeId),
									((ModelTypDef) inputElement).typeClass, ((ModelTypDef) inputElement).name);
							root.buildModel();
							getMVWizard().getRuntimeModel().put(nodeId, root);
						}
						this.model = root;
						return new ObjectValidationModel[] { (ObjectValidationModel) root };
					} catch (ServiceResultException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (inputElement instanceof ModelTypDef) {
					try {
						ExpandedNodeId nodeId = ((ModelTypDef) inputElement).nodeId;
						AbstractContentValidationModel root = getMVWizard().getRuntimeModel().get(nodeId);
						if (root == null) {
							root = new TypeValidationModel(
									ServerInstance.getInstance().getServerInstance().getNamespaceUris()
											.toNodeId(nodeId),
									((ModelTypDef) inputElement).typeClass, ((ModelTypDef) inputElement).name);
							root.buildModel();
							getMVWizard().getRuntimeModel().put(nodeId, root);
						}
						this.model = root;
						return new ValidationModel[] { (ValidationModel) root };
					} catch (ServiceResultException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			ValidationModel[] children = new ValidationModel[0];
			if (isObject) {
				List<ValidationModel> c = ((ValidationModel) parentElement).getChildrenList();
				updateChildren((ValidationModel) parentElement, c);
				children = c.toArray(new ValidationModel[0]);
			} else {
				children = ((ValidationModel) parentElement).getChildren();
			}
			return children;
		}

		@Override
		public Object getParent(Object element) {
			return ((ValidationModel) element).getParent();
		}

		@Override
		public boolean hasChildren(Object element) {
			return ((ValidationModel) element).getChildren().length > 0;
		}

		public AbstractContentValidationModel getRootModel() {
			return this.model;
		}

		private void updateChildren(ValidationModel parentElement, List<ValidationModel> children) {
			Map<NodeId, ExpandedNodeId> childMappings = new HashMap<>();
			Map<NodeId, ExpandedNodeId> objTypes = new HashMap<>();
			for (ValidationModel child : children) {
				ReferenceDescription[] result = ValidationModelContentFactory.browse(child.getNodeId(),
						BrowseDirection.Forward, Identifiers.HasTypeDefinition);
				// object exists in workspace
				if (result.length > 0) {
					ExpandedNodeId typeDef = result[0].getNodeId();
					objTypes.put(child.getNodeId(), typeDef);
				}
				// missing node, follow mapping path
				else {
					ExpandedNodeId mapping = child.getMappingId();
					if (mapping != null) {
						try {
							ReferenceDescription[] result2 = ValidationModelContentFactory.browse(ServerInstance
									.getInstance().getServerInstance().getNamespaceUris().toNodeId(mapping),
									BrowseDirection.Forward, Identifiers.HasTypeDefinition);
							// has a typedef
							if (result.length > 0) {
								objTypes.put(child.getNodeId(), result2[0].getNodeId());
							}
							// is nodeclass type itself
							else {
								objTypes.put(child.getNodeId(), mapping);
							}
						} catch (ServiceResultException e) {
							e.printStackTrace();
						}
					}
				}
				if (child.getMappingId() != null) {
					childMappings.put(child.getNodeId(), child.getMappingId());
				}
			}
			objectExistInTypes(parentElement, children, objTypes, childMappings);
		}
	}

	public void objectExistInTypes(ValidationModel parentElement, List<ValidationModel> children,
			Map<NodeId, ExpandedNodeId> objTypes, Map<NodeId, ExpandedNodeId> childMappings) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		ExpandedNodeId mappingId = parentElement.getMappingId();
		if (mappingId == null) {
			return;
		}
		VMPContentProvider cp = (VMPContentProvider) tvTypeModel.getContentProvider();
		AbstractContentValidationModel root = cp.getRootModel();
		try {
			ValidationModel found = root
					.findDeep(ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(mappingId));
			if (found != null) {
				ValidationModel[] typeChildren = found.getChildren();
				List<NodeId> typeChildrenIds = new ArrayList<>();
				for (ValidationModel child : typeChildren) {
					typeChildrenIds.add(child.getNodeId());
				}
				List<NodeId> mappedChildren = new ArrayList<>();
				List<NodeId> remainingTypes = new ArrayList<>();
				for (ValidationModel child : typeChildren) {
					ExpandedNodeId expChildId = new ExpandedNodeId(
							nsTable.getUri(child.getNodeId().getNamespaceIndex()), child.getNodeId().getValue(),
							nsTable);
					// ExpandedNodeId expChildId =
					// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
					// .toExpandedNodeId(child.getNodeId());
					NodeId childId = null;
					for (Entry<NodeId, ExpandedNodeId> e : childMappings.entrySet()) {
						if (e.getValue().equals(expChildId)) {
							childId = e.getKey();
							break;
						}
					}
					if (childId != null) {
						mappedChildren.add(childId);
					} else {
						ReferenceDescription[] result = ValidationModelContentFactory.browse(child.getNodeId(),
								BrowseDirection.Forward, Identifiers.HasTypeDefinition);
						for (ReferenceDescription refdesc : result) {
							NodeId typeId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
									.toNodeId(refdesc.getNodeId());
							remainingTypes.add(typeId);
							break;
						}
					}
				}
				boolean showMissingNode = showMissingNode(mappedChildren, remainingTypes, children);
				if (showMissingNode) {
					MissingValidationModel missing = new MissingValidationModel(NodeId.ZERO, null,
							LocalizedText.english(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
									"wizard.validation.missingnode")));
					parentElement.addChild(missing);
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	private boolean showMissingNode(List<NodeId> mappedChildren, List<NodeId> remainingTypes,
			List<ValidationModel> children) {
		Map<NodeId, NodeId> objChildrenTypes = new HashMap<>();
		for (ValidationModel child : children) {
			if (mappedChildren.contains(child.getNodeId())) {
				continue;
			}
			ReferenceDescription[] result = ValidationModelContentFactory.browse(child.getNodeId(),
					BrowseDirection.Forward, Identifiers.HasTypeDefinition);
			if (result.length > 0) {
				try {
					objChildrenTypes.put(child.getNodeId(), ServerInstance.getInstance().getServerInstance()
							.getNamespaceUris().toNodeId(result[0].getNodeId()));
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
			}
		}
		List<NodeId> availableTypesToConnect = new ArrayList<>();
		for (NodeId type : remainingTypes) {
			for (Entry<NodeId, NodeId> entry : objChildrenTypes.entrySet()) {
				ReferenceDescription[] result = ValidationModelContentFactory.browse(entry.getKey(),
						BrowseDirection.Forward, Identifiers.HasTypeDefinition);
				NodeId typeId = null;
				try {
					if (result.length > 0) {
						typeId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.toNodeId(result[0].getNodeId());
					} else {
						typeId = entry.getKey();
					}
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
				if (typeId.equals(type)) {
					availableTypesToConnect.add(entry.getValue());
				}
			}
		}
		// nothing to map, prepare a missing node
		if (availableTypesToConnect.isEmpty() && !remainingTypes.isEmpty()) {
			return true;
		}
		// there is a node to map
		return false;
	}

	class VMPLabelProvider extends StyledCellLabelProvider {
		@Override
		public void update(ViewerCell cell) {
			Object element = cell.getElement();
			Styler styler = new Styler() {
				@Override
				public void applyStyles(TextStyle textStyle) {
				}
			};
			if (element instanceof ObjectValidationModel) {
				String text = ((ValidationModel) element).getName().toString();
				StyledString styledString = new StyledString(text, styler);
				String mappingDecorator = "";
				if (((ValidationModel) element).getMappingId() != null) {
					mappingDecorator = " "
							+ CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.mapping") + " - "
							+ ((ValidationModel) element).getMappingId().toString();
					styledString.append(" [" + mappingDecorator + "]", StyledString.COUNTER_STYLER);
				} else {
					mappingDecorator = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"form.missingmapping");
					styledString.append(" [" + mappingDecorator + "]", StyledString.DECORATIONS_STYLER);
					// StyledString.createColorRegistryStyler(
					// SWTResourceManager.getColor(SWT.COLOR_RED),
					// SWTResourceManager
					// .getColor(SWT.COLOR_YELLOW))
				}
				cell.setText(styledString.toString());
				cell.setStyleRanges(styledString.getStyleRanges());
			}
			// Type Tree NodeId
			else if (element instanceof TypeValidationModel) {
				String text = ((ValidationModel) element).getName().toString();
				StyledString styledString = new StyledString(text, styler);
				String mappingDecorator = ((ValidationModel) element).getNodeId().toString();
				styledString.append(" [" + mappingDecorator + "]", StyledString.COUNTER_STYLER);
				cell.setText(styledString.toString());
				cell.setStyleRanges(styledString.getStyleRanges());
			} else if (element instanceof MissingValidationModel) {
				String text = ((ValidationModel) element).getName().toString();
				StyledString styledString = new StyledString(text, styler);
				String mappingDecorator = "";
				if (((ValidationModel) element).getMappingId() != null) {
					mappingDecorator = " "
							+ CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.mapping") + " - "
							+ ((ValidationModel) element).getMappingId().toString();
					styledString.append(" [" + mappingDecorator + "]", StyledString.COUNTER_STYLER);
				}
				cell.setText(styledString.toString());
				cell.setStyleRanges(styledString.getStyleRanges());
			}
			super.update(cell);
		}

		@Override
		protected void measure(Event event, Object element) {
			super.measure(event, element);
		}
	}
}
