package com.bichler.astudio.opcua.modeller.controls;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class CometCombo extends Combo {
	/* Package Name */
	static final String PACKAGE_PREFIX = "com.hbsoft.designer.utils.";
	private String oldText = "";
	private String tmpText = "";

	public CometCombo(Composite parent, int style) {
		super(parent, style);
		this.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				oldText = tmpText;
				tmpText = getText();
			}
		});
	}

	public void setOldText(String text) {
		this.oldText = text;
	}

	public String getOldText() {
		return this.oldText;
	}

	protected void checkSubclass() {
		String name = getClass().getName();
		int index = name.lastIndexOf('.');
		if (!name.substring(0, index + 1).equals(PACKAGE_PREFIX)) {
			// SWT.error (SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	@Override
	public void setText(String string) {
		this.tmpText = getText();
		super.setText(string);
	}
}
