package com.bichler.astudio.utils.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class HideableCoolItem extends CoolItem {

	@Override
	public Point getMinimumSize() {
		// TODO Auto-generated method stub
		return new Point(0,0);
	}

	@Override
	public Point getPreferredSize() {
		// TODO Auto-generated method stub
		return new Point(0,0);
	}

	@Override
	public Point getSize() {
		// TODO Auto-generated method stub
		return new Point(0,0);
	}

	private boolean visible = true;
	
	@Override
	public Rectangle getBounds() {
		if(this.visible) 
			return super.getBounds();
		
		return new Rectangle(0, 0, 0, 0);
	}

	@Override
	protected void checkSubclass() {
		// TODO Auto-generated method stub
		//super.checkSubclass();
	}

	public HideableCoolItem(CoolBar parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	
		this.addListener(SWT.PaintItem, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				System.out.println("HideableCoolItem painted");
			}
		});
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	
}
