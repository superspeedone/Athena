package com.superspeed.mybatis.pojo;

import java.util.Date;

public class User {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员ID
     */
	private Long userId;
    /**
     * 组织ID
     */
	
	private Long organizationId;
    /**
     * 管理员账号
     */
	
	private String loginName;
    /**
     * 管理员密码
     */
	
	private String loginPassword;
    /**
     * 加密密码的盐
     */
	private String salt;
    /**
     * 昵称
     */
	
	private String userName;
    /**
     * 真实姓名
     */
	
	private String realName;
    /**
     * 性别 0=保密/1=男/2=女
     */
	private Integer sex;
    /**
     * 年龄
     */
	private Integer age;
    /**
     * 用户头像
     */
	
	private String picImg;
    /**
     * 状态 0=冻结/1=正常
     */
	private Integer status;
    /**
     * 电子邮箱
     */
	private String email;
    /**
     * 手机号码
     */
	private String telephone;
    /**
     * 最后登录时间
     */
	
	private Date lastLoginTime;
    /**
     * 最后登录IP
     */
	
	private String lastLoginIp;
    /**
     * 创建时间
     */
	
	private Date createTime;
    /**
     * 创建者
     */
	
	private String createBy;
    /**
     * 更新时间
     */
	
	private Date updateTime;
    /**
     * 更新者
     */
	
	private String updateBy;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPicImg() {
		return picImg;
	}

	public void setPicImg(String picImg) {
		this.picImg = picImg;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", organizationId=" + organizationId +
				", loginName='" + loginName + '\'' +
				", loginPassword='" + loginPassword + '\'' +
				", salt='" + salt + '\'' +
				", userName='" + userName + '\'' +
				", realName='" + realName + '\'' +
				", sex=" + sex +
				", age=" + age +
				", picImg='" + picImg + '\'' +
				", status=" + status +
				", email='" + email + '\'' +
				", telephone='" + telephone + '\'' +
				", lastLoginTime=" + lastLoginTime +
				", lastLoginIp='" + lastLoginIp + '\'' +
				", createTime=" + createTime +
				", createBy='" + createBy + '\'' +
				", updateTime=" + updateTime +
				", updateBy='" + updateBy + '\'' +
				'}';
	}
}
