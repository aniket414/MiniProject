package com.shivomthakkar.miniproject;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Shivom on 10/13/17.
 */

@JsonObject
public class AttendanceData {

    @JsonField
    public String nameOfStudent;

    @JsonField
    public int rollNo;

    @JsonField
    public String studentYear;

    @JsonField
    public String studentDivision;

    @JsonField
    public String IMEI;

}
