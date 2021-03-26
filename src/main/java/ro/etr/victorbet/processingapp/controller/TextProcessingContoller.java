package ro.etr.victorbet.processingapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/betvictor")
public class TextProcessingContoller {

	@GetMapping("isAlive")
	private String isAlive() {
		return "TextProcessingContoller is alive!";
	}
	
}
