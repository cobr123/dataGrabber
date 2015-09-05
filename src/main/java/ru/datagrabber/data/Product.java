package ru.datagrabber.data;

import java.util.List;

/**
 * Created by cobr123 on 05.09.2015.
 */
public class Product {
    private final String name;
    private final String article;
    private final String price;
    //private final List<String> imgUrl;
    //private final List<String> size;
    private final String desc;

    public Product(final String name,final String article,final String price,final String desc) {
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

    public String getPrice() {
        return price;
    }

    public String getDesc() {
        return desc;
    }

    public void print() {
        System.out.println("name = " + name);
        System.out.println("article = " + article);
        System.out.println("price = " + price);
        System.out.println("desc = " + desc);
    }
}

