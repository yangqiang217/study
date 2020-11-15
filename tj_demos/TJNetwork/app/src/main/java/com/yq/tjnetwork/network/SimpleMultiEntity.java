
package com.yq.tjnetwork.network;

import com.yq.tjnetwork.model.ModelManagerBase.ReqInfoTaskBase;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

class SimpleMultiEntity implements HttpEntity {
    private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .toCharArray();

    private String mBoundary = null;

    private ByteArrayOutputStream mOutStream = new ByteArrayOutputStream();
    private boolean mIsSetLast = false;
    private boolean mIsSetFirst = false;

    private AsyncHttpRespHandlerBase mRresponseHandler = null;
    private ReqInfoTaskBase mReqInfoTask;

    public SimpleMultiEntity() {
        final StringBuffer buf = new StringBuffer();
        final Random rand = new Random();
        for (int i = 0; i < 40; i++) {
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        this.mBoundary = buf.toString();

    }

    /**
     * set AsyncHttpRespHandlerBase for notify post grogress
     */
    public void setAsyncHttpRespHandlerBase(AsyncHttpRespHandlerBase responseHandler) {
        this.mRresponseHandler = responseHandler;
    }

    /**
     * set ReqInfoTaskBase for notify post grogress
     */
    public void SettingReqInfoTaskBase(ReqInfoTaskBase task) {
        this.mReqInfoTask = task;
    }

    public void writeFirstBoundaryIfNeeds() {
        if (!mIsSetFirst) {
            try {
                mOutStream.write(("--" + mBoundary + "\r\n").getBytes());
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        mIsSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
        if (mIsSetLast) {
            return;
        }

        try {
            mOutStream.write(("\r\n--" + mBoundary + "--\r\n").getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }

        mIsSetLast = true;
    }

    public void addPart(final String key, final String value) {
        writeFirstBoundaryIfNeeds();
        try {
            mOutStream.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
            mOutStream.write(value.getBytes());
            mOutStream.write(("\r\n--" + mBoundary + "\r\n").getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void addPart(final String key, final String fileName, final InputStream fin) {
        addPart(key, fileName, fin, "application/octet-stream");
    }

    /**
     * 增加boundary分隔符
     */
    public void addBoundary() {
        try {
            mOutStream.write(("\r\n--" + mBoundary + "\r\n").getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void addPart(final String key, final String fileName, final InputStream fin, String type) {
        writeFirstBoundaryIfNeeds();
        try {
            type = "Content-Type: " + type + "\r\n";
            mOutStream
                    .write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"\r\n")
                            .getBytes());
            mOutStream.write(type.getBytes());
            mOutStream.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

            final byte[] tmp = new byte[4096];
            int l = 0;
            while ((l = fin.read(tmp)) != -1) {
                mOutStream.write(tmp, 0, l);
            }
            mOutStream.flush();

        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPart(final String key, final File value) {
        try {
            addPart(key, value.getName(), new FileInputStream(value));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // @Override 1.6版本不支持@Override
    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return mOutStream.toByteArray().length;
    }

    // @Override
    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + mBoundary);
    }

    // @Override
    public boolean isChunked() {
        return false;
    }

    // @Override
    public boolean isRepeatable() {
        return false;
    }

    // @Override
    public boolean isStreaming() {
        return false;
    }

    // @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        byte[] data = mOutStream.toByteArray();
        if (data != null) {
            int off = 0;
            int perSize = data.length / 10;
            if (perSize <= 0 || perSize > 4096) {
                perSize = 4096;
            }
            try {
                while (off < data.length) {
                    if ((off + perSize) >= data.length) {
                        outstream.write(data, off, data.length - off);
                        off = data.length;
                    } else {
                        outstream.write(data, off, perSize);
                        off += perSize;
                    }
                    outstream.flush();

                    if (mReqInfoTask.mProgress >= 0) {
                        int percent = 100 * off / data.length;
                        this.mRresponseHandler.onProgress(mReqInfoTask, percent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        data = null;
    }

    // @Override
    public Header getContentEncoding() {
        return null;
    }

    // @Override
    public void consumeContent() throws IOException, UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
                    "Streaming entity does not implement #consumeContent()");
        }
    }

    // @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        return new ByteArrayInputStream(mOutStream.toByteArray());
    }
}
