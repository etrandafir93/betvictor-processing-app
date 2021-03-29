package ro.etr.victorbet.processingapp.dto;

import java.util.Set;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;
import ro.etr.victorbet.processingapp.exceptions.Warning;

@Data
@Builder
public class ProcessedTextDto {

	@SerializedName("freq_word")
	private String mostFrequentWord;
	
	@SerializedName("avg_paragraph_size")
	private float avgParagraphSize;

	@SerializedName("avg_paragraph_processing_time")
	private float avgProcessingTimeInMillis;

	@SerializedName("total_processing_time")
	private long totalProcessingTimeInMllis;
	
	private transient Set<Warning> warnings;
	
	public boolean withoutWarnings() {
		return warnings.isEmpty();
	}
}