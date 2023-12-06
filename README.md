# Microsoft Authentication Library (MSAL) for Android

<p>

[![build](https://img.shields.io/github/actions/workflow/status/axzae/microsoft-authentication-library-for-android/publish-maven.yaml)][actions]
[![github tag](https://img.shields.io/github/v/tag/axzae/microsoft-authentication-library-for-android?label=github)][releases]
[![maven central](https://img.shields.io/maven-central/v/com.axzae/msal)][mavencentral]

</p>

The Microsoft Authentication Library (MSAL) for Android enables developers to acquire security tokens from the Microsoft identity platform to authenticate users and access secured web APIs for their Android based applications.
The MSAL library for Android gives your app the ability to use the [Microsoft Cloud](https://cloud.microsoft.com) by supporting [Microsoft Azure Active Directory](https://azure.microsoft.com/services/active-directory/) and [Microsoft Personal Accounts](https://account.microsoft.com)  using industry standard OAuth2 and OpenID Connect. The library also supports [Azure AD B2C](https://azure.microsoft.com/services/active-directory-b2c/).

This is the enhancement version of the [Microsoft's version](https://github.com/AzureAD/microsoft-authentication-library-for-android). By using this version:
1. Expose **RefreshToken** in `AuthenticationResult`

## Setup

#### Gradle

```kotlin
// build.gradle.kts (app module)

dependencies {
    implementation("com.axzae:msal:4.10.2")
        exclude(group = "com.microsoft.device.display")
}
```

## Usage

```kotlin
object : AuthenticationCallback {
    override fun onSuccess(authenticationResult: IAuthenticationResult?) {
        authenticationResult ?: return
        
        // Access to refresh token
        viewModel.setToken(authenticationResult.refreshToken)
    }
}
```

## See Also

- [Full Usage Guide](https://github.com/AzureAD/microsoft-authentication-library-for-android)

[mavencentral]: https://central.sonatype.com/artifact/com.axzae/msal
[actions]: https://github.com/axzae/microsoft-authentication-library-for-android/actions
[releases]: https://github.com/axzae/microsoft-authentication-library-for-android/releases
