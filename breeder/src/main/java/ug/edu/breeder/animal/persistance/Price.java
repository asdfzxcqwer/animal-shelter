package ug.edu.breeder.animal.persistance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Price implements Serializable {
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "currency")
    private Currency currency;
}