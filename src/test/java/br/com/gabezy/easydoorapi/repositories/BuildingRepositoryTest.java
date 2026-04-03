package br.com.gabezy.easydoorapi.repositories;

import br.com.gabezy.easydoorapi.domain.building.entities.Building;
import br.com.gabezy.easydoorapi.domain.building.entities.Locker;
import br.com.gabezy.easydoorapi.domain.building.repositories.BuildingRepository;
import br.com.gabezy.easydoorapi.domain.building.repositories.LockerRepository;
import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import br.com.gabezy.easydoorapi.domain.shared.entities.GeographicalCoordinates;
import br.com.gabezy.easydoorapi.resources.dto.building.FilterBuildingDTO;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class BuildingRepositoryTest {

    @Inject
    BuildingRepository buildingRepository;

    @Inject
    LockerRepository lockerRepository;

    @Test
    @TestTransaction
    public void shouldCreateBuilding() {
        var locker = new Locker("teste22", "locker 1", new GeographicalCoordinates(BigDecimal.valueOf(-23.55052), BigDecimal.valueOf(-46.633308)));
        lockerRepository.persist(locker);

        var address = new Address(
                "Rua Teste, 123",
                "São Paulo",
                "SP",
                "Brasil",
                "01234-567"
        );

        var building = new Building();
        building.lockerId = locker.id;
        building.name = "building 1";
        building.coordinates = new GeographicalCoordinates(
                BigDecimal.valueOf(-23.55052),
                BigDecimal.valueOf(-46.633308)
        );
        building.description = "building 1 description";
        building.area = 100D;
        building.address = address;

        buildingRepository.persist(building);

        assertNotNull(building.id);
        assertEquals(locker.id, building.lockerId);
        assertEquals("building 1", building.name);
        assertNotNull(building.coordinates);
        assertEquals(BigDecimal.valueOf(-23.55052), building.coordinates.latitude);
        assertEquals(BigDecimal.valueOf(-46.633308), building.coordinates.longitude);
    }

    @Test
    @TestTransaction
    public void shouldNotCreateInvalidBuilding() {
        var locker = new Locker("dsada", "locker 1", new GeographicalCoordinates(BigDecimal.valueOf(-23.55052), BigDecimal.valueOf(-46.633308)));
        lockerRepository.persist(locker);

        var building = new Building();
        building.lockerId = locker.id;
        building.name = "building 1";
        building.description = "building 1 description";
        building.area = 100D;

        assertThrows(ConstraintViolationException.class, () -> {
            buildingRepository.persist(building);
            buildingRepository.flush();
        });

        building.coordinates = new GeographicalCoordinates(
                BigDecimal.valueOf(-23.55052),
                BigDecimal.valueOf(-46.633308)
        );

        building.address = null;
        assertThrows(ConstraintViolationException.class, () -> {
            buildingRepository.persist(building);
            buildingRepository.flush();
        });

        building.address = new Address(
                "Rua Teste, 123",
                "São Paulo",
                "SP",
                "Brasil",
                "01234-567"
        );

        building.locker = null;
        assertThrows(ConstraintViolationException.class, () -> {
            buildingRepository.persist(building);
            buildingRepository.flush();
        });
    }

    @Test
    @TestTransaction
    public void shouldFindBuildingByLockerId() {
        var locker = new Locker("dsada", "locker 1", new GeographicalCoordinates(BigDecimal.valueOf(-23.55052), BigDecimal.valueOf(-46.633308)));
        lockerRepository.persist(locker);

        var address = new Address(
                "Rua Teste, 123",
                "São Paulo",
                "SP",
                "Brasil",
                "01234-567"
        );

        var building = new Building();
        building.lockerId = locker.id;
        building.name = "building 1";
        building.coordinates = new GeographicalCoordinates(
                BigDecimal.valueOf(-23.55052),
                BigDecimal.valueOf(-46.633308)
        );
        building.description = "building 1 description";
        building.area = 100D;
        building.address = address;

        buildingRepository.persist(building);

        var optFoundBuilding = buildingRepository.findByLocker(locker.id);

        assertTrue(optFoundBuilding.isPresent());
        assertEquals(building.id, optFoundBuilding.get().id);
        assertEquals(locker.id, optFoundBuilding.get().lockerId);
    }

    @Test
    @TestTransaction
    public void shouldFindAllByFilter() {
        var locker = new Locker("teste12321", "locker 2", new GeographicalCoordinates(BigDecimal.valueOf(-23.55052), BigDecimal.valueOf(-46.633308)));
        lockerRepository.persist(locker);

        var address = new Address(
                "Rua Teste, 123",
                "São Paulo",
                "SP",
                "Brasil",
                "01234-567"
        );

        var building = new Building();
        building.lockerId = locker.id;
        building.name = "building 1";
        building.coordinates = new GeographicalCoordinates(
                BigDecimal.valueOf(-23.55052),
                BigDecimal.valueOf(-46.633308)
        );
        building.description = "building 1 description";
        building.area = 100D;
        building.address = address;

        buildingRepository.persist(building);

        var locker2 = new Locker("teste123", "locker 2", new GeographicalCoordinates(BigDecimal.valueOf(-23.55052), BigDecimal.valueOf(-46.633308)));
        lockerRepository.persist(locker2);

        var building2 = new Building();
        building2.lockerId = locker2.id;
        building2.name = "building 2";
        building2.coordinates = new GeographicalCoordinates(
                BigDecimal.valueOf(-23.55052),
                BigDecimal.valueOf(-46.633308)
        );
        building2.description = "building 2 description";
        building2.area = 200D;
        building2.address = address;
        buildingRepository.persist(building2);

        var filter = new FilterBuildingDTO(null, "description", null, null);

        var foundBuildings = buildingRepository.findAllByFilter(filter);

        assertEquals(2, foundBuildings.size());
        assertEquals(building.id, foundBuildings.getFirst().id);
        assertEquals(locker.id, foundBuildings.getFirst().lockerId);
        assertEquals(building2.id, foundBuildings.getLast().id);
        assertEquals(locker2.id, foundBuildings.getLast().lockerId);
    }

}
