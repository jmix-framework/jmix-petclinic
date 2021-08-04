package io.jmix.petclinic.screen.visit;

import com.vaadin.v7.shared.ui.calendar.CalendarState;
import io.jmix.core.Messages;
import io.jmix.core.TimeSource;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.petclinic.entity.visit.VisitType;
import io.jmix.petclinic.screen.visit.calendar.CalendarMode;
import io.jmix.petclinic.screen.visit.calendar.CalendarNavigationMode;
import io.jmix.petclinic.screen.visit.calendar.CalendarNavigators;
import io.jmix.petclinic.screen.visit.calendar.CalendarScreenAdjustment;
import io.jmix.petclinic.screen.visit.calendar.VisitTypeIcon;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Calendar;
import io.jmix.ui.component.CheckBoxGroup;
import io.jmix.ui.component.DatePicker;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.component.Label;
import io.jmix.ui.component.RadioButtonGroup;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.MessageBundle;
import io.jmix.ui.screen.OpenMode;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.StandardLookup;
import io.jmix.ui.screen.StandardOutcome;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static io.jmix.petclinic.screen.visit.calendar.CalendarNavigationMode.*;
import static io.jmix.petclinic.screen.visit.calendar.RelativeDates.startOfWeek;


@UiController("petclinic_Visit.browse")
@UiDescriptor("visit-browse.xml")
@LookupComponent("visitsTable")
@Route(value = "visits")
public class VisitBrowse extends StandardLookup<Visit> {


    @Autowired
    protected Calendar<LocalDateTime> calendar;
    @Autowired
    protected CollectionLoader<Visit> visitsCalendarDl;
    @Autowired
    protected ScreenBuilders screenBuilders;
    @Autowired
    protected CollectionContainer<Visit> visitsCalendarDc;
    @Autowired
    protected CollectionLoader<Visit> visitsDl;
    @Autowired
    protected DataContext dataContext;
    @Autowired
    protected CheckBoxGroup<VisitType> typeMultiFilter;
    @Autowired
    protected RadioButtonGroup<CalendarMode> calendarMode;
    @Autowired
    protected TimeSource timeSource;
    @Autowired
    protected Label<String> calendarTitle;
    @Autowired
    protected CalendarNavigators calendarNavigators;
    @Autowired
    protected DatePicker<LocalDate> calendarNavigator;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    protected DatatypeFormatter datatypeFormatter;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected MessageBundle messageBundle;
    @Autowired
    protected Messages messages;

    @Subscribe
    protected void onInit(InitEvent event) {
        initTypeFilter();
        initSortCalendarEventsInMonthlyView();
    }

    private void initTypeFilter() {
        typeMultiFilter.setOptionsEnum(VisitType.class);
        typeMultiFilter.setValue(EnumSet.allOf(VisitType.class));
        typeMultiFilter.setOptionIconProvider(o -> VisitTypeIcon.valueOf(o.getIcon()).source());
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        current(CalendarMode.WEEK);
    }

    @SuppressWarnings("deprecation")
    private void initSortCalendarEventsInMonthlyView() {
        calendar.unwrap(com.vaadin.v7.ui.Calendar.class)
                .setEventSortOrder(CalendarState.EventSortOrder.START_DATE_DESC);
    }


    @Subscribe("calendar")
    protected void onCalendarCalendarDayClick(Calendar.CalendarDateClickEvent<LocalDateTime> event) {
        atDate(
                CalendarMode.DAY,
                event.getDate().toLocalDate()
        );
    }

    @Subscribe("calendar")
    protected void onCalendarCalendarWeekClick(Calendar.CalendarWeekClickEvent<LocalDateTime> event) {
        atDate(
                CalendarMode.WEEK,
                startOfWeek(
                        event.getYear(),
                        event.getWeek(),
                        currentAuthentication.getLocale()
                )
        );
    }

    @Subscribe("navigatorPrevious")
    protected void onNavigatorPreviousClick(Button.ClickEvent event) {
        previous(calendarMode.getValue());
    }

    @Subscribe("navigatorNext")
    protected void onNavigatorNextClick(Button.ClickEvent event) {
        next(calendarMode.getValue());
    }

    @Subscribe("navigatorCurrent")
    protected void onNavigatorCurrentClick(Button.ClickEvent event) {
        current(calendarMode.getValue());
    }

    @Subscribe("calendarNavigator")
    protected void onCalendarRangePickerValueChange(HasValue.ValueChangeEvent<LocalDate> event) {
        if (event.isUserOriginated()) {
            atDate(calendarMode.getValue(), event.getValue());
        }
    }

    private void current(CalendarMode calendarMode) {
        change(calendarMode, AT_DATE, timeSource.now().toLocalDate());
    }

    private void atDate(CalendarMode calendarMode, LocalDate date) {
        change(calendarMode, AT_DATE, date);
    }

    private void next(CalendarMode calendarMode) {
        change(calendarMode, NEXT, calendarNavigator.getValue());
    }

    private void previous(CalendarMode calendarMode) {
        change(calendarMode, PREVIOUS, calendarNavigator.getValue());
    }

    private void change(CalendarMode calendarMode, CalendarNavigationMode navigationMode, LocalDate referenceDate) {
        this.calendarMode.setValue(calendarMode);

        calendarNavigators
                .forMode(
                        CalendarScreenAdjustment.of(calendar, calendarNavigator, calendarTitle),
                        datatypeFormatter,
                        calendarMode
                )
                .navigate(navigationMode, referenceDate);

        loadEvents();
    }


    @Subscribe("calendarMode")
    protected void onCalendarRangeValueChange(HasValue.ValueChangeEvent event) {
        if (event.isUserOriginated()) {
            atDate((CalendarMode) event.getValue(), calendarNavigator.getValue());
        }
    }

    private void loadEvents() {
        visitsCalendarDl.setParameter("visitStart", calendar.getStartDate());
        visitsCalendarDl.setParameter("visitEnd", calendar.getEndDate());
        visitsCalendarDl.load();
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Calendar Visit Event Click
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Subscribe("calendar")
    protected void onCalendarCalendarDayClick(Calendar.CalendarDayClickEvent<LocalDateTime> event) {
        Screen visitEditor = screenBuilders.editor(Visit.class, this)
                .newEntity()
                .withInitializer(visit -> {
                    visit.setVisitStart(event.getDate());
                    visit.setVisitEnd(event.getDate().plusHours(1));
                })
                .withOpenMode(OpenMode.DIALOG)
                .build();

        visitEditor.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                getScreenData().loadAll();
            }
        });

        visitEditor.show();
    }

    @Subscribe("calendar")
    protected void onCalendarCalendarEventClick(Calendar.CalendarEventClickEvent<LocalDateTime> event) {

        Screen visitEditor = screenBuilders.editor(Visit.class, this)
                .editEntity((Visit) event.getEntity())
                .withOpenMode(OpenMode.DIALOG)
                .build();

        visitEditor.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                getScreenData().loadAll();
            }
        });

        visitEditor.show();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Filter for Visit Types
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Subscribe("typeMultiFilter")
    protected void onTypeMultiFilterValueChange(HasValue.ValueChangeEvent event) {

        if (event.getValue() == null) {
            visitsCalendarDl.removeParameter("type");
        } else if (CollectionUtils.isEmpty((Set<VisitType>) event.getValue())) {
            visitsCalendarDl.setParameter("type", Collections.singleton(""));
        } else {
            visitsCalendarDl.setParameter("type", event.getValue());
        }
        loadEvents();
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Visit Changes through Calendar Event Adjustments
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Subscribe("calendar")
    protected void onCalendarCalendarEventResize(Calendar.CalendarEventResizeEvent<LocalDateTime> event) {
        updateVisit(event.getEntity(), event.getNewStart(), event.getNewEnd());
    }

    @Subscribe("calendar")
    protected void onCalendarCalendarEventMove(Calendar.CalendarEventMoveEvent<LocalDateTime> event) {
        updateVisit(event.getEntity(), event.getNewStart(), event.getNewEnd());
    }

    private void updateVisit(Object entity, LocalDateTime newStart, LocalDateTime newEnd) {
        Visit visit = (Visit) entity;
        visit.setVisitStart(newStart);
        visit.setVisitEnd(newEnd);
        dataContext.commit();
        notifications.create(Notifications.NotificationType.TRAY)
                .withCaption(
                        messageBundle.formatMessage(
                                "visitUpdated",
                                messages.getMessage(visit.getType()),
                                visit.getPetName()
                        )
                )
                .show();
    }
}