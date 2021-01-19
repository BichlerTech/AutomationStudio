/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.Expression;
import com.bichler.iec.iec.FunctionDeclaration;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.ParamAssignment;
import com.bichler.iec.iec.VariableAccess;

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
 * An implementation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ExpressionImpl#getVariable <em>Variable</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ExpressionImpl#getFbname <em>Fbname</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ExpressionImpl#getOpenbr <em>Openbr</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ExpressionImpl#getParamassignment <em>Paramassignment</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ExpressionImpl#getClosebr <em>Closebr</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ExpressionImpl#getExpression <em>Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExpressionImpl extends MinimalEObjectImpl.Container implements Expression
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
  protected ExpressionImpl()
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
    return IecPackage.Literals.EXPRESSION;
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION__VARIABLE, oldVariable, newVariable);
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
        msgs = ((InternalEObject)variable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.EXPRESSION__VARIABLE, null, msgs);
      if (newVariable != null)
        msgs = ((InternalEObject)newVariable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.EXPRESSION__VARIABLE, null, msgs);
      msgs = basicSetVariable(newVariable, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION__VARIABLE, newVariable, newVariable));
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
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, IecPackage.EXPRESSION__FBNAME, oldFbname, fbname));
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
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION__FBNAME, oldFbname, fbname));
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
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION__OPENBR, oldOpenbr, openbr));
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
      paramassignment = new EObjectContainmentEList<ParamAssignment>(ParamAssignment.class, this, IecPackage.EXPRESSION__PARAMASSIGNMENT);
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
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION__CLOSEBR, oldClosebr, closebr));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION__EXPRESSION, oldExpression, newExpression);
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
        msgs = ((InternalEObject)expression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.EXPRESSION__EXPRESSION, null, msgs);
      if (newExpression != null)
        msgs = ((InternalEObject)newExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.EXPRESSION__EXPRESSION, null, msgs);
      msgs = basicSetExpression(newExpression, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION__EXPRESSION, newExpression, newExpression));
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
      case IecPackage.EXPRESSION__VARIABLE:
        return basicSetVariable(null, msgs);
      case IecPackage.EXPRESSION__PARAMASSIGNMENT:
        return ((InternalEList<?>)getParamassignment()).basicRemove(otherEnd, msgs);
      case IecPackage.EXPRESSION__EXPRESSION:
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
      case IecPackage.EXPRESSION__VARIABLE:
        return getVariable();
      case IecPackage.EXPRESSION__FBNAME:
        if (resolve) return getFbname();
        return basicGetFbname();
      case IecPackage.EXPRESSION__OPENBR:
        return getOpenbr();
      case IecPackage.EXPRESSION__PARAMASSIGNMENT:
        return getParamassignment();
      case IecPackage.EXPRESSION__CLOSEBR:
        return getClosebr();
      case IecPackage.EXPRESSION__EXPRESSION:
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
      case IecPackage.EXPRESSION__VARIABLE:
        setVariable((VariableAccess)newValue);
        return;
      case IecPackage.EXPRESSION__FBNAME:
        setFbname((FunctionDeclaration)newValue);
        return;
      case IecPackage.EXPRESSION__OPENBR:
        setOpenbr((String)newValue);
        return;
      case IecPackage.EXPRESSION__PARAMASSIGNMENT:
        getParamassignment().clear();
        getParamassignment().addAll((Collection<? extends ParamAssignment>)newValue);
        return;
      case IecPackage.EXPRESSION__CLOSEBR:
        setClosebr((String)newValue);
        return;
      case IecPackage.EXPRESSION__EXPRESSION:
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
      case IecPackage.EXPRESSION__VARIABLE:
        setVariable((VariableAccess)null);
        return;
      case IecPackage.EXPRESSION__FBNAME:
        setFbname((FunctionDeclaration)null);
        return;
      case IecPackage.EXPRESSION__OPENBR:
        setOpenbr(OPENBR_EDEFAULT);
        return;
      case IecPackage.EXPRESSION__PARAMASSIGNMENT:
        getParamassignment().clear();
        return;
      case IecPackage.EXPRESSION__CLOSEBR:
        setClosebr(CLOSEBR_EDEFAULT);
        return;
      case IecPackage.EXPRESSION__EXPRESSION:
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
      case IecPackage.EXPRESSION__VARIABLE:
        return variable != null;
      case IecPackage.EXPRESSION__FBNAME:
        return fbname != null;
      case IecPackage.EXPRESSION__OPENBR:
        return OPENBR_EDEFAULT == null ? openbr != null : !OPENBR_EDEFAULT.equals(openbr);
      case IecPackage.EXPRESSION__PARAMASSIGNMENT:
        return paramassignment != null && !paramassignment.isEmpty();
      case IecPackage.EXPRESSION__CLOSEBR:
        return CLOSEBR_EDEFAULT == null ? closebr != null : !CLOSEBR_EDEFAULT.equals(closebr);
      case IecPackage.EXPRESSION__EXPRESSION:
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

} //ExpressionImpl
