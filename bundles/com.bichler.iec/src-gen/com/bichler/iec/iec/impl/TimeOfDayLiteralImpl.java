/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.TimeOfDayLiteral;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Of Day Literal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.TimeOfDayLiteralImpl#getHour <em>Hour</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.TimeOfDayLiteralImpl#getMinute <em>Minute</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.TimeOfDayLiteralImpl#getSecond <em>Second</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TimeOfDayLiteralImpl extends TimeLiteralImpl implements TimeOfDayLiteral
{
  /**
   * The default value of the '{@link #getHour() <em>Hour</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHour()
   * @generated
   * @ordered
   */
  protected static final String HOUR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getHour() <em>Hour</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHour()
   * @generated
   * @ordered
   */
  protected String hour = HOUR_EDEFAULT;

  /**
   * The default value of the '{@link #getMinute() <em>Minute</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMinute()
   * @generated
   * @ordered
   */
  protected static final String MINUTE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMinute() <em>Minute</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMinute()
   * @generated
   * @ordered
   */
  protected String minute = MINUTE_EDEFAULT;

  /**
   * The default value of the '{@link #getSecond() <em>Second</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSecond()
   * @generated
   * @ordered
   */
  protected static final String SECOND_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSecond() <em>Second</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSecond()
   * @generated
   * @ordered
   */
  protected String second = SECOND_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TimeOfDayLiteralImpl()
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
    return IecPackage.Literals.TIME_OF_DAY_LITERAL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getHour()
  {
    return hour;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setHour(String newHour)
  {
    String oldHour = hour;
    hour = newHour;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.TIME_OF_DAY_LITERAL__HOUR, oldHour, hour));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getMinute()
  {
    return minute;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setMinute(String newMinute)
  {
    String oldMinute = minute;
    minute = newMinute;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.TIME_OF_DAY_LITERAL__MINUTE, oldMinute, minute));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getSecond()
  {
    return second;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSecond(String newSecond)
  {
    String oldSecond = second;
    second = newSecond;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.TIME_OF_DAY_LITERAL__SECOND, oldSecond, second));
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
      case IecPackage.TIME_OF_DAY_LITERAL__HOUR:
        return getHour();
      case IecPackage.TIME_OF_DAY_LITERAL__MINUTE:
        return getMinute();
      case IecPackage.TIME_OF_DAY_LITERAL__SECOND:
        return getSecond();
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
      case IecPackage.TIME_OF_DAY_LITERAL__HOUR:
        setHour((String)newValue);
        return;
      case IecPackage.TIME_OF_DAY_LITERAL__MINUTE:
        setMinute((String)newValue);
        return;
      case IecPackage.TIME_OF_DAY_LITERAL__SECOND:
        setSecond((String)newValue);
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
      case IecPackage.TIME_OF_DAY_LITERAL__HOUR:
        setHour(HOUR_EDEFAULT);
        return;
      case IecPackage.TIME_OF_DAY_LITERAL__MINUTE:
        setMinute(MINUTE_EDEFAULT);
        return;
      case IecPackage.TIME_OF_DAY_LITERAL__SECOND:
        setSecond(SECOND_EDEFAULT);
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
      case IecPackage.TIME_OF_DAY_LITERAL__HOUR:
        return HOUR_EDEFAULT == null ? hour != null : !HOUR_EDEFAULT.equals(hour);
      case IecPackage.TIME_OF_DAY_LITERAL__MINUTE:
        return MINUTE_EDEFAULT == null ? minute != null : !MINUTE_EDEFAULT.equals(minute);
      case IecPackage.TIME_OF_DAY_LITERAL__SECOND:
        return SECOND_EDEFAULT == null ? second != null : !SECOND_EDEFAULT.equals(second);
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
    result.append(" (hour: ");
    result.append(hour);
    result.append(", minute: ");
    result.append(minute);
    result.append(", second: ");
    result.append(second);
    result.append(')');
    return result.toString();
  }

} //TimeOfDayLiteralImpl
