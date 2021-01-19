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

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.utils.ObjectUtils;

public class ObjectTypeNode extends Node implements Cloneable {
	protected Boolean IsAbstract;

	public ObjectTypeNode() {
	}

	public ObjectTypeNode(NodeId NodeId, NodeClass NodeClass, QualifiedName BrowseName, LocalizedText DisplayName,
			LocalizedText Description, UnsignedInteger WriteMask, UnsignedInteger UserWriteMask,
			ReferenceNode[] References, Boolean IsAbstract) {
		super(NodeId, NodeClass, BrowseName, DisplayName, Description, WriteMask, UserWriteMask, References);
		this.IsAbstract = IsAbstract;
	}

	public Boolean getIsAbstract() {
		return IsAbstract;
	}

	public void setIsAbstract(Boolean IsAbstract) {
		this.IsAbstract = IsAbstract;
	}

	/**
	 * Deep clone
	 * 
	 * @return cloned ObjectTypeNode
	 */
	public ObjectTypeNode clone() {
		ObjectTypeNode result = new ObjectTypeNode();
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
		result.IsAbstract = IsAbstract;
		return result;
	}

	public boolean supportsAttribute(UnsignedInteger attributeId) {
		if (attributeId.equals(Attributes.IsAbstract)) {
			return true;
		}
		return super.supportsAttribute(attributeId);
	}

	@Override
	public Variant read(UnsignedInteger attributeId, String[] locales) {
		if (attributeId.equals(Attributes.IsAbstract))
			return new org.opcfoundation.ua.builtintypes.Variant(IsAbstract);
		return super.read(attributeId, locales);
	}

	public String toString() {
		return "ObjectTypeNode: " + ObjectUtils.printFieldsDeep(this);
	}
}
