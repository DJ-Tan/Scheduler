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
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.test.TestUtil;

@Ignore // TBEERNOT this fails dramatically when run by Maven
public class DeleteVEventTest extends AgendaTestAbstract
{
    @Test
    public void canDeleteOne()
    {
        // Add VComponents, listener in ICalendarAgenda makes Appointments
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily1());
            agenda.refresh();
        });
        
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        
        clickOn("#OneAppointmentSelectedDeleteButton");
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            comboBox.getSelectionModel().select(ChangeDialogOption.ONE);
        });
        clickOn("#changeDialogOkButton");
        assertEquals(5, agenda.appointments().size());
        List<Temporal> expectedStarts = Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0),
                LocalDateTime.of(2015, 11, 10, 10, 0),
                LocalDateTime.of(2015, 11, 12, 10, 0),
                LocalDateTime.of(2015, 11, 13, 10, 0),
                LocalDateTime.of(2015, 11, 14, 10, 0)
                );
        List<Temporal> starts = agenda.appointments().stream()
                .map(a -> a.getStartTemporal())
                .collect(Collectors.toList());
        assertEquals(expectedStarts, starts);
        
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent expectedVEvent = ICalendarStaticComponents.getDaily1()
                .withExceptionDates("20151111T100000")
                .withSequence(1);
        VEvent editedVEvent = agenda.getVCalendar().getVEvents().get(0);
        assertEquals(expectedVEvent, editedVEvent);
    }
    
    @Test
    public void canDeleteOneWithDeleteKey()
    {
        // Add VComponents, listener in ICalendarAgenda makes Appointments
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily1());
            agenda.refresh();
        });
        
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        
        clickOn("Cancel");
        press(KeyCode.DELETE);
        release(KeyCode.DELETE);
        
        assertEquals(5, agenda.appointments().size());
        List<Temporal> expectedStarts = Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0),
                LocalDateTime.of(2015, 11, 10, 10, 0),
                LocalDateTime.of(2015, 11, 12, 10, 0),
                LocalDateTime.of(2015, 11, 13, 10, 0),
                LocalDateTime.of(2015, 11, 14, 10, 0)
                );
        List<Temporal> starts = agenda.appointments().stream()
                .map(a -> a.getStartTemporal())
                .collect(Collectors.toList());
        assertEquals(expectedStarts, starts);
        
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent expectedVEvent = ICalendarStaticComponents.getDaily1()
                .withExceptionDates("20151111T100000")
                .withSequence(1);
        VEvent editedVEvent = agenda.getVCalendar().getVEvents().get(0);
        assertEquals(expectedVEvent, editedVEvent);
    }
    
    @Test
    public void canDeleteAll()
    {
        // Add VComponents, listener in ICalendarAgenda makes Appointments
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily1());
            agenda.refresh();
        });
        
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        
        clickOn("#OneAppointmentSelectedDeleteButton");
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            comboBox.getSelectionModel().select(ChangeDialogOption.ALL);
        });
        clickOn("#changeDialogOkButton");
        
        assertEquals(0, agenda.appointments().size());
        assertEquals(0, agenda.getVCalendar().getVEvents().size());
    }
}
