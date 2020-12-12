package com.yq.opengldemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL;

public class TextureHelper {
    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            L.e("texture id is 0");
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            L.e("bitmap is null");
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);

        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            //tells OpenGL to use trilinear filtering.
            GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            //tells OpenGL to use bilinear filtering
            GLES20.GL_LINEAR);

        //tells OpenGL to read in the bitmap data defined by bitmap and copy it over into the texture object that is currently bound.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        //it might take a few garbage collection cycles for Dalvik to release this bitmap data,
        // so we should call recycle() on the bitmap object to release the data immediately:
        bitmap.recycle();

        //generate all of the necessary levels
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        //a good practice is to then unbind from the texture so that we don’t accidentally make further changes to this texture with other texture calls
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }
}
