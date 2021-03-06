package opc.sdk.server.language;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import opc.sdk.core.language.LanguageItem;
import opc.sdk.core.node.Node;
import opc.sdk.server.core.managers.OPCAddressSpaceManager;

import org.opcfoundation.ua.builtintypes.NodeId;

public class LanguageTable {
	private Map<NodeId, LanguageItem> languageTable = new LinkedHashMap<>();
	private Set<String> languages = new HashSet<>();

	/**
	 * LanguageTable for an OPC UA server
	 */
	protected LanguageTable() {
	}

	/**
	 * Adds a Language item
	 * 
	 * @param Item Languages for OPC UA node attributes (Displayname, Description,
	 *             {Inversename[ReferenceTypeNode]})
	 */
	protected void addLanguageItem(LanguageItem item) {
		this.languageTable.put(item.getNodeId(), item);
		this.languages.addAll(item.getLanguages());
	}

	protected void removeLanguageItem(NodeId nodeId) {
		this.languageTable.remove(nodeId);
	}

	public void connectWithNodes(OPCAddressSpaceManager manager) {
		for (Entry<NodeId, LanguageItem> entry : this.languageTable.entrySet()) {
			NodeId nodeId = entry.getKey();
			LanguageItem languageItem = entry.getValue();
			Node node = manager.getNodeById(nodeId);
			node.setLanguage(languageItem);
		}
	}

	public LanguageItem[] getLanguageInformation() {
		List<LanguageItem> items = new ArrayList<>();
		for (LanguageItem item : this.languageTable.values()) {
			if (NodeId.isNull(item.getNodeId())) {
				continue;
			}
			if (item.getLanguagePacks().length == 0) {
				continue;
			}
			items.add(item);
		}
		return items.toArray(new LanguageItem[0]);
	}

	/**
	 * Creates or gets a new language item
	 * 
	 * @param nodeIds
	 * @return
	 */
	public LanguageItem[] getLanguageInformation(NodeId[] nodeIds) {
		List<LanguageItem> items = new ArrayList<>();
		for (NodeId nodeId : nodeIds) {
			LanguageItem item = this.languageTable.get(nodeId);
			if (item == null) {
				item = new LanguageItem(nodeId);
				this.languageTable.put(nodeId, item);
			}
			items.add(item);
		}
		return items.toArray(new LanguageItem[0]);
	}

	public String[] getLocales() {
		return this.languages.toArray(new String[0]);
	}
}
