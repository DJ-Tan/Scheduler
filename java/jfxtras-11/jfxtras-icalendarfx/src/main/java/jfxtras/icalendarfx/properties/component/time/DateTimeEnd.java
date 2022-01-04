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
package jfxtras.icalendarfx.properties.component.time;

import java.time.temporal.Temporal;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.parameters.NonStandardParameter;
import jfxtras.icalendarfx.parameters.TimeZoneIdentifierParameter;
import jfxtras.icalendarfx.properties.PropBaseDateTime;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;

/**
 * <h2> RFC 5545, 3.8.2.2.  Date-Time End</h2>
 * 
 *<p>Property Name:  DTEND</p>
 *
 *<p>Purpose:  This property specifies the date and time that a calendar
 * component ends.</p>
 *
 *<p>Value Type:  The default value type is DATE-TIME.  The value type can
 * be set to a DATE value type.</p>
 * 
 *<p>Property Parameters:  IANA, {@link NonStandardParameter non-standard},
 * {@link ValueType value data type}, and {@link TimeZoneIdentifierParameter time
 * zone identifier} property parameters can be specified on this
 * property.</p>
 * 
 * <p>Conformance:  This property can be specified in {@link VEvent VEVENT} or
 * {@link VFreeBusy} calendar components.</p>
 * 
 *<p>Description:  Within the {@link VEvent VEVENT} calendar component, this property
 * defines the date and time by which the event ends.  The value type
 * of this property MUST be the same as the {@link DateTimeStart DTSTART} property, and
 * its value MUST be later in time than the value of the {@link DateTimeStart DTSTART}
 * property.  Furthermore, this property MUST be specified as a date
 * with local time if and only if the {@link DateTimeStart DTSTART} property is also
 * specified as a date with local time.</p>
 * 
 *<p>Within the {@link VFreeBusy} calendar component, this property defines
 * the end date and time for the free or busy time information.  The
 * time MUST be specified in the UTC time format.  The value MUST be
 * later in time than the value of the {@link DateTimeStart DTSTART} property.</p>
 * 
 *<p>Format Definition:  This property is defined by the following
 * notation:<br>
 * 
 *<ul>
 *<li>dtend
 *  <ul>
 *  <li>{@link DateTimeEnd DTEND} dtendparam ":" dtendval CRLF
 *  </ul>
 *<li>dtendparam
 *  <ul>
 *  <li>The following are OPTIONAL, but MUST NOT occur more than once.
 *    <ul>
 *    <li>";" {@link ValueType VALUE} "=" {@link ValueType#DATE} or {@link ValueType#DATE_TIME}
 *    <li>";" {@link TimeZoneIdentifierParameter TZID}
 *    </ul>
 *  <li>The following are OPTIONAL, and MAY occur more than once.
 *    <ul>
 *    <li>";" {@link OtherParameter}
 *    </ul>
 *  </ul>
 *</ul>
 *
 *<p>Example:  The following is an example of this property:
 *<ul>
 *<li>DTEND:19960401T150000Z
 *<li>DTEND;VALUE=DATE:19980704
 *</ul>
 */
public class DateTimeEnd extends PropBaseDateTime<Temporal, DateTimeEnd>
{    
   public DateTimeEnd(Temporal temporal)
    {
        super(temporal);
    }
    
    public DateTimeEnd(DateTimeEnd source)
    {
        super(source);
    }
    
    public DateTimeEnd()
    {
        super();
    }
    
    /** Parse string to Temporal.  Not type safe.  Implementation must
     * ensure parameterized type is the same as date-time represented by String parameter */
    public static DateTimeEnd parse(String value)
    {
    	return DateTimeEnd.parse(new DateTimeEnd(), value);
    }
    
    /** Parse string with Temporal class explicitly provided as parameter */
    public static DateTimeEnd parse(Class<? extends Temporal> clazz, String value)
    {
        DateTimeEnd property = DateTimeEnd.parse(new DateTimeEnd(), value);
        clazz.cast(property.getValue()); // class check
        return property;
    }
}
