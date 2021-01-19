package com.bichler.astudio.editor.allenbradley.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDPStructureParser;

public class AllenBradleyNodeFactory
{
  // private static final String SEPERATOR_CSV = ";";
  // private static final String DATATYPE_ARRAY = "ARRAY";
  public AllenBradleyNodeFactory()
  {
  }

  public AbstractAllenBradleyNode parseCSV(BufferedReader reader, AllenBradleyDBResourceManager structManager)
  {
    try
    {
      AllenBradleyModelParser parser = new AllenBradleyModelParser(structManager);
      String line = "";
      while ((line = reader.readLine()) != null)
      {
        parser.readLine(line);
      }
      return parser.generate();
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
    return null;
  }

  public List<AbstractAllenBradleyNode> parseL5X(InputStream reader, AllenBradleyDBResourceManager structManager)
  {
    List<AbstractAllenBradleyNode> structures = new ArrayList<>();
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try
    {
      SAXParser parser = factory.newSAXParser();
      AllenBradleyDPStructureParser handler = new AllenBradleyDPStructureParser(structures, structManager);
      parser.parse(reader, handler);
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
    return structures;
  }
}
