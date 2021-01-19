/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Program Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ProgramConfiguration#getName <em>Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ProgramConfiguration#getTask <em>Task</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ProgramConfiguration#getProg <em>Prog</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ProgramConfiguration#getProgConf <em>Prog Conf</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getProgramConfiguration()
 * @model
 * @generated
 */
public interface ProgramConfiguration extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see com.bichler.iec.iec.IecPackage#getProgramConfiguration_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ProgramConfiguration#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Task</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Task</em>' reference.
   * @see #setTask(TaskConfiguration)
   * @see com.bichler.iec.iec.IecPackage#getProgramConfiguration_Task()
   * @model
   * @generated
   */
  TaskConfiguration getTask();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ProgramConfiguration#getTask <em>Task</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Task</em>' reference.
   * @see #getTask()
   * @generated
   */
  void setTask(TaskConfiguration value);

  /**
   * Returns the value of the '<em><b>Prog</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Prog</em>' reference.
   * @see #setProg(ProgramDeclaration)
   * @see com.bichler.iec.iec.IecPackage#getProgramConfiguration_Prog()
   * @model
   * @generated
   */
  ProgramDeclaration getProg();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ProgramConfiguration#getProg <em>Prog</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Prog</em>' reference.
   * @see #getProg()
   * @generated
   */
  void setProg(ProgramDeclaration value);

  /**
   * Returns the value of the '<em><b>Prog Conf</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Prog Conf</em>' containment reference.
   * @see #setProgConf(ProgConfElements)
   * @see com.bichler.iec.iec.IecPackage#getProgramConfiguration_ProgConf()
   * @model containment="true"
   * @generated
   */
  ProgConfElements getProgConf();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ProgramConfiguration#getProgConf <em>Prog Conf</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Prog Conf</em>' containment reference.
   * @see #getProgConf()
   * @generated
   */
  void setProgConf(ProgConfElements value);

} // ProgramConfiguration
