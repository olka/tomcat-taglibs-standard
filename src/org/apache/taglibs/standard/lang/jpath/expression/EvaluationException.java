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


/**
 * The EvaluationException class
 *
 *
 * @author <a href='mailto:scott.hasse@isthmusgroup.com'>Scott Hasse</a>
 * @version
 */
public class EvaluationException extends Exception {

    protected boolean specialConstructor;
    public String image;
    protected SimpleNode node;

    /**
     * The end of line string for this machine.
     */
    protected String eol = System.getProperty("line.separator", "\n");

    /**
     * The following constructors are for use by you for whatever
     * purpose you can think of.  Constructing the exception in this
     * manner makes the exception behave in the normal way - i.e., as
     * documented in the class "Throwable".
     */
    public EvaluationException() {

        super();

        specialConstructor = false;
    }

    /**
     * Used to create an instance of the EvaluationException class
     *
     *
     * @param message
     *
     */
    public EvaluationException(String message) {

        super(message);

        specialConstructor = false;
    }

    /**
     * Used to create an instance of the EvaluationException class
     *
     *
     * @param currentNode
     * @param message
     *
     */
    public EvaluationException(SimpleNode currentNode, String message) {

        super(message);

        specialConstructor = true;
        node = currentNode;
    }

    /**
     * This method has the standard behavior when this object has been
     * created using the standard constructors.  Otherwise, it uses
     * "currentToken" to generate an evaluation
     * error message and returns it.  If this object has been created
     * due to an evaluation error, and you do not catch it (it gets thrown
     * during the evaluation), then this method is called during the printing
     * of the final stack trace, and hence the correct error message
     * gets displayed.
     *
     * @return the error message
     */
    public String getMessage() {

        if (!specialConstructor) {
            return super.getMessage();
        }

        String expected = "";
        int maxSize = 1;
        String retval = eol + "Encountered \"";

        retval += super.getMessage();
        retval += "\"" + eol;
        retval += "at : " + eol;
        retval += node.rootOriginalString() + eol;

        for (int i = 0; i < node.firstToken.beginColumn - 1; i++) {
            retval += " ";
        }

        for (int i = 0; i
                < node.lastToken.endColumn
                    - (node.firstToken.beginColumn - 1); i++) {
            retval += "^";
        }

        retval += eol;
        retval += "beginning at column " + node.firstToken.beginColumn + "."
                + eol;
        retval += "ending at column " + node.lastToken.endColumn + "." + eol;

        return retval;
    }

    /**
     * Used to convert raw characters to their escaped version
     * when these raw version cannot be used as part of an ASCII
     * string literal.
     *
     * @param str
     *
     * @return
     */
    protected String add_escapes(String str) {

        StringBuffer retval = new StringBuffer();
        char ch;

        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {

            case 0 :
                continue;
            case '\b' :
                retval.append("\\b");

                continue;
            case '\t' :
                retval.append("\\t");

                continue;
            case '\n' :
                retval.append("\\n");

                continue;
            case '\f' :
                retval.append("\\f");

                continue;
            case '\r' :
                retval.append("\\r");

                continue;
            case '\"' :
                retval.append("\\\"");

                continue;
            case '\'' :
                retval.append("\\\'");

                continue;
            case '\\' :
                retval.append("\\\\");

                continue;
            default :
                if ((ch = str.charAt(i)) < 0x20 || (ch > 0x7e)) {
                    String s = "0000" + Integer.toString(ch, 16);

                    retval.append("\\u"
                            + s.substring(s.length() - 4, s.length()));
                } else {
                    retval.append(ch);
                }

                continue;
            }
        }

        return retval.toString();
    }
}