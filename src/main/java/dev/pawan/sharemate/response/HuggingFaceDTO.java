package dev.pawan.sharemate.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HuggingFaceDTO {
	    private String sequence;
	    private List<String> labels;
	    private List<Double> scores;
}
