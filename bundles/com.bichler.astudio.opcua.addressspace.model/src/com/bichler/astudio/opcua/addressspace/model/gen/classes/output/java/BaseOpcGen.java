package com.bichler.astudio.opcua.addressspace.model.gen.classes.output.java;

import java.io.IOException;
import java.io.OutputStream;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.BaseGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.InfoContext;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.ConstantTemplate;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.InstanceGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.InstanceTemplate;

import opc.sdk.server.core.UAServerApplicationInstance;

public abstract class BaseOpcGen extends InstanceGen
{
  public BaseOpcGen(BaseGen model, UAServerApplicationInstance server)
  {
    super(model, server);
  }

  @Override
  public InstanceTemplate prepare(InfoContext context, UAServerApplicationInstance server, ConstantTemplate constants)
  {
    context.addClass(getInstance2Generate().getNode().getNodeId(),
        getInstance2Generate().getNode().getBrowseName().toString());
    JavaTemplate template = new JavaTemplate(server);
    template.buildStructure(context, this, constants);
    return template;
  }

  @Override
  public void output(OutputStream out, InstanceTemplate template) throws IOException
  {
    template.output(out);
  }
}
