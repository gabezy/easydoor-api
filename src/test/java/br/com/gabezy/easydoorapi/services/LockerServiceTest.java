package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.building.entities.Locker;
import br.com.gabezy.easydoorapi.domain.building.repositories.LockerRepository;
import br.com.gabezy.easydoorapi.resources.dto.CoordinatesDTO;
import br.com.gabezy.easydoorapi.resources.dto.building.CreateLockerRequest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class LockerServiceTest {

    @Inject
    LockerService lockerService;

    @InjectMock
    LockerRepository lockerRepository;

    @BeforeEach
    public void setup() {
        Mockito.reset(lockerRepository);
    }

    @Test
    public void shouldCreateLocker() {
        var coordinates = new CoordinatesDTO(-15.776387735994753, -47.880859375);

        var request = new CreateLockerRequest(
                "Locker 1",
                "123456789",
                coordinates
        );

        Mockito.doAnswer(invocation -> {
            Locker locker = invocation.getArgument(0);
            locker.id = 1L;
            return null;
        }).when(lockerRepository).persist(Mockito.any(Locker.class));

        var locker = lockerService.create(request);

        assertNotNull(locker.id);
        assertEquals("Locker 1", locker.name);
        assertNotNull(locker.coordinates);
        assertEquals(coordinates.latitude(), locker.coordinates.latitude);
        assertEquals(coordinates.longitude(), locker.coordinates.longitude);

        Mockito.verify(lockerRepository, Mockito.times(1)).persist(Mockito.any(Locker.class));
    }

    @Test
    public void shouldValidateRequiredFields() {
        var request = new CreateLockerRequest(
                null,
                "123456789",
                new CoordinatesDTO(-15.776387735994753, -47.880859375)
        );

        assertThrows(ConstraintViolationException.class, () -> lockerService.create(request));

        var request2 = new CreateLockerRequest(
                "Locker 1",
                null,
                new CoordinatesDTO(-15.776387735994753, -47.880859375)
        );

        assertThrows(ConstraintViolationException.class, () -> lockerService.create(request2));

        var request3 = new CreateLockerRequest(
                "Locker 1",
                "123456789",
                null
        );

        assertThrows(ConstraintViolationException.class, () -> lockerService.create(request3));
    }

}
