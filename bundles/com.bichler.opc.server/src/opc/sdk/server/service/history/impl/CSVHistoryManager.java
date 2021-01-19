package opc.sdk.server.service.history.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;
import opc.sdk.server.service.history.HistoryManager;
import opc.sdk.server.service.util.Com_Utils;
import opc.sdk.ua.constants.HistoryUpdateQueryMode;

public class CSVHistoryManager extends HistoryManager {
	private File dbFile;

	public CSVHistoryManager(String database) {
		super(database);
		this.dbFile = new File(database);
		if (!dbFile.exists()) {
			dbFile.mkdirs();
		}
	}

	@Override
	public void writehistory(Node node, DataValue value) {
		String tbname = this.wrappTableName(node);
		File logg = new File(getDatabase(), tbname);
		if (!logg.exists()) {
			try {
				logg.createNewFile();
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"CSV database file does not exist! Value is not stored in database!");
				return;
			}
		}
		// now create insert string
		Timestamp servertimestamp = new Timestamp(System.currentTimeMillis());
		if (value.getServerTimestamp() != null) {
			servertimestamp = new Timestamp(value.getServerTimestamp().getTimeInMillis());
		}
		Timestamp sourcetimestamp = new Timestamp(System.currentTimeMillis());
		if (value.getSourceTimestamp() != null) {
			sourcetimestamp = new Timestamp(value.getSourceTimestamp().getTimeInMillis());
		}
		long statuscode = value.getStatusCode().getValue().longValue();
		String insert = servertimestamp.toString() + ";";
		insert += sourcetimestamp.toString() + ";";
		insert += statuscode + ";";
		if (value.getValue().getCompositeClass() == UnsignedInteger.class
				|| value.getValue().getCompositeClass() == UnsignedShort.class
				|| value.getValue().getCompositeClass() == UnsignedByte.class
				|| value.getValue().getCompositeClass() == UnsignedLong.class
				|| value.getValue().getCompositeClass() == Integer.class
				|| value.getValue().getCompositeClass() == Long.class
				|| value.getValue().getCompositeClass() == Byte.class
				|| value.getValue().getCompositeClass() == Short.class) {
			insert += value.getValue().longValue();
		} else if (value.getValue().getCompositeClass() == Float.class
				|| value.getValue().getCompositeClass() == Double.class) {
			insert += value.getValue().doubleValue();
		} else if (value.getValue().getCompositeClass() == String.class) {
			insert += value.getValue().toString();
		} else if (value.getValue().getCompositeClass() == Boolean.class) {
			insert += value.getValue().booleanValue();
		}
		try (BufferedWriter write = new BufferedWriter(new FileWriter(logg, true))) {
			write.append(insert + "\n");
			write.flush();
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"CSV database file cannot be written! Value is not stored inside the database!", e);
		}
	}

	private String wrappTableName(Node node) {
		if (node.getBrowsepath() == null) {
			String path = "";
			// generate browsepath
			Deque<String> pathitem = Com_Utils.getFullBrowsePath(node.getNodeId(), this.internalServer,
					Identifiers.ObjectsFolder);
			String sep = "";
			for (String item : pathitem) {
				if (item.compareTo("Objects") == 0)
					continue;
				path += sep + item;
				sep = "_";
			}
			path += sep + node.getBrowseName() + ".csv";
			node.setBrowsepath(path);
		}
		return node.getBrowsepath();
	}

	@Override
	public DataValue[] readHistory(Node node, DateTime startTime, DateTime endTime, UnsignedInteger maxValuesPerNode,
			boolean isReadModified, boolean timeFlowsBackward) {
		String tbname = this.wrappTableName(node);
		if (!(node instanceof VariableNode)) {
			return new DataValue[0];
		}
		Class<?> class2parse = ((VariableNode) node).getValue().getCompositeClass();
		List<DataValue> values = new ArrayList<>();
		File historical = new File(getDatabase() + tbname);
		if (!historical.exists()) {
			return new DataValue[0];
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(historical)))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] valueEntry = line.split(";");
				// source
				DateTime sourceTimestamp = DateTime.parseDateTime(valueEntry[0]);
				// iterate to starttime valuse
				if (sourceTimestamp.getTimeInMillis() < startTime.getTimeInMillis()) {
					continue;
				}
				// skip all values after endtime
				if (sourceTimestamp.getTimeInMillis() > endTime.getTimeInMillis()) {
					break;
				}
				// target
				DateTime serverTimestamp = DateTime.parseDateTime(valueEntry[1]);
				// statuscode
				StatusCode status = new StatusCode(UnsignedInteger.parseUnsignedInteger(valueEntry[2]));
				// value
				Variant value = null;
				if (class2parse == UnsignedInteger.class || class2parse == UnsignedShort.class
						|| class2parse == UnsignedByte.class || class2parse == UnsignedLong.class
						|| class2parse == Integer.class || class2parse == Long.class || class2parse == Byte.class
						|| class2parse == Short.class || class2parse == Float.class || class2parse == Double.class
						|| class2parse == Boolean.class) {
					try {
						Constructor<?> constructor = class2parse.getConstructor(String.class);
						Object genVal = constructor.newInstance(valueEntry[3]);
						value = new Variant(genVal);
					} catch (Exception e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Value " + valueEntry[3] + " cannot read from CSV database file!");
					}
				} else if (class2parse == String.class) {
					value = new Variant(valueEntry[3]);
				}
				values.add(new DataValue(value, status, sourceTimestamp, UnsignedShort.ZERO, serverTimestamp,
						UnsignedShort.ZERO));
			}
		} catch (IOException | ParseException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return values.toArray(new DataValue[0]);
	}

	@Override
	public StatusCode updateHistory(Node node, DataValue value, HistoryUpdateQueryMode mode, DateTime startTime,
			DateTime endTime) {
		return StatusCode.BAD;
	}
}
