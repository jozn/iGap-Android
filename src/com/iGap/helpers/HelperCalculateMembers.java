// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.helpers;

import com.iGap.adapter.G;

public class HelperCalculateMembers {

	
	/**
	 * if number was great change it to kilobyte or megabyte 
	 * if language select persian   the return String number format gose to persian
	 * 
	 * @param numberOfMember 
	 * count of member 
	 * @return
	 * 
	 */
    public String calculateMembers(String numberOfMember) {
    	
    	if(numberOfMember==null)
    		return "";
    	
        int numberOfMembers = (Integer.parseInt(numberOfMember) + 1);

        String members = "";
        if (numberOfMembers < 1000) {
            members = numberOfMembers + "";
        } else {
            if (numberOfMembers < 999999) {
                float number = numberOfMembers / 1000.0f;
                members = String.format("%.1f", number);
                members = members + "K";
            } else {
                float number = numberOfMembers / 1000000.0f;
                members = String.format("%.1f", number);
                members = members + "M";
            }
        }
        
        
        if (G.SelectedLanguage.equals("fa")) {

        	members = HelperGetTime.stringNumberToPersianNumberFormat(members);
        }
        
        return members;
    }

}
