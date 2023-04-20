package ru.sartfoms.mostat.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
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
import org.springframework.web.bind.annotation.ResponseBody;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.ReportData;
import ru.sartfoms.mostat.entity.ReportType;
import ru.sartfoms.mostat.exception.ExcelGeneratorException;
import ru.sartfoms.mostat.model.FormParameters;
import ru.sartfoms.mostat.service.LpuService;
import ru.sartfoms.mostat.service.ReportDataService;
import ru.sartfoms.mostat.service.ReportTypeService;
import ru.sartfoms.mostat.service.UserService;
import ru.sartfoms.mostat.service.util.DateService;

@Controller
public class TfomsController {
	private final ReportTypeService reportTypeService;
	private final LpuService lpuService;
	private final ReportDataService reportDataService;

	public TfomsController(ReportTypeService reportTypeService, LpuService lpuService,
			ReportDataService reportDataService, UserService userService) {
		this.reportTypeService = reportTypeService;
		this.lpuService = lpuService;
		this.reportDataService = reportDataService;
	}

	@RequestMapping("/tfoms")
	public String tfomsHome(Model model, @ModelAttribute("formParams") FormParameters formParams,
			BindingResult bindingResult, @ModelAttribute("show") String show) {
		Collection<Lpu> lpus = new ArrayList<>();
		ReportType reportType = null;
		Map<String, Collection<String>> headerModel = null;
		if (formParams.getReportTypeId() != null) {
			reportType = reportTypeService.getById(formParams.getReportTypeId());
			headerModel = ReportTypeService.createHeaderModel(reportType);
			Collection<ReportData> reportDatas = reportDataService.findByTypeId(formParams.getReportTypeId());
			lpus.add(lpuService.getDummy());
			lpus.addAll(
					lpuService.findByIdIn(reportDatas.stream().map(ReportData::getLpuId).collect(Collectors.toSet())));
		}

		if (formParams.getDtFrom() != null && !formParams.getDtFrom().isEmpty()
				&& !DateService.isValid(formParams.getDtFrom()))
			bindingResult.rejectValue("dtFrom", null);
		if (formParams.getDtTo() != null && !formParams.getDtTo().isEmpty()
				&& !DateService.isValid(formParams.getDtTo()))
			bindingResult.rejectValue("dtTo", null);

		model.addAttribute("reportType", reportType);
		model.addAttribute("headerModel", headerModel);
		model.addAttribute("lpus", lpus);
		model.addAttribute("reportTypes", reportTypeService.findAll());

		Collection<ReportData> reportDatas = null;
		if (!bindingResult.hasErrors() && formParams.getLpuId() != null) {
			reportDatas = reportDataService.getDataByParams(formParams);
		}
		if (show.equals("1")) {
			model.addAttribute("dataModel", ReportDataService.createDataModel(reportDatas));
			model.addAttribute("sumRow", ReportDataService.createSumRow(reportDatas, reportType));
		}

		return "tfoms-home";
	}

	@PostMapping("/tfoms/excel")
	@ResponseBody
	public ResponseEntity<?> download(@ModelAttribute("formParams") FormParameters formParams,
			BindingResult bindingResult) {
		if (formParams.getDtFrom() != null && !formParams.getDtFrom().isEmpty()
				&& !DateService.isValid(formParams.getDtFrom()))
			bindingResult.rejectValue("dtFrom", null);
		if (formParams.getDtTo() != null && !formParams.getDtTo().isEmpty()
				&& !DateService.isValid(formParams.getDtTo()))
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
					.body(new InputStreamResource(
							reportDataService.createExcel(reportTypeService.getById(formParams.getReportTypeId()),
									lpu, formParams)));
		} catch (IOException | ExcelGeneratorException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return resource;
	}
}
