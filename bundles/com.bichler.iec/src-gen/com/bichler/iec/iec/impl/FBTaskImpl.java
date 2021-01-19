/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.FBTask;
import com.bichler.iec.iec.FunctionBlockDeclaration;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.TaskConfiguration;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>FB Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.FBTaskImpl#getFbname <em>Fbname</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.FBTaskImpl#getTask <em>Task</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FBTaskImpl extends ProgConfElementImpl implements FBTask
{
  /**
   * The cached value of the '{@link #getFbname() <em>Fbname</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFbname()
   * @generated
   * @ordered
   */
  protected FunctionBlockDeclaration fbname;

  /**
   * The cached value of the '{@link #getTask() <em>Task</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTask()
   * @generated
   * @ordered
   */
  protected TaskConfiguration task;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FBTaskImpl()
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
    return IecPackage.Literals.FB_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FunctionBlockDeclaration getFbname()
  {
    return fbname;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFbname(FunctionBlockDeclaration newFbname, NotificationChain msgs)
  {
    FunctionBlockDeclaration oldFbname = fbname;
    fbname = newFbname;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.FB_TASK__FBNAME, oldFbname, newFbname);
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
  public void setFbname(FunctionBlockDeclaration newFbname)
  {
    if (newFbname != fbname)
    {
      NotificationChain msgs = null;
      if (fbname != null)
        msgs = ((InternalEObject)fbname).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.FB_TASK__FBNAME, null, msgs);
      if (newFbname != null)
        msgs = ((InternalEObject)newFbname).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.FB_TASK__FBNAME, null, msgs);
      msgs = basicSetFbname(newFbname, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.FB_TASK__FBNAME, newFbname, newFbname));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TaskConfiguration getTask()
  {
    return task;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTask(TaskConfiguration newTask, NotificationChain msgs)
  {
    TaskConfiguration oldTask = task;
    task = newTask;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.FB_TASK__TASK, oldTask, newTask);
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
  public void setTask(TaskConfiguration newTask)
  {
    if (newTask != task)
    {
      NotificationChain msgs = null;
      if (task != null)
        msgs = ((InternalEObject)task).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.FB_TASK__TASK, null, msgs);
      if (newTask != null)
        msgs = ((InternalEObject)newTask).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.FB_TASK__TASK, null, msgs);
      msgs = basicSetTask(newTask, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.FB_TASK__TASK, newTask, newTask));
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
      case IecPackage.FB_TASK__FBNAME:
        return basicSetFbname(null, msgs);
      case IecPackage.FB_TASK__TASK:
        return basicSetTask(null, msgs);
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
      case IecPackage.FB_TASK__FBNAME:
        return getFbname();
      case IecPackage.FB_TASK__TASK:
        return getTask();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case IecPackage.FB_TASK__FBNAME:
        setFbname((FunctionBlockDeclaration)newValue);
        return;
      case IecPackage.FB_TASK__TASK:
        setTask((TaskConfiguration)newValue);
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
      case IecPackage.FB_TASK__FBNAME:
        setFbname((FunctionBlockDeclaration)null);
        return;
      case IecPackage.FB_TASK__TASK:
        setTask((TaskConfiguration)null);
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
      case IecPackage.FB_TASK__FBNAME:
        return fbname != null;
      case IecPackage.FB_TASK__TASK:
        return task != null;
    }
    return super.eIsSet(featureID);
  }

} //FBTaskImpl
