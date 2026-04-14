package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.domain.building.entities.Building;
import br.com.gabezy.easydoorapi.resources.dto.AddressDTO;
import br.com.gabezy.easydoorapi.resources.dto.CoordinatesDTO;
import br.com.gabezy.easydoorapi.resources.dto.building.CreateBuildingRequest;
import br.com.gabezy.easydoorapi.services.BuildingService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URL;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@QuarkusTest
@TestHTTPEndpoint(BuildingResource.class)
class BuildingResourceTest {

    @TestHTTPResource
    URL url;

    @InjectMock
    BuildingService buildingService;

    @Test
    @TestSecurity(user = "ADMIN", roles = {"ADMIN"})
    public void shouldCreateBuildingWithAdminRole() {
        var coordinates = new CoordinatesDTO(10.5, 20.5);
        var address = new AddressDTO("Rua Test", "São Paulo", "Apto 01", "Brasil", "12345-678");
        var request = new CreateBuildingRequest(
                "Building Test",
                coordinates,
                500.0,
                "Test description",
                address,
                1L
        );

        var newBuilding = new Building();
        newBuilding.id = 1L;
        newBuilding.name = "Building Test";

        Mockito.when(buildingService.create(Mockito.any())).thenReturn(newBuilding);

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(201)
            .header("Location", Matchers.matchesPattern(url.toString() + "/\\d+"));

        Mockito.verify(buildingService, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    @TestSecurity(user = "AGENT", roles = {"CREATE_BUILDING"})
    public void shouldCreateBuildingWithCreateBuildingPermission() {
        var coordinates = new CoordinatesDTO(10.5, 20.5);
        var address = new AddressDTO("Rua Test", "São Paulo", "Apto 01", "Brasil", "12345-678");
        var request = new CreateBuildingRequest(
                "Building Test",
                coordinates,
                500.0,
                "Test description",
                address,
                1L
        );

        var newBuilding = new Building();
        newBuilding.id = 1L;
        newBuilding.name = "Building Test";

        Mockito.when(buildingService.create(Mockito.any())).thenReturn(newBuilding);

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(201)
            .header("Location", Matchers.matchesPattern(url.toString() + "/\\d+"));

        Mockito.verify(buildingService, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    @TestSecurity(user = "ADMIN", roles = {"CREATE_BUILDING"})
    public void shouldGetAllBuildingsWithAuthentication() {
        var building1 = new Building();
        building1.id = 1L;
        building1.name = "Building 1";

        var building2 = new Building();
        building2.id = 2L;
        building2.name = "Building 2";

        Mockito.when(buildingService.findByFilter(Mockito.any()))
                .thenReturn(java.util.List.of(building1, building2));

        when()
            .get()
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("size()", Matchers.equalTo(2));

        Mockito.verify(buildingService, Mockito.times(1)).findByFilter(Mockito.any());
    }

    @Test
    @TestSecurity(user = "CLIENT", roles = {"CLIENT"})
    public void shouldGetAllBuildingsWithClientRole() {
        Mockito.when(buildingService.findByFilter(Mockito.any()))
                .thenReturn(Collections.emptyList());

        when()
            .get()
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("size()", Matchers.equalTo(0));

        Mockito.verify(buildingService, Mockito.times(1)).findByFilter(Mockito.any());
    }

    @Test
    @TestSecurity(user = "AGENT", roles = {"CREATE_BUILDING"})
    public void shouldCreateBuildingWithInvalidRequest() {
        var invalidRequest = new CreateBuildingRequest(
                "",  // Invalid: blank name
                null,  // Invalid: null coordinates
                -100.0,  // Invalid: negative area
                "",  // Invalid: blank description
                null,  // Invalid: null address
                null  // Invalid: null lockerId
        );

        given()
            .contentType(ContentType.JSON)
            .body(invalidRequest)
        .when()
            .post()
        .then()
            .statusCode(400);
    }

    @Test
    @TestSecurity(user = "AGENT", roles = {"AGENT"})
    public void shouldNotCreateBuildingWithoutCreateBuildingPermission() {
        var coordinates = new CoordinatesDTO(10.5, 20.5);
        var address = new AddressDTO("Rua Test", "São Paulo", "Apto 01", "Brasil", "12345-678");
        var request = new CreateBuildingRequest(
                "Building Test",
                coordinates,
                500.0,
                "Test description",
                address,
                1L
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(403);
    }

    @Test
    public void shouldNotCreateBuildingWithoutAuthentication() {
        var coordinates = new CoordinatesDTO(10.5, 20.5);
        var address = new AddressDTO("Rua Test", "São Paulo", "Apto 01", "Brasil", "12345-678");
        var request = new CreateBuildingRequest(
                "Building Test",
                coordinates,
                500.0,
                "Test description",
                address,
                1L
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(401);
    }

    @Test
    public void shouldGetAllBuildingsWithoutAuthentication() {
        when()
            .get()
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    public void shouldGetAllBuildingsWithEmptyResultWithoutAuthentication() {
        Mockito.when(buildingService.findByFilter(Mockito.any()))
                .thenReturn(Collections.emptyList());

        when()
            .get()
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", Matchers.equalTo(0));
    }

}