package com.bichler.astudio.opcua.opcmodeler.views.namespacebrowser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.images.opcua.OPCImagesActivator;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserMethodInternalModelNode;

import opc.sdk.core.node.Node;

public class NamespaceModelView extends ViewPart {
	public static final String ID = "com.hbsoft.comet.opcmodeler.namespaceview";
	private TreeViewer treeviewer;
	private NamespaceModelContentProvider contentProvider;

	public NamespaceModelView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		this.treeviewer = new TreeViewer(parent, SWT.BORDER);
		this.contentProvider = new NamespaceModelContentProvider();
		this.treeviewer.setContentProvider(this.contentProvider);
		this.treeviewer.setLabelProvider(new NamespaceLabelProvider());
		this.treeviewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				return super.compare(viewer, e1, e2);
			}
		});
		setNamespaceUris("");
		getSite().setSelectionProvider(this.treeviewer);
		
		this.treeviewer.getTree().addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent event) {
				NamespaceModelNode selectedNode = (NamespaceModelNode) ((IStructuredSelection) treeviewer
						.getSelection()).getFirstElement();
				// Deque<BrowsePathElement> path =
				// Comet_Utils.getFullBrowsePath(selectedNode.getNode().getNodeId(),
				// ServerInstance.getInstance().getServerInstance(),
				// Identifiers.RootFolder);
				// for (BrowsePathElement p : path) {
				// System.out.println("" + p.getBrowsename());
				// }
				ModelBrowserView view = (ModelBrowserView) getSite().getWorkbenchWindow().getActivePage()
						.findView(ModelBrowserView.ID);
				view.selectNode(selectedNode.getNode().getNodeId());
				
				openNodeEditor();
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});
		
		createTableStructContextMenu();
	}

	private void createTableStructContextMenu() {
		MenuManager contextMenu = new MenuManager("#ViewerMenu"); //$NON-NLS-1$
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});

		Menu menu = contextMenu.createContextMenu(this.treeviewer.getTree());
		this.treeviewer.getTree().setMenu(menu);
	}
	
	protected void fillContextMenu(IMenuManager contextMenu) {
		contextMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		
		Action actionOpen = new Action("Open") {

			@Override
			public void run() {
				openNodeEditor();
			}
	
		};
		
		ImageDescriptor searchImage = StudioImageActivator.getImageDescriptor(StudioImages.ICON_SEARCH);
		actionOpen.setImageDescriptor(searchImage);

		contextMenu.add(actionOpen);
	}
	
	private void openNodeEditor() {
		BrowserMethodInternalModelNode bmn = new BrowserMethodInternalModelNode(null);
		bmn.setNode(((NamespaceModelNode) ((IStructuredSelection) treeviewer
						.getSelection()).getFirstElement()).getNode());
		ModelBrowserView.openNodeEditor(bmn);
	}
	
	@Override
	public void setFocus() {
		this.treeviewer.getTree().setFocus();
	}

	public void setNamespaceUris(String namespaceUris) {
		this.contentProvider.setNamespaceUri(namespaceUris);
		NamespaceModelNode root = new NamespaceModelNode(null);
		root.init();
		this.treeviewer.setInput(root);
	}

	class NamespaceLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			Node node = ((NamespaceModelNode) element).getNode();
			NodeClass nodeClass = node.getNodeClass();
			switch (nodeClass) {
			case Object:
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.OBJECT);
			case ObjectType:
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.OBJECTTYPE);
			case Variable:
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.VARIABLE);
			case VariableType:
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.VARIABLETYPE);
			case DataType:
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.DATATYPE);
			case Method:
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.METHOD);
			case ReferenceType:
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.REFERENCETYPE);
			case View:
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.VIEW);
			case Unspecified:
			default:
				break;
			}
			return super.getImage(element);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof NodeId) {
				Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById((NodeId) element);
				return node.getDisplayName().getText();
			} else if (element instanceof NamespaceModelNode) {
				Node node = ((NamespaceModelNode) element).getNode();
				return node.getDisplayName().getText();
			}
			return super.getText(element);
		}
	}

	class NamespaceModelContentProvider implements ITreeContentProvider {
		private String namespaceUri;

		public void setNamespaceUri(String nsUri) {
			this.namespaceUri = nsUri;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			List<NodeId> roots = new ArrayList<>();
			try {
				int index = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
						.getIndex(this.namespaceUri);
				if (index < 0) {
					return new Object[0];
				}
				NodeId[] nodeIds = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getAllNodeIds(index);
				for (NodeId nodeId : nodeIds) {
					roots.add(nodeId);
				}
				for (NodeId nodeId : nodeIds) {
					distinct(nodeId, roots, this.namespaceUri);
				}
				for (NodeId nodeId : roots) {
					Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
							.getNodeById(nodeId);
					((NamespaceModelNode) inputElement).addChild(new NamespaceModelNode(node));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return getChildren(inputElement);
		}

		private void distinct(NodeId nodeId, List<NodeId> roots, String namespaceUri) {
			BrowseDescription[] nodesToBrowse = new BrowseDescription[] {
					new BrowseDescription(nodeId, BrowseDirection.Inverse, Identifiers.HierarchicalReferences, true,
							NodeClass.getMask(NodeClass.ALL), BrowseResultMask.getMask(BrowseResultMask.ALL)) };
			try {
				BrowseResult[] parent = ServerInstance.getInstance().getServerInstance().getMaster()
						.browse(nodesToBrowse, UnsignedInteger.ZERO, null, null);
				if (parent != null && parent.length > 0) {
					ReferenceDescription[] parentReferences = parent[0].getReferences();
					for (ReferenceDescription pRef : parentReferences) {
						NodeId id = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.toNodeId(pRef.getNodeId());
						if (Identifiers.RootFolder.equals(id)) {
							continue;
						}
						String uri = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.getUri(id.getNamespaceIndex());
						if (!namespaceUri.equals(uri)) {
							continue;
						}
						// remove child
						roots.remove(nodeId);
						// continue distincting
						distinct(id, roots, namespaceUri);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof NamespaceModelNode) {
				NamespaceModelNode[] children = ((NamespaceModelNode) parentElement).getChildren();
				for (NamespaceModelNode child : children) {
					child.init();
				}
				return children;
			}
			if (parentElement instanceof NodeId[]) {
				return (NodeId[]) parentElement;
			} else if (parentElement instanceof NodeId) {
				BrowseDescription[] nodesToBrowse = new BrowseDescription[] { new BrowseDescription(
						(NodeId) parentElement, BrowseDirection.Forward, Identifiers.HierarchicalReferences, true,
						NodeClass.getMask(NodeClass.ALL), BrowseResultMask.getMask(BrowseResultMask.ALL)) };
				try {
					BrowseResult[] parent = ServerInstance.getInstance().getServerInstance().getMaster()
							.browse(nodesToBrowse, UnsignedInteger.ZERO, null, null);
					if (parent != null && parent.length > 0) {
						ReferenceDescription[] parentReferences = parent[0].getReferences();
						List<NodeId> nodeIds = new ArrayList<>();
						for (ReferenceDescription pRef : parentReferences) {
							NodeId id = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
									.toNodeId(pRef.getNodeId());
							nodeIds.add(id);
						}
						return nodeIds.toArray(new NodeId[0]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new Object[0];
			}
			return null; // (NodeId[]) parentElement;
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
}
