package com.bichler.astudio.picandplace;

import java.util.ArrayList;
import java.util.List;

public class PCBBoards {

	private PCBBoard original = null;
	private List<PCBBoard> boards = null;
	private double deltaX;
	private double deltaY;

	public double getDeltaX() {
		return deltaX;
	}

	public void setDeltaX(double deltaX) {
		this.deltaX = deltaX;
	}

	public double getDeltaY() {
		return deltaY;
	}

	public void setDeltaY(double deltaY) {
		this.deltaY = deltaY;
	}

	public List<PCBBoard> getBoards() {
		return boards;
	}

	public void setBoards(List<PCBBoard> boards) {
		this.boards = boards;
	}

	public PCBBoard getOriginal() {
		return original;
	}

	public void setOriginal(PCBBoard original) {
		this.original = original;
	}

	public PCBBoards() {
		original = new PCBBoard();
		boards = new ArrayList<PCBBoard>();
	}

	public void addPCBBoard(PCBBoard board) {
		boards.add(board);
	}

	/**
	 * 
	 */
	public void rotate90() {
		for (PCBBoard board : boards) {
			board.rotateElements90();
		}
	}

	public void mirrorHorizontal() {
		for (PCBBoard board : boards) {
			board.mirrorHorizontal();
		}
	}

	public void mirrorVertical() {
		for (PCBBoard board : boards) {
			board.mirrorVertical();
		}
	}

	/**
	 * generate a complete pcb panel with n-row and n-column
	 * 
	 * 
	 * --------- --------- | | | | | | | | --------- --------- --------- ---------
	 * deltaY | | | | | | | | --------- --------- 0/0 deltaX
	 * 
	 * @param row
	 * @param column
	 * @param deltaX
	 * @param deltaY
	 */
	public void generatePanel(int row, int column, double deltaX, double deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		/**
		 * first generate rows
		 */
		this.boards.clear();
		/**
		 * add original as first board
		 */
		// this.boards.add(original);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				PCBBoard newPCB = original.clone();

				// now recalc coordinates
				for (PickAndPlaceElement element : newPCB.getElements()) {
					element.setX(element.getX() + ((newPCB.getWidth() + deltaX) * j));
					element.setY(element.getY() + ((newPCB.getHeight() + deltaY) * i));
				}
				this.boards.add(newPCB);
			}
		}
	}
}
