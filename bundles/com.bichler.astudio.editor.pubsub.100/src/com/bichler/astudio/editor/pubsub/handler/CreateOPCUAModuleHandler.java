package com.bichler.astudio.editor.pubsub.handler;

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

import com.bichler.astudio.editor.pubsub.PubSubActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.events.CreateOPCUAModuleParameter;
import com.bichler.astudio.opcua.handlers.AbstractOPCCreateModuleModel;

public class CreateOPCUAModuleHandler extends AbstractOPCCreateModuleModel
{
  public static final String ID = "com.bichler.astudio.editor.pubsub.1.0.0.createmodulecommand";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    CreateOPCUAModuleParameter trigger = getCommandParameter(event);
    IFileSystem filesystem = trigger.getFilesystem();
    String modulePath = trigger.getModulePath();
    File defaultDrvStore = PubSubActivator.getDefault().getFile(PubSubActivator.getDefault().getBundle(),
        Path.ROOT.append("module").append("config").append("studio.temp"));
    if (!filesystem.isDir(modulePath))
    {
      // first create driver folder
      try
      {
        filesystem.addDir(modulePath);
      }
      catch (IOException e1)
      {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage());
        return null;
      }
      try (InputStream is = filesystem.readFile(defaultDrvStore.getAbsolutePath());
          OutputStream output = filesystem.writeFile(modulePath + filesystem.getTargetFileSeparator() + "module.com");
          BufferedReader reader = new BufferedReader(new InputStreamReader(is));)
      {
        String line = "";
        while ((line = reader.readLine()) != null)
        {
          output.write((line + "\n").getBytes());
        }
        output.flush();
      }
      catch (IOException | IllegalStateException e)
      {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
      }
    }
    return null;
  }
}
