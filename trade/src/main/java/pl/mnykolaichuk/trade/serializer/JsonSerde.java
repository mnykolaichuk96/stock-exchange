package pl.mnykolaichuk.trade.serializer;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class JsonSerde<T> implements Serializer<T>, Deserializer<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;

    public JsonSerde(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, T data) {
        return gson.toJson(data).getBytes();
    }

    @Override
    public void close() {
        Serializer.super.close();
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        return gson.fromJson(new String(data), clazz);
    }


}

