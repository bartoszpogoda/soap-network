# soap-network

Simulating network of connected distributed nodes that communicate with each other using SOAP protocole (*javax.xml.soap*) and TCP sockets (*java.net*). Nodes can send simple text messages to specified nodes (unicast), group of nodes (multicast) and all nodes in network (broadcast).

#### Node identification

Nodes can be divided into sub-networks - groups. Each node in the group can be identified by a string consisting of group name and machine name. 

```
group-machine
```

Group part and machine part are sepparated with single dash letter.

#### Messages target

Unicast - single node. Target below reffers to node **1** in group **a**.

```
a-1
```

Multicast - group of nodes. Target bellow reffers to all nodes in group **a**.

```
a-*
```

Broadcast - all nodes in the network (self included).

```
*-*
```

#### Node types

There are two node types:

* Simple node
* Router node

##### Simple node

Simple node is basically a machine in a group (sub-network). It is connected only to the next node in same network.

##### Router node

Router node is node connected to its group and also to another router node. Router nodes are used to connect sub-networks together. All cross-group communication is managed by this type of nodes.

### Sample configurations

##### 3networks-3nodes

![3network-3nodes-schema](demo\3network-3nodes-schema.jpg)

```shell
start javaw -jar router-node.jar --nodePort=5060 --nodeId="a-R" --nextNetworkNodePort=5061 --nextRouterNodePort=5070
start javaw -jar router-node.jar --nodePort=5070 --nodeId="b-R" --nextNetworkNodePort=5071 --nextRouterNodePort=5080
start javaw -jar router-node.jar --nodePort=5080 --nodeId="c-R" --nextNetworkNodePort=5081 --nextRouterNodePort=5060

start javaw -jar simple-node.jar --nodePort=5061 --nodeId="a-1" --nextNodePort=5062
start javaw -jar simple-node.jar --nodePort=5062 --nodeId="a-2" --nextNodePort=5060
start javaw -jar simple-node.jar --nodePort=5071 --nodeId="b-1" --nextNodePort=5072
start javaw -jar simple-node.jar --nodePort=5072 --nodeId="b-2" --nextNodePort=5070
start javaw -jar simple-node.jar --nodePort=5081 --nodeId="c-1" --nextNodePort=5082
start javaw -jar simple-node.jar --nodePort=5082 --nodeId="c-2" --nextNodePort=5080
```

#### Node distribution

Nodes can be distributed on different machines. It is possible to specify target host addresses. If not specified - it defaults to localhost.

```sh
start javaw -jar simple-node.jar --nodePort=5082 --nodeId="c-2" --nextNodeHost="192.168.1.2" --nextNodePort=5080
```

```sh
start javaw -jar router-node.jar --nodePort=5080 --nodeId="c-R" --nextNetworkNodeHost="192.168.1.2" --nextNetworkNodePort=5081 --nextRouterNodeHost="192.168.1.3" --nextRouterNodePort=5060
```

#### GUI screenshots

![ss4-unicast](demo\ss4-unicast.PNG)