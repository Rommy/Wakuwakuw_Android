package labs.captcha;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

public class CaptchaService {
	private String token;
	private String verifyURL;
	private String adid;
	private String adurl;
	private String audioURL;
	private boolean result=false;
	private static String CAPTCHA_URL = "http://captcha.labs.ericsson.net/";
	private static final int BUFFER_IO_SIZE = 8000;

	/**
	 * Get bitmap image from labs CAPTCHA Enabler
	 * 
	 * @param devKey	API KEY
	 * @return			bitmap image when success; null when fail
	 * @throws CaptchaException
	 */
	public Bitmap getCaptcha(String devKey) throws CaptchaException {
		Bitmap image = null;
		try {
			String getTokenURL = CAPTCHA_URL + "/get?key=" + devKey;

			URL getUrl = new URL(getTokenURL);

			HttpURLConnection httpURLConnection = (HttpURLConnection) getUrl.openConnection();

			httpURLConnection.connect();

			int code = httpURLConnection.getResponseCode();
			System.out.println(code);
			if (code == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
				String jsonString = reader.readLine();

				JSONObject jsonObject = new JSONObject(jsonString);
				String imageURL = jsonObject.getString("image");
				this.token = jsonObject.getString("token");
				this.verifyURL = jsonObject.getString("verify");
				this.adid = jsonObject.getString("adid");
				this.adurl = jsonObject.getString("adurl");
				this.audioURL = jsonObject.getString("audio");

				reader.close();


				//ini cara defaultnya
				/*URL getImageURL = new URL(imageURL);
				httpURLConnection = (HttpURLConnection) getImageURL.openConnection();
				InputStream imageStream = httpURLConnection.getInputStream();
				code = httpURLConnection.getResponseCode();
				if(code == 200){
					image = BitmapFactory.decodeStream(imageStream);
					imageStream.close();
				} else {
					System.out.println("img response http status code is:" + code);
				}
				httpURLConnection.disconnect(); */
				
				
				//ini pake method bantu
				image = loadImageFromUrl(imageURL);
			} else {
				System.out.println("get response http status code is:" + code);
			}			

			httpURLConnection.disconnect();

		} catch(MalformedURLException e) {
			e.printStackTrace();
			throw new CaptchaException(e.getMessage());
		} catch(JSONException e) {
			e.printStackTrace();
			throw new CaptchaException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new CaptchaException(e.getMessage());
		}

		return image;
	}

	/**
	 * Verify the answer
	 * 
	 * @param token
	 * @param answer
	 * @param devKey
	 * @return			true when answer correct, false when error occurred 
	 */
	public boolean verify(String token, String answer, String devKey){
		try {
			result = false;
			//URL url = new URL(CAPTCHA_URL + "CaptchaVerify");
			URL url = new URL(this.verifyURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");


			OutputStream os = conn.getOutputStream();					
			String a = "answer=" + answer + "&token=" + this.token + "&key=" + devKey;
			os.write(a.getBytes("UTF-8"));

			InputStream is = conn.getInputStream();
			int code = conn.getResponseCode();

			int length = conn.getContentLength();
			if (code == 200) {
				result = true;
			}
			is.close();
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Read the CAPTCHA 
	 * 
	 * @return
	 */
	public boolean read() {
		MediaPlayer mp = new MediaPlayer();

		try {
			if (mp.isPlaying()) {
				mp.stop();
			}
			mp.reset();
			mp.setLooping(false);
			System.out.println(this.audioURL);
			mp.setDataSource(this.audioURL);
			mp.prepare();
			mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}
	
	
	//cara ngeload image dari URL, dan ini untuk solusi jika cara pertama
	//tidak berhasil dikarenakan bug dari SDKnya
	private Bitmap loadImageFromUrl(final String url) {
        try {
            // Addresses bug in SDK :
            // http://groups.google.com/group/android-developers/browse_thread/thread/4ed17d7e48899b26/
            BufferedInputStream bis = new BufferedInputStream(new URL(url).openStream(), BUFFER_IO_SIZE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(baos, BUFFER_IO_SIZE);
            copy(bis, bos);
            bos.flush();
            return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
        } 
        catch (IOException e) {
            // handle it properly
        	return null;
        }
    }

    private void copy(final InputStream bis, final OutputStream baos) throws IOException {
        byte[] buf = new byte[256];
        int l;
        while ((l = bis.read(buf)) >= 0) baos.write(buf, 0, l);
    }
    /////////////////////////////////////////////////////////

}