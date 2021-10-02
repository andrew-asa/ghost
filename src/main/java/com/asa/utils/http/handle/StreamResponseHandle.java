package com.asa.utils.http.handle;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamResponseHandle extends BaseHttpResponseHandle<ByteArrayInputStream> {

    public static final StreamResponseHandle DEFAULT = new StreamResponseHandle();

    private static final int BUFFER_LENGTH = 8000;

    public StreamResponseHandle() {

    }

    public StreamResponseHandle(String encoding) {

        super(encoding);
    }

    @Override
    public ByteArrayInputStream parse(CloseableHttpResponse response) throws IOException {

        InputStream in = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                in = entity.getContent();
                byte[] buff = new byte[BUFFER_LENGTH];
                int bytesRead;
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                while ((bytesRead = in.read(buff)) != -1) {
                    bao.write(buff, 0, bytesRead);
                }
                byte[] data = bao.toByteArray();
                return new ByteArrayInputStream(data);
            }
            return null;
        } finally {
            if (response != null) {
                response.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }
}
