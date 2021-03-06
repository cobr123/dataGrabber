package ru.datagrabber.main;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.datagrabber.data.DataCell;
import ru.datagrabber.data.Product;
import ru.datagrabber.data.ProductCategoryType;
import ru.datagrabber.grabber.Downloader;
import ru.datagrabber.parser.ParseProductList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by cobr123 on 05.09.2015.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) throws IOException {
        // Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();

        logger.info("авторизация");
        final Map<String, String> loginCookies = Downloader.getCookies();
        final List<Product> list = new ArrayList<Product>();
        logger.info("товары для мальчиков");
        final List<Product> list1 = ParseProductList.parse(ProductCategoryType.BOYS, Downloader.baseUri + "/wear/malchikam", loginCookies);
        list.addAll(list1);
        logger.info("товары для девочек");
        final List<Product> list2 = ParseProductList.parse(ProductCategoryType.GIRLS, Downloader.baseUri + "/wear/devochkam", loginCookies);
        list.addAll(list2);
        logger.info("товары для малышей");
        final List<Product> list3 = ParseProductList.parse(ProductCategoryType.BABIES, Downloader.baseUri + "/wear/rost-56_rost-62_rost-68_rost-74_rost-80", loginCookies);
        list.addAll(list3);
        //logger.info("аксессуары");
        //final List<Product> list4 = ParseProductList.parse(Downloader.baseUri + "/accessory", loginCookies);
        //list.addAll(list4);
        logger.info("объединяем категории товаров добавленных несколько раз");
        final List<Product> uniqList = mergeProductCategories(list);
        logger.info("создаём эксель и сохораняем в файл на рабочем столе");
        createExcel(uniqList);
    }

    private static List<Product> mergeProductCategories(final List<Product> list) {
        final Map<String, Product> uniq = new HashMap<String, Product>();

        for (final Product product : list) {
            final Product ex = uniq.get(product.getId());
            if (ex == null) {
                uniq.put(product.getId(), product);
            } else {
                ex.addCategories(product.getCategory());
            }
        }

        return new ArrayList<Product>(uniq.values());
    }

    private static void createExcel(final List<Product> list) throws IOException {
        // keep 100 rows in memory, exceeding rows will be flushed to disk
        final SXSSFWorkbook wb = new SXSSFWorkbook(100);
        final Sheet sh = wb.createSheet();
        int rownum = 0;
        //заголовки
        createTitle(sh, rownum);
        ++rownum;
        //данные
        for (final Product product : list) {
            final Row row = sh.createRow(rownum);
            createCellsByProduct(row, product);
            ++rownum;
        }
        String desktopFolder = System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar;
        if (!new File(desktopFolder).exists()) {
            desktopFolder = System.getProperty("user.home") + File.separatorChar;
        }
        final String filePath = desktopFolder + Downloader.baseUri.replace("http://", "").replace("/", "") + ".xlsx";
        try (final FileOutputStream out = new FileOutputStream(filePath)) {
            wb.write(out);
            out.close();
        }

        // dispose of temporary files backing this workbook on disk
        wb.dispose();
    }

    private static void createCellsByProduct(final Row row, final Product product) {
        row.createCell(DataCell.EXT_ID.ordinal()).setCellValue(product.getId());
        row.createCell(DataCell.PRODUCT_URL.ordinal()).setCellValue(product.getUrl());
        row.createCell(DataCell.NAME.ordinal()).setCellValue(product.getName());
        row.createCell(DataCell.ARTICLE.ordinal()).setCellValue(product.getArticle());
        row.createCell(DataCell.DESC.ordinal()).setCellValue(product.getDesc());

        row.createCell(DataCell.PRICE.ordinal()).setCellValue(product.getPrice());
        row.createCell(DataCell.SIZE.ordinal()).setCellValue(product.getSizes());

        row.createCell(DataCell.IMG_MAIN.ordinal()).setCellValue(product.getImgMain());
        row.createCell(DataCell.IMG_OTHERS.ordinal()).setCellValue(product.getImgOthers());
        row.createCell(DataCell.CATEGORIES.ordinal()).setCellValue(product.getCategories());

        row.createCell(DataCell.MANUFACTURER_COUNTRY.ordinal()).setCellValue("Россия и СНГ");
        row.createCell(DataCell.CITY_FROM.ordinal()).setCellValue("Санкт-Петербург");
    }

    private static void createTitle(final Sheet sh, final int rownum) {
        final Row row = sh.createRow(rownum);
        for (final DataCell cap : DataCell.values()) {
            final Cell cell = row.createCell(cap.ordinal());
            cell.setCellValue(cap.getCaption());
        }
    }
}
