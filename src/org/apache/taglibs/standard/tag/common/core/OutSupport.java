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

package org.apache.taglibs.standard.tag.common.core;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * <p>Support for handlers of the &lt;out&gt; tag, which simply evalutes and
 * prints the result of the expression it's passed.  If the result is
 * null, we print the value of the 'default' attribute's expression or
 * our body (which two are mutually exclusive, although this constraint
 * is enforced outside this handler, in our TagLibraryValidator).</p>
 *
 * @author Shawn Bayern
 */
public class OutSupport extends BodyTagSupport {

    /*
     * (One almost wishes XML and JSP could support "anonymous tags,"
     * given the amount of trouble we had naming this one!)  :-)  - sb
     */

    //*********************************************************************
    // Internal state

    protected Object value;                     // tag attribute
    protected String def;			// tag attribute
    protected boolean escapeXml;		// tag attribute
    private boolean needBody;			// non-space body needed?

    //*********************************************************************
    // Construction and initialization

    /**
     * Constructs a new handler.  As with TagSupport, subclasses should
     * not provide other constructors and are expected to call the
     * superclass constructor.
     */
    public OutSupport() {
        super();
        init();
    }

    // resets local state
    private void init() {
        value = def = null;
        escapeXml = true;
	needBody = false;
    }

    // Releases any resources we may have (or inherit)
    public void release() {
        super.release();
        init();
    }


    //*********************************************************************
    // Tag logic

    // evaluates 'value' and determines if the body should be evaluted
    public int doStartTag() throws JspException {

      needBody = false;			// reset state related to 'default'
      super.bodyContent = null;  // clean-up body (just in case container is pooling tag handlers)

      try {
	// print value if available; otherwise, try 'default'
	if (value != null) {
            out(pageContext, escapeXml, value.toString());
	    return SKIP_BODY;
	} else {
	    // if we don't have a 'default' attribute, just go to the body
	    if (def == null) {
		needBody = true;
		return EVAL_BODY_BUFFERED;
	    }

	    // if we do have 'default', print it
	    if (def != null) {
		// good 'default'
                out(pageContext, escapeXml, def.toString());
	    }
	    return SKIP_BODY;
	}
      } catch (IOException ex) {
	throw new JspException(ex.getMessage(), ex);
      }
    }

    // prints the body if necessary; reports errors
    public int doEndTag() throws JspException {
      try {
	if (!needBody)
	    return EVAL_PAGE;		// nothing more to do

	// trim and print out the body
	if (bodyContent != null && bodyContent.getString() != null)
            out(pageContext, escapeXml, bodyContent.getString().trim());
	return EVAL_PAGE;
      } catch (IOException ex) {
	throw new JspException(ex.getMessage(), ex);
      }
    }


    //*********************************************************************
    // Public utility methods

    /**
     * Outputs <tt>text</tt> to <tt>pageContext</tt>'s current JspWriter.
     * If <tt>escapeXml</tt> is true, performs the following substring
     * replacements (to facilitate output to XML/HTML pages):
     *
     *    & -> &amp;
     *    < -> &lt;
     *    > -> &gt;
     *    " -> &#034;
     *    ' -> &#039;
     */
    public static void out(PageContext pageContext,
                           boolean escapeXml,
                           String text) throws IOException {
        JspWriter w = pageContext.getOut();
	if (!escapeXml)
            w.print(text);
        else {
            // avoid needless double-buffering (is this really more efficient?)
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '&')
                    w.print("&amp;");
                else if (c == '<')
                    w.print("&lt;");
                else if (c == '>')
                    w.print("&gt;");
                else if (c == '"')
                    w.print("&#034;");
                else if (c == '\'')
                    w.print("&#039;");
                else
                    w.print(c);
            }
        }
    }
}
