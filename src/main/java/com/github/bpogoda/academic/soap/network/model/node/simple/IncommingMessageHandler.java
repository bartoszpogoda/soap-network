package com.github.bpogoda.academic.soap.network.model.node.simple;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.github.bpogoda.academic.soap.network.model.node.NodeName;
import com.github.bpogoda.academic.soap.network.model.node.SoapUtil;
import com.github.bpogoda.academic.soap.network.node.simple.SimpleNodeController;

public class IncommingMessageHandler implements Runnable {

	private final Socket clientSocket;
	private SimpleNodeController controller;
	
	public IncommingMessageHandler(Socket clientSocket, SimpleNodeController controller) {
		super();
		this.clientSocket = clientSocket;
		this.controller = controller;
	}

	@Override
	public void run() {
		System.out.println("Got a client");
		
		SOAPMessage soapMessage;
		try {
			soapMessage = MessageFactory.newInstance().createMessage(null, clientSocket.getInputStream());
			
			NodeName sender = SoapUtil.extractSender(soapMessage);
			String message = SoapUtil.extractMessage(soapMessage);
			
			List<NodeName> pathNodes = SoapUtil.extractPathNodes(soapMessage);
			
			System.out.println("Path: ");
			pathNodes.forEach(nodeName -> System.out.println(nodeName.getCombinedName()));
			
			controller.showReceivedMessage(sender.getCombinedName(), message);
			
			clientSocket.close();
		} catch (IOException | SOAPException e1) {
			e1.printStackTrace();
		}
		
	}

}
