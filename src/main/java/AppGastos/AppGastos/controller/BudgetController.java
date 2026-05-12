package AppGastos.AppGastos.controller;

import AppGastos.AppGastos.dto.IncomeRequest;
import AppGastos.AppGastos.model.Budget;
import AppGastos.AppGastos.model.User;
import AppGastos.AppGastos.repository.BudgetRepository;
import AppGastos.AppGastos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;



    @PostMapping("/add-income")
    public ResponseEntity<?> addIncome(@RequestBody IncomeRequest request, Authentication authentication) {
        // Buscamos al usuario por el nombre que viene en el Token
        User user = userRepository.findByUsername(authentication.getName()).get();

        // Buscamos su presupuesto en Neon
        Budget budget = budgetRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Configura tu presupuesto primero"));

        // Sumamos el monto al ingreso actual
        budget.setMonthlyIncome(budget.getMonthlyIncome() + request.getAmount());

        budgetRepository.save(budget);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> saveBudget(@RequestBody Budget budgetRequest, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscamos si ya existe uno para actualizarlo, si no, creamos uno nuevo
        Budget budget = budgetRepository.findByUser(user)
                .orElse(new Budget());

        budget.setUser(user);
        budget.setMonthlyIncome(budgetRequest.getMonthlyIncome());
        budget.setMonthlyBudget(budgetRequest.getMonthlyBudget()); // Asegúrate que tu modelo Budget tenga este campo

        budgetRepository.save(budget);
        return ResponseEntity.ok().build();
    }
}