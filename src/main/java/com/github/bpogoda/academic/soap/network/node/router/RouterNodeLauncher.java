package com.github.bpogoda.academic.soap.network.node.router;

import java.io.IOException;
import java.util.Map;

import com.github.bpogoda.academic.soap.network.model.node.NodeIdentifier;
import com.github.bpogoda.academic.soap.network.model.node.router.RouterNode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RouterNodeLauncher extends Application {

	private static final String APP_TITLE_PREFIX = "Router node: ";
	private static final String ICON_PATH = "icon.png";
	private static final String CSS_PATH = "application.css";

	private Stage primaryStage;
	private BorderPane simpleNodeView;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_PATH)));

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RouterNodeLauncher.class.getResource("RouterNodeView.fxml"));
			simpleNodeView = (BorderPane) loader.load();

			RouterNodeController routerNodeController = (RouterNodeController) loader.getController();

			Map<String, String> parameters = getParameters().getNamed();
			
			int nodePort = Integer.parseInt(parameters.get("nodePort"));
			String nodeId = parameters.get("nodeId");
			int nextNetworkNodePort = Integer.parseInt(parameters.get("nextNetworkNodePort"));
			int nextRouterNodePort = Integer.parseInt(parameters.get("nextRouterNodePort"));
			
			this.primaryStage.setTitle(APP_TITLE_PREFIX + nodeId);

			RouterNode routerNode = new RouterNode(nodePort, new NodeIdentifier(nodeId), nextNetworkNodePort, nextRouterNodePort);
			routerNodeController.setRouterNode(routerNode);

			routerNode.setController(routerNodeController);

			primaryStage.setOnCloseRequest(event -> {
				try {
					routerNode.stopListening();
					stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			routerNode.startListening();

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
