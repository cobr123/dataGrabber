package ru.datagrabber.data;

import java.util.List;

/**
 * Created by cobr123 on 05.09.2015.
 */
public class Product {
    private final String url;
    private final String name;
    private final String article;
    private final String price;
    private final String desc;
    private final List<String> imgUrl;
    private final List<String> size;

    public Product(final String url
            , final String name
            , final String article
            , final String price
            , final String desc
            , final List<String> imgUrl
            , final List<String> size
    ) {
        this.url = url;
        this.name = name;
        this.article = article;
        this.price = price;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.size = size;
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

    public String getImgMain() {
        if (imgUrl.isEmpty()) {
            return "";
        }
        return imgUrl.get(0);
    }

    public String getImgOthers() {
        if (imgUrl.isEmpty() || imgUrl.size() == 1) {
            return "";
        }
        return String.join("||", imgUrl.subList(1, imgUrl.size()));
    }

    public String getSizes() {
        return String.join("||", size);
    }

    public String getUrl() {
        return url;
    }

    public void print() {
        System.out.println("name = " + getName());
        System.out.println("article = " + getArticle());
        System.out.println("price = " + getPrice());
        System.out.println("desc = " + getDesc());
        System.out.println("imgs.main = " + getImgMain());
        System.out.println("imgs.others = " + getImgOthers());
        System.out.println("size = " + getSizes());
        System.out.println("url = " + getUrl());
    }
}

