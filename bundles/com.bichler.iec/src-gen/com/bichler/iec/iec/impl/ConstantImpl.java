/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.Constant;
import com.bichler.iec.iec.DataSource;
import com.bichler.iec.iec.Expression;
import com.bichler.iec.iec.FunctionDeclaration;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.ParamAssignment;
import com.bichler.iec.iec.ProgDataSource;
import com.bichler.iec.iec.VariableAccess;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Constant</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ConstantImpl#getVariable <em>Variable</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ConstantImpl#getFbname <em>Fbname</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ConstantImpl#getOpenbr <em>Openbr</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ConstantImpl#getParamassignment <em>Paramassignment</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ConstantImpl#getClosebr <em>Closebr</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ConstantImpl#getExpression <em>Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConstantImpl extends InitialElementImpl implements Constant
{
  /**
   * The cached value of the '{@link #getVariable() <em>Variable</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVariable()
   * @generated
   * @ordered
   */
  protected VariableAccess variable;

  /**
   * The cached value of the '{@link #getFbname() <em>Fbname</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFbname()
   * @generated
   * @ordered
   */
  protected FunctionDeclaration fbname;

  /**
   * The default value of the '{@link #getOpenbr() <em>Openbr</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOpenbr()
   * @generated
   * @ordered
   */
  protected static final String OPENBR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOpenbr() <em>Openbr</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOpenbr()
   * @generated
   * @ordered
   */
  protected String openbr = OPENBR_EDEFAULT;

  /**
   * The cached value of the '{@link #getParamassignment() <em>Paramassignment</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParamassignment()
   * @generated
   * @ordered
   */
  protected EList<ParamAssignment> paramassignment;

  /**
   * The default value of the '{@link #getClosebr() <em>Closebr</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClosebr()
   * @generated
   * @ordered
   */
  protected static final String CLOSEBR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getClosebr() <em>Closebr</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClosebr()
   * @generated
   * @ordered
   */
  protected String closebr = CLOSEBR_EDEFAULT;

  /**
   * The cached value of the '{@link #getExpression() <em>Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpression()
   * @generated
   * @ordered
   */
  protected Expression expression;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ConstantImpl()
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
    return IecPackage.Literals.CONSTANT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VariableAccess getVariable()
  {
    return variable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetVariable(VariableAccess newVariable, NotificationChain msgs)
  {
    VariableAccess oldVariable = variable;
    variable = newVariable;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.CONSTANT__VARIABLE, oldVariable, newVariable);
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
  public void setVariable(VariableAccess newVariable)
  {
    if (newVariable != variable)
    {
      NotificationChain msgs = null;
      if (variable != null)
        msgs = ((InternalEObject)variable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.CONSTANT__VARIABLE, null, msgs);
      if (newVariable != null)
        msgs = ((InternalEObject)newVariable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.CONSTANT__VARIABLE, null, msgs);
      msgs = basicSetVariable(newVariable, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CONSTANT__VARIABLE, newVariable, newVariable));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FunctionDeclaration getFbname()
  {
    if (fbname != null && fbname.eIsProxy())
    {
      InternalEObject oldFbname = (InternalEObject)fbname;
      fbname = (FunctionDeclaration)eResolveProxy(oldFbname);
      if (fbname != oldFbname)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, IecPackage.CONSTANT__FBNAME, oldFbname, fbname));
      }
    }
    return fbname;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FunctionDeclaration basicGetFbname()
  {
    return fbname;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setFbname(FunctionDeclaration newFbname)
  {
    FunctionDeclaration oldFbname = fbname;
    fbname = newFbname;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CONSTANT__FBNAME, oldFbname, fbname));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getOpenbr()
  {
    return openbr;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setOpenbr(String newOpenbr)
  {
    String oldOpenbr = openbr;
    openbr = newOpenbr;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CONSTANT__OPENBR, oldOpenbr, openbr));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ParamAssignment> getParamassignment()
  {
    if (paramassignment == null)
    {
      paramassignment = new EObjectContainmentEList<ParamAssignment>(ParamAssignment.class, this, IecPackage.CONSTANT__PARAMASSIGNMENT);
    }
    return paramassignment;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getClosebr()
  {
    return closebr;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setClosebr(String newClosebr)
  {
    String oldClosebr = closebr;
    closebr = newClosebr;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CONSTANT__CLOSEBR, oldClosebr, closebr));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Expression getExpression()
  {
    return expression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetExpression(Expression newExpression, NotificationChain msgs)
  {
    Expression oldExpression = expression;
    expression = newExpression;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.CONSTANT__EXPRESSION, oldExpression, newExpression);
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
  public void setExpression(Expression newExpression)
  {
    if (newExpression != expression)
    {
      NotificationChain msgs = null;
      if (expression != null)
        msgs = ((InternalEObject)expression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.CONSTANT__EXPRESSION, null, msgs);
      if (newExpression != null)
        msgs = ((InternalEObject)newExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.CONSTANT__EXPRESSION, null, msgs);
      msgs = basicSetExpression(newExpression, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CONSTANT__EXPRESSION, newExpression, newExpression));
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
      case IecPackage.CONSTANT__VARIABLE:
        return basicSetVariable(null, msgs);
      case IecPackage.CONSTANT__PARAMASSIGNMENT:
        return ((InternalEList<?>)getParamassignment()).basicRemove(otherEnd, msgs);
      case IecPackage.CONSTANT__EXPRESSION:
        return basicSetExpression(null, msgs);
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
      case IecPackage.CONSTANT__VARIABLE:
        return getVariable();
      case IecPackage.CONSTANT__FBNAME:
        if (resolve) return getFbname();
        return basicGetFbname();
      case IecPackage.CONSTANT__OPENBR:
        return getOpenbr();
      case IecPackage.CONSTANT__PARAMASSIGNMENT:
        return getParamassignment();
      case IecPackage.CONSTANT__CLOSEBR:
        return getClosebr();
      case IecPackage.CONSTANT__EXPRESSION:
        return getExpression();
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
      case IecPackage.CONSTANT__VARIABLE:
        setVariable((VariableAccess)newValue);
        return;
      case IecPackage.CONSTANT__FBNAME:
        setFbname((FunctionDeclaration)newValue);
        return;
      case IecPackage.CONSTANT__OPENBR:
        setOpenbr((String)newValue);
        return;
      case IecPackage.CONSTANT__PARAMASSIGNMENT:
        getParamassignment().clear();
        getParamassignment().addAll((Collection<? extends ParamAssignment>)newValue);
        return;
      case IecPackage.CONSTANT__CLOSEBR:
        setClosebr((String)newValue);
        return;
      case IecPackage.CONSTANT__EXPRESSION:
        setExpression((Expression)newValue);
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
      case IecPackage.CONSTANT__VARIABLE:
        setVariable((VariableAccess)null);
        return;
      case IecPackage.CONSTANT__FBNAME:
        setFbname((FunctionDeclaration)null);
        return;
      case IecPackage.CONSTANT__OPENBR:
        setOpenbr(OPENBR_EDEFAULT);
        return;
      case IecPackage.CONSTANT__PARAMASSIGNMENT:
        getParamassignment().clear();
        return;
      case IecPackage.CONSTANT__CLOSEBR:
        setClosebr(CLOSEBR_EDEFAULT);
        return;
      case IecPackage.CONSTANT__EXPRESSION:
        setExpression((Expression)null);
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
      case IecPackage.CONSTANT__VARIABLE:
        return variable != null;
      case IecPackage.CONSTANT__FBNAME:
        return fbname != null;
      case IecPackage.CONSTANT__OPENBR:
        return OPENBR_EDEFAULT == null ? openbr != null : !OPENBR_EDEFAULT.equals(openbr);
      case IecPackage.CONSTANT__PARAMASSIGNMENT:
        return paramassignment != null && !paramassignment.isEmpty();
      case IecPackage.CONSTANT__CLOSEBR:
        return CLOSEBR_EDEFAULT == null ? closebr != null : !CLOSEBR_EDEFAULT.equals(closebr);
      case IecPackage.CONSTANT__EXPRESSION:
        return expression != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass)
  {
    if (baseClass == Expression.class)
    {
      switch (derivedFeatureID)
      {
        case IecPackage.CONSTANT__VARIABLE: return IecPackage.EXPRESSION__VARIABLE;
        case IecPackage.CONSTANT__FBNAME: return IecPackage.EXPRESSION__FBNAME;
        case IecPackage.CONSTANT__OPENBR: return IecPackage.EXPRESSION__OPENBR;
        case IecPackage.CONSTANT__PARAMASSIGNMENT: return IecPackage.EXPRESSION__PARAMASSIGNMENT;
        case IecPackage.CONSTANT__CLOSEBR: return IecPackage.EXPRESSION__CLOSEBR;
        case IecPackage.CONSTANT__EXPRESSION: return IecPackage.EXPRESSION__EXPRESSION;
        default: return -1;
      }
    }
    if (baseClass == ProgDataSource.class)
    {
      switch (derivedFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == DataSource.class)
    {
      switch (derivedFeatureID)
      {
        default: return -1;
      }
    }
    return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass)
  {
    if (baseClass == Expression.class)
    {
      switch (baseFeatureID)
      {
        case IecPackage.EXPRESSION__VARIABLE: return IecPackage.CONSTANT__VARIABLE;
        case IecPackage.EXPRESSION__FBNAME: return IecPackage.CONSTANT__FBNAME;
        case IecPackage.EXPRESSION__OPENBR: return IecPackage.CONSTANT__OPENBR;
        case IecPackage.EXPRESSION__PARAMASSIGNMENT: return IecPackage.CONSTANT__PARAMASSIGNMENT;
        case IecPackage.EXPRESSION__CLOSEBR: return IecPackage.CONSTANT__CLOSEBR;
        case IecPackage.EXPRESSION__EXPRESSION: return IecPackage.CONSTANT__EXPRESSION;
        default: return -1;
      }
    }
    if (baseClass == ProgDataSource.class)
    {
      switch (baseFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == DataSource.class)
    {
      switch (baseFeatureID)
      {
        default: return -1;
      }
    }
    return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
    result.append(" (openbr: ");
    result.append(openbr);
    result.append(", closebr: ");
    result.append(closebr);
    result.append(')');
    return result.toString();
  }

} //ConstantImpl
