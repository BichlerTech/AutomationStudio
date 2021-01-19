package com.bichler.astudio.opcua.addressspace.model.nosql.userauthority;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.node.user.AuthorityUser;
import opc.sdk.core.node.user.DBAuthority;
import opc.sdk.core.node.user.DBRole;
import opc.sdk.core.node.user.DBUser;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.utils.CryptoUtil;

public class NoSqlUtil
{
  public static final String DB_USER = "userconfig.db";
  public static final String DB_LOCAL = "userpassword.db";
  private static final String TABLE_ROLES = "ROLES";
  private static final String TABLE_USERS = "USERS";
  private static final String TABLE_NODES = "NODES";
  private static final String TABLE_CONFIGURATION = "CONFIGURATION";
  private static HashMap<NodeId, Map<Integer, Integer>> authtonodes;

  public static Connection createConnection(String databasePath) throws SQLException, ClassNotFoundException
  {
    // load no sql driver
    Class.forName("org.sqlite.JDBC");
    // create connection
    Connection sqlConnection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
    return sqlConnection;
  }

  /**
   * 
   * @param authorityTable
   * @param userTable
   */
  public static void disconnect(Connection... authorityTable)
  {
    for (Connection authority : authorityTable)
    {
      try
      {
        if (authority != null)
        {
          // size database
          Statement statement = authority.createStatement();
          statement.execute("VACUUM");
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      finally
      {
        // close connection
        if (authority != null)
        {
          try
          {
            authority.close();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * Creates a new Table.
   * 
   * @param connection
   *          NoSql database connection
   * @param tablename
   *          Tablename to create
   * @param additional
   *          Table entries (e.g.: Name Text NOT NULL)
   * @return
   * @throws SQLException
   */
  public static boolean createTable(Connection connection, boolean hasPrimaryKey, String tablename,
      String... additional) throws SQLException
  {
    Statement statement = connection.createStatement();
    try
    {
      String createstatement = "CREATE TABLE IF NOT EXISTS " + tablename + "(";
      if (hasPrimaryKey)
      {
        createstatement += "ID INTEGER PRIMARY KEY AUTOINCREMENT";
      }
      if (additional != null && additional.length > 0)
      {
        for (int i = 0; i < additional.length; i++)
        {
          // split with ',' till last line
          if (!hasPrimaryKey && i <= 0)
          {
            // skip
          }
          else if (i < additional.length)
          {
            createstatement += ", ";
          }
          // append statement
          createstatement += additional[i];
        }
      }
      createstatement += ");";
      boolean result = statement.execute(createstatement);
      return result;
    }
    finally
    {
      if (statement != null)
      {
        statement.close();
      }
    }
  }

  public static boolean createNodesTable(Connection connection) throws SQLException
  {
    Statement statement = connection.createStatement();
    try
    {
      String createstatement = "CREATE TABLE IF NOT EXISTS " + TABLE_NODES + "(NS_NODE TEXT NOT NULL, "
          + "ID_NODE TEXT NOT NULL, ID_ROLE INT NOT NULL, "
          + "AUTHORITY INT NOT NULL, PRIMARY KEY(NS_NODE,ID_NODE,ID_ROLE));";
      boolean result = statement.execute(createstatement);
      return result;
    }
    finally
    {
      if (statement != null)
      {
        statement.close();
      }
    }
  }

  public static int getIdFromeRole(Connection connection, DBRole role) throws SQLException
  {
    int roleId = 0;
    Statement statement = null;
    try
    {
      String query = "SELECT ID FROM " + TABLE_ROLES + " WHERE NAME = ('" + role.getRolename() + "');";
      statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next())
      {
        roleId = resultSet.getInt("ID");
      }
    }
    finally
    {
      if (statement != null)
      {
        try
        {
          statement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    return roleId;
  }

  public static int getMaskFromeRoles(Connection connection, DBRole[] roles) throws SQLException
  {
    int roleMask = 0;
    if (roles.length == 0)
    {
      return roleMask;
    }
    for (DBRole role : roles)
    {
      Statement statement = null;
      try
      {
        String query = "SELECT ID FROM " + TABLE_ROLES + " WHERE NAME = ('" + role.getRolename() + "');";
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next())
        {
          int roleId = resultSet.getInt("ID");
          roleMask |= (int) Math.scalb(1.0, roleId);
        }
      }
      finally
      {
        if (statement != null)
        {
          try
          {
            statement.close();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
        }
      }
    }
    return roleMask;
  }

  public static int getIdFromeUser(Connection connection, String username) throws SQLException
  {
    int userId = 0;
    Statement statement = null;
    try
    {
      statement = connection.createStatement();
      String query = "SELECT ID FROM " + TABLE_USERS + " WHERE NAME = ( '" + username + "');";
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next())
      {
        userId = resultSet.getInt("ID");
      }
    }
    finally
    {
      if (statement != null)
      {
        try
        {
          statement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    return userId;
  }

  public static void initializeUserManagementTables(Connection connection) throws SQLException
  {
    createTable(connection, false, TABLE_CONFIGURATION, "PROPERTY TEXT PRIMARY KEY NOT NULL", "VALUE TEXT NOT NULL");
    createTable(connection, true, TABLE_ROLES, "NAME TEXT NOT NULL", "DESCRIPTION TEXT");
    createTable(connection, true, TABLE_USERS, "ID_ROLE INT NOT NULL", "NAME TEXT NOT NULL", "PASSWORD TEXT NOT NULL",
        "DESCRIPTION TEXT");
    createNodesTable(connection);
    // createTable(connection, true, TABLE_NODES, "NS_NODE TEXT NOT NULL",
    // "ID_NODE TEXT NOT NULL", "ID_ROLE INT NOT NULL",
    // "AUTHORITY INT NOT NULL");
    // / TODO: Store "CONFIGURATION" in database (or in server.config file)
    // createTable(connection, false, "CONFIG", "");
  }

  public static void initializeLocalUserManagementTables(Connection connection) throws SQLException
  {
    createTable(connection, true, TABLE_USERS, "ID_ROLE INT NOT NULL", "NAME TEXT NOT NULL", "PASSWORD TEXT NOT NULL",
        "DESCRIPTION TEXT");
    // createTable(connection, false, "NODES", "ID_NODE TEXT NOT NULL",
    // "ID_ROLE INT NOT NULL", "AUTHORITY INT NOT NULL");
    // / TODO: Store "CONFIGURATION" in database (or in server.config file)
    // createTable(connection, false, "CONFIG", "");
  }

  public static void linkUsersWithRole(List<DBUser> users, List<DBRole> roles)
  {
    for (DBUser user : users)
    {
      int mask = user.getRoleMask();
      for (DBRole role : roles)
      {
        int roleMask = ((int) Math.scalb(1.0, role.getId()));
        if ((mask & roleMask) != 0)
        {
          user.addRole(role);
        }
      }
    }
  }

  public static Map<String, String> readConfiguration(Connection connection) throws SQLException
  {
    Map<String, String> configuration = new HashMap<>();
    Statement statement = connection.createStatement();
    String query = "SELECT PROPERTY,VALUE FROM " + TABLE_CONFIGURATION + ";";
    ResultSet result = statement.executeQuery(query);
    try
    {
      while (result.next())
      {
        String property = result.getString("PROPERTY");
        String value = result.getString("VALUE");
        configuration.put(property, value);
      }
      return configuration;
    }
    finally
    {
      try
      {
        if (result != null)
        {
          result.close();
        }
      }
      catch (SQLException e)
      {
        Logger.getLogger(NoSqlUtil.class.getName()).log(Level.SEVERE, null, e);
      }
      try
      {
        if (statement != null)
        {
          statement.close();
        }
      }
      catch (SQLException e)
      {
        Logger.getLogger(NoSqlUtil.class.getName()).log(Level.SEVERE, null, e);
      }
    }
  }

  public static List<DBRole> readRoleTable(Connection connection) throws SQLException
  {
    Statement statement = connection.createStatement();
    String query = "SELECT * FROM " + TABLE_ROLES + ";";
    ResultSet result = statement.executeQuery(query);
    try
    {
      List<DBRole> roles = new ArrayList<>();
      while (result.next())
      {
        int idRole = result.getInt("ID");
        String rolename = result.getString("NAME");
        String description = result.getString("DESCRIPTION");
        DBRole role = new DBRole(idRole, rolename);
        role.setDescription(description);
        roles.add(role);
      }
      return roles;
    }
    finally
    {
      try
      {
        if (result != null)
        {
          result.close();
        }
      }
      catch (SQLException e)
      {
        Logger.getLogger(NoSqlUtil.class.getName()).log(Level.SEVERE, null, e);
      }
      try
      {
        if (statement != null)
        {
          statement.close();
        }
      }
      catch (SQLException e)
      {
        Logger.getLogger(NoSqlUtil.class.getName()).log(Level.SEVERE, null, e);
      }
    }
  }

  public static Map<NodeId, DBAuthority> readOPCNodesFromRoles(Connection connection, NamespaceTable nsTable,
      DBRole edit) throws SQLException
  {
    return readOPCNodesFromRoles(connection, nsTable, edit.getId());
  }

  public static Map<NodeId, DBAuthority> readOPCNodesFromRoles(Connection connection, NamespaceTable nsTable,
      int idRole) throws SQLException
  {
    Map<NodeId, DBAuthority> nodes2authorities = new HashMap<>();
    PreparedStatement statement = null;
    try
    {
      connection.setAutoCommit(false);
      String query = "SELECT NS_NODE,ID_NODE, AUTHORITY FROM " + TABLE_NODES + " WHERE ID_ROLE = ?;";
      statement = connection.prepareStatement(query);
      statement.setInt(1, idRole);
      // send query
      ResultSet nodes = statement.executeQuery();
      while (nodes.next())
      {
        // values from resultset
        String ns = nodes.getString("NS_NODE");
        int nsIndex = nsTable.getIndex(ns);
        String id = nodes.getString("ID_NODE");
        int auth = nodes.getInt("AUTHORITY");
        // set buisness objects
        NodeId nodeId = NodeId.parseNodeId((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + "" + id);
        DBAuthority authority = new DBAuthority();
        authority.setAuthorityRole(auth);
        // add authority to node
        nodes2authorities.put(nodeId, authority);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      connection.rollback();
    }
    finally
    {
      if (statement != null)
      {
        try
        {
          statement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      connection.setAutoCommit(true);
    }
    return nodes2authorities;
  }

  public static List<DBUser> readUserTable(Connection connection) throws SQLException
  {
    Statement statement = connection.createStatement();
    String query = "SELECT * FROM " + TABLE_USERS + ";";
    ResultSet result = statement.executeQuery(query);
    try
    {
      List<DBUser> users = new ArrayList<>();
      while (result.next())
      {
        int id = result.getInt("ID");
        int roleMask = result.getInt("ID_ROLE");
        String username = result.getString("NAME");
        String password = result.getString("PASSWORD");
        String description = result.getString("DESCRIPTION");
        // ADD
        DBUser user = new DBUser(id, roleMask, username, password);
        user.setDescription(description);
        users.add(user);
      }
      return users;
    }
    finally
    {
      try
      {
        if (result != null)
        {
          result.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        if (statement != null)
        {
          statement.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
  }

  public static void loadRolesforNodes(Connection connection, NamespaceTable nsTable) throws SQLException
  {
    authtonodes = new HashMap<NodeId, Map<Integer, Integer>>();
    Statement statement = connection.createStatement();
    ;
    ResultSet result = null;
    try
    {
      connection.setAutoCommit(false);
      String query = "SELECT NS_NODE,ID_NODE, ID_ROLE, AUTHORITY FROM " + TABLE_NODES + ";";
      // statement = connection.prepareStatement(query);
      result = statement.executeQuery(query);
      NodeId nid = null;
      int nsi;
      String ns;
      while (result.next())
      {
        ns = result.getString("NS_NODE");
        nsi = nsTable.getIndex(ns);
        if (nsi > -1)
        {
          nid = NodeId.parseNodeId("ns=" + nsi + ";" + result.getString("ID_NODE"));
        }
        // now try to find nodeid
        Map<Integer, Integer> auth = authtonodes.get(nid);
        if (auth == null)
        {
          auth = new HashMap<Integer, Integer>();
          authtonodes.put(nid, auth);
        }
        auth.put(result.getInt("ID_ROLE"), result.getInt("AUTHORITY"));
        // result.getString("ID_NODE");
      }
    }
    finally
    {
      try
      {
        if (result != null)
        {
          result.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        if (statement != null)
        {
          statement.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
  }

  public static int[][] readRolesFromNode2(NodeId nodeid)
  {
    int[] roles = null;
    Map<Integer, Integer> auths = authtonodes.get(nodeid);
    List<int[]> auth = new ArrayList<int[]>();
    if (auths != null)
    {
      for (Integer key : auths.keySet())
      {
        roles = new int[2];
        roles[0] = key;
        roles[1] = auths.get(key);
        auth.add(roles);
      }
    }
    return auth.toArray(new int[0][0]);
  }

  public static int[][] readRolesFromNode(Connection connection, String ns, String nodeid) throws SQLException
  {
    int[] roles = null;
    Statement statement = connection.createStatement();
    String query = "SELECT ID_ROLE, AUTHORITY FROM " + TABLE_NODES + " WHERE NS_NODE = '" + ns + "' and ID_NODE = '"
        + nodeid + "';";
    // ResultSet resultSet = statement.executeQuery(query);
    ResultSet result = statement.executeQuery(query);
    try
    {
      List<int[]> auth = new ArrayList<int[]>();
      while (result.next())
      {
        roles = new int[2];
        roles[0] = result.getInt("ID_ROLE");
        roles[1] = result.getInt("AUTHORITY");
        auth.add(roles);
      }
      return auth.toArray(new int[0][0]);
    }
    finally
    {
      try
      {
        if (result != null)
        {
          result.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        if (statement != null)
        {
          statement.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
  }

  public static int removeNodesFromDatabase(Connection connection, int id_role) throws SQLException
  {
    PreparedStatement statement = null;
    int result = -1;
    try
    {
      /** autocommit is disabled in remove role method */
      // connection.setAutoCommit(false);
      // remove nodes
      String query = "DELETE FROM " + TABLE_NODES + " WHERE ID_ROLE = ?;";
      statement = connection.prepareStatement(query);
      statement.setInt(1, id_role);
      result = statement.executeUpdate();
      /** no commit is required, because it is done in remove role method */
    }
    finally
    {
      if (statement != null)
      {
        try
        {
          statement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      // connection.setAutoCommit(true);
    }
    return result;
  }

  public static void removeRolesFromDatabase(Connection dbConnection) throws SQLException
  {
    Statement statement = null;
    try
    {
      // table roles
      truncateTable(dbConnection, TABLE_ROLES);
    }
    finally
    {
      if (statement != null)
      {
        try
        {
          statement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    // try {
    // statement = dbConnection.createStatement();
    // // table nodes, because their id gets invalid
    // String query = "DELETE FROM " + TABLE_NODES + ";";
    // statement.execute(query);
    // } finally {
    // if (statement != null) {
    // try {
    // statement.close();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }
    // }
  }

  private static String getNodeIdValue(NodeId nodeId)
  {
    if (NodeId.isNull(nodeId))
    {
      throw new NullPointerException("Null NodeId!");
    }
    IdType idType = nodeId.getIdType();
    Object value = nodeId.getValue();
    switch (idType)
    {
    case String:
      return "s=" + value;
    case Guid:
      return "g=" + value;
    case Numeric:
      return "i=" + value;
    case Opaque:
      if (value == null)
        return "b=null";
      return "b=" + new String(CryptoUtil.base64Encode((byte[]) value));
    }
    return null;
  }

  private static String getNodeIdIndex(NamespaceTable nsTable, NodeId nodeId)
  {
    return nsTable.getUri(nodeId.getNamespaceIndex());
  }

  public static void truncateTable(Connection connection, String tablename) throws SQLException
  {
    Statement statement = null;
    try
    {
      statement = connection.createStatement();
      // table roles
      String query = "DELETE FROM " + tablename + ";";
      statement.execute(query);
      // table nodes, because their id gets invalid
    }
    finally
    {
      if (statement != null)
      {
        try
        {
          statement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    try
    {
      statement = connection.createStatement();
      // table roles
      String query = "DELETE FROM SQLITE_SEQUENCE WHERE name ='" + tablename + "';";
      statement.execute(query);
      // table nodes, because their id gets invalid
    }
    finally
    {
      if (statement != null)
      {
        try
        {
          statement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  public static DBUser[] removeUserFromDatabase(Connection connection, DBUser user2remove) throws SQLException
  {
    // collect removed users
    List<DBUser> dbUsers = new ArrayList<>();
    PreparedStatement statement = null;
    try
    {
      connection.setAutoCommit(false);
      // prepare query
      String query = "DELETE FROM " + TABLE_USERS + " WHERE ID = ?;";
      statement = connection.prepareStatement(query);
      // remove user
      int id = ((DBUser) user2remove).getId();
      // remove with id
      if (id > 0)
      {
        statement.setInt(1, id);
        int result = statement.executeUpdate();
        if (result > 0)
        {
          dbUsers.add((DBUser) user2remove);
        }
      }
      // remove with rolename if there is no id
      else
      {
        dbUsers.add((DBUser) user2remove);
      }
      connection.commit();
      return dbUsers.toArray(new DBUser[0]);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      connection.rollback();
      return new DBUser[0];
    }
    finally
    {
      if (statement != null)
      {
        try
        {
          statement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      connection.setAutoCommit(true);
    }
  }

  public static void writeConfiguration(Connection connection, boolean isAllowAnonymous, boolean isAllowSecure)
      throws SQLException
  {
    // int result = 0;
    PreparedStatement statement = null;
    try
    {
      connection.setAutoCommit(false);
      String query = "INSERT OR REPLACE INTO " + TABLE_CONFIGURATION + " (PROPERTY, VALUE) VALUES (?,?);";
      statement = connection.prepareStatement(query);
      statement.setString(1, "ALLOW_ANNONYMOUS");
      statement.setString(2, "" + isAllowAnonymous);
      statement.executeUpdate();
      statement.setString(1, "ALLOW_UNSECURE");
      statement.setString(2, "" + isAllowSecure);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (statement != null)
      {
        try
        {
          statement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      connection.setAutoCommit(true);
    }
  }

  /**
   * Adds/or updates a node to the database with its authority
   * 
   * @throws SQLException
   */
  public static int writeNodeWithAuthorityToDatabase(Connection connection, NamespaceTable nsTable, int id_role,
      Map<NodeId, DBAuthority> nodes2add) throws SQLException
  {
    int result = 0;
    PreparedStatement insertNode = null;
    try
    {
      connection.setAutoCommit(false);
      // create prepared statement
      String query = "INSERT OR REPLACE INTO " + TABLE_NODES
          + " (NS_NODE, ID_NODE, ID_ROLE, AUTHORITY) VALUES ( ?,?,?,?);";
      insertNode = connection.prepareStatement(query);
      // insert nodes to db
      for (Entry<NodeId, DBAuthority> e : nodes2add.entrySet())
      {
        NodeId nodeId = e.getKey();
        DBAuthority authority = e.getValue();
        insertNode.setString(1, getNodeIdIndex(nsTable, nodeId));
        insertNode.setString(2, getNodeIdValue(nodeId));
        insertNode.setInt(3, id_role);
        insertNode.setInt(4, authority.getAuthorityRole());
        insertNode.executeUpdate();
      }
      // commit
      connection.commit();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      // rollback on error
      try
      {
        connection.rollback();
      }
      catch (SQLException e1)
      {
        e1.printStackTrace();
      }
    }
    finally
    {
      if (insertNode != null)
      {
        try
        {
          insertNode.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      connection.setAutoCommit(true);
    }
    return result;
  }

  /**
   * Adds a role to the database.
   * 
   * @param connection
   * @param roleName
   * @return 0 for failure or the affected row
   * 
   * @throws SQLException
   */
  public static int writeOrUpdateRoleToDatabase(Connection connection, DBRole role, boolean force) throws SQLException
  {
    PreparedStatement statement = null;
    int result = 0;
    // modify role
    if (!force && role.getId() > 0)
    {
      result = role.getId();
    }
    // insert new id for role
    else
    {
      result = getIdFromeRole(connection, role);
    }
    try
    {
      connection.setAutoCommit(false);
      // update rolename
      if (result > 0)
      {
        String query = "UPDATE " + TABLE_ROLES + " SET NAME=?, DESCRIPTION=? WHERE ID=?;";
        statement = connection.prepareStatement(query);
        // find id
        statement.setString(1, role.getRolename());
        statement.setString(2, role.getDescription());
        statement.setInt(3, result);
        result = statement.executeUpdate();
        connection.commit();
      }
      else
      {
        String query = "INSERT INTO " + TABLE_ROLES + " (NAME, DESCRIPTION) VALUES (?,?);";
        statement = connection.prepareStatement(query);
        // set rolename
        statement.setString(1, role.getRolename());
        statement.setString(2, role.getDescription());
        result = statement.executeUpdate();
        connection.commit();
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      // on error roll back
      connection.rollback();
    }
    finally
    {
      if (statement != null)
      {
        try
        {
          statement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      connection.setAutoCommit(true);
    }
    // find role id and set in DBRole
    if (result > 0)
    {
      int roleId = getIdFromeRole(connection, role);
      role.setId(roleId);
    }
    return result;
  }

  public static int writeOrUpdateUserToDatabase(Connection localConnection, Connection dbConnection, DBUser user)
      throws SQLException
  {
    // set role mask to user
    int id_role = 0;
    id_role = getMaskFromeRoles(dbConnection, user.getRoles());
    // add user
    // if (id_role > 0) {
    // set roleid to user
    user.setRoleMask(id_role);
    String name = user.getUsername();
    // if (name == null || name.isEmpty()) {
    // name = user.getUsername();
    // }
    int idUser = getIdFromeUser(localConnection, name);
    // replace
    int result = 0;
    try
    {
      // add to local studio database
      result = writeOrUpdateUser(localConnection, false, idUser, id_role, user);
      // add to user configuration database
      writeOrUpdateUser(dbConnection, true, idUser, id_role, user);
    }
    catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    catch (InvalidKeySpecException e)
    {
      e.printStackTrace();
    }
    // }
    // find user id to set in DBUser
    if (result > 0)
    {
      idUser = getIdFromeUser(localConnection, user.getUsername());
      if (idUser > 0)
      {
        user.setId(idUser);
      }
    }
    return result;
  }

  private static int writeOrUpdateUser(Connection connection, boolean isEncryptedPW, int idUser, int id_role,
      DBUser user) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException
  {
    int result = 0;
    PreparedStatement statement = null;
    try
    {
      connection.setAutoCommit(false);
      String password = null;
      if (isEncryptedPW)
      {
        AuthorityUser sha512 = new AuthorityUser();
        password = sha512.calculate(user.getPassword());
      }
      else
      {
        password = user.getPassword();
      }
      String usrName = user.getModifiedUserName();
      if (usrName == null || usrName.isEmpty())
      {
        usrName = user.getUsername();
      }
      if (idUser > 0)
      {
        String query = "UPDATE " + TABLE_USERS + " SET ID_ROLE=?, NAME =?, PASSWORD=?, DESCRIPTION = ? WHERE ID=?;";
        statement = connection.prepareStatement(query);
        statement.setInt(1, id_role);
        statement.setString(2, usrName);
        statement.setString(3, password);
        statement.setString(4, user.getDescription());
        statement.setInt(5, idUser);
        result = statement.executeUpdate();
        connection.commit();
      }
      else
      {
        String query = "INSERT INTO " + TABLE_USERS + " (ID_ROLE, NAME, PASSWORD, DESCRIPTION) VALUES ( ?,?,?,? );";
        statement = connection.prepareStatement(query);
        statement.setInt(1, id_role);
        statement.setString(2, usrName);
        statement.setString(3, password);
        statement.setString(4, user.getDescription());
        result = statement.executeUpdate();
        connection.commit();
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      connection.rollback();
      throw e;
    }
    finally
    {
      if (statement != null)
      {
        try
        {
          statement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      connection.setAutoCommit(true);
    }
    return result;
  }
}
