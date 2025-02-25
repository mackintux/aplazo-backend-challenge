package com.aplazo.bnpl.challenge.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private BigDecimal amount;
    private String paymentScheme;
    private Integer numberOfPayments;
    private BigDecimal interestRate;
    private BigDecimal totalAmount;
    private BigDecimal commissionAmount;
    private BigDecimal installmentAmount;
    private LocalDate purchaseDate;

    @ElementCollection
    @CollectionTable(name = "payment_due_dates", joinColumns = @JoinColumn(name = "purchase_id"))
    @Column(name = "due_date")
    private List<LocalDate> paymentDueDates;

}
