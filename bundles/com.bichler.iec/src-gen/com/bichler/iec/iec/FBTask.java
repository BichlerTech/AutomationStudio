/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FB Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.FBTask#getFbname <em>Fbname</em>}</li>
 *   <li>{@link com.bichler.iec.iec.FBTask#getTask <em>Task</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getFBTask()
 * @model
 * @generated
 */
public interface FBTask extends ProgConfElement
{
  /**
   * Returns the value of the '<em><b>Fbname</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fbname</em>' containment reference.
   * @see #setFbname(FunctionBlockDeclaration)
   * @see com.bichler.iec.iec.IecPackage#getFBTask_Fbname()
   * @model containment="true"
   * @generated
   */
  FunctionBlockDeclaration getFbname();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.FBTask#getFbname <em>Fbname</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Fbname</em>' containment reference.
   * @see #getFbname()
   * @generated
   */
  void setFbname(FunctionBlockDeclaration value);

  /**
   * Returns the value of the '<em><b>Task</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Task</em>' containment reference.
   * @see #setTask(TaskConfiguration)
   * @see com.bichler.iec.iec.IecPackage#getFBTask_Task()
   * @model containment="true"
   * @generated
   */
  TaskConfiguration getTask();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.FBTask#getTask <em>Task</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Task</em>' containment reference.
   * @see #getTask()
   * @generated
   */
  void setTask(TaskConfiguration value);

} // FBTask
