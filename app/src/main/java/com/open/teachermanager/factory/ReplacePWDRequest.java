package com.open.teachermanager.factory;

/**
 * Created by Administrator on 2016/5/27.
 */
public class ReplacePWDRequest {
            private String teacherId;//老师id
            private String oldPwd;//原始密码
            private String newPwd;//新密码

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
