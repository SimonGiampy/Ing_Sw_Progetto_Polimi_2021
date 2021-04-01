package it.polimi.ingsw;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLParserDraft {

	public XMLParserDraft() {
	}

	public void readTiles(String filename) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(filename));
			document.getDocumentElement();

			NodeList tileList = document.getElementsByTagName("tile");

			for(int i = 0; i < tileList.getLength(); i++){
				Node tile = tileList.item(i);
				
				System.out.println("tile: "+ ((Element) tile).getAttribute("number"));
				
				//List of children nodes
				NodeList infoList = tile.getChildNodes();
				System.out.println("length = " + infoList.getLength());
				Element victory = (Element) infoList.item(1);
				System.out.println("victory points = " + victory.getAttribute("points"));
				Element papal = (Element) infoList.item(3);
				System.out.println("papal space = " + papal.getAttribute("value"));
				
				NodeList vaticans = infoList.item(5).getChildNodes();
				Element vatican1 = (Element) vaticans.item(1);
				System.out.println("vatican 1 = "+ vatican1.getAttribute("value"));
				Element vatican2 = (Element) vaticans.item(3);
				System.out.println("vatican 2 = "+ vatican2.getAttribute("value"));
				Element vatican3 = (Element) vaticans.item(5);
				System.out.println("vatican 3 = "+ vatican3.getAttribute("value"));
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}
}

/*
	public void readCards(String[] filename){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	}
}

 */
