package com.bichler.module.wizard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.Wizard;

import com.bichler.module.error.ErrorUtil;
import com.module.wizard.page.ImportWorkspaceDevicesSelectPage;

public class ImportWorkspaceDevicesWizard extends Wizard {

	private static String FOLDER_METADATA = ".metadata";
	private static String FOLDER_PLUGINS = ".plugins";
	private static String FOLDER_RUNTIME = "org.eclipse.core.runtime";
	private static String FOLDER_SETTINGS = ".settings";
	private static String FILE_PREFERENCE_DEVICES = "preferences_devicelist.prefs";

	private static String PREFERENCE_VERSION = "eclipse.preferences.version=1";

	private ImportWorkspaceDevicesSelectPage selectPage;

	@Override
	public boolean performFinish() {
		File file = this.selectPage.getFile();
		// do not remove devices => append
		boolean appendPreferences = !this.selectPage.isRemoveExistingDevices();
		File prefDev = findWorkspacePrefDev(file);
		if (prefDev == null) {
			ErrorUtil.showError("Cannot find preference file in target workspace "+file.getPath());
			return false;
		}

		File wsRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile();
		File wsPref = findWorkspacePrefDev(wsRoot);
		if (wsPref == null) {
			try {
				wsPref = createPreferenceFile(wsRoot);
			} catch (IOException e) {
				ErrorUtil.showError("Cannot find local workspace preference file");
				e.printStackTrace();
				return false;
			}
		}

		try {
			String[] missingDevices = copyPreferences(appendPreferences, prefDev, wsPref);
			
			if(missingDevices.length > 0) {
				ErrorUtil.showInfo("Devices already exist and could't be imported:", missingDevices);
			}
		} catch (IOException e) {
			ErrorUtil.showError("No file access to prefernce file");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private void appendPreferenceVersion(File file) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file));) {
			bw.write(PREFERENCE_VERSION);
			bw.newLine();
			bw.flush();
		}
	}

	private static final int PREF_COUNT = 7;
	
	private String[] copyPreferences(boolean append, File source, File target) throws IOException {
		List<String> devicenames = new ArrayList<>();
		Set<String> missingDevices = new HashSet<String>();
		if (!append) {
			target.delete();
			target.createNewFile();
			appendPreferenceVersion(target);
		}else {
			// read existing device names;
			List<String> existing = readDevicenames(target);
			devicenames.addAll(existing);
		}	 
		
		try (BufferedReader br = new BufferedReader(new FileReader(source));
				BufferedWriter bw = new BufferedWriter(new FileWriter(target, true));) {
			String line = null, currentDev = null;
			int count = PREF_COUNT;
			while ((line = br.readLine()) != null) {
				if (line.startsWith(PREFERENCE_VERSION)) {
					continue;
				}
			
				// dont add double devices
				String devname = line.substring(0, line.indexOf("/"));
				boolean isFirst = false;
				if(currentDev == null) {
					currentDev = devname;
					isFirst = true;
				}
				
				// skip multiple new devices with same name
				if(count >= PREF_COUNT && devicenames.contains(devname)){
					missingDevices.add(devname);
					continue;
				}
				
				// reset counter
				if(isFirst || !devname.equals(currentDev)) {
					count = 0;
				}				
				
				// add new devicename
				if(!devicenames.contains(devname)) {
					devicenames.add(devname);
				}
				
				bw.write(line);
				bw.newLine();
				count++;
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return missingDevices.toArray(new String[0]);
	}

	private List<String> readDevicenames(File target) throws FileNotFoundException, IOException {
		Set<String> devices = new HashSet<>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(target));){
			String line = null;
			while((line = br.readLine()) != null) {
				if (line.startsWith(PREFERENCE_VERSION)) {
					continue;
				}
				String devname = line.substring(0, line.indexOf("/"));
				devices.add(devname);
			}
		}
		
		
		return new ArrayList<String>(devices);
	}

	private File createPreferenceFile(File wsRoot) throws IOException {
		File settings = findWorkspaceFolderSettings(wsRoot);
		File preference = new File(settings.getPath() + File.separator + FILE_PREFERENCE_DEVICES);
		if (!preference.exists()) {
			preference.createNewFile();
			appendPreferenceVersion(preference);
		}
		return preference;
	}

	private File findWorkspaceFolderSettings(File file) {
		File[] metadata = file.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (FOLDER_METADATA.equals(name)) {
					return true;
				}
				return false;
			}
		});
		boolean valid = false;
		valid = metadata.length > 0 ? true : false;
		if (!valid) {
			return null;
		}

		File[] plugins = metadata[0].listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (FOLDER_PLUGINS.equals(name)) {
					return true;
				}
				return false;
			}
		});
		valid = plugins.length > 0 ? true : false;
		if (!valid) {
			return null;
		}

		File[] runtime = plugins[0].listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (FOLDER_RUNTIME.equals(name)) {
					return true;
				}
				return false;
			}
		});
		valid = runtime.length > 0 ? true : false;
		if (!valid) {
			return null;
		}

		File[] settings = runtime[0].listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (FOLDER_SETTINGS.equals(name)) {
					return true;
				}
				return false;
			}
		});
		valid = settings.length > 0 ? true : false;
		if (!valid) {
			return null;
		}
		return settings[0];
	}

	private File findWorkspacePrefDev(File file) {
		File[] metadata = file.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (FOLDER_METADATA.equals(name)) {
					return true;
				}
				return false;
			}
		});
		boolean valid = false;
		valid = metadata.length > 0 ? true : false;
		if (!valid) {
			return null;
		}

		File[] plugins = metadata[0].listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (FOLDER_PLUGINS.equals(name)) {
					return true;
				}
				return false;
			}
		});
		valid = plugins.length > 0 ? true : false;
		if (!valid) {
			return null;
		}

		File[] runtime = plugins[0].listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (FOLDER_RUNTIME.equals(name)) {
					return true;
				}
				return false;
			}
		});
		valid = runtime.length > 0 ? true : false;
		if (!valid) {
			return null;
		}

		File[] settings = runtime[0].listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (FOLDER_SETTINGS.equals(name)) {
					return true;
				}
				return false;
			}
		});
		valid = settings.length > 0 ? true : false;
		if (!valid) {
			return null;
		}

		File[] preference = settings[0].listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (FILE_PREFERENCE_DEVICES.equals(name)) {
					return true;
				}
				return false;
			}
		});
		valid = preference.length > 0 ? true : false;
		if (!valid) {
			return null;
		}

		return preference[0];
	}

	@Override
	public void addPages() {
		this.selectPage = new ImportWorkspaceDevicesSelectPage();
		addPage(this.selectPage);
	}

}
