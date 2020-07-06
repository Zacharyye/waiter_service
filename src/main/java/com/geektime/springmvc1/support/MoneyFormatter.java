package com.geektime.springmvc1.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class MoneyFormatter implements Formatter<Money> {
  /**
   * 仅做演示用
   * @author: zachary
   * @Created: 2020-06-29 09:53
   */
  @Override
  public Money parse(String s, Locale locale) throws ParseException {
    if(NumberUtils.isParsable(s)) {
      return Money.of(CurrencyUnit.of("CNY"), NumberUtils.createBigDecimal(s));
    } else if(StringUtils.isNotEmpty(s)) {
      String[] split = StringUtils.split(s, " ");
      if(split !=  null && split.length == 2 && NumberUtils.isParsable(split[1])) {
        return Money.of(CurrencyUnit.of(split[0]), NumberUtils.createBigDecimal(split[1]));
      } else {
        throw new ParseException(s, 0);
      }
    }
    throw new ParseException(s, 0);
  }

  @Override
  public String print(Money money, Locale locale) {
    if(money == null) {
      return null;
    }
    return money.getCurrencyUnit().getCode() + " " + money.getAmount();
  }
}
