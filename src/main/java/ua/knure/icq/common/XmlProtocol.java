package ua.knure.icq.common;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlProtocol {

    public static String toXml(Message message) {
        Document document = createDocumentFromMessage(message);
        if (document == null) {
            return "";
        }
        return convertDocumentToString(document);
    }

    public static Message fromXml(String xml) {
        Document document = createDocumentFromString(xml);
        if (document == null) {
            return null;
        }
        Element root = document.getDocumentElement();
        String sender = getElementText(root, "sender");
        String receiver = getElementText(root, "receiver");
        String text = getElementText(root, "text");
        String timeText = getElementText(root, "time");

        if (sender == null || receiver == null || text == null || timeText == null) {
            System.out.println("[ERROR] XML message has missing fields.");
            return null;
        }
        try {
            Instant time = Instant.parse(timeText);
            return new Message(sender, receiver, text, time);
        } catch (Exception exception) {
            System.out.println("[ERROR] Failed to parse message time.");
            exception.printStackTrace();
            return null;
        }
    }

    private static Document createDocumentFromMessage(Message message) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("message");
            document.appendChild(root);
            addElement(document, root, "sender", message.getSender());
            addElement(document, root, "receiver", message.getReceiver());
            addElement(document, root, "text", message.getText());
            addElement(document, root, "time", message.getTime().toString());
            return document;
        } catch (Exception exception) {
            System.out.println("[ERROR] Failed to create XML document from message.");
            exception.printStackTrace();
            return null;
        }
    }

    private static Document createDocumentFromString(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xml)));
        } catch (Exception exception) {
            System.out.println("[ERROR] Failed to create XML document from string.");
            exception.printStackTrace();
            return null;
        }
    }

    private static String convertDocumentToString(Document document) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.toString();
        } catch (Exception exception) {
            System.out.println("[ERROR] Failed to convert XML document to string.");
            exception.printStackTrace();
            return "";
        }
    }

    private static void addElement(Document document, Element root, String tagName, String value) {
        Element element = document.createElement(tagName);
        element.setTextContent(value);
        root.appendChild(element);
    }

    private static String getElementText(Element root, String tagName) {
        if (root.getElementsByTagName(tagName).getLength() == 0) {
            return null;
        }
        return root.getElementsByTagName(tagName).item(0).getTextContent();
    }
}