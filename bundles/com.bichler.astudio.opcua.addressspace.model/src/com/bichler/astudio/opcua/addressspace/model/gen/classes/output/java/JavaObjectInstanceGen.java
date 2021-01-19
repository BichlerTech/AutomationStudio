package com.bichler.astudio.opcua.addressspace.model.gen.classes.output.java;

import java.util.List;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.BaseGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.InfoContext;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.MethodGenHelper;

import opc.sdk.server.core.UAServerApplicationInstance;

public class JavaObjectInstanceGen extends BaseOpcGen
{
  public JavaObjectInstanceGen(BaseGen gen, UAServerApplicationInstance server)
  {
    super(gen, server);
  }

  @Override
  public void additionalMethods(InfoContext context, List<MethodGenHelper> methods)
  {
  }
}
