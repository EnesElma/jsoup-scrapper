package com.iu.house.dataCrawl;

import com.iu.house.commons.IPGenerator;
import com.iu.house.commons.SayfaNo;
import com.iu.house.dataClean.Cleaner;
import com.iu.house.database.MongoMain;
import com.iu.house.producer.URLProducer;
import com.iu.house.sehirler.Sehirler;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.List;


public class Scrapper {
    public static int sayac=1;  //ilan sayısını tutar
    public static void main(String[] args) throws Exception {

        String [] sehirler=Sehirler.sehir;
        MongoMain sendDB=new MongoMain();

        for (String il:sehirler){
            String url="https://www.hurriyetemlak.com/"+il.trim()+"-satilik?sortField=PRICE&sortDirection=ASC&p31=25000&p33=1&page=1";
            int sayfaNo=Integer.parseInt(new SayfaNo().sayfaNo(url));
            System.out.println(sayfaNo);

            for (int i=1; i<=sayfaNo; i++){

                url="https://www.hurriyetemlak.com/"+il.trim()+"-satilik?sortField=PRICE&sortDirection=ASC&p31=25000&p33=1&page="+i;

                System.out.println(url);

                String newIP= IPGenerator.getNewIP();

                Document doc=new URLProducer().urlProduce(url,newIP);
                Elements webPage=doc.select("div.listing-item");

                Cleaner cleaner=new Cleaner(webPage,il);
                List<org.bson.Document> konutlar=cleaner.clean();
                sendDB.mongoConnect().insertMany(konutlar);
                System.out.println(sayac);


                Thread.sleep(2000);
            }
        }
    }
}
