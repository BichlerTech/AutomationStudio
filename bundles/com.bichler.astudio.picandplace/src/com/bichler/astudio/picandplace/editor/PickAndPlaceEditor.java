package com.bichler.astudio.picandplace.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.bichler.astudio.picandplace.PCBBoard;
import com.bichler.astudio.picandplace.PCBBoards;
import com.bichler.astudio.picandplace.PCBPerstist;
import com.bichler.astudio.picandplace.PickAndPlaceElement;

public class PickAndPlaceEditor extends MultiPageEditorPart {
	public static final String ID = "com.bichler.astudio.pickandplace.editor.PickAndPlaceEditor";
	private PCBBoard board;
	private PCBBoards boards;

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setInput(input);
		this.setSite(site);

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void createPageOne(Composite parent) {
		Canvas canvas = new Canvas(parent, SWT.NONE);
	    LightweightSystem lws = new LightweightSystem(canvas);
	    IFigure panel = new Figure();
	    lws.setContents(panel);
	   
	    if(board == null) {
	    	PCBPerstist pers = new PCBPerstist();
			pers.loadBoard();
			board = pers.getBoard();
			board.setWidth(110.0);
			board.setHeight(58.5);
			board.rotateBoard90();
		}
		
		PCBBoards boards = new PCBBoards();
		boards.setOriginal(board);
		
		boards.generatePanel(1, 1, 0, 0);
		int index = 0;
		for(PCBBoard b : boards.getBoards()) {
			
			RectangleFigure rect = new RectangleFigure();
			double locationX = 0.0 + ((board.getWidth() + boards.getDeltaX()) * 5.0 * (double)index);
			rect.setLocation(new PrecisionPoint((int)locationX, 0));
			rect.setSize( (int)(board.getWidth() * 5.0), (int)(board.getHeight() * 5.0));
			panel.add(rect);
			for(PickAndPlaceElement element : b.getElements()) {
				//Graphics graphics = null;
				Line line = new Line();
				line.setSize(6, 6);
				line.setLocation(new PrecisionPoint((element.getX() - 2) * 5, (element.getY() -2) * 5));
				line.setElement(element);
				panel.add(line);
			}
			
			Ellipse ellipse = new Ellipse();
			ellipse.setBounds(new Rectangle((int)(board.getWidth() + boards.getDeltaX()) * index,
					(int)(board.getHeight()+ boards.getDeltaY()) * index, 4, 4));
			panel.add(ellipse);
			index++;
		}
	}
	
	public void createPageTwo(Composite parent) {
		Canvas canvas = new Canvas(parent, SWT.NONE);
	    LightweightSystem lws = new LightweightSystem(canvas);
	    
	    ScrollPane scp = new ScrollPane();
	   // IFigure figure = new Figure();
	   // scp.setContents(figure);
	    lws.setContents(scp);
	    
		if(board == null) {
			PCBPerstist pers = new PCBPerstist();
			pers.loadBoard();
			board = pers.getBoard();
			board.setWidth(110.0);
			board.setHeight(58.5);
		}
		
//		board.mirrorVertical();
		boards = new PCBBoards();
		boards.setOriginal(board);
//		boards.getOriginal().mirrorVertical();
		
		boards.generatePanel(1, 4, 2, 0);
		int index = 0;
		for(PCBBoard b : boards.getBoards()) {
			RectangleFigure rect = new RectangleFigure();
			double locationX = 0.0 + ((board.getWidth() + boards.getDeltaX()) * 5.0 * (double)index);
			rect.setLocation(new PrecisionPoint((int)locationX, 0));
			rect.setSize( (int)(board.getWidth() * 5.0), (int)(board.getHeight() * 5.0));
			scp.add(rect);
			for(PickAndPlaceElement element : b.getElements()) {
				//Graphics graphics = null;
				Line line = new Line();
				line.setSize(6, 6);
				line.setLocation(new PrecisionPoint((element.getX() - 2.0) * 5.0, (element.getY() -2.0) * 5.0));
				line.setElement(element);
				scp.add(line);
			}
			
			Ellipse ellipse = new Ellipse();
			ellipse.setBounds(new Rectangle((int)((board.getWidth() + boards.getDeltaX()) * 5.0 * (double)index),
					0, 4, 4));
			scp.add(ellipse);
			index++;
		}
		export();
	}
	
	class Line extends Shape {

		private PickAndPlaceElement element;
		
		public Line() {
		}
		
		public Line(PickAndPlaceElement element) {
			this.element = element;
		}
		
		public void setElement(PickAndPlaceElement element) {
			this.element = element;
		}
		@Override
		protected void fillShape(Graphics graphics) {
		}

		@Override
		protected void outlineShape(Graphics graphics) {
			Rectangle r = getBounds();
			/**
			 * dw
			 */
			graphics.drawLine(r.getLeft().x, r.getLeft().y, r.getRight().x, r.getRight().y);
			graphics.drawLine(r.getTop().x, r.getTop().y, r.getBottom().x, r.getBottom().y);
		}
		
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public PickAndPlaceEditorInput getEditorInput() {
		return (PickAndPlaceEditorInput) super.getEditorInput();
	}

	@Override
	protected void createPages() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		this.createPageOne(composite);
		int index = addPage(composite);
		setPageText(index, "original");
		
		composite = new Composite(getContainer(), SWT.NONE);
		layout = new FillLayout();
		composite.setLayout(layout);
		this.createPageTwo(composite);
		index = addPage(composite);
		setPageText(index, "panel");
	}

	public void export() {
//		filed
		String line = "";
		if(this.boards != null) {
			for(PCBBoard board : this.boards.getBoards()) {
				for(PickAndPlaceElement element : board.getElements()) {
					line = element.getX() + " " + element.getY() + " " + element.getAngle();
					for(String value : element.getAttributes().values()) {
						line += " " + value;
					}
					System.out.println(line);
				}
			}
		}
	}
}
