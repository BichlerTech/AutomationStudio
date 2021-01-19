package com.bichler.astudio.perspective.studio;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class ImageAndTextContributionItem extends ControlContribution {

    private Combo combo;

    public ImageAndTextContributionItem(String id) {
        super(id);
    }

    @Override
    protected Control createControl(Composite parent) {
    	Composite comp = new Composite(parent, SWT.READ_ONLY);
    	comp.setSize(178, 178);
        combo = new Combo(comp, SWT.READ_ONLY);
        Label l =new Label(comp, SWT.READ_ONLY);
        l.setText("irgendwas");
        return comp;
    }

    @Override
    public int computeWidth(Control control) {
        // The widget is now 100 pixels. You can new GC gc = new GC(control) and
        // use the gc.stringExtent(String) method to help compute a more dynamic
        // width.
        return 78;
    }
    
    

    public Combo getComboControl() {
        return combo;
    }
}