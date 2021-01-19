package com.bichler.astudio.opcua.opcmodeler.commands.handler.server;

import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.dialogs.CometProgressStartupMonitorDialog;

public class ActivateServerHandler extends AbstractHandler {
	/** timer for demo version */
	private Timer timer = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// false = start
		// true = end
		Command command = event.getCommand();
		boolean oldValue = HandlerUtil.toggleCommandState(command);
		CometProgressStartupMonitorDialog dialog = new CometProgressStartupMonitorDialog(
				HandlerUtil.getActiveShell(event));
		if (!oldValue) {
			try {
				dialog.run(false, false, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						ServerInstance.getInstance().start();
						Thread.sleep(5000);
					}
				});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/* start demo timeout counter */
			if (timer != null) {
				timer.cancel();
			}
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					timer.cancel();
					ServerInstance.getInstance().stop();
				}
			}, 1000 * 5 * 60 * 60);
		} else {
			try {
				dialog.run(false, false, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						ServerInstance.getInstance().stop();
						timer.cancel();
						timer = null;
					}
				});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
