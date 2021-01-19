/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.bichler.iec.iec.IecPackage
 * @generated
 */
public interface IecFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  IecFactory eINSTANCE = com.bichler.iec.iec.impl.IecFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Model</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Model</em>'.
   * @generated
   */
  Model createModel();

  /**
   * Returns a new object of class '<em>Model Element</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Model Element</em>'.
   * @generated
   */
  ModelElement createModelElement();

  /**
   * Returns a new object of class '<em>Library Element</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Library Element</em>'.
   * @generated
   */
  LibraryElement createLibraryElement();

  /**
   * Returns a new object of class '<em>Library Element Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Library Element Declaration</em>'.
   * @generated
   */
  LibraryElementDeclaration createLibraryElementDeclaration();

  /**
   * Returns a new object of class '<em>Data Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Data Type</em>'.
   * @generated
   */
  DataType createDataType();

  /**
   * Returns a new object of class '<em>Non Generic Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Non Generic Type</em>'.
   * @generated
   */
  NonGenericType createNonGenericType();

  /**
   * Returns a new object of class '<em>Elementary Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Elementary Type</em>'.
   * @generated
   */
  ElementaryType createElementaryType();

  /**
   * Returns a new object of class '<em>String Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>String Type</em>'.
   * @generated
   */
  StringType createStringType();

  /**
   * Returns a new object of class '<em>Numeric Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Numeric Type</em>'.
   * @generated
   */
  NumericType createNumericType();

  /**
   * Returns a new object of class '<em>Integer Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Integer Type</em>'.
   * @generated
   */
  IntegerType createIntegerType();

  /**
   * Returns a new object of class '<em>Signed Integer Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Signed Integer Type</em>'.
   * @generated
   */
  SignedIntegerType createSignedIntegerType();

  /**
   * Returns a new object of class '<em>Plain Integer Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Plain Integer Type</em>'.
   * @generated
   */
  PlainIntegerType createPlainIntegerType();

  /**
   * Returns a new object of class '<em>Unsigned Integer Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Unsigned Integer Type</em>'.
   * @generated
   */
  UnsignedIntegerType createUnsignedIntegerType();

  /**
   * Returns a new object of class '<em>Real Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Real Type</em>'.
   * @generated
   */
  RealType createRealType();

  /**
   * Returns a new object of class '<em>Date Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Date Type</em>'.
   * @generated
   */
  DateType createDateType();

  /**
   * Returns a new object of class '<em>Bit String Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Bit String Type</em>'.
   * @generated
   */
  BitStringType createBitStringType();

  /**
   * Returns a new object of class '<em>Generic Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Generic Type</em>'.
   * @generated
   */
  GenericType createGenericType();

  /**
   * Returns a new object of class '<em>Derived Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Derived Type</em>'.
   * @generated
   */
  DerivedType createDerivedType();

  /**
   * Returns a new object of class '<em>Data Type Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Data Type Declaration</em>'.
   * @generated
   */
  DataTypeDeclaration createDataTypeDeclaration();

  /**
   * Returns a new object of class '<em>Type Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Type Declaration</em>'.
   * @generated
   */
  TypeDeclaration createTypeDeclaration();

  /**
   * Returns a new object of class '<em>Simple Type Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Simple Type Declaration</em>'.
   * @generated
   */
  SimpleTypeDeclaration createSimpleTypeDeclaration();

  /**
   * Returns a new object of class '<em>Spec Init</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Spec Init</em>'.
   * @generated
   */
  SpecInit createSpecInit();

  /**
   * Returns a new object of class '<em>Range Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Range Declaration</em>'.
   * @generated
   */
  RangeDeclaration createRangeDeclaration();

  /**
   * Returns a new object of class '<em>Enum Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Enum Declaration</em>'.
   * @generated
   */
  EnumDeclaration createEnumDeclaration();

  /**
   * Returns a new object of class '<em>Array Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Array Declaration</em>'.
   * @generated
   */
  ArrayDeclaration createArrayDeclaration();

  /**
   * Returns a new object of class '<em>Enumeration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Enumeration</em>'.
   * @generated
   */
  Enumeration createEnumeration();

  /**
   * Returns a new object of class '<em>Enumerated Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Enumerated Value</em>'.
   * @generated
   */
  EnumeratedValue createEnumeratedValue();

  /**
   * Returns a new object of class '<em>Array Initialization</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Array Initialization</em>'.
   * @generated
   */
  ArrayInitialization createArrayInitialization();

  /**
   * Returns a new object of class '<em>Array Initial Elements</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Array Initial Elements</em>'.
   * @generated
   */
  ArrayInitialElements createArrayInitialElements();

  /**
   * Returns a new object of class '<em>Initial Element</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Initial Element</em>'.
   * @generated
   */
  InitialElement createInitialElement();

  /**
   * Returns a new object of class '<em>Structure Type Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Structure Type Declaration</em>'.
   * @generated
   */
  StructureTypeDeclaration createStructureTypeDeclaration();

  /**
   * Returns a new object of class '<em>Structure Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Structure Declaration</em>'.
   * @generated
   */
  StructureDeclaration createStructureDeclaration();

  /**
   * Returns a new object of class '<em>Structure Element Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Structure Element Declaration</em>'.
   * @generated
   */
  StructureElementDeclaration createStructureElementDeclaration();

  /**
   * Returns a new object of class '<em>Initialized Structure</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Initialized Structure</em>'.
   * @generated
   */
  InitializedStructure createInitializedStructure();

  /**
   * Returns a new object of class '<em>Structure Initialization</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Structure Initialization</em>'.
   * @generated
   */
  StructureInitialization createStructureInitialization();

  /**
   * Returns a new object of class '<em>Structure Element Initialization</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Structure Element Initialization</em>'.
   * @generated
   */
  StructureElementInitialization createStructureElementInitialization();

  /**
   * Returns a new object of class '<em>String Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>String Declaration</em>'.
   * @generated
   */
  StringDeclaration createStringDeclaration();

  /**
   * Returns a new object of class '<em>Variable</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Variable</em>'.
   * @generated
   */
  Variable createVariable();

  /**
   * Returns a new object of class '<em>Variable Access</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Variable Access</em>'.
   * @generated
   */
  VariableAccess createVariableAccess();

  /**
   * Returns a new object of class '<em>Direct Variable</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Direct Variable</em>'.
   * @generated
   */
  DirectVariable createDirectVariable();

  /**
   * Returns a new object of class '<em>Symbolic Variable Access</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Symbolic Variable Access</em>'.
   * @generated
   */
  SymbolicVariableAccess createSymbolicVariableAccess();

  /**
   * Returns a new object of class '<em>Named Variable Access</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Named Variable Access</em>'.
   * @generated
   */
  NamedVariableAccess createNamedVariableAccess();

  /**
   * Returns a new object of class '<em>Multi Element Variable</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Multi Element Variable</em>'.
   * @generated
   */
  MultiElementVariable createMultiElementVariable();

  /**
   * Returns a new object of class '<em>Array Variable</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Array Variable</em>'.
   * @generated
   */
  ArrayVariable createArrayVariable();

  /**
   * Returns a new object of class '<em>Structured Variable</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Structured Variable</em>'.
   * @generated
   */
  StructuredVariable createStructuredVariable();

  /**
   * Returns a new object of class '<em>Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Expression</em>'.
   * @generated
   */
  Expression createExpression();

  /**
   * Returns a new object of class '<em>Param Assignment</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Param Assignment</em>'.
   * @generated
   */
  ParamAssignment createParamAssignment();

  /**
   * Returns a new object of class '<em>Function Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Function Declaration</em>'.
   * @generated
   */
  FunctionDeclaration createFunctionDeclaration();

  /**
   * Returns a new object of class '<em>Io Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Io Var Declarations</em>'.
   * @generated
   */
  IoVarDeclarations createIoVarDeclarations();

  /**
   * Returns a new object of class '<em>Input Declarations</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Input Declarations</em>'.
   * @generated
   */
  InputDeclarations createInputDeclarations();

  /**
   * Returns a new object of class '<em>Input Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Input Declaration</em>'.
   * @generated
   */
  InputDeclaration createInputDeclaration();

  /**
   * Returns a new object of class '<em>Edge Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Edge Declaration</em>'.
   * @generated
   */
  EdgeDeclaration createEdgeDeclaration();

  /**
   * Returns a new object of class '<em>Decl Specification</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Decl Specification</em>'.
   * @generated
   */
  DeclSpecification createDeclSpecification();

  /**
   * Returns a new object of class '<em>Var Decl Specification</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Var Decl Specification</em>'.
   * @generated
   */
  VarDeclSpecification createVarDeclSpecification();

  /**
   * Returns a new object of class '<em>Edge Decl Specification</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Edge Decl Specification</em>'.
   * @generated
   */
  EdgeDeclSpecification createEdgeDeclSpecification();

  /**
   * Returns a new object of class '<em>Var1 List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Var1 List</em>'.
   * @generated
   */
  Var1List createVar1List();

  /**
   * Returns a new object of class '<em>Output Declarations</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Output Declarations</em>'.
   * @generated
   */
  OutputDeclarations createOutputDeclarations();

  /**
   * Returns a new object of class '<em>Var Init Decl</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Var Init Decl</em>'.
   * @generated
   */
  VarInitDecl createVarInitDecl();

  /**
   * Returns a new object of class '<em>Input Output Declarations</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Input Output Declarations</em>'.
   * @generated
   */
  InputOutputDeclarations createInputOutputDeclarations();

  /**
   * Returns a new object of class '<em>Function Body</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Function Body</em>'.
   * @generated
   */
  FunctionBody createFunctionBody();

  /**
   * Returns a new object of class '<em>Instruction List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Instruction List</em>'.
   * @generated
   */
  InstructionList createInstructionList();

  /**
   * Returns a new object of class '<em>Instruction</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Instruction</em>'.
   * @generated
   */
  Instruction createInstruction();

  /**
   * Returns a new object of class '<em>Label</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Label</em>'.
   * @generated
   */
  Label createLabel();

  /**
   * Returns a new object of class '<em>Operation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operation</em>'.
   * @generated
   */
  Operation createOperation();

  /**
   * Returns a new object of class '<em>Simple Operation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Simple Operation</em>'.
   * @generated
   */
  SimpleOperation createSimpleOperation();

  /**
   * Returns a new object of class '<em>Expression Operation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Expression Operation</em>'.
   * @generated
   */
  ExpressionOperation createExpressionOperation();

  /**
   * Returns a new object of class '<em>Jmp Operation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Jmp Operation</em>'.
   * @generated
   */
  JmpOperation createJmpOperation();

  /**
   * Returns a new object of class '<em>Operand</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operand</em>'.
   * @generated
   */
  Operand createOperand();

  /**
   * Returns a new object of class '<em>Referenced Operand</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Referenced Operand</em>'.
   * @generated
   */
  ReferencedOperand createReferencedOperand();

  /**
   * Returns a new object of class '<em>Simple Instruction List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Simple Instruction List</em>'.
   * @generated
   */
  SimpleInstructionList createSimpleInstructionList();

  /**
   * Returns a new object of class '<em>Simple Instruction</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Simple Instruction</em>'.
   * @generated
   */
  SimpleInstruction createSimpleInstruction();

  /**
   * Returns a new object of class '<em>Statement List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Statement List</em>'.
   * @generated
   */
  StatementList createStatementList();

  /**
   * Returns a new object of class '<em>Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Statement</em>'.
   * @generated
   */
  Statement createStatement();

  /**
   * Returns a new object of class '<em>Assign Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Assign Statement</em>'.
   * @generated
   */
  AssignStatement createAssignStatement();

  /**
   * Returns a new object of class '<em>Selection Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Selection Statement</em>'.
   * @generated
   */
  SelectionStatement createSelectionStatement();

  /**
   * Returns a new object of class '<em>If Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>If Statement</em>'.
   * @generated
   */
  IfStatement createIfStatement();

  /**
   * Returns a new object of class '<em>Else If</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Else If</em>'.
   * @generated
   */
  ElseIf createElseIf();

  /**
   * Returns a new object of class '<em>Case Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Case Statement</em>'.
   * @generated
   */
  CaseStatement createCaseStatement();

  /**
   * Returns a new object of class '<em>Case Element</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Case Element</em>'.
   * @generated
   */
  CaseElement createCaseElement();

  /**
   * Returns a new object of class '<em>Case List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Case List</em>'.
   * @generated
   */
  CaseList createCaseList();

  /**
   * Returns a new object of class '<em>Case List Element</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Case List Element</em>'.
   * @generated
   */
  CaseListElement createCaseListElement();

  /**
   * Returns a new object of class '<em>Function Block Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Function Block Declaration</em>'.
   * @generated
   */
  FunctionBlockDeclaration createFunctionBlockDeclaration();

  /**
   * Returns a new object of class '<em>Function Block Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Function Block Var Declarations</em>'.
   * @generated
   */
  FunctionBlockVarDeclarations createFunctionBlockVarDeclarations();

  /**
   * Returns a new object of class '<em>Other Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Other Var Declarations</em>'.
   * @generated
   */
  OtherVarDeclarations createOtherVarDeclarations();

  /**
   * Returns a new object of class '<em>Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Var Declarations</em>'.
   * @generated
   */
  VarDeclarations createVarDeclarations();

  /**
   * Returns a new object of class '<em>Function Block Body</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Function Block Body</em>'.
   * @generated
   */
  FunctionBlockBody createFunctionBlockBody();

  /**
   * Returns a new object of class '<em>Program Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Program Declaration</em>'.
   * @generated
   */
  ProgramDeclaration createProgramDeclaration();

  /**
   * Returns a new object of class '<em>Program Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Program Var Declarations</em>'.
   * @generated
   */
  ProgramVarDeclarations createProgramVarDeclarations();

  /**
   * Returns a new object of class '<em>Located Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Located Var Declarations</em>'.
   * @generated
   */
  LocatedVarDeclarations createLocatedVarDeclarations();

  /**
   * Returns a new object of class '<em>Located Var Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Located Var Declaration</em>'.
   * @generated
   */
  LocatedVarDeclaration createLocatedVarDeclaration();

  /**
   * Returns a new object of class '<em>Location</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Location</em>'.
   * @generated
   */
  Location createLocation();

  /**
   * Returns a new object of class '<em>Program Access Decls</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Program Access Decls</em>'.
   * @generated
   */
  ProgramAccessDecls createProgramAccessDecls();

  /**
   * Returns a new object of class '<em>Program Access Decl</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Program Access Decl</em>'.
   * @generated
   */
  ProgramAccessDecl createProgramAccessDecl();

  /**
   * Returns a new object of class '<em>Configuration Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Configuration Declaration</em>'.
   * @generated
   */
  ConfigurationDeclaration createConfigurationDeclaration();

  /**
   * Returns a new object of class '<em>Resource Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Resource Declaration</em>'.
   * @generated
   */
  ResourceDeclaration createResourceDeclaration();

  /**
   * Returns a new object of class '<em>Single Resource Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Single Resource Declaration</em>'.
   * @generated
   */
  SingleResourceDeclaration createSingleResourceDeclaration();

  /**
   * Returns a new object of class '<em>Program Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Program Configuration</em>'.
   * @generated
   */
  ProgramConfiguration createProgramConfiguration();

  /**
   * Returns a new object of class '<em>Prog Conf Elements</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Prog Conf Elements</em>'.
   * @generated
   */
  ProgConfElements createProgConfElements();

  /**
   * Returns a new object of class '<em>Prog Conf Element</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Prog Conf Element</em>'.
   * @generated
   */
  ProgConfElement createProgConfElement();

  /**
   * Returns a new object of class '<em>FB Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>FB Task</em>'.
   * @generated
   */
  FBTask createFBTask();

  /**
   * Returns a new object of class '<em>Prog CNXN</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Prog CNXN</em>'.
   * @generated
   */
  ProgCNXN createProgCNXN();

  /**
   * Returns a new object of class '<em>Data Sink</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Data Sink</em>'.
   * @generated
   */
  DataSink createDataSink();

  /**
   * Returns a new object of class '<em>Prog Data Source</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Prog Data Source</em>'.
   * @generated
   */
  ProgDataSource createProgDataSource();

  /**
   * Returns a new object of class '<em>Global Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Global Var Declarations</em>'.
   * @generated
   */
  GlobalVarDeclarations createGlobalVarDeclarations();

  /**
   * Returns a new object of class '<em>Global Var Decl</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Global Var Decl</em>'.
   * @generated
   */
  GlobalVarDecl createGlobalVarDecl();

  /**
   * Returns a new object of class '<em>Global Var Spec</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Global Var Spec</em>'.
   * @generated
   */
  GlobalVarSpec createGlobalVarSpec();

  /**
   * Returns a new object of class '<em>Global Var List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Global Var List</em>'.
   * @generated
   */
  GlobalVarList createGlobalVarList();

  /**
   * Returns a new object of class '<em>Global Var</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Global Var</em>'.
   * @generated
   */
  GlobalVar createGlobalVar();

  /**
   * Returns a new object of class '<em>Task Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Task Configuration</em>'.
   * @generated
   */
  TaskConfiguration createTaskConfiguration();

  /**
   * Returns a new object of class '<em>Task Initialization</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Task Initialization</em>'.
   * @generated
   */
  TaskInitialization createTaskInitialization();

  /**
   * Returns a new object of class '<em>Data Source</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Data Source</em>'.
   * @generated
   */
  DataSource createDataSource();

  /**
   * Returns a new object of class '<em>Constant</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Constant</em>'.
   * @generated
   */
  Constant createConstant();

  /**
   * Returns a new object of class '<em>Numeric Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Numeric Literal</em>'.
   * @generated
   */
  NumericLiteral createNumericLiteral();

  /**
   * Returns a new object of class '<em>Integer Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Integer Literal</em>'.
   * @generated
   */
  IntegerLiteral createIntegerLiteral();

  /**
   * Returns a new object of class '<em>Real Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Real Literal</em>'.
   * @generated
   */
  RealLiteral createRealLiteral();

  /**
   * Returns a new object of class '<em>Character String</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Character String</em>'.
   * @generated
   */
  CharacterString createCharacterString();

  /**
   * Returns a new object of class '<em>Bit String</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Bit String</em>'.
   * @generated
   */
  BitString createBitString();

  /**
   * Returns a new object of class '<em>Boolean</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Boolean</em>'.
   * @generated
   */
  Boolean createBoolean();

  /**
   * Returns a new object of class '<em>Time Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Time Literal</em>'.
   * @generated
   */
  TimeLiteral createTimeLiteral();

  /**
   * Returns a new object of class '<em>Duration Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Duration Literal</em>'.
   * @generated
   */
  DurationLiteral createDurationLiteral();

  /**
   * Returns a new object of class '<em>Time Of Day Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Time Of Day Literal</em>'.
   * @generated
   */
  TimeOfDayLiteral createTimeOfDayLiteral();

  /**
   * Returns a new object of class '<em>Date Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Date Literal</em>'.
   * @generated
   */
  DateLiteral createDateLiteral();

  /**
   * Returns a new object of class '<em>Date And Time Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Date And Time Literal</em>'.
   * @generated
   */
  DateAndTimeLiteral createDateAndTimeLiteral();

  /**
   * Returns a new object of class '<em>Binary Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Binary Expression</em>'.
   * @generated
   */
  BinaryExpression createBinaryExpression();

  /**
   * Returns a new object of class '<em>Unary Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Unary Expression</em>'.
   * @generated
   */
  UnaryExpression createUnaryExpression();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  IecPackage getIecPackage();

} //IecFactory
