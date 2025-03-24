package dev.pawan.sharemate.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import dev.pawan.sharemate.enums.ExpenseCategory;
import dev.pawan.sharemate.enums.SplitType;
import dev.pawan.sharemate.response.UserDTO;

@Entity
@Table(name = "EXPENSES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String expenseUid;

//    @ManyToOne
//    @JoinColumn(name = "group_id")
    private Integer groupId;
//
//    @ManyToOne
//    @JoinColumn(name = "created_by", nullable = false)
    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Column(insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SplitType splitType;

    @Column(nullable = false)
    private Integer paidBy;
    

    @Column(nullable = false)
    private String expenseCategory;

//    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
//    private Set<ExpenseSplit> expenseSplits;
}
