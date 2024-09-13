## Apache Kafka on Ubuntu

To install and use Apache Kafka on Ubuntu, follow these detailed steps (from ChatGPT):

1. Install Java
   Apache Kafka requires Java to run. Ensure you have Java installed. If not, install OpenJDK:

```
sudo apt update
sudo apt install openjdk-11-jdk
```

Verify the installation:

```
java -version
```

2. Download and Install Kafka

Navigate to the Directory Where You Want to Download Kafka:

```
cd /opt
```

Download Kafka:

Use wget to download Kafka. Replace x.x.x with the latest version number from the Apache Kafka downloads page.

```
sudo wget https://downloads.apache.org/kafka/x.x.x/kafka_2.13-x.x.x.tgz
```

Extract the Kafka Archive:

```
sudo tar -xzf kafka_2.13-x.x.x.tgz
```

Create a Symbolic Link (Optional):

Create a symbolic link for easier access:

```
sudo ln -s kafka_2.13-x.x.x kafka
```

Navigate to the Kafka Directory:

```
cd kafka
```

3. Start ZooKeeper and Kafka

Start ZooKeeper:

ZooKeeper is required for Kafka to manage distributed brokers. Start ZooKeeper with the default configuration:

```
sudo bin/zookeeper-server-start.sh config/zookeeper.properties
```

Keep this terminal open for ZooKeeper.

Open a New Terminal and Start Kafka Broker:

```
sudo bin/kafka-server-start.sh config/server.properties
```

Keep this terminal open for Kafka.

4. Verify Kafka Installation

Create a Test Topic:

```
bin/kafka-topics.sh --create --topic test --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

List Topics:

```
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

Send a Test Message:

```
bin/kafka-console-producer.sh --topic test --bootstrap-server localhost:9092
```

Type a message and press Enter to send it.

Consume Test Messages:

```
bin/kafka-console-consumer.sh --topic test --from-beginning --bootstrap-server localhost:9092
```

You should see the message you typed earlier.

Delete a topic:

```
bin/kafka-topics.sh --delete --bootstrap-server localhost:9092 --topic test
```
