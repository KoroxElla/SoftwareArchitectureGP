package com.example.part1;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.Doctor;
import com.example.part1.domain.Patient;
import com.example.part1.repo.AppointmentRepo;
import com.example.part1.repo.DoctorRepo;
import com.example.part1.repo.PatientRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DoctorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private AppointmentRepo appointmentsRepo;

    private Doctor savedDoctor;
    private Patient savedPatient;

    @BeforeEach
    void setUp() {
        //Setup data before each test
        appointmentsRepo.deleteAll();
        doctorRepo.deleteAll();
        patientRepo.deleteAll();

        //Create and save doctor
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Alice");
        doctor.setSpecialisation("Dermatology");
        doctor.setEmail("alice@example.com");
        doctor.setPhoneNumber("1234567890");
        doctor.setAppointments(new ArrayList<>());
        savedDoctor = doctorRepo.save(doctor);

        //Create and save patient
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setPhoneNumber("0000000000");
        patient.setAddress("123 Street");
        savedPatient = patientRepo.save(patient);

        //Create and link appointment
        Appointments appointment = new Appointments();
        appointment.setDoctor(savedDoctor);
        appointment.setPatient(savedPatient);
        appointment.setAppointmentDate(Timestamp.valueOf(LocalDateTime.now()));
        appointment.setStatus("Confirmed");
        appointmentsRepo.save(appointment);

        savedDoctor.getAppointments().add(appointment);
        doctorRepo.save(savedDoctor);
    }

    @Test // Tests #8: GET /doctors - List all doctors
    void testGetAllDoctors() throws Exception {
        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Dr. Alice")));
    }

    @Test // Tests #9: POST /doctors - Create new doctor
    void testCreateDoctor() throws Exception {
        String newDoctorJson = """
            {
              "name": "Dr. Bob",
              "specialisation": "Cardiology",
              "email": "bob@example.com",
              "phoneNumber": "9876543210"
            }
            """;

        mockMvc.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newDoctorJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Dr. Bob")));
    }

    @Test //Tests #10: GET /doctors/{id} - Retrieve a specific doctor
    void testGetDoctorById() throws Exception {
        mockMvc.perform(get("/doctors/" + savedDoctor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Dr. Alice")));
    }

    @Test //Tests #11: PUT /doctors/{id} - Update specific doctor
    void testUpdateDoctor() throws Exception {
        String updateJson = """
            {
              "name": "Dr. Alice Updated",
              "specialisation": "Dermatology"
            }
            """;

        mockMvc.perform(put("/doctors/" + savedDoctor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Dr. Alice Updated")))
                .andExpect(jsonPath("$.specialisation", is("Dermatology")));
    }

    @Test // Tests #12: DELETE /doctors/{id} - Delete a specific doctor
    void testDeleteDoctor() throws Exception {
        mockMvc.perform(delete("/doctors/" + savedDoctor.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Doctor deleted and medical records preserved."));
    }

    @Test //Tests #13: GET /doctors/{id}/appointments - List appointments for a doctor
    void testGetDoctorAppointments() throws Exception {
        mockMvc.perform(get("/doctors/" + savedDoctor.getId() + "/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("Confirmed")));
    }
}
