package com.bichler.astudio.core.user;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.core.user.type.AbstractStudioUser;
import com.bichler.astudio.core.user.type.DefaultUser;
import com.bichler.astudio.core.user.type.SuperUser;
import com.bichler.astudio.utils.activator.InternationalActivator;
import com.bichler.astudio.utils.constants.StudioConstants;

public class UserActivator extends InternationalActivator {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.core.user"; //$NON-NLS-1$

	public static final String BUNDLE_NAME = "com.bichler.astudio.core.user.resource.custom";
	// The shared instance
	private static UserActivator plugin;

	public static final String PASSWORD = "AuTt"; // Abwarten und Tee trinken
	
	/**
	 * The constructor
	 */
	public UserActivator() {
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		setDefaultValues();
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	public AbstractStudioUser getUser() {
		IPreferenceStore store = getPreferenceStore();
		String property_user = store.getString(StudioConstants.StudioUser);
		
		// just to be sure to enter preferences dialog
		if(property_user.isEmpty()) {
			property_user = "0";
		}
		
		AbstractStudioUser user = null;
		switch (Integer.parseInt(property_user)) {
		case 1:
			user = new SuperUser();
			break;
		default:
			user = new DefaultUser();
			break;
		}
		return user;
	}
	

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static UserActivator getDefault() {
		return plugin;
	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}
	
	/**
	 * set default values
	 */
	private void setDefaultValues() {
		IPreferenceStore store = UserActivator.getDefault().getPreferenceStore();
		String property_user = store.getString(StudioConstants.StudioUser);
		/**
		 * if all values are empty
		 */
		if (property_user.isEmpty()) {
			DefaultUser user = new DefaultUser();		
			store.setDefault(StudioConstants.StudioUser, user.getUserType());
		}
	}
}
