/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.TaskConfiguration#getName <em>Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.TaskConfiguration#getTaskInit <em>Task Init</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getTaskConfiguration()
 * @model
 * @generated
 */
public interface TaskConfiguration extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see com.bichler.iec.iec.IecPackage#getTaskConfiguration_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.TaskConfiguration#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Task Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Task Init</em>' containment reference.
   * @see #setTaskInit(TaskInitialization)
   * @see com.bichler.iec.iec.IecPackage#getTaskConfiguration_TaskInit()
   * @model containment="true"
   * @generated
   */
  TaskInitialization getTaskInit();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.TaskConfiguration#getTaskInit <em>Task Init</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Task Init</em>' containment reference.
   * @see #getTaskInit()
   * @generated
   */
  void setTaskInit(TaskInitialization value);

} // TaskConfiguration
