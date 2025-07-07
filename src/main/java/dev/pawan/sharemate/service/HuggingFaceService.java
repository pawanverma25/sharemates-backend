package dev.pawan.sharemate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.pawan.sharemate.request.HuggingFaceReqDTO;
import dev.pawan.sharemate.response.HuggingFaceDTO;

@Service
public class HuggingFaceService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${HUGGINGFACE_API_KEY}")
    private String API_KEY;
	
	@Value("${HUGGINGFACE_API_URL}")
	private String url;

	public String huggingFaceAPICall(String desc,List<String> category) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(API_KEY);
		HuggingFaceReqDTO req = new HuggingFaceReqDTO();
		HuggingFaceReqDTO.Parameters params = new HuggingFaceReqDTO.Parameters();
		params.setCandidate_labels(category);
		req.setInputs(desc);
		req.setParameters(params);

		HttpEntity<HuggingFaceReqDTO> entity = new HttpEntity<>(req, headers);
		ResponseEntity<HuggingFaceDTO> response = restTemplate.postForEntity(
		    url, entity, HuggingFaceDTO.class
		);
		
		List<String> labels = response.getBody().getLabels();
		return labels.get(0);
		
		
	}
}
