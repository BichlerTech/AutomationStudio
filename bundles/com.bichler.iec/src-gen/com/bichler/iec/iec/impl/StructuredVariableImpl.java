/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.StructuredVariable;
import com.bichler.iec.iec.Variable;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Structured Variable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.StructuredVariableImpl#getRecordVariable <em>Record Variable</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.StructuredVariableImpl#getFieldSelector <em>Field Selector</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StructuredVariableImpl extends MultiElementVariableImpl implements StructuredVariable
{
  /**
   * The cached value of the '{@link #getRecordVariable() <em>Record Variable</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRecordVariable()
   * @generated
   * @ordered
   */
  protected Variable recordVariable;

  /**
   * The default value of the '{@link #getFieldSelector() <em>Field Selector</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFieldSelector()
   * @generated
   * @ordered
   */
  protected static final String FIELD_SELECTOR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFieldSelector() <em>Field Selector</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFieldSelector()
   * @generated
   * @ordered
   */
  protected String fieldSelector = FIELD_SELECTOR_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected StructuredVariableImpl()
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
    return IecPackage.Literals.STRUCTURED_VARIABLE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Variable getRecordVariable()
  {
    if (recordVariable != null && recordVariable.eIsProxy())
    {
      InternalEObject oldRecordVariable = (InternalEObject)recordVariable;
      recordVariable = (Variable)eResolveProxy(oldRecordVariable);
      if (recordVariable != oldRecordVariable)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, IecPackage.STRUCTURED_VARIABLE__RECORD_VARIABLE, oldRecordVariable, recordVariable));
      }
    }
    return recordVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable basicGetRecordVariable()
  {
    return recordVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setRecordVariable(Variable newRecordVariable)
  {
    Variable oldRecordVariable = recordVariable;
    recordVariable = newRecordVariable;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.STRUCTURED_VARIABLE__RECORD_VARIABLE, oldRecordVariable, recordVariable));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getFieldSelector()
  {
    return fieldSelector;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setFieldSelector(String newFieldSelector)
  {
    String oldFieldSelector = fieldSelector;
    fieldSelector = newFieldSelector;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.STRUCTURED_VARIABLE__FIELD_SELECTOR, oldFieldSelector, fieldSelector));
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
      case IecPackage.STRUCTURED_VARIABLE__RECORD_VARIABLE:
        if (resolve) return getRecordVariable();
        return basicGetRecordVariable();
      case IecPackage.STRUCTURED_VARIABLE__FIELD_SELECTOR:
        return getFieldSelector();
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
      case IecPackage.STRUCTURED_VARIABLE__RECORD_VARIABLE:
        setRecordVariable((Variable)newValue);
        return;
      case IecPackage.STRUCTURED_VARIABLE__FIELD_SELECTOR:
        setFieldSelector((String)newValue);
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
      case IecPackage.STRUCTURED_VARIABLE__RECORD_VARIABLE:
        setRecordVariable((Variable)null);
        return;
      case IecPackage.STRUCTURED_VARIABLE__FIELD_SELECTOR:
        setFieldSelector(FIELD_SELECTOR_EDEFAULT);
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
      case IecPackage.STRUCTURED_VARIABLE__RECORD_VARIABLE:
        return recordVariable != null;
      case IecPackage.STRUCTURED_VARIABLE__FIELD_SELECTOR:
        return FIELD_SELECTOR_EDEFAULT == null ? fieldSelector != null : !FIELD_SELECTOR_EDEFAULT.equals(fieldSelector);
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
    result.append(" (fieldSelector: ");
    result.append(fieldSelector);
    result.append(')');
    return result.toString();
  }

} //StructuredVariableImpl
