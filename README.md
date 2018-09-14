# react-native-upi

A react native module for handling payment through UPI and handling response from the UPI app.

## Getting started

`$ npm i react-native-upi --save`

`$ react-native link react-native-upi`

## Usage

```javascript
import UPIModule from "react-native-upi";

const transactionId = "transactionId";
const message = `message`;
const payTo = "9XXXXXXXXX@upi";
const payName = "Name";
const amount = XXX;

UPIModule.openUPIClient(
  `upi://pay?pa=${payTo}&pn=${payName}&tr=${transactionId}&tn=${message}&am={amount}`
)
  .then(res => {
    if (res !== null) {
      let result = {};
      let response = res.split("&").forEach(property => {
        let temp = property.split("=");
        result[temp[0]] = temp[1];
      });
      return result;
    }
  })
  .then(res => {
    console.log(res);
  });
```

### UPI Guidelines

To learn more about deep linking in UPI, checkout [NPCI Guidelines](https://www.npci.org.in/sites/all/themes/npcl/images/PDF/UPI_Linking_Specs_ver_1.5.1.pdf "NPCI Guidelines")
