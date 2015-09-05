package ru.datagrabber.data;

import java.util.List;

/**
 * Created by cobr123 on 05.09.2015.
 */
public class Product {
    private final String name;
    private final String article;
    private final Double price;
    //private final List<String> imgUrl;
    //private final List<String> size;
    private final String desc;

    public Product(String name, String article, Double price, String desc) {
        this.name = name;
        this.article = article;
        this.price = price;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getArticle() {
        return article;
    }

    public Double getPrice() {
        return price;
    }

    public String getDesc() {
        return desc;
    }
}

