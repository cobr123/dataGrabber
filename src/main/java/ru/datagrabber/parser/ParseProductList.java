package ru.datagrabber.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.datagrabber.data.Product;
import ru.datagrabber.grabber.Downloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cobr123 on 05.09.2015.
 */
public class ParseProductList {
    public static List<Product> parse(final String url, final Map<String, String> loginCookies) throws IOException {
        final List<Product> list = new ArrayList<Product>();
        String nextUrl = url;
        while (!nextUrl.isEmpty()) {
            System.out.println("nextUrl = " + nextUrl);
            System.out.println("getFileToSave = " + Downloader.getFileToSave(nextUrl));
            final Document doc = Downloader.getDoc(nextUrl, loginCookies);
            final Elements elems = doc.select("div.img-wrap > a");
            System.out.println("elems.size() = " + elems.size());
            for (final Element elem : elems) {
                final Product product = ParseProduct.parse(Downloader.baseUri + elem.attr("href"), loginCookies);
                list.add(product);
            }
            nextUrl = Downloader.baseUri + doc.select("span.next > a").last().attr("href");
        }
        return list;
    }

    public static List<String> parse(final Document doc) {
        return null;
    }

    public static void main(final String[] args) throws IOException {
//        final Map<String, String> loginCookies = Downloader.getCookies();
        final String url = "http://alena-shop.ru/wear/devochkam";
        final List<Product> products = ParseProductList.parse(url, null);
        for (final Product product : products) {
            product.print();
        }
        System.out.println("products.size() = " + products.size());
    }
}
