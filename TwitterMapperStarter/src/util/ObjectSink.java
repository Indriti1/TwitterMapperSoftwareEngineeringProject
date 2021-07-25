package util;

import java.io.*;


public class ObjectSink {
    private File file;
    private ObjectOutputStream outstream;

    public ObjectSink(String filename) {
        try {
            file = new File(filename);
            outstream = new ObjectOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void storeObject(Object object) {
        try {
            outstream.writeObject(object);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void close() {
        try {
            outstream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
