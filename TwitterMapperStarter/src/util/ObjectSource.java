package util;

import java.io.*;

/**
 * Read objects from a file
 */
public class ObjectSource {
    private File file;
    private ObjectInputStream instream;

    public ObjectSource(String filename)  {
        file = new File(filename);
        try {
            instream = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Object readObject() {
        Object object = null;
        try {
            object = instream.readObject();
        } catch (EOFException exception) {
            // Do nothing, EOF is expected to happen eventually
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return object;
    }

    public void close() {
        try {
            instream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
