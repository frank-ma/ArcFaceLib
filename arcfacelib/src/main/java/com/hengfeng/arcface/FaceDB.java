package com.hengfeng.arcface;

import android.util.Log;

import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.guo.android_extend.java.ExtInputStream;
import com.guo.android_extend.java.ExtOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gqj3375 on 2017/7/11.
 */

public class FaceDB {
	private final String TAG = this.getClass().toString();

//	public static int screenOritation=0;//0横屏  1竖屏

	//	人脸追踪(FT):CwL6rikMxd2Bw8nFwrm2v5dpKmBSkx1wDpd2YwLMDngM
//	人脸检测(FD):CwL6rikMxd2Bw8nFwrm2v5dwVASbbqZdPrwBQdHkyF1X
//	人脸识别(FR):CwL6rikMxd2Bw8nFwrm2v5e4eZhmsh2dPy5ivVahyPaH

//	APP_Id:62vsyHzNBLPrXzD6JVW5Y59Y1XBbW944uaqBkC2YTwtB
//	SDK_key:
//	人脸追踪(FT):DiRwpWbXmoaRU1NKcAGMWKGiomRAREr9SsY3PpqgH41x
//	人脸检测(FD):DiRwpWbXmoaRU1NKcAGMWKGqyAgM6nzSwBhn6rQHwdoc
//	人脸识别(FR):DiRwpWbXmoaRU1NKcAGMWKHLcmj6Ba1K9x6wFrAWNDD4
//	年龄识别(Age):DiRwpWbXmoaRU1NKcAGMWKHawaFNym1fXUqzxhNjtaBz
//	性别识别(Gender):DiRwpWbXmoaRU1NKcAGMWKHi6yWbK7DdvW4xiHVE5iTf

//	static String appid = "62vsyHzNBLPrXzD6JVW5Y59Qr7vSFQBjUQsE5axoYDB1";
//	static String ft_key = "CwL6rikMxd2Bw8nFwrm2v5dpKmBSkx1wDpd2YwLMDngM";
//	static String fd_key = "CwL6rikMxd2Bw8nFwrm2v5dwVASbbqZdPrwBQdHkyF1X";
//	static String fr_key = "CwL6rikMxd2Bw8nFwrm2v5e4eZhmsh2dPy5ivVahyPaH";

//	static String appid = "62vsyHzNBLPrXzD6JVW5Y59Y1XBbW944uaqBkC2YTwtB";
//	static String ft_key = "DiRwpWbXmoaRU1NKcAGMWKGiomRAREr9SsY3PpqgH41x";
//	static String fd_key = "DiRwpWbXmoaRU1NKcAGMWKGqyAgM6nzSwBhn6rQHwdoc";
//	static String fr_key = "DiRwpWbXmoaRU1NKcAGMWKHLcmj6Ba1K9x6wFrAWNDD4";
//	static String age_key = "DiRwpWbXmoaRU1NKcAGMWKHawaFNym1fXUqzxhNjtaBz";
//	static String gender_key = "DiRwpWbXmoaRU1NKcAGMWKHi6yWbK7DdvW4xiHVE5iTf";

//改了appid之后要替换掉相应的so文件
	public static String appid = "62vsyHzNBLPrXzD6JVW5Y59fAvSnQgTAgXihGbbpd78R";
	public static String ft_key = "FbVPaqPGnWABkciCNZiqDuaHzPF3BDRi5JZW5KZFM7NM";
	public static String fd_key = "FbVPaqPGnWABkciCNZiqDuaR9nWGA7tjc7azwjjtt4HQ";
	public static String fr_key = "FbVPaqPGnWABkciCNZiqDuandzHnJbCHseUpSpyAveXr";
	public static String age_key = "FbVPaqPGnWABkciCNZiqDubA8C5EpfkBFtrMm4Tkr4Gr";
	public static String gender_key = "FbVPaqPGnWABkciCNZiqDubHHbLSUTJb8xoJ7RqTP8wP";

	String mDBPath;
	public List<FaceRegist> mRegister;
	AFR_FSDKEngine mFREngine;
	AFR_FSDKVersion mFRVersion;
	boolean mUpgrade;

	public class FaceRegist {
		public String mName;
		public List<AFR_FSDKFace> mFaceList;

		public FaceRegist(String name) {
			mName = name;
			mFaceList = new ArrayList<>();
		}
	}

	public FaceDB(String path) {
		mDBPath = path;
		mRegister = new ArrayList<>();
		mFRVersion = new AFR_FSDKVersion();
		mUpgrade = false;
		mFREngine = new AFR_FSDKEngine();
		AFR_FSDKError error = mFREngine.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key);
		if (error.getCode() != AFR_FSDKError.MOK) {
			Log.e(TAG, "AFR_FSDK_InitialEngine fail! error code :" + error.getCode());
		} else {
			mFREngine.AFR_FSDK_GetVersion(mFRVersion);
			Log.d(TAG, "AFR_FSDK_GetVersion=" + mFRVersion.toString());
		}
	}

	public void destroy() {
		if (mFREngine != null) {
			mFREngine.AFR_FSDK_UninitialEngine();
		}
	}

	private boolean saveInfo() {
		try {
			FileOutputStream fs = new FileOutputStream(mDBPath + "/face.txt");
			ExtOutputStream bos = new ExtOutputStream(fs);
			bos.writeString(mFRVersion.toString() + "," + mFRVersion.getFeatureLevel());
			bos.close();
			fs.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean loadInfo() {
		if (!mRegister.isEmpty()) {
			return false;
		}
		try {
			FileInputStream fs = new FileInputStream(mDBPath + "/face.txt");
			ExtInputStream bos = new ExtInputStream(fs);
			//load version
			String version_saved = bos.readString();
			if (version_saved.equals(mFRVersion.toString() + "," + mFRVersion.getFeatureLevel())) {
				mUpgrade = true;
			}
			//load all regist name.
			if (version_saved != null) {
				for (String name = bos.readString(); name != null; name = bos.readString()){
					if (new File(mDBPath + "/" + name + ".data").exists()) {
						mRegister.add(new FaceRegist(new String(name)));
					}
				}
			}
			bos.close();
			fs.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean loadFaces(){
		if (loadInfo()) {
			try {
				for (FaceRegist face : mRegister) {
					Log.d(TAG, "load name:" + face.mName + "'s face feature data.");
					FileInputStream fs = new FileInputStream(mDBPath + "/" + face.mName + ".data");
					ExtInputStream bos = new ExtInputStream(fs);
					AFR_FSDKFace afr = null;
					do {
						if (afr != null) {
							if (mUpgrade) {
								//upgrade data.
							}
							face.mFaceList.add(afr);
						}
						afr = new AFR_FSDKFace();
					} while (bos.readBytes(afr.getFeatureData()));
					bos.close();
					fs.close();
					Log.d(TAG, "load name: size = " + face.mFaceList.size());
				}
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public	void addFace(String name, AFR_FSDKFace face) {
		try {
			//check if already registered.
			boolean add = true;
			for (FaceRegist frface : mRegister) {
				if (frface.mName.equals(name)) {
					frface.mFaceList.add(face);
					add = false;
					break;
				}
			}
			if (add) { // not registered.
				FaceRegist frface = new FaceRegist(name);
				frface.mFaceList.add(face);
				mRegister.add(frface);
			}

			if (saveInfo()) {
				//update all names
				FileOutputStream fs = new FileOutputStream(mDBPath + "/face.txt", true);
				ExtOutputStream bos = new ExtOutputStream(fs);
				for (FaceRegist frface : mRegister) {
					bos.writeString(frface.mName);
				}
				bos.close();
				fs.close();

				//save new feature
				fs = new FileOutputStream(mDBPath + "/" + name + ".data", true);
				bos = new ExtOutputStream(fs);
				bos.writeBytes(face.getFeatureData());
				bos.close();
				fs.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean delete(String name) {
		try {
			//check if already registered.
			boolean find = false;
			for (FaceRegist frface : mRegister) {
				if (frface.mName.equals(name)) {
					File delfile = new File(mDBPath + "/" + name + ".data");
					if (delfile.exists()) {
						delfile.delete();
					}
					mRegister.remove(frface);
					find = true;
					break;
				}
			}

			if (find) {
				if (saveInfo()) {
					//update all names
					FileOutputStream fs = new FileOutputStream(mDBPath + "/face.txt", true);
					ExtOutputStream bos = new ExtOutputStream(fs);
					for (FaceRegist frface : mRegister) {
						bos.writeString(frface.mName);
					}
					bos.close();
					fs.close();
				}
			}
			return find;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean upgrade() {
		return false;
	}
}
