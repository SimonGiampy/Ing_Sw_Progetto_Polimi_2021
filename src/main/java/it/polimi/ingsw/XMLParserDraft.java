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
import java.net.URL;
import java.util.ArrayList;

public class XMLParserDraft {

	public XMLParserDraft() {
	}

	//for test
	public FaithTrack readTiles(String fileName) {

		ArrayList<Tile> tileList = new ArrayList<>();
		ArrayList<Integer> reportPoints = new ArrayList<>();

		URL resource = getClass().getClassLoader().getResource(fileName);
		System.out.println(resource);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(String.valueOf(fileName)));
			document.getDocumentElement();

			NodeList tileNodeList = document.getElementsByTagName("tile");

			for(int i = 0; i < tileNodeList.getLength(); i++){
				Node tileNode = tileNodeList.item(i);
				if(tileNode.getNodeType() == Node.ELEMENT_NODE) {

					ArrayList<Boolean> insideVatican = new ArrayList<>();

					Element tileElement = (Element) tileNode;

					//List of children nodes
					NodeList tileDetailList = tileElement.getChildNodes();

					Element victory = (Element)tileDetailList.item(1);
					int victoryPoints = Integer.parseInt(victory.getAttribute("points"));
					Element papal = (Element) tileDetailList.item(3);
					boolean papalSpace = Boolean.parseBoolean(papal.getAttribute("value"));

					//List of boolean for the inside vatican attribute
					NodeList vaticanList = tileDetailList.item(5).getChildNodes();
					for(int j = 0; j < vaticanList.getLength(); j++) {
						Node vaticanNode = vaticanList.item(j);
						if(vaticanNode.getNodeType() == Node.ELEMENT_NODE) {
							Element vatican = (Element) vaticanNode;
							insideVatican.add(Boolean.valueOf(vatican.getAttribute("value")));
						}

					}
					tileList.add(new Tile(victoryPoints, insideVatican, papalSpace));
				}
			}

			NodeList reportPointsList = document.getElementsByTagName("report");
			Node reportPointsNode = reportPointsList.item(0);
			NodeList reportList = reportPointsNode.getChildNodes();
			for(int i = 0; i < reportPointsList.getLength(); i++){
				Node reportNode = reportList.item(i);
				if(reportNode.getNodeType() == Node.ELEMENT_NODE){
					Element report = (Element) reportNode;
					reportPoints.add(Integer.parseInt(report.getAttribute("value")));
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return new FaithTrack(tileList, reportPoints);
	}

}


