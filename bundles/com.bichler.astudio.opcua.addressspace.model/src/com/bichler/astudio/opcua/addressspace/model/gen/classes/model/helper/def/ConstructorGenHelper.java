package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.def;

import java.io.BufferedWriter;
import java.io.IOException;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.BaseGen;

public class ConstructorGenHelper
{
  private BaseGen gen;

  public ConstructorGenHelper(BaseGen gen)
  {
    this.gen = gen;
  }

  public void streamConstructor(BufferedWriter fos) throws IOException
  {
    fos.write("public " + this.gen.getNode().getBrowseName().toString() + "() {super();}");
    fos.newLine();
  }
}
