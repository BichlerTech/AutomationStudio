package com.bichler.opcua.statemachine;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;
import com.bichler.opcua.statemachine.transform.PluginStateMachineToOpcTransformer;

public class BaseStatemachineActivator implements BundleActivator {

	public static String PLUGIN_ID = "com.bichler.opcua.statemachine";

	public static final String EXTENSION_UML = "uml";
	public static final String RESOURCES = "resources";
	
	public static final String DEFAULT_NODESET = "Opc.Ua.NodeSet2";
	
	public static final FileFilter FF_DEFAULTNODESET = new FileFilter() {

		@Override
		public boolean accept(File file) {
			if (file.getName().startsWith(DEFAULT_NODESET)) {
				return true;
			}
			return false;
		}
	};

	public static final FileFilter FF_UMLCLASSMODEL = new FileFilter() {

		@Override
		public boolean accept(File file) {
			if (file.getName().startsWith(DEFAULT_UML_CLASS_MODEL)) {
				return true;
			}
			return false;
		}
	};
	
	public static final FileFilter FF_UMLPROJECT = new FileFilter() {

		@Override
		public boolean accept(File file) {
			if (file.getName().startsWith(DEFAULT_UML_CLASS_MODEL)) {
				return true;
			} else if (file.getName().startsWith(DEFAULT_UML_NOTATION_MODEL)) {
				return true;
			} else if (file.getName().startsWith(DEFAULT_UML_DI_MODEL)) {
				return true;
			}
			return false;
		}
	};
	
	private static final String EXTENSION_XML = "xml";
	
	private static final String DEFAULT_UML_CLASS_MODEL = "OpcFoundationUA.uml";
	private static final String DEFAULT_UML_NOTATION_MODEL = "OpcFoundationUA.notation";
	private static final String DEFAULT_UML_DI_MODEL = "OpcFoundationUA.di";

	private static final String ENDOF_ARGS = "@noDefault";
	
	private static final String KEY_SUN_COMMANDLINE_ARGS = "sun.java.command";
	
	// The shared instance
	private static BaseStatemachineActivator plugin;

	private BundleContext bundleContext = null;

	public BaseStatemachineActivator() {

	}

	@Override
	public void start(BundleContext bundleContext) {
		BaseStatemachineActivator.setPlugin(this);
		this.bundleContext = bundleContext;

		String[] frameworkArguments = getFrameworkArguments(bundleContext);

		if (frameworkArguments != null) {
			if (frameworkArguments.length <= 5) {
				// fetch java properties
				Properties properties = System.getProperties();
				String commandline = (String) properties.get(KEY_SUN_COMMANDLINE_ARGS);
				// statemachine filename to transform
				String arguments = commandline.substring(commandline.indexOf(ENDOF_ARGS)+ENDOF_ARGS.length(), commandline.length()).trim();
				int indexUML = arguments.indexOf("."+EXTENSION_UML)+("."+EXTENSION_UML).length();
				// *.uml file
				String fileUML = arguments.substring(0, indexUML).trim();
				File source = new File(fileUML);
				if (!source.exists()) {
					System.out.println("ERROR - "+fileUML+" - *.uml model file does not exist");
					return;
				}
				System.out.println("--------------");
				System.out.println("Reading UML modelfile - "+fileUML);				
				// *.xml nodeset				
				String fileXML = arguments.substring(indexUML, arguments.length()).trim();				
				File output = new File(fileXML);
				System.out.println("Writing to XML nodeset file - "+ fileXML);
				
				AbstractStateMachineToOpcTransformer transformer = new PluginStateMachineToOpcTransformer(true);
				try {
					transformer.transform(source, output);
					System.out.println("SUCCESS - Generated OpcUa statemachine type model");
				} catch (StatemachineException e) {
					System.out.println("ERROR - Failed to generated OpcUa statemachine model");
					System.out.println(e.getMessage());
				}
				System.out.println("--------------");
				System.exit(1);
			}
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		BaseStatemachineActivator.setPlugin(null);
		this.bundleContext = null;
	}

	/**
	 * Resource files defining a default OPC UA nodeset (Namespace
	 * http://opcfoundation.org/UA/
	 * 
	 * @return Default Nodeset XML files
	 */
	public File[] getResourceDefaultOpcUaModelFiles() {
		return getFile(BaseStatemachineActivator.plugin.bundleContext.getBundle(), Path.ROOT.append(RESOURCES))
				.listFiles(FF_DEFAULTNODESET);
//				listFiles(new FileFilter() {
//
//					@Override
//					public boolean accept(File file) {
//						if (file.getName().startsWith(DEFAULT_NODESET)) {
//							return true;
//						}
//						return false;
//					}
//				});
	}
	
	/**
	 * Resource files defining a default OPC UA UML typeset
	 * 
	 * @return Default OPC UA typeset (Namespace http://opcfoundation.org/UA/
	 */
	public File[] getUMLResourceDefaultOpcUaClassFiles() {
		return getFile(BaseStatemachineActivator.plugin.bundleContext.getBundle(), Path.ROOT.append(RESOURCES))
				.listFiles(new FileFilter() {

					@Override
					public boolean accept(File file) {
						if (file.getName().startsWith(DEFAULT_UML_CLASS_MODEL)) {
							return true;
						}
						return false;
					}
				});
	}

	public File[] getUMLBaseTypeFiles() {
		return getFile(BaseStatemachineActivator.plugin.bundleContext.getBundle(), Path.ROOT.append(RESOURCES))
				.listFiles(FF_UMLPROJECT);
	}

	/**
	 * Returns a bundle/plugin file
	 * 
	 * @param bundle BaseStatemachine bundlecontext
	 * @param path   Path of the file
	 * @return File from the plugin
	 */
	private File getFile(Bundle bundle, IPath path) {
		URL url = FileLocator.find(bundle, path, null);
		if (url != null) {
			try {
				URL url2 = FileLocator.toFileURL(url);
				// remove '/' beginning of path if possible
				return new File(url2.getFile());
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Returns framework arguments from the environment info.
	 * 
	 * @param bc BundleContext
	 * @return framework arguments
	 */
	private String[] getFrameworkArguments(BundleContext bc) {
		EnvironmentInfo envInfo = getEnvironmentInfo(bc);
		return (envInfo.getFrameworkArgs());
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static BaseStatemachineActivator getDefault() {
		return plugin;
	}

	/**
	 * Sets the plugin activator.
	 * 
	 * @param plugin BaseStatemachineActivator plugin
	 */
	private static void setPlugin(BaseStatemachineActivator plugin) {
		BaseStatemachineActivator.plugin = plugin;
	}

	/**
	 * EnvironmentInfo for the plugin
	 * 
	 * @param bc BundleContext
	 * @return
	 */
	private static EnvironmentInfo getEnvironmentInfo(BundleContext bc) {
		if (bc == null)
			return null;
		ServiceReference infoRef = bc.getServiceReference(EnvironmentInfo.class.getName());
		if (infoRef == null)
			return null;
		EnvironmentInfo envInfo = (EnvironmentInfo) bc.getService(infoRef);
		if (envInfo == null)
			return null;
		bc.ungetService(infoRef);
		return envInfo;
	}

}
