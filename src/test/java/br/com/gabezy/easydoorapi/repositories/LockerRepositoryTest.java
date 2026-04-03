package br.com.gabezy.easydoorapi.repositories;

import br.com.gabezy.easydoorapi.domain.building.entities.Locker;
import br.com.gabezy.easydoorapi.domain.building.repositories.LockerRepository;
import br.com.gabezy.easydoorapi.domain.shared.entities.GeographicalCoordinates;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestTransaction
public class LockerRepositoryTest {

    @Inject
    LockerRepository lockerRepository;

    @Test
    public void shouldCreateLocker() {
        var serialNumber = UUID.randomUUID().toString();

        var locker = new Locker();
        locker.serialNumber = serialNumber;
        locker.name = "Locker 1";
        locker.coordinates = new GeographicalCoordinates(
                BigDecimal.valueOf(-15.776387735994753),
                BigDecimal.valueOf(-47.880859375)
        );

        lockerRepository.persist(locker);

        assertNotNull(locker.id);
        assertEquals(serialNumber, locker.serialNumber);
        assertEquals("Locker 1", locker.name);
        assertNotNull(locker.coordinates);
        assertEquals(BigDecimal.valueOf(-15.776387735994753), locker.coordinates.latitude);
        assertEquals(BigDecimal.valueOf(-47.880859375), locker.coordinates.longitude);
    }

    @Test
    public void shouldNotCreateLockerWithoutSerialNumber() {
        var locker = new Locker();
        locker.name = "Locker 1";
        locker.coordinates = new GeographicalCoordinates(
                BigDecimal.valueOf(-15.776387735994753),
                BigDecimal.valueOf(-47.880859375)
        );

        assertThrows(ConstraintViolationException.class, () -> {
            lockerRepository.persist(locker);
            lockerRepository.flush();
        });
    }

    @Test
    public void shouldNotCreateLockerWithSameSerialNumber() {
        var serialNumber = UUID.randomUUID().toString();

        var locker = new Locker();
        locker.serialNumber = serialNumber;
        locker.name = "Locker 1";
        locker.coordinates = new GeographicalCoordinates(
                BigDecimal.valueOf(-15.776387735994753),
                BigDecimal.valueOf(-47.880859375)
        );

        lockerRepository.persist(locker);

        var locker2 = new Locker();
        locker2.serialNumber = serialNumber;
        locker2.name = "Locker 2";
        locker2.coordinates = new GeographicalCoordinates(
                BigDecimal.valueOf(-15.776387735994753),
                BigDecimal.valueOf(-47.880859375)
        );

        assertThrows(ConstraintViolationException.class, () -> {
            lockerRepository.persist(locker2);
            lockerRepository.flush();
        });
    }

    @Test
    @TestTransaction
    public void shouldFindBySerialNumber() {
        var serialNumber = UUID.randomUUID().toString();
        var locker = new Locker();
        locker.serialNumber = serialNumber;
        locker.name = "Locker 1";
        locker.coordinates = new GeographicalCoordinates(
                BigDecimal.valueOf(-15.776387735994753),
                BigDecimal.valueOf(-47.880859375)
        );
        lockerRepository.persist(locker);

        var optLocker = lockerRepository.findBySerialNumber(serialNumber);
        assertTrue(optLocker.isPresent());

        var foundLocker = optLocker.get();

        assertEquals(serialNumber, foundLocker.serialNumber);
        assertEquals("Locker 1", foundLocker.name);
        assertNotNull(foundLocker.coordinates);
        assertEquals(BigDecimal.valueOf(-15.776387735994753), foundLocker.coordinates.latitude);
        assertEquals(BigDecimal.valueOf(-47.880859375), foundLocker.coordinates.longitude);
    }

    @Test
    @TestTransaction
    public void shouldNotFindBySerialNumber() {
        var optLocker = lockerRepository.findBySerialNumber("nonexistent-serial-number");
        assertTrue(optLocker.isEmpty());
    }

}