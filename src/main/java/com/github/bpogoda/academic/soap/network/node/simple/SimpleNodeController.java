package com.github.bpogoda.academic.soap.network.node.simple;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.soap.SOAPException;

import com.github.bpogoda.academic.soap.network.model.node.simple.SimpleNode;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SimpleNodeController implements Initializable {

	@FXML
	Button btnSendMessage;

	private SimpleNode simpleNode;

	@FXML
	Label lblNextNode;

	@FXML
	Label lblCurrentNode;

	@FXML
	TextArea tbReceivedMessage;

	@FXML
	Label lblMessageSender;

	@FXML TextArea tbMessage;

	@FXML TextField tfTargetNode;

	public void setSimpleNode(SimpleNode simpleNode) {
		this.simpleNode = simpleNode;

		lblCurrentNode.setText(Integer.toString(simpleNode.getNodePort()));
		lblNextNode.setText(Integer.toString(simpleNode.getNextNodePort()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void sendMessage() {
		try {
			simpleNode.sendMessage(tfTargetNode.getText(), tbMessage.getText());

		} catch (SOAPException | IOException e) {
			e.printStackTrace();
		}
	}

	public void showReceivedMessage(String sender, String message) {
		
		Platform.runLater(() -> {
			lblMessageSender.setText(sender);
			tbReceivedMessage.setText(message);
		});

	}

}
