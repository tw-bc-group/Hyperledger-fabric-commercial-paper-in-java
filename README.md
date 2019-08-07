# fabric-java-commercialpaper
Hyperledger Fabric Demo by using Java Chaincode and Java SDK

#### 一、Hyperledger Fabric 安装

参考 

1. [Prerequisites](https://hyperledger-fabric.readthedocs.io/en/latest/prereqs.html)
2. [Install Samples, Binaries and Docker Images](https://hyperledger-fabric.readthedocs.io/en/latest/install.html)

#### 二、工程介绍

1. **commercialpaper-network**：fabric 网络配置和启动文件。
2. **commercialpaper-chaincode**：基于 Java 实现的 Chaincode。
3. **commercialpaper-app**：fabric client，基于 Java SDK 实现。

#### 三、用例描述 - 商业票据

1. 用例描述

* 假如 org1 现在遇到了资金紧张的问题，它可以通过发布一个“商业票据”来暂时缓解这一问题。org2 就可以通过支付一定的金额来购买 org1 发布的“商业票据”。同时，org2 可以在“商业票据”中的截止时间前向 org1 进行资金的赎回。

2. 商业票据建模

* PaperNumber：标识该商业票据的标识
* Issuer：该商业票据的发布者
* Owner：当前该商业票据的所属者
* Issue date：该商业票据的发布日期
* Maturity date：该商业票据的赎回截止日期
* Face value：面值
* Current state：该商业票据当前的状态，只能为“ISSUED”(已发布)，“TRADING”(已被购买)，“REDEEMED”(已赎回)。

3. 商业票据生命周期

* 用户仅可以通过“购买”，“交易”，“赎回”操作来改变某一个商业票据的状态。
* ![商业票据生命周期](./images/lifecycle.png)
* 随着“购买”，“交易”，“赎回”操作的发生，账本中“商业票据”的状态变化如下：
* ![商业票据状态变化](./images/state-transfer.png)

#### 四、示例

1. 初始化网络，进入 **commercialpaper-network**。

   - 运行**generate.sh**命令，生成证书文件以及创世块等。
   
     由于所有的文件都需要在**commercialpaper-app**中调用，所以生成的文件被保存到了**commercialpaper-app/src/main/resources**文件夹中：*config*和*crypto-config*
   
   - 运行**start.sh**命令，启动fabric网络。
   
     ```
     docker启动之后：
     4个 peer 容器
     2个 ca 容器
     1个 orderer 容器
     ```

2. 启动 fabric client，进入 **commercialpaper-app**
    - `./gradlew clean build` client 项目构建
    - `./gradlew bootRun` 运行 client 项目

3. API 概述
    - `POST：localhost:8080/user` ： enroll user for channel
    - `POST：localhost:8080/channel` ： 新建 channel
    - `POST：localhost:8080/chaincode` ： 部署并初始化 chaincode
    - `POST：localhost:8080/paper` ： 新建 CommercialPaper
      - Body：
      ```
      {
        "issuer":"org1",
        "paperNumber":"002",
        "issueDateTime":"2019.08.06",
        "maturityDateTime":"2019.12.31",
        "faceValue":"500"
      }
      ```
    - `GET：localhost:8080/paper/002?org=org2` ： 查询指定 paperNumber 的 CommercialPaper
    - `POST：localhost:8080/paper/buy` ： 购买 CommercialPaper
      - Body：
      ```
      {
        "issuer":"org2",
        "paperNumber":"002",
        "currentOwner":"org1",
        "newOwner":"org2",
        "price":12,
        "purchaseDateTime":"2018.08.11"
      }
      ```
    - `POST：localhost:8080/paper/redeem` ： 赎回 CommercialPaper
      - Body：
      ```
      {
        "issuer":"org1",
        "paperNumber":"002",
        "redeemingOwner":"org2"
      }
      ```