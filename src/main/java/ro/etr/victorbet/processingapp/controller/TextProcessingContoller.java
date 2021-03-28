package ro.etr.victorbet.processingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ro.etr.victorbet.processingapp.dto.ProcessedTextDto;
import ro.etr.victorbet.processingapp.service.ProcessRequestParams;
import ro.etr.victorbet.processingapp.service.TextProcessingService;
 
@Slf4j
@RestController
@RequestMapping("betvictor")
public class TextProcessingContoller {

    @Autowired
    private TextProcessingService service;
    
	@GetMapping("isAlive")
	public String isAlive() {
		log.info( "GET /isAlive");
		return "TextProcessingContoller is alive!";
	}
	
	@GetMapping("text")
	public ResponseEntity<?> processText(
				@RequestParam(name = "p_start") int startParagraph, 
				@RequestParam(name = "p_end") int endParagraph, 
				@RequestParam(name = "w_count_min") int minWords, 
				@RequestParam(name = "w_count_max") int maxWords) {

		log.info( "GET /text  paragraphs: {}-{} , word count: {}-{}", startParagraph, endParagraph, minWords, maxWords);
		
		ProcessRequestParams request = ProcessRequestParams.builder()
			.startParagraph(startParagraph)
			.endParagraph(endParagraph)
			.minWordCount(minWords)
			.maxWordCount(maxWords)
			.build();
		
		try {
			ProcessedTextDto dto = service.process( request );
			HttpStatus statusCode = dto.withoutWarnings()? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT; 
			return new ResponseEntity<ProcessedTextDto>(dto, statusCode);
		} 
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	 

	
}
