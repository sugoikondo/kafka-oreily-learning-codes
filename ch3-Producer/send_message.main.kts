@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.apache.kafka:kafka-clients:3.7.0")

import java.util.Properties
import org.apache.kafka.clients.producer.KafkaProducer

val kafkaProperties = Properties()
// サンプルでは 2 つ以上のブローカーを指定しているが、ここでは簡易化のために 1 つのみ設定する
kafkaProperties.put("bootstrap.servers", "localhost:9092")
kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

val producer = KafkaProducer<String, String>(kafkaProperties)
