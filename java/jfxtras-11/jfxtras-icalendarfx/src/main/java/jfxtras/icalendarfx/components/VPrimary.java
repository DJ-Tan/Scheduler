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
package jfxtras.icalendarfx.components;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.components.VCommon;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

/**
 * Components with the following properties:
 * COMMENT, DTSTART
 * 
 * @author David Bal
 *
 * @param <T> - implementation subclass
 * @see VEventNewInt
 * @see VTodoInt
 * @see VJournalInt
 * @see VFreeBusy
 * @see VTimeZone
 */
public abstract class VPrimary<T> extends VCommon<T>
{
    /**
     *  COMMENT: RFC 5545 iCalendar 3.8.1.12. page 83
     * This property specifies non-processing information intended
      to provide a comment to the calendar user.
     * Example:
     * COMMENT:The meeting really needs to include both ourselves
         and the customer. We can't hold this meeting without them.
         As a matter of fact\, the venue for the meeting ought to be at
         their site. - - John
     * */
    public List<Comment> getComments() { return comments; }
    private List<Comment> comments;
    public void setComments(List<Comment> comments)
    {
    	if (this.comments != null)
    	{
    		this.comments.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.comments = comments;
    	if (comments != null)
    	{
    		comments.forEach(c -> orderChild(c)); // order new elements
    	}
	}
    public T withComments(List<Comment> comments)
    {
    	if (getComments() == null)
    	{
    		setComments(new ArrayList<>());
    	}
    	getComments().addAll(comments);
    	if (comments != null)
    	{
    		comments.forEach(c -> orderChild(c));
    	}
        return (T) this;
    }
    public T withComments(String...comments)
    {
        List<Comment> list = Arrays.stream(comments)
                .map(c -> Comment.parse(c))
                .collect(Collectors.toList());
        return withComments(list);
    }
    public T withComments(Comment...comments)
    {
    	return withComments(Arrays.asList(comments));
    }
    
    /**
     * DTSTART: Date-Time Start, from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.
     * Can contain either a LocalDate (DATE) or LocalDateTime (DATE-TIME)
     */
    // hook to be overridden in subclasses
    @Deprecated // is this needed?
    void dateTimeStartListenerHook() { }
    
    public DateTimeStart getDateTimeStart() { return dateTimeStart; }
    private DateTimeStart dateTimeStart;
    public void setDateTimeStart(DateTimeStart dateTimeStart)
    {
    	orderChild(this.dateTimeStart, dateTimeStart);
    	this.dateTimeStart = dateTimeStart;
	}
    public void setDateTimeStart(String dateTimeStart) { setDateTimeStart(DateTimeStart.parse(dateTimeStart)); }
    public void setDateTimeStart(Temporal temporal) { setDateTimeStart(new DateTimeStart(temporal)); }
    public T withDateTimeStart(DateTimeStart dateTimeStart)
    {
        setDateTimeStart(dateTimeStart);
        return (T) this;
    }
    public T withDateTimeStart(String dateTimeStart)
    {
        setDateTimeStart(dateTimeStart);
        return (T) this;
    }
    public T withDateTimeStart(Temporal dateTimeStart)
    {
        setDateTimeStart(dateTimeStart);
        return (T) this;
    }
    
    /** Component is whole day if dateTimeStart (DTSTART) only contains a date (no time) */
    public boolean isWholeDay()
    {
        return ! getDateTimeStart().getValue().isSupported(ChronoUnit.NANOS);
    }
    
    /*
     * CONSTRUCTORS
     */
    VPrimary() { super(); }

    public VPrimary(VPrimary<T> source)
    {
        super(source);
    }
    
    /**
     * Sorts VComponents by DTSTART date/time
     */
    public final static Comparator<? super VPrimary<?>> DTSTART_COMPARATOR = (v1, v2) -> 
    {
        Temporal t1 = v1.getDateTimeStart().getValue();
        Temporal t2 = v2.getDateTimeStart().getValue();
        return DateTimeUtilities.TEMPORAL_COMPARATOR2.compare(t1, t2);
    };
}
