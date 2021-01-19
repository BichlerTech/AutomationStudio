/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.EnumDeclaration#getEnumeration <em>Enumeration</em>}</li>
 *   <li>{@link com.bichler.iec.iec.EnumDeclaration#getConstant <em>Constant</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getEnumDeclaration()
 * @model
 * @generated
 */
public interface EnumDeclaration extends TypeDeclaration
{
  /**
   * Returns the value of the '<em><b>Enumeration</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Enumeration</em>' containment reference.
   * @see #setEnumeration(Enumeration)
   * @see com.bichler.iec.iec.IecPackage#getEnumDeclaration_Enumeration()
   * @model containment="true"
   * @generated
   */
  Enumeration getEnumeration();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.EnumDeclaration#getEnumeration <em>Enumeration</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Enumeration</em>' containment reference.
   * @see #getEnumeration()
   * @generated
   */
  void setEnumeration(Enumeration value);

  /**
   * Returns the value of the '<em><b>Constant</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Constant</em>' containment reference.
   * @see #setConstant(EnumeratedValue)
   * @see com.bichler.iec.iec.IecPackage#getEnumDeclaration_Constant()
   * @model containment="true"
   * @generated
   */
  EnumeratedValue getConstant();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.EnumDeclaration#getConstant <em>Constant</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Constant</em>' containment reference.
   * @see #getConstant()
   * @generated
   */
  void setConstant(EnumeratedValue value);

} // EnumDeclaration
