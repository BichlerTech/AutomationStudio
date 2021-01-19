/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enumerated Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.EnumeratedValue#getDerivedType <em>Derived Type</em>}</li>
 *   <li>{@link com.bichler.iec.iec.EnumeratedValue#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getEnumeratedValue()
 * @model
 * @generated
 */
public interface EnumeratedValue extends InitialElement, ReferencedOperand
{
  /**
   * Returns the value of the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Derived Type</em>' containment reference.
   * @see #setDerivedType(DerivedType)
   * @see com.bichler.iec.iec.IecPackage#getEnumeratedValue_DerivedType()
   * @model containment="true"
   * @generated
   */
  DerivedType getDerivedType();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.EnumeratedValue#getDerivedType <em>Derived Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Derived Type</em>' containment reference.
   * @see #getDerivedType()
   * @generated
   */
  void setDerivedType(DerivedType value);

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see com.bichler.iec.iec.IecPackage#getEnumeratedValue_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.EnumeratedValue#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

} // EnumeratedValue
