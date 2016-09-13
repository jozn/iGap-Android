  // Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.helpers;

public class HelperString {

	/**
	 * return filename from a string file path
	 * @param filePath
	 * @return
	 */
    public static String getFileName(String filePath) {
        String[] strings = filePath.split("/");
        String filename = strings[strings.length - 1];
        return filename;
    }
}
