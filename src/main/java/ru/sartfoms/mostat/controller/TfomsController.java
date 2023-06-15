package ru.sartfoms.mostat.controller;

import static ru.sartfoms.mostat.service.util.DateService.isBeforeThanNow;
import static ru.sartfoms.mostat.service.util.DateService.isValid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.ReportType;
import ru.sartfoms.mostat.entity.VReportData;
import ru.sartfoms.mostat.exception.ExcelGeneratorException;
import ru.sartfoms.mostat.model.FormParameters;
import ru.sartfoms.mostat.service.LpuService;
import ru.sartfoms.mostat.service.ReportStatusService;
import ru.sartfoms.mostat.service.ReportTypeService;
import ru.sartfoms.mostat.service.UserService;
import ru.sartfoms.mostat.service.VReportDataService;

@Controller
public class TfomsController {
	private final ReportTypeService reportTypeService;
	private final LpuService lpuService;
	private final VReportDataService vReportDataService;
	private final ReportStatusService reportStatusService;

	public TfomsController(ReportTypeService reportTypeService, LpuService lpuService, UserService userService,
			ReportStatusService reportStatusService, VReportDataService vReportDataService) {
		this.reportTypeService = reportTypeService;
		this.lpuService = lpuService;
		this.vReportDataService = vReportDataService;
		this.reportStatusService = reportStatusService;
	}

	@RequestMapping("/tfoms")
	public String home(Model model, @ModelAttribute("formParams") FormParameters formParams,
			BindingResult bindingResult, @ModelAttribute("show") String show) {
		Collection<Lpu> lpus = new ArrayList<>();
		ReportType reportType = null;
		Map<String, Collection<String>> headerModel = null;
		if (formParams.getReportTypeId() != null) {
			reportType = reportTypeService.getById(formParams.getReportTypeId());
			headerModel = ReportTypeService.createHeaderModel(reportType);
			Collection<VReportData> reportDatas = vReportDataService.findByTypeId(formParams.getReportTypeId());
			lpus.add(lpuService.getDummy());
			lpus.addAll(
					lpuService.findByIdIn(reportDatas.stream().map(VReportData::getLpuId).collect(Collectors.toSet())));
		}

		if (formParams.getDtFrom() != null && !formParams.getDtFrom().isEmpty() && !isValid(formParams.getDtFrom()))
			bindingResult.rejectValue("dtFrom", null);
		if (formParams.getDtTo() != null && !formParams.getDtTo().isEmpty() && !isValid(formParams.getDtTo()))
			bindingResult.rejectValue("dtTo", null);

		model.addAttribute("reportType", reportType);
		model.addAttribute("headerModel", headerModel);
		model.addAttribute("lpus", lpus);
		model.addAttribute("reportTypes", reportTypeService.findAll());

		Collection<VReportData> reportDatas = null;
		if (!bindingResult.hasErrors() && formParams.getLpuId() != null) {
			reportDatas = vReportDataService.getDataByParams(formParams);
		}
		if (show.equals("1")) {
			model.addAttribute("dataModel", VReportDataService.createDataModel(reportDatas));
			model.addAttribute("sumRow", VReportDataService.createSumRow(reportDatas, reportType));
		}

		return "tfoms-home";
	}

	@PostMapping("/tfoms/excel")
	@ResponseBody
	public ResponseEntity<?> download(@ModelAttribute("formParams") FormParameters formParams,
			BindingResult bindingResult) {
		if (formParams.getDtFrom() != null && !formParams.getDtFrom().isEmpty() && !isValid(formParams.getDtFrom()))
			bindingResult.rejectValue("dtFrom", null);
		if (formParams.getDtTo() != null && !formParams.getDtTo().isEmpty() && !isValid(formParams.getDtTo()))
			bindingResult.rejectValue("dtTo", null);

		ResponseEntity<?> resource;
		try {
			if (bindingResult.hasErrors() || formParams.getLpuId() == null) {
				throw new ExcelGeneratorException();
			}
			Lpu lpu;
			if (formParams.getLpuId() == 0) {
				lpu = new Lpu();
				lpu.setName("Все МО");
			} else {
				lpu = lpuService.getById(formParams.getLpuId());
			}
			resource = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=report" + formParams.getReportTypeId() + ".xlsx")
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(new InputStreamResource(vReportDataService
							.createExcel(reportTypeService.getById(formParams.getReportTypeId()), lpu, formParams)));
		} catch (IOException | ExcelGeneratorException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return resource;
	}

	@RequestMapping("/tfoms/admin")
	public String admin(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("typeId") Optional<Long> typeId, @RequestParam("lpuId") Optional<Integer> lpuId,
			@RequestParam("dtRep") Optional<String> dtRep) {

		if (typeId.isPresent() && lpuId.isPresent() && dtRep.isPresent() && isValid(dtRep.get())) {
			reportStatusService.closeReport(typeId.get(), lpuId.get(), dtRep.get());
		}

		model.addAttribute("reportStatusesPage", reportStatusService.findAll(page));
		model.addAttribute("page", page.orElse(1));

		return "tfoms-admin";
	}

	@RequestMapping("/tfoms/admin/new")
	public String add(Model model, @ModelAttribute("formParams") FormParameters formParams, BindingResult bindingResult,
			@ModelAttribute("add") String add) {
		Collection<Lpu> lpus = new ArrayList<>();
		if (formParams.getReportTypeId() != null) {
			lpus.add(lpuService.getDummy());
			lpus.addAll(lpuService.findAll());
		}
		if (formParams.getDtRep() != null && add.equals("1")
				&& (!isValid(formParams.getDtRep()) || isBeforeThanNow(formParams.getDtRep()))) {
			bindingResult.rejectValue("dtRep", "Invalid date");
		}

		if (add.equals("1") && !bindingResult.hasErrors()) {
			reportStatusService.save(formParams);
			return "redirect:/tfoms/admin";
		}

		model.addAttribute("lpus", lpus);
		model.addAttribute("reportTypes", reportTypeService.findAll());

		return "tfoms-admin-new";
	}
}
