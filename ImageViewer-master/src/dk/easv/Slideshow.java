package dk.easv;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Slideshow implements Runnable{

    private final long DELAY = 1;
    private int index = 0;
    private ImageView imageView;
    private Label imageNameLbl;
    private List<Image> images;
    private List<String> filenames;
    private ExecutorService executor;

    public Slideshow(ImageView imageView, Label imageNameLbl, List<Image> images, List<String> filenames, int index) {
        this.imageView = imageView;
        this.imageNameLbl = imageNameLbl;
        this.images = images;
        this.filenames = filenames;
        this.index = index;
    }

    @Override
    public void run() {
        if(!images.isEmpty()){
            try{
                while (true){
                    Platform.runLater(() -> {
                        imageNameLbl.setText(filenames.get(index));
                        imageView.setImage(images.get(index));
                    });

                    index = (index + 1) % images.size();
                    TimeUnit.SECONDS.sleep(DELAY);

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleStartSlideshow() {
        if(executor != null && !executor.isShutdown())
        {
            executor.shutdownNow();
        }
        executor = Executors.newSingleThreadExecutor();
        executor.submit(this);
    }

    public void handleStopSlideshow() {
        executor.shutdownNow();
    }
}
