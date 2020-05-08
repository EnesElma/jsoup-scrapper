package com.iu.house.producer;

import com.iu.house.commons.SSLHelper;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class URLProducer {
    public Document urlProduce(String url, String newIP) throws IOException {

        Document doc = SSLHelper.getConnection(url.trim())
                .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36 " +
                        "RuxitSynthetic/1.0 v4979915562 t38550")
                .timeout(30000)
                .header("Connection", "keep-alive")
                .header("X-Forwarded-For", newIP)
                .header("Referer", url.trim())
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,**;q=0.8")
                .header("Cache-Control", "max-age=0")
                .get();

        return doc;
    }
}
