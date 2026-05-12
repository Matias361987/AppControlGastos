package AppGastos.AppGastos.repository;

import AppGastos.AppGastos.model.Category;
import AppGastos.AppGastos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Busca solo las categorías que pertenecen al usuario logueado
    List<Category> findByUser(User user);
}
