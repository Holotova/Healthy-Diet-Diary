package com.project.demoSpringDiary.controller;

import com.project.demoSpringDiary.model.Category;
import com.project.demoSpringDiary.model.Product;
import com.project.demoSpringDiary.repository.ProductRepository;
import com.project.demoSpringDiary.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/products")
public class ProductController {

    ProductService productService;
    ProductRepository repository;

    @Autowired
    public ProductController(ProductService productService, ProductRepository repository) {
        this.productService = productService;
        this.repository = repository;
    }

    @PostMapping
    public String addProduct(@RequestParam String productName, @RequestParam Category category,
                                   @RequestParam Double calories, ModelAndView modelAndView) {
        productService.createAndSaveProduct(productName, category, calories);
        return "redirect:/products";
    }

    @GetMapping
    public ModelAndView filterOrSortProduct(@RequestParam(required = false) String filter,
                                            @RequestParam(required = false) String sort, ModelAndView modelAndView) {
        Iterable<Product> products = productService.getFilteredOrSortedProducts(filter, sort);
        modelAndView.addObject("products", products);
        modelAndView.setViewName("products");
        return modelAndView;
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteProduct(@PathVariable("id") String id, ModelAndView modelAndView,
                                      RedirectAttributes redirAttrs) {
        if (productService.isProductUsedByUser(id)) {
            redirAttrs.addFlashAttribute("message",
                    "You cannot delete the product. It was used in user's meal");
            return "redirect:/products";
        } else {
            repository.deleteById(id);
        }
        return "redirect:/products";
    }
}
