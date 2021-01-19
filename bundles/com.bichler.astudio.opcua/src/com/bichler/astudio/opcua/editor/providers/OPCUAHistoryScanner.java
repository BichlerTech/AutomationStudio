package com.bichler.astudio.opcua.editor.providers;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class OPCUAHistoryScanner extends RuleBasedScanner {
	public OPCUAHistoryScanner(OPCUAHistoryColorManager manager) {
		IToken green_c = new Token(new TextAttribute(manager.getColor(IOPCUAHistoryColorConstants.GREEN_C)));
		IToken blue_c = new Token(new TextAttribute(manager.getColor(IOPCUAHistoryColorConstants.BLUE_C)));
		IRule[] rules = new IRule[2];
		rules[0] = new SingleLineRule("@", ":", green_c);
		rules[1] = new SingleLineRule("--", "--", blue_c);
//    WordRule keywords = new WordRule(new IWordDetector()
//    {
//      public boolean isWordStart(char c)
//      {
//        if (c == 'm')
//          return true;
//        else
//          return false;
//      }
//
//      public boolean isWordPart(char c)
//      {
//        if (c == ' ')
//          return false;
//        else
//          return true;
//      }
//    }, green_c, true);
//    keywords.addWord("main", red_c);
//    rules[2] = keywords;
		setRules(rules);
	}
}
