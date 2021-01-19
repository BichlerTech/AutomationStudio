package com.bichler.astudio.opcua.addressspace.model;

import org.eclipse.jdt.core.compiler.CompilationProgress;

import opc.sdk.core.application.operation.ICancleOperation;

public class InformationModelCompilationProgress extends CompilationProgress
{
  // private IProgressMonitor monitor;
  public InformationModelCompilationProgress(ICancleOperation monitor)
  {
    super();
    // this.monitor = monitor;
  }

  @Override
  public void begin(int start)
  {
  }

  @Override
  public void done()
  {
  }

  @Override
  public boolean isCanceled()
  {
    return false;
  }

  @Override
  public void setTaskName(String name)
  {
  }

  @Override
  public void worked(int arg0, int arg1)
  {
  }
}
