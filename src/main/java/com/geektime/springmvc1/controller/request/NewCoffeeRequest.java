package com.geektime.springmvc1.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.money.Money;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@ToString
public class NewCoffeeRequest {
  @NotEmpty
  private String name;
  @NotNull
  private Money price;
}
