package uniandrade.br.edu.com.popseries.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by pnda on 06/04/18.
 *
 */

public class ImageConverter {

    public static Bitmap getImageBytes(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    public static byte[] converterImageToByte(String poster) {
        try{
            URL url = new URL(poster);
            InputStream inputStream;
            final Bitmap image;
            inputStream = url.openStream();
            image = BitmapFactory.decodeStream( inputStream );
            inputStream.close();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress( Bitmap.CompressFormat.PNG, 100, stream );
            byte[] bytes = stream.toByteArray();
            return bytes;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
