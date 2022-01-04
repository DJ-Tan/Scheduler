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

import java.io.IOException;
import java.net.URL;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Interval;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.DeleteChoiceDialog;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.Settings;
import jfxtras.scene.control.agenda.icalendar.editors.deleters.SimpleDeleterFactory;

/** 
 * Base TabPane that contains two tabs for editing descriptive properties and for editing a {@link RecurrenceRule}.
 * The first tab contains a {@link EditDescriptiveVBox }.  The second contains a {@link EditRecurrenceRuleVBox}.
 * 
 * @author David Bal
 * 
 * @param <T> subclass of {@link VDisplayable}
 * @param <U> subclass of {@link EditDescriptiveVBox} associated with the subclass of {@link VDisplayable}
 */
public abstract class EditDisplayableTabPane<T extends VDisplayable<T>, U extends EditDescriptiveVBox<T>> extends TabPane
{
    U editDescriptiveVBox;
    EditRecurrenceRuleVBox<T> recurrenceRuleVBox;

    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    @FXML AnchorPane descriptiveAnchorPane;
    @FXML AnchorPane recurrenceRuleAnchorPane;
    @FXML private TabPane editDisplayableTabPane;
    @FXML private Tab descriptiveTab;
    @FXML private Tab recurrenceRuleTab;
    
    @FXML private Button cancelComponentButton;
    @FXML private Button saveComponentButton;
    @FXML private Button deleteComponentButton;
    @FXML private Button cancelRepeatButton;
    @FXML private Button saveRepeatButton;

    ObjectProperty<List<VCalendar>> iTIPMessages = new SimpleObjectProperty<>();
    public ObjectProperty<List<VCalendar>> iTIPMessagesProperty() { return iTIPMessages; }
    
    public EditDisplayableTabPane( )
    {
        super();
        loadFxml(EditDescriptiveVBox.class.getResource("EditDisplayable.fxml"), this);
    }
    
    @FXML
    void handleSaveButton()
    {
        removeEmptyProperties();
    }

    void removeEmptyProperties()
    {
        if (vComponentCopy.getRecurrenceRule() != null)
        {
            if (recurrenceRuleVBox.frequencyComboBox.getValue() == FrequencyType.WEEKLY && recurrenceRuleVBox.dayOfWeekList.isEmpty())
            {
                canNotHaveZeroDaysOfWeek();
            } else if (! vComponentCopy.getRecurrenceRule().isValid())
            {
                throw new RuntimeException("Unhandled component error" + System.lineSeparator() + vComponentCopy.errors());
            }
        }
        
        if (editDescriptiveVBox.summaryTextField.getText().isEmpty())
        {
            vComponentCopy.setSummary((Summary) null); 
        }
        
        if (editDescriptiveVBox.categoryTextField.getText().isEmpty())
        {
            vComponentCopy.setCategories(null); 
        }

       // nullify Interval if value equals default (avoid unnecessary content output)
        if ((vComponentCopy.getRecurrenceRule() != null) && (recurrenceRuleVBox.intervalSpinner.getValue() == Interval.DEFAULT_INTERVAL))
        {
            vComponentCopy.getRecurrenceRule().getValue().setInterval((Interval) null); 
        }
    }
    
    @FXML private void handleCancelButton()
    {
        iTIPMessagesProperty().set(Collections.emptyList());
    }
    
    @FXML private void handleDeleteButton()
    {
        removeEmptyProperties();
        Object[] params = new Object[] {
                DeleteChoiceDialog.DELETE_DIALOG_CALLBACK,
                editDescriptiveVBox.startOriginalRecurrence
        };
        List<VCalendar> result = SimpleDeleterFactory.newDeleter(vComponentCopy, params).delete();
        iTIPMessagesProperty().set(result);
    }
    
    @FXML private void handlePressEnter(KeyEvent e)
    {
        if (e.getCode().equals(KeyCode.ENTER))
        {
            handleSaveButton();
        }
    }
    
    T vComponentCopy;
    T vComponentOriginal;
    public static VComponent vo;

    /**
     * Provide necessary data to setup
     * 
     * @param vComponentCopy - component to be edited
     * @param startRecurrence - start of selected recurrence
     * @param endRecurrence - end of selected recurrence
     * @param categories - list of category names
     */
    public void setupData(
            T vComponentCopy,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories
            )
    {
        this.vComponentCopy = vComponentCopy;
        vo = vComponentOriginal;
        editDescriptiveVBox.setupData(vComponentCopy, startRecurrence, endRecurrence, categories);
        
        /* 
         * Shut off repeat tab if vComponent is not a parent
         * Components with RECURRENCE-ID can't add repeat rules (only parent can have repeat rules)
         */
        if (vComponentCopy.getRecurrenceId() != null)
        {
            recurrenceRuleTab.setDisable(true);
            recurrenceRuleTab.setTooltip(new Tooltip(resources.getString("repeat.tab.unavailable")));
        }
        recurrenceRuleVBox.setupData(vComponentCopy, editDescriptiveVBox.startRecurrenceProperty);
    }
    
    // Displays an alert notifying at least one day of week must be present for weekly frequency
    private static void canNotHaveZeroDaysOfWeek()
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Modification");
        alert.setHeaderText("Please select at least one day of the week.");
        alert.setContentText("Weekly repeat must have at least one selected day");
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        
        // set id for testing
        alert.getDialogPane().setId("zero_day_of_week_alert");
        alert.getDialogPane().lookupButton(buttonTypeOk).setId("zero_day_of_week_alert_button_ok");
        
        alert.showAndWait();
    }
    
    protected static void loadFxml(URL fxmlFile, Object rootController)
    {
        FXMLLoader loader = new FXMLLoader(fxmlFile);
        loader.setController(rootController);
        loader.setRoot(rootController);
        loader.setResources(Settings.resources);
        try {
            loader.load();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
 
