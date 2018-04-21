package com.github.bpogoda.academic.soap.network.model.node;

public class NodeIdentifier {
	private String nodeName;

	private String networkName;

	// TODO handle situation when combinedName is not valid identifier
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

	public boolean isUnicast() {
		return !this.nodeName.equals("*") && !this.networkName.equals("*");
	}

	public boolean isNetworkBroadcast() {
		return this.nodeName.equals("*") && !this.networkName.equals("*");
	}

	public boolean isGlobalBroadcast() {
		return this.nodeName.equals("*") && this.networkName.equals("*");
	}

	public boolean isReceiver(NodeIdentifier nodeId) {
		if (this.equals(nodeId) || this.isGlobalBroadcast()
				|| this.isNetworkBroadcast() && this.getNetworkName().equals(nodeId.getNetworkName())) {
			return true;
		} else {
			return false;
		}
	}

	public String getCombinedName() {
		return networkName + "-" + nodeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((networkName == null) ? 0 : networkName.hashCode());
		result = prime * result + ((nodeName == null) ? 0 : nodeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeIdentifier other = (NodeIdentifier) obj;
		if (networkName == null) {
			if (other.networkName != null)
				return false;
		} else if (!networkName.equals(other.networkName))
			return false;
		if (nodeName == null) {
			if (other.nodeName != null)
				return false;
		} else if (!nodeName.equals(other.nodeName))
			return false;
		return true;
	}

}
