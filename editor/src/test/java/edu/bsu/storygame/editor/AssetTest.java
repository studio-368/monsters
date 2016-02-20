package edu.bsu.storygame.editor;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;

public class AssetTest {

    @Test
    public void testLoadAssetFromTestAssetsModule() throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("test-encounters/cockatrice.json");
        assertNotNull("Cannot find resource.", in);
        in.close();
    }
}
