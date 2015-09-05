package ru.datagrabber.parser;

import org.jsoup.nodes.Document;
import ru.datagrabber.data.Product;
import ru.datagrabber.grabber.Downloader;

import java.io.IOException;
import java.util.Map;

/**
 * Created by cobr123 on 05.09.2015.
 */
public class ParseProduct {
    public static Product parse(final String url, final Map<String, String> loginCookies) throws IOException {
        final Document doc = Downloader.getDoc(url, loginCookies);
        return ParseProduct.parse(doc);
    }

    public static Product parse(final Document doc) {
        final String name = doc.select("h1.item-title").first().text();
        final String article = doc.select("p.item-number").first().text().replace("Артикул: ","");
        final String price = doc.select("p.item-price").first().text();
        final String desc = doc.select("ul.item-params").first().text();

        return new Product(name, article, price, desc);
    }

    public static void main(final String[] args) throws IOException {
//        final Map<String, String> loginCookies = Downloader.getCookies();
        final String url = "http://alena-shop.ru/positions/122";
        System.out.println(Downloader.getFileToSave(url));
        final Product product = ParseProduct.parse(url, null);
        product.print();
    }
}
