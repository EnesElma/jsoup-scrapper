package com.iu.house.dataCrawl;

import com.iu.house.dataClean.Cleaner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;




public class Scrapper {
    public static void main(String[] args) throws Exception {
        Document doc= Jsoup.connect("https://www.hurriyetemlak.com/satilik").get();
        Elements blok=doc.select("div.listing-item");

        Cleaner cleaner=new Cleaner(blok);
        cleaner.clean();


    }
}
