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
package jfxtras.scene.control.agenda.icalendar.editors.revisors;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.List;

import jfxtras.icalendarfx.components.VLocatable;
import jfxtras.icalendarfx.properties.VPropertyElement;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

/**
 * Handles revising a {@link VComponentLocatable}
 * 
 * @author David Bal
 *
 * @param <T> concrete implementation of this class
 * @param <U> concrete {@link VComponentLocatable} class
 */
public abstract class ReviserLocatable<T, U extends VLocatable<U>> extends ReviserDisplayable<T, U>
{
    public ReviserLocatable(U component)
    {
        super(component);
    }

    /*
     * END RECURRENCE - NEW VALUE
     */
    /** Gets the value of the end of the selected recurrence after changes */
    public Temporal getEndRecurrence() { return endRecurrence; }
    private Temporal endRecurrence;
    /** Sets the value of the end of the selected recurrence after changes */
    public void setEndRecurrence(Temporal startRecurrence) { this.endRecurrence = startRecurrence; }
    /**
     * Sets the value of the end of the selected recurrence after changes
     * 
     * @return - this class for chaining
     */
    public T withEndRecurrence(Temporal endRecurrence) { setEndRecurrence(endRecurrence); return (T) this; }
    
    @Override
    boolean isValid()
    {
        if (getEndRecurrence() == null)
        {
//            System.out.println("endRecurrence must not be null");
            return false;
        }
        return super.isValid();
    }
    
    @Override
    void adjustStartAndEnd(U vComponentEditedCopy, U vComponentOriginalCopy)
    {
        // Adjust start and end - set recurrence temporal as start
        vComponentEditedCopy.setDateTimeStart(new DateTimeStart(getStartRecurrence()));
        vComponentEditedCopy.setEndOrDuration(getStartRecurrence(), getEndRecurrence());
    }
    
    @Override
    void becomeNonRecurring(U vComponentEditedCopy)
    {
        super.becomeNonRecurring(vComponentEditedCopy);
        if (getVComponentOriginal().getRecurrenceRule() != null)
        { // RRULE was removed, update DTSTART, DTEND or DURATION
            getVComponentCopyEdited().setDateTimeStart(new DateTimeStart(getStartRecurrence()));
            if (getVComponentCopyEdited().getDuration() != null)
            {
                TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
                getVComponentCopyEdited().setDuration(duration);
            }
        }
    }
    
    @Override
    public List<VPropertyElement> dialogRequiredProperties()
    {
        List<VPropertyElement> list = super.dialogRequiredProperties();
        list.add(VPropertyElement.DATE_TIME_END);
        return list;
    }
}
