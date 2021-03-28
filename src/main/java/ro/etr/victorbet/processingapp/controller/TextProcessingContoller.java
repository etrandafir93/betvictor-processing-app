package ro.etr.victorbet.processingapp.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ro.etr.victorbet.processingapp.dto.ProcessedTextDto;
import ro.etr.victorbet.processingapp.service.ProcessRequestParams;
import ro.etr.victorbet.processingapp.service.TextProcessingService;
 
@RestController
@RequestMapping("betvictor")
public class TextProcessingContoller {

    private static Logger logger = LoggerFactory.getLogger(TextProcessingContoller.class);

    @Autowired
    private TextProcessingService service;
    
	@GetMapping("isAlive")
	public String isAlive() {
		logger.info( "GET /isAlive");
		return "TextProcessingContoller is alive!";
	}
	
	@GetMapping("text")
	public ProcessedTextDto processText(@RequestParam(name = "p_start") int startParagraph, 
				@RequestParam(name = "p_end") int endParagraph, 
				@RequestParam(name = "w_count_min") int minWords, 
				@RequestParam(name = "w_count_max") int maxWords) throws IOException, InterruptedException {

		logger.info( "GET /text  paragraphs: {}-{} , word count: {}-{}", startParagraph, endParagraph, minWords, maxWords);
		
		ProcessRequestParams request = ProcessRequestParams.builder()
			.startParagraph(startParagraph)
			.endParagraph(endParagraph)
			.minWordCount(minWords)
			.maxWordCount(maxWords)
			.build();
		
		return service.process( request );
	}
	 

	
}
