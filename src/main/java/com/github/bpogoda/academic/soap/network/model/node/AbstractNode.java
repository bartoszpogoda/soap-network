package com.github.bpogoda.academic.soap.network.model.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

public abstract class AbstractNode {
	
	private int port;
	
	private NodeIdentifier nodeId;
	
	private ServerSocket serverSocket;
	
	public AbstractNode(int port, NodeIdentifier name) {
		super();
		this.port = port;
		this.nodeId = name;
	}

	public void startListening() {
		final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

		int nodePort = this.port;

		Thread serverThread = new Thread(() -> {
			try {
				serverSocket = new ServerSocket(nodePort);
				while (true) {
					Socket clientSocket = serverSocket.accept();
					clientProcessingPool.submit(() -> {
						try {
							SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null, clientSocket.getInputStream());
							this.onSoapMessageReceived(soapMessage);
							clientSocket.close();
						} catch (IOException | SOAPException e) {
							
						}
						
					});
				}
			} catch (IOException e) {
				
			}
		});

		serverThread.start();
	}
	
	public void stopListening() throws IOException {
		serverSocket.close();
	}
	
	public void sendMessage(String receiver, String message) throws SOAPException, UnknownHostException, IOException {
		SOAPMessage soapMessage = SoapUtil.createEnvelope(nodeId, new NodeIdentifier(receiver), message);
		
		onSoapMessageReadyToSend(soapMessage);
	}
	
	public void sendMessage(SOAPMessage soapMessage) throws SOAPException, UnknownHostException, IOException {
		onSoapMessageReadyToSend(soapMessage);
	}
	
	protected void forwardToPort(SOAPMessage soapMessage, int port) throws SOAPException, IOException {
		Socket socket = new Socket("localhost", port);
		soapMessage.writeTo(socket.getOutputStream());
		socket.getOutputStream().flush();
		socket.close();
	}
	
	abstract protected void onSoapMessageReceived(SOAPMessage soapMessage);
	
	abstract protected void onSoapMessageReadyToSend(SOAPMessage soapMessage);

	public int getPort() {
		return port;
	}

	public NodeIdentifier getNodeId() {
		return nodeId;
	}
	
}
