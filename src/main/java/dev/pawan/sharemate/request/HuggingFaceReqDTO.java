package dev.pawan.sharemate.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HuggingFaceReqDTO {

	private String inputs;
	private Parameters parameters;
	
	@Data
	public static class Parameters {
	    private List<String> candidate_labels;
	}
}


