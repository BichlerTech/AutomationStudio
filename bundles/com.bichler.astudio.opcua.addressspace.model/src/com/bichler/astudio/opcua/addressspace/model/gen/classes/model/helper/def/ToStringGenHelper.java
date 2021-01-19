package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.def;

import java.io.BufferedWriter;
import java.io.IOException;

public class ToStringGenHelper
{
  public void streamToString(BufferedWriter fos) throws IOException
  {
    fos.write("public String toString(){return \"\";}");
    fos.newLine();
  }
}
