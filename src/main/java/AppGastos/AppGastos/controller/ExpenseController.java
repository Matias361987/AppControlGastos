package AppGastos.AppGastos.controller;

import AppGastos.AppGastos.dto.ExpenseRequest;
import AppGastos.AppGastos.dto.FinanceSummaryDTO;
import AppGastos.AppGastos.model.Category;
import AppGastos.AppGastos.model.Expense;
import AppGastos.AppGastos.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // 1. Obtener categorías del usuario
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories(Authentication authentication) {
        return ResponseEntity.ok(expenseService.getUserCategories(authentication.getName()));
    }

    // 2. Crear nueva categoría
    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody String categoryName, Authentication authentication) {
        // Limpieza profunda del String
        String cleanName = categoryName.trim()
                .replace("\"", "")
                .replace("{", "")
                .replace("}", "");

        if (cleanName.isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre no puede estar vacío");
        }

        return ResponseEntity.ok(expenseService.saveCategory(cleanName, authentication.getName()));
    }

    // 3. Obtener todos los gastos del usuario (Para la tabla)
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(Authentication authentication) {
        return ResponseEntity.ok(expenseService.getUserExpenses(authentication.getName()));
    }

    @GetMapping("/by-category")
    public ResponseEntity<Map<String, Double>> getExpensesByCategory(Authentication authentication) {
        // El service que creamos antes ya tiene la lógica de agrupación
        Map<String, Double> categoryTotals = expenseService.getExpensesByCategory(authentication.getName());
        return ResponseEntity.ok(categoryTotals);
    }

    @GetMapping("/summary")
    public ResponseEntity<FinanceSummaryDTO> getSummary(Authentication authentication) {
        return ResponseEntity.ok(expenseService.getFinanceSummary(authentication.getName()));
    }

    // 4. Guardar un nuevo gasto
    @PostMapping
    public ResponseEntity<?> createExpense(@RequestBody ExpenseRequest request, Authentication authentication) {
        expenseService.saveExpense(request, authentication.getName());
        return ResponseEntity.ok().build();
    }
}