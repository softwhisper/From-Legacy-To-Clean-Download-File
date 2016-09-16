package es.apa.downloadfilesample.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.ScrollBar;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.File;

import es.apa.downloadfilesample.R;

/**
 * Created by alberto on 6/8/16.
 */
public class PdfViewerActivity extends AppCompatActivity {

    
    private static final String TAG = PdfViewerActivity.class.getSimpleName();
    private PDFView pdfView;
    

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        ScrollBar scrollBar = (ScrollBar) findViewById(R.id.scrollBar);
        scrollBar.setHorizontal(true);
        pdfView.setScrollBar(scrollBar);
        String pdf = getIntent().getStringExtra("pdf");
        File file = new File(pdf);
        pdfView.fromFile(file)
                .enableSwipe(true)
                .enableDoubletap(true)
                .swipeVertical(false)
                .defaultPage(1)
                .showMinimap(false)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        Log.d(TAG, "loadComplete: " + nbPages);
                    }
                })
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        Log.d(TAG, "onPageChanged: " + page);
                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: " + t.getMessage());
                    }
                })
                .enableAnnotationRendering(false)
                .showPageWithAnimation(true)
                .load();
    }
}
