package com.bichler.astudio.perspective.studio;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class TextContributionItem extends ControlContribution {

    private final int style;
    private Text text;

    public TextContributionItem(String id) {
        this(id, SWT.BORDER | SWT.SINGLE);
    }

    public TextContributionItem(String id, int style) {
        super(id);
        this.style = style;
    }

    @Override
    protected Control createControl(Composite parent) {
        text = new Text(parent, style);
        return text;
    }

    @Override
    public int computeWidth(Control control) {
        return 100;
    }

    public Text getTextControl() {
        return text;
    }
}