/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.ArrayDeclaration;
import com.bichler.iec.iec.ArrayInitialElements;
import com.bichler.iec.iec.ArrayInitialization;
import com.bichler.iec.iec.ArrayVariable;
import com.bichler.iec.iec.AssignStatement;
import com.bichler.iec.iec.BinaryExpression;
import com.bichler.iec.iec.BitString;
import com.bichler.iec.iec.BitStringType;
import com.bichler.iec.iec.CaseElement;
import com.bichler.iec.iec.CaseList;
import com.bichler.iec.iec.CaseListElement;
import com.bichler.iec.iec.CaseStatement;
import com.bichler.iec.iec.CharacterString;
import com.bichler.iec.iec.ConfigurationDeclaration;
import com.bichler.iec.iec.Constant;
import com.bichler.iec.iec.DataSink;
import com.bichler.iec.iec.DataSource;
import com.bichler.iec.iec.DataType;
import com.bichler.iec.iec.DataTypeDeclaration;
import com.bichler.iec.iec.DateAndTimeLiteral;
import com.bichler.iec.iec.DateLiteral;
import com.bichler.iec.iec.DateType;
import com.bichler.iec.iec.DeclSpecification;
import com.bichler.iec.iec.DerivedType;
import com.bichler.iec.iec.DirectVariable;
import com.bichler.iec.iec.DurationLiteral;
import com.bichler.iec.iec.EdgeDeclSpecification;
import com.bichler.iec.iec.EdgeDeclaration;
import com.bichler.iec.iec.ElementaryType;
import com.bichler.iec.iec.ElseIf;
import com.bichler.iec.iec.EnumDeclaration;
import com.bichler.iec.iec.EnumeratedValue;
import com.bichler.iec.iec.Enumeration;
import com.bichler.iec.iec.Expression;
import com.bichler.iec.iec.ExpressionOperation;
import com.bichler.iec.iec.FBTask;
import com.bichler.iec.iec.FunctionBlockBody;
import com.bichler.iec.iec.FunctionBlockDeclaration;
import com.bichler.iec.iec.FunctionBlockVarDeclarations;
import com.bichler.iec.iec.FunctionBody;
import com.bichler.iec.iec.FunctionDeclaration;
import com.bichler.iec.iec.GenericType;
import com.bichler.iec.iec.GlobalVar;
import com.bichler.iec.iec.GlobalVarDecl;
import com.bichler.iec.iec.GlobalVarDeclarations;
import com.bichler.iec.iec.GlobalVarList;
import com.bichler.iec.iec.GlobalVarSpec;
import com.bichler.iec.iec.IecFactory;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.IfStatement;
import com.bichler.iec.iec.InitialElement;
import com.bichler.iec.iec.InitializedStructure;
import com.bichler.iec.iec.InputDeclaration;
import com.bichler.iec.iec.InputDeclarations;
import com.bichler.iec.iec.InputOutputDeclarations;
import com.bichler.iec.iec.Instruction;
import com.bichler.iec.iec.InstructionList;
import com.bichler.iec.iec.IntegerLiteral;
import com.bichler.iec.iec.IntegerType;
import com.bichler.iec.iec.IoVarDeclarations;
import com.bichler.iec.iec.JmpOperation;
import com.bichler.iec.iec.Label;
import com.bichler.iec.iec.LibraryElement;
import com.bichler.iec.iec.LibraryElementDeclaration;
import com.bichler.iec.iec.LocatedVarDeclaration;
import com.bichler.iec.iec.LocatedVarDeclarations;
import com.bichler.iec.iec.Location;
import com.bichler.iec.iec.Model;
import com.bichler.iec.iec.ModelElement;
import com.bichler.iec.iec.MultiElementVariable;
import com.bichler.iec.iec.NamedVariableAccess;
import com.bichler.iec.iec.NonGenericType;
import com.bichler.iec.iec.NumericLiteral;
import com.bichler.iec.iec.NumericType;
import com.bichler.iec.iec.Operand;
import com.bichler.iec.iec.Operation;
import com.bichler.iec.iec.OtherVarDeclarations;
import com.bichler.iec.iec.OutputDeclarations;
import com.bichler.iec.iec.ParamAssignment;
import com.bichler.iec.iec.PlainIntegerType;
import com.bichler.iec.iec.ProgCNXN;
import com.bichler.iec.iec.ProgConfElement;
import com.bichler.iec.iec.ProgConfElements;
import com.bichler.iec.iec.ProgDataSource;
import com.bichler.iec.iec.ProgramAccessDecl;
import com.bichler.iec.iec.ProgramAccessDecls;
import com.bichler.iec.iec.ProgramConfiguration;
import com.bichler.iec.iec.ProgramDeclaration;
import com.bichler.iec.iec.ProgramVarDeclarations;
import com.bichler.iec.iec.RangeDeclaration;
import com.bichler.iec.iec.RealLiteral;
import com.bichler.iec.iec.RealType;
import com.bichler.iec.iec.ReferencedOperand;
import com.bichler.iec.iec.ResourceDeclaration;
import com.bichler.iec.iec.SelectionStatement;
import com.bichler.iec.iec.SignedIntegerType;
import com.bichler.iec.iec.SimpleInstruction;
import com.bichler.iec.iec.SimpleInstructionList;
import com.bichler.iec.iec.SimpleOperation;
import com.bichler.iec.iec.SimpleTypeDeclaration;
import com.bichler.iec.iec.SingleResourceDeclaration;
import com.bichler.iec.iec.SpecInit;
import com.bichler.iec.iec.Statement;
import com.bichler.iec.iec.StatementList;
import com.bichler.iec.iec.StringDeclaration;
import com.bichler.iec.iec.StringType;
import com.bichler.iec.iec.StructureDeclaration;
import com.bichler.iec.iec.StructureElementDeclaration;
import com.bichler.iec.iec.StructureElementInitialization;
import com.bichler.iec.iec.StructureInitialization;
import com.bichler.iec.iec.StructureTypeDeclaration;
import com.bichler.iec.iec.StructuredVariable;
import com.bichler.iec.iec.SymbolicVariableAccess;
import com.bichler.iec.iec.TaskConfiguration;
import com.bichler.iec.iec.TaskInitialization;
import com.bichler.iec.iec.TimeLiteral;
import com.bichler.iec.iec.TimeOfDayLiteral;
import com.bichler.iec.iec.TypeDeclaration;
import com.bichler.iec.iec.UnaryExpression;
import com.bichler.iec.iec.UnsignedIntegerType;
import com.bichler.iec.iec.Var1List;
import com.bichler.iec.iec.VarDeclSpecification;
import com.bichler.iec.iec.VarDeclarations;
import com.bichler.iec.iec.VarInitDecl;
import com.bichler.iec.iec.Variable;
import com.bichler.iec.iec.VariableAccess;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class IecFactoryImpl extends EFactoryImpl implements IecFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IecFactory init()
  {
    try
    {
      IecFactory theIecFactory = (IecFactory)EPackage.Registry.INSTANCE.getEFactory(IecPackage.eNS_URI);
      if (theIecFactory != null)
      {
        return theIecFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new IecFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IecFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case IecPackage.MODEL: return createModel();
      case IecPackage.MODEL_ELEMENT: return createModelElement();
      case IecPackage.LIBRARY_ELEMENT: return createLibraryElement();
      case IecPackage.LIBRARY_ELEMENT_DECLARATION: return createLibraryElementDeclaration();
      case IecPackage.DATA_TYPE: return createDataType();
      case IecPackage.NON_GENERIC_TYPE: return createNonGenericType();
      case IecPackage.ELEMENTARY_TYPE: return createElementaryType();
      case IecPackage.STRING_TYPE: return createStringType();
      case IecPackage.NUMERIC_TYPE: return createNumericType();
      case IecPackage.INTEGER_TYPE: return createIntegerType();
      case IecPackage.SIGNED_INTEGER_TYPE: return createSignedIntegerType();
      case IecPackage.PLAIN_INTEGER_TYPE: return createPlainIntegerType();
      case IecPackage.UNSIGNED_INTEGER_TYPE: return createUnsignedIntegerType();
      case IecPackage.REAL_TYPE: return createRealType();
      case IecPackage.DATE_TYPE: return createDateType();
      case IecPackage.BIT_STRING_TYPE: return createBitStringType();
      case IecPackage.GENERIC_TYPE: return createGenericType();
      case IecPackage.DERIVED_TYPE: return createDerivedType();
      case IecPackage.DATA_TYPE_DECLARATION: return createDataTypeDeclaration();
      case IecPackage.TYPE_DECLARATION: return createTypeDeclaration();
      case IecPackage.SIMPLE_TYPE_DECLARATION: return createSimpleTypeDeclaration();
      case IecPackage.SPEC_INIT: return createSpecInit();
      case IecPackage.RANGE_DECLARATION: return createRangeDeclaration();
      case IecPackage.ENUM_DECLARATION: return createEnumDeclaration();
      case IecPackage.ARRAY_DECLARATION: return createArrayDeclaration();
      case IecPackage.ENUMERATION: return createEnumeration();
      case IecPackage.ENUMERATED_VALUE: return createEnumeratedValue();
      case IecPackage.ARRAY_INITIALIZATION: return createArrayInitialization();
      case IecPackage.ARRAY_INITIAL_ELEMENTS: return createArrayInitialElements();
      case IecPackage.INITIAL_ELEMENT: return createInitialElement();
      case IecPackage.STRUCTURE_TYPE_DECLARATION: return createStructureTypeDeclaration();
      case IecPackage.STRUCTURE_DECLARATION: return createStructureDeclaration();
      case IecPackage.STRUCTURE_ELEMENT_DECLARATION: return createStructureElementDeclaration();
      case IecPackage.INITIALIZED_STRUCTURE: return createInitializedStructure();
      case IecPackage.STRUCTURE_INITIALIZATION: return createStructureInitialization();
      case IecPackage.STRUCTURE_ELEMENT_INITIALIZATION: return createStructureElementInitialization();
      case IecPackage.STRING_DECLARATION: return createStringDeclaration();
      case IecPackage.VARIABLE: return createVariable();
      case IecPackage.VARIABLE_ACCESS: return createVariableAccess();
      case IecPackage.DIRECT_VARIABLE: return createDirectVariable();
      case IecPackage.SYMBOLIC_VARIABLE_ACCESS: return createSymbolicVariableAccess();
      case IecPackage.NAMED_VARIABLE_ACCESS: return createNamedVariableAccess();
      case IecPackage.MULTI_ELEMENT_VARIABLE: return createMultiElementVariable();
      case IecPackage.ARRAY_VARIABLE: return createArrayVariable();
      case IecPackage.STRUCTURED_VARIABLE: return createStructuredVariable();
      case IecPackage.EXPRESSION: return createExpression();
      case IecPackage.PARAM_ASSIGNMENT: return createParamAssignment();
      case IecPackage.FUNCTION_DECLARATION: return createFunctionDeclaration();
      case IecPackage.IO_VAR_DECLARATIONS: return createIoVarDeclarations();
      case IecPackage.INPUT_DECLARATIONS: return createInputDeclarations();
      case IecPackage.INPUT_DECLARATION: return createInputDeclaration();
      case IecPackage.EDGE_DECLARATION: return createEdgeDeclaration();
      case IecPackage.DECL_SPECIFICATION: return createDeclSpecification();
      case IecPackage.VAR_DECL_SPECIFICATION: return createVarDeclSpecification();
      case IecPackage.EDGE_DECL_SPECIFICATION: return createEdgeDeclSpecification();
      case IecPackage.VAR1_LIST: return createVar1List();
      case IecPackage.OUTPUT_DECLARATIONS: return createOutputDeclarations();
      case IecPackage.VAR_INIT_DECL: return createVarInitDecl();
      case IecPackage.INPUT_OUTPUT_DECLARATIONS: return createInputOutputDeclarations();
      case IecPackage.FUNCTION_BODY: return createFunctionBody();
      case IecPackage.INSTRUCTION_LIST: return createInstructionList();
      case IecPackage.INSTRUCTION: return createInstruction();
      case IecPackage.LABEL: return createLabel();
      case IecPackage.OPERATION: return createOperation();
      case IecPackage.SIMPLE_OPERATION: return createSimpleOperation();
      case IecPackage.EXPRESSION_OPERATION: return createExpressionOperation();
      case IecPackage.JMP_OPERATION: return createJmpOperation();
      case IecPackage.OPERAND: return createOperand();
      case IecPackage.REFERENCED_OPERAND: return createReferencedOperand();
      case IecPackage.SIMPLE_INSTRUCTION_LIST: return createSimpleInstructionList();
      case IecPackage.SIMPLE_INSTRUCTION: return createSimpleInstruction();
      case IecPackage.STATEMENT_LIST: return createStatementList();
      case IecPackage.STATEMENT: return createStatement();
      case IecPackage.ASSIGN_STATEMENT: return createAssignStatement();
      case IecPackage.SELECTION_STATEMENT: return createSelectionStatement();
      case IecPackage.IF_STATEMENT: return createIfStatement();
      case IecPackage.ELSE_IF: return createElseIf();
      case IecPackage.CASE_STATEMENT: return createCaseStatement();
      case IecPackage.CASE_ELEMENT: return createCaseElement();
      case IecPackage.CASE_LIST: return createCaseList();
      case IecPackage.CASE_LIST_ELEMENT: return createCaseListElement();
      case IecPackage.FUNCTION_BLOCK_DECLARATION: return createFunctionBlockDeclaration();
      case IecPackage.FUNCTION_BLOCK_VAR_DECLARATIONS: return createFunctionBlockVarDeclarations();
      case IecPackage.OTHER_VAR_DECLARATIONS: return createOtherVarDeclarations();
      case IecPackage.VAR_DECLARATIONS: return createVarDeclarations();
      case IecPackage.FUNCTION_BLOCK_BODY: return createFunctionBlockBody();
      case IecPackage.PROGRAM_DECLARATION: return createProgramDeclaration();
      case IecPackage.PROGRAM_VAR_DECLARATIONS: return createProgramVarDeclarations();
      case IecPackage.LOCATED_VAR_DECLARATIONS: return createLocatedVarDeclarations();
      case IecPackage.LOCATED_VAR_DECLARATION: return createLocatedVarDeclaration();
      case IecPackage.LOCATION: return createLocation();
      case IecPackage.PROGRAM_ACCESS_DECLS: return createProgramAccessDecls();
      case IecPackage.PROGRAM_ACCESS_DECL: return createProgramAccessDecl();
      case IecPackage.CONFIGURATION_DECLARATION: return createConfigurationDeclaration();
      case IecPackage.RESOURCE_DECLARATION: return createResourceDeclaration();
      case IecPackage.SINGLE_RESOURCE_DECLARATION: return createSingleResourceDeclaration();
      case IecPackage.PROGRAM_CONFIGURATION: return createProgramConfiguration();
      case IecPackage.PROG_CONF_ELEMENTS: return createProgConfElements();
      case IecPackage.PROG_CONF_ELEMENT: return createProgConfElement();
      case IecPackage.FB_TASK: return createFBTask();
      case IecPackage.PROG_CNXN: return createProgCNXN();
      case IecPackage.DATA_SINK: return createDataSink();
      case IecPackage.PROG_DATA_SOURCE: return createProgDataSource();
      case IecPackage.GLOBAL_VAR_DECLARATIONS: return createGlobalVarDeclarations();
      case IecPackage.GLOBAL_VAR_DECL: return createGlobalVarDecl();
      case IecPackage.GLOBAL_VAR_SPEC: return createGlobalVarSpec();
      case IecPackage.GLOBAL_VAR_LIST: return createGlobalVarList();
      case IecPackage.GLOBAL_VAR: return createGlobalVar();
      case IecPackage.TASK_CONFIGURATION: return createTaskConfiguration();
      case IecPackage.TASK_INITIALIZATION: return createTaskInitialization();
      case IecPackage.DATA_SOURCE: return createDataSource();
      case IecPackage.CONSTANT: return createConstant();
      case IecPackage.NUMERIC_LITERAL: return createNumericLiteral();
      case IecPackage.INTEGER_LITERAL: return createIntegerLiteral();
      case IecPackage.REAL_LITERAL: return createRealLiteral();
      case IecPackage.CHARACTER_STRING: return createCharacterString();
      case IecPackage.BIT_STRING: return createBitString();
      case IecPackage.BOOLEAN: return createBoolean();
      case IecPackage.TIME_LITERAL: return createTimeLiteral();
      case IecPackage.DURATION_LITERAL: return createDurationLiteral();
      case IecPackage.TIME_OF_DAY_LITERAL: return createTimeOfDayLiteral();
      case IecPackage.DATE_LITERAL: return createDateLiteral();
      case IecPackage.DATE_AND_TIME_LITERAL: return createDateAndTimeLiteral();
      case IecPackage.BINARY_EXPRESSION: return createBinaryExpression();
      case IecPackage.UNARY_EXPRESSION: return createUnaryExpression();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Model createModel()
  {
    ModelImpl model = new ModelImpl();
    return model;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ModelElement createModelElement()
  {
    ModelElementImpl modelElement = new ModelElementImpl();
    return modelElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public LibraryElement createLibraryElement()
  {
    LibraryElementImpl libraryElement = new LibraryElementImpl();
    return libraryElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public LibraryElementDeclaration createLibraryElementDeclaration()
  {
    LibraryElementDeclarationImpl libraryElementDeclaration = new LibraryElementDeclarationImpl();
    return libraryElementDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DataType createDataType()
  {
    DataTypeImpl dataType = new DataTypeImpl();
    return dataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NonGenericType createNonGenericType()
  {
    NonGenericTypeImpl nonGenericType = new NonGenericTypeImpl();
    return nonGenericType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ElementaryType createElementaryType()
  {
    ElementaryTypeImpl elementaryType = new ElementaryTypeImpl();
    return elementaryType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StringType createStringType()
  {
    StringTypeImpl stringType = new StringTypeImpl();
    return stringType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NumericType createNumericType()
  {
    NumericTypeImpl numericType = new NumericTypeImpl();
    return numericType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerType createIntegerType()
  {
    IntegerTypeImpl integerType = new IntegerTypeImpl();
    return integerType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SignedIntegerType createSignedIntegerType()
  {
    SignedIntegerTypeImpl signedIntegerType = new SignedIntegerTypeImpl();
    return signedIntegerType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PlainIntegerType createPlainIntegerType()
  {
    PlainIntegerTypeImpl plainIntegerType = new PlainIntegerTypeImpl();
    return plainIntegerType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public UnsignedIntegerType createUnsignedIntegerType()
  {
    UnsignedIntegerTypeImpl unsignedIntegerType = new UnsignedIntegerTypeImpl();
    return unsignedIntegerType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RealType createRealType()
  {
    RealTypeImpl realType = new RealTypeImpl();
    return realType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DateType createDateType()
  {
    DateTypeImpl dateType = new DateTypeImpl();
    return dateType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BitStringType createBitStringType()
  {
    BitStringTypeImpl bitStringType = new BitStringTypeImpl();
    return bitStringType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GenericType createGenericType()
  {
    GenericTypeImpl genericType = new GenericTypeImpl();
    return genericType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DerivedType createDerivedType()
  {
    DerivedTypeImpl derivedType = new DerivedTypeImpl();
    return derivedType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DataTypeDeclaration createDataTypeDeclaration()
  {
    DataTypeDeclarationImpl dataTypeDeclaration = new DataTypeDeclarationImpl();
    return dataTypeDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TypeDeclaration createTypeDeclaration()
  {
    TypeDeclarationImpl typeDeclaration = new TypeDeclarationImpl();
    return typeDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SimpleTypeDeclaration createSimpleTypeDeclaration()
  {
    SimpleTypeDeclarationImpl simpleTypeDeclaration = new SimpleTypeDeclarationImpl();
    return simpleTypeDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SpecInit createSpecInit()
  {
    SpecInitImpl specInit = new SpecInitImpl();
    return specInit;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RangeDeclaration createRangeDeclaration()
  {
    RangeDeclarationImpl rangeDeclaration = new RangeDeclarationImpl();
    return rangeDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EnumDeclaration createEnumDeclaration()
  {
    EnumDeclarationImpl enumDeclaration = new EnumDeclarationImpl();
    return enumDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ArrayDeclaration createArrayDeclaration()
  {
    ArrayDeclarationImpl arrayDeclaration = new ArrayDeclarationImpl();
    return arrayDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Enumeration createEnumeration()
  {
    EnumerationImpl enumeration = new EnumerationImpl();
    return enumeration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EnumeratedValue createEnumeratedValue()
  {
    EnumeratedValueImpl enumeratedValue = new EnumeratedValueImpl();
    return enumeratedValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ArrayInitialization createArrayInitialization()
  {
    ArrayInitializationImpl arrayInitialization = new ArrayInitializationImpl();
    return arrayInitialization;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ArrayInitialElements createArrayInitialElements()
  {
    ArrayInitialElementsImpl arrayInitialElements = new ArrayInitialElementsImpl();
    return arrayInitialElements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public InitialElement createInitialElement()
  {
    InitialElementImpl initialElement = new InitialElementImpl();
    return initialElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StructureTypeDeclaration createStructureTypeDeclaration()
  {
    StructureTypeDeclarationImpl structureTypeDeclaration = new StructureTypeDeclarationImpl();
    return structureTypeDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StructureDeclaration createStructureDeclaration()
  {
    StructureDeclarationImpl structureDeclaration = new StructureDeclarationImpl();
    return structureDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StructureElementDeclaration createStructureElementDeclaration()
  {
    StructureElementDeclarationImpl structureElementDeclaration = new StructureElementDeclarationImpl();
    return structureElementDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public InitializedStructure createInitializedStructure()
  {
    InitializedStructureImpl initializedStructure = new InitializedStructureImpl();
    return initializedStructure;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StructureInitialization createStructureInitialization()
  {
    StructureInitializationImpl structureInitialization = new StructureInitializationImpl();
    return structureInitialization;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StructureElementInitialization createStructureElementInitialization()
  {
    StructureElementInitializationImpl structureElementInitialization = new StructureElementInitializationImpl();
    return structureElementInitialization;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StringDeclaration createStringDeclaration()
  {
    StringDeclarationImpl stringDeclaration = new StringDeclarationImpl();
    return stringDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Variable createVariable()
  {
    VariableImpl variable = new VariableImpl();
    return variable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VariableAccess createVariableAccess()
  {
    VariableAccessImpl variableAccess = new VariableAccessImpl();
    return variableAccess;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DirectVariable createDirectVariable()
  {
    DirectVariableImpl directVariable = new DirectVariableImpl();
    return directVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SymbolicVariableAccess createSymbolicVariableAccess()
  {
    SymbolicVariableAccessImpl symbolicVariableAccess = new SymbolicVariableAccessImpl();
    return symbolicVariableAccess;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NamedVariableAccess createNamedVariableAccess()
  {
    NamedVariableAccessImpl namedVariableAccess = new NamedVariableAccessImpl();
    return namedVariableAccess;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MultiElementVariable createMultiElementVariable()
  {
    MultiElementVariableImpl multiElementVariable = new MultiElementVariableImpl();
    return multiElementVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ArrayVariable createArrayVariable()
  {
    ArrayVariableImpl arrayVariable = new ArrayVariableImpl();
    return arrayVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StructuredVariable createStructuredVariable()
  {
    StructuredVariableImpl structuredVariable = new StructuredVariableImpl();
    return structuredVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Expression createExpression()
  {
    ExpressionImpl expression = new ExpressionImpl();
    return expression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ParamAssignment createParamAssignment()
  {
    ParamAssignmentImpl paramAssignment = new ParamAssignmentImpl();
    return paramAssignment;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FunctionDeclaration createFunctionDeclaration()
  {
    FunctionDeclarationImpl functionDeclaration = new FunctionDeclarationImpl();
    return functionDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IoVarDeclarations createIoVarDeclarations()
  {
    IoVarDeclarationsImpl ioVarDeclarations = new IoVarDeclarationsImpl();
    return ioVarDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public InputDeclarations createInputDeclarations()
  {
    InputDeclarationsImpl inputDeclarations = new InputDeclarationsImpl();
    return inputDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public InputDeclaration createInputDeclaration()
  {
    InputDeclarationImpl inputDeclaration = new InputDeclarationImpl();
    return inputDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EdgeDeclaration createEdgeDeclaration()
  {
    EdgeDeclarationImpl edgeDeclaration = new EdgeDeclarationImpl();
    return edgeDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DeclSpecification createDeclSpecification()
  {
    DeclSpecificationImpl declSpecification = new DeclSpecificationImpl();
    return declSpecification;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VarDeclSpecification createVarDeclSpecification()
  {
    VarDeclSpecificationImpl varDeclSpecification = new VarDeclSpecificationImpl();
    return varDeclSpecification;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EdgeDeclSpecification createEdgeDeclSpecification()
  {
    EdgeDeclSpecificationImpl edgeDeclSpecification = new EdgeDeclSpecificationImpl();
    return edgeDeclSpecification;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Var1List createVar1List()
  {
    Var1ListImpl var1List = new Var1ListImpl();
    return var1List;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public OutputDeclarations createOutputDeclarations()
  {
    OutputDeclarationsImpl outputDeclarations = new OutputDeclarationsImpl();
    return outputDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VarInitDecl createVarInitDecl()
  {
    VarInitDeclImpl varInitDecl = new VarInitDeclImpl();
    return varInitDecl;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public InputOutputDeclarations createInputOutputDeclarations()
  {
    InputOutputDeclarationsImpl inputOutputDeclarations = new InputOutputDeclarationsImpl();
    return inputOutputDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FunctionBody createFunctionBody()
  {
    FunctionBodyImpl functionBody = new FunctionBodyImpl();
    return functionBody;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public InstructionList createInstructionList()
  {
    InstructionListImpl instructionList = new InstructionListImpl();
    return instructionList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Instruction createInstruction()
  {
    InstructionImpl instruction = new InstructionImpl();
    return instruction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Label createLabel()
  {
    LabelImpl label = new LabelImpl();
    return label;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Operation createOperation()
  {
    OperationImpl operation = new OperationImpl();
    return operation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SimpleOperation createSimpleOperation()
  {
    SimpleOperationImpl simpleOperation = new SimpleOperationImpl();
    return simpleOperation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ExpressionOperation createExpressionOperation()
  {
    ExpressionOperationImpl expressionOperation = new ExpressionOperationImpl();
    return expressionOperation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public JmpOperation createJmpOperation()
  {
    JmpOperationImpl jmpOperation = new JmpOperationImpl();
    return jmpOperation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Operand createOperand()
  {
    OperandImpl operand = new OperandImpl();
    return operand;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ReferencedOperand createReferencedOperand()
  {
    ReferencedOperandImpl referencedOperand = new ReferencedOperandImpl();
    return referencedOperand;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SimpleInstructionList createSimpleInstructionList()
  {
    SimpleInstructionListImpl simpleInstructionList = new SimpleInstructionListImpl();
    return simpleInstructionList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SimpleInstruction createSimpleInstruction()
  {
    SimpleInstructionImpl simpleInstruction = new SimpleInstructionImpl();
    return simpleInstruction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StatementList createStatementList()
  {
    StatementListImpl statementList = new StatementListImpl();
    return statementList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Statement createStatement()
  {
    StatementImpl statement = new StatementImpl();
    return statement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public AssignStatement createAssignStatement()
  {
    AssignStatementImpl assignStatement = new AssignStatementImpl();
    return assignStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SelectionStatement createSelectionStatement()
  {
    SelectionStatementImpl selectionStatement = new SelectionStatementImpl();
    return selectionStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IfStatement createIfStatement()
  {
    IfStatementImpl ifStatement = new IfStatementImpl();
    return ifStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ElseIf createElseIf()
  {
    ElseIfImpl elseIf = new ElseIfImpl();
    return elseIf;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CaseStatement createCaseStatement()
  {
    CaseStatementImpl caseStatement = new CaseStatementImpl();
    return caseStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CaseElement createCaseElement()
  {
    CaseElementImpl caseElement = new CaseElementImpl();
    return caseElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CaseList createCaseList()
  {
    CaseListImpl caseList = new CaseListImpl();
    return caseList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CaseListElement createCaseListElement()
  {
    CaseListElementImpl caseListElement = new CaseListElementImpl();
    return caseListElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FunctionBlockDeclaration createFunctionBlockDeclaration()
  {
    FunctionBlockDeclarationImpl functionBlockDeclaration = new FunctionBlockDeclarationImpl();
    return functionBlockDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FunctionBlockVarDeclarations createFunctionBlockVarDeclarations()
  {
    FunctionBlockVarDeclarationsImpl functionBlockVarDeclarations = new FunctionBlockVarDeclarationsImpl();
    return functionBlockVarDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public OtherVarDeclarations createOtherVarDeclarations()
  {
    OtherVarDeclarationsImpl otherVarDeclarations = new OtherVarDeclarationsImpl();
    return otherVarDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VarDeclarations createVarDeclarations()
  {
    VarDeclarationsImpl varDeclarations = new VarDeclarationsImpl();
    return varDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FunctionBlockBody createFunctionBlockBody()
  {
    FunctionBlockBodyImpl functionBlockBody = new FunctionBlockBodyImpl();
    return functionBlockBody;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProgramDeclaration createProgramDeclaration()
  {
    ProgramDeclarationImpl programDeclaration = new ProgramDeclarationImpl();
    return programDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProgramVarDeclarations createProgramVarDeclarations()
  {
    ProgramVarDeclarationsImpl programVarDeclarations = new ProgramVarDeclarationsImpl();
    return programVarDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public LocatedVarDeclarations createLocatedVarDeclarations()
  {
    LocatedVarDeclarationsImpl locatedVarDeclarations = new LocatedVarDeclarationsImpl();
    return locatedVarDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public LocatedVarDeclaration createLocatedVarDeclaration()
  {
    LocatedVarDeclarationImpl locatedVarDeclaration = new LocatedVarDeclarationImpl();
    return locatedVarDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Location createLocation()
  {
    LocationImpl location = new LocationImpl();
    return location;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProgramAccessDecls createProgramAccessDecls()
  {
    ProgramAccessDeclsImpl programAccessDecls = new ProgramAccessDeclsImpl();
    return programAccessDecls;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProgramAccessDecl createProgramAccessDecl()
  {
    ProgramAccessDeclImpl programAccessDecl = new ProgramAccessDeclImpl();
    return programAccessDecl;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ConfigurationDeclaration createConfigurationDeclaration()
  {
    ConfigurationDeclarationImpl configurationDeclaration = new ConfigurationDeclarationImpl();
    return configurationDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceDeclaration createResourceDeclaration()
  {
    ResourceDeclarationImpl resourceDeclaration = new ResourceDeclarationImpl();
    return resourceDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SingleResourceDeclaration createSingleResourceDeclaration()
  {
    SingleResourceDeclarationImpl singleResourceDeclaration = new SingleResourceDeclarationImpl();
    return singleResourceDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProgramConfiguration createProgramConfiguration()
  {
    ProgramConfigurationImpl programConfiguration = new ProgramConfigurationImpl();
    return programConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProgConfElements createProgConfElements()
  {
    ProgConfElementsImpl progConfElements = new ProgConfElementsImpl();
    return progConfElements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProgConfElement createProgConfElement()
  {
    ProgConfElementImpl progConfElement = new ProgConfElementImpl();
    return progConfElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FBTask createFBTask()
  {
    FBTaskImpl fbTask = new FBTaskImpl();
    return fbTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProgCNXN createProgCNXN()
  {
    ProgCNXNImpl progCNXN = new ProgCNXNImpl();
    return progCNXN;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DataSink createDataSink()
  {
    DataSinkImpl dataSink = new DataSinkImpl();
    return dataSink;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProgDataSource createProgDataSource()
  {
    ProgDataSourceImpl progDataSource = new ProgDataSourceImpl();
    return progDataSource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GlobalVarDeclarations createGlobalVarDeclarations()
  {
    GlobalVarDeclarationsImpl globalVarDeclarations = new GlobalVarDeclarationsImpl();
    return globalVarDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GlobalVarDecl createGlobalVarDecl()
  {
    GlobalVarDeclImpl globalVarDecl = new GlobalVarDeclImpl();
    return globalVarDecl;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GlobalVarSpec createGlobalVarSpec()
  {
    GlobalVarSpecImpl globalVarSpec = new GlobalVarSpecImpl();
    return globalVarSpec;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GlobalVarList createGlobalVarList()
  {
    GlobalVarListImpl globalVarList = new GlobalVarListImpl();
    return globalVarList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GlobalVar createGlobalVar()
  {
    GlobalVarImpl globalVar = new GlobalVarImpl();
    return globalVar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TaskConfiguration createTaskConfiguration()
  {
    TaskConfigurationImpl taskConfiguration = new TaskConfigurationImpl();
    return taskConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TaskInitialization createTaskInitialization()
  {
    TaskInitializationImpl taskInitialization = new TaskInitializationImpl();
    return taskInitialization;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DataSource createDataSource()
  {
    DataSourceImpl dataSource = new DataSourceImpl();
    return dataSource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Constant createConstant()
  {
    ConstantImpl constant = new ConstantImpl();
    return constant;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NumericLiteral createNumericLiteral()
  {
    NumericLiteralImpl numericLiteral = new NumericLiteralImpl();
    return numericLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral createIntegerLiteral()
  {
    IntegerLiteralImpl integerLiteral = new IntegerLiteralImpl();
    return integerLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RealLiteral createRealLiteral()
  {
    RealLiteralImpl realLiteral = new RealLiteralImpl();
    return realLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CharacterString createCharacterString()
  {
    CharacterStringImpl characterString = new CharacterStringImpl();
    return characterString;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BitString createBitString()
  {
    BitStringImpl bitString = new BitStringImpl();
    return bitString;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public com.bichler.iec.iec.Boolean createBoolean()
  {
    BooleanImpl boolean_ = new BooleanImpl();
    return boolean_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TimeLiteral createTimeLiteral()
  {
    TimeLiteralImpl timeLiteral = new TimeLiteralImpl();
    return timeLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DurationLiteral createDurationLiteral()
  {
    DurationLiteralImpl durationLiteral = new DurationLiteralImpl();
    return durationLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TimeOfDayLiteral createTimeOfDayLiteral()
  {
    TimeOfDayLiteralImpl timeOfDayLiteral = new TimeOfDayLiteralImpl();
    return timeOfDayLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DateLiteral createDateLiteral()
  {
    DateLiteralImpl dateLiteral = new DateLiteralImpl();
    return dateLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DateAndTimeLiteral createDateAndTimeLiteral()
  {
    DateAndTimeLiteralImpl dateAndTimeLiteral = new DateAndTimeLiteralImpl();
    return dateAndTimeLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BinaryExpression createBinaryExpression()
  {
    BinaryExpressionImpl binaryExpression = new BinaryExpressionImpl();
    return binaryExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public UnaryExpression createUnaryExpression()
  {
    UnaryExpressionImpl unaryExpression = new UnaryExpressionImpl();
    return unaryExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IecPackage getIecPackage()
  {
    return (IecPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static IecPackage getPackage()
  {
    return IecPackage.eINSTANCE;
  }

} //IecFactoryImpl
