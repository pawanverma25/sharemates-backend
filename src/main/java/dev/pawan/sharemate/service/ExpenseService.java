package dev.pawan.sharemate.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.pawan.sharemate.repository.ExpenseRepository;
import dev.pawan.sharemate.response.ExpenseDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepo;

    public List<ExpenseDTO> getExpensesByUserId(Integer userId, Pageable pageable) {
        return expenseRepo.findAllByUserId(userId, pageable);
    }

}
