package com.iu.house.dataClean;

import org.bson.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Cleaner {
    private Elements pageData;              //Bir sayfadaki tüm veriler

    public Cleaner(Elements pageData) {
        this.pageData = pageData;
    }


    public List<Document> clean() {
        List<Document> konutlar= new ArrayList<Document>();


        String konut ="";
        int i = 1;
        for (Element singleAdvert : pageData) {                     //singleAdvert = sayfada ki ilanlardan biri
            if (singleAdvert.select(".floortype").text().equals(""))    //eğer kat numarası yoksa çoklu ilandır, ilan atlanır
                continue;
            else {


                String[] ucret1 = singleAdvert.select(".list-view-price").text().split(" ");     //"TL" yazısını silme
                String[] ucret2 = ucret1[0].split(",");                                         //virgülleri silme
                String ucretSon = "";
                for (String s : ucret2) {
                    ucretSon = ucretSon + s;                                                    //birleştirme
                }

                if (!ucret1[1].equals("TL")) {                                                    //Eğer para birimi TL değilse 6 ile çarp
                    int a = Integer.parseInt(ucretSon);
                    a = a * 6;
                    ucretSon = String.valueOf(a);
                }

                String[] m_kare1 = singleAdvert.select(".squareMeter").text().split(" ");     //"m2" yazısı silme
                String m_kareSon = m_kare1[0];

                String bina_yasiSon;
                if (singleAdvert.select(".buildingAge").text().equals("Sıfır Bina"))              //"Sıfır bina" yerine direkt 0 yazma
                    bina_yasiSon = "0";
                else {
                    String[] bina_yasi1 = singleAdvert.select(".buildingAge").text().split(" ");  //"yaşında" kelimesini silme
                    bina_yasiSon = bina_yasi1[0];
                }


                String kat_noSon;
                if (singleAdvert.select(".floortype").text().equals("Bahçe Katı")             //Yüksek giriş bahçe katı vs ise değeri 0 yap
                        || singleAdvert.select(".floortype").text().equals("Yüksek Giriş")
                        || singleAdvert.select(".floortype").text().equals("Giriş Katı")) {
                    kat_noSon = "0";
                } else if (singleAdvert.select(".floortype").text().equals("Villa Katı")) {
                    kat_noSon = "???";
                } else if(singleAdvert.select(".floortype").text().equals("Kot 1")
                        ||  singleAdvert.select(".floortype").text().equals("Kot 2")
                        ||  singleAdvert.select(".floortype").text().equals("Kot 3")
                        ||  singleAdvert.select(".floortype").text().equals("Kot 4")){
                    kat_noSon = "-1";
                }
                else {
                    String[] kat_no = singleAdvert.select(".floortype").text().split(". ");    //"kat" kelimesini silme
                    kat_noSon = kat_no[0];
                }

                String[] lokasyon = singleAdvert.select(".list-view-location").text().split(", ");
                String ilce = lokasyon[0];
                String mahalle = lokasyon[1];

                String odaSayisi=singleAdvert.select(".houseRoomCount").text();
                konut = i + ". " + ucretSon + " # " + singleAdvert.select(".houseRoomCount").text()
                        + " # " + m_kareSon + " # " + bina_yasiSon + " # " + kat_noSon + " # "
                        + ilce + " # " + mahalle;

                System.out.println(konut);

                Document doc=new Document("ucret",Integer.parseInt(ucretSon)).append("OdaSayisi",odaSayisi)
                        .append("metreKare",Integer.parseInt(m_kareSon))
                        .append("binaYasi",Integer.parseInt(bina_yasiSon))
                        .append("katNo",Integer.parseInt(kat_noSon))
                        .append("ilce",ilce).append("mahalle",mahalle);

                konutlar.add(doc);

                i++;
            }
        }

        return konutlar;
    }
}
