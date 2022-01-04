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
package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Frequency;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Until;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.CategorySelectionGridPane;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.agenda.AgendaTestAbstract;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.test.TestUtil;

@Ignore // fails with NPE
public class PopupReviseAllTest extends VEventPopupTestBase
{
    @Test
    public void canEditSimpleVEvent()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 9, 10, 0), // start selected instance
                    LocalDateTime.of(2015, 11, 9, 11, 0), // end selected instance
                    AgendaTestAbstract.CATEGORIES);
        });

        // Make changes
        TextField summaryTextField = find("#summaryTextField");
        summaryTextField.setText("new summary");
        
        // Save changes
        clickOn("#saveComponentButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        clickOn("#changeDialogOkButton");
        
        String dtStamp = getEditComponentPopup().iTIPMessagesProperty().get().get(0).getVEvents().get(0).getDateTimeStamp().toString();
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T100000" + System.lineSeparator() +
                "DTEND:20151109T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:new summary" + System.lineSeparator() +
                dtStamp + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canEditDescribableProperties()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),
                    LocalDateTime.of(2016, 5, 15, 11, 0),
                    categories());
        });

        // Get properties
        LocalDateTimeTextField startDateTimeTextField = find("#startDateTimeTextField");
        LocalDateTimeTextField endDateTimeTextField = find("#endDateTimeTextField");
        CheckBox wholeDayCheckBox = find("#wholeDayCheckBox");
        TextField summaryTextField = find("#summaryTextField");
        TextArea descriptionTextArea = find("#descriptionTextArea");
        TextField locationTextField = find("#locationTextField");
        TextField categoryTextField = find("#categoryTextField");
        CategorySelectionGridPane categorySelectionGridPane = find("#categorySelectionGridPane");
        
        // Check initial state
        assertEquals(LocalDateTime.of(2016, 5, 15, 10, 00), startDateTimeTextField.getLocalDateTime());
        assertEquals(LocalDateTime.of(2016, 5, 15, 11, 00), endDateTimeTextField.getLocalDateTime());
        assertTrue(startDateTimeTextField.isVisible());
        assertTrue(endDateTimeTextField.isVisible());
        assertEquals("Daily1 Summary", summaryTextField.getText());
        assertEquals("Daily1 Description", descriptionTextArea.getText());
        assertEquals("group05", categoryTextField.getText());
        assertFalse(wholeDayCheckBox.isSelected());

        // Edit and check properties
        TestUtil.runThenWaitForPaintPulse( () -> wholeDayCheckBox.setSelected(true) );
        LocalDateTextField startDateTextField = find("#startDateTextField");
        LocalDateTextField endDateTextField = find("#endDateTextField");
        assertEquals(LocalDate.of(2016, 5, 15), startDateTextField.getLocalDate());
        assertEquals(LocalDate.of(2016, 5, 16), endDateTextField.getLocalDate());
        assertTrue(startDateTextField.isVisible());
        assertTrue(endDateTextField.isVisible());
        TestUtil.runThenWaitForPaintPulse( () -> wholeDayCheckBox.setSelected(false) );
        assertTrue(startDateTimeTextField.isVisible());
        assertTrue(endDateTimeTextField.isVisible());

        // Make changes
        startDateTimeTextField.setLocalDateTime(LocalDateTime.of(2016, 5, 15, 8, 0));
        summaryTextField.setText("new summary");
        descriptionTextArea.setText("new description");
        locationTextField.setText("new location");
        TestUtil.runThenWaitForPaintPulse(() -> categorySelectionGridPane.setCategorySelected(11));
        assertEquals("group11", categoryTextField.getText());
        categoryTextField.setText("new group name");

        // save to all
        clickOn("#saveComponentButton");
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            comboBox.getSelectionModel().select(ChangeDialogOption.ALL);
        });
        clickOn("#changeDialogOkButton");
        
        // test iTIP message
        List<VCalendar> vMessages = getEditComponentPopup().iTIPMessagesProperty().get();
        assertEquals(1, vMessages.size());
        VCalendar vMessage = vMessages.get(0);
        assertEquals(1, vMessage.getVEvents().size());
        VEvent revisedVEvent = vMessage.getVEvents().get(0);
        
        VEvent expectedRevisedVEvent = ICalendarStaticComponents.getDaily1();
        expectedRevisedVEvent.setCategories(null);
        expectedRevisedVEvent
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 8, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 9, 0))
                .withSummary("new summary")
                .withDescription("new description")
                .withLocation("new location")
                .withCategories(Arrays.asList(new Categories("new group name")))
                .withDateTimeStamp(revisedVEvent.getDateTimeStamp())
                .withSequence(1);
        assertEquals(expectedRevisedVEvent, revisedVEvent);
        
        String iTIPMessage = vMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));

        String dtStamp = getEditComponentPopup().iTIPMessagesProperty().get().get(0).getVEvents().get(0).getDateTimeStamp().toString();
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:new group name" + System.lineSeparator() +
                "DTSTART:20151109T080000" + System.lineSeparator() +
                "DTEND:20151109T090000" + System.lineSeparator() +
                "DESCRIPTION:new description" + System.lineSeparator() +
                "SUMMARY:new summary" + System.lineSeparator() +
                dtStamp + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "LOCATION:new location" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canChangeFrequencyWeekly()
    {       
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),
                    LocalDateTime.of(2016, 5, 15, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });

        // Get properties
        clickOn("#recurrenceRuleTab");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");

        // Check initial state
        assertEquals(FrequencyType.DAILY, frequencyComboBox.getSelectionModel().getSelectedItem());
        
        // Change property and verify state change
        // WEEKLY
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.WEEKLY));
        // Days of the week properties
        CheckBox su = (CheckBox) find("#sundayCheckBox");
        CheckBox mo = (CheckBox) find("#mondayCheckBox");
        CheckBox tu = (CheckBox) find("#tuesdayCheckBox");
        CheckBox we = (CheckBox) find("#wednesdayCheckBox");
        CheckBox th = (CheckBox) find("#thursdayCheckBox");
        CheckBox fr = (CheckBox) find("#fridayCheckBox");
        CheckBox sa = (CheckBox) find("#saturdayCheckBox");

        // Check initial state
        HBox weeklyHBox = find("#weeklyHBox");
        assertTrue(weeklyHBox.isVisible());       
        assertTrue(su.isSelected());
        assertFalse(mo.isSelected());
        assertFalse(tu.isSelected());
        assertFalse(we.isSelected());
        assertFalse(th.isSelected());
        assertFalse(fr.isSelected());
        assertFalse(sa.isSelected());
        
        // Toggle each day of week and check
        // Sunday already selected
        TestUtil.runThenWaitForPaintPulse( () -> mo.setSelected(true));
        assertTrue(mo.isSelected());
        TestUtil.runThenWaitForPaintPulse( () -> tu.setSelected(true));
        assertTrue(tu.isSelected());
        TestUtil.runThenWaitForPaintPulse( () -> we.setSelected(true));
        assertTrue(we.isSelected());
        TestUtil.runThenWaitForPaintPulse( () -> th.setSelected(true));
        assertTrue(th.isSelected());
        TestUtil.runThenWaitForPaintPulse( () -> fr.setSelected(true));
        assertTrue(fr.isSelected());
        TestUtil.runThenWaitForPaintPulse( () -> sa.setSelected(true));
        assertTrue(sa.isSelected());

        // save changes to all
        clickOn("#saveRepeatButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        clickOn("#changeDialogOkButton");

        // test iTIP message
        List<VCalendar> messages = getEditComponentPopup().iTIPMessagesProperty().get();
        assertEquals(1, messages.size());
        VCalendar message = messages.get(0);
        assertEquals(1, message.getVEvents().size());
        VEvent revisedVEvent = message.getVEvents().get(0);
        RecurrenceRuleValue rrule = revisedVEvent.getRecurrenceRule().getValue();
        
        // check frequency
        Frequency f = rrule.getFrequency();
        assertEquals(FrequencyType.WEEKLY, f.getValue());
        
        // check ByDay
        assertEquals(1, rrule.getByRules().size());
        ByDay rule = (ByDay) rrule.lookupByRule(ByDay.class);
        
        List<DayOfWeek> allDaysOfWeek = Arrays.asList(DayOfWeek.values());
        List<DayOfWeek> daysOfWeek = rule.dayOfWeekWithoutOrdinalList();
        Collections.sort(daysOfWeek);
        assertEquals(allDaysOfWeek, daysOfWeek);
        
        VEvent expectedRevisedVEvent = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule("FREQ=WEEKLY;BYDAY=SU,MO,TU,WE,TH,FR,SA")
                .withDateTimeStamp(revisedVEvent.getDateTimeStamp())
                .withSequence(1);
        assertEquals(expectedRevisedVEvent, revisedVEvent);
    }
    
    @Test
    public void canChangeFrequencyMonthly()
    {       
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),
                    LocalDateTime.of(2016, 5, 15, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });

        // Get properties
        clickOn("#recurrenceRuleTab");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");
            
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.MONTHLY));
        
        // Monthly properties
        RadioButton dayOfMonth = find("#dayOfMonthRadioButton");
        RadioButton dayOfWeek = find("#dayOfWeekRadioButton");
        
        // Check initial state
        assertTrue(dayOfMonth.isSelected());
        assertFalse(dayOfWeek.isSelected());
        
        // Toggle monthly options and check
        TestUtil.runThenWaitForPaintPulse(() -> dayOfWeek.setSelected(true));
        assertFalse(dayOfMonth.isSelected());
        assertTrue(dayOfWeek.isSelected());
        
        clickOn("#saveRepeatButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        clickOn("#changeDialogOkButton");

        // test iTIP message
        List<VCalendar> messages = getEditComponentPopup().iTIPMessagesProperty().get();
        assertEquals(1, messages.size());
        VCalendar message = messages.get(0);
        assertEquals(1, message.getVEvents().size());
        VEvent revisedVEvent = message.getVEvents().get(0);
        
        VEvent expectedRevisedVEvent = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart("20151115T100000")
                .withDateTimeEnd("20151115T110000")
                .withRecurrenceRule("FREQ=MONTHLY;BYDAY=3SU")
                .withDateTimeStamp(revisedVEvent.getDateTimeStamp())
                .withSequence(1);
        assertEquals(expectedRevisedVEvent, revisedVEvent);
    }
    
    @Test
    public void canChangeFrequencyYearly()
    {       
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),
                    LocalDateTime.of(2016, 5, 15, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });

        // Get properties
        clickOn("#recurrenceRuleTab");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");
            
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.YEARLY));
        
        clickOn("#saveRepeatButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        clickOn("#changeDialogOkButton");

        // test iTIP message
        List<VCalendar> messages = getEditComponentPopup().iTIPMessagesProperty().get();
        assertEquals(1, messages.size());
        VCalendar message = messages.get(0);
        assertEquals(1, message.getVEvents().size());
        VEvent revisedVEvent = message.getVEvents().get(0);
        
        VEvent expectedRevisedVEvent = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule("FREQ=YEARLY")
                .withDateTimeStamp(revisedVEvent.getDateTimeStamp())
                .withSequence(1);
        assertEquals(expectedRevisedVEvent, revisedVEvent);
    }
    
    @Test
//    @Ignore // TestFX4
    public void canChangeWholeDayToTimeBased()
    {
        VEvent vevent = ICalendarStaticComponents.getWholeDayDaily3();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDate.of(2015, 11, 11), // start selected instance
                    LocalDate.of(2015, 11, 13), // end selected instance
                    AgendaTestAbstract.CATEGORIES);
        });
        CheckBox wholeDayCheckBox = find("#wholeDayCheckBox");
        assertTrue(wholeDayCheckBox.isSelected());
        LocalDateTextField start1 = find("#startDateTextField");
        assertEquals(LocalDate.of(2015, 11, 11), start1.getLocalDate());
        clickOn(wholeDayCheckBox); // turn off wholeDayCheckBox

        LocalDateTimeTextField start2 = find("#startDateTimeTextField");
        assertEquals(LocalDateTime.of(2015, 11, 11, 10, 0), start2.getLocalDateTime());
        start2.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 13, 0)); // adds 3 hour shift
        LocalDateTimeTextField end2 = find("#endDateTimeTextField");
        assertEquals(LocalDateTime.of(2015, 11, 12, 14, 0), end2.getLocalDateTime());
        end2.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 14, 0)); // make 1 hour long
        
        // Save changes
        clickOn("#saveComponentButton");        
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        clickOn("#changeDialogOkButton");
        
        String dtStamp = getEditComponentPopup().iTIPMessagesProperty().get().get(0).getVEvents().get(0).getDateTimeStamp().toString();
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                dtStamp + System.lineSeparator() +
                "UID:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "RRULE:UNTIL=20151123T210000Z;FREQ=DAILY;INTERVAL=3" + System.lineSeparator() +
                "DTSTART;TZID=America/Los_Angeles:20151108T130000" + System.lineSeparator() +
                "DTEND;TZID=America/Los_Angeles:20151108T140000" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canChangeToWholeDayAll()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1()
                .withLocation("Here");
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0), // start selected instance
                    LocalDateTime.of(2016, 5, 15, 11, 0), // end selected instance
                    AgendaTestAbstract.CATEGORIES);
        });
        CheckBox wholeDayCheckBox = find("#wholeDayCheckBox");
        clickOn(wholeDayCheckBox);

        LocalDateTextField start = find("#startDateTextField");
        assertEquals(LocalDate.of(2016, 5, 15), start.getLocalDate());
        start.setLocalDate(LocalDate.of(2016, 5, 16)); // adds 1 day shift
        
        // Save changes
        clickOn("#saveComponentButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        clickOn("#changeDialogOkButton");
        
        String dtStamp = getEditComponentPopup().iTIPMessagesProperty().get().get(0).getVEvents().get(0).getDateTimeStamp().toString();
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151110" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151111" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                dtStamp + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "LOCATION:Here" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test // testing event that has LocalDate start on a different date that the zoned "Z" date challenges the popup UNTIL checker
    public void canChangeUntilToForever()
    {
        Until until = new Until(ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 23, 30), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z")));
        VEvent vevent = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 7, 23, 30))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 8, 0, 30))
                .withDateTimeStamp("20150110T080000Z")
                .withSummary("Example Daily Event")
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY)
                        .withUntil(until))
                .withOrganizer("mailto:david@balsoftware.net")
                .withUniqueIdentifier("exampleuid000jfxtras.org");

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 10, 23, 30), // start selected instance
                    LocalDateTime.of(2015, 11, 11, 0, 30), // end selected instance
                    AgendaTestAbstract.CATEGORIES);
        });
        // make changes
        clickOn("#recurrenceRuleTab");
        clickOn("#endNeverRadioButton");
        
        // save changes
        clickOn("#saveRepeatButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        clickOn("#changeDialogOkButton");

        String dtStamp = getEditComponentPopup().iTIPMessagesProperty().get().get(0).getVEvents().get(0).getDateTimeStamp().toString();
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20151107T233000" + System.lineSeparator() +
                "DTEND:20151108T003000" + System.lineSeparator() +
                dtStamp + System.lineSeparator() +
                "SUMMARY:Example Daily Event" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER:mailto:david@balsoftware.net" + System.lineSeparator() +
                "UID:exampleuid000jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canChangeCountToForever()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY)
                        .withCount(10));
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 10, 10, 0), // start selected instance
                    LocalDateTime.of(2015, 11, 10, 11, 0), // end selected instance
                    AgendaTestAbstract.CATEGORIES);
        });
        // make changes
        clickOn("#recurrenceRuleTab");
        clickOn("#endNeverRadioButton");
        
        // save changes
        clickOn("#saveRepeatButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        clickOn("#changeDialogOkButton");

        String dtStamp = getEditComponentPopup().iTIPMessagesProperty().get().get(0).getVEvents().get(0).getDateTimeStamp().toString();
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T100000" + System.lineSeparator() +
                "DTEND:20151109T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                dtStamp + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
