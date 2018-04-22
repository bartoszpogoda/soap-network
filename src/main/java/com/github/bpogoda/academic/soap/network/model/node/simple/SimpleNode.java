package com.github.bpogoda.academic.soap.network.model.node.simple;

import java.io.IOException;
import java.util.List;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.github.bpogoda.academic.soap.network.LogsUtil;
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
		controller.log(LogsUtil.LOG_RECEIVE);
		
		try {
			NodeIdentifier receiver = SoapUtil.extractReceiver(soapMessage);
			
			// determine if message was already handled
			if(SoapUtil.extractPathNodes(soapMessage).contains(this.getNodeId())) {
				controller.log(LogsUtil.LOG_DROP);
				return;
			}
			
			// determine if message is addressed to current node
			if (receiver.isReceiver(this.getNodeId())) {
				controller.log(LogsUtil.LOG_TO_ME);
				String sender = SoapUtil.extractSender(soapMessage).getCombinedName();
				String message = SoapUtil.extractMessage(soapMessage);
				controller.showReceivedMessage(sender, message);
			} 
			
			// determine if message should be forwarded
			if (receiver.isGlobalBroadcast() || receiver.isNetworkBroadcast() || !receiver.isReceiver(this.getNodeId())) {
				List<NodeIdentifier> pathNodes = SoapUtil.extractPathNodes(soapMessage);

				if (!pathNodes.contains(this.getNodeId())) {
					SoapUtil.addPathNode(soapMessage, this.getNodeId());

					controller.log(LogsUtil.LOG_FORWARD_NETWORK);
					sendMessage(soapMessage);
				} else {
					controller.log(LogsUtil.LOG_DROP);
				}
			} else {
				controller.log(LogsUtil.LOG_DROP);
			}

		} catch (SOAPException | IOException e) {
			controller.showError("Error", e.getMessage());
		}
	}

	@Override
	protected void onSoapMessageReadyToSend(SOAPMessage soapMessage) {
		try {
			forwardToPort(soapMessage, nextNodePort);
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
