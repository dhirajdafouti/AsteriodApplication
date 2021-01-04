package com.udacity.asteroidradar.language

class Language(name: String, code: String) {

    var name: String? = name
    var code: String? = code

    companion object {

        fun getLanguageList(): ArrayList<Language> {
            val list = ArrayList<Language>()
            list.add(Language("Select Language", "lang"))
            list.add(Language("German", "de"))
            list.add(Language("French", "fr-ca"))
            list.add(Language("Italian", "it"))
            list.add(Language("Turkish", "tr"))
            return list
        }
    }

}