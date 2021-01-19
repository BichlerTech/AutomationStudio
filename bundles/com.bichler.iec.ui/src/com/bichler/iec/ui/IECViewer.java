package com.bichler.iec.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.xtext.Constants;
import org.eclipse.xtext.ui.editor.XtextSourceViewer;
import org.eclipse.xtext.ui.editor.XtextSourceViewerConfiguration;
import org.eclipse.xtext.ui.editor.model.XtextDocumentProvider;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class IECViewer extends  ViewPart {

	@Override
	protected void firePropertyChange(int propertyId) {
		// TODO Auto-generated method stub
		super.firePropertyChange(propertyId);
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site);
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site, memento);
	}
	public static final String ID = "com.bichler.iec.ui.IECViewer";
	
	@Inject
	private XtextSourceViewer.Factory sourceViewerFactory;
	
//	private IDocumentProvider fExplicitDocumentProvider;
	
	@Inject
	private Provider<XtextDocumentProvider> documentProvider;
	
	/** The view's explicit document provider. */
	private IDocumentProvider fExplicitDocumentProvider;
	
	@Inject
	private XtextSourceViewerConfiguration sourceViewerConfiguration;
	
	@Inject
	private IPreferenceStoreAccess preferenceStoreAccess;
	
	/*
	* The editor's foreground color.
	 * @since 2.0
	 */
	private Color fForegroundColor;
	/**
	 * The editor's background color.
	 * @since 2.0
	 */
	private Color fBackgroundColor;
	
	/**
	 * Key used to look up foreground color preference.
	 * Value: <code>AbstractTextEditor.Color.Foreground</code>
	 * @since 2.0
	 */
	public static final String PREFERENCE_COLOR_FOREGROUND= "AbstractTextEditor.Color.Foreground"; //$NON-NLS-1$
	/**
	 * Key used to look up background color preference.
	 * Value: <code>AbstractTextEditor.Color.Background</code>
	 * @since 2.0
	 */
	public static final String PREFERENCE_COLOR_BACKGROUND= "AbstractTextEditor.Color.Background"; //$NON-NLS-1$
	/**
	 * Key used to look up foreground color system default preference.
	 * Value: <code>AbstractTextEditor.Color.Foreground.SystemDefault</code>
	 * @since 2.0
	 */
	public static final String PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT= "AbstractTextEditor.Color.Foreground.SystemDefault"; //$NON-NLS-1$
	/**
	 * Key used to look up background color system default preference.
	 * Value: <code>AbstractTextEditor.Color.Background.SystemDefault</code>
	 * @since 2.0
	 */
	public static final String PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT= "AbstractTextEditor.Color.Background.SystemDefault"; //$NON-NLS-1$
	/**
	 * Key used to look up selection foreground color preference.
	 * Value: <code>AbstractTextEditor.Color.SelectionForeground</code>
	 * @since 3.0
	 */
	public static final String PREFERENCE_COLOR_SELECTION_FOREGROUND= "AbstractTextEditor.Color.SelectionForeground"; //$NON-NLS-1$
	/**
	 * Key used to look up selection background color preference.
	 * Value: <code>AbstractTextEditor.Color.SelectionBackground</code>
	 * @since 3.0
	 */
	public static final String PREFERENCE_COLOR_SELECTION_BACKGROUND= "AbstractTextEditor.Color.SelectionBackground"; //$NON-NLS-1$
	
	/**
	 * Key used to look up selection foreground color system default preference.
	 * Value: <code>AbstractTextEditor.Color.SelectionForeground.SystemDefault</code>
	 * @since 3.0
	 */
	public static final String PREFERENCE_COLOR_SELECTION_FOREGROUND_SYSTEM_DEFAULT= "AbstractTextEditor.Color.SelectionForeground.SystemDefault"; //$NON-NLS-1$
	/**
	 * Key used to look up selection background color system default preference.
	 * Value: <code>AbstractTextEditor.Color.SelectionBackground.SystemDefault</code>
	 * @since 3.0
	 */
	public static final String PREFERENCE_COLOR_SELECTION_BACKGROUND_SYSTEM_DEFAULT= "AbstractTextEditor.Color.SelectionBackground.SystemDefault"; //$NON-NLS-1$
	
	/**
	 * The editor's selection foreground color.
	 * @since 3.0
	 */
	private Color fSelectionForegroundColor;
	/**
	 * The editor's selection background color.
	 * @since 3.0
	 */
	private Color fSelectionBackgroundColor;

	private String languageName;

	private XtextSourceViewerConfiguration fConfiguration ;
	
	/** The editor's preference store. */
//	private IPreferenceStore fPreferenceStore;
	
	public IECViewer() {
		sourceViewerFactory = new XtextSourceViewer.DefaultFactory();
	}
	
	@Inject
	public void setLanguageName(@Named(Constants.LANGUAGE_NAME) String name) {
		this.languageName = name;
	}
	
	public String getLanguageName() {
		return languageName;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		int styles= SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION;
		XtextSourceViewer fSourceViewer = sourceViewerFactory.createSourceViewer(parent, null, null, false, styles);
		
//		DefaultConstructionProxyFactory fact = new 
		fExplicitDocumentProvider = documentProvider.get(); // new MyXtextDocumentProvider();
		
		setSourceViewerConfiguration(sourceViewerConfiguration);
		
		Document document = new Document();
		document.set("FUNCTION_BLOCK Erdgeschoss");
		
		fSourceViewer.setDocument(document);
		
		initializeViewerColors(fSourceViewer);
		
		setPreferenceStore(preferenceStoreAccess.getPreferenceStore());
//		viewer.setInput("function blau");
		
	}
	
	protected void setPreferenceStore(IPreferenceStore preferenceStore) {
		if (getSourceViewerConfiguration() instanceof XtextSourceViewerConfiguration) {
			XtextSourceViewerConfiguration xtextSourceViewerConfiguration = (XtextSourceViewerConfiguration) getSourceViewerConfiguration();
			// xtextSourceViewerConfiguration.setPreferenceStore(preferenceStore);
		}
	}
	
	/**
	 * Returns the editor's source viewer configuration. May return <code>null</code>
	 * before the editor's part has been created and after disposal.
	 *
	 * @return the editor's source viewer configuration which may be <code>null</code>
	 */
	protected final SourceViewerConfiguration getSourceViewerConfiguration() {
		return fConfiguration;
	}

	private void setSourceViewerConfiguration(XtextSourceViewerConfiguration configuration) {
		fConfiguration = configuration;
	}

	public XtextSourceViewerConfiguration getXtextSourceViewerConfiguration() {
		return sourceViewerConfiguration;
	}
	
	private IDocumentProvider getDocumentProvider() {
		return fExplicitDocumentProvider;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Initializes the fore- and background colors of the given viewer for both
	 * normal and selected text.
	 *
	 * @param viewer the viewer to be initialized
	 * @since 2.0
	 */
	protected void initializeViewerColors(ISourceViewer viewer) {

		IPreferenceStore store= getPreferenceStore();
		if (store != null) {

			StyledText styledText= viewer.getTextWidget();

			// ----------- foreground color --------------------
			Color color= store.getBoolean(PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT)
				? null
				: createColor(store, PREFERENCE_COLOR_FOREGROUND, styledText.getDisplay());
			styledText.setForeground(color);

			if (fForegroundColor != null)
				fForegroundColor.dispose();

			fForegroundColor= color;

			// ---------- background color ----------------------
			color= store.getBoolean(PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT)
				? null
				: createColor(store, PREFERENCE_COLOR_BACKGROUND, styledText.getDisplay());
			styledText.setBackground(color);

			if (fBackgroundColor != null)
				fBackgroundColor.dispose();

			fBackgroundColor= color;

			// ----------- selection foreground color --------------------
			color= store.getBoolean(PREFERENCE_COLOR_SELECTION_FOREGROUND_SYSTEM_DEFAULT)
				? null
				: createColor(store, PREFERENCE_COLOR_SELECTION_FOREGROUND, styledText.getDisplay());
			styledText.setSelectionForeground(color);

			if (fSelectionForegroundColor != null)
				fSelectionForegroundColor.dispose();

			fSelectionForegroundColor= color;

			// ---------- selection background color ----------------------
			color= store.getBoolean(PREFERENCE_COLOR_SELECTION_BACKGROUND_SYSTEM_DEFAULT)
				? null
				: createColor(store, PREFERENCE_COLOR_SELECTION_BACKGROUND, styledText.getDisplay());
			styledText.setSelectionBackground(color);

			if (fSelectionBackgroundColor != null)
				fSelectionBackgroundColor.dispose();

			fSelectionBackgroundColor= color;
		}
	}
	
	/**
	 * Creates a color from the information stored in the given preference store.
	 * Returns <code>null</code> if there is no such information available.
	 *
	 * @param store the store to read from
	 * @param key the key used for the lookup in the preference store
	 * @param display the display used create the color
	 * @return the created color according to the specification in the preference store
	 * @since 2.0
	 */
	private Color createColor(IPreferenceStore store, String key, Display display) {

		RGB rgb= null;

		if (store.contains(key)) {

			if (store.isDefault(key))
				rgb= PreferenceConverter.getDefaultColor(store, key);
			else
				rgb= PreferenceConverter.getColor(store, key);

			if (rgb != null)
				return new Color(display, rgb);
		}

		return null;
	}
	/**
	 * Returns this editor's preference store or <code>null</code> if none has
	 * been set.
	 *
	 * @return this editor's preference store which may be <code>null</code>
	 */
	protected final IPreferenceStore getPreferenceStore() {
		return EditorsPlugin.getDefault().getPreferenceStore();
	}
}
