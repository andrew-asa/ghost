package com.asa.browser.widget.degger.screenshot;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author andrew_asa
 * @date 2021/7/7.
 */
public interface OutputType<T> {
    OutputType<String> BASE64 = new OutputType<String>() {
        public String convertFromBase64Png(String base64Png) {
            return base64Png;
        }

        public String convertFromPngBytes(byte[] png) {
            return Base64.getEncoder().encodeToString(png);
        }

        public String toString() {
            return "OutputType.BASE64";
        }
    };
    OutputType<byte[]> BYTES = new OutputType<byte[]>() {
        public byte[] convertFromBase64Png(String base64Png) {
            return Base64.getMimeDecoder().decode(base64Png);
        }

        public byte[] convertFromPngBytes(byte[] png) {
            return png;
        }

        public String toString() {
            return "OutputType.BYTES";
        }
    };
    OutputType<File> FILE = new OutputType<File>() {
        public File convertFromBase64Png(String base64Png) {
            return this.save((byte[])BYTES.convertFromBase64Png(base64Png));
        }

        public File convertFromPngBytes(byte[] data) {
            return this.save(data);
        }

        private File save(byte[] data) {
            FileOutputStream stream = null;

            File ret = null;
            try {
                File tmpFile = File.createTempFile("screenshot", ".png");
                tmpFile.deleteOnExit();
                stream = new FileOutputStream(tmpFile);
                stream.write(data);
                ret = tmpFile;
            } catch (IOException ex) {
                //throw new WebDriverException(var13);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException var12) {
                    }
                }

            }

            return ret;
        }

        public String toString() {
            return "OutputType.FILE";
        }
    };

    T convertFromBase64Png(String var1);

    T convertFromPngBytes(byte[] var1);
}
