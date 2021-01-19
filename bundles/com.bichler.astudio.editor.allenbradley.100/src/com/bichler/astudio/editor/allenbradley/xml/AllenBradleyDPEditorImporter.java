package com.bichler.astudio.editor.allenbradley.xml;

import com.bichler.opc.comdrv.importer.Com_TriggerDpItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AllenBradleyDPEditorImporter
{
  private Logger logger = Logger.getLogger(getClass().getName());

  public AllenBradleyDPEditorImporter()
  {
  }

  public List<AllenBradleyDPItem> loadDPs(InputStream stream, NamespaceTable uris)
  {
    try
    {
      InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
      InputSource is = new InputSource(isr);
      return loadDPs(is, uris);
    }
    catch (UnsupportedEncodingException e)
    {
      logger.log(Level.SEVERE, e.getMessage());
    }
    return null;
  }

  public List<AllenBradleyDPItem> loadDPs(InputSource stream, NamespaceTable uris)
  {
    List<AllenBradleyDPItem> dps = new ArrayList<>();
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try
    {
      SAXParser parser = factory.newSAXParser();
      DatapointsParser dpparser = new DatapointsParser(dps, uris);
      parser.parse(stream, dpparser);
    }
    catch (ParserConfigurationException | SAXException | IOException e)
    {
      logger.log(Level.SEVERE, e.getMessage());
    }
    return dps;
  }


  class DatapointsParser extends DefaultHandler
  {
    private NodeId nodeid = null;
    private String browsename = "";
    private boolean active = false;
    private int cycletime = -1;
    private int index = 0;
    private AllenBradleyDPItem actNode = null;
    private List<AllenBradleyDPItem> nodes = null;
    private String[] items = null;
    private NamespaceTable uris = null;
    private ALLENBRADLEY_MAPPING_TYPE mapping = ALLENBRADLEY_MAPPING_TYPE.SCALAR;

    public DatapointsParser(List<AllenBradleyDPItem> nodes, NamespaceTable uris)
    {
      this.nodes = nodes;
      this.uris = uris;
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException
    {
      if (name.compareTo("dp") == 0)
      {
        this.nodeid = null;
        this.browsename = null;
        this.active = false;
        this.cycletime = -1;
        this.index = 0;
        this.mapping = ALLENBRADLEY_MAPPING_TYPE.SCALAR;
        this.actNode = null;
      }
    }

    @Override
    public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException
    {
      if (name.compareTo("nodeid") == 0)
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
            }
          }
        }
        catch (NumberFormatException ex)
        {
          logger.log(Level.SEVERE, ex.getMessage());
        }
      }
      else if (name.compareTo("symbolname") == 0)
      {
        browsename = attrs.getValue("value");
      }
      else if (name.compareTo("isactive") == 0)
      {
        active = Boolean.parseBoolean(attrs.getValue("value"));
      }
      else if (name.compareTo("cycletime") == 0)
      {
        try
        {
          cycletime = Integer.parseInt(attrs.getValue("value"));
        }
        catch (NumberFormatException ex)
        {
          logger.log(Level.SEVERE, ex.getMessage());
        }
      }
      else if (name.compareTo("mapping") == 0)
      {
        try
        {
          mapping = ALLENBRADLEY_MAPPING_TYPE.valueOf(attrs.getValue("value"));
        }
        catch (NumberFormatException nfe)
        {
          logger.log(Level.SEVERE, nfe.getMessage());
        }
      }
      else if (name.compareTo("index") == 0)
      {
        try
        {
          index = Integer.parseInt(attrs.getValue("value"));
        }
        catch (NumberFormatException ex)
        {
          logger.log(Level.SEVERE, ex.getMessage());
        }
      }
      else if (name.compareTo("datatype") == 0)
      {
        String datatype = attrs.getValue("value");
        actNode = new AllenBradleyDPItem();
        actNode.setDataType(datatype);
        actNode.setNodeId(nodeid);
        actNode.setSymbolname(browsename);
        actNode.setBrowsename(browsename);
        actNode.setActive(active);
        actNode.setCycletime(cycletime);
        actNode.setIndex(index);
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

  /**
   * creates and returns a list with all dps which trigger an read for other
   * datapoints
   * 
   * @param stream
   * @param uris
   * @return
   */
  public List<Com_TriggerDpItem> loadTriggerDPs(InputStream stream, NamespaceTable uris)
  {
    List<Com_TriggerDpItem> dps = new ArrayList<>();
    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      String line = "";
      String[] items = null;
      while ((line = reader.readLine()) != null)
      {
        try
        {
          items = line.split("\t");
          if (items != null && items.length == 2)
          {
            // we also need to get the namespace index from server
            String[] nitems = items[0].split(";");
            if (nitems != null && nitems.length == 2)
            {
              // now create node to tigger
              Com_TriggerDpItem node = new Com_TriggerDpItem();
              node.setNodesToRead(new ArrayList<NodeId>());
              int nsindex = uris.getIndex(nitems[0].replace("ns=", ""));
              if (nsindex != -1)
              {
                node.setNodeId(NodeId.parseNodeId("ns=" + nsindex + ";" + nitems[1]));
                node.setActive(Boolean.parseBoolean(items[1]));
                dps.add(node);
              }
            }
          }
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, ex.getMessage());
        }
      }
    }
    catch (IOException e)
    {
      logger.log(Level.SEVERE, e.getMessage());
    }
    return dps;
  }
}
