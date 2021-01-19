package com.bichler.astudio.opcua.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

//import javax.mail.Folder;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.Part;
//import javax.mail.Session;
//import javax.mail.Store;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class LicenseKeyDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txt_hardWareKey;
	private Text txt_licenseKey;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_6;
	private Text text_7;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public LicenseKeyDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		Form frm_mainForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(frm_mainForm);
		frm_mainForm.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.frm_mainForm.text"));
		formToolkit.decorateFormHeading(frm_mainForm);
		frm_mainForm.getBody().setLayout(new GridLayout(1, false));
		Section sctnNewSection = formToolkit.createSection(frm_mainForm.getBody(), Section.TITLE_BAR);
		sctnNewSection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.sctnNewSection.text")); //$NON-NLS-1$
		sctnNewSection.setExpanded(true);
		Composite composite = new Composite(sctnNewSection, SWT.NONE);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		sctnNewSection.setClient(composite);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginTop = 10;
		gl_composite.marginLeft = 10;
		composite.setLayout(gl_composite);
		Label lblCompany = new Label(composite, SWT.NONE);
		GridData gd_lblCompany = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblCompany.widthHint = 150;
		lblCompany.setLayoutData(gd_lblCompany);
		lblCompany.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.lblCompany.text")); //$NON-NLS-1$
		formToolkit.adapt(lblCompany, true, true);
		text = new Text(composite, SWT.BORDER);
		text.setText("LicenseKeyDialog.text.text");
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(text, true, true);
		Label lblAddress = new Label(composite, SWT.NONE);
		GridData gd_lblAddress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblAddress.widthHint = 150;
		lblAddress.setLayoutData(gd_lblAddress);
		lblAddress.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.lblAddress.text")); //$NON-NLS-1$
		formToolkit.adapt(lblAddress, true, true);
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setText("LicenseKeyDialog.text.text");
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(text_1, true, true);
		Label lblZip = new Label(composite, SWT.NONE);
		GridData gd_lblZip = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblZip.widthHint = 150;
		lblZip.setLayoutData(gd_lblZip);
		lblZip.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.lblZip.text")); //$NON-NLS-1$
		formToolkit.adapt(lblZip, true, true);
		text_2 = new Text(composite, SWT.BORDER);
		text_2.setText("LicenseKeyDialog.text.text");
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(text_2, true, true);
		Label lbl_hardwareKey = new Label(composite, SWT.NONE);
		GridData gd_lbl_hardwareKey = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_hardwareKey.widthHint = 150;
		lbl_hardwareKey.setLayoutData(gd_lbl_hardwareKey);
		formToolkit.adapt(lbl_hardwareKey, true, true);
		lbl_hardwareKey.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.lblNewLabel.text")); //$NON-NLS-1$
		txt_hardWareKey = new Text(composite, SWT.BORDER);
		txt_hardWareKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txt_hardWareKey
				.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.text.text"));
		formToolkit.adapt(txt_hardWareKey, true, true);
		Label lblCountry = new Label(composite, SWT.NONE);
		GridData gd_lblCountry = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblCountry.widthHint = 150;
		lblCountry.setLayoutData(gd_lblCountry);
		lblCountry.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.lblCountry.text")); //$NON-NLS-1$
		formToolkit.adapt(lblCountry, true, true);
		txt_licenseKey = new Text(composite, SWT.BORDER);
		txt_licenseKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txt_licenseKey.setText("LicenseKeyDialog.text.text");
		formToolkit.adapt(txt_licenseKey, true, true);
		Section sctnContactInfo = formToolkit.createSection(frm_mainForm.getBody(), Section.TITLE_BAR);
		sctnContactInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnContactInfo);
		sctnContactInfo.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"LicenseKeyDialog.sctnContactInfo.text")); //$NON-NLS-1$
		sctnContactInfo.setExpanded(true);
		Composite composite_1 = new Composite(sctnContactInfo, SWT.NONE);
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		sctnContactInfo.setClient(composite_1);
		GridLayout gl_composite_1 = new GridLayout(2, false);
		gl_composite_1.marginTop = 10;
		gl_composite_1.marginLeft = 10;
		composite_1.setLayout(gl_composite_1);
		Label lblname = new Label(composite_1, SWT.NONE);
		GridData gd_lblname = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblname.widthHint = 150;
		lblname.setLayoutData(gd_lblname);
		lblname.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.lblname.text")); //$NON-NLS-1$
		formToolkit.adapt(lblname, true, true);
		text_3 = new Text(composite_1, SWT.BORDER);
		text_3.setText("LicenseKeyDialog.text.text");
		text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(text_3, true, true);
		Label lblVorname = new Label(composite_1, SWT.NONE);
		GridData gd_lblVorname = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblVorname.widthHint = 150;
		lblVorname.setLayoutData(gd_lblVorname);
		lblVorname.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.lblVorname.text")); //$NON-NLS-1$
		formToolkit.adapt(lblVorname, true, true);
		text_4 = new Text(composite_1, SWT.BORDER);
		text_4.setText("LicenseKeyDialog.text.text");
		text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(text_4, true, true);
		Label lblPhone = new Label(composite_1, SWT.NONE);
		lblPhone.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.lblPhone.text")); //$NON-NLS-1$
		formToolkit.adapt(lblPhone, true, true);
		text_5 = new Text(composite_1, SWT.BORDER);
		text_5.setText("LicenseKeyDialog.text.text");
		text_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(text_5, true, true);
		Label lblMobile = new Label(composite_1, SWT.NONE);
		GridData gd_lblMobile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblMobile.widthHint = 150;
		lblMobile.setLayoutData(gd_lblMobile);
		lblMobile.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.lblMobile.text")); //$NON-NLS-1$
		formToolkit.adapt(lblMobile, true, true);
		text_6 = new Text(composite_1, SWT.BORDER);
		text_6.setText("LicenseKeyDialog.text.text");
		text_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(text_6, true, true);
		Label lblFax = new Label(composite_1, SWT.NONE);
		GridData gd_lblFax = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblFax.widthHint = 150;
		lblFax.setLayoutData(gd_lblFax);
		lblFax.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.lblFax.text")); //$NON-NLS-1$
		formToolkit.adapt(lblFax, true, true);
		text_7 = new Text(composite_1, SWT.BORDER);
		text_7.setText("LicenseKeyDialog.text.text");
		text_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(text_7, true, true);
		Label lblEmail = new Label(composite_1, SWT.NONE);
		lblEmail.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.lblEmail.text")); //$NON-NLS-1$
		formToolkit.adapt(lblEmail, true, true);
		new Label(composite_1, SWT.NONE);
		Composite composite_2 = new Composite(frm_mainForm.getBody(), SWT.NONE);
		composite_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_MAGENTA));
		GridLayout gl_composite_2 = new GridLayout(2, false);
		gl_composite_2.marginBottom = 5;
		gl_composite_2.marginRight = 10;
		gl_composite_2.marginLeft = 10;
		gl_composite_2.marginTop = 5;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		Button btnNewButton = new Button(composite_2, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnNewButton.widthHint = 150;
		btnNewButton.setLayoutData(gd_btnNewButton);
		formToolkit.adapt(btnNewButton, true, true);
		btnNewButton.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.btnNewButton.text_2")); //$NON-NLS-1$
		Button btnPerEmail = new Button(composite_2, SWT.NONE);
		btnPerEmail.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getMail("mail8.world4you.com", "hannes.bichler@hb-softsolution.com", "h77n19bi");
				/**
				 * postMail( "hannes.bichler@hb-softsolution.com", "activation code", "Wow. Das
				 * Buch ist schšn zu lesen", "office@hb-softsolution.com");
				 */
			}
		});
		GridData gd_btnPerEmail = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPerEmail.widthHint = 150;
		btnPerEmail.setLayoutData(gd_btnPerEmail);
		btnPerEmail.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LicenseKeyDialog.btnPerEmail.text")); //$NON-NLS-1$
		formToolkit.adapt(btnPerEmail, true, true);
		this.generateKey();
		return container;
	}

	public void postMail(String recipient, String subject, String message, String from) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", "mail8.world4you.com");
			Session session = Session.getDefaultInstance(props);
			Message msg = new MimeMessage(session);
			InternetAddress addressFrom = new InternetAddress(from);
			msg.setFrom(addressFrom);
			InternetAddress addressTo = new InternetAddress(recipient);
			msg.setRecipient(Message.RecipientType.TO, addressTo);
			msg.setSubject(subject);
			msg.setContent(message, "text/plain");
			Transport tr = session.getTransport("smtp");
			tr.connect("mail8.world4you.com", "hannes.bichler@hb-softsolution.com", "h77n19bi");
			msg.saveChanges(); // don't forget this
			tr.sendMessage(msg, msg.getAllRecipients());
			tr.close();
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
	}

	private void getMail(String host, String user, String passwd) {
		try {
			Properties props = new Properties();
			props.put("mail.pop3.port", "110");
			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("pop3");
			store.connect(host, user, passwd);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			Message[] message = folder.getMessages();
			for (int i = 0; i < message.length; i++) {
				Message m = message[i];
				// System.out.println( "Nachricht: " + i );
				// System.out.println( "From: " + m.getFrom()[0] );
				// System.out.println( "Subject: " + m.getSubject() );
				Multipart mp = (Multipart) m.getContent();
				for (int j = 0; j < mp.getCount(); j++) {
					Part part = mp.getBodyPart(j);
					String disposition = part.getDisposition();
					if (disposition == null) {
						MimeBodyPart mimePart = (MimeBodyPart) part;
						if (mimePart.isMimeType("text/plain")) {
							BufferedReader in = new BufferedReader(new InputStreamReader(mimePart.getInputStream()));
							for (String line; (line = in.readLine()) != null;)
								System.out.println(line);
						}
					}
				}
			}
			folder.close(false);
			store.close();
		} catch (javax.mail.NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		createButton(parent, IDialogConstants.OPEN_ID, "demo", true);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(518, 521);
	}

	private void generateKey() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "HB-SOFTSOLUTION");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "HB-SOFTSOLUTION");
			keyGen.initialize(1024, random);
			KeyPair pair = keyGen.generateKeyPair();
			PrivateKey priv = pair.getPrivate();
			PublicKey pub = pair.getPublic();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
