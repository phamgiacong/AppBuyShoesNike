package com.hn_2452.shoes_nike.ultils

data class Resoucce<out T>(val status :Status, val data :T?, val message :String?){
    companion object{
        fun<T> success(data:T) : Resoucce<T> = Resoucce(status = Status.SUCCESS,data = data,message = null)
        fun<T> error(data:T , message: String): Resoucce<T> = Resoucce(status = Status.ERROR,data = data,message = message)
        fun<T> loading(data :T?) :Resoucce<T> = Resoucce(status = Status.LOADING,data=data , message = null)
    }
}
