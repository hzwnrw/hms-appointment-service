package com.hzwn.hms.appointment.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hzwn.hms.appointment.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	boolean existsByDoctorIdAndAppointmentDateBetween(Long doctorId, LocalDateTime appointmentStartTime,
			LocalDateTime appointmentEndTime);
   
}
