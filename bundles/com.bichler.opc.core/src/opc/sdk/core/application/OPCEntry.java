package opc.sdk.core.application;

import java.util.Map.Entry;

/**
 * Entry with a K key and a V value.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 * @param <K> KeyType
 * @param <V> ValueType
 */
public class OPCEntry<K, V> implements Entry<K, V> {
	private K key = null;
	private V value = null;
	private OPCEntry<K, V> parent = null;

	public OPCEntry() {
		// default constructor to create an empty entry
	}

	public OPCEntry(K keyVal, V value) {
		this.key = keyVal;
		setValue(value);
	}

	public OPCEntry(K keyVal, V value, OPCEntry<K, V> parent) {
		this(keyVal, value);
		this.parent = parent;
	}

	public void setKey(K key) {
		this.key = key;
	}

	@Override
	public K getKey() {
		return this.key;
	}

	@Override
	public V getValue() {
		return this.value;
	}

	@Override
	public V setValue(V value) {
		this.value = value;
		return value;
	}

	public OPCEntry<K, V> getParent() {
		return this.parent;
	}
}
