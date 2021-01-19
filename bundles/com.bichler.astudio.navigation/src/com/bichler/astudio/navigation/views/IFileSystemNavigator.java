package com.bichler.astudio.navigation.views;

import com.bichler.astudio.navigation.nodes.StudioModelNode;

public interface IFileSystemNavigator {

	/**
	 * Delete and re-build children
	 * 
	 * @param node
	 */
	public void refresh(StudioModelNode node);

	/**
	 * 
	 * @param class for node to update
	 * @param SetInput
	 *            used to set input or only refresh
	 */
	public void refresh(final Class<?> c, final boolean setInput);
}
