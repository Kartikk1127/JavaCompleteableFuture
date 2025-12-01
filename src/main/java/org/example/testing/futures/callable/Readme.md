## Healthcare Example (Realistic)

You process sensor readings:

- **Task1:** Heart rate — completes in 4 seconds
- **Task2:** Blood sugar — completes in 2 seconds
- **Task3:** Oxygen levels — completes in 0.3 seconds (super critical)

### Submission order
```java
submit(heartRate);
submit(bloodSugar);
submit(oxygenLevel);
```

### Handling
```
future1.get();
future2.get();
future3.get();
```

**Patient oxygen drops critically low**

**Task3 finishes immediately with an alert, but the code is still waiting for:**

```
future1.get(); // heart rate will take 4 seconds
```


**The system does not warn doctors until after 4 seconds + 2 seconds = ~6 seconds.**

**In an ICU, six seconds can be the difference between recovery and brain damage.**