package AppGastos.AppGastos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // <-- Esto ya crea el constructor (Double, Double, Double, Double)
@NoArgsConstructor
public class FinanceSummaryDTO {
    private Double totalExpenses;
    private Double monthlyIncome;
    private Double budgetLimit;
    private Double remainingBalance;

    // BORRA EL CONSTRUCTOR MANUAL QUE ESCRIBISTE.
    // Lombok lo hace de forma invisible por ti.
}
