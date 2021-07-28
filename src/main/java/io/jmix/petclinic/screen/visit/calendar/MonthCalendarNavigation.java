package io.jmix.petclinic.screen.visit.calendar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static io.jmix.petclinic.screen.visit.calendar.MonthFormatter.fullMonthYear;
import static io.jmix.petclinic.screen.visit.calendar.RelativeDates.beginningOfMonth;
import static io.jmix.petclinic.screen.visit.calendar.RelativeDates.endOfMonth;


public class MonthCalendarNavigation implements CalendarNavigation {

    private final Locale locale;
    private final CalendarScreenAdjustment screenAdjustment;

    public MonthCalendarNavigation(
            CalendarScreenAdjustment screenAdjustment,
            Locale locale
    ) {
        this.screenAdjustment = screenAdjustment;
        this.locale = locale;
    }

    @Override
    public void navigate(CalendarNavigationMode navigationMode, LocalDate referenceDate) {
        LocalDate newMonthDate = navigationMode.calculate(ChronoUnit.MONTHS, referenceDate);
        YearMonth newMonth = YearMonth.from(newMonthDate);

        screenAdjustment.adjust(
                beginningOfMonth(newMonth),
                endOfMonth(newMonth),
                newMonthDate,
                fullMonthYear(newMonth, locale)
        );

    }
}
