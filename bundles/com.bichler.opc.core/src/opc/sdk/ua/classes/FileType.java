package opc.sdk.ua.classes;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.OpenFileMode;
import org.opcfoundation.ua.core.StatusCodes;

import opc.sdk.ua.FileHandle;
import opc.sdk.ua.ICallHandler;
import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.IValueHandler;

public class FileType extends BaseObjectType {
	protected PropertyVariableType<UnsignedLong> size;
	protected PropertyVariableType<Boolean> writeable;
	protected PropertyVariableType<Boolean> userWriteable;
	protected PropertyVariableType<UnsignedShort> openCount;
	protected BaseMethod open;
	protected BaseMethod close;
	protected BaseMethod read;
	protected BaseMethod write;
	protected BaseMethod getPosition;
	protected BaseMethod setPosition;
	private File file = null;
	private static final String CANNOTCFILETYPE = "Cannot create file for filetype ";
	// sessionid | unique handle for file session | file handle
	private Map<NodeId, Map<Integer, FileHandle>> filehandles = new HashMap<>();
	// sessionid | filestate
	private OpenFileMode state = null;

	public FileType(BaseNode parent) {
		super(parent);
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.size != null) {
			children.add(this.size);
		}
		if (this.writeable != null) {
			children.add(this.writeable);
		}
		if (this.userWriteable != null) {
			children.add(this.userWriteable);
		}
		if (this.openCount != null) {
			children.add(this.openCount);
		}
		if (this.open != null) {
			children.add(this.open);
		}
		if (this.close != null) {
			children.add(this.close);
		}
		if (this.read != null) {
			children.add(this.read);
		}
		if (this.write != null) {
			children.add(this.write);
		}
		if (this.getPosition != null) {
			children.add(this.getPosition);
		}
		if (this.setPosition != null) {
			children.add(this.setPosition);
		}
		children.addAll(super.getChildren());
		return children;
	}

	@Override
	protected void initialize(IOPCContext context) {
		super.initialize(context);
		File fFile = new File("file");
		if (!fFile.exists()) {
			fFile.mkdir();
		}
		/**
		 * NodeId
		 */
		int index = getNodeId().getNamespaceIndex();
		NamespaceTable nsTable = context.getNamespaceUris();
		String uri = nsTable.getUri(index);
		String value = getNodeId().getValue().toString();
		this.file = new File("file/" + uri + ";" + value);
		if (!this.file.exists()) {
			try {
				boolean created = this.file.createNewFile();
				if (created) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, "{0}{1}!",
							new String[] { CANNOTCFILETYPE, getDisplayName().toString() });
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, CANNOTCFILETYPE + getDisplayName() + "!", e);
			}
		}
	}

	@Override
	protected void initializeChildren(final IOPCContext context) {
		super.initializeChildren(context);
		this.size = new PropertyVariableType<>(this);
		this.size.setValueHandler(new IValueHandler<UnsignedLong>() {
			@Override
			public void setValue(UnsignedLong value) {
				// for further use
			}

			@Override
			public UnsignedLong getValue() {
				return new UnsignedLong(file.length());
			}
		});
		this.writeable = new PropertyVariableType<>(this);
		this.writeable.setValueHandler(new IValueHandler<Boolean>() {
			@Override
			public Boolean getValue() {
				return file.canWrite();
			}

			@Override
			public void setValue(Boolean value) {
				// for further use
			}
		});
		this.userWriteable = new PropertyVariableType<>(this);
		this.userWriteable.setValueHandler(new IValueHandler<Boolean>() {
			@Override
			public Boolean getValue() {
				return file.canWrite();
			}

			@Override
			public void setValue(Boolean value) {
				// for further use
			}
		});
		this.openCount = new PropertyVariableType<>(this);
		this.openCount.setValueHandler(new IValueHandler<UnsignedShort>() {
			@Override
			public UnsignedShort getValue() {
				int count = 0;
				for (Entry<NodeId, Map<Integer, FileHandle>> entry : filehandles.entrySet()) {
					if (entry.getValue() == null) {
						continue;
					}
					count += entry.getValue().size();
				}
				return new UnsignedShort(count);
			}

			@Override
			public void setValue(UnsignedShort value) {
				// for further use
			}
		});
		this.open = new BaseMethod(this);
		this.open.setCallHandler(new ICallHandler() {
			@Override
			public ServiceResult call(IOPCContext context, Variant[] inputArguments,
					List<StatusCode> inputArgumentResults, List<Variant> outputArguments) {
				// validate result
				try {
					UnsignedByte mode = (UnsignedByte) inputArguments[0].getValue();
					EnumSet<OpenFileMode> openMode = OpenFileMode.getSet(mode.intValue());
					if (openMode.contains(OpenFileMode.EraseExisting) && !openMode.contains(OpenFileMode.Write)) {
						return new ServiceResult(StatusCodes.Bad_InvalidArgument);
					}
					NodeId sessionId = context.getSessionId();
					if (state == OpenFileMode.Read) {
						if (openMode.contains(OpenFileMode.Write)) {
							return new ServiceResult(StatusCodes.Bad_NotWritable);
						}
					} else if (state == OpenFileMode.Write) {
						if (openMode.contains(OpenFileMode.Write)) {
							return new ServiceResult(StatusCodes.Bad_NotWritable);
						}
						if (openMode.contains(OpenFileMode.Read)) {
							return new ServiceResult(StatusCodes.Bad_NotReadable);
						}
					}
					if (openMode.contains(OpenFileMode.Read)) {
						state = OpenFileMode.Read;
					}
					if (openMode.contains(OpenFileMode.Write)) {
						state = OpenFileMode.Write;
					}
					inputArgumentResults.add(StatusCode.GOOD);
					int id = context.getSeqNrFilehandles().getAndIncrement();
					// add to filehandles
					Map<Integer, FileHandle> handles = filehandles.get(context.getSessionId());
					if (handles == null) {
						handles = new HashMap<>();
						filehandles.put(sessionId, handles);
					}
					FileHandle fa = new FileHandle(context.getSessionId(), id, filehandles, handles, getNodeId(),
							close.getNodeId());
					fa.setFile(file);
					handles.put(id, fa);
					context.getFileHandles().add(fa);
					// erase file content to make it empty
					if (openMode.contains(OpenFileMode.EraseExisting)) {
						try {
							if (!fa.getFile().delete()) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE,
										"Cannot delete file for filetype " + getDisplayName() + "!");
							}
							if (!fa.getFile().createNewFile()) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE,
										CANNOTCFILETYPE + getDisplayName() + "!");
							}
						} catch (IOException e) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
						}
					}
					// append mode
					else if (openMode.contains(OpenFileMode.Append)) {
						fa.setPosition(fa.getFile().length());
					}
					// set read/write mode
					if (openMode.contains(OpenFileMode.Read)) {
						fa.setRead(true);
					}
					if (openMode.contains(OpenFileMode.Write)) {
						fa.setWrite(true);
					}
					if (openMode.contains(OpenFileMode.EraseExisting)) {
						fa.setWrite(true);
					}
					if (openMode.contains(OpenFileMode.Append)) {
						fa.setRead(true);
						fa.setWrite(true);
					}
					// add output argument
					outputArguments.add(new Variant(id));
					// update variable value
				} catch (Exception e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					return new ServiceResult(StatusCodes.Bad_InvalidArgument);
				} finally {
					if (openCount != null) {
						openCount.update(context);
					}
				}
				return new ServiceResult(StatusCode.GOOD);
			}
		});
		this.close = new BaseMethod(this);
		this.close.setCallHandler(new ICallHandler() {
			@Override
			public ServiceResult call(IOPCContext context, Variant[] inputArguments,
					List<StatusCode> inputArgumentResults, List<Variant> outputArguments) {
				// validate result
				try {
					NodeId sessionId = context.getSessionId();
					int id = inputArguments[0].intValue();
					Map<Integer, FileHandle> handles = filehandles.get(sessionId);
					if (handles == null) {
						return new ServiceResult(StatusCodes.Bad_InvalidArgument);
					}
					FileHandle raf = handles.get(id);
					if (raf == null) {
						return new ServiceResult(StatusCodes.Bad_InvalidArgument);
					}
					// close
					raf.close();
					List<FileHandle> h = context.getFileHandles();
					// remove
					h.remove(raf);
					if (state == OpenFileMode.Read && h.isEmpty()) {
						state = null;
					}
					if (state == OpenFileMode.Write) {
						state = null;
					}
				} finally {
					if (openCount != null) {
						openCount.update(context);
					}
				}
				return new ServiceResult(StatusCode.GOOD);
			}
		});
		this.read = new BaseMethod(this);
		this.read.setCallHandler(new ICallHandler() {
			@Override
			public ServiceResult call(IOPCContext context, Variant[] inputArguments,
					List<StatusCode> inputArgumentResults, List<Variant> outputArguments) {
				Map<Integer, FileHandle> handles = filehandles.get(context.getSessionId());
				if (handles == null) {
					return new ServiceResult(StatusCodes.Bad_InvalidArgument);
				}
				// validate result
				int id = inputArguments[0].intValue();
				int length = inputArguments[1].intValue();
				RandomAccessFile raf = null;
				try {
					if (!handles.containsKey(id)) {
						return new ServiceResult(StatusCodes.Bad_InvalidArgument);
					}
					FileHandle fa = handles.get(id);
					if (!fa.isReadable()) {
						return new ServiceResult(StatusCodes.Bad_InvalidState);
					}
					int l = (int) (fa.getFile().length() - fa.getPosition());
					if (length > l) {
						length = l;
					}
					byte[] buffer = new byte[length];
					raf = fa.open();
					raf.read(buffer);
					fa.setPosition(fa.getPosition() + length);
					outputArguments.add(new Variant(buffer));
					return new ServiceResult(StatusCode.GOOD);
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				} finally {
					if (size != null) {
						size.update(context);
					}
					if (raf != null) {
						try {
							raf.close();
						} catch (IOException e) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
						}
					}
				}
				return new ServiceResult(StatusCodes.Bad_UnexpectedError);
			}
		});
		this.write = new BaseMethod(this);
		this.write.setCallHandler(new ICallHandler() {
			@Override
			public ServiceResult call(IOPCContext context, Variant[] inputArguments,
					List<StatusCode> inputArgumentResults, List<Variant> outputArguments) {
				Map<Integer, FileHandle> handles = filehandles.get(context.getSessionId());
				if (handles == null) {
					return new ServiceResult(StatusCodes.Bad_InvalidArgument);
				}
				// validate result
				int id = inputArguments[0].intValue();
				byte[] data = (byte[]) inputArguments[1].getValue();
				RandomAccessFile raf = null;
				try {
					if (!handles.containsKey(id)) {
						return new ServiceResult(StatusCodes.Bad_InvalidArgument);
					}
					FileHandle fa = handles.get(id);
					if (!fa.isWriteable()) {
						return new ServiceResult(StatusCodes.Bad_InvalidState);
					}
					raf = fa.open();
					raf.write(data);
					fa.setPosition(fa.getPosition() + data.length);
					return new ServiceResult(StatusCode.GOOD);
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				} finally {
					if (size != null) {
						size.update(context);
					}
					if (raf != null) {
						try {
							raf.close();
						} catch (IOException e) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
						}
					}
				}
				return new ServiceResult(StatusCodes.Bad_NotWritable);
			}
		});
		this.getPosition = new BaseMethod(this);
		this.getPosition.setCallHandler(new ICallHandler() {
			@Override
			public ServiceResult call(IOPCContext context, Variant[] inputArguments,
					List<StatusCode> inputArgumentResults, List<Variant> outputArguments) {
				Map<Integer, FileHandle> handles = filehandles.get(context.getSessionId());
				if (handles == null) {
					return new ServiceResult(StatusCodes.Bad_InvalidArgument);
				}
				// validate result
				int id = inputArguments[0].intValue();
				FileHandle fa = handles.get(id);
				if (fa != null) {
					outputArguments.add(new Variant(new UnsignedLong(fa.getPosition())));
					return new ServiceResult(StatusCode.GOOD);
				}
				return new ServiceResult(StatusCodes.Bad_InvalidArgument);
			}
		});
		this.setPosition = new BaseMethod(this);
		this.setPosition.setCallHandler(new ICallHandler() {
			@Override
			public ServiceResult call(IOPCContext context, Variant[] inputArguments,
					List<StatusCode> inputArgumentResults, List<Variant> outputArguments) {
				Map<Integer, FileHandle> handles = filehandles.get(context.getSessionId());
				if (handles == null) {
					return new ServiceResult(StatusCodes.Bad_InvalidArgument);
				}
				// validate result
				int id = inputArguments[0].intValue();
				long position = inputArguments[1].longValue();
				FileHandle fa = handles.get(id);
				if (fa != null) {
					fa.setPosition(position);
					return new ServiceResult(StatusCode.GOOD);
				}
				return new ServiceResult(StatusCodes.Bad_InvalidArgument);
			}
		});
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.FileType;
	}
}
