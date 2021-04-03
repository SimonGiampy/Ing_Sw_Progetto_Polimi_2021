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


	public XMLParserDraft() {
	}

	/**
	 * Opens the xml configuration file and read all the tiles of the faithTrack
	 * @param fileName path of the xml file
	 * @return the arraylist of tiles
	 */
	public ArrayList<Tile> readTiles(String fileName) {

		ArrayList<Tile> tileList = new ArrayList<>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File (fileName));
			document.getDocumentElement().normalize();

			//Get list of all the tiles
			NodeList tileNodeList = document.getElementsByTagName("tile");

			//Iterate through the list
			for(int i = 1; i < tileNodeList.getLength(); i++) {
				Node tileNode = tileNodeList.item(i);

				ArrayList<Boolean> insideVatican = new ArrayList<>();

				//List of details of the single tile
				NodeList tileDetailList = tileNode.getChildNodes();

				//Get victory points
				Element victory = (Element) tileDetailList.item(1);
				int victoryPoints = Integer.parseInt(victory.getAttribute("points"));

				//Get value of papalSpace
				Element papal = (Element) tileDetailList.item(3);
				boolean papalSpace = Boolean.parseBoolean(papal.getAttribute("value"));

				//List of boolean for the inside vatican attribute
				NodeList vaticanList = tileDetailList.item(5).getChildNodes();
				for (int j = 1; j < vaticanList.getLength(); j += 2) {
					Node vaticanNode = vaticanList.item(j);
					Element vatican = (Element) vaticanNode;
					insideVatican.add(Boolean.parseBoolean(vatican.getAttribute("value")));
				}
				tileList.add(new Tile(victoryPoints, insideVatican, papalSpace));
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return tileList;
	}

	/**
	 * Reads the value in victory points of every vatican report
	 * @param fileName path of the xml file
	 * @return arrayList of integer containing the victory points for every report
	 */
	public ArrayList<Integer> readReportPoints(String fileName){

		ArrayList<Integer> reportPoints = new ArrayList<>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File (fileName));
			document.getDocumentElement().normalize();

			//Get list of points for every vatican report
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
		return reportPoints;
	}

	/**
	 * Reads all the development cards from the xml file
	 * @param fileName name of the xml file
	 * @return the development card deck
	 */
	public ArrayList<DevelopmentCard> readDevCards(String fileName) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		ArrayList<DevelopmentCard> cardsDeck = new ArrayList<>();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(fileName));
			document.getDocumentElement();

			//Get the list of dev cards
			NodeList cardList = document.getElementsByTagName("card");

			//Iterate through all the cards
			for(int i = 0; i < cardList.getLength(); i++){
				Node cardNode = cardList.item(i);
				if(cardNode.getNodeType() == Node.ELEMENT_NODE){

					//Data required to instantiate the card
					ArrayList<Resources> input = new ArrayList<>();
					ArrayList<Resources> output = new ArrayList<>();
					ArrayList<Resources> requirements = new ArrayList<>();
					
					int faithOutput, level, victoryPoints;
					Colors color;

					//Get list of all the different data
					NodeList cardDetailList = cardNode.getChildNodes();

					//Get the requirements
					Node requirementsNode = cardDetailList.item(1);
					nodesExtraction(requirements, requirementsNode);

					//Get the list of input resources
					Node inputNode = cardDetailList.item(3);
					nodesExtraction(input, inputNode);

					//Get the list of output resources
					Node outputNode = cardDetailList.item(5);
					nodesExtraction(output, outputNode);

					//Get faith output
					Element faithOutputElement = (Element) cardDetailList.item(7);
					faithOutput = Integer.parseInt(faithOutputElement.getAttribute("output"));

					//Get level
					Element levelElement = (Element) cardDetailList.item(9);
					level = Integer.parseInt(levelElement.getAttribute("value"));

					//Get victory points
					Element victoryPointsElement = (Element) cardDetailList.item(11);
					victoryPoints = Integer.parseInt(victoryPointsElement.getAttribute("points"));

					//Get color
					Element colorElement = (Element) cardDetailList.item(13);
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
	 * Reads the list of resources and add those in the specified arrayList
	 * @param resourcesArray array that will contain the read resources
	 * @param specifiedNode parent node of the chosen list of resources (requirements, input or output)
	 */
	private void nodesExtraction(ArrayList<Resources> resourcesArray, Node specifiedNode) {

		NodeList inputList = specifiedNode.getChildNodes();
		for(int j = 1; j < inputList.getLength(); j+=2){
			Node singleNode = inputList.item(j);
			Element singleElement = (Element) singleNode;
			resourcesArray.add(Resources.valueOf(
					String.valueOf(singleElement.getAttribute("value")).toUpperCase()));
		}
	}
	
	/**
	 * reads the Leader cards configuration from the xml file
	 * @param fileName full path of the xml configuration file
	 * @return the list of leader cards
	 */
	public ArrayList<LeaderCard> readLeaderCards(String fileName) {
		
		ArrayList<LeaderCard> leaderCardArrayList = new ArrayList<>();
		LeaderCardBuilder leaderCardBuilder;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File (fileName));
			document.getDocumentElement().normalize();
			
			//Get list of all the leader cards
			NodeList leaderCardsNodeList = document.getElementsByTagName("leader_cards");
			Node leaderCardsNode = leaderCardsNodeList.item(0);
			leaderCardsNodeList = leaderCardsNode.getChildNodes();
			
			//Iterate through the list
			for (int i = 1; i < leaderCardsNodeList.getLength(); i++) {
				Node leaderCardNode = leaderCardsNodeList.item(i);
				leaderCardBuilder = new LeaderCardBuilder();
				
				if (leaderCardNode.getNodeType() == Node.ELEMENT_NODE) {
					NodeList leaderCardElements = leaderCardNode.getChildNodes();
					
					Element victoryElementNode = (Element) (leaderCardElements.item(1));
					int victoryPoints = Integer.parseInt(victoryElementNode.getAttribute("points"));
					
					ArrayList<Resources> requirementsResourcesList = new ArrayList<>();
					ArrayList<CardRequirement> requirementsCardsList = new ArrayList<>();
					
					//List of the parameters of the resources requirement of the single leader card
					NodeList requirementsList = leaderCardElements.item(3).getChildNodes();
					
					// reading list of resources required
					NodeList resourcesList = requirementsList.item(1).getChildNodes();
					for (int res = 1; res < resourcesList.getLength(); res+=2) {
						Element resource = (Element) resourcesList.item(res);
						String resourceString = String.valueOf(resource.getAttribute("value")).toUpperCase();
						requirementsResourcesList.add(Resources.valueOf(resourceString));
					}
					
					// reading list of card requirements
					NodeList cardRequirementsList = requirementsList.item(3).getChildNodes();
					for (int res = 1; res < cardRequirementsList.getLength(); res+=2) {
						Element resource = (Element) cardRequirementsList.item(res);
						String color = resource.getAttribute("color").toUpperCase();
						int level = Integer.parseInt(resource.getAttribute("level"));
						CardRequirement req = new CardRequirement(Colors.valueOf(color), level);
						requirementsCardsList.add(req);
					}
					
					//updates leader card builder with new info from the xml file
					leaderCardBuilder = leaderCardBuilder.xmlData(victoryPoints, requirementsResourcesList, requirementsCardsList);
					
					//list of power abilities for a single leader card
					NodeList abilitiesNodeList = leaderCardElements.item(5).getChildNodes();
					
					//TODO: parse multiple power abilities for a single leader card
					
					//there should be an iteration mechanism for every power element
					Node powerParametersList = abilitiesNodeList.item(1);
					Element powerParameter = (Element) powerParametersList;
					
					switch (powerParameter.getAttribute("type")) {
						case "depot" -> {
							ArrayList<Resources> slotsResourcesList = new ArrayList<>();
							NodeList slotsNodeList = powerParameter.getChildNodes();
							
							for (int slot = 1; slot < slotsNodeList.getLength(); slot+=2) {
								Element slotElement = (Element) slotsNodeList.item(slot);
								String slotString = String.valueOf(slotElement.getAttribute("value")).toUpperCase();
								slotsResourcesList.add(Resources.valueOf(slotString));
							}
							
							leaderCardBuilder = leaderCardBuilder.xmlData("depot", slotsResourcesList);
						}
						case "white marble" -> {
							ArrayList<Resources> marblesResourcesList = new ArrayList<>();
							
							Node whiteMarblesCountNode = powerParameter.getChildNodes().item(1);
							String white = ((Element) whiteMarblesCountNode).getAttribute("number");
							int whiteMarbles = Integer.parseInt(white);
							
							NodeList marblesNodeList = powerParameter.getChildNodes().item(3).getChildNodes();
							for (int marble = 1; marble < marblesNodeList.getLength(); marble+=2) {
								Element marbleElement = (Element) marblesNodeList.item(marble);
								String marbleString = String.valueOf(marbleElement.getAttribute("value")).toUpperCase();
								marblesResourcesList.add(Resources.valueOf(marbleString));
							}
							
							leaderCardBuilder = leaderCardBuilder.xmlData(marblesResourcesList, whiteMarbles);
						}
						case "production" -> {
							ArrayList<Resources> inputResourcesList = new ArrayList<>();
							ArrayList<Resources> outputResourcesList = new ArrayList<>();
							
							Node faithPointsOutputNode = powerParameter.getChildNodes().item(1);
							String faithOutputString = ((Element) faithPointsOutputNode).getAttribute("output");
							int faithOutput = Integer.parseInt(faithOutputString);
							
							NodeList inputNodeList = powerParameter.getChildNodes().item(3).getChildNodes();
							for (int res = 1; res < inputNodeList.getLength(); res+=2) {
								Element element = (Element) inputNodeList.item(res);
								String resString = String.valueOf(element.getAttribute("value")).toUpperCase();
								inputResourcesList.add(Resources.valueOf(resString));
							}
							
							NodeList outputNodeList = powerParameter.getChildNodes().item(5).getChildNodes();
							for (int res = 1; res < outputNodeList.getLength(); res+=2) {
								Element element = (Element) inputNodeList.item(res);
								String resString = String.valueOf(element.getAttribute("value")).toUpperCase();
								outputResourcesList.add(Resources.valueOf(resString));
							}
							
							leaderCardBuilder = leaderCardBuilder.xmlData(inputResourcesList, faithOutput, outputResourcesList);
						}
						case "discount" -> {
							ArrayList<Resources> resourcesArrayList = new ArrayList<>();
							NodeList resourcesNodeList = powerParameter.getChildNodes();
							for (int res = 1; res < resourcesNodeList.getLength(); res+=2) {
								Element element = (Element) resourcesNodeList.item(res);
								String resourceString = String.valueOf(element.getAttribute("value")).toUpperCase();
								resourcesArrayList.add(Resources.valueOf(resourceString));
							}
							
							leaderCardBuilder = leaderCardBuilder.xmlData("discount", resourcesArrayList);
						}
					}
					
					leaderCardArrayList.add(leaderCardBuilder.build());
					
				}
				
			}
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return leaderCardArrayList;
	}
	
	
	public void parseBaseProductionFromXML(String fileName) {
		ArrayList<Resources> inputResources = new ArrayList<>();
		ArrayList<Resources> outputResources = new ArrayList<>();
		int faithOutput;
		
		//TODO: parse base production parameters from xml
		
		//return new ProductionRules(inputResources, outputResources, faithOutput);
		
	}
	
	
}


