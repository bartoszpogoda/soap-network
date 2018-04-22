package com.github.bpogoda.academic.soap.network.model.node.router;

import java.io.IOException;
import java.util.List;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.github.bpogoda.academic.soap.network.LogsUtil;
import com.github.bpogoda.academic.soap.network.model.node.AbstractNode;
import com.github.bpogoda.academic.soap.network.model.node.NodeIdentifier;
import com.github.bpogoda.academic.soap.network.model.node.SoapUtil;
import com.github.bpogoda.academic.soap.network.node.router.RouterNodeController;

public class RouterNode extends AbstractNode {

	private int nextNetworkNodePort;
	private int nextRouterNodePort;

	private RouterNodeController controller;

	public RouterNode(int port, NodeIdentifier name, int nextNetworkNodePort, int nextRouterNodePort) {
		super(port, name);

		this.nextNetworkNodePort = nextNetworkNodePort;
		this.nextRouterNodePort = nextRouterNodePort;
	}

	@Override
	protected void onSoapMessageReceived(SOAPMessage soapMessage) {
		controller.log(LogsUtil.LOG_RECEIVE);

		try {
			NodeIdentifier receiver = SoapUtil.extractReceiver(soapMessage);

			// determine if message was already handled
			if (SoapUtil.extractPathNodes(soapMessage).contains(this.getNodeId())) {
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
			if (receiver.isGlobalBroadcast() || receiver.isNetworkBroadcast()
					|| !receiver.isReceiver(this.getNodeId())) {
				List<NodeIdentifier> pathNodes = SoapUtil.extractPathNodes(soapMessage);

				if (!pathNodes.contains(this.getNodeId())) {
					SoapUtil.addPathNode(soapMessage, this.getNodeId());

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
			NodeIdentifier receiver = SoapUtil.extractReceiver(soapMessage);

			if (receiver.isNetworkBroadcast() && receiver.getNetworkName().equals(getNodeId().getNetworkName())) {
				controller.log(LogsUtil.LOG_FORWARD_NETWORK);
				forwardToPort(soapMessage, nextNetworkNodePort);
			} else if (receiver.isNetworkBroadcast()) {
				controller.log(LogsUtil.LOG_FORWARD_ROUTER);
				forwardToPort(soapMessage, nextRouterNodePort);
			} else if (receiver.isUnicast() && receiver.getNetworkName().equals(getNodeId().getNetworkName())) {
				controller.log(LogsUtil.LOG_FORWARD_NETWORK);
				forwardToPort(soapMessage, nextNetworkNodePort);
			} else if (receiver.isUnicast() && !receiver.getNetworkName().equals(getNodeId().getNetworkName())) {
				controller.log(LogsUtil.LOG_FORWARD_ROUTER);
				forwardToPort(soapMessage, nextRouterNodePort);
			} else {
				controller.log(LogsUtil.LOG_FORWARD_NETWORK);
				forwardToPort(soapMessage, nextNetworkNodePort);
				controller.log(LogsUtil.LOG_FORWARD_ROUTER);
				forwardToPort(soapMessage, nextRouterNodePort);
			}

		} catch (IOException |

				SOAPException e) {
			controller.showError("Error", e.getMessage());
		}
	}

	public void setController(RouterNodeController routerNodeController) {
		this.controller = routerNodeController;
	}

	public int getNextNetworkNodePort() {
		return nextNetworkNodePort;
	}

	public int getNextRouterNodePort() {
		return nextRouterNodePort;
	}

}
