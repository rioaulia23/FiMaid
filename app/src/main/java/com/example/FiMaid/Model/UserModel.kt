package com.example.FiMaid.Model

class UserModel {
    private var name: String? = null
    private var email: String? = null
    private var password: String? = null
    private var phone: String? = null
    private var alamat: String? = null
    private var age: String? = null
    private var img: String? = null
    private var role: String? = null

    constructor()
    constructor(
        name: String,
        age: String,
        email: String,
        password: String,
        phone: String,
        img: String,
        role: String
    ) {
        this.name = name
        this.email = email
        this.password = password
        this.alamat = alamat
        this.phone = phone
        this.age = age
        this.img = img
        this.role = role
    }

    fun getName(): String {
        return name!!
    }

    fun getRole(): String {
        return role!!
    }

    fun getAge(): String {
        return age!!
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

    fun setRole(role: String) {
        this.role = role
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

    fun setAge(age: String) {
        this.age = age
    }

    fun setImg(img: String) {
        this.img = img
    }
}