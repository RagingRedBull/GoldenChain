package com.remd.spring.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.remd.spring.model.MedicinePrescription;
import com.remd.spring.model.MyUserDetails;
import com.remd.spring.model.PatientRecord;
import com.remd.spring.model.User;
import com.remd.spring.repository.MedicinePrescriptionRepository;
import com.remd.spring.repository.PatientRecordRepository;
import com.remd.spring.services.EmailService;

@Controller
public class PrescriptionController {
	private static final Logger log = LoggerFactory.getLogger(PrescriptionController.class);
	@Autowired
	private EmailService emailService;
	@Autowired
	private PatientRecordRepository patientRecordRepository;
	@Autowired
	private MedicinePrescriptionRepository medicinePrescriptionRespository;

	@GetMapping(path = "/app/prescription")
	public String viewPage(Model model) {
		List<PatientRecord> patients = patientRecordRepository.findAll();
		model.addAttribute("isPrescriptionActive", true);
		model.addAttribute("patients", patients);
		return "app/prescription";
	}

	@PostMapping(path = "/app/prescription/send")
	public String sendPrescriptionEmail(@RequestParam(name = "genericMedicineName") List<String> genericMedicines,
			@RequestParam(name = "brandedMedicineName") List<String> brandedMedicines,
			@RequestParam(name = "recommendedDosage") List<String> recommendedDosages,
			@RequestParam(name = "medicineNotes") List<String> medicineNotes,
			@RequestParam(name = "patientId") Integer patientId) throws MessagingException {
		Map<String, Object> params = new HashMap<String, Object>(); //Email Contents
		List<MedicinePrescription> medicineList = new ArrayList<MedicinePrescription>(); //List of Prescribed Medicines
		PatientRecord patient = patientRecordRepository.findById(patientId).get(); //Targeted Patient
		LocalDate currentDate = LocalDate.now(); //Current Date
		User currentUserDetails = ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal()).getUser();
		for (int i = 0; i < genericMedicines.size(); i++) {
			medicineList.add(new MedicinePrescription(genericMedicines.get(i), brandedMedicines.get(i),
					recommendedDosages.get(i), medicineNotes.get(i),currentDate,patient));
		}
		medicinePrescriptionRespository.saveAll(medicineList);
		params.put("patient", patient);
		params.put("doctor", currentUserDetails);
		params.put("medicineList", medicineList);
		params.put("dateGenerated", currentDate);
		emailService.sendPrescriptionEmailTemplate("someeobscuremailaddress@gmail.com", "test", params);
		log.info("User " + currentUserDetails.getFullNameFormatted() + " has a sent a prescription for " + patient.getFullNameStartingFirstName());
		return "redirect:/app/prescription";
	}
}
