package ru.datagrabber.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.datagrabber.data.Product;
import ru.datagrabber.data.ProductCategoryType;
import ru.datagrabber.grabber.Downloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cobr123 on 05.09.2015.
 */
public final class ParseProductList {
    private static final Logger logger = LoggerFactory.getLogger(ParseProductList.class);

    public static List<Product> parse(final ProductCategoryType type, final String url, final Map<String, String> loginCookies) throws IOException {
        final List<Product> list = new ArrayList<Product>();
        String nextUrl = url;
        while (!nextUrl.isEmpty()) {
            logger.trace("nextUrl = ", nextUrl);
            logger.trace("getFileToSave = ", Downloader.getFileToSave(nextUrl));
            final Document doc = Downloader.getDoc(nextUrl, loginCookies);
            final Elements elems = doc.select("div.img-wrap > a");
            logger.trace("elems.size() = ", elems.size());
            for (final Element elem : elems) {
                final Product product = ParseProduct.parse(type, Downloader.baseUri + elem.attr("href"), loginCookies);
                list.add(product);
            }
            final Element next = doc.select("span.next > a").last();
            if (next != null) {
                nextUrl = Downloader.baseUri + next.attr("href");
            } else {
                nextUrl = "";
            }
        }
        return list;
    }

    public static void main(final String[] args) throws IOException {
//        final Map<String, String> loginCookies = Downloader.getCookies();
        final String url = "http://alena-shop.ru/wear/devochkam";
        final List<Product> products = ParseProductList.parse(ProductCategoryType.GIRLS, url, null);
        for (final Product product : products) {
            product.print();
        }
        System.out.println("products.size() = " + products.size());
    }
}
