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
package jfxtras.scene.control.agenda.icalendar.agenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Parent;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Method.MethodType;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.test.TestUtil;

@Ignore // fails
public class RenderVEventsTest extends AgendaTestAbstract
{
    @Override
    public Parent getRootNode()
    {
        Parent p = super.getRootNode();
        return p;
    }
    
    @Test
    public void canRenderVComponents()
    {
        // Add VComponents, listener in ICalendarAgenda makes Appointments
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily2());
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getWeekly2());
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getWholeDayDaily3());
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getIndividual1());
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getIndividual2());
            agenda.updateAppointments();
        });

        List<LocalDateTime> startDates = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedStartDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 8, 0, 0)
              , LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 0, 0)
              , LocalDateTime.of(2015, 11, 11, 0, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 30)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 14, 0, 0)
                ));
        assertEquals(expectedStartDates, startDates);

        List<LocalDateTime> endDates = agenda.appointments()
                .stream()
                .map(a -> a.getEndLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedEndDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 11, 30)
              , LocalDateTime.of(2015, 11, 10, 0, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 45)
              , LocalDateTime.of(2015, 11, 11, 11, 30)
              , LocalDateTime.of(2015, 11, 12, 0, 0)
              , LocalDateTime.of(2015, 11, 12, 11, 30)
              , LocalDateTime.of(2015, 11, 13, 0, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 45)
              , LocalDateTime.of(2015, 11, 16, 0, 0)
                ));
        assertEquals(expectedEndDates, endDates);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        { // advance one week
            LocalDateTime date = agenda.getDisplayedLocalDateTime().plus(1, ChronoUnit.WEEKS);
            agenda.setDisplayedLocalDateTime(date);
        });
        List<LocalDateTime> startDates2 = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedStartDates2 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 14, 0, 0)
              , LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 0, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 0, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
                ));

        assertEquals(expectedStartDates2, startDates2);
        List<LocalDateTime> endDates2 = agenda.appointments()
                .stream()
                .map(a -> a.getEndLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedEndDates2 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 11, 30)
              , LocalDateTime.of(2015, 11, 16, 0, 0)
              , LocalDateTime.of(2015, 11, 18, 11, 30)
              , LocalDateTime.of(2015, 11, 19, 0, 0)
              , LocalDateTime.of(2015, 11, 21, 11, 30)
              , LocalDateTime.of(2015, 11, 22, 0, 0)
                ));
        assertEquals(expectedEndDates2, endDates2);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        { // advance one week
            LocalDateTime date = agenda.getDisplayedLocalDateTime().plus(1, ChronoUnit.WEEKS);
            agenda.setDisplayedLocalDateTime(date);
        });
        List<LocalDateTime> startDates3 = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedStartDates3 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 20, 0, 0),
                LocalDateTime.of(2015, 11, 23, 0, 0),
                LocalDateTime.of(2015, 11, 23, 10, 0),
                LocalDateTime.of(2015, 11, 24, 10, 0),
                LocalDateTime.of(2015, 11, 25, 10, 0),
                LocalDateTime.of(2015, 11, 27, 10, 0)
                ));
        assertEquals(expectedStartDates3, startDates3);
        List<LocalDateTime> endDates3 = agenda.appointments()
                .stream()
                .map(a -> a.getEndLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedEndDates3 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 22, 0, 0),
                LocalDateTime.of(2015, 11, 23, 10, 45),
                LocalDateTime.of(2015, 11, 24, 11, 30),
                LocalDateTime.of(2015, 11, 25, 0, 0),
                LocalDateTime.of(2015, 11, 25, 10, 45),
                LocalDateTime.of(2015, 11, 27, 10, 45)
                ));
        assertEquals(expectedEndDates3, endDates3);

        TestUtil.runThenWaitForPaintPulse( () ->
        { // advance ten years + one week
            LocalDateTime date = agenda.getDisplayedLocalDateTime().plus(10, ChronoUnit.YEARS).plus(1, ChronoUnit.WEEKS);
            agenda.setDisplayedLocalDateTime(date);
        });
        List<LocalDateTime> startDates4 = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedStartDates4 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 24, 10, 0)
              , LocalDateTime.of(2025, 11, 26, 10, 0)
              , LocalDateTime.of(2025, 11, 28, 10, 0)
                ));
        assertEquals(expectedStartDates4, startDates4);
        List<LocalDateTime> endDates4 = agenda.appointments()
                .stream()
                .map(a -> a.getEndLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedEndDates4 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 24, 10, 45)
              , LocalDateTime.of(2025, 11, 26, 10, 45)
              , LocalDateTime.of(2025, 11, 28, 10, 45)
                ));
        assertEquals(expectedEndDates4, endDates4);
    }

    @Test
//    @Ignore // TestFX
    public void canRenderVComponentZoned()
    {
        // Add VComponents, listener in ICalendarAgenda makes Appointments
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getIndividualZoned());
            agenda.updateAppointments();
        });
        
        List<Temporal> startZoneDates = agenda.appointments()
                .stream()
                .map(a -> a.getStartTemporal())
                .sorted()
                .collect(Collectors.toList());
        List<ZonedDateTime> expectedStartZoneDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Europe/London"))
                ));
        assertEquals(expectedStartZoneDates, startZoneDates);

        // Local dates must be converted to default time zone
        List<LocalDateTime> startDates = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedStartDates = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 2, 0)
                ));
        assertEquals(expectedStartDates, startDates);
    }
    
    @Test
    public void canRenderiTIPPublish()
    {
//        String publish = "BEGIN:VCALENDAR" + System.lineSeparator() + 
//                "METHOD:PUBLISH" + System.lineSeparator() + 
//                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
//                "VERSION:2.0" + System.lineSeparator() + 
//                "BEGIN:VEVENT" + System.lineSeparator() + 
//                "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
//                "DTSTART:20150701T200000Z" + System.lineSeparator() + 
//                "DTEND:20150701T220000Z" + System.lineSeparator() + 
//                "DTSTAMP:20150611T190000Z" + System.lineSeparator() + 
//                "RRULE:FREQ=WEEKLY;BYDAY=FR" + System.lineSeparator() +
//                "SUMMARY:Friday meeting with Joe" + System.lineSeparator() + 
//                "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
//                "END:VEVENT" + System.lineSeparator() + 
//                "END:VCALENDAR";
//        VCalendar publishMessage = VCalendar.parse(publish);
        VEvent vEvent = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Europe/London")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 11, 0), ZoneId.of("Europe/London")))
                .withDateTimeStamp(LocalDateTime.now().atZone(ZoneId.of("Z")))
                .withSummary("Example Daily Event")
                .withRecurrenceRule("RRULE:FREQ=DAILY")
                .withOrganizer("mailto:david@balsoftware.net")
                .withUniqueIdentifier("exampleuid000jfxtras.org");
        VCalendar publishMessage = new VCalendar()
                .withMethod(MethodType.PUBLISH)
                .withVEvents(vEvent);

        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().processITIPMessage(publishMessage);
            agenda.updateAppointments();
        });

        assertEquals(4, agenda.appointments().size());
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        List<Temporal> startDates = agenda.appointments()
                .stream()
                .map(a -> a.getStartTemporal())
                .sorted()
                .collect(Collectors.toList());
        List<Temporal> expectedStartDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Europe/London")),
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 12, 10, 0), ZoneId.of("Europe/London")),
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("Europe/London")),
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 14, 10, 0), ZoneId.of("Europe/London"))
                ));
        assertEquals(expectedStartDates, startDates);
    }
}
