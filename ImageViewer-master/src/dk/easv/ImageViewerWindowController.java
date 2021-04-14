package dk.easv;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewerWindowController implements Initializable
{
    private final List<Image> images = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();
    public Slider slider;
    public Label imageNameLbl;
    private int currentImageIndex = 0;
    private Scheduler imageScheduler = new Scheduler();



    @FXML
    Parent root;

    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void handleBtnLoadAction()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty())
        {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
                images.add(new Image(f.toURI().toString()));
                titles.add(f.getName());
                titles.add(f.getName());
            });
            Slideshow s = new Slideshow(imageView, imageNameLbl, images, titles, currentImageIndex);
            imageScheduler.addSlideshow(s);
            displayImage();
        }
    }

    @FXML
    private void handleBtnPreviousAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex =
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage()
    {
        if (!images.isEmpty())
        {
            imageView.setImage(images.get(currentImageIndex));
            imageNameLbl.setText(titles.get(currentImageIndex));
        }
    }

    
    public void handleBtnStartSlideshow(ActionEvent actionEvent) {
        imageScheduler.startSlideshow();
    }

    public void handleBtnStopSlideshow(ActionEvent actionEvent) {
        imageScheduler.stopSlideshow();
    }




}