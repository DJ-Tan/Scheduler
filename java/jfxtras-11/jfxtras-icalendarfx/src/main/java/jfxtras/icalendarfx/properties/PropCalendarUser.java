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
package jfxtras.icalendarfx.properties;

import jfxtras.icalendarfx.parameters.CommonName;
import jfxtras.icalendarfx.parameters.DirectoryEntry;
import jfxtras.icalendarfx.parameters.SentBy;
import jfxtras.icalendarfx.properties.PropLanguage;
import jfxtras.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.icalendarfx.properties.component.relationship.PropertyBaseCalendarUser;

/**
 * Abstract class for properties with a CAL-ADDRESS value.
 * The value is stored as a URI object
 * 
 * CAL-ADDRESS
 * Calendar User Address
 * RFC 5545, 3.3.3, page 31
 *  
 * This value type is used to identify properties that contain a calendar user address.
 * The email address of a calendar user.
 * 
 * Example: mailto:jane_doe@example.com
 * 
 * @author David Bal
 *
 * @param <T>
 * @param <T>
 *
 * Base class
 * @see PropertyBaseCalendarUser
 *  
 * concrete subclasses
 * @see Organizer
 * @see Attendee
 */
public interface PropCalendarUser<T> extends PropLanguage<T>
{
    /**
     * CN
     * Common Name
     * RFC 5545, 3.2.2, page 15
     * 
     * To specify the common name to be associated with the calendar user specified by the property.
     * 
     * Example:
     * ORGANIZER;CN="John Smith":mailto:jsmith@example.com
     */
    CommonName getCommonName();
    void setCommonName(CommonName commonName);  

    /**
     * DIR
     * Directory Entry Reference
     * RFC 5545, 3.2.6, page 18
     * 
     * To specify reference to a directory entry associated with
     *     the calendar user specified by the property.
     * 
     * Example:
     * ORGANIZER;DIR="ldap://example.com:6666/o=ABC%20Industries,
     *  c=US???(cn=Jim%20Dolittle)":mailto:jimdo@example.com
     */
    DirectoryEntry getDirectoryEntryReference();
    void setDirectoryEntryReference(DirectoryEntry directoryEntryReference);
    
    /**
     * SENT-BY
     * RFC 5545, 3.2.18, page 27
     * 
     * To specify the calendar user that is acting on behalf of
     * the calendar user specified by the property.
     * 
     * Example:
     * ORGANIZER;SENT-BY="mailto:sray@example.com":mailto:
     *  jsmith@example.com
     */
    SentBy getSentBy();
    void setSentBy(SentBy sentBy);
}
