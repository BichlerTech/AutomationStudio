package com.bichler.astudio.utils.ui.swt;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class PaletteButton extends CLabel {

	private boolean flyoutenabled = false;
	public boolean isFlyoutenabled() {
		return flyoutenabled;
	}

	public void setFlyoutenabled(boolean flyoutenabled) {
		this.flyoutenabled = flyoutenabled;
	}

	private boolean selected = false;
	private boolean mousein = false;
	private Image enterImage = null;
	private Image standardImage = null;
	private Image selectedImage = null;
	private Composite parent = null;
	
	//private Color selectedColor = null;
	private int offsetX = 0;
	private int offsetY = 0;
	private int imageOffsetX = 0;
	private int imageOffsetY = 0;
	
	private String svgEditorMode = "select";
	
	public PaletteButton(final Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
		
		this.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				mousein = true;
				
				redraw();
			}
			@Override
			public void mouseExit(MouseEvent e) {
				mousein = false;
				redraw();
			}
		});
		
		this.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				if(flyoutenabled) {
					Color fg = e.gc.getForeground();
					
					e.gc.setForeground(new Color(e.display, 100, 100, 100));
					e.gc.drawLine(e.x + e.width - 14, e.y + e.height - 10, e.x + e.width - 10, e.y + e.height - 8);
					e.gc.drawLine(e.x + e.width - 14, e.y + e.height - 6, e.x + e.width - 10, e.y + e.height - 8);
					
					e.gc.drawLine(e.x + e.width - 10, e.y + e.height - 10, e.x + e.width - 6, e.y + e.height - 8);
					e.gc.drawLine(e.x + e.width - 10, e.y + e.height - 6, e.x + e.width - 6, e.y + e.height - 8);
					
					e.gc.setForeground(fg);
				}
				Rectangle rectangle = getClientArea();
				
				if(selected) {
					e.gc.drawRectangle(rectangle.x, rectangle.y, rectangle.width - 1, rectangle.height - 1);
				}
				else {
					if(mousein) {
						e.gc.drawRectangle(rectangle.x, rectangle.y, rectangle.width - 1, rectangle.height - 1);
					}
				}
			}
		});
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				handleMouseUp();
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public void handleMouseUp() {
		selected = true;
		toggleButtons(parent);
		redraw();
	}

	public Image getEnterImage() {
		return enterImage;
	}

	public void setEnterImage(Image enterImage) {
		this.enterImage = enterImage;
	}

	public Image getStandardImage() {
		return standardImage;
	}

	public void setStandardImage(Image standardImage) {
		super.setImage(standardImage);
		this.standardImage = standardImage;
	}

	public Image getSelectedImage() {
		return selectedImage;
	}

	public void setSelectedImage(Image selectedImage) {
		this.selectedImage = selectedImage;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	private void toggleButtons(Composite container) {
		for (Control child : container.getChildren()) {
			if (child instanceof PaletteButton
					&& !child.equals(this)) {
				((PaletteButton) child).setSelected(false);
				((PaletteButton) child).redraw();
			}
		}
	}

	public String getSvgEditorMode() {
		return svgEditorMode;
	}

	public void setSvgEditorMode(String svgEditorMode) {
		this.svgEditorMode = svgEditorMode;
	}

	public int getImageOffsetX() {
		return imageOffsetX;
	}

	public void setImageOffsetX(int imageOffsetX) {
		this.imageOffsetX = imageOffsetX;
	}

	public int getImageOffsetY() {
		return imageOffsetY;
	}

	public void setImageOffsetY(int imageOffsetY) {
		this.imageOffsetY = imageOffsetY;
	}
}
