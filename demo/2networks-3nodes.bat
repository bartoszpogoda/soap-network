
start javaw -jar router-node.jar --nodePort=5060 --nodeId="a-R" --nextNetworkNodePort=5061 --nextRouterNodePort=5070
start javaw -jar router-node.jar --nodePort=5070 --nodeId="b-R" --nextNetworkNodePort=5071 --nextRouterNodePort=5060

start javaw -jar simple-node.jar --nodePort=5061 --nodeId="a-1" --nextNodePort=5062 --nextNodeHost="192.168.0.2"
start javaw -jar simple-node.jar --nodePort=5062 --nodeId="a-2" --nextNodePort=5060
start javaw -jar simple-node.jar --nodePort=5071 --nodeId="b-1" --nextNodePort=5072
start javaw -jar simple-node.jar --nodePort=5072 --nodeId="b-2" --nextNodePort=5070
