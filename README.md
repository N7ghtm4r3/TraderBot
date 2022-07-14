# TraderBot
**v1.0.1**

This is a Java Based library useful to make trading with the biggest cryptocurrencies exchanges platforms

## Implementation

Add the JitPack repository to your build file

### Gradle

- Add it in your root build.gradle at the end of repositories

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
- Add the dependency

```gradle
dependencies {
	implementation 'com.github.N7ghtm4r3:TraderBot:1.0.1'
}
```

### Maven

- Add it in your root build.gradle at the end of repositories

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
- Add the dependency

```xml
<dependency>
    <groupId>com.github.N7ghtm4r3</groupId>
    <artifactId>TraderBot</artifactId>
    <version>1.0.1</version>
</dependency>
```

## Platforms available

- **Binance**
- **Coinbase**

The other platforms will be gradually released

## Usage/Examples

**Note:** your exchange platform api keys **will not be** stored anywhere in our system, so is your responsability manage them.

### Native use
Traders work without Tecknobit's Android interface

#### Interface

You have to create your code flow to use trader

```java
//Platform means Binance or Coinbase actually
PlatformTraderBot trader = new PlatformTraderBot(/*your keys and constructor params*/);
//Operation example
trader.buyMarket("BTCBUSD", 1);
```
#### Autonomous

Trader automatically make trading flow, like checking, buying and updating your wallet

```java
//Platform means Binance or Coinbase actually
PlatformAutoTraderBot trader = new PlatformAutoTraderBot(/*your keys and constructor params*/);
//Start autonomous trader flow 
trader.start();
```

### Android use
Traders work with <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">Tecknobit's Android interface </a><br>
All data will be stored and restored automatically, **will be not saved** your exchange platform api keys. <br>
#### Auth operations
- #### Registration
    To register a new trader, both traders type, you have to use Credentials object to init your Tecknobit's account credentials.
    In first attemp will throws a SaveData exception so you can save credentials generated.

```java
//In registration you have to init Credentials object with your email and password for Tecknobit's account.

Credentials credentials = new Credentials(
    "yourEmail@mail.com",
    "yourPassword"
);

//Program will automatically stop and will give to you auth credentials in this format:

/*{
        "secret_key": "valueOfSecretKey",
        "password": "yourPasswordInserted",
        "mail": "yourEmailInserted@mail.com",
        "iv_spec": "valueOfIvSpec",
        "auth_token": "valueOfAuthToken",
        "token": "valueOfToken"
}*/

//Note you must save these credentials where you retain safe because them are needed in auth operations.
```

- #### Login
    In login operation, both traders type, you have to use Credentials object to init your Tecknobit's account credentials.

```java
//In login operation you have to init Credentials object with your credentials given from Registration

Credentials credentials = new Credentials(
    "valueOfAuthToken",
    "yourEmail@mail.com",
    "yourPassword",
    "valueOfIvSpec",
    "valueOfSecretKey"
);

//Then you have to insert credentials object in Android's trader constructor and trader will start with your Tecknobit's account.

AndroidPlatformTraderBot trader = new AndroidPlatformTraderBot(/*your keys and constructor params*/, credentials);

```
#### Interface

You have to create your code flow to use trader

```java
//Platform means Binance or Coinbase actually
AndroidPlatformTraderBot trader = new AndroidPlatformTraderBot(/*your keys and constructor params*/, credentials);
//Operation example
trader.buyMarket("BTCBUSD", 1);
```
#### Autonomous

Trader automatically make trading flow, like checking, buying and updating your wallet

```java
//Platform means Binance or Coinbase actually
AndroidPlatformAutoTrader trader = new AndroidPlatformAutoTrader(/*your keys and constructor params*/, credentials);
//Start autonomous trader flow 
trader.start();
```

## 🛠 Skills
- Java

## Authors

- [@N7ghtm4r3](https://www.github.com/N7ghtm4r3)

## Support

For support, email infotecknobitcompany@gmail.com.

## Badges

[![](https://img.shields.io/badge/Google_Play-414141?style=for-the-badge&logo=google-play&logoColor=white)](https://play.google.com/store/apps/developer?id=Tecknobit)
[![](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://github.com/N7ghtm4r3/BinanceManager/blob/main/README.md)

[![Twitter](https://img.shields.io/twitter/url/https/twitter.com/cloudposse.svg?style=social&label=Tecknobit)](https://twitter.com/tecknobit)
[![](https://jitpack.io/v/N7ghtm4r3/TraderBot.svg)](https://jitpack.io/#N7ghtm4r3/TraderBot)
## Donations 

If you want support project and developer: **0x5f63cc6d13b16dcf39cd8083f21d50151efea60e**

![](https://img.shields.io/badge/Bitcoin-000000?style=for-the-badge&logo=bitcoin&logoColor=white) 
![](https://img.shields.io/badge/Ethereum-3C3C3D?style=for-the-badge&logo=Ethereum&logoColor=white)

If you want support project and developer with <a href="https://www.paypal.com/donate/?hosted_button_id=5QMN5UQH7LDT4">PayPal</a>

## Privacy policy

This section reguards Privacy and policy for Android's application published on Google Play Store. <br>
TraderBot application **will not share any personal data of the user with third part applications.** <br> 
All data about Tecknobit's account, so not **api keys of exchanges platforms**, will be store in <a href="https://firebase.google.com/">Firebase's services</a>
and application performance and session will be tracked by <a href="https://analytics.google.com/">Google Analytics's services</a>. <br>
So all data saved reguard TraderBot's service offered by Tecknobit and **any other data** will be saved. <br>
Regards and good use!

Copyright © 2022 Tecknobit
