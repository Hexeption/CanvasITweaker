package com.kiloclient.utilities;

import com.kiloclient.resource.ResourceHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class IconUtilities {

    public static ByteBuffer[] icon() {

        InputStream[] icons = ResourceHelper.displayIcon;

        ByteBuffer[] buffers = new ByteBuffer[icons.length];

        try {
            for (int i = 0; i < icons.length; i++) {
                buffers[i] = readImageToBuffer(icons[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffers;

    }

    private static ByteBuffer readImageToBuffer(InputStream stream) throws IOException {

        BufferedImage image = ImageIO.read(stream);
        int[] rgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        ByteBuffer buffer = ByteBuffer.allocate(4 * rgb.length);

        for (int color : rgb) {
            buffer.putInt(color << 8 | color >> 24 & 255);
        }

        buffer.flip();
        return buffer;
    }

}