package com.geektime.springmvc1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "T_ORDER")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class CoffeeOrder extends BaseEntity implements Serializable {
  private String customer;
  @ManyToMany
  @JoinTable(name = "T_ORDER_COFFEE")
  @OrderBy("id")
  private List<Coffee> items;
  @Enumerated
  @Column(nullable = false)
  private OrderState state;
}
