package ru.datagrabber.parser;

import org.jsoup.nodes.Document;
import ru.datagrabber.data.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cobr123 on 05.09.2015.
 */
public class ParseProductList {
    public static List<Product> parse(final String url) {
        final List<Product> list = new ArrayList<Product>();
        final Product product = new Product("name1", "article1", 100.12, "desc1");
        list.add(product);
        return list;
    }

    public static List<Product> parse(final Document doc) {
        return null;
    }
}
