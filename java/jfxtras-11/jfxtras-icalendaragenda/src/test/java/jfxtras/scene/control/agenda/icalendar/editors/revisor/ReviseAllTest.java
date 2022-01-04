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
package jfxtras.scene.control.agenda.icalendar.editors.revisor;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.ReviserVEvent;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.SimpleRevisorFactory;

public class ReviseAllTest
{
    @Test
    public void canEditAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        mainVCalendar.addChild(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        List<VCalendar> iTIPMessages = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal)
                .revise();
        
        String expectediTIPMessage =
            "BEGIN:VCALENDAR" + System.lineSeparator() +
            "METHOD:REQUEST" + System.lineSeparator() +
            "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
            "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
            "BEGIN:VEVENT" + System.lineSeparator() +
            "CATEGORIES:group05" + System.lineSeparator() +
            "DTSTART:20151109T090000" + System.lineSeparator() +
            "DTEND:20151109T103000" + System.lineSeparator() +
            "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
            "SUMMARY:Edited summary" + System.lineSeparator() +
            vComponentEdited.getDateTimeStamp().toString() + System.lineSeparator() +
            "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
            "RRULE:FREQ=DAILY" + System.lineSeparator() +
            "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
            "SEQUENCE:1" + System.lineSeparator() +
            "END:VEVENT" + System.lineSeparator() +
            "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canShiftWeeklyAll() // shift day of weekly
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getWeekly3();
        mainVCalendar.addChild(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 17, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 17, 10, 30);

        List<VCalendar> iTIPMessages = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal)
                .revise();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20151110T090000" + System.lineSeparator() +
                "DTEND:20151110T103000" + System.lineSeparator() +
                "UID:20150110T080000-002@jfxtras.org" + System.lineSeparator() +
                vComponentEdited.getDateTimeStamp().toString() + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "RRULE:FREQ=WEEKLY;BYDAY=TU" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canShiftMonthlyAll2() // shift day of weekly with ordinal
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getMonthly7();
        mainVCalendar.addChild(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 17, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 17, 10, 30);

        List<VCalendar> iTIPMessages = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal)
                .revise();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20151117T090000" + System.lineSeparator() +
                "DTEND:20151117T103000" + System.lineSeparator() +
                "UID:20150110T080000-002@jfxtras.org" + System.lineSeparator() +
                vComponentEdited.getDateTimeStamp().toString() + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "RRULE:FREQ=MONTHLY;BYDAY=3TU" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canAddRRuleToAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividualZoned();
        mainVCalendar.addChild(vComponentOriginal);
        final List<VEvent> vComponents = mainVCalendar.getVEvents();
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        vComponents.add(vComponentEdited);
        
        vComponentEdited.setSummary("Edited summary");
        vComponentEdited.setRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));

        Temporal startOriginalRecurrence = ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Europe/London"));
        Temporal startRecurrence = ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 9, 0), ZoneId.of("Europe/London"));
        Temporal endRecurrence = ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("Europe/London"));

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "CATEGORIES:group13" + System.lineSeparator() +
                "DTSTART;TZID=Europe/London:20151113T090000" + System.lineSeparator() +
                "DTEND;TZID=Europe/London:20151113T100000" + System.lineSeparator() +
                vComponentEdited.getDateTimeStamp().toString() + System.lineSeparator() +
                "UID:20150110T080000-009@jfxtras.org" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test // edit ALL with 2 recurrences in date range
    public void canEditAllWithRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        mainVCalendar.addChild(vComponentOriginal);
        final List<VEvent> vComponents = mainVCalendar.getVEvents();
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        // make recurrence instances
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRuleValue) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);
        
        VEvent vComponentRecurrence2 = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRuleValue) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 19, 10, 0))
                .withSummary("recurrence summary2")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 19, 7, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 19, 8, 30));
        vComponents.add(vComponentRecurrence2);

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> itipMessages = reviser.revise();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T090000" + System.lineSeparator() +
                "DTEND:20151109T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                vComponentEdited.getDateTimeStamp().toString() + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                // Below CANCEL method shouldn't be needed - orphaned children should be automatically removed.
//                "END:VCALENDAR" + System.lineSeparator() +
//                "BEGIN:VCALENDAR" + System.lineSeparator() +
//                "METHOD:CANCEL" + System.lineSeparator() +
//                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
//                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
//                "BEGIN:VEVENT" + System.lineSeparator() +
//                "CATEGORIES:group05" + System.lineSeparator() +
//                "DTSTART:20160517T083000" + System.lineSeparator() +
//                "DTEND:20160517T093000" + System.lineSeparator() +
//                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
//                "SUMMARY:recurrence summary" + System.lineSeparator() +
//                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
//                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
//                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
//                "RECURRENCE-ID:20160517T100000" + System.lineSeparator() +
//                "END:VEVENT" + System.lineSeparator() +
//                "BEGIN:VEVENT" + System.lineSeparator() +
//                "CATEGORIES:group05" + System.lineSeparator() +
//                "DTSTART:20160519T073000" + System.lineSeparator() +
//                "DTEND:20160519T083000" + System.lineSeparator() +
//                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
//                "SUMMARY:recurrence summary2" + System.lineSeparator() +
//                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
//                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
//                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
//                "RECURRENCE-ID:20160519T100000" + System.lineSeparator() +
//                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = itipMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test // edit ALL with a recurrence in date range
    public void canEditAllIgnoreRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        mainVCalendar.addChild(vComponentOriginal);
        final List<VEvent> vComponents = mainVCalendar.getVEvents();
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        
        // make recurrence instances
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRuleValue) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);
        
        VEvent vComponentRecurrence2 = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRuleValue) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 19, 10, 0))
                .withSummary("recurrence summary2")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 19, 7, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 19, 8, 30));
        vComponents.add(vComponentRecurrence2);

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL_IGNORE_RECURRENCES)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> itipMessages = reviser.revise();

        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T090000" + System.lineSeparator() +
                "DTEND:20151109T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                vComponentEdited.getDateTimeStamp().toString() + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR" + System.lineSeparator() +

                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +                
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160517T083000" + System.lineSeparator() +
                "DTEND:20160517T093000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160517T090000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160519T073000" + System.lineSeparator() +
                "DTEND:20160519T083000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary2" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160519T090000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = itipMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canChangeToWholeDayAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        mainVCalendar.addChild(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        
        // make changes
        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDate.of(2016, 5, 15); // shifts back 1 day
        Temporal endRecurrence = LocalDate.of(2016, 5, 16);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> itipMessages = reviser.revise();

        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                vComponentEdited.getDateTimeStamp().toString() + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR"
                ;
        String iTIPMessage = itipMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canRemoveException2()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDailyWithException1();
        mainVCalendar.addChild(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        // make changes
        vComponentEdited.setExceptionDates(null);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> null) // not needed
                .withEndRecurrence(LocalDateTime.of(2015, 11, 18, 11, 30))
                .withStartOriginalRecurrence(LocalDateTime.of(2015, 11, 18, 10, 0))
                .withStartRecurrence(LocalDateTime.of(2015, 11, 18, 10, 0))
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> itipMessages = reviser.revise();

        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group03" + System.lineSeparator() +
                "DTSTART:20151109T100000" + System.lineSeparator() +
                "DTEND:20151109T113000" + System.lineSeparator() +
                "DESCRIPTION:Daily2 Description" + System.lineSeparator() +
                "SUMMARY:Daily2 Summary" + System.lineSeparator() +
                vComponentEdited.getDateTimeStamp().toString() + System.lineSeparator() +
                "UID:20150110T080000-005@jfxtras.org" + System.lineSeparator() +
                "RRULE:COUNT=6;FREQ=DAILY;INTERVAL=3" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR"
                ;
        String iTIPMessage = itipMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
//    @Test
//    public void canChangeUntilToForever()
//    {
//        VCalendar vCalendar = new VCalendar();
//        final List<VEvent> vComponents = vCalendar.getVEvents();
//        
//        ZonedDateTime until = ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 10, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
//        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1()
//                .withRecurrenceRule(new RecurrenceRule2()
//                        .withFrequency(FrequencyType.DAILY)
//                        .withUntil(until));
//        vComponents.add(vComponentOriginal);
//        VEvent vComponentEdited = new VEvent(vComponentOriginal);
//        
//        // make changes
//        vComponentEdited.setSummary("Edited summary");
//        vComponentEdited.setRecurrenceRule("FREQ=DAILY");
//        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
//        Temporal startRecurrence = LocalDate.of(2016, 5, 15); // shifts back 1 day
//        Temporal endRecurrence = LocalDate.of(2016, 5, 16);
//
//        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
//                .withDialogCallback((m) -> ChangeDialogOption.ALL)
//                .withEndRecurrence(endRecurrence)
//                .withStartOriginalRecurrence(startOriginalRecurrence)
//                .withStartRecurrence(startRecurrence)
//                .withVComponentEdited(vComponentEdited)
//                .withVComponentOriginal(vComponentOriginal);
//        List<VCalendar> itipMessages = reviser.revise();
//
//        String expectediTIPMessage =
//                "BEGIN:VCALENDAR" + System.lineSeparator() +
//                "METHOD:REQUEST" + System.lineSeparator() +
//                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
//                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
//                "BEGIN:VEVENT" + System.lineSeparator() +
//                "CATEGORIES:group05" + System.lineSeparator() +
//                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
//                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
//                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
//                "SUMMARY:Edited summary" + System.lineSeparator() +
//                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
//                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
//                "RRULE:FREQ=DAILY" + System.lineSeparator() +
//                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
//                "SEQUENCE:1" + System.lineSeparator() +
//                "END:VEVENT" + System.lineSeparator() +
//                "END:VCALENDAR"
//                ;
//        String iTIPMessage = itipMessages.stream()
//                .map(v -> v.toString())
//                .collect(Collectors.joining(System.lineSeparator()));
//        assertEquals(expectediTIPMessage, iTIPMessage);
//    }
}
