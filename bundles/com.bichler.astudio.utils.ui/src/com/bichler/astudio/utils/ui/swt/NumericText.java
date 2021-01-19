package com.bichler.astudio.utils.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class NumericText extends Text {

	private double min = Double.MIN_VALUE;
	
	private double max = Double.MAX_VALUE;
	
	@Override
	protected void checkSubclass() {
		// TODO Auto-generated method stub
		//super.checkSubclass();
	}

	public NumericText(Composite parent, int style) {
		super(parent, style);
		
		this.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				 switch (e.keyCode) {  
		           	case SWT.BS:           // Backspace  
		           	case SWT.DEL:          // Delete  
		            case SWT.HOME:         // Home  
		            case SWT.END:          // End  
		            case SWT.ARROW_LEFT:   // Left arrow  
		            case SWT.ARROW_RIGHT:  // Right arrow  
		                return;  
		        }  
		  
				for (char c : e.text.toCharArray()) {
					 if (!Character.isDigit(c) && c != '.' && c != '-')
					 { 
						 e.doit = false;  // disallow the action
						 return;
					 }
				 }
			}
		});
		
		this.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(getText().isEmpty()) {
					setText("0");
			
				}
				
				// check min value
				double value = 0;
				try {
					
					value = Double.parseDouble(getText());
					if(value < min) 
						setText("" + min);
					else if(value > max)
						setText("" + max);
				} catch(NumberFormatException ex) {
					setText("" + min);
				}
			}
		});
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

}
