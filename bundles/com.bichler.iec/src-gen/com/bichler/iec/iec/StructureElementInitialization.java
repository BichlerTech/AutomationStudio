/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Structure Element Initialization</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.StructureElementInitialization#getName <em>Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.StructureElementInitialization#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getStructureElementInitialization()
 * @model
 * @generated
 */
public interface StructureElementInitialization extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see com.bichler.iec.iec.IecPackage#getStructureElementInitialization_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.StructureElementInitialization#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' containment reference.
   * @see #setValue(InitialElement)
   * @see com.bichler.iec.iec.IecPackage#getStructureElementInitialization_Value()
   * @model containment="true"
   * @generated
   */
  InitialElement getValue();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.StructureElementInitialization#getValue <em>Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' containment reference.
   * @see #getValue()
   * @generated
   */
  void setValue(InitialElement value);

} // StructureElementInitialization
