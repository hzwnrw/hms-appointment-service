package com.hzwn.hms.appointment.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Appointment")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "patient_id", nullable = false)
	private Long patientId; // Reference to Patient ID
	@Column(name = "doctor_id", nullable = false)
	private Long doctorId; // Reference to Doctor ID
	@Column(name = "appointment_date", nullable = false)
	private LocalDateTime appointmentDate;
	@Column(name = "appointment_end_time", nullable = false)
	private LocalDateTime appointmentEndTime;
	@Column(name = "status", nullable = false)
	private String status; // e.g., "SCHEDULED", "CANCELLED", "COMPLETED"

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getAppointmentEndTime() {
		return appointmentEndTime;
	}

	public void setAppointmentEndTime(LocalDateTime appointmentEndTime) {
		this.appointmentEndTime = appointmentEndTime;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public LocalDateTime getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDateTime appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
