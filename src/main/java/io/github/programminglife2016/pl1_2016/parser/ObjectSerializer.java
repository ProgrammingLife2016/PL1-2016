package io.github.programminglife2016.pl1_2016.parser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Serializer for any object of choice.
 * @author Ravi Autar
 */
public class ObjectSerializer {
    static void serializeItem(Object object, String infile) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(infile);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(object);
        out.close();
        fileOut.close();
        System.out.println("Object saved in: " + infile);
    }

    static Object getSerializedItem(String infile) throws IOException, ClassNotFoundException {
        Object object;
        FileInputStream fileIn = new FileInputStream(infile);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        object = in.readObject();
        in.close();
        fileIn.close();
        return object;
    }
}
