package com.bichler.astudio.editor.aggregated;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.astudio.editor.aggregated.model.AggregatedDriverConfigurationType;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;

public class AggregatedEditorDevice extends AggregatedDevice
{
  private String uaInformationModel;
  private AggregatedDriverConfigurationType aggregatedConfigType = AggregatedDriverConfigurationType.remote;
  private UAServerApplicationInstance server = null;
  public boolean driverstatusflag = false;
  public List<NodeId> driverstatusnodes = new ArrayList<>();

  public AggregatedEditorDevice()
  {
    super();
  }

  public void connect2Server(IFileSystem fileSystem, File configurationFile, File certFile, File privKeyFile,
      String modelPath)
  {
    switch (aggregatedConfigType)
    {
    case remote:
      super.connect2Server(configurationFile, certFile, privKeyFile);
      break;
    case model:
      if (modelPath == null)
      {
        return;
      }
      this.server = new UAServerApplicationInstance();
      if (!fileSystem.isFile(modelPath))
      {
        break;
      }
      try
      {
        if (modelPath.endsWith(".jar"))
          server.getServerInstance().importModel(modelPath);
        else
          server.importModel(null, modelPath);
      }
      catch (ServiceResultException e)
      {
        e.printStackTrace();
        this.server = null;
      }
      break;
    }
  }

  @Override
  public void loadOPCUAServerConnection(InputStream stream)
  {
    if (stream != null)
    {
      BufferedReader reader = null;
      try
      {
        reader = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
          if (line.compareTo("servername") == 0)
          {
            this.setUaServerName(reader.readLine());
          }
          else if (line.compareTo("serveruri") == 0)
          {
            this.setUaServerUri(reader.readLine());
          }
          else if (line.compareTo("securitypolicy") == 0)
          {
            this.setUaSecurityPolicy(reader.readLine());
          }
          else if (line.compareTo("securitymode") == 0)
          {
            this.setUaSecurityMode(reader.readLine());
          }
          else if (line.compareTo("securitytype") == 0)
          {
            try
            {
              this.setUaSecurityType(Integer.parseInt(reader.readLine()));
            }
            catch (NumberFormatException ex)
            {
              this.setUaSecurityType(0);
            }
          }
          else if (line.compareTo("username") == 0)
          {
            this.setUaUserName(reader.readLine());
          }
          else if (line.compareTo("password") == 0)
          {
            this.setUaPassword(reader.readLine());
          }
          else if (line.compareTo("certificate") == 0)
          {
            this.setUaCertificate(reader.readLine());
          }
          else if (line.compareTo("informationmodel") == 0)
          {
            this.setUaInformationModel(reader.readLine());
          }
          else if (line.compareTo("editorconfigtype") == 0)
          {
            this.setAggregatedConfigurationType(AggregatedDriverConfigurationType.valueOf(reader.readLine()));
          }
          else if (line.compareTo("drvstatusflag") == 0)
          {
            line = reader.readLine();
            this.driverstatusflag = Boolean.parseBoolean(line);
          }
          else if (line.compareTo("driverstatus") == 0)
          {
            String driverstatus = null;
            while ((driverstatus = reader.readLine()) != null)
            {
              if (driverstatus.trim().isEmpty())
              {
                break;
              }
              int start = driverstatus.indexOf("=");
              String nodeid = driverstatus.substring(start + 1);
              String[] nodeid2parse = nodeid.split(";");
              NodeId parsedId = NodeId.NULL;
              try
              {
                if (nodeid2parse.length == 1)
                {
                  parsedId = NodeId.parseNodeId(nodeid);
                }
                else if (nodeid2parse.length == 2)
                {
                  String ns = nodeid2parse[0];
                  String idValue = nodeid2parse[1];
                  int index = ServerInstance.getInstance().getServerInstance().getNamespaceUris().getIndex(ns);
                  parsedId = NodeId.parseNodeId("ns=" + index + ";" + idValue);
                }
                // check if node exists
                Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
                    .getNodeById(parsedId);
                if (node != null)
                {
                  this.driverstatusnodes.add(parsedId);
                }
              }
              catch (IllegalArgumentException e)
              {
                // e.printStackTrace();
              }
            }
          }
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      finally
      {
        if (reader != null)
        {
          try
          {
            reader.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public AggregatedDriverConfigurationType getAggregatedConfigType()
  {
    return aggregatedConfigType;
  }

  public String getUaInformationModel()
  {
    return uaInformationModel;
  }

  private void setAggregatedConfigurationType(AggregatedDriverConfigurationType configType)
  {
    this.aggregatedConfigType = configType;
  }

  private void setUaInformationModel(String readLine)
  {
    this.uaInformationModel = readLine;
  }

  public BrowseResult browse(NodeId nodeId, BrowseDirection direction, boolean includeSubtyes,
      UnsignedInteger nodeClass, NodeId referenceId, UnsignedInteger resultMask)
  {
    switch (aggregatedConfigType)
    {
    case remote:
      if (getUaclient() != null && getUaclient().getActiveSession() != null)
      {
        try
        {
          return getUaclient().browse(null, nodeId, direction, includeSubtyes, nodeClass, referenceId, resultMask,
              UnsignedInteger.ZERO, null, false);
        }
        catch (ServiceResultException e)
        {
          e.printStackTrace();
        }
      }
      break;
    case model:
      if (this.server == null)
      {
        return null;
      }
      BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
      nodesToBrowse[0] = new BrowseDescription();
      nodesToBrowse[0].setBrowseDirection(direction);
      nodesToBrowse[0].setIncludeSubtypes(includeSubtyes);
      nodesToBrowse[0].setNodeClassMask(nodeClass);
      nodesToBrowse[0].setNodeId(nodeId);
      nodesToBrowse[0].setReferenceTypeId(referenceId);
      nodesToBrowse[0].setResultMask(resultMask);
      BrowseResult[] results = null;
      try
      {
        results = this.server.getServerInstance().getMaster().browse(nodesToBrowse, UnsignedInteger.ZERO, null, null);
        if (results != null && results.length > 0)
        {
          return results[0];
        }
      }
      catch (ServiceResultException e)
      {
        e.printStackTrace();
      }
      break;
    }
    return null;
  }

  public boolean isConnected()
  {
    switch (aggregatedConfigType)
    {
    case remote:
      if (getUaclient() != null && getUaclient().getActiveSession() != null)
      {
        return true;
      }
      return false;
    case model:
      return this.server != null;
    }
    return false;
  }

  public Deque<BrowsePathElement> getFullBrowsePath(NodeId nodeId, AggregatedEditorDevice client, NodeId endnode)
  {
    Deque<BrowsePathElement> queue = new ArrayDeque<BrowsePathElement>();
    if (NodeId.isNull(nodeId))
    {
      return queue;
    }
    BrowsePathElement element = null;
    NodeId baseTypeDef = null;
    LocalizedText displayname = null;
    QualifiedName qualifiedName = null;
    DataValue value = client.readAggregatedValue(nodeId, Attributes.DisplayName);
    if (value != null && value.getValue() != null && value.getValue().getValue() != null)
    {
      displayname = (LocalizedText) value.getValue().getValue();
    }
    value = client.readAggregatedValue(nodeId, Attributes.BrowseName);
    if (value != null && value.getValue() != null && value.getValue().getValue() != null)
    {
      qualifiedName = (QualifiedName) value.getValue().getValue();
    }
    element = new BrowsePathElement();
    element.setDisplayname(displayname);
    element.setBrowsename(qualifiedName);
    element.setId(nodeId);
    queue.add(element);
    boolean references2browse = true;
    while (!nodeId.equals(endnode) && references2browse)
    {
      BrowseResult references = client.browse(nodeId, BrowseDirection.Inverse, true, NodeClass.getMask(NodeClass.ALL),
          Identifiers.HierarchicalReferences, BrowseResultMask.getMask(Arrays.asList(BrowseResultMask.values())));
      if (references != null && references.getReferences() != null && references.getReferences().length > 0
          && references.getReferences()[0] != null)
      {
        element = new BrowsePathElement();
        element.setDisplayname(references.getReferences()[0].getDisplayName());
        element.setBrowsename(references.getReferences()[0].getBrowseName());
        baseTypeDef = NodeId.get(references.getReferences()[0].getNodeId().getIdType(),
            references.getReferences()[0].getNodeId().getNamespaceIndex(),
            references.getReferences()[0].getNodeId().getValue());
        element.setId(baseTypeDef);
        queue.addFirst(element);
        nodeId = baseTypeDef;
      }
      else
      {
        references2browse = false;
      }
    }
    return queue;
  }

  public DataValue readAggregatedValue(NodeId nodeId, UnsignedInteger attributeId)
  {
    DataValue value = null;
    switch (aggregatedConfigType)
    {
    case remote:
      if (getUaclient() != null && getUaclient().getActiveSession() != null)
      {
        try
        {
          value = getUaclient().read1(getUaclient().getActiveSession(), nodeId, attributeId, null, null, 0.0,
              TimestampsToReturn.Both);
          return value;
        }
        catch (ServiceResultException e)
        {
          e.printStackTrace();
        }
      }
    case model:
      if (this.server != null)
      {
        ReadValueId[] nodesToRead = new ReadValueId[1];
        nodesToRead[0] = new ReadValueId();
        nodesToRead[0].setNodeId(nodeId);
        nodesToRead[0].setAttributeId(attributeId);
        try
        {
          DataValue[] result = this.server.getServerInstance().getMaster().read(nodesToRead, 0.0,
              TimestampsToReturn.Both, null, null);
          if (result != null && result.length > 0)
          {
            return result[0];
          }
        }
        catch (ServiceResultException e)
        {
          e.printStackTrace();
        }
      }
      return null;
    }
    return new DataValue(StatusCode.BAD);
  }

  public Object existsNode(NodeId nodeId)
  {
    return readAggregatedValue(nodeId, Attributes.NodeId);
  }
}
