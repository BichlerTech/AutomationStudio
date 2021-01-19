package com.bichler.astudio.picandplace;

import java.util.HashMap;
import java.util.Map;

public class PickAndPlaceElement {
	
	private double x;
	private double y;
	private double angle;
	
	private Map<String, String> attributes = null;
	
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public PickAndPlaceElement() {
		this.x = 0.0;
		this.y = 0.0;
		this.angle = 0.0;
		this.attributes = new HashMap<String, String>();
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public PickAndPlaceElement clone() {
		PickAndPlaceElement element = new PickAndPlaceElement();
		element.setX(this.x);
		element.setY(this.y);
		element.setAngle(this.angle);
		for(String attribute : attributes.keySet()) {
			element.getAttributes().put(attribute, attributes.get(attribute));
		}
		return element;
	}
}
