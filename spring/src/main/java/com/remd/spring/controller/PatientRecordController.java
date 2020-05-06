package com.remd.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.remd.spring.bean.PatientRecord;
import com.remd.spring.repository.PatientRecordRepository;
import com.remd.spring.repository.UserRepository;

@Controller
public class PatientRecordController {
	// This means to get the bean called PatientRecordRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	@Autowired
	private PatientRecordRepository patientRecordRepository;
	
	@RequestMapping(path = "/app/patientrecords", method = RequestMethod.GET)
	public String viewPatientRecords(Model model) {
		model.addAttribute("patientRecords", patientRecordRepository.findAll());
		return "app/patientrecords";
	}
	@RequestMapping(path = "/app/patientrecords", method = RequestMethod.GET,
			params = "order")
	public String viewPatientRecordsSorted(Model model, @RequestParam(name = "order")int order) {
		if(order == 0) {
			model.addAttribute("patientRecords", patientRecordRepository.findAllByOrderByLastNameAsc());
		} else if (order == 1) {
			model.addAttribute("patientRecords", patientRecordRepository.findAllByOrderByLastNameDesc());
		}
		return "app/patientRecords";
	}
	@RequestMapping(path = "/app/patientrecords/new", method = RequestMethod.POST)
	public String insertRecord(
			@RequestParam(name = "patientFirstName")String firstName,
			@RequestParam(name = "patientLastName")String lastName,
			@RequestParam(name = "patientGender")String gender,
			@RequestParam(name = "patientContactNumber")String contactNumber,
			@RequestParam(name = "patientEmailAddress")String email
			) {
		PatientRecord record = new PatientRecord(firstName,lastName,gender,contactNumber,email);
		patientRecordRepository.saveAndFlush(record);
		return "redirect:/app/patientrecords";
	}
}