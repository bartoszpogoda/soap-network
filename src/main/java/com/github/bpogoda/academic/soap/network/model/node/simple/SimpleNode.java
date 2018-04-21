package com.github.bpogoda.academic.soap.network.model.node.simple;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.github.bpogoda.academic.soap.network.model.node.AbstractNode;
import com.github.bpogoda.academic.soap.network.model.node.NodeIdentifier;
import com.github.bpogoda.academic.soap.network.model.node.SoapUtil;
import com.github.bpogoda.academic.soap.network.node.simple.SimpleNodeController;

public class SimpleNode extends AbstractNode {

	private SimpleNodeController controller;

	private int nextNodePort;

	public SimpleNode(int port, NodeIdentifier name, int nextNodePort) {
		super(port, name);

		this.nextNodePort = nextNodePort;
	}

	@Override
	protected void onSoapMessageReceived(SOAPMessage soapMessage) {
		try {
			NodeIdentifier receiver = SoapUtil.extractReceiver(soapMessage);

			if (receiver.isReceiver(this.getNodeId())) {
				String sender = SoapUtil.extractSender(soapMessage).getCombinedName();
				String message = SoapUtil.extractMessage(soapMessage);
				controller.showReceivedMessage(sender, message);

				if (receiver.isGlobalBroadcast() || receiver.isNetworkBroadcast()) {

					List<NodeIdentifier> pathNodes = SoapUtil.extractPathNodes(soapMessage);

					if (!pathNodes.contains(this.getNodeId())) {
						SoapUtil.addPathNode(soapMessage, this.getNodeId());

						sendMessage(soapMessage);
					}
				}
			} else {
				
				List<NodeIdentifier> pathNodes = SoapUtil.extractPathNodes(soapMessage);

				if (!pathNodes.contains(this.getNodeId())) {
					SoapUtil.addPathNode(soapMessage, this.getNodeId());

					sendMessage(soapMessage);
				}
			}

		} catch (SOAPException | IOException e) {
			controller.showError("Error", e.getMessage());
		}
	}

	@Override
	protected void onSoapMessageReadyToSend(SOAPMessage soapMessage) {
		try {
			Socket socket = new Socket("localhost", nextNodePort);
			soapMessage.writeTo(socket.getOutputStream());
			socket.getOutputStream().flush();
			socket.close();
		} catch (IOException | SOAPException e) {
			controller.showError("Error", e.getMessage());
		}

	}

	public int getNextNodePort() {
		return nextNodePort;
	}

	public void setController(SimpleNodeController controller) {
		this.controller = controller;
	}

}
