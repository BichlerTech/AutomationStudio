package com.bichler.astudio.opcua.editor.completion;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

import com.bichler.astudio.opcua.editor.providers.IOPCUAHistoryColorConstants;
import com.bichler.astudio.opcua.editor.providers.OPCUAHistoryColorManager;
import com.bichler.astudio.opcua.editor.providers.OPCUAHistoryScanner;

public class OPCUAHistorySourceViewerConfiguration extends TextSourceViewerConfiguration {
	private ContentAssistant contentAssistant;
	private OPCUAHistoryColorManager colorManager;
	private OPCUAHistoryScanner scanner;

	public OPCUAHistorySourceViewerConfiguration(OPCUAHistoryColorManager colorManager) {
		this(null, colorManager);
	}

	public OPCUAHistorySourceViewerConfiguration(IPreferenceStore preferenceStore,
			OPCUAHistoryColorManager colorManager) {
		super(preferenceStore);
		this.colorManager = colorManager;
		// Initialize ContentAssistant
		contentAssistant = new ContentAssistant();
		// define a default ContentAssistProcessor
		contentAssistant.setContentAssistProcessor(new OPCUAHistoryCompletionProcessor(),
				IDocument.DEFAULT_CONTENT_TYPE);
		// enable auto activation
		contentAssistant.enableAutoActivation(true);
		// set a proper orientation for the content assist proposal
		contentAssistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		contentAssistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		return contentAssistant;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE };
	}

	protected OPCUAHistoryScanner getCScanner() {
		if (scanner == null) {
			scanner = new OPCUAHistoryScanner(colorManager);
			scanner.setDefaultReturnToken(
					new Token(new TextAttribute(colorManager.getColor(IOPCUAHistoryColorConstants.DEFAULT))));
		}
		return scanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getCScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		return reconciler;
	}
}