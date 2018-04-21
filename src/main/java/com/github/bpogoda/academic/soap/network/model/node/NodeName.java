package com.github.bpogoda.academic.soap.network.model.node;

public class NodeName {
	private String nodeName;

	private String networkName;

	public NodeName(String combinedName) {
		String[] splittedName = combinedName.split("-");

		networkName = splittedName[0];
		nodeName = splittedName[1];
	}

	public NodeName(String networkName, String nodeName) {
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

	public String getCombinedName() {
		return networkName + "-" + nodeName;
	}
}
