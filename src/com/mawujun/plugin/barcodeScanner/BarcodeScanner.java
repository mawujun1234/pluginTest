package com.mawujun.plugin.barcodeScanner;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class BarcodeScanner extends CordovaPlugin {
	/**
	 * ��չ��ά��ɨ���phonegap��ʵ��
	 * ʵ��ԭ�����£�
	 *    1.ʹ��phonegap��js���ʵ��ͨ�����������ص�Plugin java�ࡣ
	 *    2.plugin����zxing��صĶ�ά��ɨ��ķ���ʵ�֡�
	 *    3.�������zxingû�а�װ����google������ص�zxing apk��װ�������ö�Ӧ��intentʵ�֡�
	 * 
	 * This calls out to the ZXing barcode reader and returns the result.
	 */
		public static final int REQUEST_CODE = 0x0ba7c0de;


		public static final String defaultInstallTitle = "Install Barcode Scanner?";
		public static final String defaultInstallMessage = "This requires the free Barcode Scanner app. Would you like to install it now?";
		public static final String defaultYesString = "Yes";
		public static final String defaultNoString = "No";

		public CallbackContext callbackContext;

	    /**
	     * Constructor.
	     */
		public BarcodeScanner() {
		}

		/**
		 * 
		 * ����plugin��صķ��������ڱ�¶��صķ���ʹ�á�
		 * 
		 * Executes the request and returns PluginResult.
		 *
		 * @param action 		The action to execute.
		 * @param args 			JSONArray of arguments for the plugin.
		 * @param callbackId	The callback id used when calling back into JavaScript.
		 * @return 				A PluginResult object with a status and message.
		 */
		public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
			//this.callback = callbackId;
			this.callbackContext=callbackContext;

			try {
				if (action.equals("encode")) {
					String type = null;
					if(args.length() > 0) {
						type = args.getString(0);
					}

					String data = null;
					if(args.length() > 1) {
						data = args.getString(1);
					}

					String installTitle = defaultInstallTitle;
					if(args.length() > 2) {
						installTitle = args.getString(2);
					}

					String installMessage = defaultInstallMessage;
					if(args.length() > 3) {
						installMessage = args.getString(3);
					}

					String yesString = defaultYesString;
					if(args.length() > 4) {
						yesString = args.getString(4);
					}

					String noString = defaultNoString;
					if(args.length() > 5) {
						noString = args.getString(5);
					}

					// if data.TypeOf() == Bundle, then call
					// encode(type, Bundle)
					// else
					// encode(type, String)
					this.encode(type, data, installTitle, installMessage, yesString, noString);
				}
				else if (action.equals("scan")) {
					String barcodeTypes = null;
					if(args.length() > 0) {
						barcodeTypes = args.getString(0);
					}

					String installTitle = defaultInstallTitle;
					if(args.length() > 1) {
						installTitle = args.getString(1);
					}

					String installMessage = defaultInstallMessage;
					if(args.length() > 2) {
						installMessage = args.getString(2);
					}

					String yesString = defaultYesString;
					if(args.length() > 3) {
						yesString = args.getString(3);
					}

					String noString = defaultNoString;
					if(args.length() > 4) {
						noString = args.getString(4);
					}

					scan(barcodeTypes, installTitle, installMessage, yesString, noString);
				} else {
		           // return new PluginResult(PluginResult.Status.INVALID_ACTION);
					PluginResult r = new PluginResult(PluginResult.Status.INVALID_ACTION);
		            r.setKeepCallback(true);
		            callbackContext.sendPluginResult(r);
		            return true;
				}
//				PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
//				r.setKeepCallback(true);
//				return r;
				PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
	            r.setKeepCallback(true);
	            callbackContext.sendPluginResult(r);
	            return true;
			} catch (JSONException e) {
//				e.printStackTrace();
//				return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
				e.printStackTrace();
				callbackContext.error("Illegal Argument Exception");
                PluginResult r = new PluginResult(PluginResult.Status.ERROR);
                callbackContext.sendPluginResult(r);
                return true;
			}
		}


		/**
		 * ɨ���ά��ķ���
		 *    ��ע����ɨ���ά���������ò������ã���ǰ�ڵ�zxing������Ҫ���ں��ڵİ汾�в���Ҫ��
		 *    zxing���Զ�������ά������ͣ���ʶ����ض�ά�롣
		 *    
		 * Initiates a barcode scan. If the ZXing scanner isn't installed, the user
		 * will be prompted to install it.
		 * @param types	The barcode types to accept
		 * @param installTitle The title for the dialog box that prompts the user to install the scanner
		 * @param installMessage The message prompting the user to install the barcode scanner
		 * @param yesString The string "Yes" or localised equivalent
		 * @param noString The string "No" or localised version
		 */
		public void scan(String barcodeFormats, String installTitle, String installMessage, String yesString, String noString ) {
		    Intent intentScan = new Intent("com.google.zxing.client.android.SCAN");
		   // intentScan.addCategory(Intent.CATEGORY_DEFAULT);

		    //����ɨ���ض����͵Ķ�ά��
		    //if (barcodeFormats != null) {
		    //      Tell the scanner what types we're after
		    //			intentScan.putExtra("SCAN_FORMATS", barcodeFormats);
		    // }
		    try {
				//this.ctx.startActivityForResult((Plugin) this, intentScan, REQUEST_CODE);
		    	this.cordova.startActivityForResult((CordovaPlugin) this, intentScan, REQUEST_CODE);
		    } catch (ActivityNotFoundException e) {
		    	showDownloadDialog(installTitle, installMessage, yesString, noString);
		    }
		}

	    /**
	     * ���ڻ�ȡ��ά��ɨ��֮���ȡ��صĶ�ά����ص���Ϣ
	     * Called when the barcode scanner exits
	     *
	     * @param requestCode		The request code originally supplied to startActivityForResult(),
	     * 							allowing you to identify who this result came from.
	     * @param resultCode		The integer result code returned by the child activity through its setResult().
	     * @param intent			An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
	     */
		public void onActivityResult(int requestCode, int resultCode, Intent intent) {
			if (requestCode == REQUEST_CODE) {
				if (resultCode == Activity.RESULT_OK) {
					String contents = intent.getStringExtra("SCAN_RESULT");
					String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
					//this.success(new PluginResult(PluginResult.Status.OK, " ������Ϊ:"+contents+" ��������Ϊ: "+format), this.callback);
					this.callbackContext.success(" ������Ϊ:"+contents+" ��������Ϊ: "+format);
				} else {
					//this.error(new PluginResult(PluginResult.Status.ERROR), this.callback);
					this.callbackContext.error(PluginResult.MESSAGE_TYPE_STRING);
				}
			}
		}

		/**
		 * ������صĶԻ�����ͨ��û�а�װ��ص�zxing��Դ���ʱ�򣬵���Զ�̵�intent�����������ִ����ʵ����ص�
		 * ����
		 * @param title
		 * @param message
		 * @param yesString
		 * @param noString
		 */
		private void showDownloadDialog(final String title, final String message, final String yesString, final String noString) {
			//final PhonegapActivity context = this.ctx;
			final Activity context = this.cordova.getActivity();
			Runnable runnable = new Runnable() {
				public void run() {

					AlertDialog.Builder dialog = new AlertDialog.Builder(context);
					dialog.setTitle(title);
					dialog.setMessage(message);
					dialog.setPositiveButton(yesString, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dlg, int i) {
							dlg.dismiss();
							//Intent intent = new Intent(Intent.ACTION_VIEW,
							//						   Uri.parse("http://market.android.com/search?q=pname:com.google.zxing.client.android")
							//						   );
							//�Ժ󻻳ɱ��ص�apk
							Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/zxing/zxing/releases"));
							//intent.setData(Uri.parse("https://github.com/zxing/zxing/releases"));
							//context.startActivity(in);
							try {
								context.startActivity(intent);
							} catch (ActivityNotFoundException e) {
								//	We don't have the market app installed, so download it directly.
								Intent in = new Intent(Intent.ACTION_VIEW);
								in.setData(Uri.parse("https://github.com/zxing/zxing/releases/download/BS-4.6.3/BarcodeScanner-4.6.3.apk"));
								context.startActivity(in);

							}

						}
					});
					dialog.setNegativeButton(noString, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dlg, int i) {
							dlg.dismiss();
						}
					});
					dialog.create();
					dialog.show();
				}
			};
			context.runOnUiThread(runnable);
		}

		/**
		 * 
		 * 
		 * Initiates a barcode encode. If the ZXing scanner isn't installed, the user
		 * will be prompted to install it.
		 * @param type  The barcode type to encode
		 * @param data  The data to encode in the bar code
		 * @param installTitle The title for the dialog box that prompts the user to install the scanner
		 * @param installMessage The message prompting the user to install the barcode scanner
		 * @param yesString The string "Yes" or localised equivalent
		 * @param noString The string "No" or localised version
		 */
		public void encode(String type, String data, String installTitle, String installMessage, String yesString, String noString) {
			Intent intentEncode = new Intent("com.google.zxing.client.android.ENCODE");
			intentEncode.putExtra("ENCODE_TYPE", type);
			intentEncode.putExtra("ENCODE_DATA", data);

			try {
				//this.ctx.startActivity(intentEncode);
				this.cordova.getActivity().startActivity(intentEncode);
			} catch (ActivityNotFoundException e) {
				showDownloadDialog(installTitle, installMessage, yesString, noString);
			}
		}
	}

