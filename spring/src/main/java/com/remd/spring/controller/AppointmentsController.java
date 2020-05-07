package com.remd.spring.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.remd.spring.bean.MyUserDetails;
import com.remd.spring.bean.User;

@Controller
public class AppointmentsController {
	@GetMapping("/app/appointments")
	public String viewAppointments(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails user = (MyUserDetails)auth.getPrincipal();
		model.addAttribute("profile", user.getUserProfile());
		return "app/appointments";
	}
}
