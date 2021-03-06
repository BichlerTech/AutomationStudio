/*
 * ========================================================================
 * Copyright (c) 2005-2010 The OPC Foundation, Inc. All rights reserved.
 *
 * OPC Foundation MIT License 1.00
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED
 * "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * The complete license agreement can be found here:
 * http://opcfoundation.org/License/MIT/1.00/
 * ======================================================================
 */
package opc.sdk.core.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.utils.AttributesUtil;
import org.opcfoundation.ua.utils.ObjectUtils;

import opc.sdk.core.language.LanguageItem;
import opc.sdk.core.language.LanguagePack;
import opc.sdk.core.node.user.AuthorityRule;
import opc.sdk.core.subscription.IMonitoredItem;

public class Node extends Object implements Cloneable {
	protected NodeId NodeId;
	protected NodeClass NodeClass;
	protected QualifiedName BrowseName;
	protected LocalizedText DisplayName;
	protected LocalizedText Description;
	protected UnsignedInteger WriteMask;
	protected UnsignedInteger UserWriteMask;
	protected ReferenceNode[] References;
	/** OPC UA Monitored Items */
	private IMonitoredItem[] monitoredItems = null;
	/** user access authority rule */
	private int[] authority = null;
	private String browsepath = null;
	// flag to activate the node for the address spacea
	private boolean visible = true;
	private long[] readDrvIds = new long[0];
	private byte syncHistReadMask = 0;
	/**
	 * Language information for attributes displayname, description, [inversename
	 * ReferenceNode])
	 */
	LanguageItem languageItem = null;

	public Node() {
	}

	public Node(NodeId NodeId, NodeClass NodeClass, QualifiedName BrowseName, LocalizedText DisplayName,
			LocalizedText Description, UnsignedInteger WriteMask, UnsignedInteger UserWriteMask,
			ReferenceNode[] References) {
		this.NodeId = NodeId;
		this.NodeClass = NodeClass;
		this.BrowseName = BrowseName;
		this.DisplayName = DisplayName;
		this.Description = Description;
		this.WriteMask = WriteMask;
		this.UserWriteMask = UserWriteMask;
		this.References = References;
	}

	public NodeId getNodeId() {
		return NodeId;
	}

	public void setNodeId(NodeId NodeId) {
		this.NodeId = NodeId;
	}

	public NodeClass getNodeClass() {
		return NodeClass;
	}

	public void setNodeClass(NodeClass NodeClass) {
		this.NodeClass = NodeClass;
	}

	public QualifiedName getBrowseName() {
		return BrowseName;
	}

	public void setBrowseName(QualifiedName BrowseName) {
		this.BrowseName = BrowseName;
	}

	public LocalizedText getDisplayName() {
		return DisplayName;
	}

	public void setDisplayName(LocalizedText DisplayName) {
		this.DisplayName = DisplayName;
	}

	public LocalizedText getDescription() {
		return Description;
	}

	public void setDescription(LocalizedText Description) {
		this.Description = Description;
	}

	public UnsignedInteger getWriteMask() {
		return WriteMask;
	}

	public void setWriteMask(UnsignedInteger WriteMask) {
		this.WriteMask = WriteMask;
	}

	public UnsignedInteger getUserWriteMask() {
		return UserWriteMask;
	}

	public void setUserWriteMask(UnsignedInteger UserWriteMask) {
		this.UserWriteMask = UserWriteMask;
	}

	public ReferenceNode[] getReferences() {
		return References;
	}

	public void setReferences(ReferenceNode[] References) {
		this.References = References;
	}

	/**
	 * Deep clone
	 * 
	 * @return cloned Node
	 */
	public Node clone() {
		Node result = new Node();
		result.NodeId = NodeId;
		result.NodeClass = NodeClass;
		result.BrowseName = BrowseName;
		result.DisplayName = DisplayName;
		result.Description = Description;
		result.WriteMask = WriteMask;
		result.UserWriteMask = UserWriteMask;
		if (References != null) {
			result.References = new ReferenceNode[References.length];
			for (int i = 0; i < References.length; i++)
				result.References[i] = References[i].clone();
		}
		return result;
	}

	public ExpandedNodeId findProperty(QualifiedName browseName) {
		for (ReferenceNode reference : this.getReferences()) {
			if (!reference.getIsInverse() && reference.getReferenceTypeId().equals(Identifiers.HasProperty)) {
				return reference.getTargetId();
			}
		}
		return null;
	}

	/**
	 * Delete all refereces to targetid node
	 * 
	 * @param targetId
	 */
	public void deleteReferences(ExpandedNodeId targetId) {
		boolean found = false;
		ArrayList<ReferenceNode> referencesList = new ArrayList<ReferenceNode>(References.length);
		for (ReferenceNode reference : References) {
			if (targetId.equals(reference.getTargetId())) {
				found = true;
				continue;
			}
			referencesList.add(reference);
		}
		if (found) {
			ReferenceNode[] nodes = new ReferenceNode[referencesList.size()];
			for (int i = 0; i < nodes.length; i++) {
				nodes[i] = referencesList.get(i);
			}
			References = nodes;
		}
	}

	/**
	 * Delete specific reference
	 * 
	 * @param referenceTypeId
	 * @param isForward
	 * @param targetId
	 */
	public void deleteReferences(NodeId referenceTypeId, Boolean isForward, ExpandedNodeId targetId) {
		boolean found = false;
		ArrayList<ReferenceNode> referencesList = new ArrayList<ReferenceNode>(References.length);
		for (ReferenceNode reference : References) {
			if (targetId.equals(reference.getTargetId()) && referenceTypeId.equals(reference.getReferenceTypeId())
					&& reference.getIsInverse() == !isForward) {
				found = true;
				continue;
			}
			referencesList.add(reference);
		}
		if (found) {
			ReferenceNode[] nodes = new ReferenceNode[referencesList.size()];
			for (int i = 0; i < nodes.length; i++) {
				nodes[i] = referencesList.get(i);
			}
			References = nodes;
		}
	}

	// Added (again) on 21.1.09 by Mikko
	/**
	 * Finds all the targets of the specified reference types.
	 * 
	 * @param referenceTypeId
	 * @param isInverse
	 * @return an array of nodes referenced with the given type
	 */
	public org.opcfoundation.ua.builtintypes.ExpandedNodeId[] findTargets(NodeId referenceTypeId, boolean isInverse) {
		java.util.ArrayList<org.opcfoundation.ua.builtintypes.ExpandedNodeId> result = new java.util.ArrayList<org.opcfoundation.ua.builtintypes.ExpandedNodeId>();
		if (References != null)
			for (ReferenceNode reference : References) {
				if (reference == null || reference.getReferenceTypeId() == null) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, "reference or referencetypeid is null");
				} else {
					if (reference.getReferenceTypeId().equals(referenceTypeId) && org.opcfoundation.ua.utils.ObjectUtils
							.objectEquals(reference.getIsInverse(), isInverse)) {
						result.add(reference.getTargetId());
					}
				}
			}
		return result.toArray(new org.opcfoundation.ua.builtintypes.ExpandedNodeId[result.size()]);
	}

	/**
	 * Finds the targets for the first reference of the specified reference type.
	 * 
	 * @param referenceTypeId
	 * @param isInverse
	 * @return target reference node or null
	 */
	public org.opcfoundation.ua.builtintypes.ExpandedNodeId findTarget(NodeId referenceTypeId, boolean isInverse) {
		if (References == null)
			return null;
		for (ReferenceNode reference : References) {
			try {
				if (reference.getReferenceTypeId().equals(referenceTypeId)
						&& org.opcfoundation.ua.utils.ObjectUtils.objectEquals(reference.getIsInverse(), isInverse)) {
					return reference.getTargetId();
				}
			} catch (NullPointerException ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		// TODO what should we do??
		return null;
	}

	// Added by Mikko 12.12.2008
	public boolean supportsAttribute(UnsignedInteger attributeId) {
		if (attributeId.equals(Attributes.NodeId) || attributeId.equals(Attributes.NodeClass)
				|| attributeId.equals(Attributes.BrowseName) || attributeId.equals(Attributes.DisplayName)
				|| attributeId.equals(Attributes.Description) || attributeId.equals(Attributes.WriteMask)
				|| attributeId.equals(Attributes.UserWriteMask)) {
			return true;
		}
		return false;
	}

	// Added by Mikko 12.12.2008
	public ServiceResult readAttributeValue(UnsignedInteger attributeId, DataValue value, String[] locales) {
		// Validate attribute id
		if (!AttributesUtil.isValid(this.getNodeClass(), attributeId)) {
			return new ServiceResult(StatusCodes.Bad_AttributeIdInvalid);
		}
		// Read the attribute value.
		value.setValue(read(attributeId, locales));
		if (value.getServerTimestamp() == null) {
			value.setServerTimestamp(DateTime.currentTime());
		}
		return new ServiceResult(StatusCode.getFromBits(StatusCode.SEVERITY_GOOD));
	}

	public Variant read(UnsignedInteger attributeId, String[] locales) {
		if (attributeId.equals(Attributes.NodeId)) {
			return new Variant(this.getNodeId());
		} else if (attributeId.equals(Attributes.NodeClass)) {
			return new Variant(this.getNodeClass().getValue());
		} else if (attributeId.equals(Attributes.BrowseName)) {
			return new Variant(this.getBrowseName());
		} else if (attributeId.equals(Attributes.DisplayName)) {
			/** gets a localized text from a language pack */
			LocalizedText use = null;
			if (this.languageItem != null) {
				if (locales != null) {
					for (String locale : locales) {
						LanguagePack pack = this.languageItem.getLanguagePack(locale);
						if (pack != null) {
							use = pack.getDisplayname();
							break;
						}
					}
				}
			}
			if (use == null) {
				use = this.getDisplayName();
			}
			return new Variant(use);
		} else if (attributeId.equals(Attributes.Description)) {
			/** gets a localized text from a language pack */
			LocalizedText use = null;
			if (this.languageItem != null) {
				if (locales != null) {
					for (String locale : locales) {
						LanguagePack pack = this.languageItem.getLanguagePack(locale);
						if (pack != null) {
							use = pack.getDescription();
							break;
						}
					}
				}
			}
			if (use == null) {
				use = this.getDescription();
			}
			return new Variant(use);
		} else if (attributeId.equals(Attributes.WriteMask)) {
			return new Variant(this.getWriteMask());
		} else if (attributeId.equals(Attributes.UserWriteMask)) {
			return new Variant(this.getUserWriteMask());
		}
		return new Variant(false);
	}

	public ServiceResult write(UnsignedInteger attributeId, Object value) {
		if (attributeId.equals(Attributes.BrowseName)) {
			BrowseName = (QualifiedName) value;
			return new ServiceResult(StatusCode.GOOD);
		}
		if (attributeId.equals(Attributes.DisplayName)) {
			DisplayName = (LocalizedText) value;
			return new ServiceResult(StatusCode.GOOD);
		}
		if (attributeId.equals(Attributes.Description)) {
			Description = (LocalizedText) value;
			return new ServiceResult(StatusCode.GOOD);
		}
		if (attributeId.equals(Attributes.WriteMask)) {
			WriteMask = (UnsignedInteger) value;
			return new ServiceResult(StatusCode.GOOD);
		}
		if (attributeId.equals(Attributes.UserWriteMask)) {
			UserWriteMask = (UnsignedInteger) value;
			return new ServiceResult(StatusCode.GOOD);
		}
		return new ServiceResult(StatusCodes.Bad_AttributeIdInvalid);
	}

	/**
	 * Checks if a reference already exists. If not adds passed reference to node.
	 */
	public void ensureReferenceExists(ReferenceNode referenceToAdd) {
		// Check if reference already exists.
		for (ReferenceNode reference : References) {
			if (reference.getIsInverse().equals(referenceToAdd.getIsInverse())) {
				if (reference.getReferenceTypeId() != null
						&& reference.getReferenceTypeId().equals(referenceToAdd.getReferenceTypeId())) {
					if (reference.getTargetId().equals(referenceToAdd.getTargetId())) {
						return;
					}
				}
			}
		}
		// Add reference to node's reference nodes.
		ReferenceNode[] referenceNodes = new ReferenceNode[References.length + 1];
		for (int i = 0; i < References.length; i++) {
			referenceNodes[i] = References[i];
		}
		referenceNodes[References.length] = referenceToAdd;
		References = referenceNodes;
	}

	public String toString() {
		return "Node: " + ObjectUtils.printFieldsDeep(this);
	}

	public void setLanguage(LanguageItem languageItem) {
		this.languageItem = languageItem;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Registers an OPC monitored item
	 * 
	 * @param opcMonitoredItem
	 * @return
	 */
	public boolean register(IMonitoredItem opcMonitoredItem) {
		boolean insertable = true;
		List<IMonitoredItem> remaining = new ArrayList<>();
		// fill existing
		if (this.monitoredItems != null) {
			for (IMonitoredItem item : this.monitoredItems) {
				remaining.add(item);
				// do not insert if exist
				if (item.getSubscriptionId() == opcMonitoredItem.getSubscriptionId()
						&& item.getMonitoredItemId() == opcMonitoredItem.getMonitoredItemId()) {
					insertable = false;
					break;
				}
			}
		}
		if (insertable) {
			remaining.add(opcMonitoredItem);
			this.monitoredItems = remaining.toArray(new IMonitoredItem[0]);
		}
		return insertable;
	}

	public IMonitoredItem[] getMonitoredItems() {
		return this.monitoredItems;// .values().toArray(new IMonitoredItem[0]);
	}

	/**
	 * Unregisters an OPC monitored item
	 * 
	 * @param opcMonitoredItem
	 * @return
	 */
	public boolean unregister(IMonitoredItem opcMonitoredItem) {
		if (this.monitoredItems != null) {
			int newPosition = this.monitoredItems.length;
			List<IMonitoredItem> remaining = new ArrayList<>();
			for (IMonitoredItem item : this.monitoredItems) {
				if (item.getSubscriptionId() == opcMonitoredItem.getSubscriptionId()
						&& item.getMonitoredItemId() == opcMonitoredItem.getMonitoredItemId()) {
					continue;
				}
				remaining.add(item);
			}
			boolean found = (newPosition != remaining.size()) ? true : false;
			if (found) {
				if (remaining.isEmpty()) {
					this.monitoredItems = null;
				} else {
					this.monitoredItems = remaining.toArray(new IMonitoredItem[0]);
				}
			}
			return found;
		}
		return false;
	}

	public void setAuthority(Integer index, int authority) {
		// init index
		if (this.authority == null) {
			this.authority = new int[index + 1];
		}
		if (this.authority.length <= index) {
			int[] help = this.authority;
			this.authority = new int[index + 1];
			// copy old values
			for (int i = 0; i < this.authority.length; i++) {
				if (help.length > i) {
					this.authority[i] = help[i];
				} else {
					this.authority[i] = -1;
				}
			}
		}
		this.authority[index] = authority;
	}

	// AuthorityRule.ALL = 16383
	private static final int MAX_AUTHORITY = 16383;

	public int[] getAuthority() {
		return this.authority;
	}

	private int getAuthority(int index) {
		// Wenn keine Rechte auf einen Knoten gesetzt sind, werden alle
		// Benutzerrechte vergeben.
		if (this.authority == null) {
			return MAX_AUTHORITY;
		}
		// Wenn keine Rechte auf einen Knoten gesetzt sind, werden alle
		// Benutzerrechte vergeben.
		if (index >= this.authority.length) {
			return MAX_AUTHORITY;
		}
		int roleAuthority = this.authority[index];
		// 0 == keine benutzerrechte
		if (roleAuthority == 0) {
			return MAX_AUTHORITY;
		}
		return roleAuthority;
	}

	public boolean checkAuthority(int[] indizes, AuthorityRule serviceRule) {
		boolean valid = false;
		for (int index : indizes) {
			Set<AuthorityRule> authorities = AuthorityRule.getSet(getAuthority(index));
			// is authority set for user node role
			valid = authorities.contains(serviceRule);
			if (valid) {
				break;
			}
		}
		return valid;
	}

	public String getBrowsepath() {
		return browsepath;
	}

	public void setBrowsepath(String browsepath) {
		this.browsepath = browsepath;
	}

	public byte getSyncHistReadMask() {
		return syncHistReadMask;
	}

	public void setSyncHistReadMask(byte histread) {
		syncHistReadMask = histread;
	}

	public long[] getReadDriverIds() {
		return this.readDrvIds;
	}

	public void addReadDriverId(long drvId) {
		long[] newDrvIds = new long[this.readDrvIds.length + 1];
		boolean found = false;
		for (int i = 0; i < this.readDrvIds.length; i++) {
			if (this.readDrvIds[i] == drvId) {
				found = true;
				break;
			}
			newDrvIds[i] = this.readDrvIds[i];
		}
		if (found) {
			return;
		}
		newDrvIds[this.readDrvIds.length] = drvId;
		this.readDrvIds = newDrvIds;
	}

	public void removeReadDriverId(long drvId) {
		long[] newDrvIds = new long[this.readDrvIds.length - 1];
		boolean found = false;
		int index = 0;
		for (int i = 0; i < this.readDrvIds.length; i++) {
			if (this.readDrvIds[i] == drvId) {
				found = true;
				continue;
			}
			// check if not found
			if (i >= newDrvIds.length && !found) {
				continue;
			}
			newDrvIds[index] = this.readDrvIds[i];
			index++;
		}
		if (!found) {
			return;
		}
	}
}
