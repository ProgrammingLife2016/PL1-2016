package io.github.programminglife2016.pl1_2016.parser;

import java.io.*;
import java.net.URISyntaxException;

/**
 * Serializer for any object of choice.
 * @author Ravi Autar
 */
public class ObjectSerializer {
    public void serializeItem(Object object, String infile) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(infile);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(object);
        out.close();
        fileOut.close();
        System.out.println("Object saved succesfully!");
    }

    public Object getSerializedItem(String infile) throws IOException, ClassNotFoundException {
        Object serializedObject = null;
        FileInputStream fileIn = new FileInputStream(infile);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        serializedObject = in.readObject();
        in.close();
        fileIn.close();
        return serializedObject;
    }
}
