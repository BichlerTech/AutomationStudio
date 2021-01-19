package com.bichler.scriptmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ShellScriptManager {

	/**
	 * shell script names which are loaded as the first step of the opc ua server.
	 */
	protected List<String> prescripts = new ArrayList<String>();

	/**
	 * shell script names which are loaded as the last step of the opc ua server.
	 */
	protected List<String> postcripts = new ArrayList<String>();

	/**
	 * Default constructor with no effect.
	 */
	public ShellScriptManager() {

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
	public boolean loadShellScripts() {
		List<String> scripts = null;

		File file = new File("shellscripts/start.conf");
		String line = "";
		BufferedReader reader;

		if (file != null && file.exists() && file.isFile()) {
			try {
				reader = new BufferedReader(new FileReader(file));
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("#presection")) {
						scripts = prescripts;
					} else if (line.startsWith("#postsection")) {
						scripts = postcripts;
					} else if (line.startsWith("#") || line.trim().length() == 0) {
						// do nothing we have a comment or an empty line
						continue;
					} else {
						// we also have to check the file.exists function
						if (new File("shellscripts/" + line).exists()) {
							scripts.add("shellscripts/" + line);
						}
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	public boolean execPreScripts() {
		return this.execScripts(this.prescripts);
	}

	/**
	 * Executes the inserted scripts form the post-section after the server has
	 * started and loaded it's address space.
	 * 
	 * @return
	 */
	public boolean execPostScripts() {
		return this.execScripts(this.postcripts);
	}

	/**
	 * Executes the inserted shell scripts.
	 * 
	 * @param scripts
	 * @return
	 */
	private boolean execScripts(final List<String> scripts) {

		try {
			// execute each configured shell script
			for (String script : scripts) {
				Process p = Runtime.getRuntime().exec(script);
				InputStream is = p.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(is));

				String aux = br.readLine();

				while (aux != null) {
					System.out.println(aux);

					aux = br.readLine();
				}
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
