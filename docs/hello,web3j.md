## hello,web3j

在这一部分，我们将开发一个最简单的Java控制台应用，来接入以太坊节点，并打印 所连接节点旳版本信息。

通过这一部分的学习，你将掌握以下技能：

* 如何使用节点仿真器
* 如何创建一个Web3j对象
* 如何声明要连接的节点
* 如果向节点发送一个RPC调用并提取返回结果

我们将使用ganache来模拟以太坊节点。ganache虽然不是一个真正的以太坊节点软件， 但它完整实现了以太坊的JSON RPC接口，非常适合以太坊智能合约与去中心化应用开发的 学习与快速验证：


![json_rpc_web3j](img/ganache.png)

ganache启动后将在8545端口监听http请求，因此，我们将使用web3j将JSON RPC调用请求 使用http协议发送到节点旳8545端口。不同的节点软件可能会使用不同的监听端口，但 大部分节点软件通常默认使用8545端口。

以太坊规定了节点必须实现web3_clientVersion(http://cw.hubwiz.com/card/c/ethereum-json-rpc-api/1/1/1/) 调用来返回节点软件的版本信息，因此我们可以使用web3j对这个命令的封装来测试与 节点旳链接是否成功。