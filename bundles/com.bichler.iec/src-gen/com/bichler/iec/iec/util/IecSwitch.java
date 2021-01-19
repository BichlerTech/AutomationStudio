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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.bichler.iec.iec.IecPackage
 * @generated
 */
public class IecSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static IecPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IecSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = IecPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case IecPackage.MODEL:
      {
        Model model = (Model)theEObject;
        T result = caseModel(model);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.MODEL_ELEMENT:
      {
        ModelElement modelElement = (ModelElement)theEObject;
        T result = caseModelElement(modelElement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.LIBRARY_ELEMENT:
      {
        LibraryElement libraryElement = (LibraryElement)theEObject;
        T result = caseLibraryElement(libraryElement);
        if (result == null) result = caseModelElement(libraryElement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.LIBRARY_ELEMENT_DECLARATION:
      {
        LibraryElementDeclaration libraryElementDeclaration = (LibraryElementDeclaration)theEObject;
        T result = caseLibraryElementDeclaration(libraryElementDeclaration);
        if (result == null) result = caseModelElement(libraryElementDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.DATA_TYPE:
      {
        DataType dataType = (DataType)theEObject;
        T result = caseDataType(dataType);
        if (result == null) result = caseLibraryElement(dataType);
        if (result == null) result = caseModelElement(dataType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.NON_GENERIC_TYPE:
      {
        NonGenericType nonGenericType = (NonGenericType)theEObject;
        T result = caseNonGenericType(nonGenericType);
        if (result == null) result = caseDataType(nonGenericType);
        if (result == null) result = caseLibraryElement(nonGenericType);
        if (result == null) result = caseModelElement(nonGenericType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.ELEMENTARY_TYPE:
      {
        ElementaryType elementaryType = (ElementaryType)theEObject;
        T result = caseElementaryType(elementaryType);
        if (result == null) result = caseNonGenericType(elementaryType);
        if (result == null) result = caseDataType(elementaryType);
        if (result == null) result = caseLibraryElement(elementaryType);
        if (result == null) result = caseModelElement(elementaryType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.STRING_TYPE:
      {
        StringType stringType = (StringType)theEObject;
        T result = caseStringType(stringType);
        if (result == null) result = caseElementaryType(stringType);
        if (result == null) result = caseNonGenericType(stringType);
        if (result == null) result = caseDataType(stringType);
        if (result == null) result = caseLibraryElement(stringType);
        if (result == null) result = caseModelElement(stringType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.NUMERIC_TYPE:
      {
        NumericType numericType = (NumericType)theEObject;
        T result = caseNumericType(numericType);
        if (result == null) result = caseElementaryType(numericType);
        if (result == null) result = caseNonGenericType(numericType);
        if (result == null) result = caseDataType(numericType);
        if (result == null) result = caseLibraryElement(numericType);
        if (result == null) result = caseModelElement(numericType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.INTEGER_TYPE:
      {
        IntegerType integerType = (IntegerType)theEObject;
        T result = caseIntegerType(integerType);
        if (result == null) result = caseNumericType(integerType);
        if (result == null) result = caseElementaryType(integerType);
        if (result == null) result = caseNonGenericType(integerType);
        if (result == null) result = caseDataType(integerType);
        if (result == null) result = caseLibraryElement(integerType);
        if (result == null) result = caseModelElement(integerType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.SIGNED_INTEGER_TYPE:
      {
        SignedIntegerType signedIntegerType = (SignedIntegerType)theEObject;
        T result = caseSignedIntegerType(signedIntegerType);
        if (result == null) result = casePlainIntegerType(signedIntegerType);
        if (result == null) result = caseIntegerType(signedIntegerType);
        if (result == null) result = caseNumericType(signedIntegerType);
        if (result == null) result = caseElementaryType(signedIntegerType);
        if (result == null) result = caseNonGenericType(signedIntegerType);
        if (result == null) result = caseDataType(signedIntegerType);
        if (result == null) result = caseLibraryElement(signedIntegerType);
        if (result == null) result = caseModelElement(signedIntegerType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.PLAIN_INTEGER_TYPE:
      {
        PlainIntegerType plainIntegerType = (PlainIntegerType)theEObject;
        T result = casePlainIntegerType(plainIntegerType);
        if (result == null) result = caseIntegerType(plainIntegerType);
        if (result == null) result = caseNumericType(plainIntegerType);
        if (result == null) result = caseElementaryType(plainIntegerType);
        if (result == null) result = caseNonGenericType(plainIntegerType);
        if (result == null) result = caseDataType(plainIntegerType);
        if (result == null) result = caseLibraryElement(plainIntegerType);
        if (result == null) result = caseModelElement(plainIntegerType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.UNSIGNED_INTEGER_TYPE:
      {
        UnsignedIntegerType unsignedIntegerType = (UnsignedIntegerType)theEObject;
        T result = caseUnsignedIntegerType(unsignedIntegerType);
        if (result == null) result = caseIntegerType(unsignedIntegerType);
        if (result == null) result = caseNumericType(unsignedIntegerType);
        if (result == null) result = caseElementaryType(unsignedIntegerType);
        if (result == null) result = caseNonGenericType(unsignedIntegerType);
        if (result == null) result = caseDataType(unsignedIntegerType);
        if (result == null) result = caseLibraryElement(unsignedIntegerType);
        if (result == null) result = caseModelElement(unsignedIntegerType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.REAL_TYPE:
      {
        RealType realType = (RealType)theEObject;
        T result = caseRealType(realType);
        if (result == null) result = caseNumericType(realType);
        if (result == null) result = caseElementaryType(realType);
        if (result == null) result = caseNonGenericType(realType);
        if (result == null) result = caseDataType(realType);
        if (result == null) result = caseLibraryElement(realType);
        if (result == null) result = caseModelElement(realType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.DATE_TYPE:
      {
        DateType dateType = (DateType)theEObject;
        T result = caseDateType(dateType);
        if (result == null) result = caseElementaryType(dateType);
        if (result == null) result = caseNonGenericType(dateType);
        if (result == null) result = caseDataType(dateType);
        if (result == null) result = caseLibraryElement(dateType);
        if (result == null) result = caseModelElement(dateType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.BIT_STRING_TYPE:
      {
        BitStringType bitStringType = (BitStringType)theEObject;
        T result = caseBitStringType(bitStringType);
        if (result == null) result = caseElementaryType(bitStringType);
        if (result == null) result = caseNonGenericType(bitStringType);
        if (result == null) result = caseDataType(bitStringType);
        if (result == null) result = caseLibraryElement(bitStringType);
        if (result == null) result = caseModelElement(bitStringType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.GENERIC_TYPE:
      {
        GenericType genericType = (GenericType)theEObject;
        T result = caseGenericType(genericType);
        if (result == null) result = caseDataType(genericType);
        if (result == null) result = caseLibraryElement(genericType);
        if (result == null) result = caseModelElement(genericType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.DERIVED_TYPE:
      {
        DerivedType derivedType = (DerivedType)theEObject;
        T result = caseDerivedType(derivedType);
        if (result == null) result = caseNonGenericType(derivedType);
        if (result == null) result = caseDataType(derivedType);
        if (result == null) result = caseLibraryElement(derivedType);
        if (result == null) result = caseModelElement(derivedType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.DATA_TYPE_DECLARATION:
      {
        DataTypeDeclaration dataTypeDeclaration = (DataTypeDeclaration)theEObject;
        T result = caseDataTypeDeclaration(dataTypeDeclaration);
        if (result == null) result = caseLibraryElementDeclaration(dataTypeDeclaration);
        if (result == null) result = caseModelElement(dataTypeDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.TYPE_DECLARATION:
      {
        TypeDeclaration typeDeclaration = (TypeDeclaration)theEObject;
        T result = caseTypeDeclaration(typeDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.SIMPLE_TYPE_DECLARATION:
      {
        SimpleTypeDeclaration simpleTypeDeclaration = (SimpleTypeDeclaration)theEObject;
        T result = caseSimpleTypeDeclaration(simpleTypeDeclaration);
        if (result == null) result = caseTypeDeclaration(simpleTypeDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.SPEC_INIT:
      {
        SpecInit specInit = (SpecInit)theEObject;
        T result = caseSpecInit(specInit);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.RANGE_DECLARATION:
      {
        RangeDeclaration rangeDeclaration = (RangeDeclaration)theEObject;
        T result = caseRangeDeclaration(rangeDeclaration);
        if (result == null) result = caseTypeDeclaration(rangeDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.ENUM_DECLARATION:
      {
        EnumDeclaration enumDeclaration = (EnumDeclaration)theEObject;
        T result = caseEnumDeclaration(enumDeclaration);
        if (result == null) result = caseTypeDeclaration(enumDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.ARRAY_DECLARATION:
      {
        ArrayDeclaration arrayDeclaration = (ArrayDeclaration)theEObject;
        T result = caseArrayDeclaration(arrayDeclaration);
        if (result == null) result = caseTypeDeclaration(arrayDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.ENUMERATION:
      {
        Enumeration enumeration = (Enumeration)theEObject;
        T result = caseEnumeration(enumeration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.ENUMERATED_VALUE:
      {
        EnumeratedValue enumeratedValue = (EnumeratedValue)theEObject;
        T result = caseEnumeratedValue(enumeratedValue);
        if (result == null) result = caseInitialElement(enumeratedValue);
        if (result == null) result = caseReferencedOperand(enumeratedValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.ARRAY_INITIALIZATION:
      {
        ArrayInitialization arrayInitialization = (ArrayInitialization)theEObject;
        T result = caseArrayInitialization(arrayInitialization);
        if (result == null) result = caseInitialElement(arrayInitialization);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.ARRAY_INITIAL_ELEMENTS:
      {
        ArrayInitialElements arrayInitialElements = (ArrayInitialElements)theEObject;
        T result = caseArrayInitialElements(arrayInitialElements);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.INITIAL_ELEMENT:
      {
        InitialElement initialElement = (InitialElement)theEObject;
        T result = caseInitialElement(initialElement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.STRUCTURE_TYPE_DECLARATION:
      {
        StructureTypeDeclaration structureTypeDeclaration = (StructureTypeDeclaration)theEObject;
        T result = caseStructureTypeDeclaration(structureTypeDeclaration);
        if (result == null) result = caseTypeDeclaration(structureTypeDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.STRUCTURE_DECLARATION:
      {
        StructureDeclaration structureDeclaration = (StructureDeclaration)theEObject;
        T result = caseStructureDeclaration(structureDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.STRUCTURE_ELEMENT_DECLARATION:
      {
        StructureElementDeclaration structureElementDeclaration = (StructureElementDeclaration)theEObject;
        T result = caseStructureElementDeclaration(structureElementDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.INITIALIZED_STRUCTURE:
      {
        InitializedStructure initializedStructure = (InitializedStructure)theEObject;
        T result = caseInitializedStructure(initializedStructure);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.STRUCTURE_INITIALIZATION:
      {
        StructureInitialization structureInitialization = (StructureInitialization)theEObject;
        T result = caseStructureInitialization(structureInitialization);
        if (result == null) result = caseInitialElement(structureInitialization);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.STRUCTURE_ELEMENT_INITIALIZATION:
      {
        StructureElementInitialization structureElementInitialization = (StructureElementInitialization)theEObject;
        T result = caseStructureElementInitialization(structureElementInitialization);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.STRING_DECLARATION:
      {
        StringDeclaration stringDeclaration = (StringDeclaration)theEObject;
        T result = caseStringDeclaration(stringDeclaration);
        if (result == null) result = caseTypeDeclaration(stringDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.VARIABLE:
      {
        Variable variable = (Variable)theEObject;
        T result = caseVariable(variable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.VARIABLE_ACCESS:
      {
        VariableAccess variableAccess = (VariableAccess)theEObject;
        T result = caseVariableAccess(variableAccess);
        if (result == null) result = caseReferencedOperand(variableAccess);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.DIRECT_VARIABLE:
      {
        DirectVariable directVariable = (DirectVariable)theEObject;
        T result = caseDirectVariable(directVariable);
        if (result == null) result = caseVariableAccess(directVariable);
        if (result == null) result = caseProgDataSource(directVariable);
        if (result == null) result = caseDataSource(directVariable);
        if (result == null) result = caseReferencedOperand(directVariable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.SYMBOLIC_VARIABLE_ACCESS:
      {
        SymbolicVariableAccess symbolicVariableAccess = (SymbolicVariableAccess)theEObject;
        T result = caseSymbolicVariableAccess(symbolicVariableAccess);
        if (result == null) result = caseVariableAccess(symbolicVariableAccess);
        if (result == null) result = caseReferencedOperand(symbolicVariableAccess);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.NAMED_VARIABLE_ACCESS:
      {
        NamedVariableAccess namedVariableAccess = (NamedVariableAccess)theEObject;
        T result = caseNamedVariableAccess(namedVariableAccess);
        if (result == null) result = caseSymbolicVariableAccess(namedVariableAccess);
        if (result == null) result = caseVariableAccess(namedVariableAccess);
        if (result == null) result = caseReferencedOperand(namedVariableAccess);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.MULTI_ELEMENT_VARIABLE:
      {
        MultiElementVariable multiElementVariable = (MultiElementVariable)theEObject;
        T result = caseMultiElementVariable(multiElementVariable);
        if (result == null) result = caseSymbolicVariableAccess(multiElementVariable);
        if (result == null) result = caseVariableAccess(multiElementVariable);
        if (result == null) result = caseReferencedOperand(multiElementVariable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.ARRAY_VARIABLE:
      {
        ArrayVariable arrayVariable = (ArrayVariable)theEObject;
        T result = caseArrayVariable(arrayVariable);
        if (result == null) result = caseMultiElementVariable(arrayVariable);
        if (result == null) result = caseSymbolicVariableAccess(arrayVariable);
        if (result == null) result = caseVariableAccess(arrayVariable);
        if (result == null) result = caseReferencedOperand(arrayVariable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.STRUCTURED_VARIABLE:
      {
        StructuredVariable structuredVariable = (StructuredVariable)theEObject;
        T result = caseStructuredVariable(structuredVariable);
        if (result == null) result = caseMultiElementVariable(structuredVariable);
        if (result == null) result = caseSymbolicVariableAccess(structuredVariable);
        if (result == null) result = caseVariableAccess(structuredVariable);
        if (result == null) result = caseReferencedOperand(structuredVariable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.EXPRESSION:
      {
        Expression expression = (Expression)theEObject;
        T result = caseExpression(expression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.PARAM_ASSIGNMENT:
      {
        ParamAssignment paramAssignment = (ParamAssignment)theEObject;
        T result = caseParamAssignment(paramAssignment);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.FUNCTION_DECLARATION:
      {
        FunctionDeclaration functionDeclaration = (FunctionDeclaration)theEObject;
        T result = caseFunctionDeclaration(functionDeclaration);
        if (result == null) result = caseLibraryElement(functionDeclaration);
        if (result == null) result = caseModelElement(functionDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.IO_VAR_DECLARATIONS:
      {
        IoVarDeclarations ioVarDeclarations = (IoVarDeclarations)theEObject;
        T result = caseIoVarDeclarations(ioVarDeclarations);
        if (result == null) result = caseFunctionBlockVarDeclarations(ioVarDeclarations);
        if (result == null) result = caseProgramVarDeclarations(ioVarDeclarations);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.INPUT_DECLARATIONS:
      {
        InputDeclarations inputDeclarations = (InputDeclarations)theEObject;
        T result = caseInputDeclarations(inputDeclarations);
        if (result == null) result = caseIoVarDeclarations(inputDeclarations);
        if (result == null) result = caseFunctionBlockVarDeclarations(inputDeclarations);
        if (result == null) result = caseProgramVarDeclarations(inputDeclarations);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.INPUT_DECLARATION:
      {
        InputDeclaration inputDeclaration = (InputDeclaration)theEObject;
        T result = caseInputDeclaration(inputDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.EDGE_DECLARATION:
      {
        EdgeDeclaration edgeDeclaration = (EdgeDeclaration)theEObject;
        T result = caseEdgeDeclaration(edgeDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.DECL_SPECIFICATION:
      {
        DeclSpecification declSpecification = (DeclSpecification)theEObject;
        T result = caseDeclSpecification(declSpecification);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.VAR_DECL_SPECIFICATION:
      {
        VarDeclSpecification varDeclSpecification = (VarDeclSpecification)theEObject;
        T result = caseVarDeclSpecification(varDeclSpecification);
        if (result == null) result = caseDeclSpecification(varDeclSpecification);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.EDGE_DECL_SPECIFICATION:
      {
        EdgeDeclSpecification edgeDeclSpecification = (EdgeDeclSpecification)theEObject;
        T result = caseEdgeDeclSpecification(edgeDeclSpecification);
        if (result == null) result = caseDeclSpecification(edgeDeclSpecification);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.VAR1_LIST:
      {
        Var1List var1List = (Var1List)theEObject;
        T result = caseVar1List(var1List);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.OUTPUT_DECLARATIONS:
      {
        OutputDeclarations outputDeclarations = (OutputDeclarations)theEObject;
        T result = caseOutputDeclarations(outputDeclarations);
        if (result == null) result = caseIoVarDeclarations(outputDeclarations);
        if (result == null) result = caseFunctionBlockVarDeclarations(outputDeclarations);
        if (result == null) result = caseProgramVarDeclarations(outputDeclarations);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.VAR_INIT_DECL:
      {
        VarInitDecl varInitDecl = (VarInitDecl)theEObject;
        T result = caseVarInitDecl(varInitDecl);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.INPUT_OUTPUT_DECLARATIONS:
      {
        InputOutputDeclarations inputOutputDeclarations = (InputOutputDeclarations)theEObject;
        T result = caseInputOutputDeclarations(inputOutputDeclarations);
        if (result == null) result = caseIoVarDeclarations(inputOutputDeclarations);
        if (result == null) result = caseFunctionBlockVarDeclarations(inputOutputDeclarations);
        if (result == null) result = caseProgramVarDeclarations(inputOutputDeclarations);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.FUNCTION_BODY:
      {
        FunctionBody functionBody = (FunctionBody)theEObject;
        T result = caseFunctionBody(functionBody);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.INSTRUCTION_LIST:
      {
        InstructionList instructionList = (InstructionList)theEObject;
        T result = caseInstructionList(instructionList);
        if (result == null) result = caseFunctionBody(instructionList);
        if (result == null) result = caseFunctionBlockBody(instructionList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.INSTRUCTION:
      {
        Instruction instruction = (Instruction)theEObject;
        T result = caseInstruction(instruction);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.LABEL:
      {
        Label label = (Label)theEObject;
        T result = caseLabel(label);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.OPERATION:
      {
        Operation operation = (Operation)theEObject;
        T result = caseOperation(operation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.SIMPLE_OPERATION:
      {
        SimpleOperation simpleOperation = (SimpleOperation)theEObject;
        T result = caseSimpleOperation(simpleOperation);
        if (result == null) result = caseOperation(simpleOperation);
        if (result == null) result = caseSimpleInstruction(simpleOperation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.EXPRESSION_OPERATION:
      {
        ExpressionOperation expressionOperation = (ExpressionOperation)theEObject;
        T result = caseExpressionOperation(expressionOperation);
        if (result == null) result = caseOperation(expressionOperation);
        if (result == null) result = caseSimpleInstruction(expressionOperation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.JMP_OPERATION:
      {
        JmpOperation jmpOperation = (JmpOperation)theEObject;
        T result = caseJmpOperation(jmpOperation);
        if (result == null) result = caseOperation(jmpOperation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.OPERAND:
      {
        Operand operand = (Operand)theEObject;
        T result = caseOperand(operand);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.REFERENCED_OPERAND:
      {
        ReferencedOperand referencedOperand = (ReferencedOperand)theEObject;
        T result = caseReferencedOperand(referencedOperand);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.SIMPLE_INSTRUCTION_LIST:
      {
        SimpleInstructionList simpleInstructionList = (SimpleInstructionList)theEObject;
        T result = caseSimpleInstructionList(simpleInstructionList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.SIMPLE_INSTRUCTION:
      {
        SimpleInstruction simpleInstruction = (SimpleInstruction)theEObject;
        T result = caseSimpleInstruction(simpleInstruction);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.STATEMENT_LIST:
      {
        StatementList statementList = (StatementList)theEObject;
        T result = caseStatementList(statementList);
        if (result == null) result = caseFunctionBody(statementList);
        if (result == null) result = caseFunctionBlockBody(statementList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.STATEMENT:
      {
        Statement statement = (Statement)theEObject;
        T result = caseStatement(statement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.ASSIGN_STATEMENT:
      {
        AssignStatement assignStatement = (AssignStatement)theEObject;
        T result = caseAssignStatement(assignStatement);
        if (result == null) result = caseStatement(assignStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.SELECTION_STATEMENT:
      {
        SelectionStatement selectionStatement = (SelectionStatement)theEObject;
        T result = caseSelectionStatement(selectionStatement);
        if (result == null) result = caseStatement(selectionStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.IF_STATEMENT:
      {
        IfStatement ifStatement = (IfStatement)theEObject;
        T result = caseIfStatement(ifStatement);
        if (result == null) result = caseSelectionStatement(ifStatement);
        if (result == null) result = caseStatement(ifStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.ELSE_IF:
      {
        ElseIf elseIf = (ElseIf)theEObject;
        T result = caseElseIf(elseIf);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.CASE_STATEMENT:
      {
        CaseStatement caseStatement = (CaseStatement)theEObject;
        T result = caseCaseStatement(caseStatement);
        if (result == null) result = caseSelectionStatement(caseStatement);
        if (result == null) result = caseStatement(caseStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.CASE_ELEMENT:
      {
        CaseElement caseElement = (CaseElement)theEObject;
        T result = caseCaseElement(caseElement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.CASE_LIST:
      {
        CaseList caseList = (CaseList)theEObject;
        T result = caseCaseList(caseList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.CASE_LIST_ELEMENT:
      {
        CaseListElement caseListElement = (CaseListElement)theEObject;
        T result = caseCaseListElement(caseListElement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.FUNCTION_BLOCK_DECLARATION:
      {
        FunctionBlockDeclaration functionBlockDeclaration = (FunctionBlockDeclaration)theEObject;
        T result = caseFunctionBlockDeclaration(functionBlockDeclaration);
        if (result == null) result = caseLibraryElementDeclaration(functionBlockDeclaration);
        if (result == null) result = caseModelElement(functionBlockDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.FUNCTION_BLOCK_VAR_DECLARATIONS:
      {
        FunctionBlockVarDeclarations functionBlockVarDeclarations = (FunctionBlockVarDeclarations)theEObject;
        T result = caseFunctionBlockVarDeclarations(functionBlockVarDeclarations);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.OTHER_VAR_DECLARATIONS:
      {
        OtherVarDeclarations otherVarDeclarations = (OtherVarDeclarations)theEObject;
        T result = caseOtherVarDeclarations(otherVarDeclarations);
        if (result == null) result = caseFunctionBlockVarDeclarations(otherVarDeclarations);
        if (result == null) result = caseProgramVarDeclarations(otherVarDeclarations);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.VAR_DECLARATIONS:
      {
        VarDeclarations varDeclarations = (VarDeclarations)theEObject;
        T result = caseVarDeclarations(varDeclarations);
        if (result == null) result = caseOtherVarDeclarations(varDeclarations);
        if (result == null) result = caseFunctionBlockVarDeclarations(varDeclarations);
        if (result == null) result = caseProgramVarDeclarations(varDeclarations);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.FUNCTION_BLOCK_BODY:
      {
        FunctionBlockBody functionBlockBody = (FunctionBlockBody)theEObject;
        T result = caseFunctionBlockBody(functionBlockBody);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.PROGRAM_DECLARATION:
      {
        ProgramDeclaration programDeclaration = (ProgramDeclaration)theEObject;
        T result = caseProgramDeclaration(programDeclaration);
        if (result == null) result = caseLibraryElementDeclaration(programDeclaration);
        if (result == null) result = caseModelElement(programDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.PROGRAM_VAR_DECLARATIONS:
      {
        ProgramVarDeclarations programVarDeclarations = (ProgramVarDeclarations)theEObject;
        T result = caseProgramVarDeclarations(programVarDeclarations);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.LOCATED_VAR_DECLARATIONS:
      {
        LocatedVarDeclarations locatedVarDeclarations = (LocatedVarDeclarations)theEObject;
        T result = caseLocatedVarDeclarations(locatedVarDeclarations);
        if (result == null) result = caseProgramVarDeclarations(locatedVarDeclarations);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.LOCATED_VAR_DECLARATION:
      {
        LocatedVarDeclaration locatedVarDeclaration = (LocatedVarDeclaration)theEObject;
        T result = caseLocatedVarDeclaration(locatedVarDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.LOCATION:
      {
        Location location = (Location)theEObject;
        T result = caseLocation(location);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.PROGRAM_ACCESS_DECLS:
      {
        ProgramAccessDecls programAccessDecls = (ProgramAccessDecls)theEObject;
        T result = caseProgramAccessDecls(programAccessDecls);
        if (result == null) result = caseProgramVarDeclarations(programAccessDecls);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.PROGRAM_ACCESS_DECL:
      {
        ProgramAccessDecl programAccessDecl = (ProgramAccessDecl)theEObject;
        T result = caseProgramAccessDecl(programAccessDecl);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.CONFIGURATION_DECLARATION:
      {
        ConfigurationDeclaration configurationDeclaration = (ConfigurationDeclaration)theEObject;
        T result = caseConfigurationDeclaration(configurationDeclaration);
        if (result == null) result = caseLibraryElementDeclaration(configurationDeclaration);
        if (result == null) result = caseModelElement(configurationDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.RESOURCE_DECLARATION:
      {
        ResourceDeclaration resourceDeclaration = (ResourceDeclaration)theEObject;
        T result = caseResourceDeclaration(resourceDeclaration);
        if (result == null) result = caseLibraryElementDeclaration(resourceDeclaration);
        if (result == null) result = caseModelElement(resourceDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.SINGLE_RESOURCE_DECLARATION:
      {
        SingleResourceDeclaration singleResourceDeclaration = (SingleResourceDeclaration)theEObject;
        T result = caseSingleResourceDeclaration(singleResourceDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.PROGRAM_CONFIGURATION:
      {
        ProgramConfiguration programConfiguration = (ProgramConfiguration)theEObject;
        T result = caseProgramConfiguration(programConfiguration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.PROG_CONF_ELEMENTS:
      {
        ProgConfElements progConfElements = (ProgConfElements)theEObject;
        T result = caseProgConfElements(progConfElements);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.PROG_CONF_ELEMENT:
      {
        ProgConfElement progConfElement = (ProgConfElement)theEObject;
        T result = caseProgConfElement(progConfElement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.FB_TASK:
      {
        FBTask fbTask = (FBTask)theEObject;
        T result = caseFBTask(fbTask);
        if (result == null) result = caseProgConfElement(fbTask);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.PROG_CNXN:
      {
        ProgCNXN progCNXN = (ProgCNXN)theEObject;
        T result = caseProgCNXN(progCNXN);
        if (result == null) result = caseProgConfElement(progCNXN);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.DATA_SINK:
      {
        DataSink dataSink = (DataSink)theEObject;
        T result = caseDataSink(dataSink);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.PROG_DATA_SOURCE:
      {
        ProgDataSource progDataSource = (ProgDataSource)theEObject;
        T result = caseProgDataSource(progDataSource);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.GLOBAL_VAR_DECLARATIONS:
      {
        GlobalVarDeclarations globalVarDeclarations = (GlobalVarDeclarations)theEObject;
        T result = caseGlobalVarDeclarations(globalVarDeclarations);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.GLOBAL_VAR_DECL:
      {
        GlobalVarDecl globalVarDecl = (GlobalVarDecl)theEObject;
        T result = caseGlobalVarDecl(globalVarDecl);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.GLOBAL_VAR_SPEC:
      {
        GlobalVarSpec globalVarSpec = (GlobalVarSpec)theEObject;
        T result = caseGlobalVarSpec(globalVarSpec);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.GLOBAL_VAR_LIST:
      {
        GlobalVarList globalVarList = (GlobalVarList)theEObject;
        T result = caseGlobalVarList(globalVarList);
        if (result == null) result = caseGlobalVarSpec(globalVarList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.GLOBAL_VAR:
      {
        GlobalVar globalVar = (GlobalVar)theEObject;
        T result = caseGlobalVar(globalVar);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.TASK_CONFIGURATION:
      {
        TaskConfiguration taskConfiguration = (TaskConfiguration)theEObject;
        T result = caseTaskConfiguration(taskConfiguration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.TASK_INITIALIZATION:
      {
        TaskInitialization taskInitialization = (TaskInitialization)theEObject;
        T result = caseTaskInitialization(taskInitialization);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.DATA_SOURCE:
      {
        DataSource dataSource = (DataSource)theEObject;
        T result = caseDataSource(dataSource);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.CONSTANT:
      {
        Constant constant = (Constant)theEObject;
        T result = caseConstant(constant);
        if (result == null) result = caseInitialElement(constant);
        if (result == null) result = caseExpression(constant);
        if (result == null) result = caseProgDataSource(constant);
        if (result == null) result = caseDataSource(constant);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.NUMERIC_LITERAL:
      {
        NumericLiteral numericLiteral = (NumericLiteral)theEObject;
        T result = caseNumericLiteral(numericLiteral);
        if (result == null) result = caseConstant(numericLiteral);
        if (result == null) result = caseInitialElement(numericLiteral);
        if (result == null) result = caseExpression(numericLiteral);
        if (result == null) result = caseProgDataSource(numericLiteral);
        if (result == null) result = caseDataSource(numericLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.INTEGER_LITERAL:
      {
        IntegerLiteral integerLiteral = (IntegerLiteral)theEObject;
        T result = caseIntegerLiteral(integerLiteral);
        if (result == null) result = caseNumericLiteral(integerLiteral);
        if (result == null) result = caseConstant(integerLiteral);
        if (result == null) result = caseInitialElement(integerLiteral);
        if (result == null) result = caseExpression(integerLiteral);
        if (result == null) result = caseProgDataSource(integerLiteral);
        if (result == null) result = caseDataSource(integerLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.REAL_LITERAL:
      {
        RealLiteral realLiteral = (RealLiteral)theEObject;
        T result = caseRealLiteral(realLiteral);
        if (result == null) result = caseNumericLiteral(realLiteral);
        if (result == null) result = caseConstant(realLiteral);
        if (result == null) result = caseInitialElement(realLiteral);
        if (result == null) result = caseExpression(realLiteral);
        if (result == null) result = caseProgDataSource(realLiteral);
        if (result == null) result = caseDataSource(realLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.CHARACTER_STRING:
      {
        CharacterString characterString = (CharacterString)theEObject;
        T result = caseCharacterString(characterString);
        if (result == null) result = caseConstant(characterString);
        if (result == null) result = caseInitialElement(characterString);
        if (result == null) result = caseExpression(characterString);
        if (result == null) result = caseProgDataSource(characterString);
        if (result == null) result = caseDataSource(characterString);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.BIT_STRING:
      {
        BitString bitString = (BitString)theEObject;
        T result = caseBitString(bitString);
        if (result == null) result = caseConstant(bitString);
        if (result == null) result = caseInitialElement(bitString);
        if (result == null) result = caseExpression(bitString);
        if (result == null) result = caseProgDataSource(bitString);
        if (result == null) result = caseDataSource(bitString);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.BOOLEAN:
      {
        com.bichler.iec.iec.Boolean boolean_ = (com.bichler.iec.iec.Boolean)theEObject;
        T result = caseBoolean(boolean_);
        if (result == null) result = caseConstant(boolean_);
        if (result == null) result = caseInitialElement(boolean_);
        if (result == null) result = caseExpression(boolean_);
        if (result == null) result = caseProgDataSource(boolean_);
        if (result == null) result = caseDataSource(boolean_);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.TIME_LITERAL:
      {
        TimeLiteral timeLiteral = (TimeLiteral)theEObject;
        T result = caseTimeLiteral(timeLiteral);
        if (result == null) result = caseConstant(timeLiteral);
        if (result == null) result = caseInitialElement(timeLiteral);
        if (result == null) result = caseExpression(timeLiteral);
        if (result == null) result = caseProgDataSource(timeLiteral);
        if (result == null) result = caseDataSource(timeLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.DURATION_LITERAL:
      {
        DurationLiteral durationLiteral = (DurationLiteral)theEObject;
        T result = caseDurationLiteral(durationLiteral);
        if (result == null) result = caseTimeLiteral(durationLiteral);
        if (result == null) result = caseConstant(durationLiteral);
        if (result == null) result = caseInitialElement(durationLiteral);
        if (result == null) result = caseExpression(durationLiteral);
        if (result == null) result = caseProgDataSource(durationLiteral);
        if (result == null) result = caseDataSource(durationLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.TIME_OF_DAY_LITERAL:
      {
        TimeOfDayLiteral timeOfDayLiteral = (TimeOfDayLiteral)theEObject;
        T result = caseTimeOfDayLiteral(timeOfDayLiteral);
        if (result == null) result = caseTimeLiteral(timeOfDayLiteral);
        if (result == null) result = caseConstant(timeOfDayLiteral);
        if (result == null) result = caseInitialElement(timeOfDayLiteral);
        if (result == null) result = caseExpression(timeOfDayLiteral);
        if (result == null) result = caseProgDataSource(timeOfDayLiteral);
        if (result == null) result = caseDataSource(timeOfDayLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.DATE_LITERAL:
      {
        DateLiteral dateLiteral = (DateLiteral)theEObject;
        T result = caseDateLiteral(dateLiteral);
        if (result == null) result = caseTimeLiteral(dateLiteral);
        if (result == null) result = caseConstant(dateLiteral);
        if (result == null) result = caseInitialElement(dateLiteral);
        if (result == null) result = caseExpression(dateLiteral);
        if (result == null) result = caseProgDataSource(dateLiteral);
        if (result == null) result = caseDataSource(dateLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.DATE_AND_TIME_LITERAL:
      {
        DateAndTimeLiteral dateAndTimeLiteral = (DateAndTimeLiteral)theEObject;
        T result = caseDateAndTimeLiteral(dateAndTimeLiteral);
        if (result == null) result = caseTimeLiteral(dateAndTimeLiteral);
        if (result == null) result = caseConstant(dateAndTimeLiteral);
        if (result == null) result = caseInitialElement(dateAndTimeLiteral);
        if (result == null) result = caseExpression(dateAndTimeLiteral);
        if (result == null) result = caseProgDataSource(dateAndTimeLiteral);
        if (result == null) result = caseDataSource(dateAndTimeLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.BINARY_EXPRESSION:
      {
        BinaryExpression binaryExpression = (BinaryExpression)theEObject;
        T result = caseBinaryExpression(binaryExpression);
        if (result == null) result = caseExpression(binaryExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case IecPackage.UNARY_EXPRESSION:
      {
        UnaryExpression unaryExpression = (UnaryExpression)theEObject;
        T result = caseUnaryExpression(unaryExpression);
        if (result == null) result = caseExpression(unaryExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModel(Model object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModelElement(ModelElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Library Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Library Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLibraryElement(LibraryElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Library Element Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Library Element Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLibraryElementDeclaration(LibraryElementDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Data Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Data Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDataType(DataType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Non Generic Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Non Generic Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNonGenericType(NonGenericType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Elementary Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Elementary Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseElementaryType(ElementaryType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>String Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>String Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStringType(StringType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Numeric Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Numeric Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNumericType(NumericType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Integer Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Integer Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIntegerType(IntegerType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Signed Integer Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Signed Integer Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSignedIntegerType(SignedIntegerType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Plain Integer Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Plain Integer Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePlainIntegerType(PlainIntegerType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Unsigned Integer Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Unsigned Integer Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseUnsignedIntegerType(UnsignedIntegerType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Real Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Real Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRealType(RealType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Date Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Date Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDateType(DateType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Bit String Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Bit String Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBitStringType(BitStringType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Generic Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Generic Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGenericType(GenericType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Derived Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Derived Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDerivedType(DerivedType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Data Type Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Data Type Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDataTypeDeclaration(DataTypeDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Type Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Type Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTypeDeclaration(TypeDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Simple Type Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Simple Type Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSimpleTypeDeclaration(SimpleTypeDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Spec Init</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Spec Init</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSpecInit(SpecInit object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Range Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Range Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRangeDeclaration(RangeDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Enum Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Enum Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEnumDeclaration(EnumDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Array Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Array Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseArrayDeclaration(ArrayDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Enumeration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Enumeration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEnumeration(Enumeration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Enumerated Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Enumerated Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEnumeratedValue(EnumeratedValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Array Initialization</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Array Initialization</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseArrayInitialization(ArrayInitialization object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Array Initial Elements</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Array Initial Elements</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseArrayInitialElements(ArrayInitialElements object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Initial Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Initial Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInitialElement(InitialElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Structure Type Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Structure Type Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStructureTypeDeclaration(StructureTypeDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Structure Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Structure Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStructureDeclaration(StructureDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Structure Element Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Structure Element Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStructureElementDeclaration(StructureElementDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Initialized Structure</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Initialized Structure</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInitializedStructure(InitializedStructure object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Structure Initialization</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Structure Initialization</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStructureInitialization(StructureInitialization object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Structure Element Initialization</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Structure Element Initialization</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStructureElementInitialization(StructureElementInitialization object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>String Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>String Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStringDeclaration(StringDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Variable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Variable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVariable(Variable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Variable Access</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Variable Access</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVariableAccess(VariableAccess object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Direct Variable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Direct Variable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDirectVariable(DirectVariable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Symbolic Variable Access</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Symbolic Variable Access</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSymbolicVariableAccess(SymbolicVariableAccess object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Named Variable Access</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Named Variable Access</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNamedVariableAccess(NamedVariableAccess object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Multi Element Variable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Multi Element Variable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMultiElementVariable(MultiElementVariable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Array Variable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Array Variable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseArrayVariable(ArrayVariable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Structured Variable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Structured Variable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStructuredVariable(StructuredVariable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExpression(Expression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Param Assignment</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Param Assignment</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseParamAssignment(ParamAssignment object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Function Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Function Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFunctionDeclaration(FunctionDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Io Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Io Var Declarations</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIoVarDeclarations(IoVarDeclarations object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Input Declarations</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Input Declarations</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInputDeclarations(InputDeclarations object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Input Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Input Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInputDeclaration(InputDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Edge Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Edge Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEdgeDeclaration(EdgeDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Decl Specification</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Decl Specification</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDeclSpecification(DeclSpecification object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Var Decl Specification</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Var Decl Specification</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVarDeclSpecification(VarDeclSpecification object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Edge Decl Specification</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Edge Decl Specification</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEdgeDeclSpecification(EdgeDeclSpecification object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Var1 List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Var1 List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVar1List(Var1List object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Output Declarations</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Output Declarations</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOutputDeclarations(OutputDeclarations object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Var Init Decl</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Var Init Decl</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVarInitDecl(VarInitDecl object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Input Output Declarations</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Input Output Declarations</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInputOutputDeclarations(InputOutputDeclarations object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Function Body</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Function Body</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFunctionBody(FunctionBody object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Instruction List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Instruction List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInstructionList(InstructionList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Instruction</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Instruction</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInstruction(Instruction object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Label</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Label</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLabel(Label object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOperation(Operation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Simple Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Simple Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSimpleOperation(SimpleOperation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Expression Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Expression Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExpressionOperation(ExpressionOperation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jmp Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jmp Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJmpOperation(JmpOperation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Operand</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operand</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOperand(Operand object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Referenced Operand</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Referenced Operand</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseReferencedOperand(ReferencedOperand object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Simple Instruction List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Simple Instruction List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSimpleInstructionList(SimpleInstructionList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Simple Instruction</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Simple Instruction</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSimpleInstruction(SimpleInstruction object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Statement List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Statement List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStatementList(StatementList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStatement(Statement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Assign Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Assign Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAssignStatement(AssignStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Selection Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Selection Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSelectionStatement(SelectionStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>If Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>If Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIfStatement(IfStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Else If</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Else If</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseElseIf(ElseIf object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Case Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Case Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCaseStatement(CaseStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Case Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Case Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCaseElement(CaseElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Case List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Case List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCaseList(CaseList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Case List Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Case List Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCaseListElement(CaseListElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Function Block Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Function Block Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFunctionBlockDeclaration(FunctionBlockDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Function Block Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Function Block Var Declarations</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFunctionBlockVarDeclarations(FunctionBlockVarDeclarations object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Other Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Other Var Declarations</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOtherVarDeclarations(OtherVarDeclarations object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Var Declarations</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVarDeclarations(VarDeclarations object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Function Block Body</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Function Block Body</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFunctionBlockBody(FunctionBlockBody object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Program Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Program Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProgramDeclaration(ProgramDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Program Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Program Var Declarations</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProgramVarDeclarations(ProgramVarDeclarations object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Located Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Located Var Declarations</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLocatedVarDeclarations(LocatedVarDeclarations object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Located Var Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Located Var Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLocatedVarDeclaration(LocatedVarDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Location</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Location</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLocation(Location object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Program Access Decls</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Program Access Decls</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProgramAccessDecls(ProgramAccessDecls object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Program Access Decl</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Program Access Decl</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProgramAccessDecl(ProgramAccessDecl object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Configuration Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Configuration Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseConfigurationDeclaration(ConfigurationDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Resource Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Resource Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseResourceDeclaration(ResourceDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Single Resource Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Single Resource Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSingleResourceDeclaration(SingleResourceDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Program Configuration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Program Configuration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProgramConfiguration(ProgramConfiguration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Prog Conf Elements</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Prog Conf Elements</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProgConfElements(ProgConfElements object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Prog Conf Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Prog Conf Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProgConfElement(ProgConfElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>FB Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>FB Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFBTask(FBTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Prog CNXN</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Prog CNXN</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProgCNXN(ProgCNXN object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Data Sink</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Data Sink</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDataSink(DataSink object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Prog Data Source</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Prog Data Source</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProgDataSource(ProgDataSource object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Global Var Declarations</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Global Var Declarations</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGlobalVarDeclarations(GlobalVarDeclarations object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Global Var Decl</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Global Var Decl</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGlobalVarDecl(GlobalVarDecl object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Global Var Spec</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Global Var Spec</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGlobalVarSpec(GlobalVarSpec object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Global Var List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Global Var List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGlobalVarList(GlobalVarList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Global Var</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Global Var</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGlobalVar(GlobalVar object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Task Configuration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Task Configuration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTaskConfiguration(TaskConfiguration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Task Initialization</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Task Initialization</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTaskInitialization(TaskInitialization object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Data Source</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Data Source</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDataSource(DataSource object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Constant</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Constant</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseConstant(Constant object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Numeric Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Numeric Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNumericLiteral(NumericLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Integer Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Integer Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIntegerLiteral(IntegerLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Real Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Real Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRealLiteral(RealLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Character String</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Character String</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCharacterString(CharacterString object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Bit String</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Bit String</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBitString(BitString object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Boolean</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Boolean</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBoolean(com.bichler.iec.iec.Boolean object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Time Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Time Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTimeLiteral(TimeLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Duration Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Duration Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDurationLiteral(DurationLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Time Of Day Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Time Of Day Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTimeOfDayLiteral(TimeOfDayLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Date Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Date Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDateLiteral(DateLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Date And Time Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Date And Time Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDateAndTimeLiteral(DateAndTimeLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Binary Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Binary Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBinaryExpression(BinaryExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Unary Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Unary Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseUnaryExpression(UnaryExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //IecSwitch
