/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.api.telegram;

/*
import java.io.*;
import org.telegram.tl.*;
*/

public class TLSaveDeveloperInfo /*extends TLMethod*/ {

/*
  public static final int CLASS_ID = -757418007;
  private String name;
  private String email;
  private String phone_number;
  private int age;
  private String city;

  public TLSaveDeveloperInfo(String name, String email, String phoneNumber, int age, String city) {
    this.setName(name);
    this.setEmail(email);
    this.setPhoneNumber(phoneNumber);
    this.setAge(age);
    this.setCity(city);
  }

  public TLBool deserializeResponse(InputStream stream, TLContext context)
      throws IOException {
    TLObject res = StreamingUtils.readTLObject(stream, context);
    if (res == null) {
      throw new IOException("Unable to parse response");
    }
    if ((res instanceof TLBool)) {
      return (TLBool) res;
    }
    throw new IOException("Incorrect response type. Expected TLBool, got: " + res.getClass().getCanonicalName());
  }


  public void serializeBody(OutputStream stream)
      throws IOException {
    StreamingUtils.writeTLString(this.getName(), stream);
    StreamingUtils.writeTLString(this.getEmail(), stream);
    StreamingUtils.writeTLString(this.getPhoneNumber(), stream);
    StreamingUtils.writeInt(this.getAge(), stream);
    StreamingUtils.writeTLString(this.getCity(), stream);
  }

  public void deserializeBody(InputStream stream, TLContext context)
      throws IOException {
    this.setName(StreamingUtils.readTLString(stream));
    this.setEmail(StreamingUtils.readTLString(stream));
    this.setPhoneNumber(StreamingUtils.readTLString(stream));
    this.setAge(StreamingUtils.readInt(stream));
    this.setCity(StreamingUtils.readTLString(stream));
  }

  public String toString() {
    return "register.saveDeveloperInfo#d2dab7e9";
  }

  @Override
  public int getClassId() {
    // TODO Auto-generated method stub
    return -757418007;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phone_number;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phone_number = phoneNumber;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
*/
}