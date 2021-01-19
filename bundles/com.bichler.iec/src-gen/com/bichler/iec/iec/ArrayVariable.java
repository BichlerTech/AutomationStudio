/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Array Variable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ArrayVariable#getSubscriptedVariable <em>Subscripted Variable</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ArrayVariable#getSubscripts <em>Subscripts</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getArrayVariable()
 * @model
 * @generated
 */
public interface ArrayVariable extends MultiElementVariable
{
  /**
   * Returns the value of the '<em><b>Subscripted Variable</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Subscripted Variable</em>' reference.
   * @see #setSubscriptedVariable(Variable)
   * @see com.bichler.iec.iec.IecPackage#getArrayVariable_SubscriptedVariable()
   * @model
   * @generated
   */
  Variable getSubscriptedVariable();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ArrayVariable#getSubscriptedVariable <em>Subscripted Variable</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Subscripted Variable</em>' reference.
   * @see #getSubscriptedVariable()
   * @generated
   */
  void setSubscriptedVariable(Variable value);

  /**
   * Returns the value of the '<em><b>Subscripts</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.Expression}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Subscripts</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getArrayVariable_Subscripts()
   * @model containment="true"
   * @generated
   */
  EList<Expression> getSubscripts();

} // ArrayVariable
