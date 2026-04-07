bisa gak ini bisa jadi kayak gini di GJF Custom kita di /home/syukrikhairi/PROJECT/google-java-format/usecase.md

before

```java
  successresponse<creditresponsedto> credit(
    @org.springframework.web.bind.annotation.requestheader("x-idempotency-key")
     string idempotencykey,
    @valid @requestbody creditrequestdto requestdto);

```

after

```java

  successresponse<creditresponsedto> credit(
    @org.springframework.web.bind.annotation.requestheader("x-idempotency-key")
    string idempotencykey,
    @valid @requestbody creditrequestdto requestdto);



```

buat test nya juga
