package AppGastos.AppGastos.service;

import AppGastos.AppGastos.dto.ExpenseRequest;
import AppGastos.AppGastos.dto.FinanceSummaryDTO;
import AppGastos.AppGastos.model.Budget; // Importar Budget
import AppGastos.AppGastos.model.Category;
import AppGastos.AppGastos.model.Expense;
import AppGastos.AppGastos.model.User;
import AppGastos.AppGastos.repository.BudgetRepository; // Importar BudgetRepository
import AppGastos.AppGastos.repository.CategoryRepository;
import AppGastos.AppGastos.repository.ExpenseRepository;
import AppGastos.AppGastos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BudgetRepository budgetRepository; // <--- INYECCIÓN CLAVE

    public List<Category> getUserCategories(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return categoryRepository.findByUser(user);
    }

    public Category saveCategory(String categoryName, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Category category = new Category();
        category.setName(categoryName);
        category.setUser(user);

        return categoryRepository.save(category);
    }

    public Map<String, Double> getExpensesByCategory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Expense> expenses = expenseRepository.findByUser(user);

        // Agrupamos por nombre de categoría y sumamos los montos
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().getName(),
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }



    public List<Expense> getUserExpenses(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return expenseRepository.findByUser(user);
    }

    public FinanceSummaryDTO getFinanceSummary(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Budget budget = budgetRepository.findByUser(user)
                .orElseGet(() -> {
                    Budget newBudget = new Budget();
                    newBudget.setMonthlyIncome(0.0);
                    newBudget.setMonthlyBudget(0.0);
                    newBudget.setUser(user);
                    return newBudget;
                });

        // Obtenemos los gastos totales (Lógica que ya tienes funcional)
        List<Expense> expenses = expenseRepository.findByUser(user);
        Double totalSpent = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        // Calculamos el saldo restante
        Double balance = budget.getMonthlyIncome() - totalSpent;

        // Retornamos el DTO con los 4 campos llenos
        return new FinanceSummaryDTO(
                totalSpent,                // totalExpenses
                budget.getMonthlyIncome(), // monthlyIncome
                budget.getMonthlyBudget(), // budgetLimit (LA META)
                balance                    // remainingBalance
        );
    }

    public void saveExpense(ExpenseRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
        expense.setCategory(category);
        expense.setUser(user);

        expenseRepository.save(expense);
    }
}