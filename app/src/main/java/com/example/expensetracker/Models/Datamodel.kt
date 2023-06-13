package com.example.expensetracker.Models

class Datamodel {
    var date:String?=null
    var open:String?=null
    var close:String?=null
    var high:String?=null
    var low:String?=null
    var volume:String?=null
    var adjClose:String?=null

    constructor()
    constructor(
        date: String?,
        open: String?,
        close: String?,
        high: String?,
        low: String?,
        volume: String?,
        adjClose: String?
    ) {
        this.date = date
        this.open = open
        this.close = close
        this.high = high
        this.low = low
        this.volume = volume
        this.adjClose = adjClose
    }
}