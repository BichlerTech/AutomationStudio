package com.bichler.astudio.licensemanagement.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.licensemanagement.editor.inputs.LicenseManagerEditorInput;
import com.bichler.astudio.utils.internationalization.CustomString;

public class LicenseManagerEditorPart extends EditorPart {

	public static final String ID = "com.bichler.astudio.editors.licensemanager";

	@Override
	public String getTitleToolTip() {
		return CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "editor.tooltip");
	}

	@Override
	public String getTitle() {
		return CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "editor.title");
	}

	private Label lbl_companyName;
	private Text txt_company;
	private Label lbl_firstName;
	private Text txt_firstname;
	private Label lbl_lastName;
	private Text txt_lastname;
	private Label lblTool;
	private Combo cmb_tool;
	private Label lblLicense;
	private Text txt_lic1;
	private Label label;
	private Text txt_lic2;
	private Label label_1;
	private Text txt_lic3;
	private Label label_2;
	private Text txt_lic4;
	private Label label_3;
	private Text txt_lic5;
	private ListViewer listViewer;
	private List lst_license;
	private Button btnCheckLicense;
	private Button btn_createLic;
	private Button btn_saveLic;
	private StringBuffer licenceContent = new StringBuffer();

	public LicenseManagerEditorPart() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {

		GridLayout gl_parent = new GridLayout(12, false);
		gl_parent.horizontalSpacing = 0;
		parent.setLayout(gl_parent);

		new Label(parent, SWT.NONE);
		lbl_companyName = new Label(parent, SWT.NONE);
		lbl_companyName.setText(CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.company")+" *");
		new Label(parent, SWT.NONE);

		txt_company = new Text(parent, SWT.BORDER);
		txt_company.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Device device = Display.getCurrent();
				lbl_companyName.setForeground(new Color(device, 0, 0, 0));
				return;
			}
		});
		txt_company.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 7, 1));
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		new Label(parent, SWT.NONE);
		lbl_firstName = new Label(parent, SWT.NONE);
		lbl_firstName.setText(CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.firstname")+" *");
		new Label(parent, SWT.NONE);

		txt_firstname = new Text(parent, SWT.BORDER);
		txt_firstname.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Device device = Display.getCurrent();
				lbl_firstName.setForeground(new Color(device, 0, 0, 0));
				return;
			}
		});
		txt_firstname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		new Label(parent, SWT.NONE);
		lbl_lastName = new Label(parent, SWT.NONE);
		lbl_lastName.setText(CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.surename")+" *");
		new Label(parent, SWT.NONE);

		txt_lastname = new Text(parent, SWT.BORDER);
		txt_lastname.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Device device = Display.getCurrent();
				lbl_lastName.setForeground(new Color(device, 0, 0, 0));
				return;
			}
		});
		txt_lastname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		lblTool = new Label(parent, SWT.NONE);
		lblTool.setText("Tool: *");
		new Label(parent, SWT.NONE);

		cmb_tool = new Combo(parent, SWT.READ_ONLY);
		cmb_tool.setItems(new String[] { "Comet WebVisu", "Comet UA Server SDK", "Comet UA Client SDK" });
		cmb_tool.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		cmb_tool.select(0);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		new Label(parent, SWT.NONE);
		lblLicense = new Label(parent, SWT.NONE);
		lblLicense.setText("License: *");
		new Label(parent, SWT.NONE);

		txt_lic1 = new Text(parent, SWT.BORDER);
		txt_lic1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// validate if button create license key is enabled
				validateLicenseButton();
				if (txt_lic1.getText() != null && txt_lic1.getText().length() == 4) {
					txt_lic2.setFocus();
				}
			}
		});
		txt_lic1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_lic1.setTextLimit(4);

		label = new Label(parent, SWT.NONE);
		label.setText("-");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txt_lic2 = new Text(parent, SWT.BORDER);
		txt_lic2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// validate if button create license key is enabled
				validateLicenseButton();
				if (txt_lic2.getText() != null && txt_lic2.getText().length() == 4) {
					txt_lic3.setFocus();
				}
			}
		});
		txt_lic2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_lic2.setTextLimit(4);

		label_1 = new Label(parent, SWT.NONE);
		label_1.setText("-");
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txt_lic3 = new Text(parent, SWT.BORDER);
		txt_lic3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// validate if button create license key is enabled
				validateLicenseButton();
				if (txt_lic3.getText() != null && txt_lic3.getText().length() == 4) {
					txt_lic4.setFocus();
				}
			}
		});
		txt_lic3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_lic3.setTextLimit(4);

		label_2 = new Label(parent, SWT.NONE);
		label_2.setText("-");
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txt_lic4 = new Text(parent, SWT.BORDER);
		txt_lic4.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// validate if button create license key is enabled
				validateLicenseButton();
				if (txt_lic4.getText() != null && txt_lic4.getText().length() == 4) {
					txt_lic5.setFocus();
				}
			}
		});
		txt_lic4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_lic4.setTextLimit(4);

		label_3 = new Label(parent, SWT.NONE);
		label_3.setText("-");
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txt_lic5 = new Text(parent, SWT.BORDER);
		txt_lic5.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// validate if button create license key is enabled
				validateLicenseButton();
				if (txt_lic5.getText() != null && txt_lic5.getText().length() == 4) {
					btn_createLic.setFocus();
				}
			}
		});
		txt_lic5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_lic5.setTextLimit(4);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		listViewer = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);
		lst_license = listViewer.getList();
		lst_license.setEnabled(false);
		GridData gd_lst_license = new GridData(SWT.FILL, SWT.FILL, false, false, 9, 1);
		gd_lst_license.heightHint = 152;
		lst_license.setLayoutData(gd_lst_license);
		new Label(parent, SWT.NONE);

		btnCheckLicense = new Button(parent, SWT.NONE);
		btnCheckLicense.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// System.out.println(getEditorInput().getManager().getRootPath());

				String name = /**
								 * getEditorInput().getManager().getRootPath() +
								 */
				"license.dat";

				if (name != null) {
					File file = new File(name);
					StringBuffer buffer = new StringBuffer();
					BufferedReader reader;
					try {
						reader = new BufferedReader(new FileReader(file));
						String line = "";
						String newline = "";
						while ((line = reader.readLine()) != null) {
							if (line.toUpperCase().startsWith("CHECKSUM:")) {
								break;
							}

							buffer.append(newline + line);
							newline = "\n";

						}

						line = line.replace("CHECKSUM:", "");
						line = line.trim();

						MessageDigest complete = MessageDigest.getInstance("MD5");
						byte[] content = buffer.toString().getBytes();
						int contentlength = content.length;

						complete.update(content, 0, contentlength);
						byte[] checksum = complete.digest();

						StringBuffer generated = new StringBuffer();
						for (byte check : checksum) {
							generated.append(Integer.toString((check & 0xff) + 0x100, 16).substring(1));
						}

					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (NoSuchAlgorithmException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
				}

			}
		});
		btnCheckLicense.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		btnCheckLicense.setText("Check License");
		new Label(parent, SWT.NONE);

		btn_createLic = new Button(parent, SWT.NONE);
		btn_createLic.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					createLicense();
				}
			}
		});
		btn_createLic.setEnabled(false);
		btn_createLic.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1));
		btn_createLic.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createLicense();
			}
		});
		btn_createLic.setText("Create License");
		new Label(parent, SWT.NONE);

		btn_saveLic = new Button(parent, SWT.NONE);
		btn_saveLic.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getSite().getShell(), SWT.SAVE);
				String name = dialog.open();

				if (name != null) {
					// now create checksum of file
					try {
						File newFile = new File(name);
						if (!newFile.exists()) {
							newFile.createNewFile();
						}

						// new we need the licence content
						MessageDigest complete = MessageDigest.getInstance("MD5");
						byte[] content = licenceContent.toString().getBytes();
						int contentlength = content.length;

						complete.update(content, 0, contentlength);
						byte[] checksum = complete.digest();

						StringBuffer sum = new StringBuffer();
						// add checksum string
						sum.append("\nCHECKSUM: ");
						for (byte check : checksum) {
							// System.out.print(Integer.toString( ( check & 0xff
							// ) + 0x100, 16).substring( 1 ));
							sum.append(Integer.toString((check & 0xff) + 0x100, 16).substring(1));
						}

						FileWriter writer = new FileWriter(newFile);
						writer.write(licenceContent.toString() + sum.toString());

						writer.close();

					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
				}
			}
		});
		GridData gd_btn_saveLic = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1);
		gd_btn_saveLic.widthHint = 108;
		btn_saveLic.setLayoutData(gd_btn_saveLic);
		btn_saveLic.setText("Save License");
		btn_saveLic.setEnabled(false);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	private void createLicense() {
		if (txt_company.getText() == null || txt_company.getText().isEmpty()) {
			Device device = Display.getCurrent();
			lbl_companyName.setForeground(new Color(device, 255, 0, 0));
			return;
		}
		if (txt_firstname.getText() == null || txt_firstname.getText().isEmpty()) {
			Device device = Display.getCurrent();
			lbl_firstName.setForeground(new Color(device, 255, 0, 0));
			return;
		}
		if (txt_lastname.getText() == null || txt_lastname.getText().isEmpty()) {
			Device device = Display.getCurrent();
			lbl_lastName.setForeground(new Color(device, 255, 0, 0));
			return;
		}

		lst_license.removeAll();
		this.licenceContent = new StringBuffer();
		// Construct data
		try {
			String data = URLEncoder.encode("lic1", "UTF-8") + "=" + URLEncoder.encode(txt_lic1.getText(), "UTF-8");
			data += "&" + URLEncoder.encode("lic2", "UTF-8") + "=" + URLEncoder.encode(txt_lic2.getText(), "UTF-8");
			data += "&" + URLEncoder.encode("lic3", "UTF-8") + "=" + URLEncoder.encode(txt_lic3.getText(), "UTF-8");
			data += "&" + URLEncoder.encode("lic4", "UTF-8") + "=" + URLEncoder.encode(txt_lic4.getText(), "UTF-8");
			data += "&" + URLEncoder.encode("lic5", "UTF-8") + "=" + URLEncoder.encode(txt_lic5.getText(), "UTF-8");

			data += "&" + URLEncoder.encode("cName", "UTF-8") + "=" + URLEncoder.encode(txt_company.getText(), "UTF-8");
			data += "&" + URLEncoder.encode("fName", "UTF-8") + "="
					+ URLEncoder.encode(txt_firstname.getText(), "UTF-8");
			data += "&" + URLEncoder.encode("lName", "UTF-8") + "="
					+ URLEncoder.encode(txt_lastname.getText(), "UTF-8");
			data += "&" + URLEncoder.encode("tool", "UTF-8") + "=" + URLEncoder.encode(cmb_tool.getText(), "UTF-8");
			byte[] mac = null;
			int[] macPositive = new int[6];
			NetworkInterface ni;
			try {
				InetAddress add = InetAddress.getLocalHost();

				ni = NetworkInterface.getByInetAddress(add);

				if (ni != null) {
					mac = ni.getHardwareAddress();
					if (mac != null) {
						for (int i = 0; i < mac.length; i++) {
							macPositive[i] = mac[i];
							if (macPositive[i] < 0) {
								macPositive[i] = 128 + macPositive[i] + 128;
							}
							String val = "0" + Integer.toHexString(macPositive[i]);
							val = val.substring(val.length() - 2);
							data += "&" + URLEncoder.encode("mac" + i, "UTF-8") + "=" + URLEncoder.encode(val, "UTF-8");
						}
					}
				}
			} catch (SocketException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			} catch (UnknownHostException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			// Send data
			URL url = new URL("http://www.hb-softsolution.com/license_gen/license_gen.php");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			if ((line = rd.readLine()) != null) {
				try {
					int resp = Integer.parseInt(line);

					if (resp == 0) {
						btn_createLic.setEnabled(false);
						btn_saveLic.setEnabled(true);
					}

				} catch (NumberFormatException ex) {
					lst_license.add("can not evaluate return value from license server");
					wr.close();
					rd.close();
					btn_saveLic.setEnabled(false);
					return;
				}
			}
			while ((line = rd.readLine()) != null) {
				// txt_license
				lst_license.add(line);
				this.licenceContent.append(line + "\n");
				// System.out.println(line);
			}
			wr.close();
			rd.close();

		} catch (UnsupportedEncodingException ex1) {
			// TODO Auto-generated catch block
			lst_license.add("Can not connect to License server!");
		} catch (MalformedURLException ex2) {
			// TODO Auto-generated catch block
			lst_license.add("Can not connect to License server!");
		} catch (UnknownHostException ex3) {
			lst_license.add("Can not connect to License server!");
		} catch (IOException ex3) {
			// TODO Auto-generated catch block
			lst_license.add("Can not connect to License server!");
		}
	}

	/**
	 * 
	 */
	private void validateLicenseButton() {
		if (txt_lic1.getText() == null || txt_lic1.getText().length() < 4 || txt_lic2.getText() == null
				|| txt_lic2.getText().length() < 4 || txt_lic3.getText() == null || txt_lic3.getText().length() < 4
				|| txt_lic4.getText() == null || txt_lic4.getText().length() < 4 || txt_lic5.getText() == null
				|| txt_lic5.getText().length() < 4) {
			btn_createLic.setEnabled(false);

			return;
		}

		btn_createLic.setEnabled(true);
	}

	@Override
	public LicenseManagerEditorInput getEditorInput() {
		// TODO Auto-generated method stub
		return (LicenseManagerEditorInput) super.getEditorInput();
	}
}
