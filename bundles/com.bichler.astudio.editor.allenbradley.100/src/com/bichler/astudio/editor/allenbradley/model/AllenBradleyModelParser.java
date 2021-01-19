package com.bichler.astudio.editor.allenbradley.model;

import java.util.HashMap;
import java.util.Map;

import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;

public class AllenBradleyModelParser
{
  private static final String SEPERATOR_CSV = ",";
  private AbstractAllenBradleyNode root = null;
  private int lineCount = 0;
  private AllenBradleyDBResourceManager structManager;
  // private Map<allenbradleyStructNode, allenbradleyArrayNode> arrays = new
  // HashMap<>();

  // private float calculatedIndex = 0.0f;
  protected AllenBradleyModelParser(AllenBradleyDBResourceManager structManager)
  {
    this.root = new AllenBradleyStructNode(structManager);
    this.structManager = structManager;
  }

  public void readLine(String line)
  {
    // if(line.startsWith("\"")){
    // line.replaceAll("\"", "");
    // }
    parseLine(line);
    // if (this.lineCount == 1) {
    // parseFirstLine(line);
    // } else if (this.lineCount == 2) {
    // parseSecondLine(line);
    // } else {
    // parse(line);
    // }
  }

  // private void parse(String line) {
  // parseLine(line);
  // if (line.startsWith("+")) {
  // parsePlus(line, true);
  // } else if (line.startsWith("=")) {
  // parseEqual(line);
  // } else if (line.startsWith("*")) {
  // parseStar(line);
  // }
  // /**
  // * "First" line (+) is missing
  // */
  // else {
  // parsePlus(line, false);
  // }
  // }
  private void parseLine(String line)
  {
    if (line == null)
    {
      return;
    }
    String[] split = line.split(SEPERATOR_CSV);
    if (split.length < 7)
    {
      // skip header lines
      return;
    }
    Map<Integer, String> texts = new HashMap<>();
    findTexts(line, texts);
    // skip header line
    lineCount++;
    if (this.lineCount == 1)
    {
      return;
    }
    String tag = null;
    // String scope = null;
    String name = null;
    String description = null;
    String datatype = null;
    // String specifier = null;
    // String attributes = null;
    for (int i = 0; i < 7; i++)
    {
      String text = texts.get(i);
      if (text == null)
      {
        text = split[i];
      }
      switch (i)
      {
      case 0:
        tag = text;
        break;
      // case 1:
      // scope = text;
      // break;
      case 2:
        name = text;
        break;
      case 3:
        description = text;
        break;
      case 4:
        datatype = text;
        break;
      // case 5:
      // specifier = text;
      // break;
      // case 6:
      // attributes = text;
      // break;
      }
    }
    AllenBradleyStructNode node = new AllenBradleyStructNode(structManager);
    node.setTag(tag);
    // node.setScope(scope);
    node.setName(name);
    node.setDescription(description);
    node.setDataType(datatype);
    // node.setSpecifier(specifier);
    // node.setAttributes(attributes);
    this.root.addMember(node);
  }

  private void findTexts(String line, Map<Integer, String> texts)
  {
    char[] chars = line.toCharArray();
    boolean isField = false;
    int fieldnumber = 0;
    String field = "";
    for (char character : chars)
    {
      switch (character)
      {
      case ',':
        // increase fieldnumber
        if (!isField)
        {
          fieldnumber++;
        }
        break;
      case '"':
        // enable " field
        if (!isField)
        {
          isField = true;
        }
        // disable " field and add to string
        else
        {
          isField = false;
          texts.put(fieldnumber, field);
          field = "";
        }
        break;
      default:
        if (isField)
        {
          field += character;
        }
        break;
      }
    }
  }

  public AbstractAllenBradleyNode generate()
  {
    // this.root.setName(this.rootName);
    // this.root.setRoot(true);
    // if (isDBFile) {
    generateSymbolNames(this.root.getMembers());
    // }
    return this.root;
  }

  /**
   * Generate all unique symbol node names
   * 
   * @param root
   */
  private void generateSymbolNames(AbstractAllenBradleyNode[] root)
  {
    for (AbstractAllenBradleyNode node : root)
    {
      String member = node.getName();
      String dataType = node.getDataType();
      boolean skipNoTag = false;
      if (node instanceof AllenBradleyStructNode)
      {
        String tag = ((AllenBradleyStructNode) node).getTag();
        if (tag != null)
        {
          switch (tag)
          {
          case "TAG":
            break;
          default:
            skipNoTag = true;
            break;
          }
        }
      }
      if (skipNoTag)
      {
        continue;
      }
      // node can be array
      if (!(node instanceof AllenBradleyArrayNode))
      {
        // check for complex
        AbstractAllenBradleyNode complexStruct = structManager.getStructure(dataType);
        // add cloned complex structure in array
        if (complexStruct != null)
        {
          AbstractAllenBradleyNode clonedStruct = complexStruct.cloneNode(true);
          for (AbstractAllenBradleyNode complexMember : clonedStruct.getMembers())
          {
            String complexname = node.getName() + "." + complexMember.getName();
            complexMember.setName(complexname);
            node.addMember(complexMember);
          }
        }
        // add default array
        else
        {
          int arrayStart = dataType.indexOf("[");
          int arrayEnd = dataType.indexOf("]");
          // array
          if (arrayStart >= 0 && arrayEnd >= 0)
          {
            String arrayindex = dataType.substring(arrayStart + 1, arrayEnd);
            String arraytype = dataType.substring(0, arrayStart);
            Integer length = Integer.parseInt(arrayindex);
            for (int i = 0; i < length; i++)
            {
              AbstractAllenBradleyNode arrayChild = new AllenBradleyArrayNode(structManager);
              arrayChild.setName(member + "[" + i + "]");
              arrayChild.setDataType(arraytype);
              node.addMember(arrayChild);
            }
          }
        }
      }
      // complex or default datatype
      else
      {
        // check for complex
        AbstractAllenBradleyNode complexStruct = structManager.getStructure(dataType);
        // add cloned complex structure
        if (complexStruct != null)
        {
          AbstractAllenBradleyNode clonedStruct = complexStruct.cloneNode(true);
          node.addMember(clonedStruct);
        }
      }
      // rek
      generateSymbolNames(node.getMembers());
    }
  }
  // private String escape(String input) {
  // StringBuffer output = new StringBuffer();
  // for (int i = 0; i < input.length(); i++) {
  // if ((int) input.charAt(i) > 32 && (int) input.charAt(i) < 126) {
  // output.append(input.charAt(i));
  // }
  // }
  //
  // return output.toString().replace("Š", "ae").replace("€", "Ae")
  // .replace("š", "oe").replace("…", "Oe").replace("Ÿ", "ue")
  // .replace("†", "Ue").replace("§", "ss").replace("\"", "");
  // }
}
