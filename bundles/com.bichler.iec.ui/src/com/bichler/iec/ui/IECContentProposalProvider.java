package com.bichler.iec.ui;

import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.ui.editor.contentassist.AbstractContentProposalProvider;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.ui.editor.contentassist.IFollowElementAcceptor;
import org.eclipse.xtext.ui.editor.contentassist.AbstractContentProposalProvider.NullSafeCompletionProposalAcceptor;

public class IECContentProposalProvider extends AbstractContentProposalProvider {

	@Override
	public void createProposals(ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		ICompletionProposalAcceptor nullSafe = new NullSafeCompletionProposalAcceptor(acceptor);
		IFollowElementAcceptor selector = createSelector(context, nullSafe);
		for (AbstractElement element : context.getFirstSetGrammarElements()) {
			selector.accept(element);
		}
	}
	
	@Override
	public void completeKeyword(Keyword object, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void completeRuleCall(RuleCall object, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void completeAssignment(Assignment object, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		// TODO Auto-generated method stub

	}

}
