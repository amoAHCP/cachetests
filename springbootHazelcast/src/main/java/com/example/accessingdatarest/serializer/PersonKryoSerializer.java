package com.example.accessingdatarest.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.example.accessingdatarest.entity.Person;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class PersonKryoSerializer implements StreamSerializer<Person> {
    private final boolean compress;

    private static final ThreadLocal<Kryo> kryoThreadLocal
            = new ThreadLocal<Kryo>() {

        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.register(Person.class);
            return kryo;
        }
    };

    public PersonKryoSerializer(boolean compress) {
        this.compress = compress;
    }

    public int getTypeId() {
        return 2;
    }

    public void write(ObjectDataOutput objectDataOutput, Person product)
            throws IOException {
        Kryo kryo = kryoThreadLocal.get();

        if (compress) {
            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream(16384);
            DeflaterOutputStream deflaterOutputStream =
                    new DeflaterOutputStream(byteArrayOutputStream);
            Output output = new Output(deflaterOutputStream);
            kryo.writeObject(output, product);
            output.close();

            byte[] bytes = byteArrayOutputStream.toByteArray();
            objectDataOutput.write(bytes);
        } else {
            Output output = new Output((OutputStream) objectDataOutput);
            kryo.writeObject(output, product);
            output.flush();
        }
    }

    public Person read(ObjectDataInput objectDataInput)
            throws IOException {
        InputStream in = (InputStream) objectDataInput;
        if (compress) {
            in = new InflaterInputStream(in);
        }
        Input input = new Input(in);
        Kryo kryo = kryoThreadLocal.get();
        return kryo.readObject(input, Person.class);
    }

    public void destroy() {
    }
}