package com.tencent.qbar;

public class WxQbarNative {
    public static class QBarReportMsg {
        public String  binaryMethod;
        public String  charsetMode;
        public float   decodeScale;
        public int     detectTime;
        public String  ecLevel;
        public boolean inBlackList;
        public boolean inWhiteList;
        public int     pyramidLv;
        public int     qrcodeVersion;
        public String  scaleList;
        public int     srTime;
    }

    protected static native int FocusInit(int i, int i2, boolean z, int i3, int i4);

    protected static native boolean FocusPro(byte[] bArr, boolean z, boolean[] zArr);

    protected static native int FocusRelease();

    protected static native int QIPUtilYUVCrop(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, int i5, int i6);

    protected static native int focusedEngineForBankcardInit(int i, int i2, int i3, boolean z);

    protected static native int focusedEngineGetVersion();

    protected static native int focusedEngineProcess(byte[] bArr);

    protected static native int focusedEngineRelease();

    protected static native int AddBlackInternal(int i, int i2);

    protected static native int AddBlackList(String str, int i);

    protected static native int AddWhiteList(String str, int i);

    /**
     * 获取扫描结果
     *
     * @param qBarResultJNIArr 返回的数组
     * @param qBarPointArr     返回的数组
     * @param qBarReportMsgArr 返回的数组
     * @param i                qBarId
     * @return 被扫描出的数量
     * @see QbarNative#ScanImage(byte[], int, int, int)
     */
    protected static native int GetDetailResults(QbarNative.QBarResultJNI[] qBarResultJNIArr, QbarNative.QBarPoint[] qBarPointArr, QBarReportMsg[] qBarReportMsgArr, int i);

    protected static native int GetDetectInfoByFrames(QbarNative.QBarCodeDetectInfo qBarCodeDetectInfo, QbarNative.QBarPoint qBarPoint, int i);

    protected static native int GetOneResultReport(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int[] iArr, int[] iArr2, int i);

    protected static native int GetZoomInfo(QbarNative.QBarZoomInfo qBarZoomInfo, int i);

    protected static native int SetCenterCoordinate(int i, int i2, int i3, int i4, int i5);
}
