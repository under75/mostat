package ru.sartfoms.mostat.controller;

import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {
	
	@GetMapping(value = "/help", produces = { "application/octet-stream" })
	public ResponseEntity<byte[]> help() {
		try {
			InputStream inputStream = new ClassPathResource("files/help.docx", this.getClass().getClassLoader())
					.getInputStream();
			byte[] contents = new byte[inputStream.available()];
			inputStream.read(contents);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(("application/octet-stream")));
			headers.setContentDisposition(ContentDisposition.attachment().filename("sus_tfoms.docx").build());

			return new ResponseEntity<>(contents, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
