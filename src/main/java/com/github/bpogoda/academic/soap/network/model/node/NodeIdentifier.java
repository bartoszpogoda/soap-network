package com.github.bpogoda.academic.soap.network.model.node;

public class NodeIdentifier {
	private String nodeName;

	private String networkName;

	public NodeIdentifier(String combinedName) {
		String[] splittedName = combinedName.split("-");

		networkName = splittedName[0];
		nodeName = splittedName[1];
	}

	public NodeIdentifier(String networkName, String nodeName) {
		this.networkName = networkName;
		this.nodeName = nodeName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	
	public boolean isNetworkBroadcast() {
		return this.nodeName.equals("*") && !this.networkName.equals("*");
	}

	public boolean isGlobalBroadcast() {
		return this.nodeName.equals("*") && this.networkName.equals("*");
	}
	
	public String getCombinedName() {
		return networkName + "-" + nodeName;
	}
}
