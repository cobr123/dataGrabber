package ru.datagrabber.parser;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.utils.URIUtils;
import org.apache.log4j.BasicConfigurator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.datagrabber.data.Product;
import ru.datagrabber.data.ProductCategory;
import ru.datagrabber.data.ProductCategoryType;
import ru.datagrabber.grabber.Downloader;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * Created by cobr123 on 05.09.2015.
 */
public final class ParseProduct {
    private static final Logger logger = LoggerFactory.getLogger(ParseProduct.class);
    private static final ProductCategory category_kinds = new ProductCategory(7, "Детям");
    private static final ProductCategory category_boys = new ProductCategory(278, "Одежда для мальчиков");
    private static final ProductCategory category_girls = new ProductCategory(291, "Одежда для девочек");
    private static final List<ProductCategory> category_for_boys = new ArrayList<ProductCategory>();
    private static final List<ProductCategory> category_for_girls = new ArrayList<ProductCategory>();

    static {
        category_for_boys.add(new ProductCategory(283, "Верхняя одежда для мальчиков"));
        category_for_boys.add(new ProductCategory(281, "Джинсы, брюки для мальчиков"));
        category_for_boys.add(new ProductCategory(286, "Комбинезоны для мальчиков", "комбинезон"));
        category_for_boys.add(new ProductCategory(306, "Комплекты одежды для мальчиков", "комплект"));
        category_for_boys.add(new ProductCategory(289, "Костюмы и пиджаки для мальчиков", "костюм", "пиджак"));
        category_for_boys.add(new ProductCategory(288, "Нижнее белье для мальчиков"));
        category_for_boys.add(new ProductCategory(329, "Носки и колготки для мальчиков"));
        category_for_boys.add(new ProductCategory(287, "Пижамы, халаты для мальчиков", "пижама", "халат"));
        category_for_boys.add(new ProductCategory(285, "Рубашки для мальчиков", "рубашка"));
        category_for_boys.add(new ProductCategory(284, "Свитера, джемперы для мальчиков", "свитер", "джемпер"));
        category_for_boys.add(new ProductCategory(290, "Спортивная одежда для мальчиков"));
        category_for_boys.add(new ProductCategory(280, "Толстовки и кофты для мальчиков", "толстовка", "кофта"));
        category_for_boys.add(new ProductCategory(279, "Футболки, майки для мальчиков", "футболка", "майка"));
        category_for_boys.add(new ProductCategory(282, "Шорты для мальчиков", "шорты"));

        category_for_girls.add(new ProductCategory(300, "Блузки и рубашки для девочек", "рубашка", "блузка"));
        category_for_girls.add(new ProductCategory(294, "Верхняя одежда для девочек"));
        category_for_girls.add(new ProductCategory(293, "Джинсы и брюки для девочек"));
        category_for_girls.add(new ProductCategory(301, "Комбинезоны для девочек", "комбинезон"));
        category_for_girls.add(new ProductCategory(305, "Комплекты одежды для девочек", "комплект"));
        category_for_girls.add(new ProductCategory(298, "Купальники для девочек", "купальник"));
        category_for_girls.add(new ProductCategory(299, "Нижнее белье для девочек"));
        category_for_girls.add(new ProductCategory(297, "Носки, колготки для девочек"));
        category_for_girls.add(new ProductCategory(302, "Пижамы и халаты для девочек", "пижама", "халат"));
        category_for_girls.add(new ProductCategory(296, "Свитера, джемперы для девочек", "свитер", "джемпер"));
        category_for_girls.add(new ProductCategory(304, "Спортивная одежда для девочек"));
        category_for_girls.add(new ProductCategory(292, "Толстовки и кофты для девочек", "толстовка", "кофта"));
        category_for_girls.add(new ProductCategory(303, "Футболки, майки для девочек", "футболка", "майка"));
        category_for_girls.add(new ProductCategory(328, "Шорты для девочек"));
        category_for_girls.add(new ProductCategory(295, "Юбки, платья для девочек", "юбка", "платье"));
    }

    public static void findCategory(final String productName, final List<ProductCategory> categories, final List<ProductCategory> lookup_categories) {
        for (final ProductCategory category : lookup_categories) {
            if (category.getName().toLowerCase().contains(productName.toLowerCase())
                    || category.getNameTags().contains(productName.toLowerCase())
                    || category.tagInNameExists(productName.toLowerCase())
                    ) {
                logger.trace("Для продукта '{}' найдена категория '{}'", productName, category.getName());
                categories.add(category);
            }
        }
    }

    public static List<ProductCategory> findCategory(final String productName, final ProductCategoryType type) {
        final List<ProductCategory> cat = new ArrayList<ProductCategory>();
        cat.add(category_kinds);
        switch (type) {
            case GIRLS:
                cat.add(category_girls);
                findCategory(productName, cat, category_for_girls);
                break;
            case BOYS:
                cat.add(category_boys);
                findCategory(productName, cat, category_for_boys);
                break;
        }

        return cat;
    }

    public static Product parse(final ProductCategoryType type, final String url, final Map<String, String> loginCookies) throws IOException {
        final Document doc = Downloader.getDoc(url, loginCookies);
        return ParseProduct.parse(type, doc, url);
    }

    public static Product parse(final ProductCategoryType type, final Document doc, final String url) {
        final String[] parts = url.split("/");
        final String id = parts[parts.length - 1];
        final String name = doc.select("h1.item-title").first().text();
        final String article = doc.select("p.item-number").first().text().replace("Артикул: ", "");
        final String price = doc.select("p.item-price").first().text().replaceAll(" Р$", "");
        final String desc = doc.select("ul.item-params").first().text();

        final Elements imgElems = doc.select("div.image-radio > label > img");
        logger.trace("imgElems.size() = " + imgElems.size());
        final List<String> imgs = new ArrayList<String>(imgElems.size());
        for (final Element elem : imgElems) {
            final String imgSrc = elem.attr("src");
            final int idx = imgSrc.lastIndexOf('/');
            final String fileName = imgSrc.substring(idx + 1, imgSrc.length());
            if (fileName.toLowerCase().startsWith("color_")) {
                final String basePath = imgSrc.substring(0, idx + 1);
                imgs.add(basePath + fileName.substring(6));
            } else {
                imgs.add(elem.attr("src"));
            }
        }

        final Elements sizeElems = doc.select("div.size-radio > label > span");
        logger.trace("sizeElems.size() = " + sizeElems.size());
        final List<String> sizes = new ArrayList<String>(sizeElems.size());
        for (final Element elem : sizeElems) {
            sizes.add(elem.text());
        }
        final List<ProductCategory> cat = findCategory(name, type);
        return new Product(id, url, name, article, price, desc, imgs, sizes, cat);
    }

    public static void main(final String[] args) throws IOException {
        // Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();
//        final Map<String, String> loginCookies = Downloader.getCookies();
        final String url = Downloader.baseUri + "/positions/660";
        System.out.println(Downloader.getFileToSave(url));
        final Product product = ParseProduct.parse(ProductCategoryType.BOYS, url, null);
        product.print();
    }
}
