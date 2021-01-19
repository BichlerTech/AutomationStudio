package com.bichler.astudio.editor.siemens.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.events.CreateOPCUADriverParameter;

public class UpgradeOPCUADriverHandler extends CreateOPCUADriverHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<String> buf = new ArrayList<>();
		// now we upgrade driver.com
		CreateOPCUADriverParameter trigger = getCommandParameter(event);
		IFileSystem filesystem = trigger.getFilesystem();
		String driverPath = trigger.getDriverpath();
		if (!filesystem.isFile(driverPath)) {
			try (InputStream is = filesystem.readFile(driverPath + filesystem.getTargetFileSeparator() + "driver.com");
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));) {
				String line = "";
				boolean versFound = false;
				while ((line = reader.readLine()) != null) {
					// now replace version number
					if (line.compareTo("driverversion") == 0) {
						buf.add(line + "\n");
						reader.readLine();
						line = "1.0.2";
						versFound = true;
					}
					buf.add(line + "\n");
				}
				if (!versFound) {

					int index = buf.indexOf("drivertype\n");
					if (index >= 0) {
						buf.add(index + 3, "driverversion\n");
						buf.add(index + 4, "1.0.2\n\n");
					}
//          for (int i = 0 ; i < buf.size(); )
//          {
//            // now replace version number
//            if (line.compareTo("drivertype") == 0)
//            {
//              buffer.append(line + "\n");
//              buffer.append(reader.readLine() + "\n\n");
//              buffer.append("driverversion\n");
//              line = "1.0.2\n";
//            }
//            buffer.append(line + "\n");
//          }
				}
			} catch (IOException | IllegalStateException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
			try (OutputStream output = filesystem
					.writeFile(driverPath + filesystem.getTargetFileSeparator() + "driver.com");) {
				for (String val : buf) {
					output.write(val.getBytes());
				}
				output.flush();
			} catch (IOException | IllegalStateException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		return null;
	}
}
