package com.bichler.astudio.opcua.editor.providers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.texteditor.AbstractDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;

public class OPCUAVersionDocumentProvider extends AbstractDocumentProvider implements IDocumentProvider {

	public static File FileName;

	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		Reader reader = null;
		if (element instanceof IEditorInput) {
			IDocument document = new Document();
			IEditorInput input = (IEditorInput) element;
			FileName = ((IPathEditorInput) input).getPath().toFile();
			try {
				reader = new FileReader(((IPathEditorInput) input).getPath().toFile());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Reader in = new BufferedReader(reader);
			try {
				StringBuffer buffer = new StringBuffer(512);
				char[] readBuffer = new char[512];
				int n = in.read(readBuffer);
				while (n > 0) {
					buffer.append(readBuffer, 0, n);
					n = in.read(readBuffer);
				}

				document.set(buffer.toString());
				in.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return document;
		}
		return null;
	}

	@Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite)
			throws CoreException {
		if (element instanceof IPathEditorInput) {
			IPathEditorInput pei = (IPathEditorInput) element;
			IPath path = pei.getPath();
			File file = path.toFile();

			try {
				file.createNewFile();

				if (file.exists()) {
					if (file.canWrite()) {
						Writer writer = new FileWriter(file);
						writeDocumentContent(document, writer, monitor);
					} else {
						throw new CoreException(
								new Status(IStatus.ERROR, "myeditor", IStatus.OK, "file is read-only", null));
					}
				} else {
					throw new CoreException(
							new Status(IStatus.ERROR, "myeditor", IStatus.OK, "error creating file", null));
				}
			} catch (IOException e) {
				throw new CoreException(new Status(IStatus.ERROR, "myeditor", IStatus.OK, "error when saving file", e));
			}
		}
	}

	private void writeDocumentContent(IDocument document, Writer writer, IProgressMonitor monitor) throws IOException {
		Writer out = new BufferedWriter(writer);
		try {
			out.write(document.get());
		} finally {
			out.close();
		}
		;
	}

	@Override
	protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
		return null;
	}

	@Override
	public boolean isModifiable(Object element) {
		File file = getFile(element);
		if (file == null)
			return super.isModifiable(element);
		return file.canWrite() || !file.exists();
	}

	@Override
	public boolean isReadOnly(Object element) {
		return !isModifiable(element);
	}

	@Override
	public boolean isStateValidated(Object element) {
		return true;
	}

	@Override
	public boolean isDeleted(Object element) {
		File file = getFile(element);
		if (file == null)
			return super.isDeleted(element);
		return !file.exists();
	}

	@Override
	public long getModificationStamp(Object element) {
		File file = getFile(element);
		if (file == null)
			return super.getModificationStamp(element);
		return file.lastModified();
	}

	protected final File getFile(Object element) {
		if (element instanceof IPathEditorInput) {
			IPathEditorInput pei = (IPathEditorInput) element;
			File file = pei.getPath().toFile();
			return file;
		}
		return null;
	}

	@Override
	protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
		return null;
	}
}
