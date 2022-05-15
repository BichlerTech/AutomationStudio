/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package etherip.types;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import etherip.protocol.Connection;

/**
 * ControlNet data types
 * 
 * <p>
 * Spec. 5 p.3
 * 
 * <p>
 * Raw CIP data is kept in a <code>byte[]</code>. This class can decode the data
 * or manipulate it.
 * 
 * @author Kay Kasemir
 */
final public class CIPData {
	public static enum Type {
		BOOL(0x00C1, 1), SINT(0x00C2, 1), INT(0x00C3, 2), DINT(0x00C4, 4), LINT(0x00C5, 8),
		USINT(0x00C6, 1), UINT(0x00C7, 2), UDINT(0x00C8, 4), ULINT(0x00C9, 8), 
		REAL(0x00CA, 4), BITS(0x00D3, 4), LREAL(0x00CB, 8),
		// Order of enums matter: BITS is the last numeric type (not-string)
		STRUCT(0x02A0, 0),
		/**
		 * Experimental: The ENET doc just shows several structures for TIMER, COUNTER,
		 * CONTROL and indicates that the T_CIP_STRUCT = 0x02A0 is followed by two more
		 * bytes, shown as "?? ??". Looks like for strings, those are always 0x0FCE,
		 * followed by DINT length, characters and more zeroes
		 */
		STRUCT_STRING(0x0FCE, 1);
		final private short code;
		final private int element_size;

		public static Type forCode(final short code) throws Exception {
			for (Type type : values())
				if (type.code == code)
					return type;
			throw new Exception("Unknown CIP type code 0x" + Integer.toHexString(code));
		}

		private Type(final int code, final int element_size) {
			this.code = (short) code;
			this.element_size = element_size;
		}

		@Override
		final public String toString() {
			return name() + String.format(" (0x%04X)", code);
		}
	};

	/** Data type */
	final private Type type;
	/** Number of elements (i.e. number of array elements, not bytes) */
	private short elements;
	/** Raw data, not including type code or element count */
	private ByteBuffer data;

	/**
	 * Initialize empty CIP data
	 * 
	 * @param type     Data {@link Type}
	 * @param elements Number of elements
	 * @throws Exception when type is not handled
	 */
	public CIPData(final Type type, final int elements) throws Exception {
		switch (type) {
		case BOOL:
		case SINT:
		case INT:
		case DINT:
		case LINT:
		case USINT:
		case UINT:
		case UDINT:
		case ULINT:
		case BITS:
		case REAL:
		case LREAL:
			this.data = ByteBuffer.allocate(type.element_size * elements);
			this.data.order(Connection.BYTE_ORDER);
			this.type = type;
			this.elements = (short) elements;
			break;
		case STRUCT_STRING:
		case STRUCT:
			this.type = type;
			this.elements = (short) elements;
			break;
		default:
			throw new Exception("Type " + type + " not handled");
		}
	}

	/**
	 * Initialize
	 * 
	 * @param type Data type
	 * @param data Bytes that contain the raw CIP data
	 * @throws Exception when data is invalid
	 */
	public CIPData(final Type type, final byte[] data) throws Exception {
		this.type = type;
		this.data = ByteBuffer.allocate(data.length);
		this.data.order(Connection.BYTE_ORDER);
		this.data.put(data);
		elements = determineElementCount();
	}

	/** @return Number of elements */
	final private short determineElementCount() throws Exception {
		switch (type) {
		case BOOL:
		case SINT:
		case INT:
		case DINT:
		case LINT:
		case USINT:
		case UINT:
		case UDINT:
		case ULINT:
		case BITS:
		case REAL:
		case LREAL:
			return (short) (data.capacity() / type.element_size);
		case STRUCT: {
			final Type el_type = Type.forCode(data.getShort(0));
			if (el_type == Type.STRUCT_STRING)
				return 1;
			else
				throw new Exception("Structure elements of type " + type + " not handled");
		}
		default:
			throw new Exception("Type " + type + " not handled");
		}
	}

	/** @return CIP data type */
	final public Type getType() {
		return type;
	}

	/**
	 * @return Number of elements (numbers in array). Always 1 for String
	 */
	final public int getElementCount() {
		return elements;
	}

	/**
	 * @return <code>true</code> if data type is numeric, <code>false</code> for
	 *         string
	 */
	final public boolean isNumeric() {
		return type.ordinal() <= Type.BITS.ordinal();
	}

	/**
	 * Read CIP data as number
	 * 
	 * @param index Element index 0, 1, ...
	 * @return Numeric value of requested element
	 * @throws Exception                 on error, if data is not numeric
	 * @throws IndexOutOfBoundsException if index is invalid
	 */
	final synchronized public Number getNumber(final int index) throws Exception, IndexOutOfBoundsException {
//    Logger.getLogger(getClass().getName()).log(Level.SEVERE,
//        "Get number for elementsize {0} * index {1}, elementcount: {2}, cip postion: {3}, cip limit: {4}, Buffer: {5}",
//        new String[] { Integer.toString(type.element_size), Integer.toString(index), Integer.toString(elements),
//            Integer.toString(data.position()), Integer.toString(data.limit()), data.toString() });
		switch (type) {
		case BITS:
			int val = new Byte(data.get(index / 8));
			val = new Integer((val >> index % 8) & 1);
			return val == 1 ? -1 : 0;
		case BOOL:
		case SINT:
			return new Byte(data.get(type.element_size * index));
		case USINT: {
			int value = data.get(type.element_size * index);
			if(value < 0)
				value = 256 + value;
			return value;
		}
		case INT:
			return new Short(data.getShort(type.element_size * index));
		case DINT:
			// case BITS:
			return new Integer(data.getInt(type.element_size * index));
		case UINT: {
			// case BITS:
			int value = data.getShort(type.element_size * index);
			if(value < 0)
				value = 65536 + value;
			return value;
		}
		case LINT:
			return new Long(data.getLong(type.element_size * index));
		case UDINT: {
			long value = data.getInt(type.element_size * index);
			if(value < 0)
				value = 4294967296l + value;
			return value;
		}
		case ULINT: {
			long value =  data.getLong(type.element_size * index);
			if(value < 0)
				return UnsignedLong.MAX_VALUE.subtract((-1l)*(value+1));
			return value;
		}
		case REAL:
			return new Float(data.getFloat(type.element_size * index));
		case LREAL:
			return new Double(data.getDouble(type.element_size * index));
		default:
			throw new Exception("Cannot retrieve Number from " + type);
		}
	}

	/**
	 * Read CIP data as string
	 * 
	 * @return {@link String}
	 * @throws Exception if data does not contain a string
	 */
	final synchronized public String getString() throws Exception {
		if (type != Type.STRUCT)
			throw new Exception("Type " + type + " does not contain string");
		final short code = data.getShort(0);
		final Type el_type = Type.forCode(code);
		if (el_type != Type.STRUCT_STRING)
			throw new Exception("No string, structure element is of type " + type);
		final int len = data.getInt(2);
		final byte[] chars = new byte[len];
		for (int i = 0; i < len; ++i)
			chars[i] = data.get(6 + i);
		return new String(chars);
	}

	/**
	 * Set CIP data
	 * 
	 * @param index Element index 0, 1, ...
	 * @param value Numeric value to write to that element
	 * @throws Exception                 on invalid data type
	 * @throws IndexOutOfBoundsException if index is invalid
	 */
	final synchronized public void setString(final int index, final String value)
			throws Exception, IndexOutOfBoundsException {
		this.data = ByteBuffer.allocate(90);
		this.data.order(Connection.BYTE_ORDER);
		// first set type code
		data.putShort(Type.STRUCT_STRING.code);
		data.putInt(value.length());
		// now put all characters
		data.put(value.getBytes());
	}

	/**
	 * Set CIP data
	 * 
	 * @param index Element index 0, 1, ...
	 * @param value Numeric value to write to that element
	 * @throws Exception                 on invalid data type
	 * @throws IndexOutOfBoundsException if index is invalid
	 */
	final synchronized public void set(final int index, final Number value)
			throws Exception, IndexOutOfBoundsException {
		switch (type) {
		case BOOL:
		case SINT:
			data.put(type.element_size * index, value.byteValue());
			break;
		case INT:
			data.putShort(type.element_size * index, value.shortValue());
			break;
		case USINT:
			data.put(type.element_size * index, value.byteValue());
			break;
		case UINT:
		case DINT:
		case BITS:
			data.putInt(type.element_size * index, value.intValue());
			break;
		case UDINT:
			data.putInt(type.element_size * index, value.intValue());
			break;
		case ULINT:
		case LINT:
			data.putLong(type.element_size * index, value.longValue());
			break;
		case REAL:
			data.putFloat(type.element_size * index, value.floatValue());
			break;
		case LREAL:
			data.putDouble(type.element_size * index, value.doubleValue());
			break;
		default:
			throw new Exception("Cannot set type " + type + " to a number");
		}
	}

	/** @return size of bytes of the encoded data */
	final public int getEncodedSize() {
		// Type, Elements, raw data
		return 2 + 2 + data.capacity();
	}

	/**
	 * Encode CIP data bytes into buffer
	 * 
	 * @param buf {@link ByteBuffer} where data should be placed
	 */
	final synchronized public void encode(final ByteBuffer buf) {
		buf.putShort(type.code);
		buf.putShort(elements);
		buf.put(data.array());
	}

	public ByteBuffer getData() {
		return this.data;
	}

	/** @return String representation for debugging */
	@Override
	final synchronized public String toString() {
		final StringBuilder result = new StringBuilder();
		result.append("CIP_").append(type).append(": ");
		final ByteBuffer buf = data.asReadOnlyBuffer();
		buf.order(data.order());
		buf.clear();
		switch (type) {
		case BOOL:
		case SINT: {
			final byte[] values = new byte[elements];
			buf.get(values);
			result.append(Arrays.toString(values));
			break;
		}
		case USINT:
		case INT: {
			final short[] values = new short[elements];
			for (int i = 0; i < elements; ++i)
				values[i] = buf.getShort();
			result.append(Arrays.toString(values));
			break;
		}
		case UINT:
		case DINT:
		case BITS: {
			final int[] values = new int[elements];
			for (int i = 0; i < elements; ++i)
				values[i] = buf.getInt();
			result.append(Arrays.toString(values));
			break;
		}
		case ULINT:
		case UDINT:
		case LINT: {
			final long[] values = new long[elements];
			for (int i = 0; i < elements; ++i)
				values[i] = buf.getLong();
			result.append(Arrays.toString(values));
			break;
		}
		case REAL: {
			final float[] values = new float[elements];
			for (int i = 0; i < elements; ++i)
				values[i] = buf.getFloat();
			result.append(Arrays.toString(values));
			break;
		}
		case LREAL: {
			final double[] values = new double[elements];
			for (int i = 0; i < elements; ++i)
				values[i] = buf.getDouble();
			result.append(Arrays.toString(values));
			break;
		}
		case STRUCT: {
			final short code = buf.getShort();
			final Type el_type;
			try {
				el_type = Type.forCode(code);
			} catch (Exception ex) {
				result.append("Structure element with type code 0x" + Integer.toHexString(code));
				break;
			}
			if (el_type == Type.STRUCT_STRING) {
				final int len = buf.getInt();
				final byte[] chars = new byte[len];
				buf.get(chars);
				final String value = new String(chars);
				result.append("'").append(value).append("'");
			} else
				result.append("Structure element of type " + type);
			break;
		}
		default:
			result.append("Unknown Type " + type);
		}
		return result.toString();
	}
}
