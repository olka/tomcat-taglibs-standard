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

package org.apache.taglibs.standard.lang.jpath.expression;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.PageContext;

import org.apache.taglibs.standard.lang.jpath.adapter.IterationContext;

/**
 * The ListLiteral class
 *
 *
 * @author <a href='mailto:scott.hasse@isthmusgroup.com'>Scott Hasse</a>
 * @version
 */
public class ListLiteral extends SimpleNode {

    /**
     * Used to create an instance of the ListLiteral class
     *
     *
     * @param id
     *
     */
    public ListLiteral(int id) {
        super(id);
    }

    /**
     * Used to create an instance of the ListLiteral class
     *
     *
     * @param p
     * @param id
     *
     */
    public ListLiteral(Parser p, int id) {
        super(p, id);
    }

    /**
     * Provides a method to print a normalized version of the original
     * expression.  The normalized version has standardized spacing and
     * parenthesis, and can be used to compare expressions formatted
     * in different ways to see if they are actually the same expression.
     *
     *
     * @return The normalized version of the original expression
     *
     */
    public String toNormalizedString() {

        boolean first = true;
        String normalized;

        normalized = "{";

        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                if (!first) {
                    normalized = normalized + ",";
                }

                first = false;

                SimpleNode n = (SimpleNode) children[i];

                if (n != null) {
                    normalized = normalized + n.toNormalizedString();
                }
            }
        }

        normalized = normalized + "}";

        return normalized;
    }

    /**
     * This method evaluates this node of the expression and all child nodes.
     * It returns the result of the
     * evaluation as an <tt>Object</tt>.  If any problems are encountered
     * during the evaluation, an <tt>EvaluationException</tt> is thrown.
     *
     *
     * @param pageContext the current JSP PageContext
     *
     * @param icontext the Iteration Context of the expression.  If there is
     *         no interation context, this should be null.
     *
     * @return the result of the expression evaluation as an object
     *
     * @throws EvaluationException if a problem is encountered during the
     *         evaluation
     */
    public Object evaluate(PageContext pageContext, IterationContext icontext)
            throws EvaluationException {

        Collection c = new ArrayList();

        for (int i = 0; i < jjtGetNumChildren(); i++) {
            c.add(jjtGetChild(i).evaluate(pageContext, icontext));
        }

        return c;
    }
}