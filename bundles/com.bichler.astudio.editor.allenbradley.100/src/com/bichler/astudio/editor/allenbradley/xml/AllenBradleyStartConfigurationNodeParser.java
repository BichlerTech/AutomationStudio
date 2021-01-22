package com.bichler.astudio.editor.allenbradley.xml;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bichler.astudio.editor.allenbradley.model.AllenBradleyRootStartConfigurationNode;
import com.bichler.astudio.editor.allenbradley.model.AllenBradleyStartConfigurationNode;

public class AllenBradleyStartConfigurationNodeParser extends DefaultHandler
{
  public static final String RootTag = "startconfig";
  public static final String ValueTag = "value";
  public static final String NodeTag = "node";
  public static final String IsActiveTag = "isactive";
  public static final String DeviceTagNS = "devicens";
  public static final String ConfigNodeIdTagNS = "confignodeidns";
  public static final String DeviceTagID = "deviceid";
  public static final String ConfigNodeIdTagID = "confignodeid";
  public static final String DeviceTagName = "devicename";
  public static final String ConfigNodeTagName = "confignodename";
  public static final String IndexTag = "index";
  private NamespaceTable namespaceTable = null;
  private AllenBradleyRootStartConfigurationNode root;

  public AllenBradleyStartConfigurationNodeParser(NamespaceTable namespaceTable,
      AllenBradleyRootStartConfigurationNode root)
  {
    this.namespaceTable = namespaceTable;
    this.root = root;
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    if (RootTag.compareTo(qName) == 0)
    {
      this.root.setActive(Boolean.parseBoolean(attributes.getValue(IsActiveTag)));
    }
    else if (NodeTag.compareTo(qName) == 0)
    {
      AllenBradleyStartConfigurationNode node = new AllenBradleyStartConfigurationNode();
      String active = attributes.getValue(IsActiveTag);
      String configNS = attributes.getValue(ConfigNodeIdTagNS);
      String configID = attributes.getValue(ConfigNodeIdTagID);
      String deviceNS = attributes.getValue(DeviceTagNS);
      String deviceID = attributes.getValue(DeviceTagID);
      String index = attributes.getValue(IndexTag);
      String value = attributes.getValue(ValueTag);
      String configName = attributes.getValue(ConfigNodeTagName);
      String deviceName = attributes.getValue(DeviceTagName);
      /** active */
      node.setActive(Boolean.parseBoolean(active));
      /** config node id */
      int nsIndex = namespaceTable.getIndex(configNS);
      NodeId id = NodeId.decode((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + configID);
      node.setConfigId(id);
      /** device id */
      nsIndex = namespaceTable.getIndex(deviceNS);
      id = NodeId.decode((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + deviceID);
      node.setDeviceId(id);
      /** index */
      node.setIndex(Integer.parseInt(index));
      /** value */
      node.setValue(Integer.parseInt(value));
      node.setConfigNodeName(configName != null ? configName : "");
      node.setDeviceName(deviceName != null ? deviceName : "");
      this.root.addChild(node);
    }
  }
}