package com.bichler.astudio.images.opcua;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class OPCImagesActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.bichler.astudio.images.opcua";
	private static OPCImagesActivator plugin;

	private static final String IMG_PATH_NODES = new Path("icons").append(
			"nodes").toOSString();
	private static final String IMG_PATH_OTHER = new Path("icons").append(
			"other").toOSString();
	public static final String FOLDER_16 = "16";
//	public static final String FOLDER_24 = "24";

	public static final String DATATYPE = "datatype.png";

	public static final String FOLDER = "folder.png";
	public static final String FOLDER_M = "folder_m.png";
	public static final String FOLDER_O = "folder_o.png";

	public static final String METHOD = "method.png";
	public static final String METHOD_M = "method_m.png";
	public static final String METHOD_O = "method_o.png";

	public static final String OBJECT = "object.png";
	public static final String OBJECT_M = "object_m.png";
	public static final String OBJECT_O = "object_o.png";

	public static final String OBJECTTYPE = "objecttype.png";
//	public static final String OBJECTTYPE_M = "objecttype_m.png";
//	public static final String OBJECTTYPE_O = "objecttype_o.png";

	public static final String REFERENCETYPE = "referencetype.png";
//	public static final String REFERENCETYPE_M = "referencetype_m.png";
//	public static final String REFERENCETYPE_O = "referencetype_o.png";

	public static final String VARIABLE = "variable.png";
	public static final String VARIABLE_M = "variable_m.png";
	public static final String VARIABLE_O = "variable_o.png";
	
	public static final String PROPERTY = "property.png";
	public static final String PROPERTY_M = "property_m.png";
	public static final String PROPERTY_O = "property_o.png";

	public static final String VARIABLETYPE = "variabletype.png";
//	public static final String VARIABLETYPE_M = "variabletype_m.png";
//	public static final String VARIABLETYPE_O = "variabletype_o.png";

	public static final String VIEW = "view.png";
	public static final String EVENT = "event.png";

	public static final String BOOK = "book.png";
	
	public OPCImagesActivator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static OPCImagesActivator getDefault() {
		return plugin;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		Path root_nodes = new Path(IMG_PATH_NODES);
		Path root_other = new Path(IMG_PATH_OTHER);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, DATATYPE);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, FOLDER);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, FOLDER);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, FOLDER_M);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, FOLDER_O);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, METHOD);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, METHOD_M);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, METHOD_O);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, OBJECT);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, OBJECT_M);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, OBJECT_O);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, OBJECTTYPE);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, REFERENCETYPE);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, VARIABLE);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, VARIABLE_M);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, VARIABLE_O);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, PROPERTY);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, PROPERTY_M);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, PROPERTY_O);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, VARIABLETYPE);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, VIEW);
		initializeIcons(bundle, reg, root_nodes, FOLDER_16, EVENT);
		initializeIcons(bundle, reg, root_other, FOLDER_16, BOOK);
	}

	private void initializeIcons(Bundle bundle, ImageRegistry reg, IPath path,
			String size, String icon) {

		IPath id = new Path(size).append(icon);
		IPath imgpath = path.append(id);

		URL url = FileLocator.find(bundle, imgpath, null);
		reg.put(id.toOSString(), ImageDescriptor.createFromURL(url));
	}

	/**
	 * 
	 * @param size
	 *            FOLDER_16, FOLDER_32
	 * @param name
	 *            ICON CONSTANT
	 * @return
	 */
	public Image getRegisteredImage(String size, String name) {
		return getImageRegistry().get(new Path(size).append(name).toOSString());
	}
}
