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

package org.apache.taglibs.standard.lang.jstl.test;

import java.beans.PropertyEditorSupport;

/**
 *
 * PropertyEditor for parsing Bean2
 * 
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author$
 **/

public class Bean2Editor
  extends PropertyEditorSupport
{
  //-------------------------------------
  public void setAsText (String pText)
    throws IllegalArgumentException
  {
    if ("badvalue".equals (pText)) {
      throw new IllegalArgumentException ("Bad value " + pText);
    }
    else {
      setValue (new Bean2 (pText));
    }
  }

  //-------------------------------------
}
