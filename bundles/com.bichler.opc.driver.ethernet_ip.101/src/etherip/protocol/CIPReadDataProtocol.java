/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package etherip.protocol;

import java.nio.ByteBuffer;

import etherip.types.CIPData;
import etherip.types.CNService;

/**
 * Protocol body for {@link CNService#CIP_ReadData}
 *
 * @author Kay Kasemir
 */
public class CIPReadDataProtocol extends ProtocolAdapter {
	private CIPData data;
	private int arraycount = 1;

	public CIPReadDataProtocol(int arraycount) {
		this.arraycount = arraycount;
	}

	@Override
	public int getRequestSize() {
		return 2;
	}

	@Override
	public void encode(final ByteBuffer buf) {
		buf.putShort((short) this.arraycount); // elements
	}

	@Override
	public void decode(final ByteBuffer buf, final int available) throws Exception {
		if (available <= 0) {
			data = null;
			return;
		}
		final CIPData.Type type = CIPData.Type.forCode(buf.getShort());
		final byte[] raw = new byte[available - 2];
		buf.get(raw);
		data = new CIPData(type, raw);
	}

	final public CIPData getData() {
		return data;
	}
}
