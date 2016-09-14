##iGap Client Connection for Android
This repo contains the official source code of iGapSocket Connection On Android system. 
<br />
<br />
##Language
These codes are written in Java language and are used for Android systems.
<br />
<br />
###The way of connection to server
Because of the possibility of receiving different responses from the server with/without request, in this report, it is used protobuffer for connecting to server.
<br />
<br />
### [Request](https://github.com/RooyeKhat-Media/iGap-CJava/tree/master/app/src/main/java/com/iGap_CJava/request)
[RequestQueue](https://github.com/RooyeKhat-Media/iGap-CJava/blob/master/app/src/main/java/com/iGap_CJava/request/RequestQueue.java) class is used for sending request to the server. All requests are sent using [sendRequest] method.
[sendRequest](https://github.com/RooyeKhat-Media/iGap-CJava/blob/master/app/src/main/java/com/iGap_CJava/request/RequestQueue.java#L24) method is a variadic function that accepts at least unlimited number of input as  [RequestWraper](https://github.com/RooyeKhat-Media/iGap-CJava/blob/master/app/src/main/java/com/iGap_CJava/request/RequestWrapper.java).
Sending request
<br />
1-	 Create a builder of Intended protobuffer
<br />
2-	Fill the methods with setters that require decimalisation for sending.
<br />
3-	Create a new RequestWrapper and then set the number that is corresponded with this proto in the [LookupTable](https://github.com/RooyeKhat-Media/iGap-CJava/blob/master/app/src/main/java/com/iGap_CJava/helper/HelperFillLookUpClass.java) as actionId. For example, [ConnectionSymmetricKey](https://github.com/RooyeKhat-Media/iGap-CJava/blob/master/app/src/main/java/com/iGap_CJava/helper/HelperFillLookUpClass.java#L15) is corresponded with  actionId2. So send it using sendRequest method  after filling RequestWrapper by proto and Id.
<br />
####Example for sending Request
```java
  ProtoConnectionSecuring.ConnectionSymmetricKey.Builder connectionSymmetricKey = ProtoConnectionSecuring.ConnectionSymmetricKey.newBuilder();
  connectionSymmetricKey.setSymmetricKey(ByteString.copyFrom(encryption));
  RequestWrapper requestWrapper = new RequestWrapper(2, connectionSymmetricKey);
  try {
      RequestQueue.sendRequest(requestWrapper);
  } catch (IllegalAccessException e) {
      e.printStackTrace();
  }
  ```
### [Response](https://github.com/RooyeKhat-Media/iGap-CJava/tree/master/app/src/main/java/com/iGap_CJava/response)
After receiving response from the server, use [HandlerResponse](https://github.com/RooyeKhat-Media/iGap-CJava/blob/master/app/src/main/java/com/iGap_CJava/response/HandleResponse.java) for reading the message. 
At this class, we know automatically that it is necessary to decrypt the codes before reading the messages. Then we send byteArray to the [HelperUnpackMessage](https://github.com/RooyeKhat-Media/iGap-CJava/blob/master/app/src/main/java/com/iGap_CJava/helper/HelperUnpackMessage.java).  
After fetching the actionId in this class, we find intended name by it at the Lookup Table.
<br />
<br />
**Notice**:  during naming the classes that are exist as Response in  LookupTable, it is necessary that the class name be the same with the name in the LookupTable.
<br />
<br />
We create dynamically a sample of response class by [instanceResponseClass](https://github.com/RooyeKhat-Media/iGap-CJava/blob/master/app/src/main/java/com/iGap_CJava/helper/HelperUnpackMessage.java#L255) method for recievinf response. Then using received ByteArray from the server that is now divided to ActionId and ProtoObject, invite the existing constructor and amount it. 
As a result of this, after receiving any response, the message is sending to HelperUnpackMessage automatically and there, amounting and inviting given received class. So we can see all receiving responses from the server in Response classes.
#### example for get response

```java
  ProtoConnectionSecuring.ConnectionSymmetricKeyResponse.Builder builder = (ProtoConnectionSecuring.ConnectionSymmetricKeyResponse.Builder) message;
  ProtoConnectionSecuring.ConnectionSymmetricKeyResponse.Status status = builder.getStatus();
  int statusNumber = status.getNumber();

  if (statusNumber == Config.REJECT) {

      WebSocketClient.getInstance().disconnect();

  } else if (statusNumber == Config.ACCEPT) {
      G.isSecure = true;

      G.ivSize = builder.getSymmetricIvSize();
      String sm = builder.getSymmetricMethod();
      G.symmetricMethod = sm.split("-")[2];
  }
```
### The main variables used in the program:
The Global variables are set in [G](https://github.com/RooyeKhat-Media/iGap-CJava/blob/master/app/src/main/java/com/iGap_CJava/G.java) class. Sent messages are encrypted by  symmetricKey.
<br />
ivSize is used for iv and sent message Separation.
<br />
isSecure stands for security detection.
<br />
lookupMap ActionId stands for maintaining list and response classes name.


### [Config](https://github.com/RooyeKhat-Media/iGap-CJava/blob/master/app/src/main/java/com/iGap_CJava/Config.java)
in this page,there are general setting for example the amount of delay for TimeOut as well as server adress and other settings.
