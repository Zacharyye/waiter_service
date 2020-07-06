package com.geektime.springmvc1.controller;

import com.geektime.springmvc1.controller.exception.FormValidationException;
import com.geektime.springmvc1.controller.request.NewCoffeeRequest;
import com.geektime.springmvc1.model.Coffee;
import com.geektime.springmvc1.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/coffee")
@Slf4j
public class CoffeeController {
  @Autowired
  private CoffeeService coffeeService;

  @GetMapping(path = "/", params = "!name")
  @ResponseBody
  public List<Coffee> getAll() {
    return coffeeService.getAllCoffee();
  }

  @GetMapping(path = "/", params = "name")
  @ResponseBody
  public Coffee getByName(@RequestParam String name) {
    return coffeeService.getCoffee(name);
  }

  @RequestMapping(path = "/{id}", method = RequestMethod.GET)
  @ResponseBody
  public Coffee getByid(@PathVariable Long id) {
    return coffeeService.getCoffee(id);
  }

  @PostMapping(path = "/" , consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  public Coffee addCoffee(@Valid  NewCoffeeRequest newCoffee, BindingResult result) {
    if(result.hasErrors()) {
      log.warn("Binding Errors: {}",result);
      throw new FormValidationException(result);
    }
    return coffeeService.saveCoffee(newCoffee.getName(), newCoffee.getPrice());
  }


  @PostMapping(path = "/" , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  public Coffee addJsonCoffee(@Valid @RequestBody NewCoffeeRequest newCoffee, BindingResult result) {
    if(result.hasErrors()) {
      log.warn("Binding Errors: {}",result);
      throw new ValidationException(result.toString());
    }
    return coffeeService.saveCoffee(newCoffee.getName(), newCoffee.getPrice());
  }
//  @PostMapping(path = "/" , consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//  @ResponseBody
//  @ResponseStatus(HttpStatus.CREATED)
//  public Coffee addCoffeeWithoutBindingResult(@Valid  NewCoffeeRequest newCoffee) {
//    return coffeeService.saveCoffee(newCoffee.getName(), newCoffee.getPrice());
//  }
//
//  @PostMapping(path = "/" , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
//  @ResponseBody
//  @ResponseStatus(HttpStatus.CREATED)
//  public Coffee addJsonCoffeeWithoutBindingResult(@Valid @RequestBody  NewCoffeeRequest newCoffee) {
//    return coffeeService.saveCoffee(newCoffee.getName(), newCoffee.getPrice());
//  }

  @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  public List<Coffee> batchAddCoffee(@RequestParam("file")MultipartFile file) {
    List<Coffee> coffees = new ArrayList<>();
    if(!file.isEmpty()) {
      BufferedReader br = null;
      try {
        br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String str;
        while ((str = br.readLine()) != null) {
          String[] arr = StringUtils.split(str, " ");
          if(arr != null && arr.length == 2) {
            coffees.add(coffeeService.saveCoffee(arr[0], Money.of(CurrencyUnit.of("CNY"), NumberUtils.createBigDecimal(arr[1]))));
          }
        }
      } catch (IOException e) {
        log.error("exception", e);
      } finally {
        IOUtils.closeQuietly(br);
      }
    }
    return coffees;
  }
}
