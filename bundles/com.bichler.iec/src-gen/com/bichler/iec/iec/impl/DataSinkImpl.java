/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.DataSink;
import com.bichler.iec.iec.DirectVariable;
import com.bichler.iec.iec.GlobalVar;
import com.bichler.iec.iec.IecPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Sink</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.DataSinkImpl#getGlobvar <em>Globvar</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.DataSinkImpl#getDirvar <em>Dirvar</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DataSinkImpl extends MinimalEObjectImpl.Container implements DataSink
{
  /**
   * The cached value of the '{@link #getGlobvar() <em>Globvar</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGlobvar()
   * @generated
   * @ordered
   */
  protected GlobalVar globvar;

  /**
   * The cached value of the '{@link #getDirvar() <em>Dirvar</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDirvar()
   * @generated
   * @ordered
   */
  protected DirectVariable dirvar;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DataSinkImpl()
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
    return IecPackage.Literals.DATA_SINK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GlobalVar getGlobvar()
  {
    return globvar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetGlobvar(GlobalVar newGlobvar, NotificationChain msgs)
  {
    GlobalVar oldGlobvar = globvar;
    globvar = newGlobvar;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.DATA_SINK__GLOBVAR, oldGlobvar, newGlobvar);
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
  public void setGlobvar(GlobalVar newGlobvar)
  {
    if (newGlobvar != globvar)
    {
      NotificationChain msgs = null;
      if (globvar != null)
        msgs = ((InternalEObject)globvar).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.DATA_SINK__GLOBVAR, null, msgs);
      if (newGlobvar != null)
        msgs = ((InternalEObject)newGlobvar).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.DATA_SINK__GLOBVAR, null, msgs);
      msgs = basicSetGlobvar(newGlobvar, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.DATA_SINK__GLOBVAR, newGlobvar, newGlobvar));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DirectVariable getDirvar()
  {
    return dirvar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetDirvar(DirectVariable newDirvar, NotificationChain msgs)
  {
    DirectVariable oldDirvar = dirvar;
    dirvar = newDirvar;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.DATA_SINK__DIRVAR, oldDirvar, newDirvar);
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
  public void setDirvar(DirectVariable newDirvar)
  {
    if (newDirvar != dirvar)
    {
      NotificationChain msgs = null;
      if (dirvar != null)
        msgs = ((InternalEObject)dirvar).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.DATA_SINK__DIRVAR, null, msgs);
      if (newDirvar != null)
        msgs = ((InternalEObject)newDirvar).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.DATA_SINK__DIRVAR, null, msgs);
      msgs = basicSetDirvar(newDirvar, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.DATA_SINK__DIRVAR, newDirvar, newDirvar));
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
      case IecPackage.DATA_SINK__GLOBVAR:
        return basicSetGlobvar(null, msgs);
      case IecPackage.DATA_SINK__DIRVAR:
        return basicSetDirvar(null, msgs);
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
      case IecPackage.DATA_SINK__GLOBVAR:
        return getGlobvar();
      case IecPackage.DATA_SINK__DIRVAR:
        return getDirvar();
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
      case IecPackage.DATA_SINK__GLOBVAR:
        setGlobvar((GlobalVar)newValue);
        return;
      case IecPackage.DATA_SINK__DIRVAR:
        setDirvar((DirectVariable)newValue);
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
      case IecPackage.DATA_SINK__GLOBVAR:
        setGlobvar((GlobalVar)null);
        return;
      case IecPackage.DATA_SINK__DIRVAR:
        setDirvar((DirectVariable)null);
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
      case IecPackage.DATA_SINK__GLOBVAR:
        return globvar != null;
      case IecPackage.DATA_SINK__DIRVAR:
        return dirvar != null;
    }
    return super.eIsSet(featureID);
  }

} //DataSinkImpl
