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
package jfxtras.scene.control.agenda.icalendar.factories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.icalendarfx.utilities.Callback;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Default factory to create a {@link VDisplayable} from {@link Appointment}
 * 
 * @author David Bal
 *
 */
public class DefaultVComponentFactory extends VComponentFactory<Appointment>
{
    final private Organizer organizer;
    final private Callback<Void, String> uidGeneratorCallback;
    
    public DefaultVComponentFactory(Organizer organizer, Callback<Void, String> uidGeneratorCallback)
    {
        this.organizer = organizer;
        this.uidGeneratorCallback = uidGeneratorCallback;
    }
    
    @Override
    public VDisplayable<?> createVComponent(Appointment appointment)
    {
        final VDisplayable<?> newVComponent;
        ZonedDateTime dtCreated = ZonedDateTime.now(ZoneId.of("Z"));
        String summary = ((appointment.getSummary() == null) || appointment.getSummary().isEmpty()) ? null : appointment.getSummary();
        String description = ((appointment.getDescription() == null) || appointment.getDescription().isEmpty()) ? null : appointment.getDescription();
        String category = (appointment.getAppointmentGroup() == null) ? null : appointment.getAppointmentGroup().getDescription();
        String location = ((appointment.getLocation() == null) || appointment.getLocation().isEmpty()) ? null : appointment.getLocation();
        Temporal dtstart = (appointment.isWholeDay()) ? LocalDate.from(appointment.getStartTemporal()) : appointment.getStartTemporal();
        Temporal dtend = (appointment.isWholeDay()) ? LocalDate.from(appointment.getEndTemporal()) : appointment.getEndTemporal();
        
        boolean hasEnd = appointment.getEndTemporal() != null;
        if (hasEnd)
        {
            newVComponent = new VEvent();
            newVComponent.withOrganizer(organizer);
            newVComponent.withSummary(summary);
            newVComponent.withCategories(category);
            newVComponent.withDateTimeStart(dtstart);
            ((VEvent) newVComponent).withDateTimeEnd(dtend);
            // TODO - HANDLE NULL STRINGS
            ((VEvent) newVComponent).withDescription(description);
            ((VEvent) newVComponent).setLocation(location);
            newVComponent.setDateTimeCreated(dtCreated);
            newVComponent.setDateTimeStamp(dtCreated);
            newVComponent.setUidGeneratorCallback(uidGeneratorCallback);
            newVComponent.setUniqueIdentifier();
            // TODO - GRADLE WON'T ALLOW CHAINING AS SEEN BELOW.  WHY???
//            newVComponent = new VEvent()
//                    .withOrganizer(organizer)
//                    .withSummary(summary)
//                    .withCategories(category)
//                    .withDateTimeStart(dtstart)
//                    .withDateTimeEnd(dtend)
//                    .withDescription(description)
//                    .withLocation(location)
//                    .withDateTimeCreated(dtCreated)
//                    .withDateTimeStamp(dtCreated)
//                    .withUniqueIdentifier(); // using default UID generator
        } else
        {
            newVComponent = new VTodo();
            newVComponent.withOrganizer(organizer);
            newVComponent.withSummary(summary);
            newVComponent.withCategories(category);
            newVComponent.withDateTimeStart(dtstart);
            ((VTodo) newVComponent).withDescription(description);
            ((VTodo) newVComponent).setLocation(location);
            newVComponent.setDateTimeCreated(dtCreated);
            newVComponent.setDateTimeStamp(dtCreated);
            newVComponent.setUidGeneratorCallback(uidGeneratorCallback);
            newVComponent.setUniqueIdentifier();
            // TODO - GRADLE WON'T ALLOW CHAINING AS SEEN BELOW.  WHY???
//          newVComponent = new VTodo()
//          .withOrganizer(organizer)
//          .withSummary(summary)
//          .withCategories(category)
//          .withDateTimeStart(dtstart)
//          .withDescription(description)
//          .withLocation(location)
//          .withDateTimeCreated(dtCreated)
//          .withDateTimeStamp(dtCreated)
//          .withUniqueIdentifier();
        }
        /* Note: If other VComponents are to be supported then other tests to determine
         * which type of VComponent the Appointment represents will need to be created.
         */
        return newVComponent;
    }
}
