package com.library.library_management.service;

import com.library.library_management.model.Loan;
import com.library.library_management.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class LoanScheduler {
    @Autowired
    private LoanRepository loanRepository;

    // run every minute for testing, for daily change ("0 0 0 * * ?")
    @Scheduled(cron = "0 * * * * ?")
    public void checkOverdueLoans() {
        System.out.println("Checking for overdue loans...");
        List<Loan> loans = loanRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Loan loan : loans) {
            if (today.isAfter(loan.getDueDate())) {
                long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), today);

                double newFine = daysOverdue * 10;
                if (newFine > loan.getFine()) { // Only update if fine has increased
                    loan.setFine(newFine);
                    loanRepository.save(loan);
                    System.out.println("Charged fine of ₹" + newFine + " to user " + loan.getUser().getName() + " for loan ID " + loan.getLoanId());
                }
            }
        }
    }
}