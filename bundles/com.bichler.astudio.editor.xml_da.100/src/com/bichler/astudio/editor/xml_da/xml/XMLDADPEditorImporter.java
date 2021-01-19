package com.bichler.astudio.editor.xml_da.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bichler.astudio.editor.xml_da.driver.datatype.XML_DA_DATATYPE;

public class XMLDADPEditorImporter
{
  public XMLDADPEditorImporter()
  {
  }

  public List<XML_DA_DPItem> loadDPs(InputStream stream, NamespaceTable uris)
  {
    try
    {
      InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
      InputSource is = new InputSource(isr);
      return loadDPs(is, uris);
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public List<XML_DA_DPItem> loadDPs(InputSource stream, NamespaceTable uris)
  {
    List<XML_DA_DPItem> dps = new ArrayList<XML_DA_DPItem>();
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try
    {
      SAXParser parser = factory.newSAXParser();
      DatapointsParser dpparser = new DatapointsParser(dps, uris);
      parser.parse(stream, dpparser);
    }
    catch (ParserConfigurationException e)
    {
      e.printStackTrace();
    }
    catch (SAXException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return dps;
  }


  class DatapointsParser extends DefaultHandler
  {
    private NodeId nodeid = null;
    private String browsename = "";
    private boolean active = false;
    private int cycletime = -1;
    private String itempath = "";
    private String itemname = "";
    private XML_DA_DPItem actNode = null;
    private List<XML_DA_DPItem> nodes = null;
    private String[] items = null;
    private NamespaceTable uris = null;
    private XML_DA_MAPPING_TYPE mapping = XML_DA_MAPPING_TYPE.SCALAR;

    public DatapointsParser(List<XML_DA_DPItem> nodes, NamespaceTable uris)
    {
      this.nodes = nodes;
      this.uris = uris;
    }

    private void init()
    {
      this.nodeid = null;
      this.browsename = "";
      this.active = false;
      this.cycletime = -1;
      this.itempath = "";
      this.itemname = "";
      this.actNode = null;
      this.items = null;
      this.mapping = XML_DA_MAPPING_TYPE.SCALAR;
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException
    {
    }

    @Override
    public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException
    {
      if (name.compareTo("dp") == 0)
      {
        // reset node attributes to parse
        init();
      }
      else if (name.compareTo("nodeid") == 0)
      {
        try
        {
          items = attrs.getValue("value").split(";");
          if (items != null && items.length == 2)
          {
            int nsindex = uris.getIndex(items[0].replace("ns=", ""));
            if (nsindex != -1)
            {
              nodeid = NodeId.parseNodeId("ns=" + nsindex + ";" + items[1]);
              // actNode.setNodeId(NodeId.decode("ns=" + nsindex +
              // ";" + items[1]));
            }
          }
        }
        catch (NumberFormatException ex)
        {
          // TODO logg numberformatexception
        }
      }
      else if (name.compareTo("symbolname") == 0)
      {
        browsename = attrs.getValue("value");
        // actNode.setSymbolname(attrs.getValue("value"));
        // actNode.setBrowsename(attrs.getValue("value"));
      }
      else if (name.compareTo("isactive") == 0)
      {
        active = Boolean.parseBoolean(attrs.getValue("value"));
        // actNode.setActive(Boolean.parseBoolean(attrs.getValue("value")));
      }
      else if (name.compareTo("cycletime") == 0)
      {
        try
        {
          cycletime = Integer.parseInt(attrs.getValue("value"));
          // actNode.setCycletime(Integer.parseInt(attrs
          // .getValue("value")));
        }
        catch (NumberFormatException ex)
        {
          // TODO log numberformatexception
        }
      }
      else if (name.compareTo("itempath") == 0)
      {
        itempath = attrs.getValue("value");
      }
      else if (name.compareTo("itemname") == 0)
      {
        itemname = attrs.getValue("value");
      }
      else if (name.compareTo("mapping") == 0)
      {
        try
        {
          String val = attrs.getValue("value");
          mapping = XML_DA_MAPPING_TYPE.valueOf(val);
        }
        catch (IllegalArgumentException ex)
        {
          // TODO log argument exception
          ex.printStackTrace();
          mapping = XML_DA_MAPPING_TYPE.SCALAR;
        }
      }
      else if (name.compareTo("datatype") == 0)
      {
        XML_DA_DATATYPE dt = XML_DA_DATATYPE.ANY;
        int arraylength = 0;
        try
        {
          String dtvalue = attrs.getValue("value");
          dt = XML_DA_DATATYPE.valueOf(dtvalue);
        }
        catch (IllegalArgumentException ex)
        {
          // now it is possible to have an array
          String datatype = attrs.getValue("value");
          if (datatype.contains("[") && datatype.contains("]"))
          {
            // now extract array length
            String d = datatype.substring(0, datatype.indexOf("[")).trim();
            String arraycount = datatype.substring(datatype.indexOf("["));
            arraycount = arraycount.replace("[", "").replaceAll("]", "").trim();
            try
            {
              // try to parse int to integer
              arraylength = Integer.parseInt(arraycount);
              dt = XML_DA_DATATYPE.valueOf(d);
            }
            catch (NumberFormatException nex)
            {
            }
          }
        }
        // TODO:
        switch (dt)
        {
        // case BOOLEAN:
        // actNode = new XML_DA_BooleanItem();
        // break;
        // case BYTE:
        // actNode = new XML_DA_ByteItem();
        // break;
        // case SHORT:
        // actNode = new XML_DA_ShortItem();
        // break;
        // case INT:
        // actNode = new XML_DA_IntegerItem();
        // break;
        // case LONG:
        // actNode = new XML_DA_LongItem();
        // break;
        // case FLOAT:
        // actNode = new XML_DA_FloatItem();
        // break;
        // case DOUBLE:
        // actNode = new XML_DA_DoubleItem();
        // break;
        // case UNSIGNEDSHORT:
        // actNode = new XML_DA_UnsignedShortItem();
        // break;
        // case UNSIGNEDINT:
        // actNode = new XML_DA_UnsignedIntItem();
        // break;
        // case UNSIGNEDLONG:
        // actNode = new XML_DA_UnsignedLongItem();
        // break;
        default:
          actNode = new XML_DA_DPItem();
          actNode.setDataType(dt);
          break;
        }
        // if we have an arrays
        if (mapping == XML_DA_MAPPING_TYPE.SCALAR_ARRAY || mapping == XML_DA_MAPPING_TYPE.ARRAY_ARRAY)
        {
          actNode.setArraylength(arraylength);
        }
        actNode.setDataType(dt);
        actNode.setNodeId(nodeid);
        actNode.setSymbolname(browsename);
        actNode.setBrowsename(browsename);
        actNode.setActive(active);
        actNode.setCycletime(cycletime);
        actNode.setItemPath(itempath);
        actNode.setItemName(itemname);
        actNode.setMapping(mapping);
        this.nodes.add(actNode);
      }
      else if (name.compareTo("trigger") == 0)
      {
        actNode.setTriggerNode(attrs.getValue("value"));
      }
      else if (name.compareTo("description") == 0)
      {
        actNode.setDescription(attrs.getValue("value"));
      }
    }
  }
}
