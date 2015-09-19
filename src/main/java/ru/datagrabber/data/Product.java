package ru.datagrabber.data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cobr123 on 05.09.2015.
 */
public final class Product {
    private final String id;
    private final String url;
    private final String name;
    private final String article;
    private final String price;
    private final String desc;
    private final List<String> imgUrl;
    private final List<String> size;
    private final List<ProductCategory> category;

    public Product(final String id
            , final String url
            , final String name
            , final String article
            , final String price
            , final String desc
            , final List<String> imgUrl
            , final List<String> size
            , final List<ProductCategory> category
    ) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.article = article;
        this.price = price;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.size = size;
        this.category = category;
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

    public String getCategories() {
        if (category.isEmpty()) {
            return "";
        }
        return String.join("||", category.stream().map(c -> c.getId().toString()).collect(Collectors.toList()));
    }
    public String getCategoryNames() {
        if (category.isEmpty()) {
            return "";
        }
        return String.join("||", category.stream().map(c -> c.getName()).collect(Collectors.toList()));
    }

    public String getSizes() {
        return String.join("||", size);
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public void print() {
        System.out.println("id = " + getId());
        System.out.println("name = " + getName());
        System.out.println("article = " + getArticle());
        System.out.println("price = " + getPrice());
        System.out.println("desc = " + getDesc());
        System.out.println("imgs.main = " + getImgMain());
        System.out.println("imgs.others = " + getImgOthers());
        System.out.println("size = " + getSizes());
        System.out.println("url = " + getUrl());
        System.out.println("category = " + getCategories());
        System.out.println("category_name = " + getCategoryNames());
    }
}

