/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.DateAndTimeLiteral;
import com.bichler.iec.iec.IecPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Date And Time Literal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.DateAndTimeLiteralImpl#getYear <em>Year</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.DateAndTimeLiteralImpl#getMonth <em>Month</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.DateAndTimeLiteralImpl#getDay <em>Day</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.DateAndTimeLiteralImpl#getHour <em>Hour</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.DateAndTimeLiteralImpl#getMinute <em>Minute</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.DateAndTimeLiteralImpl#getSecond <em>Second</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DateAndTimeLiteralImpl extends TimeLiteralImpl implements DateAndTimeLiteral
{
  /**
   * The default value of the '{@link #getYear() <em>Year</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getYear()
   * @generated
   * @ordered
   */
  protected static final String YEAR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getYear() <em>Year</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getYear()
   * @generated
   * @ordered
   */
  protected String year = YEAR_EDEFAULT;

  /**
   * The default value of the '{@link #getMonth() <em>Month</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMonth()
   * @generated
   * @ordered
   */
  protected static final String MONTH_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMonth() <em>Month</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMonth()
   * @generated
   * @ordered
   */
  protected String month = MONTH_EDEFAULT;

  /**
   * The default value of the '{@link #getDay() <em>Day</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDay()
   * @generated
   * @ordered
   */
  protected static final String DAY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDay() <em>Day</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDay()
   * @generated
   * @ordered
   */
  protected String day = DAY_EDEFAULT;

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
  protected DateAndTimeLiteralImpl()
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
    return IecPackage.Literals.DATE_AND_TIME_LITERAL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getYear()
  {
    return year;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setYear(String newYear)
  {
    String oldYear = year;
    year = newYear;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.DATE_AND_TIME_LITERAL__YEAR, oldYear, year));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getMonth()
  {
    return month;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setMonth(String newMonth)
  {
    String oldMonth = month;
    month = newMonth;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.DATE_AND_TIME_LITERAL__MONTH, oldMonth, month));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getDay()
  {
    return day;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setDay(String newDay)
  {
    String oldDay = day;
    day = newDay;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.DATE_AND_TIME_LITERAL__DAY, oldDay, day));
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
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.DATE_AND_TIME_LITERAL__HOUR, oldHour, hour));
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
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.DATE_AND_TIME_LITERAL__MINUTE, oldMinute, minute));
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
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.DATE_AND_TIME_LITERAL__SECOND, oldSecond, second));
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
      case IecPackage.DATE_AND_TIME_LITERAL__YEAR:
        return getYear();
      case IecPackage.DATE_AND_TIME_LITERAL__MONTH:
        return getMonth();
      case IecPackage.DATE_AND_TIME_LITERAL__DAY:
        return getDay();
      case IecPackage.DATE_AND_TIME_LITERAL__HOUR:
        return getHour();
      case IecPackage.DATE_AND_TIME_LITERAL__MINUTE:
        return getMinute();
      case IecPackage.DATE_AND_TIME_LITERAL__SECOND:
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
      case IecPackage.DATE_AND_TIME_LITERAL__YEAR:
        setYear((String)newValue);
        return;
      case IecPackage.DATE_AND_TIME_LITERAL__MONTH:
        setMonth((String)newValue);
        return;
      case IecPackage.DATE_AND_TIME_LITERAL__DAY:
        setDay((String)newValue);
        return;
      case IecPackage.DATE_AND_TIME_LITERAL__HOUR:
        setHour((String)newValue);
        return;
      case IecPackage.DATE_AND_TIME_LITERAL__MINUTE:
        setMinute((String)newValue);
        return;
      case IecPackage.DATE_AND_TIME_LITERAL__SECOND:
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
      case IecPackage.DATE_AND_TIME_LITERAL__YEAR:
        setYear(YEAR_EDEFAULT);
        return;
      case IecPackage.DATE_AND_TIME_LITERAL__MONTH:
        setMonth(MONTH_EDEFAULT);
        return;
      case IecPackage.DATE_AND_TIME_LITERAL__DAY:
        setDay(DAY_EDEFAULT);
        return;
      case IecPackage.DATE_AND_TIME_LITERAL__HOUR:
        setHour(HOUR_EDEFAULT);
        return;
      case IecPackage.DATE_AND_TIME_LITERAL__MINUTE:
        setMinute(MINUTE_EDEFAULT);
        return;
      case IecPackage.DATE_AND_TIME_LITERAL__SECOND:
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
      case IecPackage.DATE_AND_TIME_LITERAL__YEAR:
        return YEAR_EDEFAULT == null ? year != null : !YEAR_EDEFAULT.equals(year);
      case IecPackage.DATE_AND_TIME_LITERAL__MONTH:
        return MONTH_EDEFAULT == null ? month != null : !MONTH_EDEFAULT.equals(month);
      case IecPackage.DATE_AND_TIME_LITERAL__DAY:
        return DAY_EDEFAULT == null ? day != null : !DAY_EDEFAULT.equals(day);
      case IecPackage.DATE_AND_TIME_LITERAL__HOUR:
        return HOUR_EDEFAULT == null ? hour != null : !HOUR_EDEFAULT.equals(hour);
      case IecPackage.DATE_AND_TIME_LITERAL__MINUTE:
        return MINUTE_EDEFAULT == null ? minute != null : !MINUTE_EDEFAULT.equals(minute);
      case IecPackage.DATE_AND_TIME_LITERAL__SECOND:
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
    result.append(" (year: ");
    result.append(year);
    result.append(", month: ");
    result.append(month);
    result.append(", day: ");
    result.append(day);
    result.append(", hour: ");
    result.append(hour);
    result.append(", minute: ");
    result.append(minute);
    result.append(", second: ");
    result.append(second);
    result.append(')');
    return result.toString();
  }

} //DateAndTimeLiteralImpl
