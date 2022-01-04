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
package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.time.DateTimeException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VLocatable;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.GeographicPosition;
import jfxtras.icalendarfx.properties.component.descriptive.Location;
import jfxtras.icalendarfx.properties.component.descriptive.Priority;
import jfxtras.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.icalendarfx.properties.component.time.DurationProp;

/**
 * Test following components:
 * @see VEvent
 * @see VTodo
 * 
 * for the following properties:
 * @see Description
 * @see GeographicPosition
 * @see DurationProp
 * @see Location
 * @see Priority
 * @see Resources
 * 
 * @author David Bal
 *
 */
public class LocatableTest
{
    @Test
    @Ignore // TBEERNOT see what is going wrong here
    public void canBuildLocatable() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VLocatable<?>> components = Arrays.asList(
                new VEvent()
                    .withDescription("DESCRIPTION:A simple description")
                    .withDuration(Duration.ofMinutes(45))
                    .withGeographicPosition("37.386013;-122.082932")
                    .withLocation("Antarctica")
                    .withPriority(2)
                    .withResources(new Resources("Nettoyeur haute pression")
                            .withLanguage("fr"))
                    .withVAlarms(VAlarm.parse("BEGIN:VALARM" + System.lineSeparator() +
                                     "ACTION:DISPLAY" + System.lineSeparator() +
                                     "DESCRIPTION:Test alarm" + System.lineSeparator() +
                                     "TRIGGER;RELATED=START:-PT30M" + System.lineSeparator() +
                                     "END:VALARM"),
                                 VAlarm.parse("BEGIN:VALARM" + System.lineSeparator() +
                                     "TRIGGER;VALUE=DATE-TIME:19970317T133000Z" + System.lineSeparator() +
                                     "REPEAT:4" + System.lineSeparator() +
                                     "DURATION:PT15M" + System.lineSeparator() +
                                     "ACTION:AUDIO" + System.lineSeparator() +
                                     "ATTACH;FMTTYPE=audio/basic:ftp://example.com/pub/sounds/bell-01.aud" + System.lineSeparator() +
                                     "END:VALARM")),
                new VTodo()
                    .withDescription("DESCRIPTION:A simple description")
                    .withDuration(Duration.ofMinutes(45))
                    .withGeographicPosition("37.386013;-122.082932")
                    .withLocation("Antarctica")
                    .withPriority(2)
                    .withResources(new Resources("Nettoyeur haute pression")
                            .withLanguage("fr"))
                    .withVAlarms(VAlarm.parse("BEGIN:VALARM" + System.lineSeparator() +
                            "ACTION:DISPLAY" + System.lineSeparator() +
                            "DESCRIPTION:Test alarm" + System.lineSeparator() +
                            "TRIGGER;RELATED=START:-PT30M" + System.lineSeparator() +
                            "END:VALARM"),
                        VAlarm.parse("BEGIN:VALARM" + System.lineSeparator() +
                            "TRIGGER;VALUE=DATE-TIME:19970317T133000Z" + System.lineSeparator() +
                            "REPEAT:4" + System.lineSeparator() +
                            "DURATION:PT15M" + System.lineSeparator() +
                            "ACTION:AUDIO" + System.lineSeparator() +
                            "ATTACH;FMTTYPE=audio/basic:ftp://example.com/pub/sounds/bell-01.aud" + System.lineSeparator() +
                            "END:VALARM"))
                );
        
        for (VLocatable<?> builtComponent : components)
        {
            String componentName = builtComponent.name();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "DESCRIPTION:A simple description" + System.lineSeparator() +
                    "DURATION:PT45M" + System.lineSeparator() +
                    "GEO:37.386013;-122.082932" + System.lineSeparator() +
                    "LOCATION:Antarctica" + System.lineSeparator() +
                    "PRIORITY:2" + System.lineSeparator() +
                    "RESOURCES;LANGUAGE=fr:Nettoyeur haute pression" + System.lineSeparator() +
                    "BEGIN:VALARM" + System.lineSeparator() +
                    "ACTION:DISPLAY" + System.lineSeparator() +
                    "DESCRIPTION:Test alarm" + System.lineSeparator() +
                    "TRIGGER;RELATED=START:-PT30M" + System.lineSeparator() +
                    "END:VALARM" + System.lineSeparator() +
                    "BEGIN:VALARM" + System.lineSeparator() +
                    "TRIGGER;VALUE=DATE-TIME:19970317T133000Z" + System.lineSeparator() +
                    "REPEAT:4" + System.lineSeparator() +
                    "DURATION:PT15M" + System.lineSeparator() +
                    "ACTION:AUDIO" + System.lineSeparator() +
                    "ATTACH;FMTTYPE=audio/basic:ftp://example.com/pub/sounds/bell-01.aud" + System.lineSeparator() +
                    "END:VALARM" + System.lineSeparator() +
                    "END:" + componentName;

            VComponent parsedComponent = (VComponent) builtComponent.getClass().getMethod("parse", String.class).invoke(null, expectedContent);
//            parsedComponent.addChild(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toString());            
        }
    }
    
    @Test (expected = DateTimeException.class)
    public void canCatchNegativeDuration()
    {
        VEvent vEvent = new VEvent()
                .withDuration(Duration.ofHours(-1))
                .withSummary("test");
        assertNull(vEvent.getDuration());
        assertEquals("test", vEvent.getSummary().getValue());
    }
}
