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
package jfxtras.icalendarfx.parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jfxtras.icalendarfx.VElementBase;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.parameters.VParameter;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.parameters.VParameterElement;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * Base class of all iCalendar Parameters.  Parameters can't have children.
 * Example VALUE=DATE
 * 
 * @author David Bal
 *
 * @param <T> - type of value stored in the Parameter, such as String for text-based, or the enumerated type of the classes based on an enum
 * @param <U> - implemented subclass
 */
abstract public class VParameterBase<U,T> extends VElementBase implements VParameter<T>
{
    private VParent myParent;
    @Override
    public void setParent(VParent parent) { myParent = parent; }
    @Override
    public VParent getParent() { return myParent; }
    
    /*
     * PARAMETER VALUE
     */
    @Override
    public T getValue()
    {
        return value;
    }
    private T value;
    @Override
    public void setValue(T value)
    {
        this.value = value;
    }
    public void setValue(CharSequence value)
    {
    	parseContent(value.toString());
    }
    public U withValue(T value)
    {
        setValue(value);
        return (U) this;
    }
    public U withValue(CharSequence value)
    {
        setValue(value);
        return (U) this;
    }
    
    // convert value to string, is overridden for enum-based parameters to handle UNKNOWN value
    String valueAsString()
    {
    	if (getValue() == null) return "";
        return getConverter().toString(getValue());
    }
    
    @Override
    protected List<Message> parseContent(String content)
    {
        String valueString = extractValue(content);
        T value = getConverter().fromString(valueString);
        setValue(value);
        return Collections.EMPTY_LIST;
    }
    
    /**
     * STRING CONVERTER
     * 
     * Get the parameter value string converter.
     */ 
    protected final StringConverter<T> converter;
    private StringConverter<T> getConverter()
    {
        return converter;
    }
    
    /**
     * PARAMETER TYPE
     * 
     *  The enumerated type of the parameter.
     */
    final protected VParameterElement elementType;
    @Override
    public String name()
    {
    	return elementType.toString();
  	}
	
    @Override
    public String toString()
    {
        return name() + "=" + valueAsString();
    }

    @Override // Note: can't check equality of parents - causes stack overflow
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        VParameterBase<U,T> testObj = (VParameterBase<U,T>) obj;
        boolean valueEquals = getValue().equals(testObj.getValue());
        return valueEquals;
    }
    
    @Override // Note: can't check hashCode of parents - causes stack overflow
    public int hashCode()
    {
        int hash = super.hashCode();
        final int prime = 31;
        hash = prime * hash + getValue().hashCode();
        return hash;
    }
    
    /*
     * CONSTRUCTORS
     */
    VParameterBase(StringConverter<T> stringConverter)
    {
    	elementType = VParameterElement.enumFromClass(getClass());
        this.converter = elementType.getConverter();
    }

    VParameterBase(T value, StringConverter<T> stringConverter)
    {
        this(stringConverter);
        setValue(value);
    }

    
    VParameterBase(VParameterBase<U,T> source, StringConverter<T> stringConverter)
    {
        this(stringConverter);
        setValue(source.getValue());
        setParent(source.getParent());
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
        if (getValue() == null)
        {
            errors.add(name() + " value is null.  The parameter MUST have a value."); 
        }
        return errors;
    }
    
	/*
	 * Get value from a name-value pair separated by an equal sign
	 */
    static String extractValue(String content)
    {
        int equalsIndex = content.indexOf('=');
        final String valueString;
        if (equalsIndex > 0)
        {
            String name = content.substring(0, equalsIndex);
            boolean hasName1 = VParameterElement.enumFromName(name.toUpperCase()) != null;
//            boolean hasName2 = (IANAParameter.getRegisteredIANAParameters() != null) ? IANAParameter.getRegisteredIANAParameters().contains(name.toUpperCase()) : false;
            valueString = (hasName1) ? content.substring(equalsIndex+1) : content;    
        } else
        {
            valueString = content;
        }
        return valueString;
    }
}
