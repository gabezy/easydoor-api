package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.domain.building.entities.Locker;
import br.com.gabezy.easydoorapi.resources.dto.building.CreateLockerRequest;
import br.com.gabezy.easydoorapi.services.LockerService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URL;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(LockerResource.class)
public class LockerResourceTest {

    @TestHTTPResource
    URL url;

    @InjectMock
    LockerService lockerService;

    @Test
    @TestSecurity(user = "admin", roles = "ADMIN")
    public void testCreateLocker() {
        var request = new CreateLockerRequest(
                "Locker 1",
                "123456789",
                new br.com.gabezy.easydoorapi.resources.dto.CoordinatesDTO(-15.776387735994753, -47.880859375)
        );

        var locker = new Locker();
        locker.id = 1L;
        locker.serialNumber = "123456789";
        locker.name = "Locker 1";

        Mockito.reset(lockerService);
        Mockito.when(lockerService.create(request))
                .thenReturn(locker);

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post()
            .then()
            .statusCode(201)
            .header("Location", org.hamcrest.Matchers.matchesPattern(url.toString() + "/\\d+"));

        Mockito.verify(lockerService, Mockito.times(1)).create(request);
    }

    @Test
    public void testCreateLockerWithoutAuthentication() {
        var request = new CreateLockerRequest(
                "Locker 1",
                "123456789",
                new br.com.gabezy.easydoorapi.resources.dto.CoordinatesDTO(-15.776387735994753, -47.880859375)
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
    @TestSecurity(user = "admin", roles = "ADMIN")
    public void testCreateLockerWithInvalidData() {
        var request = new CreateLockerRequest(
                "",
                "",
                new br.com.gabezy.easydoorapi.resources.dto.CoordinatesDTO(-15.776387735994753, -47.880859375)
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post()
            .then()
            .statusCode(400);
    }

}
