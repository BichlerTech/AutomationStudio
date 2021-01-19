/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Single Resource Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.SingleResourceDeclaration#getTaskConf <em>Task Conf</em>}</li>
 *   <li>{@link com.bichler.iec.iec.SingleResourceDeclaration#getProgramConf <em>Program Conf</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getSingleResourceDeclaration()
 * @model
 * @generated
 */
public interface SingleResourceDeclaration extends EObject
{
  /**
   * Returns the value of the '<em><b>Task Conf</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Task Conf</em>' containment reference.
   * @see #setTaskConf(TaskConfiguration)
   * @see com.bichler.iec.iec.IecPackage#getSingleResourceDeclaration_TaskConf()
   * @model containment="true"
   * @generated
   */
  TaskConfiguration getTaskConf();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.SingleResourceDeclaration#getTaskConf <em>Task Conf</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Task Conf</em>' containment reference.
   * @see #getTaskConf()
   * @generated
   */
  void setTaskConf(TaskConfiguration value);

  /**
   * Returns the value of the '<em><b>Program Conf</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.ProgramConfiguration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Program Conf</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getSingleResourceDeclaration_ProgramConf()
   * @model containment="true"
   * @generated
   */
  EList<ProgramConfiguration> getProgramConf();

} // SingleResourceDeclaration
