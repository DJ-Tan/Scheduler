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
package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.ValueParameter;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;
import jfxtras.icalendarfx.utilities.Pair;

public class GeneralPropertyTest
{
    @Test
    public void canMakeParameterList()
    {
        String contentLine = "DESCRIPTION;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management";
        List<Pair<String, String>> valueList = ICalendarUtilities.parseInlineElementsToListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>("ALTREP", "\"CID:part3.msg.970415T083000@example.com\""));
        expectedList.add(new Pair<>("LANGUAGE", "en"));
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList2()
    {
        String contentLine = "DESCRIPTION:";
        List<Pair<String, String>> valueList = ICalendarUtilities.parseInlineElementsToListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, null));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList3()
    {
        String contentLine = ":";
        List<Pair<String, String>> valueList = ICalendarUtilities.parseInlineElementsToListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, null));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList4()
    {
        String contentLine = "SUMMARY:Department Party";
        List<Pair<String, String>> valueList = ICalendarUtilities.parseInlineElementsToListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "Department Party"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList5()
    {
        String contentLine = "Department Party";
        List<Pair<String, String>> valueList = ICalendarUtilities.parseInlineElementsToListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "Department Party"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList6()
    {
        String contentLine = ";LANGUAGE=en:TEST SUMMARY";
        List<Pair<String, String>> valueList = ICalendarUtilities.parseInlineElementsToListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>("LANGUAGE", "en"));
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "TEST SUMMARY"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList7()
    {
        String contentLine = "LANGUAGE=en:TEST SUMMARY";
        List<Pair<String, String>> valueList = ICalendarUtilities.parseInlineElementsToListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>("LANGUAGE", "en"));
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "TEST SUMMARY"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canChangeValueType()
    {
        String content = "X-MYPROP:1";
        NonStandardProperty madeProperty = NonStandardProperty.parse(content);
        madeProperty.setValueType(new ValueParameter(ValueType.INTEGER));
        assertEquals(1, madeProperty.getValue());
    }
    
    @Test
    public void canEscapeSpecialCharacters()
    {
        String content = "the dog, frog; ran \\ far\naway\\;";
        String contentEscaped = "the dog\\, frog\\; ran \\\\ far\\naway\\\\\\;";
        String content2 = (String) ValueType.TEXT.getConverter().fromString(contentEscaped);
        String contentEscaped2 = ValueType.TEXT.getConverter().toString(content);
        assertEquals(content2, content);
        assertEquals(contentEscaped2, contentEscaped);
    }
    
    @Test // can handle lowercase in property and parameter name
    public void canHandleLowercase()
    {
        String content = "sUmmARY;lanGUAGE=en:TEST SUMMARY";
        Summary madeProperty = Summary.parse(content);
        assertEquals("SUMMARY;LANGUAGE=en:TEST SUMMARY", madeProperty.toString());
        Summary expectedProperty = Summary.parse("TEST SUMMARY")
                .withLanguage("en");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void canIgnoreDuplicateParameter()
    {
        String contentLines = "SUMMARY;LANGUAGE=en;LANGUAGE=fr:TEST SUMMARY";
        Summary s = Summary.parse(contentLines);
        String expectedContent = "SUMMARY;LANGUAGE=en:TEST SUMMARY";
        assertEquals(expectedContent, s.toString());
    }
    
    @Test
    public void canCheckEqualityEmptyProperty()
    {
        Summary p1 = new Summary();
        Summary p2 = new Summary();
        assertEquals(p1, p2);
        assertEquals("SUMMARY:", p2.toString());
        assertEquals(p1.toString(), p2.toString());
    }
    
    @Test // property with non-standard parameters
    public void canHandleNonStandardParameters()
    {
        String content = "SUMMARY;X-PARAM1=VALUE1;X-PARAM2=VALUE2:Department Party";
        Summary madeProperty = Summary.parse(content);
        assertEquals(content, madeProperty.toString());
        Summary expectedProperty = new Summary()
                .withValue("Department Party")
                .withNonStandard("X-PARAM1=VALUE1", "X-PARAM2=VALUE2");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canIgnoreUnrecognizedParameter()
    {
        String content = "SUMMARY;IGNORE-THIS-PARAMETER=VALUE1:test value";
        Summary madeProperty = Summary.parse(content);
        assertEquals("SUMMARY:test value", madeProperty.toString());
    }
}
