package com.github.bpogoda.academic.soap.network.node.router;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.soap.SOAPException;

import com.github.bpogoda.academic.soap.network.model.node.router.RouterNode;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;

public class RouterNodeController implements Initializable {

	private RouterNode routerNode;
	
	@FXML
	Button btnSendMessage;

	@FXML
	Label lblMessageSender;

	@FXML TextArea tbMessage;

	@FXML TextField tfTargetNode;

	@FXML Label lblCurrentNodePort;

	@FXML Label lblCurrentNodeId;

	@FXML Label lblNextNetworkNodePort;

	@FXML Label lblNextRouterNodePort;

	@FXML ListView<String> listViewLog;
	
	private ObservableList<String> logs = FXCollections.observableArrayList();

	@FXML Text txtReceivedMessage;

	@FXML BorderPane paneReceivedMessage;

	public void setRouterNode(RouterNode routerNode) {
		this.routerNode = routerNode;

		lblCurrentNodeId.setText(routerNode.getNodeId().getCombinedName());
		lblCurrentNodePort.setText(Integer.toString(routerNode.getPort()));
		lblNextNetworkNodePort.setText(Integer.toString(routerNode.getNextNetworkNodePort()));
		lblNextRouterNodePort.setText(Integer.toString(routerNode.getNextRouterNodePort()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listViewLog.setItems(logs);
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
			txtReceivedMessage.setText(message);
			
			this.blink();
		});

	}
	
	private void blink() {
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(0.05),
						evt -> paneReceivedMessage.backgroundProperty()
								.set(new Background(new BackgroundFill(Paint.valueOf("#E8AC2F"), new CornerRadii(15), Insets.EMPTY)))),
				new KeyFrame(Duration.seconds(1), evt -> paneReceivedMessage.backgroundProperty()
						.set(new Background(new BackgroundFill(Paint.valueOf("#E5DCC9"), new CornerRadii(15), Insets.EMPTY)))));

		timeline.setCycleCount(1);
		timeline.play();

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
