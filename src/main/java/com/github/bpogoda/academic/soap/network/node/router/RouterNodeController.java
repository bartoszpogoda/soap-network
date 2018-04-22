package com.github.bpogoda.academic.soap.network.node.router;

import com.github.bpogoda.academic.soap.network.model.node.router.RouterNode;
import com.github.bpogoda.academic.soap.network.node.AbstractNodeController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RouterNodeController extends AbstractNodeController {

	@FXML Label lblNextNetworkNodePort;

	@FXML Label lblNextRouterNodePort;

	public void setRouterNode(RouterNode routerNode) {
		this.setNode(routerNode);

		lblNextNetworkNodePort.setText(Integer.toString(routerNode.getNextNetworkNodePort()));
		lblNextRouterNodePort.setText(Integer.toString(routerNode.getNextRouterNodePort()));
	}

}
