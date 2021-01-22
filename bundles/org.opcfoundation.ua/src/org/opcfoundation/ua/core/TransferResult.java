/* ========================================================================
 * Copyright (c) 2005-2015 The OPC Foundation, Inc. All rights reserved.
 *
 * OPC Foundation MIT License 1.00
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * The complete license agreement can be found here:
 * http://opcfoundation.org/License/MIT/1.00/
 * ======================================================================*/

package org.opcfoundation.ua.core;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.utils.ObjectUtils;
import org.opcfoundation.ua.common.NamespaceTable;

import java.util.Arrays;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.utils.AbstractStructure;

public class TransferResult extends AbstractStructure {

	public static final ExpandedNodeId ID = new ExpandedNodeId(null, NamespaceTable.OPCUA_NAMESPACE,
			Identifiers.TransferResult.getValue(), null);
	public static final ExpandedNodeId BINARY = new ExpandedNodeId(null, NamespaceTable.OPCUA_NAMESPACE,
			Identifiers.TransferResult_Encoding_DefaultBinary.getValue(), null);
	public static final ExpandedNodeId XML = new ExpandedNodeId(null, NamespaceTable.OPCUA_NAMESPACE,
			Identifiers.TransferResult_Encoding_DefaultXml.getValue(), null);

	protected StatusCode StatusCode;
	protected UnsignedInteger[] AvailableSequenceNumbers;

	public TransferResult() {
	}

	public TransferResult(StatusCode StatusCode, UnsignedInteger[] AvailableSequenceNumbers) {
		this.StatusCode = StatusCode;
		this.AvailableSequenceNumbers = AvailableSequenceNumbers;
	}

	public StatusCode getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(StatusCode StatusCode) {
		this.StatusCode = StatusCode;
	}

	public UnsignedInteger[] getAvailableSequenceNumbers() {
		return AvailableSequenceNumbers;
	}

	public void setAvailableSequenceNumbers(UnsignedInteger[] AvailableSequenceNumbers) {
		this.AvailableSequenceNumbers = AvailableSequenceNumbers;
	}

	/**
	 * Deep clone
	 *
	 * @return cloned TransferResult
	 */
	public TransferResult clone() {
		TransferResult result = (TransferResult) super.clone();
		result.StatusCode = StatusCode;
		result.AvailableSequenceNumbers = AvailableSequenceNumbers == null ? null : AvailableSequenceNumbers.clone();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransferResult other = (TransferResult) obj;
		if (StatusCode == null) {
			if (other.StatusCode != null)
				return false;
		} else if (!StatusCode.equals(other.StatusCode))
			return false;
		if (AvailableSequenceNumbers == null) {
			if (other.AvailableSequenceNumbers != null)
				return false;
		} else if (!Arrays.equals(AvailableSequenceNumbers, other.AvailableSequenceNumbers))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((StatusCode == null) ? 0 : StatusCode.hashCode());
		result = prime * result + ((AvailableSequenceNumbers == null) ? 0 : Arrays.hashCode(AvailableSequenceNumbers));
		return result;
	}

	public ExpandedNodeId getTypeId() {
		return ID;
	}

	public ExpandedNodeId getXmlEncodeId() {
		return XML;
	}

	public ExpandedNodeId getBinaryEncodeId() {
		return BINARY;
	}

	public String toString() {
		return "TransferResult: " + ObjectUtils.printFieldsDeep(this);
	}

}