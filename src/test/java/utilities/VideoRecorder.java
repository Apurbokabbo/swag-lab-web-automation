package utilities;

import io.qameta.allure.Allure;
import org.monte.media.Format;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class VideoRecorder {

    private static final ThreadLocal<ScreenRecorder> RECORDER = new ThreadLocal<>();
    private static final File VIDEO_DIR = new File("target/videos");

    /** Start recording. Call from @BeforeMethod. Works only in headed mode. */
    public static void startRecording(String testName) {
        try {
            if (GraphicsEnvironment.isHeadless()) {
                return; // no display available, skip silently
            }
            VIDEO_DIR.mkdirs();

            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration();

            ScreenRecorder recorder = new NamedScreenRecorder(gc, gc.getBounds(),
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO,
                            EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                            QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO,
                            EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                    null, VIDEO_DIR, testName);

            recorder.start();
            RECORDER.set(recorder);
        } catch (Exception e) {
            System.out.println("Video recording could not start: " + e.getMessage());
        }
    }

    /**
     * Stop recording. Call from @AfterMethod.
     * @param keep true = attach to Allure and keep file (use for failed tests),
     *             false = delete the file (passed tests)
     */
    public static void stopRecording(boolean keep) {
        ScreenRecorder recorder = RECORDER.get();
        if (recorder == null) return;

        try {
            recorder.stop();
            for (File video : recorder.getCreatedMovieFiles()) {
                if (keep) {
                    try (FileInputStream in = new FileInputStream(video)) {
                        Allure.addAttachment("Video", "video/avi", in, "avi");
                    }
                } else {
                    video.delete();
                }
            }
        } catch (IOException e) {
            System.out.println("Video recording could not stop: " + e.getMessage());
        } finally {
            RECORDER.remove();
        }
    }

    // Names the file: <testName>_<timestamp>.avi
    private static class NamedScreenRecorder extends ScreenRecorder {
        private final String name;

        NamedScreenRecorder(GraphicsConfiguration gc, Rectangle area, Format fileFormat,
                            Format screenFormat, Format mouseFormat, Format audioFormat,
                            File folder, String name) throws IOException, AWTException {
            super(gc, area, fileFormat, screenFormat, mouseFormat, audioFormat, folder);
            this.name = name;
        }

        @Override
        protected File createMovieFile(Format fileFormat) throws IOException {
            if (!movieFolder.exists()) movieFolder.mkdirs();
            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            return new File(movieFolder, name + "_" + time + ".avi");
        }
    }
}