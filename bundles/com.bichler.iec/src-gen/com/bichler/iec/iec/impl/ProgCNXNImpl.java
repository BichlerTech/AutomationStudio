/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.ProgCNXN;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Prog CNXN</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ProgCNXNImpl#getVariablename <em>Variablename</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ProgCNXNImpl#getProgd <em>Progd</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProgCNXNImpl extends ProgConfElementImpl implements ProgCNXN
{
  /**
   * The default value of the '{@link #getVariablename() <em>Variablename</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVariablename()
   * @generated
   * @ordered
   */
  protected static final String VARIABLENAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getVariablename() <em>Variablename</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVariablename()
   * @generated
   * @ordered
   */
  protected String variablename = VARIABLENAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getProgd() <em>Progd</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProgd()
   * @generated
   * @ordered
   */
  protected EObject progd;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProgCNXNImpl()
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
    return IecPackage.Literals.PROG_CNXN;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getVariablename()
  {
    return variablename;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVariablename(String newVariablename)
  {
    String oldVariablename = variablename;
    variablename = newVariablename;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PROG_CNXN__VARIABLENAME, oldVariablename, variablename));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject getProgd()
  {
    return progd;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetProgd(EObject newProgd, NotificationChain msgs)
  {
    EObject oldProgd = progd;
    progd = newProgd;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.PROG_CNXN__PROGD, oldProgd, newProgd);
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
  public void setProgd(EObject newProgd)
  {
    if (newProgd != progd)
    {
      NotificationChain msgs = null;
      if (progd != null)
        msgs = ((InternalEObject)progd).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROG_CNXN__PROGD, null, msgs);
      if (newProgd != null)
        msgs = ((InternalEObject)newProgd).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROG_CNXN__PROGD, null, msgs);
      msgs = basicSetProgd(newProgd, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PROG_CNXN__PROGD, newProgd, newProgd));
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
      case IecPackage.PROG_CNXN__PROGD:
        return basicSetProgd(null, msgs);
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
      case IecPackage.PROG_CNXN__VARIABLENAME:
        return getVariablename();
      case IecPackage.PROG_CNXN__PROGD:
        return getProgd();
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
      case IecPackage.PROG_CNXN__VARIABLENAME:
        setVariablename((String)newValue);
        return;
      case IecPackage.PROG_CNXN__PROGD:
        setProgd((EObject)newValue);
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
      case IecPackage.PROG_CNXN__VARIABLENAME:
        setVariablename(VARIABLENAME_EDEFAULT);
        return;
      case IecPackage.PROG_CNXN__PROGD:
        setProgd((EObject)null);
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
      case IecPackage.PROG_CNXN__VARIABLENAME:
        return VARIABLENAME_EDEFAULT == null ? variablename != null : !VARIABLENAME_EDEFAULT.equals(variablename);
      case IecPackage.PROG_CNXN__PROGD:
        return progd != null;
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
    result.append(" (variablename: ");
    result.append(variablename);
    result.append(')');
    return result.toString();
  }

} //ProgCNXNImpl
