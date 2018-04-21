package com.github.bpogoda.academic.soap.network;

import java.io.IOException;
import java.util.Map;

import com.github.bpogoda.academic.soap.network.model.node.simple.SimpleNode;
import com.github.bpogoda.academic.soap.network.node.simple.SimpleNodeController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class SimpleNodeLauncher extends Application {

	private static final String APP_TITLE = "Simple node";
	private static final String ICON_PATH = "icon.jpeg";
	private static final String CSS_PATH = "application.css";

	private Stage primaryStage;
	private BorderPane simpleNodeView;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(APP_TITLE);
		this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_PATH)));

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SimpleNodeLauncher.class.getResource("node/simple/SimpleNodeView.fxml"));
			simpleNodeView = (BorderPane) loader.load();

			SimpleNodeController simpleNodeController = (SimpleNodeController) loader.getController();

			Map<String, String> parameters = getParameters().getNamed();
			int nodePort = Integer.parseInt(parameters.get("nodePort"));
			String nodeName = parameters.get("nodeName");
			int nextNodePort = Integer.parseInt(parameters.get("nextNodePort"));

			SimpleNode simpleNode = new SimpleNode(nodePort, nodeName, nextNodePort);
			simpleNodeController.setSimpleNode(simpleNode);

			simpleNode.setController(simpleNodeController);

			primaryStage.setOnCloseRequest(event -> {
				try {
					simpleNode.stopServer();
					stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			simpleNode.startServer();

			Scene scene = new Scene(simpleNodeView);
			scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
