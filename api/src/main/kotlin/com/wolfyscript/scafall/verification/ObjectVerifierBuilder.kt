package com.wolfyscript.scafall.verification

interface ObjectVerifierBuilder<T : Any> : VerifierBuilder<T, ObjectVerifierBuilder<T>, ObjectVerifier<T>>
