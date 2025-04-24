package com.example.part1;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.Doctor;
import com.example.part1.domain.Patient;
import com.example.part1.domain.Record;
import com.example.part1.repo.AppointmentRepo;
import com.example.part1.repo.DoctorRepo;
import com.example.part1.repo.PatientRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class AppointmentRestControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AppointmentRepo appointmentRepo;
    @Autowired private DoctorRepo doctorRepo;
    @Autowired private PatientRepo patientRepo;


    private Appointments appointment;
    private Patient patient;
    private Doctor doctor;

    @BeforeEach
    void setup() {
        //Setup data
        patient = new Patient();
        patient.setName("John Smith");
        patient = patientRepo.save(patient);

        doctor = new Doctor();
        doctor.setName("Dr. House");
        doctor = doctorRepo.save(doctor);

        appointment = new Appointments();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(Timestamp.valueOf("2025-04-24 10:00:00"));
        appointment.setStatus("Scheduled");
        appointment.setNotes("Routine check-up");
        appointment = appointmentRepo.save(appointment);
    }

    @Test // Tests: #14: GET /appointments - List all appointments
    public void testGetAllAppointments() throws Exception {
        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Scheduled"));
    }

    @Test // Tests:  #16: GET /appointments/{id} - Get specific appointment (success case)
    public void testGetAppointmentByIdSuccess() throws Exception {
        mockMvc.perform(get("/appointments/" + appointment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value("Routine check-up"));
    }

    @Test // Tests: #16: GET /appointments/{id} - Get specific appointment (not found)
    public void testGetAppointmentByIdNotFound() throws Exception {
        mockMvc.perform(get("/appointments/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Appointment not found."));
    }

    @Test //Tests: #15: POST /appointments - Create a new appointment
    public void testCreateAppointment() throws Exception {
        String json = """
            {
              "appointmentDate": "2025-04-24T14:00:00",
              "status": "Scheduled",
              "notes": "Follow-up visit",
              "patient": { "id": %d },
              "doctor": { "id": %d }
            }
            """.formatted(patient.getId(), doctor.getId());

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("Scheduled"));
    }

    @Test //Tests: #19: GET /appointments/{id}/medical-record - Get medical record for an appointment (success)
    public void testGetMedicalRecordForAppointment() throws Exception {
        Record record = new Record();
        record.setDiagnosis("Flu");
        appointment.setRecord(record);
        appointment = appointmentRepo.save(appointment);

        mockMvc.perform(get("/appointments/" + appointment.getId() + "/medical-record"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosis").value("Flu"));
    }

    @Test //Tests:  #19: GET /appointments/{id}/medical-record - Record not found for appointment
    public void testGetMedicalRecordForAppointmentNotFound() throws Exception {
        Appointments newAppointment = new Appointments();
        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor);
        newAppointment.setAppointmentDate(Timestamp.valueOf("2025-04-25 11:00:00"));
        newAppointment = appointmentRepo.save(newAppointment);

        mockMvc.perform(get("/appointments/" + newAppointment.getId() + "/medical-record"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Record not found for this appointment."));
    }
    @Test //Tests #17: PUT /appointments/{id} - Update an existing appointment
    public void testUpdateAppointmentSuccess() throws Exception {
        String updateJson = """
        {
          "appointmentDate": "2025-04-25T16:30:00",
          "status": "Completed",
          "notes": "Updated note",
          "patient": { "id": %d },
          "doctor": { "id": %d }
        }
        """.formatted(patient.getId(), doctor.getId());

        mockMvc.perform(put("/appointments/" + appointment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Completed"))
                .andExpect(jsonPath("$.notes").value("Updated note"));
    }

    @Test //Tests #17: PUT /appointments/{id} - Update non-existing appointment (Not Found)
    public void testUpdateAppointmentNotFound() throws Exception {
        String updateJson = """
        {
          "appointmentDate": "2025-04-25T16:30:00",
          "status": "Cancelled",
          "notes": "Cancelled appointment",
          "patient": { "id": %d },
          "doctor": { "id": %d }
        }
        """.formatted(patient.getId(), doctor.getId());

        mockMvc.perform(put("/appointments/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Appointment not found."));
    }

    @Test // Tests: #18: DELETE /appointments/{id} - Delete an existing appointment
    public void testDeleteAppointmentSuccess() throws Exception {
        mockMvc.perform(delete("/appointments/" + appointment.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment deleted."));
    }

    @Test //Tests: #18: DELETE /appointments/{id} - Delete non-existing appointment
    public void testDeleteAppointmentNotFound() throws Exception {
        mockMvc.perform(delete("/appointments/9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Appointment not found."));
    }
}
