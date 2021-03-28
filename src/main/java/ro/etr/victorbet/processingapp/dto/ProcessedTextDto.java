package ro.etr.victorbet.processingapp.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessedTextDto {

	@SerializedName("freq_word")
	private String mostFrequentWord;
	
	@SerializedName("avg_paragraph_size")
	private int avgParagraphSize;

	@SerializedName("avg_paragraph_processing_time")
	private long avgProcessingTimeInMillis;

	@SerializedName("total_processing_time")
	private long totalProcessingTimeInMllis;
}