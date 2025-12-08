package com.example.serviconnecta.feature.auth.ui.navigation

sealed class AuthDestination(val route: String) {
    data object Login : AuthDestination("auth/login")
    data object RegisterAccountType : AuthDestination("auth/register/account-type")
    data object RegisterDataInfo : AuthDestination("auth/register/data-info")
    data object RegisterPhone : AuthDestination("auth/register/phone")
    data object RegisterVerifyCode : AuthDestination("auth/register/verify-code")
    data object RegisterIdentityIntro : AuthDestination("auth/register/identity-intro")
    data object IdentityStart : AuthDestination("auth/identity/start")
    data object IdentitySelectDocument : AuthDestination("auth/identity/select-document")
    data object IdentityInstructions : AuthDestination("auth/identity/instructions")
    data object IdentityCapture : AuthDestination("auth/identity/capture")
    data object IdentitySuccess : AuthDestination("auth/identity/success")
    data object IdentityOverview : AuthDestination("auth/identity/overview")
}