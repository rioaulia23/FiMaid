package com.example.FiMaid.Model

class UserModel {
    private var name: String? = null
    private var email: String? = null
    private var password: String? = null
    private var phone: String? = null
    private var alamat: String? = null
    private var img: String? = null

    constructor()
    constructor(name: String, username: String, email: String, password: String, phone: String, img: String) {
        this.name = name
        this.email = email
        this.password = password
        this.alamat = alamat
        this.phone = phone
        this.img = img
    }

    fun getName(): String {
        return name!!
    }

    fun getAlamat(): String {
        return alamat!!
    }

    fun getEmail(): String {
        return email!!
    }

    fun getPassword(): String {
        return password!!
    }

    fun getPhone(): String {
        return phone!!
    }

    fun getImg(): String {
        return img!!
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setAlamat(alamat: String) {
        this.alamat = alamat
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setPhone(phone: String) {
        this.phone = phone
    }

    fun setImg(img: String) {
        this.img = img
    }
}