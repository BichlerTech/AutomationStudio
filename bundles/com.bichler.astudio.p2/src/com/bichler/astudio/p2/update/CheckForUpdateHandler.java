package com.bichler.astudio.p2.update;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.p2.util.UpdateUtil;

public class CheckForUpdateHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.update.check";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		UpdateUtil.checkForUpdate();
		return null;
	}

}
