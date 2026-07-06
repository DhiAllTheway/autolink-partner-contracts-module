package utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    public static void generateAndShowQRCode(String content) {
        try {
            int width = 300;
            int height = 300;

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

            ImageView imageView = new ImageView(fxImage);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);

            VBox vbox = new VBox(imageView);
            vbox.setStyle("-fx-padding: 20; -fx-background-color: white;");
            Scene scene = new Scene(vbox);

            Stage stage = new Stage();
            stage.setTitle("Entreprise QR Code");
            stage.setScene(scene);
            stage.show();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
