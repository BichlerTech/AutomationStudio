/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.bichler.iec.iec.IecFactory
 * @model kind="package"
 * @generated
 */
public interface IecPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "iec";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.bichler.com/iec/Iec";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "iec";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  IecPackage eINSTANCE = com.bichler.iec.iec.impl.IecPackageImpl.init();

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ModelImpl <em>Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ModelImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getModel()
   * @generated
   */
  int MODEL = 0;

  /**
   * The feature id for the '<em><b>Model Element</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__MODEL_ELEMENT = 0;

  /**
   * The number of structural features of the '<em>Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ModelElementImpl <em>Model Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ModelElementImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getModelElement()
   * @generated
   */
  int MODEL_ELEMENT = 1;

  /**
   * The number of structural features of the '<em>Model Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ELEMENT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.LibraryElementImpl <em>Library Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.LibraryElementImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getLibraryElement()
   * @generated
   */
  int LIBRARY_ELEMENT = 2;

  /**
   * The number of structural features of the '<em>Library Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LIBRARY_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.LibraryElementDeclarationImpl <em>Library Element Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.LibraryElementDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getLibraryElementDeclaration()
   * @generated
   */
  int LIBRARY_ELEMENT_DECLARATION = 3;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LIBRARY_ELEMENT_DECLARATION__NAME = MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Library Element Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT = MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.DataTypeImpl <em>Data Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.DataTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getDataType()
   * @generated
   */
  int DATA_TYPE = 4;

  /**
   * The number of structural features of the '<em>Data Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATA_TYPE_FEATURE_COUNT = LIBRARY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.NonGenericTypeImpl <em>Non Generic Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.NonGenericTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getNonGenericType()
   * @generated
   */
  int NON_GENERIC_TYPE = 5;

  /**
   * The number of structural features of the '<em>Non Generic Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NON_GENERIC_TYPE_FEATURE_COUNT = DATA_TYPE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ElementaryTypeImpl <em>Elementary Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ElementaryTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getElementaryType()
   * @generated
   */
  int ELEMENTARY_TYPE = 6;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ELEMENTARY_TYPE__TYPE_NAME = NON_GENERIC_TYPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Elementary Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ELEMENTARY_TYPE_FEATURE_COUNT = NON_GENERIC_TYPE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.StringTypeImpl <em>String Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.StringTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getStringType()
   * @generated
   */
  int STRING_TYPE = 7;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_TYPE__TYPE_NAME = ELEMENTARY_TYPE__TYPE_NAME;

  /**
   * The number of structural features of the '<em>String Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_TYPE_FEATURE_COUNT = ELEMENTARY_TYPE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.NumericTypeImpl <em>Numeric Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.NumericTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getNumericType()
   * @generated
   */
  int NUMERIC_TYPE = 8;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_TYPE__TYPE_NAME = ELEMENTARY_TYPE__TYPE_NAME;

  /**
   * The number of structural features of the '<em>Numeric Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_TYPE_FEATURE_COUNT = ELEMENTARY_TYPE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.IntegerTypeImpl <em>Integer Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.IntegerTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getIntegerType()
   * @generated
   */
  int INTEGER_TYPE = 9;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTEGER_TYPE__TYPE_NAME = NUMERIC_TYPE__TYPE_NAME;

  /**
   * The number of structural features of the '<em>Integer Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTEGER_TYPE_FEATURE_COUNT = NUMERIC_TYPE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.SignedIntegerTypeImpl <em>Signed Integer Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.SignedIntegerTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getSignedIntegerType()
   * @generated
   */
  int SIGNED_INTEGER_TYPE = 10;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SIGNED_INTEGER_TYPE__TYPE_NAME = INTEGER_TYPE__TYPE_NAME;

  /**
   * The number of structural features of the '<em>Signed Integer Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SIGNED_INTEGER_TYPE_FEATURE_COUNT = INTEGER_TYPE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.PlainIntegerTypeImpl <em>Plain Integer Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.PlainIntegerTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getPlainIntegerType()
   * @generated
   */
  int PLAIN_INTEGER_TYPE = 11;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PLAIN_INTEGER_TYPE__TYPE_NAME = INTEGER_TYPE__TYPE_NAME;

  /**
   * The number of structural features of the '<em>Plain Integer Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PLAIN_INTEGER_TYPE_FEATURE_COUNT = INTEGER_TYPE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.UnsignedIntegerTypeImpl <em>Unsigned Integer Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.UnsignedIntegerTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getUnsignedIntegerType()
   * @generated
   */
  int UNSIGNED_INTEGER_TYPE = 12;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNSIGNED_INTEGER_TYPE__TYPE_NAME = INTEGER_TYPE__TYPE_NAME;

  /**
   * The number of structural features of the '<em>Unsigned Integer Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNSIGNED_INTEGER_TYPE_FEATURE_COUNT = INTEGER_TYPE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.RealTypeImpl <em>Real Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.RealTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getRealType()
   * @generated
   */
  int REAL_TYPE = 13;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REAL_TYPE__TYPE_NAME = NUMERIC_TYPE__TYPE_NAME;

  /**
   * The number of structural features of the '<em>Real Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REAL_TYPE_FEATURE_COUNT = NUMERIC_TYPE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.DateTypeImpl <em>Date Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.DateTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getDateType()
   * @generated
   */
  int DATE_TYPE = 14;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_TYPE__TYPE_NAME = ELEMENTARY_TYPE__TYPE_NAME;

  /**
   * The number of structural features of the '<em>Date Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_TYPE_FEATURE_COUNT = ELEMENTARY_TYPE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.BitStringTypeImpl <em>Bit String Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.BitStringTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getBitStringType()
   * @generated
   */
  int BIT_STRING_TYPE = 15;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BIT_STRING_TYPE__TYPE_NAME = ELEMENTARY_TYPE__TYPE_NAME;

  /**
   * The number of structural features of the '<em>Bit String Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BIT_STRING_TYPE_FEATURE_COUNT = ELEMENTARY_TYPE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.GenericTypeImpl <em>Generic Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.GenericTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getGenericType()
   * @generated
   */
  int GENERIC_TYPE = 16;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GENERIC_TYPE__TYPE_NAME = DATA_TYPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Generic Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GENERIC_TYPE_FEATURE_COUNT = DATA_TYPE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.DerivedTypeImpl <em>Derived Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.DerivedTypeImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getDerivedType()
   * @generated
   */
  int DERIVED_TYPE = 17;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DERIVED_TYPE__NAME = NON_GENERIC_TYPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Derived Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DERIVED_TYPE_FEATURE_COUNT = NON_GENERIC_TYPE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.DataTypeDeclarationImpl <em>Data Type Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.DataTypeDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getDataTypeDeclaration()
   * @generated
   */
  int DATA_TYPE_DECLARATION = 18;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATA_TYPE_DECLARATION__NAME = LIBRARY_ELEMENT_DECLARATION__NAME;

  /**
   * The feature id for the '<em><b>Type Declaration</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATA_TYPE_DECLARATION__TYPE_DECLARATION = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Data Type Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATA_TYPE_DECLARATION_FEATURE_COUNT = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.TypeDeclarationImpl <em>Type Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.TypeDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getTypeDeclaration()
   * @generated
   */
  int TYPE_DECLARATION = 19;

  /**
   * The feature id for the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_DECLARATION__DERIVED_TYPE = 0;

  /**
   * The number of structural features of the '<em>Type Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_DECLARATION_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.SimpleTypeDeclarationImpl <em>Simple Type Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.SimpleTypeDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getSimpleTypeDeclaration()
   * @generated
   */
  int SIMPLE_TYPE_DECLARATION = 20;

  /**
   * The feature id for the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SIMPLE_TYPE_DECLARATION__DERIVED_TYPE = TYPE_DECLARATION__DERIVED_TYPE;

  /**
   * The feature id for the '<em><b>Spec Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SIMPLE_TYPE_DECLARATION__SPEC_INIT = TYPE_DECLARATION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Simple Type Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SIMPLE_TYPE_DECLARATION_FEATURE_COUNT = TYPE_DECLARATION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.SpecInitImpl <em>Spec Init</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.SpecInitImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getSpecInit()
   * @generated
   */
  int SPEC_INIT = 21;

  /**
   * The feature id for the '<em><b>Base Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SPEC_INIT__BASE_TYPE = 0;

  /**
   * The feature id for the '<em><b>Constant</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SPEC_INIT__CONSTANT = 1;

  /**
   * The number of structural features of the '<em>Spec Init</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SPEC_INIT_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.RangeDeclarationImpl <em>Range Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.RangeDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getRangeDeclaration()
   * @generated
   */
  int RANGE_DECLARATION = 22;

  /**
   * The feature id for the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RANGE_DECLARATION__DERIVED_TYPE = TYPE_DECLARATION__DERIVED_TYPE;

  /**
   * The feature id for the '<em><b>Base Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RANGE_DECLARATION__BASE_TYPE = TYPE_DECLARATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Range</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RANGE_DECLARATION__RANGE = TYPE_DECLARATION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Constant</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RANGE_DECLARATION__CONSTANT = TYPE_DECLARATION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Range Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RANGE_DECLARATION_FEATURE_COUNT = TYPE_DECLARATION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.EnumDeclarationImpl <em>Enum Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.EnumDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getEnumDeclaration()
   * @generated
   */
  int ENUM_DECLARATION = 23;

  /**
   * The feature id for the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_DECLARATION__DERIVED_TYPE = TYPE_DECLARATION__DERIVED_TYPE;

  /**
   * The feature id for the '<em><b>Enumeration</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_DECLARATION__ENUMERATION = TYPE_DECLARATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Constant</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_DECLARATION__CONSTANT = TYPE_DECLARATION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Enum Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_DECLARATION_FEATURE_COUNT = TYPE_DECLARATION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ArrayDeclarationImpl <em>Array Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ArrayDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getArrayDeclaration()
   * @generated
   */
  int ARRAY_DECLARATION = 24;

  /**
   * The feature id for the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_DECLARATION__DERIVED_TYPE = TYPE_DECLARATION__DERIVED_TYPE;

  /**
   * The feature id for the '<em><b>Ranges</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_DECLARATION__RANGES = TYPE_DECLARATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Base Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_DECLARATION__BASE_TYPE = TYPE_DECLARATION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Constant</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_DECLARATION__CONSTANT = TYPE_DECLARATION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Array Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_DECLARATION_FEATURE_COUNT = TYPE_DECLARATION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.EnumerationImpl <em>Enumeration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.EnumerationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getEnumeration()
   * @generated
   */
  int ENUMERATION = 25;

  /**
   * The feature id for the '<em><b>Values</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUMERATION__VALUES = 0;

  /**
   * The number of structural features of the '<em>Enumeration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUMERATION_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.InitialElementImpl <em>Initial Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.InitialElementImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getInitialElement()
   * @generated
   */
  int INITIAL_ELEMENT = 29;

  /**
   * The number of structural features of the '<em>Initial Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INITIAL_ELEMENT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.EnumeratedValueImpl <em>Enumerated Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.EnumeratedValueImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getEnumeratedValue()
   * @generated
   */
  int ENUMERATED_VALUE = 26;

  /**
   * The feature id for the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUMERATED_VALUE__DERIVED_TYPE = INITIAL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUMERATED_VALUE__NAME = INITIAL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Enumerated Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUMERATED_VALUE_FEATURE_COUNT = INITIAL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ArrayInitializationImpl <em>Array Initialization</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ArrayInitializationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getArrayInitialization()
   * @generated
   */
  int ARRAY_INITIALIZATION = 27;

  /**
   * The feature id for the '<em><b>Initial Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_INITIALIZATION__INITIAL_ELEMENTS = INITIAL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Array Initialization</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_INITIALIZATION_FEATURE_COUNT = INITIAL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ArrayInitialElementsImpl <em>Array Initial Elements</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ArrayInitialElementsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getArrayInitialElements()
   * @generated
   */
  int ARRAY_INITIAL_ELEMENTS = 28;

  /**
   * The feature id for the '<em><b>Initial Element</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT = 0;

  /**
   * The feature id for the '<em><b>Index</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_INITIAL_ELEMENTS__INDEX = 1;

  /**
   * The number of structural features of the '<em>Array Initial Elements</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_INITIAL_ELEMENTS_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.StructureTypeDeclarationImpl <em>Structure Type Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.StructureTypeDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructureTypeDeclaration()
   * @generated
   */
  int STRUCTURE_TYPE_DECLARATION = 30;

  /**
   * The feature id for the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_TYPE_DECLARATION__DERIVED_TYPE = TYPE_DECLARATION__DERIVED_TYPE;

  /**
   * The feature id for the '<em><b>Declaration</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_TYPE_DECLARATION__DECLARATION = TYPE_DECLARATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Initialization</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_TYPE_DECLARATION__INITIALIZATION = TYPE_DECLARATION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Structure Type Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_TYPE_DECLARATION_FEATURE_COUNT = TYPE_DECLARATION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.StructureDeclarationImpl <em>Structure Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.StructureDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructureDeclaration()
   * @generated
   */
  int STRUCTURE_DECLARATION = 31;

  /**
   * The feature id for the '<em><b>Structure Element</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_DECLARATION__STRUCTURE_ELEMENT = 0;

  /**
   * The number of structural features of the '<em>Structure Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_DECLARATION_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.StructureElementDeclarationImpl <em>Structure Element Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.StructureElementDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructureElementDeclaration()
   * @generated
   */
  int STRUCTURE_ELEMENT_DECLARATION = 32;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_ELEMENT_DECLARATION__NAME = 0;

  /**
   * The feature id for the '<em><b>Spec Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_ELEMENT_DECLARATION__SPEC_INIT = 1;

  /**
   * The number of structural features of the '<em>Structure Element Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_ELEMENT_DECLARATION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.InitializedStructureImpl <em>Initialized Structure</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.InitializedStructureImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getInitializedStructure()
   * @generated
   */
  int INITIALIZED_STRUCTURE = 33;

  /**
   * The feature id for the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INITIALIZED_STRUCTURE__DERIVED_TYPE = 0;

  /**
   * The feature id for the '<em><b>Initialization</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INITIALIZED_STRUCTURE__INITIALIZATION = 1;

  /**
   * The number of structural features of the '<em>Initialized Structure</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INITIALIZED_STRUCTURE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.StructureInitializationImpl <em>Structure Initialization</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.StructureInitializationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructureInitialization()
   * @generated
   */
  int STRUCTURE_INITIALIZATION = 34;

  /**
   * The feature id for the '<em><b>Initial Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_INITIALIZATION__INITIAL_ELEMENTS = INITIAL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Structure Initialization</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_INITIALIZATION_FEATURE_COUNT = INITIAL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.StructureElementInitializationImpl <em>Structure Element Initialization</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.StructureElementInitializationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructureElementInitialization()
   * @generated
   */
  int STRUCTURE_ELEMENT_INITIALIZATION = 35;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_ELEMENT_INITIALIZATION__NAME = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_ELEMENT_INITIALIZATION__VALUE = 1;

  /**
   * The number of structural features of the '<em>Structure Element Initialization</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURE_ELEMENT_INITIALIZATION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.StringDeclarationImpl <em>String Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.StringDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getStringDeclaration()
   * @generated
   */
  int STRING_DECLARATION = 36;

  /**
   * The feature id for the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_DECLARATION__DERIVED_TYPE = TYPE_DECLARATION__DERIVED_TYPE;

  /**
   * The feature id for the '<em><b>String</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_DECLARATION__STRING = TYPE_DECLARATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Size</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_DECLARATION__SIZE = TYPE_DECLARATION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Initial Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_DECLARATION__INITIAL_VALUE = TYPE_DECLARATION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>String Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_DECLARATION_FEATURE_COUNT = TYPE_DECLARATION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.VariableImpl <em>Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.VariableImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getVariable()
   * @generated
   */
  int VARIABLE = 37;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__NAME = 0;

  /**
   * The number of structural features of the '<em>Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ReferencedOperandImpl <em>Referenced Operand</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ReferencedOperandImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getReferencedOperand()
   * @generated
   */
  int REFERENCED_OPERAND = 68;

  /**
   * The number of structural features of the '<em>Referenced Operand</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCED_OPERAND_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.VariableAccessImpl <em>Variable Access</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.VariableAccessImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getVariableAccess()
   * @generated
   */
  int VARIABLE_ACCESS = 38;

  /**
   * The number of structural features of the '<em>Variable Access</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_ACCESS_FEATURE_COUNT = REFERENCED_OPERAND_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.DirectVariableImpl <em>Direct Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.DirectVariableImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getDirectVariable()
   * @generated
   */
  int DIRECT_VARIABLE = 39;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIRECT_VARIABLE__NAME = VARIABLE_ACCESS_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Direct Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIRECT_VARIABLE_FEATURE_COUNT = VARIABLE_ACCESS_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.SymbolicVariableAccessImpl <em>Symbolic Variable Access</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.SymbolicVariableAccessImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getSymbolicVariableAccess()
   * @generated
   */
  int SYMBOLIC_VARIABLE_ACCESS = 40;

  /**
   * The number of structural features of the '<em>Symbolic Variable Access</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYMBOLIC_VARIABLE_ACCESS_FEATURE_COUNT = VARIABLE_ACCESS_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.NamedVariableAccessImpl <em>Named Variable Access</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.NamedVariableAccessImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getNamedVariableAccess()
   * @generated
   */
  int NAMED_VARIABLE_ACCESS = 41;

  /**
   * The feature id for the '<em><b>Named Variable</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAMED_VARIABLE_ACCESS__NAMED_VARIABLE = SYMBOLIC_VARIABLE_ACCESS_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Named Variable Access</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAMED_VARIABLE_ACCESS_FEATURE_COUNT = SYMBOLIC_VARIABLE_ACCESS_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.MultiElementVariableImpl <em>Multi Element Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.MultiElementVariableImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getMultiElementVariable()
   * @generated
   */
  int MULTI_ELEMENT_VARIABLE = 42;

  /**
   * The number of structural features of the '<em>Multi Element Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MULTI_ELEMENT_VARIABLE_FEATURE_COUNT = SYMBOLIC_VARIABLE_ACCESS_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ArrayVariableImpl <em>Array Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ArrayVariableImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getArrayVariable()
   * @generated
   */
  int ARRAY_VARIABLE = 43;

  /**
   * The feature id for the '<em><b>Subscripted Variable</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_VARIABLE__SUBSCRIPTED_VARIABLE = MULTI_ELEMENT_VARIABLE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Subscripts</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_VARIABLE__SUBSCRIPTS = MULTI_ELEMENT_VARIABLE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Array Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_VARIABLE_FEATURE_COUNT = MULTI_ELEMENT_VARIABLE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.StructuredVariableImpl <em>Structured Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.StructuredVariableImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructuredVariable()
   * @generated
   */
  int STRUCTURED_VARIABLE = 44;

  /**
   * The feature id for the '<em><b>Record Variable</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURED_VARIABLE__RECORD_VARIABLE = MULTI_ELEMENT_VARIABLE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Field Selector</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURED_VARIABLE__FIELD_SELECTOR = MULTI_ELEMENT_VARIABLE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Structured Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCTURED_VARIABLE_FEATURE_COUNT = MULTI_ELEMENT_VARIABLE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ExpressionImpl <em>Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ExpressionImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getExpression()
   * @generated
   */
  int EXPRESSION = 45;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION__VARIABLE = 0;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION__FBNAME = 1;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION__OPENBR = 2;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION__PARAMASSIGNMENT = 3;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION__CLOSEBR = 4;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION__EXPRESSION = 5;

  /**
   * The number of structural features of the '<em>Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_FEATURE_COUNT = 6;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ParamAssignmentImpl <em>Param Assignment</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ParamAssignmentImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getParamAssignment()
   * @generated
   */
  int PARAM_ASSIGNMENT = 46;

  /**
   * The feature id for the '<em><b>Variablename</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM_ASSIGNMENT__VARIABLENAME = 0;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM_ASSIGNMENT__EXPRESSION = 1;

  /**
   * The feature id for the '<em><b>Not</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM_ASSIGNMENT__NOT = 2;

  /**
   * The feature id for the '<em><b>Variable1</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM_ASSIGNMENT__VARIABLE1 = 3;

  /**
   * The feature id for the '<em><b>Variable2</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM_ASSIGNMENT__VARIABLE2 = 4;

  /**
   * The number of structural features of the '<em>Param Assignment</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM_ASSIGNMENT_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.FunctionDeclarationImpl <em>Function Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.FunctionDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getFunctionDeclaration()
   * @generated
   */
  int FUNCTION_DECLARATION = 47;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_DECLARATION__NAME = LIBRARY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_DECLARATION__TYPE = LIBRARY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Io Var Declarations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_DECLARATION__IO_VAR_DECLARATIONS = LIBRARY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_DECLARATION__BODY = LIBRARY_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Function Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_DECLARATION_FEATURE_COUNT = LIBRARY_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.FunctionBlockVarDeclarationsImpl <em>Function Block Var Declarations</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.FunctionBlockVarDeclarationsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getFunctionBlockVarDeclarations()
   * @generated
   */
  int FUNCTION_BLOCK_VAR_DECLARATIONS = 82;

  /**
   * The number of structural features of the '<em>Function Block Var Declarations</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_BLOCK_VAR_DECLARATIONS_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.IoVarDeclarationsImpl <em>Io Var Declarations</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.IoVarDeclarationsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getIoVarDeclarations()
   * @generated
   */
  int IO_VAR_DECLARATIONS = 48;

  /**
   * The number of structural features of the '<em>Io Var Declarations</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_VAR_DECLARATIONS_FEATURE_COUNT = FUNCTION_BLOCK_VAR_DECLARATIONS_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.InputDeclarationsImpl <em>Input Declarations</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.InputDeclarationsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getInputDeclarations()
   * @generated
   */
  int INPUT_DECLARATIONS = 49;

  /**
   * The feature id for the '<em><b>Declarations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT_DECLARATIONS__DECLARATIONS = IO_VAR_DECLARATIONS_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Input Declarations</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT_DECLARATIONS_FEATURE_COUNT = IO_VAR_DECLARATIONS_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.InputDeclarationImpl <em>Input Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.InputDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getInputDeclaration()
   * @generated
   */
  int INPUT_DECLARATION = 50;

  /**
   * The feature id for the '<em><b>Var1 List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT_DECLARATION__VAR1_LIST = 0;

  /**
   * The feature id for the '<em><b>Decl Specification</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT_DECLARATION__DECL_SPECIFICATION = 1;

  /**
   * The number of structural features of the '<em>Input Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT_DECLARATION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.EdgeDeclarationImpl <em>Edge Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.EdgeDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getEdgeDeclaration()
   * @generated
   */
  int EDGE_DECLARATION = 51;

  /**
   * The feature id for the '<em><b>Var1 List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EDGE_DECLARATION__VAR1_LIST = 0;

  /**
   * The number of structural features of the '<em>Edge Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EDGE_DECLARATION_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.DeclSpecificationImpl <em>Decl Specification</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.DeclSpecificationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getDeclSpecification()
   * @generated
   */
  int DECL_SPECIFICATION = 52;

  /**
   * The number of structural features of the '<em>Decl Specification</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DECL_SPECIFICATION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.VarDeclSpecificationImpl <em>Var Decl Specification</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.VarDeclSpecificationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getVarDeclSpecification()
   * @generated
   */
  int VAR_DECL_SPECIFICATION = 53;

  /**
   * The feature id for the '<em><b>Spec Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VAR_DECL_SPECIFICATION__SPEC_INIT = DECL_SPECIFICATION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Var Decl Specification</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VAR_DECL_SPECIFICATION_FEATURE_COUNT = DECL_SPECIFICATION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.EdgeDeclSpecificationImpl <em>Edge Decl Specification</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.EdgeDeclSpecificationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getEdgeDeclSpecification()
   * @generated
   */
  int EDGE_DECL_SPECIFICATION = 54;

  /**
   * The feature id for the '<em><b>REdge</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EDGE_DECL_SPECIFICATION__REDGE = DECL_SPECIFICATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>FEdge</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EDGE_DECL_SPECIFICATION__FEDGE = DECL_SPECIFICATION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Edge Decl Specification</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EDGE_DECL_SPECIFICATION_FEATURE_COUNT = DECL_SPECIFICATION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.Var1ListImpl <em>Var1 List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.Var1ListImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getVar1List()
   * @generated
   */
  int VAR1_LIST = 55;

  /**
   * The feature id for the '<em><b>Variables</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VAR1_LIST__VARIABLES = 0;

  /**
   * The number of structural features of the '<em>Var1 List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VAR1_LIST_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.OutputDeclarationsImpl <em>Output Declarations</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.OutputDeclarationsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getOutputDeclarations()
   * @generated
   */
  int OUTPUT_DECLARATIONS = 56;

  /**
   * The feature id for the '<em><b>Init Decls</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OUTPUT_DECLARATIONS__INIT_DECLS = IO_VAR_DECLARATIONS_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Output Declarations</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OUTPUT_DECLARATIONS_FEATURE_COUNT = IO_VAR_DECLARATIONS_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.VarInitDeclImpl <em>Var Init Decl</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.VarInitDeclImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getVarInitDecl()
   * @generated
   */
  int VAR_INIT_DECL = 57;

  /**
   * The feature id for the '<em><b>Var1 List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VAR_INIT_DECL__VAR1_LIST = 0;

  /**
   * The feature id for the '<em><b>Spec Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VAR_INIT_DECL__SPEC_INIT = 1;

  /**
   * The number of structural features of the '<em>Var Init Decl</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VAR_INIT_DECL_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.InputOutputDeclarationsImpl <em>Input Output Declarations</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.InputOutputDeclarationsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getInputOutputDeclarations()
   * @generated
   */
  int INPUT_OUTPUT_DECLARATIONS = 58;

  /**
   * The feature id for the '<em><b>Init Decls</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT_OUTPUT_DECLARATIONS__INIT_DECLS = IO_VAR_DECLARATIONS_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Input Output Declarations</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT_OUTPUT_DECLARATIONS_FEATURE_COUNT = IO_VAR_DECLARATIONS_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.FunctionBodyImpl <em>Function Body</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.FunctionBodyImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getFunctionBody()
   * @generated
   */
  int FUNCTION_BODY = 59;

  /**
   * The number of structural features of the '<em>Function Body</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_BODY_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.InstructionListImpl <em>Instruction List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.InstructionListImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getInstructionList()
   * @generated
   */
  int INSTRUCTION_LIST = 60;

  /**
   * The feature id for the '<em><b>Instructions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTRUCTION_LIST__INSTRUCTIONS = FUNCTION_BODY_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Instruction List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTRUCTION_LIST_FEATURE_COUNT = FUNCTION_BODY_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.InstructionImpl <em>Instruction</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.InstructionImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getInstruction()
   * @generated
   */
  int INSTRUCTION = 61;

  /**
   * The feature id for the '<em><b>Label</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTRUCTION__LABEL = 0;

  /**
   * The feature id for the '<em><b>Instruction</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTRUCTION__INSTRUCTION = 1;

  /**
   * The number of structural features of the '<em>Instruction</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTRUCTION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.LabelImpl <em>Label</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.LabelImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getLabel()
   * @generated
   */
  int LABEL = 62;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LABEL__NAME = 0;

  /**
   * The number of structural features of the '<em>Label</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LABEL_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.OperationImpl <em>Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.OperationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getOperation()
   * @generated
   */
  int OPERATION = 63;

  /**
   * The number of structural features of the '<em>Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.SimpleOperationImpl <em>Simple Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.SimpleOperationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getSimpleOperation()
   * @generated
   */
  int SIMPLE_OPERATION = 64;

  /**
   * The feature id for the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SIMPLE_OPERATION__OPERATOR = OPERATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SIMPLE_OPERATION__OPERAND = OPERATION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Simple Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SIMPLE_OPERATION_FEATURE_COUNT = OPERATION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ExpressionOperationImpl <em>Expression Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ExpressionOperationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getExpressionOperation()
   * @generated
   */
  int EXPRESSION_OPERATION = 65;

  /**
   * The feature id for the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_OPERATION__OPERATOR = OPERATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_OPERATION__OPERAND = OPERATION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Simple Instruction List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST = OPERATION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Expression Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_OPERATION_FEATURE_COUNT = OPERATION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.JmpOperationImpl <em>Jmp Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.JmpOperationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getJmpOperation()
   * @generated
   */
  int JMP_OPERATION = 66;

  /**
   * The feature id for the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JMP_OPERATION__OPERATOR = OPERATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Label</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JMP_OPERATION__LABEL = OPERATION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Jmp Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JMP_OPERATION_FEATURE_COUNT = OPERATION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.OperandImpl <em>Operand</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.OperandImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getOperand()
   * @generated
   */
  int OPERAND = 67;

  /**
   * The feature id for the '<em><b>Constant</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERAND__CONSTANT = 0;

  /**
   * The feature id for the '<em><b>Reference</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERAND__REFERENCE = 1;

  /**
   * The number of structural features of the '<em>Operand</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERAND_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.SimpleInstructionListImpl <em>Simple Instruction List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.SimpleInstructionListImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getSimpleInstructionList()
   * @generated
   */
  int SIMPLE_INSTRUCTION_LIST = 69;

  /**
   * The feature id for the '<em><b>Instructions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SIMPLE_INSTRUCTION_LIST__INSTRUCTIONS = 0;

  /**
   * The number of structural features of the '<em>Simple Instruction List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SIMPLE_INSTRUCTION_LIST_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.SimpleInstructionImpl <em>Simple Instruction</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.SimpleInstructionImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getSimpleInstruction()
   * @generated
   */
  int SIMPLE_INSTRUCTION = 70;

  /**
   * The number of structural features of the '<em>Simple Instruction</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SIMPLE_INSTRUCTION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.StatementListImpl <em>Statement List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.StatementListImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getStatementList()
   * @generated
   */
  int STATEMENT_LIST = 71;

  /**
   * The feature id for the '<em><b>Statements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATEMENT_LIST__STATEMENTS = FUNCTION_BODY_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Statement List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATEMENT_LIST_FEATURE_COUNT = FUNCTION_BODY_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.StatementImpl <em>Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.StatementImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getStatement()
   * @generated
   */
  int STATEMENT = 72;

  /**
   * The number of structural features of the '<em>Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATEMENT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.AssignStatementImpl <em>Assign Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.AssignStatementImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getAssignStatement()
   * @generated
   */
  int ASSIGN_STATEMENT = 73;

  /**
   * The feature id for the '<em><b>Variable</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ASSIGN_STATEMENT__VARIABLE = STATEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ASSIGN_STATEMENT__EXPRESSION = STATEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Assign Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ASSIGN_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.SelectionStatementImpl <em>Selection Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.SelectionStatementImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getSelectionStatement()
   * @generated
   */
  int SELECTION_STATEMENT = 74;

  /**
   * The feature id for the '<em><b>Else Statement List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECTION_STATEMENT__ELSE_STATEMENT_LIST = STATEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Selection Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECTION_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.IfStatementImpl <em>If Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.IfStatementImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getIfStatement()
   * @generated
   */
  int IF_STATEMENT = 75;

  /**
   * The feature id for the '<em><b>Else Statement List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IF_STATEMENT__ELSE_STATEMENT_LIST = SELECTION_STATEMENT__ELSE_STATEMENT_LIST;

  /**
   * The feature id for the '<em><b>If Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IF_STATEMENT__IF_EXPRESSION = SELECTION_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Then Statement List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IF_STATEMENT__THEN_STATEMENT_LIST = SELECTION_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Else Ifs</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IF_STATEMENT__ELSE_IFS = SELECTION_STATEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>If Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IF_STATEMENT_FEATURE_COUNT = SELECTION_STATEMENT_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ElseIfImpl <em>Else If</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ElseIfImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getElseIf()
   * @generated
   */
  int ELSE_IF = 76;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ELSE_IF__EXPRESSION = 0;

  /**
   * The feature id for the '<em><b>Statement List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ELSE_IF__STATEMENT_LIST = 1;

  /**
   * The number of structural features of the '<em>Else If</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ELSE_IF_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.CaseStatementImpl <em>Case Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.CaseStatementImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getCaseStatement()
   * @generated
   */
  int CASE_STATEMENT = 77;

  /**
   * The feature id for the '<em><b>Else Statement List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_STATEMENT__ELSE_STATEMENT_LIST = SELECTION_STATEMENT__ELSE_STATEMENT_LIST;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_STATEMENT__EXPRESSION = SELECTION_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Case Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_STATEMENT__CASE_ELEMENTS = SELECTION_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Case Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_STATEMENT_FEATURE_COUNT = SELECTION_STATEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.CaseElementImpl <em>Case Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.CaseElementImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getCaseElement()
   * @generated
   */
  int CASE_ELEMENT = 78;

  /**
   * The feature id for the '<em><b>Case List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_ELEMENT__CASE_LIST = 0;

  /**
   * The feature id for the '<em><b>Statement List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_ELEMENT__STATEMENT_LIST = 1;

  /**
   * The number of structural features of the '<em>Case Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_ELEMENT_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.CaseListImpl <em>Case List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.CaseListImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getCaseList()
   * @generated
   */
  int CASE_LIST = 79;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_LIST__ELEMENTS = 0;

  /**
   * The number of structural features of the '<em>Case List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_LIST_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.CaseListElementImpl <em>Case List Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.CaseListElementImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getCaseListElement()
   * @generated
   */
  int CASE_LIST_ELEMENT = 80;

  /**
   * The feature id for the '<em><b>Sub Range</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_LIST_ELEMENT__SUB_RANGE = 0;

  /**
   * The feature id for the '<em><b>Integer</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_LIST_ELEMENT__INTEGER = 1;

  /**
   * The feature id for the '<em><b>Enumerated Value</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_LIST_ELEMENT__ENUMERATED_VALUE = 2;

  /**
   * The number of structural features of the '<em>Case List Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASE_LIST_ELEMENT_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.FunctionBlockDeclarationImpl <em>Function Block Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.FunctionBlockDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getFunctionBlockDeclaration()
   * @generated
   */
  int FUNCTION_BLOCK_DECLARATION = 81;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_BLOCK_DECLARATION__NAME = LIBRARY_ELEMENT_DECLARATION__NAME;

  /**
   * The feature id for the '<em><b>Var Declarations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_BLOCK_DECLARATION__VAR_DECLARATIONS = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_BLOCK_DECLARATION__BODY = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Function Block Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_BLOCK_DECLARATION_FEATURE_COUNT = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.OtherVarDeclarationsImpl <em>Other Var Declarations</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.OtherVarDeclarationsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getOtherVarDeclarations()
   * @generated
   */
  int OTHER_VAR_DECLARATIONS = 83;

  /**
   * The number of structural features of the '<em>Other Var Declarations</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OTHER_VAR_DECLARATIONS_FEATURE_COUNT = FUNCTION_BLOCK_VAR_DECLARATIONS_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.VarDeclarationsImpl <em>Var Declarations</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.VarDeclarationsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getVarDeclarations()
   * @generated
   */
  int VAR_DECLARATIONS = 84;

  /**
   * The feature id for the '<em><b>Constant</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VAR_DECLARATIONS__CONSTANT = OTHER_VAR_DECLARATIONS_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Init Decls</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VAR_DECLARATIONS__INIT_DECLS = OTHER_VAR_DECLARATIONS_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Var Declarations</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VAR_DECLARATIONS_FEATURE_COUNT = OTHER_VAR_DECLARATIONS_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.FunctionBlockBodyImpl <em>Function Block Body</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.FunctionBlockBodyImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getFunctionBlockBody()
   * @generated
   */
  int FUNCTION_BLOCK_BODY = 85;

  /**
   * The number of structural features of the '<em>Function Block Body</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_BLOCK_BODY_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ProgramDeclarationImpl <em>Program Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ProgramDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgramDeclaration()
   * @generated
   */
  int PROGRAM_DECLARATION = 86;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_DECLARATION__NAME = LIBRARY_ELEMENT_DECLARATION__NAME;

  /**
   * The feature id for the '<em><b>Var Declarations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_DECLARATION__VAR_DECLARATIONS = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_DECLARATION__BODY = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Program Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_DECLARATION_FEATURE_COUNT = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ProgramVarDeclarationsImpl <em>Program Var Declarations</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ProgramVarDeclarationsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgramVarDeclarations()
   * @generated
   */
  int PROGRAM_VAR_DECLARATIONS = 87;

  /**
   * The number of structural features of the '<em>Program Var Declarations</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_VAR_DECLARATIONS_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.LocatedVarDeclarationsImpl <em>Located Var Declarations</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.LocatedVarDeclarationsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getLocatedVarDeclarations()
   * @generated
   */
  int LOCATED_VAR_DECLARATIONS = 88;

  /**
   * The feature id for the '<em><b>Located Var Declaration</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATED_VAR_DECLARATIONS__LOCATED_VAR_DECLARATION = PROGRAM_VAR_DECLARATIONS_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Located Var Declarations</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATED_VAR_DECLARATIONS_FEATURE_COUNT = PROGRAM_VAR_DECLARATIONS_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.LocatedVarDeclarationImpl <em>Located Var Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.LocatedVarDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getLocatedVarDeclaration()
   * @generated
   */
  int LOCATED_VAR_DECLARATION = 89;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATED_VAR_DECLARATION__NAME = 0;

  /**
   * The feature id for the '<em><b>Location</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATED_VAR_DECLARATION__LOCATION = 1;

  /**
   * The feature id for the '<em><b>Spec Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATED_VAR_DECLARATION__SPEC_INIT = 2;

  /**
   * The number of structural features of the '<em>Located Var Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATED_VAR_DECLARATION_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.LocationImpl <em>Location</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.LocationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getLocation()
   * @generated
   */
  int LOCATION = 90;

  /**
   * The feature id for the '<em><b>Direct Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATION__DIRECT_VARIABLE = 0;

  /**
   * The number of structural features of the '<em>Location</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCATION_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ProgramAccessDeclsImpl <em>Program Access Decls</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ProgramAccessDeclsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgramAccessDecls()
   * @generated
   */
  int PROGRAM_ACCESS_DECLS = 91;

  /**
   * The feature id for the '<em><b>Program Access Decl</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_ACCESS_DECLS__PROGRAM_ACCESS_DECL = PROGRAM_VAR_DECLARATIONS_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Program Access Decls</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_ACCESS_DECLS_FEATURE_COUNT = PROGRAM_VAR_DECLARATIONS_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ProgramAccessDeclImpl <em>Program Access Decl</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ProgramAccessDeclImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgramAccessDecl()
   * @generated
   */
  int PROGRAM_ACCESS_DECL = 92;

  /**
   * The feature id for the '<em><b>Access Name</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_ACCESS_DECL__ACCESS_NAME = 0;

  /**
   * The feature id for the '<em><b>Symbolic Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE = 1;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_ACCESS_DECL__TYPE_NAME = 2;

  /**
   * The feature id for the '<em><b>Direction</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_ACCESS_DECL__DIRECTION = 3;

  /**
   * The number of structural features of the '<em>Program Access Decl</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_ACCESS_DECL_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ConfigurationDeclarationImpl <em>Configuration Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ConfigurationDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getConfigurationDeclaration()
   * @generated
   */
  int CONFIGURATION_DECLARATION = 93;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIGURATION_DECLARATION__NAME = LIBRARY_ELEMENT_DECLARATION__NAME;

  /**
   * The feature id for the '<em><b>Global Var Declarations</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Resdecl</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIGURATION_DECLARATION__RESDECL = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Configuration Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIGURATION_DECLARATION_FEATURE_COUNT = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ResourceDeclarationImpl <em>Resource Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ResourceDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getResourceDeclaration()
   * @generated
   */
  int RESOURCE_DECLARATION = 94;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_DECLARATION__NAME = LIBRARY_ELEMENT_DECLARATION__NAME;

  /**
   * The feature id for the '<em><b>Resname</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_DECLARATION__RESNAME = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Global Var Declarations</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Singleresource</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_DECLARATION__SINGLERESOURCE = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Resource Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_DECLARATION_FEATURE_COUNT = LIBRARY_ELEMENT_DECLARATION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.SingleResourceDeclarationImpl <em>Single Resource Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.SingleResourceDeclarationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getSingleResourceDeclaration()
   * @generated
   */
  int SINGLE_RESOURCE_DECLARATION = 95;

  /**
   * The feature id for the '<em><b>Task Conf</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SINGLE_RESOURCE_DECLARATION__TASK_CONF = 0;

  /**
   * The feature id for the '<em><b>Program Conf</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SINGLE_RESOURCE_DECLARATION__PROGRAM_CONF = 1;

  /**
   * The number of structural features of the '<em>Single Resource Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SINGLE_RESOURCE_DECLARATION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ProgramConfigurationImpl <em>Program Configuration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ProgramConfigurationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgramConfiguration()
   * @generated
   */
  int PROGRAM_CONFIGURATION = 96;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_CONFIGURATION__NAME = 0;

  /**
   * The feature id for the '<em><b>Task</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_CONFIGURATION__TASK = 1;

  /**
   * The feature id for the '<em><b>Prog</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_CONFIGURATION__PROG = 2;

  /**
   * The feature id for the '<em><b>Prog Conf</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_CONFIGURATION__PROG_CONF = 3;

  /**
   * The number of structural features of the '<em>Program Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROGRAM_CONFIGURATION_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ProgConfElementsImpl <em>Prog Conf Elements</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ProgConfElementsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgConfElements()
   * @generated
   */
  int PROG_CONF_ELEMENTS = 97;

  /**
   * The feature id for the '<em><b>Progconf</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROG_CONF_ELEMENTS__PROGCONF = 0;

  /**
   * The number of structural features of the '<em>Prog Conf Elements</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROG_CONF_ELEMENTS_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ProgConfElementImpl <em>Prog Conf Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ProgConfElementImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgConfElement()
   * @generated
   */
  int PROG_CONF_ELEMENT = 98;

  /**
   * The number of structural features of the '<em>Prog Conf Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROG_CONF_ELEMENT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.FBTaskImpl <em>FB Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.FBTaskImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getFBTask()
   * @generated
   */
  int FB_TASK = 99;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FB_TASK__FBNAME = PROG_CONF_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Task</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FB_TASK__TASK = PROG_CONF_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>FB Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FB_TASK_FEATURE_COUNT = PROG_CONF_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ProgCNXNImpl <em>Prog CNXN</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ProgCNXNImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgCNXN()
   * @generated
   */
  int PROG_CNXN = 100;

  /**
   * The feature id for the '<em><b>Variablename</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROG_CNXN__VARIABLENAME = PROG_CONF_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Progd</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROG_CNXN__PROGD = PROG_CONF_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Prog CNXN</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROG_CNXN_FEATURE_COUNT = PROG_CONF_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.DataSinkImpl <em>Data Sink</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.DataSinkImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getDataSink()
   * @generated
   */
  int DATA_SINK = 101;

  /**
   * The feature id for the '<em><b>Globvar</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATA_SINK__GLOBVAR = 0;

  /**
   * The feature id for the '<em><b>Dirvar</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATA_SINK__DIRVAR = 1;

  /**
   * The number of structural features of the '<em>Data Sink</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATA_SINK_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ProgDataSourceImpl <em>Prog Data Source</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ProgDataSourceImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgDataSource()
   * @generated
   */
  int PROG_DATA_SOURCE = 102;

  /**
   * The number of structural features of the '<em>Prog Data Source</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROG_DATA_SOURCE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.GlobalVarDeclarationsImpl <em>Global Var Declarations</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.GlobalVarDeclarationsImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getGlobalVarDeclarations()
   * @generated
   */
  int GLOBAL_VAR_DECLARATIONS = 103;

  /**
   * The feature id for the '<em><b>Global Var Decl</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_DECLARATIONS__GLOBAL_VAR_DECL = 0;

  /**
   * The number of structural features of the '<em>Global Var Declarations</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_DECLARATIONS_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.GlobalVarDeclImpl <em>Global Var Decl</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.GlobalVarDeclImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getGlobalVarDecl()
   * @generated
   */
  int GLOBAL_VAR_DECL = 104;

  /**
   * The feature id for the '<em><b>Spec</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_DECL__SPEC = 0;

  /**
   * The feature id for the '<em><b>Spec Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_DECL__SPEC_INIT = 1;

  /**
   * The number of structural features of the '<em>Global Var Decl</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_DECL_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.GlobalVarSpecImpl <em>Global Var Spec</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.GlobalVarSpecImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getGlobalVarSpec()
   * @generated
   */
  int GLOBAL_VAR_SPEC = 105;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_SPEC__VARIABLE = 0;

  /**
   * The feature id for the '<em><b>Location</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_SPEC__LOCATION = 1;

  /**
   * The number of structural features of the '<em>Global Var Spec</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_SPEC_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.GlobalVarListImpl <em>Global Var List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.GlobalVarListImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getGlobalVarList()
   * @generated
   */
  int GLOBAL_VAR_LIST = 106;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_LIST__VARIABLE = GLOBAL_VAR_SPEC__VARIABLE;

  /**
   * The feature id for the '<em><b>Location</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_LIST__LOCATION = GLOBAL_VAR_SPEC__LOCATION;

  /**
   * The feature id for the '<em><b>Variables</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_LIST__VARIABLES = GLOBAL_VAR_SPEC_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Global Var List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_LIST_FEATURE_COUNT = GLOBAL_VAR_SPEC_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.GlobalVarImpl <em>Global Var</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.GlobalVarImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getGlobalVar()
   * @generated
   */
  int GLOBAL_VAR = 107;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR__NAME = 0;

  /**
   * The number of structural features of the '<em>Global Var</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GLOBAL_VAR_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.TaskConfigurationImpl <em>Task Configuration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.TaskConfigurationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getTaskConfiguration()
   * @generated
   */
  int TASK_CONFIGURATION = 108;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TASK_CONFIGURATION__NAME = 0;

  /**
   * The feature id for the '<em><b>Task Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TASK_CONFIGURATION__TASK_INIT = 1;

  /**
   * The number of structural features of the '<em>Task Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TASK_CONFIGURATION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.TaskInitializationImpl <em>Task Initialization</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.TaskInitializationImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getTaskInitialization()
   * @generated
   */
  int TASK_INITIALIZATION = 109;

  /**
   * The feature id for the '<em><b>Single</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TASK_INITIALIZATION__SINGLE = 0;

  /**
   * The feature id for the '<em><b>Interval</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TASK_INITIALIZATION__INTERVAL = 1;

  /**
   * The feature id for the '<em><b>Prior</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TASK_INITIALIZATION__PRIOR = 2;

  /**
   * The number of structural features of the '<em>Task Initialization</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TASK_INITIALIZATION_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.DataSourceImpl <em>Data Source</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.DataSourceImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getDataSource()
   * @generated
   */
  int DATA_SOURCE = 110;

  /**
   * The number of structural features of the '<em>Data Source</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATA_SOURCE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.ConstantImpl <em>Constant</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.ConstantImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getConstant()
   * @generated
   */
  int CONSTANT = 111;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTANT__VARIABLE = INITIAL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTANT__FBNAME = INITIAL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTANT__OPENBR = INITIAL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTANT__PARAMASSIGNMENT = INITIAL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTANT__CLOSEBR = INITIAL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTANT__EXPRESSION = INITIAL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>Constant</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTANT_FEATURE_COUNT = INITIAL_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.NumericLiteralImpl <em>Numeric Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.NumericLiteralImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getNumericLiteral()
   * @generated
   */
  int NUMERIC_LITERAL = 112;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_LITERAL__VARIABLE = CONSTANT__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_LITERAL__FBNAME = CONSTANT__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_LITERAL__OPENBR = CONSTANT__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_LITERAL__PARAMASSIGNMENT = CONSTANT__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_LITERAL__CLOSEBR = CONSTANT__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_LITERAL__EXPRESSION = CONSTANT__EXPRESSION;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_LITERAL__VALUE = CONSTANT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Numeric Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_LITERAL_FEATURE_COUNT = CONSTANT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.IntegerLiteralImpl <em>Integer Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.IntegerLiteralImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getIntegerLiteral()
   * @generated
   */
  int INTEGER_LITERAL = 113;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTEGER_LITERAL__VARIABLE = NUMERIC_LITERAL__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTEGER_LITERAL__FBNAME = NUMERIC_LITERAL__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTEGER_LITERAL__OPENBR = NUMERIC_LITERAL__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTEGER_LITERAL__PARAMASSIGNMENT = NUMERIC_LITERAL__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTEGER_LITERAL__CLOSEBR = NUMERIC_LITERAL__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTEGER_LITERAL__EXPRESSION = NUMERIC_LITERAL__EXPRESSION;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTEGER_LITERAL__VALUE = NUMERIC_LITERAL__VALUE;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTEGER_LITERAL__TYPE = NUMERIC_LITERAL_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Integer Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTEGER_LITERAL_FEATURE_COUNT = NUMERIC_LITERAL_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.RealLiteralImpl <em>Real Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.RealLiteralImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getRealLiteral()
   * @generated
   */
  int REAL_LITERAL = 114;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REAL_LITERAL__VARIABLE = NUMERIC_LITERAL__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REAL_LITERAL__FBNAME = NUMERIC_LITERAL__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REAL_LITERAL__OPENBR = NUMERIC_LITERAL__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REAL_LITERAL__PARAMASSIGNMENT = NUMERIC_LITERAL__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REAL_LITERAL__CLOSEBR = NUMERIC_LITERAL__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REAL_LITERAL__EXPRESSION = NUMERIC_LITERAL__EXPRESSION;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REAL_LITERAL__VALUE = NUMERIC_LITERAL__VALUE;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REAL_LITERAL__TYPE = NUMERIC_LITERAL_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Real Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REAL_LITERAL_FEATURE_COUNT = NUMERIC_LITERAL_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.CharacterStringImpl <em>Character String</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.CharacterStringImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getCharacterString()
   * @generated
   */
  int CHARACTER_STRING = 115;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CHARACTER_STRING__VARIABLE = CONSTANT__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CHARACTER_STRING__FBNAME = CONSTANT__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CHARACTER_STRING__OPENBR = CONSTANT__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CHARACTER_STRING__PARAMASSIGNMENT = CONSTANT__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CHARACTER_STRING__CLOSEBR = CONSTANT__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CHARACTER_STRING__EXPRESSION = CONSTANT__EXPRESSION;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CHARACTER_STRING__VALUE = CONSTANT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Character String</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CHARACTER_STRING_FEATURE_COUNT = CONSTANT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.BitStringImpl <em>Bit String</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.BitStringImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getBitString()
   * @generated
   */
  int BIT_STRING = 116;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BIT_STRING__VARIABLE = CONSTANT__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BIT_STRING__FBNAME = CONSTANT__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BIT_STRING__OPENBR = CONSTANT__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BIT_STRING__PARAMASSIGNMENT = CONSTANT__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BIT_STRING__CLOSEBR = CONSTANT__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BIT_STRING__EXPRESSION = CONSTANT__EXPRESSION;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BIT_STRING__VALUE = CONSTANT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Bit String</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BIT_STRING_FEATURE_COUNT = CONSTANT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.BooleanImpl <em>Boolean</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.BooleanImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getBoolean()
   * @generated
   */
  int BOOLEAN = 117;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN__VARIABLE = CONSTANT__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN__FBNAME = CONSTANT__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN__OPENBR = CONSTANT__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN__PARAMASSIGNMENT = CONSTANT__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN__CLOSEBR = CONSTANT__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN__EXPRESSION = CONSTANT__EXPRESSION;

  /**
   * The feature id for the '<em><b>Bool Int</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN__BOOL_INT = CONSTANT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>True</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN__TRUE = CONSTANT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Boolean</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN_FEATURE_COUNT = CONSTANT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.TimeLiteralImpl <em>Time Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.TimeLiteralImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getTimeLiteral()
   * @generated
   */
  int TIME_LITERAL = 118;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_LITERAL__VARIABLE = CONSTANT__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_LITERAL__FBNAME = CONSTANT__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_LITERAL__OPENBR = CONSTANT__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_LITERAL__PARAMASSIGNMENT = CONSTANT__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_LITERAL__CLOSEBR = CONSTANT__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_LITERAL__EXPRESSION = CONSTANT__EXPRESSION;

  /**
   * The number of structural features of the '<em>Time Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_LITERAL_FEATURE_COUNT = CONSTANT_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.DurationLiteralImpl <em>Duration Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.DurationLiteralImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getDurationLiteral()
   * @generated
   */
  int DURATION_LITERAL = 119;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DURATION_LITERAL__VARIABLE = TIME_LITERAL__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DURATION_LITERAL__FBNAME = TIME_LITERAL__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DURATION_LITERAL__OPENBR = TIME_LITERAL__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DURATION_LITERAL__PARAMASSIGNMENT = TIME_LITERAL__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DURATION_LITERAL__CLOSEBR = TIME_LITERAL__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DURATION_LITERAL__EXPRESSION = TIME_LITERAL__EXPRESSION;

  /**
   * The feature id for the '<em><b>Duration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DURATION_LITERAL__DURATION = TIME_LITERAL_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Duration Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DURATION_LITERAL_FEATURE_COUNT = TIME_LITERAL_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.TimeOfDayLiteralImpl <em>Time Of Day Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.TimeOfDayLiteralImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getTimeOfDayLiteral()
   * @generated
   */
  int TIME_OF_DAY_LITERAL = 120;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_OF_DAY_LITERAL__VARIABLE = TIME_LITERAL__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_OF_DAY_LITERAL__FBNAME = TIME_LITERAL__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_OF_DAY_LITERAL__OPENBR = TIME_LITERAL__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_OF_DAY_LITERAL__PARAMASSIGNMENT = TIME_LITERAL__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_OF_DAY_LITERAL__CLOSEBR = TIME_LITERAL__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_OF_DAY_LITERAL__EXPRESSION = TIME_LITERAL__EXPRESSION;

  /**
   * The feature id for the '<em><b>Hour</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_OF_DAY_LITERAL__HOUR = TIME_LITERAL_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Minute</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_OF_DAY_LITERAL__MINUTE = TIME_LITERAL_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Second</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_OF_DAY_LITERAL__SECOND = TIME_LITERAL_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Time Of Day Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_OF_DAY_LITERAL_FEATURE_COUNT = TIME_LITERAL_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.DateLiteralImpl <em>Date Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.DateLiteralImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getDateLiteral()
   * @generated
   */
  int DATE_LITERAL = 121;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_LITERAL__VARIABLE = TIME_LITERAL__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_LITERAL__FBNAME = TIME_LITERAL__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_LITERAL__OPENBR = TIME_LITERAL__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_LITERAL__PARAMASSIGNMENT = TIME_LITERAL__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_LITERAL__CLOSEBR = TIME_LITERAL__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_LITERAL__EXPRESSION = TIME_LITERAL__EXPRESSION;

  /**
   * The feature id for the '<em><b>Year</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_LITERAL__YEAR = TIME_LITERAL_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Month</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_LITERAL__MONTH = TIME_LITERAL_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Day</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_LITERAL__DAY = TIME_LITERAL_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Date Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_LITERAL_FEATURE_COUNT = TIME_LITERAL_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.DateAndTimeLiteralImpl <em>Date And Time Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.DateAndTimeLiteralImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getDateAndTimeLiteral()
   * @generated
   */
  int DATE_AND_TIME_LITERAL = 122;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__VARIABLE = TIME_LITERAL__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__FBNAME = TIME_LITERAL__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__OPENBR = TIME_LITERAL__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__PARAMASSIGNMENT = TIME_LITERAL__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__CLOSEBR = TIME_LITERAL__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__EXPRESSION = TIME_LITERAL__EXPRESSION;

  /**
   * The feature id for the '<em><b>Year</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__YEAR = TIME_LITERAL_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Month</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__MONTH = TIME_LITERAL_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Day</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__DAY = TIME_LITERAL_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Hour</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__HOUR = TIME_LITERAL_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Minute</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__MINUTE = TIME_LITERAL_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Second</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL__SECOND = TIME_LITERAL_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>Date And Time Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_AND_TIME_LITERAL_FEATURE_COUNT = TIME_LITERAL_FEATURE_COUNT + 6;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.BinaryExpressionImpl <em>Binary Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.BinaryExpressionImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getBinaryExpression()
   * @generated
   */
  int BINARY_EXPRESSION = 123;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__VARIABLE = EXPRESSION__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__FBNAME = EXPRESSION__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__OPENBR = EXPRESSION__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__PARAMASSIGNMENT = EXPRESSION__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__CLOSEBR = EXPRESSION__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__EXPRESSION = EXPRESSION__EXPRESSION;

  /**
   * The feature id for the '<em><b>Left</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__LEFT = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__OPERATOR = EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__RIGHT = EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Binary Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.bichler.iec.iec.impl.UnaryExpressionImpl <em>Unary Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.bichler.iec.iec.impl.UnaryExpressionImpl
   * @see com.bichler.iec.iec.impl.IecPackageImpl#getUnaryExpression()
   * @generated
   */
  int UNARY_EXPRESSION = 124;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNARY_EXPRESSION__VARIABLE = EXPRESSION__VARIABLE;

  /**
   * The feature id for the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNARY_EXPRESSION__FBNAME = EXPRESSION__FBNAME;

  /**
   * The feature id for the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNARY_EXPRESSION__OPENBR = EXPRESSION__OPENBR;

  /**
   * The feature id for the '<em><b>Paramassignment</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNARY_EXPRESSION__PARAMASSIGNMENT = EXPRESSION__PARAMASSIGNMENT;

  /**
   * The feature id for the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNARY_EXPRESSION__CLOSEBR = EXPRESSION__CLOSEBR;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNARY_EXPRESSION__EXPRESSION = EXPRESSION__EXPRESSION;

  /**
   * The feature id for the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNARY_EXPRESSION__OPERATOR = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Unary Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNARY_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;


  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Model <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model</em>'.
   * @see com.bichler.iec.iec.Model
   * @generated
   */
  EClass getModel();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.Model#getModelElement <em>Model Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Model Element</em>'.
   * @see com.bichler.iec.iec.Model#getModelElement()
   * @see #getModel()
   * @generated
   */
  EReference getModel_ModelElement();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ModelElement <em>Model Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model Element</em>'.
   * @see com.bichler.iec.iec.ModelElement
   * @generated
   */
  EClass getModelElement();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.LibraryElement <em>Library Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Library Element</em>'.
   * @see com.bichler.iec.iec.LibraryElement
   * @generated
   */
  EClass getLibraryElement();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.LibraryElementDeclaration <em>Library Element Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Library Element Declaration</em>'.
   * @see com.bichler.iec.iec.LibraryElementDeclaration
   * @generated
   */
  EClass getLibraryElementDeclaration();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.LibraryElementDeclaration#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.LibraryElementDeclaration#getName()
   * @see #getLibraryElementDeclaration()
   * @generated
   */
  EAttribute getLibraryElementDeclaration_Name();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.DataType <em>Data Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Data Type</em>'.
   * @see com.bichler.iec.iec.DataType
   * @generated
   */
  EClass getDataType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.NonGenericType <em>Non Generic Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Non Generic Type</em>'.
   * @see com.bichler.iec.iec.NonGenericType
   * @generated
   */
  EClass getNonGenericType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ElementaryType <em>Elementary Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Elementary Type</em>'.
   * @see com.bichler.iec.iec.ElementaryType
   * @generated
   */
  EClass getElementaryType();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.ElementaryType#getTypeName <em>Type Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type Name</em>'.
   * @see com.bichler.iec.iec.ElementaryType#getTypeName()
   * @see #getElementaryType()
   * @generated
   */
  EAttribute getElementaryType_TypeName();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.StringType <em>String Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>String Type</em>'.
   * @see com.bichler.iec.iec.StringType
   * @generated
   */
  EClass getStringType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.NumericType <em>Numeric Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Numeric Type</em>'.
   * @see com.bichler.iec.iec.NumericType
   * @generated
   */
  EClass getNumericType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.IntegerType <em>Integer Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Integer Type</em>'.
   * @see com.bichler.iec.iec.IntegerType
   * @generated
   */
  EClass getIntegerType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.SignedIntegerType <em>Signed Integer Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Signed Integer Type</em>'.
   * @see com.bichler.iec.iec.SignedIntegerType
   * @generated
   */
  EClass getSignedIntegerType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.PlainIntegerType <em>Plain Integer Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Plain Integer Type</em>'.
   * @see com.bichler.iec.iec.PlainIntegerType
   * @generated
   */
  EClass getPlainIntegerType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.UnsignedIntegerType <em>Unsigned Integer Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Unsigned Integer Type</em>'.
   * @see com.bichler.iec.iec.UnsignedIntegerType
   * @generated
   */
  EClass getUnsignedIntegerType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.RealType <em>Real Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Real Type</em>'.
   * @see com.bichler.iec.iec.RealType
   * @generated
   */
  EClass getRealType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.DateType <em>Date Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Date Type</em>'.
   * @see com.bichler.iec.iec.DateType
   * @generated
   */
  EClass getDateType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.BitStringType <em>Bit String Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Bit String Type</em>'.
   * @see com.bichler.iec.iec.BitStringType
   * @generated
   */
  EClass getBitStringType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.GenericType <em>Generic Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Generic Type</em>'.
   * @see com.bichler.iec.iec.GenericType
   * @generated
   */
  EClass getGenericType();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.GenericType#getTypeName <em>Type Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type Name</em>'.
   * @see com.bichler.iec.iec.GenericType#getTypeName()
   * @see #getGenericType()
   * @generated
   */
  EAttribute getGenericType_TypeName();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.DerivedType <em>Derived Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Derived Type</em>'.
   * @see com.bichler.iec.iec.DerivedType
   * @generated
   */
  EClass getDerivedType();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DerivedType#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.DerivedType#getName()
   * @see #getDerivedType()
   * @generated
   */
  EAttribute getDerivedType_Name();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.DataTypeDeclaration <em>Data Type Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Data Type Declaration</em>'.
   * @see com.bichler.iec.iec.DataTypeDeclaration
   * @generated
   */
  EClass getDataTypeDeclaration();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.DataTypeDeclaration#getTypeDeclaration <em>Type Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Type Declaration</em>'.
   * @see com.bichler.iec.iec.DataTypeDeclaration#getTypeDeclaration()
   * @see #getDataTypeDeclaration()
   * @generated
   */
  EReference getDataTypeDeclaration_TypeDeclaration();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.TypeDeclaration <em>Type Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Type Declaration</em>'.
   * @see com.bichler.iec.iec.TypeDeclaration
   * @generated
   */
  EClass getTypeDeclaration();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.TypeDeclaration#getDerivedType <em>Derived Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Derived Type</em>'.
   * @see com.bichler.iec.iec.TypeDeclaration#getDerivedType()
   * @see #getTypeDeclaration()
   * @generated
   */
  EReference getTypeDeclaration_DerivedType();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.SimpleTypeDeclaration <em>Simple Type Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Simple Type Declaration</em>'.
   * @see com.bichler.iec.iec.SimpleTypeDeclaration
   * @generated
   */
  EClass getSimpleTypeDeclaration();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.SimpleTypeDeclaration#getSpecInit <em>Spec Init</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Spec Init</em>'.
   * @see com.bichler.iec.iec.SimpleTypeDeclaration#getSpecInit()
   * @see #getSimpleTypeDeclaration()
   * @generated
   */
  EReference getSimpleTypeDeclaration_SpecInit();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.SpecInit <em>Spec Init</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Spec Init</em>'.
   * @see com.bichler.iec.iec.SpecInit
   * @generated
   */
  EClass getSpecInit();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.SpecInit#getBaseType <em>Base Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Base Type</em>'.
   * @see com.bichler.iec.iec.SpecInit#getBaseType()
   * @see #getSpecInit()
   * @generated
   */
  EReference getSpecInit_BaseType();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.SpecInit#getConstant <em>Constant</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Constant</em>'.
   * @see com.bichler.iec.iec.SpecInit#getConstant()
   * @see #getSpecInit()
   * @generated
   */
  EReference getSpecInit_Constant();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.RangeDeclaration <em>Range Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Range Declaration</em>'.
   * @see com.bichler.iec.iec.RangeDeclaration
   * @generated
   */
  EClass getRangeDeclaration();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.RangeDeclaration#getBaseType <em>Base Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Base Type</em>'.
   * @see com.bichler.iec.iec.RangeDeclaration#getBaseType()
   * @see #getRangeDeclaration()
   * @generated
   */
  EReference getRangeDeclaration_BaseType();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.RangeDeclaration#getRange <em>Range</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Range</em>'.
   * @see com.bichler.iec.iec.RangeDeclaration#getRange()
   * @see #getRangeDeclaration()
   * @generated
   */
  EAttribute getRangeDeclaration_Range();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.RangeDeclaration#getConstant <em>Constant</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Constant</em>'.
   * @see com.bichler.iec.iec.RangeDeclaration#getConstant()
   * @see #getRangeDeclaration()
   * @generated
   */
  EAttribute getRangeDeclaration_Constant();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.EnumDeclaration <em>Enum Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Enum Declaration</em>'.
   * @see com.bichler.iec.iec.EnumDeclaration
   * @generated
   */
  EClass getEnumDeclaration();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.EnumDeclaration#getEnumeration <em>Enumeration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Enumeration</em>'.
   * @see com.bichler.iec.iec.EnumDeclaration#getEnumeration()
   * @see #getEnumDeclaration()
   * @generated
   */
  EReference getEnumDeclaration_Enumeration();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.EnumDeclaration#getConstant <em>Constant</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Constant</em>'.
   * @see com.bichler.iec.iec.EnumDeclaration#getConstant()
   * @see #getEnumDeclaration()
   * @generated
   */
  EReference getEnumDeclaration_Constant();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ArrayDeclaration <em>Array Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Array Declaration</em>'.
   * @see com.bichler.iec.iec.ArrayDeclaration
   * @generated
   */
  EClass getArrayDeclaration();

  /**
   * Returns the meta object for the attribute list '{@link com.bichler.iec.iec.ArrayDeclaration#getRanges <em>Ranges</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Ranges</em>'.
   * @see com.bichler.iec.iec.ArrayDeclaration#getRanges()
   * @see #getArrayDeclaration()
   * @generated
   */
  EAttribute getArrayDeclaration_Ranges();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ArrayDeclaration#getBaseType <em>Base Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Base Type</em>'.
   * @see com.bichler.iec.iec.ArrayDeclaration#getBaseType()
   * @see #getArrayDeclaration()
   * @generated
   */
  EReference getArrayDeclaration_BaseType();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ArrayDeclaration#getConstant <em>Constant</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Constant</em>'.
   * @see com.bichler.iec.iec.ArrayDeclaration#getConstant()
   * @see #getArrayDeclaration()
   * @generated
   */
  EReference getArrayDeclaration_Constant();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Enumeration <em>Enumeration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Enumeration</em>'.
   * @see com.bichler.iec.iec.Enumeration
   * @generated
   */
  EClass getEnumeration();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.Enumeration#getValues <em>Values</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Values</em>'.
   * @see com.bichler.iec.iec.Enumeration#getValues()
   * @see #getEnumeration()
   * @generated
   */
  EReference getEnumeration_Values();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.EnumeratedValue <em>Enumerated Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Enumerated Value</em>'.
   * @see com.bichler.iec.iec.EnumeratedValue
   * @generated
   */
  EClass getEnumeratedValue();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.EnumeratedValue#getDerivedType <em>Derived Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Derived Type</em>'.
   * @see com.bichler.iec.iec.EnumeratedValue#getDerivedType()
   * @see #getEnumeratedValue()
   * @generated
   */
  EReference getEnumeratedValue_DerivedType();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.EnumeratedValue#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.EnumeratedValue#getName()
   * @see #getEnumeratedValue()
   * @generated
   */
  EAttribute getEnumeratedValue_Name();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ArrayInitialization <em>Array Initialization</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Array Initialization</em>'.
   * @see com.bichler.iec.iec.ArrayInitialization
   * @generated
   */
  EClass getArrayInitialization();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.ArrayInitialization#getInitialElements <em>Initial Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Initial Elements</em>'.
   * @see com.bichler.iec.iec.ArrayInitialization#getInitialElements()
   * @see #getArrayInitialization()
   * @generated
   */
  EReference getArrayInitialization_InitialElements();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ArrayInitialElements <em>Array Initial Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Array Initial Elements</em>'.
   * @see com.bichler.iec.iec.ArrayInitialElements
   * @generated
   */
  EClass getArrayInitialElements();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ArrayInitialElements#getInitialElement <em>Initial Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Initial Element</em>'.
   * @see com.bichler.iec.iec.ArrayInitialElements#getInitialElement()
   * @see #getArrayInitialElements()
   * @generated
   */
  EReference getArrayInitialElements_InitialElement();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.ArrayInitialElements#getIndex <em>Index</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Index</em>'.
   * @see com.bichler.iec.iec.ArrayInitialElements#getIndex()
   * @see #getArrayInitialElements()
   * @generated
   */
  EAttribute getArrayInitialElements_Index();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.InitialElement <em>Initial Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Initial Element</em>'.
   * @see com.bichler.iec.iec.InitialElement
   * @generated
   */
  EClass getInitialElement();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.StructureTypeDeclaration <em>Structure Type Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Structure Type Declaration</em>'.
   * @see com.bichler.iec.iec.StructureTypeDeclaration
   * @generated
   */
  EClass getStructureTypeDeclaration();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.StructureTypeDeclaration#getDeclaration <em>Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Declaration</em>'.
   * @see com.bichler.iec.iec.StructureTypeDeclaration#getDeclaration()
   * @see #getStructureTypeDeclaration()
   * @generated
   */
  EReference getStructureTypeDeclaration_Declaration();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.StructureTypeDeclaration#getInitialization <em>Initialization</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Initialization</em>'.
   * @see com.bichler.iec.iec.StructureTypeDeclaration#getInitialization()
   * @see #getStructureTypeDeclaration()
   * @generated
   */
  EReference getStructureTypeDeclaration_Initialization();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.StructureDeclaration <em>Structure Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Structure Declaration</em>'.
   * @see com.bichler.iec.iec.StructureDeclaration
   * @generated
   */
  EClass getStructureDeclaration();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.StructureDeclaration#getStructureElement <em>Structure Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Structure Element</em>'.
   * @see com.bichler.iec.iec.StructureDeclaration#getStructureElement()
   * @see #getStructureDeclaration()
   * @generated
   */
  EReference getStructureDeclaration_StructureElement();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.StructureElementDeclaration <em>Structure Element Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Structure Element Declaration</em>'.
   * @see com.bichler.iec.iec.StructureElementDeclaration
   * @generated
   */
  EClass getStructureElementDeclaration();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.StructureElementDeclaration#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.StructureElementDeclaration#getName()
   * @see #getStructureElementDeclaration()
   * @generated
   */
  EAttribute getStructureElementDeclaration_Name();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.StructureElementDeclaration#getSpecInit <em>Spec Init</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Spec Init</em>'.
   * @see com.bichler.iec.iec.StructureElementDeclaration#getSpecInit()
   * @see #getStructureElementDeclaration()
   * @generated
   */
  EReference getStructureElementDeclaration_SpecInit();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.InitializedStructure <em>Initialized Structure</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Initialized Structure</em>'.
   * @see com.bichler.iec.iec.InitializedStructure
   * @generated
   */
  EClass getInitializedStructure();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.InitializedStructure#getDerivedType <em>Derived Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Derived Type</em>'.
   * @see com.bichler.iec.iec.InitializedStructure#getDerivedType()
   * @see #getInitializedStructure()
   * @generated
   */
  EReference getInitializedStructure_DerivedType();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.InitializedStructure#getInitialization <em>Initialization</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Initialization</em>'.
   * @see com.bichler.iec.iec.InitializedStructure#getInitialization()
   * @see #getInitializedStructure()
   * @generated
   */
  EReference getInitializedStructure_Initialization();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.StructureInitialization <em>Structure Initialization</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Structure Initialization</em>'.
   * @see com.bichler.iec.iec.StructureInitialization
   * @generated
   */
  EClass getStructureInitialization();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.StructureInitialization#getInitialElements <em>Initial Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Initial Elements</em>'.
   * @see com.bichler.iec.iec.StructureInitialization#getInitialElements()
   * @see #getStructureInitialization()
   * @generated
   */
  EReference getStructureInitialization_InitialElements();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.StructureElementInitialization <em>Structure Element Initialization</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Structure Element Initialization</em>'.
   * @see com.bichler.iec.iec.StructureElementInitialization
   * @generated
   */
  EClass getStructureElementInitialization();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.StructureElementInitialization#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.StructureElementInitialization#getName()
   * @see #getStructureElementInitialization()
   * @generated
   */
  EAttribute getStructureElementInitialization_Name();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.StructureElementInitialization#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see com.bichler.iec.iec.StructureElementInitialization#getValue()
   * @see #getStructureElementInitialization()
   * @generated
   */
  EReference getStructureElementInitialization_Value();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.StringDeclaration <em>String Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>String Declaration</em>'.
   * @see com.bichler.iec.iec.StringDeclaration
   * @generated
   */
  EClass getStringDeclaration();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.StringDeclaration#isString <em>String</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>String</em>'.
   * @see com.bichler.iec.iec.StringDeclaration#isString()
   * @see #getStringDeclaration()
   * @generated
   */
  EAttribute getStringDeclaration_String();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.StringDeclaration#getSize <em>Size</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Size</em>'.
   * @see com.bichler.iec.iec.StringDeclaration#getSize()
   * @see #getStringDeclaration()
   * @generated
   */
  EAttribute getStringDeclaration_Size();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.StringDeclaration#getInitialValue <em>Initial Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Initial Value</em>'.
   * @see com.bichler.iec.iec.StringDeclaration#getInitialValue()
   * @see #getStringDeclaration()
   * @generated
   */
  EReference getStringDeclaration_InitialValue();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Variable <em>Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Variable</em>'.
   * @see com.bichler.iec.iec.Variable
   * @generated
   */
  EClass getVariable();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.Variable#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.Variable#getName()
   * @see #getVariable()
   * @generated
   */
  EAttribute getVariable_Name();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.VariableAccess <em>Variable Access</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Variable Access</em>'.
   * @see com.bichler.iec.iec.VariableAccess
   * @generated
   */
  EClass getVariableAccess();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.DirectVariable <em>Direct Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Direct Variable</em>'.
   * @see com.bichler.iec.iec.DirectVariable
   * @generated
   */
  EClass getDirectVariable();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DirectVariable#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.DirectVariable#getName()
   * @see #getDirectVariable()
   * @generated
   */
  EAttribute getDirectVariable_Name();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.SymbolicVariableAccess <em>Symbolic Variable Access</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Symbolic Variable Access</em>'.
   * @see com.bichler.iec.iec.SymbolicVariableAccess
   * @generated
   */
  EClass getSymbolicVariableAccess();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.NamedVariableAccess <em>Named Variable Access</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Named Variable Access</em>'.
   * @see com.bichler.iec.iec.NamedVariableAccess
   * @generated
   */
  EClass getNamedVariableAccess();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.NamedVariableAccess#getNamedVariable <em>Named Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Named Variable</em>'.
   * @see com.bichler.iec.iec.NamedVariableAccess#getNamedVariable()
   * @see #getNamedVariableAccess()
   * @generated
   */
  EReference getNamedVariableAccess_NamedVariable();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.MultiElementVariable <em>Multi Element Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Multi Element Variable</em>'.
   * @see com.bichler.iec.iec.MultiElementVariable
   * @generated
   */
  EClass getMultiElementVariable();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ArrayVariable <em>Array Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Array Variable</em>'.
   * @see com.bichler.iec.iec.ArrayVariable
   * @generated
   */
  EClass getArrayVariable();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.ArrayVariable#getSubscriptedVariable <em>Subscripted Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Subscripted Variable</em>'.
   * @see com.bichler.iec.iec.ArrayVariable#getSubscriptedVariable()
   * @see #getArrayVariable()
   * @generated
   */
  EReference getArrayVariable_SubscriptedVariable();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.ArrayVariable#getSubscripts <em>Subscripts</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Subscripts</em>'.
   * @see com.bichler.iec.iec.ArrayVariable#getSubscripts()
   * @see #getArrayVariable()
   * @generated
   */
  EReference getArrayVariable_Subscripts();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.StructuredVariable <em>Structured Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Structured Variable</em>'.
   * @see com.bichler.iec.iec.StructuredVariable
   * @generated
   */
  EClass getStructuredVariable();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.StructuredVariable#getRecordVariable <em>Record Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Record Variable</em>'.
   * @see com.bichler.iec.iec.StructuredVariable#getRecordVariable()
   * @see #getStructuredVariable()
   * @generated
   */
  EReference getStructuredVariable_RecordVariable();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.StructuredVariable#getFieldSelector <em>Field Selector</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Field Selector</em>'.
   * @see com.bichler.iec.iec.StructuredVariable#getFieldSelector()
   * @see #getStructuredVariable()
   * @generated
   */
  EAttribute getStructuredVariable_FieldSelector();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Expression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Expression</em>'.
   * @see com.bichler.iec.iec.Expression
   * @generated
   */
  EClass getExpression();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.Expression#getVariable <em>Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Variable</em>'.
   * @see com.bichler.iec.iec.Expression#getVariable()
   * @see #getExpression()
   * @generated
   */
  EReference getExpression_Variable();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.Expression#getFbname <em>Fbname</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Fbname</em>'.
   * @see com.bichler.iec.iec.Expression#getFbname()
   * @see #getExpression()
   * @generated
   */
  EReference getExpression_Fbname();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.Expression#getOpenbr <em>Openbr</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Openbr</em>'.
   * @see com.bichler.iec.iec.Expression#getOpenbr()
   * @see #getExpression()
   * @generated
   */
  EAttribute getExpression_Openbr();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.Expression#getParamassignment <em>Paramassignment</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Paramassignment</em>'.
   * @see com.bichler.iec.iec.Expression#getParamassignment()
   * @see #getExpression()
   * @generated
   */
  EReference getExpression_Paramassignment();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.Expression#getClosebr <em>Closebr</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Closebr</em>'.
   * @see com.bichler.iec.iec.Expression#getClosebr()
   * @see #getExpression()
   * @generated
   */
  EAttribute getExpression_Closebr();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.Expression#getExpression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expression</em>'.
   * @see com.bichler.iec.iec.Expression#getExpression()
   * @see #getExpression()
   * @generated
   */
  EReference getExpression_Expression();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ParamAssignment <em>Param Assignment</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Param Assignment</em>'.
   * @see com.bichler.iec.iec.ParamAssignment
   * @generated
   */
  EClass getParamAssignment();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.ParamAssignment#getVariablename <em>Variablename</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Variablename</em>'.
   * @see com.bichler.iec.iec.ParamAssignment#getVariablename()
   * @see #getParamAssignment()
   * @generated
   */
  EReference getParamAssignment_Variablename();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ParamAssignment#getExpression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expression</em>'.
   * @see com.bichler.iec.iec.ParamAssignment#getExpression()
   * @see #getParamAssignment()
   * @generated
   */
  EReference getParamAssignment_Expression();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.ParamAssignment#isNot <em>Not</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Not</em>'.
   * @see com.bichler.iec.iec.ParamAssignment#isNot()
   * @see #getParamAssignment()
   * @generated
   */
  EAttribute getParamAssignment_Not();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.ParamAssignment#getVariable1 <em>Variable1</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Variable1</em>'.
   * @see com.bichler.iec.iec.ParamAssignment#getVariable1()
   * @see #getParamAssignment()
   * @generated
   */
  EReference getParamAssignment_Variable1();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.ParamAssignment#getVariable2 <em>Variable2</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Variable2</em>'.
   * @see com.bichler.iec.iec.ParamAssignment#getVariable2()
   * @see #getParamAssignment()
   * @generated
   */
  EReference getParamAssignment_Variable2();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.FunctionDeclaration <em>Function Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Function Declaration</em>'.
   * @see com.bichler.iec.iec.FunctionDeclaration
   * @generated
   */
  EClass getFunctionDeclaration();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.FunctionDeclaration#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.FunctionDeclaration#getName()
   * @see #getFunctionDeclaration()
   * @generated
   */
  EAttribute getFunctionDeclaration_Name();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.FunctionDeclaration#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see com.bichler.iec.iec.FunctionDeclaration#getType()
   * @see #getFunctionDeclaration()
   * @generated
   */
  EReference getFunctionDeclaration_Type();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.FunctionDeclaration#getIoVarDeclarations <em>Io Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Io Var Declarations</em>'.
   * @see com.bichler.iec.iec.FunctionDeclaration#getIoVarDeclarations()
   * @see #getFunctionDeclaration()
   * @generated
   */
  EReference getFunctionDeclaration_IoVarDeclarations();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.FunctionDeclaration#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see com.bichler.iec.iec.FunctionDeclaration#getBody()
   * @see #getFunctionDeclaration()
   * @generated
   */
  EReference getFunctionDeclaration_Body();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.IoVarDeclarations <em>Io Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Io Var Declarations</em>'.
   * @see com.bichler.iec.iec.IoVarDeclarations
   * @generated
   */
  EClass getIoVarDeclarations();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.InputDeclarations <em>Input Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Input Declarations</em>'.
   * @see com.bichler.iec.iec.InputDeclarations
   * @generated
   */
  EClass getInputDeclarations();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.InputDeclarations#getDeclarations <em>Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Declarations</em>'.
   * @see com.bichler.iec.iec.InputDeclarations#getDeclarations()
   * @see #getInputDeclarations()
   * @generated
   */
  EReference getInputDeclarations_Declarations();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.InputDeclaration <em>Input Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Input Declaration</em>'.
   * @see com.bichler.iec.iec.InputDeclaration
   * @generated
   */
  EClass getInputDeclaration();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.InputDeclaration#getVar1List <em>Var1 List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Var1 List</em>'.
   * @see com.bichler.iec.iec.InputDeclaration#getVar1List()
   * @see #getInputDeclaration()
   * @generated
   */
  EReference getInputDeclaration_Var1List();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.InputDeclaration#getDeclSpecification <em>Decl Specification</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Decl Specification</em>'.
   * @see com.bichler.iec.iec.InputDeclaration#getDeclSpecification()
   * @see #getInputDeclaration()
   * @generated
   */
  EReference getInputDeclaration_DeclSpecification();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.EdgeDeclaration <em>Edge Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Edge Declaration</em>'.
   * @see com.bichler.iec.iec.EdgeDeclaration
   * @generated
   */
  EClass getEdgeDeclaration();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.EdgeDeclaration#getVar1List <em>Var1 List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Var1 List</em>'.
   * @see com.bichler.iec.iec.EdgeDeclaration#getVar1List()
   * @see #getEdgeDeclaration()
   * @generated
   */
  EReference getEdgeDeclaration_Var1List();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.DeclSpecification <em>Decl Specification</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Decl Specification</em>'.
   * @see com.bichler.iec.iec.DeclSpecification
   * @generated
   */
  EClass getDeclSpecification();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.VarDeclSpecification <em>Var Decl Specification</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Var Decl Specification</em>'.
   * @see com.bichler.iec.iec.VarDeclSpecification
   * @generated
   */
  EClass getVarDeclSpecification();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.VarDeclSpecification#getSpecInit <em>Spec Init</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Spec Init</em>'.
   * @see com.bichler.iec.iec.VarDeclSpecification#getSpecInit()
   * @see #getVarDeclSpecification()
   * @generated
   */
  EReference getVarDeclSpecification_SpecInit();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.EdgeDeclSpecification <em>Edge Decl Specification</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Edge Decl Specification</em>'.
   * @see com.bichler.iec.iec.EdgeDeclSpecification
   * @generated
   */
  EClass getEdgeDeclSpecification();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.EdgeDeclSpecification#isREdge <em>REdge</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>REdge</em>'.
   * @see com.bichler.iec.iec.EdgeDeclSpecification#isREdge()
   * @see #getEdgeDeclSpecification()
   * @generated
   */
  EAttribute getEdgeDeclSpecification_REdge();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.EdgeDeclSpecification#isFEdge <em>FEdge</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>FEdge</em>'.
   * @see com.bichler.iec.iec.EdgeDeclSpecification#isFEdge()
   * @see #getEdgeDeclSpecification()
   * @generated
   */
  EAttribute getEdgeDeclSpecification_FEdge();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Var1List <em>Var1 List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Var1 List</em>'.
   * @see com.bichler.iec.iec.Var1List
   * @generated
   */
  EClass getVar1List();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.Var1List#getVariables <em>Variables</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Variables</em>'.
   * @see com.bichler.iec.iec.Var1List#getVariables()
   * @see #getVar1List()
   * @generated
   */
  EReference getVar1List_Variables();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.OutputDeclarations <em>Output Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Output Declarations</em>'.
   * @see com.bichler.iec.iec.OutputDeclarations
   * @generated
   */
  EClass getOutputDeclarations();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.OutputDeclarations#getInitDecls <em>Init Decls</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Init Decls</em>'.
   * @see com.bichler.iec.iec.OutputDeclarations#getInitDecls()
   * @see #getOutputDeclarations()
   * @generated
   */
  EReference getOutputDeclarations_InitDecls();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.VarInitDecl <em>Var Init Decl</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Var Init Decl</em>'.
   * @see com.bichler.iec.iec.VarInitDecl
   * @generated
   */
  EClass getVarInitDecl();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.VarInitDecl#getVar1List <em>Var1 List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Var1 List</em>'.
   * @see com.bichler.iec.iec.VarInitDecl#getVar1List()
   * @see #getVarInitDecl()
   * @generated
   */
  EReference getVarInitDecl_Var1List();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.VarInitDecl#getSpecInit <em>Spec Init</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Spec Init</em>'.
   * @see com.bichler.iec.iec.VarInitDecl#getSpecInit()
   * @see #getVarInitDecl()
   * @generated
   */
  EReference getVarInitDecl_SpecInit();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.InputOutputDeclarations <em>Input Output Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Input Output Declarations</em>'.
   * @see com.bichler.iec.iec.InputOutputDeclarations
   * @generated
   */
  EClass getInputOutputDeclarations();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.InputOutputDeclarations#getInitDecls <em>Init Decls</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Init Decls</em>'.
   * @see com.bichler.iec.iec.InputOutputDeclarations#getInitDecls()
   * @see #getInputOutputDeclarations()
   * @generated
   */
  EReference getInputOutputDeclarations_InitDecls();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.FunctionBody <em>Function Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Function Body</em>'.
   * @see com.bichler.iec.iec.FunctionBody
   * @generated
   */
  EClass getFunctionBody();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.InstructionList <em>Instruction List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Instruction List</em>'.
   * @see com.bichler.iec.iec.InstructionList
   * @generated
   */
  EClass getInstructionList();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.InstructionList#getInstructions <em>Instructions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Instructions</em>'.
   * @see com.bichler.iec.iec.InstructionList#getInstructions()
   * @see #getInstructionList()
   * @generated
   */
  EReference getInstructionList_Instructions();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Instruction <em>Instruction</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Instruction</em>'.
   * @see com.bichler.iec.iec.Instruction
   * @generated
   */
  EClass getInstruction();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.Instruction#getLabel <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Label</em>'.
   * @see com.bichler.iec.iec.Instruction#getLabel()
   * @see #getInstruction()
   * @generated
   */
  EReference getInstruction_Label();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.Instruction#getInstruction <em>Instruction</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Instruction</em>'.
   * @see com.bichler.iec.iec.Instruction#getInstruction()
   * @see #getInstruction()
   * @generated
   */
  EReference getInstruction_Instruction();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Label <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Label</em>'.
   * @see com.bichler.iec.iec.Label
   * @generated
   */
  EClass getLabel();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.Label#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.Label#getName()
   * @see #getLabel()
   * @generated
   */
  EAttribute getLabel_Name();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Operation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operation</em>'.
   * @see com.bichler.iec.iec.Operation
   * @generated
   */
  EClass getOperation();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.SimpleOperation <em>Simple Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Simple Operation</em>'.
   * @see com.bichler.iec.iec.SimpleOperation
   * @generated
   */
  EClass getSimpleOperation();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.SimpleOperation#getOperator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operator</em>'.
   * @see com.bichler.iec.iec.SimpleOperation#getOperator()
   * @see #getSimpleOperation()
   * @generated
   */
  EAttribute getSimpleOperation_Operator();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.SimpleOperation#getOperand <em>Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Operand</em>'.
   * @see com.bichler.iec.iec.SimpleOperation#getOperand()
   * @see #getSimpleOperation()
   * @generated
   */
  EReference getSimpleOperation_Operand();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ExpressionOperation <em>Expression Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Expression Operation</em>'.
   * @see com.bichler.iec.iec.ExpressionOperation
   * @generated
   */
  EClass getExpressionOperation();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.ExpressionOperation#getOperator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operator</em>'.
   * @see com.bichler.iec.iec.ExpressionOperation#getOperator()
   * @see #getExpressionOperation()
   * @generated
   */
  EAttribute getExpressionOperation_Operator();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ExpressionOperation#getOperand <em>Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Operand</em>'.
   * @see com.bichler.iec.iec.ExpressionOperation#getOperand()
   * @see #getExpressionOperation()
   * @generated
   */
  EReference getExpressionOperation_Operand();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ExpressionOperation#getSimpleInstructionList <em>Simple Instruction List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Simple Instruction List</em>'.
   * @see com.bichler.iec.iec.ExpressionOperation#getSimpleInstructionList()
   * @see #getExpressionOperation()
   * @generated
   */
  EReference getExpressionOperation_SimpleInstructionList();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.JmpOperation <em>Jmp Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Jmp Operation</em>'.
   * @see com.bichler.iec.iec.JmpOperation
   * @generated
   */
  EClass getJmpOperation();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.JmpOperation#getOperator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operator</em>'.
   * @see com.bichler.iec.iec.JmpOperation#getOperator()
   * @see #getJmpOperation()
   * @generated
   */
  EAttribute getJmpOperation_Operator();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.JmpOperation#getLabel <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Label</em>'.
   * @see com.bichler.iec.iec.JmpOperation#getLabel()
   * @see #getJmpOperation()
   * @generated
   */
  EReference getJmpOperation_Label();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Operand <em>Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operand</em>'.
   * @see com.bichler.iec.iec.Operand
   * @generated
   */
  EClass getOperand();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.Operand#getConstant <em>Constant</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Constant</em>'.
   * @see com.bichler.iec.iec.Operand#getConstant()
   * @see #getOperand()
   * @generated
   */
  EReference getOperand_Constant();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.Operand#getReference <em>Reference</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Reference</em>'.
   * @see com.bichler.iec.iec.Operand#getReference()
   * @see #getOperand()
   * @generated
   */
  EReference getOperand_Reference();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ReferencedOperand <em>Referenced Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Referenced Operand</em>'.
   * @see com.bichler.iec.iec.ReferencedOperand
   * @generated
   */
  EClass getReferencedOperand();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.SimpleInstructionList <em>Simple Instruction List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Simple Instruction List</em>'.
   * @see com.bichler.iec.iec.SimpleInstructionList
   * @generated
   */
  EClass getSimpleInstructionList();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.SimpleInstructionList#getInstructions <em>Instructions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Instructions</em>'.
   * @see com.bichler.iec.iec.SimpleInstructionList#getInstructions()
   * @see #getSimpleInstructionList()
   * @generated
   */
  EReference getSimpleInstructionList_Instructions();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.SimpleInstruction <em>Simple Instruction</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Simple Instruction</em>'.
   * @see com.bichler.iec.iec.SimpleInstruction
   * @generated
   */
  EClass getSimpleInstruction();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.StatementList <em>Statement List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Statement List</em>'.
   * @see com.bichler.iec.iec.StatementList
   * @generated
   */
  EClass getStatementList();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.StatementList#getStatements <em>Statements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Statements</em>'.
   * @see com.bichler.iec.iec.StatementList#getStatements()
   * @see #getStatementList()
   * @generated
   */
  EReference getStatementList_Statements();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Statement <em>Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Statement</em>'.
   * @see com.bichler.iec.iec.Statement
   * @generated
   */
  EClass getStatement();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.AssignStatement <em>Assign Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Assign Statement</em>'.
   * @see com.bichler.iec.iec.AssignStatement
   * @generated
   */
  EClass getAssignStatement();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.AssignStatement#getVariable <em>Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Variable</em>'.
   * @see com.bichler.iec.iec.AssignStatement#getVariable()
   * @see #getAssignStatement()
   * @generated
   */
  EReference getAssignStatement_Variable();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.AssignStatement#getExpression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expression</em>'.
   * @see com.bichler.iec.iec.AssignStatement#getExpression()
   * @see #getAssignStatement()
   * @generated
   */
  EReference getAssignStatement_Expression();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.SelectionStatement <em>Selection Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Selection Statement</em>'.
   * @see com.bichler.iec.iec.SelectionStatement
   * @generated
   */
  EClass getSelectionStatement();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.SelectionStatement#getElseStatementList <em>Else Statement List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Else Statement List</em>'.
   * @see com.bichler.iec.iec.SelectionStatement#getElseStatementList()
   * @see #getSelectionStatement()
   * @generated
   */
  EReference getSelectionStatement_ElseStatementList();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.IfStatement <em>If Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>If Statement</em>'.
   * @see com.bichler.iec.iec.IfStatement
   * @generated
   */
  EClass getIfStatement();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.IfStatement#getIfExpression <em>If Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>If Expression</em>'.
   * @see com.bichler.iec.iec.IfStatement#getIfExpression()
   * @see #getIfStatement()
   * @generated
   */
  EReference getIfStatement_IfExpression();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.IfStatement#getThenStatementList <em>Then Statement List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Then Statement List</em>'.
   * @see com.bichler.iec.iec.IfStatement#getThenStatementList()
   * @see #getIfStatement()
   * @generated
   */
  EReference getIfStatement_ThenStatementList();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.IfStatement#getElseIfs <em>Else Ifs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Else Ifs</em>'.
   * @see com.bichler.iec.iec.IfStatement#getElseIfs()
   * @see #getIfStatement()
   * @generated
   */
  EReference getIfStatement_ElseIfs();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ElseIf <em>Else If</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Else If</em>'.
   * @see com.bichler.iec.iec.ElseIf
   * @generated
   */
  EClass getElseIf();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ElseIf#getExpression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expression</em>'.
   * @see com.bichler.iec.iec.ElseIf#getExpression()
   * @see #getElseIf()
   * @generated
   */
  EReference getElseIf_Expression();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ElseIf#getStatementList <em>Statement List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Statement List</em>'.
   * @see com.bichler.iec.iec.ElseIf#getStatementList()
   * @see #getElseIf()
   * @generated
   */
  EReference getElseIf_StatementList();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.CaseStatement <em>Case Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Case Statement</em>'.
   * @see com.bichler.iec.iec.CaseStatement
   * @generated
   */
  EClass getCaseStatement();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.CaseStatement#getExpression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expression</em>'.
   * @see com.bichler.iec.iec.CaseStatement#getExpression()
   * @see #getCaseStatement()
   * @generated
   */
  EReference getCaseStatement_Expression();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.CaseStatement#getCaseElements <em>Case Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Case Elements</em>'.
   * @see com.bichler.iec.iec.CaseStatement#getCaseElements()
   * @see #getCaseStatement()
   * @generated
   */
  EReference getCaseStatement_CaseElements();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.CaseElement <em>Case Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Case Element</em>'.
   * @see com.bichler.iec.iec.CaseElement
   * @generated
   */
  EClass getCaseElement();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.CaseElement#getCaseList <em>Case List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Case List</em>'.
   * @see com.bichler.iec.iec.CaseElement#getCaseList()
   * @see #getCaseElement()
   * @generated
   */
  EReference getCaseElement_CaseList();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.CaseElement#getStatementList <em>Statement List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Statement List</em>'.
   * @see com.bichler.iec.iec.CaseElement#getStatementList()
   * @see #getCaseElement()
   * @generated
   */
  EReference getCaseElement_StatementList();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.CaseList <em>Case List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Case List</em>'.
   * @see com.bichler.iec.iec.CaseList
   * @generated
   */
  EClass getCaseList();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.CaseList#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see com.bichler.iec.iec.CaseList#getElements()
   * @see #getCaseList()
   * @generated
   */
  EReference getCaseList_Elements();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.CaseListElement <em>Case List Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Case List Element</em>'.
   * @see com.bichler.iec.iec.CaseListElement
   * @generated
   */
  EClass getCaseListElement();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.CaseListElement#getSubRange <em>Sub Range</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Sub Range</em>'.
   * @see com.bichler.iec.iec.CaseListElement#getSubRange()
   * @see #getCaseListElement()
   * @generated
   */
  EAttribute getCaseListElement_SubRange();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.CaseListElement#getInteger <em>Integer</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Integer</em>'.
   * @see com.bichler.iec.iec.CaseListElement#getInteger()
   * @see #getCaseListElement()
   * @generated
   */
  EAttribute getCaseListElement_Integer();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.CaseListElement#getEnumeratedValue <em>Enumerated Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Enumerated Value</em>'.
   * @see com.bichler.iec.iec.CaseListElement#getEnumeratedValue()
   * @see #getCaseListElement()
   * @generated
   */
  EReference getCaseListElement_EnumeratedValue();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.FunctionBlockDeclaration <em>Function Block Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Function Block Declaration</em>'.
   * @see com.bichler.iec.iec.FunctionBlockDeclaration
   * @generated
   */
  EClass getFunctionBlockDeclaration();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.FunctionBlockDeclaration#getVarDeclarations <em>Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Var Declarations</em>'.
   * @see com.bichler.iec.iec.FunctionBlockDeclaration#getVarDeclarations()
   * @see #getFunctionBlockDeclaration()
   * @generated
   */
  EReference getFunctionBlockDeclaration_VarDeclarations();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.FunctionBlockDeclaration#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see com.bichler.iec.iec.FunctionBlockDeclaration#getBody()
   * @see #getFunctionBlockDeclaration()
   * @generated
   */
  EReference getFunctionBlockDeclaration_Body();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.FunctionBlockVarDeclarations <em>Function Block Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Function Block Var Declarations</em>'.
   * @see com.bichler.iec.iec.FunctionBlockVarDeclarations
   * @generated
   */
  EClass getFunctionBlockVarDeclarations();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.OtherVarDeclarations <em>Other Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Other Var Declarations</em>'.
   * @see com.bichler.iec.iec.OtherVarDeclarations
   * @generated
   */
  EClass getOtherVarDeclarations();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.VarDeclarations <em>Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Var Declarations</em>'.
   * @see com.bichler.iec.iec.VarDeclarations
   * @generated
   */
  EClass getVarDeclarations();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.VarDeclarations#isConstant <em>Constant</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Constant</em>'.
   * @see com.bichler.iec.iec.VarDeclarations#isConstant()
   * @see #getVarDeclarations()
   * @generated
   */
  EAttribute getVarDeclarations_Constant();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.VarDeclarations#getInitDecls <em>Init Decls</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Init Decls</em>'.
   * @see com.bichler.iec.iec.VarDeclarations#getInitDecls()
   * @see #getVarDeclarations()
   * @generated
   */
  EReference getVarDeclarations_InitDecls();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.FunctionBlockBody <em>Function Block Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Function Block Body</em>'.
   * @see com.bichler.iec.iec.FunctionBlockBody
   * @generated
   */
  EClass getFunctionBlockBody();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ProgramDeclaration <em>Program Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Program Declaration</em>'.
   * @see com.bichler.iec.iec.ProgramDeclaration
   * @generated
   */
  EClass getProgramDeclaration();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.ProgramDeclaration#getVarDeclarations <em>Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Var Declarations</em>'.
   * @see com.bichler.iec.iec.ProgramDeclaration#getVarDeclarations()
   * @see #getProgramDeclaration()
   * @generated
   */
  EReference getProgramDeclaration_VarDeclarations();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ProgramDeclaration#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see com.bichler.iec.iec.ProgramDeclaration#getBody()
   * @see #getProgramDeclaration()
   * @generated
   */
  EReference getProgramDeclaration_Body();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ProgramVarDeclarations <em>Program Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Program Var Declarations</em>'.
   * @see com.bichler.iec.iec.ProgramVarDeclarations
   * @generated
   */
  EClass getProgramVarDeclarations();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.LocatedVarDeclarations <em>Located Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Located Var Declarations</em>'.
   * @see com.bichler.iec.iec.LocatedVarDeclarations
   * @generated
   */
  EClass getLocatedVarDeclarations();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.LocatedVarDeclarations#getLocatedVarDeclaration <em>Located Var Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Located Var Declaration</em>'.
   * @see com.bichler.iec.iec.LocatedVarDeclarations#getLocatedVarDeclaration()
   * @see #getLocatedVarDeclarations()
   * @generated
   */
  EReference getLocatedVarDeclarations_LocatedVarDeclaration();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.LocatedVarDeclaration <em>Located Var Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Located Var Declaration</em>'.
   * @see com.bichler.iec.iec.LocatedVarDeclaration
   * @generated
   */
  EClass getLocatedVarDeclaration();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.LocatedVarDeclaration#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.LocatedVarDeclaration#getName()
   * @see #getLocatedVarDeclaration()
   * @generated
   */
  EAttribute getLocatedVarDeclaration_Name();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.LocatedVarDeclaration#getLocation <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Location</em>'.
   * @see com.bichler.iec.iec.LocatedVarDeclaration#getLocation()
   * @see #getLocatedVarDeclaration()
   * @generated
   */
  EReference getLocatedVarDeclaration_Location();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.LocatedVarDeclaration#getSpecInit <em>Spec Init</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Spec Init</em>'.
   * @see com.bichler.iec.iec.LocatedVarDeclaration#getSpecInit()
   * @see #getLocatedVarDeclaration()
   * @generated
   */
  EReference getLocatedVarDeclaration_SpecInit();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Location <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Location</em>'.
   * @see com.bichler.iec.iec.Location
   * @generated
   */
  EClass getLocation();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.Location#getDirectVariable <em>Direct Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Direct Variable</em>'.
   * @see com.bichler.iec.iec.Location#getDirectVariable()
   * @see #getLocation()
   * @generated
   */
  EReference getLocation_DirectVariable();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ProgramAccessDecls <em>Program Access Decls</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Program Access Decls</em>'.
   * @see com.bichler.iec.iec.ProgramAccessDecls
   * @generated
   */
  EClass getProgramAccessDecls();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.ProgramAccessDecls#getProgramAccessDecl <em>Program Access Decl</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Program Access Decl</em>'.
   * @see com.bichler.iec.iec.ProgramAccessDecls#getProgramAccessDecl()
   * @see #getProgramAccessDecls()
   * @generated
   */
  EReference getProgramAccessDecls_ProgramAccessDecl();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ProgramAccessDecl <em>Program Access Decl</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Program Access Decl</em>'.
   * @see com.bichler.iec.iec.ProgramAccessDecl
   * @generated
   */
  EClass getProgramAccessDecl();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ProgramAccessDecl#getAccessName <em>Access Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Access Name</em>'.
   * @see com.bichler.iec.iec.ProgramAccessDecl#getAccessName()
   * @see #getProgramAccessDecl()
   * @generated
   */
  EReference getProgramAccessDecl_AccessName();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ProgramAccessDecl#getSymbolicVariable <em>Symbolic Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Symbolic Variable</em>'.
   * @see com.bichler.iec.iec.ProgramAccessDecl#getSymbolicVariable()
   * @see #getProgramAccessDecl()
   * @generated
   */
  EReference getProgramAccessDecl_SymbolicVariable();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ProgramAccessDecl#getTypeName <em>Type Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type Name</em>'.
   * @see com.bichler.iec.iec.ProgramAccessDecl#getTypeName()
   * @see #getProgramAccessDecl()
   * @generated
   */
  EReference getProgramAccessDecl_TypeName();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.ProgramAccessDecl#getDirection <em>Direction</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Direction</em>'.
   * @see com.bichler.iec.iec.ProgramAccessDecl#getDirection()
   * @see #getProgramAccessDecl()
   * @generated
   */
  EAttribute getProgramAccessDecl_Direction();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ConfigurationDeclaration <em>Configuration Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Configuration Declaration</em>'.
   * @see com.bichler.iec.iec.ConfigurationDeclaration
   * @generated
   */
  EClass getConfigurationDeclaration();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ConfigurationDeclaration#getGlobalVarDeclarations <em>Global Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Global Var Declarations</em>'.
   * @see com.bichler.iec.iec.ConfigurationDeclaration#getGlobalVarDeclarations()
   * @see #getConfigurationDeclaration()
   * @generated
   */
  EReference getConfigurationDeclaration_GlobalVarDeclarations();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ConfigurationDeclaration#getResdecl <em>Resdecl</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Resdecl</em>'.
   * @see com.bichler.iec.iec.ConfigurationDeclaration#getResdecl()
   * @see #getConfigurationDeclaration()
   * @generated
   */
  EReference getConfigurationDeclaration_Resdecl();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ResourceDeclaration <em>Resource Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Resource Declaration</em>'.
   * @see com.bichler.iec.iec.ResourceDeclaration
   * @generated
   */
  EClass getResourceDeclaration();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.ResourceDeclaration#getResname <em>Resname</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Resname</em>'.
   * @see com.bichler.iec.iec.ResourceDeclaration#getResname()
   * @see #getResourceDeclaration()
   * @generated
   */
  EAttribute getResourceDeclaration_Resname();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ResourceDeclaration#getGlobalVarDeclarations <em>Global Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Global Var Declarations</em>'.
   * @see com.bichler.iec.iec.ResourceDeclaration#getGlobalVarDeclarations()
   * @see #getResourceDeclaration()
   * @generated
   */
  EReference getResourceDeclaration_GlobalVarDeclarations();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ResourceDeclaration#getSingleresource <em>Singleresource</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Singleresource</em>'.
   * @see com.bichler.iec.iec.ResourceDeclaration#getSingleresource()
   * @see #getResourceDeclaration()
   * @generated
   */
  EReference getResourceDeclaration_Singleresource();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.SingleResourceDeclaration <em>Single Resource Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Single Resource Declaration</em>'.
   * @see com.bichler.iec.iec.SingleResourceDeclaration
   * @generated
   */
  EClass getSingleResourceDeclaration();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.SingleResourceDeclaration#getTaskConf <em>Task Conf</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Task Conf</em>'.
   * @see com.bichler.iec.iec.SingleResourceDeclaration#getTaskConf()
   * @see #getSingleResourceDeclaration()
   * @generated
   */
  EReference getSingleResourceDeclaration_TaskConf();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.SingleResourceDeclaration#getProgramConf <em>Program Conf</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Program Conf</em>'.
   * @see com.bichler.iec.iec.SingleResourceDeclaration#getProgramConf()
   * @see #getSingleResourceDeclaration()
   * @generated
   */
  EReference getSingleResourceDeclaration_ProgramConf();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ProgramConfiguration <em>Program Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Program Configuration</em>'.
   * @see com.bichler.iec.iec.ProgramConfiguration
   * @generated
   */
  EClass getProgramConfiguration();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.ProgramConfiguration#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.ProgramConfiguration#getName()
   * @see #getProgramConfiguration()
   * @generated
   */
  EAttribute getProgramConfiguration_Name();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.ProgramConfiguration#getTask <em>Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Task</em>'.
   * @see com.bichler.iec.iec.ProgramConfiguration#getTask()
   * @see #getProgramConfiguration()
   * @generated
   */
  EReference getProgramConfiguration_Task();

  /**
   * Returns the meta object for the reference '{@link com.bichler.iec.iec.ProgramConfiguration#getProg <em>Prog</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Prog</em>'.
   * @see com.bichler.iec.iec.ProgramConfiguration#getProg()
   * @see #getProgramConfiguration()
   * @generated
   */
  EReference getProgramConfiguration_Prog();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ProgramConfiguration#getProgConf <em>Prog Conf</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Prog Conf</em>'.
   * @see com.bichler.iec.iec.ProgramConfiguration#getProgConf()
   * @see #getProgramConfiguration()
   * @generated
   */
  EReference getProgramConfiguration_ProgConf();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ProgConfElements <em>Prog Conf Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Prog Conf Elements</em>'.
   * @see com.bichler.iec.iec.ProgConfElements
   * @generated
   */
  EClass getProgConfElements();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.ProgConfElements#getProgconf <em>Progconf</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Progconf</em>'.
   * @see com.bichler.iec.iec.ProgConfElements#getProgconf()
   * @see #getProgConfElements()
   * @generated
   */
  EReference getProgConfElements_Progconf();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ProgConfElement <em>Prog Conf Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Prog Conf Element</em>'.
   * @see com.bichler.iec.iec.ProgConfElement
   * @generated
   */
  EClass getProgConfElement();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.FBTask <em>FB Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>FB Task</em>'.
   * @see com.bichler.iec.iec.FBTask
   * @generated
   */
  EClass getFBTask();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.FBTask#getFbname <em>Fbname</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Fbname</em>'.
   * @see com.bichler.iec.iec.FBTask#getFbname()
   * @see #getFBTask()
   * @generated
   */
  EReference getFBTask_Fbname();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.FBTask#getTask <em>Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Task</em>'.
   * @see com.bichler.iec.iec.FBTask#getTask()
   * @see #getFBTask()
   * @generated
   */
  EReference getFBTask_Task();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ProgCNXN <em>Prog CNXN</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Prog CNXN</em>'.
   * @see com.bichler.iec.iec.ProgCNXN
   * @generated
   */
  EClass getProgCNXN();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.ProgCNXN#getVariablename <em>Variablename</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Variablename</em>'.
   * @see com.bichler.iec.iec.ProgCNXN#getVariablename()
   * @see #getProgCNXN()
   * @generated
   */
  EAttribute getProgCNXN_Variablename();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.ProgCNXN#getProgd <em>Progd</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Progd</em>'.
   * @see com.bichler.iec.iec.ProgCNXN#getProgd()
   * @see #getProgCNXN()
   * @generated
   */
  EReference getProgCNXN_Progd();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.DataSink <em>Data Sink</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Data Sink</em>'.
   * @see com.bichler.iec.iec.DataSink
   * @generated
   */
  EClass getDataSink();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.DataSink#getGlobvar <em>Globvar</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Globvar</em>'.
   * @see com.bichler.iec.iec.DataSink#getGlobvar()
   * @see #getDataSink()
   * @generated
   */
  EReference getDataSink_Globvar();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.DataSink#getDirvar <em>Dirvar</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Dirvar</em>'.
   * @see com.bichler.iec.iec.DataSink#getDirvar()
   * @see #getDataSink()
   * @generated
   */
  EReference getDataSink_Dirvar();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.ProgDataSource <em>Prog Data Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Prog Data Source</em>'.
   * @see com.bichler.iec.iec.ProgDataSource
   * @generated
   */
  EClass getProgDataSource();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.GlobalVarDeclarations <em>Global Var Declarations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Global Var Declarations</em>'.
   * @see com.bichler.iec.iec.GlobalVarDeclarations
   * @generated
   */
  EClass getGlobalVarDeclarations();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.GlobalVarDeclarations#getGlobalVarDecl <em>Global Var Decl</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Global Var Decl</em>'.
   * @see com.bichler.iec.iec.GlobalVarDeclarations#getGlobalVarDecl()
   * @see #getGlobalVarDeclarations()
   * @generated
   */
  EReference getGlobalVarDeclarations_GlobalVarDecl();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.GlobalVarDecl <em>Global Var Decl</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Global Var Decl</em>'.
   * @see com.bichler.iec.iec.GlobalVarDecl
   * @generated
   */
  EClass getGlobalVarDecl();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.GlobalVarDecl#getSpec <em>Spec</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Spec</em>'.
   * @see com.bichler.iec.iec.GlobalVarDecl#getSpec()
   * @see #getGlobalVarDecl()
   * @generated
   */
  EReference getGlobalVarDecl_Spec();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.GlobalVarDecl#getSpecInit <em>Spec Init</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Spec Init</em>'.
   * @see com.bichler.iec.iec.GlobalVarDecl#getSpecInit()
   * @see #getGlobalVarDecl()
   * @generated
   */
  EReference getGlobalVarDecl_SpecInit();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.GlobalVarSpec <em>Global Var Spec</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Global Var Spec</em>'.
   * @see com.bichler.iec.iec.GlobalVarSpec
   * @generated
   */
  EClass getGlobalVarSpec();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.GlobalVarSpec#getVariable <em>Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Variable</em>'.
   * @see com.bichler.iec.iec.GlobalVarSpec#getVariable()
   * @see #getGlobalVarSpec()
   * @generated
   */
  EReference getGlobalVarSpec_Variable();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.GlobalVarSpec#getLocation <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Location</em>'.
   * @see com.bichler.iec.iec.GlobalVarSpec#getLocation()
   * @see #getGlobalVarSpec()
   * @generated
   */
  EReference getGlobalVarSpec_Location();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.GlobalVarList <em>Global Var List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Global Var List</em>'.
   * @see com.bichler.iec.iec.GlobalVarList
   * @generated
   */
  EClass getGlobalVarList();

  /**
   * Returns the meta object for the containment reference list '{@link com.bichler.iec.iec.GlobalVarList#getVariables <em>Variables</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Variables</em>'.
   * @see com.bichler.iec.iec.GlobalVarList#getVariables()
   * @see #getGlobalVarList()
   * @generated
   */
  EReference getGlobalVarList_Variables();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.GlobalVar <em>Global Var</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Global Var</em>'.
   * @see com.bichler.iec.iec.GlobalVar
   * @generated
   */
  EClass getGlobalVar();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.GlobalVar#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.GlobalVar#getName()
   * @see #getGlobalVar()
   * @generated
   */
  EAttribute getGlobalVar_Name();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.TaskConfiguration <em>Task Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Task Configuration</em>'.
   * @see com.bichler.iec.iec.TaskConfiguration
   * @generated
   */
  EClass getTaskConfiguration();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.TaskConfiguration#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.bichler.iec.iec.TaskConfiguration#getName()
   * @see #getTaskConfiguration()
   * @generated
   */
  EAttribute getTaskConfiguration_Name();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.TaskConfiguration#getTaskInit <em>Task Init</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Task Init</em>'.
   * @see com.bichler.iec.iec.TaskConfiguration#getTaskInit()
   * @see #getTaskConfiguration()
   * @generated
   */
  EReference getTaskConfiguration_TaskInit();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.TaskInitialization <em>Task Initialization</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Task Initialization</em>'.
   * @see com.bichler.iec.iec.TaskInitialization
   * @generated
   */
  EClass getTaskInitialization();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.TaskInitialization#getSingle <em>Single</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Single</em>'.
   * @see com.bichler.iec.iec.TaskInitialization#getSingle()
   * @see #getTaskInitialization()
   * @generated
   */
  EReference getTaskInitialization_Single();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.TaskInitialization#getInterval <em>Interval</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Interval</em>'.
   * @see com.bichler.iec.iec.TaskInitialization#getInterval()
   * @see #getTaskInitialization()
   * @generated
   */
  EReference getTaskInitialization_Interval();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.TaskInitialization#getPrior <em>Prior</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Prior</em>'.
   * @see com.bichler.iec.iec.TaskInitialization#getPrior()
   * @see #getTaskInitialization()
   * @generated
   */
  EAttribute getTaskInitialization_Prior();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.DataSource <em>Data Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Data Source</em>'.
   * @see com.bichler.iec.iec.DataSource
   * @generated
   */
  EClass getDataSource();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Constant <em>Constant</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Constant</em>'.
   * @see com.bichler.iec.iec.Constant
   * @generated
   */
  EClass getConstant();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.NumericLiteral <em>Numeric Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Numeric Literal</em>'.
   * @see com.bichler.iec.iec.NumericLiteral
   * @generated
   */
  EClass getNumericLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.NumericLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see com.bichler.iec.iec.NumericLiteral#getValue()
   * @see #getNumericLiteral()
   * @generated
   */
  EAttribute getNumericLiteral_Value();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.IntegerLiteral <em>Integer Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Integer Literal</em>'.
   * @see com.bichler.iec.iec.IntegerLiteral
   * @generated
   */
  EClass getIntegerLiteral();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.IntegerLiteral#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see com.bichler.iec.iec.IntegerLiteral#getType()
   * @see #getIntegerLiteral()
   * @generated
   */
  EReference getIntegerLiteral_Type();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.RealLiteral <em>Real Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Real Literal</em>'.
   * @see com.bichler.iec.iec.RealLiteral
   * @generated
   */
  EClass getRealLiteral();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.RealLiteral#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see com.bichler.iec.iec.RealLiteral#getType()
   * @see #getRealLiteral()
   * @generated
   */
  EReference getRealLiteral_Type();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.CharacterString <em>Character String</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Character String</em>'.
   * @see com.bichler.iec.iec.CharacterString
   * @generated
   */
  EClass getCharacterString();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.CharacterString#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see com.bichler.iec.iec.CharacterString#getValue()
   * @see #getCharacterString()
   * @generated
   */
  EAttribute getCharacterString_Value();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.BitString <em>Bit String</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Bit String</em>'.
   * @see com.bichler.iec.iec.BitString
   * @generated
   */
  EClass getBitString();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.BitString#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see com.bichler.iec.iec.BitString#getValue()
   * @see #getBitString()
   * @generated
   */
  EAttribute getBitString_Value();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.Boolean <em>Boolean</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Boolean</em>'.
   * @see com.bichler.iec.iec.Boolean
   * @generated
   */
  EClass getBoolean();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.Boolean#getBoolInt <em>Bool Int</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Bool Int</em>'.
   * @see com.bichler.iec.iec.Boolean#getBoolInt()
   * @see #getBoolean()
   * @generated
   */
  EAttribute getBoolean_BoolInt();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.Boolean#isTrue <em>True</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>True</em>'.
   * @see com.bichler.iec.iec.Boolean#isTrue()
   * @see #getBoolean()
   * @generated
   */
  EAttribute getBoolean_True();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.TimeLiteral <em>Time Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Time Literal</em>'.
   * @see com.bichler.iec.iec.TimeLiteral
   * @generated
   */
  EClass getTimeLiteral();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.DurationLiteral <em>Duration Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Duration Literal</em>'.
   * @see com.bichler.iec.iec.DurationLiteral
   * @generated
   */
  EClass getDurationLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DurationLiteral#getDuration <em>Duration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Duration</em>'.
   * @see com.bichler.iec.iec.DurationLiteral#getDuration()
   * @see #getDurationLiteral()
   * @generated
   */
  EAttribute getDurationLiteral_Duration();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.TimeOfDayLiteral <em>Time Of Day Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Time Of Day Literal</em>'.
   * @see com.bichler.iec.iec.TimeOfDayLiteral
   * @generated
   */
  EClass getTimeOfDayLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.TimeOfDayLiteral#getHour <em>Hour</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Hour</em>'.
   * @see com.bichler.iec.iec.TimeOfDayLiteral#getHour()
   * @see #getTimeOfDayLiteral()
   * @generated
   */
  EAttribute getTimeOfDayLiteral_Hour();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.TimeOfDayLiteral#getMinute <em>Minute</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Minute</em>'.
   * @see com.bichler.iec.iec.TimeOfDayLiteral#getMinute()
   * @see #getTimeOfDayLiteral()
   * @generated
   */
  EAttribute getTimeOfDayLiteral_Minute();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.TimeOfDayLiteral#getSecond <em>Second</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Second</em>'.
   * @see com.bichler.iec.iec.TimeOfDayLiteral#getSecond()
   * @see #getTimeOfDayLiteral()
   * @generated
   */
  EAttribute getTimeOfDayLiteral_Second();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.DateLiteral <em>Date Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Date Literal</em>'.
   * @see com.bichler.iec.iec.DateLiteral
   * @generated
   */
  EClass getDateLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DateLiteral#getYear <em>Year</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Year</em>'.
   * @see com.bichler.iec.iec.DateLiteral#getYear()
   * @see #getDateLiteral()
   * @generated
   */
  EAttribute getDateLiteral_Year();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DateLiteral#getMonth <em>Month</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Month</em>'.
   * @see com.bichler.iec.iec.DateLiteral#getMonth()
   * @see #getDateLiteral()
   * @generated
   */
  EAttribute getDateLiteral_Month();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DateLiteral#getDay <em>Day</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Day</em>'.
   * @see com.bichler.iec.iec.DateLiteral#getDay()
   * @see #getDateLiteral()
   * @generated
   */
  EAttribute getDateLiteral_Day();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.DateAndTimeLiteral <em>Date And Time Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Date And Time Literal</em>'.
   * @see com.bichler.iec.iec.DateAndTimeLiteral
   * @generated
   */
  EClass getDateAndTimeLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DateAndTimeLiteral#getYear <em>Year</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Year</em>'.
   * @see com.bichler.iec.iec.DateAndTimeLiteral#getYear()
   * @see #getDateAndTimeLiteral()
   * @generated
   */
  EAttribute getDateAndTimeLiteral_Year();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DateAndTimeLiteral#getMonth <em>Month</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Month</em>'.
   * @see com.bichler.iec.iec.DateAndTimeLiteral#getMonth()
   * @see #getDateAndTimeLiteral()
   * @generated
   */
  EAttribute getDateAndTimeLiteral_Month();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DateAndTimeLiteral#getDay <em>Day</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Day</em>'.
   * @see com.bichler.iec.iec.DateAndTimeLiteral#getDay()
   * @see #getDateAndTimeLiteral()
   * @generated
   */
  EAttribute getDateAndTimeLiteral_Day();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DateAndTimeLiteral#getHour <em>Hour</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Hour</em>'.
   * @see com.bichler.iec.iec.DateAndTimeLiteral#getHour()
   * @see #getDateAndTimeLiteral()
   * @generated
   */
  EAttribute getDateAndTimeLiteral_Hour();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DateAndTimeLiteral#getMinute <em>Minute</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Minute</em>'.
   * @see com.bichler.iec.iec.DateAndTimeLiteral#getMinute()
   * @see #getDateAndTimeLiteral()
   * @generated
   */
  EAttribute getDateAndTimeLiteral_Minute();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.DateAndTimeLiteral#getSecond <em>Second</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Second</em>'.
   * @see com.bichler.iec.iec.DateAndTimeLiteral#getSecond()
   * @see #getDateAndTimeLiteral()
   * @generated
   */
  EAttribute getDateAndTimeLiteral_Second();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.BinaryExpression <em>Binary Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binary Expression</em>'.
   * @see com.bichler.iec.iec.BinaryExpression
   * @generated
   */
  EClass getBinaryExpression();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.BinaryExpression#getLeft <em>Left</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left</em>'.
   * @see com.bichler.iec.iec.BinaryExpression#getLeft()
   * @see #getBinaryExpression()
   * @generated
   */
  EReference getBinaryExpression_Left();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.BinaryExpression#getOperator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operator</em>'.
   * @see com.bichler.iec.iec.BinaryExpression#getOperator()
   * @see #getBinaryExpression()
   * @generated
   */
  EAttribute getBinaryExpression_Operator();

  /**
   * Returns the meta object for the containment reference '{@link com.bichler.iec.iec.BinaryExpression#getRight <em>Right</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right</em>'.
   * @see com.bichler.iec.iec.BinaryExpression#getRight()
   * @see #getBinaryExpression()
   * @generated
   */
  EReference getBinaryExpression_Right();

  /**
   * Returns the meta object for class '{@link com.bichler.iec.iec.UnaryExpression <em>Unary Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Unary Expression</em>'.
   * @see com.bichler.iec.iec.UnaryExpression
   * @generated
   */
  EClass getUnaryExpression();

  /**
   * Returns the meta object for the attribute '{@link com.bichler.iec.iec.UnaryExpression#getOperator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operator</em>'.
   * @see com.bichler.iec.iec.UnaryExpression#getOperator()
   * @see #getUnaryExpression()
   * @generated
   */
  EAttribute getUnaryExpression_Operator();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  IecFactory getIecFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ModelImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getModel()
     * @generated
     */
    EClass MODEL = eINSTANCE.getModel();

    /**
     * The meta object literal for the '<em><b>Model Element</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__MODEL_ELEMENT = eINSTANCE.getModel_ModelElement();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ModelElementImpl <em>Model Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ModelElementImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getModelElement()
     * @generated
     */
    EClass MODEL_ELEMENT = eINSTANCE.getModelElement();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.LibraryElementImpl <em>Library Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.LibraryElementImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getLibraryElement()
     * @generated
     */
    EClass LIBRARY_ELEMENT = eINSTANCE.getLibraryElement();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.LibraryElementDeclarationImpl <em>Library Element Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.LibraryElementDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getLibraryElementDeclaration()
     * @generated
     */
    EClass LIBRARY_ELEMENT_DECLARATION = eINSTANCE.getLibraryElementDeclaration();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LIBRARY_ELEMENT_DECLARATION__NAME = eINSTANCE.getLibraryElementDeclaration_Name();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.DataTypeImpl <em>Data Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.DataTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getDataType()
     * @generated
     */
    EClass DATA_TYPE = eINSTANCE.getDataType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.NonGenericTypeImpl <em>Non Generic Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.NonGenericTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getNonGenericType()
     * @generated
     */
    EClass NON_GENERIC_TYPE = eINSTANCE.getNonGenericType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ElementaryTypeImpl <em>Elementary Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ElementaryTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getElementaryType()
     * @generated
     */
    EClass ELEMENTARY_TYPE = eINSTANCE.getElementaryType();

    /**
     * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ELEMENTARY_TYPE__TYPE_NAME = eINSTANCE.getElementaryType_TypeName();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.StringTypeImpl <em>String Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.StringTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getStringType()
     * @generated
     */
    EClass STRING_TYPE = eINSTANCE.getStringType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.NumericTypeImpl <em>Numeric Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.NumericTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getNumericType()
     * @generated
     */
    EClass NUMERIC_TYPE = eINSTANCE.getNumericType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.IntegerTypeImpl <em>Integer Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.IntegerTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getIntegerType()
     * @generated
     */
    EClass INTEGER_TYPE = eINSTANCE.getIntegerType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.SignedIntegerTypeImpl <em>Signed Integer Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.SignedIntegerTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getSignedIntegerType()
     * @generated
     */
    EClass SIGNED_INTEGER_TYPE = eINSTANCE.getSignedIntegerType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.PlainIntegerTypeImpl <em>Plain Integer Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.PlainIntegerTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getPlainIntegerType()
     * @generated
     */
    EClass PLAIN_INTEGER_TYPE = eINSTANCE.getPlainIntegerType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.UnsignedIntegerTypeImpl <em>Unsigned Integer Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.UnsignedIntegerTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getUnsignedIntegerType()
     * @generated
     */
    EClass UNSIGNED_INTEGER_TYPE = eINSTANCE.getUnsignedIntegerType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.RealTypeImpl <em>Real Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.RealTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getRealType()
     * @generated
     */
    EClass REAL_TYPE = eINSTANCE.getRealType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.DateTypeImpl <em>Date Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.DateTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getDateType()
     * @generated
     */
    EClass DATE_TYPE = eINSTANCE.getDateType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.BitStringTypeImpl <em>Bit String Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.BitStringTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getBitStringType()
     * @generated
     */
    EClass BIT_STRING_TYPE = eINSTANCE.getBitStringType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.GenericTypeImpl <em>Generic Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.GenericTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getGenericType()
     * @generated
     */
    EClass GENERIC_TYPE = eINSTANCE.getGenericType();

    /**
     * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GENERIC_TYPE__TYPE_NAME = eINSTANCE.getGenericType_TypeName();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.DerivedTypeImpl <em>Derived Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.DerivedTypeImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getDerivedType()
     * @generated
     */
    EClass DERIVED_TYPE = eINSTANCE.getDerivedType();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DERIVED_TYPE__NAME = eINSTANCE.getDerivedType_Name();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.DataTypeDeclarationImpl <em>Data Type Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.DataTypeDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getDataTypeDeclaration()
     * @generated
     */
    EClass DATA_TYPE_DECLARATION = eINSTANCE.getDataTypeDeclaration();

    /**
     * The meta object literal for the '<em><b>Type Declaration</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DATA_TYPE_DECLARATION__TYPE_DECLARATION = eINSTANCE.getDataTypeDeclaration_TypeDeclaration();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.TypeDeclarationImpl <em>Type Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.TypeDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getTypeDeclaration()
     * @generated
     */
    EClass TYPE_DECLARATION = eINSTANCE.getTypeDeclaration();

    /**
     * The meta object literal for the '<em><b>Derived Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TYPE_DECLARATION__DERIVED_TYPE = eINSTANCE.getTypeDeclaration_DerivedType();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.SimpleTypeDeclarationImpl <em>Simple Type Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.SimpleTypeDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getSimpleTypeDeclaration()
     * @generated
     */
    EClass SIMPLE_TYPE_DECLARATION = eINSTANCE.getSimpleTypeDeclaration();

    /**
     * The meta object literal for the '<em><b>Spec Init</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SIMPLE_TYPE_DECLARATION__SPEC_INIT = eINSTANCE.getSimpleTypeDeclaration_SpecInit();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.SpecInitImpl <em>Spec Init</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.SpecInitImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getSpecInit()
     * @generated
     */
    EClass SPEC_INIT = eINSTANCE.getSpecInit();

    /**
     * The meta object literal for the '<em><b>Base Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SPEC_INIT__BASE_TYPE = eINSTANCE.getSpecInit_BaseType();

    /**
     * The meta object literal for the '<em><b>Constant</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SPEC_INIT__CONSTANT = eINSTANCE.getSpecInit_Constant();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.RangeDeclarationImpl <em>Range Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.RangeDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getRangeDeclaration()
     * @generated
     */
    EClass RANGE_DECLARATION = eINSTANCE.getRangeDeclaration();

    /**
     * The meta object literal for the '<em><b>Base Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RANGE_DECLARATION__BASE_TYPE = eINSTANCE.getRangeDeclaration_BaseType();

    /**
     * The meta object literal for the '<em><b>Range</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RANGE_DECLARATION__RANGE = eINSTANCE.getRangeDeclaration_Range();

    /**
     * The meta object literal for the '<em><b>Constant</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RANGE_DECLARATION__CONSTANT = eINSTANCE.getRangeDeclaration_Constant();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.EnumDeclarationImpl <em>Enum Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.EnumDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getEnumDeclaration()
     * @generated
     */
    EClass ENUM_DECLARATION = eINSTANCE.getEnumDeclaration();

    /**
     * The meta object literal for the '<em><b>Enumeration</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ENUM_DECLARATION__ENUMERATION = eINSTANCE.getEnumDeclaration_Enumeration();

    /**
     * The meta object literal for the '<em><b>Constant</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ENUM_DECLARATION__CONSTANT = eINSTANCE.getEnumDeclaration_Constant();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ArrayDeclarationImpl <em>Array Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ArrayDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getArrayDeclaration()
     * @generated
     */
    EClass ARRAY_DECLARATION = eINSTANCE.getArrayDeclaration();

    /**
     * The meta object literal for the '<em><b>Ranges</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ARRAY_DECLARATION__RANGES = eINSTANCE.getArrayDeclaration_Ranges();

    /**
     * The meta object literal for the '<em><b>Base Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARRAY_DECLARATION__BASE_TYPE = eINSTANCE.getArrayDeclaration_BaseType();

    /**
     * The meta object literal for the '<em><b>Constant</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARRAY_DECLARATION__CONSTANT = eINSTANCE.getArrayDeclaration_Constant();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.EnumerationImpl <em>Enumeration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.EnumerationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getEnumeration()
     * @generated
     */
    EClass ENUMERATION = eINSTANCE.getEnumeration();

    /**
     * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ENUMERATION__VALUES = eINSTANCE.getEnumeration_Values();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.EnumeratedValueImpl <em>Enumerated Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.EnumeratedValueImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getEnumeratedValue()
     * @generated
     */
    EClass ENUMERATED_VALUE = eINSTANCE.getEnumeratedValue();

    /**
     * The meta object literal for the '<em><b>Derived Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ENUMERATED_VALUE__DERIVED_TYPE = eINSTANCE.getEnumeratedValue_DerivedType();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENUMERATED_VALUE__NAME = eINSTANCE.getEnumeratedValue_Name();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ArrayInitializationImpl <em>Array Initialization</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ArrayInitializationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getArrayInitialization()
     * @generated
     */
    EClass ARRAY_INITIALIZATION = eINSTANCE.getArrayInitialization();

    /**
     * The meta object literal for the '<em><b>Initial Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARRAY_INITIALIZATION__INITIAL_ELEMENTS = eINSTANCE.getArrayInitialization_InitialElements();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ArrayInitialElementsImpl <em>Array Initial Elements</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ArrayInitialElementsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getArrayInitialElements()
     * @generated
     */
    EClass ARRAY_INITIAL_ELEMENTS = eINSTANCE.getArrayInitialElements();

    /**
     * The meta object literal for the '<em><b>Initial Element</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT = eINSTANCE.getArrayInitialElements_InitialElement();

    /**
     * The meta object literal for the '<em><b>Index</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ARRAY_INITIAL_ELEMENTS__INDEX = eINSTANCE.getArrayInitialElements_Index();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.InitialElementImpl <em>Initial Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.InitialElementImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getInitialElement()
     * @generated
     */
    EClass INITIAL_ELEMENT = eINSTANCE.getInitialElement();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.StructureTypeDeclarationImpl <em>Structure Type Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.StructureTypeDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructureTypeDeclaration()
     * @generated
     */
    EClass STRUCTURE_TYPE_DECLARATION = eINSTANCE.getStructureTypeDeclaration();

    /**
     * The meta object literal for the '<em><b>Declaration</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRUCTURE_TYPE_DECLARATION__DECLARATION = eINSTANCE.getStructureTypeDeclaration_Declaration();

    /**
     * The meta object literal for the '<em><b>Initialization</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRUCTURE_TYPE_DECLARATION__INITIALIZATION = eINSTANCE.getStructureTypeDeclaration_Initialization();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.StructureDeclarationImpl <em>Structure Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.StructureDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructureDeclaration()
     * @generated
     */
    EClass STRUCTURE_DECLARATION = eINSTANCE.getStructureDeclaration();

    /**
     * The meta object literal for the '<em><b>Structure Element</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRUCTURE_DECLARATION__STRUCTURE_ELEMENT = eINSTANCE.getStructureDeclaration_StructureElement();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.StructureElementDeclarationImpl <em>Structure Element Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.StructureElementDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructureElementDeclaration()
     * @generated
     */
    EClass STRUCTURE_ELEMENT_DECLARATION = eINSTANCE.getStructureElementDeclaration();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRUCTURE_ELEMENT_DECLARATION__NAME = eINSTANCE.getStructureElementDeclaration_Name();

    /**
     * The meta object literal for the '<em><b>Spec Init</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRUCTURE_ELEMENT_DECLARATION__SPEC_INIT = eINSTANCE.getStructureElementDeclaration_SpecInit();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.InitializedStructureImpl <em>Initialized Structure</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.InitializedStructureImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getInitializedStructure()
     * @generated
     */
    EClass INITIALIZED_STRUCTURE = eINSTANCE.getInitializedStructure();

    /**
     * The meta object literal for the '<em><b>Derived Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INITIALIZED_STRUCTURE__DERIVED_TYPE = eINSTANCE.getInitializedStructure_DerivedType();

    /**
     * The meta object literal for the '<em><b>Initialization</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INITIALIZED_STRUCTURE__INITIALIZATION = eINSTANCE.getInitializedStructure_Initialization();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.StructureInitializationImpl <em>Structure Initialization</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.StructureInitializationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructureInitialization()
     * @generated
     */
    EClass STRUCTURE_INITIALIZATION = eINSTANCE.getStructureInitialization();

    /**
     * The meta object literal for the '<em><b>Initial Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRUCTURE_INITIALIZATION__INITIAL_ELEMENTS = eINSTANCE.getStructureInitialization_InitialElements();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.StructureElementInitializationImpl <em>Structure Element Initialization</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.StructureElementInitializationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructureElementInitialization()
     * @generated
     */
    EClass STRUCTURE_ELEMENT_INITIALIZATION = eINSTANCE.getStructureElementInitialization();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRUCTURE_ELEMENT_INITIALIZATION__NAME = eINSTANCE.getStructureElementInitialization_Name();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRUCTURE_ELEMENT_INITIALIZATION__VALUE = eINSTANCE.getStructureElementInitialization_Value();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.StringDeclarationImpl <em>String Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.StringDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getStringDeclaration()
     * @generated
     */
    EClass STRING_DECLARATION = eINSTANCE.getStringDeclaration();

    /**
     * The meta object literal for the '<em><b>String</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_DECLARATION__STRING = eINSTANCE.getStringDeclaration_String();

    /**
     * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_DECLARATION__SIZE = eINSTANCE.getStringDeclaration_Size();

    /**
     * The meta object literal for the '<em><b>Initial Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRING_DECLARATION__INITIAL_VALUE = eINSTANCE.getStringDeclaration_InitialValue();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.VariableImpl <em>Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.VariableImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getVariable()
     * @generated
     */
    EClass VARIABLE = eINSTANCE.getVariable();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE__NAME = eINSTANCE.getVariable_Name();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.VariableAccessImpl <em>Variable Access</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.VariableAccessImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getVariableAccess()
     * @generated
     */
    EClass VARIABLE_ACCESS = eINSTANCE.getVariableAccess();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.DirectVariableImpl <em>Direct Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.DirectVariableImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getDirectVariable()
     * @generated
     */
    EClass DIRECT_VARIABLE = eINSTANCE.getDirectVariable();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DIRECT_VARIABLE__NAME = eINSTANCE.getDirectVariable_Name();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.SymbolicVariableAccessImpl <em>Symbolic Variable Access</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.SymbolicVariableAccessImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getSymbolicVariableAccess()
     * @generated
     */
    EClass SYMBOLIC_VARIABLE_ACCESS = eINSTANCE.getSymbolicVariableAccess();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.NamedVariableAccessImpl <em>Named Variable Access</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.NamedVariableAccessImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getNamedVariableAccess()
     * @generated
     */
    EClass NAMED_VARIABLE_ACCESS = eINSTANCE.getNamedVariableAccess();

    /**
     * The meta object literal for the '<em><b>Named Variable</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference NAMED_VARIABLE_ACCESS__NAMED_VARIABLE = eINSTANCE.getNamedVariableAccess_NamedVariable();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.MultiElementVariableImpl <em>Multi Element Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.MultiElementVariableImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getMultiElementVariable()
     * @generated
     */
    EClass MULTI_ELEMENT_VARIABLE = eINSTANCE.getMultiElementVariable();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ArrayVariableImpl <em>Array Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ArrayVariableImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getArrayVariable()
     * @generated
     */
    EClass ARRAY_VARIABLE = eINSTANCE.getArrayVariable();

    /**
     * The meta object literal for the '<em><b>Subscripted Variable</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARRAY_VARIABLE__SUBSCRIPTED_VARIABLE = eINSTANCE.getArrayVariable_SubscriptedVariable();

    /**
     * The meta object literal for the '<em><b>Subscripts</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARRAY_VARIABLE__SUBSCRIPTS = eINSTANCE.getArrayVariable_Subscripts();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.StructuredVariableImpl <em>Structured Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.StructuredVariableImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getStructuredVariable()
     * @generated
     */
    EClass STRUCTURED_VARIABLE = eINSTANCE.getStructuredVariable();

    /**
     * The meta object literal for the '<em><b>Record Variable</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRUCTURED_VARIABLE__RECORD_VARIABLE = eINSTANCE.getStructuredVariable_RecordVariable();

    /**
     * The meta object literal for the '<em><b>Field Selector</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRUCTURED_VARIABLE__FIELD_SELECTOR = eINSTANCE.getStructuredVariable_FieldSelector();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ExpressionImpl <em>Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ExpressionImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getExpression()
     * @generated
     */
    EClass EXPRESSION = eINSTANCE.getExpression();

    /**
     * The meta object literal for the '<em><b>Variable</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXPRESSION__VARIABLE = eINSTANCE.getExpression_Variable();

    /**
     * The meta object literal for the '<em><b>Fbname</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXPRESSION__FBNAME = eINSTANCE.getExpression_Fbname();

    /**
     * The meta object literal for the '<em><b>Openbr</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute EXPRESSION__OPENBR = eINSTANCE.getExpression_Openbr();

    /**
     * The meta object literal for the '<em><b>Paramassignment</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXPRESSION__PARAMASSIGNMENT = eINSTANCE.getExpression_Paramassignment();

    /**
     * The meta object literal for the '<em><b>Closebr</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute EXPRESSION__CLOSEBR = eINSTANCE.getExpression_Closebr();

    /**
     * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXPRESSION__EXPRESSION = eINSTANCE.getExpression_Expression();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ParamAssignmentImpl <em>Param Assignment</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ParamAssignmentImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getParamAssignment()
     * @generated
     */
    EClass PARAM_ASSIGNMENT = eINSTANCE.getParamAssignment();

    /**
     * The meta object literal for the '<em><b>Variablename</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAM_ASSIGNMENT__VARIABLENAME = eINSTANCE.getParamAssignment_Variablename();

    /**
     * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAM_ASSIGNMENT__EXPRESSION = eINSTANCE.getParamAssignment_Expression();

    /**
     * The meta object literal for the '<em><b>Not</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAM_ASSIGNMENT__NOT = eINSTANCE.getParamAssignment_Not();

    /**
     * The meta object literal for the '<em><b>Variable1</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAM_ASSIGNMENT__VARIABLE1 = eINSTANCE.getParamAssignment_Variable1();

    /**
     * The meta object literal for the '<em><b>Variable2</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAM_ASSIGNMENT__VARIABLE2 = eINSTANCE.getParamAssignment_Variable2();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.FunctionDeclarationImpl <em>Function Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.FunctionDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getFunctionDeclaration()
     * @generated
     */
    EClass FUNCTION_DECLARATION = eINSTANCE.getFunctionDeclaration();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FUNCTION_DECLARATION__NAME = eINSTANCE.getFunctionDeclaration_Name();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FUNCTION_DECLARATION__TYPE = eINSTANCE.getFunctionDeclaration_Type();

    /**
     * The meta object literal for the '<em><b>Io Var Declarations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FUNCTION_DECLARATION__IO_VAR_DECLARATIONS = eINSTANCE.getFunctionDeclaration_IoVarDeclarations();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FUNCTION_DECLARATION__BODY = eINSTANCE.getFunctionDeclaration_Body();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.IoVarDeclarationsImpl <em>Io Var Declarations</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.IoVarDeclarationsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getIoVarDeclarations()
     * @generated
     */
    EClass IO_VAR_DECLARATIONS = eINSTANCE.getIoVarDeclarations();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.InputDeclarationsImpl <em>Input Declarations</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.InputDeclarationsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getInputDeclarations()
     * @generated
     */
    EClass INPUT_DECLARATIONS = eINSTANCE.getInputDeclarations();

    /**
     * The meta object literal for the '<em><b>Declarations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INPUT_DECLARATIONS__DECLARATIONS = eINSTANCE.getInputDeclarations_Declarations();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.InputDeclarationImpl <em>Input Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.InputDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getInputDeclaration()
     * @generated
     */
    EClass INPUT_DECLARATION = eINSTANCE.getInputDeclaration();

    /**
     * The meta object literal for the '<em><b>Var1 List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INPUT_DECLARATION__VAR1_LIST = eINSTANCE.getInputDeclaration_Var1List();

    /**
     * The meta object literal for the '<em><b>Decl Specification</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INPUT_DECLARATION__DECL_SPECIFICATION = eINSTANCE.getInputDeclaration_DeclSpecification();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.EdgeDeclarationImpl <em>Edge Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.EdgeDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getEdgeDeclaration()
     * @generated
     */
    EClass EDGE_DECLARATION = eINSTANCE.getEdgeDeclaration();

    /**
     * The meta object literal for the '<em><b>Var1 List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EDGE_DECLARATION__VAR1_LIST = eINSTANCE.getEdgeDeclaration_Var1List();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.DeclSpecificationImpl <em>Decl Specification</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.DeclSpecificationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getDeclSpecification()
     * @generated
     */
    EClass DECL_SPECIFICATION = eINSTANCE.getDeclSpecification();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.VarDeclSpecificationImpl <em>Var Decl Specification</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.VarDeclSpecificationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getVarDeclSpecification()
     * @generated
     */
    EClass VAR_DECL_SPECIFICATION = eINSTANCE.getVarDeclSpecification();

    /**
     * The meta object literal for the '<em><b>Spec Init</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference VAR_DECL_SPECIFICATION__SPEC_INIT = eINSTANCE.getVarDeclSpecification_SpecInit();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.EdgeDeclSpecificationImpl <em>Edge Decl Specification</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.EdgeDeclSpecificationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getEdgeDeclSpecification()
     * @generated
     */
    EClass EDGE_DECL_SPECIFICATION = eINSTANCE.getEdgeDeclSpecification();

    /**
     * The meta object literal for the '<em><b>REdge</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute EDGE_DECL_SPECIFICATION__REDGE = eINSTANCE.getEdgeDeclSpecification_REdge();

    /**
     * The meta object literal for the '<em><b>FEdge</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute EDGE_DECL_SPECIFICATION__FEDGE = eINSTANCE.getEdgeDeclSpecification_FEdge();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.Var1ListImpl <em>Var1 List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.Var1ListImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getVar1List()
     * @generated
     */
    EClass VAR1_LIST = eINSTANCE.getVar1List();

    /**
     * The meta object literal for the '<em><b>Variables</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference VAR1_LIST__VARIABLES = eINSTANCE.getVar1List_Variables();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.OutputDeclarationsImpl <em>Output Declarations</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.OutputDeclarationsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getOutputDeclarations()
     * @generated
     */
    EClass OUTPUT_DECLARATIONS = eINSTANCE.getOutputDeclarations();

    /**
     * The meta object literal for the '<em><b>Init Decls</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OUTPUT_DECLARATIONS__INIT_DECLS = eINSTANCE.getOutputDeclarations_InitDecls();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.VarInitDeclImpl <em>Var Init Decl</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.VarInitDeclImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getVarInitDecl()
     * @generated
     */
    EClass VAR_INIT_DECL = eINSTANCE.getVarInitDecl();

    /**
     * The meta object literal for the '<em><b>Var1 List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference VAR_INIT_DECL__VAR1_LIST = eINSTANCE.getVarInitDecl_Var1List();

    /**
     * The meta object literal for the '<em><b>Spec Init</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference VAR_INIT_DECL__SPEC_INIT = eINSTANCE.getVarInitDecl_SpecInit();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.InputOutputDeclarationsImpl <em>Input Output Declarations</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.InputOutputDeclarationsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getInputOutputDeclarations()
     * @generated
     */
    EClass INPUT_OUTPUT_DECLARATIONS = eINSTANCE.getInputOutputDeclarations();

    /**
     * The meta object literal for the '<em><b>Init Decls</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INPUT_OUTPUT_DECLARATIONS__INIT_DECLS = eINSTANCE.getInputOutputDeclarations_InitDecls();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.FunctionBodyImpl <em>Function Body</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.FunctionBodyImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getFunctionBody()
     * @generated
     */
    EClass FUNCTION_BODY = eINSTANCE.getFunctionBody();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.InstructionListImpl <em>Instruction List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.InstructionListImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getInstructionList()
     * @generated
     */
    EClass INSTRUCTION_LIST = eINSTANCE.getInstructionList();

    /**
     * The meta object literal for the '<em><b>Instructions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INSTRUCTION_LIST__INSTRUCTIONS = eINSTANCE.getInstructionList_Instructions();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.InstructionImpl <em>Instruction</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.InstructionImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getInstruction()
     * @generated
     */
    EClass INSTRUCTION = eINSTANCE.getInstruction();

    /**
     * The meta object literal for the '<em><b>Label</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INSTRUCTION__LABEL = eINSTANCE.getInstruction_Label();

    /**
     * The meta object literal for the '<em><b>Instruction</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INSTRUCTION__INSTRUCTION = eINSTANCE.getInstruction_Instruction();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.LabelImpl <em>Label</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.LabelImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getLabel()
     * @generated
     */
    EClass LABEL = eINSTANCE.getLabel();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LABEL__NAME = eINSTANCE.getLabel_Name();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.OperationImpl <em>Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.OperationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getOperation()
     * @generated
     */
    EClass OPERATION = eINSTANCE.getOperation();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.SimpleOperationImpl <em>Simple Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.SimpleOperationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getSimpleOperation()
     * @generated
     */
    EClass SIMPLE_OPERATION = eINSTANCE.getSimpleOperation();

    /**
     * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SIMPLE_OPERATION__OPERATOR = eINSTANCE.getSimpleOperation_Operator();

    /**
     * The meta object literal for the '<em><b>Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SIMPLE_OPERATION__OPERAND = eINSTANCE.getSimpleOperation_Operand();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ExpressionOperationImpl <em>Expression Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ExpressionOperationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getExpressionOperation()
     * @generated
     */
    EClass EXPRESSION_OPERATION = eINSTANCE.getExpressionOperation();

    /**
     * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute EXPRESSION_OPERATION__OPERATOR = eINSTANCE.getExpressionOperation_Operator();

    /**
     * The meta object literal for the '<em><b>Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXPRESSION_OPERATION__OPERAND = eINSTANCE.getExpressionOperation_Operand();

    /**
     * The meta object literal for the '<em><b>Simple Instruction List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST = eINSTANCE.getExpressionOperation_SimpleInstructionList();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.JmpOperationImpl <em>Jmp Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.JmpOperationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getJmpOperation()
     * @generated
     */
    EClass JMP_OPERATION = eINSTANCE.getJmpOperation();

    /**
     * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JMP_OPERATION__OPERATOR = eINSTANCE.getJmpOperation_Operator();

    /**
     * The meta object literal for the '<em><b>Label</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference JMP_OPERATION__LABEL = eINSTANCE.getJmpOperation_Label();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.OperandImpl <em>Operand</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.OperandImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getOperand()
     * @generated
     */
    EClass OPERAND = eINSTANCE.getOperand();

    /**
     * The meta object literal for the '<em><b>Constant</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERAND__CONSTANT = eINSTANCE.getOperand_Constant();

    /**
     * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERAND__REFERENCE = eINSTANCE.getOperand_Reference();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ReferencedOperandImpl <em>Referenced Operand</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ReferencedOperandImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getReferencedOperand()
     * @generated
     */
    EClass REFERENCED_OPERAND = eINSTANCE.getReferencedOperand();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.SimpleInstructionListImpl <em>Simple Instruction List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.SimpleInstructionListImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getSimpleInstructionList()
     * @generated
     */
    EClass SIMPLE_INSTRUCTION_LIST = eINSTANCE.getSimpleInstructionList();

    /**
     * The meta object literal for the '<em><b>Instructions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SIMPLE_INSTRUCTION_LIST__INSTRUCTIONS = eINSTANCE.getSimpleInstructionList_Instructions();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.SimpleInstructionImpl <em>Simple Instruction</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.SimpleInstructionImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getSimpleInstruction()
     * @generated
     */
    EClass SIMPLE_INSTRUCTION = eINSTANCE.getSimpleInstruction();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.StatementListImpl <em>Statement List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.StatementListImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getStatementList()
     * @generated
     */
    EClass STATEMENT_LIST = eINSTANCE.getStatementList();

    /**
     * The meta object literal for the '<em><b>Statements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STATEMENT_LIST__STATEMENTS = eINSTANCE.getStatementList_Statements();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.StatementImpl <em>Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.StatementImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getStatement()
     * @generated
     */
    EClass STATEMENT = eINSTANCE.getStatement();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.AssignStatementImpl <em>Assign Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.AssignStatementImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getAssignStatement()
     * @generated
     */
    EClass ASSIGN_STATEMENT = eINSTANCE.getAssignStatement();

    /**
     * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ASSIGN_STATEMENT__VARIABLE = eINSTANCE.getAssignStatement_Variable();

    /**
     * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ASSIGN_STATEMENT__EXPRESSION = eINSTANCE.getAssignStatement_Expression();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.SelectionStatementImpl <em>Selection Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.SelectionStatementImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getSelectionStatement()
     * @generated
     */
    EClass SELECTION_STATEMENT = eINSTANCE.getSelectionStatement();

    /**
     * The meta object literal for the '<em><b>Else Statement List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SELECTION_STATEMENT__ELSE_STATEMENT_LIST = eINSTANCE.getSelectionStatement_ElseStatementList();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.IfStatementImpl <em>If Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.IfStatementImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getIfStatement()
     * @generated
     */
    EClass IF_STATEMENT = eINSTANCE.getIfStatement();

    /**
     * The meta object literal for the '<em><b>If Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IF_STATEMENT__IF_EXPRESSION = eINSTANCE.getIfStatement_IfExpression();

    /**
     * The meta object literal for the '<em><b>Then Statement List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IF_STATEMENT__THEN_STATEMENT_LIST = eINSTANCE.getIfStatement_ThenStatementList();

    /**
     * The meta object literal for the '<em><b>Else Ifs</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IF_STATEMENT__ELSE_IFS = eINSTANCE.getIfStatement_ElseIfs();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ElseIfImpl <em>Else If</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ElseIfImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getElseIf()
     * @generated
     */
    EClass ELSE_IF = eINSTANCE.getElseIf();

    /**
     * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ELSE_IF__EXPRESSION = eINSTANCE.getElseIf_Expression();

    /**
     * The meta object literal for the '<em><b>Statement List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ELSE_IF__STATEMENT_LIST = eINSTANCE.getElseIf_StatementList();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.CaseStatementImpl <em>Case Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.CaseStatementImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getCaseStatement()
     * @generated
     */
    EClass CASE_STATEMENT = eINSTANCE.getCaseStatement();

    /**
     * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CASE_STATEMENT__EXPRESSION = eINSTANCE.getCaseStatement_Expression();

    /**
     * The meta object literal for the '<em><b>Case Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CASE_STATEMENT__CASE_ELEMENTS = eINSTANCE.getCaseStatement_CaseElements();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.CaseElementImpl <em>Case Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.CaseElementImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getCaseElement()
     * @generated
     */
    EClass CASE_ELEMENT = eINSTANCE.getCaseElement();

    /**
     * The meta object literal for the '<em><b>Case List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CASE_ELEMENT__CASE_LIST = eINSTANCE.getCaseElement_CaseList();

    /**
     * The meta object literal for the '<em><b>Statement List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CASE_ELEMENT__STATEMENT_LIST = eINSTANCE.getCaseElement_StatementList();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.CaseListImpl <em>Case List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.CaseListImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getCaseList()
     * @generated
     */
    EClass CASE_LIST = eINSTANCE.getCaseList();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CASE_LIST__ELEMENTS = eINSTANCE.getCaseList_Elements();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.CaseListElementImpl <em>Case List Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.CaseListElementImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getCaseListElement()
     * @generated
     */
    EClass CASE_LIST_ELEMENT = eINSTANCE.getCaseListElement();

    /**
     * The meta object literal for the '<em><b>Sub Range</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CASE_LIST_ELEMENT__SUB_RANGE = eINSTANCE.getCaseListElement_SubRange();

    /**
     * The meta object literal for the '<em><b>Integer</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CASE_LIST_ELEMENT__INTEGER = eINSTANCE.getCaseListElement_Integer();

    /**
     * The meta object literal for the '<em><b>Enumerated Value</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CASE_LIST_ELEMENT__ENUMERATED_VALUE = eINSTANCE.getCaseListElement_EnumeratedValue();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.FunctionBlockDeclarationImpl <em>Function Block Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.FunctionBlockDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getFunctionBlockDeclaration()
     * @generated
     */
    EClass FUNCTION_BLOCK_DECLARATION = eINSTANCE.getFunctionBlockDeclaration();

    /**
     * The meta object literal for the '<em><b>Var Declarations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FUNCTION_BLOCK_DECLARATION__VAR_DECLARATIONS = eINSTANCE.getFunctionBlockDeclaration_VarDeclarations();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FUNCTION_BLOCK_DECLARATION__BODY = eINSTANCE.getFunctionBlockDeclaration_Body();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.FunctionBlockVarDeclarationsImpl <em>Function Block Var Declarations</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.FunctionBlockVarDeclarationsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getFunctionBlockVarDeclarations()
     * @generated
     */
    EClass FUNCTION_BLOCK_VAR_DECLARATIONS = eINSTANCE.getFunctionBlockVarDeclarations();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.OtherVarDeclarationsImpl <em>Other Var Declarations</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.OtherVarDeclarationsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getOtherVarDeclarations()
     * @generated
     */
    EClass OTHER_VAR_DECLARATIONS = eINSTANCE.getOtherVarDeclarations();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.VarDeclarationsImpl <em>Var Declarations</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.VarDeclarationsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getVarDeclarations()
     * @generated
     */
    EClass VAR_DECLARATIONS = eINSTANCE.getVarDeclarations();

    /**
     * The meta object literal for the '<em><b>Constant</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VAR_DECLARATIONS__CONSTANT = eINSTANCE.getVarDeclarations_Constant();

    /**
     * The meta object literal for the '<em><b>Init Decls</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference VAR_DECLARATIONS__INIT_DECLS = eINSTANCE.getVarDeclarations_InitDecls();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.FunctionBlockBodyImpl <em>Function Block Body</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.FunctionBlockBodyImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getFunctionBlockBody()
     * @generated
     */
    EClass FUNCTION_BLOCK_BODY = eINSTANCE.getFunctionBlockBody();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ProgramDeclarationImpl <em>Program Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ProgramDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgramDeclaration()
     * @generated
     */
    EClass PROGRAM_DECLARATION = eINSTANCE.getProgramDeclaration();

    /**
     * The meta object literal for the '<em><b>Var Declarations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROGRAM_DECLARATION__VAR_DECLARATIONS = eINSTANCE.getProgramDeclaration_VarDeclarations();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROGRAM_DECLARATION__BODY = eINSTANCE.getProgramDeclaration_Body();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ProgramVarDeclarationsImpl <em>Program Var Declarations</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ProgramVarDeclarationsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgramVarDeclarations()
     * @generated
     */
    EClass PROGRAM_VAR_DECLARATIONS = eINSTANCE.getProgramVarDeclarations();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.LocatedVarDeclarationsImpl <em>Located Var Declarations</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.LocatedVarDeclarationsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getLocatedVarDeclarations()
     * @generated
     */
    EClass LOCATED_VAR_DECLARATIONS = eINSTANCE.getLocatedVarDeclarations();

    /**
     * The meta object literal for the '<em><b>Located Var Declaration</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference LOCATED_VAR_DECLARATIONS__LOCATED_VAR_DECLARATION = eINSTANCE.getLocatedVarDeclarations_LocatedVarDeclaration();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.LocatedVarDeclarationImpl <em>Located Var Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.LocatedVarDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getLocatedVarDeclaration()
     * @generated
     */
    EClass LOCATED_VAR_DECLARATION = eINSTANCE.getLocatedVarDeclaration();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LOCATED_VAR_DECLARATION__NAME = eINSTANCE.getLocatedVarDeclaration_Name();

    /**
     * The meta object literal for the '<em><b>Location</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference LOCATED_VAR_DECLARATION__LOCATION = eINSTANCE.getLocatedVarDeclaration_Location();

    /**
     * The meta object literal for the '<em><b>Spec Init</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference LOCATED_VAR_DECLARATION__SPEC_INIT = eINSTANCE.getLocatedVarDeclaration_SpecInit();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.LocationImpl <em>Location</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.LocationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getLocation()
     * @generated
     */
    EClass LOCATION = eINSTANCE.getLocation();

    /**
     * The meta object literal for the '<em><b>Direct Variable</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference LOCATION__DIRECT_VARIABLE = eINSTANCE.getLocation_DirectVariable();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ProgramAccessDeclsImpl <em>Program Access Decls</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ProgramAccessDeclsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgramAccessDecls()
     * @generated
     */
    EClass PROGRAM_ACCESS_DECLS = eINSTANCE.getProgramAccessDecls();

    /**
     * The meta object literal for the '<em><b>Program Access Decl</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROGRAM_ACCESS_DECLS__PROGRAM_ACCESS_DECL = eINSTANCE.getProgramAccessDecls_ProgramAccessDecl();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ProgramAccessDeclImpl <em>Program Access Decl</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ProgramAccessDeclImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgramAccessDecl()
     * @generated
     */
    EClass PROGRAM_ACCESS_DECL = eINSTANCE.getProgramAccessDecl();

    /**
     * The meta object literal for the '<em><b>Access Name</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROGRAM_ACCESS_DECL__ACCESS_NAME = eINSTANCE.getProgramAccessDecl_AccessName();

    /**
     * The meta object literal for the '<em><b>Symbolic Variable</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE = eINSTANCE.getProgramAccessDecl_SymbolicVariable();

    /**
     * The meta object literal for the '<em><b>Type Name</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROGRAM_ACCESS_DECL__TYPE_NAME = eINSTANCE.getProgramAccessDecl_TypeName();

    /**
     * The meta object literal for the '<em><b>Direction</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROGRAM_ACCESS_DECL__DIRECTION = eINSTANCE.getProgramAccessDecl_Direction();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ConfigurationDeclarationImpl <em>Configuration Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ConfigurationDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getConfigurationDeclaration()
     * @generated
     */
    EClass CONFIGURATION_DECLARATION = eINSTANCE.getConfigurationDeclaration();

    /**
     * The meta object literal for the '<em><b>Global Var Declarations</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS = eINSTANCE.getConfigurationDeclaration_GlobalVarDeclarations();

    /**
     * The meta object literal for the '<em><b>Resdecl</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CONFIGURATION_DECLARATION__RESDECL = eINSTANCE.getConfigurationDeclaration_Resdecl();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ResourceDeclarationImpl <em>Resource Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ResourceDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getResourceDeclaration()
     * @generated
     */
    EClass RESOURCE_DECLARATION = eINSTANCE.getResourceDeclaration();

    /**
     * The meta object literal for the '<em><b>Resname</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RESOURCE_DECLARATION__RESNAME = eINSTANCE.getResourceDeclaration_Resname();

    /**
     * The meta object literal for the '<em><b>Global Var Declarations</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS = eINSTANCE.getResourceDeclaration_GlobalVarDeclarations();

    /**
     * The meta object literal for the '<em><b>Singleresource</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RESOURCE_DECLARATION__SINGLERESOURCE = eINSTANCE.getResourceDeclaration_Singleresource();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.SingleResourceDeclarationImpl <em>Single Resource Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.SingleResourceDeclarationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getSingleResourceDeclaration()
     * @generated
     */
    EClass SINGLE_RESOURCE_DECLARATION = eINSTANCE.getSingleResourceDeclaration();

    /**
     * The meta object literal for the '<em><b>Task Conf</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SINGLE_RESOURCE_DECLARATION__TASK_CONF = eINSTANCE.getSingleResourceDeclaration_TaskConf();

    /**
     * The meta object literal for the '<em><b>Program Conf</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SINGLE_RESOURCE_DECLARATION__PROGRAM_CONF = eINSTANCE.getSingleResourceDeclaration_ProgramConf();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ProgramConfigurationImpl <em>Program Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ProgramConfigurationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgramConfiguration()
     * @generated
     */
    EClass PROGRAM_CONFIGURATION = eINSTANCE.getProgramConfiguration();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROGRAM_CONFIGURATION__NAME = eINSTANCE.getProgramConfiguration_Name();

    /**
     * The meta object literal for the '<em><b>Task</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROGRAM_CONFIGURATION__TASK = eINSTANCE.getProgramConfiguration_Task();

    /**
     * The meta object literal for the '<em><b>Prog</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROGRAM_CONFIGURATION__PROG = eINSTANCE.getProgramConfiguration_Prog();

    /**
     * The meta object literal for the '<em><b>Prog Conf</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROGRAM_CONFIGURATION__PROG_CONF = eINSTANCE.getProgramConfiguration_ProgConf();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ProgConfElementsImpl <em>Prog Conf Elements</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ProgConfElementsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgConfElements()
     * @generated
     */
    EClass PROG_CONF_ELEMENTS = eINSTANCE.getProgConfElements();

    /**
     * The meta object literal for the '<em><b>Progconf</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROG_CONF_ELEMENTS__PROGCONF = eINSTANCE.getProgConfElements_Progconf();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ProgConfElementImpl <em>Prog Conf Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ProgConfElementImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgConfElement()
     * @generated
     */
    EClass PROG_CONF_ELEMENT = eINSTANCE.getProgConfElement();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.FBTaskImpl <em>FB Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.FBTaskImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getFBTask()
     * @generated
     */
    EClass FB_TASK = eINSTANCE.getFBTask();

    /**
     * The meta object literal for the '<em><b>Fbname</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FB_TASK__FBNAME = eINSTANCE.getFBTask_Fbname();

    /**
     * The meta object literal for the '<em><b>Task</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FB_TASK__TASK = eINSTANCE.getFBTask_Task();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ProgCNXNImpl <em>Prog CNXN</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ProgCNXNImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgCNXN()
     * @generated
     */
    EClass PROG_CNXN = eINSTANCE.getProgCNXN();

    /**
     * The meta object literal for the '<em><b>Variablename</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROG_CNXN__VARIABLENAME = eINSTANCE.getProgCNXN_Variablename();

    /**
     * The meta object literal for the '<em><b>Progd</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROG_CNXN__PROGD = eINSTANCE.getProgCNXN_Progd();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.DataSinkImpl <em>Data Sink</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.DataSinkImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getDataSink()
     * @generated
     */
    EClass DATA_SINK = eINSTANCE.getDataSink();

    /**
     * The meta object literal for the '<em><b>Globvar</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DATA_SINK__GLOBVAR = eINSTANCE.getDataSink_Globvar();

    /**
     * The meta object literal for the '<em><b>Dirvar</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DATA_SINK__DIRVAR = eINSTANCE.getDataSink_Dirvar();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ProgDataSourceImpl <em>Prog Data Source</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ProgDataSourceImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getProgDataSource()
     * @generated
     */
    EClass PROG_DATA_SOURCE = eINSTANCE.getProgDataSource();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.GlobalVarDeclarationsImpl <em>Global Var Declarations</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.GlobalVarDeclarationsImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getGlobalVarDeclarations()
     * @generated
     */
    EClass GLOBAL_VAR_DECLARATIONS = eINSTANCE.getGlobalVarDeclarations();

    /**
     * The meta object literal for the '<em><b>Global Var Decl</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GLOBAL_VAR_DECLARATIONS__GLOBAL_VAR_DECL = eINSTANCE.getGlobalVarDeclarations_GlobalVarDecl();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.GlobalVarDeclImpl <em>Global Var Decl</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.GlobalVarDeclImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getGlobalVarDecl()
     * @generated
     */
    EClass GLOBAL_VAR_DECL = eINSTANCE.getGlobalVarDecl();

    /**
     * The meta object literal for the '<em><b>Spec</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GLOBAL_VAR_DECL__SPEC = eINSTANCE.getGlobalVarDecl_Spec();

    /**
     * The meta object literal for the '<em><b>Spec Init</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GLOBAL_VAR_DECL__SPEC_INIT = eINSTANCE.getGlobalVarDecl_SpecInit();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.GlobalVarSpecImpl <em>Global Var Spec</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.GlobalVarSpecImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getGlobalVarSpec()
     * @generated
     */
    EClass GLOBAL_VAR_SPEC = eINSTANCE.getGlobalVarSpec();

    /**
     * The meta object literal for the '<em><b>Variable</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GLOBAL_VAR_SPEC__VARIABLE = eINSTANCE.getGlobalVarSpec_Variable();

    /**
     * The meta object literal for the '<em><b>Location</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GLOBAL_VAR_SPEC__LOCATION = eINSTANCE.getGlobalVarSpec_Location();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.GlobalVarListImpl <em>Global Var List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.GlobalVarListImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getGlobalVarList()
     * @generated
     */
    EClass GLOBAL_VAR_LIST = eINSTANCE.getGlobalVarList();

    /**
     * The meta object literal for the '<em><b>Variables</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GLOBAL_VAR_LIST__VARIABLES = eINSTANCE.getGlobalVarList_Variables();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.GlobalVarImpl <em>Global Var</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.GlobalVarImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getGlobalVar()
     * @generated
     */
    EClass GLOBAL_VAR = eINSTANCE.getGlobalVar();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GLOBAL_VAR__NAME = eINSTANCE.getGlobalVar_Name();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.TaskConfigurationImpl <em>Task Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.TaskConfigurationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getTaskConfiguration()
     * @generated
     */
    EClass TASK_CONFIGURATION = eINSTANCE.getTaskConfiguration();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TASK_CONFIGURATION__NAME = eINSTANCE.getTaskConfiguration_Name();

    /**
     * The meta object literal for the '<em><b>Task Init</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TASK_CONFIGURATION__TASK_INIT = eINSTANCE.getTaskConfiguration_TaskInit();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.TaskInitializationImpl <em>Task Initialization</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.TaskInitializationImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getTaskInitialization()
     * @generated
     */
    EClass TASK_INITIALIZATION = eINSTANCE.getTaskInitialization();

    /**
     * The meta object literal for the '<em><b>Single</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TASK_INITIALIZATION__SINGLE = eINSTANCE.getTaskInitialization_Single();

    /**
     * The meta object literal for the '<em><b>Interval</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TASK_INITIALIZATION__INTERVAL = eINSTANCE.getTaskInitialization_Interval();

    /**
     * The meta object literal for the '<em><b>Prior</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TASK_INITIALIZATION__PRIOR = eINSTANCE.getTaskInitialization_Prior();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.DataSourceImpl <em>Data Source</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.DataSourceImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getDataSource()
     * @generated
     */
    EClass DATA_SOURCE = eINSTANCE.getDataSource();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.ConstantImpl <em>Constant</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.ConstantImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getConstant()
     * @generated
     */
    EClass CONSTANT = eINSTANCE.getConstant();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.NumericLiteralImpl <em>Numeric Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.NumericLiteralImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getNumericLiteral()
     * @generated
     */
    EClass NUMERIC_LITERAL = eINSTANCE.getNumericLiteral();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute NUMERIC_LITERAL__VALUE = eINSTANCE.getNumericLiteral_Value();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.IntegerLiteralImpl <em>Integer Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.IntegerLiteralImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getIntegerLiteral()
     * @generated
     */
    EClass INTEGER_LITERAL = eINSTANCE.getIntegerLiteral();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INTEGER_LITERAL__TYPE = eINSTANCE.getIntegerLiteral_Type();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.RealLiteralImpl <em>Real Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.RealLiteralImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getRealLiteral()
     * @generated
     */
    EClass REAL_LITERAL = eINSTANCE.getRealLiteral();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference REAL_LITERAL__TYPE = eINSTANCE.getRealLiteral_Type();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.CharacterStringImpl <em>Character String</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.CharacterStringImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getCharacterString()
     * @generated
     */
    EClass CHARACTER_STRING = eINSTANCE.getCharacterString();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CHARACTER_STRING__VALUE = eINSTANCE.getCharacterString_Value();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.BitStringImpl <em>Bit String</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.BitStringImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getBitString()
     * @generated
     */
    EClass BIT_STRING = eINSTANCE.getBitString();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BIT_STRING__VALUE = eINSTANCE.getBitString_Value();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.BooleanImpl <em>Boolean</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.BooleanImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getBoolean()
     * @generated
     */
    EClass BOOLEAN = eINSTANCE.getBoolean();

    /**
     * The meta object literal for the '<em><b>Bool Int</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BOOLEAN__BOOL_INT = eINSTANCE.getBoolean_BoolInt();

    /**
     * The meta object literal for the '<em><b>True</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BOOLEAN__TRUE = eINSTANCE.getBoolean_True();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.TimeLiteralImpl <em>Time Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.TimeLiteralImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getTimeLiteral()
     * @generated
     */
    EClass TIME_LITERAL = eINSTANCE.getTimeLiteral();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.DurationLiteralImpl <em>Duration Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.DurationLiteralImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getDurationLiteral()
     * @generated
     */
    EClass DURATION_LITERAL = eINSTANCE.getDurationLiteral();

    /**
     * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DURATION_LITERAL__DURATION = eINSTANCE.getDurationLiteral_Duration();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.TimeOfDayLiteralImpl <em>Time Of Day Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.TimeOfDayLiteralImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getTimeOfDayLiteral()
     * @generated
     */
    EClass TIME_OF_DAY_LITERAL = eINSTANCE.getTimeOfDayLiteral();

    /**
     * The meta object literal for the '<em><b>Hour</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TIME_OF_DAY_LITERAL__HOUR = eINSTANCE.getTimeOfDayLiteral_Hour();

    /**
     * The meta object literal for the '<em><b>Minute</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TIME_OF_DAY_LITERAL__MINUTE = eINSTANCE.getTimeOfDayLiteral_Minute();

    /**
     * The meta object literal for the '<em><b>Second</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TIME_OF_DAY_LITERAL__SECOND = eINSTANCE.getTimeOfDayLiteral_Second();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.DateLiteralImpl <em>Date Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.DateLiteralImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getDateLiteral()
     * @generated
     */
    EClass DATE_LITERAL = eINSTANCE.getDateLiteral();

    /**
     * The meta object literal for the '<em><b>Year</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATE_LITERAL__YEAR = eINSTANCE.getDateLiteral_Year();

    /**
     * The meta object literal for the '<em><b>Month</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATE_LITERAL__MONTH = eINSTANCE.getDateLiteral_Month();

    /**
     * The meta object literal for the '<em><b>Day</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATE_LITERAL__DAY = eINSTANCE.getDateLiteral_Day();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.DateAndTimeLiteralImpl <em>Date And Time Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.DateAndTimeLiteralImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getDateAndTimeLiteral()
     * @generated
     */
    EClass DATE_AND_TIME_LITERAL = eINSTANCE.getDateAndTimeLiteral();

    /**
     * The meta object literal for the '<em><b>Year</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATE_AND_TIME_LITERAL__YEAR = eINSTANCE.getDateAndTimeLiteral_Year();

    /**
     * The meta object literal for the '<em><b>Month</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATE_AND_TIME_LITERAL__MONTH = eINSTANCE.getDateAndTimeLiteral_Month();

    /**
     * The meta object literal for the '<em><b>Day</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATE_AND_TIME_LITERAL__DAY = eINSTANCE.getDateAndTimeLiteral_Day();

    /**
     * The meta object literal for the '<em><b>Hour</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATE_AND_TIME_LITERAL__HOUR = eINSTANCE.getDateAndTimeLiteral_Hour();

    /**
     * The meta object literal for the '<em><b>Minute</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATE_AND_TIME_LITERAL__MINUTE = eINSTANCE.getDateAndTimeLiteral_Minute();

    /**
     * The meta object literal for the '<em><b>Second</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATE_AND_TIME_LITERAL__SECOND = eINSTANCE.getDateAndTimeLiteral_Second();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.BinaryExpressionImpl <em>Binary Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.BinaryExpressionImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getBinaryExpression()
     * @generated
     */
    EClass BINARY_EXPRESSION = eINSTANCE.getBinaryExpression();

    /**
     * The meta object literal for the '<em><b>Left</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINARY_EXPRESSION__LEFT = eINSTANCE.getBinaryExpression_Left();

    /**
     * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BINARY_EXPRESSION__OPERATOR = eINSTANCE.getBinaryExpression_Operator();

    /**
     * The meta object literal for the '<em><b>Right</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINARY_EXPRESSION__RIGHT = eINSTANCE.getBinaryExpression_Right();

    /**
     * The meta object literal for the '{@link com.bichler.iec.iec.impl.UnaryExpressionImpl <em>Unary Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.bichler.iec.iec.impl.UnaryExpressionImpl
     * @see com.bichler.iec.iec.impl.IecPackageImpl#getUnaryExpression()
     * @generated
     */
    EClass UNARY_EXPRESSION = eINSTANCE.getUnaryExpression();

    /**
     * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute UNARY_EXPRESSION__OPERATOR = eINSTANCE.getUnaryExpression_Operator();

  }

} //IecPackage
