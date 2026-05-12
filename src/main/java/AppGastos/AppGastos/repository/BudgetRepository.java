package AppGastos.AppGastos.repository;

import AppGastos.AppGastos.model.Budget;
import AppGastos.AppGastos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    // Este método es clave para buscar el presupuesto de Matias específicamente
    Optional<Budget> findByUser(User user);
}
