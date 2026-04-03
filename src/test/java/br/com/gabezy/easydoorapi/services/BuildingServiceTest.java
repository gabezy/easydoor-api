package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.building.entities.Building;
import br.com.gabezy.easydoorapi.domain.building.repositories.BuildingRepository;
import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import br.com.gabezy.easydoorapi.domain.shared.entities.GeographicalCoordinates;
import br.com.gabezy.easydoorapi.resources.dto.AddressDTO;
import br.com.gabezy.easydoorapi.resources.dto.CoordinatesDTO;
import br.com.gabezy.easydoorapi.resources.dto.building.CreateBuildingRequest;
import br.com.gabezy.easydoorapi.resources.dto.building.FilterBuildingDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class BuildingServiceTest {

    @Inject
    BuildingService buildingService;

    @InjectMock
    BuildingRepository buildingRepository;

    @Test
    public void shouldCreateBuilding() {
        var coordinates = new CoordinatesDTO(
                -23.55052,
                -46.633308
        );

        var address = new AddressDTO(
                "Rua Teste, 123",
                "São Paulo",
                "SP",
                "Brasil",
                "01234-567"
        );


        var request = new CreateBuildingRequest(
                "Building 1",
                coordinates,
                100D,
                "some description",
                address,
                1L
        );

        Mockito.doAnswer(invocation -> {
            var persistedBuilding = invocation.getArgument(0, Building.class);
            persistedBuilding.id = 1L;
            return null;
        }).when(buildingRepository).persist(Mockito.any(Building.class));

        var building = buildingService.create(request);

        assertNotNull(building.id);
        assertEquals(1L, building.lockerId);
        assertEquals("Building 1", building.name);
        assertEquals(100D, building.area);
        assertEquals("some description", building.description);
        assertEquals(address, building.address);
        assertNotNull(building.coordinates);
        assertEquals(coordinates.latitude(), building.coordinates.latitude);
        assertEquals(coordinates.longitude(), building.coordinates.longitude);

        Mockito.verify(buildingRepository, Mockito.times(1)).persist(Mockito.any(Building.class));
    }

    @Test
    public void shouldValidateRequiredFields() {
        var request = new CreateBuildingRequest(
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThrows(ConstraintViolationException.class, () -> buildingService.create(request));
    }

    @Test
    public void shouldFindAllByFilter() {
        var filter = new FilterBuildingDTO("Building 1", null, null, null);

        var building1 = new Building();
        building1.name = "Building 1";
        building1.area = 100D;
        building1.description = "some description";
        building1.lockerId = 1L;
        building1.coordinates = new GeographicalCoordinates(BigDecimal.valueOf(-23.55052), BigDecimal.valueOf(-46.633308));
        building1.address = new Address(
                "Rua Teste, 123",
                "São Paulo",
                "SP",
                "Brasil",
                "01234-567"
        );

        var building2 = new Building();
        building2.name = "Building 2";
        building2.area = 200D;
        building2.description = "some description";
        building2.lockerId = 2L;
        building2.coordinates = new GeographicalCoordinates(BigDecimal.valueOf(-23.55052), BigDecimal.valueOf(-46.633308));
        building2.address = new Address(
                "Rua Teste, 456",
                "São Paulo",
                "SP",
                "Brasil",
                "01234-567"
        );

        Mockito.when(buildingRepository.findAllByFilter(filter))
                .thenReturn(List.of( building1, building2));

        var result = buildingService.findByFilter(filter);
        assertEquals(2, result.size());
        assertEquals("Building 1", result.getFirst().name);
        assertEquals("Building 2", result.getLast().name);
    }

}