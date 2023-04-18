package ru.sartfoms.mostat.service.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateService {
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");

	public static boolean isValid(String dateStr) {
		try {
			LocalDate.parse(dateStr, DATE_TIME_FORMATTER);
		} catch (DateTimeParseException e) {
			return false;
		}
		return true;
	}
}
