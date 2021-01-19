/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.DataSource;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.TaskInitialization;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Task Initialization</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.TaskInitializationImpl#getSingle <em>Single</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.TaskInitializationImpl#getInterval <em>Interval</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.TaskInitializationImpl#getPrior <em>Prior</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TaskInitializationImpl extends MinimalEObjectImpl.Container implements TaskInitialization
{
  /**
   * The cached value of the '{@link #getSingle() <em>Single</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSingle()
   * @generated
   * @ordered
   */
  protected DataSource single;

  /**
   * The cached value of the '{@link #getInterval() <em>Interval</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInterval()
   * @generated
   * @ordered
   */
  protected DataSource interval;

  /**
   * The default value of the '{@link #getPrior() <em>Prior</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPrior()
   * @generated
   * @ordered
   */
  protected static final int PRIOR_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getPrior() <em>Prior</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPrior()
   * @generated
   * @ordered
   */
  protected int prior = PRIOR_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TaskInitializationImpl()
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
    return IecPackage.Literals.TASK_INITIALIZATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DataSource getSingle()
  {
    return single;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSingle(DataSource newSingle, NotificationChain msgs)
  {
    DataSource oldSingle = single;
    single = newSingle;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.TASK_INITIALIZATION__SINGLE, oldSingle, newSingle);
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
  public void setSingle(DataSource newSingle)
  {
    if (newSingle != single)
    {
      NotificationChain msgs = null;
      if (single != null)
        msgs = ((InternalEObject)single).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.TASK_INITIALIZATION__SINGLE, null, msgs);
      if (newSingle != null)
        msgs = ((InternalEObject)newSingle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.TASK_INITIALIZATION__SINGLE, null, msgs);
      msgs = basicSetSingle(newSingle, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.TASK_INITIALIZATION__SINGLE, newSingle, newSingle));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DataSource getInterval()
  {
    return interval;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetInterval(DataSource newInterval, NotificationChain msgs)
  {
    DataSource oldInterval = interval;
    interval = newInterval;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.TASK_INITIALIZATION__INTERVAL, oldInterval, newInterval);
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
  public void setInterval(DataSource newInterval)
  {
    if (newInterval != interval)
    {
      NotificationChain msgs = null;
      if (interval != null)
        msgs = ((InternalEObject)interval).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.TASK_INITIALIZATION__INTERVAL, null, msgs);
      if (newInterval != null)
        msgs = ((InternalEObject)newInterval).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.TASK_INITIALIZATION__INTERVAL, null, msgs);
      msgs = basicSetInterval(newInterval, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.TASK_INITIALIZATION__INTERVAL, newInterval, newInterval));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int getPrior()
  {
    return prior;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setPrior(int newPrior)
  {
    int oldPrior = prior;
    prior = newPrior;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.TASK_INITIALIZATION__PRIOR, oldPrior, prior));
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
      case IecPackage.TASK_INITIALIZATION__SINGLE:
        return basicSetSingle(null, msgs);
      case IecPackage.TASK_INITIALIZATION__INTERVAL:
        return basicSetInterval(null, msgs);
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
      case IecPackage.TASK_INITIALIZATION__SINGLE:
        return getSingle();
      case IecPackage.TASK_INITIALIZATION__INTERVAL:
        return getInterval();
      case IecPackage.TASK_INITIALIZATION__PRIOR:
        return getPrior();
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
      case IecPackage.TASK_INITIALIZATION__SINGLE:
        setSingle((DataSource)newValue);
        return;
      case IecPackage.TASK_INITIALIZATION__INTERVAL:
        setInterval((DataSource)newValue);
        return;
      case IecPackage.TASK_INITIALIZATION__PRIOR:
        setPrior((Integer)newValue);
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
      case IecPackage.TASK_INITIALIZATION__SINGLE:
        setSingle((DataSource)null);
        return;
      case IecPackage.TASK_INITIALIZATION__INTERVAL:
        setInterval((DataSource)null);
        return;
      case IecPackage.TASK_INITIALIZATION__PRIOR:
        setPrior(PRIOR_EDEFAULT);
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
      case IecPackage.TASK_INITIALIZATION__SINGLE:
        return single != null;
      case IecPackage.TASK_INITIALIZATION__INTERVAL:
        return interval != null;
      case IecPackage.TASK_INITIALIZATION__PRIOR:
        return prior != PRIOR_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (prior: ");
    result.append(prior);
    result.append(')');
    return result.toString();
  }

} //TaskInitializationImpl
