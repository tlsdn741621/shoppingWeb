package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.security.MemberSecurityDTO;
import com.busanit501.shoppingweb_project.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Log4j2
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/home")
    public String home(Model model) {
//        List<ProductDTO> products = productService.getAllProducts();
//        for(ProductDTO product : products){
//            log.info("Controller 에서 product 이름 확인하는중 : " +  product.getProductName());
//        }
//        model.addAttribute("products", products);
        return "home";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal MemberSecurityDTO member, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("==== 🔐 로그인 디버깅 ====");
        log.info("Authentication 객체: {}", auth);
        log.info("이름: {}", auth.getName());
        log.info("권한들: {}", auth.getAuthorities());
        log.info("Principal 클래스: {}", auth.getPrincipal().getClass());

        if (member == null) {
            return "redirect:/login";
        }

        model.addAttribute("member", member);
        return "mypage";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id , Model model) {
        model.addAttribute("productId", id);
        return "product-detail";
        // 화면만 그렸음 대신( productId라는 데이터 하나만 가지고)
    }
}