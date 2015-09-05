package ru.datagrabber.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.datagrabber.data.Product;
import ru.datagrabber.grabber.Downloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cobr123 on 05.09.2015.
 */
public class ParseProduct {
    private static final Logger logger = LoggerFactory.getLogger(ParseProduct.class);

    public static Product parse(final String url, final Map<String, String> loginCookies) throws IOException {
        final Document doc = Downloader.getDoc(url, loginCookies);
        return ParseProduct.parse(doc, url);
    }

    public static Product parse(final Document doc, final String url) {
        final String name = doc.select("h1.item-title").first().text();
        final String article = doc.select("p.item-number").first().text().replace("Артикул: ", "");
        final String price = doc.select("p.item-price").first().text().replaceAll(" Р$", "");
        final String desc = doc.select("ul.item-params").first().text();

        final Elements imgElems = doc.select("div.image-radio > label > img");
        logger.trace("imgElems.size() = " + imgElems.size());
        final List<String> imgs = new ArrayList<String>(imgElems.size());
        for (final Element elem : imgElems) {
            imgs.add(elem.attr("src"));
        }

        final Elements sizeElems = doc.select("div.size-radio > label > span");
        logger.trace("sizeElems.size() = " + sizeElems.size());
        final List<String> sizes = new ArrayList<String>(sizeElems.size());
        for (final Element elem : sizeElems) {
            sizes.add(elem.text());
        }
        return new Product(url, name, article, price, desc, imgs, sizes);
    }

    public static void main(final String[] args) throws IOException {
//        final Map<String, String> loginCookies = Downloader.getCookies();
        final String url = Downloader.baseUri + "/positions/122";
        System.out.println(Downloader.getFileToSave(url));
        final Product product = ParseProduct.parse(url, null);
        product.print();
    }
}
