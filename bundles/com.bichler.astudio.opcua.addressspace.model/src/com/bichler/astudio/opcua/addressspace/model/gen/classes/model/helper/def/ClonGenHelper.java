package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.def;

import java.io.BufferedWriter;
import java.io.IOException;

public class ClonGenHelper
{
  public void streamClone(BufferedWriter fos) throws IOException
  {
    fos.write("public Object clone(){return null;}");
    fos.newLine();
  }
}
