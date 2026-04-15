package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Appontiment;
import br.com.gabezy.easydoorapi.infra.exceptions.ResourceNotFoundException;
import br.com.gabezy.easydoorapi.resources.dto.appointment.AppointmentApprovalRequest;
import br.com.gabezy.easydoorapi.resources.dto.appointment.CreateAppointmentRequest;
import br.com.gabezy.easydoorapi.resources.dto.appointment.FilterAppointmentDTO;
import br.com.gabezy.easydoorapi.services.AppointmentService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(AppointmentResource.class)
public class AppointmentResourceTest {

    @TestHTTPResource
    URL url;

    @InjectMock
    AppointmentService appointmentService;

    @Test
    @TestSecurity(user = "AGENT", roles = {"CREATE_APPOINTMENT"})
    public void shouldCreateAppointment() {
        var request = new CreateAppointmentRequest(LocalDateTime.now(), 1L, 1L, 1L);
        var newAppointment = new Appontiment(LocalDateTime.now(), 1L, 1L, 1L, null, null, null, null, null);
        newAppointment.id = 1L;
        Mockito.when(appointmentService.createAppointment(Mockito.any())).thenReturn(newAppointment);

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post()
            .then()
            .statusCode(201)
            .header("Location", Matchers.matchesPattern(url.toString() + "/\\d+"));

        Mockito.verify(appointmentService, Mockito.times(1)).createAppointment(Mockito.any());
    }

    @Test
    @TestSecurity(user = "AGENT", roles = {"VIEW_APPOINTMENT"})
    public void shouldFindAppointmentById() {
        var newAppointment = new Appontiment(LocalDateTime.now(), 1L, 1L, 1L, null, null, null, null, null);
        newAppointment.id = 1L;
        Mockito.when(appointmentService.findById(Mockito.any())).thenReturn(newAppointment);

        when()
            .get("/1")
            .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("id", Matchers.equalTo(1));

        Mockito.verify(appointmentService, Mockito.times(1)).findById(1L);
    }


    @Test
    @TestSecurity(user = "AGENT", roles = {"VIEW_APPOINTMENT"})
    public void shouldNotFindAppointmentById() {
        Mockito.when(appointmentService.findById(Mockito.any())).thenThrow(new ResourceNotFoundException("Appointment not found"));

        when()
            .get("/1")
            .then()
            .contentType(ContentType.JSON)
            .statusCode(404)
            .body("message", Matchers.equalTo("Appointment not found"))
            .body("error", Matchers.equalTo("NOT_FOUND"))
            .body("status", Matchers.equalTo(404));


        Mockito.verify(appointmentService, Mockito.times(1)).findById(1L);
    }

    @Test
    public void shouldNotCreateAppointmentWithoutAuthentication() {
        var request = new CreateAppointmentRequest(LocalDateTime.now(), 1L, 1L, 1L);
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post()
            .then()
            .statusCode(401);
    }

    @Test
    public void shouldNotFindAppointmentWithoutAuthentication() {
        when()
            .get("/1")
            .then()
            .statusCode(401);
    }

    @Test
    @TestSecurity(user = "AGENT", roles = {"VIEW_APPOINTMENTS"})
    public void shouldFindByFilter() {
        Map<String, Object> params = Map.of(
                "clientId", 1,
                "realEstateAgentId", 1,
                "dateFrom", "2026-01-20"
        );

        Mockito.when(appointmentService.findByFilter(Mockito.any(FilterAppointmentDTO.class)))
                .thenReturn(Collections.emptyList());

        given()
            .queryParams(params)
        .when()
            .get()
            .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("size()", Matchers.equalTo(0));

        ArgumentCaptor<FilterAppointmentDTO> filterCaptor = ArgumentCaptor.forClass(FilterAppointmentDTO.class);
        Mockito.verify(appointmentService).findByFilter(filterCaptor.capture());

        // Fazer asserções sobre o objeto capturado
        FilterAppointmentDTO capturedFilter = filterCaptor.getValue();
        assertEquals(1L, capturedFilter.clientId());
        assertEquals(1L, capturedFilter.realEstateAgentId());
        assertEquals(LocalDate.of(2026, 1, 20), capturedFilter.dateFrom());
        assertNull(capturedFilter.buildingId());
        assertNull(capturedFilter.dateTo());
        assertFalse(capturedFilter.canceled());
    }

    @Test
    @TestSecurity(user = "ADMIN", roles = {"ADMIN"})
    public void shouldReviewAppointment() {
        var request = new AppointmentApprovalRequest(1L, true);
        var appointment = new Appontiment(LocalDateTime.now(), 1L, 1L, 1L, null, LocalDateTime.now(), null, null, null);
        appointment.id = 1L;

        Mockito.when(appointmentService.reviewAppointment(Mockito.eq(1L), Mockito.any())).thenReturn(appointment);

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .patch("/1/approval")
        .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(1));
    }

}
