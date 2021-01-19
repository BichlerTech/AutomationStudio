package com.bichler.astudio.utils.ui.swt;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import com.bichler.astudio.utils.ui.images.SharedImages;

public class CheckBoxButton extends CLabel {

	private Image imgChecked = null;
	private Image imgUnChecked = null;
	private Image imgDisabled = null;
	private Image imgEnabled = null;
	
	private boolean checked = false;
	
	private SelectionListener selectionListener = null;
	
	public CheckBoxButton(Composite parent, int style) {
		
		super(parent, style);
		
		this.imgUnChecked = SharedImages.getImage(SharedImages.ICON_CHECKED_0); //ResourceManager.getPluginImage("OPC_Designer_0_2", "icons/browser_icons/checked_0.png");
		this.imgChecked = SharedImages.getImage(SharedImages.ICON_CHECKED_1); //ResourceManager.getPluginImage("OPC_Designer_0_2", "icons/browser_icons/checked_1.png");
		this.imgDisabled = SharedImages.getImage(SharedImages.ICON_CHECKED_0_GRAY); //ResourceManager.getPluginImage("OPC_Designer_0_2", "icons/browser_icons/checked_0_gray.png");
		this.imgEnabled = SharedImages.getImage(SharedImages.ICON_CHECKED_1_GRAY); //ResourceManager.getPluginImage("OPC_Designer_0_2", "icons/browser_icons/checked_1_gray.png");
		
		this.setImage(imgUnChecked);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setChecked(e);
			}
		});
	}

	private void setChecked(MouseEvent e) {
		if(this.getImage() != null) {						
			if(e.x < this.getBounds().width &&
					e.y < this.getBounds().height) {
				
				if(this.checked == false) {
					this.setImage(imgChecked);
					this.checked = true;
				}
				else if(this.checked == true) {
					this.setImage(imgUnChecked);
					this.checked = false;
				}
				
				if(this.selectionListener != null) {
					
					Event ev = new Event();
					ev.x = e.x;
					ev.y = e.y;
					ev.widget = this;
					ev.button = e.button;
					
					SelectionEvent event = new SelectionEvent(ev);
					this.selectionListener.widgetSelected(event);
				}
			}
		}
	}
	
	public Image getImgUnChecked() {
		return imgUnChecked;
	}

	public void setImgUnChecked(Image imgUnChecked) {
		this.imgUnChecked = imgUnChecked;
	}

	public Image getImgChecked() {
		return imgChecked;
	}

	public void setImgChecked(Image imgChecked) {
		this.imgChecked = imgChecked;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		
		if(this.getEnabled()) {
			if(checked) {
				this.setImage(this.imgChecked);
			} else {
				this.setImage(this.imgUnChecked);
				return;
			}
		} else {
			if(checked) {
				this.setImage(this.imgEnabled);
			} else {
				this.setImage(this.imgDisabled);
				return;
			}
		}
	}

	public void addSelectionListener(SelectionListener listener) {
		this.selectionListener = listener;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		if(enabled) {
			if(this.checked) {
				this.setImage(this.imgChecked);
			} else {
				this.setImage(this.imgUnChecked);
				return;
			}
		} else {
			if(this.checked) {
				this.setImage(this.imgEnabled);
			} else {
				this.setImage(this.imgDisabled);
				return;
			}
		}
	}
}
