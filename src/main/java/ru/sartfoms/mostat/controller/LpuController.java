package ru.sartfoms.mostat.controller;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.sartfoms.mostat.entity.User;
import ru.sartfoms.mostat.service.LpuService;
import ru.sartfoms.mostat.service.ReportTypeService;
import ru.sartfoms.mostat.service.UserService;

@Controller
public class LpuController {
	private final LpuService lpuService;
	private final UserService userService;
	private final ReportTypeService reportTypeService;

	public LpuController(LpuService lpuService, UserService userService, ReportTypeService reportTypeService) {
		this.lpuService = lpuService;
		this.userService = userService;
		this.reportTypeService = reportTypeService;
	}

	@RequestMapping("/lpu")
	public String lpuHome(Model model, @RequestParam("page") Optional<Integer> page) {
		User user = userService.getByName(SecurityContextHolder.getContext().getAuthentication().getName());

		model.addAttribute("lpu", lpuService.getById(user.getLpuId()));
		model.addAttribute("reportTypesPage", reportTypeService.findAll(page));

		return "lpu-home";
	}
}
