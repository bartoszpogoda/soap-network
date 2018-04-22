
start javaw -jar router-node.jar --nodePort=5060 --nodeId="a-R" --nextNetworkNodePort=5061 --nextRouterNodePort=5070
start javaw -jar router-node.jar --nodePort=5070 --nodeId="b-R" --nextNetworkNodePort=5071 --nextRouterNodePort=5080
start javaw -jar router-node.jar --nodePort=5080 --nodeId="c-R" --nextNetworkNodePort=5081 --nextRouterNodePort=5060

start javaw -jar simple-node.jar --nodePort=5061 --nodeId="a-1" --nextNodePort=5062
start javaw -jar simple-node.jar --nodePort=5062 --nodeId="a-2" --nextNodePort=5060
start javaw -jar simple-node.jar --nodePort=5071 --nodeId="b-1" --nextNodePort=5072
start javaw -jar simple-node.jar --nodePort=5072 --nodeId="b-2" --nextNodePort=5070
start javaw -jar simple-node.jar --nodePort=5081 --nodeId="c-1" --nextNodePort=5082
start javaw -jar simple-node.jar --nodePort=5082 --nodeId="c-2" --nextNodePort=5080
