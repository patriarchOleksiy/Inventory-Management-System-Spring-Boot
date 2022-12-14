package ru.kataaas.ims.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductDTO {

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @Min(1)
    @NotNull
    private BigDecimal price;

    @Min(0)
    @NotNull
    private int quantity;

    @NotNull
    private Date createdAt;

    @NotNull
    private CategoryDTO category;

    @NotNull
    private String vendorName;

    private int discountPercent;

}
