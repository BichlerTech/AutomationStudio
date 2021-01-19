package com.bichler.scriptmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class EcmaScriptManager {

	/**
	 * shell script names which are loaded as the first step of the opc ua server.
	 */
	protected ArrayList<String> single = new ArrayList<String>();

	/**
	 * shell script names which are loaded as the last step of the opc ua server.
	 */
	protected Map<Integer, ArrayList<String>> interval = new HashMap<Integer, ArrayList<String>>();

	private ScriptEngineManager manager = null;
	private ScriptEngine engine = null;

	private String ecmaScriptFolder = "ecmascripts/";
	private String ecmaScriptInternalFolderName = "internal/";
	private String startConfigName = "start.conf";

	public ScriptEngine getEngine() {
		return engine;
	}

	public void setEngine(ScriptEngine engine) {
		this.engine = engine;
	}

	private List<Timer> timers = null;

	/**
	 * Default constructor with no effect.
	 */
	public EcmaScriptManager() {
		this.manager = new ScriptEngineManager();
		this.engine = manager.getEngineByName("JavaScript");
		this.timers = new ArrayList<Timer>();
	}

	private boolean loadInternalScripts() {
		boolean success = false;
		ArrayList<String> scripts = new ArrayList<>();
		File files = new File(ecmaScriptFolder + ecmaScriptInternalFolderName);
		if (files.isDirectory()) {
			for (String file : files.list()) {
				// if we are on linux systems, so skip . and ..
				if (file.endsWith(".") || file.endsWith("..") || !file.endsWith(".js"))
					continue;
				scripts.add(ecmaScriptFolder + ecmaScriptInternalFolderName + file);
			}
		}
		if (!scripts.isEmpty()) {
			success = this.execScripts(scripts);
		}

		Logger.getLogger(getClass().getName()).log(Level.INFO, "Loading internal scripts finished");
		return success;
	}

	/**
	 * Loads all shell scripts which are configured in the start.conf file. the
	 * following reagions are required.
	 * 
	 * #presection here we can insert all script file names which should be executed
	 * before the server starts to load it's address space.
	 * 
	 * #postsection here we can insert all script file names which should be
	 * executed after the server loaded it's address space.
	 * 
	 * @return
	 */
	public boolean loadEcmaScripts() {
		ArrayList<String> scripts = null;

		// firs load all internal system scripts
		loadInternalScripts();

		// File file = new File("ecmascripts/start.conf");
		File file = new File(ecmaScriptFolder + startConfigName);
		String line = "";
		BufferedReader reader;

		if (file != null && file.exists() && file.isFile()) {
			try {
				reader = new BufferedReader(new FileReader(file));
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("#single")) {
						scripts = single;
					} else if (line.startsWith("#interval")) {
						// single = new ArrayList<String>();
					} else if (line.startsWith("##")) {
						line = line.replace("##", "");
						try {
							int seconds = Integer.parseInt(line);
							if (scripts.contains(line)) {
								scripts = this.interval.get(seconds);
							} else {
								scripts = new ArrayList<String>();
								this.interval.put(seconds, scripts);
							}
						} catch (NumberFormatException ex) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
						}
					} else if (line.startsWith("#") || line.trim().length() == 0) {
						// do nothing we have a comment or an empty line
						continue;
					} else {
						// is the script active
						String[] lineItems = line.split(":");
						int active = 1;
						if (lineItems.length > 1) {
							try {
								active = Integer.parseInt(lineItems[0]);
							} catch (NumberFormatException ex) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
								active = 0;
							}
							line = lineItems[1];
						}
						// we also have to check the file.exists function
						if (active == 1 && new File(ecmaScriptFolder + line + ".js").exists()) {
							scripts.add(ecmaScriptFolder + line + ".js");
						}
					}
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}

		return false;
	}

	/**
	 * Executes the inserted scripts form the post-section after the server has
	 * started and loaded it's address space.
	 * 
	 * @return
	 */
	public boolean execSingleScripts() {
		return this.execScripts(this.single);
	}

	/**
	 * Executes the inserted scripts form the post-section after the server has
	 * started and loaded it's address space.
	 * 
	 * @return
	 */
	public boolean execIntervalScripts() {
		Set<Integer> keySet = this.interval.keySet();
		TimerTask timertask = null;
		Timer timer = null;
		File file = null;
		String line = "";
		BufferedReader reader;
		ArrayList<String> list = null;
		StringBuffer buffer = null;

		for (int key : keySet) {

			list = this.interval.get(key);

			for (String script : list) {
				buffer = new StringBuffer();

				file = new File(script);
				if (file.exists()) {
					try {
						reader = new BufferedReader(new FileReader(file));

						while ((line = reader.readLine()) != null) {
							buffer.append(line + "\n");
						}
					} catch (IOException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
					}
				}

				final EcmaScript ecs = new EcmaScript();
				ecs.setName(script);
				ecs.setScript(buffer.toString());
				// final String eval = buffer.toString();

				timertask = new TimerTask() {

					@Override
					public void run() {
						try {
							engine.eval(ecs.getScript());
						} catch (ScriptException e) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE,
									"Script: " + ecs.getName() + " - " + e.getMessage(), e);
						}
					}
				};
				timer = new Timer();
				timers.add(timer);
				timer.schedule(timertask, 0, key);
			}

		}
		return true;
	}

	/**
	 * Executes the inserted shell scripts.
	 * 
	 * @param scripts
	 * @return
	 */
	public boolean execScripts(final List<String> scripts) {
		File file = null;
		String line = "";
		BufferedReader reader;
		StringBuffer buffer = null;
		try {
			// execute each configured shell script
			for (String script : scripts) {

				file = new File(script);
				if (file.exists()) {
					buffer = new StringBuffer();
					reader = new BufferedReader(new FileReader(file));
					while ((line = reader.readLine()) != null) {
						buffer.append(line + "\n");
					}
					/** now execute java script */
					try {
						this.engine.eval(buffer.toString());
					} catch (ScriptException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Script: " + script + " - " + e.getMessage(), e);
					}
				}
			}
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
		return true;
	}

	public String getEcmaScriptFolderName() {
		return ecmaScriptFolder;
	}

	public void setEcmaScriptFolderName(String ecmaScriptFolder) {
		this.ecmaScriptFolder = ecmaScriptFolder;
		if (!this.ecmaScriptFolder.endsWith(File.separator))
			this.ecmaScriptFolder += File.separator;
	}

	public String getStartConfigName() {
		return startConfigName;
	}

	public void setStartConfigName(String startConfigName) {
		this.startConfigName = startConfigName;
	}

	public String getEcmaScriptInternalFolderName() {
		return ecmaScriptInternalFolderName;
	}

	public void setEcmaScriptInternalFolderName(String ecmaScriptInternalFolderName) {
		this.ecmaScriptInternalFolderName = ecmaScriptInternalFolderName;
		if (!this.ecmaScriptInternalFolderName.endsWith(File.separator))
			this.ecmaScriptInternalFolderName += File.separator;
	}
}
