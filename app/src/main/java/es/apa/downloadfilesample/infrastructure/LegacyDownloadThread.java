package es.apa.downloadfilesample.infrastructure;


public class LegacyDownloadThread extends Thread {


		private String DownloadUrl;
		private String fileName;


		public LegacyDownloadThread(String downloadUrl, String fileName) {
			super();
			DownloadUrl = downloadUrl;
			this.fileName = fileName;
		}





}