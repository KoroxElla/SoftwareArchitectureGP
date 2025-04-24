package com.example.part1;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.Record;
import com.example.part1.repo.AppointmentRepo;
import com.example.part1.repo.MedicalRecordRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicalRecordRepo medicalRecordRepo;

    @MockitoBean
    private AppointmentRepo appointmentRepo;

    private Appointments testAppointment;

    @BeforeEach
    public void setup() {
        //Setup reusable test appointment with ID 1
        testAppointment = new Appointments();
        testAppointment.setId(1L);
    }

    @Test // Tests: #20 POST /medical-records
    public void testCreateMedicalRecordSuccess() throws Exception {
        // Simulate valid appointment
        given(appointmentRepo.findById(1L)).willReturn(Optional.of(testAppointment));

        // Simulate saving medical record to repo
        Record saved = new Record();
        saved.setId(100L);
        saved.setDiagnosis("Test Diagnosis");
        saved.setTreatment("Test Treatment");
        saved.setNotes("Some notes");
        saved.setAppointment(testAppointment);
        given(medicalRecordRepo.save(any(Record.class))).willReturn(saved);
        //JSON request with data
        String json = """
            {
                "diagnosis": "Test Diagnosis",
                "treatment": "Test Treatment",
                "notes": "Some notes",
                "appointment": {
                    "id": 1
                }
            }
        """;

        mockMvc.perform(post("/medical-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.diagnosis").value("Test Diagnosis"))
                .andExpect(jsonPath("$.treatment").value("Test Treatment"));
    }

    @Test // Tests #20: fails if appointment does not exist
    public void testCreateMedicalRecordFailsIfAppointmentNotFound() throws Exception {
        //Simulates missing appointment
        given(appointmentRepo.findById(999L)).willReturn(Optional.empty());

        String json = """
            {
                "diagnosis": "Missing appointment test",
                "appointment": {
                    "id": 999
                }
            }
        """;

        mockMvc.perform(post("/medical-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Appointment not found."));
    }

    @Test //Tests #20 fails if diagnosis is missing
    public void testCreateMedicalRecordFailsIfDiagnosisMissing() throws Exception {
        //Missing required diagnosis field
        String json = """
            {
                "treatment": "Some treatment"
            }
        """;

        mockMvc.perform(post("/medical-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Diagnosis is required."));
    }

    @Test //Tests #20 fails if appointment already has a record
    public void testCreateMedicalRecordFailsIfAppointmentAlreadyHasRecord() throws Exception {
        //Simualte appointment that already has a record
        Appointments alreadyUsed = new Appointments();
        alreadyUsed.setId(2L);
        alreadyUsed.setRecord(new Record());

        given(appointmentRepo.findById(2L)).willReturn(Optional.of(alreadyUsed));

        String json = """
            {
                "diagnosis": "Test Diagnosis",
                "appointment": {
                    "id": 2
                }
            }
        """;

        mockMvc.perform(post("/medical-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Appointment already has a record."));
    }
}

