package com.iu.house.commons;

import com.iu.house.producer.URLProducer;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class SayfaNo {          //her bir ilin kaç sayfa ilanı olduğunu buluruz
    public String sayfaNo(String url) throws IOException, InterruptedException {

        String newIP= IPGenerator.getNewIP();
        Document doc=new URLProducer().urlProduce(url,newIP);

        String sayfaNo;
        if (doc.select("ul.pagination").text().contains(" … ")) {
            String[] text1 = doc.select("ul.pagination").text().split(" … ");
            String[] text2 = text1[1].split(" Sonraki ");
            sayfaNo = text2[0];
        }
        else {
            sayfaNo="0";    //Eğer o şehirde hiç ilan yoksa sayfaNo=0 yap
        }

        return sayfaNo;
    }
}
