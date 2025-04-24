package com.example.part1;

import com.example.part1.domain.Patient;
import com.example.part1.repo.PatientRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PatientRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientRepo patientRepo;

    private Patient samplePatient;

    //Setup to initialize a sample patient before each test
    @BeforeEach
    public void setup() {
        samplePatient = new Patient();
        samplePatient.setId(1L);
        samplePatient.setName("John Doe");
        samplePatient.setEmail("john@example.com");
        samplePatient.setPhoneNumber("07000000000");
        samplePatient.setAddress("10 King Street");
    }

    @Test // Tests :GET /patients - returns all patients.
    public void testGetAllPatients() throws Exception {
        given(patientRepo.findAll()).willReturn(List.of(samplePatient));

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test // Tests: POST /patients - successfully creates a patient.
    public void testCreatePatientSuccess() throws Exception {
        Patient created = new Patient();
        created.setId(2L);
        created.setName("Jane Doe");
        created.setEmail("jane@example.com");
        created.setPhoneNumber("08000000000");
        created.setAddress("123 Maple Street");

        given(patientRepo.save(any(Patient.class))).willReturn(created);  // <--- ADD THIS

        String json = """
          {
            "name": "Jane Doe",
            "email": "jane@example.com",
            "phoneNumber": "08000000000",
            "address": "123 Maple Street"
          }
          """;

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test // Tests: GET /patients/{id} - returns the patient if found.
    public void testGetPatientByIdSuccess() throws Exception {
        given(patientRepo.findById(1L)).willReturn(Optional.of(samplePatient));

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test // Tests: GET /patients/{id} - returns 404 if patient not found.
    public void testGetPatientByIdFailureNotFound() throws Exception {
        given(patientRepo.findById(9999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/patients/9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patient not found."));
    }

    @Test // Tests: PUT /patients/{id} - successfully updates a patient.
    public void testUpdatePatientSuccess() throws Exception {
        // existing patient found
        given(patientRepo.findById(1L)).willReturn(Optional.of(samplePatient));
        // simulate updated returned by save
        Patient updated = new Patient();
        updated.setId(1L);
        updated.setName("Ana Alb");
        updated.setEmail("ana@example.com");
        updated.setPhoneNumber("0716578979");
        updated.setAddress("20 Queen Street");
        given(patientRepo.save(any(Patient.class))).willReturn(updated);

        String json = """
          {
            "name": "Ana Alb",
            "email": "ana@example.com",
            "phoneNumber": "0716578979",
            "address": "20 Queen Street"
          }
          """;

        mockMvc.perform(put("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.address").value("20 Queen Street"));
    }

    @Test // Tests: PUT /patients/{id} - returns 404 if patient not found during update.
    public void testUpdatePatientFailureNotFound() throws Exception {
        given(patientRepo.findById(9999L)).willReturn(Optional.empty());

        mockMvc.perform(put("/patients/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {
                    "name": "Ghost",
                    "email": "ghost@example.com",
                    "phoneNumber": "0000000000",
                    "address": "Nowhere"
                  }
                  """))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patient not found."));
    }

    @Test // Tests: DELETE /patients/{id} - successfully deletes a patient.
    public void testDeletePatientSuccess() throws Exception {
        given(patientRepo.findById(1L)).willReturn(Optional.of(samplePatient));

        mockMvc.perform(delete("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient deleted."));
    }

    @Test // Tests: DELETE /patients/{id} - returns 404 if patient not found.
    public void testDeletePatientFailureNotFound() throws Exception {
        given(patientRepo.findById(9999L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/patients/9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patient not found."));
    }
}
