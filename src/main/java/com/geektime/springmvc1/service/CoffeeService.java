package com.geektime.springmvc1.service;

import com.geektime.springmvc1.model.Coffee;
import com.geektime.springmvc1.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@CacheConfig(cacheNames = "CoffeeCache")
public class CoffeeService {
  @Autowired
  private CoffeeRepository coffeeRepository;

  @Cacheable
  public List<Coffee> getAllCoffee() {
    return coffeeRepository.findAll(Sort.by("id"));
  }

  public List<Coffee> getCoffeeByName(List<String> names) {
    return coffeeRepository.findByNameInOrderById(names);
  }

  public Coffee getCoffee(String name) {
    return coffeeRepository.findByName(name);
  }

  public Coffee getCoffee(Long id) {
    Optional<Coffee> coffee = coffeeRepository.findById(id);
    return coffee.get();
  }

  public Coffee saveCoffee(String name, Money price) {
    return coffeeRepository.save(Coffee.builder().name(name).price(price).build());
  }

  public long getCoffeeCount() {
    return coffeeRepository.count();
  }
}
