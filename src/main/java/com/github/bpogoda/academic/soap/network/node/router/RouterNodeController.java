package com.github.bpogoda.academic.soap.network.node.router;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.soap.SOAPException;

import com.github.bpogoda.academic.soap.network.model.node.router.RouterNode;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RouterNodeController implements Initializable {

	private RouterNode routerNode;
	
	@FXML
	Button btnSendMessage;

	@FXML
	TextArea tbReceivedMessage;

	@FXML
	Label lblMessageSender;

	@FXML TextArea tbMessage;

	@FXML TextField tfTargetNode;

	@FXML Label lblCurrentNodePort;

	@FXML Label lblCurrentNodeId;

	@FXML Label lblNextNetworkNodePort;

	@FXML Label lblNextRouterNodePort;

	public void setRouterNode(RouterNode routerNode) {
		this.routerNode = routerNode;

		lblCurrentNodeId.setText(routerNode.getNodeId().getCombinedName());
		lblCurrentNodePort.setText(Integer.toString(routerNode.getPort()));
		lblNextNetworkNodePort.setText(Integer.toString(routerNode.getNextNetworkNodePort()));
		lblNextRouterNodePort.setText(Integer.toString(routerNode.getNextRouterNodePort()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void sendMessage() {
		try {
			routerNode.sendMessage(tfTargetNode.getText(), tbMessage.getText());

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
	
	public void showError(String title, String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(text);

		alert.show();
	}

}
