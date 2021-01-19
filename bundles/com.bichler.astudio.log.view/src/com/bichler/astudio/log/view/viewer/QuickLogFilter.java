package com.bichler.astudio.log.view.viewer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.bichler.astudio.log.server.core.ASLog;
import com.bichler.astudio.log.server.util.ASLogConstants;
import com.bichler.astudio.log.server.util.ASLogConverter;

public class QuickLogFilter extends ViewerFilter {

	private String searchText;

	public void setSearchText(String searchText) {
		StringBuilder builder = new StringBuilder();
		builder.append("(?i)(?s).*(").append(searchText).append(").*");
		this.searchText = builder.toString();
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		boolean isPastFilter = false;

		if (this.searchText != null && !this.searchText.isEmpty()) {
			ASLog log = (ASLog) element;

			for (String logFieldName : ASLogConstants.LOG_FIELD_NAMES) {
				if (ASLogConverter.getValue(log, logFieldName).matches(searchText)) {
					isPastFilter = true;
					break;
				}
			}
		} else {
			isPastFilter = true;
		}

		return isPastFilter;
	}
}
