/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Initialized Structure</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.InitializedStructure#getDerivedType <em>Derived Type</em>}</li>
 *   <li>{@link com.bichler.iec.iec.InitializedStructure#getInitialization <em>Initialization</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getInitializedStructure()
 * @model
 * @generated
 */
public interface InitializedStructure extends EObject
{
  /**
   * Returns the value of the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Derived Type</em>' containment reference.
   * @see #setDerivedType(DerivedType)
   * @see com.bichler.iec.iec.IecPackage#getInitializedStructure_DerivedType()
   * @model containment="true"
   * @generated
   */
  DerivedType getDerivedType();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.InitializedStructure#getDerivedType <em>Derived Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Derived Type</em>' containment reference.
   * @see #getDerivedType()
   * @generated
   */
  void setDerivedType(DerivedType value);

  /**
   * Returns the value of the '<em><b>Initialization</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Initialization</em>' containment reference.
   * @see #setInitialization(StructureInitialization)
   * @see com.bichler.iec.iec.IecPackage#getInitializedStructure_Initialization()
   * @model containment="true"
   * @generated
   */
  StructureInitialization getInitialization();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.InitializedStructure#getInitialization <em>Initialization</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Initialization</em>' containment reference.
   * @see #getInitialization()
   * @generated
   */
  void setInitialization(StructureInitialization value);

} // InitializedStructure
