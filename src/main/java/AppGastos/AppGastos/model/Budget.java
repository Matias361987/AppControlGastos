package AppGastos.AppGastos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "budgets")
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double monthlyIncome;   // Tus ingresos (Sueldo, etc)
    private Double monthlyBudget;   // Tu meta de ahorro/gasto máximo
    private String monthYear;        // Formato "MM-YYYY" (ej: "05-2026")

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}