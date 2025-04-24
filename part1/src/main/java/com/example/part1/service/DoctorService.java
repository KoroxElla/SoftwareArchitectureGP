package com.example.part1.service;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.Doctor;
import com.example.part1.domain.Record;
import com.example.part1.domain.Patient;
import com.example.part1.repo.AppointmentRepo;
import com.example.part1.repo.DoctorRepo;
import com.example.part1.repo.MedicalRecordRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepo doctorRepo;
    private final AppointmentRepo appointmentsRepo;
    private final MedicalRecordRepo medicalRecordRepo;

    public DoctorService(DoctorRepo doctorRepo, AppointmentRepo appointmentsRepo, MedicalRecordRepo medicalRecordRepo) {
        this.doctorRepo = doctorRepo;
        this.appointmentsRepo = appointmentsRepo;
        this.medicalRecordRepo = medicalRecordRepo;
    }


    public boolean deleteDoctorAndPreserveMedicalRecords(Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepo.findById(doctorId);
        if (optionalDoctor.isEmpty()) return false;

        Doctor doctor = optionalDoctor.get();

        for (Appointments appointment : doctor.getAppointments()) {
            Patient patient = appointment.getPatient();
            Record medicalRecord = appointment.getMedicalRecord();

            if (medicalRecord != null && patient != null) {
                medicalRecord.setAppointment(null);
                medicalRecord.setPatient(patient);
                medicalRecordRepo.save(medicalRecord);
            }

            appointmentsRepo.delete(appointment);
        }

        doctorRepo.deleteById(doctorId);
        return true;
    }
}
