package dk.easv;

import java.util.concurrent.*;

public class Scheduler implements Runnable{
    private BlockingQueue<Slideshow> slideshows = new LinkedBlockingQueue();
    private int timeSplitter = 10;
    private Slideshow currentRunningSlideshow = null;
    private ExecutorService executor = null;


    @Override
    public void run() {
        try {
            while (true) {
                if (!slideshows.isEmpty()) {
                    playNextSlideshow();
                }
                TimeUnit.SECONDS.sleep(timeSplitter);
            }
        } catch (InterruptedException ex) {
        }
    }

    private void playNextSlideshow() throws InterruptedException {
        currentRunningSlideshow.handleStopSlideshow();
        slideshows.put(currentRunningSlideshow);
        currentRunningSlideshow = slideshows.take();
        currentRunningSlideshow.handleStartSlideshow();

    }

    public synchronized void addSlideshow(Slideshow slideshow){
        if(executor == null || executor.isShutdown()){
            executor = Executors.newSingleThreadExecutor();
            executor.submit(this);
        }

        if(currentRunningSlideshow == null && slideshows.isEmpty()){
            currentRunningSlideshow = slideshow;
        }
        else{
            try{
                slideshows.put(slideshow);
            }catch(InterruptedException ex){

            }
        }
    }
    public synchronized void startSlideshow(){
        executor.submit(this);
        currentRunningSlideshow.handleStartSlideshow();
    }

    public synchronized void stopSlideshow(){
        if(currentRunningSlideshow != null){
            currentRunningSlideshow.handleStopSlideshow();
        }
        if(slideshows.isEmpty()){
            executor.shutdownNow();
            currentRunningSlideshow = null;
            executor = null;
        }else{
            try{
                currentRunningSlideshow = slideshows.take();
                currentRunningSlideshow.handleStartSlideshow();
            }catch (InterruptedException ex){

            }
        }
    }


}
