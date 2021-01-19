package com.bichler.astudio.picandplace;

import java.util.ArrayList;
import java.util.List;

public class PCBBoard {
	
	private List<PickAndPlaceElement> elements;
	private double origRotation = 0.0;
	private double originX = 0.0;
	private double originY = 0.0;
	
	private double height = 0.0;

	private double width = 0.0;
	
	public PCBBoard() {
		this.elements = new ArrayList<PickAndPlaceElement>();
	}
	
	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}
	
	public List<PickAndPlaceElement> getElements() {
		return elements;
	}

	public void setElements(List<PickAndPlaceElement> elements) {
		this.elements = elements;
	}
	
	public void setXYOrigin() {
		
	}
	
	/**
	 * rotates all elements in list 90.0 degree clockwise 
	 */
	public void rotateElements90() {
		//origRotation += 90.0;
		for(PickAndPlaceElement element : elements) {
			element.setAngle(element.getAngle() + 90.0);
			if(element.getAngle() > 360.0)
				element.setAngle(element.getAngle() - 360.0);
		}
	}
	
	public void rotateBoard90() {
		// origRotation += 90.0;
		double tmpX;
		double tmpY;
		for(PickAndPlaceElement element : elements) {
			tmpX = element.getX();
			tmpY = element.getY();
			element.setX(tmpY);
			element.setY(width - tmpX);
		}
		this.rotateElements90();
		
		double width = this.width;
		this.width = this.height;
		this.height = width;
	}
	
	/**
	 * resets rotation of each element to 0 degree
	 */
	public void resetElements() {
		for(PickAndPlaceElement element : elements) {
			element.setAngle(element.getAngle() - origRotation);
		}
		origRotation = 0;
	}
	
	/**
	 * 
	 * @param xToMirror
	 */
	public void mirrorHorizontal() {
		for(PickAndPlaceElement element : elements) {
			element.setX(width - element.getX());
		}
	}
	
	public void mirrorVertical() {
		for(PickAndPlaceElement element : elements) {
			element.setY(height - element.getY());
			System.out.println("Y: " + element.getY() + " attribute " + element.getAttributes() + "\n");
		}
	}
	
	public void shiftVertical(double yToshift) {
		for(PickAndPlaceElement element : elements) {
			element.setY(element.getY() + yToshift);
		}
	}
	
	public void shiftHorizontal(double xToshift) {
		for(PickAndPlaceElement element : elements) {
			element.setY(element.getY() + xToshift);
		}
	}
	
	public PCBBoard clone() {
		PCBBoard board = new PCBBoard();
		board.setWidth(this.width);
		board.setHeight(this.height);
		for(PickAndPlaceElement element : this.elements)
			board.getElements().add(element.clone());
		return board;
	}
	
}
