package ro.etr.victorbet.processingapp.infrastructure;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RandomTextResponse {

	@SerializedName("text_out")
	private String textOut;
	private String type;
	private String time;
	private String format;

	@SerializedName("max_number")
	private int maxNumber;
	private int number;
	private int amount;
	
}
