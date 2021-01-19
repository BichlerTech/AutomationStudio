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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class IecPackageImpl extends EPackageImpl implements IecPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass modelEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass modelElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass libraryElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass libraryElementDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dataTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass nonGenericTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass elementaryTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stringTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass numericTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass integerTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass signedIntegerTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass plainIntegerTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass unsignedIntegerTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass realTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dateTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass bitStringTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass genericTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass derivedTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dataTypeDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass typeDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass simpleTypeDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass specInitEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass rangeDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass enumDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass arrayDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass enumerationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass enumeratedValueEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass arrayInitializationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass arrayInitialElementsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass initialElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass structureTypeDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass structureDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass structureElementDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass initializedStructureEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass structureInitializationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass structureElementInitializationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stringDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass variableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass variableAccessEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass directVariableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass symbolicVariableAccessEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass namedVariableAccessEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass multiElementVariableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass arrayVariableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass structuredVariableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass expressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass paramAssignmentEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass functionDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ioVarDeclarationsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass inputDeclarationsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass inputDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass edgeDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass declSpecificationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass varDeclSpecificationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass edgeDeclSpecificationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass var1ListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass outputDeclarationsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass varInitDeclEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass inputOutputDeclarationsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass functionBodyEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass instructionListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass instructionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass labelEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass operationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass simpleOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass expressionOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jmpOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass operandEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass referencedOperandEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass simpleInstructionListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass simpleInstructionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass statementListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass statementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass assignStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass selectionStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ifStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass elseIfEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass caseStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass caseElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass caseListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass caseListElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass functionBlockDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass functionBlockVarDeclarationsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass otherVarDeclarationsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass varDeclarationsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass functionBlockBodyEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass programDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass programVarDeclarationsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass locatedVarDeclarationsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass locatedVarDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass locationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass programAccessDeclsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass programAccessDeclEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass configurationDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass resourceDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass singleResourceDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass programConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass progConfElementsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass progConfElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass fbTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass progCNXNEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dataSinkEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass progDataSourceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass globalVarDeclarationsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass globalVarDeclEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass globalVarSpecEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass globalVarListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass globalVarEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass taskConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass taskInitializationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dataSourceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass constantEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass numericLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass integerLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass realLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass characterStringEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass bitStringEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass booleanEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass timeLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass durationLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass timeOfDayLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dateLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dateAndTimeLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass binaryExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass unaryExpressionEClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see com.bichler.iec.iec.IecPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private IecPackageImpl()
  {
    super(eNS_URI, IecFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   *
   * <p>This method is used to initialize {@link IecPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static IecPackage init()
  {
    if (isInited) return (IecPackage)EPackage.Registry.INSTANCE.getEPackage(IecPackage.eNS_URI);

    // Obtain or create and register package
    Object registeredIecPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
    IecPackageImpl theIecPackage = registeredIecPackage instanceof IecPackageImpl ? (IecPackageImpl)registeredIecPackage : new IecPackageImpl();

    isInited = true;

    // Create package meta-data objects
    theIecPackage.createPackageContents();

    // Initialize created meta-data
    theIecPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theIecPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(IecPackage.eNS_URI, theIecPackage);
    return theIecPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getModel()
  {
    return modelEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getModel_ModelElement()
  {
    return (EReference)modelEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getModelElement()
  {
    return modelElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getLibraryElement()
  {
    return libraryElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getLibraryElementDeclaration()
  {
    return libraryElementDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getLibraryElementDeclaration_Name()
  {
    return (EAttribute)libraryElementDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDataType()
  {
    return dataTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getNonGenericType()
  {
    return nonGenericTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getElementaryType()
  {
    return elementaryTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getElementaryType_TypeName()
  {
    return (EAttribute)elementaryTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getStringType()
  {
    return stringTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getNumericType()
  {
    return numericTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getIntegerType()
  {
    return integerTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getSignedIntegerType()
  {
    return signedIntegerTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getPlainIntegerType()
  {
    return plainIntegerTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getUnsignedIntegerType()
  {
    return unsignedIntegerTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getRealType()
  {
    return realTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDateType()
  {
    return dateTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getBitStringType()
  {
    return bitStringTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getGenericType()
  {
    return genericTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getGenericType_TypeName()
  {
    return (EAttribute)genericTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDerivedType()
  {
    return derivedTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDerivedType_Name()
  {
    return (EAttribute)derivedTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDataTypeDeclaration()
  {
    return dataTypeDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDataTypeDeclaration_TypeDeclaration()
  {
    return (EReference)dataTypeDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getTypeDeclaration()
  {
    return typeDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getTypeDeclaration_DerivedType()
  {
    return (EReference)typeDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getSimpleTypeDeclaration()
  {
    return simpleTypeDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getSimpleTypeDeclaration_SpecInit()
  {
    return (EReference)simpleTypeDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getSpecInit()
  {
    return specInitEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getSpecInit_BaseType()
  {
    return (EReference)specInitEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getSpecInit_Constant()
  {
    return (EReference)specInitEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getRangeDeclaration()
  {
    return rangeDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getRangeDeclaration_BaseType()
  {
    return (EReference)rangeDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getRangeDeclaration_Range()
  {
    return (EAttribute)rangeDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getRangeDeclaration_Constant()
  {
    return (EAttribute)rangeDeclarationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getEnumDeclaration()
  {
    return enumDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getEnumDeclaration_Enumeration()
  {
    return (EReference)enumDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getEnumDeclaration_Constant()
  {
    return (EReference)enumDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getArrayDeclaration()
  {
    return arrayDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getArrayDeclaration_Ranges()
  {
    return (EAttribute)arrayDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getArrayDeclaration_BaseType()
  {
    return (EReference)arrayDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getArrayDeclaration_Constant()
  {
    return (EReference)arrayDeclarationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getEnumeration()
  {
    return enumerationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getEnumeration_Values()
  {
    return (EReference)enumerationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getEnumeratedValue()
  {
    return enumeratedValueEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getEnumeratedValue_DerivedType()
  {
    return (EReference)enumeratedValueEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getEnumeratedValue_Name()
  {
    return (EAttribute)enumeratedValueEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getArrayInitialization()
  {
    return arrayInitializationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getArrayInitialization_InitialElements()
  {
    return (EReference)arrayInitializationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getArrayInitialElements()
  {
    return arrayInitialElementsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getArrayInitialElements_InitialElement()
  {
    return (EReference)arrayInitialElementsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getArrayInitialElements_Index()
  {
    return (EAttribute)arrayInitialElementsEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getInitialElement()
  {
    return initialElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getStructureTypeDeclaration()
  {
    return structureTypeDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getStructureTypeDeclaration_Declaration()
  {
    return (EReference)structureTypeDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getStructureTypeDeclaration_Initialization()
  {
    return (EReference)structureTypeDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getStructureDeclaration()
  {
    return structureDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getStructureDeclaration_StructureElement()
  {
    return (EReference)structureDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getStructureElementDeclaration()
  {
    return structureElementDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getStructureElementDeclaration_Name()
  {
    return (EAttribute)structureElementDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getStructureElementDeclaration_SpecInit()
  {
    return (EReference)structureElementDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getInitializedStructure()
  {
    return initializedStructureEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getInitializedStructure_DerivedType()
  {
    return (EReference)initializedStructureEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getInitializedStructure_Initialization()
  {
    return (EReference)initializedStructureEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getStructureInitialization()
  {
    return structureInitializationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getStructureInitialization_InitialElements()
  {
    return (EReference)structureInitializationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getStructureElementInitialization()
  {
    return structureElementInitializationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getStructureElementInitialization_Name()
  {
    return (EAttribute)structureElementInitializationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getStructureElementInitialization_Value()
  {
    return (EReference)structureElementInitializationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getStringDeclaration()
  {
    return stringDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getStringDeclaration_String()
  {
    return (EAttribute)stringDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getStringDeclaration_Size()
  {
    return (EAttribute)stringDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getStringDeclaration_InitialValue()
  {
    return (EReference)stringDeclarationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getVariable()
  {
    return variableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getVariable_Name()
  {
    return (EAttribute)variableEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getVariableAccess()
  {
    return variableAccessEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDirectVariable()
  {
    return directVariableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDirectVariable_Name()
  {
    return (EAttribute)directVariableEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getSymbolicVariableAccess()
  {
    return symbolicVariableAccessEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getNamedVariableAccess()
  {
    return namedVariableAccessEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getNamedVariableAccess_NamedVariable()
  {
    return (EReference)namedVariableAccessEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getMultiElementVariable()
  {
    return multiElementVariableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getArrayVariable()
  {
    return arrayVariableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getArrayVariable_SubscriptedVariable()
  {
    return (EReference)arrayVariableEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getArrayVariable_Subscripts()
  {
    return (EReference)arrayVariableEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getStructuredVariable()
  {
    return structuredVariableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getStructuredVariable_RecordVariable()
  {
    return (EReference)structuredVariableEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getStructuredVariable_FieldSelector()
  {
    return (EAttribute)structuredVariableEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getExpression()
  {
    return expressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getExpression_Variable()
  {
    return (EReference)expressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getExpression_Fbname()
  {
    return (EReference)expressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getExpression_Openbr()
  {
    return (EAttribute)expressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getExpression_Paramassignment()
  {
    return (EReference)expressionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getExpression_Closebr()
  {
    return (EAttribute)expressionEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getExpression_Expression()
  {
    return (EReference)expressionEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getParamAssignment()
  {
    return paramAssignmentEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getParamAssignment_Variablename()
  {
    return (EReference)paramAssignmentEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getParamAssignment_Expression()
  {
    return (EReference)paramAssignmentEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getParamAssignment_Not()
  {
    return (EAttribute)paramAssignmentEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getParamAssignment_Variable1()
  {
    return (EReference)paramAssignmentEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getParamAssignment_Variable2()
  {
    return (EReference)paramAssignmentEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getFunctionDeclaration()
  {
    return functionDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getFunctionDeclaration_Name()
  {
    return (EAttribute)functionDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getFunctionDeclaration_Type()
  {
    return (EReference)functionDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getFunctionDeclaration_IoVarDeclarations()
  {
    return (EReference)functionDeclarationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getFunctionDeclaration_Body()
  {
    return (EReference)functionDeclarationEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getIoVarDeclarations()
  {
    return ioVarDeclarationsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getInputDeclarations()
  {
    return inputDeclarationsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getInputDeclarations_Declarations()
  {
    return (EReference)inputDeclarationsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getInputDeclaration()
  {
    return inputDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getInputDeclaration_Var1List()
  {
    return (EReference)inputDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getInputDeclaration_DeclSpecification()
  {
    return (EReference)inputDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getEdgeDeclaration()
  {
    return edgeDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getEdgeDeclaration_Var1List()
  {
    return (EReference)edgeDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDeclSpecification()
  {
    return declSpecificationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getVarDeclSpecification()
  {
    return varDeclSpecificationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getVarDeclSpecification_SpecInit()
  {
    return (EReference)varDeclSpecificationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getEdgeDeclSpecification()
  {
    return edgeDeclSpecificationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getEdgeDeclSpecification_REdge()
  {
    return (EAttribute)edgeDeclSpecificationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getEdgeDeclSpecification_FEdge()
  {
    return (EAttribute)edgeDeclSpecificationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getVar1List()
  {
    return var1ListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getVar1List_Variables()
  {
    return (EReference)var1ListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getOutputDeclarations()
  {
    return outputDeclarationsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getOutputDeclarations_InitDecls()
  {
    return (EReference)outputDeclarationsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getVarInitDecl()
  {
    return varInitDeclEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getVarInitDecl_Var1List()
  {
    return (EReference)varInitDeclEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getVarInitDecl_SpecInit()
  {
    return (EReference)varInitDeclEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getInputOutputDeclarations()
  {
    return inputOutputDeclarationsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getInputOutputDeclarations_InitDecls()
  {
    return (EReference)inputOutputDeclarationsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getFunctionBody()
  {
    return functionBodyEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getInstructionList()
  {
    return instructionListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getInstructionList_Instructions()
  {
    return (EReference)instructionListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getInstruction()
  {
    return instructionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getInstruction_Label()
  {
    return (EReference)instructionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getInstruction_Instruction()
  {
    return (EReference)instructionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getLabel()
  {
    return labelEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getLabel_Name()
  {
    return (EAttribute)labelEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getOperation()
  {
    return operationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getSimpleOperation()
  {
    return simpleOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getSimpleOperation_Operator()
  {
    return (EAttribute)simpleOperationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getSimpleOperation_Operand()
  {
    return (EReference)simpleOperationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getExpressionOperation()
  {
    return expressionOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getExpressionOperation_Operator()
  {
    return (EAttribute)expressionOperationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getExpressionOperation_Operand()
  {
    return (EReference)expressionOperationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getExpressionOperation_SimpleInstructionList()
  {
    return (EReference)expressionOperationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getJmpOperation()
  {
    return jmpOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getJmpOperation_Operator()
  {
    return (EAttribute)jmpOperationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getJmpOperation_Label()
  {
    return (EReference)jmpOperationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getOperand()
  {
    return operandEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getOperand_Constant()
  {
    return (EReference)operandEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getOperand_Reference()
  {
    return (EReference)operandEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getReferencedOperand()
  {
    return referencedOperandEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getSimpleInstructionList()
  {
    return simpleInstructionListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getSimpleInstructionList_Instructions()
  {
    return (EReference)simpleInstructionListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getSimpleInstruction()
  {
    return simpleInstructionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getStatementList()
  {
    return statementListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getStatementList_Statements()
  {
    return (EReference)statementListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getStatement()
  {
    return statementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getAssignStatement()
  {
    return assignStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getAssignStatement_Variable()
  {
    return (EReference)assignStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getAssignStatement_Expression()
  {
    return (EReference)assignStatementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getSelectionStatement()
  {
    return selectionStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getSelectionStatement_ElseStatementList()
  {
    return (EReference)selectionStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getIfStatement()
  {
    return ifStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getIfStatement_IfExpression()
  {
    return (EReference)ifStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getIfStatement_ThenStatementList()
  {
    return (EReference)ifStatementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getIfStatement_ElseIfs()
  {
    return (EReference)ifStatementEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getElseIf()
  {
    return elseIfEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getElseIf_Expression()
  {
    return (EReference)elseIfEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getElseIf_StatementList()
  {
    return (EReference)elseIfEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getCaseStatement()
  {
    return caseStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getCaseStatement_Expression()
  {
    return (EReference)caseStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getCaseStatement_CaseElements()
  {
    return (EReference)caseStatementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getCaseElement()
  {
    return caseElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getCaseElement_CaseList()
  {
    return (EReference)caseElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getCaseElement_StatementList()
  {
    return (EReference)caseElementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getCaseList()
  {
    return caseListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getCaseList_Elements()
  {
    return (EReference)caseListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getCaseListElement()
  {
    return caseListElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getCaseListElement_SubRange()
  {
    return (EAttribute)caseListElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getCaseListElement_Integer()
  {
    return (EAttribute)caseListElementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getCaseListElement_EnumeratedValue()
  {
    return (EReference)caseListElementEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getFunctionBlockDeclaration()
  {
    return functionBlockDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getFunctionBlockDeclaration_VarDeclarations()
  {
    return (EReference)functionBlockDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getFunctionBlockDeclaration_Body()
  {
    return (EReference)functionBlockDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getFunctionBlockVarDeclarations()
  {
    return functionBlockVarDeclarationsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getOtherVarDeclarations()
  {
    return otherVarDeclarationsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getVarDeclarations()
  {
    return varDeclarationsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getVarDeclarations_Constant()
  {
    return (EAttribute)varDeclarationsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getVarDeclarations_InitDecls()
  {
    return (EReference)varDeclarationsEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getFunctionBlockBody()
  {
    return functionBlockBodyEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getProgramDeclaration()
  {
    return programDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProgramDeclaration_VarDeclarations()
  {
    return (EReference)programDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProgramDeclaration_Body()
  {
    return (EReference)programDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getProgramVarDeclarations()
  {
    return programVarDeclarationsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getLocatedVarDeclarations()
  {
    return locatedVarDeclarationsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getLocatedVarDeclarations_LocatedVarDeclaration()
  {
    return (EReference)locatedVarDeclarationsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getLocatedVarDeclaration()
  {
    return locatedVarDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getLocatedVarDeclaration_Name()
  {
    return (EAttribute)locatedVarDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getLocatedVarDeclaration_Location()
  {
    return (EReference)locatedVarDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getLocatedVarDeclaration_SpecInit()
  {
    return (EReference)locatedVarDeclarationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getLocation()
  {
    return locationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getLocation_DirectVariable()
  {
    return (EReference)locationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getProgramAccessDecls()
  {
    return programAccessDeclsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProgramAccessDecls_ProgramAccessDecl()
  {
    return (EReference)programAccessDeclsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getProgramAccessDecl()
  {
    return programAccessDeclEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProgramAccessDecl_AccessName()
  {
    return (EReference)programAccessDeclEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProgramAccessDecl_SymbolicVariable()
  {
    return (EReference)programAccessDeclEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProgramAccessDecl_TypeName()
  {
    return (EReference)programAccessDeclEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getProgramAccessDecl_Direction()
  {
    return (EAttribute)programAccessDeclEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getConfigurationDeclaration()
  {
    return configurationDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getConfigurationDeclaration_GlobalVarDeclarations()
  {
    return (EReference)configurationDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getConfigurationDeclaration_Resdecl()
  {
    return (EReference)configurationDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getResourceDeclaration()
  {
    return resourceDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getResourceDeclaration_Resname()
  {
    return (EAttribute)resourceDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getResourceDeclaration_GlobalVarDeclarations()
  {
    return (EReference)resourceDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getResourceDeclaration_Singleresource()
  {
    return (EReference)resourceDeclarationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getSingleResourceDeclaration()
  {
    return singleResourceDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getSingleResourceDeclaration_TaskConf()
  {
    return (EReference)singleResourceDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getSingleResourceDeclaration_ProgramConf()
  {
    return (EReference)singleResourceDeclarationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getProgramConfiguration()
  {
    return programConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getProgramConfiguration_Name()
  {
    return (EAttribute)programConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProgramConfiguration_Task()
  {
    return (EReference)programConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProgramConfiguration_Prog()
  {
    return (EReference)programConfigurationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProgramConfiguration_ProgConf()
  {
    return (EReference)programConfigurationEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getProgConfElements()
  {
    return progConfElementsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProgConfElements_Progconf()
  {
    return (EReference)progConfElementsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getProgConfElement()
  {
    return progConfElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getFBTask()
  {
    return fbTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getFBTask_Fbname()
  {
    return (EReference)fbTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getFBTask_Task()
  {
    return (EReference)fbTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getProgCNXN()
  {
    return progCNXNEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getProgCNXN_Variablename()
  {
    return (EAttribute)progCNXNEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getProgCNXN_Progd()
  {
    return (EReference)progCNXNEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDataSink()
  {
    return dataSinkEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDataSink_Globvar()
  {
    return (EReference)dataSinkEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDataSink_Dirvar()
  {
    return (EReference)dataSinkEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getProgDataSource()
  {
    return progDataSourceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getGlobalVarDeclarations()
  {
    return globalVarDeclarationsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getGlobalVarDeclarations_GlobalVarDecl()
  {
    return (EReference)globalVarDeclarationsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getGlobalVarDecl()
  {
    return globalVarDeclEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getGlobalVarDecl_Spec()
  {
    return (EReference)globalVarDeclEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getGlobalVarDecl_SpecInit()
  {
    return (EReference)globalVarDeclEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getGlobalVarSpec()
  {
    return globalVarSpecEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getGlobalVarSpec_Variable()
  {
    return (EReference)globalVarSpecEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getGlobalVarSpec_Location()
  {
    return (EReference)globalVarSpecEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getGlobalVarList()
  {
    return globalVarListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getGlobalVarList_Variables()
  {
    return (EReference)globalVarListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getGlobalVar()
  {
    return globalVarEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getGlobalVar_Name()
  {
    return (EAttribute)globalVarEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getTaskConfiguration()
  {
    return taskConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getTaskConfiguration_Name()
  {
    return (EAttribute)taskConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getTaskConfiguration_TaskInit()
  {
    return (EReference)taskConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getTaskInitialization()
  {
    return taskInitializationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getTaskInitialization_Single()
  {
    return (EReference)taskInitializationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getTaskInitialization_Interval()
  {
    return (EReference)taskInitializationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getTaskInitialization_Prior()
  {
    return (EAttribute)taskInitializationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDataSource()
  {
    return dataSourceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getConstant()
  {
    return constantEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getNumericLiteral()
  {
    return numericLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getNumericLiteral_Value()
  {
    return (EAttribute)numericLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getIntegerLiteral()
  {
    return integerLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getIntegerLiteral_Type()
  {
    return (EReference)integerLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getRealLiteral()
  {
    return realLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getRealLiteral_Type()
  {
    return (EReference)realLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getCharacterString()
  {
    return characterStringEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getCharacterString_Value()
  {
    return (EAttribute)characterStringEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getBitString()
  {
    return bitStringEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getBitString_Value()
  {
    return (EAttribute)bitStringEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getBoolean()
  {
    return booleanEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getBoolean_BoolInt()
  {
    return (EAttribute)booleanEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getBoolean_True()
  {
    return (EAttribute)booleanEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getTimeLiteral()
  {
    return timeLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDurationLiteral()
  {
    return durationLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDurationLiteral_Duration()
  {
    return (EAttribute)durationLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getTimeOfDayLiteral()
  {
    return timeOfDayLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getTimeOfDayLiteral_Hour()
  {
    return (EAttribute)timeOfDayLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getTimeOfDayLiteral_Minute()
  {
    return (EAttribute)timeOfDayLiteralEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getTimeOfDayLiteral_Second()
  {
    return (EAttribute)timeOfDayLiteralEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDateLiteral()
  {
    return dateLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDateLiteral_Year()
  {
    return (EAttribute)dateLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDateLiteral_Month()
  {
    return (EAttribute)dateLiteralEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDateLiteral_Day()
  {
    return (EAttribute)dateLiteralEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDateAndTimeLiteral()
  {
    return dateAndTimeLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDateAndTimeLiteral_Year()
  {
    return (EAttribute)dateAndTimeLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDateAndTimeLiteral_Month()
  {
    return (EAttribute)dateAndTimeLiteralEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDateAndTimeLiteral_Day()
  {
    return (EAttribute)dateAndTimeLiteralEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDateAndTimeLiteral_Hour()
  {
    return (EAttribute)dateAndTimeLiteralEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDateAndTimeLiteral_Minute()
  {
    return (EAttribute)dateAndTimeLiteralEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDateAndTimeLiteral_Second()
  {
    return (EAttribute)dateAndTimeLiteralEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getBinaryExpression()
  {
    return binaryExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getBinaryExpression_Left()
  {
    return (EReference)binaryExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getBinaryExpression_Operator()
  {
    return (EAttribute)binaryExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getBinaryExpression_Right()
  {
    return (EReference)binaryExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getUnaryExpression()
  {
    return unaryExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getUnaryExpression_Operator()
  {
    return (EAttribute)unaryExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IecFactory getIecFactory()
  {
    return (IecFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    modelEClass = createEClass(MODEL);
    createEReference(modelEClass, MODEL__MODEL_ELEMENT);

    modelElementEClass = createEClass(MODEL_ELEMENT);

    libraryElementEClass = createEClass(LIBRARY_ELEMENT);

    libraryElementDeclarationEClass = createEClass(LIBRARY_ELEMENT_DECLARATION);
    createEAttribute(libraryElementDeclarationEClass, LIBRARY_ELEMENT_DECLARATION__NAME);

    dataTypeEClass = createEClass(DATA_TYPE);

    nonGenericTypeEClass = createEClass(NON_GENERIC_TYPE);

    elementaryTypeEClass = createEClass(ELEMENTARY_TYPE);
    createEAttribute(elementaryTypeEClass, ELEMENTARY_TYPE__TYPE_NAME);

    stringTypeEClass = createEClass(STRING_TYPE);

    numericTypeEClass = createEClass(NUMERIC_TYPE);

    integerTypeEClass = createEClass(INTEGER_TYPE);

    signedIntegerTypeEClass = createEClass(SIGNED_INTEGER_TYPE);

    plainIntegerTypeEClass = createEClass(PLAIN_INTEGER_TYPE);

    unsignedIntegerTypeEClass = createEClass(UNSIGNED_INTEGER_TYPE);

    realTypeEClass = createEClass(REAL_TYPE);

    dateTypeEClass = createEClass(DATE_TYPE);

    bitStringTypeEClass = createEClass(BIT_STRING_TYPE);

    genericTypeEClass = createEClass(GENERIC_TYPE);
    createEAttribute(genericTypeEClass, GENERIC_TYPE__TYPE_NAME);

    derivedTypeEClass = createEClass(DERIVED_TYPE);
    createEAttribute(derivedTypeEClass, DERIVED_TYPE__NAME);

    dataTypeDeclarationEClass = createEClass(DATA_TYPE_DECLARATION);
    createEReference(dataTypeDeclarationEClass, DATA_TYPE_DECLARATION__TYPE_DECLARATION);

    typeDeclarationEClass = createEClass(TYPE_DECLARATION);
    createEReference(typeDeclarationEClass, TYPE_DECLARATION__DERIVED_TYPE);

    simpleTypeDeclarationEClass = createEClass(SIMPLE_TYPE_DECLARATION);
    createEReference(simpleTypeDeclarationEClass, SIMPLE_TYPE_DECLARATION__SPEC_INIT);

    specInitEClass = createEClass(SPEC_INIT);
    createEReference(specInitEClass, SPEC_INIT__BASE_TYPE);
    createEReference(specInitEClass, SPEC_INIT__CONSTANT);

    rangeDeclarationEClass = createEClass(RANGE_DECLARATION);
    createEReference(rangeDeclarationEClass, RANGE_DECLARATION__BASE_TYPE);
    createEAttribute(rangeDeclarationEClass, RANGE_DECLARATION__RANGE);
    createEAttribute(rangeDeclarationEClass, RANGE_DECLARATION__CONSTANT);

    enumDeclarationEClass = createEClass(ENUM_DECLARATION);
    createEReference(enumDeclarationEClass, ENUM_DECLARATION__ENUMERATION);
    createEReference(enumDeclarationEClass, ENUM_DECLARATION__CONSTANT);

    arrayDeclarationEClass = createEClass(ARRAY_DECLARATION);
    createEAttribute(arrayDeclarationEClass, ARRAY_DECLARATION__RANGES);
    createEReference(arrayDeclarationEClass, ARRAY_DECLARATION__BASE_TYPE);
    createEReference(arrayDeclarationEClass, ARRAY_DECLARATION__CONSTANT);

    enumerationEClass = createEClass(ENUMERATION);
    createEReference(enumerationEClass, ENUMERATION__VALUES);

    enumeratedValueEClass = createEClass(ENUMERATED_VALUE);
    createEReference(enumeratedValueEClass, ENUMERATED_VALUE__DERIVED_TYPE);
    createEAttribute(enumeratedValueEClass, ENUMERATED_VALUE__NAME);

    arrayInitializationEClass = createEClass(ARRAY_INITIALIZATION);
    createEReference(arrayInitializationEClass, ARRAY_INITIALIZATION__INITIAL_ELEMENTS);

    arrayInitialElementsEClass = createEClass(ARRAY_INITIAL_ELEMENTS);
    createEReference(arrayInitialElementsEClass, ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT);
    createEAttribute(arrayInitialElementsEClass, ARRAY_INITIAL_ELEMENTS__INDEX);

    initialElementEClass = createEClass(INITIAL_ELEMENT);

    structureTypeDeclarationEClass = createEClass(STRUCTURE_TYPE_DECLARATION);
    createEReference(structureTypeDeclarationEClass, STRUCTURE_TYPE_DECLARATION__DECLARATION);
    createEReference(structureTypeDeclarationEClass, STRUCTURE_TYPE_DECLARATION__INITIALIZATION);

    structureDeclarationEClass = createEClass(STRUCTURE_DECLARATION);
    createEReference(structureDeclarationEClass, STRUCTURE_DECLARATION__STRUCTURE_ELEMENT);

    structureElementDeclarationEClass = createEClass(STRUCTURE_ELEMENT_DECLARATION);
    createEAttribute(structureElementDeclarationEClass, STRUCTURE_ELEMENT_DECLARATION__NAME);
    createEReference(structureElementDeclarationEClass, STRUCTURE_ELEMENT_DECLARATION__SPEC_INIT);

    initializedStructureEClass = createEClass(INITIALIZED_STRUCTURE);
    createEReference(initializedStructureEClass, INITIALIZED_STRUCTURE__DERIVED_TYPE);
    createEReference(initializedStructureEClass, INITIALIZED_STRUCTURE__INITIALIZATION);

    structureInitializationEClass = createEClass(STRUCTURE_INITIALIZATION);
    createEReference(structureInitializationEClass, STRUCTURE_INITIALIZATION__INITIAL_ELEMENTS);

    structureElementInitializationEClass = createEClass(STRUCTURE_ELEMENT_INITIALIZATION);
    createEAttribute(structureElementInitializationEClass, STRUCTURE_ELEMENT_INITIALIZATION__NAME);
    createEReference(structureElementInitializationEClass, STRUCTURE_ELEMENT_INITIALIZATION__VALUE);

    stringDeclarationEClass = createEClass(STRING_DECLARATION);
    createEAttribute(stringDeclarationEClass, STRING_DECLARATION__STRING);
    createEAttribute(stringDeclarationEClass, STRING_DECLARATION__SIZE);
    createEReference(stringDeclarationEClass, STRING_DECLARATION__INITIAL_VALUE);

    variableEClass = createEClass(VARIABLE);
    createEAttribute(variableEClass, VARIABLE__NAME);

    variableAccessEClass = createEClass(VARIABLE_ACCESS);

    directVariableEClass = createEClass(DIRECT_VARIABLE);
    createEAttribute(directVariableEClass, DIRECT_VARIABLE__NAME);

    symbolicVariableAccessEClass = createEClass(SYMBOLIC_VARIABLE_ACCESS);

    namedVariableAccessEClass = createEClass(NAMED_VARIABLE_ACCESS);
    createEReference(namedVariableAccessEClass, NAMED_VARIABLE_ACCESS__NAMED_VARIABLE);

    multiElementVariableEClass = createEClass(MULTI_ELEMENT_VARIABLE);

    arrayVariableEClass = createEClass(ARRAY_VARIABLE);
    createEReference(arrayVariableEClass, ARRAY_VARIABLE__SUBSCRIPTED_VARIABLE);
    createEReference(arrayVariableEClass, ARRAY_VARIABLE__SUBSCRIPTS);

    structuredVariableEClass = createEClass(STRUCTURED_VARIABLE);
    createEReference(structuredVariableEClass, STRUCTURED_VARIABLE__RECORD_VARIABLE);
    createEAttribute(structuredVariableEClass, STRUCTURED_VARIABLE__FIELD_SELECTOR);

    expressionEClass = createEClass(EXPRESSION);
    createEReference(expressionEClass, EXPRESSION__VARIABLE);
    createEReference(expressionEClass, EXPRESSION__FBNAME);
    createEAttribute(expressionEClass, EXPRESSION__OPENBR);
    createEReference(expressionEClass, EXPRESSION__PARAMASSIGNMENT);
    createEAttribute(expressionEClass, EXPRESSION__CLOSEBR);
    createEReference(expressionEClass, EXPRESSION__EXPRESSION);

    paramAssignmentEClass = createEClass(PARAM_ASSIGNMENT);
    createEReference(paramAssignmentEClass, PARAM_ASSIGNMENT__VARIABLENAME);
    createEReference(paramAssignmentEClass, PARAM_ASSIGNMENT__EXPRESSION);
    createEAttribute(paramAssignmentEClass, PARAM_ASSIGNMENT__NOT);
    createEReference(paramAssignmentEClass, PARAM_ASSIGNMENT__VARIABLE1);
    createEReference(paramAssignmentEClass, PARAM_ASSIGNMENT__VARIABLE2);

    functionDeclarationEClass = createEClass(FUNCTION_DECLARATION);
    createEAttribute(functionDeclarationEClass, FUNCTION_DECLARATION__NAME);
    createEReference(functionDeclarationEClass, FUNCTION_DECLARATION__TYPE);
    createEReference(functionDeclarationEClass, FUNCTION_DECLARATION__IO_VAR_DECLARATIONS);
    createEReference(functionDeclarationEClass, FUNCTION_DECLARATION__BODY);

    ioVarDeclarationsEClass = createEClass(IO_VAR_DECLARATIONS);

    inputDeclarationsEClass = createEClass(INPUT_DECLARATIONS);
    createEReference(inputDeclarationsEClass, INPUT_DECLARATIONS__DECLARATIONS);

    inputDeclarationEClass = createEClass(INPUT_DECLARATION);
    createEReference(inputDeclarationEClass, INPUT_DECLARATION__VAR1_LIST);
    createEReference(inputDeclarationEClass, INPUT_DECLARATION__DECL_SPECIFICATION);

    edgeDeclarationEClass = createEClass(EDGE_DECLARATION);
    createEReference(edgeDeclarationEClass, EDGE_DECLARATION__VAR1_LIST);

    declSpecificationEClass = createEClass(DECL_SPECIFICATION);

    varDeclSpecificationEClass = createEClass(VAR_DECL_SPECIFICATION);
    createEReference(varDeclSpecificationEClass, VAR_DECL_SPECIFICATION__SPEC_INIT);

    edgeDeclSpecificationEClass = createEClass(EDGE_DECL_SPECIFICATION);
    createEAttribute(edgeDeclSpecificationEClass, EDGE_DECL_SPECIFICATION__REDGE);
    createEAttribute(edgeDeclSpecificationEClass, EDGE_DECL_SPECIFICATION__FEDGE);

    var1ListEClass = createEClass(VAR1_LIST);
    createEReference(var1ListEClass, VAR1_LIST__VARIABLES);

    outputDeclarationsEClass = createEClass(OUTPUT_DECLARATIONS);
    createEReference(outputDeclarationsEClass, OUTPUT_DECLARATIONS__INIT_DECLS);

    varInitDeclEClass = createEClass(VAR_INIT_DECL);
    createEReference(varInitDeclEClass, VAR_INIT_DECL__VAR1_LIST);
    createEReference(varInitDeclEClass, VAR_INIT_DECL__SPEC_INIT);

    inputOutputDeclarationsEClass = createEClass(INPUT_OUTPUT_DECLARATIONS);
    createEReference(inputOutputDeclarationsEClass, INPUT_OUTPUT_DECLARATIONS__INIT_DECLS);

    functionBodyEClass = createEClass(FUNCTION_BODY);

    instructionListEClass = createEClass(INSTRUCTION_LIST);
    createEReference(instructionListEClass, INSTRUCTION_LIST__INSTRUCTIONS);

    instructionEClass = createEClass(INSTRUCTION);
    createEReference(instructionEClass, INSTRUCTION__LABEL);
    createEReference(instructionEClass, INSTRUCTION__INSTRUCTION);

    labelEClass = createEClass(LABEL);
    createEAttribute(labelEClass, LABEL__NAME);

    operationEClass = createEClass(OPERATION);

    simpleOperationEClass = createEClass(SIMPLE_OPERATION);
    createEAttribute(simpleOperationEClass, SIMPLE_OPERATION__OPERATOR);
    createEReference(simpleOperationEClass, SIMPLE_OPERATION__OPERAND);

    expressionOperationEClass = createEClass(EXPRESSION_OPERATION);
    createEAttribute(expressionOperationEClass, EXPRESSION_OPERATION__OPERATOR);
    createEReference(expressionOperationEClass, EXPRESSION_OPERATION__OPERAND);
    createEReference(expressionOperationEClass, EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST);

    jmpOperationEClass = createEClass(JMP_OPERATION);
    createEAttribute(jmpOperationEClass, JMP_OPERATION__OPERATOR);
    createEReference(jmpOperationEClass, JMP_OPERATION__LABEL);

    operandEClass = createEClass(OPERAND);
    createEReference(operandEClass, OPERAND__CONSTANT);
    createEReference(operandEClass, OPERAND__REFERENCE);

    referencedOperandEClass = createEClass(REFERENCED_OPERAND);

    simpleInstructionListEClass = createEClass(SIMPLE_INSTRUCTION_LIST);
    createEReference(simpleInstructionListEClass, SIMPLE_INSTRUCTION_LIST__INSTRUCTIONS);

    simpleInstructionEClass = createEClass(SIMPLE_INSTRUCTION);

    statementListEClass = createEClass(STATEMENT_LIST);
    createEReference(statementListEClass, STATEMENT_LIST__STATEMENTS);

    statementEClass = createEClass(STATEMENT);

    assignStatementEClass = createEClass(ASSIGN_STATEMENT);
    createEReference(assignStatementEClass, ASSIGN_STATEMENT__VARIABLE);
    createEReference(assignStatementEClass, ASSIGN_STATEMENT__EXPRESSION);

    selectionStatementEClass = createEClass(SELECTION_STATEMENT);
    createEReference(selectionStatementEClass, SELECTION_STATEMENT__ELSE_STATEMENT_LIST);

    ifStatementEClass = createEClass(IF_STATEMENT);
    createEReference(ifStatementEClass, IF_STATEMENT__IF_EXPRESSION);
    createEReference(ifStatementEClass, IF_STATEMENT__THEN_STATEMENT_LIST);
    createEReference(ifStatementEClass, IF_STATEMENT__ELSE_IFS);

    elseIfEClass = createEClass(ELSE_IF);
    createEReference(elseIfEClass, ELSE_IF__EXPRESSION);
    createEReference(elseIfEClass, ELSE_IF__STATEMENT_LIST);

    caseStatementEClass = createEClass(CASE_STATEMENT);
    createEReference(caseStatementEClass, CASE_STATEMENT__EXPRESSION);
    createEReference(caseStatementEClass, CASE_STATEMENT__CASE_ELEMENTS);

    caseElementEClass = createEClass(CASE_ELEMENT);
    createEReference(caseElementEClass, CASE_ELEMENT__CASE_LIST);
    createEReference(caseElementEClass, CASE_ELEMENT__STATEMENT_LIST);

    caseListEClass = createEClass(CASE_LIST);
    createEReference(caseListEClass, CASE_LIST__ELEMENTS);

    caseListElementEClass = createEClass(CASE_LIST_ELEMENT);
    createEAttribute(caseListElementEClass, CASE_LIST_ELEMENT__SUB_RANGE);
    createEAttribute(caseListElementEClass, CASE_LIST_ELEMENT__INTEGER);
    createEReference(caseListElementEClass, CASE_LIST_ELEMENT__ENUMERATED_VALUE);

    functionBlockDeclarationEClass = createEClass(FUNCTION_BLOCK_DECLARATION);
    createEReference(functionBlockDeclarationEClass, FUNCTION_BLOCK_DECLARATION__VAR_DECLARATIONS);
    createEReference(functionBlockDeclarationEClass, FUNCTION_BLOCK_DECLARATION__BODY);

    functionBlockVarDeclarationsEClass = createEClass(FUNCTION_BLOCK_VAR_DECLARATIONS);

    otherVarDeclarationsEClass = createEClass(OTHER_VAR_DECLARATIONS);

    varDeclarationsEClass = createEClass(VAR_DECLARATIONS);
    createEAttribute(varDeclarationsEClass, VAR_DECLARATIONS__CONSTANT);
    createEReference(varDeclarationsEClass, VAR_DECLARATIONS__INIT_DECLS);

    functionBlockBodyEClass = createEClass(FUNCTION_BLOCK_BODY);

    programDeclarationEClass = createEClass(PROGRAM_DECLARATION);
    createEReference(programDeclarationEClass, PROGRAM_DECLARATION__VAR_DECLARATIONS);
    createEReference(programDeclarationEClass, PROGRAM_DECLARATION__BODY);

    programVarDeclarationsEClass = createEClass(PROGRAM_VAR_DECLARATIONS);

    locatedVarDeclarationsEClass = createEClass(LOCATED_VAR_DECLARATIONS);
    createEReference(locatedVarDeclarationsEClass, LOCATED_VAR_DECLARATIONS__LOCATED_VAR_DECLARATION);

    locatedVarDeclarationEClass = createEClass(LOCATED_VAR_DECLARATION);
    createEAttribute(locatedVarDeclarationEClass, LOCATED_VAR_DECLARATION__NAME);
    createEReference(locatedVarDeclarationEClass, LOCATED_VAR_DECLARATION__LOCATION);
    createEReference(locatedVarDeclarationEClass, LOCATED_VAR_DECLARATION__SPEC_INIT);

    locationEClass = createEClass(LOCATION);
    createEReference(locationEClass, LOCATION__DIRECT_VARIABLE);

    programAccessDeclsEClass = createEClass(PROGRAM_ACCESS_DECLS);
    createEReference(programAccessDeclsEClass, PROGRAM_ACCESS_DECLS__PROGRAM_ACCESS_DECL);

    programAccessDeclEClass = createEClass(PROGRAM_ACCESS_DECL);
    createEReference(programAccessDeclEClass, PROGRAM_ACCESS_DECL__ACCESS_NAME);
    createEReference(programAccessDeclEClass, PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE);
    createEReference(programAccessDeclEClass, PROGRAM_ACCESS_DECL__TYPE_NAME);
    createEAttribute(programAccessDeclEClass, PROGRAM_ACCESS_DECL__DIRECTION);

    configurationDeclarationEClass = createEClass(CONFIGURATION_DECLARATION);
    createEReference(configurationDeclarationEClass, CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS);
    createEReference(configurationDeclarationEClass, CONFIGURATION_DECLARATION__RESDECL);

    resourceDeclarationEClass = createEClass(RESOURCE_DECLARATION);
    createEAttribute(resourceDeclarationEClass, RESOURCE_DECLARATION__RESNAME);
    createEReference(resourceDeclarationEClass, RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS);
    createEReference(resourceDeclarationEClass, RESOURCE_DECLARATION__SINGLERESOURCE);

    singleResourceDeclarationEClass = createEClass(SINGLE_RESOURCE_DECLARATION);
    createEReference(singleResourceDeclarationEClass, SINGLE_RESOURCE_DECLARATION__TASK_CONF);
    createEReference(singleResourceDeclarationEClass, SINGLE_RESOURCE_DECLARATION__PROGRAM_CONF);

    programConfigurationEClass = createEClass(PROGRAM_CONFIGURATION);
    createEAttribute(programConfigurationEClass, PROGRAM_CONFIGURATION__NAME);
    createEReference(programConfigurationEClass, PROGRAM_CONFIGURATION__TASK);
    createEReference(programConfigurationEClass, PROGRAM_CONFIGURATION__PROG);
    createEReference(programConfigurationEClass, PROGRAM_CONFIGURATION__PROG_CONF);

    progConfElementsEClass = createEClass(PROG_CONF_ELEMENTS);
    createEReference(progConfElementsEClass, PROG_CONF_ELEMENTS__PROGCONF);

    progConfElementEClass = createEClass(PROG_CONF_ELEMENT);

    fbTaskEClass = createEClass(FB_TASK);
    createEReference(fbTaskEClass, FB_TASK__FBNAME);
    createEReference(fbTaskEClass, FB_TASK__TASK);

    progCNXNEClass = createEClass(PROG_CNXN);
    createEAttribute(progCNXNEClass, PROG_CNXN__VARIABLENAME);
    createEReference(progCNXNEClass, PROG_CNXN__PROGD);

    dataSinkEClass = createEClass(DATA_SINK);
    createEReference(dataSinkEClass, DATA_SINK__GLOBVAR);
    createEReference(dataSinkEClass, DATA_SINK__DIRVAR);

    progDataSourceEClass = createEClass(PROG_DATA_SOURCE);

    globalVarDeclarationsEClass = createEClass(GLOBAL_VAR_DECLARATIONS);
    createEReference(globalVarDeclarationsEClass, GLOBAL_VAR_DECLARATIONS__GLOBAL_VAR_DECL);

    globalVarDeclEClass = createEClass(GLOBAL_VAR_DECL);
    createEReference(globalVarDeclEClass, GLOBAL_VAR_DECL__SPEC);
    createEReference(globalVarDeclEClass, GLOBAL_VAR_DECL__SPEC_INIT);

    globalVarSpecEClass = createEClass(GLOBAL_VAR_SPEC);
    createEReference(globalVarSpecEClass, GLOBAL_VAR_SPEC__VARIABLE);
    createEReference(globalVarSpecEClass, GLOBAL_VAR_SPEC__LOCATION);

    globalVarListEClass = createEClass(GLOBAL_VAR_LIST);
    createEReference(globalVarListEClass, GLOBAL_VAR_LIST__VARIABLES);

    globalVarEClass = createEClass(GLOBAL_VAR);
    createEAttribute(globalVarEClass, GLOBAL_VAR__NAME);

    taskConfigurationEClass = createEClass(TASK_CONFIGURATION);
    createEAttribute(taskConfigurationEClass, TASK_CONFIGURATION__NAME);
    createEReference(taskConfigurationEClass, TASK_CONFIGURATION__TASK_INIT);

    taskInitializationEClass = createEClass(TASK_INITIALIZATION);
    createEReference(taskInitializationEClass, TASK_INITIALIZATION__SINGLE);
    createEReference(taskInitializationEClass, TASK_INITIALIZATION__INTERVAL);
    createEAttribute(taskInitializationEClass, TASK_INITIALIZATION__PRIOR);

    dataSourceEClass = createEClass(DATA_SOURCE);

    constantEClass = createEClass(CONSTANT);

    numericLiteralEClass = createEClass(NUMERIC_LITERAL);
    createEAttribute(numericLiteralEClass, NUMERIC_LITERAL__VALUE);

    integerLiteralEClass = createEClass(INTEGER_LITERAL);
    createEReference(integerLiteralEClass, INTEGER_LITERAL__TYPE);

    realLiteralEClass = createEClass(REAL_LITERAL);
    createEReference(realLiteralEClass, REAL_LITERAL__TYPE);

    characterStringEClass = createEClass(CHARACTER_STRING);
    createEAttribute(characterStringEClass, CHARACTER_STRING__VALUE);

    bitStringEClass = createEClass(BIT_STRING);
    createEAttribute(bitStringEClass, BIT_STRING__VALUE);

    booleanEClass = createEClass(BOOLEAN);
    createEAttribute(booleanEClass, BOOLEAN__BOOL_INT);
    createEAttribute(booleanEClass, BOOLEAN__TRUE);

    timeLiteralEClass = createEClass(TIME_LITERAL);

    durationLiteralEClass = createEClass(DURATION_LITERAL);
    createEAttribute(durationLiteralEClass, DURATION_LITERAL__DURATION);

    timeOfDayLiteralEClass = createEClass(TIME_OF_DAY_LITERAL);
    createEAttribute(timeOfDayLiteralEClass, TIME_OF_DAY_LITERAL__HOUR);
    createEAttribute(timeOfDayLiteralEClass, TIME_OF_DAY_LITERAL__MINUTE);
    createEAttribute(timeOfDayLiteralEClass, TIME_OF_DAY_LITERAL__SECOND);

    dateLiteralEClass = createEClass(DATE_LITERAL);
    createEAttribute(dateLiteralEClass, DATE_LITERAL__YEAR);
    createEAttribute(dateLiteralEClass, DATE_LITERAL__MONTH);
    createEAttribute(dateLiteralEClass, DATE_LITERAL__DAY);

    dateAndTimeLiteralEClass = createEClass(DATE_AND_TIME_LITERAL);
    createEAttribute(dateAndTimeLiteralEClass, DATE_AND_TIME_LITERAL__YEAR);
    createEAttribute(dateAndTimeLiteralEClass, DATE_AND_TIME_LITERAL__MONTH);
    createEAttribute(dateAndTimeLiteralEClass, DATE_AND_TIME_LITERAL__DAY);
    createEAttribute(dateAndTimeLiteralEClass, DATE_AND_TIME_LITERAL__HOUR);
    createEAttribute(dateAndTimeLiteralEClass, DATE_AND_TIME_LITERAL__MINUTE);
    createEAttribute(dateAndTimeLiteralEClass, DATE_AND_TIME_LITERAL__SECOND);

    binaryExpressionEClass = createEClass(BINARY_EXPRESSION);
    createEReference(binaryExpressionEClass, BINARY_EXPRESSION__LEFT);
    createEAttribute(binaryExpressionEClass, BINARY_EXPRESSION__OPERATOR);
    createEReference(binaryExpressionEClass, BINARY_EXPRESSION__RIGHT);

    unaryExpressionEClass = createEClass(UNARY_EXPRESSION);
    createEAttribute(unaryExpressionEClass, UNARY_EXPRESSION__OPERATOR);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    libraryElementEClass.getESuperTypes().add(this.getModelElement());
    libraryElementDeclarationEClass.getESuperTypes().add(this.getModelElement());
    dataTypeEClass.getESuperTypes().add(this.getLibraryElement());
    nonGenericTypeEClass.getESuperTypes().add(this.getDataType());
    elementaryTypeEClass.getESuperTypes().add(this.getNonGenericType());
    stringTypeEClass.getESuperTypes().add(this.getElementaryType());
    numericTypeEClass.getESuperTypes().add(this.getElementaryType());
    integerTypeEClass.getESuperTypes().add(this.getNumericType());
    signedIntegerTypeEClass.getESuperTypes().add(this.getIntegerType());
    signedIntegerTypeEClass.getESuperTypes().add(this.getPlainIntegerType());
    plainIntegerTypeEClass.getESuperTypes().add(this.getIntegerType());
    unsignedIntegerTypeEClass.getESuperTypes().add(this.getIntegerType());
    realTypeEClass.getESuperTypes().add(this.getNumericType());
    dateTypeEClass.getESuperTypes().add(this.getElementaryType());
    bitStringTypeEClass.getESuperTypes().add(this.getElementaryType());
    genericTypeEClass.getESuperTypes().add(this.getDataType());
    derivedTypeEClass.getESuperTypes().add(this.getNonGenericType());
    dataTypeDeclarationEClass.getESuperTypes().add(this.getLibraryElementDeclaration());
    simpleTypeDeclarationEClass.getESuperTypes().add(this.getTypeDeclaration());
    rangeDeclarationEClass.getESuperTypes().add(this.getTypeDeclaration());
    enumDeclarationEClass.getESuperTypes().add(this.getTypeDeclaration());
    arrayDeclarationEClass.getESuperTypes().add(this.getTypeDeclaration());
    enumeratedValueEClass.getESuperTypes().add(this.getInitialElement());
    enumeratedValueEClass.getESuperTypes().add(this.getReferencedOperand());
    arrayInitializationEClass.getESuperTypes().add(this.getInitialElement());
    structureTypeDeclarationEClass.getESuperTypes().add(this.getTypeDeclaration());
    structureInitializationEClass.getESuperTypes().add(this.getInitialElement());
    stringDeclarationEClass.getESuperTypes().add(this.getTypeDeclaration());
    variableAccessEClass.getESuperTypes().add(this.getReferencedOperand());
    directVariableEClass.getESuperTypes().add(this.getVariableAccess());
    directVariableEClass.getESuperTypes().add(this.getProgDataSource());
    directVariableEClass.getESuperTypes().add(this.getDataSource());
    symbolicVariableAccessEClass.getESuperTypes().add(this.getVariableAccess());
    namedVariableAccessEClass.getESuperTypes().add(this.getSymbolicVariableAccess());
    multiElementVariableEClass.getESuperTypes().add(this.getSymbolicVariableAccess());
    arrayVariableEClass.getESuperTypes().add(this.getMultiElementVariable());
    structuredVariableEClass.getESuperTypes().add(this.getMultiElementVariable());
    functionDeclarationEClass.getESuperTypes().add(this.getLibraryElement());
    ioVarDeclarationsEClass.getESuperTypes().add(this.getFunctionBlockVarDeclarations());
    ioVarDeclarationsEClass.getESuperTypes().add(this.getProgramVarDeclarations());
    inputDeclarationsEClass.getESuperTypes().add(this.getIoVarDeclarations());
    varDeclSpecificationEClass.getESuperTypes().add(this.getDeclSpecification());
    edgeDeclSpecificationEClass.getESuperTypes().add(this.getDeclSpecification());
    outputDeclarationsEClass.getESuperTypes().add(this.getIoVarDeclarations());
    inputOutputDeclarationsEClass.getESuperTypes().add(this.getIoVarDeclarations());
    instructionListEClass.getESuperTypes().add(this.getFunctionBody());
    instructionListEClass.getESuperTypes().add(this.getFunctionBlockBody());
    simpleOperationEClass.getESuperTypes().add(this.getOperation());
    simpleOperationEClass.getESuperTypes().add(this.getSimpleInstruction());
    expressionOperationEClass.getESuperTypes().add(this.getOperation());
    expressionOperationEClass.getESuperTypes().add(this.getSimpleInstruction());
    jmpOperationEClass.getESuperTypes().add(this.getOperation());
    statementListEClass.getESuperTypes().add(this.getFunctionBody());
    statementListEClass.getESuperTypes().add(this.getFunctionBlockBody());
    assignStatementEClass.getESuperTypes().add(this.getStatement());
    selectionStatementEClass.getESuperTypes().add(this.getStatement());
    ifStatementEClass.getESuperTypes().add(this.getSelectionStatement());
    caseStatementEClass.getESuperTypes().add(this.getSelectionStatement());
    functionBlockDeclarationEClass.getESuperTypes().add(this.getLibraryElementDeclaration());
    otherVarDeclarationsEClass.getESuperTypes().add(this.getFunctionBlockVarDeclarations());
    otherVarDeclarationsEClass.getESuperTypes().add(this.getProgramVarDeclarations());
    varDeclarationsEClass.getESuperTypes().add(this.getOtherVarDeclarations());
    programDeclarationEClass.getESuperTypes().add(this.getLibraryElementDeclaration());
    locatedVarDeclarationsEClass.getESuperTypes().add(this.getProgramVarDeclarations());
    programAccessDeclsEClass.getESuperTypes().add(this.getProgramVarDeclarations());
    configurationDeclarationEClass.getESuperTypes().add(this.getLibraryElementDeclaration());
    resourceDeclarationEClass.getESuperTypes().add(this.getLibraryElementDeclaration());
    fbTaskEClass.getESuperTypes().add(this.getProgConfElement());
    progCNXNEClass.getESuperTypes().add(this.getProgConfElement());
    globalVarListEClass.getESuperTypes().add(this.getGlobalVarSpec());
    constantEClass.getESuperTypes().add(this.getInitialElement());
    constantEClass.getESuperTypes().add(this.getExpression());
    constantEClass.getESuperTypes().add(this.getProgDataSource());
    constantEClass.getESuperTypes().add(this.getDataSource());
    numericLiteralEClass.getESuperTypes().add(this.getConstant());
    integerLiteralEClass.getESuperTypes().add(this.getNumericLiteral());
    realLiteralEClass.getESuperTypes().add(this.getNumericLiteral());
    characterStringEClass.getESuperTypes().add(this.getConstant());
    bitStringEClass.getESuperTypes().add(this.getConstant());
    booleanEClass.getESuperTypes().add(this.getConstant());
    timeLiteralEClass.getESuperTypes().add(this.getConstant());
    durationLiteralEClass.getESuperTypes().add(this.getTimeLiteral());
    timeOfDayLiteralEClass.getESuperTypes().add(this.getTimeLiteral());
    dateLiteralEClass.getESuperTypes().add(this.getTimeLiteral());
    dateAndTimeLiteralEClass.getESuperTypes().add(this.getTimeLiteral());
    binaryExpressionEClass.getESuperTypes().add(this.getExpression());
    unaryExpressionEClass.getESuperTypes().add(this.getExpression());

    // Initialize classes and features; add operations and parameters
    initEClass(modelEClass, Model.class, "Model", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getModel_ModelElement(), this.getModelElement(), null, "modelElement", null, 0, -1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(modelElementEClass, ModelElement.class, "ModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(libraryElementEClass, LibraryElement.class, "LibraryElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(libraryElementDeclarationEClass, LibraryElementDeclaration.class, "LibraryElementDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getLibraryElementDeclaration_Name(), ecorePackage.getEString(), "name", null, 0, 1, LibraryElementDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(dataTypeEClass, DataType.class, "DataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(nonGenericTypeEClass, NonGenericType.class, "NonGenericType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(elementaryTypeEClass, ElementaryType.class, "ElementaryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getElementaryType_TypeName(), ecorePackage.getEString(), "typeName", null, 0, 1, ElementaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stringTypeEClass, StringType.class, "StringType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(numericTypeEClass, NumericType.class, "NumericType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(integerTypeEClass, IntegerType.class, "IntegerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(signedIntegerTypeEClass, SignedIntegerType.class, "SignedIntegerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(plainIntegerTypeEClass, PlainIntegerType.class, "PlainIntegerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(unsignedIntegerTypeEClass, UnsignedIntegerType.class, "UnsignedIntegerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(realTypeEClass, RealType.class, "RealType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(dateTypeEClass, DateType.class, "DateType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(bitStringTypeEClass, BitStringType.class, "BitStringType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(genericTypeEClass, GenericType.class, "GenericType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getGenericType_TypeName(), ecorePackage.getEString(), "typeName", null, 0, 1, GenericType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(derivedTypeEClass, DerivedType.class, "DerivedType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDerivedType_Name(), ecorePackage.getEString(), "name", null, 0, 1, DerivedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(dataTypeDeclarationEClass, DataTypeDeclaration.class, "DataTypeDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getDataTypeDeclaration_TypeDeclaration(), this.getTypeDeclaration(), null, "typeDeclaration", null, 0, -1, DataTypeDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(typeDeclarationEClass, TypeDeclaration.class, "TypeDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTypeDeclaration_DerivedType(), this.getDerivedType(), null, "derivedType", null, 0, 1, TypeDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(simpleTypeDeclarationEClass, SimpleTypeDeclaration.class, "SimpleTypeDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSimpleTypeDeclaration_SpecInit(), this.getSpecInit(), null, "specInit", null, 0, 1, SimpleTypeDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(specInitEClass, SpecInit.class, "SpecInit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSpecInit_BaseType(), this.getNonGenericType(), null, "baseType", null, 0, 1, SpecInit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSpecInit_Constant(), this.getInitialElement(), null, "constant", null, 0, 1, SpecInit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(rangeDeclarationEClass, RangeDeclaration.class, "RangeDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRangeDeclaration_BaseType(), this.getElementaryType(), null, "baseType", null, 0, 1, RangeDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRangeDeclaration_Range(), ecorePackage.getEString(), "range", null, 0, 1, RangeDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRangeDeclaration_Constant(), ecorePackage.getEString(), "constant", null, 0, 1, RangeDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(enumDeclarationEClass, EnumDeclaration.class, "EnumDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getEnumDeclaration_Enumeration(), this.getEnumeration(), null, "enumeration", null, 0, 1, EnumDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getEnumDeclaration_Constant(), this.getEnumeratedValue(), null, "constant", null, 0, 1, EnumDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(arrayDeclarationEClass, ArrayDeclaration.class, "ArrayDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getArrayDeclaration_Ranges(), ecorePackage.getEString(), "ranges", null, 0, -1, ArrayDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getArrayDeclaration_BaseType(), this.getNonGenericType(), null, "baseType", null, 0, 1, ArrayDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getArrayDeclaration_Constant(), this.getArrayInitialization(), null, "constant", null, 0, 1, ArrayDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(enumerationEClass, Enumeration.class, "Enumeration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getEnumeration_Values(), this.getEnumeratedValue(), null, "values", null, 0, -1, Enumeration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(enumeratedValueEClass, EnumeratedValue.class, "EnumeratedValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getEnumeratedValue_DerivedType(), this.getDerivedType(), null, "derivedType", null, 0, 1, EnumeratedValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getEnumeratedValue_Name(), ecorePackage.getEString(), "name", null, 0, 1, EnumeratedValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(arrayInitializationEClass, ArrayInitialization.class, "ArrayInitialization", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getArrayInitialization_InitialElements(), this.getArrayInitialElements(), null, "initialElements", null, 0, -1, ArrayInitialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(arrayInitialElementsEClass, ArrayInitialElements.class, "ArrayInitialElements", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getArrayInitialElements_InitialElement(), this.getInitialElement(), null, "initialElement", null, 0, 1, ArrayInitialElements.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getArrayInitialElements_Index(), ecorePackage.getEString(), "index", null, 0, 1, ArrayInitialElements.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(initialElementEClass, InitialElement.class, "InitialElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(structureTypeDeclarationEClass, StructureTypeDeclaration.class, "StructureTypeDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getStructureTypeDeclaration_Declaration(), this.getStructureDeclaration(), null, "declaration", null, 0, 1, StructureTypeDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getStructureTypeDeclaration_Initialization(), this.getInitializedStructure(), null, "initialization", null, 0, 1, StructureTypeDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(structureDeclarationEClass, StructureDeclaration.class, "StructureDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getStructureDeclaration_StructureElement(), this.getStructureElementDeclaration(), null, "structureElement", null, 0, -1, StructureDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(structureElementDeclarationEClass, StructureElementDeclaration.class, "StructureElementDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getStructureElementDeclaration_Name(), ecorePackage.getEString(), "name", null, 0, 1, StructureElementDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getStructureElementDeclaration_SpecInit(), this.getSpecInit(), null, "specInit", null, 0, 1, StructureElementDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(initializedStructureEClass, InitializedStructure.class, "InitializedStructure", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getInitializedStructure_DerivedType(), this.getDerivedType(), null, "derivedType", null, 0, 1, InitializedStructure.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getInitializedStructure_Initialization(), this.getStructureInitialization(), null, "initialization", null, 0, 1, InitializedStructure.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(structureInitializationEClass, StructureInitialization.class, "StructureInitialization", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getStructureInitialization_InitialElements(), this.getStructureElementInitialization(), null, "initialElements", null, 0, -1, StructureInitialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(structureElementInitializationEClass, StructureElementInitialization.class, "StructureElementInitialization", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getStructureElementInitialization_Name(), ecorePackage.getEString(), "name", null, 0, 1, StructureElementInitialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getStructureElementInitialization_Value(), this.getInitialElement(), null, "value", null, 0, 1, StructureElementInitialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stringDeclarationEClass, StringDeclaration.class, "StringDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getStringDeclaration_String(), ecorePackage.getEBoolean(), "string", null, 0, 1, StringDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStringDeclaration_Size(), ecorePackage.getEString(), "size", null, 0, 1, StringDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getStringDeclaration_InitialValue(), this.getCharacterString(), null, "initialValue", null, 0, 1, StringDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(variableEClass, Variable.class, "Variable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getVariable_Name(), ecorePackage.getEString(), "name", null, 0, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(variableAccessEClass, VariableAccess.class, "VariableAccess", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(directVariableEClass, DirectVariable.class, "DirectVariable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDirectVariable_Name(), ecorePackage.getEString(), "name", null, 0, 1, DirectVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(symbolicVariableAccessEClass, SymbolicVariableAccess.class, "SymbolicVariableAccess", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(namedVariableAccessEClass, NamedVariableAccess.class, "NamedVariableAccess", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getNamedVariableAccess_NamedVariable(), this.getVariable(), null, "namedVariable", null, 0, 1, NamedVariableAccess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(multiElementVariableEClass, MultiElementVariable.class, "MultiElementVariable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(arrayVariableEClass, ArrayVariable.class, "ArrayVariable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getArrayVariable_SubscriptedVariable(), this.getVariable(), null, "subscriptedVariable", null, 0, 1, ArrayVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getArrayVariable_Subscripts(), this.getExpression(), null, "subscripts", null, 0, -1, ArrayVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(structuredVariableEClass, StructuredVariable.class, "StructuredVariable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getStructuredVariable_RecordVariable(), this.getVariable(), null, "recordVariable", null, 0, 1, StructuredVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStructuredVariable_FieldSelector(), ecorePackage.getEString(), "fieldSelector", null, 0, 1, StructuredVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(expressionEClass, Expression.class, "Expression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getExpression_Variable(), this.getVariableAccess(), null, "variable", null, 0, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getExpression_Fbname(), this.getFunctionDeclaration(), null, "fbname", null, 0, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getExpression_Openbr(), ecorePackage.getEString(), "openbr", null, 0, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getExpression_Paramassignment(), this.getParamAssignment(), null, "paramassignment", null, 0, -1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getExpression_Closebr(), ecorePackage.getEString(), "closebr", null, 0, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getExpression_Expression(), this.getExpression(), null, "expression", null, 0, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(paramAssignmentEClass, ParamAssignment.class, "ParamAssignment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getParamAssignment_Variablename(), this.getNamedVariableAccess(), null, "variablename", null, 0, 1, ParamAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getParamAssignment_Expression(), this.getExpression(), null, "expression", null, 0, 1, ParamAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getParamAssignment_Not(), ecorePackage.getEBoolean(), "not", null, 0, 1, ParamAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getParamAssignment_Variable1(), this.getNamedVariableAccess(), null, "variable1", null, 0, 1, ParamAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getParamAssignment_Variable2(), this.getVariable(), null, "variable2", null, 0, 1, ParamAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(functionDeclarationEClass, FunctionDeclaration.class, "FunctionDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getFunctionDeclaration_Name(), ecorePackage.getEString(), "name", null, 0, 1, FunctionDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFunctionDeclaration_Type(), this.getNonGenericType(), null, "type", null, 0, 1, FunctionDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFunctionDeclaration_IoVarDeclarations(), this.getIoVarDeclarations(), null, "ioVarDeclarations", null, 0, -1, FunctionDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFunctionDeclaration_Body(), this.getFunctionBody(), null, "body", null, 0, 1, FunctionDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(ioVarDeclarationsEClass, IoVarDeclarations.class, "IoVarDeclarations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(inputDeclarationsEClass, InputDeclarations.class, "InputDeclarations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getInputDeclarations_Declarations(), this.getInputDeclaration(), null, "declarations", null, 0, -1, InputDeclarations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(inputDeclarationEClass, InputDeclaration.class, "InputDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getInputDeclaration_Var1List(), this.getVar1List(), null, "var1List", null, 0, 1, InputDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getInputDeclaration_DeclSpecification(), this.getDeclSpecification(), null, "declSpecification", null, 0, 1, InputDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(edgeDeclarationEClass, EdgeDeclaration.class, "EdgeDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getEdgeDeclaration_Var1List(), this.getVar1List(), null, "var1List", null, 0, 1, EdgeDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(declSpecificationEClass, DeclSpecification.class, "DeclSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(varDeclSpecificationEClass, VarDeclSpecification.class, "VarDeclSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getVarDeclSpecification_SpecInit(), this.getSpecInit(), null, "specInit", null, 0, 1, VarDeclSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(edgeDeclSpecificationEClass, EdgeDeclSpecification.class, "EdgeDeclSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getEdgeDeclSpecification_REdge(), ecorePackage.getEBoolean(), "rEdge", null, 0, 1, EdgeDeclSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getEdgeDeclSpecification_FEdge(), ecorePackage.getEBoolean(), "fEdge", null, 0, 1, EdgeDeclSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(var1ListEClass, Var1List.class, "Var1List", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getVar1List_Variables(), this.getVariable(), null, "variables", null, 0, -1, Var1List.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(outputDeclarationsEClass, OutputDeclarations.class, "OutputDeclarations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOutputDeclarations_InitDecls(), this.getVarInitDecl(), null, "initDecls", null, 0, -1, OutputDeclarations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(varInitDeclEClass, VarInitDecl.class, "VarInitDecl", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getVarInitDecl_Var1List(), this.getVar1List(), null, "var1List", null, 0, 1, VarInitDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getVarInitDecl_SpecInit(), this.getSpecInit(), null, "specInit", null, 0, 1, VarInitDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(inputOutputDeclarationsEClass, InputOutputDeclarations.class, "InputOutputDeclarations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getInputOutputDeclarations_InitDecls(), this.getVarInitDecl(), null, "initDecls", null, 0, -1, InputOutputDeclarations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(functionBodyEClass, FunctionBody.class, "FunctionBody", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(instructionListEClass, InstructionList.class, "InstructionList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getInstructionList_Instructions(), this.getInstruction(), null, "instructions", null, 0, -1, InstructionList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(instructionEClass, Instruction.class, "Instruction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getInstruction_Label(), this.getLabel(), null, "label", null, 0, 1, Instruction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getInstruction_Instruction(), this.getOperation(), null, "instruction", null, 0, 1, Instruction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(labelEClass, Label.class, "Label", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getLabel_Name(), ecorePackage.getEString(), "name", null, 0, 1, Label.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(operationEClass, Operation.class, "Operation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(simpleOperationEClass, SimpleOperation.class, "SimpleOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSimpleOperation_Operator(), ecorePackage.getEString(), "operator", null, 0, 1, SimpleOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSimpleOperation_Operand(), this.getOperand(), null, "operand", null, 0, 1, SimpleOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(expressionOperationEClass, ExpressionOperation.class, "ExpressionOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getExpressionOperation_Operator(), ecorePackage.getEString(), "operator", null, 0, 1, ExpressionOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getExpressionOperation_Operand(), this.getOperand(), null, "operand", null, 0, 1, ExpressionOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getExpressionOperation_SimpleInstructionList(), this.getSimpleInstructionList(), null, "simpleInstructionList", null, 0, 1, ExpressionOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jmpOperationEClass, JmpOperation.class, "JmpOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getJmpOperation_Operator(), ecorePackage.getEString(), "operator", null, 0, 1, JmpOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getJmpOperation_Label(), this.getLabel(), null, "label", null, 0, 1, JmpOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(operandEClass, Operand.class, "Operand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOperand_Constant(), this.getConstant(), null, "constant", null, 0, 1, Operand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOperand_Reference(), this.getReferencedOperand(), null, "reference", null, 0, 1, Operand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(referencedOperandEClass, ReferencedOperand.class, "ReferencedOperand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(simpleInstructionListEClass, SimpleInstructionList.class, "SimpleInstructionList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSimpleInstructionList_Instructions(), this.getSimpleInstruction(), null, "instructions", null, 0, -1, SimpleInstructionList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(simpleInstructionEClass, SimpleInstruction.class, "SimpleInstruction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(statementListEClass, StatementList.class, "StatementList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getStatementList_Statements(), this.getStatement(), null, "statements", null, 0, -1, StatementList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(statementEClass, Statement.class, "Statement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(assignStatementEClass, AssignStatement.class, "AssignStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getAssignStatement_Variable(), this.getVariable(), null, "variable", null, 0, 1, AssignStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getAssignStatement_Expression(), this.getExpression(), null, "expression", null, 0, 1, AssignStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(selectionStatementEClass, SelectionStatement.class, "SelectionStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSelectionStatement_ElseStatementList(), this.getStatementList(), null, "elseStatementList", null, 0, 1, SelectionStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(ifStatementEClass, IfStatement.class, "IfStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIfStatement_IfExpression(), this.getExpression(), null, "ifExpression", null, 0, 1, IfStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIfStatement_ThenStatementList(), this.getStatementList(), null, "thenStatementList", null, 0, 1, IfStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIfStatement_ElseIfs(), this.getElseIf(), null, "elseIfs", null, 0, -1, IfStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(elseIfEClass, ElseIf.class, "ElseIf", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getElseIf_Expression(), this.getExpression(), null, "expression", null, 0, 1, ElseIf.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getElseIf_StatementList(), this.getStatementList(), null, "statementList", null, 0, 1, ElseIf.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(caseStatementEClass, CaseStatement.class, "CaseStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getCaseStatement_Expression(), this.getExpression(), null, "expression", null, 0, 1, CaseStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getCaseStatement_CaseElements(), this.getCaseElement(), null, "caseElements", null, 0, -1, CaseStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(caseElementEClass, CaseElement.class, "CaseElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getCaseElement_CaseList(), this.getCaseList(), null, "caseList", null, 0, 1, CaseElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getCaseElement_StatementList(), this.getStatementList(), null, "statementList", null, 0, 1, CaseElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(caseListEClass, CaseList.class, "CaseList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getCaseList_Elements(), this.getCaseListElement(), null, "elements", null, 0, -1, CaseList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(caseListElementEClass, CaseListElement.class, "CaseListElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getCaseListElement_SubRange(), ecorePackage.getEString(), "subRange", null, 0, 1, CaseListElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getCaseListElement_Integer(), ecorePackage.getEString(), "integer", null, 0, 1, CaseListElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getCaseListElement_EnumeratedValue(), this.getEnumeratedValue(), null, "enumeratedValue", null, 0, 1, CaseListElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(functionBlockDeclarationEClass, FunctionBlockDeclaration.class, "FunctionBlockDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getFunctionBlockDeclaration_VarDeclarations(), this.getFunctionBlockVarDeclarations(), null, "varDeclarations", null, 0, -1, FunctionBlockDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFunctionBlockDeclaration_Body(), this.getFunctionBlockBody(), null, "body", null, 0, 1, FunctionBlockDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(functionBlockVarDeclarationsEClass, FunctionBlockVarDeclarations.class, "FunctionBlockVarDeclarations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(otherVarDeclarationsEClass, OtherVarDeclarations.class, "OtherVarDeclarations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(varDeclarationsEClass, VarDeclarations.class, "VarDeclarations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getVarDeclarations_Constant(), ecorePackage.getEBoolean(), "constant", null, 0, 1, VarDeclarations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getVarDeclarations_InitDecls(), this.getVarInitDecl(), null, "initDecls", null, 0, -1, VarDeclarations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(functionBlockBodyEClass, FunctionBlockBody.class, "FunctionBlockBody", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(programDeclarationEClass, ProgramDeclaration.class, "ProgramDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getProgramDeclaration_VarDeclarations(), this.getProgramVarDeclarations(), null, "varDeclarations", null, 0, -1, ProgramDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProgramDeclaration_Body(), this.getFunctionBlockBody(), null, "body", null, 0, 1, ProgramDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(programVarDeclarationsEClass, ProgramVarDeclarations.class, "ProgramVarDeclarations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(locatedVarDeclarationsEClass, LocatedVarDeclarations.class, "LocatedVarDeclarations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getLocatedVarDeclarations_LocatedVarDeclaration(), this.getLocatedVarDeclaration(), null, "locatedVarDeclaration", null, 0, -1, LocatedVarDeclarations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(locatedVarDeclarationEClass, LocatedVarDeclaration.class, "LocatedVarDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getLocatedVarDeclaration_Name(), ecorePackage.getEString(), "name", null, 0, 1, LocatedVarDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getLocatedVarDeclaration_Location(), this.getLocation(), null, "location", null, 0, 1, LocatedVarDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getLocatedVarDeclaration_SpecInit(), this.getSpecInit(), null, "specInit", null, 0, 1, LocatedVarDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(locationEClass, Location.class, "Location", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getLocation_DirectVariable(), this.getDirectVariable(), null, "directVariable", null, 0, 1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(programAccessDeclsEClass, ProgramAccessDecls.class, "ProgramAccessDecls", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getProgramAccessDecls_ProgramAccessDecl(), this.getProgramAccessDecl(), null, "programAccessDecl", null, 0, -1, ProgramAccessDecls.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(programAccessDeclEClass, ProgramAccessDecl.class, "ProgramAccessDecl", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getProgramAccessDecl_AccessName(), this.getVariable(), null, "accessName", null, 0, 1, ProgramAccessDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProgramAccessDecl_SymbolicVariable(), this.getNamedVariableAccess(), null, "symbolicVariable", null, 0, 1, ProgramAccessDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProgramAccessDecl_TypeName(), this.getNonGenericType(), null, "typeName", null, 0, 1, ProgramAccessDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProgramAccessDecl_Direction(), ecorePackage.getEString(), "direction", null, 0, 1, ProgramAccessDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(configurationDeclarationEClass, ConfigurationDeclaration.class, "ConfigurationDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getConfigurationDeclaration_GlobalVarDeclarations(), this.getGlobalVarDeclarations(), null, "globalVarDeclarations", null, 0, 1, ConfigurationDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getConfigurationDeclaration_Resdecl(), this.getResourceDeclaration(), null, "resdecl", null, 0, 1, ConfigurationDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(resourceDeclarationEClass, ResourceDeclaration.class, "ResourceDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getResourceDeclaration_Resname(), ecorePackage.getEString(), "resname", null, 0, 1, ResourceDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getResourceDeclaration_GlobalVarDeclarations(), this.getGlobalVarDeclarations(), null, "globalVarDeclarations", null, 0, 1, ResourceDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getResourceDeclaration_Singleresource(), this.getSingleResourceDeclaration(), null, "singleresource", null, 0, 1, ResourceDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(singleResourceDeclarationEClass, SingleResourceDeclaration.class, "SingleResourceDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSingleResourceDeclaration_TaskConf(), this.getTaskConfiguration(), null, "taskConf", null, 0, 1, SingleResourceDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSingleResourceDeclaration_ProgramConf(), this.getProgramConfiguration(), null, "programConf", null, 0, -1, SingleResourceDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(programConfigurationEClass, ProgramConfiguration.class, "ProgramConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getProgramConfiguration_Name(), ecorePackage.getEString(), "name", null, 0, 1, ProgramConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProgramConfiguration_Task(), this.getTaskConfiguration(), null, "task", null, 0, 1, ProgramConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProgramConfiguration_Prog(), this.getProgramDeclaration(), null, "prog", null, 0, 1, ProgramConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProgramConfiguration_ProgConf(), this.getProgConfElements(), null, "progConf", null, 0, 1, ProgramConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(progConfElementsEClass, ProgConfElements.class, "ProgConfElements", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getProgConfElements_Progconf(), this.getProgConfElement(), null, "progconf", null, 0, -1, ProgConfElements.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(progConfElementEClass, ProgConfElement.class, "ProgConfElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(fbTaskEClass, FBTask.class, "FBTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getFBTask_Fbname(), this.getFunctionBlockDeclaration(), null, "fbname", null, 0, 1, FBTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFBTask_Task(), this.getTaskConfiguration(), null, "task", null, 0, 1, FBTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(progCNXNEClass, ProgCNXN.class, "ProgCNXN", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getProgCNXN_Variablename(), ecorePackage.getEString(), "variablename", null, 0, 1, ProgCNXN.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProgCNXN_Progd(), ecorePackage.getEObject(), null, "progd", null, 0, 1, ProgCNXN.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(dataSinkEClass, DataSink.class, "DataSink", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getDataSink_Globvar(), this.getGlobalVar(), null, "globvar", null, 0, 1, DataSink.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDataSink_Dirvar(), this.getDirectVariable(), null, "dirvar", null, 0, 1, DataSink.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(progDataSourceEClass, ProgDataSource.class, "ProgDataSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(globalVarDeclarationsEClass, GlobalVarDeclarations.class, "GlobalVarDeclarations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getGlobalVarDeclarations_GlobalVarDecl(), this.getGlobalVarDecl(), null, "globalVarDecl", null, 0, -1, GlobalVarDeclarations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(globalVarDeclEClass, GlobalVarDecl.class, "GlobalVarDecl", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getGlobalVarDecl_Spec(), this.getGlobalVarSpec(), null, "spec", null, 0, 1, GlobalVarDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGlobalVarDecl_SpecInit(), this.getSpecInit(), null, "specInit", null, 0, 1, GlobalVarDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(globalVarSpecEClass, GlobalVarSpec.class, "GlobalVarSpec", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getGlobalVarSpec_Variable(), this.getGlobalVar(), null, "variable", null, 0, 1, GlobalVarSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGlobalVarSpec_Location(), this.getLocation(), null, "location", null, 0, 1, GlobalVarSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(globalVarListEClass, GlobalVarList.class, "GlobalVarList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getGlobalVarList_Variables(), this.getGlobalVar(), null, "variables", null, 0, -1, GlobalVarList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(globalVarEClass, GlobalVar.class, "GlobalVar", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getGlobalVar_Name(), ecorePackage.getEString(), "name", null, 0, 1, GlobalVar.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(taskConfigurationEClass, TaskConfiguration.class, "TaskConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTaskConfiguration_Name(), ecorePackage.getEString(), "name", null, 0, 1, TaskConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTaskConfiguration_TaskInit(), this.getTaskInitialization(), null, "taskInit", null, 0, 1, TaskConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(taskInitializationEClass, TaskInitialization.class, "TaskInitialization", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTaskInitialization_Single(), this.getDataSource(), null, "single", null, 0, 1, TaskInitialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTaskInitialization_Interval(), this.getDataSource(), null, "interval", null, 0, 1, TaskInitialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTaskInitialization_Prior(), ecorePackage.getEInt(), "prior", null, 0, 1, TaskInitialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(dataSourceEClass, DataSource.class, "DataSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(constantEClass, Constant.class, "Constant", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(numericLiteralEClass, NumericLiteral.class, "NumericLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getNumericLiteral_Value(), ecorePackage.getEString(), "value", null, 0, 1, NumericLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(integerLiteralEClass, IntegerLiteral.class, "IntegerLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIntegerLiteral_Type(), this.getIntegerType(), null, "type", null, 0, 1, IntegerLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(realLiteralEClass, RealLiteral.class, "RealLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRealLiteral_Type(), this.getRealType(), null, "type", null, 0, 1, RealLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(characterStringEClass, CharacterString.class, "CharacterString", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getCharacterString_Value(), ecorePackage.getEString(), "value", null, 0, 1, CharacterString.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(bitStringEClass, BitString.class, "BitString", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getBitString_Value(), ecorePackage.getEString(), "value", null, 0, 1, BitString.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(booleanEClass, com.bichler.iec.iec.Boolean.class, "Boolean", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getBoolean_BoolInt(), ecorePackage.getEString(), "boolInt", null, 0, 1, com.bichler.iec.iec.Boolean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBoolean_True(), ecorePackage.getEBoolean(), "true", null, 0, 1, com.bichler.iec.iec.Boolean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(timeLiteralEClass, TimeLiteral.class, "TimeLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(durationLiteralEClass, DurationLiteral.class, "DurationLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDurationLiteral_Duration(), ecorePackage.getEString(), "duration", null, 0, 1, DurationLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(timeOfDayLiteralEClass, TimeOfDayLiteral.class, "TimeOfDayLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTimeOfDayLiteral_Hour(), ecorePackage.getEString(), "hour", null, 0, 1, TimeOfDayLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTimeOfDayLiteral_Minute(), ecorePackage.getEString(), "minute", null, 0, 1, TimeOfDayLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTimeOfDayLiteral_Second(), ecorePackage.getEString(), "second", null, 0, 1, TimeOfDayLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(dateLiteralEClass, DateLiteral.class, "DateLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDateLiteral_Year(), ecorePackage.getEString(), "year", null, 0, 1, DateLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDateLiteral_Month(), ecorePackage.getEString(), "month", null, 0, 1, DateLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDateLiteral_Day(), ecorePackage.getEString(), "day", null, 0, 1, DateLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(dateAndTimeLiteralEClass, DateAndTimeLiteral.class, "DateAndTimeLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDateAndTimeLiteral_Year(), ecorePackage.getEString(), "year", null, 0, 1, DateAndTimeLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDateAndTimeLiteral_Month(), ecorePackage.getEString(), "month", null, 0, 1, DateAndTimeLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDateAndTimeLiteral_Day(), ecorePackage.getEString(), "day", null, 0, 1, DateAndTimeLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDateAndTimeLiteral_Hour(), ecorePackage.getEString(), "hour", null, 0, 1, DateAndTimeLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDateAndTimeLiteral_Minute(), ecorePackage.getEString(), "minute", null, 0, 1, DateAndTimeLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDateAndTimeLiteral_Second(), ecorePackage.getEString(), "second", null, 0, 1, DateAndTimeLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(binaryExpressionEClass, BinaryExpression.class, "BinaryExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getBinaryExpression_Left(), this.getExpression(), null, "left", null, 0, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBinaryExpression_Operator(), ecorePackage.getEString(), "operator", null, 0, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getBinaryExpression_Right(), this.getExpression(), null, "right", null, 0, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(unaryExpressionEClass, UnaryExpression.class, "UnaryExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getUnaryExpression_Operator(), ecorePackage.getEString(), "operator", null, 0, 1, UnaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource(eNS_URI);
  }

} //IecPackageImpl
