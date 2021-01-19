package com.bichler.astudio.device.core.preference;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.filesystem.SshFileSystem;

public class DevicePreferenceManager {

	public DevicePreferenceManager() {

	}

	public Preferences getRoot() {
		Preferences devlist = getRootPreference();
		return devlist;
	}

	public void addDevice(IFileSystem filesystem) {
		Preferences devlist = getRootPreference();
		String connectionName = filesystem.getConnectionName();

		int filesystemType = filesystem instanceof SshFileSystem ? DevicePreferenceConstants.PREFERENCE_TYPE_SSH
				: DevicePreferenceConstants.PREFERENCE_TYPE_SIMPLE;
		int timeout = filesystem.getTimeOut();
		String host = filesystem.getHostName();
		String user = filesystem.getUser();
		String password = filesystem.getPassword();
		String rootPath = filesystem.getRootPath();
		String targetFileSeperator = filesystem.getTargetFileSeparator();

		Preferences device = devlist.node(connectionName);

		device.putInt(DevicePreferenceConstants.PREFERENCE_DEVICE_FILETYPE,
				filesystemType);
		device.put(DevicePreferenceConstants.PREFERENCE_DEVICE_HOST, host);
		device.putInt(DevicePreferenceConstants.PREFERENCE_DEVICE_TIMEOUT,
				timeout);
		device.put(DevicePreferenceConstants.PREFERENCE_DEVICE_USER, user);
		device.put(DevicePreferenceConstants.PREFERENCE_DEVICE_PASSWORD,
				password);
		device.put(DevicePreferenceConstants.PREFERENCE_DEVICE_ROOTPATH,
				rootPath);
		device.put(DevicePreferenceConstants.PREFERENCE_DEVICE_FILESEPARATOR,
				targetFileSeperator);
		try {
			devlist.flush();
		} catch (BackingStoreException bse) {
			bse.printStackTrace();
		}
	}

	public void editDeviceFilesystem(String lastName,
			IFileSystem filesystem) {
		Preferences devlist = getRootPreference();

		String connectionName = filesystem.getConnectionName();
		int filesystemType = filesystem instanceof SshFileSystem ? DevicePreferenceConstants.PREFERENCE_TYPE_SSH
				: DevicePreferenceConstants.PREFERENCE_TYPE_SIMPLE;
		int timeout = filesystem.getTimeOut();
		String host = filesystem.getHostName();
		String user = filesystem.getUser();
		String password = filesystem.getPassword();
		String rootPath = filesystem.getRootPath();
		String targetFileSeperator = filesystem.getTargetFileSeparator();

		// remove old node
		Preferences node2modify = devlist.node(lastName);
		if (node2modify != null) {
			try {
				node2modify.removeNode();
				devlist.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
		// create new node
		Preferences device = devlist.node(connectionName);
		device.putInt(DevicePreferenceConstants.PREFERENCE_DEVICE_FILETYPE,
				filesystemType);
		device.put(DevicePreferenceConstants.PREFERENCE_DEVICE_HOST, host);
		device.putInt(DevicePreferenceConstants.PREFERENCE_DEVICE_TIMEOUT,
				timeout);
		device.put(DevicePreferenceConstants.PREFERENCE_DEVICE_USER, user);
		device.put(DevicePreferenceConstants.PREFERENCE_DEVICE_PASSWORD,
				password);
		device.put(DevicePreferenceConstants.PREFERENCE_DEVICE_ROOTPATH,
				rootPath);
		device.put(DevicePreferenceConstants.PREFERENCE_DEVICE_FILESEPARATOR,
				targetFileSeperator);
		try {
			devlist.flush();
		} catch (BackingStoreException bse) {
			bse.printStackTrace();
		}
	}
	
	public boolean isOnlyDataHub() {
		IEclipsePreferences root = InstanceScope.INSTANCE
				.getNode(DevicePreferenceConstants.PREFERENCE_DEVICE_ONLYDATAHUB);
		return root.getBoolean(DevicePreferenceConstants.PREFERENCE_DEVICE_ONLYDATAHUB, false);
	}
	
	public void setIsOnlyDataHub(boolean onlyDataHub) {
		IEclipsePreferences root = InstanceScope.INSTANCE
				.getNode(DevicePreferenceConstants.PREFERENCE_DEVICE_ONLYDATAHUB);
		root.putBoolean(DevicePreferenceConstants.PREFERENCE_DEVICE_ONLYDATAHUB, onlyDataHub);
	}
	
	public static String getPreferenceDeviceName(Preferences preference){
		return preference.name();
	}

	public static String getPreferenceDeviceHost(Preferences preference){
		return preference.get(DevicePreferenceConstants.PREFERENCE_DEVICE_HOST, "");
	}
	
	public static String getPreferenceDeviceUser(Preferences preference){
		return preference.get(DevicePreferenceConstants.PREFERENCE_DEVICE_USER, "");
	}
	
	public static String getPreferenceDeviceTimeout(Preferences preference){
		return preference.get(DevicePreferenceConstants.PREFERENCE_DEVICE_TIMEOUT, "");
	}
	
	public static String getPreferenceDeviceRootpath(Preferences preference){
		return preference.get(DevicePreferenceConstants.PREFERENCE_DEVICE_ROOTPATH, "");
	}
	
	public static String getPreferenceDevicePassword(Preferences preference){
		return preference.get(DevicePreferenceConstants.PREFERENCE_DEVICE_PASSWORD, "");
	}
	
	private Preferences getRootPreference() {
		IEclipsePreferences root = InstanceScope.INSTANCE
				.getNode(DevicePreferenceConstants.PREFERENCE_DEVICE_DEVICELIST);
		return root;
	}

	public void removeDevice(Preferences obj) {
		Preferences root = getRootPreference();
		try {
			obj.removeNode();
			root.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	public IFileSystem getFilesystem(String targetDeviceName) {
		Preferences device = getRoot().node(targetDeviceName);
		if (device == null) {
			return null;
		}

		IFileSystem targetFileSystem = null;
		int fileType = device.getInt(
				DevicePreferenceConstants.PREFERENCE_DEVICE_FILETYPE, 0);
		switch (fileType) {
		case 0:
			targetFileSystem = new DataHubFileSystem();
			break;
		case 1:
			targetFileSystem = new SimpleFileSystem();
			break;
		}

		String host = device.get(
				DevicePreferenceConstants.PREFERENCE_DEVICE_HOST, "");
		int timeout = device.getInt(
				DevicePreferenceConstants.PREFERENCE_DEVICE_TIMEOUT, 0);
		String user = device.get(
				DevicePreferenceConstants.PREFERENCE_DEVICE_USER, "");
		String password = device.get(
				DevicePreferenceConstants.PREFERENCE_DEVICE_PASSWORD, "");
		String fileseparator = device.get(
				DevicePreferenceConstants.PREFERENCE_DEVICE_FILESEPARATOR, "");
		String rootPath = device.get(
				DevicePreferenceConstants.PREFERENCE_DEVICE_ROOTPATH, "");

		targetFileSystem.setConnectionName(targetDeviceName);
		targetFileSystem.setHostName(host);
		targetFileSystem.setTimeOut(timeout);
		targetFileSystem.setUser(user);
		targetFileSystem.setPassword(password);
		targetFileSystem.setTargetFileSeparator(fileseparator);
		targetFileSystem.setRootPath(rootPath);

		return targetFileSystem;
	}

	

}
