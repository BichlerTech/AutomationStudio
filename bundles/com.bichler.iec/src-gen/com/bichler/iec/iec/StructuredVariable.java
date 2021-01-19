/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Structured Variable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.StructuredVariable#getRecordVariable <em>Record Variable</em>}</li>
 *   <li>{@link com.bichler.iec.iec.StructuredVariable#getFieldSelector <em>Field Selector</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getStructuredVariable()
 * @model
 * @generated
 */
public interface StructuredVariable extends MultiElementVariable
{
  /**
   * Returns the value of the '<em><b>Record Variable</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Record Variable</em>' reference.
   * @see #setRecordVariable(Variable)
   * @see com.bichler.iec.iec.IecPackage#getStructuredVariable_RecordVariable()
   * @model
   * @generated
   */
  Variable getRecordVariable();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.StructuredVariable#getRecordVariable <em>Record Variable</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Record Variable</em>' reference.
   * @see #getRecordVariable()
   * @generated
   */
  void setRecordVariable(Variable value);

  /**
   * Returns the value of the '<em><b>Field Selector</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Field Selector</em>' attribute.
   * @see #setFieldSelector(String)
   * @see com.bichler.iec.iec.IecPackage#getStructuredVariable_FieldSelector()
   * @model
   * @generated
   */
  String getFieldSelector();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.StructuredVariable#getFieldSelector <em>Field Selector</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Field Selector</em>' attribute.
   * @see #getFieldSelector()
   * @generated
   */
  void setFieldSelector(String value);

} // StructuredVariable
