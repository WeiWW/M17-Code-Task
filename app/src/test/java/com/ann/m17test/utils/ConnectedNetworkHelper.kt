package com.ann.m17test.utils

class ConnectedNetworkHelper:NetworkHelper {
    override fun isNetworkConnected(): Boolean {
        return true
    }
}

class DisconnectedNetworkHelper:NetworkHelper{
    override fun isNetworkConnected(): Boolean {
        return false
    }

}