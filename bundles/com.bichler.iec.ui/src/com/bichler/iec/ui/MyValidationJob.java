package com.bichler.iec.ui;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;

import java.io.PrintStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.internal.core.search.matching.ConstructorLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.internal.console.ConsoleView;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.service.OperationCanceledError;
import org.eclipse.xtext.ui.editor.validation.IValidationIssueProcessor;
import org.eclipse.xtext.ui.editor.validation.ValidationJob;
import org.eclipse.xtext.util.concurrent.IReadAccess;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

public class MyValidationJob extends ValidationJob {

	private static final Logger log = Logger.getLogger(ValidationJob.class);
	private final IResourceValidator resourceValidator;
	private final IReadAccess<XtextResource> xtextDocument;
	private final IValidationIssueProcessor validationIssueProcessor;
	private final CheckMode checkMode;
	private MessageConsole console = null;
	private Color errorColor = new Color(Display.getCurrent(), 255, 0, 0);
	private Color stdColor = new Color(Display.getCurrent(), 0, 0, 0);
	private MessageConsoleStream errorstream;
	private MessageConsoleStream stdstream;

	public MyValidationJob(IResourceValidator xtextResourceChecker, IReadAccess<XtextResource> xtextDocument,
			IValidationIssueProcessor validationIssueProcessor, CheckMode checkMode) {
		super(xtextResourceChecker, xtextDocument, validationIssueProcessor, checkMode);

		this.xtextDocument = xtextDocument;
		this.resourceValidator = xtextResourceChecker;
		this.validationIssueProcessor = validationIssueProcessor;
		this.checkMode = checkMode;
		
		console = findConsole("IEC Console");
	}

	private MessageConsole findConsole(String name) {

		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		MessageConsole myConsole = null;
		IConsole[] existing = conMan.getConsoles();
		// if console exists, clear it
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName())) {
				((MessageConsole) existing[i]).clearConsole(); // this is the
																// important
																// part
				return ((MessageConsole) existing[i]);
			}

		ImageDescriptor desc = AbstractUIPlugin
		.imageDescriptorFromPlugin(StudioImageActivator.PLUGIN_ID, StudioImages.ICON_16_FUNCTIONBLOCK);
		myConsole = new MessageConsole(name, desc);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	@Override
	protected IStatus run(final IProgressMonitor monitor) {
		if (monitor.isCanceled())
			return Status.CANCEL_STATUS;
		final List<Issue> issues = createIssues(monitor);
		try {
			Display.getDefault().syncExec(new Runnable() {
				
				@Override
				public void run() {
					console.clearConsole();
					errorstream = console.newMessageStream();
					errorstream.setColor(errorColor);
					PrintStream myES = new PrintStream(errorstream);
					stdstream = console.newMessageStream();
					stdstream.setColor(stdColor);
					PrintStream mySS = new PrintStream(stdstream);
					System.setOut(mySS); // link standard output stream to the console
					System.setErr(myES); // link error output stream to the console
					
					if (issues != null && !issues.isEmpty()) {
						for (Issue iss : issues) {
							
							System.err.println(iss.getSeverity() + " | " + "line: " + iss.getLineNumber() + " column: "
									+ iss.getColumn() + " | file: " + iss.getUriToProblem().lastSegment().replace(".iec", "")
									+ " | " + iss.getMessage());
						}
					} else {
						System.out.println("INFO | compilation OK");
					}
				}
			});
			
		} catch (OperationCanceledError canceled) {
			return Status.CANCEL_STATUS;
		} catch (OperationCanceledException canceled) {
			return Status.CANCEL_STATUS;
		} catch (Exception e) {
			log.error("Error running validator", e);
			return Status.OK_STATUS;
		}
		if (monitor.isCanceled())
			return Status.CANCEL_STATUS;
		this.validationIssueProcessor.processIssues(issues, monitor);
		if (monitor.isCanceled())
			return Status.CANCEL_STATUS;
		return Status.OK_STATUS;
	}

}
