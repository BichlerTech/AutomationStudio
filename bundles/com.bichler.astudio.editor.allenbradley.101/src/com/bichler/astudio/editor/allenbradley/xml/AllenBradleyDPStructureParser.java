package com.bichler.astudio.editor.allenbradley.xml;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.editor.allenbradley.model.AbstractAllenBradleyNode;
import com.bichler.astudio.editor.allenbradley.model.AllenBradleyStructNode;

public class AllenBradleyDPStructureParser extends DefaultHandler
{
  // attributes
  private static final String ATTRIBUTE_NAME = "Name";
  private static final String ATTRIBUTE_DATATYPE = "DataType";
  // private static final String ATTRIBUTE_DIMENSION = "Dimension";
  // private static final String ATTRIBUTE_RADIX = "Radix";
  private static final String ATTRIBUTE_TYPE = "Type";
  // structure tags
  // private static final String TAG_CONTROLLER = "Controller";
  // private static final String TAG_DATATYPES = "DataTypes";
  private static final String TAG_DATATYPE = "DataType";
  // private static final String TAG_DEPENDANCIES = "Dependancies";
  private static final String TAG_DEPENDANCY = "Dependancy";
  // private static final String TAG_MEMBERS = "Members";
  private static final String TAG_MEMBER = "Member";
  private List<AbstractAllenBradleyNode> structures;
  // private AbstractallenbradleyNode root = null;
  private AbstractAllenBradleyNode current = null;
  private AllenBradleyDBResourceManager structManager;

  public AllenBradleyDPStructureParser(List<AbstractAllenBradleyNode> structures,
      AllenBradleyDBResourceManager structManager)
  {
    this.structures = structures;
    this.structManager = structManager;
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if (qName == null)
    {
      return;
    }
    switch (qName)
    {
    case TAG_DATATYPE:
      this.structures.add(this.current);
      this.current = null;
      break;
    }
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    if (qName == null)
    {
      return;
    }
    switch (qName)
    {
    case TAG_DATATYPE:
      this.current = new AllenBradleyStructNode(structManager);
      current.setName(attributes.getValue(ATTRIBUTE_NAME));
      break;
    case TAG_MEMBER:
      AllenBradleyStructNode member = new AllenBradleyStructNode(structManager);
      member.setName(attributes.getValue(ATTRIBUTE_NAME));
      member.setDataType(attributes.getValue(ATTRIBUTE_DATATYPE));
      // member.setDimension(attributes.getValue(ATTRIBUTE_DIMENSION));
      this.current.addMember(member);
      break;
    case TAG_DEPENDANCY:
      AllenBradleyStructNode dependancy = new AllenBradleyStructNode(structManager);
      dependancy.setName(attributes.getValue(ATTRIBUTE_NAME));
      dependancy.setDependancyType(attributes.getValue(ATTRIBUTE_TYPE));
      this.current.addDependancy(dependancy);
      break;
    }
  }
}
