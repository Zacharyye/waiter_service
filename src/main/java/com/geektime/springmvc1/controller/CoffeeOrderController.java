package com.geektime.springmvc1.controller;

import com.geektime.springmvc1.controller.request.NewOrderRequest;
import com.geektime.springmvc1.model.Coffee;
import com.geektime.springmvc1.model.CoffeeOrder;
import com.geektime.springmvc1.service.CoffeeOrderService;
import com.geektime.springmvc1.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/order")
@Slf4j
public class CoffeeOrderController {
  @Autowired
  private CoffeeOrderService orderService;
  @Autowired
  private CoffeeService coffeeService;

  @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public CoffeeOrder create(@RequestBody NewOrderRequest newOrder) {
    log.info("Receive new Order {}", newOrder);
    Coffee[] coffeeList = coffeeService.getCoffeeByName(newOrder.getItems())
          .toArray(new Coffee[] {});
    return orderService.createOrder(newOrder.getCustomer(), coffeeList);
  }

  @GetMapping("/{id}")
  @ResponseBody
  public CoffeeOrder getOrder(@PathVariable("id") Long id) {
    CoffeeOrder order = orderService.get(id);
    log.info("Get Order: {}", order);
    return order;
  }

  @ModelAttribute
  public List<Coffee> coffeeList() {
    return coffeeService.getAllCoffee();
  }

  @GetMapping(path = "/")
  public ModelAndView showCreateForm() {
    return new ModelAndView("create-order-form");
  }

  @PostMapping(path = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public String createorder(@Valid NewOrderRequest newOrder, BindingResult result, ModelMap map) {
    if (result.hasErrors()) {
      log.warn("Binding Result: {}", result);
      map.addAttribute("message", result.toString());
      return "create-order-form";
    }
    log.info("Receive new Order: {}", newOrder);
    Coffee[] coffeeList = coffeeService.getCoffeeByName(newOrder.getItems())
        .toArray(new Coffee[] {});
    CoffeeOrder order = orderService.createOrder(newOrder.getCustomer(), coffeeList);
    return "redirect:/order/" + order.getId();
  }
}
