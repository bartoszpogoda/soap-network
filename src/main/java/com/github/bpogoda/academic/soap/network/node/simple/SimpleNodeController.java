package com.github.bpogoda.academic.soap.network.node.simple;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.soap.SOAPException;

import com.github.bpogoda.academic.soap.network.model.node.simple.SimpleNode;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class SimpleNodeController implements Initializable {

	@FXML
	Button btnSendMessage;

	private SimpleNode simpleNode;

	@FXML
	TextArea tbReceivedMessage;

	@FXML
	Label lblMessageSender;

	@FXML TextArea tbMessage;

	@FXML TextField tfTargetNode;

	@FXML Label lblNextNodePort;

	@FXML Label lblCurrentNodePort;

	@FXML Label lblCurrentNodeId;

	@FXML ListView<String> listViewLog;
	
	private ObservableList<String> logs = FXCollections.observableArrayList();
	
	public void setSimpleNode(SimpleNode simpleNode) {
		this.simpleNode = simpleNode;

		lblCurrentNodeId.setText(simpleNode.getNodeId().getCombinedName());
		lblCurrentNodePort.setText(Integer.toString(simpleNode.getPort()));
		lblNextNodePort.setText(Integer.toString(simpleNode.getNextNodePort()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listViewLog.setItems(logs);
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
	
	public void showError(String title, String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(text);

		alert.show();
	}
	
	public void log(String message) {
		Platform.runLater(() -> {
			String timestampMs = Long.toString(System.currentTimeMillis());
			String lastSixDigits = timestampMs.substring(timestampMs.length() - 6);
			this.logs.add(lastSixDigits + ": " + message);
		});
	}

}
