/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.ProgramConfiguration;
import com.bichler.iec.iec.SingleResourceDeclaration;
import com.bichler.iec.iec.TaskConfiguration;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Single Resource Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.SingleResourceDeclarationImpl#getTaskConf <em>Task Conf</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.SingleResourceDeclarationImpl#getProgramConf <em>Program Conf</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SingleResourceDeclarationImpl extends MinimalEObjectImpl.Container implements SingleResourceDeclaration
{
  /**
   * The cached value of the '{@link #getTaskConf() <em>Task Conf</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTaskConf()
   * @generated
   * @ordered
   */
  protected TaskConfiguration taskConf;

  /**
   * The cached value of the '{@link #getProgramConf() <em>Program Conf</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProgramConf()
   * @generated
   * @ordered
   */
  protected EList<ProgramConfiguration> programConf;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SingleResourceDeclarationImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return IecPackage.Literals.SINGLE_RESOURCE_DECLARATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TaskConfiguration getTaskConf()
  {
    return taskConf;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTaskConf(TaskConfiguration newTaskConf, NotificationChain msgs)
  {
    TaskConfiguration oldTaskConf = taskConf;
    taskConf = newTaskConf;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.SINGLE_RESOURCE_DECLARATION__TASK_CONF, oldTaskConf, newTaskConf);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setTaskConf(TaskConfiguration newTaskConf)
  {
    if (newTaskConf != taskConf)
    {
      NotificationChain msgs = null;
      if (taskConf != null)
        msgs = ((InternalEObject)taskConf).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.SINGLE_RESOURCE_DECLARATION__TASK_CONF, null, msgs);
      if (newTaskConf != null)
        msgs = ((InternalEObject)newTaskConf).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.SINGLE_RESOURCE_DECLARATION__TASK_CONF, null, msgs);
      msgs = basicSetTaskConf(newTaskConf, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.SINGLE_RESOURCE_DECLARATION__TASK_CONF, newTaskConf, newTaskConf));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ProgramConfiguration> getProgramConf()
  {
    if (programConf == null)
    {
      programConf = new EObjectContainmentEList<ProgramConfiguration>(ProgramConfiguration.class, this, IecPackage.SINGLE_RESOURCE_DECLARATION__PROGRAM_CONF);
    }
    return programConf;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case IecPackage.SINGLE_RESOURCE_DECLARATION__TASK_CONF:
        return basicSetTaskConf(null, msgs);
      case IecPackage.SINGLE_RESOURCE_DECLARATION__PROGRAM_CONF:
        return ((InternalEList<?>)getProgramConf()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case IecPackage.SINGLE_RESOURCE_DECLARATION__TASK_CONF:
        return getTaskConf();
      case IecPackage.SINGLE_RESOURCE_DECLARATION__PROGRAM_CONF:
        return getProgramConf();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case IecPackage.SINGLE_RESOURCE_DECLARATION__TASK_CONF:
        setTaskConf((TaskConfiguration)newValue);
        return;
      case IecPackage.SINGLE_RESOURCE_DECLARATION__PROGRAM_CONF:
        getProgramConf().clear();
        getProgramConf().addAll((Collection<? extends ProgramConfiguration>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case IecPackage.SINGLE_RESOURCE_DECLARATION__TASK_CONF:
        setTaskConf((TaskConfiguration)null);
        return;
      case IecPackage.SINGLE_RESOURCE_DECLARATION__PROGRAM_CONF:
        getProgramConf().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case IecPackage.SINGLE_RESOURCE_DECLARATION__TASK_CONF:
        return taskConf != null;
      case IecPackage.SINGLE_RESOURCE_DECLARATION__PROGRAM_CONF:
        return programConf != null && !programConf.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //SingleResourceDeclarationImpl
