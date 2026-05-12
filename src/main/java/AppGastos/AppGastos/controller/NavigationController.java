package AppGastos.AppGastos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Busca templates/login.html
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // Busca templates/register.html
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Página principal de gastos
    }
}