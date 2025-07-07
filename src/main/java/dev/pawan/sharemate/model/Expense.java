package dev.pawan.sharemate.model;

import java.math.BigDecimal;
import java.sql.Date;

import dev.pawan.sharemate.enums.SplitType;
import dev.pawan.sharemate.request.ExpenseRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EXPENSES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @ManyToOne
    // @JoinColumn(name = "group_id")
    private Integer groupId;
    //
    // @ManyToOne
    // @JoinColumn(name = "created_by", nullable = false)
    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private Date createdDate = new Date(System.currentTimeMillis());

    @Column(insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date modifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SplitType splitType;

    @Column(nullable = false)
    private Integer paidBy;

    @Column(nullable = true)
    private String expenseCategory;
    
    @Column(name = "is_active", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private Character isActive;

    // @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    // private Set<ExpenseSplit> expenseSplits;
    
    public Expense(ExpenseRequestDTO request) {
    	this.id= request.getExpenseId();
		this.description = request.getDescription();
		this.createdDate = request.getCreatedDate();
		this.paidBy = request.getPaidBy();
		this.groupId = request.getGroupId();
		this.createdBy = request.getCreatedBy();
		this.amount = request.getAmount();
		this.splitType = request.getSplitType();
		this.isActive = 'Y';
	}
}
