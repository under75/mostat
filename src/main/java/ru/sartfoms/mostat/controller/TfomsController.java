package ru.sartfoms.mostat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TfomsController {

	@RequestMapping("/tfoms")
	public String tfomsHome(Model model) {

		return "tfoms-home";
	}
}
