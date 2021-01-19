package opc.sdk.ua;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.opcfoundation.ua.builtintypes.NodeId;

public interface IOPCSession {
	NodeId getSessionId();

	AtomicInteger getSeqNrFilehandles();

	List<FileHandle> getFileHandles();
}
