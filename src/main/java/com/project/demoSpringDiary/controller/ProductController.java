package com.project.demoSpringDiary.controller;

import com.project.demoSpringDiary.model.Category;
import com.project.demoSpringDiary.model.Product;
import com.project.demoSpringDiary.repository.ProductRepository;
import com.project.demoSpringDiary.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


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
    public ModelAndView addProduct(@RequestParam String productName, @RequestParam Category category,
                                   @RequestParam Double calories, ModelAndView modelAndView) {
        productService.createAndSaveProduct(productName, category, calories);
        modelAndView.setViewName("products");
        return modelAndView;
    }

    @GetMapping
    public ModelAndView filterOrSortProduct(@RequestParam(required = false) String filter,
                                      @RequestParam(required = false) String sort, ModelAndView modelAndView) {
        Iterable<Product> products = productService.getFilteredOrSortedProducts(filter, sort);
        modelAndView.addObject("products", products);
        modelAndView.setViewName("products");
        return modelAndView;
    }

}
