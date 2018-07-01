package com.nephi.getoffyourphone;

/**
 * Created by xerxes on 15.01.18.
 */

public class apps {
    //private variables
    int _id;
    String _PKG;
    int s_id;


    //Empty constructor
    public apps() {
    }

    //constructor
    public apps(int _id, String _PKG, int s_id) {
        this._id = _id;
        this._PKG = _PKG;
        this.s_id = s_id;
    }

    public apps(String _PKG, int s_id) {
        this._PKG = _PKG;
        this.s_id = s_id;
    }

    public apps(int _id, String _PKG) {
        this._id = _id;
        this._PKG = _PKG;
    }

    public apps(String _PKG) {
        this._id = _id;
        this._PKG = _PKG;
    }

    public apps(int s_id) {
        this.s_id = s_id;
    }

    public String get_PKG() {
        return _PKG;
    }

    // setting name
    public void set_PKG(String _PKG) {
        this._PKG = _PKG;
    }

    public int get_id() {
        return _id;
    }

    // setting id
    public void set_id(int _id) {
        this._id = _id;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

}
