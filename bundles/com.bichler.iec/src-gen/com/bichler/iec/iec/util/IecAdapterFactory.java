/**
 */
package com.bichler.iec.iec.util;

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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.bichler.iec.iec.IecPackage
 * @generated
 */
public class IecAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static IecPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IecAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = IecPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IecSwitch<Adapter> modelSwitch =
    new IecSwitch<Adapter>()
    {
      @Override
      public Adapter caseModel(Model object)
      {
        return createModelAdapter();
      }
      @Override
      public Adapter caseModelElement(ModelElement object)
      {
        return createModelElementAdapter();
      }
      @Override
      public Adapter caseLibraryElement(LibraryElement object)
      {
        return createLibraryElementAdapter();
      }
      @Override
      public Adapter caseLibraryElementDeclaration(LibraryElementDeclaration object)
      {
        return createLibraryElementDeclarationAdapter();
      }
      @Override
      public Adapter caseDataType(DataType object)
      {
        return createDataTypeAdapter();
      }
      @Override
      public Adapter caseNonGenericType(NonGenericType object)
      {
        return createNonGenericTypeAdapter();
      }
      @Override
      public Adapter caseElementaryType(ElementaryType object)
      {
        return createElementaryTypeAdapter();
      }
      @Override
      public Adapter caseStringType(StringType object)
      {
        return createStringTypeAdapter();
      }
      @Override
      public Adapter caseNumericType(NumericType object)
      {
        return createNumericTypeAdapter();
      }
      @Override
      public Adapter caseIntegerType(IntegerType object)
      {
        return createIntegerTypeAdapter();
      }
      @Override
      public Adapter caseSignedIntegerType(SignedIntegerType object)
      {
        return createSignedIntegerTypeAdapter();
      }
      @Override
      public Adapter casePlainIntegerType(PlainIntegerType object)
      {
        return createPlainIntegerTypeAdapter();
      }
      @Override
      public Adapter caseUnsignedIntegerType(UnsignedIntegerType object)
      {
        return createUnsignedIntegerTypeAdapter();
      }
      @Override
      public Adapter caseRealType(RealType object)
      {
        return createRealTypeAdapter();
      }
      @Override
      public Adapter caseDateType(DateType object)
      {
        return createDateTypeAdapter();
      }
      @Override
      public Adapter caseBitStringType(BitStringType object)
      {
        return createBitStringTypeAdapter();
      }
      @Override
      public Adapter caseGenericType(GenericType object)
      {
        return createGenericTypeAdapter();
      }
      @Override
      public Adapter caseDerivedType(DerivedType object)
      {
        return createDerivedTypeAdapter();
      }
      @Override
      public Adapter caseDataTypeDeclaration(DataTypeDeclaration object)
      {
        return createDataTypeDeclarationAdapter();
      }
      @Override
      public Adapter caseTypeDeclaration(TypeDeclaration object)
      {
        return createTypeDeclarationAdapter();
      }
      @Override
      public Adapter caseSimpleTypeDeclaration(SimpleTypeDeclaration object)
      {
        return createSimpleTypeDeclarationAdapter();
      }
      @Override
      public Adapter caseSpecInit(SpecInit object)
      {
        return createSpecInitAdapter();
      }
      @Override
      public Adapter caseRangeDeclaration(RangeDeclaration object)
      {
        return createRangeDeclarationAdapter();
      }
      @Override
      public Adapter caseEnumDeclaration(EnumDeclaration object)
      {
        return createEnumDeclarationAdapter();
      }
      @Override
      public Adapter caseArrayDeclaration(ArrayDeclaration object)
      {
        return createArrayDeclarationAdapter();
      }
      @Override
      public Adapter caseEnumeration(Enumeration object)
      {
        return createEnumerationAdapter();
      }
      @Override
      public Adapter caseEnumeratedValue(EnumeratedValue object)
      {
        return createEnumeratedValueAdapter();
      }
      @Override
      public Adapter caseArrayInitialization(ArrayInitialization object)
      {
        return createArrayInitializationAdapter();
      }
      @Override
      public Adapter caseArrayInitialElements(ArrayInitialElements object)
      {
        return createArrayInitialElementsAdapter();
      }
      @Override
      public Adapter caseInitialElement(InitialElement object)
      {
        return createInitialElementAdapter();
      }
      @Override
      public Adapter caseStructureTypeDeclaration(StructureTypeDeclaration object)
      {
        return createStructureTypeDeclarationAdapter();
      }
      @Override
      public Adapter caseStructureDeclaration(StructureDeclaration object)
      {
        return createStructureDeclarationAdapter();
      }
      @Override
      public Adapter caseStructureElementDeclaration(StructureElementDeclaration object)
      {
        return createStructureElementDeclarationAdapter();
      }
      @Override
      public Adapter caseInitializedStructure(InitializedStructure object)
      {
        return createInitializedStructureAdapter();
      }
      @Override
      public Adapter caseStructureInitialization(StructureInitialization object)
      {
        return createStructureInitializationAdapter();
      }
      @Override
      public Adapter caseStructureElementInitialization(StructureElementInitialization object)
      {
        return createStructureElementInitializationAdapter();
      }
      @Override
      public Adapter caseStringDeclaration(StringDeclaration object)
      {
        return createStringDeclarationAdapter();
      }
      @Override
      public Adapter caseVariable(Variable object)
      {
        return createVariableAdapter();
      }
      @Override
      public Adapter caseVariableAccess(VariableAccess object)
      {
        return createVariableAccessAdapter();
      }
      @Override
      public Adapter caseDirectVariable(DirectVariable object)
      {
        return createDirectVariableAdapter();
      }
      @Override
      public Adapter caseSymbolicVariableAccess(SymbolicVariableAccess object)
      {
        return createSymbolicVariableAccessAdapter();
      }
      @Override
      public Adapter caseNamedVariableAccess(NamedVariableAccess object)
      {
        return createNamedVariableAccessAdapter();
      }
      @Override
      public Adapter caseMultiElementVariable(MultiElementVariable object)
      {
        return createMultiElementVariableAdapter();
      }
      @Override
      public Adapter caseArrayVariable(ArrayVariable object)
      {
        return createArrayVariableAdapter();
      }
      @Override
      public Adapter caseStructuredVariable(StructuredVariable object)
      {
        return createStructuredVariableAdapter();
      }
      @Override
      public Adapter caseExpression(Expression object)
      {
        return createExpressionAdapter();
      }
      @Override
      public Adapter caseParamAssignment(ParamAssignment object)
      {
        return createParamAssignmentAdapter();
      }
      @Override
      public Adapter caseFunctionDeclaration(FunctionDeclaration object)
      {
        return createFunctionDeclarationAdapter();
      }
      @Override
      public Adapter caseIoVarDeclarations(IoVarDeclarations object)
      {
        return createIoVarDeclarationsAdapter();
      }
      @Override
      public Adapter caseInputDeclarations(InputDeclarations object)
      {
        return createInputDeclarationsAdapter();
      }
      @Override
      public Adapter caseInputDeclaration(InputDeclaration object)
      {
        return createInputDeclarationAdapter();
      }
      @Override
      public Adapter caseEdgeDeclaration(EdgeDeclaration object)
      {
        return createEdgeDeclarationAdapter();
      }
      @Override
      public Adapter caseDeclSpecification(DeclSpecification object)
      {
        return createDeclSpecificationAdapter();
      }
      @Override
      public Adapter caseVarDeclSpecification(VarDeclSpecification object)
      {
        return createVarDeclSpecificationAdapter();
      }
      @Override
      public Adapter caseEdgeDeclSpecification(EdgeDeclSpecification object)
      {
        return createEdgeDeclSpecificationAdapter();
      }
      @Override
      public Adapter caseVar1List(Var1List object)
      {
        return createVar1ListAdapter();
      }
      @Override
      public Adapter caseOutputDeclarations(OutputDeclarations object)
      {
        return createOutputDeclarationsAdapter();
      }
      @Override
      public Adapter caseVarInitDecl(VarInitDecl object)
      {
        return createVarInitDeclAdapter();
      }
      @Override
      public Adapter caseInputOutputDeclarations(InputOutputDeclarations object)
      {
        return createInputOutputDeclarationsAdapter();
      }
      @Override
      public Adapter caseFunctionBody(FunctionBody object)
      {
        return createFunctionBodyAdapter();
      }
      @Override
      public Adapter caseInstructionList(InstructionList object)
      {
        return createInstructionListAdapter();
      }
      @Override
      public Adapter caseInstruction(Instruction object)
      {
        return createInstructionAdapter();
      }
      @Override
      public Adapter caseLabel(Label object)
      {
        return createLabelAdapter();
      }
      @Override
      public Adapter caseOperation(Operation object)
      {
        return createOperationAdapter();
      }
      @Override
      public Adapter caseSimpleOperation(SimpleOperation object)
      {
        return createSimpleOperationAdapter();
      }
      @Override
      public Adapter caseExpressionOperation(ExpressionOperation object)
      {
        return createExpressionOperationAdapter();
      }
      @Override
      public Adapter caseJmpOperation(JmpOperation object)
      {
        return createJmpOperationAdapter();
      }
      @Override
      public Adapter caseOperand(Operand object)
      {
        return createOperandAdapter();
      }
      @Override
      public Adapter caseReferencedOperand(ReferencedOperand object)
      {
        return createReferencedOperandAdapter();
      }
      @Override
      public Adapter caseSimpleInstructionList(SimpleInstructionList object)
      {
        return createSimpleInstructionListAdapter();
      }
      @Override
      public Adapter caseSimpleInstruction(SimpleInstruction object)
      {
        return createSimpleInstructionAdapter();
      }
      @Override
      public Adapter caseStatementList(StatementList object)
      {
        return createStatementListAdapter();
      }
      @Override
      public Adapter caseStatement(Statement object)
      {
        return createStatementAdapter();
      }
      @Override
      public Adapter caseAssignStatement(AssignStatement object)
      {
        return createAssignStatementAdapter();
      }
      @Override
      public Adapter caseSelectionStatement(SelectionStatement object)
      {
        return createSelectionStatementAdapter();
      }
      @Override
      public Adapter caseIfStatement(IfStatement object)
      {
        return createIfStatementAdapter();
      }
      @Override
      public Adapter caseElseIf(ElseIf object)
      {
        return createElseIfAdapter();
      }
      @Override
      public Adapter caseCaseStatement(CaseStatement object)
      {
        return createCaseStatementAdapter();
      }
      @Override
      public Adapter caseCaseElement(CaseElement object)
      {
        return createCaseElementAdapter();
      }
      @Override
      public Adapter caseCaseList(CaseList object)
      {
        return createCaseListAdapter();
      }
      @Override
      public Adapter caseCaseListElement(CaseListElement object)
      {
        return createCaseListElementAdapter();
      }
      @Override
      public Adapter caseFunctionBlockDeclaration(FunctionBlockDeclaration object)
      {
        return createFunctionBlockDeclarationAdapter();
      }
      @Override
      public Adapter caseFunctionBlockVarDeclarations(FunctionBlockVarDeclarations object)
      {
        return createFunctionBlockVarDeclarationsAdapter();
      }
      @Override
      public Adapter caseOtherVarDeclarations(OtherVarDeclarations object)
      {
        return createOtherVarDeclarationsAdapter();
      }
      @Override
      public Adapter caseVarDeclarations(VarDeclarations object)
      {
        return createVarDeclarationsAdapter();
      }
      @Override
      public Adapter caseFunctionBlockBody(FunctionBlockBody object)
      {
        return createFunctionBlockBodyAdapter();
      }
      @Override
      public Adapter caseProgramDeclaration(ProgramDeclaration object)
      {
        return createProgramDeclarationAdapter();
      }
      @Override
      public Adapter caseProgramVarDeclarations(ProgramVarDeclarations object)
      {
        return createProgramVarDeclarationsAdapter();
      }
      @Override
      public Adapter caseLocatedVarDeclarations(LocatedVarDeclarations object)
      {
        return createLocatedVarDeclarationsAdapter();
      }
      @Override
      public Adapter caseLocatedVarDeclaration(LocatedVarDeclaration object)
      {
        return createLocatedVarDeclarationAdapter();
      }
      @Override
      public Adapter caseLocation(Location object)
      {
        return createLocationAdapter();
      }
      @Override
      public Adapter caseProgramAccessDecls(ProgramAccessDecls object)
      {
        return createProgramAccessDeclsAdapter();
      }
      @Override
      public Adapter caseProgramAccessDecl(ProgramAccessDecl object)
      {
        return createProgramAccessDeclAdapter();
      }
      @Override
      public Adapter caseConfigurationDeclaration(ConfigurationDeclaration object)
      {
        return createConfigurationDeclarationAdapter();
      }
      @Override
      public Adapter caseResourceDeclaration(ResourceDeclaration object)
      {
        return createResourceDeclarationAdapter();
      }
      @Override
      public Adapter caseSingleResourceDeclaration(SingleResourceDeclaration object)
      {
        return createSingleResourceDeclarationAdapter();
      }
      @Override
      public Adapter caseProgramConfiguration(ProgramConfiguration object)
      {
        return createProgramConfigurationAdapter();
      }
      @Override
      public Adapter caseProgConfElements(ProgConfElements object)
      {
        return createProgConfElementsAdapter();
      }
      @Override
      public Adapter caseProgConfElement(ProgConfElement object)
      {
        return createProgConfElementAdapter();
      }
      @Override
      public Adapter caseFBTask(FBTask object)
      {
        return createFBTaskAdapter();
      }
      @Override
      public Adapter caseProgCNXN(ProgCNXN object)
      {
        return createProgCNXNAdapter();
      }
      @Override
      public Adapter caseDataSink(DataSink object)
      {
        return createDataSinkAdapter();
      }
      @Override
      public Adapter caseProgDataSource(ProgDataSource object)
      {
        return createProgDataSourceAdapter();
      }
      @Override
      public Adapter caseGlobalVarDeclarations(GlobalVarDeclarations object)
      {
        return createGlobalVarDeclarationsAdapter();
      }
      @Override
      public Adapter caseGlobalVarDecl(GlobalVarDecl object)
      {
        return createGlobalVarDeclAdapter();
      }
      @Override
      public Adapter caseGlobalVarSpec(GlobalVarSpec object)
      {
        return createGlobalVarSpecAdapter();
      }
      @Override
      public Adapter caseGlobalVarList(GlobalVarList object)
      {
        return createGlobalVarListAdapter();
      }
      @Override
      public Adapter caseGlobalVar(GlobalVar object)
      {
        return createGlobalVarAdapter();
      }
      @Override
      public Adapter caseTaskConfiguration(TaskConfiguration object)
      {
        return createTaskConfigurationAdapter();
      }
      @Override
      public Adapter caseTaskInitialization(TaskInitialization object)
      {
        return createTaskInitializationAdapter();
      }
      @Override
      public Adapter caseDataSource(DataSource object)
      {
        return createDataSourceAdapter();
      }
      @Override
      public Adapter caseConstant(Constant object)
      {
        return createConstantAdapter();
      }
      @Override
      public Adapter caseNumericLiteral(NumericLiteral object)
      {
        return createNumericLiteralAdapter();
      }
      @Override
      public Adapter caseIntegerLiteral(IntegerLiteral object)
      {
        return createIntegerLiteralAdapter();
      }
      @Override
      public Adapter caseRealLiteral(RealLiteral object)
      {
        return createRealLiteralAdapter();
      }
      @Override
      public Adapter caseCharacterString(CharacterString object)
      {
        return createCharacterStringAdapter();
      }
      @Override
      public Adapter caseBitString(BitString object)
      {
        return createBitStringAdapter();
      }
      @Override
      public Adapter caseBoolean(com.bichler.iec.iec.Boolean object)
      {
        return createBooleanAdapter();
      }
      @Override
      public Adapter caseTimeLiteral(TimeLiteral object)
      {
        return createTimeLiteralAdapter();
      }
      @Override
      public Adapter caseDurationLiteral(DurationLiteral object)
      {
        return createDurationLiteralAdapter();
      }
      @Override
      public Adapter caseTimeOfDayLiteral(TimeOfDayLiteral object)
      {
        return createTimeOfDayLiteralAdapter();
      }
      @Override
      public Adapter caseDateLiteral(DateLiteral object)
      {
        return createDateLiteralAdapter();
      }
      @Override
      public Adapter caseDateAndTimeLiteral(DateAndTimeLiteral object)
      {
        return createDateAndTimeLiteralAdapter();
      }
      @Override
      public Adapter caseBinaryExpression(BinaryExpression object)
      {
        return createBinaryExpressionAdapter();
      }
      @Override
      public Adapter caseUnaryExpression(UnaryExpression object)
      {
        return createUnaryExpressionAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Model <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Model
   * @generated
   */
  public Adapter createModelAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ModelElement <em>Model Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ModelElement
   * @generated
   */
  public Adapter createModelElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.LibraryElement <em>Library Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.LibraryElement
   * @generated
   */
  public Adapter createLibraryElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.LibraryElementDeclaration <em>Library Element Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.LibraryElementDeclaration
   * @generated
   */
  public Adapter createLibraryElementDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.DataType <em>Data Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.DataType
   * @generated
   */
  public Adapter createDataTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.NonGenericType <em>Non Generic Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.NonGenericType
   * @generated
   */
  public Adapter createNonGenericTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ElementaryType <em>Elementary Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ElementaryType
   * @generated
   */
  public Adapter createElementaryTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.StringType <em>String Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.StringType
   * @generated
   */
  public Adapter createStringTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.NumericType <em>Numeric Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.NumericType
   * @generated
   */
  public Adapter createNumericTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.IntegerType <em>Integer Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.IntegerType
   * @generated
   */
  public Adapter createIntegerTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.SignedIntegerType <em>Signed Integer Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.SignedIntegerType
   * @generated
   */
  public Adapter createSignedIntegerTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.PlainIntegerType <em>Plain Integer Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.PlainIntegerType
   * @generated
   */
  public Adapter createPlainIntegerTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.UnsignedIntegerType <em>Unsigned Integer Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.UnsignedIntegerType
   * @generated
   */
  public Adapter createUnsignedIntegerTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.RealType <em>Real Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.RealType
   * @generated
   */
  public Adapter createRealTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.DateType <em>Date Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.DateType
   * @generated
   */
  public Adapter createDateTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.BitStringType <em>Bit String Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.BitStringType
   * @generated
   */
  public Adapter createBitStringTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.GenericType <em>Generic Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.GenericType
   * @generated
   */
  public Adapter createGenericTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.DerivedType <em>Derived Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.DerivedType
   * @generated
   */
  public Adapter createDerivedTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.DataTypeDeclaration <em>Data Type Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.DataTypeDeclaration
   * @generated
   */
  public Adapter createDataTypeDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.TypeDeclaration <em>Type Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.TypeDeclaration
   * @generated
   */
  public Adapter createTypeDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.SimpleTypeDeclaration <em>Simple Type Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.SimpleTypeDeclaration
   * @generated
   */
  public Adapter createSimpleTypeDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.SpecInit <em>Spec Init</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.SpecInit
   * @generated
   */
  public Adapter createSpecInitAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.RangeDeclaration <em>Range Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.RangeDeclaration
   * @generated
   */
  public Adapter createRangeDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.EnumDeclaration <em>Enum Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.EnumDeclaration
   * @generated
   */
  public Adapter createEnumDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ArrayDeclaration <em>Array Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ArrayDeclaration
   * @generated
   */
  public Adapter createArrayDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Enumeration <em>Enumeration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Enumeration
   * @generated
   */
  public Adapter createEnumerationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.EnumeratedValue <em>Enumerated Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.EnumeratedValue
   * @generated
   */
  public Adapter createEnumeratedValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ArrayInitialization <em>Array Initialization</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ArrayInitialization
   * @generated
   */
  public Adapter createArrayInitializationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ArrayInitialElements <em>Array Initial Elements</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ArrayInitialElements
   * @generated
   */
  public Adapter createArrayInitialElementsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.InitialElement <em>Initial Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.InitialElement
   * @generated
   */
  public Adapter createInitialElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.StructureTypeDeclaration <em>Structure Type Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.StructureTypeDeclaration
   * @generated
   */
  public Adapter createStructureTypeDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.StructureDeclaration <em>Structure Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.StructureDeclaration
   * @generated
   */
  public Adapter createStructureDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.StructureElementDeclaration <em>Structure Element Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.StructureElementDeclaration
   * @generated
   */
  public Adapter createStructureElementDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.InitializedStructure <em>Initialized Structure</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.InitializedStructure
   * @generated
   */
  public Adapter createInitializedStructureAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.StructureInitialization <em>Structure Initialization</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.StructureInitialization
   * @generated
   */
  public Adapter createStructureInitializationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.StructureElementInitialization <em>Structure Element Initialization</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.StructureElementInitialization
   * @generated
   */
  public Adapter createStructureElementInitializationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.StringDeclaration <em>String Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.StringDeclaration
   * @generated
   */
  public Adapter createStringDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Variable <em>Variable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Variable
   * @generated
   */
  public Adapter createVariableAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.VariableAccess <em>Variable Access</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.VariableAccess
   * @generated
   */
  public Adapter createVariableAccessAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.DirectVariable <em>Direct Variable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.DirectVariable
   * @generated
   */
  public Adapter createDirectVariableAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.SymbolicVariableAccess <em>Symbolic Variable Access</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.SymbolicVariableAccess
   * @generated
   */
  public Adapter createSymbolicVariableAccessAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.NamedVariableAccess <em>Named Variable Access</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.NamedVariableAccess
   * @generated
   */
  public Adapter createNamedVariableAccessAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.MultiElementVariable <em>Multi Element Variable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.MultiElementVariable
   * @generated
   */
  public Adapter createMultiElementVariableAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ArrayVariable <em>Array Variable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ArrayVariable
   * @generated
   */
  public Adapter createArrayVariableAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.StructuredVariable <em>Structured Variable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.StructuredVariable
   * @generated
   */
  public Adapter createStructuredVariableAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Expression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Expression
   * @generated
   */
  public Adapter createExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ParamAssignment <em>Param Assignment</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ParamAssignment
   * @generated
   */
  public Adapter createParamAssignmentAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.FunctionDeclaration <em>Function Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.FunctionDeclaration
   * @generated
   */
  public Adapter createFunctionDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.IoVarDeclarations <em>Io Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.IoVarDeclarations
   * @generated
   */
  public Adapter createIoVarDeclarationsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.InputDeclarations <em>Input Declarations</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.InputDeclarations
   * @generated
   */
  public Adapter createInputDeclarationsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.InputDeclaration <em>Input Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.InputDeclaration
   * @generated
   */
  public Adapter createInputDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.EdgeDeclaration <em>Edge Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.EdgeDeclaration
   * @generated
   */
  public Adapter createEdgeDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.DeclSpecification <em>Decl Specification</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.DeclSpecification
   * @generated
   */
  public Adapter createDeclSpecificationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.VarDeclSpecification <em>Var Decl Specification</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.VarDeclSpecification
   * @generated
   */
  public Adapter createVarDeclSpecificationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.EdgeDeclSpecification <em>Edge Decl Specification</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.EdgeDeclSpecification
   * @generated
   */
  public Adapter createEdgeDeclSpecificationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Var1List <em>Var1 List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Var1List
   * @generated
   */
  public Adapter createVar1ListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.OutputDeclarations <em>Output Declarations</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.OutputDeclarations
   * @generated
   */
  public Adapter createOutputDeclarationsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.VarInitDecl <em>Var Init Decl</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.VarInitDecl
   * @generated
   */
  public Adapter createVarInitDeclAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.InputOutputDeclarations <em>Input Output Declarations</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.InputOutputDeclarations
   * @generated
   */
  public Adapter createInputOutputDeclarationsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.FunctionBody <em>Function Body</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.FunctionBody
   * @generated
   */
  public Adapter createFunctionBodyAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.InstructionList <em>Instruction List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.InstructionList
   * @generated
   */
  public Adapter createInstructionListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Instruction <em>Instruction</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Instruction
   * @generated
   */
  public Adapter createInstructionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Label <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Label
   * @generated
   */
  public Adapter createLabelAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Operation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Operation
   * @generated
   */
  public Adapter createOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.SimpleOperation <em>Simple Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.SimpleOperation
   * @generated
   */
  public Adapter createSimpleOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ExpressionOperation <em>Expression Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ExpressionOperation
   * @generated
   */
  public Adapter createExpressionOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.JmpOperation <em>Jmp Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.JmpOperation
   * @generated
   */
  public Adapter createJmpOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Operand <em>Operand</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Operand
   * @generated
   */
  public Adapter createOperandAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ReferencedOperand <em>Referenced Operand</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ReferencedOperand
   * @generated
   */
  public Adapter createReferencedOperandAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.SimpleInstructionList <em>Simple Instruction List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.SimpleInstructionList
   * @generated
   */
  public Adapter createSimpleInstructionListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.SimpleInstruction <em>Simple Instruction</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.SimpleInstruction
   * @generated
   */
  public Adapter createSimpleInstructionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.StatementList <em>Statement List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.StatementList
   * @generated
   */
  public Adapter createStatementListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Statement <em>Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Statement
   * @generated
   */
  public Adapter createStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.AssignStatement <em>Assign Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.AssignStatement
   * @generated
   */
  public Adapter createAssignStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.SelectionStatement <em>Selection Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.SelectionStatement
   * @generated
   */
  public Adapter createSelectionStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.IfStatement <em>If Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.IfStatement
   * @generated
   */
  public Adapter createIfStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ElseIf <em>Else If</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ElseIf
   * @generated
   */
  public Adapter createElseIfAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.CaseStatement <em>Case Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.CaseStatement
   * @generated
   */
  public Adapter createCaseStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.CaseElement <em>Case Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.CaseElement
   * @generated
   */
  public Adapter createCaseElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.CaseList <em>Case List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.CaseList
   * @generated
   */
  public Adapter createCaseListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.CaseListElement <em>Case List Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.CaseListElement
   * @generated
   */
  public Adapter createCaseListElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.FunctionBlockDeclaration <em>Function Block Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.FunctionBlockDeclaration
   * @generated
   */
  public Adapter createFunctionBlockDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.FunctionBlockVarDeclarations <em>Function Block Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.FunctionBlockVarDeclarations
   * @generated
   */
  public Adapter createFunctionBlockVarDeclarationsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.OtherVarDeclarations <em>Other Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.OtherVarDeclarations
   * @generated
   */
  public Adapter createOtherVarDeclarationsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.VarDeclarations <em>Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.VarDeclarations
   * @generated
   */
  public Adapter createVarDeclarationsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.FunctionBlockBody <em>Function Block Body</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.FunctionBlockBody
   * @generated
   */
  public Adapter createFunctionBlockBodyAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ProgramDeclaration <em>Program Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ProgramDeclaration
   * @generated
   */
  public Adapter createProgramDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ProgramVarDeclarations <em>Program Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ProgramVarDeclarations
   * @generated
   */
  public Adapter createProgramVarDeclarationsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.LocatedVarDeclarations <em>Located Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.LocatedVarDeclarations
   * @generated
   */
  public Adapter createLocatedVarDeclarationsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.LocatedVarDeclaration <em>Located Var Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.LocatedVarDeclaration
   * @generated
   */
  public Adapter createLocatedVarDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Location <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Location
   * @generated
   */
  public Adapter createLocationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ProgramAccessDecls <em>Program Access Decls</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ProgramAccessDecls
   * @generated
   */
  public Adapter createProgramAccessDeclsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ProgramAccessDecl <em>Program Access Decl</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ProgramAccessDecl
   * @generated
   */
  public Adapter createProgramAccessDeclAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ConfigurationDeclaration <em>Configuration Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ConfigurationDeclaration
   * @generated
   */
  public Adapter createConfigurationDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ResourceDeclaration <em>Resource Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ResourceDeclaration
   * @generated
   */
  public Adapter createResourceDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.SingleResourceDeclaration <em>Single Resource Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.SingleResourceDeclaration
   * @generated
   */
  public Adapter createSingleResourceDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ProgramConfiguration <em>Program Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ProgramConfiguration
   * @generated
   */
  public Adapter createProgramConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ProgConfElements <em>Prog Conf Elements</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ProgConfElements
   * @generated
   */
  public Adapter createProgConfElementsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ProgConfElement <em>Prog Conf Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ProgConfElement
   * @generated
   */
  public Adapter createProgConfElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.FBTask <em>FB Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.FBTask
   * @generated
   */
  public Adapter createFBTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ProgCNXN <em>Prog CNXN</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ProgCNXN
   * @generated
   */
  public Adapter createProgCNXNAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.DataSink <em>Data Sink</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.DataSink
   * @generated
   */
  public Adapter createDataSinkAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.ProgDataSource <em>Prog Data Source</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.ProgDataSource
   * @generated
   */
  public Adapter createProgDataSourceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.GlobalVarDeclarations <em>Global Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.GlobalVarDeclarations
   * @generated
   */
  public Adapter createGlobalVarDeclarationsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.GlobalVarDecl <em>Global Var Decl</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.GlobalVarDecl
   * @generated
   */
  public Adapter createGlobalVarDeclAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.GlobalVarSpec <em>Global Var Spec</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.GlobalVarSpec
   * @generated
   */
  public Adapter createGlobalVarSpecAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.GlobalVarList <em>Global Var List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.GlobalVarList
   * @generated
   */
  public Adapter createGlobalVarListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.GlobalVar <em>Global Var</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.GlobalVar
   * @generated
   */
  public Adapter createGlobalVarAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.TaskConfiguration <em>Task Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.TaskConfiguration
   * @generated
   */
  public Adapter createTaskConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.TaskInitialization <em>Task Initialization</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.TaskInitialization
   * @generated
   */
  public Adapter createTaskInitializationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.DataSource <em>Data Source</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.DataSource
   * @generated
   */
  public Adapter createDataSourceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Constant <em>Constant</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Constant
   * @generated
   */
  public Adapter createConstantAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.NumericLiteral <em>Numeric Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.NumericLiteral
   * @generated
   */
  public Adapter createNumericLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.IntegerLiteral <em>Integer Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.IntegerLiteral
   * @generated
   */
  public Adapter createIntegerLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.RealLiteral <em>Real Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.RealLiteral
   * @generated
   */
  public Adapter createRealLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.CharacterString <em>Character String</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.CharacterString
   * @generated
   */
  public Adapter createCharacterStringAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.BitString <em>Bit String</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.BitString
   * @generated
   */
  public Adapter createBitStringAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.Boolean <em>Boolean</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.Boolean
   * @generated
   */
  public Adapter createBooleanAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.TimeLiteral <em>Time Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.TimeLiteral
   * @generated
   */
  public Adapter createTimeLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.DurationLiteral <em>Duration Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.DurationLiteral
   * @generated
   */
  public Adapter createDurationLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.TimeOfDayLiteral <em>Time Of Day Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.TimeOfDayLiteral
   * @generated
   */
  public Adapter createTimeOfDayLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.DateLiteral <em>Date Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.DateLiteral
   * @generated
   */
  public Adapter createDateLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.DateAndTimeLiteral <em>Date And Time Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.DateAndTimeLiteral
   * @generated
   */
  public Adapter createDateAndTimeLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.BinaryExpression <em>Binary Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.BinaryExpression
   * @generated
   */
  public Adapter createBinaryExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.bichler.iec.iec.UnaryExpression <em>Unary Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.bichler.iec.iec.UnaryExpression
   * @generated
   */
  public Adapter createUnaryExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //IecAdapterFactory
