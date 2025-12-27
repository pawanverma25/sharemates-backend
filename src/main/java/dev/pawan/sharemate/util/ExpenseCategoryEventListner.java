package dev.pawan.sharemate.util;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.pawan.sharemate.service.ExpenseCategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Component
@RequiredArgsConstructor
public class ExpenseCategoryEventListner {
	
	private final ExpenseCategoryService expenseCategoryService;
	
	@EventListener(ApplicationReadyEvent.class)
	public void loadDataOnStartup() {
		expenseCategoryService.loadAllCategories();
	}

}
