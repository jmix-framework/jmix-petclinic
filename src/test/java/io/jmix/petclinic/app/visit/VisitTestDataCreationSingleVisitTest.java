package io.jmix.petclinic.app.visit;

import io.jmix.core.DataManager;
import io.jmix.core.TimeSource;
import io.jmix.petclinic.app.EmployeeRepository;
import io.jmix.petclinic.entity.User;
import io.jmix.petclinic.entity.pet.Pet;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.petclinic.entity.visit.VisitTreatmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class VisitTestDataCreationSingleVisitTest {

    /**
     * NOW is freezed to some wednesday in order to not accidentally hit the weekend limitation
     * of the visit test data generation
     */
    private static final LocalDate TODAY = LocalDate.now().with(DayOfWeek.WEDNESDAY);
    private static final LocalDate TOMORROW = TODAY.plusDays(1);
    private static final LocalDate YESTERDAY = TODAY.minusDays(1);
    private static final ZonedDateTime NOW = TODAY.atStartOfDay(ZoneId.of("Europe/Berlin")).plusHours(8);

    VisitTestDataCreation visitTestDataCreation;

    @Mock
    PetclinicTestdataProperties petclinicTestdataProperties;
    @Mock
    TimeSource timeSource;
    @Mock
    DataManager dataManager;

    PetclinicData data;


    @BeforeEach
    void setup() {

        lenient().when(dataManager.create(Visit.class))
                .then(invocation -> {
                    Visit visit = new Visit();
                    visit.setId(UUID.randomUUID());
                    return visit;
                });
        lenient().when(timeSource.now())
                .thenReturn(NOW);

        visitTestDataCreation = new VisitTestDataCreation(
                petclinicTestdataProperties,
                timeSource,
                dataManager,
                new RandomVisitDateTime(),
                mock(EmployeeRepository.class)
        );

        data = new PetclinicData();
    }

    @Test
    void when_createVisit_thenVisitContainsRandomValuesForTypeAndPetAndDescriptionAndNurse() {

        // given:
        possibleDescriptions(List.of("Fever", "Disease"));

        List<Pet> possiblePets = asList(
                data.pet("Pikachu"),
                data.pet("Garchomp")
        );

        List<User> possibleNurses = asList(
                data.nurse("Joy"),
                data.nurse("Audino")
        );

        // when:
        Visit visit = visitTestDataCreation.createVisit(TOMORROW, possiblePets, possibleNurses);

        // then:
        assertThat(visit.getType())
                .isNotNull();
        assertThat(visit.getPet())
                .isIn(possiblePets);
        assertThat(visit.getAssignedNurse())
                .isIn(possibleNurses);

        assertThat(visit.getDescription())
                .isIn("Fever", "Disease");
    }


    @Test
    void when_createVisitInTheFutureMoreThenOneWeek_thenAssignedNurseIsNotSet() {

        // given:
        possibleDescriptions(List.of("Fever", "Disease"));

        // when:
        Visit visit = visitTestDataCreation.createVisit(
                TOMORROW.plusDays(7),
                asList(data.pet("Pikachu"), data.pet("Garchomp")),
                asList(data.nurse("Joy"), data.nurse("Audino"))
        );

        // then:
        assertThat(visit.getAssignedNurse())
                .isNull();
    }



    @Test
    void when_createVisitForYesterday_thenTreatmentStatusIsDone() {

        // given:
        somePossibleDescriptions();

        // when:
        Visit visit = visitTestDataCreation.createVisit(
                YESTERDAY,
                asList(data.pet("Pikachu")),
                asList(data.nurse("Joy"))
        );

        // then:
        assertThat(visit.getTreatmentStatus())
                .isEqualTo(VisitTreatmentStatus.DONE);
    }


    @Test
    void when_createVisitForToday_thenTreatmentStatusIsUpComing() {

        // given:
        somePossibleDescriptions();

        // when:
        Visit visit = visitTestDataCreation.createVisit(
                TODAY,
                asList(data.pet("Pikachu")),
                asList(data.nurse("Joy"))
        );

        // then:
        assertThat(visit.getTreatmentStatus())
                .isEqualTo(VisitTreatmentStatus.IN_PROGRESS);
    }

    @Test
    void when_createVisitForTomorrow_thenTreatmentStatusIsUpComing() {

        // given:
        somePossibleDescriptions();

        // when:
        Visit visit = visitTestDataCreation.createVisit(
                TOMORROW,
                asList(data.pet("Pikachu")),
                asList(data.nurse("Joy"))
        );

        // then:
        assertThat(visit.getTreatmentStatus())
                .isEqualTo(VisitTreatmentStatus.UPCOMING);
    }

    private void somePossibleDescriptions() {
        possibleDescriptions(List.of("Fever", "Disease"));
    }

    private void possibleDescriptions(List<String> descriptions) {
        lenient().when(petclinicTestdataProperties.getDescriptionOptions())
                .thenReturn(descriptions);
    }

}