package com.github.bpogoda.academic.soap.network.model.node.simple;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.github.bpogoda.academic.soap.network.model.node.NodeName;
import com.github.bpogoda.academic.soap.network.model.node.SoapUtil;
import com.github.bpogoda.academic.soap.network.node.simple.SimpleNodeController;

public class SimpleNode {

	private int nodePort;

	private NodeName nodeName;

	private int nextNodePort;

	private ServerSocket serverSocket;
	
	private SimpleNodeController controller;

	public SimpleNode(int nodePort, String nodeName, int nextNodePort) {
		this.nodePort = nodePort;
		this.nodeName = new NodeName(nodeName);
		this.nextNodePort = nextNodePort;
	}

	public void startServer() {
		final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

		int nodePort = this.nodePort;

		Thread serverThread = new Thread(() -> {
			try {
				serverSocket = new ServerSocket(nodePort);
				System.out.println("Waiting for clients to connect...");
				while (true) {
					Socket clientSocket = serverSocket.accept();
					clientProcessingPool.submit(new IncommingMessageHandler(clientSocket, controller));
				}
			} catch (IOException e) {
				
			}
		});

		serverThread.start();
	}
	
	public void stopServer() throws IOException {
		serverSocket.close();
	}

	public void sendMessage(String receiver, String message) throws SOAPException, UnknownHostException, IOException {
		SOAPMessage soapMessage = SoapUtil.createEnvelope(nodeName, new NodeName(receiver), message);
		
		Socket socket = new Socket("localhost", nextNodePort);
		soapMessage.writeTo(socket.getOutputStream());
		socket.getOutputStream().flush();

		socket.close();
	}

	public int getNodePort() {
		return nodePort;
	}

	public NodeName getNodeName() {
		return nodeName;
	}

	public int getNextNodePort() {
		return nextNodePort;
	}

	public void setController(SimpleNodeController controller) {
		this.controller = controller;
	}

}
