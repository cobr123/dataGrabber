package ru.datagrabber.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by cobr123 on 30.08.2015.
 */
public final class Downloader {
    private static final Logger logger = LoggerFactory.getLogger(Downloader.class);
    private static final String baseUri = "http://alena-shop.ru/";

    public static File get(final String url) throws IOException {
        return get(url, "");
    }

    public static String getClearedUrl(final String url, final String referrer) {
        String clearedUrl;
        if (referrer != null && !referrer.isEmpty()) {
            final String[] parts = url.split("/");
            final String page = File.separator + parts[parts.length - 2] + File.separator + parts[parts.length - 1];
            clearedUrl = referrer.replace("http://", "").replace("/", File.separator) + page;
        } else {
            clearedUrl = url.replace("http://", "").replace("/", File.separator);
        }
        return clearedUrl;
    }

    public static void invalidateCache(final String url) throws IOException {
        invalidateCache(url, "");
    }

    public static String getFileToSave(final String url) throws IOException {
        return getFileToSave(url, "");
    }

    public static String getFileToSave(final String url, final String referrer) throws IOException {
        final String clearedUrl = getClearedUrl(url, referrer);
        return Utils.getDir() + clearedUrl + ".html";
    }

    public static void invalidateCache(final String url, final String referrer) throws IOException {
        final String fileToSave = getFileToSave(url, referrer);
        final File file = new File(fileToSave);
        if (file.exists()) {
            file.delete();
        }
    }

    public static File get(final String url, final String referrer) throws IOException {
        final String clearedUrl = getClearedUrl(url, referrer);
        final String fileToSave = Utils.getDir() + clearedUrl + ".html";
        final File file = new File(fileToSave);
        if (file.exists() && Utils.equalsWoTime(new Date(file.lastModified()), new Date())) {
            logger.trace("Взят из кэша: {}", file.getAbsolutePath());
        } else {
            logger.trace("Запрошен адрес: {}", url);

            final int maxTriesCnt = 3;
            for (int tries = 1; tries <= maxTriesCnt; ++tries) {
                try {
                    final Connection conn = Jsoup.connect(url);
                    if (referrer != null && !referrer.isEmpty()) {
                        logger.trace("referrer: {}", referrer);
                        conn.referrer(referrer);
                    }
                    final Document doc = conn.get();
                    Utils.writeFile(fileToSave, doc.outerHtml());
                    break;
                } catch (final IOException e) {
                    logger.error("Ошибка при запросе, попытка #{}: {}", tries, url);
                    logger.error("Ошибка: ", e);
                    if (maxTriesCnt == tries) {
                        throw new IOException(e);
                    } else {
                        waitSecond(3);
                    }
                }
            }
        }
        return file;
    }

    public static void waitSecond(final long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static Document getDoc(final String url) throws IOException {
        return getDoc(url, "");
    }

    public static Document getDoc(final String url, final String referrer) throws IOException {
        final File input = Downloader.get(url, referrer);
        return Jsoup.parse(input, "UTF-8", baseUri);
    }

    public static void main(final String[] args) throws IOException {
        final String url = baseUri + "wear/devochkam";
        final Document doc = Jsoup.connect(url).get();
        Utils.writeFile(getFileToSave(url), doc.outerHtml());
    }
}
