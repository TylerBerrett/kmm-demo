package com.tylerb.kmm

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}