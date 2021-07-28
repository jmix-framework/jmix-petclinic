package io.jmix.petclinic.app.visit;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class RandomVisitDateTimeTest {

    RandomVisitDateTime randomVisitDateTime;

    @BeforeEach
    void createTestEnvironment() {
        randomVisitDateTime = new RandomVisitDateTime();
    }

    @RepeatedTest(10)
    void aVisitEventRangeStartsAt7AMEarliest() {

        // when:
        final LocalDate date = aWeekday();
        VisitEventRange actual = randomVisitDateTime.randomVisitEventRange(date);

        // then:
        assertThat(actual.getVisitStart())
                .isAfterOrEqualTo(date.atTime(7,0));
    }


    @RepeatedTest(1000)
    void aVisitEventRangeEndsAt8PMLatest() {

        // when:
        final LocalDate date = aWeekday();
        VisitEventRange actual = randomVisitDateTime.randomVisitEventRange(date);

        // then:
        assertThat(actual.getVisitEnd())
                .isBefore(date.atTime(18,0));
    }

    @RepeatedTest(1000)
    void aVisitEventRangeIsMin15Minutes() {

        // when:
        VisitEventRange actual = randomVisitDateTime.randomVisitEventRange(aWeekday());

        // then:
        assertThat(actual.lengthInMinutes())
                .isGreaterThanOrEqualTo(15);
    }


    @RepeatedTest(1000)
    void aVisitEventRangeIsMax90Minutes() {

        // when:
        VisitEventRange actual = randomVisitDateTime.randomVisitEventRange(aWeekday());

        // then:
        assertThat(actual.lengthInMinutes())
                .isLessThanOrEqualTo(90);
    }

    @RepeatedTest(100)
    void aVisitEventStartsOnlyAtEveryFullQuarterHour() {

        // when:
        VisitEventRange actual = randomVisitDateTime.randomVisitEventRange(aWeekday());

        // then:
        assertThat(actual.getVisitStart().getMinute() % 15)
            .isZero();
    }

    @RepeatedTest(100)
    void aVisitEventIsNeverGeneratedOnASunday() {

        // when:
        VisitEventRange actual = randomVisitDateTime.randomVisitEventRange(aSunday());

        // then:
        assertThat(actual)
                .isEqualTo(VisitEventRange.empty());
    }


    @RepeatedTest(1000)
    void aVisitEventIsAlmostNeverGeneratedOnASaturday() {

        // when:
        List<Boolean> allResults = IntStream.range(0, 1000)
                .mapToObj(i -> randomVisitDateTime.randomVisitEventRange(aSaturday()))
                .map(VisitEventRange::isEmpty)
                .collect(Collectors.toList());

        long amountOfFilledSaturdays = allResults.stream()
                .filter(aBoolean -> !aBoolean)
                .count();

        // then:
        double filledToFreeRatio = (double) amountOfFilledSaturdays / (double) allResults.size();
        double ONE_IN_A_HUNDRED = 0.01;

        assertThat(filledToFreeRatio)
            .isCloseTo(ONE_IN_A_HUNDRED, within(0.05));
    }


    private LocalDate aWeekday() {
        return LocalDate.now().with(DayOfWeek.WEDNESDAY);
    }

    private LocalDate aSaturday() {
        return aWeekday().with(DayOfWeek.SATURDAY);
    }

    private LocalDate aSunday() {
        return aWeekday().with(DayOfWeek.SUNDAY);
    }

}