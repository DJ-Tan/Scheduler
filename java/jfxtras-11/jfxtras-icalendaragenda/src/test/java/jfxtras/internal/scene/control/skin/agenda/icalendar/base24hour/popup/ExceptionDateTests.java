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
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.calendar.Method.MethodType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.agenda.AgendaTestAbstract;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.test.TestUtil;

/**
 * Tests the edit controls ability to handle EXDATE recurrence exceptions
 * 
 * @author David Bal
 *
 */
@Ignore // fails
public class ExceptionDateTests extends VEventPopupTestBase
{
    @Test
    public void canMakeExceptionList()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 10, 10, 0),
                    LocalDateTime.of(2015, 11, 10, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });
        
        // Get properties
        clickOn("#recurrenceRuleTab");
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");

        // Check initial state
        List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
        LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
        List<LocalDateTime> expectedDates = Stream
                .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                .limit(EditRecurrenceRuleVBox.EXCEPTION_CHOICE_LIMIT)
                .collect(Collectors.toList());
        assertEquals(expectedDates, exceptions);
        clickOn("#cancelRepeatButton");
    }
    
    @Test
    public void canMakeExceptionListWholeDay() // Whole day appointments
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 10, 10, 0),
                    LocalDateTime.of(2015, 11, 10, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });
        
        // Make whole day
        clickOn("#wholeDayCheckBox");
        
        // check whole day fields
        LocalDateTextField startDateTextField = find("#startDateTextField");
        LocalDateTextField endDateTextField = find("#endDateTextField");
        assertEquals(LocalDate.of(2015, 11, 10), startDateTextField.getLocalDate());
        assertEquals(LocalDate.of(2015, 11, 11), endDateTextField.getLocalDate());

        // go back to time based
        clickOn("#wholeDayCheckBox");
        LocalDateTimeTextField startDateTimeTextField = find("#startDateTimeTextField");
        LocalDateTimeTextField endDateTimeTextField = find("#endDateTimeTextField");

        assertEquals(LocalDateTime.of(2015, 11, 10, 10, 0), startDateTimeTextField.getLocalDateTime());
        assertEquals(LocalDateTime.of(2015, 11, 10, 11, 0), endDateTimeTextField.getLocalDateTime());

        // Make whole day again
        clickOn("#wholeDayCheckBox");
        
        // Go to repeatable tab
        clickOn("#recurrenceRuleTab");
        
        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");

        // Check initial state
        List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
        LocalDate seed = LocalDate.of(2015, 11, 9);
        List<LocalDate> expectedDates = Stream
                .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                .limit(EditRecurrenceRuleVBox.EXCEPTION_CHOICE_LIMIT)
                .collect(Collectors.toList());
        assertEquals(expectedDates, exceptions);
        clickOn("#cancelRepeatButton");
    }
    
    @Test
    public void canMakeExceptionListWeekly()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 10, 10, 0),
                    LocalDateTime.of(2015, 11, 10, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });
        
        // Go to repeatable tab
        clickOn("#recurrenceRuleTab");
        
        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");

        // Change property and verify state change
        // Frequency - Weekly
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.WEEKLY));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 17, 10, 0)
                  , LocalDateTime.of(2015, 11, 24, 10, 0)
                  , LocalDateTime.of(2015, 12, 1, 10, 0)
                  , LocalDateTime.of(2015, 12, 8, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
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
        assertFalse(su.isSelected());
        assertFalse(mo.isSelected());
        assertTrue(tu.isSelected());
        assertFalse(we.isSelected());
        assertFalse(th.isSelected());
        assertFalse(fr.isSelected());
        assertFalse(sa.isSelected());
        
        // Toggle each day of week and check
        TestUtil.runThenWaitForPaintPulse( () -> su.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                   LocalDateTime.of(2015, 11, 10, 10, 0)
                 , LocalDateTime.of(2015, 11, 15, 10, 0)
                 , LocalDateTime.of(2015, 11, 17, 10, 0)
                 , LocalDateTime.of(2015, 11, 22, 10, 0)
                 , LocalDateTime.of(2015, 11, 24, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }

        TestUtil.runThenWaitForPaintPulse( () -> mo.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                  , LocalDateTime.of(2015, 11, 16, 10, 0)
                  , LocalDateTime.of(2015, 11, 17, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }

        TestUtil.runThenWaitForPaintPulse( () -> tu.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                  , LocalDateTime.of(2015, 11, 16, 10, 0)
                  , LocalDateTime.of(2015, 11, 17, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        TestUtil.runThenWaitForPaintPulse( () -> we.setSelected(false)); // turn Wednesday off (initially on)
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                  , LocalDateTime.of(2015, 11, 16, 10, 0)
                  , LocalDateTime.of(2015, 11, 17, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        TestUtil.runThenWaitForPaintPulse( () -> th.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                  , LocalDateTime.of(2015, 11, 16, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        TestUtil.runThenWaitForPaintPulse( () -> fr.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        TestUtil.runThenWaitForPaintPulse( () -> sa.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                  , LocalDateTime.of(2015, 11, 14, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        // save changes to all
        clickOn("#saveRepeatButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        clickOn("#changeDialogOkButton");
        
        List<VCalendar> messages = getEditComponentPopup().iTIPMessagesProperty().get();
        assertEquals(1, messages.size());
        VCalendar message = messages.get(0);
        assertEquals(1, message.getVEvents().size());
        VEvent newVEvent = message.getVEvents().get(0);
        RecurrenceRuleValue rrule = newVEvent.getRecurrenceRule().getValue();
        ByDay rule = (ByDay) rrule.lookupByRule(ByDay.class);
        
        List<DayOfWeek> expectedDaysOfWeek = Arrays.asList(
                DayOfWeek.MONDAY
              , DayOfWeek.TUESDAY
              , DayOfWeek.THURSDAY
              , DayOfWeek.FRIDAY
              , DayOfWeek.SATURDAY
              , DayOfWeek.SUNDAY
              );
        List<DayOfWeek> daysOfWeek = rule.dayOfWeekWithoutOrdinalList();
        Collections.sort(daysOfWeek);
        assertEquals(expectedDaysOfWeek, daysOfWeek);
    }
    
    @Test
    public void canMakeExceptionListMonthly()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 10, 10, 0),
                    LocalDateTime.of(2015, 11, 10, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });
        clickOn("#recurrenceRuleTab");
        
        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");
        
        // Change property and verify state change
        // Frequency - Monthly
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.MONTHLY));
        RadioButton dayOfMonthRadioButton = find("#dayOfMonthRadioButton");
        RadioButton dayOfWeekRadioButton = find("#dayOfWeekRadioButton");
        
        // Check initial state
        assertTrue(dayOfMonthRadioButton.isSelected());
        assertFalse(dayOfWeekRadioButton.isSelected());
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 12, 9, 10, 0)
                  , LocalDateTime.of(2016, 1, 9, 10, 0)
                  , LocalDateTime.of(2016, 2, 9, 10, 0)
                  , LocalDateTime.of(2016, 3, 9, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }

        // check dayOfWeekRadioButton and check state
        TestUtil.runThenWaitForPaintPulse(() -> dayOfWeekRadioButton.setSelected(true));
        assertFalse(dayOfMonthRadioButton.isSelected());
        assertTrue(dayOfWeekRadioButton.isSelected());
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 12, 8, 10, 0)
                  , LocalDateTime.of(2016, 1, 12, 10, 0)
                  , LocalDateTime.of(2016, 2, 9, 10, 0)
                  , LocalDateTime.of(2016, 3, 8, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        clickOn("#cancelRepeatButton");
    }
    
    @Test
    public void canMakeExceptionListYearly()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 10, 10, 0),
                    LocalDateTime.of(2015, 11, 10, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });
        clickOn("#recurrenceRuleTab");
        
        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");
        
        // Change property and verify state change
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.YEARLY));
        
        // Check initial state
        List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2016, 11, 9, 10, 0)
              , LocalDateTime.of(2017, 11, 9, 10, 0)
              , LocalDateTime.of(2018, 11, 9, 10, 0)
              , LocalDateTime.of(2019, 11, 9, 10, 0)
                ));
        assertEquals(expectedDates, exceptions);
        clickOn("#cancelRepeatButton");
    }
    
    @Test
    public void canMakeExceptionListEndsCriteria()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 10, 10, 0),
                    LocalDateTime.of(2015, 11, 10, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });
        clickOn("#recurrenceRuleTab");

        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        RadioButton endNeverRadioButton = find("#endNeverRadioButton");
        RadioButton endAfterRadioButton = find("#endAfterRadioButton");
        Spinner<Integer> endAfterEventsSpinner = find("#endAfterEventsSpinner");
        RadioButton untilRadioButton = find("#untilRadioButton");
        DatePicker untilDatePicker = find("#untilDatePicker");

        // Change property and verify state change
        // Ends After (COUNT)
        TestUtil.runThenWaitForPaintPulse( () -> endAfterRadioButton.setSelected(true) );
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
            LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
            List<LocalDateTime> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(EditRecurrenceRuleVBox.INITIAL_COUNT)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, exceptions);
        }
        TestUtil.runThenWaitForPaintPulse( () -> endAfterEventsSpinner.getValueFactory().decrement(5) );
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
            LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
            List<LocalDateTime> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(EditRecurrenceRuleVBox.INITIAL_COUNT-5)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, exceptions);
        }
        
        // Ends On (UNTIL)
        TestUtil.runThenWaitForPaintPulse( () -> untilRadioButton.setSelected(true));
        {
            LocalDateTime expectedUntil = LocalDateTime.of(2015, 11, 9, 10, 0).plus(EditRecurrenceRuleVBox.DEFAULT_UNTIL_PERIOD);
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
            LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
            Iterator<LocalDateTime> i = Stream.iterate(seed, a -> a.plus(1, ChronoUnit.DAYS)).iterator();
            List<LocalDateTime> expectedDates = new ArrayList<>();
            while (i.hasNext())
            {
                LocalDateTime d = i.next();
                if (d.isAfter(expectedUntil)) break;
                expectedDates.add(d);
            }            
            assertEquals(expectedDates, exceptions);
        }
        TestUtil.runThenWaitForPaintPulse( () -> untilDatePicker.setValue(LocalDate.of(2015, 11, 13)) );
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 11, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                    ));      
            assertEquals(expectedDates, exceptions);
        }
        
        // Ends Never
        TestUtil.runThenWaitForPaintPulse( () -> endNeverRadioButton.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
            LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
            List<LocalDateTime> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(EditRecurrenceRuleVBox.EXCEPTION_CHOICE_LIMIT)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, exceptions);
        }
        clickOn("#cancelRepeatButton");
    }
    
    @Test
    public void canAddException2()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 10, 10, 0),
                    LocalDateTime.of(2015, 11, 10, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });
        clickOn("#recurrenceRuleTab");
        
        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        ListView<Temporal> exceptionsListView = find("#exceptionsListView");
        
        // Add exceptions and check
        Temporal e1 = exceptionComboBox.getItems().get(2);
        TestUtil.runThenWaitForPaintPulse( () -> exceptionComboBox.getSelectionModel().select(e1) );
        clickOn("#addExceptionButton");
        
        {
            // verify date/time removal from exception combo box
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                  , LocalDateTime.of(2015, 11, 14, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
            // verify date/time addition to exception list
            List<Temporal> expectedExceptions = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 11, 10, 0)
                    ));
            assertEquals(expectedExceptions, exceptionsListView.getItems()); 
        }

        // Add another exceptions and check
        Temporal e2 = exceptionComboBox.getItems().get(0);
        TestUtil.runThenWaitForPaintPulse( () -> exceptionComboBox.getSelectionModel().select(e2) );
        clickOn("#addExceptionButton");
        {
            // verify date/time removal from exception combo box
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                  , LocalDateTime.of(2015, 11, 14, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                    ));
            // verify date/time addition to exception list
            assertEquals(expectedDates, exceptions);
            List<LocalDateTime> expectedExceptions = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 11, 10, 0)
                    ));
            assertEquals(expectedExceptions, exceptionsListView.getItems()); 
        }
        
        // save changes to all
        clickOn("#saveRepeatButton");       
        List<VCalendar> messages = getEditComponentPopup().iTIPMessagesProperty().get();
        assertEquals(1, messages.size());
        VCalendar message = messages.get(0);
        
        VCalendar expectedMessage = new VCalendar()
                .withMethod(MethodType.REQUEST)
                .withProductIdentifier(ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER)
                .withVersion(Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION)
                .withVEvents(ICalendarStaticComponents.getDaily1()
                        .withExceptionDates(LocalDateTime.of(2015, 11, 9, 10, 0), LocalDateTime.of(2015, 11, 11, 10, 0))
                        .withDateTimeStamp(message.getVEvents().get(0).getDateTimeStamp())
                        .withSequence(1));
        assertEquals(expectedMessage, message);
    }
    
    @Test
    public void canRemoveException2()
    {
        VEvent vevent = ICalendarStaticComponents.getDailyWithException1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 9, 10, 0),
                    LocalDateTime.of(2015, 11, 9, 11, 30),
                    AgendaTestAbstract.CATEGORIES);
        });
        clickOn("#recurrenceRuleTab");

        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        ListView<Temporal> exceptionsListView = find("#exceptionsListView");
        
        { // verify initial state
            Set<Temporal> vExceptions = vevent.getExceptionDates().get(0).getValue();
            List<Temporal> exceptions = vExceptions
                    .stream()
                    .map(a -> (LocalDateTime) a).sorted()
                    .collect(Collectors.toList());
            List<Temporal> expectedExceptions = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                    ));
            assertEquals(expectedExceptions, exceptions);
            assertEquals(expectedExceptions, exceptionsListView.getItems());            
        }

        { // verify added exception is not in exceptionComboBox list
            List<Temporal> exceptions = exceptionComboBox.getItems().stream()
                    .limit(4)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                      LocalDateTime.of(2015, 11, 9, 10, 0)
                    , LocalDateTime.of(2015, 11, 18, 10, 0)
                    , LocalDateTime.of(2015, 11, 21, 10, 0)
                    , LocalDateTime.of(2015, 11, 24, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }

        // remove Exceptions one at a time, confirm gone and returned to exceptionComboBox
        TestUtil.runThenWaitForPaintPulse( () -> exceptionsListView.getSelectionModel().select(LocalDateTime.of(2015, 11, 12, 10, 0)));
        clickOn("#removeExceptionButton");
        { // verify new state
            List<Temporal> vExceptions = exceptionsListView.getItems();
            List<Temporal> exceptions = vExceptions
                    .stream()
                    .map(a -> (LocalDateTime) a).sorted()
                    .collect(Collectors.toList());
            List<Temporal> expectedExceptions = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 15, 10, 0)
                    ));
            assertEquals(expectedExceptions, exceptions);
            assertEquals(expectedExceptions, exceptionsListView.getItems());            
        }
        { // verify added exception is not in exceptionComboBox list
            List<Temporal> exceptions = exceptionComboBox.getItems().stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                      LocalDateTime.of(2015, 11, 9, 10, 0)
                    , LocalDateTime.of(2015, 11, 12, 10, 0)
                    , LocalDateTime.of(2015, 11, 18, 10, 0)
                    , LocalDateTime.of(2015, 11, 21, 10, 0)
                    , LocalDateTime.of(2015, 11, 24, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        TestUtil.runThenWaitForPaintPulse( () -> exceptionsListView.getSelectionModel().select(LocalDateTime.of(2015, 11, 15, 10, 0)));
        clickOn("#removeExceptionButton");
        { // verify new state
            List<Temporal> vExceptions = exceptionsListView.getItems();
            List<Temporal> exceptions = vExceptions
                    .stream()
                    .map(a -> (LocalDateTime) a).sorted()
                    .collect(Collectors.toList());
            List<Temporal> expectedExceptions = new ArrayList<>(); // empty list
            assertEquals(expectedExceptions, exceptions);
            assertEquals(expectedExceptions, exceptionsListView.getItems());            
        }
        { // verify added exception is not in exceptionComboBox list
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                      LocalDateTime.of(2015, 11, 9, 10, 0)
                    , LocalDateTime.of(2015, 11, 12, 10, 0)
                    , LocalDateTime.of(2015, 11, 15, 10, 0)
                    , LocalDateTime.of(2015, 11, 18, 10, 0)
                    , LocalDateTime.of(2015, 11, 21, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        clickOn("#saveRepeatButton");
        
        String dtstamp = getEditComponentPopup().iTIPMessagesProperty().get().get(0).getVEvents().get(0).getDateTimeStamp().toString();
        
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
                dtstamp + System.lineSeparator() +
                "UID:20150110T080000-005@jfxtras.org" + System.lineSeparator() +
                "RRULE:COUNT=6;FREQ=DAILY;INTERVAL=3" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
