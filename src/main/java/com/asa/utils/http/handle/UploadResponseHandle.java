package com.asa.utils.http.handle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class UploadResponseHandle extends BaseHttpResponseHandle<Void> {

    public static final UploadResponseHandle DEFAULT = new UploadResponseHandle();

    public UploadResponseHandle() {
    }

    public UploadResponseHandle(String encoding) {
        super(encoding);
    }

    @Override
    public Void parse(CloseableHttpResponse response) throws IOException {
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    EntityUtils.consume(entity);
                }
            } else {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, getEncoding());
                throw new IOException("Connect error, error code:" + statusCode + "; message:" + result);
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }
}
