/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;

import java.lang.Boolean;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Boolean</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.BooleanImpl#getBoolInt <em>Bool Int</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.BooleanImpl#isTrue <em>True</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BooleanImpl extends ConstantImpl implements com.bichler.iec.iec.Boolean
{
  /**
   * The default value of the '{@link #getBoolInt() <em>Bool Int</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBoolInt()
   * @generated
   * @ordered
   */
  protected static final String BOOL_INT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getBoolInt() <em>Bool Int</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBoolInt()
   * @generated
   * @ordered
   */
  protected String boolInt = BOOL_INT_EDEFAULT;

  /**
   * The default value of the '{@link #isTrue() <em>True</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isTrue()
   * @generated
   * @ordered
   */
  protected static final boolean TRUE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isTrue() <em>True</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isTrue()
   * @generated
   * @ordered
   */
  protected boolean true_ = TRUE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BooleanImpl()
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
    return IecPackage.Literals.BOOLEAN;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getBoolInt()
  {
    return boolInt;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setBoolInt(String newBoolInt)
  {
    String oldBoolInt = boolInt;
    boolInt = newBoolInt;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.BOOLEAN__BOOL_INT, oldBoolInt, boolInt));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isTrue()
  {
    return true_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setTrue(boolean newTrue)
  {
    boolean oldTrue = true_;
    true_ = newTrue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.BOOLEAN__TRUE, oldTrue, true_));
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
      case IecPackage.BOOLEAN__BOOL_INT:
        return getBoolInt();
      case IecPackage.BOOLEAN__TRUE:
        return isTrue();
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
      case IecPackage.BOOLEAN__BOOL_INT:
        setBoolInt((String)newValue);
        return;
      case IecPackage.BOOLEAN__TRUE:
        setTrue((Boolean)newValue);
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
      case IecPackage.BOOLEAN__BOOL_INT:
        setBoolInt(BOOL_INT_EDEFAULT);
        return;
      case IecPackage.BOOLEAN__TRUE:
        setTrue(TRUE_EDEFAULT);
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
      case IecPackage.BOOLEAN__BOOL_INT:
        return BOOL_INT_EDEFAULT == null ? boolInt != null : !BOOL_INT_EDEFAULT.equals(boolInt);
      case IecPackage.BOOLEAN__TRUE:
        return true_ != TRUE_EDEFAULT;
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
    result.append(" (boolInt: ");
    result.append(boolInt);
    result.append(", true: ");
    result.append(true_);
    result.append(')');
    return result.toString();
  }

} //BooleanImpl
