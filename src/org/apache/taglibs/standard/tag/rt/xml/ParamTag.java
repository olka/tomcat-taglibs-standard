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

package org.apache.taglibs.standard.tag.rt.xml;

import javax.servlet.jsp.JspTagException;

import org.apache.taglibs.standard.tag.common.xml.ParamSupport;

/**
 * <p>A handler for &lt;param&gt; that supports rtexprvalue-based
 * attributes.</p>
 *
 * @author Shawn Bayern
 */

public class ParamTag extends ParamSupport {

    //*********************************************************************
    // Accessor methods

    // for tag attribute
    public void setName(String name) throws JspTagException {
        this.name = name;
    }

    // for tag attribute
    public void setValue(Object value) throws JspTagException {
        this.value = value;
    }

}
