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

package org.apache.taglibs.standard.lang.jpath.adapter;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.PageContext;

import org.apache.taglibs.standard.lang.jpath.expression.EvaluationException;
import org.apache.taglibs.standard.lang.jpath.expression.Expression;
import org.apache.taglibs.standard.lang.jpath.expression.Predicate;

public class JSPListUnion implements JSPList {

    private Expression predicate;

    private JSPList left;
    private JSPList right;
    private int position;
    private Object current;
    boolean first;

    public JSPListUnion(JSPList left, JSPList right) {
        this.left = left;
        this.right = right;
        this.position = 0;
        first = true;
    }

    public Object next() {
        if (first) {
            if (left.hasNext()) {
                current = left.next();
            } else {
                first = false;
                current = right.next();
            }
        } else {
            current = right.next();
        }
        position++;
        return current;
    }

    public Object getCurrent() {
        return current;
    }

    public boolean hasNext() {
        boolean result = left.hasNext() || right.hasNext();
        return result;
    }

    public int getPosition() {
        return position;
    }

    public int getLast() {
        return (left.getLast() + right.getLast());
    }

    public boolean applyPredicate(PageContext pageContext, Predicate predicate) throws ConversionException, EvaluationException {
        Object result;
        boolean oneItem = false;
        boolean predicateTrue;
        if (position != 0) {
            throw new ConversionException("You cannot apply a predicate to "
                    + "a JSPList that has begun to be iterated");
        }
        Collection predicated = new ArrayList();
        while (this.hasNext()) {
            this.next();
            result = predicate.evaluate(pageContext, new JSPListIterationContext(this));
            if (result instanceof Double) {
                oneItem = true;
                predicateTrue = ((Double)result).doubleValue() == position;
            } else {
                oneItem = false;
                predicateTrue = Convert.toBoolean(result).booleanValue();
            }
            if (predicateTrue) {
                predicated.add(current);
            }
        }
        this.left = Convert.toJSPList(predicated);
        this.right = Convert.toJSPList(new ArrayList());
        this.position = 0;
        first = true;
        return oneItem;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}