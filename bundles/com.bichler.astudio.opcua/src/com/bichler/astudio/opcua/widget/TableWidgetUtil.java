package com.bichler.astudio.opcua.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeColumn;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedRootConfigurationNode;

public class TableWidgetUtil {

	static enum SORTORDER {
		ASC(1), DESC(-1), OTHER(1);
		private int order = 0;

		SORTORDER(int order) {
			this.order = order;
		}

		public int getOrder() {
			return this.order;
		}
	}

	public TableWidgetUtil() {

	}

	private static TableViewerColumn createTableColumn(final TableViewer tableViewer, String name, int width,
			final TableWidgetEnum sorter, final AbstractTableComperator comperator) {

		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableColumn tc = column.getColumn();
		tc.setWidth(width);
		tc.setText(name);
		switch (sorter) {
		case OTHER:
			break;
		default:
			tc.addSelectionListener(new SelectionAdapter() {

				private SORTORDER order = SORTORDER.OTHER;

				@Override
				public void widgetSelected(SelectionEvent e) {
					List input = null;
					if (tableViewer.getInput() instanceof List) {
						input = (List<?>) tableViewer.getInput();
					} else if (tableViewer.getInput() instanceof AdvancedRootConfigurationNode) {
						AdvancedConfigurationNode[] children = ((AdvancedRootConfigurationNode) tableViewer.getInput())
								.getChildren();

						input = new ArrayList<AdvancedConfigurationNode>();
						for (AdvancedConfigurationNode node : children) {
							input.add(node);
						}
					}

					Object[] sorted = input.toArray();
					SORTORDER nextOrder = getNextSortOrder();
					this.order = nextOrder;
					switch (sorter) {
					case NODEID:
						comperator.setOrder(nextOrder);
						Arrays.sort(sorted, comperator);
						break;
					case BROWSEPATH:
						comperator.setOrder(nextOrder);
						Arrays.sort(sorted, comperator);
						break;
					case DISPLAYNAME:
						comperator.setOrder(nextOrder);
						Arrays.sort(sorted, comperator);
						break;
					case CUSTOM:
						comperator.setOrder(nextOrder);
						Arrays.sort(sorted, comperator);
						break;
					default:
						// exit
						return;
					}
					// remove
					input.clear();
					// addall
					// List<IDriverNode> sortedInput = new ArrayList<>();
					for (Object dn : sorted) {
						input.add(dn);
					}

					tableViewer.getTable().setSortColumn(tc);

					if (tableViewer.getInput() instanceof AdvancedRootConfigurationNode) {
						((AdvancedRootConfigurationNode) tableViewer.getInput()).setChildren(input);
						tableViewer.setInput(tableViewer.getInput());
					} else {
						tableViewer.setInput(input);
					}
				}

				private SORTORDER getNextSortOrder() {
					switch (this.order) {
					case OTHER:
						return SORTORDER.ASC;
					case ASC:
						return SORTORDER.DESC;
					case DESC:
						return SORTORDER.ASC;
					default:
						return SORTORDER.OTHER;
					}
				}

			});
			break;
		}

		return column;
	}

	public static TableViewerColumn createTableColumn(final TableViewer tableViewer, String name, int width,
			final TableWidgetEnum sorter) {

		AbstractTableComperator comperator = null;

		switch (sorter) {
		case NODEID:
			comperator = new TableWidgetUtil().new ComperatorNodeId();
			break;
		case BROWSEPATH:
			comperator = new TableWidgetUtil().new ComperatorBrowsepath();
			break;
		case DISPLAYNAME:
			comperator = new TableWidgetUtil().new ComperatorDisplayname();
			break;
		case CUSTOM:
			break;
		case OTHER:
		default:
			break;
		}

		return createTableColumn(tableViewer, name, width, sorter, comperator);
	}

	public static TableViewerColumn createTableColumn(TableViewer tableViewer, String name, int width) {

		return createTableColumn(tableViewer, name, width, TableWidgetEnum.OTHER, null);
	}

	public static TableViewerColumn createTableColumn(TableViewer tableViewer, String name, int width,
			AbstractTableComperator comperator) {

		return createTableColumn(tableViewer, name, width, TableWidgetEnum.CUSTOM, comperator);
	}

	private static TreeViewerColumn createTreeColumn(final TreeViewer treeViewer, String name, int width,
			final TableWidgetEnum sorter, final AbstractTableComperator comperator) {

		TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.NONE);
		final TreeColumn tc = column.getColumn();
		tc.setWidth(width);
		tc.setText(name);
		switch (sorter) {
		case OTHER:
			break;
		default:
			tc.addSelectionListener(new SelectionAdapter() {

				private SORTORDER order = SORTORDER.OTHER;

				@Override
				public void widgetSelected(SelectionEvent e) {
					List input = null;
					if (treeViewer.getInput() instanceof List) {
						input = (List<?>) treeViewer.getInput();
					} else if (treeViewer.getInput() instanceof AdvancedRootConfigurationNode) {
						AdvancedConfigurationNode[] children = ((AdvancedRootConfigurationNode) treeViewer.getInput())
								.getChildren();

						input = new ArrayList<AdvancedConfigurationNode>();
						for (AdvancedConfigurationNode node : children) {
							input.add(node);
						}
					}

					Object[] sorted = input.toArray();
					SORTORDER nextOrder = getNextSortOrder();
					this.order = nextOrder;
					switch (sorter) {
					case NODEID:
						comperator.setOrder(nextOrder);
						Arrays.sort(sorted, comperator);
						break;
					case BROWSEPATH:
						comperator.setOrder(nextOrder);
						Arrays.sort(sorted, comperator);
						break;
					case DISPLAYNAME:
						comperator.setOrder(nextOrder);
						Arrays.sort(sorted, comperator);
						break;
					case CUSTOM:
						comperator.setOrder(nextOrder);
						Arrays.sort(sorted, comperator);
						break;
					default:
						// exit
						return;
					}
					// remove
					input.clear();
					// addall
					// List<IDriverNode> sortedInput = new ArrayList<>();
					for (Object dn : sorted) {
						input.add(dn);
					}

					treeViewer.getTree().setSortColumn(tc);

					if (treeViewer.getInput() instanceof AdvancedRootConfigurationNode) {
						((AdvancedRootConfigurationNode) treeViewer.getInput()).setChildren(input);
						treeViewer.setInput(treeViewer.getInput());
					} else {
						treeViewer.setInput(input);
					}
				}

				private SORTORDER getNextSortOrder() {
					switch (this.order) {
					case OTHER:
						return SORTORDER.ASC;
					case ASC:
						return SORTORDER.DESC;
					case DESC:
						return SORTORDER.ASC;
					default:
						return SORTORDER.OTHER;
					}
				}

			});
			break;
		}

		return column;
	}

	public static TreeViewerColumn createTreeColumn(final TreeViewer treeViewer, String name, int width,
			final TableWidgetEnum sorter) {

		AbstractTableComperator comperator = null;

		switch (sorter) {
		case NODEID:
			comperator = new TableWidgetUtil().new ComperatorNodeId();
			break;
		case BROWSEPATH:
			comperator = new TableWidgetUtil().new ComperatorBrowsepath();
			break;
		case DISPLAYNAME:
			comperator = new TableWidgetUtil().new ComperatorDisplayname();
			break;
		case CUSTOM:
			break;
		case OTHER:
		default:
			break;
		}

		return createTreeColumn(treeViewer, name, width, sorter, comperator);
	}

	public static TreeViewerColumn createTreeColumn(TreeViewer treeViewer, String name, int width) {

		return createTreeColumn(treeViewer, name, width, TableWidgetEnum.OTHER, null);
	}

	public static TreeViewerColumn createTreeColumn(TreeViewer treeViewer, String name, int width,
			AbstractTableComperator comperator) {

		return createTreeColumn(treeViewer, name, width, TableWidgetEnum.CUSTOM, comperator);
	}

	class ComperatorBrowsepath extends AbstractTableComperator<IDriverNode> {

		public ComperatorBrowsepath() {
			super();
		}

		@Override
		public int compare(IDriverNode o1, IDriverNode o2) {
			String n1 = null;
			String n2 = null;

			if (o1.getBrowsepath() == null) {
				n1 = "";
			} else {
				n1 = o1.getBrowsepath();
			}

			if (o2.getBrowsepath() == null) {
				n2 = "";
			} else {
				n2 = o2.getBrowsepath();
			}

			return n1.toLowerCase().compareTo(n2.toLowerCase()) * getOrder();
		}
	}

	class ComperatorDisplayname extends AbstractTableComperator<IDriverNode> {

		public ComperatorDisplayname() {
			super();
		}

		@Override
		public int compare(IDriverNode o1, IDriverNode o2) {
			String n1 = null;
			String n2 = null;

			if (o1.getDname() == null) {
				n1 = "";
			} else {
				n1 = o1.getDname();
			}

			if (o2.getDname() == null) {
				n2 = "";
			} else {
				n2 = o2.getDname();
			}

			return n1.toLowerCase().compareTo(n2.toLowerCase()) * getOrder();
		}
	}

	class ComperatorNodeId extends AbstractTableComperator<IDriverNode> {

		public ComperatorNodeId() {
			super();
		}

		@Override
		public int compare(IDriverNode o1, IDriverNode o2) {
			NodeId n1 = null;
			NodeId n2 = null;

			if (NodeId.isNull(o1.getNId())) {
				n1 = NodeId.NULL;
			} else {
				n1 = o1.getNId();
			}

			if (NodeId.isNull(o2.getNId())) {
				n2 = NodeId.NULL;
			} else {
				n2 = o2.getNId();
			}

			return n1.compareTo(n2) * getOrder();
		}
	}

	public abstract class AbstractCustomComperator extends AbstractTableComperator<Object> {

		public AbstractCustomComperator() {
			super();
		}

		@Override
		public int compare(Object o1, Object o2) {
			Object n1 = getComparableObject(o1);
			Object n2 = getComparableObject(o2);

			if (n1 == null && n2 == null) {
				return 0;
			}

			else if (n1 == null && n2 != null) {
				return -1;
			} else if (n1 != null && n2 == null) {
				return 1;
			}

			return doCompare(n1, n2) * getOrder();
		}

		public abstract int doCompare(Object o1, Object o2);

		public abstract Object getComparableObject(Object element);
	}

	public abstract class AbstractDriverComperator extends AbstractTableComperator<IDriverNode> {

		public AbstractDriverComperator() {
			super();
		}

		@Override
		public int compare(IDriverNode o1, IDriverNode o2) {
			Object n1 = getComparableObject(o1);
			Object n2 = getComparableObject(o2);

			if (n1 == null && n2 == null) {
				return 0;
			}

			else if (n1 == null && n2 != null) {
				return -1;
			} else if (n1 != null && n2 == null) {
				return 1;
			}

			return doCompare(n1, n2) * getOrder();
		}

		public abstract int doCompare(Object o1, Object o2);

		public abstract Object getComparableObject(IDriverNode element);
	}

	abstract class AbstractTableComperator<Object> implements Comparator<Object> {
		private SORTORDER order = null;

		public AbstractTableComperator() {
			this.order = SORTORDER.OTHER;
		}

		int getOrder() {
			return this.order.getOrder();
		}

		public SORTORDER getSortOrder() {
			return this.order;
		}

		public void setOrder(SORTORDER order) {
			this.order = order;
		}
	}
}
