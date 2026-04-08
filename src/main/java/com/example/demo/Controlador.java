package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // defines this class as the web traffic controller
public class Controlador {

    @Autowired // connects to the database automatically
    private UserRepository userRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    // registration
    @PostMapping("/register")
    public String registerProcess(User user, HttpSession session, HttpServletResponse response) {

        userRepository.save(user);

        session.setAttribute("userSession", user);

        Cookie cookie = new Cookie("userEmail", user.getEmail());

        cookie.setMaxAge(86400);

        response.addCookie(cookie);

        return "redirect:/";
    }

    // opens the signup page

    @GetMapping("/register")
    public String showRegister(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

    // showing the login/home page
    @GetMapping("/")
    public String showPrincipal(Model model) {

        model.addAttribute("user", new User());

        return "index";
    }

    @GetMapping("/datesuser")
    public String showDates(HttpSession session, Model model,
            @CookieValue(value = "userEmail", defaultValue = "There is not cookie") String userEmailCookie) {

        User userInSession = (User) session.getAttribute("userSession");
        model.addAttribute("perfil", userInSession);

        System.out.println("Log Server: " + userEmailCookie);
        return "showDates";
    }

    // login logic

    @PostMapping("/login")
    public String loginProcess(@RequestParam String nick, @RequestParam String password, HttpSession session,
            Model model) {

        User user = userRepository.findByNickAndPassword(nick, password);

        if (user != null) {
            session.setAttribute("userSession", user);

            if ("admin".equals(user.getNick()) && "admin123".equals(user.getPassword())) {
                return "redirect:/admin";
            }
            return "redirect:/tienda-paypal";
        } else {
            model.addAttribute("error", "Credenciales incorrectas en la base de datos");
            return "index";
        }
    }

    // show store page for simple user
    @GetMapping("/tienda")
    public String showTienda(HttpSession session, Model model) {

        if (session.getAttribute("userSession") == null) {
            return "redirect:/";
        }

        model.addAttribute("listaArticulos", articuloRepository.findAll());
        return "tienda";
    }

    @GetMapping("/tienda-paypal")
    public String showTiendaPaypal(HttpSession session, Model model) {
        // security check
        if (session.getAttribute("userSession") == null) {
            return "redirect:/";
        }
        // here you could pass a different list or just show the static page
        return "tienda-paypal";
    }

    // show admin page

    @GetMapping("/admin")
    public String adminPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("userSession");

        if (user != null && "admin".equals(user.getNick())) {
            model.addAttribute("registeredUser", userRepository.findAll());
            model.addAttribute("listaArticulos", articuloRepository.findAll());
            model.addAttribute("newArticle", new Articulo());
            return "admin";
        }
        return "/";

    }

    // function for admin for add new article

    @PostMapping("/admin/add-article")
    public String addArticle(Articulo a) {

        articuloRepository.save(a);
        return "redirect:/admin#articulos";
    }

    // function for admin to delete an article

    @GetMapping("/admin/delete/{id}")
    public String deleteArticle(@PathVariable Integer id) {

        articuloRepository.deleteById(id);
        return "redirect:/admin#articulos";
    }

    // add to cart an article
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Integer id, HttpSession session) {

        List<Articulo> carrito = (List<Articulo>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        Articulo art = articuloRepository.findById(id).orElse(null);
        if (art != null) {
            carrito.add(art);
        }

        session.setAttribute("carrito", carrito);

        return "redirect:/tienda?agregado=true";
    }

    @GetMapping("/cart") // calculates the total price and shows the cart
    public String showCart(HttpSession session, Model model) {

        List<Articulo> carrito = (List<Articulo>) session.getAttribute("carrito");

        double total = 0;

        if (carrito != null) {
            for (Articulo a : carrito)
                total += a.getPrice();// summing up all prices
        }

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);

        return "carrito";
    }

    // destroys the session and clears user data
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
