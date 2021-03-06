package opc.sdk.core.classes.ua;

import java.io.InputStream;

import opc.sdk.core.context.StringTable;

import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.encoding.DecodingException;
import org.opcfoundation.ua.encoding.binary.BinaryDecoder;

/**
 * Importer of a binary model file.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class BinaryImporterDecoder extends BinaryDecoder {
	public BinaryImporterDecoder(InputStream is, int limit) {
		super(is, limit);
	}

	public boolean loadNamespaceTable(NamespaceTable namespaceTable) throws DecodingException {
		int count = getInt32(null);
		if (count < 0) {
			return false;
		}
		for (int i = 0; i < count; i++) {
			namespaceTable.add(-1, getString(null));
		}
		return true;
	}

	public boolean loadStringTable(StringTable defaultTable) throws DecodingException {
		int count = getInt32(null);
		if (count < 0) {
			return false;
		}
		for (int i = 0; i < count; i++) {
			defaultTable.append(getString(null));
		}
		return true;
	}
}
