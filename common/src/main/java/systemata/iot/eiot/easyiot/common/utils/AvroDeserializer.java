//package systemata.iot.eiot.easyiot.common.utils;
//
//import org.apache.avro.Schema;
//import org.apache.avro.generic.GenericDatumReader;
//import org.apache.avro.generic.GenericRecord;
//import org.apache.avro.io.Decoder;
//import org.apache.avro.io.DecoderFactory;
//
//import java.io.IOException;
//
//public class AvroDeserializer {
//    private static final String SCHEMA_JSON = "{\"type\":\"record\",\"name\":\"DeviceEvent\",\"fields\":[{\"name\":\"deviceId\",\"type\":\"string\"},{\"name\":\"status\",\"type\":\"string\"}]}";
//
//    public static GenericRecord deserialize(byte[] data) {
//        try {
//            Schema schema = new Schema.Parser().parse(SCHEMA_JSON);
//            GenericDatumReader<GenericRecord> reader = new GenericDatumReader<>(schema);
//            Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
//            return reader.read(null, decoder);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to deserialize Avro message", e);
//        }
//    }
//}
