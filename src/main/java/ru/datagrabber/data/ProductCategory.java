package ru.datagrabber.data;

/**
 * Created by cobr123 on 19.09.15.
 */
public final class ProductCategory {
    private final Integer id;
    private final String name;

    /**
     *
     * @param id ID раздела
     * @param name Название
     */
    public ProductCategory(final Integer id,final String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
