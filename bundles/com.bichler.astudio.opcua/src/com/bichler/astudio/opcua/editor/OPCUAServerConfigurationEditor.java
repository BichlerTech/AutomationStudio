package com.bichler.astudio.opcua.editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.opcfoundation.ua.common.NamespaceTable;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.opcua.IOPCPerspectiveEditor;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.opcua.driver.OPCDriverUtil;
import com.bichler.astudio.opcua.driver.OPCModuleUtil;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.NumericText;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.components.ui.ComponentsUIActivator;
import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.device.core.preference.DevicePreferenceConstants;
import com.bichler.astudio.device.core.preference.DevicePreferenceManager;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.editor.input.OPCUAServerConfigEditorInput;
import com.bichler.astudio.opcua.editor.server.ServerConfigUtil;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerConfigModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelsNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModuleModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;

import opc.sdk.core.application.ApplicationConfiguration;

public class OPCUAServerConfigurationEditor extends EditorPart implements IOPCPerspectiveEditor {
	public static final String ID = "com.bichler.astudio.editors.opcuaserverconfigeditor"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private static final String DRIVER_NAME_MYSQL = "com.mysql.jdbc.Driver";
	private static final String DRIVER_URL_MYSQL = "jdbc:mysql://";
	private static final String DRIVER_NAME_SQLITE = "org.sqlite.JDBC";
	private static final String DRIVER_URL_SQLITE = "jdbc:sqlite://";
	private static final String DRIVER_NAME_MSSQL = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	private static final String DRIVER_URL_MSSQL = "jdbc:microsoft:sqlserver://";
	private static final String DRIVER_NAME_ORACLE = "oracle.jdbc.driver.OracleDriver";
	private static final String DRIVER_URL_ORACLE = "jdbc:oracle:thin://";
	private static final String DRIVER_NAME_CLOUD = "com.hbsoft.cloud.CloudDriver";
	private static final String DRIVER_URL_CLOUD = "jdbc:cloud://";
	private static final String DRIVER_NAME_CSV = "CSV";
	private static final String DRIVER_URL_CSV = "";
	private static final String[] DATABASES = new String[] { DRIVER_NAME_MYSQL, DRIVER_NAME_SQLITE, DRIVER_NAME_MSSQL,
			DRIVER_NAME_ORACLE, DRIVER_NAME_CLOUD, DRIVER_NAME_CSV };

	private ScrolledComposite scrolledComposite;
	private Composite composite;
	private Label lblDatabaseType;
	private Combo cmb_historicalDriverType;
	private Text txtDatabase;
	private Label lblDatabase;
	private Text txtDBName;
	private Label lblDBName;
	private Text txtUser;
	private Label lblUser;
	private Text txtPassword;
	private Label lblPassword;
	private Section sctnApplication;
	private Section sctnInformationModel;
	private Composite composite_1;
	private Label label_2;
	private Text txt_applicationName;
	private Label label_3;
	private Text txt_appuri;
	private Label label_4;
	private Text txt_producturi;
	private Label label_5;
	private Text txt_apptype;
	private Section sctnEndpoints;
	private Composite cmp_InformationModel;
	private List list_endpoints;
	private List list_informationmodel;
	private Button btn_addAddress;
	private Button btn_removeAddress;
	private Section sctnTransportQuotas;
	private Composite composite_4;
	private Label lbl_operationTimeout;
	private NumericText txt_operationTimeout;
	private Label lbl_maxStringLength;
	private Label lbl_maxByteString;
	private Label lbl_maxArrayLength;
	private NumericText txt_maxArrayLength;
	private Label lbl_maxMessageSize;
	private NumericText txt_maxMessageSize;
	private Label lbl_maxBufferSize;
	private NumericText txt_maxBufferSize;
	private Label lbl_channelLifetime;
	private NumericText txt_channelLifetime;
	private Label lbl_securityTokenLifetime;
	private NumericText txt_securityTokenLifetime;
	private Label lbl_maxSubscriptionCount;
	private NumericText txt_maxSubscriptionCount;
	private NumericText txt_maxStringLength;
	private NumericText txt_maxByteStringLength;
	private Label lbl_publishResolution;
	private NumericText txt_publishResolution;
	private Section sctnHistoricalConfig;
	private Section sctnDriverConfig;
	private Section sctnDevicesConfig;
	private Section sctnModuleConfig;
	// private Composite cmpHistory;
	private Button cbox_activeDatabase;
	private Button btn_addInformationModel;
	private Button btn_removeInformationModel;
	private Button btn_editAddress;
	private ListViewer listViewer_InformationModel;
	private ListViewer listViewer_endpoints;
	private Button btn_editInformationModel;
	private Label label_6;
	private Text txt_certvalidity;

	private Button btnBrowseDB;

	private boolean initState = true;
	private boolean dirty = false;

	public class InfoTableItem {
		private boolean enabled = false;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	@Override
	public String getPartName() {
		return getEditorInput().getNode().getServerName();
	}

	@Override
	public OPCUAServerConfigEditorInput getEditorInput() {
		return (OPCUAServerConfigEditorInput) super.getEditorInput();
	}

	public OPCUAServerConfigurationEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setMinWidth(700);
		scrolledComposite.setMinHeight(750);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		composite.setLayout(new GridLayout(1, false));
		createSections();
		scrolledComposite.setMinSize(new Point(700, 750));
		this.fillControls();
		setHandler();
		computeSize();
		// set index 0 driver if none exist
		if (getEditorInput().getAppConfig().getHistoryConfiguration().getDrvName().isEmpty()) {
			this.cmb_historicalDriverType.notifyListeners(SWT.Selection, new Event());
		} else {
			switch (getEditorInput().getAppConfig().getHistoryConfiguration().getDrvName()) {
			// show required fields
			case "CSV":
				layoutDataExclude(true, lblDBName, lblUser, lblPassword, txtDBName, txtUser, txtPassword);
				computeSize();
				break;
			}
		}
		// we have now exit initstate
		this.initState = false;
	}

	public void fillControls() {
		ApplicationConfiguration config = getEditorInput().getAppConfig();
		ComponentsUIActivator.getDefault().getPreferenceStore();
		IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
		/**
		 * general application descriptions
		 */
		this.txt_applicationName.setText(config.getApplicationName());
		this.txt_appuri.setText(config.getApplicationUri());
		this.txt_producturi.setText(config.getProductUri());
		this.txt_apptype.setText(config.getApplicationType().name());
		this.txt_certvalidity
				.setText(config.getCertificateValidity().isEmpty() ? "" + 3650 : config.getCertificateValidity());
		/**
		 * transport quotas
		 */
		this.txt_operationTimeout.setText(config.getOperationTimeout());
		this.txt_maxStringLength.setText(config.getMaxStringLength());
		this.txt_maxByteStringLength.setText(config.getMaxByteStringLength());
		this.txt_maxArrayLength.setText(config.getMaxArrayLength());
		this.txt_maxMessageSize.setText(config.getMaxMessageSize());
		this.txt_maxBufferSize.setText(config.getMaxBufferSize());
		this.txt_channelLifetime.setText(config.getChannelLifetime());
		this.txt_securityTokenLifetime.setText(config.getSecurityTokenLifetime());
		this.txt_maxSubscriptionCount.setText(config.getServerConfiguration().getMaxSubscriptionCount() + "");
		this.txt_publishResolution.setText(config.getServerConfiguration().getPublishingResolution());
		this.cbox_activeDatabase.setSelection(config.getHistoryConfiguration().isActive());
		this.cmb_historicalDriverType.select(0);

		if (config.getHistoryConfiguration().getDrvName() != null
				&& !config.getHistoryConfiguration().getDrvName().isEmpty()) {
			cmb_historicalDriverType.setText(config.getHistoryConfiguration().getDrvName());
			txtDatabase.setText(config.getHistoryConfiguration().getDatabase());
			txtDBName.setText(config.getHistoryConfiguration().getDatabaseName());
			txtUser.setText(config.getHistoryConfiguration().getUser());
			txtPassword.setText(config.getHistoryConfiguration().getPw());
		}
		/**
		 * certificate
		 */
		// txt_storePath.setText(config.getSecurityConfiguration()
		// .getApplicationCertificateStorePath());
		IFileSystem filesystem = getEditorInput().getNode().getFilesystem();
		/**
		 * fill trusted certificates
		 **/
		String path = filesystem.getRootPath() + filesystem.getTargetFileSeparator()
				+ opcuastore.getString(OPCUAConstants.ASOPCUAServersPath) + filesystem.getTargetFileSeparator()
				+ getEditorInput().getNode().getServerName() + filesystem.getTargetFileSeparator() + "certificateStore"
				+ filesystem.getTargetFileSeparator() + "trusted" + filesystem.getTargetFileSeparator() + "PrivateKey";
		// keyDir = new File(path);
		/**
		 * fill rejected certificates
		 **/
		path = filesystem.getRootPath() + filesystem.getTargetFileSeparator()
				+ opcuastore.getString(OPCUAConstants.ASOPCUAServersPath) + filesystem.getTargetFileSeparator()
				+ getEditorInput().getNode().getServerName() + filesystem.getTargetFileSeparator() + "certificateStore"
				+ filesystem.getTargetFileSeparator() + "rejected" + filesystem.getTargetFileSeparator() + "PrivateKey";
		// keyDir = new File(path);
		// java.util.List<String> rej = new ArrayList<String>();
		/**
		 * server configuration
		 */
		java.util.List<String> endpoints = config.getEndpoints();
		for (String endpoint : endpoints) {
			list_endpoints.add(endpoint);
		}
		java.util.List<String> informationmodells = config.getServerInformationModels();
		for (String informationmodel : informationmodells) {
			list_informationmodel.add(informationmodel);
		}
		// java.util.List<ServerSecurityPolicy> policies = config
		// .getSecurityPolicy();
		// for (ServerSecurityPolicy policy : policies) {
		// lst_securityPolicies.add(policy.getSecurityMode()
		// .getMessageSecurityMode().name());
		// }
		/**
		 * fill information models
		 */
		java.util.List<InfoTableItem> items = new ArrayList<OPCUAServerConfigurationEditor.InfoTableItem>();
		path = filesystem.getRootPath() + filesystem.getTargetFileSeparator()
				+ opcuastore.getString(OPCUAConstants.ASOPCUAServersPath) + filesystem.getTargetFileSeparator()
				+ getEditorInput().getNode().getServerName() + filesystem.getTargetFileSeparator() + "informationmodel";
		new File(path);
		if (filesystem.isDir(path)) {
			String[] files;
			try {
				files = filesystem.listFiles(path);
				InfoTableItem item = null;
				for (String file : files) {
					if (file.startsWith(".") || file.startsWith("..") || file.compareTo("nodeset.xml") == 0) {
						continue;
					}
					item = new InfoTableItem();
					if (config.getServerInformationModels() != null) {
						item.setEnabled(config.getServerInformationModels().contains(file));
					}
					items.add(item);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// set input if items are empty or not
		// tableViewer.setInput(items);
	}

	@Override
	public void setFocus() {
		// Set the focus
		this.scrolledComposite.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		ServerConfigUtil.doSaveServerConfiguration(getEditorInput().getNode().getFilesystem(),
				getEditorInput().getAppConfig());
		// ICometFileSystem filesystem = getEditorInput().getNode()
		// .getFilesystem();
		// String path = new Path(filesystem.getRootPath())
		// .append("serverconfig").append("server.config.xml")
		// .toOSString();
		//
		// this.persist(filesystem, path, false);
		// path = new Path(filesystem.getRootPath()).append("serverconfig")
		// .append("server.config.com").toOSString();
		// this.persist(filesystem, path, true);
		postSave();
		setDirty(false);
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
	}

	public void setDirty(boolean dirty) {
		if (this.initState) {
			return;
		}

		this.dirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isDirty() {
		return this.dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * we persist a previously loaded and changed application config file
	 */
	// public void persist(IFileSystem filesystem, String file, boolean
	// skipEndpoints) {
	// StringBuffer output = new StringBuffer();
	//
	// output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	// output.append("<ApplicationConfiguration");
	//// for (Namespace ns : getEditorInput().getAppConfig().getXmlNamespaces()) {
	////
	//// output.append(" xmlns:" + ns.getPrefix() + "=\"");
	//// output.append(ns.getURI() + "\"");
	//// }
	// output.append(">\n");
	//
	// output.append(" <ApplicationName>");
	// output.append(getEditorInput().getAppConfig().getApplicationName());
	// output.append("</ApplicationName>\n");
	//
	// output.append(" <ApplicationUri>");
	// output.append(getEditorInput().getAppConfig().getApplicationUri());
	// output.append("</ApplicationUri>\n");
	//
	// output.append(" <ProductUri>");
	// output.append(getEditorInput().getAppConfig().getProductUri());
	// output.append("</ProductUri>\n");
	//
	// output.append(" <ApplicationType>");
	// output.append(getEditorInput().getAppConfig().getApplicationType());
	// output.append("</ApplicationType>\n");
	//
	// output.append(" <SecurityConfiguration>\n");
	// output.append(" <ApplicationCertificate>\n");
	// output.append(" <StorePath>");
	// output.append(getEditorInput().getAppConfig().getSecurityConfiguration().getApplicationCertificateStorePath());
	// output.append("</StorePath>\n");
	// output.append(" <CertKeyName>");
	// output.append(getEditorInput().getAppConfig().getSecurityConfiguration().getApplicationCertificate()
	// .getCertKeyName());
	// output.append("</CertKeyName>\n");
	// output.append(" <SubjectName>");
	// output.append(getEditorInput().getAppConfig().getSecurityConfiguration().getApplicationCertificate()
	// .getSubjectName());
	// output.append("</SubjectName>\n");
	// output.append(" </ApplicationCertificate>\n");
	//
	// output.append(" <TrustedPeerCertificates>\n");
	// output.append(" <StorePath>");
	// output.append(
	// getEditorInput().getAppConfig().getSecurityConfiguration().getTrustedPeerCertificates().getStorePath());
	// output.append("</StorePath>\n");
	// // ArrayList<TrustedPeerCertificates.PeerCertificate> certs =
	// // (ArrayList<TrustedPeerCertificates.PeerCertificate>)
	// // tblv_trustedCerts
	// // .getInput();
	// // if (certs != null) {
	// // for (PeerCertificate cert : certs) {
	// // output.append(" <Certificate enabled=");
	// // output.append("\"" + cert.isEnabled() + "\"");
	// // output.append(">");
	// // output.append(cert.getCertName());
	// // output.append("</Certificate>\n");
	// // }
	// // }
	//
	// output.append(" </TrustedPeerCertificates>\n");
	//
	// output.append(" </SecurityConfiguration>\n");
	//
	// output.append(" <TransportQuotas>\n");
	// output.append(" <OperationTimeout>");
	// output.append(getEditorInput().getAppConfig().getOperationTimeout());
	// output.append("</OperationTimeout>\n");
	// output.append(" <MaxStringLength>");
	// output.append(getEditorInput().getAppConfig().getMaxStringLength());
	// output.append("</MaxStringLength>\n");
	// output.append(" <MaxByteStringLength>");
	// output.append(getEditorInput().getAppConfig().getMaxByteStringLength());
	// output.append("</MaxByteStringLength>\n");
	// output.append(" <MaxArrayLength>");
	// output.append(getEditorInput().getAppConfig().getMaxArrayLength());
	// output.append("</MaxArrayLength>\n");
	// output.append(" <MaxMessageSize>");
	// output.append(getEditorInput().getAppConfig().getMaxMessageSize());
	// output.append("</MaxMessageSize>\n");
	// output.append(" <MaxBufferSize>");
	// output.append(getEditorInput().getAppConfig().getMaxBufferSize());
	// output.append("</MaxBufferSize>\n");
	// output.append(" <ChannelLifetime>");
	// output.append(getEditorInput().getAppConfig().getChannelLifetime());
	// output.append("</ChannelLifetime>\n");
	// output.append(" <SecurityTokenLifetime>");
	// output.append(getEditorInput().getAppConfig().getSecurityTokenLifetime());
	// output.append("</SecurityTokenLifetime>\n");
	// output.append(" </TransportQuotas>\n");
	//
	// output.append(" <ServerConfiguration>\n");
	//
	// output.append(" <BaseAddresses>\n");
	// /**
	// * server.config.xml has given endpoints
	// */
	// if (!skipEndpoints) {
	// for (String endpoint :
	// getEditorInput().getAppConfig().getServerConfiguration().getEndpoints()) {
	// output.append(" <ua:String>");
	// output.append(endpoint);
	// output.append("</ua:String>\n");
	// }
	// }
	// /**
	// * server.config.com is serverconfiguration for local server
	// */
	// else {
	// output.append(" <ua:String>opc.tcp://127.0.0.1:1234</ua:String>");
	// }
	// output.append(" </BaseAddresses>\n");
	//
	// /**
	// * fill in security policies
	// */
	// output.append(" <SecurityPolicies>\n");
	// for (ServerSecurityPolicy policy :
	// getEditorInput().getAppConfig().getSecurityPolicy()) {
	// output.append(" <ServerSecurityPolicy>\n");
	//
	// output.append(" <SecurityMode>");
	// output.append(policy.getSecurityMode().getMessageSecurityMode().name());
	// output.append("</SecurityMode>\n");
	//
	// output.append(" <SecurityPolicyUri>");
	// output.append(policy.getSecurityMode().getSecurityPolicy().getPolicyUri());
	// output.append("</SecurityPolicyUri>\n");
	//
	// output.append(" <SecurityLevel>");
	// output.append(policy.getSecurityLevel());
	// output.append("</SecurityLevel>\n");
	//
	// output.append(" </ServerSecurityPolicy>\n");
	// }
	// output.append(" </SecurityPolicies>\n");
	//
	// /**
	// * fill in user token policies
	// */
	// output.append(" <UserTokenPolicies>\n");
	// for (UserTokenPolicy policy :
	// getEditorInput().getAppConfig().getUserTokenPolicies()) {
	// output.append(" <ua:UserTokenPolicy>\n");
	// output.append(" <ua:TokenType>");
	// output.append(policy.getTokenType().toString());
	// output.append("</ua:TokenType>\n");
	// output.append(" </ua:UserTokenPolicy>\n");
	// }
	// output.append(" </UserTokenPolicies>\n");
	//
	// /**
	// * fill in additional infomations
	// */
	// output.append(" <DiagnosticsEnabled>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getDiagnosticsEnabled());
	// output.append("</DiagnosticsEnabled>\n");
	// output.append(" <MaxSessionCount>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxSessionCount());
	// output.append("</MaxSessionCount>\n");
	// output.append(" <MinSessionTimeout>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMinSessionTimeout());
	// output.append("</MinSessionTimeout>\n");
	// output.append(" <MaxSessionTimeout>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxSessionTimeout());
	// output.append("</MaxSessionTimeout>\n");
	// output.append(" <MaxBrowseContinuationPoints>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxBrowseContinuationPoints());
	// output.append("</MaxBrowseContinuationPoints>\n");
	// output.append(" <MaxQueryContinuationPoints>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxQueryContinuationPoints());
	// output.append("</MaxQueryContinuationPoints>\n");
	//
	// output.append(" <MaxHistoryContinuationPoints>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxHistoryContinuationPoints());
	// output.append("</MaxHistoryContinuationPoints>\n");
	// output.append(" <MaxRequestAge>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxRequestAge());
	// output.append("</MaxRequestAge>\n");
	// output.append(" <MinPublishingInterval>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMinPublishingInterval());
	// output.append("</MinPublishingInterval>\n");
	//
	// output.append(" <MaxPublishingInterval>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxPublishingInterval());
	// output.append("</MaxPublishingInterval>\n");
	// output.append(" <PublishingResolution>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getPublishingResolution());
	// output.append("</PublishingResolution>\n");
	//
	// output.append(" <MaxSubscriptionLifetime>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxSubscriptionLifetime());
	// output.append("</MaxSubscriptionLifetime>\n");
	//
	// output.append(" <MaxMessageQueueSize>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxMessageQueueSize());
	// output.append("</MaxMessageQueueSize>\n");
	//
	// output.append(" <MaxNotificationQueueSize>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxNotificationQueueSize());
	// output.append("</MaxNotificationQueueSize>\n");
	//
	// output.append(" <MaxNotificationsPerPublish>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxNotificationPerPublish());
	// output.append("</MaxNotificationsPerPublish>\n");
	//
	// output.append(" <MinMetadataSamplingInterval>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMinMetaDataSamplingInterval());
	// output.append("</MinMetadataSamplingInterval>\n");
	//
	// output.append(" <MaxRegistrationInterval>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxRegistrationInterval());
	// output.append("</MaxRegistrationInterval>\n");
	//
	// output.append(" <MaxSubscriptionCount>");
	// output.append(getEditorInput().getAppConfig().getServerConfiguration().getMaxSubscriptionCount());
	// output.append("</MaxSubscriptionCount>\n");
	//
	// output.append(" <HistoryConfiguration>\n");
	//
	// output.append(" <DriverName>");
	// output.append(getEditorInput().getAppConfig().getHistoryConfiguration().getDrvName());
	// output.append("</DriverName>\n");
	//
	// output.append(" <DBUrl>");
	// output.append(getEditorInput().getAppConfig().getHistoryConfiguration().getDatabase());
	// output.append("</DBUrl>\n");
	//
	// output.append(" <DBName>");
	// output.append(getEditorInput().getAppConfig().getHistoryConfiguration().getDatabaseName());
	// output.append("</DBName>\n");
	//
	// output.append(" <User>");
	// output.append(getEditorInput().getAppConfig().getHistoryConfiguration().getUser());
	// output.append("</User>\n");
	//
	// output.append(" <Pw>");
	// output.append(getEditorInput().getAppConfig().getHistoryConfiguration().getPw());
	// output.append("</Pw>\n");
	//
	// output.append(" </HistoryConfiguration>\n");
	//
	// output.append(" </ServerConfiguration>\n");
	//
	// output.append(" <InformationModel>\n");
	// for (String info :
	// getEditorInput().getAppConfig().getServerInformationModels()) {
	// output.append(" <InformationModelFile>");
	// output.append(info);
	// output.append("</InformationModelFile>\n");
	// }
	// output.append(" </InformationModel>\n");
	//
	// output.append("</ApplicationConfiguration>\n");
	// BufferedWriter writer = null;
	//
	// try {
	// if (filesystem.isFile(file)) {
	// filesystem.addFile(file);
	// }
	//
	// OutputStream os = filesystem.writeFile(file);
	// writer = new BufferedWriter(new OutputStreamWriter(os));
	// writer.write(output.toString());
	// writer.flush();
	// } catch (FileNotFoundException e1) {
	// e1.printStackTrace();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// } finally {
	// if (writer != null) {
	// try {
	// writer.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
	protected void computeSize() {
		Point size = computeSection();
		this.scrolledComposite.setMinSize(size);
		this.composite.layout(true);
	}

	private void setHandler() {
		/** application */
		txt_applicationName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setApplicationName(txt_applicationName.getText());
				setDirty(true);
			}
		});
		this.txt_appuri.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setApplicationUri(txt_appuri.getText());
				setDirty(true);
			}
		});
		this.txt_producturi.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setProductUri(txt_producturi.getText());
				setDirty(true);
			}
		});
		// final ControlDecoration dec = new
		// ControlDecoration(this.txt_certvalidity, SWT.TOP | SWT.LEFT);
		// dec.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR));
		// dec.setShowOnlyOnFocus(true);
		// dec.hide();
		this.txt_certvalidity.addKeyListener(new KeyAdapter() {
			// boolean active = false;
			@Override
			public void keyPressed(KeyEvent e) {
				String value = ((Text) e.getSource()).getText() + e.character;
				try {
					Integer.parseInt(value);
					// dec.hide();
				} catch (NumberFormatException nfe) {
					e.doit = false;
					// if (!this.active) {
					// System.out.println("33");
					// Display.getDefault().asyncExec(new Runnable() {
					//
					// @Override
					// public void run() {
					// active = true;
					// dec.show();
					// try {
					// Thread.sleep(1000);
					// } catch (InterruptedException e) {
					// e.printStackTrace();
					// }
					// dec.hide();
					// active = false;
					// }
					// });
					//
					// }
				}
			}
		});
		this.txt_certvalidity.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setCertificateValidity(txt_certvalidity.getText());
				setDirty(true);
			}
		});
		/** transport quotas */
		this.txt_channelLifetime.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setChannelLifetime(txt_channelLifetime.getText());
				setDirty(true);
			}
		});
		this.txt_maxArrayLength.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setMaxArrayLength(txt_maxArrayLength.getText());
				setDirty(true);
			}
		});
		this.txt_maxBufferSize.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setMaxBufferSize(txt_maxBufferSize.getText());
				setDirty(true);
			}
		});
		this.txt_maxByteStringLength.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setMaxByteStringLength(txt_maxByteStringLength.getText());
				setDirty(true);
			}
		});
		this.txt_maxMessageSize.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setMaxMessageSize(txt_maxMessageSize.getText());
				setDirty(true);
			}
		});
		this.txt_maxStringLength.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setMaxStringLength(txt_maxStringLength.getText());
				setDirty(true);
			}
		});
		this.txt_maxSubscriptionCount.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().getServerConfiguration()
						.setMaxSubscriptionCount(txt_maxSubscriptionCount.getText());
				setDirty(true);
			}
		});
		this.txt_publishResolution.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().getServerConfiguration()
						.setPublishingResolution(txt_publishResolution.getText());
				setDirty(true);
			}
		});
		this.txt_operationTimeout.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setOperationTimeout(txt_operationTimeout.getText());
				setDirty(true);
			}
		});
		this.txt_securityTokenLifetime.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getEditorInput().getAppConfig().setSecurityTokenLifetime(txt_securityTokenLifetime.getText());
				setDirty(true);
			}
		});
		ExpansionAdapter adapter = new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				computeSize();
			}
		};
		this.sctnApplication.addExpansionListener(adapter);
		this.sctnEndpoints.addExpansionListener(adapter);
		this.sctnTransportQuotas.addExpansionListener(adapter);
		this.sctnHistoricalConfig.addExpansionListener(adapter);
		this.sctnInformationModel.addExpansionListener(adapter);
		this.listViewer_endpoints.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (selection == null) {
					btn_editAddress.setEnabled(false);
					return;
				}
				btn_editAddress.setEnabled(true);
			}
		});
		this.btn_addAddress.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (initState)
					return;
				InputDialog dialog = new InputDialog(getSite().getShell(),
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
								"editor.form.addbaseaddress"),
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.addaddress"),
						"", null);
				if (dialog.open() == Dialog.OK) {
					java.util.List<String> endpoints = getEditorInput().getAppConfig().getEndpoints();
					endpoints.add(dialog.getValue());
					list_endpoints.add(dialog.getValue());
					list_endpoints.deselectAll();
					list_endpoints.select(endpoints.size() - 1);
					btn_editAddress.setEnabled(true);
					setDirty(true);
				}
			}
		});
		this.btn_editAddress.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (initState)
					return;
				String[] selection = list_endpoints.getSelection();
				if (selection != null && selection.length > 0) {
					InputDialog dialog = new InputDialog(getSite().getShell(),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"editor.form.editbaseaddress"),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"editor.form.editaddress"),
							selection[0], null);
					if (dialog.open() == 0) {
						java.util.List<String> models = getEditorInput().getAppConfig().getEndpoints();
						list_endpoints.removeAll();
						String newValue = dialog.getValue();
						// replace
						if (models.contains(selection[0])) {
							int index = models.indexOf(selection[0]);
							models.set(index, newValue);
						}
						for (String endpoint : models) {
							list_endpoints.add(endpoint);
						}
						btn_editAddress.setEnabled(false);
						setDirty(true);
					}
				}
			}
		});
		this.btn_removeAddress.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (initState)
					return;
				String[] selection = list_endpoints.getSelection();
				if (selection != null && selection.length > 0) {
					boolean confirmed = MessageDialog.openConfirm(getSite().getShell(),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"editor.form.deletebaseaddress"),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"editor.form.deleteaddress"));
					// MessageDialog dialog = new
					// MessageDialog(getSite().getShell(),
					// CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					// "editor.form.deletebaseaddress"),
					// null,
					// CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					// "editor.form.deleteaddress"),
					// MessageDialog.QUESTION, new String[] { "yes", "no" }, 0);
					if (confirmed) {
						java.util.List<String> models = getEditorInput().getAppConfig().getEndpoints();
						for (String endpoint : selection) {
							models.remove(endpoint);
							list_endpoints.remove(endpoint);
						}
						btn_editAddress.setEnabled(false);
						setDirty(true);
					}
				}
			}
		});
		this.list_endpoints.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				String[] selection = list_endpoints.getSelection();
				if (selection != null && selection.length > 0) {
					InputDialog dialog = new InputDialog(getSite().getShell(),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"editor.form.editbaseaddress"),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"editor.form.editaddress"),
							selection[0], null);
					if (dialog.open() == 0) {
						java.util.List<String> models = getEditorInput().getAppConfig().getEndpoints();
						list_endpoints.removeAll();
						String newValue = dialog.getValue();
						// replace
						if (models.contains(selection[0])) {
							int index = models.indexOf(selection[0]);
							models.set(index, newValue);
						}
						for (String endpoint : models) {
							list_endpoints.add(endpoint);
						}
						btn_editAddress.setEnabled(false);
						setDirty(true);
					}
				}
			}
		});
		this.btn_addInformationModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (initState)
					return;
				InputDialog dialog = new InputDialog(getSite().getShell(),
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.addopcmodel"),
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.addopc"), "",
						null);
				if (dialog.open() == Dialog.OK) {
					java.util.List<String> models = getEditorInput().getAppConfig().getServerInformationModels();
					models.add(dialog.getValue());
					list_informationmodel.add(dialog.getValue());
					list_informationmodel.deselectAll();
					list_informationmodel.select(models.size() - 1);
					btn_addInformationModel.setEnabled(true);
					setDirty(true);
				}
			}
		});
		this.btn_editInformationModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (initState)
					return;
				String[] selection = list_informationmodel.getSelection();
				if (selection != null && selection.length > 0) {
					InputDialog dialog = new InputDialog(getSite().getShell(),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"editor.form.editbaseaddaddress"),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"editor.form.editaddress"),
							selection[0], null);
					if (dialog.open() == 0) {
						java.util.List<String> models = getEditorInput().getAppConfig().getServerInformationModels();
						list_informationmodel.removeAll();
						String newValue = dialog.getValue();
						// replace
						if (models.contains(selection[0])) {
							int index = models.indexOf(selection[0]);
							models.set(index, newValue);
						}
						for (String endpoint : models) {
							list_informationmodel.add(endpoint);
						}
						btn_editInformationModel.setEnabled(false);
						setDirty(true);
					}
				}
			}
		});
		this.btn_removeInformationModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (initState)
					return;
				String[] selection = list_informationmodel.getSelection();
				if (selection != null && selection.length > 0) {
					MessageDialog dialog = new MessageDialog(getSite().getShell(),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"editor.form.deleteopcmodel"),
							null,
							CustomString
									.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.deleteopc"),
							MessageDialog.QUESTION,
							new String[] {
									CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
											"editor.form.yes"),
									CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
											"editor.form.no") },
							0);
					if (dialog.open() == 0) {
						java.util.List<String> informationModels = getEditorInput().getAppConfig()
								.getServerInformationModels();
						for (String models : selection) {
							informationModels.remove(models);
							list_informationmodel.remove(models);
						}
						btn_editInformationModel.setEnabled(false);
						setDirty(true);
					}
				}
			}
		});
		this.listViewer_InformationModel.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (selection == null) {
					btn_editInformationModel.setEnabled(false);
					return;
				}
				btn_editInformationModel.setEnabled(true);
			}
		});
		this.list_informationmodel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (initState)
					return;
				if (list_informationmodel.getSelection() != null && list_informationmodel.getSelection().length > 0) {
					String uri = list_informationmodel.getSelection()[0];
					InputDialog dialog = new InputDialog(getSite().getShell(),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"editor.form.editbaseaddress"),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"editor.form.editaddress"),
							uri, null);
					if (dialog.open() == Dialog.OK) {
						java.util.List<String> models = getEditorInput().getAppConfig().getServerInformationModels();
						list_informationmodel.removeAll();
						String newValue = dialog.getValue();
						// replace
						if (models.contains(uri)) {
							int index = models.indexOf(uri);
							models.set(index, newValue);
						}
						for (String informationModel : models) {
							list_informationmodel.add(informationModel);
						}
						btn_editInformationModel.setEnabled(false);
						setDirty(true);
					}
				}
			}
		});
		this.cbox_activeDatabase.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// set flag
				ApplicationConfiguration config = getEditorInput().getAppConfig();
				boolean active = ((Button) e.getSource()).getSelection();
				config.getHistoryConfiguration().setActive(active);
				// set dirty
				setDirty(true);
				// TODO: set enable widgets
			}
		});
		txtDatabase.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (initState)
					return;
				ApplicationConfiguration config = getEditorInput().getAppConfig();
				config.getHistoryConfiguration().setDatabase(txtDatabase.getText());
				setDirty(true);
			}
		});
		txtDBName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (initState)
					return;
				ApplicationConfiguration config = getEditorInput().getAppConfig();
				config.getHistoryConfiguration().setDatabaseName(txtDBName.getText());
				setDirty(true);
			}
		});
		txtUser.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (initState)
					return;
				ApplicationConfiguration config = getEditorInput().getAppConfig();
				config.getHistoryConfiguration().setUser(txtUser.getText());
				setDirty(true);
			}
		});
		txtPassword.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (initState)
					return;
				ApplicationConfiguration config = getEditorInput().getAppConfig();
				config.getHistoryConfiguration().setPw(txtPassword.getText());
				setDirty(true);
			}
		});
		cmb_historicalDriverType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String drvType = cmb_historicalDriverType.getText();
				ApplicationConfiguration config = getEditorInput().getAppConfig();
				config.getHistoryConfiguration().setDrvName(drvType);
				switch (drvType) {
				case "CSV":
					// invisible
					// Database is filesystem path to csv file
					txtDatabase.setText(DRIVER_URL_CSV);
					layoutDataExclude(true, lblDBName, lblUser, lblPassword, txtDBName, txtUser, txtPassword);
					computeSize();
					break;
				default:
					// visible
					// Database is URL + port for database
					switch (drvType) {
					case DRIVER_NAME_MYSQL:
						txtDatabase.setText(DRIVER_URL_MYSQL);
						break;
					case DRIVER_NAME_SQLITE:
						txtDatabase.setText(DRIVER_URL_SQLITE);
						break;
					case DRIVER_NAME_ORACLE:
						txtDatabase.setText(DRIVER_URL_ORACLE);
						break;
					case DRIVER_NAME_MSSQL:
						txtDatabase.setText(DRIVER_URL_MSSQL);
						break;
					case DRIVER_NAME_CLOUD:
						txtDatabase.setText(DRIVER_URL_CLOUD);
						break;
					}
					layoutDataExclude(false, lblDBName, lblUser, lblPassword, txtDBName, txtUser, txtPassword);
					computeSize();
					break;
				}
				setDirty(true);
			}
		});
	}

	private void layoutDataExclude(boolean exclude, Control... control) {
		for (Control c : control) {
			((GridData) c.getLayoutData()).exclude = exclude;
			c.setVisible(!exclude);
		}
	}

	private Point computeSection() {
		this.sctnApplication.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.sctnEndpoints.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.sctnTransportQuotas.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.sctnHistoricalConfig.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.sctnInformationModel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		return this.composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	private void createSections() {
		createSectionApplication();
		createSectionDevices();
		createSectionDrivers();
		createSectionModules();
		createSectionEndpoints();
		createSectionTransportQuotas();
		createSectionHistorical();
		createSectionInformationModel();

	}

	private void createSectionInformationModel() {
		sctnInformationModel = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnInformationModel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnInformationModel);
		sctnInformationModel.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.informationmodel"));
		sctnInformationModel.setExpanded(true);
		cmp_InformationModel = new Composite(sctnInformationModel, SWT.NONE);
		formToolkit.adapt(cmp_InformationModel);
		formToolkit.paintBordersFor(cmp_InformationModel);
		sctnInformationModel.setClient(cmp_InformationModel);
		cmp_InformationModel.setLayout(new GridLayout(1, false));
		Composite cmp_InfoModelButtonGroup = new Composite(cmp_InformationModel, SWT.NONE);
		formToolkit.adapt(cmp_InfoModelButtonGroup);
		formToolkit.paintBordersFor(cmp_InfoModelButtonGroup);
		cmp_InfoModelButtonGroup.setLayout(new GridLayout(3, false));
		btn_addInformationModel = new Button(cmp_InfoModelButtonGroup, SWT.NONE);
		GridData gd_btn_addAddress = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btn_addAddress.heightHint = 48;
		gd_btn_addAddress.widthHint = 48;
		btn_addInformationModel.setLayoutData(gd_btn_addAddress);
		formToolkit.adapt(btn_addInformationModel, true, true);
		// btn_addAddress.setText("Add");
		btn_addInformationModel.setImage(CommonImagesActivator.getDefault()
				.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.ADD));
		this.btn_editInformationModel = new Button(cmp_InfoModelButtonGroup, SWT.NONE);
		GridData gd_btn_editInformationModel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btn_editInformationModel.heightHint = 48;
		gd_btn_editInformationModel.widthHint = 48;
		btn_editInformationModel.setLayoutData(gd_btn_editInformationModel);
		formToolkit.adapt(btn_editInformationModel, true, true);
		// btn_addAddress.setText("Add");
		btn_editInformationModel.setImage(CommonImagesActivator.getDefault()
				.getRegisteredImage(CommonImagesActivator.IMG_32, CommonImagesActivator.EDIT));
		btn_editInformationModel.setEnabled(false);
		btn_removeInformationModel = new Button(cmp_InfoModelButtonGroup, SWT.NONE);
		GridData gd_btn_removeAddress = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btn_removeAddress.heightHint = 48;
		gd_btn_removeAddress.widthHint = 48;
		btn_removeInformationModel.setLayoutData(gd_btn_removeAddress);
		formToolkit.adapt(btn_removeInformationModel, true, true);
		btn_removeInformationModel.setImage(CommonImagesActivator.getDefault()
				.getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.DELETE));
		this.listViewer_InformationModel = new ListViewer(cmp_InformationModel, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		list_informationmodel = listViewer_InformationModel.getList();
		GridData gd_listInformationmodel = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_listInformationmodel.heightHint = 150;
		list_informationmodel.setLayoutData(gd_listInformationmodel);
	}

	private void createSectionHistorical() {
		sctnHistoricalConfig = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnHistoricalConfig.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnHistoricalConfig);
		sctnHistoricalConfig.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.historicalaccess"));
		sctnHistoricalConfig.setExpanded(true);
		Composite cmpHistory = formToolkit.createComposite(sctnHistoricalConfig, SWT.NONE);
		formToolkit.paintBordersFor(cmpHistory);
		sctnHistoricalConfig.setClient(cmpHistory);
		cmpHistory.setLayout(new GridLayout(2, false));
		Label lblActiveDatabase = new Label(cmpHistory, SWT.RIGHT);
		GridData gd_lblActiveDatabase = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblActiveDatabase.widthHint = 150;
		lblActiveDatabase.setLayoutData(gd_lblActiveDatabase);
		lblActiveDatabase
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"));
		lblActiveDatabase.setBounds(0, 0, 124, 15);
		formToolkit.adapt(lblActiveDatabase, true, true);
		this.cbox_activeDatabase = new Button(cmpHistory, SWT.CHECK);
		this.cbox_activeDatabase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(this.cbox_activeDatabase, true, true);
		
		
		lblDatabaseType = new Label(cmpHistory, SWT.RIGHT);
		GridData gd_lblDatabaseType = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblDatabaseType.widthHint = 150;
		lblDatabaseType.setLayoutData(gd_lblDatabaseType);
		lblDatabaseType
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.drivername"));
		lblDatabaseType.setBounds(0, 0, 124, 15);
		formToolkit.adapt(lblDatabaseType, true, true);
		cmb_historicalDriverType = new Combo(cmpHistory, SWT.BORDER);
		cmb_historicalDriverType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmb_historicalDriverType.setItems(DATABASES);
		cmb_historicalDriverType.setBounds(0, 0, 359, 21);
		formToolkit.adapt(cmb_historicalDriverType, true, true);
		cmb_historicalDriverType.select(0);
		
		
		lblDatabase = new Label(cmpHistory, SWT.RIGHT);
		lblDatabase.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDatabase
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.database"));
		lblDatabase.setBounds(0, 0, 124, 15);
		formToolkit.adapt(lblDatabase, true, true);
		txtDatabase = new Text(cmpHistory, SWT.BORDER);
		txtDatabase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtDatabase
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.hostname"));
		txtDatabase.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txtDatabase, true, true);
		// btnBrowseDB = new Button(cmpHistory, SWT.PUSH);
		// btnBrowseDB.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
		// false, 1, 1));
		// formToolkit.adapt(btnBrowseDB, true, true);
		lblDBName = new Label(cmpHistory, SWT.RIGHT);
		lblDBName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDBName.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.databasename"));
		lblDBName.setBounds(0, 0, 124, 15);
		formToolkit.adapt(lblDBName, true, true);
		txtDBName = new Text(cmpHistory, SWT.BORDER);
		txtDBName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		// txtDBName.setText(
		// CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
		// "editor.form.databasename"));
		txtDBName.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txtDBName, true, true);
		lblUser = new Label(cmpHistory, SWT.RIGHT);
		lblUser.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUser.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.user"));
		lblUser.setBounds(0, 0, 124, 15);
		formToolkit.adapt(lblUser, true, true);
		txtUser = new Text(cmpHistory, SWT.BORDER);
		txtUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtUser.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.user"));
		txtUser.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txtUser, true, true);
		lblPassword = new Label(cmpHistory, SWT.RIGHT);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.password"));
		lblPassword.setBounds(0, 0, 124, 15);
		formToolkit.adapt(lblPassword, true, true);
		txtPassword = new Text(cmpHistory, SWT.BORDER);
		txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtPassword
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.password"));
		txtPassword.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txtPassword, true, true);
	}

	private void createSectionTransportQuotas() {
		sctnTransportQuotas = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnTransportQuotas.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnTransportQuotas);
		sctnTransportQuotas.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.transportquotas"));
		sctnTransportQuotas.setExpanded(false);
		composite_4 = formToolkit.createComposite(sctnTransportQuotas, SWT.NONE);
		formToolkit.paintBordersFor(composite_4);
		sctnTransportQuotas.setClient(composite_4);
		composite_4.setLayout(new GridLayout(2, false));
		lbl_operationTimeout = new Label(composite_4, SWT.RIGHT);
		GridData gd_lbl_operationTimeout = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_operationTimeout.widthHint = 150;
		lbl_operationTimeout.setLayoutData(gd_lbl_operationTimeout);
		lbl_operationTimeout.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.operationtimeout"));
		lbl_operationTimeout.setBounds(0, 0, 135, 15);
		formToolkit.adapt(lbl_operationTimeout, true, true);
		txt_operationTimeout = new NumericText(composite_4, SWT.BORDER);
		txt_operationTimeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_operationTimeout.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_operationTimeout, true, true);
		lbl_maxStringLength = new Label(composite_4, SWT.RIGHT);
		lbl_maxStringLength.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_maxStringLength.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.maxstringlength"));
		lbl_maxStringLength.setBounds(0, 0, 93, 15);
		formToolkit.adapt(lbl_maxStringLength, true, true);
		txt_maxStringLength = new NumericText(composite_4, SWT.BORDER);
		txt_maxStringLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_maxStringLength.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_maxStringLength, true, true);
		lbl_maxByteString = new Label(composite_4, SWT.RIGHT);
		lbl_maxByteString.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_maxByteString.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.maxbytestringlength"));
		lbl_maxByteString.setBounds(0, 0, 116, 15);
		formToolkit.adapt(lbl_maxByteString, true, true);
		txt_maxByteStringLength = new NumericText(composite_4, SWT.BORDER);
		txt_maxByteStringLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_maxByteStringLength.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_maxByteStringLength, true, true);
		lbl_maxArrayLength = new Label(composite_4, SWT.RIGHT);
		lbl_maxArrayLength.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_maxArrayLength.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.maxarraylength"));
		lbl_maxArrayLength.setBounds(0, 0, 90, 15);
		formToolkit.adapt(lbl_maxArrayLength, true, true);
		txt_maxArrayLength = new NumericText(composite_4, SWT.BORDER);
		txt_maxArrayLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_maxArrayLength.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_maxArrayLength, true, true);
		lbl_maxMessageSize = new Label(composite_4, SWT.RIGHT);
		lbl_maxMessageSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_maxMessageSize.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.maxmessagesize"));
		lbl_maxMessageSize.setBounds(0, 0, 91, 15);
		formToolkit.adapt(lbl_maxMessageSize, true, true);
		txt_maxMessageSize = new NumericText(composite_4, SWT.BORDER);
		txt_maxMessageSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_maxMessageSize.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_maxMessageSize, true, true);
		lbl_maxBufferSize = new Label(composite_4, SWT.RIGHT);
		lbl_maxBufferSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_maxBufferSize.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.maxbuffersize"));
		lbl_maxBufferSize.setBounds(0, 0, 77, 15);
		formToolkit.adapt(lbl_maxBufferSize, true, true);
		txt_maxBufferSize = new NumericText(composite_4, SWT.BORDER);
		txt_maxBufferSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_maxBufferSize.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_maxBufferSize, true, true);
		lbl_channelLifetime = new Label(composite_4, SWT.RIGHT);
		lbl_channelLifetime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_channelLifetime.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.channellifetime"));
		lbl_channelLifetime.setBounds(0, 0, 90, 15);
		formToolkit.adapt(lbl_channelLifetime, true, true);
		txt_channelLifetime = new NumericText(composite_4, SWT.BORDER);
		txt_channelLifetime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_channelLifetime.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_channelLifetime, true, true);
		lbl_securityTokenLifetime = new Label(composite_4, SWT.RIGHT);
		lbl_securityTokenLifetime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_securityTokenLifetime.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"editor.form.securitytokenlifetime"));
		lbl_securityTokenLifetime.setBounds(0, 0, 121, 15);
		formToolkit.adapt(lbl_securityTokenLifetime, true, true);
		txt_securityTokenLifetime = new NumericText(composite_4, SWT.BORDER);
		txt_securityTokenLifetime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_securityTokenLifetime.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_securityTokenLifetime, true, true);
		lbl_maxSubscriptionCount = new Label(composite_4, SWT.RIGHT);
		lbl_maxSubscriptionCount.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_maxSubscriptionCount.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"editor.form.maxsubscriptioncount"));
		lbl_maxSubscriptionCount.setBounds(0, 0, 124, 15);
		formToolkit.adapt(lbl_maxSubscriptionCount, true, true);
		txt_maxSubscriptionCount = new NumericText(composite_4, SWT.BORDER);
		txt_maxSubscriptionCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txt_maxSubscriptionCount.setText("0");
		txt_maxSubscriptionCount.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_maxSubscriptionCount, true, true);
		lbl_publishResolution = new Label(composite_4, SWT.RIGHT);
		lbl_publishResolution.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_publishResolution.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.publishresolution"));
		lbl_publishResolution.setBounds(0, 0, 124, 15);
		formToolkit.adapt(lbl_publishResolution, true, true);
		txt_publishResolution = new NumericText(composite_4, SWT.BORDER);
		txt_publishResolution.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txt_publishResolution.setText("50");
		txt_publishResolution.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_publishResolution, true, true);
	}

	private void createSectionEndpoints() {
		sctnEndpoints = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnEndpoints.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnEndpoints);
		sctnEndpoints
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.endpoints"));
		sctnEndpoints.setExpanded(false);
		cmp_InformationModel = new Composite(sctnEndpoints, SWT.NONE);
		formToolkit.adapt(cmp_InformationModel);
		formToolkit.paintBordersFor(cmp_InformationModel);
		sctnEndpoints.setClient(cmp_InformationModel);
		cmp_InformationModel.setLayout(new GridLayout(1, false));
		Composite composite_3 = new Composite(cmp_InformationModel, SWT.NONE);
		formToolkit.adapt(composite_3);
		formToolkit.paintBordersFor(composite_3);
		composite_3.setLayout(new GridLayout(3, false));
		btn_addAddress = new Button(composite_3, SWT.NONE);
		GridData gd_btn_addAddress = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btn_addAddress.heightHint = 48;
		gd_btn_addAddress.widthHint = 48;
		btn_addAddress.setLayoutData(gd_btn_addAddress);
		formToolkit.adapt(btn_addAddress, true, true);
		// btn_addAddress.setText("Add");
		btn_addAddress.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.ADD));
		this.btn_editAddress = new Button(composite_3, SWT.NONE);
		GridData gd_btn_editAddress = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btn_editAddress.heightHint = 48;
		gd_btn_editAddress.widthHint = 48;
		btn_editAddress.setLayoutData(gd_btn_editAddress);
		formToolkit.adapt(btn_editAddress, true, true);
		// btn_addAddress.setText("Add");
		btn_editAddress.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_32,
				CommonImagesActivator.EDIT));
		btn_editAddress.setEnabled(false);
		btn_removeAddress = new Button(composite_3, SWT.NONE);
		GridData gd_btn_removeAddress = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btn_removeAddress.heightHint = 48;
		gd_btn_removeAddress.widthHint = 48;
		btn_removeAddress.setLayoutData(gd_btn_removeAddress);
		formToolkit.adapt(btn_removeAddress, true, true);
		btn_removeAddress.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.DELETE));
		this.listViewer_endpoints = new ListViewer(cmp_InformationModel, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		list_endpoints = listViewer_endpoints.getList();
		GridData gd_list = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_list.heightHint = 150;
		list_endpoints.setLayoutData(gd_list);
	}

	private void createSectionDevices() {
		sctnDevicesConfig = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnDevicesConfig.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnDevicesConfig);
		sctnDevicesConfig.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.deviceconfig"));
		sctnDevicesConfig.setExpanded(true);
		Composite compositeDevices = formToolkit.createComposite(sctnDevicesConfig, SWT.NONE);
		formToolkit.paintBordersFor(compositeDevices);
		sctnDevicesConfig.setClient(compositeDevices);
		
		compositeDevices.setLayout(new GridLayout(2, false));
		
		Label lblDevice = new Label(compositeDevices, SWT.RIGHT);
		GridData gd_lblDevices = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblDevices.widthHint = 150;
		lblDevice.setLayoutData(gd_lblDevices);
		lblDevice
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.devices"));
		lblDevice.setBounds(0, 0, 124, 15);
		formToolkit.adapt(lblDevice, true, true);
		Combo cmb_Devices = new Combo(compositeDevices, SWT.BORDER);
		cmb_Devices.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		setDevicesInput(cmb_Devices);
		cmb_Devices.setBounds(0, 0, 359, 21);
		formToolkit.adapt(cmb_Devices, true, true);
		cmb_Devices.select(0);
	}
	
	private void setDevicesInput(Combo cmbDevices) {
		boolean allowSimpleFilesystem = UserUtils.testUserRights(1);
		DevicePreferenceManager devicemanager = new DevicePreferenceManager();
		Preferences input = devicemanager.getRoot();
		try {
			String[] children = input.childrenNames();
			for (String child : children) {

				Preferences prefDevice = input.node(child);
				int filetype = prefDevice.getInt(DevicePreferenceConstants.PREFERENCE_DEVICE_FILETYPE, -1);
				if (!allowSimpleFilesystem && DevicePreferenceConstants.PREFERENCE_TYPE_SIMPLE == filetype) {
					continue;
				}

				cmbDevices.add(child);
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	private void createSectionDrivers() {
		sctnDriverConfig = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnDriverConfig.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnDriverConfig);
		sctnDriverConfig.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.driverconfig"));
		sctnDriverConfig.setExpanded(true);
		Composite compositeDrivers = formToolkit.createComposite(sctnDriverConfig, SWT.NONE);
		formToolkit.paintBordersFor(compositeDrivers);
		sctnDriverConfig.setClient(compositeDrivers);

		compositeDrivers.setLayout(new GridLayout(2, false));
		
		CheckboxTableViewer tableViewer = CheckboxTableViewer.newCheckList(compositeDrivers,
				SWT.BORDER_SOLID | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		tableViewer.setContentProvider(new DriverContentProvider());

		createDriverTableViewerColumns(tableViewer);
		
		tableViewer.setInput(getEditorInput().getNode().getFilesystem());
	    tableViewer.setAllChecked(true);
	}

	private void createSectionModules() {
		sctnModuleConfig = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnModuleConfig.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnModuleConfig);
		sctnModuleConfig.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.moduleconfig"));
		sctnModuleConfig.setExpanded(true);
		Composite compositeModules = formToolkit.createComposite(sctnModuleConfig, SWT.NONE);
		formToolkit.paintBordersFor(compositeModules);
		sctnModuleConfig.setClient(compositeModules);

		compositeModules.setLayout(new GridLayout(2, false));

		CheckboxTableViewer tableViewer = CheckboxTableViewer.newCheckList(compositeModules,
				SWT.BORDER_SOLID | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		tableViewer.setContentProvider(new ModuleContentProvider());

		createModuleTableViewerColumns(tableViewer);
		
		tableViewer.setInput(getEditorInput().getNode().getFilesystem());
	    tableViewer.setAllChecked(true);
	}

	private void createDriverTableViewerColumns(CheckboxTableViewer tableViewer) {
		TableViewerColumn tvcImg = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_1 = tvcImg.getColumn();
		tblclmnNewColumn_1.setAlignment(SWT.CENTER);
		tblclmnNewColumn_1.setWidth(50);
		tvcImg.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					return ((OPCUAServerDriverModelNode) element).getDriverImage();
				}
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				return "";
			}
		});
		TableViewerColumn tvcName = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tvcName.getColumn();
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.moduleselection.page.column.name"));
		tvcName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getForeground(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!readVersionFromTarget((OPCUAServerDriverModelNode) element)
							.contains(node.getDriverVersion())) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				}
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					return ((OPCUAServerDriverModelNode) element).getDriverName();
				}
				return "";
			}
		});
		TableViewerColumn tvcType = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnType = tvcType.getColumn();
		tblclmnType.setWidth(100);
		tblclmnType.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.moduleselection.page.column.type"));
		tvcType.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getForeground(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!readVersionFromTarget((OPCUAServerDriverModelNode) element)
							.contains(node.getDriverVersion())) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				}
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					return ((OPCUAServerDriverModelNode) element).getDriverType();
				}
				return "";
			}
		});
		TableViewerColumn tvcVersion = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnVersoin = tvcVersion.getColumn();
		tblclmnVersoin.setWidth(100);
		tblclmnVersoin.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.moduleselection.page.column.version"));
		tvcVersion.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getForeground(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!readVersionFromTarget((OPCUAServerDriverModelNode) element)
							.contains(node.getDriverVersion())) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				}
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					return ((OPCUAServerDriverModelNode) element).getDriverVersion();
				}
				return "";
			}
		});
		TableViewerColumn tvcTargetVersion = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTargetVersion = tvcTargetVersion.getColumn();
		tblclmnTargetVersion.setWidth(100);
		tblclmnTargetVersion.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.moduleselection.page.column.targetversion"));
		tvcTargetVersion.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					return readVersionFromTarget((OPCUAServerDriverModelNode) element);
				}
				return "";
			}
		});
		TableViewerColumn tvcInfo = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnInfo = tvcInfo.getColumn();
		tblclmnInfo.setWidth(100);
		tblclmnInfo.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.moduleselection.page.column.info"));
		tvcInfo.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					String targetversion = readVersionFromTarget((OPCUAServerDriverModelNode) element);
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!targetversion.contains(node.getDriverVersion())) {
						// de-select and de activate row
						tableViewer.setChecked(element, false);
						return "module is not compatible with runtime";
					}
				}
				return "";
			}
		});
		tableViewer.setCheckStateProvider(new ICheckStateProvider() {
			@Override
			public boolean isGrayed(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!readVersionFromTarget((OPCUAServerDriverModelNode) element)
							.contains(node.getDriverVersion())) {
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean isChecked(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!readVersionFromTarget((OPCUAServerDriverModelNode) element)
							.contains(node.getDriverVersion())) {
						return false;
					}
				}
				return false;
			}
		});
		tableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {

				// validate();

				if (event.getElement() instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) event.getElement();
					if (!validateDriverVersion(node)) {
						Logger.getLogger(getClass().getName()).log(Level.WARNING,
								"Module {0} is not compatible with runtime!", node.getDriverName());
						// tableViewer.
						for (TableItem item : tableViewer.getTable().getItems()) {
							if (item.getChecked()) {
								if (!validateDriverVersion((OPCUAServerDriverModelNode) item.getData())) {
									item.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
								} else
									item.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							} else {
								item.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							}
						}
					}
				}
			}
		});
	}

	
	private void createModuleTableViewerColumns(CheckboxTableViewer tableViewer) {
		TableViewerColumn tvcImg = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_1 = tvcImg.getColumn();
		tblclmnNewColumn_1.setAlignment(SWT.CENTER);
		tblclmnNewColumn_1.setWidth(50);
		tvcImg.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				if (element instanceof OPCUAServerModuleModelNode) {
					return ((OPCUAServerModuleModelNode) element).getModuleImage();
				}
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				return "";
			}
		});
		TableViewerColumn tvcName = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tvcName.getColumn();
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.moduleselection.page.column.name"));
		tvcName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getForeground(Object element) {
				if (element instanceof OPCUAServerModuleModelNode) {
					OPCUAServerModuleModelNode node = (OPCUAServerModuleModelNode) element;
					if (!readVersionFromTarget((OPCUAServerModuleModelNode) element)
							.contains(node.getModuleVersion())) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				}
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerModuleModelNode) {
					return ((OPCUAServerModuleModelNode) element).getModuleName();
				}
				return "";
			}
		});
		TableViewerColumn tvcType = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnType = tvcType.getColumn();
		tblclmnType.setWidth(100);
		tblclmnType.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.moduleselection.page.column.type"));
		tvcType.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getForeground(Object element) {
				if (element instanceof OPCUAServerModuleModelNode) {
					OPCUAServerModuleModelNode node = (OPCUAServerModuleModelNode) element;
					if (!readVersionFromTarget((OPCUAServerModuleModelNode) element)
							.contains(node.getModuleVersion())) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				}
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerModuleModelNode) {
					return ((OPCUAServerModuleModelNode) element).getModuleType();
				}
				return "";
			}
		});
		TableViewerColumn tvcVersion = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnVersoin = tvcVersion.getColumn();
		tblclmnVersoin.setWidth(100);
		tblclmnVersoin.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.moduleselection.page.column.version"));
		tvcVersion.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getForeground(Object element) {
				if (element instanceof OPCUAServerModuleModelNode) {
					OPCUAServerModuleModelNode node = (OPCUAServerModuleModelNode) element;
					if (!readVersionFromTarget((OPCUAServerModuleModelNode) element)
							.contains(node.getModuleVersion())) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				}
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerModuleModelNode) {
					return ((OPCUAServerModuleModelNode) element).getModuleVersion();
				}
				return "";
			}
		});
		TableViewerColumn tvcTargetVersion = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTargetVersion = tvcTargetVersion.getColumn();
		tblclmnTargetVersion.setWidth(100);
		tblclmnTargetVersion.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.moduleselection.page.column.targetversion"));
		tvcTargetVersion.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerModuleModelNode) {
					return readVersionFromTarget((OPCUAServerModuleModelNode) element);
				}
				return "";
			}
		});
		TableViewerColumn tvcInfo = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnInfo = tvcInfo.getColumn();
		tblclmnInfo.setWidth(100);
		tblclmnInfo.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.moduleselection.page.column.info"));
		tvcInfo.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerModuleModelNode) {
					String targetversion = readVersionFromTarget((OPCUAServerModuleModelNode) element);
					OPCUAServerModuleModelNode node = (OPCUAServerModuleModelNode) element;
					if (!targetversion.contains(node.getModuleVersion())) {
						// de-select and de activate row
						tableViewer.setChecked(element, false);
						return "module is not compatible with runtime";
					}
				}
				return "";
			}
		});
		tableViewer.setCheckStateProvider(new ICheckStateProvider() {
			@Override
			public boolean isGrayed(Object element) {
				if (element instanceof OPCUAServerModuleModelNode) {
					OPCUAServerModuleModelNode node = (OPCUAServerModuleModelNode) element;
					if (!readVersionFromTarget((OPCUAServerModuleModelNode) element)
							.contains(node.getModuleVersion())) {
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean isChecked(Object element) {
				if (element instanceof OPCUAServerModuleModelNode) {
					OPCUAServerModuleModelNode node = (OPCUAServerModuleModelNode) element;
					if (!readVersionFromTarget((OPCUAServerModuleModelNode) element)
							.contains(node.getModuleVersion())) {
						return false;
					}
				}
				return false;
			}
		});
		tableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {

				// validate();

				if (event.getElement() instanceof OPCUAServerModuleModelNode) {
					OPCUAServerModuleModelNode node = (OPCUAServerModuleModelNode) event.getElement();
					if (!validateModuleVersion(node)) {
						Logger.getLogger(getClass().getName()).log(Level.WARNING,
								"Module {0} is not compatible with runtime!", node.getModuleName());
						// tableViewer.
						for (TableItem item : tableViewer.getTable().getItems()) {
							if (item.getChecked()) {
								if (!validateModuleVersion((OPCUAServerModuleModelNode) item.getData())) {
									item.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
								} else
									item.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							} else {
								item.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							}
						}
					}
				}
			}
		});
	}

	private boolean validateDriverVersion(OPCUAServerDriverModelNode node) {
		String versions = readVersionFromTarget(node);
		return versions.contains(node.getDriverVersion());
	}
	
	private boolean validateModuleVersion(OPCUAServerModuleModelNode node) {
		String versions = readVersionFromTarget(node);
		return versions.contains(node.getModuleVersion());
	}

	private String readVersionFromTarget(OPCUAServerModuleModelNode node) {
//	    // only validate if we are on previous page
//	    if (modules == null)
//	    {
//	      modules = new HashMap<>();
//	      String mods = this.device.getRootPath() + this.device.getTargetFileSeparator() + "runtime"
//	          + this.device.getTargetFileSeparator() + "modules" + this.device.getTargetFileSeparator(); // +
//	                                                                                                     // node.getDriverType()
//	      // + this.device.getTargetFileSeparator();
//	      StringBuilder version = new StringBuilder();
//	      try
//	      {
//	        if (!this.device.connect())
//	        {
//	          version.append("no connection");
//	        }
//	        else
//	        {
//	          String[] types = this.device.listDirs(mods);
//	          for (String type : types)
//	          {
//	            version = new StringBuilder();
//	            if (type.compareTo(".") == 0 || type.compareTo("..") == 0)
//	              continue;
//	            String[] versions = this.device.listDirs(mods + type);
//	            String deliminator = "";
//	            if (versions == null)
//	            {
//	              version.append("????");
//	            }
//	            else
//	            {
//	              for (String vers : versions)
//	              {
//	                if (vers.compareTo(".") == 0 || vers.compareTo("..") == 0)
//	                  continue;
//	                version.append(deliminator + vers);
//	                deliminator = " | ";
//	              }
//	            }
//	            modules.put(type, version.toString());
//	          }
//	          this.device.disconnect();
//	        }
//	      }
//	      catch (IOException e)
//	      {
//	        return "0";
//	      }
//	    }
//	    if(!modules.containsKey(node.getModuleType()))
		return "????";
//	    return modules.get(node.getModuleType());
	}

	private String readVersionFromTarget(OPCUAServerDriverModelNode node) {
//	    // only validate if we are on previous page
//	    if (modules == null)
//	    {
//	      modules = new HashMap<>();
//	      String mods = this.device.getRootPath() + this.device.getTargetFileSeparator() + "runtime"
//	          + this.device.getTargetFileSeparator() + "modules" + this.device.getTargetFileSeparator(); // +
//	                                                                                                     // node.getDriverType()
//	      // + this.device.getTargetFileSeparator();
//	      StringBuilder version = new StringBuilder();
//	      try
//	      {
//	        if (!this.device.connect())
//	        {
//	          version.append("no connection");
//	        }
//	        else
//	        {
//	          String[] types = this.device.listDirs(mods);
//	          for (String type : types)
//	          {
//	            version = new StringBuilder();
//	            if (type.compareTo(".") == 0 || type.compareTo("..") == 0)
//	              continue;
//	            String[] versions = this.device.listDirs(mods + type);
//	            String deliminator = "";
//	            if (versions == null)
//	            {
//	              version.append("????");
//	            }
//	            else
//	            {
//	              for (String vers : versions)
//	              {
//	                if (vers.compareTo(".") == 0 || vers.compareTo("..") == 0)
//	                  continue;
//	                version.append(deliminator + vers);
//	                deliminator = " | ";
//	              }
//	            }
//	            modules.put(type, version.toString());
//	          }
//	          this.device.disconnect();
//	        }
//	      }
//	      catch (IOException e)
//	      {
//	        return "0";
//	      }
//	    }
//	    if(!modules.containsKey(node.getModuleType()))
		return "????";
//	    return modules.get(node.getModuleType());
	}
	
	class ModuleContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IFileSystem) {
				return OPCModuleUtil.getAllModuleNamesFromOPCProject((IFileSystem)
				 inputElement, getEditorInput().getNode().getParent());
			}
			return new String[0];
		}
	}
	
	class DriverContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IFileSystem) {
				return OPCDriverUtil.getAllDriverNamesFromOPCProject((IFileSystem)
				 inputElement, getEditorInput().getNode().getParent());
			}
			return new String[0];
		}
	}
	
	class DeviceContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IFileSystem) {
				return OPCDriverUtil.getAllDriverNamesFromOPCProject((IFileSystem)
				 inputElement, getEditorInput().getNode().getParent());
			}
			return new String[0];
		}
	}

	/**
	 * Post saving the server configuration. Application Uri is NamespaceIndex 1!
	 */
	private void postSave() {
		String applicationUri = getEditorInput().getAppConfig().getApplicationUri();
		// sets the new namespace uri (used for designer)
		NamespaceTable namespaceTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		String serverUri = namespaceTable.getUri(1);
		if (serverUri != null && !serverUri.equals(applicationUri)) {
			namespaceTable.remove(1);
			namespaceTable.add(1, applicationUri);
			ModelBrowserView view = (ModelBrowserView) getSite().getPage().findView(ModelBrowserView.ID);
			view.setDirty(true);
		}
		/** refresh gui */
		OPCUAServerConfigModelNode node = getEditorInput().getNode();
		StudioModelNode parent = node.getParent();
		java.util.List<StudioModelNode> children = parent.getChildrenList();
		OPCNavigationView navigation = (OPCNavigationView) getSite().getPage().findView(OPCNavigationView.ID);
		if (navigation != null) {
			for (StudioModelNode child : children) {
				if (child instanceof OPCUAServerInfoModelsNode) {
					navigation.refresh(child);
					break;
				}
			}
		}
	}

	private void createSectionApplication() {
		sctnApplication = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnApplication.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnApplication);
		sctnApplication.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.application"));
		sctnApplication.setExpanded(true);
		composite_1 = formToolkit.createComposite(sctnApplication, SWT.NONE);
		formToolkit.paintBordersFor(composite_1);
		sctnApplication.setClient(composite_1);
		composite_1.setLayout(new GridLayout(2, false));
		label_2 = new Label(composite_1, SWT.RIGHT);
		label_2.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.applicationname"));
		label_2.setBounds(0, 0, 135, 15);
		formToolkit.adapt(label_2, true, true);
		txt_applicationName = new Text(composite_1, SWT.BORDER);
		txt_applicationName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_applicationName.setEditable(true);
		txt_applicationName.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_applicationName, true, true);
		label_3 = new Label(composite_1, SWT.NONE);
		label_3.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.applicationuri"));
		label_3.setBounds(0, 0, 79, 15);
		formToolkit.adapt(label_3, true, true);
		txt_appuri = new Text(composite_1, SWT.BORDER);
		txt_appuri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txt_appuri.setEditable(true);
		txt_appuri.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_appuri, true, true);
		label_4 = new Label(composite_1, SWT.NONE);
		label_4.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.producturi"));
		label_4.setBounds(0, 0, 60, 15);
		formToolkit.adapt(label_4, true, true);
		txt_producturi = new Text(composite_1, SWT.BORDER);
		txt_producturi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_producturi.setEditable(true);
		txt_producturi.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_producturi, true, true);
		label_5 = new Label(composite_1, SWT.NONE);
		label_5.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.applicationtype"));
		label_5.setBounds(0, 0, 93, 15);
		formToolkit.adapt(label_5, true, true);
		txt_apptype = new Text(composite_1, SWT.BORDER);
		txt_apptype.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_apptype.setEditable(false);
		txt_apptype.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_apptype, true, true);
		label_6 = new Label(composite_1, SWT.NONE);
		label_6.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.certvalidity"));
		label_6.setBounds(0, 0, 60, 15);
		formToolkit.adapt(label_6, true, true);
		txt_certvalidity = new Text(composite_1, SWT.BORDER);
		txt_certvalidity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_certvalidity.setEditable(true);
		txt_certvalidity.setBounds(0, 0, 359, 21);
		formToolkit.adapt(txt_certvalidity, true, true);
	}
}
