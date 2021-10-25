
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class FrameUtil {
    //��������ý�������˽�л�
    private FrameUtil() {
    }

    /**
     * ����ָ��·���ļ���ͼƬ����
     * @param path
     * @return
     */
    public static Image getImage(String path) {
        BufferedImage bi = null;
        try {
            URL u = FrameUtil.class.getClassLoader().getResource(path);
            bi = ImageIO.read(u);
        }catch(IOException e) {
            e.printStackTrace();
        }
        return bi;
    }
}
