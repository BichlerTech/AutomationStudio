package com.bichler.astudio.log.view.viewer;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.bichler.astudio.log.view.provider.ASLogContentProvider;
import com.bichler.astudio.log.view.provider.DefaultColumnLabelProvider;
import com.bichler.astudio.log.view.provider.LevelColumnLabelProvider;
import com.bichler.astudio.log.view.provider.TextColumnLabelProvider;
import com.bichler.astudio.log.server.util.ASLogConstants;

public class ASLogTableWidget extends TableViewer {

	public ASLogTableWidget(Composite parent, int style) {
		super(parent, style);
		Table table = getTable();
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createColumns();
		setUseHashlookup(true);
		setContentProvider(new ASLogContentProvider());
	}

	private void createColumns() {
		String[] headers = { ASLogConstants.LEVEL_FIELD_NAME, ASLogConstants.CATEGORY_FIELD_NAME,
				ASLogConstants.MESSAGE_FIELD_NAME, ASLogConstants.LINE_FIELD_NAME, ASLogConstants.DATE_FIELD_NAME,
				ASLogConstants.THROWABLE_FIELD_NAME, };
		int[] bounds = { 55, 100, 100, 100, 100, 100 };

		for (int i = 0; i < headers.length; i++) {
			TableViewerColumn column = new TableViewerColumn(this, SWT.NONE);
			column.getColumn().setText(headers[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(false);

			switch (headers[i]) {
			case ASLogConstants.LEVEL_FIELD_NAME:
				column.setLabelProvider(new LevelColumnLabelProvider());
				break;
			case ASLogConstants.MESSAGE_FIELD_NAME:
			case ASLogConstants.THROWABLE_FIELD_NAME:
				column.setLabelProvider(new TextColumnLabelProvider(headers[i]));
				break;
			default:
				column.setLabelProvider(new DefaultColumnLabelProvider(headers[i]));
			}
		}
	}
}
