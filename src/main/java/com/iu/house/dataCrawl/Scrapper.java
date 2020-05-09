package com.iu.house.dataCrawl;

import com.iu.house.commons.IPGenerator;
import com.iu.house.commons.SayfaNo;
import com.iu.house.dataClean.Cleaner;
import com.iu.house.database.MongoMain;
import com.iu.house.producer.URLProducer;
import com.iu.house.sehirler.Sehirler;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.List;

public class Scrapper {

    public static int sayac=1;                                  //ilan numarasını tutar
    public static void main(String[] args) throws Exception {

        String [] sehirler=Sehirler.sehir;

        MongoClient mongoClient = new MongoMain().mongoConnect();
        MongoDatabase database = mongoClient.getDatabase("HomeRecords");
        final MongoCollection<org.bson.Document> sendDatabase = database.getCollection("records");  //records collectiona bağlantı


        for (String il:sehirler){               //Şehirleri döngü ile tek tek tarıyoruz
            String url="https://www.hurriyetemlak.com/"+il.trim()+"-satilik?sortField=PRICE&sortDirection=ASC&p31=25000&p33=1&page=1";

            int sayfaNo=Integer.parseInt(new SayfaNo().sayfaNo(url));   //her şehirdeki ilanların sayfa sayısını SayfaNo sınıfı yardımıyla sayfaNo değişkenine atıyoruz
            System.out.println("SayfaNO= "+sayfaNo);

            for (int i=1; i<=sayfaNo; i++){     //her sayfayı tek tek tarıyoruz

                url="https://www.hurriyetemlak.com/"+il.trim()+"-satilik?sortField=PRICE&sortDirection=ASC&p31=25000&p33=1&page="+i;

                System.out.println(url);

                String newIP= IPGenerator.getNewIP();
                Document doc=new URLProducer().urlProduce(url,newIP);
                Elements webPage=doc.select("div.listing-item");

                Cleaner cleaner=new Cleaner(webPage,il);    //sayfa url den alınan veriyi temizleyip cleaner nesnesine atıyoruz.

                List<org.bson.Document> konutlar=cleaner.clean();


                for (org.bson.Document konut:konutlar){     //cleaner nesnesindeki dataları mongoya kaydetmek için kullanılan döngü

                    int maxConnectTry=0;
                    while (maxConnectTry!=5) {          //mongo bağlantısı 5 defa tekrarlanırsa çıkış yapılır
                        try {
                            try {
                                sendDatabase.insertOne(konut);
                            }catch (MongoWriteException e){     //tekrarlı kopya ilanları atlamak için kullanılan exception
                                System.out.println(konut);
                            }
                            break;
                        }catch (Exception e){               //mongo bağlantı ve soket hataları için exception
                            Thread.sleep(6000);     //mongo hatası olursa 6saniye dinlen
                            maxConnectTry++;
                            System.err.println(e+"\n\nMongoSend Hatası");
                        }
                    }
                }

                System.out.println(sayac+".kayit");
                Thread.sleep(1500);
            }
        }
    }
}
