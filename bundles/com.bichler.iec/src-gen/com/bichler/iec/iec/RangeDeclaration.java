/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Range Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.RangeDeclaration#getBaseType <em>Base Type</em>}</li>
 *   <li>{@link com.bichler.iec.iec.RangeDeclaration#getRange <em>Range</em>}</li>
 *   <li>{@link com.bichler.iec.iec.RangeDeclaration#getConstant <em>Constant</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getRangeDeclaration()
 * @model
 * @generated
 */
public interface RangeDeclaration extends TypeDeclaration
{
  /**
   * Returns the value of the '<em><b>Base Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Base Type</em>' containment reference.
   * @see #setBaseType(ElementaryType)
   * @see com.bichler.iec.iec.IecPackage#getRangeDeclaration_BaseType()
   * @model containment="true"
   * @generated
   */
  ElementaryType getBaseType();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.RangeDeclaration#getBaseType <em>Base Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Base Type</em>' containment reference.
   * @see #getBaseType()
   * @generated
   */
  void setBaseType(ElementaryType value);

  /**
   * Returns the value of the '<em><b>Range</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Range</em>' attribute.
   * @see #setRange(String)
   * @see com.bichler.iec.iec.IecPackage#getRangeDeclaration_Range()
   * @model
   * @generated
   */
  String getRange();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.RangeDeclaration#getRange <em>Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Range</em>' attribute.
   * @see #getRange()
   * @generated
   */
  void setRange(String value);

  /**
   * Returns the value of the '<em><b>Constant</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Constant</em>' attribute.
   * @see #setConstant(String)
   * @see com.bichler.iec.iec.IecPackage#getRangeDeclaration_Constant()
   * @model
   * @generated
   */
  String getConstant();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.RangeDeclaration#getConstant <em>Constant</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Constant</em>' attribute.
   * @see #getConstant()
   * @generated
   */
  void setConstant(String value);

} // RangeDeclaration
