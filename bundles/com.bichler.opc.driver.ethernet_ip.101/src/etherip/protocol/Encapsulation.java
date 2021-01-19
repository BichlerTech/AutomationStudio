/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package etherip.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.bichler.opc.driver.ethernet_ip.ComEthernetIPCommands;

/**
 * Encapsulation commands
 * 
 * <p>
 * Spec 4 p. 164
 * 
 * @author Kay Kasemir
 */
public class Encapsulation implements Protocol {
	/** Byte size of encapsulation header */
	public static final int ENCAPSULATION_HEADER_SIZE = 24;
	private final ComEthernetIPCommands command;
	private int session;
	private final Protocol body;
	private static final byte[] context = new byte[] { 'F', 'u', 'n', 's', 't', 'u', 'f', 'f' };

	public Encapsulation(final ComEthernetIPCommands command, final int session, final Protocol body) {
		this.command = command;
		this.session = session;
		this.body = body;
	}

	/** {@inheritDoc} */
	@Override
	public int getRequestSize() {
		return ENCAPSULATION_HEADER_SIZE + body.getRequestSize();
	}

	/** {@inheritDoc} */
	@Override
	public void encode(final ByteBuffer buf) {
		final int status = 0;
		final int options = 0;
		buf.putShort((short) command.getValue());
		buf.putShort((short) body.getRequestSize());
		buf.putInt(session);
		buf.putInt(status);
		buf.put(context);
		buf.putInt(options);
		body.encode(buf);
	}

	/** {@inheritDoc} */
	@Override
	public int getResponseSize(final ByteBuffer buf) throws Exception {
		// Need at least the encapsulation header
		int needed = 24 - buf.position();
		if (needed > 0)
			return needed;
		// Buffer contains header (and maybe more after that)
		final short command_code = buf.getShort(0);
		final ComEthernetIPCommands command = ComEthernetIPCommands.forCode(command_code);
		if (command == null)
			throw new Exception("Received unknown command code " + command_code);
		if (command != this.command)
			throw new Exception("Received command " + command + " instead of " + this.command);
		final short body_size = buf.getShort(2);
		return 24 + body_size;
	}

	/** {@inheritDoc} */
	@Override
	public void decode(final ByteBuffer buf, final int available) throws Exception {
		// Start decoding
		final short command_code = buf.getShort();
		final ComEthernetIPCommands command = ComEthernetIPCommands.forCode(command_code);
		if (command == null)
			throw new Exception("Received unknown command code " + command_code);
		if (command != this.command)
			throw new Exception("Received command " + command + " instead of " + this.command);
		final short body_size = buf.getShort();
		final int session = buf.getInt();
		if (session != this.session) {
			// If we did not have a session, remember the newly obtained session
			if (this.session == 0)
				this.session = session;
			else
				// Error: Session changed
				throw new Exception("Received session " + session + " instead of " + this.session);
		}
		final int status = buf.getInt();
		final byte[] context = new byte[8];
		buf.get(context);
		if (!Arrays.equals(context, Encapsulation.context))
			throw new Exception("Received context " + context);
		// final int options =
		buf.getInt();
		if (status != 0)
			throw new Exception(String.format("Received status 0x%08X (%s)\n", status, getStatusMessage(status)));
		if (buf.remaining() < body_size)
			throw new Exception("Need " + body_size + " more bytes, have " + buf.remaining());
		body.decode(buf, body_size);
	}

	private String getStatusMessage(final int status) {
		switch (status) {
		case 0x00:
			return "OK";
		case 0x01:
			return "invalid/unsupported command";
		case 0x02:
			return "no memory on target";
		case 0x03:
			return "malformed data in request";
		case 0x64:
			return "invalid session ID";
		case 0x65:
			return "invalid data length";
		case 0x69:
			return "unsupported protocol revision";
		}
		return "<unknown>";
	}

	/** @return Session ID */
	final public int getSession() {
		return session;
	}
}
