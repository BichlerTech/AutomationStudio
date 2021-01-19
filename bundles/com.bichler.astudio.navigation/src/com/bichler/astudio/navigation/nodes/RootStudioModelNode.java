package com.bichler.astudio.navigation.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;

public class RootStudioModelNode extends StudioModelNode {

	@Override
	public void nodeDBLClicked() {
	}

	@Override
	public Image getLabelImage() {
	  return null;
		//return StudioImageActivator.getImage(StudioImages.ICON_COMET_16);
	}

	@Override
	public String getLabelText() {
		return "Automation Studio";
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();
		if (nodes == null) {
			IConfigurationElement[] config = Platform.getExtensionRegistry()
					.getConfigurationElementsFor("com.bichler.astudio.extensionpoint.navigation");

			/** list with all studio model nodes */
			nodes = new ArrayList<StudioModelNode>();

			for (IConfigurationElement e : config) {
				Object o;
				try {
					o = e.createExecutableExtension("class");
					if (o instanceof StudioModelNode) {
						nodes.add((StudioModelNode) o);
					}

				} catch (CoreException e1) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getLocalizedMessage());
				}

			}
			setChildren(nodes);
		}

		/** add hmi servers plugin */

		// nodes.add(new HMIServersModelNode());

		// nodes.add(new TrendingServersModelNode());

		/** add data servers plugin */
		// nodes.add(new DataServerModelNode());

		/** add license manager model node */
		// nodes.add(new LicenseModelNode());

		/** add iec models */
		// nodes.add(new IEC61131ModelsNode());

		/** return all nodes after the root element */

		return nodes.toArray();
		// return null;
	}

	@Override
	public void refresh() {

	}

}
