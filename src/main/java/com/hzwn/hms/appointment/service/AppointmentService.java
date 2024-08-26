package com.hzwn.hms.appointment.service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hzwn.hms.appointment.model.Appointment;
import com.hzwn.hms.appointment.repository.AppointmentRepository;
import com.hzwn.hms.config.ServiceUrlConfig;
import com.hzwn.hms.model.AppointmentModel;
import com.hzwn.hms.model.DoctorModel;
import com.hzwn.hms.model.PatientModel;

@Service
public class AppointmentService {

	private final static Logger log = LoggerFactory.getLogger(AppointmentService.class);

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ServiceUrlConfig serviceUrlConfig;

	@Autowired
	ApplicationContext applicationContext;

	public Appointment saveAppointment(Appointment appointment) {
		// Optionally validate patientId and doctorId by calling respective services
		LocalDateTime appointmentStartTime = appointment.getAppointmentDate();
        LocalDateTime appointmentEndTime = appointmentStartTime.plusMinutes(30);
        appointment.setAppointmentEndTime(appointmentEndTime);
		return appointmentRepository.save(appointment);
	}

	public List<Appointment> getAllAppointments() {
		return appointmentRepository.findAll();
	}

	public AppointmentModel getAppointmentById(Long id) throws IllegalAccessException, InvocationTargetException {
		// Fetch appointment
		Appointment appointment = appointmentRepository.findById(id).orElse(null);
		if (appointment != null) {
			AppointmentModel appointmentResponse = new AppointmentModel();
			BeanUtils.copyProperties(appointmentResponse, appointment);

			// Fetch patient details
			ResponseEntity<PatientModel> patientResponse = restTemplate.getForEntity(
					serviceUrlConfig.getPatientServiceUrl() + "patients/" + appointment.getPatientId(),
					PatientModel.class);
			try {
				if (patientResponse.getStatusCode().is2xxSuccessful() && patientResponse.getBody() != null) {
					log.info("Patient Valid");
				}
			} catch (Exception e) {
				
				throw new RuntimeException(e);
			}

			// Fetch doctor details
			 try {
		            ResponseEntity<DoctorModel> doctorResponse = restTemplate.getForEntity(
		                    serviceUrlConfig.getDoctorServiceUrl() + "doctors/" + appointment.getDoctorId(),
		                    DoctorModel.class);
		            if (doctorResponse.getStatusCode().is2xxSuccessful() && doctorResponse.getBody() != null) {
		                log.info("Doctor Valid");

		                // Check for conflicting appointments
		                LocalDateTime appointmentStartTime = appointment.getAppointmentDate();
		                LocalDateTime appointmentEndTime = appointmentStartTime.plusMinutes(30); // Assuming 30 mins per appointment

		                boolean hasConflict = appointmentRepository.existsByDoctorIdAndAppointmentDateBetween(
		                        appointment.getDoctorId(),
		                        appointmentStartTime,
		                        appointmentEndTime
		                );

		                if (hasConflict) {
		                    throw new RuntimeException("Doctor has a conflicting appointment at the scheduled time.");
		                }
		            }
		        } catch (Exception e) {
		            throw new RuntimeException(e);
		        }

		        return appointmentResponse;
		    } else {
		        throw new RuntimeException("Failed to retrieve appointment");
		    }
	}

	public void deleteAppointment(Long id) {
		appointmentRepository.deleteById(id);
	}
}
