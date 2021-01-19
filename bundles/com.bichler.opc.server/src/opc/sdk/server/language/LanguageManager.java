package opc.sdk.server.language;

import org.opcfoundation.ua.builtintypes.NodeId;

import opc.sdk.core.language.LanguageItem;
import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.core.managers.IOPCManager;
import opc.sdk.server.core.managers.OPCAddressSpaceManager;

public class LanguageManager implements IOPCManager {
	/** table contains all languages */
	private LanguageTable languageTable = null;
	/** server address space manager */
	private OPCAddressSpaceManager manager = null;

	LanguageManager() {
		this.languageTable = new LanguageTable();
	}

	public LanguageManager(OPCAddressSpaceManager manager) {
		this();
		this.manager = manager;
	}

	/**
	 * Connects the OPC UA Node with the given Language pack
	 */
	public void connectNode2Language() {
		this.languageTable.connectWithNodes(this.manager);
	}

	public LanguageItem[] getLanguageInformation(NodeId[] nodeids) {
		return this.languageTable.getLanguageInformation(nodeids);
	}

	public LanguageItem[] getLanguageInformation() {
		return this.languageTable.getLanguageInformation();
	}

	public String[] getServerLocales() {
		return this.languageTable.getLocales();
	}

	/**
	 * Adds a language item to the table
	 * 
	 * @param item Representing a connection to an OPC UA node, containg all
	 *             language information
	 */
	public void importLanguage(LanguageItem item) {
		this.languageTable.addLanguageItem(item);
	}

	public void importLanguage(LanguageItem[] items) {
		for (LanguageItem item : items) {
			this.languageTable.addLanguageItem(item);
		}
	}

	@Override
	public boolean start() {
		connectNode2Language();
		this.hasInitialized = true;
		return this.hasInitialized;
	}

	@Override
	public boolean stop() {
		this.hasInitialized = false;
		return this.hasInitialized;
	}

	@Override
	public OPCInternalServer getServer() {
		return this.manager.getServer();
	}

	boolean hasInitialized = false;

	@Override
	public boolean isInitialized() {
		return this.hasInitialized;
	}
}
