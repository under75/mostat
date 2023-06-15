package ru.sartfoms.mostat.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.User;
import ru.sartfoms.mostat.service.LpuService;
import ru.sartfoms.mostat.service.ReportDataService;
import ru.sartfoms.mostat.service.ReportStatusService;
import ru.sartfoms.mostat.service.UserService;
import ru.sartfoms.mostat.service.util.Status;

@Controller
public class LpuController {
	private final LpuService lpuService;
	private final UserService userService;
	private final ReportDataService reportDataService;
	private final ReportStatusService reportStatusService;

	public LpuController(LpuService lpuService, UserService userService, ReportDataService reportDataService,
			ReportStatusService reportStatusService) {
		this.lpuService = lpuService;
		this.userService = userService;
		this.reportDataService = reportDataService;
		this.reportStatusService = reportStatusService;
	}

	@RequestMapping("/lpu")
	public String lpuHome(Model model, @RequestParam("page") Optional<Integer> page) {
		User user = userService.getByName(SecurityContextHolder.getContext().getAuthentication().getName());
		Lpu lpu = lpuService.getById(user.getLpuId());
		model.addAttribute("lpu", lpu);
		model.addAttribute("reportStatusPage",
				reportStatusService.findByLpuIdAndStatus(lpu.getId(), Status.OPEN, page));

		return "lpu-home";
	}

	@RequestMapping("/lpu/reports")
	public String reportsList(Model model, @RequestParam("page") Optional<Integer> page) {
		User user = userService.getByName(SecurityContextHolder.getContext().getAuthentication().getName());

		model.addAttribute("lpu", lpuService.getById(user.getLpuId()));
		model.addAttribute("reportStatusPage", reportStatusService.findByLpuIdAndStatusNot(user.getLpuId(), 0, page));

		return "lpu-reports-list";
	}

	@GetMapping("/lpu/download/{id}")
	@ResponseBody
	public ResponseEntity<?> downloadTemplate(@PathVariable("id") Long id) {
		User user = userService.getByName(SecurityContextHolder.getContext().getAuthentication().getName());

		ResponseEntity<?> resource;
		try {
			resource = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report" + id + ".xlsx")
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(new InputStreamResource(lpuService.createExcelTemplate(id, user)));
		} catch (IOException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resource;

	}

	@GetMapping("/lpu/reports/download/{typeId}/{dateTimeStr}")
	@ResponseBody
	public ResponseEntity<?> downloadReport(@PathVariable("typeId") Long typeId,
			@PathVariable("dateTimeStr") String dateTimeStr) {
		User user = userService.getByName(SecurityContextHolder.getContext().getAuthentication().getName());
		LocalDate dateTime = LocalDate.parse(dateTimeStr);
		ResponseEntity<?> resource;
		try {
			resource = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=report" + typeId + "_" + dateTimeStr + ".xlsx")
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(new InputStreamResource(reportDataService.getReport(typeId, dateTime, user.getLpuId())));
		} catch (NullPointerException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resource;

	}

	@GetMapping("/lpu/reports/delete/{typeId}/{dateTimeStr}")
	public String deleteReport(@PathVariable("typeId") Long typeId, @PathVariable("dateTimeStr") String dateTimeStr) {

		reportDataService.delete(typeId, dateTimeStr);

		return "redirect:/lpu/reports";
	}

	@PostMapping("/lpu/upload")
	public String upload(Model model, @RequestParam("report") MultipartFile[] file, @RequestParam("dtRep") String dtRep,
			RedirectAttributes redirectAttributes) {
		User user = userService.getByName(SecurityContextHolder.getContext().getAuthentication().getName());

		redirectAttributes.addFlashAttribute("hasError", reportDataService.parseAndSave(file, user, dtRep));

		return "redirect:/lpu";
	}
}
