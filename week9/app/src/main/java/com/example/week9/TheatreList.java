package com.example.week9;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TheatreList {
    ArrayList<Theatre> theatres = new ArrayList<Theatre>();


    public TheatreList() {}


    public ArrayList<Theatre> getTheatres() {
        return theatres;
    }

    public void fetchAllTheatres() {
        theatres.clear();

        try {
            URL url = new URL("https://www.finnkino.fi/xml/TheatreAreas/");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("TheatreArea");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element)nodeList.item(i);
                NodeList idList = element.getElementsByTagName("ID");
                Element idElement = (Element) idList.item(0);

                NodeList nameList = element.getElementsByTagName("Name");
                Element nameElement = (Element) nameList.item(0);

                theatres.add(new Theatre(Integer.parseInt(idElement.getTextContent()), nameElement.getTextContent()));
            }
        } catch (Exception e) {
            Log.e("XMLException", e.toString());
        }
    }
}
