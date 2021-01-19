package com.bichler.astudio.editor.pubsub.wizard.core;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.KeyValuePair;

public class WrapperKeyValuePair {

	protected QualifiedName Key;
	protected Variant Value;

	public WrapperKeyValuePair() {

	}

	public QualifiedName getKey() {
		return this.Key;
	}

	public Variant getValue() {
		return this.Value;
	}

	public void setKey(QualifiedName key) {
		this.Key = key;
	}

	public void setValue(Variant value) {
		this.Value = value;
	}

	public static KeyValuePair[] unwrap(WrapperKeyValuePair[] value) {
		List<KeyValuePair> values = new ArrayList<KeyValuePair>();
		if (value != null) {
			for (WrapperKeyValuePair kv : value) {
				KeyValuePair kvp = new KeyValuePair();
				kvp.setKey(kv.Key);
				kvp.setValue(kv.Value);
				values.add(kvp);
			}
		}

		return values.toArray(new KeyValuePair[0]);
	}

	public static WrapperKeyValuePair[] wrap(KeyValuePair[] value) {
		if (value == null) {
			return new WrapperKeyValuePair[0];
		}

		WrapperKeyValuePair[] kvp = new WrapperKeyValuePair[value.length];
		return kvp;
	}

	public static List<WrapperKeyValuePair> clone(WrapperKeyValuePair[] properties) {
		List<WrapperKeyValuePair> kv = new ArrayList<WrapperKeyValuePair>();
		if (properties == null) {
			return kv;
		}

		for (int i = 0; i < properties.length; i++) {
			WrapperKeyValuePair cloned = new WrapperKeyValuePair();
			cloned.Key = properties[i].Key;
			cloned.Value = properties[i].Value;
			
			kv.add(cloned);
		}

		return kv;
	}
}
