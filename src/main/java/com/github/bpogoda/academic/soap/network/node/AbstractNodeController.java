package com.github.bpogoda.academic.soap.network.node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.soap.SOAPException;

import com.github.bpogoda.academic.soap.network.model.node.AbstractNode;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;;

public class AbstractNodeController implements Initializable {

	private AbstractNode node;

	private ObservableList<String> logs = FXCollections.observableArrayList();

	@FXML
	Button btnSendMessage;

	@FXML
	Label lblMessageSender;

	@FXML
	TextArea tbMessage;

	@FXML
	TextField tfTargetNode;

	@FXML
	Label lblCurrentNodePort;

	@FXML
	Label lblCurrentNodeId;

	@FXML
	ListView<String> listViewLog;

	@FXML
	Text txtReceivedMessage;

	@FXML
	BorderPane paneReceivedMessage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listViewLog.setItems(logs);
		
		logs.addListener(new ListChangeListener<String>() {

			@Override
			public void onChanged(Change<? extends String> c) {
				listViewLog.scrollTo(listViewLog.getItems().size() - 1);
			}
		});
	}

	public void setNode(AbstractNode node) {
		this.node = node;

		lblCurrentNodeId.setText(node.getNodeId().getCombinedName());
		lblCurrentNodePort.setText(Integer.toString(node.getPort()));
	}

	@FXML
	public void sendMessage() {
		try {
			node.sendMessage(tfTargetNode.getText(), tbMessage.getText());

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
								.set(new Background(new BackgroundFill(Paint.valueOf("#E8AC2F"), new CornerRadii(15),
										Insets.EMPTY)))),
				new KeyFrame(Duration.seconds(1), evt -> paneReceivedMessage.backgroundProperty().set(new Background(
						new BackgroundFill(Paint.valueOf("#E5DCC9"), new CornerRadii(15), Insets.EMPTY)))));

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
