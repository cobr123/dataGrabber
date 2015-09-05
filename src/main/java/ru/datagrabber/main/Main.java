package ru.datagrabber.main;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import ru.datagrabber.data.DataCell;
import ru.datagrabber.data.Product;
import ru.datagrabber.grabber.Downloader;
import ru.datagrabber.parser.ParseProductList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cobr123 on 05.09.2015.
 */
public class Main {
    public static void main(final String[] args) throws IOException {
        final List<Product> list = new ArrayList<Product>();
        //товары для мальчиков
        final List<Product> list1 = ParseProductList.parse(Downloader.baseUri + "wear/malchikam");
        list.addAll(list1);
        //товары для девочек
        final List<Product> list2 = ParseProductList.parse(Downloader.baseUri + "wear/devochkam");
        list.addAll(list2);
        //товары для малышей
        final List<Product> list3 = ParseProductList.parse(Downloader.baseUri + "wear/rost-56_rost-62_rost-68_rost-74_rost-80");
        list.addAll(list3);
        //аксессуары
        final List<Product> list4 = ParseProductList.parse(Downloader.baseUri + "accessory");
        list.addAll(list4);
        //создаём эксель и сохораняем в файл на рабочем столе
        createExcel(list);
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
        final String filePath = System.getProperty("user.home") + "/Desktop/" + Downloader.baseUri.replace("http://", "").replace("/", "") + ".xlsx";
        try (final FileOutputStream out = new FileOutputStream(filePath)) {
            wb.write(out);
            out.close();
        }

        // dispose of temporary files backing this workbook on disk
        wb.dispose();
    }

    private static void createCellsByProduct(final Row row,final Product product) {
        row.createCell(DataCell.ARTICLE.ordinal()).setCellValue(product.getArticle());
        row.createCell(DataCell.DESC.ordinal()).setCellValue(product.getDesc());
        row.createCell(DataCell.PRICE.ordinal()).setCellValue(product.getPrice());
        row.createCell(DataCell.NAME.ordinal()).setCellValue(product.getName());
    }

    private static void createTitle(final Sheet sh, final int rownum) {
        final Row row = sh.createRow(rownum);
        for (final DataCell cap: DataCell.values()) {
            final Cell cell = row.createCell(cap.ordinal());
            cell.setCellValue(cap.getCaption());
        }
    }
}
