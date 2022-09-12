# yahoo-rate-service
Provding integrated API to get currencies rate from Yahoo Finance.

# Requirements
For building and running the application you need:
- JDK 1.8

# API provided
/getRate - get default HKD.JPY rate

e.g http://localhost:8080/getRate/
# ![image](https://user-images.githubusercontent.com/63330849/189732985-558e3715-2df1-40be-9e89-81ea67f9602a.png)



/getRate/baseCcy/termCcy - get requested CcyPair rate

e.g http://localhost:8080/getRate/JPY/HKD
# ![image](https://user-images.githubusercontent.com/63330849/189728038-617ae71b-17f4-4b9d-bf7d-1269623e87d3.png)

e.g http://localhost:8080/getRate/USD/JPY
# ![image](https://user-images.githubusercontent.com/63330849/189727846-e82a99d6-8eee-4d52-b7b1-d27d912d4b84.png)
