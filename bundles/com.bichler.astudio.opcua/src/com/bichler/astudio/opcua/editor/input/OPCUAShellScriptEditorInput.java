package com.bichler.astudio.opcua.editor.input;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;

import com.bichler.astudio.components.file.CometLocaleFile;

public class OPCUAShellScriptEditorInput extends FileStoreEditorInput implements IStorageEditorInput {

	private CometLocaleFile fileStore = null;

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		// TODO Auto-generated method stub
		return super.getAdapter(adapter);
	}

	private String event;

	public OPCUAShellScriptEditorInput(CometLocaleFile fileStore) {
		super(fileStore);
		this.fileStore = fileStore;
		this.setEvent(event);
	}

	public void setEvent(String event) {
		// TODO Auto-generated method stub
		this.event = event;
	}

	public String getEvent() {
		// TODO Auto-generated method stub
		return this.event;
	}

	@Override
	public IStorage getStorage() throws CoreException {

		return new IStorage() {

			@Override
			public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isReadOnly() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public String getName() {
				return fileStore.getName();
			}

			@Override
			public IPath getFullPath() {
				Path path = new Path(fileStore.getFile().getPath());
				return path;
			}

			@Override
			public InputStream getContents() throws CoreException {
				InputStream stream = null;
				try {
					stream = new FileInputStream(fileStore.getFile());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return stream;
			}
		};
	}
}
