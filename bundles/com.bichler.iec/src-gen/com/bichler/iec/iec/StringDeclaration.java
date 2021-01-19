/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>String Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.StringDeclaration#isString <em>String</em>}</li>
 *   <li>{@link com.bichler.iec.iec.StringDeclaration#getSize <em>Size</em>}</li>
 *   <li>{@link com.bichler.iec.iec.StringDeclaration#getInitialValue <em>Initial Value</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getStringDeclaration()
 * @model
 * @generated
 */
public interface StringDeclaration extends TypeDeclaration
{
  /**
   * Returns the value of the '<em><b>String</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>String</em>' attribute.
   * @see #setString(boolean)
   * @see com.bichler.iec.iec.IecPackage#getStringDeclaration_String()
   * @model
   * @generated
   */
  boolean isString();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.StringDeclaration#isString <em>String</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>String</em>' attribute.
   * @see #isString()
   * @generated
   */
  void setString(boolean value);

  /**
   * Returns the value of the '<em><b>Size</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Size</em>' attribute.
   * @see #setSize(String)
   * @see com.bichler.iec.iec.IecPackage#getStringDeclaration_Size()
   * @model
   * @generated
   */
  String getSize();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.StringDeclaration#getSize <em>Size</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Size</em>' attribute.
   * @see #getSize()
   * @generated
   */
  void setSize(String value);

  /**
   * Returns the value of the '<em><b>Initial Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Initial Value</em>' containment reference.
   * @see #setInitialValue(CharacterString)
   * @see com.bichler.iec.iec.IecPackage#getStringDeclaration_InitialValue()
   * @model containment="true"
   * @generated
   */
  CharacterString getInitialValue();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.StringDeclaration#getInitialValue <em>Initial Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Initial Value</em>' containment reference.
   * @see #getInitialValue()
   * @generated
   */
  void setInitialValue(CharacterString value);

} // StringDeclaration
