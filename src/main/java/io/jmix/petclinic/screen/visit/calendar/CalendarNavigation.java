package io.jmix.petclinic.screen.visit.calendar;

import java.time.LocalDate;

public interface CalendarNavigation {
    void navigate(CalendarNavigationMode navigationMode, LocalDate referenceDate);
}
