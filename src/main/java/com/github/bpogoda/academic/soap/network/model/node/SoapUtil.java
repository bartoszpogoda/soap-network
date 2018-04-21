package com.github.bpogoda.academic.soap.network.model.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.DOMException;

public class SoapUtil {
	
	public static final String BODY_MESSAGE = "Message"; 
	
	public static final String HEADER_NAMESPACE_URI = "head";
	
	public static final String HEADER_SENDER = "Sender";

	public static final String HEADER_RECEIVER = "ReceiverUltimate";
	
	public static final String HEADER_PATH_NODES = "PathNodes";
	
	public static final String HEADER_PATH_NODE = "PathNode";
	
	public static SOAPMessage createEnvelope(NodeIdentifier sender, NodeIdentifier receiver, String message) throws DOMException, SOAPException {
		MessageFactory messageFactory = MessageFactory.newInstance();
		
		SOAPMessage soapMessage = messageFactory.createMessage();

		soapMessage.getSOAPHeader().addChildElement(new QName(HEADER_NAMESPACE_URI, HEADER_SENDER)).setTextContent(sender.getCombinedName());
		soapMessage.getSOAPHeader().addChildElement(new QName(HEADER_NAMESPACE_URI, HEADER_RECEIVER)).setTextContent(receiver.getCombinedName());
		soapMessage.getSOAPHeader().addChildElement(new QName(HEADER_NAMESPACE_URI, HEADER_PATH_NODES));
		
		addPathNode(soapMessage, sender);
		
		soapMessage.getSOAPBody().addBodyElement(new QName(BODY_MESSAGE)).setTextContent(message);

		return soapMessage;
	}
	
	public static NodeIdentifier extractSender(SOAPMessage soapMessage) throws SOAPException {
		return new NodeIdentifier(((SOAPElement)soapMessage.getSOAPHeader().getChildElements(new QName(HEADER_NAMESPACE_URI, HEADER_SENDER)).next()).getTextContent());
	}
	
	public static NodeIdentifier extractReceiver(SOAPMessage soapMessage) throws SOAPException {
		return new NodeIdentifier(((SOAPElement)soapMessage.getSOAPHeader().getChildElements(new QName(HEADER_NAMESPACE_URI, HEADER_RECEIVER)).next()).getTextContent());
	}
	
	@SuppressWarnings("unchecked")
	public static List<NodeIdentifier> extractPathNodes(SOAPMessage soapMessage) throws SOAPException {
		List<NodeIdentifier> pathNodes = new ArrayList<>();
		
		SOAPElement pathNodesElement = (SOAPElement) soapMessage.getSOAPHeader().getChildElements(new QName(HEADER_NAMESPACE_URI, HEADER_PATH_NODES)).next();
		
		Iterator<SOAPElement> pathNodeElements = pathNodesElement.getChildElements(new QName(HEADER_NAMESPACE_URI, HEADER_PATH_NODE));
		
		while(pathNodeElements.hasNext()) {
			pathNodes.add(new NodeIdentifier(pathNodeElements.next().getTextContent()));
		}
		
		return pathNodes;
	}
	
	public static SOAPMessage addPathNode(SOAPMessage soapMessage, NodeIdentifier pathNode) throws SOAPException {
		SOAPElement pathNodesElement = (SOAPElement) soapMessage.getSOAPHeader().getChildElements(new QName(HEADER_NAMESPACE_URI, HEADER_PATH_NODES)).next();
		
		pathNodesElement.addChildElement(new QName(HEADER_NAMESPACE_URI, HEADER_PATH_NODE)).setTextContent(pathNode.getCombinedName());
		
		return soapMessage;
	}
	
	public static String extractMessage(SOAPMessage soapMessage) throws SOAPException {
		return ((SOAPElement)soapMessage.getSOAPBody().getChildElements(new QName(BODY_MESSAGE)).next()).getTextContent();
	}
}
