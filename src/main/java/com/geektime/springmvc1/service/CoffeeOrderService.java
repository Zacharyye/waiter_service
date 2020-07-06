package com.geektime.springmvc1.service;

import com.geektime.springmvc1.model.Coffee;
import com.geektime.springmvc1.model.CoffeeOrder;
import com.geektime.springmvc1.model.OrderState;
import com.geektime.springmvc1.repository.CoffeeOrderRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional
@Slf4j
public class CoffeeOrderService implements MeterBinder {
  @Autowired
  private CoffeeOrderRepository orderRepository;

  private Counter orderCounter = null;

  public CoffeeOrder createOrder(String customer, Coffee... coffees) {
    CoffeeOrder order = CoffeeOrder.builder()
          .customer(customer)
          .items(Arrays.asList(coffees))
          .state(OrderState.INIT)
          .build();
    CoffeeOrder saved = orderRepository.save(order);
    log.info("New Order: {}", saved);
    orderCounter.increment();
    return saved;
  }

  public boolean updateState(CoffeeOrder order, OrderState state) {
    if(state.compareTo(order.getState()) <= 0) {
      log.warn("Wrong State order: {}, {}", state, order.getState());
      return false;
    }
    order.setState(state);
    orderRepository.save(order);
    log.info("Updated Order: {}", order);
    return true;
  }

  public CoffeeOrder get(Long id) {
    CoffeeOrder order = orderRepository.getOne(id);
    return order;
  }

  @Override
  public void bindTo(MeterRegistry meterRegistry) {
    this.orderCounter = meterRegistry.counter("order.count");
  }
}
