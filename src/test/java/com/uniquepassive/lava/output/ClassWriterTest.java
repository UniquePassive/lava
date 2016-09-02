package com.uniquepassive.lava.output;

import com.uniquepassive.lava.input.ClassReader;
import com.uniquepassive.lava.types.info.RawClassInfo;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClassWriterTest {

    @Test
    public void run() throws IOException {
        byte[] inputBytes = Files.readAllBytes(Paths.get("testing_class.class"));

        RawClassInfo rawClassInfo = new ClassReader()
                .parse(inputBytes);

        byte[] outputBytes = new ClassWriter()
                .write(rawClassInfo);

        Assert.assertArrayEquals(inputBytes, outputBytes);
    }
}
