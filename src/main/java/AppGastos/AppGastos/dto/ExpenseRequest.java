package AppGastos.AppGastos.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ExpenseRequest {
    private String description;
    private Double amount;
    private Long categoryId;
    private LocalDate date;
}