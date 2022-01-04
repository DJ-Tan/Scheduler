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
package jfxtras.internal.scene.control.skin.agenda.base24hour;

import javafx.scene.text.Text;
import jfxtras.scene.control.agenda.Agenda;

/**
 * Responsible for rendering a single whole day appointment on a day header.
 * 
 */
class AppointmentWholedayHeaderPane extends AppointmentAbstractPane
{
	/**
	 * 
	 * @param calendar
	 * @param appointment
	 */
	public AppointmentWholedayHeaderPane(Agenda.Appointment appointment, LayoutHelp layoutHelp)
	{
		super(appointment, layoutHelp);
		
		// add the duration as text
		getChildren().add(createSummaryText());
		
		// add the menu header
		getChildren().add(appointmentMenu);
	}

	private Text createSummaryText() {
		summaryText = new Text(appointment.getSummary());
		summaryText.getStyleClass().add("AppointmentLabel");
		summaryText.setX( layoutHelp.paddingProperty.get() );
		summaryText.setY(summaryText.prefHeight(0));
		layoutHelp.clip(this, summaryText, widthProperty().subtract(layoutHelp.paddingProperty), heightProperty().subtract(0.0), true, 10.0);
		return summaryText;
	}
	private Text summaryText = null;
}