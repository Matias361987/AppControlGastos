package AppGastos.AppGastos.repository;

import AppGastos.AppGastos.model.Expense;
import AppGastos.AppGastos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Esto nos permitirá traer solo los gastos del usuario que inició sesión
    List<Expense> findByUser(User user);
}
