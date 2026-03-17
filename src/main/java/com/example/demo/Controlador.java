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





@Controller
public class Controlador {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticuloRepository articuloRepository;


    @PostMapping("/register")
    public String registerProcess(User user, HttpSession session, HttpServletResponse response) {

        userRepository.save(user);
        
        session.setAttribute("userSession", user);

        Cookie cookie = new Cookie("userEmail", user.getEmail());

        cookie.setMaxAge(86400);

        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {

        model.addAttribute("user", new User());
        
        return "register";
    }

    @GetMapping("/")
    public String showPrincipal(Model model) {

        model.addAttribute("user", new User());
        
        return "index";
    }

    @GetMapping("/datesuser")
    public String showDates(HttpSession session, Model model, 
        @CookieValue(value = "userEmail", defaultValue = "There is not cookie") String userEmailCookie
    ) 
    {

        User userInSession = (User) session.getAttribute("userSession");
        model.addAttribute("perfil", userInSession);

        System.out.println("Log Server: "+ userEmailCookie);
        return "showDates";
    }
    
   @PostMapping("/login")
    public String loginProcess(@RequestParam String nick, @RequestParam String password, HttpSession session, Model model) {
        
        User user = userRepository.findByNickAndPassword(nick, password);

        if (user != null) {
            session.setAttribute("userSession", user);
            
           
            if ("admin".equals(user.getNick())&& "admin123".equals(user.getPassword())) {
                return "redirect:/admin";
            }
            return "redirect:/tienda";
        } else {
            model.addAttribute("error", "Credenciales incorrectas en la base de datos");
            return "index";
        }
    }


@GetMapping("/tienda")
public String showTienda(HttpSession session, Model model) {
    
    if (session.getAttribute("userSession") == null) {
        return "redirect:/";
    }

    model.addAttribute("listaArticulos", articuloRepository.findAll());
    return "tienda"; 
}

@GetMapping("/admin")
public String adminPage(HttpSession session, Model model){

    User user = (User)session.getAttribute("userSession");

    if(user!=null && "admin".equals(user.getNick())){
        model.addAttribute("registeredUser", userRepository.findAll());
        model.addAttribute("listaArticulos", articuloRepository.findAll());
        model.addAttribute("newArticle", new Articulo());
        return "admin";
    }
    return "/";

}

@PostMapping ("/admin/add-article")
public String addArticle(Articulo a){

    articuloRepository.save(a);
    return "redirect:/admin#articulos";
}


@GetMapping("/admin/delete/{id}")
public String deleteArticle(@PathVariable Integer id){

    articuloRepository.deleteById(id);
    return "redirect:/admin#articulos";
}

@PostMapping("/cart/add")
public String addToCart(@RequestParam Integer id, HttpSession session){


    List<Articulo> carrito = (List<Articulo>) session.getAttribute("carrito");
    
    if(carrito==null){
        carrito = new ArrayList<>();
    }

    Articulo art = articuloRepository.findById(id).orElse(null);
    if(art!=null){
        carrito.add(art);
    }

    session.setAttribute("carrito", carrito);

    return "redirect:/tienda?agregado=true";
}

@GetMapping("/cart")
public String showCart(HttpSession session, Model model){


    List <Articulo> carrito = (List<Articulo>) session.getAttribute("carrito");

    double total = 0;

    if( carrito!=null){
        for(Articulo a: carrito) total += a.getPrice();
    }

    model.addAttribute("carrito", carrito);
    model.addAttribute("total", total);

    return "carrito";
}



@PostMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/";  
}
    
    


}
