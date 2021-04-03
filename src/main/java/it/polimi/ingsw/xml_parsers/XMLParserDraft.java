package it.polimi.ingsw.xml_parsers;

import it.polimi.ingsw.*;
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
import java.util.ArrayList;

public class XMLParserDraft {
	
	//TODO: reorder code and add more clarity to the code. Also add some inline comments and javadoc

	public XMLParserDraft() {
	}

	//for test
	public FaithTrack readTiles(String fileName) {

		ArrayList<Tile> tileList = new ArrayList<>();
		ArrayList<Integer> reportPoints = new ArrayList<>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File (fileName));
			document.getDocumentElement().normalize();

			NodeList tileNodeList = document.getElementsByTagName("tile");

			for(int i = 1; i < tileNodeList.getLength(); i++){
				Node tileNode = tileNodeList.item(i);
				
				if(tileNode.getNodeType() == Node.ELEMENT_NODE) {

					ArrayList<Boolean> insideVatican = new ArrayList<>();

					//List of children nodes
					NodeList tileDetailList = tileNode.getChildNodes();

					Element victory = (Element) tileDetailList.item(1);
					int victoryPoints = Integer.parseInt(victory.getAttribute("points"));
					Element papal = (Element) tileDetailList.item(3);
					boolean papalSpace = Boolean.parseBoolean(papal.getAttribute("value"));

					//List of boolean for the inside vatican attribute
					NodeList vaticanList = tileDetailList.item(5).getChildNodes();
					for(int j = 1; j < vaticanList.getLength(); j+=2) {
						Node vaticanNode = vaticanList.item(j);
						
							Element vatican = (Element) vaticanNode;
							insideVatican.add(Boolean.parseBoolean(vatican.getAttribute("value")));
						

					}
					tileList.add(new Tile(victoryPoints, insideVatican, papalSpace));
				}
			}

			
			NodeList reportPointsNodeList = document.getElementsByTagName("report_points");
			Node reportPointsNode = reportPointsNodeList.item(0);
			NodeList reportList = reportPointsNode.getChildNodes();
			reportPointsNode = reportList.item(1);
			reportList = reportPointsNode.getChildNodes();
			for(int i = 1; i < reportList.getLength(); i+=2){
				Node reportNode = reportList.item(i);
				
					Element report = (Element) reportNode;
					reportPoints.add(Integer.parseInt(report.getAttribute("points")));
				
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return new FaithTrack(tileList, reportPoints);
	}

	//for test
	public ArrayList<DevelopmentCard> readDevCards(String fileName) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		ArrayList<DevelopmentCard> cardsDeck = new ArrayList<>();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(fileName));
			document.getDocumentElement();

			//list of dev cards
			NodeList cardList = document.getElementsByTagName("card");

			for(int i = 0; i < cardList.getLength(); i++){
				Node cardNode = cardList.item(i);
				if(cardNode.getNodeType() == Node.ELEMENT_NODE){

					//data to instantiate the card
					ArrayList<Resources> input = new ArrayList<>();
					ArrayList<Resources> output = new ArrayList<>();
					ArrayList<Resources> requirements = new ArrayList<>();
					
					int faithOutput, level, victoryPoints;
					Colors color;

					//children
					NodeList cardDetailList = cardNode.getChildNodes();

					//get list of requirements
					Node requirementsNode = cardDetailList.item(1);
					nodesExtraction(requirements, requirementsNode);
					//get list of input resources
					Node inputNode = cardDetailList.item(3);
					nodesExtraction(input, inputNode);
					//get list of output resources
					Node outputNode = cardDetailList.item(5);
					nodesExtraction(output, outputNode);
					//get faith output
					Node faithOutputNode = cardDetailList.item(7);
					Element faithOutputElement = (Element) faithOutputNode;
					faithOutput = Integer.parseInt(faithOutputElement.getAttribute("output"));
					//get level
					Node levelNode = cardDetailList.item(9);
					Element levelElement = (Element) levelNode;
					level = Integer.parseInt(levelElement.getAttribute("value"));
					//get victory points
					Node victoryPointsNode = cardDetailList.item(11);
					Element victoryPointsElement = (Element) victoryPointsNode;
					victoryPoints = Integer.parseInt(victoryPointsElement.getAttribute("points"));
					//get color
					Node colorNode = cardDetailList.item(13);
					Element colorElement = (Element) colorNode;
					color = Colors.valueOf(String.valueOf(colorElement.getAttribute("value")).toUpperCase());

					//Instantiate production rule and card
					cardsDeck.add(new DevelopmentCard(level, color, victoryPoints, requirements,
							new ProductionRules(input, output,faithOutput)));
				}
			}


		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return cardsDeck;
	}
	
	/**
	 *
	 * @param input
	 * @param inputNode
	 */
	private void nodesExtraction(ArrayList<Resources> input, Node inputNode) {
		NodeList inputList = inputNode.getChildNodes();
		for(int j = 0; j < inputList.getLength(); j++){
			Node singleInputNode = inputList.item(j);
			if(singleInputNode.getNodeType() == Node.ELEMENT_NODE){
				Element singleInputElement = (Element) singleInputNode;
				input.add(Resources.valueOf(String.valueOf(singleInputElement.
						getAttribute("value")).toUpperCase()));
			}
		}
	}
	
	
}


