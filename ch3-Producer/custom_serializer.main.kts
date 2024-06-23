@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.apache.kafka:kafka-clients:3.7.0")
@file:DependsOn("io.confluent:kafka-schema-registry-client:7.0.0")
@file:DependsOn("io.confluent.kafka:avro-serializer:7.0.0")
@file:DependsOn("org.apache.avro:avro:1.11.3")

import java.util.Properties
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.avro.generic.GenericRecord
import org.apache.avro.generic.GenericData
import org.apache.avro.Schema

class Customer(val id: Int, val name: String, val email: String)

val props = Properties()
props.put("bootstrap.servers", "localhost:9092")
props.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer")
props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer")

val topic = "customerContracts"
val producer = KafkaProducer<String, GenericRecord>(props)

val schemaString = """
{
  "type": "record",
  "name": "Customer",
  "fields": [
    {"name": "id", "type": "int"},
    {"name": "name", "type": "string"},
    {"name": "email", "type": ["null", "string"], "default:"null"}
  ]
}
""".trimIndent()

val parser = Schema.Parser()
val schema = parser.parse(schemaString)

for (i in 1..10) {
    val customer = Customer(
        id = i,
        name = "Customer $i",
        email = "test@test.com"
    )

    val avroRecord = GenericData.Record(schema)
    avroRecord.put("id", customer.id)
    avroRecord.put("name", customer.name)
    avroRecord.put("email", customer.email)

    val record = ProducerRecord<String, GenericRecord>(topic, avroRecord)

    try {
        producer.send(record).get()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}