package com.github.bpogoda.academic.soap.network.node.simple;

import com.github.bpogoda.academic.soap.network.model.node.simple.SimpleNode;
import com.github.bpogoda.academic.soap.network.node.AbstractNodeController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SimpleNodeController extends AbstractNodeController {

	@FXML
	Label lblNextNodePort;

	public void setSimpleNode(SimpleNode simpleNode) {
		this.setNode(simpleNode);

		lblNextNodePort.setText(Integer.toString(simpleNode.getNextNodePort()));
	}

}
