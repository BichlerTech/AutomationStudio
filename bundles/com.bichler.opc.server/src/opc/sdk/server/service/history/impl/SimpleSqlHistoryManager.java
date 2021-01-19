package opc.sdk.server.service.history.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.rowset.serial.SerialBlob;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.encoding.DecodingException;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;
import org.opcfoundation.ua.encoding.binary.BinaryDecoder;
import org.opcfoundation.ua.encoding.binary.BinaryEncoder;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;
import opc.sdk.server.service.history.HistoryManager;
import opc.sdk.server.service.util.Com_Utils;
import opc.sdk.ua.constants.HistoryUpdateQueryMode;

/**
 * 
 * Default implementation of the historizing services. TODO: Should be done by a
 * 
 * framework!
 * 
 * 
 * 
 * @author Thomas Z&ouml;chbauer
 * 
 * 
 * 
 */
public class SimpleSqlHistoryManager extends HistoryManager {
	private SQLQueryGenerator generator = null;
	private String databaseDriver;
	private String user;
	private String password;
	private String databaseName;
	private boolean isConnected = false;
	private boolean isPlainValue;
	private static final String BETWEEN = " BETWEEN '";

	// private static final String
	public SimpleSqlHistoryManager(String dbDriver, String dbName, String databaseUri, String username, String password,
			boolean isPlainValue) {
		super(databaseUri);
		try {
			this.databaseName = dbName;
			this.databaseDriver = dbDriver;
			this.user = username;
			this.password = password;
			Class.forName(this.databaseDriver);
			this.generator = new SQLQueryGenerator();
			Connection connection = connect();
			close(connection);
			this.isConnected = true;
			this.isPlainValue = isPlainValue;
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Cannot create database driver " + this.databaseDriver + "!");
		}
	}

	public DataValue[] readHistoryPlain(Node node, DateTime startTime, DateTime endTime,
			UnsignedInteger maxValuesPerNode, boolean isReadModified, boolean timeFlowsBackwards) {
		if (!isConnected) {
			return new DataValue[0];
		}
		// node is allowed for history
		// PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection sqlConnection = null;
		List<DataValue> values = new ArrayList<>();
		try {
			// connect to db
			sqlConnection = connect();
			// execute a read
			String tableName = wrappTableName((VariableNode) node);
			String select = this.generator.selectHistoryValuesPlain(tableName, startTime, endTime, maxValuesPerNode,
					timeFlowsBackwards);
			// send statement to db
			try (PreparedStatement statement = sqlConnection.prepareStatement(select);) {
				resultSet = statement.executeQuery();
				// fetch values from the resultset
				while (resultSet.next()) {
					try {
						DataValue dataValue = fetchDataValueFromResultSetPlain(resultSet, (VariableNode) node);
						values.add(dataValue);
					} catch (Exception e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
					}
				}
			} finally {
				// close resultset
				if (resultSet != null) {
					resultSet.close();
				}
			}
		} catch (SQLException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		} finally {
			close(sqlConnection);
		}
		return values.toArray(new DataValue[0]);
	}

	@Override
	public DataValue[] readHistory(Node node, DateTime startTime, DateTime endTime, UnsignedInteger maxValuesPerNode,
			boolean isReadModified, boolean timeFlowsBackwards) {
		if (!isConnected) {
			return new DataValue[0];
		}
		if (isPlainValue) {
			return this.readHistoryPlain(node, startTime, endTime, maxValuesPerNode, isReadModified,
					timeFlowsBackwards);
		}
		// node is allowed for history
		// PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection sqlConnection = null;
		List<DataValue> values = new ArrayList<>();
		try {
			// connect to db
			sqlConnection = connect();
			// execute a read
			String tableName = wrappTableName((VariableNode) node);
			String select = this.generator.selectHistoryValues(tableName, startTime, endTime, maxValuesPerNode,
					timeFlowsBackwards);
			// send statement to db
			try (PreparedStatement statement = sqlConnection.prepareStatement(select);) {
				resultSet = statement.executeQuery();
				// fetch values from the resultset
				while (resultSet.next()) {
					try {
						DataValue dataValue = fetchDataValueFromResultSet(resultSet);
						values.add(dataValue);
					} catch (Exception e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
					}
				}
			} finally {
				// close resultset
				if (resultSet != null) {
					resultSet.close();
				}
			}
		} catch (SQLException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		} finally {
			close(sqlConnection);
		}
		return values.toArray(new DataValue[0]);
	}

	@Override
	public StatusCode updateHistory(Node node, DataValue value, HistoryUpdateQueryMode mode, DateTime startTime,
			DateTime endTime) {
		if (!isConnected) {
			return new StatusCode(StatusCodes.Bad_HistoryOperationUnsupported);
		}
		String tableName = wrappTableName(node);
		String query = null;
		// PreparedStatement statement = null;
		int result;
		StatusCode status = null;
		DataValue[] historyValues = null;
		ByteArrayOutputStream baos = null;
		BinaryEncoder encoder = null;
		long timestamp = 0;
		Blob blob = null;
		try (Connection sqlConnection = connect()) {
			// connect
			switch (mode) {
			// delete values between timestamps
			case DELETE:
				query = this.generator.deleteHistoricalValue(tableName, startTime, endTime);
				try (PreparedStatement statement = sqlConnection.prepareStatement(query)) {
					/*******************************************************/
					result = statement.executeUpdate();
					/*******************************************************/
					if (result == 0) {
						// no row deleted
						status = new StatusCode(StatusCodes.Bad_NoData);
						break;
					}
					status = StatusCode.GOOD;
				} catch (SQLException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
				break;
			/** update or insert a value if it does not exist */
			case INSERTREPLACE:
				baos = new ByteArrayOutputStream();
				encoder = new BinaryEncoder(baos);
				encoder.setEncoderContext(EncoderContext.getDefaultInstance());
				// inserts a value, if no value exists
				historyValues = readHistory(node, value.getSourceTimestamp(), value.getSourceTimestamp(),
						UnsignedInteger.ONE, false, false);
				// insert value
				if (historyValues == null || historyValues.length == 0) {
					query = this.generator.insertHistoricalValue(tableName);
					try (PreparedStatement statement = sqlConnection.prepareStatement(query)) {
						/*******************************************************/
						// 1 time
						timestamp = value.getSourceTimestamp().getTimeInMillis();
						// 2 blob
						encoder.putDataValue(null, value);
						byte[] bytes = baos.toByteArray();
						baos.flush();
						blob = new SerialBlob(bytes);
						statement.setLong(1, timestamp);
						statement.setBlob(2, blob);
						/*******************************************************/
						result = statement.executeUpdate();
						status = new StatusCode(StatusCodes.Good_EntryInserted);
					} catch (IOException | SQLException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
					} finally {
						if (baos != null) {
							try {
								baos.close();
							} catch (IOException e) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
							}
						}
					}
				}
				// replace value
				else {
					query = this.generator.updateHistoricalValue(tableName);
					try (PreparedStatement statement = sqlConnection.prepareStatement(query)) {
						/*******************************************************/
						// 1 time
						timestamp = value.getSourceTimestamp().getTimeInMillis();
						// 2 blob
						encoder.putDataValue(null, value);
						byte[] bytes = baos.toByteArray();
						baos.flush();
						blob = new SerialBlob(bytes);
						statement.setLong(1, timestamp);
						statement.setBlob(2, blob);
						/*******************************************************/
						result = statement.executeUpdate();
						status = new StatusCode(StatusCodes.Good_EntryReplaced);
					} catch (IOException | SQLException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
					} finally {
						if (baos != null) {
							try {
								baos.close();
							} catch (IOException e) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
							}
						}
					}
				}
				break;
			case INSERT:
				query = this.generator.insertHistoricalValue(tableName);
				try (PreparedStatement statement = sqlConnection.prepareStatement(query)) {
					baos = new ByteArrayOutputStream();
					encoder = new BinaryEncoder(baos);
					encoder.setEncoderContext(EncoderContext.getDefaultInstance());
					historyValues = readHistory(node, value.getSourceTimestamp(), value.getSourceTimestamp(),
							UnsignedInteger.ONE, false, false);
					if (historyValues != null && historyValues.length > 0) {
						status = new StatusCode(StatusCodes.Bad_EntryExists);
						break;
					}
					/*******************************************************/
					// 1 time
					timestamp = value.getSourceTimestamp().getTimeInMillis();
					// 2 blob
					encoder.putDataValue(null, value);
					byte[] bytes = baos.toByteArray();
					blob = new SerialBlob(bytes);
					statement.setLong(1, timestamp);
					statement.setBlob(2, blob);
					/*******************************************************/
					result = statement.executeUpdate();
					status = StatusCode.GOOD;
				} finally {
					if (baos != null) {
						try {
							baos.close();
						} catch (IOException e) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
						}
					}
				}
				break;
			// replaces an existing value, if a value exists
			case REPLACE:
				query = this.generator.updateHistoricalValue(tableName);
				try (PreparedStatement statement = sqlConnection.prepareStatement(query)) {
					baos = new ByteArrayOutputStream();
					encoder = new BinaryEncoder(baos);
					encoder.setEncoderContext(EncoderContext.getDefaultInstance());
					historyValues = readHistory(node, value.getSourceTimestamp(), value.getSourceTimestamp(),
							UnsignedInteger.ONE, false, false);
					if (historyValues == null || historyValues.length == 0) {
						status = new StatusCode(StatusCodes.Bad_NoEntryExists);
						break;
					}
					/*******************************************************/
					// 1 time
					timestamp = value.getSourceTimestamp().getTimeInMillis();
					// 2 blob
					encoder.putDataValue(null, value);
					blob = new SerialBlob(baos.toByteArray());
					statement.setLong(1, timestamp);
					statement.setBlob(2, blob);
					/*******************************************************/
					result = statement.executeUpdate();
					status = StatusCode.GOOD;
				} finally {
					if (baos != null) {
						try {
							baos.close();
						} catch (IOException e) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
						}
					}
				}
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			return new StatusCode(StatusCodes.Bad_HistoryOperationInvalid);
		} catch (EncodingException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			return new StatusCode(StatusCodes.Bad_DataEncodingInvalid);
		}
		return status;
	}

	/**
	 * 
	 * HB 11.03.2016 write history value as plain datatype, not as blob
	 * 
	 */
	public void writeHistoryPlain(final Node node, final DataValue value) {
		if (!isConnected) {
			return;
		}
		// nothing to write if no connection to a db exist
		boolean isInsert = false;
		String tableName = wrappTableName(node);
		// PreparedStatement statement = null;
		ByteArrayOutputStream baos = null;
		Connection sqlConnection = null;
		try {
			sqlConnection = connect();
			do {
				// create the query
				String insertQuery = generator.insertHistoricalValuePlain(tableName);
				try (PreparedStatement statement = sqlConnection.prepareStatement(insertQuery);) {
					// INSERT INTO test_blob (Time,blob_value) VALUES (?,?)
					// INSERT INTO DoubleSimulationValue_1 (time,blob) VALUES
					// (?,?)
					// insert values to query
					// 1 time
					long servertimestamp = System.currentTimeMillis();
					if (value.getServerTimestamp() != null) {
						servertimestamp = value.getServerTimestamp().getTimeInMillis();
					}
					long sourcetimestamp = System.currentTimeMillis();
					if (value.getSourceTimestamp() != null) {
						sourcetimestamp = value.getSourceTimestamp().getTimeInMillis();
					}
					long statuscode = value.getStatusCode().getValue().longValue();
					// 2 blob
					// timestamp & statuscode
					statement.setLong(1, servertimestamp);
					statement.setLong(2, sourcetimestamp);
					statement.setTimestamp(3, new Timestamp(servertimestamp));
					statement.setLong(4, statuscode);
					// datavalue
					baos = new ByteArrayOutputStream();
					BinaryEncoder encoder = new BinaryEncoder(baos);
					encoder.setEncoderContext(EncoderContext.getDefaultInstance());
					encoder.putDataValue(null, value);
					byte[] bytes = baos.toByteArray();
					baos.flush();
					Blob blob = new SerialBlob(bytes);
					statement.setBlob(5, blob);
					this.setNodeValue(statement, node);
					statement.executeUpdate();
					isInsert = true;
				} catch (SQLException e) {
					// create a new table for the node
					if (e.getErrorCode() == 1146) {
						String createTableQuery = generator.CREATE_NODE_TABLE_PLAIN(tableName, value);
						try (PreparedStatement statement = sqlConnection.prepareStatement(createTableQuery);) {
							statement.executeUpdate(createTableQuery);
						} catch (SQLException e1) {
							// SQL Error
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
						}
					} else {
						break;
					}
				} catch (EncodingException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			} while (!isInsert);
		} catch (SQLException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		} finally {
			close(sqlConnection);
		}
	}

	@Override
	public void writehistory(final Node node, final DataValue value) {
		if (!isConnected) {
			return;
		}
		if (isPlainValue) {
			writeHistoryPlain(node, value);
			return;
		}
		// nothing to write if no connection to a db exist
		boolean isInsert = false;
		String tableName = wrappTableName(node);
		try (Connection sqlConnection = connect()) {
			do {
				String insertQuery = generator.insertHistoricalValue(tableName);
				try (PreparedStatement statement = sqlConnection.prepareStatement(insertQuery);) {
					// create the query
					// INSERT INTO test_blob (Time,blob_value) VALUES (?,?)
					// INSERT INTO DoubleSimulationValue_1 (time,blob) VALUES
					// (?,?)
					// insert values to query
					// 1 time
					long servertimestamp = System.currentTimeMillis();
					if (value.getServerTimestamp() != null) {
						servertimestamp = value.getServerTimestamp().getTimeInMillis();
					}
					long sourcetimestamp = System.currentTimeMillis();
					if (value.getSourceTimestamp() != null) {
						sourcetimestamp = value.getSourceTimestamp().getTimeInMillis();
					}
					long statuscode = value.getStatusCode().getValue().longValue();
					// 2 blob
					try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
						// timestamp & statuscode
						statement.setLong(1, servertimestamp);
						statement.setLong(3, sourcetimestamp);
						statement.setLong(4, statuscode);
						// datavalue
						BinaryEncoder encoder = new BinaryEncoder(baos);
						encoder.setEncoderContext(EncoderContext.getDefaultInstance());
						encoder.putDataValue(null, value);
						byte[] bytes = baos.toByteArray();
						baos.flush();
						Blob blob = new SerialBlob(bytes);
						statement.setBlob(5, blob);
						setNodeValue(statement, node);
						statement.executeUpdate();
						isInsert = true;
					} catch (IOException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
					}
					// output(tableName);
				}
				// table not found;
				catch (SQLException e) {
					// create a new table for the node
					if (e.getErrorCode() == 1146) {
						String createTableQuery = generator.CREATE_NODE_TABLE(tableName, value);
						try (PreparedStatement statement = sqlConnection.prepareStatement(createTableQuery);) {
							statement.executeUpdate(createTableQuery);
						} catch (SQLException e1) {
							// SQL Error
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
						}
					} else {
						break;
					}
				} catch (EncodingException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			} while (!isInsert);
		} catch (SQLException e) {
			// cannot connect
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * 
	 * Close statemenet and disconnects from db
	 * 
	 * 
	 * 
	 * @param sqlConnection
	 * 
	 */
	private void close(Connection sqlConnection) {
		if (sqlConnection != null) {
			try {
				sqlConnection.close();
				sqlConnection = null;
			} catch (SQLException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Cannot close SQL connection!");
			}
		}
	}

	private Connection connect() throws SQLException {
		Connection sqlConnection = null;
		try {
			sqlConnection = DriverManager.getConnection(getDatabase() + "/" + this.databaseName, this.user, password);
			sqlConnection.setAutoCommit(true);
		} catch (SQLException e) {
			if (sqlConnection != null) {
				sqlConnection.close();
			}
			throw e;
		}
		return sqlConnection;
	}

	private DataValue fetchDataValueFromResultSet(ResultSet resultSet) throws SQLException, DecodingException {
		Blob valueBlob = resultSet.getBlob(ColumnNames.BLOB_VALUE.name());
		int l = (int) valueBlob.length();
		byte[] bytes = valueBlob.getBytes(1, l);
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
			BinaryDecoder bd = new BinaryDecoder(bais, 2048);
			bd.setEncoderContext(EncoderContext.getDefaultInstance());
			return bd.getDataValue(null);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	private DataValue fetchDataValueFromResultSetPlain(ResultSet resultSet, VariableNode value)
			throws SQLException, DecodingException {
		DataValue val = new DataValue();
		if (value.getValueRank() == ValueRanks.Scalar.getValue()) {
			try {
				if (value.getValue().getCompositeClass() == UnsignedInteger.class
						|| value.getValue().getCompositeClass() == UnsignedShort.class
						|| value.getValue().getCompositeClass() == UnsignedByte.class
						|| value.getValue().getCompositeClass() == UnsignedLong.class
						|| value.getValue().getCompositeClass() == Integer.class
						|| value.getValue().getCompositeClass() == Long.class
						|| value.getValue().getCompositeClass() == Byte.class
						|| value.getValue().getCompositeClass() == Short.class) {
					val.setValue(new Variant(resultSet.getInt(ColumnNames.PLAIN_VALUE.name())));
				} else if (value.getValue().getCompositeClass() == Float.class
						|| value.getValue().getCompositeClass() == Double.class) {
					val.setValue(new Variant(resultSet.getFloat(ColumnNames.PLAIN_VALUE.name())));
				} else if (value.getValue().getCompositeClass() == Boolean.class) {
					val.setValue(new Variant(resultSet.getBoolean(ColumnNames.PLAIN_VALUE.name())));
				} else if (value.getValue().getCompositeClass() == String.class) {
					val.setValue(new Variant(resultSet.getString(ColumnNames.PLAIN_VALUE.name())));
				}
			} catch (SQLException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		val.setServerTimestamp(DateTime.fromMillis(resultSet.getLong(ColumnNames.SERVERTIME.name())));
		val.setSourceTimestamp(DateTime.fromMillis(resultSet.getLong(ColumnNames.SOURCETIME.name())));
		val.setStatusCode(new StatusCode(new UnsignedInteger(resultSet.getInt(ColumnNames.STATUSCODE.name()))));
		return val;
	}

	/**
	 * 
	 * HB 11.03.2016 add plain node value to table, only scalar values are allowed
	 * 
	 * 
	 * 
	 * @param statement
	 * 
	 * @param node
	 * 
	 */
	private void setNodeValue(PreparedStatement statement, Node val) {
		VariableNode value;
		if (val instanceof VariableNode)
			value = (VariableNode) val;
		else {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Wrong node type found, only Variablenode is supported! " + val.toString());
			return;
		}
		if (value.getValueRank() == ValueRanks.Scalar.getValue()) {
			try {
				if (value.getValue().getCompositeClass() == UnsignedInteger.class
						|| value.getValue().getCompositeClass() == UnsignedShort.class
						|| value.getValue().getCompositeClass() == UnsignedByte.class
						|| value.getValue().getCompositeClass() == UnsignedLong.class
						|| value.getValue().getCompositeClass() == Integer.class
						|| value.getValue().getCompositeClass() == Long.class
						|| value.getValue().getCompositeClass() == Byte.class
						|| value.getValue().getCompositeClass() == Short.class) {
					statement.setInt(6, value.getValue().intValue());
				} else if (value.getValue().getCompositeClass() == Float.class
						|| value.getValue().getCompositeClass() == Double.class) {
					statement.setFloat(6, value.getValue().floatValue());
				} else if (value.getValue().getCompositeClass() == Boolean.class) {
					statement.setBoolean(6, value.getValue().booleanValue());
				} else if (value.getValue().getCompositeClass() == String.class) {
					statement.setString(6, value.getValue().toString());
				}
			} catch (SQLException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
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
			path += sep + node.getBrowseName();
			node.setBrowsepath(path);
		}
		return node.getBrowsepath();
	}

	class SQLQueryGenerator {
		public String CREATE_NODE_TABLE(String tableName, DataValue value) {
			return "CREATE TABLE " + tableName + " (" + ColumnNames.SERVERTIME.name() + " LONG NOT NULL, "
					+ ColumnNames.SOURCETIME.name() + " LONG NOT NULL, " + ColumnNames.STATUSCODE.name()
					+ " INT NOT NULL, " + ColumnNames.BLOB_VALUE.name() + "  " + this.generateSQLTypeName(value)
					+ " NOT NULL)";
		}

		public String CREATE_NODE_TABLE_PLAIN(String tableName, DataValue value) {
			return "CREATE TABLE " + tableName + " (" + ColumnNames.SERVERTIME.name() + " LONG NOT NULL, "
					+ ColumnNames.SOURCETIME.name()
					+ " LONG NOT NULL, date_time timestamp(3) not null , sqlserver_time timestamp(3) default CURRENT_TIMESTAMP(3),"
					+ ColumnNames.STATUSCODE.name() + " INT NOT NULL, " + ColumnNames.BLOB_VALUE.name() + "  "
					+ this.generateSQLTypeName(value) + " NOT NULL, " + ColumnNames.PLAIN_VALUE.name() + "  "
					+ this.generateSQLTypeNamePlain(value) + " NOT NULL)";
		}

		private String generateSQLTypeName(DataValue value) {
			return "blob";
		}

		private String generateSQLTypeNamePlain(DataValue value) {
			String type = "int";
			if (value.getValue().isArray()) {
				type = "blob";
			} else if (value.getValue().getCompositeClass() == UnsignedInteger.class
					|| value.getValue().getCompositeClass() == UnsignedShort.class
					|| value.getValue().getCompositeClass() == UnsignedByte.class
					|| value.getValue().getCompositeClass() == UnsignedLong.class
					|| value.getValue().getCompositeClass() == Integer.class
					|| value.getValue().getCompositeClass() == Long.class
					|| value.getValue().getCompositeClass() == Byte.class
					|| value.getValue().getCompositeClass() == Short.class) {
				type = "int";
			} else if (value.getValue().getCompositeClass() == Float.class
					|| value.getValue().getCompositeClass() == Double.class) {
				type = "double";
			} else if (value.getValue().getCompositeClass() == Boolean.class) {
				type = "tinyint(1)";
			} else if (value.getValue().getCompositeClass() == String.class) {
				type = "varchar(50)";
			}
			return type;
		}

		public String deleteHistoricalValue(String tableName, DateTime startTime, DateTime endTime) {
			long start = startTime.getTimeInMillis();
			long end = endTime.getTimeInMillis();
			return "DELETE FROM " + tableName + " WHERE " + ColumnNames.SOURCETIME.name() + BETWEEN + start + "' and '"
					+ end + "'";
		}

		public String insertHistoricalValue(String tableName) {
			return "INSERT INTO " + tableName + " (" + ColumnNames.SERVERTIME.name() + ","
					+ ColumnNames.SOURCETIME.name() + "," + ColumnNames.STATUSCODE.name() + ","
					+ ColumnNames.BLOB_VALUE.name() + ") VALUES (?,?,?,?);";
		}

		public String insertHistoricalValuePlain(String tableName) {
			return "INSERT INTO " + tableName + " (" + ColumnNames.SERVERTIME.name() + ", "
					+ ColumnNames.SOURCETIME.name() + ", date_time , " + ColumnNames.STATUSCODE.name() + ","
					+ ColumnNames.BLOB_VALUE.name() + "," + ColumnNames.PLAIN_VALUE.name() + ") VALUES (?,?,?,?,?,?);";
		}

		public String selectHistoryValues(String tableName, DateTime startTime, DateTime endTime,
				UnsignedInteger maxValues, boolean timeFlowsBackwards) {
			long start = startTime.getTimeInMillis();
			long end = endTime.getTimeInMillis();
			String select = "SELECT " + ColumnNames.BLOB_VALUE.name() + ", " + ColumnNames.SOURCETIME.name() + " FROM "
					+ tableName + " WHERE ";
			if (DateTime.MIN_VALUE.compareTo(endTime) != 0) {
				if (!timeFlowsBackwards) {
					select = select.concat(ColumnNames.SOURCETIME.name() + BETWEEN + start + "' and '" + end + "'");
				} else {
					select = select.concat(ColumnNames.SOURCETIME.name() + BETWEEN + end + "' and '" + start + "'"
							+ " ORDER BY " + ColumnNames.SOURCETIME.name() + " DESC");
				}
			}
			// no end time
			else if (DateTime.MIN_VALUE.compareTo(startTime) != 0 && DateTime.MIN_VALUE.compareTo(endTime) == 0) {
				select = select.concat(ColumnNames.SOURCETIME.name() + " > '" + start + "'");
			}
			if (maxValues != null && maxValues.intValue() > 0) {
				select = select.concat(" LIMIT 0," + maxValues);
			}
			return select;
		}

		public String selectHistoryValuesPlain(String tableName, DateTime startTime, DateTime endTime,
				UnsignedInteger maxValues, boolean timeFlowsBackwards) {
			long start = startTime.getTimeInMillis();
			long end = endTime.getTimeInMillis();
			String select = "SELECT " + ColumnNames.PLAIN_VALUE.name() + ", " + ColumnNames.SOURCETIME.name() + ", "
					+ ColumnNames.STATUSCODE.name() + ", " + ColumnNames.SERVERTIME.name() + " FROM " + tableName
					+ " WHERE ";
			if (DateTime.MIN_VALUE.compareTo(endTime) != 0) {
				if (!timeFlowsBackwards) {
					select = select.concat(ColumnNames.SOURCETIME.name() + BETWEEN + start + "' and '" + end + "'");
				} else {
					select = select.concat(ColumnNames.SOURCETIME.name() + BETWEEN + end + "' and '" + start + "'"
							+ " ORDER BY " + ColumnNames.SOURCETIME.name() + " DESC");
				}
			}
			// no end time
			else if (DateTime.MIN_VALUE.compareTo(startTime) != 0 && DateTime.MIN_VALUE.compareTo(endTime) == 0) {
				select = select.concat(ColumnNames.SOURCETIME.name() + " > '" + start + "'");
			}
			if (maxValues != null && maxValues.intValue() > 0) {
				select = select.concat(" LIMIT 0," + maxValues);
			}
			return select;
		}

		public String updateHistoricalValue(String tablename) {
			return "UPDATE " + tablename + " SET " + ColumnNames.BLOB_VALUE.name() + " = ? WHERE "
					+ ColumnNames.SOURCETIME.name() + " = ?";
		}
	}

	enum ColumnNames {
		SERVERTIME, SOURCETIME, STATUSCODE, BLOB_VALUE, PLAIN_VALUE;
	}
}
