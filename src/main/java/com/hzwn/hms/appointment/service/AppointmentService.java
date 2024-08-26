package com.hzwn.hms.appointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.hzwn.hms.appointment.model.Appointment;
import com.hzwn.hms.appointment.repository.AppointmentRepository;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Appointment saveAppointment(Appointment appointment) {
        // Optionally validate patientId and doctorId by calling respective services
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment != null) {
            String patientServiceUrl = "http://hms-patient-service/patients/" + appointment.getPatientId();
            String doctorServiceUrl = "http://hms-doctor-service/doctors/" + appointment.getDoctorId();
            // Use restTemplate to fetch patient and doctor details
        }
        return appointment;
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
