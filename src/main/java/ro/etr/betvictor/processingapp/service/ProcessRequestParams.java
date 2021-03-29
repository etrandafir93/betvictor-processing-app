package ro.etr.betvictor.processingapp.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProcessRequestParams {

	private int startParagraph;
	private int endParagraph;
	private int minWordCount;
	private int maxWordCount;
	
}
