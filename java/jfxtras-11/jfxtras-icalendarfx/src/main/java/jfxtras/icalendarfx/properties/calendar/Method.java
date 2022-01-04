/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties.calendar;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.calendar.Method;
import jfxtras.icalendarfx.properties.calendar.Method.MethodType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * METHOD
 * RFC 5545, 3.7.2, page 77
 * 
 * This property defines the iCalendar object method associated with the calendar object.
 * 
 * No methods are defined by this specification.  This is the subject
 * of other specifications, such as the iCalendar Transport-
 * independent Interoperability Protocol (iTIP) defined by [2446bis]
 * 
 * Example:
 * METHOD:PUBLISH
 * 
 * @author David Bal
 * @see VCalendar
 */
public class Method extends VPropertyBase<MethodType, Method> implements VElement
{
    private final static StringConverter<MethodType> CONVERTER = new StringConverter<MethodType>()
    {
        @Override
        public String toString(MethodType object)
        {
            return object.toString();
        }

        @Override
        public MethodType fromString(String string)
        {
            return MethodType.valueOf(string.toUpperCase());
        }
    };
    
    public Method(Method source)
    {
        super(source);
    }

    public Method(MethodType methodType)
    {
       super(methodType);
       setConverter(CONVERTER);
    }
    
    public Method()
    {
       super();
       setConverter(CONVERTER);
    }
    
    public static Method parse(String content)
    {
    	return Method.parse(new Method(), content);
    }
    
    /** Method types from RFC 5546 */
    public enum MethodType
    {
        PUBLISH,
        REQUEST,
        REPLY,
        ADD,
        CANCEL,
        REFRESH,
        COUNTER,
        DECLINECOUNTER;
    }
}
