package dev.pawan.sharemate.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.pawan.sharemate.repository.ExpenseCategoryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {
	private final ExpenseCategoryRepository expenseCategoryRepository;
	private final List<String> categories = new ArrayList<>();
	
	public List<String> loadAllCategories() {
		if (categories.isEmpty()) {
			categories.addAll(expenseCategoryRepository.getAllCategory());
		}
		return categories;
	}

}
