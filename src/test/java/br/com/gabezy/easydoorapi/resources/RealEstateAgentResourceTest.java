package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.domain.user.entities.RealEstateAgent;
import br.com.gabezy.easydoorapi.resources.dto.AddressDTO;
import br.com.gabezy.easydoorapi.resources.dto.realestateagent.CreateRealEstateAgentRequest;
import br.com.gabezy.easydoorapi.services.RealEstateAgentService;
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
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(RealEstateAgentResource.class)
class RealEstateAgentResourceTest {

    @TestHTTPResource
    URL url;

    @InjectMock
    RealEstateAgentService service;

    @Test
    @TestSecurity(user = "ADMIN", roles = {"ADMIN"})
    void shouldCreateWithAdminRole() {
        var request = validRequest();
        var realEstateAgent = new RealEstateAgent();
        realEstateAgent.id = 1L;

        Mockito.when(service.create(Mockito.any())).thenReturn(realEstateAgent);

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post()
        .then()
                .statusCode(201)
                .header("Location", Matchers.matchesPattern(url.toString() + "/\\d+"));
    }

    @Test
    @TestSecurity(user = "MANAGER", roles = {"CREATE_REAL_ESTATE_AGENT"})
    void shouldCreateWithPermission() {
        var request = validRequest();
        var realEstateAgent = new RealEstateAgent();
        realEstateAgent.id = 1L;

        Mockito.when(service.create(Mockito.any())).thenReturn(realEstateAgent);

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post()
        .then()
                .statusCode(201)
                .header("Location", Matchers.matchesPattern(url.toString() + "/\\d+"));
    }

    @Test
    @TestSecurity(user = "AGENT", roles = {"AGENT"})
    void shouldNotCreateWithoutPermission() {
        given()
                .contentType(ContentType.JSON)
                .body(validRequest())
        .when()
                .post()
        .then()
                .statusCode(403);
    }

    @Test
    void shouldNotCreateWithoutAuthentication() {
        given()
                .contentType(ContentType.JSON)
                .body(validRequest())
        .when()
                .post()
        .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "ADMIN", roles = {"ADMIN"})
    void shouldValidateRequest() {
        var request = new CreateRealEstateAgentRequest(
                "",
                "",
                "",
                "",
                "",
                "invalid-email",
                "",
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post()
        .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "ADMIN", roles = {"ADMIN"})
    void shouldListAllWithAdminRole() {
        Mockito.when(service.findAll()).thenReturn(List.of());

        given()
        .when()
                .get()
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", Matchers.equalTo(0));
    }

    @Test
    @TestSecurity(user = "MANAGER", roles = {"VIEW_REAL_ESTATE_AGENTS"})
    void shouldListAllWithPermission() {
        Mockito.when(service.findAll()).thenReturn(List.of());

        given()
        .when()
                .get()
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", Matchers.equalTo(0));
    }

    @Test
    @TestSecurity(user = "AGENT", roles = {"AGENT"})
    void shouldNotListAllWithoutPermission() {
        given()
        .when()
                .get()
        .then()
                .statusCode(403);
    }

    @Test
    void shouldNotListAllWithoutAuthentication() {
        given()
        .when()
                .get()
        .then()
                .statusCode(401);
    }

    private CreateRealEstateAgentRequest validRequest() {
        return new CreateRealEstateAgentRequest(
                "Imobiliaria Teste",
                "12345678000199",
                "123456",
                "11999999999",
                "agent.test",
                "agent@test.com",
                "123456",
                new AddressDTO("Rua Teste", "Sao Paulo", "SP", "Brasil", "01234-567")
        );
    }
}
