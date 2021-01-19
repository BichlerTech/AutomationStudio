package com.bichler.astudio.opcua.components.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.wb.swt.SWTResourceManager;

// TODO: Auto-generated Javadoc
/**
 * The Class InputDialog.
 */
public class OPCUANodeMonitoringDialog extends Dialog {

	public Tree getTreeview() {
		return treeview;
	}

	public void setTreeview(Tree treeview) {
		this.treeview = treeview;
	}

	public Text getText_Server() {
		return text_Server;
	}

	public void setText_Server(Text text_Server) {
		this.text_Server = text_Server;
	}

	public Text getText_BrowsePath() {
		return text_BrowsePath;
	}

	public void setText_BrowsePath(Text text_BrowsePath) {
		this.text_BrowsePath = text_BrowsePath;
	}

	public Text getText_Namespace() {
		return text_Namespace;
	}

	public void setText_Namespace(Text text_Namespace) {
		this.text_Namespace = text_Namespace;
	}

	public Text getText_Id() {
		return text_Id;
	}

	public void setText_Id(Text text_Id) {
		this.text_Id = text_Id;
	}

	public Text getText_Interval() {
		return text_Interval;
	}

	public void setText_Interval(Text text_Interval) {
		this.text_Interval = text_Interval;
	}

	public Combo getCombo_DisOldest() {
		return combo_DisOldest;
	}

	public void setCombo_DisOldest(Combo combo_DisOldest) {
		this.combo_DisOldest = combo_DisOldest;
	}

	public Text getText_QSize() {
		return text_QSize;
	}

	public void setText_QSize(Text text_QSize) {
		this.text_QSize = text_QSize;
	}

	public Combo getCombo_Filter() {
		return combo_Filter;
	}

	public void setCombo_Filter(Combo combo_Filter) {
		this.combo_Filter = combo_Filter;
	}

	public Combo getCombo_DeadBandT() {
		return combo_DeadBandT;
	}

	public void setCombo_DeadBandT(Combo combo_DeadBandT) {
		this.combo_DeadBandT = combo_DeadBandT;
	}

	public Text getText_DeadBandV() {
		return text_DeadBandV;
	}

	public void setText_DeadBandV(Text text_DeadBandV) {
		this.text_DeadBandV = text_DeadBandV;
	}

	public Text getText_Trigger() {
		return text_Trigger;
	}

	public void setText_Trigger(Text text_Trigger) {
		this.text_Trigger = text_Trigger;
	}

	public Text getText_IndexR() {
		return text_IndexR;
	}

	public void setText_IndexR(Text text_IndexR) {
		this.text_IndexR = text_IndexR;
	}

	public Combo getCombo_Dataenc() {
		return combo_Dataenc;
	}

	public void setCombo_Dataenc(Combo combo_Dataenc) {
		this.combo_Dataenc = combo_Dataenc;
	}

	public Combo getCombo_TimeStampRet() {
		return combo_TimeStampRet;
	}

	public void setCombo_TimeStampRet(Combo combo_TimeStampRet) {
		this.combo_TimeStampRet = combo_TimeStampRet;
	}

	public String[] getDiscoItems() {
		return discoItems;
	}

	public void setDiscoItems(String[] discoItems) {
		this.discoItems = discoItems;
	}

	public String[] getFilterItems() {
		return filterItems;
	}

	public void setFilterItems(String[] filterItems) {
		this.filterItems = filterItems;
	}

	public String[] getTimesttoretItems() {
		return timesttoretItems;
	}

	public void setTimesttoretItems(String[] timesttoretItems) {
		this.timesttoretItems = timesttoretItems;
	}

	public String[] getDataEncItems() {
		return dataEncItems;
	}

	public void setDataEncItems(String[] dataEncItems) {
		this.dataEncItems = dataEncItems;
	}

	public String[] getDeatbandItems() {
		return deatbandItems;
	}

	public void setDeatbandItems(String[] deatbandItems) {
		this.deatbandItems = deatbandItems;
	}

	public void setParent(Shell parent) {
		this.parent = parent;
	}

	/** The message. */
	private String message = "";

	/** The input. */
	private OPCUANodeField input = new OPCUANodeField();

	/** The parent. */
	private Shell parent = null;

	/** The treeview. */
	private Tree treeview = null;

	/** The text_ driver. */
	private Text text_Server = null;

	/** The text_ datenpunkt. */
	private Text text_BrowsePath = null;
	
	/** The text_ nodeid. */
	private Text text_Namespace = null;
	
	/** The text_ nodeid. */
	private Text text_Id = null;

	/** The text_ nodeid. */
	private Text text_Interval = null;
	
	/** The text_ nodeid. */
	private Combo combo_DisOldest = null;
	
	/** The text_ nodeid. */
	private Text text_QSize;
	
	/** The text_ nodeid. */
	private Combo combo_Filter;
	
	/** The text_ nodeid. */
	private Combo combo_DeadBandT;
	
	/** The text_ nodeid. */
	private Text text_DeadBandV;
	
	/** The text_ nodeid. */
	private Text text_Trigger;
	
	/** The text_ nodeid. */
	private Text text_IndexR;
	
	/** The text_ nodeid. */
	private Combo combo_Dataenc;
	
	/** The text_ nodeid. */
	private Combo combo_TimeStampRet;

	/** The items of the Combobox discard oldest. */
	private String[] discoItems = { "true", "false" };
	
	/** The items of the Combobox filter oldest. */
	private String[] filterItems = { "true", "false" };
	
	/** The items of the Combobox filter oldest. */
	private String[] timesttoretItems = { "Source", "Server", "Both", "Neither"};
	
	/** The items of the Combobox filter oldest. */
	private String[] dataEncItems = { "BINARY", "XML" };
	
	/** The items of the Combobox filter oldest. */
	private String[] deatbandItems = { "None", "Absolute", "Percent"};

	private GridData data;
	
	/**
	 * InputDialog constructor.
	 * 
	 * @param parent
	 *            the parent
	 * @param df
	 *            the df
	 */
	public OPCUANodeMonitoringDialog(Shell parent, OPCUANodeField df) {
		// Pass the default styles here
		this(parent);
		this.input = df;
		this.parent = parent;
	}

	/**
	 * InputDialog constructor.
	 * 
	 * @param parent
	 *            the parent
	 * @param style
	 *            the style
	 * @wbp.parser.constructor
	 */
	public OPCUANodeMonitoringDialog(Shell parent) {
		// Let users override the default styles
		super(parent);
		setMessage("ENTER_VALUE");
	}

	/**
	 * Gets the message.
	 * 
	 * @return String
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the input.
	 * 
	 * @return String
	 */
	public OPCUANodeField getInput() {
		return input;
	}

	/**
	 * Sets the input.
	 * 
	 * @param input
	 *            the new input
	 */
	public void setInput(OPCUANodeField input) {
		this.input = input;
	}

	/**
	 * Creates the dialog's contents.
	 * 
	 * @param shell
	 *            the dialog window
	 */
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(3, true));

		Label label_Server = new Label(container, SWT.NONE);
		label_Server.setText("OPC UA Server: ");

		// Display the input box
		text_Server = new Text(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		text_Server.setLayoutData(data);

		Label label_DP = new Label(container, SWT.NONE);
		label_DP.setText("Datenpunkt: ");
		
		// Display the input box
		text_BrowsePath = new Text(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		text_BrowsePath.setLayoutData(data);
	//	text_BrowsePath.setText(this.browsePath);
		text_BrowsePath.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));

		
		Label label_Namespace = new Label(container, SWT.NONE);
		label_Namespace.setText("Namespace: ");
		
		// Display the input box
		text_Namespace = new Text(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		text_Namespace.setLayoutData(data);
		
		Label label_NodeId = new Label(container, SWT.NONE);
		label_NodeId.setText("Id: ");

		// Display the input box
		text_Id = new Text(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		text_Id.setLayoutData(data);
		
		Label label_Interval = new Label(container, SWT.NONE);
		label_Interval.setText("Interval: ");

		// Display the input box
		text_Interval = new Text(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		text_Interval.setLayoutData(data);
		
		Label label_DisOldest = new Label(container, SWT.NONE);
		label_DisOldest.setText("Discard oldest: ");

		// Display the input box
		combo_DisOldest = new Combo(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		combo_DisOldest.setItems(this.discoItems);
		combo_DisOldest.setLayoutData(data);
		
		Label label_QSize = new Label(container, SWT.NONE);
		label_QSize.setText("Queue size: ");

		// Display the input box
		text_QSize = new Text(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		text_QSize.setLayoutData(data);
		
		Label label_Filter = new Label(container, SWT.NONE);
		label_Filter.setText("Filter: ");

		// Display the input box
		combo_Filter = new Combo(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		combo_Filter.setItems(this.filterItems);
		combo_Filter.setLayoutData(data);
		
		Label label_DeadBandT = new Label(container, SWT.NONE);
		label_DeadBandT.setText("Deadband type: ");

		// Display the input box
		combo_DeadBandT = new Combo(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		combo_DeadBandT.setItems(this.deatbandItems);
		combo_DeadBandT.setLayoutData(data);
		
		Label label_DeadBandV = new Label(container, SWT.NONE);
		label_DeadBandV.setText("Deadband value: ");

		// Display the input box
		text_DeadBandV = new Text(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		text_DeadBandV.setLayoutData(data);
		
		Label label_Trigger = new Label(container, SWT.NONE);
		label_Trigger.setText("Trigger: ");

		// Display the input box
		text_Trigger = new Text(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		text_Trigger.setLayoutData(data);
		
		Label label_IndexR = new Label(container, SWT.NONE);
		label_IndexR.setText("Index Range: ");

		// Display the input box
		text_IndexR = new Text(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		text_IndexR.setLayoutData(data);
		
		Label label_Dataenc = new Label(container, SWT.NONE);
		label_Dataenc.setText("Dataencoding: ");

		// Display the input box
		combo_Dataenc = new Combo(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		combo_Dataenc.setItems(this.dataEncItems);
		combo_Dataenc.setLayoutData(data);
		
		Label label_TimeStampRet = new Label(container, SWT.NONE);
		label_TimeStampRet.setText("Timestamp to return: ");

		// Display the input box
		combo_TimeStampRet = new Combo(container, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		combo_TimeStampRet.setItems(this.timesttoretItems);
		combo_TimeStampRet.setLayoutData(data);
		
		// load tags from files
		Button btn_loadTag = new Button(container, SWT.PUSH);
		btn_loadTag.setText("Load Datenpunkt...");
		btn_loadTag.setData(data);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		btn_loadTag.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				OPCUATreeViewDialog inputDialog = new OPCUATreeViewDialog(getShell());
				
				// new driverfield
				OPCUANodeField field = inputDialog.open();
				if(field != null) {
					text_BrowsePath.setText(field.getBrowsepath());
					text_Id.setText(field.getId());
					text_Namespace.setText(field.getNamespace());
					text_Server.setText(field.getServer());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		// Set the OK button as the default, so
		// user can type input and press Enter
		// to dismiss
		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Dialog#getParent()
	 */
	public Shell getParent() {
		return this.parent;
	}
}