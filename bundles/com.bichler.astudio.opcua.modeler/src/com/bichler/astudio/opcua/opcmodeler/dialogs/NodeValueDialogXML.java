package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridEditor;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.DateTimeStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.LocalizedTextStringConverter;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;

public class NodeValueDialogXML extends Dialog {
	// private TableViewer tableView = null;
	private ValueRanks valueRank = ValueRanks.Any;
	private UnsignedInteger[] arrayDim = null;
	private UnsignedInteger[] dimensions = null;
	private DataValue value = null;
	private BuiltinType valueType = BuiltinType.Null;
	private Button changeDim = null;
	private Grid grid = null;
	private GridEditor editor = null;
	private Control newEditor = null;
	private DesignerFormToolkit controllCreationToolkit = null;
	private GridItem currentItem;
	private NodeId datatypeId;
	private DataValue newValue = null;

	public NodeValueDialogXML(Shell parentShell, ValueRanks valueRank, UnsignedInteger[] arrayDim,
			BuiltinType valueType, DataValue value, NodeId datatypeId) {
		super(parentShell);
		this.valueRank = valueRank;
		this.arrayDim = arrayDim;
		this.valueType = valueType;
		this.value = value;
		this.controllCreationToolkit = new DesignerFormToolkit();
		this.datatypeId = datatypeId;
	}

	private LocalizedText[] getEnumStrings() {
		BrowseDescription nodesToBrowse = new BrowseDescription();
		nodesToBrowse.setBrowseDirection(BrowseDirection.Forward);
		nodesToBrowse.setIncludeSubtypes(true);
		nodesToBrowse.setNodeClassMask(NodeClass.getMask(NodeClass.ALL));
		nodesToBrowse.setNodeId(datatypeId);
		nodesToBrowse.setReferenceTypeId(Identifiers.HierarchicalReferences);
		nodesToBrowse.setResultMask(BrowseResultMask.getMask(BrowseResultMask.ALL));
		LocalizedText[] txt = new LocalizedText[0];
		try {
			BrowseResult[] result = ServerInstance.getInstance().getServerInstance().getMaster()
					.browse(new BrowseDescription[] { nodesToBrowse }, UnsignedInteger.ZERO, null, null);
			if (result != null && result.length > 0 && result[0].getReferences() != null) {
				for (ReferenceDescription refDesc : result[0].getReferences()) {
					ExpandedNodeId nodeId = refDesc.getNodeId();
					UAVariableNode valueNode = (UAVariableNode) ServerInstance.getNode(nodeId);
					txt = (LocalizedText[]) valueNode.getValue().getValue();
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return txt;
	}

	/**
	 * Configures the Shell.
	 * 
	 * @param NewShell Shell to configure.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.nodevalue"));
	}

	//
	// @Override
	// protected void initializeBounds() {
	// // TODO Auto-generated method stub
	// super.initializeBounds();
	// }
	@Override
	protected Point getInitialSize() {
		Point initSize = super.getInitialSize();
		// x863
		// y930
		if (initSize.x > 863) {
			initSize.x = 863;
		}
		if (initSize.y > 930) {
			initSize.y = 930;
		}
		return initSize;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		// ScrolledComposite composite = new ScrolledComposite(c, SWT.H_SCROLL |
		// SWT.V_SCROLL);
		// composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
		// false));
		composite.setLayout(new GridLayout(2, false));
		String type = this.valueType.name();
		this.controllCreationToolkit.createLabel(composite,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.valuetype") + " " + type);
		this.changeDim = this.controllCreationToolkit.createButtonPush(composite, "change ArrayDimensions");
		GridDataFactory.fillDefaults().applyTo(changeDim);
		this.changeDim.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				UnsignedInteger[] newDimensions = null;
				switch (valueRank) {
				case OneOrMoreDimensions:
					ArrayDimensionDialog oneOreMoreDimensionDialog = new ArrayDimensionDialog(getParentShell(),
							dimensions, valueRank);
					if (oneOreMoreDimensionDialog.open() == OK) {
						newDimensions = oneOreMoreDimensionDialog.getArrayDimensions();
					}
					break;
				default:
					ArrayDimensionAttributeDialog dialog = new ArrayDimensionAttributeDialog(getParentShell(),
							valueRank, arrayDim, false);
					int ok = dialog.open();
					if (ok == Dialog.OK) {
						newDimensions = dialog.getArrayDimension();
					}
					break;
				}
				if (newEditor != null) {
					newEditor.dispose();
				}
				if (newDimensions != null) {
					if (valueType == BuiltinType.Boolean) {
						expandBooleanTable(newDimensions);
					} else {
						expandEditTable(newDimensions);
					}
				}
				// now we have to update the
			}
		});
		if (this.value != null) {
			if (this.value.getValue() != null || this.value.getValue().getValue() != null) {
				switch (valueType) {
				case Null:
					break;
				case Boolean:
					this.expandBooleanTable(this.arrayDim);
					break;
				default:
					this.expandEditTable(this.arrayDim);
					break;
				}
			}
		}
		return composite;
	}

	public DataValue getNewValue() {
		return this.newValue;
	}

	@Override
	protected void okPressed() {
		int rows = 0;
		int columns = 0;
		this.newValue = new DataValue();
		switch (this.valueType) {
		case Null: // NULL
			this.newValue.setValue(Variant.NULL);
			break;
		case Boolean: // BOOLEAN
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				Boolean[][] bool = new Boolean[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						bool[i][j] = grid.getItem(i).getChecked(j);
					}
				}
				this.newValue.setValue(new Variant(bool));
				break;
			} else {
				Boolean[] bool = new Boolean[rows];
				for (int i = 0; i < rows; i++) {
					bool[i] = grid.getItem(i).getChecked();
				}
				this.newValue.setValue(new Variant(bool));
			}
			break;
		case SByte: // SBYTE
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				Byte[][] sbyte_ = new Byte[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							sbyte_[i][j] = new Byte(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							sbyte_[i][j] = 0;
						}
					}
				}
				this.newValue.setValue(new Variant(sbyte_));
				break;
			} else {
				Byte[] sbyte_ = new Byte[rows];
				for (int i = 0; i < rows; i++) {
					sbyte_[i] = new Byte(grid.getItem(i).getText());
				}
				this.newValue.setValue(new Variant(sbyte_));
			}
			break;
		case Byte: // BYTE
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				UnsignedByte[][] byte_ = new UnsignedByte[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							byte_[i][j] = new UnsignedByte(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							byte_[i][j] = new UnsignedByte(0);
						}
					}
				}
				this.newValue.setValue(new Variant(byte_));
				break;
			} else {
				UnsignedByte[] byte_ = new UnsignedByte[rows];
				for (int i = 0; i < rows; i++) {
					byte_[i] = new UnsignedByte(grid.getItem(i).getText());
				}
				this.newValue.setValue(new Variant(byte_));
			}
			break;
		case ByteString:
			rows = grid.getItemCount();
			if (this.valueRank == ValueRanks.OneDimension) {
				columns = grid.getColumnCount();
				byte[][] byte_ = new byte[rows][columns];
				for (int i = 0; i < rows; i++) {
					byte_[i] = grid.getItem(i).getText(0).getBytes();
				}
				this.newValue.setValue(new Variant(byte_));
				break;
			} else if (this.valueRank == ValueRanks.Scalar) {
				byte[] byte_ = grid.getItem(0).getText().getBytes();
				this.newValue.setValue(new Variant(byte_));
			}
			break;
		case Int16: // int16
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				Short[][] int16 = new Short[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							int16[i][j] = new Short(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							int16[i][j] = 0;
						}
					}
				}
				this.newValue.setValue(new Variant(int16));
				break;
			} else {
				Short[] int16 = new Short[rows];
				for (int i = 0; i < rows; i++) {
					try {
						int16[i] = new Short(grid.getItem(i).getText());
					} catch (NumberFormatException ex) {
						int16[i] = 0;
					}
				}
				this.newValue.setValue(new Variant(int16));
			}
			break;
		case UInt16: // uint16
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				UnsignedShort[][] uint16 = new UnsignedShort[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							uint16[i][j] = new UnsignedShort(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							uint16[i][j] = new UnsignedShort(0);
						}
					}
				}
				this.newValue.setValue(new Variant(uint16));
				break;
			} else {
				UnsignedShort[] uint16 = new UnsignedShort[rows];
				for (int i = 0; i < rows; i++) {
					try {
						uint16[i] = new UnsignedShort(grid.getItem(i).getText());
					} catch (NumberFormatException ex) {
						uint16[i] = new UnsignedShort(0);
					}
				}
				this.newValue.setValue(new Variant(uint16));
			}
			break;
		case Int32: // int32
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				Integer[][] int32 = new Integer[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							int32[i][j] = new Integer(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							int32[i][j] = 0;
						}
					}
				}
				this.newValue.setValue(new Variant(int32));
				break;
			} else {
				Integer[] int32 = new Integer[rows];
				for (int i = 0; i < rows; i++) {
					try {
						int32[i] = new Integer(grid.getItem(i).getText());
					} catch (NumberFormatException ex) {
						int32[i] = 0;
					}
				}
				this.newValue.setValue(new Variant(int32));
			}
			break;
		case UInt32: // uint32
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				UnsignedInteger[][] uint32 = new UnsignedInteger[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							uint32[i][j] = new UnsignedInteger(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							uint32[i][j] = new UnsignedInteger(0);
						}
					}
				}
				this.newValue.setValue(new Variant(uint32));
				break;
			} else {
				UnsignedInteger[] uint32 = new UnsignedInteger[rows];
				for (int i = 0; i < rows; i++) {
					try {
						uint32[i] = new UnsignedInteger(grid.getItem(i).getText());
					} catch (NumberFormatException ex) {
						uint32[i] = new UnsignedInteger(0);
					}
				}
				this.newValue.setValue(new Variant(uint32));
			}
			break;
		case Int64: // int64
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				Long[][] int64 = new Long[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							int64[i][j] = new Long(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							int64[i][j] = (long) 0;
						}
					}
				}
				this.newValue.setValue(new Variant(int64));
				break;
			} else {
				Long[] int64 = new Long[rows];
				for (int i = 0; i < rows; i++) {
					try {
						int64[i] = new Long(grid.getItem(i).getText());
					} catch (NumberFormatException ex) {
						int64[i] = (long) 0;
					}
				}
				this.newValue.setValue(new Variant(int64));
			}
			break;
		case UInt64: // uint64
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				UnsignedLong[][] uint64 = new UnsignedLong[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							uint64[i][j] = new UnsignedLong(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							uint64[i][j] = new UnsignedLong(0);
						}
					}
				}
				this.newValue.setValue(new Variant(uint64));
				break;
			} else {
				UnsignedLong[] uint64 = new UnsignedLong[rows];
				for (int i = 0; i < rows; i++) {
					try {
						uint64[i] = new UnsignedLong(grid.getItem(i).getText());
					} catch (NumberFormatException ex) {
						uint64[i] = new UnsignedLong(0);
					}
				}
				this.newValue.setValue(new Variant(uint64));
			}
			break;
		case Integer: // uint64
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				Integer[][] integer = new Integer[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							integer[i][j] = new Integer(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							integer[i][j] = new Integer(0);
						}
					}
				}
				this.newValue.setValue(new Variant(integer));
				break;
			} else {
				Integer[] integer = new Integer[rows];
				for (int i = 0; i < rows; i++) {
					try {
						integer[i] = new Integer(grid.getItem(i).getText());
					} catch (NumberFormatException ex) {
						integer[i] = new Integer(0);
					}
				}
				this.newValue.setValue(new Variant(integer));
			}
			break;
		case UInteger: // uint64
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				UnsignedInteger[][] uinteger = new UnsignedInteger[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							uinteger[i][j] = new UnsignedInteger(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							uinteger[i][j] = new UnsignedInteger(0);
						}
					}
				}
				this.newValue.setValue(new Variant(uinteger));
				break;
			} else {
				UnsignedInteger[] uinteger = new UnsignedInteger[rows];
				for (int i = 0; i < rows; i++) {
					try {
						uinteger[i] = new UnsignedInteger(grid.getItem(i).getText());
					} catch (NumberFormatException ex) {
						uinteger[i] = new UnsignedInteger(0);
					}
				}
				this.newValue.setValue(new Variant(uinteger));
			}
			break;
		case Float: // float
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				Float[][] float_ = new Float[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							float_[i][j] = new Float(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							float_[i][j] = (float) 0;
						}
					}
				}
				this.newValue.setValue(new Variant(float_));
				break;
			} else {
				Float[] float_ = new Float[rows];
				for (int i = 0; i < rows; i++) {
					try {
						float_[i] = new Float(grid.getItem(i).getText());
					} catch (NumberFormatException ex) {
						float_[i] = (float) 0;
					}
				}
				this.newValue.setValue(new Variant(float_));
			}
			break;
		case Double: // double
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				Double[][] double_ = new Double[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							double_[i][j] = new Double(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							double_[i][j] = 0.0;
						}
					}
				}
				this.newValue.setValue(new Variant(double_));
				break;
			} else {
				Double[] double_ = new Double[rows];
				for (int i = 0; i < rows; i++) {
					try {
						double_[i] = new Double(grid.getItem(i).getText());
					} catch (NumberFormatException ex) {
						double_[i] = 0.0;
					}
				}
				this.newValue.setValue(new Variant(double_));
			}
			break;
		case String: // String
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				String[][] string_ = new String[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						string_[i][j] = grid.getItem(i).getText(j);
					}
				}
				this.newValue.setValue(new Variant(string_));
				break;
			} else {
				String[] string_ = new String[rows];
				for (int i = 0; i < rows; i++) {
					string_[i] = grid.getItem(i).getText();
				}
				this.newValue.setValue(new Variant(string_));
			}
			break;
		case DateTime: // datetime
			rows = grid.getItemCount();
			DateTimeStringConverter dtConverter = new DateTimeStringConverter();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				DateTime[][] dateTimes_ = new DateTime[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							String val = grid.getItem(i).getText(j);
							dateTimes_[i][j] = dtConverter.convertFromString(val);
						} catch (NumberFormatException ex) {
							dateTimes_[i][j] = DateTime.currentTime();
						}
					}
				}
				this.newValue.setValue(new Variant(dateTimes_));
				break;
			} else {
				DateTime[] dateTimes_ = new DateTime[rows];
				for (int i = 0; i < rows; i++) {
					try {
						String val = grid.getItem(i).getText();
						dateTimes_[i] = dtConverter.convertFromString(val);
					} catch (NumberFormatException ex) {
						dateTimes_[i] = DateTime.currentTime();
					}
				}
				this.newValue.setValue(new Variant(dateTimes_));
			}
			break;
		case Enumeration: // enumeration
			rows = grid.getItemCount();
			LocalizedText[] enumvalues = getEnumStrings();
			if (enumvalues == null) {
				// not defined
				break;
			}
			if (this.valueRank == ValueRanks.Scalar) {
				Integer integer = 0;
				for (int i = 0; i < rows; i++) {
					try {
						String enumText = grid.getItem(i).getText();
						for (int ii = 0; ii < enumvalues.length; ii++) {
							if (enumvalues[ii].toString().equals(enumText)) {
								integer = ii;
								break;
							}
						}
						// integer = new Integer();
					} catch (NumberFormatException ex) {
						integer = new Integer(0);
					}
				}
				this.newValue.setValue(new Variant(integer));
			} else {
				columns = grid.getColumnCount();
				Integer[][] integer = new Integer[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						try {
							String enumText = grid.getItem(i).getText();
							for (int ii = 0; ii < enumvalues.length; ii++) {
								if (enumvalues[ii].toString().equals(enumText)) {
									integer[i][j] = ii;
									break;
								}
							}
							// integer[i][j] = new
							// Integer(grid.getItem(i).getText(j));
						} catch (NumberFormatException ex) {
							integer[i][j] = new Integer(0);
						}
					}
				}
				this.newValue.setValue(new Variant(integer));
			}
			break;
		case LocalizedText:
			rows = grid.getItemCount();
			if (this.dimensions.length == 2) {
				columns = grid.getColumnCount();
				LocalizedText[][] string_ = new LocalizedText[rows][columns];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						String value = grid.getItem(i).getText(j);
						LocalizedTextStringConverter converter = new LocalizedTextStringConverter();
						string_[i][j] = converter.convertFromString(value);
					}
				}
				this.newValue.setValue(new Variant(string_));
				break;
			} else {
				LocalizedText[] string_ = new LocalizedText[rows];
				for (int i = 0; i < rows; i++) {
					String value = grid.getItem(i).getText();
					LocalizedTextStringConverter converter = new LocalizedTextStringConverter();
					string_[i] = converter.convertFromString(value);
				}
				this.newValue.setValue(new Variant(string_));
			}
			break;
		case DataValue:
			break;
		case DiagnosticInfo:
			break;
		case ExpandedNodeId:
			break;
		case ExtensionObject:
			break;
		case Guid:
			break;
		case NodeId:
			break;
		case Number:
			break;
		case QualifiedName:
			break;
		case StatusCode:
			break;
		case Variant:
			break;
		case XmlElement:
			break;
		default:
			break;
		}
		super.okPressed();
	}

	private void createBooleanTable(Composite parent, DataValue value) {
		this.grid = new Grid(parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI | SWT.H_SCROLL);
		this.grid.setCellSelectionEnabled(true);
		this.grid.setHeaderVisible(true);
		this.grid.setRowHeaderVisible(true);
		GridItem item = null;
		GridColumn column = null;
		if (value.getValue().getValue() instanceof Boolean[][]) {
			Boolean[][] val = (Boolean[][]) value.getValue().getValue();
			int rows = val.length;
			int cols = val[0].length;
			this.dimensions = new UnsignedInteger[2];
			this.dimensions[0] = new UnsignedInteger(rows);
			this.dimensions[1] = new UnsignedInteger(cols);
			// first we create all columns
			for (int i = 0; i < cols; i++) {
				column = new GridColumn(grid, SWT.CHECK | SWT.CENTER);
				column.setText("" + (i + 1));
				column.setWidth(80);
			}
			// now create all rows
			for (int i = 0; i < rows; i++) {
				item = new GridItem(grid, SWT.NONE);
				for (int j = 0; j < cols; j++) {
					item.setChecked(j, val[i][j]);
				}
			}
		} else if (value.getValue().getValue() instanceof Boolean[]) {
			Boolean[] val = (Boolean[]) value.getValue().getValue();
			this.dimensions = new UnsignedInteger[1];
			this.dimensions[0] = new UnsignedInteger(val.length);
			column = new GridColumn(grid, SWT.CHECK | SWT.CENTER);
			column.setText("1");
			column.setWidth(80);
			for (int i = 0; i < val.length; i++) {
				item = new GridItem(grid, SWT.NONE);
				item.setChecked(0, val[i]);
			}
		} else if (value.getValue().getValue() instanceof Boolean) {
			Boolean[] val = new Boolean[1];
			val[0] = (Boolean) value.getValue().getValue();
			this.dimensions = new UnsignedInteger[1];
			this.dimensions[0] = new UnsignedInteger(val.length);
			column = new GridColumn(grid, SWT.CHECK | SWT.CENTER);
			column.setText("1");
			column.setWidth(80);
			for (int i = 0; i < val.length; i++) {
				item = new GridItem(grid, SWT.NONE);
				item.setChecked(0, val[i]);
			}
		}
		editor = new GridEditor(grid);
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		// grid.setAutoHeight(true);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(grid);
	}

	private void createEditTable(Composite parent, DataValue value) {
		this.grid = new Grid(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL);
		grid.setCellSelectionEnabled(true);
		grid.setHeaderVisible(true);
		grid.setRowHeaderVisible(true);
		GridItem item = null;
		GridColumn column = null;
		final DesignerFormToolkit controllCreationToolkit = new DesignerFormToolkit();
		if (value.getValue().getValue() instanceof Object[][]) {
			Object[][] val = (Object[][]) value.getValue().getValue();
			int rows = val.length;
			int cols = val[0].length;
			this.dimensions = new UnsignedInteger[2];
			this.dimensions[0] = new UnsignedInteger(rows);
			this.dimensions[1] = new UnsignedInteger(cols);
			// first we create all columns
			for (int i = 0; i < cols; i++) {
				column = new GridColumn(grid, SWT.NONE | SWT.CENTER);
				column.setText("" + (i + 1));
				column.setWidth(80);
			}
			// now create all rows
			for (int i = 0; i < rows; i++) {
				item = new GridItem(grid, SWT.NONE);
				for (int j = 0; j < cols; j++) {
					if (val[i][j] != null) {
						item.setText(j, val[i][j].toString());
					} else {
						item.setText("");
					}
				}
			}
		} else if (value.getValue().getValue() instanceof Object[]) {
			Object[] val = (Object[]) value.getValue().getValue();
			this.dimensions = new UnsignedInteger[1];
			this.dimensions[0] = new UnsignedInteger(val.length);
			column = new GridColumn(grid, SWT.NONE | SWT.CENTER);
			column.setText("1");
			column.setWidth(80);
			for (int i = 0; i < val.length; i++) {
				item = new GridItem(grid, SWT.NONE);
				try {
					if (val[i] != null) {
						switch (valueType) {
						case ByteString:
							item.setText(new String((byte[]) val[i]));
							break;
						default:
							item.setText(val[i].toString());
							break;
						}
					} else {
						item.setText("");
					}
				} catch (RuntimeException e) {
					// cannot use XMLElement toString();
					// TODO OPC UA XML Element:
					item.setText("");
				}
			}
		} else if (value.getValue().getValue() instanceof Object) {
			Object[] val = new Object[1];
			switch (valueType) {
			case ByteString:
				val[0] = new String((byte[]) value.getValue().getValue());
				break;
			case Enumeration:
				BrowseDescription nodesToBrowse = new BrowseDescription();
				nodesToBrowse.setBrowseDirection(BrowseDirection.Forward);
				nodesToBrowse.setIncludeSubtypes(true);
				nodesToBrowse.setNodeClassMask(NodeClass.getMask(NodeClass.Variable));
				nodesToBrowse.setNodeId(datatypeId);
				nodesToBrowse.setReferenceTypeId(Identifiers.HierarchicalReferences);
				nodesToBrowse.setResultMask(BrowseResultMask.getMask(BrowseResultMask.ALL));
				LocalizedText[] txt = new LocalizedText[0];
				try {
					BrowseResult[] result = ServerInstance.getInstance().getServerInstance().getMaster()
							.browse(new BrowseDescription[] { nodesToBrowse }, UnsignedInteger.ZERO, null, null);
					if (result != null && result.length > 0 && result[0].getReferences() != null) {
						for (ReferenceDescription refDesc : result[0].getReferences()) {
							ExpandedNodeId nodeId = refDesc.getNodeId();
							Node valueNode = ServerInstance.getNode(nodeId);
							if (valueNode instanceof UAVariableNode) {
								txt = (LocalizedText[]) ((UAVariableNode) valueNode).getValue().getValue();
							} else if (valueNode instanceof UAVariableTypeNode) {
								txt = (LocalizedText[]) ((UAVariableTypeNode) valueNode).getValue().getValue();
							}
						}
					}
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
				int ordinal = (int) value.getValue().getValue();
				if (txt == null) {
					val[0] = "[Not defined]";
				} else if (txt.length < ordinal) {
					// TODO: set error message
					val[0] = txt[0].toString();
				} else {
					val[0] = txt[ordinal].toString();
				}
				break;
			default:
				val[0] = value.getValue().getValue();
				break;
			}
			this.dimensions = new UnsignedInteger[1];
			this.dimensions[0] = new UnsignedInteger(val.length);
			column = new GridColumn(grid, SWT.NONE | SWT.CENTER);
			column.setText("1");
			column.setWidth(80);
			for (int i = 0; i < val.length; i++) {
				item = new GridItem(grid, SWT.NONE);
				item.setText(val[i].toString());
			}
		}
		this.grid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Grid viewer = ((Grid) e.getSource());
				Point pt = viewer.getCellSelection()[0];
				GridItem item = currentItem;
				editGrid(pt, item);
			}
		});
		this.grid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Point pt = ((Grid) e.getSource()).getCellSelection()[0];
				currentItem = (GridItem) e.item;
				editGrid(pt, (GridItem) e.item);
			}
		});
		editor = new GridEditor(grid);
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		// grid.setAutoHeight(true);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(grid);
	}

	private void editGrid(final Point pt, GridItem item) {
		Control oldEditor = editor.getEditor();
		if (oldEditor != null)
			oldEditor.dispose();
		if (item == null)
			return;
		// create the write validating field
		switch (valueType) {
		case SByte: // sbyte
			ValidatingField<Byte> sbyteval = controllCreationToolkit.createTextSByte(grid, (byte) 0);
			newEditor = (Text) sbyteval.getControl();
			break;
		case Byte: // byte
			ValidatingField<UnsignedByte> ubyteval = controllCreationToolkit.createTextByte(grid, new UnsignedByte(0));
			newEditor = (Text) ubyteval.getControl();
			break;
		case Int16: // int16
			ValidatingField<Short> int16val = controllCreationToolkit.createTextInt16(grid, (short) 0);
			newEditor = (Text) int16val.getControl();
			break;
		case UInt16: // uint16
			ValidatingField<UnsignedShort> uint16val = controllCreationToolkit.createTextUInt16(grid,
					new UnsignedShort(0));
			newEditor = (Text) uint16val.getControl();
			break;
		case Int32: // int32
			ValidatingField<Integer> int32val = controllCreationToolkit.createTextInt32(grid, 0);
			newEditor = (Text) int32val.getControl();
			break;
		case UInt32: // uint32
			ValidatingField<UnsignedInteger> uint32val = controllCreationToolkit.createTextUInt32(grid,
					new UnsignedInteger(0));
			newEditor = (Text) uint32val.getControl();
			break;
		case Int64: // int64
			ValidatingField<Long> int64val = controllCreationToolkit.createTextInt64(grid, new Long(0));
			newEditor = (Text) int64val.getControl();
			break;
		case UInt64: // uint64
			ValidatingField<UnsignedLong> uint64val = controllCreationToolkit.createTextUInt64(grid,
					new UnsignedLong(0));
			newEditor = (Text) uint64val.getControl();
			break;
		case Float: // float
			ValidatingField<Float> floatval = controllCreationToolkit.createTextFloat(grid, new Float(0));
			newEditor = (Text) floatval.getControl();
			break;
		case Double: // double
			ValidatingField<Double> doubleval = controllCreationToolkit.createTextDouble(grid, new Double(0));
			newEditor = (Text) doubleval.getControl();
			break;
		case DateTime: // datetime
			ValidatingField<DateTime> datetimeval = controllCreationToolkit.createTextDateTime(grid,
					DateTime.currentTime());
			newEditor = (Text) datetimeval.getControl();
			break;
		case Guid: // guid
			ValidatingField<UUID> guidval = controllCreationToolkit.createTextGuid(grid, UUID.randomUUID());
			newEditor = (Text) guidval.getControl();
			break;
		case String: // String
			ValidatingField<String> stringval = controllCreationToolkit.createTextString(grid);
			newEditor = (Text) stringval.getControl();
			break;
		case ByteString:
			ValidatingField<String> bytestringval = controllCreationToolkit.createTextString(grid);
			newEditor = (Text) bytestringval.getControl();
			break;
		case Enumeration:
			ValidatingField<LocalizedText> eival = controllCreationToolkit.createComboEnum(grid, this.datatypeId);
			newEditor = (CCombo) eival.getControl();
			break;
		case LocalizedText:
			ValidatingField<LocalizedText> localizedtextval = controllCreationToolkit.createLocalizedText(grid,
					new LocalizedText(""));
			newEditor = (Text) localizedtextval.getControl();
			break;
		case Boolean:
			break;
		case DataValue:
			break;
		case DiagnosticInfo:
			break;
		case ExpandedNodeId:
			break;
		case ExtensionObject:
			break;
		case Integer:
			break;
		case NodeId:
			break;
		case Null:
			break;
		case Number:
			break;
		case QualifiedName:
			break;
		case StatusCode:
			break;
		case UInteger:
			break;
		case Variant:
			break;
		case XmlElement:
			break;
		default:
			break;
		}
		if (newEditor != null) {
			String itemText = null;
			switch (valueType) {
			case LocalizedText:
				itemText = item.getText(pt.x);
				LocalizedTextStringConverter converter = new LocalizedTextStringConverter();
				final LocalizedText val = converter.convertFromString(itemText);
				((Text) this.newEditor).setText(itemText);
				((Text) this.newEditor).addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						Text text = (Text) editor.getEditor();
						LocalizedText lt2 = new LocalizedText(text.getText(), val.getLocale());
						editor.getItem().setText(pt.x, lt2.toString());
					}
				});
				((Text) this.newEditor).selectAll();
				newEditor.setFocus();
				editor.setEditor(newEditor, item, pt.x);
				break;
			case Enumeration:
				itemText = item.getText(pt.x);
				((CCombo) this.newEditor).setText(itemText);
				((CCombo) this.newEditor).addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						CCombo text = (CCombo) editor.getEditor();
						// fill enum value
						editor.getItem().setText(pt.x, text.getText());
						if (newEditor != null) {
							newEditor.dispose();
						}
					}
				});
				newEditor.setFocus();
				editor.grabHorizontal = true;
				editor.setEditor(newEditor, item, pt.x);
				break;
			default:
				itemText = item.getText(pt.x);
				((Text) this.newEditor).setText(itemText);
				((Text) this.newEditor).addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						Text text = (Text) editor.getEditor();
						editor.getItem().setText(pt.x, text.getText());
					}
				});
				((Text) this.newEditor).selectAll();
				newEditor.setFocus();
				editor.setEditor(newEditor, item, pt.x);
				break;
			}
		} else {
			// TODO: LOG VALUE CANNOT BE DEFINED
		}
	}

	private void expandBooleanTable(UnsignedInteger[] dimensions) {
		int[] valueDim = this.value.getValue().getArrayDimensions();
		int actrows = this.grid.getItemCount();
		int actcolumns = this.grid.getColumnCount();
		int rows = 0;
		int columns = 1;
		this.dimensions = dimensions;
		this.arrayDim = dimensions;
		if (dimensions.length > 0) {
			rows = dimensions[0].intValue();
		} else if (valueDim.length > 0) {
			rows = valueDim[0];
		}
		if (rows == 0 && valueDim.length > 0 && valueDim[0] > 0) {
			rows = valueDim[0];
		}
		/**
		 * OneOrMoreDimensions section
		 */
		else if (rows == 0 && valueDim.length == 0 && dimensions.length == 0) {
			rows = 1;
		} else if (rows == 0 && valueDim.length == 0 && dimensions.length > 0) {
			rows = dimensions.length;
		}
		if (actrows > rows) {
			// we have to delete some rows
			for (int i = actrows; i > rows; i--) {
				this.grid.remove(i - 1);
			}
		}
		if (dimensions.length > 1) {
			// we have min a two dimensional array
			columns = dimensions[1].intValue();
			if (columns == 0 && valueDim.length > 1 && valueDim[1] > 0) {
				columns = valueDim[1];
			}
		} else if (valueDim.length > 1) {
			columns = valueDim[1];
		}
		/**
		 * No defined array dimension
		 */
		if (this.dimensions.length == 0) {
			this.dimensions = new UnsignedInteger[] { new UnsignedInteger(rows), new UnsignedInteger(columns) };
		}
		if (actcolumns > columns) {
			// we have to delete some columns
			for (int i = actcolumns; i > columns; i--) {
				this.grid.getColumn(i - 1).dispose();
			}
		}
		if (actrows < rows) {
			// now create all rows
			for (int i = actrows; i < rows; i++) {
				new GridItem(grid, SWT.NONE);
			}
		}
		if (actcolumns < columns) {
			GridColumn column = null;
			// now create all rows
			for (int i = actcolumns; i < columns; i++) {
				column = new GridColumn(grid, SWT.CHECK | SWT.CENTER);
				column.setText("" + (i + 1));
				column.setWidth(80);
			}
		}
		editor = new GridEditor(grid);
	}

	private void expandEditTable(UnsignedInteger[] dimensions) {
		int[] valueDim = this.value.getValue().getArrayDimensions();
		int actrows = this.grid.getItemCount();
		int actcolumns = this.grid.getColumnCount();
		int rows = 0;
		int columns = 1;
		this.dimensions = dimensions;
		this.arrayDim = dimensions;
		if (dimensions.length > 0) {
			rows = dimensions[0].intValue();
		} else if (valueDim.length > 0) {
			rows = valueDim[0];
		}
		if (rows == 0 && valueDim.length > 0 && valueDim[0] > 0) {
			rows = valueDim[0];
		}
		/**
		 * OneOrMoreDimensions section
		 */
		else if (rows == 0 && valueDim.length == 0 && dimensions.length == 0) {
			rows = 1;
		} else if (rows == 0 && valueDim.length == 0 && dimensions.length > 0) {
			rows = dimensions.length;
		}
		if (actrows > rows) {
			// we have to delete some rows
			for (int i = actrows; i > rows; i--) {
				this.grid.remove(i - 1);
			}
		}
		if (dimensions.length > 1) {
			// we have min a two dimensional array
			columns = dimensions[1].intValue();
			if (columns == 0 && valueDim.length > 1 && valueDim[1] > 0) {
				columns = valueDim[1];
			}
		} else if (valueDim.length > 1) {
			columns = valueDim[1];
		}
		/**
		 * No defined array dimension
		 */
		if (this.dimensions != null && this.dimensions.length == 0) {
			this.dimensions = new UnsignedInteger[] { new UnsignedInteger(rows), new UnsignedInteger(columns) };
		}
		if (actcolumns > columns) {
			// we have to delete some columns
			for (int i = actcolumns; i > columns; i--) {
				this.grid.getColumn(i - 1).dispose();
			}
		}
		GridItem item = null;
		GridColumn column = null;
		// Text text = null;
		if (actrows < rows) {
			// now create all rows
			for (int i = actrows; i < rows; i++) {
				item = new GridItem(grid, SWT.NONE);
				// fill all columns with 0
				for (int j = 0; j < actcolumns; j++) {
					Object additionalNew = DesignerFormToolkit.createDefaultValue(valueType, ValueRanks.Scalar);
					if (additionalNew != null) {
						item.setText(j, additionalNew.toString());
					} else {
						item.setText(j, "0");
					}
				}
			}
		}
		if (actcolumns < columns) {
			// now create all columns
			for (int i = actcolumns; i < columns; i++) {
				column = new GridColumn(grid, SWT.NONE | SWT.CENTER);
				column.setText("" + (i + 1));
				column.setWidth(80);
				// expand all rows
				for (int j = 0; j < rows; j++) {
					grid.getItem(j).setText(grid.getColumnCount() - 1, "0");
				}
			}
		}
		editor = new GridEditor(grid);
	}
}
