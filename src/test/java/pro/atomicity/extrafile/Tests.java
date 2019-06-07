package pro.atomicity.extrafile;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Tests {

    @Test(expected = IllegalArgumentException.class)
    public void create_GivenEmptyString_ShouldThrow() {
        new MultipartFileWrapper("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_GivenDirectory_ShouldThrow() {
        new MultipartFileWrapper("/var/www");
    }

    @Test
    public void getBytes_GivenCorrectFile_ShouldWorks() throws IOException {

        Path tmp = Files.createTempFile("test_", ".txt");
        Files.write(tmp, "hello".getBytes(), StandardOpenOption.CREATE);

        MultipartFileWrapper wrapper = new MultipartFileWrapper(tmp);
        Assert.assertNotNull(wrapper.getBytes());
        Assert.assertEquals("hello", new String(wrapper.getBytes()));
    }

    @Test
    public void getInputStream_GivenCorrectFile_ShouldWorks() throws IOException {

        Path tmp = Files.createTempFile("test_", ".txt");
        Files.write(tmp, "hello".getBytes(), StandardOpenOption.CREATE);

        MultipartFileWrapper wrapper = new MultipartFileWrapper(tmp);
        InputStream stream = wrapper.getInputStream();

        byte[] buffer = new byte["hello".getBytes().length];

        BufferedInputStream buffStream = new BufferedInputStream(stream);
        int c = buffStream.read(buffer);

        Assert.assertEquals("hello", new String(buffer, 0, c));

        buffStream.close();
        Files.delete(tmp);
    }

    @Test
    public void getContentType_GivenCorrectFile_ShouldWorks() throws IOException {
        Path tmp = Files.createTempFile("test_", ".txt");
        Files.write(tmp, "hello".getBytes(), StandardOpenOption.CREATE);

        MultipartFileWrapper wrapper = new MultipartFileWrapper(tmp);

        Assert.assertEquals("text/plain", wrapper.getContentType());
    }
}
