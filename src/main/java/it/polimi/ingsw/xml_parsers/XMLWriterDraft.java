package it.polimi.ingsw.xml_parsers;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XMLWriterDraft {

	public XMLWriterDraft(){

	}

	public void createAndWriteDocument(){

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.newDocument();

			/*
			<root>
				<main attribute = value>
					<child>textNode<child/>
				<main/>
			<root/>
			*/

			Element rootElement = document.createElement("root");
			Element mainElement = document.createElement("main");
			Element childOfMain = document.createElement("child");
			Text textInTheNode = document.createTextNode("textNode");

			mainElement.setAttribute("value", "10");
			childOfMain.appendChild(textInTheNode);
			mainElement.appendChild(childOfMain);
			rootElement.appendChild(mainElement);

			document.appendChild(rootElement);

			//output
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);

			StreamResult file = new StreamResult(new File("D:\\Progetto SwEng\\Masters-Of-Renaissance\\src\\main\\resources\\output.xml"));

			transformer.transform(source, file);



		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}

	}

	public void openAndEditDocument(String fileName){

		URL resource = getClass().getClassLoader().getResource(fileName);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder builder= factory.newDocumentBuilder();
			Document document = builder.parse(String.valueOf(resource));


			document.getDocumentElement();

			NodeList list = document.getElementsByTagName("root");
			Element root = (Element) list.item(0);

			NodeList mainList = root.getChildNodes();

			//Edit existing node
			Node mainNode = mainList.item(1);

			Element main = (Element) mainNode;

			Element newChild = document.createElement("new_child");
			newChild.setAttribute("value","5");
			main.appendChild(newChild);


			//Create new node
			Element mainElement2 = document.createElement("main");
			Element child = document.createElement("child2");
			child.setAttribute("value", "2");

			mainElement2.appendChild(child);
			root.appendChild(mainElement2);

			//Magic code for removing new lines
			XPath xp = XPathFactory.newInstance().newXPath();
			NodeList nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", document, XPathConstants.NODESET);

			for (int i=0; i < nl.getLength(); ++i) {
				Node node = nl.item(i);
				node.getParentNode().removeChild(node);
			}


			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);

			StreamResult file = new StreamResult(new File("D:\\Progetto SwEng\\Masters-Of-Renaissance\\src\\main\\resources\\output.xml"));

			transformer.transform(source, file);
			transformer.transform(source, new StreamResult(System.out));

		} catch (ParserConfigurationException | SAXException | IOException | TransformerException | XPathExpressionException e) {
			e.printStackTrace();
		}

	}

	public void readOutput(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse("D:\\Progetto SwEng\\Masters-Of-Renaissance\\src\\main\\resources\\output.xml");

			//document.normalize();

			document.getDocumentElement();

			NodeList mainList = document.getElementsByTagName("main");

			Element main1 = (Element) mainList.item(0);
			System.out.println("main " + main1.getAttribute("attribute"));
			NodeList childList = main1.getChildNodes();
			Element child1 = (Element) childList.item(1);
			System.out.println("child " + child1.getTextContent());
			Element child2 = (Element) childList.item(3);
			System.out.println("child2 " + child2.getAttribute("value"));

			Element main2 = (Element) mainList.item(1);
			NodeList childList2 = main2.getChildNodes();
			Element childMain2 = (Element) childList2.item(1);
			System.out.println("child " + childMain2.getAttribute("value"));


		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

}
