/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package etherip.types;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Control Net Path for element path
 * 
 * <p>
 * Example (with suitable static import):
 * <p>
 * <code>CNPath path = Symbol.name("my_tag")</code>
 * 
 * @author Kay Kasemir
 */
public class CNSymbolPath extends CNPath {
	private String symbol;

	/**
	 * Initialize
	 * 
	 * @param symbol Name of symbol
	 */
	protected CNSymbolPath(final String symbol) {
		this.symbol = symbol;
	}

	/** {@inheritDoc} */
	@Override
	public int getRequestSize() {
		String[] items = symbol.split("\\.");
		int size = 0;
		for (int i = 0; i < items.length; i++) {
			size += this.encode(items[i]).capacity();
		}
		return size;
	}

	/** {@inheritDoc} */
	@Override
	public void encode(final ByteBuffer buf) {
		buf.put((byte) (getRequestSize() / 2));
		// spec 4 p.21: "ANSI extended symbol segment"
		String[] items = symbol.split("\\.");
		for (int i = 0; i < items.length; i++) {
			buf.put(this.encode(items[i]).array());
		}
	}

	private ByteBuffer encode(String item) {
		byte index = -1;
		/**
		 * if we have an array definition, first remove array definition
		 */
		if (item.contains("[")) {
			if (!item.contains("]")) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Wrong array definition in path segment: {0}",
						item);
			} else {
				String[] items = item.split("\\[");
				String i = items[1].replace("]", "");
				if (i == null || i.isEmpty()) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"Wrong array definition in path segment: {0}", item);
				} else {
					item = items[0];
					try {
						index = Byte.parseByte(i);
					} catch (NumberFormatException ex) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage());
					}
				}
			}
		}
		ByteBuffer buf = ByteBuffer.allocate(2 + item.length() + (needPad(item) ? 1 : 0) + (index > -1 ? 2 : 0));
		buf.put((byte) 0x91);
		buf.put((byte) item.length());
		buf.put(item.getBytes());
		if (needPad(item))
			buf.put((byte) 0);
		/**
		 * we have an array definition
		 */
		if (index > -1) {
			buf.put((byte) 0x28);
			buf.put(index);
		}
		return buf;
	}

	/** @return Is path of odd length, requiring a pad byte? */
	private boolean needPad(String item) {
		// Findbugs: x%2==1 fails for negative numbers
		return (item.length() % 2) != 0;
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append("Path Symbol(0x91) '").append(symbol).append("'");
		if (needPad(symbol))
			buf.append(", 0x00");
		return buf.toString();
	}
}
