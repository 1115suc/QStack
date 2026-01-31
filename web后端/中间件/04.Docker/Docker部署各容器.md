# Docker 容器部署 🐳

## Docker 部署数据库

### Docker 部署 MySQL
```shell
docker run -d -p 3306:3306 --privileged=true \
      -v /SuChan/docker/volume/mysql/log:/var/log/mysql \
      -v /SuChan/docker/volume/mysql/data:/var/lib/mysql \
      -v /SuChan/docker/volume/mysql/conf:/etc/mysql/conf.d \
      -v /SuChan/docker/volume/mysql/mysql-files:/var/lib/mysql-files/ \
      -e TZ=Asia/Shanghai \
      -e MYSQL_CHARACTER_SET_SERVER=utf8mb4 \
      -e MYSQL_COLLATION_SERVER=utf8mb4_unicode_ci \
      -e MYSQL_DEFAULT_TIME_ZONE='+8:00' \
      -e MYSQL_ROOT_PASSWORD=24364726 \
      --name mysql mysql:5.7
```

### Docker 部署 MongoDB
```shell
docker run -d \
      --name MongoDB \
      -v /SuChan/docker/volume/mongodb/data:/data/db \
      -v /SuChan/docker/volume/mongodb/log:/data/log \
      -p 27017:27017 \
      --privileged=true \  
      -e MONGO_INITDB_ROOT_USERNAME=root \
      -e MONGO_INITDB_ROOT_PASSWORD=24364726 \
      -e TZ=Asia/Shanghai \
      mongo
```

> --privileged=true \ 当使用--privileged=true选项运行容器时，Docker会赋予容器几乎与主机相同的权限4。具体来说，这个选项做了以下两件事情：         
> 1.给容器添加了所有的capabilities         
> 2.允许容器访问主机的所有设备

### Docker 部署 Neo4j

```shell
docker run \
    --restart=always \
    --name neo4j \
    -p 7474:7474 \
    -p 7687:7687 \
    -v neo4j_data:/data \
    -d neo4j:4.4.5
```

## Docker 部署 消息队列

### Docker 部署 RabbitMQ
```shell
docker run \
      -e RABBITMQ_DEFAULT_USER=root \
      -e RABBITMQ_DEFAULT_PASS=24364726 \
      -v mq-plugins:/plugins \
      --name RabbitMQ \
      --hostname mq \
      -p 15672:15672 \
      -p 5672:5672 \
      -d rabbitmq:3.8-management
```


### Docker 部署 Zookeeper

```shell
docker run -d \
  --name zookeeper \
  --hostname zookeeper \
  --network kafka-net \
  -p 2181:2181 \
  -e TZ="Asia/Shanghai" \
  -e ALLOW_ANONYMOUS_LOGIN=yes \
  -v /root/zookeeper/node-1/data:/data \
  -v /root/zookeeper/node-1/conf:/conf \
  zookeeper:3.7.2
```


### Docker 部署 Kafka

```shell
docker run -d \
  --name kafka \
  -p 9092:9092 \
  -p 9999:9999 \
  --network kafka-net \
  -e KAFKA_NODE_ID=1 \
  -e KAFKA_PROCESS_ROLES=broker,controller \
  -e KAFKA_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \
  -e KAFKA_JMX_PORT=9999 \
  -e KAFKA_JMX_HOSTNAME=localhost \
  -e ALLOW_PLAINTEXT_LISTENER=yes \
  apache/kafka:4.0.1
```

- kafka 可视化工具 Eagle

```shell
docker run -d --name kafka-eagle \
   -p 8048:8048 \
   -e EFAK_CLUSTER_ZK_LIST="192.168.200.128:2181" \
   nickzurich/efak:latest
```

- Kafka 可视化工具 WebUI

1. 拉取 Docker 镜像

```shell
docker pull lcc1024/kafka_ui_lcc:1.0
```

2. 创建 KafkaUILCC 的文件夹

```shell
mkdir -p /usr/local/KafkaUILCC/config
```

3. 创建 KafkaUILCC 的配置文件

```shell
vim /usr/local/KafkaUILCC/config/application.properties
```

```properties
# zookeeper_connect
zookeeper.host=你的zookeeper连接地址
zookeeper.port=你的zookeeper连接端口
zookeeper.session_timeout=连接超时时间
```


4. 启动 KafkaUILCC

```shell
docker run --name KafkaUILCC \
  -p 8093:8093 \
  --network kafka-net \
  --ulimit nofile=65536:65536 \
  --ulimit nproc=4096:4096 \
  -v /usr/local/kafka_UI_LCC/config/application.properties:/application.properties \
  -d lcc1024/kafka_ui_lcc:1.0
```


默认登录信息：

- 账号：admin
- 密码：123456

## Docker 部署其他中间件

### Docker 部署 Elasticsearch
```shell
docker run -d \
      --name elasticsearch \
      -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
      -e "discovery.type=single-node" \
      -v es-data:/usr/share/elasticsearch/data \
      -v es-plugins:/usr/share/elasticsearch/plugins \
      --privileged \
      --network es-net \
      -p 9200:9200 \
      -p 9300:9300 \
      elasticsearch:7.17.5
```


### Docker 部署 Kibana
```shell
docker run -d \
      --name kibana \
      -e ELASTICSEARCH_HOSTS=http://elasticsearch:9200 \
      --network=es-net \
      -p 5601:5601 \
      kibana:7.17.5
```


### Docker 部署 xxl-job-admin (需要创建数据库 xxl_job)
```shell
docker run -d \
      -e PARAMS="--spring.datasource.url=jdbc:mysql://192.168.88.128:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=UTC --spring.datasource.username=root --spring.datasource.password=24364726" 
      -p 8093:8080  
      -v xxldata:/data/applogs 
      --name=xxl-job-admin 
      xuxueli/xxl-job-admin:2.3.0
```


### Docker 部署 MinIO
```shell
docker run -p 9000:9000 \
       --name minio -d \
       --restart=always \
       -e "MINIO_ACCESS_KEY=minio" \
       -e "MINIO_SECRET_KEY=minio" \
       -v /home/data:/data \
       -v /home/config:/root/.minio minio/minio \
       server /data
```


### Docker 部署 Sentinel
```shell
docker run -d \
      --name sentinel \
      -p 8858:8858 \ 
      bladex/sentinel-dashboard:1.8.0
```