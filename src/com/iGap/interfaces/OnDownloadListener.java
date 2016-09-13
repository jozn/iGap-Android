  // Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.interfaces;

public interface OnDownloadListener {

    public void onProgressDownload(int percent, int downloadedSize, int fileSize, boolean completeDownload);
}