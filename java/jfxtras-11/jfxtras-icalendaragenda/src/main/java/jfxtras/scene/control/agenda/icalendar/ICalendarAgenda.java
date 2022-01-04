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
package jfxtras.scene.control.agenda.icalendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.VPropertyElement;
import jfxtras.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.Location;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Count;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Frequency;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Interval;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Until;
import jfxtras.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.AgendaDateTimeUtilities;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.DeleteChoiceDialog;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.EditChoiceDialog;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.EditWithRecurrencesChoiceDialog;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.NewAppointmentDialog;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.OneAppointmentSelectedAlert;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.Settings;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.EditDisplayableScene;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.SimpleEditSceneFactory;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.scene.control.agenda.icalendar.editors.deleters.SimpleDeleterFactory;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.Reviser;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.SimpleRevisorFactory;
import jfxtras.scene.control.agenda.icalendar.factories.DefaultRecurrenceFactory;
import jfxtras.scene.control.agenda.icalendar.factories.DefaultVComponentFactory;
import jfxtras.scene.control.agenda.icalendar.factories.RecurrenceFactory;
import jfxtras.scene.control.agenda.icalendar.factories.VComponentFactory;
import jfxtras.util.NodeUtil;

/**
 * <p>The {@link ICalendarAgenda} control is designed to take a {@link VCalendar VCALENDAR} object,
 * which is based on the iCalendar RFC 5545 standard, and renders it in {@link Agenda}, which is a calendar
 * display control. {@link ICalendarAgenda} renders only the {@link VDisplayable displayable}
 * iCalendar components which are {@link VEvent VEVENT}, {@link VTodo VTODO}, and {@link VJournal VJOURNAL}.
 * Other calendar components are ignored.</p>
 * 
 * <p>The {@link ICalendarAgenda} control has a number of features, including:
 * <ul>
 * <li>Powerful {@link EditDisplayableScene edit control} to modify calendar components:
 *   <ul>
 *   <li>Edits DATE or DATE-TIME properties including:
 *     <ul>
 *     <li>{@link DateTimeStart DTSTART} - when the calendar component begins.
 *     <li>{@link DateTimeEnd DTEND} - when the calendar component ends.
 *     </ul>
 *   <li>Can toggle between DATE or DATE-TIME values
 *   <li>Edits descriptive properties including:
 *     <ul>
 *     <li>{@link Summary SUMMARY}
 *     <li>{@link Description DESCRIPTION}
 *     <li>{@link Location LOCATION}
 *     <li>{@link Categories CATEGORIES} - from a color-coded selectable grid (only one category supported)
 *     </ul>
 *   <li>Edits {@link RecurrenceRule RRULE}, recurrence rule, elements including:
 *     <ul>
 *     <li>{@link Frequency FREQUENCY} - type of recurrence, including Daily, Weekly, Monthly and Yearly 
 *     <li>{@link Interval INTERVAL} - represents the intervals the recurrence rule repeats
 *     <li>{@link Count COUNT} - the number of occurrences.
 *     <li>{@link Until UNTIL} - the DATE or DATE-TIME value that bounds the recurrence rule in an inclusive manner
 *     <li>{@link ExceptionDates EXDATE} - list of DATE-TIME values that are skipped
 *     </ul>
 *   <li>Displays a easy-to-read description of the {@link RecurrenceRule RRULE}, recurrence rule
 *   </ul>
 * <li>Automatically synchronizes graphical changes with the {@link VCalendar VCALENDAR} object.
 * <li>Uses an abstract {@link RecurrenceFactory} to create {@link Appointment} objects that are rendered
 *  by {@link Agenda}
 *   <ul>
 *   <li>A default factory is included that creates the default {@link AppointmentImplTemporal} objects
 *   <li>A custom factory can be added to create custom {@link Appointment} objects.
 *   </ul>
 * <li>Uses an abstract {@link VComponentFactory} to create {@link VDisplayable} objects when new events
 *  are drawn by clicking and drag-and-drop actions.
 *   <ul>
 *   <li>A default factory is included that creates {@link VEvent VEVENT} and {@link VTodo VTODO} components
 *    from the default {@link AppointmentImplTemporal} object.
 *   <li>A custom factory can be added to create iCalendar components from custom {@link Appointment} objects.
 *   </ul>
 * </ul>
 * </p>
 * 
 * <p>If not using the default {@link AppointmentImplTemporal} implementation, but a different {@link Appointment}
 * implementation, then use the following setter methods to configure the required factories and callback:
 * <ul>
 * <li>{@link #setRecurrenceFactory(RecurrenceFactory)}
 * <li>{@link #setVComponentFactory(VComponentFactory)} 
 * <li>{@link #setNewAppointmentCallback(Callback)} 
 * </ul>
 * </p>
 * 
 * <h2>Creating a ICalendarAgenda</h2>
 * 
 * <p>Firstly, a {@link VCalendar VCALENDAR} instance needs to be defined.  For example:
 * <pre> {@code VCalendar vCalendar = new VCalendar();}</pre>
 * Optionally, the {@link VCalendar VCALENDAR} instance can be set with calendar components.  This can be done
 * by reading a .ics file or building the calendar components programmatically through the API.  Please see the
 * iCalendarFX documentation for more details.  An empty {@link VCalendar VCALENDAR} is also acceptable.</p>
 * 
 * <p>Next, the {@link VCalendar VCALENDAR} instance must be provided in the {@link ICalendarAgenda} constructor
 * as shown below:.
 * <pre> {@code ICalendarAgenda iCalendarAgenda = new ICalendarAgenda(vCalendar);}</pre>
 * Nothing else special is required to instantiate {@link ICalendarAgenda} if you use the default factories.</p>
 * 
 * <p> A simple example to display a {@link ICalendarAgenda} with an example {@link VEvent VEVENT} is below:
 * 
 * <pre>
 * {@code
 * public class ICalendarAgendaSimpleTrial extends Application
 * {        
 *    public static void main(String[] args) {
 *       launch(args);       
 * }
 *
 *   public void start(Stage primaryStage) {
 *       VCalendar vCalendar = new VCalendar();
 *       VEvent vEvent = new VEvent()
 *               .withDateTimeStart(LocalDateTime.now().minusMonths(1))
 *               .withDateTimeEnd(LocalDateTime.now().minusMonths(1).plusHours(1))
 *               .withSummary("Example Daily Event")
 *               .withRecurrenceRule("RRULE:FREQ=DAILY")
 *               .withUniqueIdentifier("exampleuid000jfxtras.org");
 *       vCalendar.addChild(vEvent);
 *       ICalendarAgenda agenda = new ICalendarAgenda(vCalendar);
 *       
 *       BorderPane root = new BorderPane();
 *       root.setCenter(agenda);
 *       Scene scene = new Scene(root, 1366, 768);
 *       primaryStage.setScene(scene);
 *       primaryStage.setTitle("ICalendar Agenda Simple Demo");
 *       primaryStage.show();
 *   }
 * }}</pre>
 * </p>
 * 
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc5545#section-3.8.2.2">iCalendar 5545 Specification</a>
 * @author David Bal
 *
 */
public class ICalendarAgenda extends Agenda
{   
    public final static String ICALENDAR_STYLE_SHEET = ICalendarAgenda.class.getResource(ICalendarAgenda.class.getSimpleName() + ".css").toExternalForm();

    public static final String MY_VERSION = "1.0";
    public static final String DEFAULT_PRODUCT_IDENTIFIER = ("-//JFxtras//iCalendarAgenda " + ICalendarAgenda.MY_VERSION + "//EN");
    
    /* Default Organizer */
    public static final String DEFAULT_ORGANIZER = "mailto:default_organizer@example.org";
    public ObjectProperty<Organizer> organizerProperty()
    {
        return organizer;
    }
    private ObjectProperty<Organizer> organizer = new SimpleObjectProperty<>(this, VPropertyElement.ORGANIZER.toString(), Organizer.parse(DEFAULT_ORGANIZER));
    public Organizer getOrganizer()
    {
        return organizer.get();
    }
    public void setOrganizer(Organizer organizer)
    {
        this.organizer.set(organizer);
        if (vComponentFactory instanceof DefaultVComponentFactory)
        {
            vComponentFactory = new DefaultVComponentFactory(getOrganizer(), getUidGeneratorCallback());
        }
        // Note: if not using the default VComponent factory, and the organizer is set to a non-default
        // value, and the vComponentFactory uses the organizer property, then the vComponentFactory
        // must be replaced with a new one with the new organizer property.
        // The code here only replaces the default vcomponent factory automatically.
    }
    public void setOrganizer(String organizer)
    {
        setOrganizer(Organizer.parse(organizer));
    }
    public ICalendarAgenda withOrganizer(Organizer organizer)
    {
        setOrganizer(organizer);
        return this;
    }
    public ICalendarAgenda withOrganizer(String organizer)
    {
        setOrganizer(organizer);
        return this;
    }
    
    /* UID Generator Callback */
    private static Integer nextKey = 0;
    private jfxtras.icalendarfx.utilities.Callback<Void, String> uidGeneratorCallback = (Void) ->
    { // default UID generator callback
        String dateTime = DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(LocalDateTime.now());
        String domain = "jfxtras.org";
        return dateTime + "-" + nextKey++ + domain;
    };
    /** set UID callback generator.  It makes UID values for new components. */
    public jfxtras.icalendarfx.utilities.Callback<Void, String> getUidGeneratorCallback()
    {
    	return uidGeneratorCallback;
	}
    /** set UID callback generator. It makes UID values for new components. */
    public void setUidGeneratorCallback(jfxtras.icalendarfx.utilities.Callback<Void, String> uidCallback)
    {
    	this.uidGeneratorCallback = uidCallback;
        if (vComponentFactory instanceof DefaultVComponentFactory)
        {
            vComponentFactory = new DefaultVComponentFactory(getOrganizer(), getUidGeneratorCallback());
        }
        // Note: if not using the default VComponent factory, and the organizer is set to a non-default
        // value, and the vComponentFactory uses the organizer property, then the vComponentFactory
        // must be replaced with a new one with the new organizer property.
        // The code here only replaces the default vcomponent factory automatically.
	}
    /** set UID callback generator.  Return itself for chaining. */
    public ICalendarAgenda withUidGeneratorCallback(jfxtras.icalendarfx.utilities.Callback<Void, String> uidCallback)
    {
        setUidGeneratorCallback(uidCallback);
        return this;
    }
    
    final private VCalendar vCalendar;
    /** get the VCalendar object that is a model of the iCalendar RFC 5545 specification */
    public VCalendar getVCalendar() { return vCalendar; }
    
    /*
     * Factory to make VComponents from Appointments
     */
    private VComponentFactory<Appointment> vComponentFactory;
    /** Gets the value of the {@link VComponent} factory */
    public VComponentFactory<Appointment> getVComponentFactory() { return vComponentFactory; }
    /** Sets the value of the {@link VComponent} factory */
    public void setVComponentFactory(VComponentFactory<Appointment> vComponentFactory) { this.vComponentFactory = vComponentFactory; }

    /*
     * Factory to make Appointments from VComponents
     */
    private RecurrenceFactory<Appointment> recurrenceFactory;
    /** Gets the value of the VComponent factory */
    public RecurrenceFactory<Appointment> getRecurrenceFactory() { return recurrenceFactory; }
    /** Sets the value of the recurrence factory */
    public void setRecurrenceFactory(RecurrenceFactory<Appointment> recurrenceFactory) { this.recurrenceFactory = recurrenceFactory; }

    
    /*
     * This consumer's opperation is performed when a change is made to the vCalendar by agenda
     * It can be used to notify client code of changes. 
     */
    private Consumer<VCalendar> calendarConsumer = (v) ->
    {
    	/* default consumer does nothing
    	 * 
    	 * Example - run a notional method that synchs the calendar with external data store.
    	 * synchCalendar(v);
    	 */
    };
	public void setVCalendarUpdatedConsumer(Consumer<VCalendar> calendarConsumer)
	{
		this.calendarConsumer = calendarConsumer;
	}
	public Consumer<VCalendar> getVCalendarUpdatedConsumer()
	{
		return calendarConsumer;
	}
	
    /*
     * Category list - contains the descriptive part of AppointmentGroups
     */
    // TODO - consider replacing this object - can I rely on AppointmentGroups list?
    private ObservableList<String> categories;
    /** Gets the value of the categories list */
    public ObservableList<String> getCategories() { return categories; }
    /** Sets the value of the categories list */
    public void setCategories(ObservableList<String> categories) { this.categories = categories; }

    /*
     * INTERNAL MAPS
     */
    /* Map to match the System.identityHashCode of Appointments with the original start DATE or DATE/TIME
     * The original value is required for revising a VComponent when Agenda changes an appointment (e.g. drag-n-drop).
     * The original is needed for the RECURRENCE-ID. property */
    private final Map<Integer, Temporal> appointmentStartOriginalMap = new HashMap<>();
    /* Map to match the System.identityHashCode of each Appointment with the VComponent it represents. */
    private final Map<Integer, VDisplayable<?>> appointmentVComponentMap = new HashMap<>();
    /* Map to match the System.identityHashCode of each VComponent with a List of Appointments it represents */
    private final Map<Integer, List<Appointment>> vComponentAppointmentMap = new HashMap<>();
    /* When a new appointment is drawn, it's added to this map to indicate SEQUENCE shouldn't be incremented
     * when editAppointmentCallback is used */
    private final Map<Appointment, Boolean> newAppointmentMap = new HashMap<>();

    /** used by default {@link #selectedOneAppointmentCallback} */
    private Alert lastOneAppointmentSelectedAlert;
    /*
     * Select One Appointment Callback
     * 
     * Callback that is executed only one appointment is selected in Agenda.  This is done by primary clicking
     * on one appointment.  Pressing and holding Ctrl and clicking other appointments does not trigger this callback.
     * 
     * The callback is initialized to a provide a simple Dialog that prompts the user to edit, delete or cancel action.
     */
    private Callback<Appointment, Void> selectedOneAppointmentCallback = (Appointment appointment) ->
    {
        OneAppointmentSelectedAlert alert = new OneAppointmentSelectedAlert(appointment, Settings.resources);

        alert.initOwner(this.getScene().getWindow());
        Pane bodyPane = (Pane) ((AgendaSkin) getSkin()).getNodeForPopup(appointment);
        double x = NodeUtil.screenX(bodyPane) + bodyPane.getWidth()/2;
		alert.setX(x);
        double y = NodeUtil.screenY(bodyPane) + bodyPane.getHeight()/2;
		alert.setY(y);
        // Check if previous alert so it can be closed (like autoHide for popups)
        if (lastOneAppointmentSelectedAlert != null)
        {
            lastOneAppointmentSelectedAlert.close();
        }
        lastOneAppointmentSelectedAlert = alert; // save for next time

        alert.resultProperty().addListener((obs, oldValue, newValue) -> 
        {
            if (newValue != null)
            {
                lastOneAppointmentSelectedAlert = null;
                String buttonText = newValue.getText();
                if (buttonText.equals(Settings.resources.getString("edit")))
                {
                    getEditAppointmentCallback().call(appointment);
                } else if (buttonText.equals(Settings.resources.getString("delete")))
                {
                    VDisplayable<?> vComponent = appointmentVComponentMap.get(System.identityHashCode(appointment));
                    VDisplayable<?> vComponentCopy = null;
                    try
                    {
                        vComponentCopy = vComponent.getClass().newInstance();
                        vComponent.copyChildrenInto(vComponentCopy);
                    } catch (InstantiationException | IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                    List<VCalendar> cancelMessage = SimpleDeleterFactory.newDeleter(
                            vComponentCopy,
                            DeleteChoiceDialog.DELETE_DIALOG_CALLBACK,
                            appointment.getStartTemporal()
                            ).delete();
                    getVCalendar().processITIPMessage(cancelMessage);
                    calendarConsumer.accept(getVCalendar()); // provide notification of calendar change
                    refresh();
                }
            }
        });
        
        alert.show(); // NOTE: alert.showAndWait() doesn't work - results in a blank dialog panel for 2nd Alert and beyond
        return null;
    };
    /** Sets the value of the select one appointment callback.  The callback is executed only one appointment
     * is selected in Agenda.  This is done by primary clicking on one appointment.  Pressing and holding Ctrl
     * and clicking other appointments does not trigger this callback. */
    public void setSelectedOneAppointmentCallback(Callback<Appointment, Void> c) { selectedOneAppointmentCallback = c; }
    /** Gets the value of the select one appointment callback.  The callback is executed only one appointment
     * is selected in Agenda.  This is done by primary clicking on one appointment.  Pressing and holding Ctrl
     * and clicking other appointments does not trigger this callback. */
    public Callback<Appointment, Void> getSelectedOneAppointmentCallback() { return selectedOneAppointmentCallback; }

    /*
     * New Appointment Callback
     * 
     * Callback that is executed after a new appointment is added in Agenda.  This is done by clicking
     * on the start time, dragging, and releasing on the end time.
     * 
     * The callback is initialized to a provide a simple Dialog that allows modification a few properties and
     * buttons to do an advanced edit, cancel or create event.
     * 
     * This callback is called in the appointmentsListChangeListener.
     */
    private Callback<Appointment, ButtonData> newAppointmentDrawnCallback = (Appointment appointment) ->
    {
        Dialog<ButtonData> newAppointmentDialog = new NewAppointmentDialog(appointment, appointmentGroups(), Settings.resources);
        Pane bodyPane = (Pane) ((AgendaSkin) getSkin()).getNodeForPopup(appointment);
        double x = NodeUtil.screenX(bodyPane) + bodyPane.getPrefWidth()/2;
        newAppointmentDialog.setX(x);
        double y = NodeUtil.screenY(bodyPane);
        newAppointmentDialog.setY(y);
        newAppointmentDialog.getDialogPane().getStylesheets().add(getUserAgentStylesheet());
        Optional<ButtonData> result = newAppointmentDialog.showAndWait();
        ButtonData button = result.isPresent() ? result.get() : ButtonData.CANCEL_CLOSE;
        return button;
    };
    // TODO - SHOULD CALLBACK BE WRAPPED IN AN OBJECT PROPERTY?
    /** Sets the value of the new appointment callback.  The callback is executed after a new appointment is
     * added in Agenda.  This is done by clicking on the start time, dragging, and releasing on the end time. */
    public Callback<Appointment, ButtonData> getNewAppointmentDrawnCallback() { return newAppointmentDrawnCallback; }
    /** Gets the value of the new appointment callback.  The callback is executed after a new appointment is
     * added in Agenda.  This is done by clicking on the start time, dragging, and releasing on the end time. */
    public void setNewAppointmentDrawnCallback(Callback<Appointment, ButtonData> c) { newAppointmentDrawnCallback = c; }
    
    private ListChangeListener<Appointment> appointmentsListChangeListener;
    /*
     * CONSTRUCTOR
     */
    public ICalendarAgenda()
    {
        this(new VCalendar()); // create empty VCalendar
    }
    
    public ICalendarAgenda(VCalendar vCalendar)
    {
        super();
        this.vCalendar = vCalendar;

        // Default recurrence factory
        recurrenceFactory = new DefaultRecurrenceFactory(appointmentGroups());
        // Default VComponent factory
        vComponentFactory = new DefaultVComponentFactory(getOrganizer(), getUidGeneratorCallback());
        
        // setup i18n resource bundle
        Locale myLocale = Locale.getDefault();
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.ICalendarAgenda", myLocale);
        Settings.setup(resources);
        
        /*
         * Default New Appointment Callback
         * 
         *  Default callback to accept new drawn appointments.
         *  Note: If a different Appointment implementation is used then custom recurrenceFactory, 
         *  vComponentFactory and a custom newAppointmentCallback is required
         */
        setNewAppointmentCallback((LocalDateTimeRange dateTimeRange) -> 
        {
            Temporal s = dateTimeRange.getStartLocalDateTime().atZone(ZoneId.systemDefault());
            Temporal e = dateTimeRange.getEndLocalDateTime().atZone(ZoneId.systemDefault());
            return new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(s)
                    .withEndTemporal(e)
                    .withSummary(resources.getString("new"))
//                    .withDescription("")
                    .withAppointmentGroup(appointmentGroups().get(0));
        });
        
        /*
         * Appointment Changed Callback (change from default in Agenda)
         * 
         * Callback that is executed after an appointment is changed in Agenda.  This is done by drag-n-drop,
         * expand end time, and other user actions.
         * 
         * Below default provides a simple changed appointment callback (handles drag-n-drop and expand end time)
         * allows dialog to prompt user for change to all, this-and-future or all for repeating VComponents
         */
        Callback<Appointment, Void> appointmentChangedCallback = (Appointment appointment) ->
        {
            VDisplayable<?> vComponent = appointmentVComponentMap.get(System.identityHashCode(appointment));
            Object[] params = revisorParamGenerator(vComponent, appointment);
            List<VCalendar> iTIPMessage = SimpleRevisorFactory.newReviser(vComponent, params).revise();
            getVCalendar().processITIPMessage(iTIPMessage);
            calendarConsumer.accept(getVCalendar()); // provide notification of calendar change
            appointmentStartOriginalMap.put(System.identityHashCode(appointment), appointment.getStartTemporal()); // update start map
            Platform.runLater(() -> refresh());
            return null;
        };
        setAppointmentChangedCallback(appointmentChangedCallback);
        
        /*
         * Default Edit Appointment Callback 
         * 
         * This callback replaces the default popup in Agenda.
         * It displays a scene with a control that can edit a number of descriptive properties
         * and a recurrence rule.  See this comments at the top of this class for more details.
         */
        Callback<Appointment, Void> editAppointmentCallback = (Appointment appointment) ->
        {
            VDisplayable<?> vComponent = appointmentVComponentMap.get(System.identityHashCode(appointment));
            if (vComponent == null)
            { // having no VComponent in map means the appointment is new and VComponent must be created
                vComponent = getVComponentFactory().createVComponent(appointment);
                // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
                // Throwing an exception will leave the mouse unresponsive.
            }
                // make popup stage
                Stage popupStage = new Stage();
                String appointmentTime = AgendaDateTimeUtilities.formatRange(appointment.getStartTemporal(), appointment.getEndTemporal());
                popupStage.setTitle(vComponent.getSummary().getValue() + ":" + appointmentTime);

                Object[] params = new Object[] {
                        getVCalendar(),
                        appointment.getStartTemporal(),
                        appointment.getEndTemporal(),
                        getCategories()
                        };
                EditDisplayableScene popupScene = SimpleEditSceneFactory.newScene(vComponent, params);
                popupScene.getStylesheets().addAll(getUserAgentStylesheet(), ICalendarAgenda.ICALENDAR_STYLE_SHEET);
                popupStage.setScene(popupScene);
                
                /* POSITION POPUP
                 * Position popup to left or right of bodyPane, where there is room.
                 * Note: assumes the control is displayed at its preferred height and width */
                Pane bodyPane = (Pane) ((AgendaSkin) getSkin()).getNodeForPopup(appointment);
                double prefHeightControl = ((Control) popupStage.getScene().getRoot()).getPrefHeight();
                double prefWidthControl = ((Control) popupStage.getScene().getRoot()).getPrefWidth();
                double xLeft = NodeUtil.screenX(bodyPane) - prefWidthControl - 5;
                double xRight = NodeUtil.screenX(bodyPane) + bodyPane.getWidth() + 5;
                double x = (xLeft > 0) ? xLeft : xRight;
                double y = NodeUtil.screenY(bodyPane) - prefHeightControl/2;
                popupStage.setX(x);
                popupStage.setY(y);
                popupStage.show();
                
                /* Check if Appointment is new
                 * Add listener to newVComponentsProperty to get resulting VComponents
                 * Remove SEQUENCE if Appointment is new
                 */
                popupScene.getEditDisplayableTabPane().iTIPMessagesProperty().addListener((obs, oldValue, newValue) ->
                {
                    newValue.forEach(message -> getVCalendar().processITIPMessage(message));
                    calendarConsumer.accept(getVCalendar()); // provide notification of calendar change
                    popupStage.hide();
                    refresh();
                });
            return null;
        };
        setEditAppointmentCallback(editAppointmentCallback);
        
        // Default Categories from appointment groups
        categories = FXCollections.observableArrayList(
                appointmentGroups().stream()
                    .map(a -> a.getDescription())
                    .collect(Collectors.toList())
                    );
        
        // Listener to update appointmentGroup descriptions with categories change
        getCategories().addListener((ListChangeListener.Change<? extends String> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList().forEach(c ->
                    {
                        int index = change.getList().indexOf(c);
                        appointmentGroups().get(index).setDescription(c);
                    });
                }
            }
        });

        /*
         * Appointment List Change Listener
         * 
         * Handles making new VComponents from drawn Appointments.
         */
        appointmentsListChangeListener = (ListChangeListener.Change<? extends Appointment> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    boolean isOneAppointmentAdded = change.getAddedSubList().size() == 1;
                    if (isOneAppointmentAdded)
                    {
                        Appointment appointment = change.getAddedSubList().get(0);
                        ButtonData button = newAppointmentDrawnCallback.call(change.getAddedSubList().get(0));
                        switch (button)
                        {
                        case CANCEL_CLOSE:
                        	appointments().remove(appointment);
                        	((AgendaSkin) getSkin()).setupAppointments();
                            break;
                        case OK_DONE: // Create VComponent
                            {
                                VComponent newVComponent = getVComponentFactory().createVComponent(appointment);
                                VCalendar message = Reviser.emptyPublishiTIPMessage();
                                message.addChild(newVComponent);
                                getVCalendar().processITIPMessage(message);
                                calendarConsumer.accept(getVCalendar()); // provide notification of calendar change
//                                System.out.println("create vcomponent");
                                List<VChild> calendarChildren = vCalendar.childrenUnmodifiable();
                                VDisplayable<?> v = (VDisplayable<?>) calendarChildren.get(calendarChildren.size()-1); // get last child
                                vComponentAppointmentMap.put(System.identityHashCode(v), new ArrayList<>(Arrays.asList(appointment)));
                                appointmentVComponentMap.put(System.identityHashCode(appointment), v);
                                appointmentStartOriginalMap.put(System.identityHashCode(appointment), appointment.getStartTemporal());
                                Platform.runLater(() -> ((AgendaSkin) getSkin()).setupAppointments());
                                break;
                            }
                        case OTHER: // Advanced Edit
                            {
                                editAppointmentCallback.call(appointment);
                                break;
                            }
                        default:
                            throw new RuntimeException("unknown button type:" + button);
                        }
                    } else
                    {
                        throw new RuntimeException("Adding multiple appointments at once is not supported (" + change.getAddedSubList().size() + ")");
                    }
                } else if (change.wasRemoved())
                {
                    change.getRemoved().forEach(appointment ->
                    {
                    	// get appointment's vComponent and update maps
                        VDisplayable<?> vComponent = appointmentVComponentMap.remove(System.identityHashCode(appointment));
                        List<Appointment> mappedAppointments = vComponentAppointmentMap.get(System.identityHashCode(vComponent));
                    	mappedAppointments.remove(appointment);
                        // make copy for deleter
                        VDisplayable<?> vComponentCopy = null;
                        try
                        {
                            vComponentCopy = vComponent.getClass().newInstance();
                            vComponent.copyChildrenInto(vComponentCopy);
                        } catch (InstantiationException | IllegalAccessException e)
                        {
                            e.printStackTrace();
                        }
                        List<VCalendar> cancelMessage = SimpleDeleterFactory.newDeleter(
                                vComponentCopy,
                                choices -> ChangeDialogOption.ONE,
                                appointment.getStartTemporal()
                                ).delete();
                        getVCalendar().processITIPMessage(cancelMessage);
                        calendarConsumer.accept(getVCalendar()); // provide notification of calendar change
                    });
                }
            }
        };
        appointments().addListener(appointmentsListChangeListener);
        
        /*
         * Listener to delete selected appointments when delete key is pressed
         */
        sceneProperty().addListener((obs, oldValue, newValue) ->
        {
            if (newValue != null)
            {
                getScene().setOnKeyPressed((event) ->
                {
                    if (event.getCode().equals(KeyCode.DELETE) && (! selectedAppointments().isEmpty()))
                    {
                        appointments().removeAll(selectedAppointments());
                    }
                });
            }
        });

        /*
         * Select One Appointment List Change Listener
         * 
         * Handles calling selectedOneAppointmentCallback when only one appointment is selected
         */
        ListChangeListener<Appointment> selectOneAppointmentListener = (ListChangeListener.Change<? extends Appointment> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded() && (selectedAppointments().size() == 1))
                {
                    Appointment appointment = selectedAppointments().get(0);
                    selectedOneAppointmentCallback.call(appointment);
                }
            }
        };
        selectedAppointments().addListener(selectOneAppointmentListener);

        // LISTEN FOR AGENDA RANGE CHANGES
        /*
         * Agenda LocalDateTimeRange Callback
         * 
         * This callback is executed when the LocalDateTime range of Agenda changes.  It clears
         * the appointments list, re-creates the appointments for all the VComponents, and then adds them back
         * to the appointments list.
         */
        setLocalDateTimeRangeCallback(dateTimeRange ->
        {
            getRecurrenceFactory().setStartRange(dateTimeRange.getStartLocalDateTime());
            getRecurrenceFactory().setEndRange(dateTimeRange.getEndLocalDateTime());
            if (dateTimeRange != null)
            {
            	updateAppointments();
            }
            return null; // return argument for the Callback
        });
    } // end of constructor
    
    /** Generate the parameters required for {@link SimpleRevisorFactory} */
    private Object[] revisorParamGenerator(VDisplayable<?> vComponent, Appointment appointment)
    {
        if (vComponent == null)
        {
            // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
            // Throwing an exception will leave the mouse unresponsive.
            System.out.println("ERROR: no component found - popup can'b be displayed");
            return null;
        } else
        {
            VComponent vComponentCopy = null;
            try
            {
                vComponentCopy = vComponent.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
            vComponent.copyChildrenInto(vComponentCopy);
            vComponentCopy.setParent(vComponent.getParent());
            Temporal startOriginalRecurrence = appointmentStartOriginalMap.get(System.identityHashCode(appointment));
            final Temporal startRecurrence;
            final Temporal endRecurrence;

            boolean wasDateType = DateTimeType.of(startOriginalRecurrence).equals(DateTimeType.DATE);
            boolean isNotDateType = ! DateTimeType.of(appointment.getStartTemporal()).equals(DateTimeType.DATE);
            boolean isChangedToTimeBased = wasDateType && isNotDateType;
            boolean isChangedToWholeDay = appointment.isWholeDay() && isNotDateType;
            if (isChangedToTimeBased)
            {
                startRecurrence = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getStartTemporal(), ZoneId.systemDefault());
                endRecurrence = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getEndTemporal(), ZoneId.systemDefault());
            } else if (isChangedToWholeDay)
            {
                startRecurrence = LocalDate.from(appointment.getStartTemporal());
                Temporal endInstanceTemp = LocalDate.from(appointment.getEndTemporal());
                endRecurrence = (endInstanceTemp.equals(startRecurrence)) ? endInstanceTemp.plus(1, ChronoUnit.DAYS) : endInstanceTemp; // make period between start and end at least one day
            } else
            {
                startRecurrence = appointment.getStartTemporal();
                endRecurrence = appointment.getEndTemporal();            
            }
            // select editDialogCallback depending if there are special recurrence children
            Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> editDialogCallback = (vComponent.recurrenceChildren().isEmpty()) ? EditChoiceDialog.EDIT_DIALOG_CALLBACK : EditWithRecurrencesChoiceDialog.EDIT_DIALOG_CALLBACK;
            return new Object[] {
                    editDialogCallback,
                    endRecurrence,
                    startOriginalRecurrence,
                    startRecurrence,
                    vComponentCopy, // edited
                    vComponent
                 // Note: edited and original are the same here - can't edit descriptive properties with drag-n-drop
                    };
        }
    }
    
    /* Make Appointments by calling the RecurrenceFactory */
    private Collection<Appointment> makeAppointments(VDisplayable<?> v)
    {
        List<Appointment> myAppointments = getRecurrenceFactory().makeRecurrences(v);
        myAppointments.forEach(a -> 
        {
            appointmentVComponentMap.put(System.identityHashCode(a), v);
            appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartTemporal());
        });
        vComponentAppointmentMap.put(System.identityHashCode(v), myAppointments);
        return myAppointments;
    }
    
//    @Override
//	public void refresh()
//    {
//    	System.out.println("refresh");
//    	super.refresh()
//    }
    
    /** Clear and make new appointments for all displayable VComponents */
    public void updateAppointments()
    {
        List<Appointment> newAppointments = new ArrayList<>();
        appointments().removeListener(appointmentsListChangeListener);
        appointments().clear();
        vComponentAppointmentMap.clear();
        appointmentStartOriginalMap.clear();
        appointmentVComponentMap.clear();
        if (getVCalendar().getVEvents() != null)
        {
        	getVCalendar().getVEvents().stream().forEach(v -> newAppointments.addAll(makeAppointments(v)));
        }
        if (getVCalendar().getVTodos() != null)
        {
        	getVCalendar().getVTodos().stream().forEach(v -> newAppointments.addAll(makeAppointments(v)));
        }
        if (getVCalendar().getVJournals() != null)
        {
        	getVCalendar().getVJournals().stream().forEach(v -> newAppointments.addAll(makeAppointments(v)));
        }
        appointments().addAll(newAppointments);
        appointments().addListener(appointmentsListChangeListener); // add back appointmentListener
    }
}