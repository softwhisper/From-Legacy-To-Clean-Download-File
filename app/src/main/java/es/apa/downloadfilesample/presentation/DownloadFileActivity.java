package es.apa.downloadfilesample.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import es.apa.downloadfilesample.R;
import es.apa.downloadfilesample.application.Download;
import es.apa.downloadfilesample.application.downloadfile.DownloadFile;
import es.apa.downloadfilesample.application.downloadfile.DownloadFileUseCase;
import es.apa.downloadfilesample.infrastructure.concurrent.ExecutorAdapter;
import es.apa.downloadfilesample.infrastructure.concurrent.ExecutorImpl;
import es.apa.downloadfilesample.infrastructure.concurrent.MainThreadImpl;
import es.apa.downloadfilesample.infrastructure.disk.Disk;
import es.apa.downloadfilesample.infrastructure.disk.DiskAdapter;
import es.apa.downloadfilesample.infrastructure.disk.DiskState;
import es.apa.downloadfilesample.infrastructure.disk.DiskStateAdapter;
import es.apa.downloadfilesample.infrastructure.network.DowloadFile;
import es.apa.downloadfilesample.infrastructure.permissions.Permissions;

public class DownloadFileActivity
        extends AppCompatActivity
        implements View.OnClickListener{


    private DownloadFile.UseCase downloadFileUseCase;
    private String url = "https://ia802508.us.archive.org/28/items/pdfy-TIGwEuUmNnQVm7x7/The%20Way%20Of%20The%20Superior%20Man.pdf";
    private File rootFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    private File toFile = new File(rootFolder, "down_file");
    private Button btn;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);
        Disk disk = new Disk();
        es.apa.downloadfilesample.application.Disk.Accesor accesor = new DiskAdapter(disk);
        DiskState diskState = new DiskState();
        es.apa.downloadfilesample.application.Disk.State state = new DiskStateAdapter(diskState);
        Download.File downloadFile = new DowloadFile();
        downloadFileUseCase = new DownloadFileUseCase(accesor, state, new ExecutorAdapter(new ExecutorImpl(), new MainThreadImpl()), downloadFile);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);

        findViewById(R.id.legacy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadThread downloadThread = new DownloadThread(url, "legacy_down_file");
                downloadThread.start();

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        downloadFileUseCase.stop();
    }


    @Override
    public void onClick(View view) {
        Permissions.check(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, new Permissions.Callback() {
            @Override
            public void onPermissionGranted() {
                btn.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                if (!downloadFileUseCase.isRunning()){
                    downloadFileUseCase.start(url, toFile, new DownloadFile.UseCase.Callback() {
                        @Override
                        public void onDownload(File storedFile) {
                            btn.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(DownloadFileActivity.this, PdfViewerActivity.class);
                            intent.putExtra("pdf", storedFile.getAbsolutePath());
                            startActivity(intent);
                        }

                        @Override
                        public void onDownloadError(Throwable throwable) {
                            btn.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(DownloadFileActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(DownloadFileActivity.this, "permission denied...", Toast.LENGTH_LONG).show();
            }
        });
    }



    //TODO: errors
    //	-> estaba inner sin ser static => leak!!, thread retiene activity
    //  -> no se verifica si el almacenamiento esta preparado para escritura
    //  -> no se verifican permisos de android 6
    //  -> no se verifica si el fichero ya existe
    //  -> no se cancela si se sale de la activity
    //  -> no se borra el fichero si se cancela y se queda a medias
    //  -> se abre una activity aunque navegemos fuera de la actual.
    //TODO: not tested before change byteArray...pending
    public class DownloadThread extends Thread {


        private String DownloadUrl;
        private String fileName;


        public DownloadThread(String downloadUrl, String fileName) {
            super();
            DownloadUrl = downloadUrl;
            this.fileName = fileName;
        }


        // After call for background.start this run method call
        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

                File root = android.os.Environment.getExternalStorageDirectory();

                File dir = new File (root.getAbsolutePath() + "/xmls");
                if(dir.exists()==false) {
                    dir.mkdirs();
                }

                URL url = new URL(DownloadUrl); //you can write here any link
                File file = new File(dir, fileName);

                long startTime = System.currentTimeMillis();
                Log.d("DownloadManager", "download begining");
                Log.d("DownloadManager", "download url:" + url);
                Log.d("DownloadManager", "downloaded file name:" + fileName);

				/* Open a connection to that URL. */
                URLConnection ucon = url.openConnection();

				/*
				 * Define InputStreams to read from the URLConnection.
				 */
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                FileOutputStream fos = new FileOutputStream(file);

                int current = 0;
                while ((current = bis.read()) != -1) {
                    fos.write(current);
                }

                fos.close();
                fos.flush();
                fos.close();
                Log.d("DownloadManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
                Intent intent2 = new Intent(DownloadFileActivity.this, PdfViewerActivity.class);
                intent2.putExtra("pdf", file.getAbsolutePath());
                startActivity(intent2);

            } catch (IOException e) {
                Log.d("DownloadManager", "Error: " + e);

            } catch (Throwable t) {
                // just end the background thread
                Log.i("Animation", "Thread  exception " + t);
            } finally {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
}
