/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

/* +===================================================================
 * 
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 *
 */ 

package org.apache.taglibs.standard.lang.jpath.expression;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.servlet.jsp.PageContext;

import org.apache.taglibs.standard.lang.jpath.adapter.IterationContext;

/**
 * The MethodCall class
 *
 *
 * @author <a href='mailto:scott.hasse@isthmusgroup.com'>Scott Hasse</a>
 * @version
 */
public class MethodCall extends SimpleNode implements Introspectable {

    /**
     * Used to create an instance of the MethodCall class
     *
     *
     * @param id
     *
     */
    public MethodCall(int id) {
        super(id);
    }

    /**
     * Used to create an instance of the MethodCall class
     *
     *
     * @param p
     * @param id
     *
     */
    public MethodCall(Parser p, int id) {
        super(p, id);
    }

    /**
     * The evaluate method
     *
     *
     * @param pageContext
     * @param icontext
     * @param scope
     *
     * @return
     *
     * @throws EvaluationException
     *
     */
    public Object evaluate(
            PageContext pageContext, IterationContext icontext, int scope)
                throws EvaluationException {
        throw new EvaluationException(this,
                "A MethodCall must be called on " + "another object");
    }

    /**
     * The evaluate method
     *
     *
     * @param pageContext
     * @param icontext
     * @param parent
     *
     * @return
     *
     * @throws EvaluationException
     *
     */
    public Object evaluate(
            PageContext pageContext, IterationContext icontext, Object parent)
                throws EvaluationException {

        Object result = null;

        //try {
        String methodName = ((Identifier) jjtGetChild(0)).val;

        if (parent != null) {
            try {
                MethodDescriptor md = getFeatureDescriptor(parent.getClass(),
                                          methodName);
                Object[] args = new Object[jjtGetNumChildren() - 1];

                for (int i = 1; i < jjtGetNumChildren(); i++) {
                    args[i - 1] = jjtGetChild(i).evaluate(pageContext,
                            icontext);
                }

                if (md != null) {

                    //result = getAttribute(md, parent, args);
                    result = tempGetAttribute(parent, methodName, args);
                }
            } catch (IntrospectionException ie) {
                throw new EvaluationException(this,
                        "Introspection Exception:" + ie.getMessage());
            } catch (NoSuchMethodException nsme) {
                throw new EvaluationException(this,
                        "NoSuchMethodException:" + nsme.getMessage());
            } catch (IllegalAccessException iae) {
                throw new EvaluationException(this,
                        "IllegalAccessException:" + iae.getMessage());
            } catch (InvocationTargetException ite) {
                throw new EvaluationException(this,
                        "InvocationTargetException:" + ite.getMessage());
            }
        }

        //result = Convert.toJSPType(result);
        //} catch (ConversionException ce) {
        //throw new EvaluationException(this, ce.getMessage());
        //}
        return result;
    }

    /**
     * The getFeatureDescriptor method
     *
     *
     * @param c
     * @param key
     *
     * @return
     *
     * @throws IntrospectionException
     *
     */
    private MethodDescriptor getFeatureDescriptor(Class c, String key)
            throws IntrospectionException {

        BeanInfo beanInfo = Introspector.getBeanInfo(c);
        MethodDescriptor[] mda = beanInfo.getMethodDescriptors();

        for (int i = mda.length - 1; i >= 0; --i) {
            MethodDescriptor md = mda[i];

            if (md.getName().equals(key)) {
                return md;
            }
        }

        return null;
    }

    /**
     * The getAttribute method
     *
     *
     * @param md
     * @param o
     * @param args
     *
     * @return
     *
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     *
     */
    private Object getAttribute(MethodDescriptor md, Object o, Object[] args)
            throws NoSuchMethodException, IllegalAccessException,
                InvocationTargetException {

        Object result = null;
        Method m = md.getMethod();

        result = m.invoke(o, args);

        return result;
    }

    /**
     * The tempGetAttribute method
     *
     *
     * @param parent
     * @param key
     * @param args
     *
     * @return
     *
     * @throws IllegalAccessException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     *
     */
    private Object tempGetAttribute(Object parent, String key, Object[] args)
            throws IntrospectionException, NoSuchMethodException,
                IllegalAccessException, InvocationTargetException {

        Object result;
        Class c = parent.getClass();
        Method[] methods = c.getMethods();
        Method m = null;

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(key)) {
                m = methods[i];
            }
        }

        m = getPublicMethod(c, m.getName(), m.getParameterTypes());
        result = m.invoke(parent, args);

        return result;
    }

    /**
     * The getPublicMethod method
     *
     *
     * @param c
     * @param name
     * @param paramTypes
     *
     * @return
     *
     * @throws NoSuchMethodException
     *
     */
    private Method getPublicMethod(Class c, String name, Class[] paramTypes)
            throws NoSuchMethodException {

        Method result = null;

        if ((c.getModifiers() & Modifier.PUBLIC) == 0) {
            Class sc = c.getSuperclass();

            if (sc != null) {
                try {
                    result = getPublicMethod(sc, name, paramTypes);
                } catch (NoSuchMethodException nsme) {

                    //Intentionally ignored and thrown later
                }
            }

            if (result == null) {
                Class[] interfaces = c.getInterfaces();

                for (int i = 0; i < interfaces.length; i++) {
                    try {
                        result = getPublicMethod(interfaces[i], name,
                                paramTypes);
                    } catch (NoSuchMethodException nsme) {

                        //Intentionally ignored and thrown later
                    }
                }
            }
        } else {

            //It was public
            result = c.getMethod(name, paramTypes);
        }

        return result;
    }
}
