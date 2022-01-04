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
package jfxtras.scene.control.agenda.icalendar.editors.deleters;

import java.time.temporal.Temporal;
import java.util.Map;

import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;

/**
 * <p>Simple factory to create {@link Deleter} objects.  Two methods to create Deleter
 * exist.  One takes only a VComponent as a parameter and builds an empty {@link Deleter}.
 * The second takes a VComponent and an array of parameters required to completely
 * initialize the {@link Deleter}.</p>
 * 
 * <p>The parameters array contains the following  (in this order):
 * <ul>
 * <li>{@code Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>} - callback for user dialog
 * <li>Temporal - startOriginalRecurrence, start of selected recurrence
 * <li>List-VComponent - list of components that vComponent is a member
 * </ul>
 * </p>
 * 
 * @author David Bal
 *
 */
public class SimpleDeleterFactory
{
	/**
	 * 
	 * @param vComponent - {@link VComponent} to have delete action acted upon
	 * @param params 
	 * @return New Deleter
	 */
    public static Deleter newDeleter (VComponent vComponent, Object[] params)
    {
        if (vComponent instanceof VEvent)
        {
            return new DeleterVEvent((VEvent) vComponent)
                    .withDialogCallback((Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>) params[0])
                    .withStartOriginalRecurrence((Temporal) params[1])
                    ;
        } else if (vComponent instanceof VTodo)
        {
            return new DeleterVTodo((VTodo) vComponent)
                    .withDialogCallback((Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>) params[0])
                    .withStartOriginalRecurrence((Temporal) params[1])
                    ;

        } else if (vComponent instanceof VJournal)
        {
            return new DeleterVJournal((VJournal) vComponent)
                    .withDialogCallback((Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>) params[0])
                    .withStartOriginalRecurrence((Temporal) params[1])
                    ;
        } else if (vComponent instanceof VFreeBusy)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VTimeZone)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VAlarm)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof StandardTime)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof DaylightSavingTime)
        {
            throw new RuntimeException("not implemented");          
        } else
        {
            throw new RuntimeException("Unsupported VComponent type" + vComponent.getClass());
        }
    }
    
    /**
     * 
     * @param vComponent - {@link VComponent} to have delete action acted upon
     * @param params 
     * @return New Deleter
     */
    public static Deleter newDeleter (
            VComponent vComponent,
            Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption> changeDialogCallback,
            Temporal startOriginalRecurrence
            )
    {
        if (vComponent instanceof VEvent)
        {
            return new DeleterVEvent((VEvent) vComponent)
                    .withDialogCallback(changeDialogCallback)
                    .withStartOriginalRecurrence(startOriginalRecurrence);
        } else if (vComponent instanceof VTodo)
        {
            return new DeleterVTodo((VTodo) vComponent)
                    .withDialogCallback(changeDialogCallback)
                    .withStartOriginalRecurrence(startOriginalRecurrence);
        } else if (vComponent instanceof VJournal)
        {
            return new DeleterVJournal((VJournal) vComponent)
                    .withDialogCallback(changeDialogCallback)
                    .withStartOriginalRecurrence(startOriginalRecurrence);
        } else if (vComponent instanceof VFreeBusy)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VTimeZone)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VAlarm)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof StandardTime)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof DaylightSavingTime)
        {
            throw new RuntimeException("not implemented");          
        } else
        {
            throw new RuntimeException("Unsupported VComponent type" + vComponent.getClass());
        }
    }
    
    public static Deleter newDeleter (VComponent vComponent)
    {
        if (vComponent instanceof VEvent)
        {
            return new DeleterVEvent((VEvent) vComponent);
        } else if (vComponent instanceof VTodo)
        {
            return new DeleterVTodo((VTodo) vComponent);            
        } else if (vComponent instanceof VJournal)
        {
            return new DeleterVJournal((VJournal) vComponent);            
        } else if (vComponent instanceof VFreeBusy)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VTimeZone)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VAlarm)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof StandardTime)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof DaylightSavingTime)
        {
            throw new RuntimeException("not implemented");          
        } else
        {
            throw new RuntimeException("Unsupported VComponent type" + vComponent.getClass());
        }
    }
}
