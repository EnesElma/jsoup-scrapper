package com.iu.house.dataCrawl;

import com.iu.house.dataClean.Cleaner;
import com.iu.house.database.MongoMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;




public class Scrapper {
    public static void main(String[] args) throws Exception {
        Document doc= Jsoup.connect("https://www.hurriyetemlak.com/satilik").get();
        Elements webPage=doc.select("div.listing-item");

        Cleaner cleaner=new Cleaner(webPage);

        MongoMain sendDB=new MongoMain();

        try{
            sendDB.mongoConnect().insertMany(cleaner.clean());
        }catch (Exception e){

        }



    }
}
