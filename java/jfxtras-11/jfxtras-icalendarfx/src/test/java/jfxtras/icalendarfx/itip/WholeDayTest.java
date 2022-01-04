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
package jfxtras.icalendarfx.itip;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarStaticComponents;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;

public class WholeDayTest
{
    @Test
    public void canChangeToWholeDayAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(LocalDate.of(2015, 11, 8), myComponent.getDateTimeStart().getValue());        
        assertEquals(LocalDate.of(2015, 11, 9), myComponent.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponent.getSummary().getValue());
    }
    
    @Test
    public void canChangOneWholeDayToTimeBased()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20160516" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20160517" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20160914T180627Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160516T100000" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(2, vComponents.size());
        // TODO - CHECK COMPONENTS
    }
    
    @Test
    public void canChangeWholeDayToTimeBasedThisAndFuture()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getWholeDayDaily1();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20160914T200517Z" + System.lineSeparator() +
                "UID:20160914T130517-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "DTSTART:20160516T090000" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND:20160516T100000" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;UNTIL=20160515" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);


        assertEquals(2, vComponents.size());
        Collections.sort(vComponents, VPrimary.DTSTART_COMPARATOR);
        VEvent newVComponentOriginal = vComponents.get(0);
        VEvent newVComponentFuture = vComponents.get(1);

        VEvent expectedVComponentOriginal = ICalendarStaticComponents.getWholeDayDaily1();
        RecurrenceRuleValue rrule = expectedVComponentOriginal.getRecurrenceRule().getValue();
        rrule.setUntil(LocalDate.of(2016, 5, 15));
        
        assertEquals(expectedVComponentOriginal, newVComponentOriginal);

        VEvent expectedVComponentFuture = ICalendarStaticComponents.getWholeDayDaily1();
        expectedVComponentFuture.setDateTimeStart(LocalDateTime.of(2016, 5, 16, 9, 0));
        expectedVComponentFuture.setDateTimeEnd(LocalDateTime.of(2016, 5, 16, 10, 0));
        expectedVComponentFuture.setUniqueIdentifier(new UniqueIdentifier(newVComponentFuture.getUniqueIdentifier()));
        expectedVComponentFuture.setDateTimeStamp(new DateTimeStamp(newVComponentFuture.getDateTimeStamp()));
        expectedVComponentFuture.setSummary("Edited summary");
        expectedVComponentFuture.withRelatedTo(vComponentOriginal.getUniqueIdentifier().getValue());
        expectedVComponentFuture.setSequence(1);
        
        assertEquals(expectedVComponentFuture, newVComponentFuture);
    }
    
    @Test // with a recurrence in between new date range, from whole-day to time-based - special recurrence stays unmodified.
    public void canEditWholeDayToTimeBasedThisAndFutureIgnoreRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getWholeDayDaily1();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getWholeDayDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRuleValue) null);
        vComponentRecurrence.setRecurrenceId(LocalDate.of(2016, 5, 17));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 8, 30), ZoneId.of("Europe/London")));
        vComponentRecurrence.setDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 30), ZoneId.of("Europe/London")));
        mainVCalendar.addChild(vComponentRecurrence);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;UNTIL=20160514" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR" + System.lineSeparator() +
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20160914T200517Z" + System.lineSeparator() +
                "UID:20160914T130517-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "DTSTART;TZID=Europe/London:20160515T090000" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;TZID=Europe/London:20160515T103000" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20160914T200517Z" + System.lineSeparator() +
                "UID:20160914T130517-0jfxtras.org" + System.lineSeparator() +
                "DTSTART;TZID=Europe/London:20160517T083000" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;TZID=Europe/London:20160517T093000" + System.lineSeparator() +
                "RECURRENCE-ID;TZID=Europe/London:20160517T090000" + System.lineSeparator() +
                "SUMMARY:recurrence summary" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
        
        assertEquals(3, vComponents.size());
        Collections.sort(vComponents, VPrimary.DTSTART_COMPARATOR);
        VEvent myComponentOriginal = vComponents.get(0);
        VEvent newVComponentFuture = vComponents.get(1);
        VEvent myComponentRecurrence = vComponents.get(2);
        
        VEvent expectedOriginalEdited = ICalendarStaticComponents.getWholeDayDaily1()
                .withSequence(1);
        expectedOriginalEdited.getRecurrenceRule().getValue()
            .setUntil(LocalDate.of(2016, 5, 14));
        assertEquals(expectedOriginalEdited, myComponentOriginal);

        RelatedTo relatedTo = RelatedTo.parse(vComponentEdited.getUniqueIdentifier().getValue());
        VEvent expectedComponentFuture = ICalendarStaticComponents.getWholeDayDaily1()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 9, 0), ZoneId.of("Europe/London")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 10, 30), ZoneId.of("Europe/London")))
                .withSummary("Edited summary")
                .withRelatedTo(Arrays.asList(relatedTo))
                .withUniqueIdentifier(new UniqueIdentifier(newVComponentFuture.getUniqueIdentifier()))
                .withDateTimeStamp(new DateTimeStamp(newVComponentFuture.getDateTimeStamp()));

        VEvent expectedvComponentRecurrence = ICalendarStaticComponents.getWholeDayDaily1()
                .withRecurrenceRule((RecurrenceRule) null)
                .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 0), ZoneId.of("Europe/London")))
                .withSummary("recurrence summary")
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 8, 30), ZoneId.of("Europe/London")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 30), ZoneId.of("Europe/London")))
                .withUniqueIdentifier(new UniqueIdentifier(expectedComponentFuture.getUniqueIdentifier()))
                .withDateTimeStamp(new DateTimeStamp(expectedComponentFuture.getDateTimeStamp()));
        assertEquals(expectedvComponentRecurrence, myComponentRecurrence);
    }
}
