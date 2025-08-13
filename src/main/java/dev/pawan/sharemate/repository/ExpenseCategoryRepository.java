package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.pawan.sharemate.model.ExpenseCategory;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Integer>{

	@Query("""
            SELECT distinct category from ExpenseCategory
        """)
    List<String> getAllCategory();
}
