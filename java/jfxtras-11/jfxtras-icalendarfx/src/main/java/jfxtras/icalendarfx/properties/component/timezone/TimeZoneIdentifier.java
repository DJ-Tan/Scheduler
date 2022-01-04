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
package jfxtras.icalendarfx.properties.component.timezone;

import java.time.DateTimeException;
import java.time.ZoneId;

import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * TZID
 * Time Zone Identifier
 * RFC 5545, 3.8.3.1, page 102
 * 
 * To specify the identifier for the time zone definition for
 * a time component in the property value
 * 
 * LIMITATION: globally unique time zones are stored as strings and the ZoneID is null.
 * Only the toString and toContentLine methods will display the original string.  Another
 * method to convert the unique time zone string into a ZoneId is required.
 * 
 * EXAMPLE:
 * TZID:America/Los_Angeles
 * 
 * @author David Bal
 * @see VTimeZone
 */
public class TimeZoneIdentifier extends VPropertyBase<ZoneId, TimeZoneIdentifier>
{
    private final static StringConverter<ZoneId> CONVERTER = new StringConverter<ZoneId>()
    {
        @Override
        public String toString(ZoneId object)
        {
            // null means value is unknown and non-converted string in PropertyBase unknownValue should be used instead
            return (object == null) ? null: object.toString();
        }

        @Override
        public ZoneId fromString(String string)
        {
            try
            {
            return ZoneId.of(string);
            } catch (DateTimeException e)
            {
                // null means value is unknown and should be stored as non-converted string by PropertyBase
                return null;
            }           
        }
    };
    
    public TimeZoneIdentifier(TimeZoneIdentifier source)
    {
        super(source);
    }
    
    public TimeZoneIdentifier(ZoneId value)
    {
        this();
        setValue(value);
    }

    public TimeZoneIdentifier()
    {
        super();
        setConverter(CONVERTER);
    }
    
    @Override
    public boolean isValid()
    {
        boolean isNonGlobalOK = (getValue() != null);
        boolean isGloballyUniqueOK = ((getUnknownValue() != null) && (getUnknownValue().charAt(0) == '/'));
//        System.out.println("time zone isValid:" + propertyType() + " " + getValueParameter());
        boolean isValueTypeOK = (getValueType() != null) ? allowedValueTypes.contains(getValueType().getValue()) : true;
//        System.out.println("TimeZoneIdentifier isValid:" + isNonGlobalOK + " " + isGloballyUniqueOK + " " + isValueTypeOK);
        return (isNonGlobalOK || isGloballyUniqueOK) && isValueTypeOK;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean superEquals = super.equals(obj);
        if (superEquals == false)
        {
            return false;
        }
        TimeZoneIdentifier testObj = (TimeZoneIdentifier) obj;
        boolean unknownEquals = (getUnknownValue() == null) ? testObj.getUnknownValue() == null : getUnknownValue().equals(testObj.getUnknownValue());
        return unknownEquals;
    }
    
    public static TimeZoneIdentifier parse(String content)
    {
    	return TimeZoneIdentifier.parse(new TimeZoneIdentifier(), content);
    }
}
