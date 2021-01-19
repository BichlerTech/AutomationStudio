package com.bichler.astudio.device.website;

import java.io.File;

import org.eclipse.core.runtime.Path;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;

public class WebsiteActivator extends InternationalActivator {

	// The plug-in ID
		public static final String PLUGIN_ID = "com.bichler.astudio.device.website"; //$NON-NLS-1$
		public static final String BUNDLE_NAME = "com.bichler.astudio.device.website.resource.custom"; //$NON-NLS-1$
		
		private static final String FILES = "files";
		private static final String DOKU = "docu.pdf";
		// The shared instance
		private static WebsiteActivator plugin;
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
		 * BundleContext )
		 */
		@Override
		public void start(BundleContext context) throws Exception {
			super.start(context);
//		    plugin = this;
			WebsiteActivator.setPlugin(this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
		 * BundleContext )
		 */
		@Override
		public void stop(BundleContext context) throws Exception {
			WebsiteActivator.setPlugin(null);
			super.stop(context);
		}

		public static void setPlugin(WebsiteActivator plugin) {
			WebsiteActivator.plugin = plugin;
		}

		/**
		 * Returns the shared instance
		 * 
		 * @return the shared instance
		 */
		public static WebsiteActivator getDefault() {
			return plugin;
		}
		
		public File getDocuFile(String version) {
			return getFile(getDefault().getBundle(), Path.ROOT.append(FILES).append(version).append(DOKU));
		}
		
		public File getFiles() {
			return getFile(getDefault().getBundle(), Path.ROOT.append(FILES));
		}
		
		@Override
		public String getBundleName() {
			return BUNDLE_NAME;
		}
}
