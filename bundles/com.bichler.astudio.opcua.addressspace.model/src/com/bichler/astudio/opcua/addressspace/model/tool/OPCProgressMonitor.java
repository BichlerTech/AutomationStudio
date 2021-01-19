package com.bichler.astudio.opcua.addressspace.model.tool;

import org.eclipse.core.runtime.IProgressMonitor;

import opc.sdk.core.application.operation.ICancleOperation;

public class OPCProgressMonitor implements ICancleOperation
{
  private IProgressMonitor monitor;

  public OPCProgressMonitor(IProgressMonitor monitor)
  {
    this.monitor = monitor;
  }

  @Override
  public boolean isCanceled()
  {
    return monitor != null ? monitor.isCanceled() : false;
  }

  @Override
  public void subTask(String name)
  {
    if (monitor != null)
    {
      monitor.subTask(name);
    }
  }

  @Override
  public void worked(int count)
  {
    if (monitor != null)
    {
      monitor.worked(count);
    }
  }

	@Override
	public void done() {
		monitor.done();
	}
}
