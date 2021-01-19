package com.bichler.astudio.editor.siemens.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;

import com.bichler.astudio.editor.siemens.SiemensActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.events.CreateOPCUADriverParameter;
import com.bichler.astudio.opcua.handlers.AbstractOPCCreateDriverModel;

public class CreateOPCUADriverHandler extends AbstractOPCCreateDriverModel {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		CreateOPCUADriverParameter trigger = getCommandParameter(event);
		IFileSystem filesystem = trigger.getFilesystem();
		String driverPath = trigger.getDriverpath();
		File defaultDrvStore = SiemensActivator.getDefault().getFile(SiemensActivator.getDefault().getBundle(),
				Path.ROOT.append("driver").append("config").append("studio.temp"));
		if (!filesystem.isDir(driverPath)) {
			// first create driver folder
			try {
				filesystem.addDir(driverPath);
			} catch (IOException e1) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage());
				return null;
			}
			try (InputStream is = filesystem.readFile(defaultDrvStore.getAbsolutePath());
					OutputStream output = filesystem
							.writeFile(driverPath + filesystem.getTargetFileSeparator() + "driver.com");
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));) {
				String line = "";
				while ((line = reader.readLine()) != null) {
					output.write((line + "\n").getBytes());
				}
			} catch (IOException | IllegalStateException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		return null;
	}
}
