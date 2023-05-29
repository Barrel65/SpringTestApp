package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.*;
import com.example.springsecurityapplication.repositories.CategoryRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.repositories.PersonRepository;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.OrderService;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminController {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final PersonRepository personRepository;
    private final PersonService personService;

    @Value("${upload.path}")
    private String uploadPath;

    private final CategoryRepository categoryRepository;

    public AdminController(ProductService productService, OrderRepository orderRepository, OrderService orderService, PersonRepository personRepository, PersonService personService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.personRepository = personRepository;
        this.personService = personService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("admin/product/add")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }

    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one")MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three, @RequestParam("file_four")MultipartFile file_four, @RequestParam("file_five")MultipartFile file_five, @RequestParam("category") int category, Model model) throws IOException {
        Category category_db = (Category) categoryRepository.findById(category).orElseThrow();
        System.out.println(category_db.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/addProduct";
        }

        if(file_one != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);

        }

        if(file_two != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_three != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_four != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_five != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five .getOriginalFilename();
            file_five .transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        productService.saveProduct(product, category_db);
        return "redirect:/admin";
    }


    @GetMapping("/admin")
    public String admin(Model model)
    {
        model.addAttribute("products", productService.getAllProduct());
        return "admin";
    }

    @GetMapping("admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin";
    }

    @GetMapping("admin/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id){
        model.addAttribute("product", productService.getProductId(id));
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";


    }

    @PostMapping("admin/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }



    @GetMapping("admin/user_list")
    public String userList(Model model) {
        List<Person> userList = personRepository.findAll();
        model.addAttribute("alert","");
        model.addAttribute("users", userList);
        return "admin/user_list";
    }

    @GetMapping("admin/user_list/edit/{id}")
    public String  changeUserRole(@PathVariable("id") int id, Model model){
        model.addAttribute("user", personRepository.getReferenceById(id));
        return "admin/user_role_change";
    }

    @PostMapping ("admin/user_list/edit/{id}")
    public String  changeUserRolePost(@PathVariable("id") int id, @RequestParam("role") String role, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        if (personDetails.getPerson().getId() == id) {
            model.addAttribute("alert", "Нельзя изменить свою Роль!");
            List<Person> userList = personRepository.findAll();
            model.addAttribute("users", userList);
            return "admin/user_list";
        } else {

            System.out.println("test");
            personService.editRole(id, role);
            return "redirect:/admin/user_list";
        }
    }

    @GetMapping("admin/orders")
    public String orders(Model model) {
        List<Order> orderList = orderRepository.findAll();
        model.addAttribute("orders", orderList);
        model.addAttribute("searchForm", new OrdersSearchForm());
        return "admin/orders";
    }

    @GetMapping("admin/orders/edit/{id}")
    public String  changeOrderStatus(@PathVariable("id") int id, Model model){
        model.addAttribute("order", orderService.findByID(id));
        return "admin/order_status_change";
    }

    @PostMapping ("admin/orders/edit/{id}")
    public String  changeOrderStatusPost(@ModelAttribute("order") Order order, @RequestParam("status") Status st) {

            orderService.editStatus(order.getId(), st);
            return "redirect:/admin/orders";
    }


    @PostMapping("admin/orders/search")
    public String searchOrders(@ModelAttribute("searchForm")  @Valid OrdersSearchForm form, BindingResult bindingResult,@RequestParam("search") String searchStr, Model model) {
        if(bindingResult.hasErrors()){
            List<Order> orderList = orderRepository.findAll();
            model.addAttribute("orders", orderList);
            model.addAttribute("searchForm", new OrdersSearchForm());
            System.out.println(form.getSearch());
            System.out.println(searchStr);
            return "admin/orders";

        }
        System.out.println(form.getSearch());
        System.out.println(searchStr);
        model.addAttribute("search_orders", orderRepository.findByLastFourSymbols(searchStr.toLowerCase()));
        List<Order> orderList = orderRepository.findAll();
        model.addAttribute("orders", orderList);
        model.addAttribute("searchForm", new OrdersSearchForm());

        return "admin/orders";
    }



}







