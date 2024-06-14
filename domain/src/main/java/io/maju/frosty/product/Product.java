package io.maju.frosty.product;

import io.maju.frosty.AggregateRoot;
import io.maju.frosty.utils.InstantUtils;
import io.maju.frosty.validation.ValidationHandler;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class Product extends AggregateRoot<ProductID> {

    private String name;
    private String description;
    private boolean active;
    private BigDecimal price;
    private LocalDate expirationDate;
    private Integer stock;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Product(
            final ProductID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final BigDecimal aPrice,
            final LocalDate anExpirationDate,
            final Integer aStock,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.price = aPrice;
        this.expirationDate = anExpirationDate;
        this.stock = aStock;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(anUpdatedAt, "'updatedAt' should not be null");
        this.deletedAt = aDeletedAt;
    }

    public static Product newProduct(
            final String aName,
            final String aDescription,
            final boolean isActive,
            final BigDecimal aPrice,
            final LocalDate anExpirationDate,
            final Integer aStock
    ) {
        final var anId = ProductID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Product(anId, aName, aDescription, isActive, aPrice, anExpirationDate, aStock, now, now, deletedAt);
    }

    public static Product with(
            final ProductID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final BigDecimal aPrice,
            final LocalDate anExpirationDate,
            final Integer aStock,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        return new Product(
                anId,
                aName,
                aDescription,
                isActive,
                aPrice,
                anExpirationDate,
                aStock,
                aCreatedAt,
                anUpdatedAt,
                aDeletedAt);
    }

    public static Product with(final Product aProduct) {
        return with(
                aProduct.getId(),
                aProduct.name,
                aProduct.description,
                aProduct.active,
                aProduct.price,
                aProduct.expirationDate,
                aProduct.stock,
                aProduct.createdAt,
                aProduct.updatedAt,
                aProduct.deletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new ProductValidator(this, handler).validate();
    }

    public Product activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Product deactivate() {
        if (deletedAt == null) {
            this.deletedAt = InstantUtils.now();
        }
        this.active = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Product update(
            final String aName,
            final String aDescription,
            final boolean isActive,
            final BigDecimal aPrice,
            final LocalDate anExpirationDate,
            final Integer aStock
    ) {
        if (isActive) activate();
        else deactivate();

        if (aStock != null) {
            this.stock = aStock;
        }

        this.name = aName;
        this.description = aDescription;
        this.price = aPrice;
        this.expirationDate = anExpirationDate;
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public BigDecimal price() {
        return price;
    }

    public LocalDate expirationDate() {
        return expirationDate;
    }

    public Integer stock() {
        return stock;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }
}
