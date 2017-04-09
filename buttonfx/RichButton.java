/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buttonfx;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 *
 * @author Alessio
 */
public class RichButton extends Group {

    private final static double OFFSET_SHADOW_X = 0;
    private final static double OFFSET_SHADOW_Y = 0;

    private final static double MAX_ROTATION_ANGLE = 30;
    private final static double SHADOW_DISPACEMENT_FACTOR = 1.4;

    private final static double SHADOW_BLUR_FACTOR = 22;

    private Rectangle backgroundRectangle;
    private ImageView imageIconView;
    private ImageView imageShadowIconView;

    private Rotate backgroundRotation_XAxis;
    private Rotate backgroundRotation_YAxis;

    private Button button;

    private RichButton() {

    }

    public RichButton(double width, double height, Paint paint, Image icon) {
        backgroundRectangle = new Rectangle(0, 0, width, height);
        backgroundRectangle.setFill(paint);

        getChildren().add(backgroundRectangle);

        imageIconView = new ImageView(icon);
        imageIconView.relocate((width - icon.getWidth()) / 2, (height - icon.getHeight()) / 2);

        imageShadowIconView = new ImageView(icon);
        imageShadowIconView.relocate((width - icon.getWidth()) / 2 + OFFSET_SHADOW_X, (height - icon.getHeight()) / 2 + OFFSET_SHADOW_Y);

        getChildren().addAll(imageShadowIconView, imageIconView);

        // generate shadow
        ColorAdjust colorBW = new ColorAdjust(0, -1, -1, -2);
        GaussianBlur blur = new GaussianBlur(SHADOW_BLUR_FACTOR);
        colorBW.setInput(blur);
        imageShadowIconView.setEffect(colorBW);

        // setup_rotation
        backgroundRotation_XAxis = new Rotate(0, Rotate.X_AXIS);
        backgroundRotation_YAxis = new Rotate(0, Rotate.Y_AXIS);

        backgroundRectangle.getTransforms().addAll(backgroundRotation_XAxis, backgroundRotation_YAxis);

        imageIconView.setMouseTransparent(true);
        imageShadowIconView.setMouseTransparent(true);
        backgroundRectangle.setMouseTransparent(true);

        button = new Button();
        button.relocate(0, 0);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        button.setStyle("-fx-background-color:transparent;");

        getChildren().add(button);

        button.setOnMouseMoved(e -> {

            double mouseX = e.getX();
            double mouseY = e.getY();

            // calculate angle
            // widht : MAX_ROTATION_ANGLE = mouseX : x  ===> angle = (MAX_ROTATION_ANGLE * mouseX) / width
            double angleX = MAX_ROTATION_ANGLE * mouseX / button.getPrefWidth();

            angleX = -1 * (angleX - (MAX_ROTATION_ANGLE / 2));

            double angleY = MAX_ROTATION_ANGLE * mouseY / button.getPrefHeight();

            angleY = angleY - (MAX_ROTATION_ANGLE / 2);

            backgroundRotation_XAxis.setAngle(angleY);
            backgroundRotation_YAxis.setAngle(angleX);
            imageShadowIconView.setLayoutX((width - icon.getWidth()) / 2 + OFFSET_SHADOW_X + angleX * SHADOW_DISPACEMENT_FACTOR);
            imageShadowIconView.setLayoutY((height - icon.getHeight()) / 2 + OFFSET_SHADOW_Y - angleY * SHADOW_DISPACEMENT_FACTOR);
//
//            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1),
//                                                          new KeyValue(backgroundRotation_XAxis.angleProperty(), angleY),
//                                                          new KeyValue(backgroundRotation_YAxis.angleProperty(), angleX),
//                                                          new KeyValue(imageShadowIconView.layoutXProperty(), (width - icon.getWidth()) / 2 + OFFSET_SHADOW_X + angleX * SHADOW_DISPACEMENT_FACTOR),
//                                                          new KeyValue(imageShadowIconView.layoutYProperty(), (height - icon.getHeight()) / 2 + OFFSET_SHADOW_Y - angleY * SHADOW_DISPACEMENT_FACTOR)));
//            timeline.play();

        });

        button.setOnMouseExited(e -> {

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1),
                                                          new KeyValue(backgroundRotation_XAxis.angleProperty(), 0),
                                                          new KeyValue(backgroundRotation_YAxis.angleProperty(), 0),
                                                          new KeyValue(imageShadowIconView.layoutXProperty(), (width - icon.getWidth()) / 2 + OFFSET_SHADOW_X),
                                                          new KeyValue(imageShadowIconView.layoutYProperty(), (height - icon.getHeight()) / 2 + OFFSET_SHADOW_Y)));
            timeline.play();
        });

    }
}
