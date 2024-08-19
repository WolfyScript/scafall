package com.wolfyscript.scafall.i18n

import java.util.Locale

interface Language {

    val locale: Locale

    val keys: Map<String, String>

}