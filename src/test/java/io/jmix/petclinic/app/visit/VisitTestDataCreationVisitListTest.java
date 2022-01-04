package io.jmix.petclinic.app.visit;

import io.jmix.core.DataManager;
import io.jmix.core.FluentLoader;
import io.jmix.core.TimeSource;
import io.jmix.petclinic.app.EmployeeRepository;
import io.jmix.petclinic.entity.User;
import io.jmix.petclinic.entity.pet.Pet;
import io.jmix.petclinic.entity.visit.Visit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.List.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitTestDataCreationVisitListTest {

    /**
     * NOW is freezed to some wednesday in order to not accidentally hit the weekend limitation
     * of the visit test data generation
     */
    private static final LocalDate TODAY = LocalDate.now().with(DayOfWeek.WEDNESDAY);
    private static final LocalDate TOMORROW = TODAY.plusDays(1);
    private static final LocalDateTime TOMORROW_MORNING = TOMORROW.atStartOfDay();
    private static final ZonedDateTime NOW = TODAY.atStartOfDay(ZoneId.of("Europe/Berlin")).plusHours(8);

    VisitTestDataCreation visitTestDataCreation;

    @Mock
    PetclinicTestdataProperties petclinicTestdataProperties;
    @Mock
    TimeSource timeSource;
    @Mock
    DataManager dataManager;
    @Mock
    EmployeeRepository employeeRepository;

    PetclinicData data;


    @BeforeEach
    void setup() {

        when(dataManager.create(Visit.class))
                .then(invocation -> new Visit());

        visitTestDataCreation = new VisitTestDataCreation(
                petclinicTestdataProperties,
                timeSource,
                dataManager,
                new RandomVisitDateTime(),
                employeeRepository
        );

        when(timeSource.now())
                .thenReturn(NOW);

        data = new PetclinicData();
    }

    @Test
    void given_noDaysInFutureShouldBeGenerated_when_generateVisits_then_noVisitIsInFuture() {

        // given:
        daysInPastToGenerateFor(10);
        daysInFutureToGenerateFor(0);
        visitAmountPerDay(10);

        possibleDescriptions(of("Fever", "Disease"));

        possiblePets(
                data.pet("Pikachu"),
                data.pet("Garchomp")
        );

        possibleNurses(of(data.nurse("Joy")));

        // when:
        List<Visit> visits = visitTestDataCreation.createVisits();

        // then:
        assertThat(futureVisits(visits))
                .isEmpty();
    }

    @Test
    void given_daysInFutureShouldBeGenerated_when_generateVisits_then_amountOfVisitsIsDecreasingIntoTheFuture() {

        // given:
        daysInPastToGenerateFor(1);
        daysInFutureToGenerateFor(30);
        visitAmountPerDay(10);

        possibleDescriptions(of("Fever", "Disease"));

        possiblePets(
                data.pet("Pikachu"),
                data.pet("Garchomp")
        );

        possibleNurses(of(data.nurse("Joy")));

        // when:
        List<Visit> visits = visitTestDataCreation.createVisits();

        // then: the more far into the future, the less amount of visits are generated
        assertThat(amountOfVisitsForDate(visits, TOMORROW))
                .isLessThan(10);
        assertThat(amountOfVisitsForDate(visits, TOMORROW.plusDays(7)))
                .isLessThan(8);
        assertThat(amountOfVisitsForDate(visits, TOMORROW.plusDays(14)))
                .isLessThan(6);
        assertThat(amountOfVisitsForDate(visits, TOMORROW.plusDays(21)))
                .isLessThan(4);
    }


    @Test
    void given_10VisitsPerDays_when_generateVisits_then_sizeIs10() {

        // given:
        daysInPastToGenerateFor(1);
        daysInFutureToGenerateFor(0);
        visitAmountPerDay(10);

        possibleDescriptions(of("Fever", "Disease"));

        possiblePets(
                data.pet("Pikachu"),
                data.pet("Garchomp")
        );

        possibleNurses(of(data.nurse("Joy")));

        // when:
        List<Visit> visits = visitTestDataCreation.createVisits();

        // then:
        assertThat(visits.size())
                .isEqualTo(10);
    }

    private void possiblePets(Pet... pets) {
        FluentLoader<Pet> petLoaderMock = mock(FluentLoader.class);
        FluentLoader.ByCondition<Pet> allPets = mock(FluentLoader.ByCondition.class);

        when(dataManager.load(Pet.class))
                .thenReturn(petLoaderMock);

        when(petLoaderMock.all())
                .thenReturn(allPets);

        when(allPets.list())
                .thenReturn(asList(pets));
    }

    private int amountOfVisitsForDate(List<Visit> visits, LocalDate date) {
        return futureVisits(visits)
                .stream()
                .collect(Collectors.groupingBy(visit -> visit.getVisitStart().toLocalDate()))
                .get(date)
                .size();
    }

    private List<Visit> futureVisits(List<Visit> visits) {
        return visits.stream()
                .filter(visit -> visit.getVisitStart().isAfter(TOMORROW_MORNING))
                .collect(Collectors.toList());
    }

    private void possibleNurses(List<User> nurses) {
        when(employeeRepository.findAllNurses())
                .thenReturn(nurses);
    }

    private void daysInPastToGenerateFor(int days) {
        when(petclinicTestdataProperties.getVisitStartAmountPastDays())
                .thenReturn(days);
    }

    private void daysInFutureToGenerateFor(int days) {
        when(petclinicTestdataProperties.getVisitStartAmountFutureDays())
                .thenReturn(days);
    }

    private void visitAmountPerDay(int amount) {
        when(petclinicTestdataProperties.getAmountPerDay())
                .thenReturn(amount);
    }

    private void possibleDescriptions(List<String> descriptions) {
        lenient().when(petclinicTestdataProperties.getDescriptionOptions())
                .thenReturn(descriptions);
    }

}