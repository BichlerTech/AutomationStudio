package com.bichler.astudio.opcua.statemachine.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.statemachine.StatemachineActivator;
import com.bichler.astudio.opcua.statemachine.StatemachinePreferenceConstants;

public class WizardUtils {

	public static String openFileDialog(Shell shell, int style, String[] extensions, String selection) {
		FileDialog dialog = new FileDialog(shell, style);
		if (selection != null) {
			dialog.setFileName(selection);
		}
		dialog.setFilterExtensions(extensions);
		return dialog.open();
	}

	public static String openDirectoryDialog(Shell shell, String selection) {
		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.NONE);
		if (selection != null) {
			dialog.setFilterPath(selection);
		}
		return dialog.open();
	}

	public static TableComboViewer createTableComboViewer(Shell shell, Composite parent, String preference) {
		TableCombo combo = new TableCombo(parent, SWT.BORDER | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.setShowTableHeader(false);
		combo.setShowTableLines(true);
		combo.setShowImageWithinSelection(true);
		combo.defineColumns(2);

		TableComboViewer viewer = new TableComboViewer(combo);

		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new TableComboViewerLabelProvider());
//		viewer.setSorter(getSorter());

		WizardUtils.addTableComboViewerListener(shell, viewer, preference);
		
		return viewer;
	}
	
	public static void addTableComboViewerListener(Shell shell, TableComboViewer viewer, String preference) {
		Table table = viewer.getTableCombo().getTable();
		table.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TableItem item = table.getItem(pt);
				if (item == null) {
					return;
				}
				// check if selected column is remove column
				int column = -1;
				for (int col = 0; col < table.getColumnCount(); col++) {
					Rectangle rect = item.getBounds(col);
					if (rect.contains(pt)) {
						column = col;
						break;
					}
				}

				switch (column) {
				case 1:
					IStructuredSelection selection = viewer.getStructuredSelection();
					// ask to remove column
					boolean confirm = MessageDialog.openConfirm(shell, "Remove",
							"Do you really want to remove " + selection.getFirstElement() + "?");
					if (!confirm) {
						return;
					}
					// remove item

					viewer.remove(selection.getFirstElement());
					// remove from preference store
					IPreferenceStore preferenceStore = StatemachineActivator.getDefault().getPreferenceStore();
					String prefValue = preferenceStore.getString(preference);
					String[] prefArray = StatemachinePreferenceConstants.convertStringToArray(prefValue);
					List<String> newPrefArray = new ArrayList<>();
					for (String pref : prefArray) {
						if (selection.getFirstElement().equals(pref)) {
							continue;
						}
						newPrefArray.add(pref);
					}

					String newPrefValue = StatemachinePreferenceConstants
							.convertArrayToString(newPrefArray.toArray(new String[0]));
					preferenceStore.setValue(preference, newPrefValue);

					break;
				}

			}
		});
	}
	
	static class TableComboViewerLabelProvider extends LabelProvider implements ITableLabelProvider {

		public TableComboViewerLabelProvider() {
		}

		public Image getColumnImage(Object element, int columnIndex) {

			switch (columnIndex) {
			case 0:
				break;
			case 1:
				return CommonImagesActivator.getImage(CommonImagesActivator.IMG_16, CommonImagesActivator.DELETE);
			}

			return null;

		}

		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				if (element instanceof String) {
					String array = (String) element;
					return array;
				}
				break;
			case 1:
				break;
			}

			return null;
		}

		public void dispose() {
//			super.dispose();
		}

	}
}
