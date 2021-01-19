package opc.sdk.ua;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.NodeId;

public class FileHandle {
	private boolean read = false;
	private boolean write = false;
	private int id = 0;
	private File file = null;
	private long position = 0;
	private Map<Integer, FileHandle> serverMap;
	private Map<NodeId, Map<Integer, FileHandle>> fileHandles;
	private NodeId sessionId;
	private NodeId objectId = NodeId.NULL;
	private NodeId methodId = NodeId.NULL;
	private Logger logger = Logger.getLogger(getClass().getName());

	public FileHandle(NodeId nodeId, int id, Map<NodeId, Map<Integer, FileHandle>> filehandles,
			Map<Integer, FileHandle> handles, NodeId objectId, NodeId methodId) {
		this.sessionId = nodeId;
		this.id = id;
		this.serverMap = handles;
		this.fileHandles = filehandles;
		this.objectId = objectId;
		this.methodId = methodId;
	}

	public void close() {
		if (this.fileHandles == null) {
			return;
		}
		if (this.serverMap == null) {
			return;
		}
		this.serverMap.remove(this.id);
		if (this.serverMap.isEmpty()) {
			this.fileHandles.remove(this.sessionId);
		}
	}

	public RandomAccessFile open() throws FileNotFoundException {
		String mode = getMode();
		RandomAccessFile raf = new RandomAccessFile(file, mode);
		try {
			raf.seek(position);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return raf;
	}

	private String getMode() {
		String mode = "";
		if (this.read) {
			mode += "r";
		}
		if (this.write) {
			mode += "w";
		}
		return mode;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return this.file;
	}

	public void setPosition(long length) {
		this.position = length;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public boolean isReadable() {
		return this.read;
	}

	public long getPosition() {
		return this.position;
	}

	public boolean isWriteable() {
		return this.write;
	}

	public int getId() {
		return this.id;
	}

	public NodeId getMethodId() {
		return this.methodId;
	}

	public NodeId getObjectId() {
		return this.objectId;
	}
}
